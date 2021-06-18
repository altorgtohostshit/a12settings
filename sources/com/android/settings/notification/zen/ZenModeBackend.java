package com.android.settings.notification.zen;

import android.app.ActivityManager;
import android.app.AutomaticZenRule;
import android.app.NotificationManager;
import android.content.Context;
import android.database.Cursor;
import android.icu.text.MessageFormat;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.service.notification.ZenModeConfig;
import android.service.notification.ZenPolicy;
import android.util.Log;
import com.android.settings.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ZenModeBackend {
    public static final Comparator<Map.Entry<String, AutomaticZenRule>> RULE_COMPARATOR = new Comparator<Map.Entry<String, AutomaticZenRule>>() {
        public int compare(Map.Entry<String, AutomaticZenRule> entry, Map.Entry<String, AutomaticZenRule> entry2) {
            boolean contains = ZenModeBackend.getDefaultRuleIds().contains(entry.getKey());
            if (contains != ZenModeBackend.getDefaultRuleIds().contains(entry2.getKey())) {
                return contains ? -1 : 1;
            }
            int compare = Long.compare(entry.getValue().getCreationTime(), entry2.getValue().getCreationTime());
            if (compare != 0) {
                return compare;
            }
            return key(entry.getValue()).compareTo(key(entry2.getValue()));
        }

        private String key(AutomaticZenRule automaticZenRule) {
            int i;
            if (ZenModeConfig.isValidScheduleConditionId(automaticZenRule.getConditionId())) {
                i = 1;
            } else {
                i = ZenModeConfig.isValidEventConditionId(automaticZenRule.getConditionId()) ? 2 : 3;
            }
            return i + automaticZenRule.getName().toString();
        }
    };
    protected static final String ZEN_MODE_FROM_ANYONE = "zen_mode_from_anyone";
    protected static final String ZEN_MODE_FROM_CONTACTS = "zen_mode_from_contacts";
    protected static final String ZEN_MODE_FROM_NONE = "zen_mode_from_none";
    protected static final String ZEN_MODE_FROM_STARRED = "zen_mode_from_starred";
    private static List<String> mDefaultRuleIds;
    private static ZenModeBackend sInstance;
    private String TAG = "ZenModeSettingsBackend";
    private final Context mContext;
    private final NotificationManager mNotificationManager;
    protected NotificationManager.Policy mPolicy;
    protected int mZenMode;

    private int clearDeprecatedEffects(int i) {
        return i & -4;
    }

    protected static String getKeyFromZenPolicySetting(int i) {
        return i != 1 ? i != 2 ? i != 3 ? ZEN_MODE_FROM_NONE : ZEN_MODE_FROM_STARRED : ZEN_MODE_FROM_CONTACTS : ZEN_MODE_FROM_ANYONE;
    }

    /* access modifiers changed from: protected */
    public int getAlarmsTotalSilencePeopleSummary(int i) {
        return i == 4 ? R.string.zen_mode_none_messages : i == 8 ? R.string.zen_mode_none_calls : R.string.zen_mode_from_no_conversations;
    }

    public static ZenModeBackend getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ZenModeBackend(context);
        }
        return sInstance;
    }

    public ZenModeBackend(Context context) {
        this.mContext = context;
        this.mNotificationManager = (NotificationManager) context.getSystemService("notification");
        updateZenMode();
        updatePolicy();
    }

    /* access modifiers changed from: protected */
    public void updatePolicy() {
        NotificationManager notificationManager = this.mNotificationManager;
        if (notificationManager != null) {
            this.mPolicy = notificationManager.getNotificationPolicy();
        }
    }

    /* access modifiers changed from: protected */
    public void updateZenMode() {
        this.mZenMode = Settings.Global.getInt(this.mContext.getContentResolver(), "zen_mode", this.mZenMode);
    }

    /* access modifiers changed from: protected */
    public boolean updateZenRule(String str, AutomaticZenRule automaticZenRule) {
        return NotificationManager.from(this.mContext).updateAutomaticZenRule(str, automaticZenRule);
    }

    /* access modifiers changed from: protected */
    public void setZenMode(int i) {
        NotificationManager.from(this.mContext).setZenMode(i, (Uri) null, this.TAG);
        this.mZenMode = getZenMode();
    }

    /* access modifiers changed from: protected */
    public void setZenModeForDuration(int i) {
        this.mNotificationManager.setZenMode(1, ZenModeConfig.toTimeCondition(this.mContext, i, ActivityManager.getCurrentUser(), true).id, this.TAG);
        this.mZenMode = getZenMode();
    }

    /* access modifiers changed from: protected */
    public int getZenMode() {
        int i = Settings.Global.getInt(this.mContext.getContentResolver(), "zen_mode", this.mZenMode);
        this.mZenMode = i;
        return i;
    }

    /* access modifiers changed from: protected */
    public boolean isVisualEffectSuppressed(int i) {
        return (this.mPolicy.suppressedVisualEffects & i) != 0;
    }

    /* access modifiers changed from: protected */
    public boolean isPriorityCategoryEnabled(int i) {
        return (this.mPolicy.priorityCategories & i) != 0;
    }

    /* access modifiers changed from: protected */
    public int getNewDefaultPriorityCategories(boolean z, int i) {
        int i2 = this.mPolicy.priorityCategories;
        return z ? i2 | i : i2 & (~i);
    }

    /* access modifiers changed from: protected */
    public int getPriorityCallSenders() {
        if (isPriorityCategoryEnabled(8)) {
            return this.mPolicy.priorityCallSenders;
        }
        return -1;
    }

    /* access modifiers changed from: protected */
    public int getPriorityMessageSenders() {
        if (isPriorityCategoryEnabled(4)) {
            return this.mPolicy.priorityMessageSenders;
        }
        return -1;
    }

    /* access modifiers changed from: protected */
    public int getPriorityConversationSenders() {
        if (isPriorityCategoryEnabled(256)) {
            return this.mPolicy.priorityConversationSenders;
        }
        return 3;
    }

    /* access modifiers changed from: protected */
    public void saveVisualEffectsPolicy(int i, boolean z) {
        Settings.Secure.putInt(this.mContext.getContentResolver(), "zen_settings_updated", 1);
        int newSuppressedEffects = getNewSuppressedEffects(z, i);
        NotificationManager.Policy policy = this.mPolicy;
        savePolicy(policy.priorityCategories, policy.priorityCallSenders, policy.priorityMessageSenders, newSuppressedEffects, policy.priorityConversationSenders);
    }

    /* access modifiers changed from: protected */
    public void saveSoundPolicy(int i, boolean z) {
        int newDefaultPriorityCategories = getNewDefaultPriorityCategories(z, i);
        NotificationManager.Policy policy = this.mPolicy;
        savePolicy(newDefaultPriorityCategories, policy.priorityCallSenders, policy.priorityMessageSenders, policy.suppressedVisualEffects, policy.priorityConversationSenders);
    }

    /* access modifiers changed from: protected */
    public void savePolicy(int i, int i2, int i3, int i4, int i5) {
        NotificationManager.Policy policy = new NotificationManager.Policy(i, i2, i3, i4, i5);
        this.mPolicy = policy;
        this.mNotificationManager.setNotificationPolicy(policy);
    }

    private int getNewSuppressedEffects(boolean z, int i) {
        int i2 = this.mPolicy.suppressedVisualEffects;
        return clearDeprecatedEffects(z ? i2 | i : (~i) & i2);
    }

    /* access modifiers changed from: protected */
    public void saveSenders(int i, int i2) {
        int i3;
        String str;
        int i4;
        int priorityCallSenders = getPriorityCallSenders();
        int priorityMessageSenders = getPriorityMessageSenders();
        int prioritySenders = getPrioritySenders(i);
        boolean z = i2 != -1;
        if (i2 == -1) {
            i2 = prioritySenders;
        }
        if (i == 8) {
            str = "Calls";
            i3 = i2;
        } else {
            i3 = priorityCallSenders;
            str = "";
        }
        if (i == 4) {
            str = "Messages";
            i4 = i2;
        } else {
            i4 = priorityMessageSenders;
        }
        int newDefaultPriorityCategories = getNewDefaultPriorityCategories(z, i);
        NotificationManager.Policy policy = this.mPolicy;
        savePolicy(newDefaultPriorityCategories, i3, i4, policy.suppressedVisualEffects, policy.priorityConversationSenders);
        if (ZenModeSettingsBase.DEBUG) {
            String str2 = this.TAG;
            Log.d(str2, "onPrefChange allow" + str + "=" + z + " allow" + str + "From=" + ZenModeConfig.sourceToString(i2));
        }
    }

    /* access modifiers changed from: protected */
    public void saveConversationSenders(int i) {
        int newDefaultPriorityCategories = getNewDefaultPriorityCategories(i != 3, 256);
        NotificationManager.Policy policy = this.mPolicy;
        savePolicy(newDefaultPriorityCategories, policy.priorityCallSenders, policy.priorityMessageSenders, policy.suppressedVisualEffects, i);
    }

    private int getPrioritySenders(int i) {
        if (i == 8) {
            return getPriorityCallSenders();
        }
        if (i == 4) {
            return getPriorityMessageSenders();
        }
        if (i == 256) {
            return getPriorityConversationSenders();
        }
        return -1;
    }

    /* access modifiers changed from: protected */
    public int getConversationSummary() {
        int priorityConversationSenders = getPriorityConversationSenders();
        if (priorityConversationSenders != 1) {
            return priorityConversationSenders != 2 ? R.string.zen_mode_from_no_conversations : R.string.zen_mode_from_important_conversations;
        }
        return R.string.zen_mode_from_all_conversations;
    }

    /* access modifiers changed from: protected */
    public int getContactsCallsSummary(ZenPolicy zenPolicy) {
        int priorityCallSenders = zenPolicy.getPriorityCallSenders();
        if (priorityCallSenders == 1) {
            return R.string.zen_mode_from_anyone;
        }
        if (priorityCallSenders != 2) {
            return priorityCallSenders != 3 ? R.string.zen_mode_none_calls : R.string.zen_mode_from_starred;
        }
        return R.string.zen_mode_from_contacts;
    }

    /* access modifiers changed from: protected */
    public int getContactsMessagesSummary(ZenPolicy zenPolicy) {
        int priorityMessageSenders = zenPolicy.getPriorityMessageSenders();
        if (priorityMessageSenders == 1) {
            return R.string.zen_mode_from_anyone;
        }
        if (priorityMessageSenders != 2) {
            return priorityMessageSenders != 3 ? R.string.zen_mode_none_messages : R.string.zen_mode_from_starred;
        }
        return R.string.zen_mode_from_contacts;
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected static int getZenPolicySettingFromPrefKey(java.lang.String r4) {
        /*
            int r0 = r4.hashCode()
            r1 = 3
            r2 = 2
            r3 = 1
            switch(r0) {
                case -946901971: goto L_0x0029;
                case -423126328: goto L_0x001f;
                case 187510959: goto L_0x0015;
                case 462773226: goto L_0x000b;
                default: goto L_0x000a;
            }
        L_0x000a:
            goto L_0x0033
        L_0x000b:
            java.lang.String r0 = "zen_mode_from_starred"
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L_0x0033
            r4 = r2
            goto L_0x0034
        L_0x0015:
            java.lang.String r0 = "zen_mode_from_anyone"
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L_0x0033
            r4 = 0
            goto L_0x0034
        L_0x001f:
            java.lang.String r0 = "zen_mode_from_contacts"
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L_0x0033
            r4 = r3
            goto L_0x0034
        L_0x0029:
            java.lang.String r0 = "zen_mode_from_none"
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L_0x0033
            r4 = r1
            goto L_0x0034
        L_0x0033:
            r4 = -1
        L_0x0034:
            if (r4 == 0) goto L_0x003e
            if (r4 == r3) goto L_0x003d
            if (r4 == r2) goto L_0x003c
            r4 = 4
            return r4
        L_0x003c:
            return r1
        L_0x003d:
            return r2
        L_0x003e:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.notification.zen.ZenModeBackend.getZenPolicySettingFromPrefKey(java.lang.String):int");
    }

    public boolean removeZenRule(String str) {
        return NotificationManager.from(this.mContext).removeAutomaticZenRule(str);
    }

    public NotificationManager.Policy getConsolidatedPolicy() {
        return NotificationManager.from(this.mContext).getConsolidatedNotificationPolicy();
    }

    /* access modifiers changed from: protected */
    public String addZenRule(AutomaticZenRule automaticZenRule) {
        try {
            return NotificationManager.from(this.mContext).addAutomaticZenRule(automaticZenRule);
        } catch (Exception unused) {
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    public ZenPolicy setDefaultZenPolicy(ZenPolicy zenPolicy) {
        int i = 4;
        int zenPolicySenders = this.mPolicy.allowCalls() ? ZenModeConfig.getZenPolicySenders(this.mPolicy.allowCallsFrom()) : 4;
        if (this.mPolicy.allowMessages()) {
            i = ZenModeConfig.getZenPolicySenders(this.mPolicy.allowMessagesFrom());
        }
        return new ZenPolicy.Builder(zenPolicy).allowAlarms(this.mPolicy.allowAlarms()).allowCalls(zenPolicySenders).allowEvents(this.mPolicy.allowEvents()).allowMedia(this.mPolicy.allowMedia()).allowMessages(i).allowConversations(this.mPolicy.allowConversations() ? this.mPolicy.allowConversationsFrom() : 3).allowReminders(this.mPolicy.allowReminders()).allowRepeatCallers(this.mPolicy.allowRepeatCallers()).allowSystem(this.mPolicy.allowSystem()).showFullScreenIntent(this.mPolicy.showFullScreenIntents()).showLights(this.mPolicy.showLights()).showInAmbientDisplay(this.mPolicy.showAmbient()).showInNotificationList(this.mPolicy.showInNotificationList()).showBadges(this.mPolicy.showBadges()).showPeeking(this.mPolicy.showPeeking()).showStatusBarIcons(this.mPolicy.showStatusBarIcons()).build();
    }

    /* access modifiers changed from: protected */
    public Map.Entry<String, AutomaticZenRule>[] getAutomaticZenRules() {
        Map<String, AutomaticZenRule> automaticZenRules = NotificationManager.from(this.mContext).getAutomaticZenRules();
        Map.Entry<String, AutomaticZenRule>[] entryArr = (Map.Entry[]) automaticZenRules.entrySet().toArray(new Map.Entry[automaticZenRules.size()]);
        Arrays.sort(entryArr, RULE_COMPARATOR);
        return entryArr;
    }

    /* access modifiers changed from: protected */
    public AutomaticZenRule getAutomaticZenRule(String str) {
        return NotificationManager.from(this.mContext).getAutomaticZenRule(str);
    }

    /* access modifiers changed from: private */
    public static List<String> getDefaultRuleIds() {
        if (mDefaultRuleIds == null) {
            mDefaultRuleIds = ZenModeConfig.DEFAULT_RULE_IDS;
        }
        return mDefaultRuleIds;
    }

    /* access modifiers changed from: package-private */
    public NotificationManager.Policy toNotificationPolicy(ZenPolicy zenPolicy) {
        return new ZenModeConfig().toNotificationPolicy(zenPolicy);
    }

    /* access modifiers changed from: package-private */
    public List<String> getStarredContacts(Cursor cursor) {
        ArrayList arrayList = new ArrayList();
        if (cursor == null || !cursor.moveToFirst()) {
            return arrayList;
        }
        do {
            String string = cursor.getString(0);
            if (string == null) {
                string = this.mContext.getString(R.string.zen_mode_starred_contacts_empty_name);
            }
            arrayList.add(string);
        } while (cursor.moveToNext());
        return arrayList;
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0014  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.util.List<java.lang.String> getStarredContacts() {
        /*
            r1 = this;
            android.database.Cursor r0 = r1.queryStarredContactsData()     // Catch:{ all -> 0x0010 }
            java.util.List r1 = r1.getStarredContacts(r0)     // Catch:{ all -> 0x000e }
            if (r0 == 0) goto L_0x000d
            r0.close()
        L_0x000d:
            return r1
        L_0x000e:
            r1 = move-exception
            goto L_0x0012
        L_0x0010:
            r1 = move-exception
            r0 = 0
        L_0x0012:
            if (r0 == 0) goto L_0x0017
            r0.close()
        L_0x0017:
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.notification.zen.ZenModeBackend.getStarredContacts():java.util.List");
    }

    /* access modifiers changed from: package-private */
    public String getStarredContactsSummary(Context context) {
        List<String> starredContacts = getStarredContacts();
        int size = starredContacts.size();
        MessageFormat messageFormat = new MessageFormat(this.mContext.getString(R.string.zen_mode_starred_contacts_summary_contacts), Locale.getDefault());
        HashMap hashMap = new HashMap();
        hashMap.put("count", Integer.valueOf(size));
        if (size >= 1) {
            hashMap.put("contact_1", starredContacts.get(0));
            if (size >= 2) {
                hashMap.put("contact_2", starredContacts.get(1));
                if (size == 3) {
                    hashMap.put("contact_3", starredContacts.get(2));
                }
            }
        }
        return messageFormat.format(hashMap);
    }

    /* access modifiers changed from: package-private */
    public String getContactsNumberSummary(Context context) {
        MessageFormat messageFormat = new MessageFormat(this.mContext.getString(R.string.zen_mode_contacts_count), Locale.getDefault());
        HashMap hashMap = new HashMap();
        hashMap.put("count", Integer.valueOf(queryAllContactsData().getCount()));
        return messageFormat.format(hashMap);
    }

    private Cursor queryStarredContactsData() {
        return this.mContext.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, new String[]{"display_name"}, "starred=1", (String[]) null, "times_contacted");
    }

    private Cursor queryAllContactsData() {
        return this.mContext.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, new String[]{"display_name"}, (String) null, (String[]) null, (String) null);
    }
}
