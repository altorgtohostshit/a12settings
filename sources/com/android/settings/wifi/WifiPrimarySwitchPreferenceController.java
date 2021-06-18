package com.android.settings.wifi;

import android.content.Context;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.widget.GenericSwitchController;
import com.android.settings.widget.PrimarySwitchPreference;
import com.android.settings.widget.SummaryUpdater;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;

public class WifiPrimarySwitchPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin, SummaryUpdater.OnSummaryChangeListener, LifecycleObserver, OnResume, OnPause, OnStart, OnStop {
    private final MetricsFeatureProvider mMetricsFeatureProvider;
    private final WifiSummaryUpdater mSummaryHelper = new WifiSummaryUpdater(this.mContext, this);
    private WifiEnabler mWifiEnabler;
    private PrimarySwitchPreference mWifiPreference;

    public String getPreferenceKey() {
        return "main_toggle_wifi";
    }

    public WifiPrimarySwitchPreferenceController(Context context, MetricsFeatureProvider metricsFeatureProvider) {
        super(context);
        this.mMetricsFeatureProvider = metricsFeatureProvider;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mWifiPreference = (PrimarySwitchPreference) preferenceScreen.findPreference("main_toggle_wifi");
    }

    public boolean isAvailable() {
        return this.mContext.getResources().getBoolean(R.bool.config_show_wifi_settings);
    }

    public void onResume() {
        this.mSummaryHelper.register(true);
        WifiEnabler wifiEnabler = this.mWifiEnabler;
        if (wifiEnabler != null) {
            wifiEnabler.resume(this.mContext);
        }
    }

    public void onPause() {
        WifiEnabler wifiEnabler = this.mWifiEnabler;
        if (wifiEnabler != null) {
            wifiEnabler.pause();
        }
        this.mSummaryHelper.register(false);
    }

    public void onStart() {
        this.mWifiEnabler = new WifiEnabler(this.mContext, new GenericSwitchController(this.mWifiPreference), this.mMetricsFeatureProvider);
    }

    public void onStop() {
        WifiEnabler wifiEnabler = this.mWifiEnabler;
        if (wifiEnabler != null) {
            wifiEnabler.teardownSwitchController();
        }
    }

    public void onSummaryChanged(String str) {
        PrimarySwitchPreference primarySwitchPreference = this.mWifiPreference;
        if (primarySwitchPreference != null) {
            primarySwitchPreference.setSummary((CharSequence) str);
        }
    }
}
