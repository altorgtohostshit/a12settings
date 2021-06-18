package com.google.android.settings.gestures.columbus;

import android.content.pm.ApplicationInfo;
import android.view.View;
import java.util.List;

public final /* synthetic */ class ColumbusAppRecyclerViewAdapter$$ExternalSyntheticLambda1 implements View.OnClickListener {
    public final /* synthetic */ ColumbusAppRecyclerViewAdapter f$0;
    public final /* synthetic */ ApplicationInfo f$1;
    public final /* synthetic */ List f$2;

    public /* synthetic */ ColumbusAppRecyclerViewAdapter$$ExternalSyntheticLambda1(ColumbusAppRecyclerViewAdapter columbusAppRecyclerViewAdapter, ApplicationInfo applicationInfo, List list) {
        this.f$0 = columbusAppRecyclerViewAdapter;
        this.f$1 = applicationInfo;
        this.f$2 = list;
    }

    public final void onClick(View view) {
        this.f$0.lambda$onBindViewHolder$1(this.f$1, this.f$2, view);
    }
}
