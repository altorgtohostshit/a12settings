package com.android.settings.display;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.hardware.display.BrightnessInfo;
import android.hardware.display.DisplayManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.Settings;
import android.service.vr.IVrManager;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import com.android.settingslib.display.BrightnessUtils;
import java.text.NumberFormat;

public class BrightnessLevelPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin, LifecycleObserver, OnStart, OnStop {
    private static final Uri BRIGHTNESS_ADJ_URI = Settings.System.getUriFor("screen_auto_brightness_adj");
    private static final Uri BRIGHTNESS_FOR_VR_URI = Settings.System.getUriFor("screen_brightness_for_vr");
    private ContentObserver mBrightnessObserver;
    private final ContentResolver mContentResolver;
    private final DisplayManager.DisplayListener mDisplayListener = new DisplayManager.DisplayListener() {
        public void onDisplayAdded(int i) {
        }

        public void onDisplayRemoved(int i) {
        }

        public void onDisplayChanged(int i) {
            BrightnessLevelPreferenceController brightnessLevelPreferenceController = BrightnessLevelPreferenceController.this;
            brightnessLevelPreferenceController.updatedSummary(brightnessLevelPreferenceController.mPreference);
        }
    };
    private final DisplayManager mDisplayManager;
    private final Handler mHandler;
    private final float mMaxVrBrightness;
    private final float mMinVrBrightness;
    /* access modifiers changed from: private */
    public Preference mPreference;

    private double getPercentage(double d, int i, int i2) {
        if (d > ((double) i2)) {
            return 1.0d;
        }
        double d2 = (double) i;
        if (d < d2) {
            return 0.0d;
        }
        return (d - d2) / ((double) (i2 - i));
    }

    public String getPreferenceKey() {
        return "brightness";
    }

    public boolean isAvailable() {
        return true;
    }

    public BrightnessLevelPreferenceController(Context context, Lifecycle lifecycle) {
        super(context);
        Handler handler = new Handler(Looper.getMainLooper());
        this.mHandler = handler;
        this.mBrightnessObserver = new ContentObserver(handler) {
            public void onChange(boolean z) {
                BrightnessLevelPreferenceController brightnessLevelPreferenceController = BrightnessLevelPreferenceController.this;
                brightnessLevelPreferenceController.updatedSummary(brightnessLevelPreferenceController.mPreference);
            }
        };
        this.mDisplayManager = (DisplayManager) context.getSystemService(DisplayManager.class);
        if (lifecycle != null) {
            lifecycle.addObserver(this);
        }
        PowerManager powerManager = (PowerManager) context.getSystemService(PowerManager.class);
        this.mMinVrBrightness = powerManager.getBrightnessConstraint(5);
        this.mMaxVrBrightness = powerManager.getBrightnessConstraint(6);
        this.mContentResolver = this.mContext.getContentResolver();
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference("brightness");
    }

    public void updateState(Preference preference) {
        updatedSummary(preference);
    }

    public void onStart() {
        this.mContentResolver.registerContentObserver(BRIGHTNESS_FOR_VR_URI, false, this.mBrightnessObserver);
        this.mContentResolver.registerContentObserver(BRIGHTNESS_ADJ_URI, false, this.mBrightnessObserver);
        this.mDisplayManager.registerDisplayListener(this.mDisplayListener, this.mHandler, 8);
    }

    public void onStop() {
        this.mContentResolver.unregisterContentObserver(this.mBrightnessObserver);
        this.mDisplayManager.unregisterDisplayListener(this.mDisplayListener);
    }

    /* access modifiers changed from: private */
    public void updatedSummary(Preference preference) {
        if (preference != null) {
            preference.setSummary((CharSequence) NumberFormat.getPercentInstance().format(getCurrentBrightness()));
        }
    }

    private double getCurrentBrightness() {
        int i;
        if (isInVrMode()) {
            i = BrightnessUtils.convertLinearToGammaFloat(Settings.System.getFloat(this.mContentResolver, "screen_brightness_for_vr_float", this.mMaxVrBrightness), this.mMinVrBrightness, this.mMaxVrBrightness);
        } else {
            BrightnessInfo brightnessInfo = this.mContext.getDisplay().getBrightnessInfo();
            i = brightnessInfo != null ? BrightnessUtils.convertLinearToGammaFloat(brightnessInfo.brightness, brightnessInfo.brightnessMinimum, brightnessInfo.brightnessMaximum) : 0;
        }
        return getPercentage((double) i, 0, 65535);
    }

    /* access modifiers changed from: package-private */
    public IVrManager safeGetVrManager() {
        return IVrManager.Stub.asInterface(ServiceManager.getService("vrmanager"));
    }

    /* access modifiers changed from: package-private */
    public boolean isInVrMode() {
        IVrManager safeGetVrManager = safeGetVrManager();
        if (safeGetVrManager == null) {
            return false;
        }
        try {
            return safeGetVrManager.getVrModeState();
        } catch (RemoteException e) {
            Log.e("BrightnessPrefCtrl", "Failed to check vr mode!", e);
            return false;
        }
    }
}
