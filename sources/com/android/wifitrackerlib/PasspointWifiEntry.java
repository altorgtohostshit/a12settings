package com.android.wifitrackerlib;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkScoreCache;
import android.net.wifi.hotspot2.PasspointConfiguration;
import android.os.Handler;
import android.text.TextUtils;
import androidx.core.util.Preconditions;
import com.android.internal.annotations.VisibleForTesting;
import com.android.wifitrackerlib.WifiEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@VisibleForTesting
public class PasspointWifiEntry extends WifiEntry implements WifiEntry.WifiEntryCallback {
    private final Context mContext;
    private final List<ScanResult> mCurrentHomeScanResults = new ArrayList();
    private final List<ScanResult> mCurrentRoamingScanResults = new ArrayList();
    private String mFqdn;
    private String mFriendlyName;
    private boolean mIsRoaming = false;
    private final String mKey;
    private final Object mLock = new Object();
    private int mMeteredOverride = 0;
    private OsuWifiEntry mOsuWifiEntry;
    private PasspointConfiguration mPasspointConfig;
    private int mSecurity = 3;
    private boolean mShouldAutoOpenCaptivePortal = false;
    protected long mSubscriptionExpirationTimeInMillis;
    private WifiConfiguration mWifiConfig;

    /* access modifiers changed from: protected */
    public String getScanResultDescription() {
        return "";
    }

    PasspointWifiEntry(Context context, Handler handler, PasspointConfiguration passpointConfiguration, WifiManager wifiManager, WifiNetworkScoreCache wifiNetworkScoreCache, boolean z) throws IllegalArgumentException {
        super(handler, wifiManager, wifiNetworkScoreCache, z);
        Preconditions.checkNotNull(passpointConfiguration, "Cannot construct with null PasspointConfiguration!");
        this.mContext = context;
        this.mPasspointConfig = passpointConfiguration;
        this.mKey = uniqueIdToPasspointWifiEntryKey(passpointConfiguration.getUniqueId());
        String fqdn = passpointConfiguration.getHomeSp().getFqdn();
        this.mFqdn = fqdn;
        Preconditions.checkNotNull(fqdn, "Cannot construct with null PasspointConfiguration FQDN!");
        this.mFriendlyName = passpointConfiguration.getHomeSp().getFriendlyName();
        this.mSubscriptionExpirationTimeInMillis = passpointConfiguration.getSubscriptionExpirationTimeMillis();
        this.mMeteredOverride = this.mPasspointConfig.getMeteredOverride();
    }

    PasspointWifiEntry(Context context, Handler handler, WifiConfiguration wifiConfiguration, WifiManager wifiManager, WifiNetworkScoreCache wifiNetworkScoreCache, boolean z) throws IllegalArgumentException {
        super(handler, wifiManager, wifiNetworkScoreCache, z);
        Preconditions.checkNotNull(wifiConfiguration, "Cannot construct with null WifiConfiguration!");
        if (wifiConfiguration.isPasspoint()) {
            this.mContext = context;
            this.mWifiConfig = wifiConfiguration;
            this.mKey = uniqueIdToPasspointWifiEntryKey(wifiConfiguration.getKey());
            String str = wifiConfiguration.FQDN;
            this.mFqdn = str;
            Preconditions.checkNotNull(str, "Cannot construct with null WifiConfiguration FQDN!");
            this.mFriendlyName = this.mWifiConfig.providerFriendlyName;
            return;
        }
        throw new IllegalArgumentException("Given WifiConfiguration is not for Passpoint!");
    }

    public String getKey() {
        return this.mKey;
    }

    public int getConnectedState() {
        OsuWifiEntry osuWifiEntry;
        if (!isExpired() || super.getConnectedState() != 0 || (osuWifiEntry = this.mOsuWifiEntry) == null) {
            return super.getConnectedState();
        }
        return osuWifiEntry.getConnectedState();
    }

    public String getTitle() {
        return this.mFriendlyName;
    }

