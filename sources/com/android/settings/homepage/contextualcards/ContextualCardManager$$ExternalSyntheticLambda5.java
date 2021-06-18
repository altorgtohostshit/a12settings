package com.android.settings.homepage.contextualcards;

import java.util.Set;
import java.util.function.Predicate;

public final /* synthetic */ class ContextualCardManager$$ExternalSyntheticLambda5 implements Predicate {
    public final /* synthetic */ Set f$0;

    public /* synthetic */ ContextualCardManager$$ExternalSyntheticLambda5(Set set) {
        this.f$0 = set;
    }

    public final boolean test(Object obj) {
        return ContextualCardManager.lambda$onContextualCardUpdated$3(this.f$0, (ContextualCard) obj);
    }
}
