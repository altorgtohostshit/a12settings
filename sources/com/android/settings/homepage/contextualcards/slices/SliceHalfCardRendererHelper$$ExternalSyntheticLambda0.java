package com.android.settings.homepage.contextualcards.slices;

import android.view.View;
import androidx.slice.core.SliceAction;
import com.android.settings.homepage.contextualcards.ContextualCard;
import com.android.settings.homepage.contextualcards.slices.SliceHalfCardRendererHelper;

public final /* synthetic */ class SliceHalfCardRendererHelper$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ SliceHalfCardRendererHelper f$0;
    public final /* synthetic */ SliceAction f$1;
    public final /* synthetic */ ContextualCard f$2;
    public final /* synthetic */ SliceHalfCardRendererHelper.HalfCardViewHolder f$3;

    public /* synthetic */ SliceHalfCardRendererHelper$$ExternalSyntheticLambda0(SliceHalfCardRendererHelper sliceHalfCardRendererHelper, SliceAction sliceAction, ContextualCard contextualCard, SliceHalfCardRendererHelper.HalfCardViewHolder halfCardViewHolder) {
        this.f$0 = sliceHalfCardRendererHelper;
        this.f$1 = sliceAction;
        this.f$2 = contextualCard;
        this.f$3 = halfCardViewHolder;
    }

    public final void onClick(View view) {
        this.f$0.lambda$bindView$0(this.f$1, this.f$2, this.f$3, view);
    }
}
