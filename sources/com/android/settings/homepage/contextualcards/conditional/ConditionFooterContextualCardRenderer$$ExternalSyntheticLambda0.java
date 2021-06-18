package com.android.settings.homepage.contextualcards.conditional;

import android.view.View;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;

public final /* synthetic */ class ConditionFooterContextualCardRenderer$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ ConditionFooterContextualCardRenderer f$0;
    public final /* synthetic */ MetricsFeatureProvider f$1;

    public /* synthetic */ ConditionFooterContextualCardRenderer$$ExternalSyntheticLambda0(ConditionFooterContextualCardRenderer conditionFooterContextualCardRenderer, MetricsFeatureProvider metricsFeatureProvider) {
        this.f$0 = conditionFooterContextualCardRenderer;
        this.f$1 = metricsFeatureProvider;
    }

    public final void onClick(View view) {
        this.f$0.lambda$bindView$0(this.f$1, view);
    }
}
