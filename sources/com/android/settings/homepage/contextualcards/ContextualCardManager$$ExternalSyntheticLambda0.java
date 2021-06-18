package com.android.settings.homepage.contextualcards;

import java.util.Comparator;

public final /* synthetic */ class ContextualCardManager$$ExternalSyntheticLambda0 implements Comparator {
    public static final /* synthetic */ ContextualCardManager$$ExternalSyntheticLambda0 INSTANCE = new ContextualCardManager$$ExternalSyntheticLambda0();

    private /* synthetic */ ContextualCardManager$$ExternalSyntheticLambda0() {
    }

    public final int compare(Object obj, Object obj2) {
        return Double.compare(((ContextualCard) obj2).getRankingScore(), ((ContextualCard) obj).getRankingScore());
    }
}
