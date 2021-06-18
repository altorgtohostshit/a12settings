package com.android.settings.homepage.contextualcards.conditional;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R;
import com.android.settings.homepage.contextualcards.ContextualCard;
import com.android.settings.homepage.contextualcards.ContextualCardRenderer;
import com.android.settings.homepage.contextualcards.ControllerRendererPool;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;

public class ConditionHeaderContextualCardRenderer implements ContextualCardRenderer {
    private final Context mContext;
    private final ControllerRendererPool mControllerRendererPool;

    public ConditionHeaderContextualCardRenderer(Context context, ControllerRendererPool controllerRendererPool) {
        this.mContext = context;
        this.mControllerRendererPool = controllerRendererPool;
    }

    public RecyclerView.ViewHolder createViewHolder(View view, int i) {
        return new ConditionHeaderCardHolder(view);
    }

    public void bindView(RecyclerView.ViewHolder viewHolder, ContextualCard contextualCard) {
        ConditionHeaderCardHolder conditionHeaderCardHolder = (ConditionHeaderCardHolder) viewHolder;
        MetricsFeatureProvider metricsFeatureProvider = FeatureFactory.getFactory(this.mContext).getMetricsFeatureProvider();
        conditionHeaderCardHolder.icons.removeAllViews();
        ((ConditionHeaderContextualCard) contextualCard).getConditionalCards().forEach(new ConditionHeaderContextualCardRenderer$$ExternalSyntheticLambda1(this, conditionHeaderCardHolder));
        conditionHeaderCardHolder.itemView.setOnClickListener(new ConditionHeaderContextualCardRenderer$$ExternalSyntheticLambda0(this, metricsFeatureProvider));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$bindView$0(ConditionHeaderCardHolder conditionHeaderCardHolder, ContextualCard contextualCard) {
        ImageView imageView = (ImageView) LayoutInflater.from(this.mContext).inflate(R.layout.conditional_card_header_icon, conditionHeaderCardHolder.icons, false);
        imageView.setImageDrawable(contextualCard.getIconDrawable());
        conditionHeaderCardHolder.icons.addView(imageView);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$bindView$1(MetricsFeatureProvider metricsFeatureProvider, View view) {
        metricsFeatureProvider.action(0, 373, 1502, (String) null, 1);
        ConditionContextualCardController conditionContextualCardController = (ConditionContextualCardController) this.mControllerRendererPool.getController(this.mContext, 4);
        conditionContextualCardController.setIsExpanded(true);
        conditionContextualCardController.onConditionsChanged();
    }

    public static class ConditionHeaderCardHolder extends RecyclerView.ViewHolder {
        public final ImageView expandIndicator;
        public final LinearLayout icons;

        public ConditionHeaderCardHolder(View view) {
            super(view);
            this.icons = (LinearLayout) view.findViewById(R.id.header_icons_container);
            this.expandIndicator = (ImageView) view.findViewById(R.id.expand_indicator);
        }
    }
}
