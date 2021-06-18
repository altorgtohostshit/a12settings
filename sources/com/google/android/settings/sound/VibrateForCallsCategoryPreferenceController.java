package com.google.android.settings.sound;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.DeviceConfig;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.android.settings.Utils;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import com.google.android.settings.accessibility.HapticsRingReceiverHelper;

public class VibrateForCallsCategoryPreferenceController extends BasePreferenceController implements LifecycleObserver, OnStart, OnStop {
    static final String RAMPING_RINGER_ENABLED = "ramping_ringer_enabled";
    protected PreferenceCategory mPreferenceCategory;
    protected HapticsRingReceiverHelper mReceiver = new HapticsRingReceiverHelper(this.mContext) {
        public void onChange() {
            VibrateForCallsCategoryPreferenceController.this.updateState((Preference) null);
        }
    };

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

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public VibrateForCallsCategoryPreferenceController(Context context, String str) {
        super(context, str);
    }

    static boolean isVibrateForCallsAvailable(Context context) {
        return Utils.isVoiceCapable(context) && !DeviceConfig.getBoolean("telephony", RAMPING_RINGER_ENABLED, false);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreferenceCategory = (PreferenceCategory) preferenceScreen.findPreference(getPreferenceKey());
    }

    public int getAvailabilityStatus() {
        return isVibrateForCallsAvailable(this.mContext) ? 0 : 3;
    }

    public void onStart() {
        this.mReceiver.register(true);
        updateState((Preference) null);
    }

    public void onStop() {
        this.mReceiver.register(false);
    }

    public void updateState(Preference preference) {
        this.mPreferenceCategory.setVisible(!this.mReceiver.isRingerModeSilent());
    }
}
