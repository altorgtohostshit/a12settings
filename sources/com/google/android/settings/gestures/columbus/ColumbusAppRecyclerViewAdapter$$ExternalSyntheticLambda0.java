package com.google.android.settings.gestures.columbus;

import android.content.pm.ApplicationInfo;
import android.view.View;

public final /* synthetic */ class ColumbusAppRecyclerViewAdapter$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ ColumbusAppRecyclerViewAdapter f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ ApplicationInfo f$2;

    public /* synthetic */ ColumbusAppRecyclerViewAdapter$$ExternalSyntheticLambda0(ColumbusAppRecyclerViewAdapter columbusAppRecyclerViewAdapter, int i, ApplicationInfo applicationInfo) {
        this.f$0 = columbusAppRecyclerViewAdapter;
        this.f$1 = i;
        this.f$2 = applicationInfo;
    }

    public final void onClick(View view) {
        this.f$0.lambda$onBindViewHolder$0(this.f$1, this.f$2, view);
    }
}
