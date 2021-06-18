package com.android.settings.homepage.contextualcards.slices;

import android.view.View;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import androidx.slice.Slice;
import com.android.settings.homepage.contextualcards.ContextualCard;

public final /* synthetic */ class SliceContextualCardRenderer$$ExternalSyntheticLambda2 implements Observer {
    public final /* synthetic */ SliceContextualCardRenderer f$0;
    public final /* synthetic */ RecyclerView.ViewHolder f$1;
    public final /* synthetic */ ContextualCard f$2;
    public final /* synthetic */ View f$3;

    public /* synthetic */ SliceContextualCardRenderer$$ExternalSyntheticLambda2(SliceContextualCardRenderer sliceContextualCardRenderer, RecyclerView.ViewHolder viewHolder, ContextualCard contextualCard, View view) {
        this.f$0 = sliceContextualCardRenderer;
        this.f$1 = viewHolder;
        this.f$2 = contextualCard;
        this.f$3 = view;
    }

    public final void onChanged(Object obj) {
        this.f$0.lambda$bindView$2(this.f$1, this.f$2, this.f$3, (Slice) obj);
    }
}
