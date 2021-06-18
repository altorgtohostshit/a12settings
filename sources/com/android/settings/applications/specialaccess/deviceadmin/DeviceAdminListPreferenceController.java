package com.android.settings.applications.specialaccess.deviceadmin;

import android.app.AppGlobals;
import android.app.admin.DeviceAdminInfo;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.IPackageManager;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import com.android.settingslib.widget.AppSwitchPreference;
import com.android.settingslib.widget.FooterPreference;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.xmlpull.v1.XmlPullParserException;

public class DeviceAdminListPreferenceController extends BasePreferenceController implements LifecycleObserver, OnStart, OnStop {
    private static final IntentFilter FILTER;
    private static final String KEY_DEVICE_ADMIN_FOOTER = "device_admin_footer";
    private static final String TAG = "DeviceAdminListPrefCtrl";
    private final ArrayList<DeviceAdminListItem> mAdmins = new ArrayList<>();
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals("android.app.action.DEVICE_POLICY_MANAGER_STATE_CHANGED", intent.getAction())) {
                DeviceAdminListPreferenceController.this.updateList();
            }
        }
    };
    private final DevicePolicyManager mDPM;
    private FooterPreference mFooterPreference;
    private final IPackageManager mIPackageManager;
    private final MetricsFeatureProvider mMetricsFeatureProvider;
    private final PackageManager mPackageManager;
    private PreferenceGroup mPreferenceGroup;
    private final SparseArray<ComponentName> mProfileOwnerComponents = new SparseArray<>();
    private final UserManager mUm;

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$bindPreference$1(Preference preference, Object obj) {
        return false;
    }

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public int getAvailabilityStatus() {
        return 0;
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    static {
        IntentFilter intentFilter = new IntentFilter();
        FILTER = intentFilter;
        intentFilter.addAction("android.app.action.DEVICE_POLICY_MANAGER_STATE_CHANGED");
    }

    public DeviceAdminListPreferenceController(Context context, String str) {
        super(context, str);
        this.mDPM = (DevicePolicyManager) context.getSystemService("device_policy");
        this.mUm = (UserManager) context.getSystemService("user");
        this.mPackageManager = this.mContext.getPackageManager();
        this.mIPackageManager = AppGlobals.getPackageManager();
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(context).getMetricsFeatureProvider();
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        PreferenceGroup preferenceGroup = (PreferenceGroup) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreferenceGroup = preferenceGroup;
        this.mFooterPreference = (FooterPreference) preferenceGroup.findPreference(KEY_DEVICE_ADMIN_FOOTER);
    }

    public void onStart() {
        this.mContext.registerReceiverAsUser(this.mBroadcastReceiver, UserHandle.ALL, FILTER, (String) null, (Handler) null);
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        this.mProfileOwnerComponents.clear();
        List<UserHandle> userProfiles = this.mUm.getUserProfiles();
        int size = userProfiles.size();
        for (int i = 0; i < size; i++) {
            int identifier = userProfiles.get(i).getIdentifier();
            this.mProfileOwnerComponents.put(identifier, this.mDPM.getProfileOwnerAsUser(identifier));
        }
        updateList();
    }

    public void onStop() {
        this.mContext.unregisterReceiver(this.mBroadcastReceiver);
    }

    /* access modifiers changed from: package-private */
    public void updateList() {
        refreshData();
        refreshUI();
    }

    private void refreshData() {
        this.mAdmins.clear();
        for (UserHandle identifier : this.mUm.getUserProfiles()) {
            updateAvailableAdminsForProfile(identifier.getIdentifier());
        }
        Collections.sort(this.mAdmins);
    }

    private void refreshUI() {
        if (this.mPreferenceGroup != null) {
            FooterPreference footerPreference = this.mFooterPreference;
            if (footerPreference != null) {
                footerPreference.setVisible(this.mAdmins.isEmpty());
            }
            ArrayMap arrayMap = new ArrayMap();
            Context context = this.mPreferenceGroup.getContext();
            int preferenceCount = this.mPreferenceGroup.getPreferenceCount();
            for (int i = 0; i < preferenceCount; i++) {
                Preference preference = this.mPreferenceGroup.getPreference(i);
                if (preference instanceof AppSwitchPreference) {
                    AppSwitchPreference appSwitchPreference = (AppSwitchPreference) preference;
                    arrayMap.put(appSwitchPreference.getKey(), appSwitchPreference);
                }
            }
            Iterator<DeviceAdminListItem> it = this.mAdmins.iterator();
            while (it.hasNext()) {
                DeviceAdminListItem next = it.next();
                AppSwitchPreference appSwitchPreference2 = (AppSwitchPreference) arrayMap.remove(next.getKey());
                if (appSwitchPreference2 == null) {
                    appSwitchPreference2 = new AppSwitchPreference(context);
                    this.mPreferenceGroup.addPreference(appSwitchPreference2);
                }
                bindPreference(next, appSwitchPreference2);
            }
            for (AppSwitchPreference removePreference : arrayMap.values()) {
                this.mPreferenceGroup.removePreference(removePreference);
            }
        }
    }

    private void bindPreference(DeviceAdminListItem deviceAdminListItem, AppSwitchPreference appSwitchPreference) {
        appSwitchPreference.setKey(deviceAdminListItem.getKey());
        appSwitchPreference.setTitle(deviceAdminListItem.getName());
        appSwitchPreference.setIcon(deviceAdminListItem.getIcon());
        appSwitchPreference.setChecked(deviceAdminListItem.isActive());
        appSwitchPreference.setSummary(deviceAdminListItem.getDescription());
        appSwitchPreference.setEnabled(deviceAdminListItem.isEnabled());
        appSwitchPreference.setOnPreferenceClickListener(new DeviceAdminListPreferenceController$$ExternalSyntheticLambda1(this, deviceAdminListItem));
        appSwitchPreference.setOnPreferenceChangeListener(DeviceAdminListPreferenceController$$ExternalSyntheticLambda0.INSTANCE);
        appSwitchPreference.setSingleLineTitle(true);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$bindPreference$0(DeviceAdminListItem deviceAdminListItem, Preference preference) {
        this.mMetricsFeatureProvider.logClickedPreference(preference, getMetricsCategory());
        UserHandle user = deviceAdminListItem.getUser();
        Context context = this.mContext;
        context.startActivityAsUser(deviceAdminListItem.getLaunchIntent(context), user);
        return true;
    }

    private void updateAvailableAdminsForProfile(int i) {
        List activeAdminsAsUser = this.mDPM.getActiveAdminsAsUser(i);
        addActiveAdminsForProfile(activeAdminsAsUser, i);
        addDeviceAdminBroadcastReceiversForProfile(activeAdminsAsUser, i);
    }

    private void addActiveAdminsForProfile(List<ComponentName> list, int i) {
        if (list != null) {
            for (ComponentName next : list) {
                try {
                    DeviceAdminInfo createDeviceAdminInfo = createDeviceAdminInfo(this.mContext, this.mIPackageManager.getReceiverInfo(next, 819328, i));
                    if (createDeviceAdminInfo != null) {
                        this.mAdmins.add(new DeviceAdminListItem(this.mContext, createDeviceAdminInfo));
                    }
                } catch (RemoteException unused) {
                    Log.w(TAG, "Unable to load component: " + next);
                }
            }
        }
    }

    private void addDeviceAdminBroadcastReceiversForProfile(Collection<ComponentName> collection, int i) {
        DeviceAdminInfo createDeviceAdminInfo;
        List<ResolveInfo> queryBroadcastReceiversAsUser = this.mPackageManager.queryBroadcastReceiversAsUser(new Intent("android.app.action.DEVICE_ADMIN_ENABLED"), 32896, i);
        if (queryBroadcastReceiversAsUser != null) {
            for (ResolveInfo resolveInfo : queryBroadcastReceiversAsUser) {
                ActivityInfo activityInfo = resolveInfo.activityInfo;
                ComponentName componentName = new ComponentName(activityInfo.packageName, activityInfo.name);
                if ((collection == null || !collection.contains(componentName)) && (createDeviceAdminInfo = createDeviceAdminInfo(this.mContext, resolveInfo.activityInfo)) != null && createDeviceAdminInfo.isVisible() && createDeviceAdminInfo.getActivityInfo().applicationInfo.isInternal()) {
                    this.mAdmins.add(new DeviceAdminListItem(this.mContext, createDeviceAdminInfo));
                }
            }
        }
    }

    private static DeviceAdminInfo createDeviceAdminInfo(Context context, ActivityInfo activityInfo) {
        try {
            return new DeviceAdminInfo(context, activityInfo);
        } catch (IOException | XmlPullParserException e) {
            Log.w(TAG, "Skipping " + activityInfo, e);
            return null;
        }
    }
}
