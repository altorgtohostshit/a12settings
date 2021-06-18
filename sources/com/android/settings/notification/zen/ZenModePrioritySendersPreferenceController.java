package com.android.settings.notification.zen;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.widget.RadioButtonPreference;
import java.util.ArrayList;
import java.util.List;

public class ZenModePrioritySendersPreferenceController extends AbstractZenModePreferenceController {
    /* access modifiers changed from: private */
    public static final Intent ALL_CONTACTS_INTENT = new Intent("com.android.contacts.action.LIST_DEFAULT");
    /* access modifiers changed from: private */
    public static final Intent FALLBACK_INTENT = new Intent("android.intent.action.MAIN");
    static final String KEY_ANY = "senders_anyone";
    static final String KEY_CONTACTS = "senders_contacts";
    static final String KEY_NONE = "senders_none";
    static final String KEY_STARRED = "senders_starred_contacts";
    /* access modifiers changed from: private */
    public static final Intent STARRED_CONTACTS_INTENT = new Intent("com.android.contacts.action.LIST_STARRED");
    /* access modifiers changed from: private */
    public final boolean mIsMessages;
    /* access modifiers changed from: private */
    public final PackageManager mPackageManager;
    private PreferenceCategory mPreferenceCategory;
    private RadioButtonPreference.OnClickListener mRadioButtonClickListener = new RadioButtonPreference.OnClickListener() {
        public void onRadioButtonClicked(RadioButtonPreference radioButtonPreference) {
            int access$000 = ZenModePrioritySendersPreferenceController.keyToSetting(radioButtonPreference.getKey());
            if (access$000 != ZenModePrioritySendersPreferenceController.this.getPrioritySenders()) {
                ZenModePrioritySendersPreferenceController zenModePrioritySendersPreferenceController = ZenModePrioritySendersPreferenceController.this;
                zenModePrioritySendersPreferenceController.mBackend.saveSenders(zenModePrioritySendersPreferenceController.mIsMessages ? 4 : 8, access$000);
            }
        }
    };
    private List<RadioButtonPreference> mRadioButtonPreferences = new ArrayList();

    public boolean isAvailable() {
        return true;
    }

    public ZenModePrioritySendersPreferenceController(Context context, String str, Lifecycle lifecycle, boolean z) {
        super(context, str, lifecycle);
        this.mIsMessages = z;
        this.mPackageManager = this.mContext.getPackageManager();
        Intent intent = FALLBACK_INTENT;
        if (!intent.hasCategory("android.intent.category.APP_CONTACTS")) {
            intent.addCategory("android.intent.category.APP_CONTACTS");
        }
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        PreferenceCategory preferenceCategory = (PreferenceCategory) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreferenceCategory = preferenceCategory;
        if (preferenceCategory.findPreference(KEY_ANY) == null) {
            makeRadioPreference(KEY_STARRED, R.string.zen_mode_from_starred);
            makeRadioPreference(KEY_CONTACTS, R.string.zen_mode_from_contacts);
            makeRadioPreference(KEY_ANY, R.string.zen_mode_from_anyone);
            makeRadioPreference(KEY_NONE, this.mIsMessages ? R.string.zen_mode_none_messages : R.string.zen_mode_none_calls);
            updateSummaries();
        }
        super.displayPreference(preferenceScreen);
    }

    public String getPreferenceKey() {
        return this.KEY;
    }

    public void updateState(Preference preference) {
        int prioritySenders = getPrioritySenders();
        for (RadioButtonPreference next : this.mRadioButtonPreferences) {
            next.setChecked(keyToSetting(next.getKey()) == prioritySenders);
        }
    }

    public void onResume() {
        super.onResume();
        updateSummaries();
    }

