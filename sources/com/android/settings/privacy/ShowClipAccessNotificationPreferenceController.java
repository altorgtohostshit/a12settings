package com.android.settings.privacy;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.DeviceConfig;
import android.provider.Settings;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;

public class ShowClipAccessNotificationPreferenceController extends TogglePreferenceController implements LifecycleObserver {
    private static final String KEY_SHOW_CLIP_ACCESS_NOTIFICATION = "show_clip_access_notification";
    private boolean mDefault;
    private final DeviceConfig.OnPropertiesChangedListener mDeviceConfigListener = new C1241x5bd58b62(this);
    private Preference mPreference;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public int getAvailabilityStatus() {
        return 0;
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

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(DeviceConfig.Properties properties) {
        updateConfig();
    }

    public ShowClipAccessNotificationPreferenceController(Context context) {
        super(context, KEY_SHOW_CLIP_ACCESS_NOTIFICATION);
        updateConfig();
    }

    public boolean isChecked() {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), "clipboard_show_access_notifications", this.mDefault ? 1 : 0) != 0;
    }

    public boolean setChecked(boolean z) {
        Settings.Secure.putInt(this.mContext.getContentResolver(), "clipboard_show_access_notifications", z ? 1 : 0);
        return true;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        DeviceConfig.addOnPropertiesChangedListener("clipboard", this.mContext.getMainExecutor(), this.mDeviceConfigListener);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        DeviceConfig.removeOnPropertiesChangedListener(this.mDeviceConfigListener);
    }

    private void updateConfig() {
        this.mDefault = DeviceConfig.getBoolean("clipboard", "show_access_notifications", true);
        updateState(this.mPreference);
    }
}