    public String getSummary(boolean z) {
        StringJoiner stringJoiner = new StringJoiner(this.mContext.getString(R$string.wifitrackerlib_summary_separator));
        if (isExpired()) {
            OsuWifiEntry osuWifiEntry = this.mOsuWifiEntry;
            if (osuWifiEntry != null) {
                stringJoiner.add(osuWifiEntry.getSummary(z));
            } else {
                stringJoiner.add(this.mContext.getString(R$string.wifitrackerlib_wifi_passpoint_expired));
            }
        } else if (getConnectedState() == 0) {
            String disconnectedStateDescription = Utils.getDisconnectedStateDescription(this.mContext, this);
            if (!TextUtils.isEmpty(disconnectedStateDescription)) {
                stringJoiner.add(disconnectedStateDescription);
            } else if (z) {
                stringJoiner.add(this.mContext.getString(R$string.wifitrackerlib_wifi_disconnected));
            } else if (!this.mForSavedNetworksPage) {
                WifiConfiguration wifiConfiguration = this.mWifiConfig;
                if (wifiConfiguration == null || !wifiConfiguration.fromWifiNetworkSuggestion) {
                    stringJoiner.add(this.mContext.getString(R$string.wifitrackerlib_wifi_remembered));
                } else {
                    Context context = this.mContext;
                    String carrierNameForSubId = Utils.getCarrierNameForSubId(context, Utils.getSubIdForConfig(context, wifiConfiguration));
                    String appLabel = Utils.getAppLabel(this.mContext, this.mWifiConfig.creatorName);
                    if (TextUtils.isEmpty(appLabel)) {
                        appLabel = this.mWifiConfig.creatorName;
                    }
                    Context context2 = this.mContext;
                    int i = R$string.wifitrackerlib_available_via_app;
                    Object[] objArr = new Object[1];
                    if (carrierNameForSubId == null) {
                        carrierNameForSubId = appLabel;
                    }
                    objArr[0] = carrierNameForSubId;
                    stringJoiner.add(context2.getString(i, objArr));
                }
            }
        } else {
            String connectStateDescription = getConnectStateDescription();
            if (!TextUtils.isEmpty(connectStateDescription)) {
                stringJoiner.add(connectStateDescription);
            }
        }
        String speedDescription = Utils.getSpeedDescription(this.mContext, this);
        if (!TextUtils.isEmpty(speedDescription)) {
            stringJoiner.add(speedDescription);
        }
        String autoConnectDescription = Utils.getAutoConnectDescription(this.mContext, this);
        if (!TextUtils.isEmpty(autoConnectDescription)) {
            stringJoiner.add(autoConnectDescription);
        }
        String meteredDescription = Utils.getMeteredDescription(this.mContext, this);
        if (!TextUtils.isEmpty(meteredDescription)) {
            stringJoiner.add(meteredDescription);
        }
        if (!z) {
            String verboseLoggingDescription = Utils.getVerboseLoggingDescription(this);
            if (!TextUtils.isEmpty(verboseLoggingDescription)) {
                stringJoiner.add(verboseLoggingDescription);
            }
        }
        return stringJoiner.toString();
    }

    private String getConnectStateDescription() {
        if (getConnectedState() == 2) {
            WifiInfo wifiInfo = this.mWifiInfo;
            String str = null;
            String requestingPackageName = wifiInfo != null ? wifiInfo.getRequestingPackageName() : null;
            if (!TextUtils.isEmpty(requestingPackageName)) {
                WifiConfiguration wifiConfiguration = this.mWifiConfig;
                if (wifiConfiguration != null) {
                    Context context = this.mContext;
                    str = Utils.getCarrierNameForSubId(context, Utils.getSubIdForConfig(context, wifiConfiguration));
                }
                String appLabel = Utils.getAppLabel(this.mContext, requestingPackageName);
                if (!TextUtils.isEmpty(appLabel)) {
                    requestingPackageName = appLabel;
                }
                Context context2 = this.mContext;
                int i = R$string.wifitrackerlib_connected_via_app;
                Object[] objArr = new Object[1];
                if (str == null) {
                    str = requestingPackageName;
                }
                objArr[0] = str;
                return context2.getString(i, objArr);
            } else if (this.mIsLowQuality) {
                return this.mContext.getString(R$string.wifi_connected_low_quality);
            } else {
                String currentNetworkCapabilitiesInformation = Utils.getCurrentNetworkCapabilitiesInformation(this.mContext, this.mNetworkCapabilities);
                if (!TextUtils.isEmpty(currentNetworkCapabilitiesInformation)) {
                    return currentNetworkCapabilitiesInformation;
                }
            }
        }
        return Utils.getNetworkDetailedState(this.mContext, this.mNetworkInfo);
    }

