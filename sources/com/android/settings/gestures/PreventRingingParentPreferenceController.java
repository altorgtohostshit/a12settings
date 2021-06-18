package com.android.settings.gestures;

import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.widget.PrimarySwitchPreference;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;

public class PreventRingingParentPreferenceController extends TogglePreferenceController implements LifecycleObserver, OnStart, OnStop {
    final String SECURE_KEY = "volume_hush_gesture";
    private PrimarySwitchPreference mPreference;
    private SettingObserver mSettingObserver;

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

    public PreventRingingParentPreferenceController(Context context, String str) {
        super(context, str);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = (PrimarySwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mSettingObserver = new SettingObserver(this.mPreference);
    }

    public boolean isChecked() {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), "volume_hush_gesture", 1) != 0;
    }

    public boolean setChecked(boolean z) {
        int i = 1;
        int i2 = Settings.Secure.getInt(this.mContext.getContentResolver(), "volume_hush_gesture", 1);
        if (i2 != 0) {
            i = i2;
        }
        ContentResolver contentResolver = this.mContext.getContentResolver();
        if (!z) {
            i = 0;
        }
        return Settings.Secure.putInt(contentResolver, "volume_hush_gesture", i);
    }

    public void updateState(Preference preference) {
        CharSequence charSequence;
        super.updateState(preference);
        int i = Settings.Secure.getInt(this.mContext.getContentResolver(), "volume_hush_gesture", 1);
        if (i == 1) {
            charSequence = this.mContext.getText(R.string.prevent_ringing_option_vibrate_summary);
        } else if (i != 2) {
            charSequence = this.mContext.getText(R.string.switch_off_text);
        } else {
            charSequence = this.mContext.getText(R.string.prevent_ringing_option_mute_summary);
        }
        preference.setSummary(charSequence);
    }

    public int getAvailabilityStatus() {
        return this.mContext.getResources().getBoolean(17891694) ? 0 : 3;
    }

    public void onStart() {
        SettingObserver settingObserver = this.mSettingObserver;
        if (settingObserver != null) {
            settingObserver.register(this.mContext.getContentResolver());
            this.mSettingObserver.onChange(false, (Uri) null);
        }
    }

    public void onStop() {
        SettingObserver settingObserver = this.mSettingObserver;
        if (settingObserver != null) {
            settingObserver.unregister(this.mContext.getContentResolver());
        }
    }

    private class SettingObserver extends ContentObserver {
        private final Preference mPreference;
        private final Uri mVolumeHushGestureUri = Settings.Secure.getUriFor("volume_hush_gesture");

        SettingObserver(Preference preference) {
            super(new Handler());
            this.mPreference = preference;
        }

        public void register(ContentResolver contentResolver) {
            contentResolver.registerContentObserver(this.mVolumeHushGestureUri, false, this);
        }

        public void unregister(ContentResolver contentResolver) {
            contentResolver.unregisterContentObserver(this);
        }

        public void onChange(boolean z, Uri uri) {
            super.onChange(z, uri);
            if (uri == null || this.mVolumeHushGestureUri.equals(uri)) {
                PreventRingingParentPreferenceController.this.updateState(this.mPreference);
            }
        }
    }
}
