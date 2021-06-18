package com.android.settings.homepage.contextualcards.legacysuggestion;

import android.app.PendingIntent;
import android.service.settings.suggestions.Suggestion;
import com.android.settings.homepage.contextualcards.ContextualCard;

public class LegacySuggestionContextualCard extends ContextualCard {
    private final PendingIntent mPendingIntent;
    private final Suggestion mSuggestion;

    public int getCardType() {
        return 2;
    }

    public LegacySuggestionContextualCard(Builder builder) {
        super((ContextualCard.Builder) builder);
        this.mPendingIntent = builder.mPendingIntent;
        this.mSuggestion = builder.mSuggestion;
    }

    public PendingIntent getPendingIntent() {
        return this.mPendingIntent;
    }

    public Suggestion getSuggestion() {
        return this.mSuggestion;
    }

    public static class Builder extends ContextualCard.Builder {
        /* access modifiers changed from: private */
        public PendingIntent mPendingIntent;
        /* access modifiers changed from: private */
        public Suggestion mSuggestion;

        public Builder setPendingIntent(PendingIntent pendingIntent) {
            this.mPendingIntent = pendingIntent;
            return this;
        }

        public Builder setSuggestion(Suggestion suggestion) {
            this.mSuggestion = suggestion;
            return this;
        }

        public Builder setCardType(int i) {
            throw new IllegalArgumentException("Cannot change card type for " + Builder.class.getName());
        }

        public LegacySuggestionContextualCard build() {
            return new LegacySuggestionContextualCard(this);
        }
    }
}
