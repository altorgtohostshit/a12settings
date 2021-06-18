package com.android.settings.notification.app;

import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.content.Context;
import android.widget.Switch;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.notification.NotificationBackend;
import com.android.settings.notification.app.NotificationSettings;
import com.android.settings.widget.SettingsMainSwitchPreference;
import com.android.settingslib.widget.OnMainSwitchChangeListener;

public class BlockPreferenceController extends NotificationPreferenceController implements PreferenceControllerMixin, OnMainSwitchChangeListener {
    private NotificationSettings.DependentFieldListener mDependentFieldListener;

    public String getPreferenceKey() {
        return "block";
    }

    public BlockPreferenceController(Context context, NotificationSettings.DependentFieldListener dependentFieldListener, NotificationBackend notificationBackend) {
        super(context, notificationBackend);
        this.mDependentFieldListener = dependentFieldListener;
    }

    public boolean isAvailable() {
        if (this.mAppRow == null) {
            return false;
        }
        if (this.mPreferenceFilter == null || isIncludedInFilter()) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean isIncludedInFilter() {
        return this.mPreferenceFilter.contains("importance");
    }

    public void updateState(Preference preference) {
        SettingsMainSwitchPreference settingsMainSwitchPreference = (SettingsMainSwitchPreference) preference;
        if (settingsMainSwitchPreference != null) {
            settingsMainSwitchPreference.setTitle(getSwitchBarText());
            settingsMainSwitchPreference.show();
            try {
                settingsMainSwitchPreference.addOnSwitchChangeListener(this);
            } catch (IllegalStateException unused) {
            }
            settingsMainSwitchPreference.setDisabledByAdmin(this.mAdmin);
            boolean z = false;
            if (this.mChannel != null && !isChannelBlockable()) {
                settingsMainSwitchPreference.setSwitchBarEnabled(false);
            }
            if (this.mChannelGroup != null && !isChannelGroupBlockable()) {
                settingsMainSwitchPreference.setSwitchBarEnabled(false);
            }
            if (this.mChannel == null) {
                NotificationBackend.AppRow appRow = this.mAppRow;
                if (appRow.systemApp && (!appRow.banned || appRow.lockedImportance)) {
                    settingsMainSwitchPreference.setSwitchBarEnabled(false);
                }
            }
            NotificationChannel notificationChannel = this.mChannel;
            if (notificationChannel != null) {
                if (!this.mAppRow.banned && notificationChannel.getImportance() != 0) {
                    z = true;
                }
                settingsMainSwitchPreference.setChecked(z);
                return;
            }
            NotificationChannelGroup notificationChannelGroup = this.mChannelGroup;
            if (notificationChannelGroup != null) {
                if (!this.mAppRow.banned && !notificationChannelGroup.isBlocked()) {
                    z = true;
                }
                settingsMainSwitchPreference.setChecked(z);
                return;
            }
            settingsMainSwitchPreference.setChecked(!this.mAppRow.banned);
        }
    }

    public void onSwitchChanged(Switch switchR, boolean z) {
        int i;
        boolean z2 = !z;
        NotificationChannel notificationChannel = this.mChannel;
        if (notificationChannel != null) {
            int importance = notificationChannel.getImportance();
            if (z2 || importance == 0) {
                if (z2) {
                    i = 0;
                } else if (isDefaultChannel()) {
                    i = -1000;
                } else {
                    i = Math.max(this.mChannel.getOriginalImportance(), 2);
                }
                this.mChannel.setImportance(i);
                saveChannel();
            }
            NotificationBackend notificationBackend = this.mBackend;
            NotificationBackend.AppRow appRow = this.mAppRow;
            if (notificationBackend.onlyHasDefaultChannel(appRow.pkg, appRow.uid)) {
                NotificationBackend.AppRow appRow2 = this.mAppRow;
                if (appRow2.banned != z2) {
                    appRow2.banned = z2;
                    this.mBackend.setNotificationsEnabledForPackage(appRow2.pkg, appRow2.uid, !z2);
                }
            }
        } else {
            NotificationChannelGroup notificationChannelGroup = this.mChannelGroup;
            if (notificationChannelGroup != null) {
                notificationChannelGroup.setBlocked(z2);
                NotificationBackend notificationBackend2 = this.mBackend;
                NotificationBackend.AppRow appRow3 = this.mAppRow;
                notificationBackend2.updateChannelGroup(appRow3.pkg, appRow3.uid, this.mChannelGroup);
            } else {
                NotificationBackend.AppRow appRow4 = this.mAppRow;
                if (appRow4 != null) {
                    appRow4.banned = z2;
                    this.mBackend.setNotificationsEnabledForPackage(appRow4.pkg, appRow4.uid, !z2);
                }
            }
        }
        this.mDependentFieldListener.onFieldValueChanged();
    }

    /* access modifiers changed from: package-private */
    public String getSwitchBarText() {
        CharSequence charSequence;
        if (this.mChannel != null) {
            return this.mContext.getString(R.string.notification_content_block_title);
        }
        NotificationChannelGroup notificationChannelGroup = this.mChannelGroup;
        if (notificationChannelGroup != null) {
            charSequence = notificationChannelGroup.getName();
        } else {
            charSequence = this.mAppRow.label;
        }
        return this.mContext.getString(R.string.notification_app_switch_label, new Object[]{charSequence});
    }
}
