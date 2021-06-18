package com.android.settings.notification;

import android.service.notification.ConversationChannelWrapper;
import android.view.View;
import com.android.settings.notification.AppBubbleListPreferenceController;

public final /* synthetic */ class AppBubbleListPreferenceController$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ AppBubbleListPreferenceController f$0;
    public final /* synthetic */ ConversationChannelWrapper f$1;
    public final /* synthetic */ AppBubbleListPreferenceController.ConversationPreference f$2;

    public /* synthetic */ AppBubbleListPreferenceController$$ExternalSyntheticLambda0(AppBubbleListPreferenceController appBubbleListPreferenceController, ConversationChannelWrapper conversationChannelWrapper, AppBubbleListPreferenceController.ConversationPreference conversationPreference) {
        this.f$0 = appBubbleListPreferenceController;
        this.f$1 = conversationChannelWrapper;
        this.f$2 = conversationPreference;
    }

    public final void onClick(View view) {
        this.f$0.lambda$createConversationPref$1(this.f$1, this.f$2, view);
    }
}
