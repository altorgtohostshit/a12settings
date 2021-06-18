package com.android.settings.display;

import android.content.Context;
import android.content.IntentFilter;
import android.hardware.display.ColorDisplayManager;
import android.text.TextUtils;
import android.widget.Switch;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.widget.MainSwitchPreference;
import com.android.settingslib.widget.OnMainSwitchChangeListener;
import java.time.LocalTime;

public class NightDisplayActivationPreferenceController extends TogglePreferenceController implements OnMainSwitchChangeListener {
    private ColorDisplayManager mColorDisplayManager;
    private final MetricsFeatureProvider mMetricsFeatureProvider;
    private MainSwitchPreference mPreference;
    private NightDisplayTimeFormatter mTimeFormatter;

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

    public boolean isPublicSlice() {
        return true;
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public NightDisplayActivationPreferenceController(Context context, String str) {
        super(context, str);
        this.mColorDisplayManager = (ColorDisplayManager) context.getSystemService(ColorDisplayManager.class);
        this.mTimeFormatter = new NightDisplayTimeFormatter(context);
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(context).getMetricsFeatureProvider();
    }

    public int getAvailabilityStatus() {
        return ColorDisplayManager.isNightDisplayAvailable(this.mContext) ? 1 : 3;
    }

    public boolean isSliceable() {
        return TextUtils.equals(getPreferenceKey(), "night_display_activated");
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        MainSwitchPreference mainSwitchPreference = (MainSwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = mainSwitchPreference;
        mainSwitchPreference.addOnSwitchChangeListener(this);
        this.mPreference.updateStatus(this.mColorDisplayManager.isNightDisplayActivated());
    }

    public void onSwitchChanged(Switch switchR, boolean z) {
        if (z != this.mColorDisplayManager.isNightDisplayActivated()) {
            setChecked(z);
        }
    }

    public final void updateState(Preference preference) {
        updateStateInternal();
    }

    public boolean isChecked() {
        return this.mColorDisplayManager.isNightDisplayActivated();
    }

    public boolean setChecked(boolean z) {
        return this.mColorDisplayManager.setNightDisplayActivated(z);
    }

    public CharSequence getSummary() {
        return this.mTimeFormatter.getAutoModeSummary(this.mContext, this.mColorDisplayManager);
    }

    private void updateStateInternal() {
        LocalTime localTime;
        boolean isNightDisplayActivated = this.mColorDisplayManager.isNightDisplayActivated();
        if (this.mColorDisplayManager.getNightDisplayAutoMode() == 1) {
            NightDisplayTimeFormatter nightDisplayTimeFormatter = this.mTimeFormatter;
            if (isNightDisplayActivated) {
                localTime = this.mColorDisplayManager.getNightDisplayCustomStartTime();
            } else {
                localTime = this.mColorDisplayManager.getNightDisplayCustomEndTime();
            }
            nightDisplayTimeFormatter.getFormattedTimeString(localTime);
        }
    }
}