    private void updateSummaries() {
        for (RadioButtonPreference next : this.mRadioButtonPreferences) {
            next.setSummary((CharSequence) getSummary(next.getKey()));
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int keyToSetting(java.lang.String r5) {
        /*
            int r0 = r5.hashCode()
            r1 = 0
            r2 = -1
            r3 = 2
            r4 = 1
            switch(r0) {
                case -1145842476: goto L_0x002a;
                case -133103980: goto L_0x0020;
                case 1725241211: goto L_0x0016;
                case 1767544313: goto L_0x000c;
                default: goto L_0x000b;
            }
        L_0x000b:
            goto L_0x0034
        L_0x000c:
            java.lang.String r0 = "senders_none"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x0034
            r5 = 3
            goto L_0x0035
        L_0x0016:
            java.lang.String r0 = "senders_anyone"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x0034
            r5 = r3
            goto L_0x0035
        L_0x0020:
            java.lang.String r0 = "senders_contacts"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x0034
            r5 = r4
            goto L_0x0035
        L_0x002a:
            java.lang.String r0 = "senders_starred_contacts"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x0034
            r5 = r1
            goto L_0x0035
        L_0x0034:
            r5 = r2
        L_0x0035:
            if (r5 == 0) goto L_0x003e
            if (r5 == r4) goto L_0x003d
            if (r5 == r3) goto L_0x003c
            return r2
        L_0x003c:
            return r1
        L_0x003d:
            return r4
        L_0x003e:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.notification.zen.ZenModePrioritySendersPreferenceController.keyToSetting(java.lang.String):int");
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String getSummary(java.lang.String r4) {
        /*
            r3 = this;
            int r0 = r4.hashCode()
            r1 = 2
            r2 = 1
            switch(r0) {
                case -1145842476: goto L_0x0028;
                case -133103980: goto L_0x001e;
                case 1725241211: goto L_0x0014;
                case 1767544313: goto L_0x000a;
                default: goto L_0x0009;
            }
        L_0x0009:
            goto L_0x0032
        L_0x000a:
            java.lang.String r0 = "senders_none"
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L_0x0032
            r4 = 3
            goto L_0x0033
        L_0x0014:
            java.lang.String r0 = "senders_anyone"
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L_0x0032
            r4 = r1
            goto L_0x0033
        L_0x001e:
            java.lang.String r0 = "senders_contacts"
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L_0x0032
            r4 = r2
            goto L_0x0033
        L_0x0028:
            java.lang.String r0 = "senders_starred_contacts"
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L_0x0032
            r4 = 0
            goto L_0x0033
        L_0x0032:
            r4 = -1
        L_0x0033:
            if (r4 == 0) goto L_0x005a
            if (r4 == r2) goto L_0x0051
            if (r4 == r1) goto L_0x003b
            r3 = 0
            return r3
        L_0x003b:
            android.content.Context r4 = r3.mContext
            android.content.res.Resources r4 = r4.getResources()
            boolean r3 = r3.mIsMessages
            if (r3 == 0) goto L_0x0049
            r3 = 2130974505(0x7f041729, float:1.7557835E38)
            goto L_0x004c
        L_0x0049:
            r3 = 2130974504(0x7f041728, float:1.7557833E38)
        L_0x004c:
            java.lang.String r3 = r4.getString(r3)
            return r3
        L_0x0051:
            com.android.settings.notification.zen.ZenModeBackend r4 = r3.mBackend
            android.content.Context r3 = r3.mContext
            java.lang.String r3 = r4.getContactsNumberSummary(r3)
            return r3
        L_0x005a:
            com.android.settings.notification.zen.ZenModeBackend r4 = r3.mBackend
            android.content.Context r3 = r3.mContext
            java.lang.String r3 = r4.getStarredContactsSummary(r3)
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.notification.zen.ZenModePrioritySendersPreferenceController.getSummary(java.lang.String):java.lang.String");
    }

    /* access modifiers changed from: private */
    public int getPrioritySenders() {
        if (this.mIsMessages) {
            return this.mBackend.getPriorityMessageSenders();
        }
        return this.mBackend.getPriorityCallSenders();
    }

    private RadioButtonPreference makeRadioPreference(String str, int i) {
        RadioButtonPreference radioButtonPreference = new RadioButtonPreference(this.mPreferenceCategory.getContext());
        radioButtonPreference.setKey(str);
        radioButtonPreference.setTitle(i);
        radioButtonPreference.setOnClickListener(this.mRadioButtonClickListener);
        View.OnClickListener widgetClickListener = getWidgetClickListener(str);
        if (widgetClickListener != null) {
            radioButtonPreference.setExtraWidgetOnClickListener(widgetClickListener);
        }
        this.mPreferenceCategory.addPreference(radioButtonPreference);
        this.mRadioButtonPreferences.add(radioButtonPreference);
        return radioButtonPreference;
    }

    private View.OnClickListener getWidgetClickListener(final String str) {
        if (!KEY_CONTACTS.equals(str) && !KEY_STARRED.equals(str)) {
            return null;
        }
        if (KEY_STARRED.equals(str) && !isStarredIntentValid()) {
            return null;
        }
        if (!KEY_CONTACTS.equals(str) || isContactsIntentValid()) {
            return new View.OnClickListener() {
                public void onClick(View view) {
                    if (ZenModePrioritySendersPreferenceController.KEY_STARRED.equals(str) && ZenModePrioritySendersPreferenceController.STARRED_CONTACTS_INTENT.resolveActivity(ZenModePrioritySendersPreferenceController.this.mPackageManager) != null) {
                        ZenModePrioritySendersPreferenceController.this.mContext.startActivity(ZenModePrioritySendersPreferenceController.STARRED_CONTACTS_INTENT);
                    } else if (!ZenModePrioritySendersPreferenceController.KEY_CONTACTS.equals(str) || ZenModePrioritySendersPreferenceController.ALL_CONTACTS_INTENT.resolveActivity(ZenModePrioritySendersPreferenceController.this.mPackageManager) == null) {
                        ZenModePrioritySendersPreferenceController.this.mContext.startActivity(ZenModePrioritySendersPreferenceController.FALLBACK_INTENT);
                    } else {
                        ZenModePrioritySendersPreferenceController.this.mContext.startActivity(ZenModePrioritySendersPreferenceController.ALL_CONTACTS_INTENT);
                    }
                }
            };
        }
        return null;
    }

    private boolean isStarredIntentValid() {
        return (STARRED_CONTACTS_INTENT.resolveActivity(this.mPackageManager) == null && FALLBACK_INTENT.resolveActivity(this.mPackageManager) == null) ? false : true;
    }

    private boolean isContactsIntentValid() {
        return (ALL_CONTACTS_INTENT.resolveActivity(this.mPackageManager) == null && FALLBACK_INTENT.resolveActivity(this.mPackageManager) == null) ? false : true;
    }
}
