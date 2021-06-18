package com.android.settings.homepage.contextualcards;

import com.android.settings.homepage.contextualcards.ContextualCardLookupTable;
import java.util.function.Predicate;

public final /* synthetic */ class ContextualCardLookupTable$$ExternalSyntheticLambda0 implements Predicate {
    public final /* synthetic */ int f$0;

    public /* synthetic */ ContextualCardLookupTable$$ExternalSyntheticLambda0(int i) {
        this.f$0 = i;
    }

    public final boolean test(Object obj) {
        return ContextualCardLookupTable.lambda$getCardRendererClassByViewType$0(this.f$0, (ContextualCardLookupTable.ControllerRendererMapping) obj);
    }
}
