package com.google.android.settings.dashboard.suggestions;

import android.service.settings.suggestions.Suggestion;
import android.view.View;

public final /* synthetic */ class ContextualSuggestionFragment$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ Suggestion f$0;

    public /* synthetic */ ContextualSuggestionFragment$$ExternalSyntheticLambda0(Suggestion suggestion) {
        this.f$0 = suggestion;
    }

    public final void onClick(View view) {
        ContextualSuggestionFragment.lambda$updateState$2(this.f$0, view);
    }
}
