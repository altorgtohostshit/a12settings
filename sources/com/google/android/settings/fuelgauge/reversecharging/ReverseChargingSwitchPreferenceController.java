package com.google.android.settings.fuelgauge.reversecharging;

import android.content.Context;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Switch;
import androidx.constraintlayout.widget.R$styleable;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.Utils;
import com.android.settingslib.widget.MainSwitchPreference;
import com.android.settingslib.widget.OnMainSwitchChangeListener;
import com.android.settingslib.widget.TopIntroPreference;
import com.google.android.systemui.reversecharging.ReverseChargingMetrics;

public class ReverseChargingSwitchPreferenceController extends ReverseChargingBasePreferenceController implements OnMainSwitchChangeListener {
    private static final String KEY_INTRO_PREFERENCE = "reverse_charging_summary";
    static final int NO_ERROR = -1;
    private static final String TAG = "RCSwitchPrefController";
    MainSwitchPreference mPreference;
    TopIntroPreference mTopIntroPreference;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
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

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public ReverseChargingSwitchPreferenceController(Context context, String str) {
        super(context, str);
    }

    public void updateState() {
        updateState(this.mPreference);
    }

    public boolean isChecked() {
        return this.mReverseChargingManager.isReverseChargingOn();
    }

    /* access modifiers changed from: package-private */
    public int checkLaunchRequirements() {
        if (this.mReverseChargingManager.isOnWirelessCharge()) {
            return R$styleable.Constraint_layout_goneMarginRight;
        }
        if (isLowBattery()) {
            return 100;
        }
        if (isPowerSaveMode()) {
            return R$styleable.Constraint_motionStagger;
        }
        if (this.mIsUsbPlugIn) {
            return R$styleable.Constraint_progress;
        }
        return isOverheat() ? R$styleable.Constraint_transitionPathRotate : NO_ERROR;
    }

    private void logLaunchFailEvent(int i) {
        Log.d(TAG, "checkLaunchRequirements() = " + i);
        int intProperty = ((BatteryManager) this.mContext.getSystemService(BatteryManager.class)).getIntProperty(4);
        if (i != 100) {
            ReverseChargingMetrics.logStopEvent(i, intProperty, 0);
        } else if (this.mLevelChanged) {
            ReverseChargingMetrics.logStopEvent(i, intProperty, 0);
        }
    }

    public void onSwitchChanged(Switch switchR, boolean z) {
        if (z != this.mReverseChargingManager.isReverseChargingOn()) {
            this.mReverseChargingManager.setReverseChargingState(z);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isLowBattery() {
        int thresholdLevel = getThresholdLevel();
        int i = this.mLevel;
        return thresholdLevel >= i || i < 10;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        MainSwitchPreference mainSwitchPreference = (MainSwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = mainSwitchPreference;
        mainSwitchPreference.addOnSwitchChangeListener(this);
        this.mTopIntroPreference = (TopIntroPreference) preferenceScreen.findPreference(KEY_INTRO_PREFERENCE);
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        int thresholdLevel = getThresholdLevel();
        String string = this.mContext.getString(R.string.reverse_charging_switch_title);
        boolean z = true;
        if (isOverheat()) {
            string = this.mContext.getString(R.string.reverse_charging_detail_charging_overheat_message);
        } else if (isPowerSaveMode()) {
            string = this.mContext.getString(R.string.reverse_charging_detail_power_save_mode_is_on_message);
        } else if (this.mReverseChargingManager.isOnWirelessCharge()) {
            string = this.mContext.getString(R.string.reverse_charging_detail_charging_wirelessly_message);
        } else if (this.mIsUsbPlugIn) {
            string = this.mContext.getString(R.string.reverse_charging_detail_unplug_usb_cable_message);
        } else {
            int i = this.mLevel;
            if (i != NO_ERROR && i < 10) {
                string = this.mContext.getString(R.string.reverse_charging_warning_title, new Object[]{Utils.formatPercentage(10)});
                Log.d(TAG, "updateState() phone is low battery ! level : " + this.mLevel);
            } else if (i != NO_ERROR && thresholdLevel >= i) {
                string = this.mContext.getString(R.string.reverse_charging_warning_title, new Object[]{Utils.formatPercentage(thresholdLevel)});
                Log.d(TAG, "updateState() phone is low battery ! level : " + this.mLevel + ", thresholdLevel : " + thresholdLevel);
            }
        }
        if (!TextUtils.isEmpty(string)) {
            this.mTopIntroPreference.setTitle((CharSequence) string);
        }
        int checkLaunchRequirements = checkLaunchRequirements();
        if (checkLaunchRequirements != NO_ERROR) {
            logLaunchFailEvent(checkLaunchRequirements);
        }
        if (checkLaunchRequirements != NO_ERROR) {
            z = false;
        }
        preference.setEnabled(z);
    }
}
