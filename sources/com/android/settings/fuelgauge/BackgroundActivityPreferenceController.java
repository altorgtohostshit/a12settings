package com.android.settings.fuelgauge;

import android.app.AppOpsManager;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.os.UserManager;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.core.InstrumentedPreferenceFragment;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.fuelgauge.batterytip.AppInfo;
import com.android.settings.fuelgauge.batterytip.BatteryTipDialogFragment;
import com.android.settings.fuelgauge.batterytip.tips.BatteryTip;
import com.android.settings.fuelgauge.batterytip.tips.RestrictAppTip;
import com.android.settings.fuelgauge.batterytip.tips.UnrestrictAppTip;
import com.android.settingslib.RestrictedPreference;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.fuelgauge.PowerAllowlistBackend;

public class BackgroundActivityPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin {
    static final String KEY_BACKGROUND_ACTIVITY = "background_activity";
    private final AppOpsManager mAppOpsManager;
    BatteryUtils mBatteryUtils;
    DevicePolicyManager mDpm;
    private InstrumentedPreferenceFragment mFragment;
    private PowerAllowlistBackend mPowerAllowlistBackend;
    private String mTargetPackage;
    private final int mUid;
    private final UserManager mUserManager;

    public String getPreferenceKey() {
        return KEY_BACKGROUND_ACTIVITY;
    }

    public BackgroundActivityPreferenceController(Context context, InstrumentedPreferenceFragment instrumentedPreferenceFragment, int i, String str) {
        this(context, instrumentedPreferenceFragment, i, str, PowerAllowlistBackend.getInstance(context));
    }

    BackgroundActivityPreferenceController(Context context, InstrumentedPreferenceFragment instrumentedPreferenceFragment, int i, String str, PowerAllowlistBackend powerAllowlistBackend) {
        super(context);
        this.mPowerAllowlistBackend = powerAllowlistBackend;
        this.mUserManager = (UserManager) context.getSystemService("user");
        this.mDpm = (DevicePolicyManager) context.getSystemService("device_policy");
        this.mAppOpsManager = (AppOpsManager) context.getSystemService("appops");
        this.mUid = i;
        this.mFragment = instrumentedPreferenceFragment;
        this.mTargetPackage = str;
        this.mBatteryUtils = BatteryUtils.getInstance(context);
    }

    public void updateState(Preference preference) {
        if (!((RestrictedPreference) preference).isDisabledByAdmin()) {
            int checkOpNoThrow = this.mAppOpsManager.checkOpNoThrow(70, this.mUid, this.mTargetPackage);
            if (this.mPowerAllowlistBackend.isAllowlisted(this.mTargetPackage) || checkOpNoThrow == 2 || Utils.isProfileOrDeviceOwner(this.mUserManager, this.mDpm, this.mTargetPackage)) {
                preference.setEnabled(false);
            } else {
                preference.setEnabled(true);
            }
            updateSummary(preference);
        }
    }

    public boolean isAvailable() {
        return this.mTargetPackage != null;
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (KEY_BACKGROUND_ACTIVITY.equals(preference.getKey())) {
            boolean z = true;
            if (this.mAppOpsManager.checkOpNoThrow(70, this.mUid, this.mTargetPackage) != 1) {
                z = false;
            }
            showDialog(z);
        }
        return false;
    }

    public void updateSummary(Preference preference) {
        if (this.mPowerAllowlistBackend.isAllowlisted(this.mTargetPackage)) {
            preference.setSummary((int) R.string.background_activity_summary_allowlisted);
            return;
        }
        int checkOpNoThrow = this.mAppOpsManager.checkOpNoThrow(70, this.mUid, this.mTargetPackage);
        if (checkOpNoThrow == 2) {
            preference.setSummary((int) R.string.background_activity_summary_disabled);
            return;
        }
        boolean z = true;
        if (checkOpNoThrow != 1) {
            z = false;
        }
        preference.setSummary(z ? R.string.restricted_true_label : R.string.restricted_false_label);
    }

    /* access modifiers changed from: package-private */
    public void showDialog(boolean z) {
        BatteryTip batteryTip;
        AppInfo build = new AppInfo.Builder().setUid(this.mUid).setPackageName(this.mTargetPackage).build();
        if (z) {
            batteryTip = new UnrestrictAppTip(0, build);
        } else {
            batteryTip = new RestrictAppTip(0, build);
        }
        BatteryTipDialogFragment newInstance = BatteryTipDialogFragment.newInstance(batteryTip, this.mFragment.getMetricsCategory());
        newInstance.setTargetFragment(this.mFragment, 0);
        newInstance.show(this.mFragment.getFragmentManager(), "BgActivityPrefContr");
    }
}
