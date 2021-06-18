package com.android.settings.homepage.contextualcards.conditional;

import com.android.settings.homepage.contextualcards.ContextualCard;

public class ConditionFooterContextualCard extends ContextualCard {
    public int getCardType() {
        return 5;
    }

    private ConditionFooterContextualCard(Builder builder) {
        super((ContextualCard.Builder) builder);
    }

    public static class Builder extends ContextualCard.Builder {
        public Builder setCardType(int i) {
            throw new IllegalArgumentException("Cannot change card type for " + Builder.class.getName());
        }

        public ConditionFooterContextualCard build() {
            return new ConditionFooterContextualCard(this);
        }
    }
}
