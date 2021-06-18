package com.android.settings.homepage.contextualcards.slices;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.recyclerview.widget.RecyclerView;
import androidx.slice.Slice;
import androidx.slice.widget.SliceLiveData;
import com.android.settings.R;
import com.android.settings.homepage.contextualcards.CardContentProvider;
import com.android.settings.homepage.contextualcards.ContextualCard;
import com.android.settings.homepage.contextualcards.ContextualCardRenderer;
import com.android.settings.homepage.contextualcards.ControllerRendererPool;
import com.android.settings.homepage.contextualcards.slices.SliceFullCardRendererHelper;
import com.android.settings.homepage.contextualcards.slices.SliceHalfCardRendererHelper;
import com.android.settingslib.utils.ThreadUtils;
import java.util.Map;
import java.util.Set;

public class SliceContextualCardRenderer implements ContextualCardRenderer, LifecycleObserver {
    /* access modifiers changed from: private */
    public final Context mContext;
    /* access modifiers changed from: private */
    public final ControllerRendererPool mControllerRendererPool;
    final Set<RecyclerView.ViewHolder> mFlippedCardSet;
    private final SliceFullCardRendererHelper mFullCardHelper;
    private final SliceHalfCardRendererHelper mHalfCardHelper;
    private final LifecycleOwner mLifecycleOwner;
    final Map<Uri, LiveData<Slice>> mSliceLiveDataMap = new ArrayMap();

    public SliceContextualCardRenderer(Context context, LifecycleOwner lifecycleOwner, ControllerRendererPool controllerRendererPool) {
        this.mContext = context;
        this.mLifecycleOwner = lifecycleOwner;
        this.mControllerRendererPool = controllerRendererPool;
        this.mFlippedCardSet = new ArraySet();
        lifecycleOwner.getLifecycle().addObserver(this);
        this.mFullCardHelper = new SliceFullCardRendererHelper(context);
        this.mHalfCardHelper = new SliceHalfCardRendererHelper(context);
    }

    public RecyclerView.ViewHolder createViewHolder(View view, int i) {
        if (i == R.layout.contextual_slice_half_tile) {
            return this.mHalfCardHelper.createViewHolder(view);
        }
        return this.mFullCardHelper.createViewHolder(view);
    }

