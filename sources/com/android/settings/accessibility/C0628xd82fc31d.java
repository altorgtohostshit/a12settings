package com.android.settings.accessibility;

import android.content.DialogInterface;

/* renamed from: com.android.settings.accessibility.ToggleScreenMagnificationPreferenceFragment$$ExternalSyntheticLambda0 */
public final /* synthetic */ class C0628xd82fc31d implements DialogInterface.OnClickListener {
    public final /* synthetic */ ToggleScreenMagnificationPreferenceFragment f$0;

    public /* synthetic */ C0628xd82fc31d(ToggleScreenMagnificationPreferenceFragment toggleScreenMagnificationPreferenceFragment) {
        this.f$0 = toggleScreenMagnificationPreferenceFragment;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.callOnAlertDialogCheckboxClicked(dialogInterface, i);
    }
}
