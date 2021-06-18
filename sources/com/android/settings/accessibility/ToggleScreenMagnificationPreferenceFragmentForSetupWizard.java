package com.android.settings.accessibility;

import android.os.Bundle;
import android.view.View;

public class ToggleScreenMagnificationPreferenceFragmentForSetupWizard extends ToggleScreenMagnificationPreferenceFragment {
    public int getMetricsCategory() {
        return 368;
    }

    public void onStop() {
        Bundle arguments = getArguments();
        if (!(arguments == null || !arguments.containsKey("checked") || this.mToggleServiceSwitchPreference.isChecked() == arguments.getBoolean("checked"))) {
            this.mMetricsFeatureProvider.action(getContext(), 368, this.mToggleServiceSwitchPreference.isChecked());
        }
        super.onStop();
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mSettingsPreference.setVisible(false);
    }
}
