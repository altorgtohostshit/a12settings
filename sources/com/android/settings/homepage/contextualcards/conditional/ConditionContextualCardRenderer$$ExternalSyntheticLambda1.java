package com.android.settings.homepage.contextualcards.conditional;

import android.view.View;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;

public final /* synthetic */ class ConditionContextualCardRenderer$$ExternalSyntheticLambda1 implements View.OnClickListener {
    public final /* synthetic */ ConditionContextualCardRenderer f$0;
    public final /* synthetic */ MetricsFeatureProvider f$1;
    public final /* synthetic */ ConditionalContextualCard f$2;

    public /* synthetic */ ConditionContextualCardRenderer$$ExternalSyntheticLambda1(ConditionContextualCardRenderer conditionContextualCardRenderer, MetricsFeatureProvider metricsFeatureProvider, ConditionalContextualCard conditionalContextualCard) {
        this.f$0 = conditionContextualCardRenderer;
        this.f$1 = metricsFeatureProvider;
        this.f$2 = conditionalContextualCard;
    }

    public final void onClick(View view) {
        this.f$0.lambda$initializePrimaryClick$0(this.f$1, this.f$2, view);
    }
}
