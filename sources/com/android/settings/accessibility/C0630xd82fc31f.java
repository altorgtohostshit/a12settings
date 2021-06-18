package com.android.settings.accessibility;

import android.view.accessibility.AccessibilityManager;

/* renamed from: com.android.settings.accessibility.ToggleScreenMagnificationPreferenceFragment$$ExternalSyntheticLambda2 */
public final /* synthetic */ class C0630xd82fc31f implements AccessibilityManager.TouchExplorationStateChangeListener {
    public final /* synthetic */ ToggleScreenMagnificationPreferenceFragment f$0;

    public /* synthetic */ C0630xd82fc31f(ToggleScreenMagnificationPreferenceFragment toggleScreenMagnificationPreferenceFragment) {
        this.f$0 = toggleScreenMagnificationPreferenceFragment;
    }

    public final void onTouchExplorationStateChanged(boolean z) {
        this.f$0.lambda$onCreateView$0(z);
    }
}
