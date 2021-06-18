package com.android.settings.dream;

import android.content.Context;
import android.content.IntentFilter;
import androidx.preference.Preference;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.widget.SettingsMainSwitchPreferenceController;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.dream.DreamBackend;
import com.android.settingslib.widget.MainSwitchPreference;

public class StartNowPreferenceController extends SettingsMainSwitchPreferenceController {
    private final DreamBackend mBackend;
    private final MetricsFeatureProvider mMetricsFeatureProvider;

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

    public boolean isChecked() {
        return false;
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public StartNowPreferenceController(Context context, String str) {
        super(context, str);
        this.mBackend = DreamBackend.getInstance(context);
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(context).getMetricsFeatureProvider();
    }

    public void updateState(Preference preference) {
        boolean z = false;
        this.mSwitchPreference.setChecked(false);
        MainSwitchPreference mainSwitchPreference = this.mSwitchPreference;
        if (this.mBackend.getWhenToDreamSetting() != 3) {
            z = true;
        }
        mainSwitchPreference.setEnabled(z);
    }

    public boolean setChecked(boolean z) {
        if (!z) {
            return true;
        }
        MetricsFeatureProvider metricsFeatureProvider = this.mMetricsFeatureProvider;
        MainSwitchPreference mainSwitchPreference = this.mSwitchPreference;
        metricsFeatureProvider.logClickedPreference(mainSwitchPreference, mainSwitchPreference.getExtras().getInt("category"));
        this.mBackend.startDreaming();
        return true;
    }
}
