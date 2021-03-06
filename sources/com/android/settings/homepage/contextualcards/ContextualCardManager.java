package com.android.settings.homepage.contextualcards;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.util.ArrayMap;
import android.util.FeatureFlagUtils;
import android.util.Log;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment$$ExternalSyntheticLambda10;
import com.android.settings.homepage.contextualcards.ContextualCardLoader;
import com.android.settings.homepage.contextualcards.conditional.ConditionalCardController;
import com.android.settings.homepage.contextualcards.logging.ContextualCardLogUtils;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnSaveInstanceState;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ContextualCardManager implements ContextualCardLoader.CardContentLoaderListener, ContextualCardUpdateListener, LifecycleObserver, OnSaveInstanceState {
    static final long CARD_CONTENT_LOADER_TIMEOUT_MS = 1000;
    static final String KEY_CONTEXTUAL_CARDS = "key_contextual_cards";
    static final String KEY_GLOBAL_CARD_LOADER_TIMEOUT = "global_card_loader_timeout_key";
    private final Context mContext;
    final List<ContextualCard> mContextualCards = new ArrayList();
    final ControllerRendererPool mControllerRendererPool = new ControllerRendererPool();
    boolean mIsFirstLaunch;
    private final Lifecycle mLifecycle;
    private final List<LifecycleObserver> mLifecycleObservers = new ArrayList();
    private ContextualCardUpdateListener mListener;
    List<String> mSavedCards;
    long mStartTime;

    public ContextualCardManager(Context context, Lifecycle lifecycle, Bundle bundle) {
        this.mContext = context;
        this.mLifecycle = lifecycle;
        lifecycle.addObserver(this);
        if (bundle == null) {
            this.mIsFirstLaunch = true;
            this.mSavedCards = null;
        } else {
            this.mSavedCards = bundle.getStringArrayList(KEY_CONTEXTUAL_CARDS);
        }
        for (int i : getSettingsCards()) {
            setupController(i);
        }
    }

    /* access modifiers changed from: package-private */
    public void loadContextualCards(LoaderManager loaderManager, boolean z) {
        if (this.mContext.getResources().getBoolean(R.bool.config_use_legacy_suggestion)) {
            Log.w("ContextualCardManager", "Legacy suggestion contextual card enabled, skipping contextual cards.");
            return;
        }
        this.mStartTime = System.currentTimeMillis();
        CardContentLoaderCallbacks cardContentLoaderCallbacks = new CardContentLoaderCallbacks(this.mContext);
        cardContentLoaderCallbacks.setListener(this);
        if (!z) {
            loaderManager.initLoader(1, (Bundle) null, cardContentLoaderCallbacks);
            return;
        }
        this.mIsFirstLaunch = true;
        loaderManager.restartLoader(1, (Bundle) null, cardContentLoaderCallbacks);
    }

    private void loadCardControllers() {
        for (ContextualCard cardType : this.mContextualCards) {
            setupController(cardType.getCardType());
        }
    }

    /* access modifiers changed from: package-private */
    public int[] getSettingsCards() {
        if (FeatureFlagUtils.isEnabled(this.mContext, "settings_conditionals")) {
            return new int[]{3, 2};
        }
        return new int[]{2};
    }

    /* access modifiers changed from: package-private */
    public void setupController(int i) {
        ContextualCardController controller = this.mControllerRendererPool.getController(this.mContext, i);
        if (controller == null) {
            Log.w("ContextualCardManager", "Cannot find ContextualCardController for type " + i);
            return;
        }
        controller.setCardUpdateListener(this);
        if ((controller instanceof LifecycleObserver) && !this.mLifecycleObservers.contains(controller)) {
            LifecycleObserver lifecycleObserver = (LifecycleObserver) controller;
            this.mLifecycleObservers.add(lifecycleObserver);
            this.mLifecycle.addObserver(lifecycleObserver);
        }
    }

    /* access modifiers changed from: package-private */
    public List<ContextualCard> sortCards(List<ContextualCard> list) {
        List<ContextualCard> list2 = (List) list.stream().sorted(ContextualCardManager$$ExternalSyntheticLambda0.INSTANCE).collect(Collectors.toList());
        List list3 = (List) list2.stream().filter(ContextualCardManager$$ExternalSyntheticLambda7.INSTANCE).collect(Collectors.toList());
        list2.removeAll(list3);
        list2.addAll(list3);
        return list2;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$sortCards$1(ContextualCard contextualCard) {
        return contextualCard.getCategory() == 6;
    }

    public void onContextualCardUpdated(Map<Integer, List<ContextualCard>> map) {
        List list;
        Set<Integer> keySet = map.keySet();
        if (keySet.isEmpty()) {
            list = (List) this.mContextualCards.stream().filter(new ContextualCardManager$$ExternalSyntheticLambda6(new TreeSet<Integer>() {
                {
                    add(3);
                    add(4);
                    add(5);
                }
            })).collect(Collectors.toList());
        } else {
            list = (List) this.mContextualCards.stream().filter(new ContextualCardManager$$ExternalSyntheticLambda5(keySet)).collect(Collectors.toList());
        }
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(list);
        arrayList.addAll((Collection) map.values().stream().flatMap(DashboardFragment$$ExternalSyntheticLambda10.INSTANCE).collect(Collectors.toList()));
        this.mContextualCards.clear();
        this.mContextualCards.addAll(getCardsWithViewType(sortCards(arrayList)));
        loadCardControllers();
        if (this.mListener != null) {
            ArrayMap arrayMap = new ArrayMap();
            arrayMap.put(0, this.mContextualCards);
            this.mListener.onContextualCardUpdated(arrayMap);
        }
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$onContextualCardUpdated$3(Set set, ContextualCard contextualCard) {
        return !set.contains(Integer.valueOf(contextualCard.getCardType()));
    }

    public void onFinishCardLoading(List<ContextualCard> list) {
        long currentTimeMillis = System.currentTimeMillis() - this.mStartTime;
        Log.d("ContextualCardManager", "Total loading time = " + currentTimeMillis);
        List<ContextualCard> cardsToKeep = getCardsToKeep(list);
        MetricsFeatureProvider metricsFeatureProvider = FeatureFactory.getFactory(this.mContext).getMetricsFeatureProvider();
        if (!this.mIsFirstLaunch) {
            onContextualCardUpdated((Map) cardsToKeep.stream().collect(Collectors.groupingBy(ContextualCardManager$$ExternalSyntheticLambda1.INSTANCE)));
            metricsFeatureProvider.action(this.mContext, 1663, ContextualCardLogUtils.buildCardListLog(cardsToKeep));
            return;
        }
        if (currentTimeMillis <= getCardLoaderTimeout()) {
            onContextualCardUpdated((Map) list.stream().collect(Collectors.groupingBy(ContextualCardManager$$ExternalSyntheticLambda1.INSTANCE)));
            metricsFeatureProvider.action(this.mContext, 1663, ContextualCardLogUtils.buildCardListLog(list));
        } else {
            metricsFeatureProvider.action(0, 1685, 1502, (String) null, (int) currentTimeMillis);
        }
        metricsFeatureProvider.action(this.mContext, 1662, (int) (System.currentTimeMillis() - this.mStartTime));
        this.mIsFirstLaunch = false;
    }

    public void onSaveInstanceState(Bundle bundle) {
        bundle.putStringArrayList(KEY_CONTEXTUAL_CARDS, (ArrayList) this.mContextualCards.stream().map(ContextualCardManager$$ExternalSyntheticLambda2.INSTANCE).collect(Collectors.toCollection(ContextualCardManager$$ExternalSyntheticLambda8.INSTANCE)));
    }

    public void onWindowFocusChanged(boolean z) {
        boolean z2 = false;
        for (ContextualCard cardType : new ArrayList(this.mContextualCards)) {
            ContextualCardController controller = getControllerRendererPool().getController(this.mContext, cardType.getCardType());
            if (controller instanceof ConditionalCardController) {
                z2 = true;
            }
            if (z && (controller instanceof OnStart)) {
                ((OnStart) controller).onStart();
            }
            if (!z && (controller instanceof OnStop)) {
                ((OnStop) controller).onStop();
            }
        }
        if (!z2) {
            ContextualCardController controller2 = getControllerRendererPool().getController(this.mContext, 3);
            if (z && (controller2 instanceof OnStart)) {
                ((OnStart) controller2).onStart();
            }
            if (!z && (controller2 instanceof OnStop)) {
                ((OnStop) controller2).onStop();
            }
        }
    }

    public ControllerRendererPool getControllerRendererPool() {
        return this.mControllerRendererPool;
    }

    /* access modifiers changed from: package-private */
    public void setListener(ContextualCardUpdateListener contextualCardUpdateListener) {
        this.mListener = contextualCardUpdateListener;
    }

    /* access modifiers changed from: package-private */
    public List<ContextualCard> getCardsWithViewType(List<ContextualCard> list) {
        if (list.isEmpty()) {
            return list;
        }
        return getCardsWithSuggestionViewType(getCardsWithStickyViewType(list));
    }

    /* access modifiers changed from: package-private */
    public long getCardLoaderTimeout() {
        return Settings.Global.getLong(this.mContext.getContentResolver(), KEY_GLOBAL_CARD_LOADER_TIMEOUT, CARD_CONTENT_LOADER_TIMEOUT_MS);
    }

    private List<ContextualCard> getCardsWithSuggestionViewType(List<ContextualCard> list) {
        ArrayList arrayList = new ArrayList(list);
        int i = 1;
        while (i < arrayList.size()) {
            int i2 = i - 1;
            ContextualCard contextualCard = (ContextualCard) arrayList.get(i2);
            ContextualCard contextualCard2 = (ContextualCard) arrayList.get(i);
            if (contextualCard2.getCategory() == 1 && contextualCard.getCategory() == 1) {
                arrayList.set(i2, contextualCard.mutate().setViewType(R.layout.contextual_slice_half_tile).build());
                arrayList.set(i, contextualCard2.mutate().setViewType(R.layout.contextual_slice_half_tile).build());
                i++;
            }
            i++;
        }
        return arrayList;
    }

    private List<ContextualCard> getCardsWithStickyViewType(List<ContextualCard> list) {
        ArrayList arrayList = new ArrayList(list);
        for (int i = 0; i < arrayList.size(); i++) {
            ContextualCard contextualCard = list.get(i);
            if (contextualCard.getCategory() == 6) {
                arrayList.set(i, contextualCard.mutate().setViewType(R.layout.contextual_slice_sticky_tile).build());
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: package-private */
    public List<ContextualCard> getCardsToKeep(List<ContextualCard> list) {
        if (this.mSavedCards == null) {
            return (List) list.stream().filter(new ContextualCardManager$$ExternalSyntheticLambda3(this)).collect(Collectors.toList());
        }
        List<ContextualCard> list2 = (List) list.stream().filter(new ContextualCardManager$$ExternalSyntheticLambda4(this)).collect(Collectors.toList());
        this.mSavedCards = null;
        return list2;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getCardsToKeep$4(ContextualCard contextualCard) {
        return this.mSavedCards.contains(contextualCard.getName());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getCardsToKeep$5(ContextualCard contextualCard) {
        return this.mContextualCards.contains(contextualCard);
    }

    static class CardContentLoaderCallbacks implements LoaderManager.LoaderCallbacks<List<ContextualCard>> {
        private Context mContext;
        private ContextualCardLoader.CardContentLoaderListener mListener;

        public void onLoaderReset(Loader<List<ContextualCard>> loader) {
        }

        CardContentLoaderCallbacks(Context context) {
            this.mContext = context.getApplicationContext();
        }

        /* access modifiers changed from: protected */
        public void setListener(ContextualCardLoader.CardContentLoaderListener cardContentLoaderListener) {
            this.mListener = cardContentLoaderListener;
        }

        public Loader<List<ContextualCard>> onCreateLoader(int i, Bundle bundle) {
            if (i == 1) {
                return new ContextualCardLoader(this.mContext);
            }
            throw new IllegalArgumentException("Unknown loader id: " + i);
        }

        public void onLoadFinished(Loader<List<ContextualCard>> loader, List<ContextualCard> list) {
            ContextualCardLoader.CardContentLoaderListener cardContentLoaderListener = this.mListener;
            if (cardContentLoaderListener != null) {
                cardContentLoaderListener.onFinishCardLoading(list);
            }
        }
    }
}
