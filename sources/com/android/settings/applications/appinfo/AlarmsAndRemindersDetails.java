package com.android.settings.applications.appinfo;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.Settings;
import com.android.settings.applications.AppInfoWithHeader;
import com.android.settings.applications.AppStateAlarmsAndRemindersBridge;
import com.android.settings.applications.AppStateBaseBridge;
import com.android.settingslib.RestrictedSwitchPreference;
import com.android.settingslib.applications.ApplicationsState;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;

public class AlarmsAndRemindersDetails extends AppInfoWithHeader implements Preference.OnPreferenceChangeListener {
    private ActivityManager mActivityManager;
    private AppStateAlarmsAndRemindersBridge mAppBridge;
    private AppOpsManager mAppOpsManager;
    private AppStateAlarmsAndRemindersBridge.AlarmsAndRemindersState mPermissionState;
    private RestrictedSwitchPreference mSwitchPref;
    private volatile Boolean mUncommittedState;

    /* access modifiers changed from: protected */
    public AlertDialog createDialog(int i, int i2) {
        return null;
    }

    public int getMetricsCategory() {
        return 1869;
    }

    public static int getSummary(Context context, ApplicationsState.AppEntry appEntry) {
        AppStateAlarmsAndRemindersBridge.AlarmsAndRemindersState alarmsAndRemindersState;
        Object obj = appEntry.extraInfo;
        if (obj instanceof AppStateAlarmsAndRemindersBridge.AlarmsAndRemindersState) {
            alarmsAndRemindersState = (AppStateAlarmsAndRemindersBridge.AlarmsAndRemindersState) obj;
        } else {
            AppStateAlarmsAndRemindersBridge appStateAlarmsAndRemindersBridge = new AppStateAlarmsAndRemindersBridge(context, (ApplicationsState) null, (AppStateBaseBridge.Callback) null);
            ApplicationInfo applicationInfo = appEntry.info;
            alarmsAndRemindersState = appStateAlarmsAndRemindersBridge.createPermissionState(applicationInfo.packageName, applicationInfo.uid);
        }
        return alarmsAndRemindersState.isAllowed() ? R.string.app_permission_summary_allowed : R.string.app_permission_summary_not_allowed;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        FragmentActivity activity = getActivity();
        this.mAppBridge = new AppStateAlarmsAndRemindersBridge(activity, this.mState, (AppStateBaseBridge.Callback) null);
        this.mAppOpsManager = (AppOpsManager) activity.getSystemService(AppOpsManager.class);
        this.mActivityManager = (ActivityManager) activity.getSystemService(ActivityManager.class);
        if (bundle != null) {
            this.mUncommittedState = (Boolean) bundle.get("uncommitted_state");
        }
        addPreferencesFromResource(R.xml.alarms_and_reminders);
        RestrictedSwitchPreference restrictedSwitchPreference = (RestrictedSwitchPreference) findPreference("alarms_and_reminders_switch");
        this.mSwitchPref = restrictedSwitchPreference;
        restrictedSwitchPreference.setOnPreferenceChangeListener(this);
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (this.mUncommittedState != null) {
            bundle.putObject("uncommitted_state", this.mUncommittedState);
        }
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        boolean booleanValue = ((Boolean) obj).booleanValue();
        if (preference != this.mSwitchPref) {
            return false;
        }
        this.mUncommittedState = Boolean.valueOf(booleanValue);
        refreshUi();
        return true;
    }

    private void setCanScheduleAlarms(boolean z) {
        int i = this.mPackageInfo.applicationInfo.uid;
        this.mAppOpsManager.setUidMode("android:schedule_exact_alarm", i, z ? 0 : 2);
        if (!z) {
            this.mActivityManager.killUid(i, "android:schedule_exact_alarm no longer allowed.");
        }
    }

    private void logPermissionChange(boolean z, String str) {
        MetricsFeatureProvider metricsFeatureProvider = this.mMetricsFeatureProvider;
        metricsFeatureProvider.action(metricsFeatureProvider.getAttribution(getActivity()), 1752, getMetricsCategory(), str, z ? 1 : 0);
    }

    public void onPause() {
        super.onPause();
        if (!getActivity().isChangingConfigurations() && this.mPermissionState != null && this.mUncommittedState != null && this.mUncommittedState.booleanValue() != this.mPermissionState.isAllowed()) {
            if (Settings.AlarmsAndRemindersAppActivity.class.getName().equals(getIntent().getComponent().getClassName())) {
                setResult(this.mUncommittedState.booleanValue() ? -1 : 0);
            }
            setCanScheduleAlarms(this.mUncommittedState.booleanValue());
            logPermissionChange(this.mUncommittedState.booleanValue(), this.mPackageName);
            this.mUncommittedState = null;
        }
    }

    /* access modifiers changed from: protected */
    public boolean refreshUi() {
        ApplicationInfo applicationInfo;
        PackageInfo packageInfo = this.mPackageInfo;
        if (packageInfo == null || (applicationInfo = packageInfo.applicationInfo) == null) {
            return false;
        }
        AppStateAlarmsAndRemindersBridge.AlarmsAndRemindersState createPermissionState = this.mAppBridge.createPermissionState(this.mPackageName, applicationInfo.uid);
        this.mPermissionState = createPermissionState;
        this.mSwitchPref.setEnabled(createPermissionState.shouldBeVisible());
        this.mSwitchPref.setChecked(this.mUncommittedState != null ? this.mUncommittedState.booleanValue() : this.mPermissionState.isAllowed());
        return true;
    }
}
