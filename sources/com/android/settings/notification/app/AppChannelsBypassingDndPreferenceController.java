package com.android.settings.notification.app;

import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.UserHandle;
import androidx.core.text.BidiFormatter;
import androidx.lifecycle.LifecycleObserver;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.settings.R;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.notification.NotificationBackend;
import com.android.settings.widget.PrimarySwitchPreference;
import com.android.settingslib.RestrictedSwitchPreference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppChannelsBypassingDndPreferenceController extends NotificationPreferenceController implements PreferenceControllerMixin, LifecycleObserver {
    /* access modifiers changed from: private */
    public RestrictedSwitchPreference mAllNotificationsToggle;
    /* access modifiers changed from: private */
    public List<NotificationChannel> mChannels = new ArrayList();
    /* access modifiers changed from: private */
    public PreferenceCategory mPreferenceCategory;

    public String getPreferenceKey() {
        return "zen_mode_bypassing_app_channels_list";
    }

    /* access modifiers changed from: package-private */
    public boolean isIncludedInFilter() {
        return false;
    }

    public AppChannelsBypassingDndPreferenceController(Context context, NotificationBackend notificationBackend) {
        super(context, notificationBackend);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        PreferenceCategory preferenceCategory = (PreferenceCategory) preferenceScreen.findPreference("zen_mode_bypassing_app_channels_list");
        this.mPreferenceCategory = preferenceCategory;
        RestrictedSwitchPreference restrictedSwitchPreference = new RestrictedSwitchPreference(preferenceCategory.getContext());
        this.mAllNotificationsToggle = restrictedSwitchPreference;
        restrictedSwitchPreference.setTitle((int) R.string.zen_mode_bypassing_app_channels_toggle_all);
        this.mAllNotificationsToggle.setDisabledByAdmin(this.mAdmin);
        RestrictedSwitchPreference restrictedSwitchPreference2 = this.mAllNotificationsToggle;
        restrictedSwitchPreference2.setEnabled(this.mAdmin == null || !restrictedSwitchPreference2.isDisabledByAdmin());
        this.mAllNotificationsToggle.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                boolean isChecked = ((SwitchPreference) preference).isChecked();
                for (NotificationChannel notificationChannel : AppChannelsBypassingDndPreferenceController.this.mChannels) {
                    if (AppChannelsBypassingDndPreferenceController.this.showNotification(notificationChannel) && AppChannelsBypassingDndPreferenceController.this.isChannelConfigurable(notificationChannel)) {
                        notificationChannel.setBypassDnd(isChecked);
                        notificationChannel.lockFields(1);
                        AppChannelsBypassingDndPreferenceController appChannelsBypassingDndPreferenceController = AppChannelsBypassingDndPreferenceController.this;
                        NotificationBackend notificationBackend = appChannelsBypassingDndPreferenceController.mBackend;
                        NotificationBackend.AppRow appRow = appChannelsBypassingDndPreferenceController.mAppRow;
                        notificationBackend.updateChannel(appRow.pkg, appRow.uid, notificationChannel);
                    }
                }
                for (int i = 1; i < AppChannelsBypassingDndPreferenceController.this.mPreferenceCategory.getPreferenceCount(); i++) {
                    AppChannelsBypassingDndPreferenceController appChannelsBypassingDndPreferenceController2 = AppChannelsBypassingDndPreferenceController.this;
                    ((PrimarySwitchPreference) AppChannelsBypassingDndPreferenceController.this.mPreferenceCategory.getPreference(i)).setChecked(appChannelsBypassingDndPreferenceController2.showNotificationInDnd((NotificationChannel) appChannelsBypassingDndPreferenceController2.mChannels.get(i - 1)));
                }
                return true;
            }
        });
        loadAppChannels();
        super.displayPreference(preferenceScreen);
    }

    public boolean isAvailable() {
        return this.mAppRow != null;
    }

    public void updateState(Preference preference) {
        if (this.mAppRow != null) {
            loadAppChannels();
        }
    }

    private void loadAppChannels() {
        new AsyncTask<Void, Void, Void>() {
            /* access modifiers changed from: protected */
            public Void doInBackground(Void... voidArr) {
                ArrayList arrayList = new ArrayList();
                AppChannelsBypassingDndPreferenceController appChannelsBypassingDndPreferenceController = AppChannelsBypassingDndPreferenceController.this;
                NotificationBackend notificationBackend = appChannelsBypassingDndPreferenceController.mBackend;
                NotificationBackend.AppRow appRow = appChannelsBypassingDndPreferenceController.mAppRow;
                for (NotificationChannelGroup channels : notificationBackend.getGroups(appRow.pkg, appRow.uid).getList()) {
                    for (NotificationChannel next : channels.getChannels()) {
                        if (!AppChannelsBypassingDndPreferenceController.this.isConversation(next)) {
                            arrayList.add(next);
                        }
                    }
                }
                Collections.sort(arrayList, NotificationPreferenceController.CHANNEL_COMPARATOR);
                List unused = AppChannelsBypassingDndPreferenceController.this.mChannels = arrayList;
                return null;
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(Void voidR) {
                AppChannelsBypassingDndPreferenceController appChannelsBypassingDndPreferenceController = AppChannelsBypassingDndPreferenceController.this;
                if (appChannelsBypassingDndPreferenceController.mContext != null) {
                    appChannelsBypassingDndPreferenceController.populateList();
                }
            }
        }.execute(new Void[0]);
    }

    /* access modifiers changed from: private */
    public void populateList() {
        PreferenceCategory preferenceCategory = this.mPreferenceCategory;
        if (preferenceCategory != null) {
            preferenceCategory.removeAll();
            this.mPreferenceCategory.addPreference(this.mAllNotificationsToggle);
            for (final NotificationChannel next : this.mChannels) {
                PrimarySwitchPreference primarySwitchPreference = new PrimarySwitchPreference(this.mContext);
                primarySwitchPreference.setDisabledByAdmin(this.mAdmin);
                primarySwitchPreference.setSwitchEnabled((this.mAdmin == null || !primarySwitchPreference.isDisabledByAdmin()) && isChannelConfigurable(next) && showNotification(next));
                primarySwitchPreference.setTitle(BidiFormatter.getInstance().unicodeWrap(next.getName()));
                primarySwitchPreference.setChecked(showNotificationInDnd(next));
                primarySwitchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    public boolean onPreferenceChange(Preference preference, Object obj) {
                        next.setBypassDnd(((Boolean) obj).booleanValue());
                        next.lockFields(1);
                        AppChannelsBypassingDndPreferenceController appChannelsBypassingDndPreferenceController = AppChannelsBypassingDndPreferenceController.this;
                        NotificationBackend notificationBackend = appChannelsBypassingDndPreferenceController.mBackend;
                        NotificationBackend.AppRow appRow = appChannelsBypassingDndPreferenceController.mAppRow;
                        notificationBackend.updateChannel(appRow.pkg, appRow.uid, next);
                        AppChannelsBypassingDndPreferenceController.this.mAllNotificationsToggle.setChecked(AppChannelsBypassingDndPreferenceController.this.areAllChannelsBypassing());
                        return true;
                    }
                });
                Bundle bundle = new Bundle();
                bundle.putInt("uid", this.mAppRow.uid);
                bundle.putString("package", this.mAppRow.pkg);
                bundle.putString("android.provider.extra.CHANNEL_ID", next.getId());
                bundle.putBoolean("fromSettings", true);
                primarySwitchPreference.setOnPreferenceClickListener(new C1099x5981a397(this, bundle));
                this.mPreferenceCategory.addPreference(primarySwitchPreference);
            }
            this.mAllNotificationsToggle.setChecked(areAllChannelsBypassing());
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$populateList$0(Bundle bundle, Preference preference) {
        new SubSettingLauncher(this.mContext).setDestination(ChannelNotificationSettings.class.getName()).setArguments(bundle).setUserHandle(UserHandle.of(this.mAppRow.userId)).setTitleRes(R.string.notification_channel_title).setSourceMetricsCategory(1840).launch();
        return true;
    }

    /* access modifiers changed from: private */
    public boolean areAllChannelsBypassing() {
        boolean z = true;
        for (NotificationChannel next : this.mChannels) {
            if (showNotification(next)) {
                z &= showNotificationInDnd(next);
            }
        }
        return z;
    }

    /* access modifiers changed from: private */
    public boolean showNotificationInDnd(NotificationChannel notificationChannel) {
        return notificationChannel.canBypassDnd() && showNotification(notificationChannel);
    }

    /* access modifiers changed from: private */
    public boolean showNotification(NotificationChannel notificationChannel) {
        return notificationChannel.getImportance() != 0;
    }

    /* access modifiers changed from: private */
    public boolean isConversation(NotificationChannel notificationChannel) {
        return notificationChannel.getConversationId() != null && !notificationChannel.isDemoted();
    }
}
