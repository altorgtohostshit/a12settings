package com.android.settings.accessibility;

import android.view.View;
import com.android.settings.accessibility.AccessibilityServiceWarning;

public final /* synthetic */ class AccessibilityServiceWarning$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ AccessibilityServiceWarning.UninstallActionPerformer f$0;

    public /* synthetic */ AccessibilityServiceWarning$$ExternalSyntheticLambda0(AccessibilityServiceWarning.UninstallActionPerformer uninstallActionPerformer) {
        this.f$0 = uninstallActionPerformer;
    }

    public final void onClick(View view) {
        this.f$0.uninstallPackage();
    }
}
