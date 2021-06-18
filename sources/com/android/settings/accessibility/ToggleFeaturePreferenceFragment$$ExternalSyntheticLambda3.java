package com.android.settings.accessibility;

import android.view.accessibility.AccessibilityManager;

public final /* synthetic */ class ToggleFeaturePreferenceFragment$$ExternalSyntheticLambda3 implements AccessibilityManager.TouchExplorationStateChangeListener {
    public final /* synthetic */ ToggleFeaturePreferenceFragment f$0;

    public /* synthetic */ ToggleFeaturePreferenceFragment$$ExternalSyntheticLambda3(ToggleFeaturePreferenceFragment toggleFeaturePreferenceFragment) {
        this.f$0 = toggleFeaturePreferenceFragment;
    }

    public final void onTouchExplorationStateChanged(boolean z) {
        this.f$0.lambda$onCreateView$1(z);
    }
}
