package com.android.settings.display;

import android.content.Context;
import android.content.IntentFilter;
import android.hardware.display.DisplayManager;
import android.os.Handler;
import android.provider.DeviceConfig;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import java.util.concurrent.Executor;

public class PeakRefreshRatePreferenceController extends TogglePreferenceController implements LifecycleObserver, OnStart, OnStop {
    static float DEFAULT_REFRESH_RATE = 60.0f;
    private static final float INVALIDATE_REFRESH_RATE = -1.0f;
    private static final String TAG = "RefreshRatePrefCtr";
    private final DeviceConfigDisplaySettings mDeviceConfigDisplaySettings = new DeviceConfigDisplaySettings();
    /* access modifiers changed from: private */
    public final Handler mHandler;
    /* access modifiers changed from: private */
    public final IDeviceConfigChange mOnDeviceConfigChange = new IDeviceConfigChange() {
        public void onDefaultRefreshRateChanged() {
            PeakRefreshRatePreferenceController peakRefreshRatePreferenceController = PeakRefreshRatePreferenceController.this;
            peakRefreshRatePreferenceController.updateState(peakRefreshRatePreferenceController.mPreference);
        }
    };
    float mPeakRefreshRate;
    /* access modifiers changed from: private */
    public Preference mPreference;

    private interface IDeviceConfigChange {
        void onDefaultRefreshRateChanged();
    }

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

    public PeakRefreshRatePreferenceController(Context context, String str) {
        super(context, str);
        this.mHandler = new Handler(context.getMainLooper());
        Display display = ((DisplayManager) this.mContext.getSystemService(DisplayManager.class)).getDisplay(0);
        if (display == null) {
            Log.w(TAG, "No valid default display device");
            this.mPeakRefreshRate = DEFAULT_REFRESH_RATE;
        } else {
            this.mPeakRefreshRate = findPeakRefreshRate(display.getSupportedModes());
        }
        Log.d(TAG, "DEFAULT_REFRESH_RATE : " + DEFAULT_REFRESH_RATE + " mPeakRefreshRate : " + this.mPeakRefreshRate);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    public int getAvailabilityStatus() {
        if (!this.mContext.getResources().getBoolean(R.bool.config_show_smooth_display) || this.mPeakRefreshRate <= DEFAULT_REFRESH_RATE) {
            return 3;
        }
        return 0;
    }

    public boolean isChecked() {
        return Math.round(Settings.System.getFloat(this.mContext.getContentResolver(), "peak_refresh_rate", getDefaultPeakRefreshRate())) == Math.round(this.mPeakRefreshRate);
    }

    public boolean setChecked(boolean z) {
        float f = z ? this.mPeakRefreshRate : DEFAULT_REFRESH_RATE;
        Log.d(TAG, "setChecked to : " + f);
        return Settings.System.putFloat(this.mContext.getContentResolver(), "peak_refresh_rate", f);
    }

    public void onStart() {
        this.mDeviceConfigDisplaySettings.startListening();
    }

    public void onStop() {
        this.mDeviceConfigDisplaySettings.stopListening();
    }

    /* access modifiers changed from: package-private */
    public float findPeakRefreshRate(Display.Mode[] modeArr) {
        float f = DEFAULT_REFRESH_RATE;
        for (Display.Mode mode : modeArr) {
            if (((float) Math.round(mode.getRefreshRate())) > f) {
                f = mode.getRefreshRate();
            }
        }
        return f;
    }

    private class DeviceConfigDisplaySettings implements DeviceConfig.OnPropertiesChangedListener, Executor {
        private DeviceConfigDisplaySettings() {
        }

        public void startListening() {
            DeviceConfig.addOnPropertiesChangedListener("display_manager", this, this);
        }

        public void stopListening() {
            DeviceConfig.removeOnPropertiesChangedListener(this);
        }

        public float getDefaultPeakRefreshRate() {
            float f = DeviceConfig.getFloat("display_manager", "peak_refresh_rate_default", PeakRefreshRatePreferenceController.INVALIDATE_REFRESH_RATE);
            Log.d(PeakRefreshRatePreferenceController.TAG, "DeviceConfig getDefaultPeakRefreshRate : " + f);
            return f;
        }

        public void onPropertiesChanged(DeviceConfig.Properties properties) {
            if (PeakRefreshRatePreferenceController.this.mOnDeviceConfigChange != null) {
                PeakRefreshRatePreferenceController.this.mOnDeviceConfigChange.onDefaultRefreshRateChanged();
                PeakRefreshRatePreferenceController peakRefreshRatePreferenceController = PeakRefreshRatePreferenceController.this;
                peakRefreshRatePreferenceController.updateState(peakRefreshRatePreferenceController.mPreference);
            }
        }

        public void execute(Runnable runnable) {
            if (PeakRefreshRatePreferenceController.this.mHandler != null) {
                PeakRefreshRatePreferenceController.this.mHandler.post(runnable);
            }
        }
    }

    private float getDefaultPeakRefreshRate() {
        float defaultPeakRefreshRate = this.mDeviceConfigDisplaySettings.getDefaultPeakRefreshRate();
        if (defaultPeakRefreshRate == INVALIDATE_REFRESH_RATE) {
            defaultPeakRefreshRate = (float) this.mContext.getResources().getInteger(17694783);
        }
        Log.d(TAG, "DeviceConfig getDefaultPeakRefreshRate : " + defaultPeakRefreshRate);
        return defaultPeakRefreshRate;
    }
}
