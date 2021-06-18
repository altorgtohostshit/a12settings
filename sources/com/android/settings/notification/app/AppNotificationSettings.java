package com.android.settings.notification.app;

import android.content.Context;
import android.content.pm.ShortcutInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import com.android.internal.widget.LockPatternUtils;
import com.android.settings.R;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.ArrayList;
import java.util.List;

public class AppNotificationSettings extends NotificationSettings {
    private static final boolean DEBUG = Log.isLoggable("AppNotificationSettings", 3);
    private static String KEY_ADVANCED_CATEGORY = "app_advanced";
    private static String KEY_APP_LINK = "app_link";
    private static String KEY_BADGE = "badge";
    private static String[] LEGACY_NON_ADVANCED_KEYS = {"badge", "app_link"};

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "AppNotificationSettings";
    }

    public int getMetricsCategory() {
        return 72;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.app_notification_settings;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        if (this.mShowLegacyChannelConfig && preferenceScreen != null) {
            PreferenceGroup preferenceGroup = (PreferenceGroup) findPreference(KEY_ADVANCED_CATEGORY);
            removePreference(KEY_ADVANCED_CATEGORY);
            if (preferenceGroup != null) {
                for (String findPreference : LEGACY_NON_ADVANCED_KEYS) {
                    Preference findPreference2 = preferenceGroup.findPreference(findPreference);
                    preferenceGroup.removePreference(findPreference2);
                    if (findPreference2 != null) {
                        preferenceScreen.addPreference(findPreference2);
                    }
                }
            }
        }
    }

    public void onResume() {
        super.onResume();
        if (this.mUid < 0 || TextUtils.isEmpty(this.mPkg) || this.mPkgInfo == null) {
            Log.w("AppNotificationSettings", "Missing package or uid or packageinfo");
            finish();
            return;
        }
        for (NotificationPreferenceController next : this.mControllers) {
            next.onResume(this.mAppRow, this.mChannel, this.mChannelGroup, (Drawable) null, (ShortcutInfo) null, this.mSuspendedAppsAdmin, (List<String>) null);
            next.displayPreference(getPreferenceScreen());
        }
        updatePreferenceStates();
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        ArrayList arrayList = new ArrayList();
        this.mControllers = arrayList;
        arrayList.add(new HeaderPreferenceController(context, this));
        this.mControllers.add(new BlockPreferenceController(context, this.mDependentFieldListener, this.mBackend));
        this.mControllers.add(new BadgePreferenceController(context, this.mBackend));
        this.mControllers.add(new AllowSoundPreferenceController(context, this.mDependentFieldListener, this.mBackend));
        this.mControllers.add(new ImportancePreferenceController(context, this.mDependentFieldListener, this.mBackend));
        this.mControllers.add(new MinImportancePreferenceController(context, this.mDependentFieldListener, this.mBackend));
        this.mControllers.add(new HighImportancePreferenceController(context, this.mDependentFieldListener, this.mBackend));
        this.mControllers.add(new SoundPreferenceController(context, this, this.mDependentFieldListener, this.mBackend));
        this.mControllers.add(new LightsPreferenceController(context, this.mBackend));
        this.mControllers.add(new VibrationPreferenceController(context, this.mBackend));
        this.mControllers.add(new VisibilityPreferenceController(context, new LockPatternUtils(context), this.mBackend));
        this.mControllers.add(new DndPreferenceController(context, this.mBackend));
        this.mControllers.add(new AppLinkPreferenceController(context));
        this.mControllers.add(new DescriptionPreferenceController(context));
        this.mControllers.add(new NotificationsOffPreferenceController(context));
        this.mControllers.add(new DeletedChannelsPreferenceController(context, this.mBackend));
        this.mControllers.add(new ChannelListPreferenceController(context, this.mBackend));
        this.mControllers.add(new AppConversationListPreferenceController(context, this.mBackend));
        this.mControllers.add(new InvalidConversationInfoPreferenceController(context, this.mBackend));
        this.mControllers.add(new InvalidConversationPreferenceController(context, this.mBackend));
        this.mControllers.add(new BubbleSummaryPreferenceController(context, this.mBackend));
        return new ArrayList(this.mControllers);
    }
}
