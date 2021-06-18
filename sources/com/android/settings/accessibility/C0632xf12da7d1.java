package com.android.settings.accessibility;

import android.os.Bundle;
import android.view.View;

/* renamed from: com.android.settings.accessibility.VolumeShortcutToggleSelectToSpeakPreferenceFragmentForSetupWizard */
public class C0632xf12da7d1 extends VolumeShortcutToggleAccessibilityServicePreferenceFragment {
    private boolean mToggleSwitchWasInitiallyChecked;

    public int getMetricsCategory() {
        return 371;
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mToggleSwitchWasInitiallyChecked = this.mToggleServiceSwitchPreference.isChecked();
    }

    public void onStop() {
        if (this.mToggleServiceSwitchPreference.isChecked() != this.mToggleSwitchWasInitiallyChecked) {
            this.mMetricsFeatureProvider.action(getContext(), 817, this.mToggleServiceSwitchPreference.isChecked());
        }
        super.onStop();
    }
}
