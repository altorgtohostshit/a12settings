package com.android.settings.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.settings.R;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.utils.AnnotationSpan;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;

public class WifiWakeupPreferenceController extends TogglePreferenceController implements LifecycleObserver, OnPause, OnResume {
    private static final String KEY_ENABLE_WIFI_WAKEUP = "enable_wifi_wakeup";
    private static final String TAG = "WifiWakeupPrefController";
    private Fragment mFragment;
    private final IntentFilter mLocationFilter = new IntentFilter("android.location.MODE_CHANGED");
    LocationManager mLocationManager;
    private final BroadcastReceiver mLocationReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            WifiWakeupPreferenceController wifiWakeupPreferenceController = WifiWakeupPreferenceController.this;
            wifiWakeupPreferenceController.updateState(wifiWakeupPreferenceController.mPreference);
        }
    };
    SwitchPreference mPreference;
    WifiManager mWifiManager;

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

    public WifiWakeupPreferenceController(Context context) {
        super(context, KEY_ENABLE_WIFI_WAKEUP);
        this.mLocationManager = (LocationManager) context.getSystemService("location");
        this.mWifiManager = (WifiManager) context.getSystemService(WifiManager.class);
    }

    public void setFragment(Fragment fragment) {
        this.mFragment = fragment;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = (SwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
    }

    public boolean isChecked() {
        return getWifiWakeupEnabled() && getWifiScanningEnabled() && this.mLocationManager.isLocationEnabled();
    }

    public boolean setChecked(boolean z) {
        if (z) {
            if (this.mFragment == null) {
                throw new IllegalStateException("No fragment to start activity");
            } else if (!this.mLocationManager.isLocationEnabled()) {
                this.mFragment.startActivityForResult(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"), 600);
                return false;
            } else if (!getWifiScanningEnabled()) {
                showScanningDialog();
                return false;
            }
        }
        setWifiWakeupEnabled(z);
        return true;
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        refreshSummary(preference);
    }

    public CharSequence getSummary() {
        if (!this.mLocationManager.isLocationEnabled()) {
            return getNoLocationSummary();
        }
        return this.mContext.getText(R.string.wifi_wakeup_summary);
    }

    /* access modifiers changed from: package-private */
    public CharSequence getNoLocationSummary() {
        AnnotationSpan.LinkInfo linkInfo = new AnnotationSpan.LinkInfo("link", (View.OnClickListener) null);
        return AnnotationSpan.linkify(this.mContext.getText(R.string.wifi_wakeup_summary_no_location), linkInfo);
    }

    public void onActivityResult(int i, int i2) {
        if (i == 600 && this.mLocationManager.isLocationEnabled() && getWifiScanningEnabled()) {
            setWifiWakeupEnabled(true);
            updateState(this.mPreference);
        }
    }

    private boolean getWifiScanningEnabled() {
        return this.mWifiManager.isScanAlwaysAvailable();
    }

    private void showScanningDialog() {
        WifiScanningRequiredFragment newInstance = WifiScanningRequiredFragment.newInstance();
        newInstance.setTargetFragment(this.mFragment, 600);
        newInstance.show(this.mFragment.getFragmentManager(), TAG);
    }

    private boolean getWifiWakeupEnabled() {
        return this.mWifiManager.isAutoWakeupEnabled();
    }

    private void setWifiWakeupEnabled(boolean z) {
        this.mWifiManager.setAutoWakeupEnabled(z);
    }

    public void onResume() {
        this.mContext.registerReceiver(this.mLocationReceiver, this.mLocationFilter);
    }

    public void onPause() {
        this.mContext.unregisterReceiver(this.mLocationReceiver);
    }
}
