package com.android.settings.notification;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.widget.SettingsMainSwitchPreferenceController;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;

public class BubbleNotificationPreferenceController extends SettingsMainSwitchPreferenceController implements LifecycleObserver, OnResume, OnPause {
    static final int OFF = 0;

    /* renamed from: ON */
    static final int f88ON = 1;
    private static final String TAG = "BubbleNotifPrefContr";
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

    public boolean isSliceable() {
        return false;
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public BubbleNotificationPreferenceController(Context context, String str) {
        super(context, str);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        if (this.mSwitchPreference != null) {
            this.mSettingObserver = new SettingObserver(this.mSwitchPreference);
        }
    }

    public void onResume() {
        SettingObserver settingObserver = this.mSettingObserver;
        if (settingObserver != null) {
            settingObserver.register(this.mContext.getContentResolver(), true);
        }
    }

    public void onPause() {
        SettingObserver settingObserver = this.mSettingObserver;
        if (settingObserver != null) {
            settingObserver.register(this.mContext.getContentResolver(), false);
        }
    }

    public int getAvailabilityStatus() {
        return ((ActivityManager) this.mContext.getSystemService(ActivityManager.class)).isLowRamDevice() ? 3 : 0;
    }

    public boolean isChecked() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "notification_bubbles", 1) == 1;
    }

    public boolean setChecked(boolean z) {
        Settings.Global.putInt(this.mContext.getContentResolver(), "notification_bubbles", z ? 1 : 0);
        return true;
    }

    class SettingObserver extends ContentObserver {
        private final Uri NOTIFICATION_BUBBLES_URI = Settings.Secure.getUriFor("notification_bubbles");
        private final Preference mPreference;

        SettingObserver(Preference preference) {
            super(new Handler());
            this.mPreference = preference;
        }

        public void register(ContentResolver contentResolver, boolean z) {
            if (z) {
                contentResolver.registerContentObserver(this.NOTIFICATION_BUBBLES_URI, false, this);
            } else {
                contentResolver.unregisterContentObserver(this);
            }
        }

        public void onChange(boolean z, Uri uri) {
            super.onChange(z, uri);
            if (this.NOTIFICATION_BUBBLES_URI.equals(uri)) {
                BubbleNotificationPreferenceController.this.updateState(this.mPreference);
            }
        }
    }
}
