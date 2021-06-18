package com.android.settings.notification.app;

import android.content.Context;
import android.os.Vibrator;
import androidx.preference.Preference;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.notification.NotificationBackend;
import com.android.settingslib.RestrictedSwitchPreference;

public class VibrationPreferenceController extends NotificationPreferenceController implements PreferenceControllerMixin, Preference.OnPreferenceChangeListener {
    private final Vibrator mVibrator;

    public String getPreferenceKey() {
        return "vibrate";
    }

    public VibrationPreferenceController(Context context, NotificationBackend notificationBackend) {
        super(context, notificationBackend);
        this.mVibrator = (Vibrator) context.getSystemService("vibrator");
    }

    public boolean isAvailable() {
        Vibrator vibrator;
        if (!super.isAvailable() || this.mChannel == null || !checkCanBeVisible(3) || isDefaultChannel() || (vibrator = this.mVibrator) == null || !vibrator.hasVibrator()) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean isIncludedInFilter() {
        return this.mPreferenceFilter.contains("vibration");
    }

    public void updateState(Preference preference) {
        if (this.mChannel != null) {
            RestrictedSwitchPreference restrictedSwitchPreference = (RestrictedSwitchPreference) preference;
            restrictedSwitchPreference.setDisabledByAdmin(this.mAdmin);
            restrictedSwitchPreference.setEnabled(!restrictedSwitchPreference.isDisabledByAdmin());
            restrictedSwitchPreference.setChecked(this.mChannel.shouldVibrate());
        }
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        if (this.mChannel == null) {
            return true;
        }
        this.mChannel.enableVibration(((Boolean) obj).booleanValue());
        saveChannel();
        return true;
    }
}
