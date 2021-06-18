package com.android.settings.notification;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;

public abstract class SettingPrefController extends AbstractPreferenceController implements PreferenceControllerMixin, LifecycleObserver, OnResume, OnPause {
    private SettingsPreferenceFragment mParent;
    protected SettingPref mPreference;
    protected SettingsObserver mSettingsObserver;

    public SettingPrefController(Context context, SettingsPreferenceFragment settingsPreferenceFragment, Lifecycle lifecycle) {
        super(context);
        this.mParent = settingsPreferenceFragment;
        if (lifecycle != null) {
            lifecycle.addObserver(this);
        }
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        this.mPreference.init(this.mParent);
        super.displayPreference(preferenceScreen);
        if (isAvailable()) {
            this.mSettingsObserver = new SettingsObserver();
        }
    }

    public String getPreferenceKey() {
        return this.mPreference.getKey();
    }

    public boolean isAvailable() {
        return this.mPreference.isApplicable(this.mContext);
    }

    public void updateState(Preference preference) {
        this.mPreference.update(this.mContext);
    }

    public void onResume() {
        SettingsObserver settingsObserver = this.mSettingsObserver;
        if (settingsObserver != null) {
            settingsObserver.register(true);
        }
    }

    public void onPause() {
        SettingsObserver settingsObserver = this.mSettingsObserver;
        if (settingsObserver != null) {
            settingsObserver.register(false);
        }
    }

    final class SettingsObserver extends ContentObserver {
        public SettingsObserver() {
            super(new Handler());
        }

        public void register(boolean z) {
            ContentResolver contentResolver = SettingPrefController.this.mContext.getContentResolver();
            if (z) {
                contentResolver.registerContentObserver(SettingPrefController.this.mPreference.getUri(), false, this);
            } else {
                contentResolver.unregisterContentObserver(this);
            }
        }

        public void onChange(boolean z, Uri uri) {
            super.onChange(z, uri);
            if (SettingPrefController.this.mPreference.getUri().equals(uri)) {
                SettingPrefController settingPrefController = SettingPrefController.this;
                settingPrefController.mPreference.update(settingPrefController.mContext);
            }
        }
    }
}