    public void bindView(RecyclerView.ViewHolder viewHolder, ContextualCard contextualCard) {
        Uri sliceUri = contextualCard.getSliceUri();
        if (!"content".equals(sliceUri.getScheme())) {
            Log.w("SliceCardRenderer", "Invalid uri, skipping slice: " + sliceUri);
            return;
        }
        if (viewHolder.getItemViewType() != R.layout.contextual_slice_half_tile) {
            ((SliceFullCardRendererHelper.SliceViewHolder) viewHolder).sliceView.setSlice(contextualCard.getSlice());
        }
        LiveData<Slice> liveData = this.mSliceLiveDataMap.get(sliceUri);
        if (liveData == null) {
            liveData = SliceLiveData.fromUri(this.mContext, sliceUri, new SliceContextualCardRenderer$$ExternalSyntheticLambda3(this, sliceUri));
            this.mSliceLiveDataMap.put(sliceUri, liveData);
        }
        View findViewById = viewHolder.itemView.findViewById(R.id.dismissal_swipe_background);
        liveData.removeObservers(this.mLifecycleOwner);
        if (findViewById != null) {
            findViewById.setVisibility(8);
        }
        liveData.observe(this.mLifecycleOwner, new SliceContextualCardRenderer$$ExternalSyntheticLambda2(this, viewHolder, contextualCard, findViewById));
        if (viewHolder.getItemViewType() != R.layout.contextual_slice_sticky_tile) {
            initDismissalActions(viewHolder, contextualCard);
            if (contextualCard.isPendingDismiss()) {
                showDismissalView(viewHolder);
                this.mFlippedCardSet.add(viewHolder);
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$bindView$1(Uri uri, int i, Throwable th) {
        Log.w("SliceCardRenderer", "Slice may be null. uri = " + uri + ", error = " + i);
        ThreadUtils.postOnMainThread(new SliceContextualCardRenderer$$ExternalSyntheticLambda4(this, uri));
        this.mContext.getContentResolver().notifyChange(CardContentProvider.REFRESH_CARD_URI, (ContentObserver) null);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$bindView$0(Uri uri) {
        this.mSliceLiveDataMap.get(uri).removeObservers(this.mLifecycleOwner);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$bindView$2(RecyclerView.ViewHolder viewHolder, ContextualCard contextualCard, View view, Slice slice) {
        if (slice != null) {
            if (slice.hasHint("error")) {
                Log.w("SliceCardRenderer", "Slice has HINT_ERROR, skipping rendering. uri=" + slice.getUri());
                this.mSliceLiveDataMap.get(slice.getUri()).removeObservers(this.mLifecycleOwner);
                this.mContext.getContentResolver().notifyChange(CardContentProvider.REFRESH_CARD_URI, (ContentObserver) null);
                return;
            }
            if (viewHolder.getItemViewType() == R.layout.contextual_slice_half_tile) {
                this.mHalfCardHelper.bindView(viewHolder, contextualCard, slice);
            } else {
                this.mFullCardHelper.bindView(viewHolder, contextualCard, slice);
            }
            if (view != null) {
                view.setVisibility(0);
            }
        }
    }

    private void initDismissalActions(RecyclerView.ViewHolder viewHolder, final ContextualCard contextualCard) {
        ((Button) viewHolder.itemView.findViewById(R.id.keep)).setOnClickListener(new SliceContextualCardRenderer$$ExternalSyntheticLambda0(this, viewHolder));
        ((Button) viewHolder.itemView.findViewById(R.id.remove)).setOnClickListener(new SliceContextualCardRenderer$$ExternalSyntheticLambda1(this, contextualCard, viewHolder));
        ViewCompat.setAccessibilityDelegate(getInitialView(viewHolder), new AccessibilityDelegateCompat() {
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
                accessibilityNodeInfoCompat.addAction(1048576);
                accessibilityNodeInfoCompat.setDismissable(true);
            }

            public boolean performAccessibilityAction(View view, int i, Bundle bundle) {
                if (i == 1048576) {
                    SliceContextualCardRenderer.this.mControllerRendererPool.getController(SliceContextualCardRenderer.this.mContext, contextualCard.getCardType()).onDismissed(contextualCard);
                }
                return super.performAccessibilityAction(view, i, bundle);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initDismissalActions$3(RecyclerView.ViewHolder viewHolder, View view) {
        this.mFlippedCardSet.remove(viewHolder);
        lambda$onStop$5(viewHolder);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initDismissalActions$4(ContextualCard contextualCard, RecyclerView.ViewHolder viewHolder, View view) {
        this.mControllerRendererPool.getController(this.mContext, contextualCard.getCardType()).onDismissed(contextualCard);
        this.mFlippedCardSet.remove(viewHolder);
        lambda$onStop$5(viewHolder);
        this.mSliceLiveDataMap.get(contextualCard.getSliceUri()).removeObservers(this.mLifecycleOwner);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        this.mFlippedCardSet.forEach(new SliceContextualCardRenderer$$ExternalSyntheticLambda5(this));
        this.mFlippedCardSet.clear();
    }

    /* access modifiers changed from: private */
    /* renamed from: resetCardView */
    public void lambda$onStop$5(RecyclerView.ViewHolder viewHolder) {
        viewHolder.itemView.findViewById(R.id.dismissal_view).setVisibility(8);
        getInitialView(viewHolder).setVisibility(0);
    }

    private void showDismissalView(RecyclerView.ViewHolder viewHolder) {
        viewHolder.itemView.findViewById(R.id.dismissal_view).setVisibility(0);
        getInitialView(viewHolder).setVisibility(4);
    }

    private View getInitialView(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder.getItemViewType() == R.layout.contextual_slice_half_tile) {
            return ((SliceHalfCardRendererHelper.HalfCardViewHolder) viewHolder).content;
        }
        return ((SliceFullCardRendererHelper.SliceViewHolder) viewHolder).sliceView;
    }
}
