package com.android.settings.notification.app;

import android.app.NotificationChannel;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.notification.NotificationBackend;
import com.android.settings.notification.app.NotificationSettings;

public class SoundPreferenceController extends NotificationPreferenceController implements PreferenceControllerMixin, Preference.OnPreferenceChangeListener, PreferenceManager.OnActivityResultListener {
    private final SettingsPreferenceFragment mFragment;
    private final NotificationSettings.DependentFieldListener mListener;
    private NotificationSoundPreference mPreference;

    public String getPreferenceKey() {
        return "ringtone";
    }

    public SoundPreferenceController(Context context, SettingsPreferenceFragment settingsPreferenceFragment, NotificationSettings.DependentFieldListener dependentFieldListener, NotificationBackend notificationBackend) {
        super(context, notificationBackend);
        this.mFragment = settingsPreferenceFragment;
        this.mListener = dependentFieldListener;
    }

    public boolean isAvailable() {
        if (super.isAvailable() && this.mChannel != null && checkCanBeVisible(3) && !isDefaultChannel()) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean isIncludedInFilter() {
        return this.mPreferenceFilter.contains("sound");
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = (NotificationSoundPreference) preferenceScreen.findPreference(getPreferenceKey());
    }

    public void updateState(Preference preference) {
        if (this.mAppRow != null && this.mChannel != null) {
            NotificationSoundPreference notificationSoundPreference = (NotificationSoundPreference) preference;
            notificationSoundPreference.setEnabled(this.mAdmin == null);
            notificationSoundPreference.setRingtone(this.mChannel.getSound());
        }
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        NotificationChannel notificationChannel = this.mChannel;
        if (notificationChannel == null) {
            return true;
        }
        notificationChannel.setSound((Uri) obj, notificationChannel.getAudioAttributes());
        saveChannel();
        return true;
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!"ringtone".equals(preference.getKey()) || this.mFragment == null) {
            return false;
        }
        NotificationSoundPreference notificationSoundPreference = (NotificationSoundPreference) preference;
        NotificationChannel notificationChannel = this.mChannel;
        if (!(notificationChannel == null || notificationChannel.getAudioAttributes() == null)) {
            if (4 == this.mChannel.getAudioAttributes().getUsage()) {
                notificationSoundPreference.setRingtoneType(4);
            } else if (6 == this.mChannel.getAudioAttributes().getUsage()) {
                notificationSoundPreference.setRingtoneType(1);
            } else {
                notificationSoundPreference.setRingtoneType(2);
            }
        }
        notificationSoundPreference.onPrepareRingtonePickerIntent(notificationSoundPreference.getIntent());
        this.mFragment.startActivityForResult(preference.getIntent(), 200);
        return true;
    }

    public boolean onActivityResult(int i, int i2, Intent intent) {
        if (200 != i) {
            return false;
        }
        NotificationSoundPreference notificationSoundPreference = this.mPreference;
        if (notificationSoundPreference != null) {
            notificationSoundPreference.onActivityResult(i, i2, intent);
        }
        this.mListener.onFieldValueChanged();
        return true;
    }

    protected static boolean hasValidSound(NotificationChannel notificationChannel) {
        return (notificationChannel == null || notificationChannel.getSound() == null || Uri.EMPTY.equals(notificationChannel.getSound())) ? false : true;
    }
}
