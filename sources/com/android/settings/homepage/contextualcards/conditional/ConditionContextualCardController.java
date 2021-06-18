package com.android.settings.homepage.contextualcards.conditional;

import android.content.Context;
import android.util.ArrayMap;
import com.android.settings.R;
import com.android.settings.homepage.contextualcards.ContextualCard;
import com.android.settings.homepage.contextualcards.ContextualCardController;
import com.android.settings.homepage.contextualcards.ContextualCardUpdateListener;
import com.android.settings.homepage.contextualcards.conditional.ConditionFooterContextualCard;
import com.android.settings.homepage.contextualcards.conditional.ConditionHeaderContextualCard;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConditionContextualCardController implements ContextualCardController, ConditionListener, LifecycleObserver, OnStart, OnStop {
    private final ConditionManager mConditionManager;
    private final Context mContext;
    private boolean mIsExpanded;
    private ContextualCardUpdateListener mListener;

    public void onDismissed(ContextualCard contextualCard) {
    }

    public ConditionContextualCardController(Context context) {
        this.mContext = context;
        ConditionManager conditionManager = new ConditionManager(context.getApplicationContext(), this);
        this.mConditionManager = conditionManager;
        conditionManager.startMonitoringStateChange();
    }

    public void setIsExpanded(boolean z) {
        this.mIsExpanded = z;
    }

    public void setCardUpdateListener(ContextualCardUpdateListener contextualCardUpdateListener) {
        this.mListener = contextualCardUpdateListener;
    }

    public void onStart() {
        this.mConditionManager.startMonitoringStateChange();
    }

    public void onStop() {
        this.mConditionManager.stopMonitoringStateChange();
    }

    public void onPrimaryClick(ContextualCard contextualCard) {
        this.mConditionManager.onPrimaryClick(this.mContext, ((ConditionalContextualCard) contextualCard).getConditionId());
    }

    public void onActionClick(ContextualCard contextualCard) {
        this.mConditionManager.onActionClick(((ConditionalContextualCard) contextualCard).getConditionId());
    }

    public void onConditionsChanged() {
        if (this.mListener != null) {
            this.mListener.onContextualCardUpdated(buildConditionalCardsWithFooterOrHeader(this.mConditionManager.getDisplayableCards()));
        }
    }

    /* access modifiers changed from: package-private */
    public Map<Integer, List<ContextualCard>> buildConditionalCardsWithFooterOrHeader(List<ContextualCard> list) {
        ArrayMap arrayMap = new ArrayMap();
        arrayMap.put(3, getExpandedConditionalCards(list));
        arrayMap.put(5, getConditionalFooterCard(list));
        arrayMap.put(4, getConditionalHeaderCard(list));
        return arrayMap;
    }

    private List<ContextualCard> getExpandedConditionalCards(List<ContextualCard> list) {
        if (list.isEmpty() || (list.size() > 0 && !this.mIsExpanded)) {
            return Collections.EMPTY_LIST;
        }
        List<ContextualCard> list2 = (List) list.stream().collect(Collectors.toList());
        if (list2.size() % 2 == 1) {
            int size = list2.size() - 1;
            list2.set(size, ((ConditionalContextualCard) list2.get(size)).mutate().setViewType(R.layout.conditional_card_full_tile).build());
        }
        return list2;
    }

    private List<ContextualCard> getConditionalFooterCard(List<ContextualCard> list) {
        if (list.isEmpty() || !this.mIsExpanded || list.size() <= 0) {
            return Collections.EMPTY_LIST;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ConditionFooterContextualCard.Builder().setName("condition_footer").setRankingScore(-99999.0d).setViewType(R.layout.conditional_card_footer).build());
        return arrayList;
    }

    private List<ContextualCard> getConditionalHeaderCard(List<ContextualCard> list) {
        if (list.isEmpty() || this.mIsExpanded || list.size() <= 0) {
            return Collections.EMPTY_LIST;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ConditionHeaderContextualCard.Builder().setConditionalCards(list).setName("condition_header").setRankingScore(-99999.0d).setViewType(R.layout.conditional_card_header).build());
        return arrayList;
    }
}
