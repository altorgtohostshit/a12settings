package com.android.settings.notification;

import android.app.NotificationManager;
import android.app.admin.DevicePolicyManager;
import android.companion.ICompanionDeviceManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.IconDrawableFactory;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.applications.specialaccess.notificationaccess.NotificationAccessDetails;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.utils.ManagedServiceSettings;
import com.android.settings.widget.EmptyTextSettings;
import com.android.settingslib.applications.ServiceListing;
import com.android.settingslib.widget.AppPreference;
import java.util.List;

public class NotificationAccessSettings extends EmptyTextSettings {
    private static final ManagedServiceSettings.Config CONFIG = new ManagedServiceSettings.Config.Builder().setTag("NotifAccessSettings").setSetting("enabled_notification_listeners").setIntentAction("android.service.notification.NotificationListenerService").setPermission("android.permission.BIND_NOTIFICATION_LISTENER_SERVICE").setNoun("notification listener").setWarningDialogTitle(R.string.notification_listener_security_warning_title).setWarningDialogSummary(R.string.notification_listener_security_warning_summary).setEmptyText(R.string.no_notification_listeners).build();
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.notification_access_settings);
    private NotificationBackend mBackend = new NotificationBackend();
    protected Context mContext;
    private DevicePolicyManager mDpm;
    private IconDrawableFactory mIconDrawableFactory;
    private NotificationManager mNm;
    private PackageManager mPm;
    private ServiceListing mServiceListing;

    public int getMetricsCategory() {
        return 179;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.notification_access_settings;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        FragmentActivity activity = getActivity();
        this.mContext = activity;
        this.mPm = activity.getPackageManager();
        this.mDpm = (DevicePolicyManager) this.mContext.getSystemService("device_policy");
        this.mIconDrawableFactory = IconDrawableFactory.newInstance(this.mContext);
        ServiceListing.Builder builder = new ServiceListing.Builder(this.mContext);
        ManagedServiceSettings.Config config = CONFIG;
        ServiceListing build = builder.setPermission(config.permission).setIntentAction(config.intentAction).setNoun(config.noun).setSetting(config.setting).setTag(config.tag).build();
        this.mServiceListing = build;
        build.addCallback(new NotificationAccessSettings$$ExternalSyntheticLambda1(this));
        if (UserManager.get(this.mContext).isManagedProfile()) {
            Toast.makeText(this.mContext, R.string.notification_settings_work_profile, 0).show();
            finish();
        }
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        setEmptyText(CONFIG.emptyText);
    }

    public void onResume() {
        super.onResume();
        this.mServiceListing.reload();
        this.mServiceListing.setListening(true);
    }

    public void onPause() {
        super.onPause();
        this.mServiceListing.setListening(false);
    }

    /* access modifiers changed from: private */
    public void updateList(List<ServiceInfo> list) {
        int managedProfileId = Utils.getManagedProfileId((UserManager) this.mContext.getSystemService("user"), UserHandle.myUserId());
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        PreferenceCategory preferenceCategory = (PreferenceCategory) preferenceScreen.findPreference("allowed");
        preferenceCategory.removeAll();
        PreferenceCategory preferenceCategory2 = (PreferenceCategory) preferenceScreen.findPreference("not_allowed");
        preferenceCategory2.removeAll();
        list.sort(new PackageItemInfo.DisplayNameComparator(this.mPm));
        for (ServiceInfo next : list) {
            ComponentName componentName = new ComponentName(next.packageName, next.name);
            CharSequence charSequence = null;
            try {
                charSequence = this.mPm.getApplicationInfoAsUser(next.packageName, 0, UserHandle.myUserId()).loadLabel(this.mPm);
            } catch (PackageManager.NameNotFoundException e) {
                Log.e("NotifAccessSettings", "can't find package name", e);
            }
            AppPreference appPreference = new AppPreference(getPrefContext());
            appPreference.setTitle(charSequence);
            IconDrawableFactory iconDrawableFactory = this.mIconDrawableFactory;
            ApplicationInfo applicationInfo = next.applicationInfo;
            appPreference.setIcon(iconDrawableFactory.getBadgedIcon(next, applicationInfo, UserHandle.getUserId(applicationInfo.uid)));
            appPreference.setKey(componentName.flattenToString());
            appPreference.setSummary(NotificationBackend.getDeviceList(ICompanionDeviceManager.Stub.asInterface(ServiceManager.getService("companiondevice")), com.android.settings.bluetooth.Utils.getLocalBtManager(this.mContext), next.packageName, UserHandle.myUserId()));
            if (managedProfileId != -10000 && !this.mDpm.isNotificationListenerServicePermitted(next.packageName, managedProfileId)) {
                appPreference.setSummary((int) R.string.work_profile_notification_access_blocked_summary);
            }
            appPreference.setOnPreferenceClickListener(new NotificationAccessSettings$$ExternalSyntheticLambda0(this, componentName, next));
            appPreference.setKey(componentName.flattenToString());
            if (this.mNm.isNotificationListenerAccessGranted(componentName)) {
                preferenceCategory.addPreference(appPreference);
            } else {
                preferenceCategory2.addPreference(appPreference);
            }
        }
        highlightPreferenceIfNeeded();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$updateList$0(ComponentName componentName, ServiceInfo serviceInfo, Preference preference) {
        Bundle bundle = new Bundle();
        bundle.putString("package", componentName.getPackageName());
        bundle.putInt("uid", serviceInfo.applicationInfo.uid);
        Bundle bundle2 = new Bundle();
        bundle2.putString("android.provider.extra.NOTIFICATION_LISTENER_COMPONENT_NAME", componentName.flattenToString());
        new SubSettingLauncher(getContext()).setDestination(NotificationAccessDetails.class.getName()).setSourceMetricsCategory(getMetricsCategory()).setTitleRes(R.string.manage_notification_access_title).setArguments(bundle).setExtras(bundle2).setUserHandle(UserHandle.getUserHandleForUid(serviceInfo.applicationInfo.uid)).launch();
        return true;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.mNm = (NotificationManager) context.getSystemService(NotificationManager.class);
    }
}
