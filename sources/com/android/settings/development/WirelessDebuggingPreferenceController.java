package com.android.settings.development;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.debug.IAdbManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.widget.PrimarySwitchPreference;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;
import com.android.settingslib.development.DeveloperOptionsPreferenceController;

public class WirelessDebuggingPreferenceController extends DeveloperOptionsPreferenceController implements Preference.OnPreferenceChangeListener, PreferenceControllerMixin, LifecycleObserver, OnResume, OnPause {
    private final IAdbManager mAdbManager;
    private final ContentResolver mContentResolver;
    private final Handler mHandler;
    private final ContentObserver mSettingsObserver;

    public String getPreferenceKey() {
        return "toggle_adb_wireless";
    }

    public WirelessDebuggingPreferenceController(Context context, Lifecycle lifecycle) {
        super(context);
        Handler handler = new Handler(Looper.getMainLooper());
        this.mHandler = handler;
        if (lifecycle != null) {
            lifecycle.addObserver(this);
        }
        this.mAdbManager = IAdbManager.Stub.asInterface(ServiceManager.getService("adb"));
        this.mSettingsObserver = new ContentObserver(handler) {
            public void onChange(boolean z, Uri uri) {
                WirelessDebuggingPreferenceController wirelessDebuggingPreferenceController = WirelessDebuggingPreferenceController.this;
                wirelessDebuggingPreferenceController.updateState(wirelessDebuggingPreferenceController.mPreference);
            }
        };
        this.mContentResolver = context.getContentResolver();
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
    }

    public boolean isAvailable() {
        try {
            return this.mAdbManager.isAdbWifiSupported();
        } catch (RemoteException e) {
            Log.e("WirelessDebugPrefCtrl", "Unable to check if adb wifi is supported.", e);
            return false;
        }
    }

    /* access modifiers changed from: protected */
    public void onDeveloperOptionsSwitchEnabled() {
        super.onDeveloperOptionsSwitchEnabled();
        this.mPreference.setEnabled(true);
    }

    /* access modifiers changed from: protected */
    public void onDeveloperOptionsSwitchDisabled() {
        super.onDeveloperOptionsSwitchDisabled();
        this.mPreference.setEnabled(false);
        Settings.Global.putInt(this.mContext.getContentResolver(), "adb_wifi_enabled", 0);
    }

    public void onResume() {
        this.mContentResolver.registerContentObserver(Settings.Global.getUriFor("adb_wifi_enabled"), false, this.mSettingsObserver);
    }

    public void onPause() {
        this.mContentResolver.unregisterContentObserver(this.mSettingsObserver);
    }

    public void updateState(Preference preference) {
        boolean z = false;
        if (Settings.Global.getInt(this.mContentResolver, "adb_wifi_enabled", 0) != 0) {
            z = true;
        }
        ((PrimarySwitchPreference) preference).setChecked(z);
    }

    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null) {
            return false;
        }
        for (Network networkCapabilities : connectivityManager.getAllNetworks()) {
            NetworkCapabilities networkCapabilities2 = connectivityManager.getNetworkCapabilities(networkCapabilities);
            if (networkCapabilities2 != null && networkCapabilities2.hasTransport(1)) {
                return true;
            }
        }
        return false;
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        boolean booleanValue = ((Boolean) obj).booleanValue();
        if (!booleanValue || isWifiConnected(this.mContext)) {
            Settings.Global.putInt(this.mContext.getContentResolver(), "adb_wifi_enabled", booleanValue ? 1 : 0);
            return true;
        }
        Toast.makeText(this.mContext, R.string.adb_wireless_no_network_msg, 1).show();
        return false;
    }
}
