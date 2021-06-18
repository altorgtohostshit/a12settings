package com.android.settings.wifi;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.FeatureFlagUtils;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.wifi.p2p.WifiP2pPreferenceController;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.ArrayList;
import java.util.List;

public class ConfigureWifiSettings extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.wifi_configure_settings) {
        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            return context.getResources().getBoolean(R.bool.config_show_wifi_settings);
        }
    };
    private UseOpenWifiPreferenceController mUseOpenWifiPreferenceController;
    private WifiWakeupPreferenceController mWifiWakeupPreferenceController;

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "ConfigureWifiSettings";
    }

    public int getMetricsCategory() {
        return 338;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.wifi_configure_settings;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (FeatureFlagUtils.isEnabled(getContext(), "settings_provider_model")) {
            getActivity().setTitle(R.string.network_and_internet_preferences_title);
        }
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new WifiP2pPreferenceController(context, getSettingsLifecycle(), (WifiManager) getSystemService("wifi")));
        return arrayList;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        WifiWakeupPreferenceController wifiWakeupPreferenceController = (WifiWakeupPreferenceController) use(WifiWakeupPreferenceController.class);
        this.mWifiWakeupPreferenceController = wifiWakeupPreferenceController;
        wifiWakeupPreferenceController.setFragment(this);
        UseOpenWifiPreferenceController useOpenWifiPreferenceController = (UseOpenWifiPreferenceController) use(UseOpenWifiPreferenceController.class);
        this.mUseOpenWifiPreferenceController = useOpenWifiPreferenceController;
        useOpenWifiPreferenceController.setFragment(this);
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 600) {
            this.mWifiWakeupPreferenceController.onActivityResult(i, i2);
        } else if (i == 400) {
            this.mUseOpenWifiPreferenceController.onActivityResult(i, i2);
        } else {
            super.onActivityResult(i, i2, intent);
        }
    }
}
