package com.google.android.settings.gestures.columbus;

import android.content.pm.ShortcutInfo;
import android.view.View;

public final /* synthetic */ class ColumbusAppShortcutRecyclerViewAdapter$$ExternalSyntheticLambda1 implements View.OnClickListener {
    public final /* synthetic */ ColumbusAppShortcutRecyclerViewAdapter f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ ShortcutInfo f$2;

    public /* synthetic */ ColumbusAppShortcutRecyclerViewAdapter$$ExternalSyntheticLambda1(ColumbusAppShortcutRecyclerViewAdapter columbusAppShortcutRecyclerViewAdapter, int i, ShortcutInfo shortcutInfo) {
        this.f$0 = columbusAppShortcutRecyclerViewAdapter;
        this.f$1 = i;
        this.f$2 = shortcutInfo;
    }

    public final void onClick(View view) {
        this.f$0.lambda$onBindViewHolder$1(this.f$1, this.f$2, view);
    }
}
