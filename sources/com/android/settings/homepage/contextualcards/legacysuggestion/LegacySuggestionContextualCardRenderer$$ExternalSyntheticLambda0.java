package com.android.settings.homepage.contextualcards.legacysuggestion;

import android.view.View;
import com.android.settings.homepage.contextualcards.ContextualCard;
import com.android.settings.homepage.contextualcards.ContextualCardController;

public final /* synthetic */ class LegacySuggestionContextualCardRenderer$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ ContextualCardController f$0;
    public final /* synthetic */ ContextualCard f$1;

    public /* synthetic */ LegacySuggestionContextualCardRenderer$$ExternalSyntheticLambda0(ContextualCardController contextualCardController, ContextualCard contextualCard) {
        this.f$0 = contextualCardController;
        this.f$1 = contextualCard;
    }

    public final void onClick(View view) {
        this.f$0.onPrimaryClick(this.f$1);
    }
}
