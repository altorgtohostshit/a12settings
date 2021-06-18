package com.android.settings.display;

import android.content.Context;
import android.content.IntentFilter;
import android.hardware.display.ColorDisplayManager;
import android.text.TextUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.SliderPreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.widget.SeekBarPreference;

public class NightDisplayIntensityPreferenceController extends SliderPreferenceController {
    private ColorDisplayManager mColorDisplayManager;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public int getMin() {
        return 0;
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public boolean isPublicSlice() {
        return true;
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public NightDisplayIntensityPreferenceController(Context context, String str) {
        super(context, str);
        this.mColorDisplayManager = (ColorDisplayManager) context.getSystemService(ColorDisplayManager.class);
    }

    public int getAvailabilityStatus() {
        if (!ColorDisplayManager.isNightDisplayAvailable(this.mContext)) {
            return 3;
        }
        return !this.mColorDisplayManager.isNightDisplayActivated() ? 5 : 0;
    }

    public boolean isSliceable() {
        return TextUtils.equals(getPreferenceKey(), "night_display_temperature");
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        SeekBarPreference seekBarPreference = (SeekBarPreference) preferenceScreen.findPreference(getPreferenceKey());
        seekBarPreference.setContinuousUpdates(true);
        seekBarPreference.setMax(getMax());
        seekBarPreference.setMin(getMin());
        seekBarPreference.setHapticFeedbackMode(2);
    }

    public final void updateState(Preference preference) {
        super.updateState(preference);
        preference.setEnabled(this.mColorDisplayManager.isNightDisplayActivated());
    }

    public int getSliderPosition() {
        return convertTemperature(this.mColorDisplayManager.getNightDisplayColorTemperature());
    }

    public boolean setSliderPosition(int i) {
        return this.mColorDisplayManager.setNightDisplayColorTemperature(convertTemperature(i));
    }

    public int getMax() {
        return convertTemperature(ColorDisplayManager.getMinimumColorTemperature(this.mContext));
    }

    private int convertTemperature(int i) {
        return ColorDisplayManager.getMaximumColorTemperature(this.mContext) - i;
    }
}