    public CharSequence getSecondSummary() {
        return getConnectedState() == 2 ? Utils.getImsiProtectionDescription(this.mContext, this.mWifiConfig) : "";
    }

    public String getSsid() {
        WifiInfo wifiInfo = this.mWifiInfo;
        if (wifiInfo != null) {
            return WifiInfo.sanitizeSsid(wifiInfo.getSSID());
        }
        WifiConfiguration wifiConfiguration = this.mWifiConfig;
        if (wifiConfiguration != null) {
            return WifiInfo.sanitizeSsid(wifiConfiguration.SSID);
        }
        return null;
    }

    public int getSecurity() {
        return this.mSecurity;
    }

    public String getMacAddress() {
        WifiInfo wifiInfo = this.mWifiInfo;
        if (wifiInfo != null) {
            String macAddress = wifiInfo.getMacAddress();
            if (!TextUtils.isEmpty(macAddress) && !TextUtils.equals(macAddress, "02:00:00:00:00:00")) {
                return macAddress;
            }
        }
        if (this.mWifiConfig != null && getPrivacy() == 1) {
            return this.mWifiConfig.getRandomizedMacAddress().toString();
        }
        String[] factoryMacAddresses = this.mWifiManager.getFactoryMacAddresses();
        if (factoryMacAddresses.length > 0) {
            return factoryMacAddresses[0];
        }
        return null;
    }

    public boolean isMetered() {
        if (getMeteredChoice() == 1) {
            return true;
        }
        WifiConfiguration wifiConfiguration = this.mWifiConfig;
        return wifiConfiguration != null && wifiConfiguration.meteredHint;
    }

    public boolean isSuggestion() {
        WifiConfiguration wifiConfiguration = this.mWifiConfig;
        return wifiConfiguration != null && wifiConfiguration.fromWifiNetworkSuggestion;
    }

    public boolean isSubscription() {
        return this.mPasspointConfig != null;
    }

    public boolean canConnect() {
        if (isExpired()) {
            OsuWifiEntry osuWifiEntry = this.mOsuWifiEntry;
            if (osuWifiEntry == null || !osuWifiEntry.canConnect()) {
                return false;
            }
            return true;
        } else if (this.mLevel == -1 || getConnectedState() != 0 || this.mWifiConfig == null) {
            return false;
        } else {
            return true;
        }
    }

    public void connect(WifiEntry.ConnectCallback connectCallback) {
        OsuWifiEntry osuWifiEntry;
        if (!isExpired() || (osuWifiEntry = this.mOsuWifiEntry) == null) {
            this.mShouldAutoOpenCaptivePortal = true;
            this.mConnectCallback = connectCallback;
            if (this.mWifiConfig == null) {
                new WifiEntry.ConnectActionListener().onFailure(0);
            }
            this.mWifiManager.stopRestrictingAutoJoinToSubscriptionId();
            this.mWifiManager.connect(this.mWifiConfig, new WifiEntry.ConnectActionListener());
            return;
        }
        osuWifiEntry.connect(connectCallback);
    }

    public boolean canDisconnect() {
        return getConnectedState() == 2;
    }

