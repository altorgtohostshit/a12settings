package com.android.settings.homepage.contextualcards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R;
import com.android.settings.homepage.contextualcards.slices.SwipeDismissalDelegate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ContextualCardsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ContextualCardUpdateListener, SwipeDismissalDelegate.Listener {
    private final Context mContext;
    final List<ContextualCard> mContextualCards = new ArrayList();
    private final ControllerRendererPool mControllerRendererPool;
    private final LifecycleOwner mLifecycleOwner;
    private RecyclerView mRecyclerView;

    public ContextualCardsAdapter(Context context, LifecycleOwner lifecycleOwner, ContextualCardManager contextualCardManager) {
        this.mContext = context;
        this.mControllerRendererPool = contextualCardManager.getControllerRendererPool();
        this.mLifecycleOwner = lifecycleOwner;
        setHasStableIds(true);
    }

    public long getItemId(int i) {
        return (long) this.mContextualCards.get(i).hashCode();
    }

    public int getItemViewType(int i) {
        return this.mContextualCards.get(i).getViewType();
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return this.mControllerRendererPool.getRendererByViewType(this.mContext, this.mLifecycleOwner, i).createViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(i, viewGroup, false), i);
    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ContextualCard contextualCard = this.mContextualCards.get(i);
        this.mControllerRendererPool.getRendererByViewType(this.mContext, this.mLifecycleOwner, contextualCard.getViewType()).bindView(viewHolder, contextualCard);
    }

    public int getItemCount() {
        return this.mContextualCards.size();
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mRecyclerView = recyclerView;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                public int getSpanSize(int i) {
                    int viewType = ContextualCardsAdapter.this.mContextualCards.get(i).getViewType();
                    return (viewType == R.layout.conditional_card_half_tile || viewType == R.layout.contextual_slice_half_tile) ? 1 : 2;
                }
            });
        }
    }

    public void onContextualCardUpdated(Map<Integer, List<ContextualCard>> map) {
        boolean z = false;
        List list = map.get(0);
        boolean isEmpty = this.mContextualCards.isEmpty();
        if (list == null || list.isEmpty()) {
            z = true;
        }
        if (list == null) {
            this.mContextualCards.clear();
            notifyDataSetChanged();
        } else {
            DiffUtil.DiffResult calculateDiff = DiffUtil.calculateDiff(new ContextualCardsDiffCallback(this.mContextualCards, list));
            this.mContextualCards.clear();
            this.mContextualCards.addAll(list);
            calculateDiff.dispatchUpdatesTo((RecyclerView.Adapter) this);
        }
        RecyclerView recyclerView = this.mRecyclerView;
        if (recyclerView != null && isEmpty && !z) {
            recyclerView.scheduleLayoutAnimation();
        }
    }

    public void onSwiped(int i) {
        this.mContextualCards.set(i, this.mContextualCards.get(i).mutate().setIsPendingDismiss(true).build());
        notifyItemChanged(i);
    }
}
