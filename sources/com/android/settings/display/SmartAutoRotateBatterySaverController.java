package com.android.settings.display;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import android.view.View;
import androidx.lifecycle.LifecycleObserver;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import com.android.settingslib.widget.BannerMessagePreference;

public class SmartAutoRotateBatterySaverController extends BasePreferenceController implements LifecycleObserver, OnStart, OnStop {
    private final PowerManager mPowerManager;
    /* access modifiers changed from: private */
    public Preference mPreference;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (SmartAutoRotateBatterySaverController.this.mPreference != null) {
                SmartAutoRotateBatterySaverController.this.mPreference.setVisible(SmartAutoRotateBatterySaverController.this.isPowerSaveMode());
                SmartAutoRotateBatterySaverController smartAutoRotateBatterySaverController = SmartAutoRotateBatterySaverController.this;
                smartAutoRotateBatterySaverController.updateState(smartAutoRotateBatterySaverController.mPreference);
            }
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

    public SmartAutoRotateBatterySaverController(Context context, String str) {
        super(context, str);
        this.mPowerManager = (PowerManager) context.getSystemService(PowerManager.class);
    }

    /* access modifiers changed from: package-private */
    public boolean isPowerSaveMode() {
        return this.mPowerManager.isPowerSaveMode();
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        Preference findPreference = preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = findPreference;
        ((BannerMessagePreference) findPreference).setPositiveButtonText(R.string.disable_text).setPositiveButtonOnClickListener(new SmartAutoRotateBatterySaverController$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$displayPreference$0(View view) {
        this.mPowerManager.setPowerSaveModeEnabled(false);
    }

    public void onStart() {
        this.mContext.registerReceiver(this.mReceiver, new IntentFilter("android.os.action.POWER_SAVE_MODE_CHANGED"));
    }

    public void onStop() {
        this.mContext.unregisterReceiver(this.mReceiver);
    }

    public int getAvailabilityStatus() {
        return (!SmartAutoRotateController.isRotationResolverServiceAvailable(this.mContext) || !isPowerSaveMode()) ? 3 : 1;
    }
}
