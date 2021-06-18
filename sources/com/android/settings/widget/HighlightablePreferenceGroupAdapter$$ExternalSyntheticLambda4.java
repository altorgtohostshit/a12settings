package com.android.settings.widget;

import com.google.android.material.appbar.AppBarLayout;

public final /* synthetic */ class HighlightablePreferenceGroupAdapter$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ AppBarLayout f$0;

    public /* synthetic */ HighlightablePreferenceGroupAdapter$$ExternalSyntheticLambda4(AppBarLayout appBarLayout) {
        this.f$0 = appBarLayout;
    }

    public final void run() {
        this.f$0.setExpanded(false, true);
    }
}
