package com.android.settings.accessibility;

import android.content.Context;
import android.content.IntentFilter;
import android.hardware.display.ColorDisplayManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.SliderPreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.widget.SeekBarPreference;

public class ReduceBrightColorsIntensityPreferenceController extends SliderPreferenceController {
    private final ColorDisplayManager mColorDisplayManager;

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

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public ReduceBrightColorsIntensityPreferenceController(Context context, String str) {
        super(context, str);
        this.mColorDisplayManager = (ColorDisplayManager) context.getSystemService(ColorDisplayManager.class);
    }

    public int getAvailabilityStatus() {
        if (!ColorDisplayManager.isReduceBrightColorsAvailable(this.mContext)) {
            return 3;
        }
        return !this.mColorDisplayManager.isReduceBrightColorsActivated() ? 5 : 0;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        SeekBarPreference seekBarPreference = (SeekBarPreference) preferenceScreen.findPreference(getPreferenceKey());
        seekBarPreference.setContinuousUpdates(true);
        seekBarPreference.setMax(getMax());
        seekBarPreference.setMin(getMin());
        seekBarPreference.setHapticFeedbackMode(2);
        updateState(seekBarPreference);
    }

    public final void updateState(Preference preference) {
        super.updateState(preference);
        preference.setEnabled(this.mColorDisplayManager.isReduceBrightColorsActivated());
    }

    public int getSliderPosition() {
        return this.mColorDisplayManager.getReduceBrightColorsStrength();
    }

    public boolean setSliderPosition(int i) {
        return this.mColorDisplayManager.setReduceBrightColorsStrength(i);
    }

    public int getMax() {
        return ColorDisplayManager.getMaximumReduceBrightColorsStrength(this.mContext);
    }

    public int getMin() {
        return ColorDisplayManager.getMinimumReduceBrightColorsStrength(this.mContext);
    }
}
