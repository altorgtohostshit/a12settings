package com.android.settings.notification.zen;

import android.content.Context;
import android.content.pm.ParceledListSlice;
import android.icu.text.MessageFormat;
import android.os.AsyncTask;
import android.service.notification.ConversationChannelWrapper;
import android.view.View;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.notification.NotificationBackend;
import com.android.settings.notification.app.ConversationListSettings;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.widget.RadioButtonPreference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ZenModePriorityConversationsPreferenceController extends AbstractZenModePreferenceController {
    static final String KEY_ALL = "conversations_all";
    static final String KEY_IMPORTANT = "conversations_important";
    static final String KEY_NONE = "conversations_none";
    private View.OnClickListener mConversationSettingsWidgetClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            new SubSettingLauncher(ZenModePriorityConversationsPreferenceController.this.mPreferenceScreenContext).setDestination(ConversationListSettings.class.getName()).setSourceMetricsCategory(1837).launch();
        }
    };
    /* access modifiers changed from: private */
    public final NotificationBackend mNotificationBackend;
    /* access modifiers changed from: private */
    public int mNumConversations = -1;
    /* access modifiers changed from: private */
    public int mNumImportantConversations = -1;
    /* access modifiers changed from: private */
    public PreferenceCategory mPreferenceCategory;
    /* access modifiers changed from: private */
    public Context mPreferenceScreenContext;
    private RadioButtonPreference.OnClickListener mRadioButtonClickListener = new RadioButtonPreference.OnClickListener() {
        public void onRadioButtonClicked(RadioButtonPreference radioButtonPreference) {
            int access$600 = ZenModePriorityConversationsPreferenceController.keyToSetting(radioButtonPreference.getKey());
            if (access$600 != ZenModePriorityConversationsPreferenceController.this.mBackend.getPriorityConversationSenders()) {
                ZenModePriorityConversationsPreferenceController.this.mBackend.saveConversationSenders(access$600);
            }
        }
    };
    private List<RadioButtonPreference> mRadioButtonPreferences = new ArrayList();

    public boolean isAvailable() {
        return true;
    }

    public ZenModePriorityConversationsPreferenceController(Context context, String str, Lifecycle lifecycle, NotificationBackend notificationBackend) {
        super(context, str, lifecycle);
        this.mNotificationBackend = notificationBackend;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        this.mPreferenceScreenContext = preferenceScreen.getContext();
        PreferenceCategory preferenceCategory = (PreferenceCategory) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreferenceCategory = preferenceCategory;
        if (preferenceCategory.findPreference(KEY_ALL) == null) {
            makeRadioPreference(KEY_ALL, R.string.zen_mode_from_all_conversations);
            makeRadioPreference(KEY_IMPORTANT, R.string.zen_mode_from_important_conversations);
            makeRadioPreference(KEY_NONE, R.string.zen_mode_from_no_conversations);
            updateChannelCounts();
        }
        super.displayPreference(preferenceScreen);
    }

    public void onResume() {
        super.onResume();
        updateChannelCounts();
    }

    public String getPreferenceKey() {
        return this.KEY;
    }

    public void updateState(Preference preference) {
        int priorityConversationSenders = this.mBackend.getPriorityConversationSenders();
        for (RadioButtonPreference next : this.mRadioButtonPreferences) {
            next.setChecked(keyToSetting(next.getKey()) == priorityConversationSenders);
            next.setSummary((CharSequence) getSummary(next.getKey()));
        }
    }

    /* access modifiers changed from: private */
    public static int keyToSetting(String str) {
        str.hashCode();
        if (!str.equals(KEY_IMPORTANT)) {
            return !str.equals(KEY_ALL) ? 3 : 1;
        }
        return 2;
    }

    private String getSummary(String str) {
        int i;
        if (KEY_ALL.equals(str)) {
            i = this.mNumConversations;
        } else if (!KEY_IMPORTANT.equals(str)) {
            return null;
        } else {
            i = this.mNumImportantConversations;
        }
        if (i == -1) {
            return null;
        }
        MessageFormat messageFormat = new MessageFormat(this.mContext.getString(R.string.zen_mode_conversations_count), Locale.getDefault());
        HashMap hashMap = new HashMap();
        hashMap.put("count", Integer.valueOf(i));
        return messageFormat.format(hashMap);
    }

    private void updateChannelCounts() {
        new AsyncTask<Void, Void, Void>() {
            /* access modifiers changed from: protected */
            public Void doInBackground(Void... voidArr) {
                int i;
                int i2 = 0;
                ParceledListSlice<ConversationChannelWrapper> conversations = ZenModePriorityConversationsPreferenceController.this.mNotificationBackend.getConversations(false);
                if (conversations != null) {
                    i = 0;
                    for (ConversationChannelWrapper notificationChannel : conversations.getList()) {
                        if (!notificationChannel.getNotificationChannel().isDemoted()) {
                            i++;
                        }
                    }
                } else {
                    i = 0;
                }
                int unused = ZenModePriorityConversationsPreferenceController.this.mNumConversations = i;
                ParceledListSlice<ConversationChannelWrapper> conversations2 = ZenModePriorityConversationsPreferenceController.this.mNotificationBackend.getConversations(true);
                if (conversations2 != null) {
                    for (ConversationChannelWrapper notificationChannel2 : conversations2.getList()) {
                        if (!notificationChannel2.getNotificationChannel().isDemoted()) {
                            i2++;
                        }
                    }
                }
                int unused2 = ZenModePriorityConversationsPreferenceController.this.mNumImportantConversations = i2;
                return null;
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(Void voidR) {
                if (ZenModePriorityConversationsPreferenceController.this.mContext != null) {
                    ZenModePriorityConversationsPreferenceController zenModePriorityConversationsPreferenceController = ZenModePriorityConversationsPreferenceController.this;
                    zenModePriorityConversationsPreferenceController.updateState(zenModePriorityConversationsPreferenceController.mPreferenceCategory);
                }
            }
        }.execute(new Void[0]);
    }

    private RadioButtonPreference makeRadioPreference(String str, int i) {
        RadioButtonPreference radioButtonPreference = new RadioButtonPreference(this.mPreferenceCategory.getContext());
        if (KEY_ALL.equals(str) || KEY_IMPORTANT.equals(str)) {
            radioButtonPreference.setExtraWidgetOnClickListener(this.mConversationSettingsWidgetClickListener);
        }
        radioButtonPreference.setKey(str);
        radioButtonPreference.setTitle(i);
        radioButtonPreference.setOnClickListener(this.mRadioButtonClickListener);
        this.mPreferenceCategory.addPreference(radioButtonPreference);
        this.mRadioButtonPreferences.add(radioButtonPreference);
        return radioButtonPreference;
    }
}
