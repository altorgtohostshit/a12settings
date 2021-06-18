package com.google.android.settings.fuelgauge.reversecharging;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.core.SliderPreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.Utils;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStop;
import com.google.android.systemui.reversecharging.ReverseChargingMetrics;

public class ReverseChargingSeekBarController extends SliderPreferenceController implements LifecycleObserver, OnStop {
    public static final int BASE_LEVEL_TIMES = 5;
    public static final int MAX_SEEKBAR_VALUE = 10;
    public static final int MIN_SEEKBAR_VALUE = 2;
    @VisibleForTesting
    boolean mIsPreferenceChanged;
    @VisibleForTesting
    ReverseChargingSeekBarPreference mPreference;
    @VisibleForTesting
    ReverseChargingManager mReverseChargingManager;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public int getMax() {
        return 10;
    }

    public int getMin() {
        return 2;
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

    public ReverseChargingSeekBarController(Context context, String str) {
        super(context, str);
        this.mReverseChargingManager = ReverseChargingManager.getInstance(context);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        ReverseChargingSeekBarPreference reverseChargingSeekBarPreference = (ReverseChargingSeekBarPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = reverseChargingSeekBarPreference;
        reverseChargingSeekBarPreference.setContinuousUpdates(true);
        this.mPreference.setMax(getMax());
        this.mPreference.setMin(getMin());
        this.mPreference.setHapticFeedbackMode(1);
        this.mPreference.setSeekBarStateDescription(Utils.formatPercentage(getSliderPosition() * 5));
    }

    public int getSliderPosition() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "advanced_battery_usage_amount", 2);
    }

    public boolean setSliderPosition(int i) {
        Settings.Global.putInt(this.mContext.getContentResolver(), "advanced_battery_usage_amount", i);
        return true;
    }

    public int getAvailabilityStatus() {
        ReverseChargingManager reverseChargingManager = this.mReverseChargingManager;
        return (reverseChargingManager == null || !reverseChargingManager.isSupportedReverseCharging()) ? 3 : 0;
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        Integer num = (Integer) obj;
        int intValue = num.intValue() * 5;
        this.mPreference.setPercentageValue(Utils.formatPercentage(intValue));
        this.mPreference.setSeekBarStateDescription(Utils.formatPercentage(intValue));
        setSliderPosition(num.intValue());
        this.mIsPreferenceChanged = true;
        return true;
    }

    public void onStop() {
        if (this.mIsPreferenceChanged) {
            ReverseChargingMetrics.logLowBatteryThresholdChange(getSliderPosition() * 5);
            this.mIsPreferenceChanged = false;
        }
    }
}
