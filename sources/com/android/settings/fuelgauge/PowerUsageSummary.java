package com.android.settings.fuelgauge;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.SettingsActivity;
import com.android.settings.Utils;
import com.android.settings.fuelgauge.batterytip.BatteryTipLoader;
import com.android.settings.fuelgauge.batterytip.BatteryTipPreferenceController;
import com.android.settings.fuelgauge.batterytip.tips.BatteryTip;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.widget.LayoutPreference;
import java.util.List;

public class PowerUsageSummary extends PowerUsageBase implements BatteryTipPreferenceController.BatteryTipListener {
    static final int BATTERY_INFO_LOADER = 1;
    static final int BATTERY_TIP_LOADER = 2;
    static final String KEY_BATTERY_ERROR = "battery_help_message";
    static final String KEY_BATTERY_USAGE = "battery_usage_summary";
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.power_usage_summary);
    BatteryHeaderPreferenceController mBatteryHeaderPreferenceController;
    BatteryInfo mBatteryInfo;
    LoaderManager.LoaderCallbacks<BatteryInfo> mBatteryInfoLoaderCallbacks = new LoaderManager.LoaderCallbacks<BatteryInfo>() {
        public void onLoaderReset(Loader<BatteryInfo> loader) {
        }

        public Loader<BatteryInfo> onCreateLoader(int i, Bundle bundle) {
            return new BatteryInfoLoader(PowerUsageSummary.this.getContext());
        }

        public void onLoadFinished(Loader<BatteryInfo> loader, BatteryInfo batteryInfo) {
            PowerUsageSummary.this.mBatteryHeaderPreferenceController.updateHeaderPreference(batteryInfo);
            PowerUsageSummary powerUsageSummary = PowerUsageSummary.this;
            powerUsageSummary.mBatteryHeaderPreferenceController.updateHeaderByBatteryTips(powerUsageSummary.mBatteryTipPreferenceController.getCurrentBatteryTip(), batteryInfo);
            PowerUsageSummary.this.mBatteryInfo = batteryInfo;
        }
    };
    LayoutPreference mBatteryLayoutPref;
    BatteryTipPreferenceController mBatteryTipPreferenceController;
    private LoaderManager.LoaderCallbacks<List<BatteryTip>> mBatteryTipsCallbacks = new LoaderManager.LoaderCallbacks<List<BatteryTip>>() {
        public void onLoaderReset(Loader<List<BatteryTip>> loader) {
        }

        public Loader<List<BatteryTip>> onCreateLoader(int i, Bundle bundle) {
            return new BatteryTipLoader(PowerUsageSummary.this.getContext(), PowerUsageSummary.this.mBatteryUsageStats);
        }

        public void onLoadFinished(Loader<List<BatteryTip>> loader, List<BatteryTip> list) {
            PowerUsageSummary.this.mBatteryTipPreferenceController.updateBatteryTips(list);
            PowerUsageSummary powerUsageSummary = PowerUsageSummary.this;
            powerUsageSummary.mBatteryHeaderPreferenceController.updateHeaderByBatteryTips(powerUsageSummary.mBatteryTipPreferenceController.getCurrentBatteryTip(), PowerUsageSummary.this.mBatteryInfo);
        }
    };
    Preference mBatteryUsagePreference;
    BatteryUtils mBatteryUtils;
    Preference mHelpPreference;
    boolean mNeedUpdateBatteryTip;
    PowerUsageFeatureProvider mPowerFeatureProvider;
    final ContentObserver mSettingsObserver = new ContentObserver(new Handler()) {
        public void onChange(boolean z, Uri uri) {
            PowerUsageSummary.this.restartBatteryInfoLoader();
        }
    };

    public int getHelpResource() {
        return R.string.help_url_battery;
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "PowerUsageSummary";
    }

    public int getMetricsCategory() {
        return 1263;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.power_usage_summary;
    }

    /* access modifiers changed from: protected */
    public boolean isBatteryHistoryNeeded() {
        return false;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        SettingsActivity settingsActivity = (SettingsActivity) getActivity();
        BatteryHeaderPreferenceController batteryHeaderPreferenceController = (BatteryHeaderPreferenceController) use(BatteryHeaderPreferenceController.class);
        this.mBatteryHeaderPreferenceController = batteryHeaderPreferenceController;
        batteryHeaderPreferenceController.setActivity(settingsActivity);
        this.mBatteryHeaderPreferenceController.setFragment(this);
        this.mBatteryHeaderPreferenceController.setLifecycle(getSettingsLifecycle());
        BatteryTipPreferenceController batteryTipPreferenceController = (BatteryTipPreferenceController) use(BatteryTipPreferenceController.class);
        this.mBatteryTipPreferenceController = batteryTipPreferenceController;
        batteryTipPreferenceController.setActivity(settingsActivity);
        this.mBatteryTipPreferenceController.setFragment(this);
        this.mBatteryTipPreferenceController.setBatteryTipListener(new PowerUsageSummary$$ExternalSyntheticLambda0(this));
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setAnimationAllowed(true);
        initFeatureProvider();
        initPreference();
        this.mBatteryUtils = BatteryUtils.getInstance(getContext());
        if (Utils.isBatteryPresent(getContext())) {
            restartBatteryInfoLoader();
        } else {
            this.mHelpPreference.setVisible(true);
        }
        this.mBatteryTipPreferenceController.restoreInstanceState(bundle);
        updateBatteryTipFlag(bundle);
    }

    public void onResume() {
        super.onResume();
        getContentResolver().registerContentObserver(Settings.Global.getUriFor("battery_estimates_last_update_time"), false, this.mSettingsObserver);
    }

    public void onPause() {
        getContentResolver().unregisterContentObserver(this.mSettingsObserver);
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void refreshUi(int i) {
        if (getContext() != null && this.mIsBatteryPresent) {
            if (!this.mNeedUpdateBatteryTip || i == 1) {
                this.mNeedUpdateBatteryTip = true;
            } else {
                restartBatteryTipLoader();
            }
            restartBatteryInfoLoader();
        }
    }

    /* access modifiers changed from: package-private */
    public void restartBatteryTipLoader() {
        getLoaderManager().restartLoader(2, Bundle.EMPTY, this.mBatteryTipsCallbacks);
    }

    /* access modifiers changed from: package-private */
    public void setBatteryLayoutPreference(LayoutPreference layoutPreference) {
        this.mBatteryLayoutPref = layoutPreference;
    }

    /* access modifiers changed from: package-private */
    public void initFeatureProvider() {
        Context context = getContext();
        this.mPowerFeatureProvider = FeatureFactory.getFactory(context).getPowerUsageFeatureProvider(context);
    }

    /* access modifiers changed from: package-private */
    public void initPreference() {
        String str;
        Preference findPreference = findPreference(KEY_BATTERY_USAGE);
        this.mBatteryUsagePreference = findPreference;
        if (this.mPowerFeatureProvider.isChartGraphEnabled(getContext())) {
            str = getString(R.string.advanced_battery_preference_summary_with_hours);
        } else {
            str = getString(R.string.advanced_battery_preference_summary);
        }
        findPreference.setSummary((CharSequence) str);
        Preference findPreference2 = findPreference(KEY_BATTERY_ERROR);
        this.mHelpPreference = findPreference2;
        findPreference2.setVisible(false);
    }

    /* access modifiers changed from: package-private */
    public void restartBatteryInfoLoader() {
        if (getContext() != null && this.mIsBatteryPresent) {
            getLoaderManager().restartLoader(1, Bundle.EMPTY, this.mBatteryInfoLoaderCallbacks);
        }
    }

    /* access modifiers changed from: package-private */
    public void updateBatteryTipFlag(Bundle bundle) {
        this.mNeedUpdateBatteryTip = bundle == null || this.mBatteryTipPreferenceController.needUpdate();
    }

    /* access modifiers changed from: protected */
    public void restartBatteryStatsLoader(int i) {
        super.restartBatteryStatsLoader(i);
        if (this.mIsBatteryPresent) {
            this.mBatteryHeaderPreferenceController.quickUpdateHeaderPreference();
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        this.mBatteryTipPreferenceController.saveInstanceState(bundle);
    }

    public void onBatteryTipHandled(BatteryTip batteryTip) {
        restartBatteryTipLoader();
    }
}
