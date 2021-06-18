package com.android.settings.homepage.contextualcards.conditional;

import android.text.TextUtils;
import com.android.settings.homepage.contextualcards.ContextualCard;
import java.util.List;
import java.util.Objects;

public class ConditionHeaderContextualCard extends ContextualCard {
    private final List<ContextualCard> mConditionalCards;

    public int getCardType() {
        return 4;
    }

    private ConditionHeaderContextualCard(Builder builder) {
        super((ContextualCard.Builder) builder);
        this.mConditionalCards = builder.mConditionalCards;
    }

    public List<ContextualCard> getConditionalCards() {
        return this.mConditionalCards;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{getName(), this.mConditionalCards});
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ConditionHeaderContextualCard)) {
            return false;
        }
        ConditionHeaderContextualCard conditionHeaderContextualCard = (ConditionHeaderContextualCard) obj;
        if (!TextUtils.equals(getName(), conditionHeaderContextualCard.getName()) || !this.mConditionalCards.equals(conditionHeaderContextualCard.mConditionalCards)) {
            return false;
        }
        return true;
    }

    public static class Builder extends ContextualCard.Builder {
        /* access modifiers changed from: private */
        public List<ContextualCard> mConditionalCards;

        public Builder setConditionalCards(List<ContextualCard> list) {
            this.mConditionalCards = list;
            return this;
        }

        public Builder setCardType(int i) {
            throw new IllegalArgumentException("Cannot change card type for " + Builder.class.getName());
        }

        public ConditionHeaderContextualCard build() {
            return new ConditionHeaderContextualCard(this);
        }
    }
}
