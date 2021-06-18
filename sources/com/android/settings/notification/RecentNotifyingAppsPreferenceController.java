package com.android.settings.notification;

import android.app.Application;
import android.app.usage.IUsageStatsManager;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.service.notification.NotifyingApp;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.IconDrawableFactory;
import android.util.Slog;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.notification.app.AppNotificationSettings;
import com.android.settings.widget.PrimarySwitchPreference;
import com.android.settingslib.applications.ApplicationsState;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.utils.StringUtil;
import com.android.settingslib.utils.ThreadUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class RecentNotifyingAppsPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin {
    static final String KEY_SEE_ALL = "all_notifications";
    private final ApplicationsState mApplicationsState;
    List<NotifyingApp> mApps;
    private Calendar mCal;
    private PreferenceCategory mCategory;
    private final Fragment mHost;
    private final IconDrawableFactory mIconDrawableFactory;
    private final NotificationBackend mNotificationBackend;
    private final PackageManager mPm;
    private Preference mSeeAllPref;
    private IUsageStatsManager mUsageStatsManager;
    protected List<Integer> mUserIds;

    public String getPreferenceKey() {
        return "recent_notifications_category";
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public RecentNotifyingAppsPreferenceController(Context context, NotificationBackend notificationBackend, IUsageStatsManager iUsageStatsManager, UserManager userManager, Application application, Fragment fragment) {
        this(context, notificationBackend, iUsageStatsManager, userManager, application == null ? null : ApplicationsState.getInstance(application), fragment);
    }

    RecentNotifyingAppsPreferenceController(Context context, NotificationBackend notificationBackend, IUsageStatsManager iUsageStatsManager, UserManager userManager, ApplicationsState applicationsState, Fragment fragment) {
        super(context);
        this.mIconDrawableFactory = IconDrawableFactory.newInstance(context);
        this.mPm = context.getPackageManager();
        this.mHost = fragment;
        this.mApplicationsState = applicationsState;
        this.mNotificationBackend = notificationBackend;
        this.mUsageStatsManager = iUsageStatsManager;
        ArrayList arrayList = new ArrayList();
        this.mUserIds = arrayList;
        arrayList.add(Integer.valueOf(this.mContext.getUserId()));
        int managedProfileId = Utils.getManagedProfileId(userManager, this.mContext.getUserId());
        if (managedProfileId != -10000) {
            this.mUserIds.add(Integer.valueOf(managedProfileId));
        }
    }

    public boolean isAvailable() {
        return this.mApplicationsState != null;
    }

    public void updateNonIndexableKeys(List<String> list) {
        super.updateNonIndexableKeys(list);
        list.add("recent_notifications_category");
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        this.mCategory = (PreferenceCategory) preferenceScreen.findPreference(getPreferenceKey());
        this.mSeeAllPref = preferenceScreen.findPreference(KEY_SEE_ALL);
        super.displayPreference(preferenceScreen);
        refreshUi(this.mCategory.getContext());
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        refreshUi(this.mCategory.getContext());
        this.mSeeAllPref.setTitle((CharSequence) this.mContext.getString(R.string.recent_notifications_see_all_title));
    }

    /* access modifiers changed from: package-private */
    public void refreshUi(Context context) {
        for (int i = 1; i <= 3; i++) {
            PrimarySwitchPreference primarySwitchPreference = (PrimarySwitchPreference) this.mCategory.findPreference("app" + i);
            if (primarySwitchPreference != null) {
                primarySwitchPreference.setChecked(true);
            }
        }
        ThreadUtils.postOnBackgroundThread((Runnable) new C1077xfd336858(this, context));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$refreshUi$1(Context context) {
        reloadData();
        ThreadUtils.postOnMainThread(new C1078xfd336859(this, getDisplayableRecentAppList(), context));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$refreshUi$0(List list, Context context) {
        if (list == null || list.isEmpty()) {
            displayOnlyAllAppsLink();
        } else {
            displayRecentApps(context, list);
        }
    }

    /* access modifiers changed from: package-private */
    public void reloadData() {
        this.mApps = new ArrayList();
        Calendar instance = Calendar.getInstance();
        this.mCal = instance;
        instance.add(6, -3);
        for (Integer intValue : this.mUserIds) {
            int intValue2 = intValue.intValue();
            UsageEvents usageEvents = null;
            try {
                usageEvents = this.mUsageStatsManager.queryEventsForUser(this.mCal.getTimeInMillis(), System.currentTimeMillis(), intValue2, this.mContext.getPackageName());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if (usageEvents != null) {
                ArrayMap arrayMap = new ArrayMap();
                UsageEvents.Event event = new UsageEvents.Event();
                while (usageEvents.hasNextEvent()) {
                    usageEvents.getNextEvent(event);
                    if (event.getEventType() == 12) {
                        NotifyingApp notifyingApp = (NotifyingApp) arrayMap.get(getKey(intValue2, event.getPackageName()));
                        if (notifyingApp == null) {
                            notifyingApp = new NotifyingApp();
                            arrayMap.put(getKey(intValue2, event.getPackageName()), notifyingApp);
                            notifyingApp.setPackage(event.getPackageName());
                            notifyingApp.setUserId(intValue2);
                        }
                        if (event.getTimeStamp() > notifyingApp.getLastNotified()) {
                            notifyingApp.setLastNotified(event.getTimeStamp());
                        }
                    }
                }
                this.mApps.addAll(arrayMap.values());
            }
        }
    }

    private static String getKey(int i, String str) {
        return i + "|" + str;
    }

    private void displayOnlyAllAppsLink() {
        this.mCategory.setTitle((CharSequence) null);
        this.mSeeAllPref.setTitle((int) R.string.notifications_title);
        this.mSeeAllPref.setIcon((Drawable) null);
        for (int preferenceCount = this.mCategory.getPreferenceCount() - 1; preferenceCount >= 0; preferenceCount--) {
            Preference preference = this.mCategory.getPreference(preferenceCount);
            if (!TextUtils.equals(preference.getKey(), KEY_SEE_ALL)) {
                this.mCategory.removePreference(preference);
            }
        }
    }

    private void displayRecentApps(Context context, List<NotifyingApp> list) {
        this.mCategory.setTitle((int) R.string.recent_notifications);
        this.mSeeAllPref.setSummary((CharSequence) null);
        this.mSeeAllPref.setIcon((int) R.drawable.ic_chevron_right_24dp);
        int size = list.size();
        int i = 0;
        int i2 = 1;
        while (i < size) {
            NotifyingApp notifyingApp = list.get(i);
            String str = notifyingApp.getPackage();
            ApplicationsState.AppEntry entry = this.mApplicationsState.getEntry(notifyingApp.getPackage(), notifyingApp.getUserId());
            if (!(entry == null || entry.label == null)) {
                PrimarySwitchPreference primarySwitchPreference = (PrimarySwitchPreference) this.mCategory.findPreference("app" + i2);
                primarySwitchPreference.setTitle((CharSequence) entry.label);
                primarySwitchPreference.setIcon(this.mIconDrawableFactory.getBadgedIcon(entry.info));
                primarySwitchPreference.setIconSize(2);
                primarySwitchPreference.setSummary(StringUtil.formatRelativeTime(this.mContext, (double) (System.currentTimeMillis() - notifyingApp.getLastNotified()), true));
                Bundle bundle = new Bundle();
                bundle.putString("package", str);
                bundle.putInt("uid", entry.info.uid);
                primarySwitchPreference.setOnPreferenceClickListener(new C1076xfd336857(this, bundle, entry));
                primarySwitchPreference.setSwitchEnabled(this.mNotificationBackend.isBlockable(this.mContext, entry.info));
                primarySwitchPreference.setOnPreferenceChangeListener(new C1075xfd336856(this, str, entry));
                primarySwitchPreference.setChecked(!this.mNotificationBackend.getNotificationsBanned(str, entry.info.uid));
            }
            i++;
            i2++;
        }
        while (i2 <= 3) {
            this.mCategory.removePreferenceRecursively("app" + i2);
            i2++;
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$displayRecentApps$2(Bundle bundle, ApplicationsState.AppEntry appEntry, Preference preference) {
        new SubSettingLauncher(this.mHost.getActivity()).setDestination(AppNotificationSettings.class.getName()).setTitleRes(R.string.notifications_title).setArguments(bundle).setUserHandle(new UserHandle(UserHandle.getUserId(appEntry.info.uid))).setSourceMetricsCategory(133).launch();
        return true;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$displayRecentApps$3(String str, ApplicationsState.AppEntry appEntry, Preference preference, Object obj) {
        this.mNotificationBackend.setNotificationsEnabledForPackage(str, appEntry.info.uid, ((Boolean) obj).booleanValue());
        return true;
    }

    private List<NotifyingApp> getDisplayableRecentAppList() {
        Collections.sort(this.mApps);
        ArrayList arrayList = new ArrayList(3);
        int i = 0;
        for (NotifyingApp next : this.mApps) {
            try {
                if (this.mApplicationsState.getEntry(next.getPackage(), next.getUserId()) != null) {
                    arrayList.add(next);
                    i++;
                    if (i >= 3) {
                        break;
                    }
                }
            } catch (Exception e) {
                Slog.e("RecentNotisCtrl", "Failed to find app " + next.getPackage() + "/" + next.getUserId(), e);
            }
        }
        return arrayList;
    }
}
