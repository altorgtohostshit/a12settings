package com.android.settings.homepage.contextualcards;

import java.util.Set;
import java.util.function.Predicate;

public final /* synthetic */ class ContextualCardManager$$ExternalSyntheticLambda6 implements Predicate {
    public final /* synthetic */ Set f$0;

    public /* synthetic */ ContextualCardManager$$ExternalSyntheticLambda6(Set set) {
        this.f$0 = set;
    }

    public final boolean test(Object obj) {
        return this.f$0.contains(Integer.valueOf(((ContextualCard) obj).getCardType()));
    }
}
