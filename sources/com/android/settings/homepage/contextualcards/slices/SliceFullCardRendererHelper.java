package com.android.settings.homepage.contextualcards.slices;

import android.content.Context;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import androidx.slice.Slice;
import androidx.slice.SliceItem;
import androidx.slice.widget.EventInfo;
import androidx.slice.widget.SliceView;
import com.android.settings.R;
import com.android.settings.homepage.contextualcards.ContextualCard;
import com.android.settings.homepage.contextualcards.logging.ContextualCardLogUtils;
import com.android.settings.overlay.FeatureFactory;

class SliceFullCardRendererHelper {
    private final Context mContext;

    SliceFullCardRendererHelper(Context context) {
        this.mContext = context;
    }

    /* access modifiers changed from: package-private */
    public RecyclerView.ViewHolder createViewHolder(View view) {
        return new SliceViewHolder(view);
    }

    /* access modifiers changed from: package-private */
    public void bindView(RecyclerView.ViewHolder viewHolder, ContextualCard contextualCard, Slice slice) {
        SliceViewHolder sliceViewHolder = (SliceViewHolder) viewHolder;
        sliceViewHolder.sliceView.setScrollable(false);
        sliceViewHolder.sliceView.setTag(contextualCard.getSliceUri());
        sliceViewHolder.sliceView.setMode(2);
        sliceViewHolder.sliceView.setSlice(slice);
        sliceViewHolder.sliceView.setOnSliceActionListener(new SliceFullCardRendererHelper$$ExternalSyntheticLambda0(this, contextualCard, sliceViewHolder));
        sliceViewHolder.sliceView.setShowTitleItems(true);
        if (contextualCard.isLargeCard()) {
            sliceViewHolder.sliceView.setShowHeaderDivider(true);
            sliceViewHolder.sliceView.setShowActionDividers(true);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$bindView$0(ContextualCard contextualCard, SliceViewHolder sliceViewHolder, EventInfo eventInfo, SliceItem sliceItem) {
        FeatureFactory.getFactory(this.mContext).getMetricsFeatureProvider().action(this.mContext, 1666, ContextualCardLogUtils.buildCardClickLog(contextualCard, eventInfo.rowIndex, eventInfo.actionType, sliceViewHolder.getAdapterPosition()));
    }

    static class SliceViewHolder extends RecyclerView.ViewHolder {
        public final SliceView sliceView;

        public SliceViewHolder(View view) {
            super(view);
            this.sliceView = (SliceView) view.findViewById(R.id.slice_view);
        }
    }
}
