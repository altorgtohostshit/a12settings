package com.android.settings.accessibility;

import android.content.DialogInterface;

public final /* synthetic */ class ToggleFeaturePreferenceFragment$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public final /* synthetic */ ToggleFeaturePreferenceFragment f$0;

    public /* synthetic */ ToggleFeaturePreferenceFragment$$ExternalSyntheticLambda0(ToggleFeaturePreferenceFragment toggleFeaturePreferenceFragment) {
        this.f$0 = toggleFeaturePreferenceFragment;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.callOnAlertDialogCheckboxClicked(dialogInterface, i);
    }
}
