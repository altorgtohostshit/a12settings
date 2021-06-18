package com.android.settings.homepage.contextualcards;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import androidx.slice.Slice;
import androidx.slice.SliceMetadata;
import androidx.slice.SliceViewManager;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.utils.ThreadUtils;
import java.util.concurrent.Callable;

public class EligibleCardChecker implements Callable<ContextualCard> {
    ContextualCard mCard;
    private final Context mContext;

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$bindSlice$0(Slice slice) {
    }

    EligibleCardChecker(Context context, ContextualCard contextualCard) {
        this.mContext = context;
        this.mCard = contextualCard;
    }

    public ContextualCard call() {
        ContextualCard contextualCard;
        long currentTimeMillis = System.currentTimeMillis();
        MetricsFeatureProvider metricsFeatureProvider = FeatureFactory.getFactory(this.mContext).getMetricsFeatureProvider();
        if (isCardEligibleToDisplay(this.mCard)) {
            metricsFeatureProvider.action(0, 1686, 1502, this.mCard.getTextSliceUri(), 1);
            contextualCard = this.mCard;
        } else {
            metricsFeatureProvider.action(0, 1686, 1502, this.mCard.getTextSliceUri(), 0);
            contextualCard = null;
        }
        ContextualCard contextualCard2 = contextualCard;
        metricsFeatureProvider.action(0, 1684, 1502, this.mCard.getTextSliceUri(), (int) (System.currentTimeMillis() - currentTimeMillis));
        return contextualCard2;
    }

    /* access modifiers changed from: package-private */
    public boolean isCardEligibleToDisplay(ContextualCard contextualCard) {
        if (contextualCard.getRankingScore() < 0.0d) {
            return false;
        }
        Uri sliceUri = contextualCard.getSliceUri();
        if (!"content".equals(sliceUri.getScheme())) {
            return false;
        }
        Slice bindSlice = bindSlice(sliceUri);
        if (bindSlice == null || bindSlice.hasHint("error")) {
            Log.w("EligibleCardChecker", "Failed to bind slice, not eligible for display " + sliceUri);
            return false;
        }
        this.mCard = contextualCard.mutate().setSlice(bindSlice).build();
        if (isSliceToggleable(bindSlice)) {
            this.mCard = contextualCard.mutate().setHasInlineAction(true).build();
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public Slice bindSlice(Uri uri) {
        SliceViewManager instance = SliceViewManager.getInstance(this.mContext);
        EligibleCardChecker$$ExternalSyntheticLambda0 eligibleCardChecker$$ExternalSyntheticLambda0 = EligibleCardChecker$$ExternalSyntheticLambda0.INSTANCE;
        instance.registerSliceCallback(uri, eligibleCardChecker$$ExternalSyntheticLambda0);
        Slice bindSlice = instance.bindSlice(uri);
        ThreadUtils.postOnMainThread(new EligibleCardChecker$$ExternalSyntheticLambda2(instance, uri, eligibleCardChecker$$ExternalSyntheticLambda0));
        return bindSlice;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$bindSlice$1(SliceViewManager sliceViewManager, Uri uri, SliceViewManager.SliceCallback sliceCallback) {
        try {
            sliceViewManager.unregisterSliceCallback(uri, sliceCallback);
        } catch (SecurityException e) {
            Log.d("EligibleCardChecker", "No permission currently: " + e);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isSliceToggleable(Slice slice) {
        return !SliceMetadata.from(this.mContext, slice).getToggles().isEmpty();
    }
}
