package com.android.settings.applications.appinfo;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.os.BatteryUsageStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.UidBatteryConsumer;
import android.os.UserManager;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.core.InstrumentedPreferenceFragment;
import com.android.settings.fuelgauge.AdvancedPowerUsageDetail;
import com.android.settings.fuelgauge.BatteryEntry;
import com.android.settings.fuelgauge.BatteryUsageStatsLoader;
import com.android.settings.fuelgauge.BatteryUtils;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.Utils;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;
import java.util.List;

public class AppBatteryPreferenceController extends BasePreferenceController implements LifecycleObserver, OnResume, OnPause {
    private static final String KEY_BATTERY = "battery";
    private String mBatteryPercent;
    BatteryUsageStats mBatteryUsageStats;
    final BatteryUsageStatsLoaderCallbacks mBatteryUsageStatsLoaderCallbacks = new BatteryUsageStatsLoaderCallbacks();
    BatteryUtils mBatteryUtils;
    private final String mPackageName;
    private final AppInfoDashboardFragment mParent;
    private Preference mPreference;
    UidBatteryConsumer mUidBatteryConsumer;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
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

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public AppBatteryPreferenceController(Context context, AppInfoDashboardFragment appInfoDashboardFragment, String str, Lifecycle lifecycle) {
        super(context, KEY_BATTERY);
        this.mParent = appInfoDashboardFragment;
        this.mBatteryUtils = BatteryUtils.getInstance(this.mContext);
        this.mPackageName = str;
        if (lifecycle != null) {
            lifecycle.addObserver(this);
        }
    }

    public int getAvailabilityStatus() {
        return this.mContext.getResources().getBoolean(R.bool.config_show_app_info_settings_battery) ? 0 : 2;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        Preference findPreference = preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = findPreference;
        findPreference.setEnabled(false);
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!KEY_BATTERY.equals(preference.getKey())) {
            return false;
        }
        if (isBatteryStatsAvailable()) {
            AdvancedPowerUsageDetail.startBatteryDetailPage(this.mParent.getActivity(), this.mParent, new BatteryEntry(this.mContext, (Handler) null, (UserManager) this.mContext.getSystemService("user"), this.mUidBatteryConsumer, false, (String[]) null, this.mPackageName), this.mBatteryPercent);
            return true;
        }
        AdvancedPowerUsageDetail.startBatteryDetailPage((Activity) this.mParent.getActivity(), (InstrumentedPreferenceFragment) this.mParent, this.mPackageName);
        return true;
    }

    public void onResume() {
        this.mParent.getLoaderManager().restartLoader(5, Bundle.EMPTY, this.mBatteryUsageStatsLoaderCallbacks);
    }

    public void onPause() {
        this.mParent.getLoaderManager().destroyLoader(5);
    }

    /* access modifiers changed from: private */
    public void onLoadFinished() {
        PackageInfo packageInfo;
        if (this.mBatteryUsageStats != null && (packageInfo = this.mParent.getPackageInfo()) != null) {
            this.mUidBatteryConsumer = findTargetUidBatteryConsumer(this.mBatteryUsageStats, packageInfo.applicationInfo.uid);
            if (this.mParent.getActivity() != null) {
                updateBattery();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void updateBattery() {
        this.mPreference.setEnabled(true);
        if (isBatteryStatsAvailable()) {
            String formatPercentage = Utils.formatPercentage((int) this.mBatteryUtils.calculateBatteryPercent(this.mUidBatteryConsumer.getConsumedPower(), this.mBatteryUsageStats.getConsumedPower(), this.mBatteryUsageStats.getDischargePercentage()));
            this.mBatteryPercent = formatPercentage;
            this.mPreference.setSummary((CharSequence) this.mContext.getString(R.string.battery_summary, new Object[]{formatPercentage}));
            return;
        }
        this.mPreference.setSummary((CharSequence) this.mContext.getString(R.string.no_battery_summary));
    }

    /* access modifiers changed from: package-private */
    public boolean isBatteryStatsAvailable() {
        return this.mUidBatteryConsumer != null;
    }

    /* access modifiers changed from: package-private */
    public UidBatteryConsumer findTargetUidBatteryConsumer(BatteryUsageStats batteryUsageStats, int i) {
        List uidBatteryConsumers = batteryUsageStats.getUidBatteryConsumers();
        int size = uidBatteryConsumers.size();
        for (int i2 = 0; i2 < size; i2++) {
            UidBatteryConsumer uidBatteryConsumer = (UidBatteryConsumer) uidBatteryConsumers.get(i2);
            if (uidBatteryConsumer.getUid() == i) {
                return uidBatteryConsumer;
            }
        }
        return null;
    }

    private class BatteryUsageStatsLoaderCallbacks implements LoaderManager.LoaderCallbacks<BatteryUsageStats> {
        public void onLoaderReset(Loader<BatteryUsageStats> loader) {
        }

        private BatteryUsageStatsLoaderCallbacks() {
        }

        public Loader<BatteryUsageStats> onCreateLoader(int i, Bundle bundle) {
            return new BatteryUsageStatsLoader(AppBatteryPreferenceController.this.mContext, false);
        }

        public void onLoadFinished(Loader<BatteryUsageStats> loader, BatteryUsageStats batteryUsageStats) {
            AppBatteryPreferenceController appBatteryPreferenceController = AppBatteryPreferenceController.this;
            appBatteryPreferenceController.mBatteryUsageStats = batteryUsageStats;
            appBatteryPreferenceController.onLoadFinished();
        }
    }
}
