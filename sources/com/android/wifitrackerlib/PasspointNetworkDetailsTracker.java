package com.android.wifitrackerlib;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkScoreManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.hotspot2.OsuProvider;
import android.net.wifi.hotspot2.PasspointConfiguration;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Pair;
import androidx.core.util.Preconditions;
import androidx.lifecycle.Lifecycle;
import java.time.Clock;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PasspointNetworkDetailsTracker extends NetworkDetailsTracker {
    private final PasspointWifiEntry mChosenEntry;
    private NetworkInfo mCurrentNetworkInfo;
    private WifiConfiguration mCurrentWifiConfig;
    private OsuWifiEntry mOsuWifiEntry;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public PasspointNetworkDetailsTracker(Lifecycle lifecycle, Context context, WifiManager wifiManager, ConnectivityManager connectivityManager, NetworkScoreManager networkScoreManager, Handler handler, Handler handler2, Clock clock, long j, long j2, String str) {
        super(lifecycle, context, wifiManager, connectivityManager, networkScoreManager, handler, handler2, clock, j, j2, "PasspointNetworkDetailsTracker");
        String str2 = str;
        Optional findAny = this.mWifiManager.getPasspointConfigurations().stream().filter(new PasspointNetworkDetailsTracker$$ExternalSyntheticLambda3(str2)).findAny();
        if (findAny.isPresent()) {
            this.mChosenEntry = new PasspointWifiEntry(this.mContext, this.mMainHandler, (PasspointConfiguration) findAny.get(), this.mWifiManager, this.mWifiNetworkScoreCache, false);
            return;
        }
        Optional findAny2 = this.mWifiManager.getPrivilegedConfiguredNetworks().stream().filter(new PasspointNetworkDetailsTracker$$ExternalSyntheticLambda2(str2)).findAny();
        if (findAny2.isPresent()) {
            this.mChosenEntry = new PasspointWifiEntry(this.mContext, this.mMainHandler, (WifiConfiguration) findAny2.get(), this.mWifiManager, this.mWifiNetworkScoreCache, false);
            return;
        }
        throw new IllegalArgumentException("Cannot find config for given PasspointWifiEntry key!");
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$new$1(String str, WifiConfiguration wifiConfiguration) {
        return wifiConfiguration.isPasspoint() && TextUtils.equals(str, PasspointWifiEntry.uniqueIdToPasspointWifiEntryKey(wifiConfiguration.getKey()));
    }

    public WifiEntry getWifiEntry() {
        return this.mChosenEntry;
    }

    /* access modifiers changed from: protected */
    public void handleOnStart() {
        boolean z = true;
        conditionallyUpdateScanResults(true);
        conditionallyUpdateConfig();
        WifiInfo connectionInfo = this.mWifiManager.getConnectionInfo();
        Network currentNetwork = this.mWifiManager.getCurrentNetwork();
        NetworkInfo networkInfo = this.mConnectivityManager.getNetworkInfo(currentNetwork);
        this.mCurrentNetworkInfo = networkInfo;
        this.mChosenEntry.updateConnectionInfo(connectionInfo, networkInfo);
        handleNetworkCapabilitiesChanged(this.mConnectivityManager.getNetworkCapabilities(currentNetwork));
        handleLinkPropertiesChanged(this.mConnectivityManager.getLinkProperties(currentNetwork));
        this.mChosenEntry.setIsDefaultNetwork(this.mIsWifiDefaultRoute);
        PasspointWifiEntry passpointWifiEntry = this.mChosenEntry;
        if (!this.mIsWifiValidated || !this.mIsCellDefaultRoute) {
            z = false;
        }
        passpointWifiEntry.setIsLowQuality(z);
    }

    /* access modifiers changed from: protected */
    public void handleWifiStateChangedAction() {
        conditionallyUpdateScanResults(true);
    }

    /* access modifiers changed from: protected */
    public void handleScanResultsAvailableAction(Intent intent) {
        Preconditions.checkNotNull(intent, "Intent cannot be null!");
        conditionallyUpdateScanResults(intent.getBooleanExtra("resultsUpdated", true));
    }

    /* access modifiers changed from: protected */
    public void handleConfiguredNetworksChangedAction(Intent intent) {
        Preconditions.checkNotNull(intent, "Intent cannot be null!");
        conditionallyUpdateConfig();
    }

    private void updatePasspointWifiEntryScans(List<ScanResult> list) {
        Preconditions.checkNotNull(list, "Scan Result list should not be null!");
        for (Pair pair : this.mWifiManager.getAllMatchingWifiConfigs(list)) {
            WifiConfiguration wifiConfiguration = (WifiConfiguration) pair.first;
            if (TextUtils.equals(PasspointWifiEntry.uniqueIdToPasspointWifiEntryKey(wifiConfiguration.getKey()), this.mChosenEntry.getKey())) {
                this.mCurrentWifiConfig = wifiConfiguration;
                this.mChosenEntry.updateScanResultInfo(wifiConfiguration, (List) ((Map) pair.second).get(0), (List) ((Map) pair.second).get(1));
                return;
            }
        }
        this.mChosenEntry.updateScanResultInfo(this.mCurrentWifiConfig, (List<ScanResult>) null, (List<ScanResult>) null);
    }

    private void updateOsuWifiEntryScans(List<ScanResult> list) {
        Preconditions.checkNotNull(list, "Scan Result list should not be null!");
        Map matchingOsuProviders = this.mWifiManager.getMatchingOsuProviders(list);
        Map matchingPasspointConfigsForOsuProviders = this.mWifiManager.getMatchingPasspointConfigsForOsuProviders(matchingOsuProviders.keySet());
        OsuWifiEntry osuWifiEntry = this.mOsuWifiEntry;
        if (osuWifiEntry != null) {
            osuWifiEntry.updateScanResultInfo((List) matchingOsuProviders.get(osuWifiEntry.getOsuProvider()));
        } else {
            for (OsuProvider osuProvider : matchingOsuProviders.keySet()) {
                PasspointConfiguration passpointConfiguration = (PasspointConfiguration) matchingPasspointConfigsForOsuProviders.get(osuProvider);
                if (passpointConfiguration != null && TextUtils.equals(this.mChosenEntry.getKey(), PasspointWifiEntry.uniqueIdToPasspointWifiEntryKey(passpointConfiguration.getUniqueId()))) {
                    OsuWifiEntry osuWifiEntry2 = new OsuWifiEntry(this.mContext, this.mMainHandler, osuProvider, this.mWifiManager, this.mWifiNetworkScoreCache, false);
                    this.mOsuWifiEntry = osuWifiEntry2;
                    osuWifiEntry2.updateScanResultInfo((List) matchingOsuProviders.get(osuProvider));
                    this.mOsuWifiEntry.setAlreadyProvisioned(true);
                    this.mChosenEntry.setOsuWifiEntry(this.mOsuWifiEntry);
                    return;
                }
            }
        }
        OsuWifiEntry osuWifiEntry3 = this.mOsuWifiEntry;
        if (osuWifiEntry3 != null && osuWifiEntry3.getLevel() == -1) {
            this.mChosenEntry.setOsuWifiEntry((OsuWifiEntry) null);
            this.mOsuWifiEntry = null;
        }
    }

    private void conditionallyUpdateScanResults(boolean z) {
        if (this.mWifiManager.getWifiState() == 1) {
            this.mChosenEntry.updateScanResultInfo(this.mCurrentWifiConfig, (List<ScanResult>) null, (List<ScanResult>) null);
            return;
        }
        long j = this.mMaxScanAgeMillis;
        if (z) {
            cacheNewScanResults();
        } else {
            j += this.mScanIntervalMillis;
        }
        List<ScanResult> scanResults = this.mScanResultUpdater.getScanResults(j);
        updatePasspointWifiEntryScans(scanResults);
        updateOsuWifiEntryScans(scanResults);
    }

    private void conditionallyUpdateConfig() {
        this.mWifiManager.getPasspointConfigurations().stream().filter(new PasspointNetworkDetailsTracker$$ExternalSyntheticLambda1(this)).findAny().ifPresent(new PasspointNetworkDetailsTracker$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$conditionallyUpdateConfig$2(PasspointConfiguration passpointConfiguration) {
        return TextUtils.equals(PasspointWifiEntry.uniqueIdToPasspointWifiEntryKey(passpointConfiguration.getUniqueId()), this.mChosenEntry.getKey());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$conditionallyUpdateConfig$3(PasspointConfiguration passpointConfiguration) {
        this.mChosenEntry.updatePasspointConfig(passpointConfiguration);
    }

    private void cacheNewScanResults() {
        this.mScanResultUpdater.update(this.mWifiManager.getScanResults());
    }
}
