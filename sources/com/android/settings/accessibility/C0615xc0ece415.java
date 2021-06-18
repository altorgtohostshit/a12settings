package com.android.settings.accessibility;

import android.content.DialogInterface;

/* renamed from: com.android.settings.accessibility.ToggleAccessibilityServicePreferenceFragment$$ExternalSyntheticLambda0 */
public final /* synthetic */ class C0615xc0ece415 implements DialogInterface.OnClickListener {
    public final /* synthetic */ ToggleAccessibilityServicePreferenceFragment f$0;

    public /* synthetic */ C0615xc0ece415(ToggleAccessibilityServicePreferenceFragment toggleAccessibilityServicePreferenceFragment) {
        this.f$0 = toggleAccessibilityServicePreferenceFragment;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.onDialogButtonFromDisableToggleClicked(dialogInterface, i);
    }
}
