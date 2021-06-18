package com.google.android.settings.aware;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.DeviceConfig;
import android.provider.Settings;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.aware.AwareFeatureProvider;
import com.android.settings.overlay.FeatureFactory;

public class AwareHelper {
    @VisibleForTesting
    static final String FLAG_TAP_ENABLE = "enable_tap";
    @VisibleForTesting
    static final String NAMESPACE = "oslo";
    private final String SHARE_PERFS = "aware_settings";
    /* access modifiers changed from: private */
    public final Context mContext;
    private final AwareFeatureProvider mFeatureProvider;
    private final SettingsObserver mSettingsObserver;

    public interface Callback {
        void onChange(Uri uri);
    }

    public AwareHelper(Context context) {
        this.mContext = context;
        this.mSettingsObserver = new SettingsObserver(new Handler(Looper.getMainLooper()));
        this.mFeatureProvider = FeatureFactory.getFactory(context).getAwareFeatureProvider();
    }

    public boolean isGestureConfigurable() {
        return isEnabled() && isAvailable();
    }

    public boolean isAvailable() {
        return isSupported() && !isAirplaneModeOn() && !isBatterySaverModeOn();
    }

    public boolean isSupported() {
        return this.mFeatureProvider.isSupported(this.mContext);
    }

    public boolean isEnabled() {
        return this.mFeatureProvider.isEnabled(this.mContext);
    }

    public void register(Callback callback) {
        this.mSettingsObserver.observe();
        this.mSettingsObserver.setCallback(callback);
    }

    public void unregister() {
        this.mContext.getContentResolver().unregisterContentObserver(this.mSettingsObserver);
    }

    public void writeFeatureEnabled(String str, boolean z) {
        this.mContext.getSharedPreferences("aware_settings", 0).edit().putBoolean(str, z).apply();
    }

    public boolean readFeatureEnabled(String str) {
        return this.mContext.getSharedPreferences("aware_settings", 0).getBoolean(str, true);
    }

    public static boolean isTapAvailableOnTheDevice() {
        return DeviceConfig.getBoolean(NAMESPACE, FLAG_TAP_ENABLE, true);
    }

    /* access modifiers changed from: package-private */
    public boolean isAirplaneModeOn() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0) == 1;
    }

    /* access modifiers changed from: package-private */
    public boolean isBatterySaverModeOn() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "low_power", 0) == 1;
    }

    private final class SettingsObserver extends ContentObserver {
        private final Uri mAirplaneMode = Settings.Global.getUriFor("airplane_mode_on");
        private final Uri mAwareAllowed = Settings.Global.getUriFor("aware_allowed");
        private final Uri mAwareEnabled = Settings.Secure.getUriFor("aware_enabled");
        private final Uri mBatterySaver = Settings.Global.getUriFor("low_power");
        private Callback mCallback;

        public SettingsObserver(Handler handler) {
            super(handler);
        }

        /* access modifiers changed from: private */
        public void setCallback(Callback callback) {
            this.mCallback = callback;
        }

        public void observe() {
            ContentResolver contentResolver = AwareHelper.this.mContext.getContentResolver();
            contentResolver.registerContentObserver(this.mAwareEnabled, false, this);
            contentResolver.registerContentObserver(this.mAwareAllowed, false, this);
            contentResolver.registerContentObserver(this.mAirplaneMode, false, this);
            contentResolver.registerContentObserver(this.mBatterySaver, false, this);
        }

        public void onChange(boolean z, Uri uri) {
            Callback callback = this.mCallback;
            if (callback != null) {
                callback.onChange(uri);
            }
        }
    }
}
