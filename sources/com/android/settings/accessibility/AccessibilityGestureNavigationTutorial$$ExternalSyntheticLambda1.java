package com.android.settings.accessibility;

import android.content.Context;
import android.view.View;
import android.widget.ViewSwitcher;

public final /* synthetic */ class AccessibilityGestureNavigationTutorial$$ExternalSyntheticLambda1 implements ViewSwitcher.ViewFactory {
    public final /* synthetic */ Context f$0;

    public /* synthetic */ AccessibilityGestureNavigationTutorial$$ExternalSyntheticLambda1(Context context) {
        this.f$0 = context;
    }

    public final View makeView() {
        return AccessibilityGestureNavigationTutorial.makeInstructionView(this.f$0);
    }
}
