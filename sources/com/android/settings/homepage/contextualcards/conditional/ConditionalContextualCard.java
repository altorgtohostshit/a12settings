package com.android.settings.homepage.contextualcards.conditional;

import com.android.settings.homepage.contextualcards.ContextualCard;

public class ConditionalContextualCard extends ContextualCard {
    static final double UNSUPPORTED_RANKING_SCORE = -100.0d;
    private final CharSequence mActionText;
    private final long mConditionId;
    private final int mMetricsConstant;

    public int getCardType() {
        return 3;
    }

    private ConditionalContextualCard(Builder builder) {
        super((ContextualCard.Builder) builder);
        this.mConditionId = builder.mConditionId;
        this.mMetricsConstant = builder.mMetricsConstant;
        this.mActionText = builder.mActionText;
    }

    public long getConditionId() {
        return this.mConditionId;
    }

    public int getMetricsConstant() {
        return this.mMetricsConstant;
    }

    public CharSequence getActionText() {
        return this.mActionText;
    }

    public static class Builder extends ContextualCard.Builder {
        /* access modifiers changed from: private */
        public CharSequence mActionText;
        /* access modifiers changed from: private */
        public long mConditionId;
        /* access modifiers changed from: private */
        public int mMetricsConstant;

        public Builder setConditionId(long j) {
            this.mConditionId = j;
            return this;
        }

        public Builder setMetricsConstant(int i) {
            this.mMetricsConstant = i;
            return this;
        }

        public Builder setActionText(CharSequence charSequence) {
            this.mActionText = charSequence;
            return this;
        }

        public Builder setCardType(int i) {
            throw new IllegalArgumentException("Cannot change card type for " + Builder.class.getName());
        }

        public ConditionalContextualCard build() {
            setRankingScore(ConditionalContextualCard.UNSUPPORTED_RANKING_SCORE);
            return new ConditionalContextualCard(this);
        }
    }
}
