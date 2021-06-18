package com.android.settings.notification.app;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.widget.LockPatternUtils;
import com.android.settings.R;
import com.android.settings.notification.app.NotificationSettings;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.ArrayList;
import java.util.List;

public class ConversationNotificationSettings extends NotificationSettings {
    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "ConvoSettings";
    }

    public int getMetricsCategory() {
        return 1830;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.conversation_notification_settings;
    }

    public void onResume() {
        super.onResume();
        if (this.mUid < 0 || TextUtils.isEmpty(this.mPkg) || this.mPkgInfo == null || this.mChannel == null) {
            Log.w("ConvoSettings", "Missing package or uid or packageinfo or channel");
            finish();
            return;
        }
        for (NotificationPreferenceController next : this.mControllers) {
            next.onResume(this.mAppRow, this.mChannel, this.mChannelGroup, this.mConversationDrawable, this.mConversationInfo, this.mSuspendedAppsAdmin, this.mPreferenceFilter);
            next.displayPreference(getPreferenceScreen());
        }
        updatePreferenceStates();
        animatePanel();
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        for (NotificationPreferenceController next : this.mControllers) {
            if (next instanceof PreferenceManager.OnActivityResultListener) {
                ((PreferenceManager.OnActivityResultListener) next).onActivityResult(i, i2, intent);
            }
        }
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        ArrayList arrayList = new ArrayList();
        this.mControllers = arrayList;
        arrayList.add(new ConversationHeaderPreferenceController(context, this));
        this.mControllers.add(new ConversationPriorityPreferenceController(context, this.mBackend, this.mDependentFieldListener));
        this.mControllers.add(new HighImportancePreferenceController(context, this.mDependentFieldListener, this.mBackend));
        this.mControllers.add(new SoundPreferenceController(context, this, this.mDependentFieldListener, this.mBackend));
        this.mControllers.add(new VibrationPreferenceController(context, this.mBackend));
        this.mControllers.add(new VisibilityPreferenceController(context, new LockPatternUtils(context), this.mBackend));
        this.mControllers.add(new LightsPreferenceController(context, this.mBackend));
        this.mControllers.add(new BadgePreferenceController(context, this.mBackend));
        this.mControllers.add(new NotificationsOffPreferenceController(context));
        this.mControllers.add(new BubblePreferenceController(context, getChildFragmentManager(), this.mBackend, false, (NotificationSettings.DependentFieldListener) null));
        this.mControllers.add(new ConversationDemotePreferenceController(context, this, this.mBackend));
        this.mControllers.add(new BubbleCategoryPreferenceController(context));
        this.mControllers.add(new BubbleLinkPreferenceController(context));
        return new ArrayList(this.mControllers);
    }
}
