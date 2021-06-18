package com.android.settings.homepage.contextualcards;

import java.util.List;
import java.util.function.Consumer;

public final /* synthetic */ class ContextualCardLoader$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ List f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ List f$2;

    public /* synthetic */ ContextualCardLoader$$ExternalSyntheticLambda1(List list, int i, List list2) {
        this.f$0 = list;
        this.f$1 = i;
        this.f$2 = list2;
    }

    public final void accept(Object obj) {
        ContextualCardLoader.lambda$getDisplayableCards$1(this.f$0, this.f$1, this.f$2, (ContextualCard) obj);
    }
}
