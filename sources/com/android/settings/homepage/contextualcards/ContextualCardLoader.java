package com.android.settings.homepage.contextualcards;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import com.android.settings.R;
import com.android.settings.homepage.contextualcards.logging.ContextualCardLogUtils;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.slices.CustomSliceRegistry;
import com.android.settingslib.utils.AsyncLoaderCompat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ContextualCardLoader extends AsyncLoaderCompat<List<ContextualCard>> {
    static final String CONTEXTUAL_CARD_COUNT = "contextual_card_count";
    static final int DEFAULT_CARD_COUNT = 3;
    private final Context mContext;
    Uri mNotifyUri;
    private final ContentObserver mObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
        public void onChange(boolean z, Uri uri) {
            if (ContextualCardLoader.this.isStarted()) {
                ContextualCardLoader contextualCardLoader = ContextualCardLoader.this;
                contextualCardLoader.mNotifyUri = uri;
                contextualCardLoader.forceLoad();
            }
        }
    };

    public interface CardContentLoaderListener {
        void onFinishCardLoading(List<ContextualCard> list);
    }

    /* access modifiers changed from: protected */
    public void onDiscardResult(List<ContextualCard> list) {
    }

    ContextualCardLoader(Context context) {
        super(context);
        this.mContext = context.getApplicationContext();
    }

    /* access modifiers changed from: protected */
    public void onStartLoading() {
        super.onStartLoading();
        this.mNotifyUri = null;
        this.mContext.getContentResolver().registerContentObserver(CardContentProvider.REFRESH_CARD_URI, false, this.mObserver);
        this.mContext.getContentResolver().registerContentObserver(CardContentProvider.DELETE_CARD_URI, false, this.mObserver);
    }

    /* access modifiers changed from: protected */
    public void onStopLoading() {
        super.onStopLoading();
        this.mContext.getContentResolver().unregisterContentObserver(this.mObserver);
    }

    public List<ContextualCard> loadInBackground() {
        ArrayList arrayList = new ArrayList();
        if (this.mContext.getResources().getBoolean(R.bool.config_use_legacy_suggestion)) {
            Log.d("ContextualCardLoader", "Skipping - in legacy suggestion mode");
            return arrayList;
        }
        Cursor contextualCardsFromProvider = getContextualCardsFromProvider();
        try {
            if (contextualCardsFromProvider.getCount() > 0) {
                contextualCardsFromProvider.moveToFirst();
                while (!contextualCardsFromProvider.isAfterLast()) {
                    ContextualCard contextualCard = new ContextualCard(contextualCardsFromProvider);
                    if (isLargeCard(contextualCard)) {
                        arrayList.add(contextualCard.mutate().setIsLargeCard(true).build());
                    } else {
                        arrayList.add(contextualCard);
                    }
                    contextualCardsFromProvider.moveToNext();
                }
            }
            contextualCardsFromProvider.close();
            return getDisplayableCards(arrayList);
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        throw th;
    }

    /* access modifiers changed from: package-private */
    public List<ContextualCard> getDisplayableCards(List<ContextualCard> list) {
        List<ContextualCard> filterEligibleCards = filterEligibleCards(list);
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        int cardCount = getCardCount();
        filterEligibleCards.forEach(new ContextualCardLoader$$ExternalSyntheticLambda0(arrayList, cardCount, arrayList3));
        filterEligibleCards.forEach(new ContextualCardLoader$$ExternalSyntheticLambda1(arrayList2, cardCount - arrayList.size(), arrayList3));
        arrayList2.addAll(arrayList);
        if (!CardContentProvider.DELETE_CARD_URI.equals(this.mNotifyUri)) {
            FeatureFactory.getFactory(this.mContext).getMetricsFeatureProvider().action(this.mContext, 1664, ContextualCardLogUtils.buildCardListLog(arrayList3));
        }
        return arrayList2;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$getDisplayableCards$0(List list, int i, List list2, ContextualCard contextualCard) {
        if (contextualCard.getCategory() == 6) {
            if (list.size() < i) {
                list.add(contextualCard);
            } else {
                list2.add(contextualCard);
            }
        }
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$getDisplayableCards$1(List list, int i, List list2, ContextualCard contextualCard) {
        if (contextualCard.getCategory() != 6) {
            if (list.size() < i) {
                list.add(contextualCard);
            } else {
                list2.add(contextualCard);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public int getCardCount() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), CONTEXTUAL_CARD_COUNT, 3);
    }

    /* access modifiers changed from: package-private */
    public Cursor getContextualCardsFromProvider() {
        return FeatureFactory.getFactory(this.mContext).getContextualCardFeatureProvider(this.mContext).getContextualCards();
    }

    /* access modifiers changed from: package-private */
    public List<ContextualCard> filterEligibleCards(List<ContextualCard> list) {
        if (list.isEmpty()) {
            return list;
        }
        ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(list.size());
        ArrayList arrayList = new ArrayList();
        List arrayList2 = new ArrayList();
        try {
            arrayList2 = newFixedThreadPool.invokeAll((List) list.stream().map(new ContextualCardLoader$$ExternalSyntheticLambda2(this)).collect(Collectors.toList()), 400, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Log.w("ContextualCardLoader", "Failed to get eligible states for all cards", e);
        }
        newFixedThreadPool.shutdown();
        for (int i = 0; i < arrayList2.size(); i++) {
            Future future = (Future) arrayList2.get(i);
            if (future.isCancelled()) {
                Log.w("ContextualCardLoader", "Timeout getting eligible state for card: " + list.get(i).getSliceUri());
            } else {
                try {
                    ContextualCard contextualCard = (ContextualCard) future.get();
                    if (contextualCard != null) {
                        arrayList.add(contextualCard);
                    }
                } catch (Exception e2) {
                    Log.w("ContextualCardLoader", "Failed to get eligible state for card", e2);
                }
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ EligibleCardChecker lambda$filterEligibleCards$2(ContextualCard contextualCard) {
        return new EligibleCardChecker(this.mContext, contextualCard);
    }

    private boolean isLargeCard(ContextualCard contextualCard) {
        return contextualCard.getSliceUri().equals(CustomSliceRegistry.CONTEXTUAL_WIFI_SLICE_URI) || contextualCard.getSliceUri().equals(CustomSliceRegistry.BLUETOOTH_DEVICES_SLICE_URI);
    }
}
