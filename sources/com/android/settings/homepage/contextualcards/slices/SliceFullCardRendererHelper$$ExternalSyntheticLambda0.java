package com.android.settings.homepage.contextualcards.slices;

import androidx.slice.SliceItem;
import androidx.slice.widget.EventInfo;
import androidx.slice.widget.SliceView;
import com.android.settings.homepage.contextualcards.ContextualCard;
import com.android.settings.homepage.contextualcards.slices.SliceFullCardRendererHelper;

public final /* synthetic */ class SliceFullCardRendererHelper$$ExternalSyntheticLambda0 implements SliceView.OnSliceActionListener {
    public final /* synthetic */ SliceFullCardRendererHelper f$0;
    public final /* synthetic */ ContextualCard f$1;
    public final /* synthetic */ SliceFullCardRendererHelper.SliceViewHolder f$2;

    public /* synthetic */ SliceFullCardRendererHelper$$ExternalSyntheticLambda0(SliceFullCardRendererHelper sliceFullCardRendererHelper, ContextualCard contextualCard, SliceFullCardRendererHelper.SliceViewHolder sliceViewHolder) {
        this.f$0 = sliceFullCardRendererHelper;
        this.f$1 = contextualCard;
        this.f$2 = sliceViewHolder;
    }

    public final void onSliceAction(EventInfo eventInfo, SliceItem sliceItem) {
        this.f$0.lambda$bindView$0(this.f$1, this.f$2, eventInfo, sliceItem);
    }
}
