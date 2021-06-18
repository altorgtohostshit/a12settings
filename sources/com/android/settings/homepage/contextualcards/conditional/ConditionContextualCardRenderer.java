package com.android.settings.homepage.contextualcards.conditional;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R;
import com.android.settings.homepage.contextualcards.ContextualCard;
import com.android.settings.homepage.contextualcards.ContextualCardRenderer;
import com.android.settings.homepage.contextualcards.ControllerRendererPool;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;

public class ConditionContextualCardRenderer implements ContextualCardRenderer {
    private final Context mContext;
    private final ControllerRendererPool mControllerRendererPool;

    public ConditionContextualCardRenderer(Context context, ControllerRendererPool controllerRendererPool) {
        this.mContext = context;
        this.mControllerRendererPool = controllerRendererPool;
    }

    public RecyclerView.ViewHolder createViewHolder(View view, int i) {
        return new ConditionalCardHolder(view);
    }

    public void bindView(RecyclerView.ViewHolder viewHolder, ContextualCard contextualCard) {
        ConditionalCardHolder conditionalCardHolder = (ConditionalCardHolder) viewHolder;
        ConditionalContextualCard conditionalContextualCard = (ConditionalContextualCard) contextualCard;
        MetricsFeatureProvider metricsFeatureProvider = FeatureFactory.getFactory(this.mContext).getMetricsFeatureProvider();
        metricsFeatureProvider.visible(this.mContext, 1502, conditionalContextualCard.getMetricsConstant(), 0);
        initializePrimaryClick(conditionalCardHolder, conditionalContextualCard, metricsFeatureProvider);
        initializeView(conditionalCardHolder, conditionalContextualCard);
        initializeActionButton(conditionalCardHolder, conditionalContextualCard, metricsFeatureProvider);
    }

    private void initializePrimaryClick(ConditionalCardHolder conditionalCardHolder, ConditionalContextualCard conditionalContextualCard, MetricsFeatureProvider metricsFeatureProvider) {
        conditionalCardHolder.itemView.findViewById(R.id.content).setOnClickListener(new ConditionContextualCardRenderer$$ExternalSyntheticLambda1(this, metricsFeatureProvider, conditionalContextualCard));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initializePrimaryClick$0(MetricsFeatureProvider metricsFeatureProvider, ConditionalContextualCard conditionalContextualCard, View view) {
        metricsFeatureProvider.action(this.mContext, 375, conditionalContextualCard.getMetricsConstant());
        this.mControllerRendererPool.getController(this.mContext, conditionalContextualCard.getCardType()).onPrimaryClick(conditionalContextualCard);
    }

    private void initializeView(ConditionalCardHolder conditionalCardHolder, ConditionalContextualCard conditionalContextualCard) {
        conditionalCardHolder.icon.setImageDrawable(conditionalContextualCard.getIconDrawable());
        conditionalCardHolder.title.setText(conditionalContextualCard.getTitleText());
        conditionalCardHolder.summary.setText(conditionalContextualCard.getSummaryText());
    }

    private void initializeActionButton(ConditionalCardHolder conditionalCardHolder, ConditionalContextualCard conditionalContextualCard, MetricsFeatureProvider metricsFeatureProvider) {
        CharSequence actionText = conditionalContextualCard.getActionText();
        boolean z = !TextUtils.isEmpty(actionText);
        Button button = (Button) conditionalCardHolder.itemView.findViewById(R.id.first_action);
        if (z) {
            button.setVisibility(0);
            button.setText(actionText);
            button.setOnClickListener(new ConditionContextualCardRenderer$$ExternalSyntheticLambda0(this, metricsFeatureProvider, conditionalContextualCard));
            return;
        }
        button.setVisibility(8);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initializeActionButton$1(MetricsFeatureProvider metricsFeatureProvider, ConditionalContextualCard conditionalContextualCard, View view) {
        metricsFeatureProvider.action(view.getContext(), 376, conditionalContextualCard.getMetricsConstant());
        this.mControllerRendererPool.getController(this.mContext, conditionalContextualCard.getCardType()).onActionClick(conditionalContextualCard);
    }

    public static class ConditionalCardHolder extends RecyclerView.ViewHolder {
        public final ImageView icon;
        public final TextView summary;
        public final TextView title;

        public ConditionalCardHolder(View view) {
            super(view);
            this.icon = (ImageView) view.findViewById(16908294);
            this.title = (TextView) view.findViewById(16908310);
            this.summary = (TextView) view.findViewById(16908304);
        }
    }
}
