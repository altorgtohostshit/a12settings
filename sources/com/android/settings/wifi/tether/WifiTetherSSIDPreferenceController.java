package com.android.settings.wifi.tether;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.SoftApConfiguration;
import android.text.TextUtils;
import android.util.FeatureFlagUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.widget.ValidatedEditTextPreference;
import com.android.settings.wifi.dpp.WifiDppUtils;
import com.android.settings.wifi.tether.WifiTetherBasePreferenceController;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;

public class WifiTetherSSIDPreferenceController extends WifiTetherBasePreferenceController implements ValidatedEditTextPreference.Validator {
    static final String DEFAULT_SSID = "AndroidAP";
    private final MetricsFeatureProvider mMetricsFeatureProvider;
    private String mSSID;
    private WifiDeviceNameTextValidator mWifiDeviceNameTextValidator = new WifiDeviceNameTextValidator();

    WifiTetherSSIDPreferenceController(Context context, WifiTetherBasePreferenceController.OnTetherConfigUpdateListener onTetherConfigUpdateListener, MetricsFeatureProvider metricsFeatureProvider) {
        super(context, onTetherConfigUpdateListener);
        this.mMetricsFeatureProvider = metricsFeatureProvider;
    }

    public WifiTetherSSIDPreferenceController(Context context, WifiTetherBasePreferenceController.OnTetherConfigUpdateListener onTetherConfigUpdateListener) {
        super(context, onTetherConfigUpdateListener);
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(context).getMetricsFeatureProvider();
    }

    public String getPreferenceKey() {
        return FeatureFlagUtils.isEnabled(this.mContext, "settings_tether_all_in_one") ? "wifi_tether_network_name_2" : "wifi_tether_network_name";
    }

    public void updateDisplay() {
        SoftApConfiguration softApConfiguration = this.mWifiManager.getSoftApConfiguration();
        if (softApConfiguration != null) {
            this.mSSID = softApConfiguration.getSsid();
        } else {
            this.mSSID = DEFAULT_SSID;
        }
        ((ValidatedEditTextPreference) this.mPreference).setValidator(this);
        if (!this.mWifiManager.isWifiApEnabled() || softApConfiguration == null) {
            ((WifiTetherSsidPreference) this.mPreference).setButtonVisible(false);
        } else {
            Intent hotspotConfiguratorIntentOrNull = WifiDppUtils.getHotspotConfiguratorIntentOrNull(this.mContext, this.mWifiManager, softApConfiguration);
            if (hotspotConfiguratorIntentOrNull == null) {
                Log.e("WifiTetherSsidPref", "Invalid security to share hotspot");
                ((WifiTetherSsidPreference) this.mPreference).setButtonVisible(false);
            } else {
                ((WifiTetherSsidPreference) this.mPreference).setButtonOnClickListener(new WifiTetherSSIDPreferenceController$$ExternalSyntheticLambda0(this, hotspotConfiguratorIntentOrNull));
                ((WifiTetherSsidPreference) this.mPreference).setButtonVisible(true);
            }
        }
        updateSsidDisplay((EditTextPreference) this.mPreference);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDisplay$0(Intent intent, View view) {
        shareHotspotNetwork(intent);
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        String str = (String) obj;
        if (!TextUtils.equals(this.mSSID, str)) {
            this.mMetricsFeatureProvider.action(this.mContext, 1736, (Pair<Integer, Object>[]) new Pair[0]);
        }
        this.mSSID = str;
        updateSsidDisplay((EditTextPreference) preference);
        this.mListener.onTetherConfigUpdated(this);
        return true;
    }

    public boolean isTextValid(String str) {
        return this.mWifiDeviceNameTextValidator.isTextValid(str);
    }

    public String getSSID() {
        return this.mSSID;
    }

    private void updateSsidDisplay(EditTextPreference editTextPreference) {
        editTextPreference.setText(this.mSSID);
        editTextPreference.setSummary((CharSequence) this.mSSID);
    }

    private void shareHotspotNetwork(Intent intent) {
        WifiDppUtils.showLockScreen(this.mContext, new WifiTetherSSIDPreferenceController$$ExternalSyntheticLambda1(this, intent));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$shareHotspotNetwork$1(Intent intent) {
        this.mMetricsFeatureProvider.action(0, 1712, 1595, (String) null, Integer.MIN_VALUE);
        this.mContext.startActivity(intent);
    }

    /* access modifiers changed from: package-private */
    public boolean isQrCodeButtonAvailable() {
        return ((WifiTetherSsidPreference) this.mPreference).isQrCodeButtonAvailable();
    }
}
