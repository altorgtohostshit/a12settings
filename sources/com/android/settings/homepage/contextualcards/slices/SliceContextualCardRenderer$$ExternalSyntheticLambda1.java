package com.android.settings.homepage.contextualcards.slices;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.homepage.contextualcards.ContextualCard;

public final /* synthetic */ class SliceContextualCardRenderer$$ExternalSyntheticLambda1 implements View.OnClickListener {
    public final /* synthetic */ SliceContextualCardRenderer f$0;
    public final /* synthetic */ ContextualCard f$1;
    public final /* synthetic */ RecyclerView.ViewHolder f$2;

    public /* synthetic */ SliceContextualCardRenderer$$ExternalSyntheticLambda1(SliceContextualCardRenderer sliceContextualCardRenderer, ContextualCard contextualCard, RecyclerView.ViewHolder viewHolder) {
        this.f$0 = sliceContextualCardRenderer;
        this.f$1 = contextualCard;
        this.f$2 = viewHolder;
    }

    public final void onClick(View view) {
        this.f$0.lambda$initDismissalActions$4(this.f$1, this.f$2, view);
    }
}
