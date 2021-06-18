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

public class PriorityConversationsPreferenceController extends ConversationListPreferenceController {
    /* access modifiers changed from: private */
    public List<ConversationChannelWrapper> mConversations;

    public String getPreferenceKey() {
        return "important_conversations";
    }

    public boolean isAvailable() {
        return true;
    }

    public PriorityConversationsPreferenceController(Context context, NotificationBackend notificationBackend) {
        super(context, notificationBackend);
    }

    /* access modifiers changed from: package-private */
    public Preference getSummaryPreference() {
        Preference preference = new Preference(this.mContext);
        preference.setOrder(1);
        preference.setSummary((int) R.string.important_conversations_summary_bubbles);
        return preference;
    }

    /* access modifiers changed from: package-private */
    public boolean matchesFilter(ConversationChannelWrapper conversationChannelWrapper) {
        return conversationChannelWrapper.getNotificationChannel().isImportantConversation();
    }

    public void updateState(Preference preference) {
        final PreferenceCategory preferenceCategory = (PreferenceCategory) preference;
        new AsyncTask<Void, Void, Void>() {
            /* access modifiers changed from: protected */
            public Void doInBackground(Void... voidArr) {
                PriorityConversationsPreferenceController priorityConversationsPreferenceController = PriorityConversationsPreferenceController.this;
                List unused = priorityConversationsPreferenceController.mConversations = priorityConversationsPreferenceController.mBackend.getConversations(true).getList();
                Collections.sort(PriorityConversationsPreferenceController.this.mConversations, PriorityConversationsPreferenceController.this.mConversationComparator);
                return null;
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(Void voidR) {
                if (PriorityConversationsPreferenceController.this.mContext != null) {
                    PriorityConversationsPreferenceController priorityConversationsPreferenceController = PriorityConversationsPreferenceController.this;
                    priorityConversationsPreferenceController.populateList(priorityConversationsPreferenceController.mConversations, preferenceCategory);
                }
            }
        }.execute(new Void[0]);
    }
}
