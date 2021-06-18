package com.android.settings.inputmethod;

import android.content.Context;
import android.content.IntentFilter;
import android.hardware.input.InputManager;
import android.os.Handler;
import android.provider.Settings;
import android.view.InputDevice;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;

public class GameControllerPreferenceController extends TogglePreferenceController implements PreferenceControllerMixin, InputManager.InputDeviceListener, LifecycleObserver, OnResume, OnPause {
    private final InputManager mIm;
    private Preference mPreference;

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

    public GameControllerPreferenceController(Context context, String str) {
        super(context, str);
        this.mIm = (InputManager) context.getSystemService("input");
    }

    public void onResume() {
        this.mIm.registerInputDeviceListener(this, (Handler) null);
    }

    public void onPause() {
        this.mIm.unregisterInputDeviceListener(this);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    public int getAvailabilityStatus() {
        if (!this.mContext.getResources().getBoolean(R.bool.config_show_vibrate_input_devices)) {
            return 3;
        }
        for (int inputDevice : this.mIm.getInputDeviceIds()) {
            InputDevice inputDevice2 = this.mIm.getInputDevice(inputDevice);
            if (inputDevice2 != null && !inputDevice2.isVirtual() && inputDevice2.getVibrator().hasVibrator()) {
                return 0;
            }
        }
        return 2;
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        if (preference != null) {
            this.mPreference.setVisible(isAvailable());
        }
    }

    public boolean isChecked() {
        if (Settings.System.getInt(this.mContext.getContentResolver(), "vibrate_input_devices", 1) > 0) {
            return true;
        }
        return false;
    }

    public boolean setChecked(boolean z) {
        return Settings.System.putInt(this.mContext.getContentResolver(), "vibrate_input_devices", z ? 1 : 0);
    }

    public void onInputDeviceAdded(int i) {
        updateState(this.mPreference);
    }

    public void onInputDeviceRemoved(int i) {
        updateState(this.mPreference);
    }

    public void onInputDeviceChanged(int i) {
        updateState(this.mPreference);
    }
}