    public void disconnect(WifiEntry.DisconnectCallback disconnectCallback) {
        if (canDisconnect()) {
            this.mCalledDisconnect = true;
            this.mDisconnectCallback = disconnectCallback;
            this.mCallbackHandler.postDelayed(new PasspointWifiEntry$$ExternalSyntheticLambda0(this, disconnectCallback), 10000);
            this.mWifiManager.disableEphemeralNetwork(this.mFqdn);
            this.mWifiManager.disconnect();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$disconnect$0(WifiEntry.DisconnectCallback disconnectCallback) {
        if (disconnectCallback != null && this.mCalledDisconnect) {
            disconnectCallback.onDisconnectResult(1);
        }
    }

    public boolean canForget() {
        return !isSuggestion() && this.mPasspointConfig != null;
    }

    public void forget(WifiEntry.ForgetCallback forgetCallback) {
        if (canForget()) {
            this.mForgetCallback = forgetCallback;
            this.mWifiManager.removePasspointConfiguration(this.mPasspointConfig.getHomeSp().getFqdn());
            new WifiEntry.ForgetActionListener().onSuccess();
        }
    }

    public int getMeteredChoice() {
        int i = this.mMeteredOverride;
        if (i == 1) {
            return 1;
        }
        return i == 2 ? 2 : 0;
    }

    public boolean canSetMeteredChoice() {
        return !isSuggestion() && this.mPasspointConfig != null;
    }

    public void setMeteredChoice(int i) {
        if (canSetMeteredChoice()) {
            if (i == 0) {
                this.mMeteredOverride = 0;
            } else if (i == 1) {
                this.mMeteredOverride = 1;
            } else if (i == 2) {
                this.mMeteredOverride = 2;
            } else {
                return;
            }
            this.mWifiManager.setPasspointMeteredOverride(this.mPasspointConfig.getHomeSp().getFqdn(), this.mMeteredOverride);
        }
    }

    public boolean canSetPrivacy() {
        return !isSuggestion() && this.mPasspointConfig != null;
    }

    public int getPrivacy() {
        PasspointConfiguration passpointConfiguration = this.mPasspointConfig;
        if (passpointConfiguration == null) {
            return 1;
        }
        return passpointConfiguration.isMacRandomizationEnabled() ? 1 : 0;
    }

    public void setPrivacy(int i) {
        if (canSetPrivacy()) {
            this.mWifiManager.setMacRandomizationSettingPasspointEnabled(this.mPasspointConfig.getHomeSp().getFqdn(), i != 0);
        }
    }

    public boolean isAutoJoinEnabled() {
        PasspointConfiguration passpointConfiguration = this.mPasspointConfig;
        if (passpointConfiguration != null) {
            return passpointConfiguration.isAutojoinEnabled();
        }
        WifiConfiguration wifiConfiguration = this.mWifiConfig;
        if (wifiConfiguration != null) {
            return wifiConfiguration.allowAutojoin;
        }
        return false;
    }

    public boolean canSetAutoJoinEnabled() {
        return (this.mPasspointConfig == null && this.mWifiConfig == null) ? false : true;
    }

    public void setAutoJoinEnabled(boolean z) {
        PasspointConfiguration passpointConfiguration = this.mPasspointConfig;
        if (passpointConfiguration != null) {
            this.mWifiManager.allowAutojoinPasspoint(passpointConfiguration.getHomeSp().getFqdn(), z);
            return;
        }
        WifiConfiguration wifiConfiguration = this.mWifiConfig;
        if (wifiConfiguration != null) {
            this.mWifiManager.allowAutojoin(wifiConfiguration.networkId, z);
        }
    }

    public String getSecurityString(boolean z) {
        if (z) {
            return this.mContext.getString(R$string.wifitrackerlib_wifi_security_short_eap);
        }
        return this.mContext.getString(R$string.wifitrackerlib_wifi_security_eap);
    }

    public boolean isExpired() {
        if (this.mSubscriptionExpirationTimeInMillis > 0 && System.currentTimeMillis() >= this.mSubscriptionExpirationTimeInMillis) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public void updatePasspointConfig(PasspointConfiguration passpointConfiguration) {
        this.mPasspointConfig = passpointConfiguration;
        if (passpointConfiguration != null) {
            this.mFriendlyName = passpointConfiguration.getHomeSp().getFriendlyName();
            this.mSubscriptionExpirationTimeInMillis = passpointConfiguration.getSubscriptionExpirationTimeMillis();
            this.mMeteredOverride = passpointConfiguration.getMeteredOverride();
        }
        notifyOnUpdated();
    }

    /* access modifiers changed from: package-private */
    public void updateScanResultInfo(WifiConfiguration wifiConfiguration, List<ScanResult> list, List<ScanResult> list2) throws IllegalArgumentException {
        this.mIsRoaming = false;
        this.mWifiConfig = wifiConfiguration;
        synchronized (this.mLock) {
            this.mCurrentHomeScanResults.clear();
            this.mCurrentRoamingScanResults.clear();
            if (list != null) {
                this.mCurrentHomeScanResults.addAll(list);
            }
            if (list2 != null) {
                this.mCurrentRoamingScanResults.addAll(list2);
            }
        }
        int i = -1;
        if (this.mWifiConfig != null) {
            this.mSecurity = Utils.getSecurityTypeFromWifiConfiguration(wifiConfiguration);
            ArrayList arrayList = new ArrayList();
            if (list != null && !list.isEmpty()) {
                arrayList.addAll(list);
            } else if (list2 != null && !list2.isEmpty()) {
                arrayList.addAll(list2);
                this.mIsRoaming = true;
            }
            ScanResult bestScanResultByLevel = Utils.getBestScanResultByLevel(arrayList);
            if (bestScanResultByLevel != null) {
                WifiConfiguration wifiConfiguration2 = this.mWifiConfig;
                wifiConfiguration2.SSID = "\"" + bestScanResultByLevel.SSID + "\"";
            }
            if (getConnectedState() == 0) {
                if (bestScanResultByLevel != null) {
                    i = this.mWifiManager.calculateSignalLevel(bestScanResultByLevel.level);
                }
                this.mLevel = i;
                this.mSpeed = Utils.getAverageSpeedFromScanResults(this.mScoreCache, arrayList);
            }
        } else {
            this.mLevel = -1;
        }
        notifyOnUpdated();
    }

    /* access modifiers changed from: package-private */
    public void onScoreCacheUpdated() {
        WifiInfo wifiInfo = this.mWifiInfo;
        if (wifiInfo != null) {
            this.mSpeed = Utils.getSpeedFromWifiInfo(this.mScoreCache, wifiInfo);
        } else {
            synchronized (this.mLock) {
                if (!this.mCurrentHomeScanResults.isEmpty()) {
                    this.mSpeed = Utils.getAverageSpeedFromScanResults(this.mScoreCache, this.mCurrentHomeScanResults);
                } else {
                    this.mSpeed = Utils.getAverageSpeedFromScanResults(this.mScoreCache, this.mCurrentRoamingScanResults);
                }
            }
        }
        notifyOnUpdated();
    }

    /* access modifiers changed from: protected */
    public boolean connectionInfoMatches(WifiInfo wifiInfo, NetworkInfo networkInfo) {
        if (!wifiInfo.isPasspointAp()) {
            return false;
        }
        return TextUtils.equals(wifiInfo.getPasspointFqdn(), this.mFqdn);
    }

    /* access modifiers changed from: package-private */
    public void updateNetworkCapabilities(NetworkCapabilities networkCapabilities) {
        super.updateNetworkCapabilities(networkCapabilities);
        if (canSignIn() && this.mShouldAutoOpenCaptivePortal) {
            this.mShouldAutoOpenCaptivePortal = false;
            signIn((WifiEntry.SignInCallback) null);
        }
    }

    /* access modifiers changed from: package-private */
    public static String uniqueIdToPasspointWifiEntryKey(String str) {
        Preconditions.checkNotNull(str, "Cannot create key with null unique id!");
        return "PasspointWifiEntry:" + str;
    }

    /* access modifiers changed from: package-private */
    public String getNetworkSelectionDescription() {
        return Utils.getNetworkSelectionDescription(this.mWifiConfig);
    }

    /* access modifiers changed from: package-private */
    public void setOsuWifiEntry(OsuWifiEntry osuWifiEntry) {
        this.mOsuWifiEntry = osuWifiEntry;
        if (osuWifiEntry != null) {
            osuWifiEntry.setListener(this);
        }
    }

    public void onUpdated() {
        notifyOnUpdated();
    }

    public boolean canSignIn() {
        NetworkCapabilities networkCapabilities = this.mNetworkCapabilities;
        return networkCapabilities != null && networkCapabilities.hasCapability(17);
    }

    public void signIn(WifiEntry.SignInCallback signInCallback) {
        if (canSignIn()) {
            ((ConnectivityManager) this.mContext.getSystemService("connectivity")).startCaptivePortalApp(this.mWifiManager.getCurrentNetwork());
        }
    }

    public PasspointConfiguration getPasspointConfig() {
        return this.mPasspointConfig;
    }
}
