package com.android.settings.display;

public class ScreenZoomPreferenceFragmentForSetupWizard extends ScreenZoomSettings {
    public int getMetricsCategory() {
        return 370;
    }

    public void onStop() {
        if (this.mCurrentIndex != this.mInitialIndex) {
            this.mMetricsFeatureProvider.action(getContext(), 370, this.mCurrentIndex);
        }
        super.onStop();
    }
}
