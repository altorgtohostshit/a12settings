package com.android.settings.homepage.contextualcards.conditional;

import android.view.View;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;

public final /* synthetic */ class ConditionHeaderContextualCardRenderer$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ ConditionHeaderContextualCardRenderer f$0;
    public final /* synthetic */ MetricsFeatureProvider f$1;

    public /* synthetic */ ConditionHeaderContextualCardRenderer$$ExternalSyntheticLambda0(ConditionHeaderContextualCardRenderer conditionHeaderContextualCardRenderer, MetricsFeatureProvider metricsFeatureProvider) {
        this.f$0 = conditionHeaderContextualCardRenderer;
        this.f$1 = metricsFeatureProvider;
    }

    public final void onClick(View view) {
        this.f$0.lambda$bindView$1(this.f$1, view);
    }
}
