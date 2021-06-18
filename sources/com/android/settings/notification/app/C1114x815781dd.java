package com.android.settings.notification.app;

import androidx.preference.PreferenceGroup;
import com.android.settings.notification.app.RecentConversationPreference;

/* renamed from: com.android.settings.notification.app.RecentConversationsPreferenceController$$ExternalSyntheticLambda2 */
public final /* synthetic */ class C1114x815781dd implements RecentConversationPreference.OnClearClickListener {
    public final /* synthetic */ RecentConversationsPreferenceController f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ String f$3;
    public final /* synthetic */ PreferenceGroup f$4;
    public final /* synthetic */ RecentConversationPreference f$5;

    public /* synthetic */ C1114x815781dd(RecentConversationsPreferenceController recentConversationsPreferenceController, String str, int i, String str2, PreferenceGroup preferenceGroup, RecentConversationPreference recentConversationPreference) {
        this.f$0 = recentConversationsPreferenceController;
        this.f$1 = str;
        this.f$2 = i;
        this.f$3 = str2;
        this.f$4 = preferenceGroup;
        this.f$5 = recentConversationPreference;
    }

    public final void onClear() {
        this.f$0.lambda$createConversationPref$1(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}
