package com.android.settings.gestures;

import android.content.Context;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;

public class LongPressPowerButtonPreferenceController extends TogglePreferenceController implements LifecycleObserver {
    @VisibleForTesting
    static final String CARDS_AVAILABLE_KEY = "global_actions_panel_available";
    @VisibleForTesting
    static final String CARDS_ENABLED_KEY = "global_actions_panel_enabled";
    @VisibleForTesting
    static final int KEY_CHORD_POWER_VOLUME_UP_GLOBAL_ACTIONS = 2;
    @VisibleForTesting
    static final int KEY_CHORD_POWER_VOLUME_UP_MUTE_TOGGLE = 1;
    private static final String KEY_CHORD_POWER_VOLUME_UP_SETTING = "key_chord_power_volume_up";
    @VisibleForTesting
    static final int LONG_PRESS_POWER_ASSISTANT_VALUE = 5;
    @VisibleForTesting
    static final int LONG_PRESS_POWER_GLOBAL_ACTIONS = 1;
    @VisibleForTesting
    static final int LONG_PRESS_POWER_SHUT_OFF = 2;
    private static final int POWER_BUTTON_LONG_PRESS_DEFAULT_VALUE_RESOURCE = 17694835;
    private static final String POWER_BUTTON_LONG_PRESS_SETTING = "power_button_long_press";
    /* access modifiers changed from: private */
    public static final Uri POWER_BUTTON_LONG_PRESS_SETTING_URI = Settings.Global.getUriFor(POWER_BUTTON_LONG_PRESS_SETTING);
    private SettingObserver mSettingsObserver;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public LongPressPowerButtonPreferenceController(Context context, String str) {
        super(context, str);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mSettingsObserver = new SettingObserver(preferenceScreen.findPreference(getPreferenceKey()));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        SettingObserver settingObserver = this.mSettingsObserver;
        if (settingObserver != null) {
            settingObserver.register();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        SettingObserver settingObserver = this.mSettingsObserver;
        if (settingObserver != null) {
            settingObserver.unregister();
        }
    }

    public int getAvailabilityStatus() {
        return this.mContext.getResources().getBoolean(17891582) ? 0 : 3;
    }

    public boolean isChecked() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), POWER_BUTTON_LONG_PRESS_SETTING, this.mContext.getResources().getInteger(POWER_BUTTON_LONG_PRESS_DEFAULT_VALUE_RESOURCE)) == 5;
    }

    public boolean setChecked(boolean z) {
        if (setPowerLongPressValue(z)) {
            return setPowerVolumeChordValue(z);
        }
        return false;
    }

    private boolean setPowerLongPressValue(boolean z) {
        if (z) {
            return Settings.Global.putInt(this.mContext.getContentResolver(), POWER_BUTTON_LONG_PRESS_SETTING, 5);
        }
        int integer = this.mContext.getResources().getInteger(POWER_BUTTON_LONG_PRESS_DEFAULT_VALUE_RESOURCE);
        if (integer != 5) {
            return Settings.Global.putInt(this.mContext.getContentResolver(), POWER_BUTTON_LONG_PRESS_SETTING, integer);
        }
        return Settings.Global.putInt(this.mContext.getContentResolver(), POWER_BUTTON_LONG_PRESS_SETTING, isCardsOrControlsAvailable() ? 1 : 2);
    }

    private boolean setPowerVolumeChordValue(boolean z) {
        return Settings.Global.putInt(this.mContext.getContentResolver(), KEY_CHORD_POWER_VOLUME_UP_SETTING, z ? 2 : 1);
    }

    private boolean isCardsOrControlsAvailable() {
        boolean z = Settings.Secure.getInt(this.mContext.getContentResolver(), CARDS_AVAILABLE_KEY, 0) != 0;
        boolean hasSystemFeature = this.mContext.getPackageManager().hasSystemFeature("android.software.controls");
        if (z || hasSystemFeature) {
            return true;
        }
        return false;
    }

    private final class SettingObserver extends ContentObserver {
        private final Preference mPreference;

        SettingObserver(Preference preference) {
            super(new Handler(Looper.getMainLooper()));
            this.mPreference = preference;
        }

        public void register() {
            LongPressPowerButtonPreferenceController.this.mContext.getContentResolver().registerContentObserver(LongPressPowerButtonPreferenceController.POWER_BUTTON_LONG_PRESS_SETTING_URI, false, this);
        }

        public void unregister() {
            LongPressPowerButtonPreferenceController.this.mContext.getContentResolver().unregisterContentObserver(this);
        }

        public void onChange(boolean z) {
            LongPressPowerButtonPreferenceController.this.updateState(this.mPreference);
        }
    }
}
