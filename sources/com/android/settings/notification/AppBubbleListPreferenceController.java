package com.android.settings.notification;

import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.content.Context;
import android.content.pm.ShortcutInfo;
import android.graphics.drawable.Drawable;
import android.service.notification.ConversationChannelWrapper;
import android.view.View;
import android.widget.ImageView;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
import com.android.settings.notification.NotificationBackend;
import com.android.settings.notification.app.AppConversationListPreferenceController;
import com.android.settingslib.RestrictedLockUtils;
import java.util.List;
import java.util.stream.Collectors;

public class AppBubbleListPreferenceController extends AppConversationListPreferenceController {
    public String getPreferenceKey() {
        return "bubble_conversations";
    }

    public AppBubbleListPreferenceController(Context context, NotificationBackend notificationBackend) {
        super(context, notificationBackend);
    }

    public void updateState(Preference preference) {
        preference.setVisible(false);
        super.updateState(preference);
    }

    public void onResume(NotificationBackend.AppRow appRow, NotificationChannel notificationChannel, NotificationChannelGroup notificationChannelGroup, Drawable drawable, ShortcutInfo shortcutInfo, RestrictedLockUtils.EnforcedAdmin enforcedAdmin, List<String> list) {
        super.onResume(appRow, notificationChannel, notificationChannelGroup, drawable, shortcutInfo, enforcedAdmin, list);
        loadConversationsAndPopulate();
    }

    public boolean isAvailable() {
        NotificationBackend.AppRow appRow = this.mAppRow;
        if (appRow == null || appRow.banned) {
            return false;
        }
        if ((this.mChannel == null || (!this.mBackend.onlyHasDefaultChannel(appRow.pkg, appRow.uid) && !"miscellaneous".equals(this.mChannel.getId()))) && this.mAppRow.bubblePreference != 0) {
            return true;
        }
        return false;
    }

    public List<ConversationChannelWrapper> filterAndSortConversations(List<ConversationChannelWrapper> list) {
        return (List) list.stream().sorted(this.mConversationComparator).filter(new AppBubbleListPreferenceController$$ExternalSyntheticLambda1(this)).collect(Collectors.toList());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$filterAndSortConversations$0(ConversationChannelWrapper conversationChannelWrapper) {
        int i = this.mAppRow.bubblePreference;
        if (i == 2) {
            return conversationChannelWrapper.getNotificationChannel().canBubble();
        }
        if (i == 1 && conversationChannelWrapper.getNotificationChannel().getAllowBubbles() == 0) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public int getTitleResId() {
        return this.mAppRow.bubblePreference == 2 ? R.string.bubble_app_setting_selected_conversation_title : R.string.bubble_app_setting_excluded_conversation_title;
    }

    public Preference createConversationPref(ConversationChannelWrapper conversationChannelWrapper) {
        ConversationPreference conversationPreference = new ConversationPreference(this.mContext);
        populateConversationPreference(conversationChannelWrapper, conversationPreference);
        boolean z = true;
        if (this.mAppRow.bubblePreference != 1) {
            z = false;
        }
        conversationPreference.setOnClickBubblesConversation(z);
        conversationPreference.setOnClickListener(new AppBubbleListPreferenceController$$ExternalSyntheticLambda0(this, conversationChannelWrapper, conversationPreference));
        return conversationPreference;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$createConversationPref$1(ConversationChannelWrapper conversationChannelWrapper, ConversationPreference conversationPreference, View view) {
        conversationChannelWrapper.getNotificationChannel().setAllowBubbles(-1);
        NotificationBackend notificationBackend = this.mBackend;
        NotificationBackend.AppRow appRow = this.mAppRow;
        notificationBackend.updateChannel(appRow.pkg, appRow.uid, conversationChannelWrapper.getNotificationChannel());
        this.mPreference.removePreference(conversationPreference);
        if (this.mPreference.getPreferenceCount() == 0) {
            this.mPreference.setVisible(false);
        }
    }

    /* access modifiers changed from: protected */
    public void populateList() {
        super.populateList();
        PreferenceCategory preferenceCategory = this.mPreference;
        if (preferenceCategory != null) {
            preferenceCategory.setVisible(preferenceCategory.getPreferenceCount() > 0);
        }
    }

    public static class ConversationPreference extends Preference implements View.OnClickListener {
        boolean mOnClickBubbles;
        View.OnClickListener mOnClickListener;

        ConversationPreference(Context context) {
            super(context);
            setWidgetLayoutResource(R.layout.bubble_conversation_remove_button);
        }

        public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
            String str;
            super.onBindViewHolder(preferenceViewHolder);
            ImageView imageView = (ImageView) preferenceViewHolder.itemView.findViewById(R.id.button);
            if (this.mOnClickBubbles) {
                str = getContext().getString(R.string.bubble_app_setting_bubble_conversation);
            } else {
                str = getContext().getString(R.string.bubble_app_setting_unbubble_conversation);
            }
            imageView.setContentDescription(str);
            imageView.setOnClickListener(this.mOnClickListener);
        }

        public void setOnClickBubblesConversation(boolean z) {
            this.mOnClickBubbles = z;
        }

        public void setOnClickListener(View.OnClickListener onClickListener) {
            this.mOnClickListener = onClickListener;
        }

        public void onClick(View view) {
            View.OnClickListener onClickListener = this.mOnClickListener;
            if (onClickListener != null) {
                onClickListener.onClick(view);
            }
        }
    }
}
