package com.android.settings.display;

public class FontSizePreferenceFragmentForSetupWizard extends ToggleFontSizePreferenceFragment {
    public int getMetricsCategory() {
        return 369;
    }

    public void onStop() {
        if (this.mCurrentIndex != this.mInitialIndex) {
            this.mMetricsFeatureProvider.action(getContext(), 369, this.mCurrentIndex);
        }
        super.onStop();
    }
}
