package com.android.settings.notification.zen;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.UserHandle;
import androidx.core.text.BidiFormatter;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.notification.NotificationBackend;
import com.android.settings.notification.app.AppChannelsBypassingDndSettings;
import com.android.settingslib.applications.ApplicationsState;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.widget.AppPreference;
import java.util.ArrayList;
import java.util.List;

public class ZenModeAddBypassingAppsPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin {
    /* access modifiers changed from: private */
    public Preference mAddPreference;
    /* access modifiers changed from: private */
    public ApplicationsState.Session mAppSession;
    /* access modifiers changed from: private */
    public final ApplicationsState.Callbacks mAppSessionCallbacks;
    ApplicationsState mApplicationsState;
    /* access modifiers changed from: private */
    public Fragment mHostFragment;
    private final NotificationBackend mNotificationBackend;
    Context mPrefContext;
    PreferenceCategory mPreferenceCategory;
    PreferenceScreen mPreferenceScreen;

    public String getPreferenceKey() {
        return "zen_mode_non_bypassing_apps_list";
    }

    public boolean isAvailable() {
        return true;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public ZenModeAddBypassingAppsPreferenceController(Context context, Application application, Fragment fragment, NotificationBackend notificationBackend) {
        this(context, application == null ? null : ApplicationsState.getInstance(application), fragment, notificationBackend);
    }

    private ZenModeAddBypassingAppsPreferenceController(Context context, ApplicationsState applicationsState, Fragment fragment, NotificationBackend notificationBackend) {
        super(context);
        this.mAppSessionCallbacks = new ApplicationsState.Callbacks() {
            public void onAllSizesComputed() {
            }

            public void onRunningStateChanged(boolean z) {
                ZenModeAddBypassingAppsPreferenceController.this.updateAppList();
            }

            public void onPackageListChanged() {
                ZenModeAddBypassingAppsPreferenceController.this.updateAppList();
            }

            public void onRebuildComplete(ArrayList<ApplicationsState.AppEntry> arrayList) {
                ZenModeAddBypassingAppsPreferenceController.this.updateAppList(arrayList);
            }

            public void onPackageIconChanged() {
                ZenModeAddBypassingAppsPreferenceController.this.updateAppList();
            }

            public void onPackageSizeChanged(String str) {
                ZenModeAddBypassingAppsPreferenceController.this.updateAppList();
            }

            public void onLauncherInfoChanged() {
                ZenModeAddBypassingAppsPreferenceController.this.updateAppList();
            }

            public void onLoadEntriesCompleted() {
                ZenModeAddBypassingAppsPreferenceController.this.updateAppList();
            }
        };
        this.mNotificationBackend = notificationBackend;
        this.mApplicationsState = applicationsState;
        this.mHostFragment = fragment;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        this.mPreferenceScreen = preferenceScreen;
        Preference findPreference = preferenceScreen.findPreference("zen_mode_bypassing_apps_add");
        this.mAddPreference = findPreference;
        findPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                ZenModeAddBypassingAppsPreferenceController.this.mAddPreference.setVisible(false);
                ZenModeAddBypassingAppsPreferenceController zenModeAddBypassingAppsPreferenceController = ZenModeAddBypassingAppsPreferenceController.this;
                if (zenModeAddBypassingAppsPreferenceController.mApplicationsState == null || zenModeAddBypassingAppsPreferenceController.mHostFragment == null) {
                    return true;
                }
                ZenModeAddBypassingAppsPreferenceController zenModeAddBypassingAppsPreferenceController2 = ZenModeAddBypassingAppsPreferenceController.this;
                ApplicationsState.Session unused = zenModeAddBypassingAppsPreferenceController2.mAppSession = zenModeAddBypassingAppsPreferenceController2.mApplicationsState.newSession(zenModeAddBypassingAppsPreferenceController2.mAppSessionCallbacks, ZenModeAddBypassingAppsPreferenceController.this.mHostFragment.getLifecycle());
                return true;
            }
        });
        this.mPrefContext = preferenceScreen.getContext();
        super.displayPreference(preferenceScreen);
    }

    public void updateAppList() {
        ApplicationsState.Session session = this.mAppSession;
        if (session != null) {
            updateAppList(session.rebuild(ApplicationsState.FILTER_ALL_ENABLED, ApplicationsState.ALPHA_COMPARATOR));
        }
    }

    /* access modifiers changed from: package-private */
    public void updateAppList(List<ApplicationsState.AppEntry> list) {
        if (list != null) {
            if (this.mPreferenceCategory == null) {
                PreferenceCategory preferenceCategory = new PreferenceCategory(this.mPrefContext);
                this.mPreferenceCategory = preferenceCategory;
                preferenceCategory.setTitle((int) R.string.zen_mode_bypassing_apps_add_header);
                this.mPreferenceScreen.addPreference(this.mPreferenceCategory);
            }
            ArrayList<Preference> arrayList = new ArrayList<>();
            for (ApplicationsState.AppEntry next : list) {
                String str = next.info.packageName;
                this.mApplicationsState.ensureIcon(next);
                int channelCount = this.mNotificationBackend.getChannelCount(str, next.info.uid);
                if (this.mNotificationBackend.getNotificationChannelsBypassingDnd(str, next.info.uid).getList().size() == 0 && channelCount > 0) {
                    String key = ZenModeAllBypassingAppsPreferenceController.getKey(str);
                    Preference findPreference = this.mPreferenceCategory.findPreference("");
                    if (findPreference == null) {
                        findPreference = new AppPreference(this.mPrefContext);
                        findPreference.setKey(key);
                        findPreference.setOnPreferenceClickListener(new C1128x33e36a97(this, next));
                    }
                    findPreference.setTitle((CharSequence) BidiFormatter.getInstance().unicodeWrap(next.label));
                    findPreference.setIcon(next.icon);
                    arrayList.add(findPreference);
                }
            }
            if (arrayList.size() == 0) {
                PreferenceCategory preferenceCategory2 = this.mPreferenceCategory;
                String str2 = ZenModeAllBypassingAppsPreferenceController.KEY_NO_APPS;
                Preference findPreference2 = preferenceCategory2.findPreference(str2);
                if (findPreference2 == null) {
                    findPreference2 = new Preference(this.mPrefContext);
                    findPreference2.setKey(str2);
                    findPreference2.setTitle((int) R.string.zen_mode_bypassing_apps_subtext_none);
                }
                this.mPreferenceCategory.addPreference(findPreference2);
            }
            if (ZenModeAllBypassingAppsPreferenceController.hasAppListChanged(arrayList, this.mPreferenceCategory)) {
                this.mPreferenceCategory.removeAll();
                for (Preference addPreference : arrayList) {
                    this.mPreferenceCategory.addPreference(addPreference);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$updateAppList$0(ApplicationsState.AppEntry appEntry, Preference preference) {
        Bundle bundle = new Bundle();
        bundle.putString("package", appEntry.info.packageName);
        bundle.putInt("uid", appEntry.info.uid);
        new SubSettingLauncher(this.mContext).setDestination(AppChannelsBypassingDndSettings.class.getName()).setArguments(bundle).setResultListener(this.mHostFragment, 0).setUserHandle(new UserHandle(UserHandle.getUserId(appEntry.info.uid))).setSourceMetricsCategory(1589).launch();
        return true;
    }
}
