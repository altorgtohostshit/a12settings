package com.google.android.settings.accessibility;

import android.content.Context;
import android.content.IntentFilter;
import android.view.View;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;

public class HapticsErrorInfoPreferenceController extends BasePreferenceController implements LifecycleObserver, OnStart, OnStop {
    private Context mContext;
    private View mInfoIcon;
    /* access modifiers changed from: private */
    public Preference mPreference;
    private HapticsRingReceiverHelper mReceiver;
    private View mTitle;

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

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public HapticsErrorInfoPreferenceController(Context context, String str) {
        super(context, str);
        this.mContext = context;
        init();
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        Preference findPreference = preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = findPreference;
        updateState(findPreference);
    }

    public void onStart() {
        updateState(this.mPreference);
        this.mReceiver.register(true);
    }

    public void onStop() {
        this.mReceiver.register(false);
    }

    public void updateState(Preference preference) {
        this.mPreference.setVisible(this.mReceiver.isRingerModeSilent());
    }

    private void init() {
        this.mReceiver = new HapticsRingReceiverHelper(this.mContext) {
            public void onChange() {
                HapticsErrorInfoPreferenceController hapticsErrorInfoPreferenceController = HapticsErrorInfoPreferenceController.this;
                hapticsErrorInfoPreferenceController.updateState(hapticsErrorInfoPreferenceController.mPreference);
            }
        };
    }
}
