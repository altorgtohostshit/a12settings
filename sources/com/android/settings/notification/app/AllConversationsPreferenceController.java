package com.android.settings.notification.app;

import android.content.Context;
import android.os.AsyncTask;
import android.service.notification.ConversationChannelWrapper;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import com.android.settings.R;
import com.android.settings.notification.NotificationBackend;
import java.util.Collections;
import java.util.List;

public class AllConversationsPreferenceController extends ConversationListPreferenceController {
    /* access modifiers changed from: private */
    public List<ConversationChannelWrapper> mConversations;

    public String getPreferenceKey() {
        return "other_conversations";
    }

    public boolean isAvailable() {
        return true;
    }

    public AllConversationsPreferenceController(Context context, NotificationBackend notificationBackend) {
        super(context, notificationBackend);
    }

    /* access modifiers changed from: package-private */
    public Preference getSummaryPreference() {
        Preference preference = new Preference(this.mContext);
        preference.setOrder(1);
        preference.setSummary((int) R.string.other_conversations_summary);
        preference.setSelectable(false);
        return preference;
    }

    /* access modifiers changed from: package-private */
    public boolean matchesFilter(ConversationChannelWrapper conversationChannelWrapper) {
        return !conversationChannelWrapper.getNotificationChannel().isImportantConversation();
    }

    public void updateState(Preference preference) {
        final PreferenceCategory preferenceCategory = (PreferenceCategory) preference;
        new AsyncTask<Void, Void, Void>() {
            /* access modifiers changed from: protected */
            public Void doInBackground(Void... voidArr) {
                AllConversationsPreferenceController allConversationsPreferenceController = AllConversationsPreferenceController.this;
                List unused = allConversationsPreferenceController.mConversations = allConversationsPreferenceController.mBackend.getConversations(false).getList();
                Collections.sort(AllConversationsPreferenceController.this.mConversations, AllConversationsPreferenceController.this.mConversationComparator);
                return null;
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(Void voidR) {
                if (AllConversationsPreferenceController.this.mContext != null) {
                    AllConversationsPreferenceController allConversationsPreferenceController = AllConversationsPreferenceController.this;
                    allConversationsPreferenceController.populateList(allConversationsPreferenceController.mConversations, preferenceCategory);
                }
            }
        }.execute(new Void[0]);
    }
}
