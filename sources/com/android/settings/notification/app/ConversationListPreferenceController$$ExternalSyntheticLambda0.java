package com.android.settings.notification.app;

import android.service.notification.ConversationChannelWrapper;
import androidx.preference.Preference;

public final /* synthetic */ class ConversationListPreferenceController$$ExternalSyntheticLambda0 implements Preference.OnPreferenceClickListener {
    public final /* synthetic */ ConversationListPreferenceController f$0;
    public final /* synthetic */ ConversationChannelWrapper f$1;
    public final /* synthetic */ Preference f$2;

    public /* synthetic */ ConversationListPreferenceController$$ExternalSyntheticLambda0(ConversationListPreferenceController conversationListPreferenceController, ConversationChannelWrapper conversationChannelWrapper, Preference preference) {
        this.f$0 = conversationListPreferenceController;
        this.f$1 = conversationChannelWrapper;
        this.f$2 = preference;
    }

    public final boolean onPreferenceClick(Preference preference) {
        return this.f$0.lambda$createConversationPref$0(this.f$1, this.f$2, preference);
    }
}
