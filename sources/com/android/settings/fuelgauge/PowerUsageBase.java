package com.android.settings.fuelgauge;

import android.app.Activity;
import android.os.BatteryUsageStats;
import android.os.Bundle;
import android.os.UserManager;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import com.android.settings.dashboard.DashboardFragment;

public abstract class PowerUsageBase extends DashboardFragment {
    private BatteryBroadcastReceiver mBatteryBroadcastReceiver;
    BatteryUsageStats mBatteryUsageStats;
    final BatteryUsageStatsLoaderCallbacks mBatteryUsageStatsLoaderCallbacks = new BatteryUsageStatsLoaderCallbacks();
    protected boolean mIsBatteryPresent = true;
    protected UserManager mUm;

    /* access modifiers changed from: protected */
    public abstract boolean isBatteryHistoryNeeded();

    /* access modifiers changed from: protected */
    public abstract void refreshUi(int i);

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mUm = (UserManager) activity.getSystemService("user");
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        BatteryBroadcastReceiver batteryBroadcastReceiver = new BatteryBroadcastReceiver(getContext());
        this.mBatteryBroadcastReceiver = batteryBroadcastReceiver;
        batteryBroadcastReceiver.setBatteryChangedListener(new PowerUsageBase$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(int i) {
        if (i == 5) {
            this.mIsBatteryPresent = false;
        }
        restartBatteryStatsLoader(i);
    }

    public void onStart() {
        super.onStart();
        this.mBatteryBroadcastReceiver.register();
    }

    public void onStop() {
        super.onStop();
        this.mBatteryBroadcastReceiver.unRegister();
    }

    /* access modifiers changed from: protected */
    public void restartBatteryStatsLoader(int i) {
        Bundle bundle = new Bundle();
        bundle.putInt("refresh_type", i);
        bundle.putBoolean("include_history", isBatteryHistoryNeeded());
        getLoaderManager().restartLoader(1, bundle, this.mBatteryUsageStatsLoaderCallbacks);
    }

    /* access modifiers changed from: protected */
    public void onLoadFinished(int i) {
        refreshUi(i);
    }

    /* access modifiers changed from: protected */
    public void updatePreference(BatteryHistoryPreference batteryHistoryPreference) {
        long currentTimeMillis = System.currentTimeMillis();
        batteryHistoryPreference.setBatteryUsageStats(this.mBatteryUsageStats);
        BatteryUtils.logRuntime("PowerUsageBase", "updatePreference", currentTimeMillis);
    }

    private class BatteryUsageStatsLoaderCallbacks implements LoaderManager.LoaderCallbacks<BatteryUsageStats> {
        private int mRefreshType;

        public void onLoaderReset(Loader<BatteryUsageStats> loader) {
        }

        private BatteryUsageStatsLoaderCallbacks() {
        }

        public Loader<BatteryUsageStats> onCreateLoader(int i, Bundle bundle) {
            this.mRefreshType = bundle.getInt("refresh_type");
            return new BatteryUsageStatsLoader(PowerUsageBase.this.getContext(), bundle.getBoolean("include_history"));
        }

        public void onLoadFinished(Loader<BatteryUsageStats> loader, BatteryUsageStats batteryUsageStats) {
            PowerUsageBase powerUsageBase = PowerUsageBase.this;
            powerUsageBase.mBatteryUsageStats = batteryUsageStats;
            powerUsageBase.onLoadFinished(this.mRefreshType);
        }
    }
}
