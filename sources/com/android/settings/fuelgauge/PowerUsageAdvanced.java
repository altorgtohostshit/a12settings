package com.android.settings.fuelgauge;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.SearchIndexableResource;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import com.android.settings.R;
import com.android.settings.SettingsActivity;
import com.android.settings.core.InstrumentedPreferenceFragment;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PowerUsageAdvanced extends PowerUsageBase {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider() {
        public List<SearchIndexableResource> getXmlResourcesToIndex(Context context, boolean z) {
            SearchIndexableResource searchIndexableResource = new SearchIndexableResource(context);
            searchIndexableResource.xmlResId = R.xml.power_usage_advanced;
            return Arrays.asList(new SearchIndexableResource[]{searchIndexableResource});
        }

        public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(new BatteryAppListPreferenceController(context, "app_list", (Lifecycle) null, (SettingsActivity) null, (InstrumentedPreferenceFragment) null));
            return arrayList;
        }
    };
    private BatteryAppListPreferenceController mBatteryAppListPreferenceController;
    private BatteryChartPreferenceController mBatteryChartPreferenceController;
    final BatteryHistoryLoaderCallbacks mBatteryHistoryLoaderCallbacks = new BatteryHistoryLoaderCallbacks();
    Map<Long, Map<String, BatteryHistEntry>> mBatteryHistoryMap;
    BatteryHistoryPreference mHistPref;
    private boolean mIsChartDataLoaded = false;
    private boolean mIsChartGraphEnabled = false;
    private PowerUsageFeatureProvider mPowerUsageFeatureProvider;

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "AdvancedBatteryUsage";
    }

    public int getMetricsCategory() {
        return 51;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.power_usage_advanced;
    }

    /* access modifiers changed from: protected */
    public boolean isBatteryHistoryNeeded() {
        return true;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Context context = getContext();
        refreshFeatureFlag(context);
        this.mHistPref = (BatteryHistoryPreference) findPreference("battery_graph");
        if (this.mIsChartGraphEnabled) {
            setBatteryChartPreferenceController();
        } else {
            updateHistPrefSummary(context);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (getActivity().isChangingConfigurations()) {
            BatteryEntry.clearUidCache();
        }
    }

    public void onPause() {
        super.onPause();
        this.mIsChartDataLoaded = false;
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        refreshFeatureFlag(context);
        ArrayList arrayList = new ArrayList();
        if (this.mIsChartGraphEnabled) {
            BatteryChartPreferenceController batteryChartPreferenceController = new BatteryChartPreferenceController(context, "app_list", getSettingsLifecycle(), (SettingsActivity) getActivity(), this);
            this.mBatteryChartPreferenceController = batteryChartPreferenceController;
            arrayList.add(batteryChartPreferenceController);
            setBatteryChartPreferenceController();
        } else {
            BatteryAppListPreferenceController batteryAppListPreferenceController = new BatteryAppListPreferenceController(context, "app_list", getSettingsLifecycle(), (SettingsActivity) getActivity(), this);
            this.mBatteryAppListPreferenceController = batteryAppListPreferenceController;
            arrayList.add(batteryAppListPreferenceController);
        }
        return arrayList;
    }

    /* access modifiers changed from: protected */
    public void refreshUi(int i) {
        Map<Long, Map<String, BatteryHistEntry>> map;
        Context context = getContext();
        if (context != null) {
            updatePreference(this.mHistPref);
            if (!(this.mBatteryAppListPreferenceController == null || this.mBatteryUsageStats == null)) {
                updateHistPrefSummary(context);
                this.mBatteryAppListPreferenceController.refreshAppListGroup(this.mBatteryUsageStats, true);
            }
            BatteryChartPreferenceController batteryChartPreferenceController = this.mBatteryChartPreferenceController;
            if (batteryChartPreferenceController != null && (map = this.mBatteryHistoryMap) != null) {
                batteryChartPreferenceController.setBatteryHistoryMap(map);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void restartBatteryStatsLoader(int i) {
        Bundle bundle = new Bundle();
        bundle.putInt("refresh_type", i);
        boolean z = this.mIsChartGraphEnabled;
        if (z && !this.mIsChartDataLoaded) {
            this.mIsChartDataLoaded = true;
            getLoaderManager().restartLoader(2, bundle, this.mBatteryHistoryLoaderCallbacks);
        } else if (!z) {
            super.restartBatteryStatsLoader(i);
        }
    }

    private void updateHistPrefSummary(Context context) {
        boolean z = context.registerReceiver((BroadcastReceiver) null, new IntentFilter("android.intent.action.BATTERY_CHANGED")).getIntExtra("plugged", -1) != 0;
        if (!this.mPowerUsageFeatureProvider.isEnhancedBatteryPredictionEnabled(context) || z) {
            this.mHistPref.hideBottomSummary();
        } else {
            this.mHistPref.setBottomSummary(this.mPowerUsageFeatureProvider.getAdvancedUsageScreenInfoString());
        }
    }

    private void refreshFeatureFlag(Context context) {
        if (this.mPowerUsageFeatureProvider == null) {
            PowerUsageFeatureProvider powerUsageFeatureProvider = FeatureFactory.getFactory(context).getPowerUsageFeatureProvider(context);
            this.mPowerUsageFeatureProvider = powerUsageFeatureProvider;
            this.mIsChartGraphEnabled = powerUsageFeatureProvider.isChartGraphEnabled(context);
        }
    }

    private void setBatteryChartPreferenceController() {
        BatteryChartPreferenceController batteryChartPreferenceController;
        BatteryHistoryPreference batteryHistoryPreference = this.mHistPref;
        if (batteryHistoryPreference != null && (batteryChartPreferenceController = this.mBatteryChartPreferenceController) != null) {
            batteryHistoryPreference.setChartPreferenceController(batteryChartPreferenceController);
        }
    }

    private class BatteryHistoryLoaderCallbacks implements LoaderManager.LoaderCallbacks<Map<Long, Map<String, BatteryHistEntry>>> {
        private int mRefreshType;

        public void onLoaderReset(Loader<Map<Long, Map<String, BatteryHistEntry>>> loader) {
        }

        private BatteryHistoryLoaderCallbacks() {
        }

        public Loader<Map<Long, Map<String, BatteryHistEntry>>> onCreateLoader(int i, Bundle bundle) {
            this.mRefreshType = bundle.getInt("refresh_type");
            return new BatteryHistoryLoader(PowerUsageAdvanced.this.getContext());
        }

        public void onLoadFinished(Loader<Map<Long, Map<String, BatteryHistEntry>>> loader, Map<Long, Map<String, BatteryHistEntry>> map) {
            PowerUsageAdvanced powerUsageAdvanced = PowerUsageAdvanced.this;
            powerUsageAdvanced.mBatteryHistoryMap = map;
            powerUsageAdvanced.onLoadFinished(this.mRefreshType);
        }
    }
}
