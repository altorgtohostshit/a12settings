package com.android.settings.gestures;

import android.content.Context;
import android.content.IntentFilter;
import android.net.Uri;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.gestures.OneHandedSettingsUtils;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;

public class OneHandedAppTapsExitPreferenceController extends TogglePreferenceController implements LifecycleObserver, OnStart, OnStop, OneHandedSettingsUtils.TogglesCallback {
    private Preference mPreference;
    private final OneHandedSettingsUtils mUtils;

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

    public OneHandedAppTapsExitPreferenceController(Context context, String str) {
        super(context, str);
        this.mUtils = new OneHandedSettingsUtils(context);
        OneHandedSettingsUtils.setTapsAppToExitEnabled(this.mContext, isChecked());
    }

    public int getAvailabilityStatus() {
        return OneHandedSettingsUtils.isOneHandedModeEnabled(this.mContext) ? 0 : 5;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        int availabilityStatus = getAvailabilityStatus();
        boolean z = true;
        if (!(availabilityStatus == 0 || availabilityStatus == 1)) {
            z = false;
        }
        preference.setEnabled(z);
    }

    public boolean setChecked(boolean z) {
        return OneHandedSettingsUtils.setTapsAppToExitEnabled(this.mContext, z);
    }

    public boolean isChecked() {
        return OneHandedSettingsUtils.isTapsAppToExitEnabled(this.mContext);
    }

    public void onStart() {
        this.mUtils.registerToggleAwareObserver(this);
    }

    public void onStop() {
        this.mUtils.unregisterToggleAwareObserver();
    }

    public void onChange(Uri uri) {
        updateState(this.mPreference);
    }
}
