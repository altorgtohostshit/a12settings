package com.android.wifitrackerlib;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkScoreManager;
import android.net.NetworkScorerAppData;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiEnterpriseConfig;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkScoreCache;
import android.os.Handler;
import android.os.SystemClock;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.wifitrackerlib.WifiEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@VisibleForTesting
public class StandardWifiEntry extends WifiEntry {
    private final Context mContext;
    private int mEapType;
    private final boolean mIsEnhancedOpenSupported;
    private boolean mIsUserShareable;
    private final boolean mIsWpa3SaeSupported;
    private final boolean mIsWpa3SuiteBSupported;
    private final StandardWifiEntryKey mKey;
    private final Object mLock;
    private final Map<Integer, List<ScanResult>> mMatchingScanResults;
    private final Map<Integer, WifiConfiguration> mMatchingWifiConfigs;
    private int mPskType;
    private String mRecommendationServiceLabel;
    private boolean mShouldAutoOpenCaptivePortal;
    private final List<ScanResult> mTargetScanResults;
    private int mTargetSecurity;
    private WifiConfiguration mTargetWifiConfig;

    StandardWifiEntry(Context context, Handler handler, StandardWifiEntryKey standardWifiEntryKey, WifiManager wifiManager, WifiNetworkScoreCache wifiNetworkScoreCache, boolean z) {
        super(handler, wifiManager, wifiNetworkScoreCache, z);
        this.mLock = new Object();
        this.mMatchingScanResults = new HashMap();
        this.mMatchingWifiConfigs = new HashMap();
        this.mTargetScanResults = new ArrayList();
        this.mTargetSecurity = 0;
        this.mEapType = 2;
        this.mPskType = 3;
        this.mIsUserShareable = false;
        this.mShouldAutoOpenCaptivePortal = false;
        this.mContext = context;
        this.mKey = standardWifiEntryKey;
        this.mTargetSecurity = ((Integer) standardWifiEntryKey.getScanResultKey().getSecurityTypes().stream().sorted().findFirst().get()).intValue();
        this.mIsWpa3SaeSupported = wifiManager.isWpa3SaeSupported();
        this.mIsWpa3SuiteBSupported = wifiManager.isWpa3SuiteBSupported();
        this.mIsEnhancedOpenSupported = wifiManager.isEnhancedOpenSupported();
        updateRecommendationServiceLabel();
    }

    StandardWifiEntry(Context context, Handler handler, StandardWifiEntryKey standardWifiEntryKey, List<WifiConfiguration> list, List<ScanResult> list2, WifiManager wifiManager, WifiNetworkScoreCache wifiNetworkScoreCache, boolean z) throws IllegalArgumentException {
        this(context, handler, standardWifiEntryKey, wifiManager, wifiNetworkScoreCache, z);
        if (list != null && !list.isEmpty()) {
            updateConfig(list);
        }
        if (list2 != null && !list2.isEmpty()) {
            updateScanResultInfo(list2);
        }
    }

    public String getKey() {
        return this.mKey.toString();
    }

    /* access modifiers changed from: package-private */
    public StandardWifiEntryKey getStandardWifiEntryKey() {
        return this.mKey;
    }

    public String getTitle() {
        return this.mKey.getScanResultKey().getSsid();
    }

    public String getSummary(boolean z) {
        WifiConfiguration wifiConfiguration;
        StringJoiner stringJoiner = new StringJoiner(this.mContext.getString(R$string.wifitrackerlib_summary_separator));
        if (!z && this.mForSavedNetworksPage && (wifiConfiguration = this.mTargetWifiConfig) != null) {
            String appLabel = Utils.getAppLabel(this.mContext, wifiConfiguration.creatorName);
            if (!TextUtils.isEmpty(appLabel)) {
                stringJoiner.add(this.mContext.getString(R$string.wifitrackerlib_saved_network, new Object[]{appLabel}));
            }
        }
        if (getConnectedState() == 0) {
            String disconnectedStateDescription = Utils.getDisconnectedStateDescription(this.mContext, this);
            if (!TextUtils.isEmpty(disconnectedStateDescription)) {
                stringJoiner.add(disconnectedStateDescription);
            } else if (z) {
                stringJoiner.add(this.mContext.getString(R$string.wifitrackerlib_wifi_disconnected));
            } else if (!this.mForSavedNetworksPage) {
                if (isSuggestion()) {
                    Context context = this.mContext;
                    String carrierNameForSubId = Utils.getCarrierNameForSubId(context, Utils.getSubIdForConfig(context, this.mTargetWifiConfig));
                    String appLabel2 = Utils.getAppLabel(this.mContext, this.mTargetWifiConfig.creatorName);
                    if (TextUtils.isEmpty(appLabel2)) {
                        appLabel2 = this.mTargetWifiConfig.creatorName;
                    }
                    Context context2 = this.mContext;
                    int i = R$string.wifitrackerlib_available_via_app;
                    Object[] objArr = new Object[1];
                    if (carrierNameForSubId == null) {
                        carrierNameForSubId = appLabel2;
                    }
                    objArr[0] = carrierNameForSubId;
                    stringJoiner.add(context2.getString(i, objArr));
                } else if (isSaved()) {
                    stringJoiner.add(this.mContext.getString(R$string.wifitrackerlib_wifi_remembered));
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
                WifiConfiguration wifiConfiguration = this.mTargetWifiConfig;
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
            } else if (isSaved() || isSuggestion()) {
                if (this.mIsLowQuality) {
                    return this.mContext.getString(R$string.wifi_connected_low_quality);
                }
                String currentNetworkCapabilitiesInformation = Utils.getCurrentNetworkCapabilitiesInformation(this.mContext, this.mNetworkCapabilities);
                if (!TextUtils.isEmpty(currentNetworkCapabilitiesInformation)) {
                    return currentNetworkCapabilitiesInformation;
                }
            } else if (TextUtils.isEmpty(this.mRecommendationServiceLabel)) {
                return this.mContext.getString(R$string.wifitrackerlib_connected_via_network_scorer_default);
            } else {
                return String.format(this.mContext.getString(R$string.wifitrackerlib_connected_via_network_scorer), new Object[]{this.mRecommendationServiceLabel});
            }
        }
        return Utils.getNetworkDetailedState(this.mContext, this.mNetworkInfo);
    }

    public CharSequence getSecondSummary() {
        return getConnectedState() == 2 ? Utils.getImsiProtectionDescription(this.mContext, getWifiConfiguration()) : "";
    }

    public String getSsid() {
        return this.mKey.getScanResultKey().getSsid();
    }

    public int getSecurity() {
        return this.mTargetSecurity;
    }

    public String getMacAddress() {
        WifiInfo wifiInfo = this.mWifiInfo;
        if (wifiInfo != null) {
            String macAddress = wifiInfo.getMacAddress();
            if (!TextUtils.isEmpty(macAddress) && !TextUtils.equals(macAddress, "02:00:00:00:00:00")) {
                return macAddress;
            }
        }
        if (this.mTargetWifiConfig != null && getPrivacy() == 1) {
            return this.mTargetWifiConfig.getRandomizedMacAddress().toString();
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
        WifiConfiguration wifiConfiguration = this.mTargetWifiConfig;
        return wifiConfiguration != null && wifiConfiguration.meteredHint;
    }

    public boolean isSaved() {
        WifiConfiguration wifiConfiguration = this.mTargetWifiConfig;
        return wifiConfiguration != null && !wifiConfiguration.fromWifiNetworkSuggestion && !wifiConfiguration.isEphemeral();
    }

    public boolean isSuggestion() {
        WifiConfiguration wifiConfiguration = this.mTargetWifiConfig;
        return wifiConfiguration != null && wifiConfiguration.fromWifiNetworkSuggestion;
    }

    public WifiConfiguration getWifiConfiguration() {
        if (!isSaved()) {
            return null;
        }
        return this.mTargetWifiConfig;
    }

    public WifiEntry.ConnectedInfo getConnectedInfo() {
        return this.mConnectedInfo;
    }

    public boolean canConnect() {
        WifiConfiguration wifiConfiguration;
        WifiEnterpriseConfig wifiEnterpriseConfig;
        if (this.mLevel == -1 || getConnectedState() != 0) {
            return false;
        }
        if (this.mTargetSecurity != 3 || (wifiConfiguration = this.mTargetWifiConfig) == null || (wifiEnterpriseConfig = wifiConfiguration.enterpriseConfig) == null || !wifiEnterpriseConfig.isAuthenticationSimBased()) {
            return true;
        }
        List<SubscriptionInfo> activeSubscriptionInfoList = ((SubscriptionManager) this.mContext.getSystemService("telephony_subscription_service")).getActiveSubscriptionInfoList();
        if (!(activeSubscriptionInfoList == null || activeSubscriptionInfoList.size() == 0)) {
            if (this.mTargetWifiConfig.carrierId == -1) {
                return true;
            }
            for (SubscriptionInfo carrierId : activeSubscriptionInfoList) {
                if (carrierId.getCarrierId() == this.mTargetWifiConfig.carrierId) {
                    return true;
                }
            }
        }
        return false;
    }

    public void connect(WifiEntry.ConnectCallback connectCallback) {
        this.mConnectCallback = connectCallback;
        this.mShouldAutoOpenCaptivePortal = true;
        this.mWifiManager.stopRestrictingAutoJoinToSubscriptionId();
        if (!isSaved() && !isSuggestion()) {
            int i = this.mTargetSecurity;
            if (i == 0 || i == 4) {
                WifiConfiguration wifiConfiguration = new WifiConfiguration();
                wifiConfiguration.SSID = "\"" + this.mKey.getScanResultKey().getSsid() + "\"";
                if (this.mTargetSecurity == 4) {
                    wifiConfiguration.allowedKeyManagement.set(9);
                    wifiConfiguration.requirePmf = true;
                } else {
                    wifiConfiguration.allowedKeyManagement.set(0);
                }
                this.mWifiManager.connect(wifiConfiguration, new WifiEntry.ConnectActionListener());
            } else if (connectCallback != null) {
                this.mCallbackHandler.post(new StandardWifiEntry$$ExternalSyntheticLambda2(connectCallback));
            }
        } else if (!Utils.isSimCredential(this.mTargetWifiConfig) || Utils.isSimPresent(this.mContext, this.mTargetWifiConfig.carrierId)) {
            this.mWifiManager.connect(this.mTargetWifiConfig.networkId, new WifiEntry.ConnectActionListener());
        } else if (connectCallback != null) {
            this.mCallbackHandler.post(new StandardWifiEntry$$ExternalSyntheticLambda1(connectCallback));
        }
    }

    public boolean canDisconnect() {
        return getConnectedState() == 2;
    }

    public void disconnect(WifiEntry.DisconnectCallback disconnectCallback) {
        if (canDisconnect()) {
            this.mCalledDisconnect = true;
            this.mDisconnectCallback = disconnectCallback;
            this.mCallbackHandler.postDelayed(new StandardWifiEntry$$ExternalSyntheticLambda0(this, disconnectCallback), 10000);
            WifiManager wifiManager = this.mWifiManager;
            wifiManager.disableEphemeralNetwork("\"" + this.mKey.getScanResultKey().getSsid() + "\"");
            this.mWifiManager.disconnect();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$disconnect$2(WifiEntry.DisconnectCallback disconnectCallback) {
        if (disconnectCallback != null && this.mCalledDisconnect) {
            disconnectCallback.onDisconnectResult(1);
        }
    }

    public boolean canForget() {
        return getWifiConfiguration() != null;
    }

    public void forget(WifiEntry.ForgetCallback forgetCallback) {
        if (canForget()) {
            this.mForgetCallback = forgetCallback;
            this.mWifiManager.forget(this.mTargetWifiConfig.networkId, new WifiEntry.ForgetActionListener());
        }
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

    public boolean canShare() {
        if (getWifiConfiguration() == null) {
            return false;
        }
        int i = this.mTargetSecurity;
        if (i == 0 || i == 1 || i == 2 || i == 4 || i == 5) {
            return true;
        }
        return false;
    }

    public boolean canEasyConnect() {
        if (getWifiConfiguration() == null || !this.mWifiManager.isEasyConnectSupported()) {
            return false;
        }
        int i = this.mTargetSecurity;
        if (i == 2 || i == 5) {
            return true;
        }
        return false;
    }

    public int getMeteredChoice() {
        if (getWifiConfiguration() == null) {
            return 0;
        }
        int i = getWifiConfiguration().meteredOverride;
        if (i == 1) {
            return 1;
        }
        return i == 2 ? 2 : 0;
    }

    public boolean canSetMeteredChoice() {
        return getWifiConfiguration() != null;
    }

    public void setMeteredChoice(int i) {
        if (canSetMeteredChoice()) {
            if (i == 0) {
                this.mTargetWifiConfig.meteredOverride = 0;
            } else if (i == 1) {
                this.mTargetWifiConfig.meteredOverride = 1;
            } else if (i == 2) {
                this.mTargetWifiConfig.meteredOverride = 2;
            }
            this.mWifiManager.save(this.mTargetWifiConfig, (WifiManager.ActionListener) null);
        }
    }

    public boolean canSetPrivacy() {
        return isSaved();
    }

    public int getPrivacy() {
        WifiConfiguration wifiConfiguration = this.mTargetWifiConfig;
        return (wifiConfiguration == null || wifiConfiguration.macRandomizationSetting != 0) ? 1 : 0;
    }

    public void setPrivacy(int i) {
        if (canSetPrivacy()) {
            WifiConfiguration wifiConfiguration = this.mTargetWifiConfig;
            wifiConfiguration.macRandomizationSetting = i == 1 ? 3 : 0;
            this.mWifiManager.save(wifiConfiguration, (WifiManager.ActionListener) null);
        }
    }

    public boolean isAutoJoinEnabled() {
        WifiConfiguration wifiConfiguration = this.mTargetWifiConfig;
        if (wifiConfiguration == null) {
            return false;
        }
        return wifiConfiguration.allowAutojoin;
    }

    public boolean canSetAutoJoinEnabled() {
        return isSaved() || isSuggestion();
    }

    public void setAutoJoinEnabled(boolean z) {
        if (canSetAutoJoinEnabled()) {
            this.mWifiManager.allowAutojoin(this.mTargetWifiConfig.networkId, z);
        }
    }

    public String getSecurityString(boolean z) {
        switch (this.mTargetSecurity) {
            case 1:
                return this.mContext.getString(R$string.wifitrackerlib_wifi_security_wep);
            case 2:
                int i = this.mPskType;
                if (i != 0) {
                    if (i != 1) {
                        if (z) {
                            return this.mContext.getString(R$string.wifitrackerlib_wifi_security_short_wpa_wpa2_wpa3);
                        }
                        return this.mContext.getString(R$string.wifitrackerlib_wifi_security_wpa_wpa2_wpa3);
                    } else if (z) {
                        return this.mContext.getString(R$string.wifitrackerlib_wifi_security_short_wpa2_wpa3);
                    } else {
                        return this.mContext.getString(R$string.wifitrackerlib_wifi_security_wpa2_wpa3);
                    }
                } else if (z) {
                    return this.mContext.getString(R$string.wifitrackerlib_wifi_security_short_wpa);
                } else {
                    return this.mContext.getString(R$string.wifitrackerlib_wifi_security_wpa);
                }
            case 3:
                int i2 = this.mEapType;
                if (i2 != 0) {
                    if (i2 != 1) {
                        if (z) {
                            return this.mContext.getString(R$string.wifitrackerlib_wifi_security_short_eap);
                        }
                        return this.mContext.getString(R$string.wifitrackerlib_wifi_security_eap);
                    } else if (z) {
                        return this.mContext.getString(R$string.wifitrackerlib_wifi_security_short_eap_wpa2_wpa3);
                    } else {
                        return this.mContext.getString(R$string.wifitrackerlib_wifi_security_eap_wpa2_wpa3);
                    }
                } else if (z) {
                    return this.mContext.getString(R$string.wifitrackerlib_wifi_security_short_eap_wpa);
                } else {
                    return this.mContext.getString(R$string.wifitrackerlib_wifi_security_eap_wpa);
                }
            case 4:
                if (z) {
                    return this.mContext.getString(R$string.wifitrackerlib_wifi_security_short_owe);
                }
                return this.mContext.getString(R$string.wifitrackerlib_wifi_security_owe);
            case 5:
                if (z) {
                    return this.mContext.getString(R$string.wifitrackerlib_wifi_security_short_sae);
                }
                return this.mContext.getString(R$string.wifitrackerlib_wifi_security_sae);
            case 6:
                if (z) {
                    return this.mContext.getString(R$string.wifitrackerlib_wifi_security_short_eap_suiteb);
                }
                return this.mContext.getString(R$string.wifitrackerlib_wifi_security_eap_suiteb);
            default:
                if (z) {
                    return "";
                }
                return this.mContext.getString(R$string.wifitrackerlib_wifi_security_none);
        }
    }

    public boolean shouldEditBeforeConnect() {
        WifiConfiguration wifiConfiguration = getWifiConfiguration();
        if (wifiConfiguration == null) {
            return false;
        }
        WifiConfiguration.NetworkSelectionStatus networkSelectionStatus = wifiConfiguration.getNetworkSelectionStatus();
        if (networkSelectionStatus.getNetworkSelectionStatus() == 0 || (networkSelectionStatus.getDisableReasonCounter(2) <= 0 && networkSelectionStatus.getDisableReasonCounter(8) <= 0 && networkSelectionStatus.getDisableReasonCounter(5) <= 0)) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public void updateScanResultInfo(List<ScanResult> list) throws IllegalArgumentException {
        if (list == null) {
            list = new ArrayList<>();
        }
        String ssid = this.mKey.getScanResultKey().getSsid();
        for (ScanResult next : list) {
            if (!TextUtils.equals(next.SSID, ssid)) {
                throw new IllegalArgumentException("Attempted to update with wrong SSID! Expected: " + ssid + ", Actual: " + next.SSID + ", ScanResult: " + next);
            }
        }
        this.mMatchingScanResults.clear();
        for (ScanResult next2 : list) {
            for (Integer intValue : Utils.getSecurityTypesFromScanResult(next2)) {
                int intValue2 = intValue.intValue();
                if (isSecurityTypeSupported(intValue2)) {
                    if (!this.mMatchingScanResults.containsKey(Integer.valueOf(intValue2))) {
                        this.mMatchingScanResults.put(Integer.valueOf(intValue2), new ArrayList());
                    }
                    this.mMatchingScanResults.get(Integer.valueOf(intValue2)).add(next2);
                }
            }
        }
        updateTargetSecurityTypes();
        updateTargetScanResultInfo();
        notifyOnUpdated();
    }

    private void updateTargetScanResultInfo() {
        ScanResult bestScanResultByLevel = Utils.getBestScanResultByLevel(this.mTargetScanResults);
        if (bestScanResultByLevel != null) {
            updateEapType(bestScanResultByLevel);
            updatePskType(bestScanResultByLevel);
        }
        if (getConnectedState() == 0) {
            this.mLevel = bestScanResultByLevel != null ? this.mWifiManager.calculateSignalLevel(bestScanResultByLevel.level) : -1;
            synchronized (this.mLock) {
                this.mSpeed = Utils.getAverageSpeedFromScanResults(this.mScoreCache, this.mTargetScanResults);
            }
        }
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
    public void onScoreCacheUpdated() {
        WifiInfo wifiInfo = this.mWifiInfo;
        if (wifiInfo != null) {
            this.mSpeed = Utils.getSpeedFromWifiInfo(this.mScoreCache, wifiInfo);
        } else {
            synchronized (this.mLock) {
                this.mSpeed = Utils.getAverageSpeedFromScanResults(this.mScoreCache, this.mTargetScanResults);
            }
        }
        notifyOnUpdated();
    }

    private void updateEapType(ScanResult scanResult) {
        if (scanResult.capabilities.contains("RSN-EAP")) {
            this.mEapType = 1;
        } else if (scanResult.capabilities.contains("WPA-EAP")) {
            this.mEapType = 0;
        } else {
            this.mEapType = 2;
        }
    }

    private void updatePskType(ScanResult scanResult) {
        if (this.mTargetSecurity != 2) {
            this.mPskType = 3;
            return;
        }
        boolean contains = scanResult.capabilities.contains("WPA-PSK");
        boolean contains2 = scanResult.capabilities.contains("RSN-PSK");
        if (contains2 && contains) {
            this.mPskType = 2;
        } else if (contains2) {
            this.mPskType = 1;
        } else if (contains) {
            this.mPskType = 0;
        } else {
            this.mPskType = 3;
        }
    }

    /* access modifiers changed from: package-private */
    public void updateConfig(List<WifiConfiguration> list) throws IllegalArgumentException {
        if (list == null) {
            list = Collections.emptyList();
        }
        ScanResultKey scanResultKey = this.mKey.getScanResultKey();
        String ssid = scanResultKey.getSsid();
        Set<Integer> securityTypes = scanResultKey.getSecurityTypes();
        this.mMatchingWifiConfigs.clear();
        for (WifiConfiguration next : list) {
            if (TextUtils.equals(ssid, WifiInfo.sanitizeSsid(next.SSID))) {
                int securityTypeFromWifiConfiguration = Utils.getSecurityTypeFromWifiConfiguration(next);
                if (securityTypes.contains(Integer.valueOf(securityTypeFromWifiConfiguration))) {
                    this.mMatchingWifiConfigs.put(Integer.valueOf(securityTypeFromWifiConfiguration), next);
                } else {
                    throw new IllegalArgumentException("Attempted to update with wrong security! Expected one of: " + securityTypes + ", Actual: " + securityTypeFromWifiConfiguration + ", Config: " + next);
                }
            } else {
                throw new IllegalArgumentException("Attempted to update with wrong SSID! Expected: " + ssid + ", Actual: " + WifiInfo.sanitizeSsid(next.SSID) + ", Config: " + next);
            }
        }
        updateTargetSecurityTypes();
        updateTargetScanResultInfo();
        notifyOnUpdated();
    }

    private boolean isSecurityTypeSupported(int i) {
        if (i == 4) {
            return this.mIsEnhancedOpenSupported;
        }
        if (i == 5) {
            return this.mIsWpa3SaeSupported;
        }
        if (i != 6) {
            return true;
        }
        return this.mIsWpa3SuiteBSupported;
    }

    private void updateTargetSecurityTypes() {
        TreeSet treeSet = new TreeSet();
        WifiInfo wifiInfo = this.mWifiInfo;
        if (wifiInfo != null) {
            treeSet.add(Integer.valueOf(Utils.getSecurityTypeFromWifiInfo(wifiInfo)));
        }
        Set<Integer> securityTypes = this.mKey.getScanResultKey().getSecurityTypes();
        if (treeSet.isEmpty() && !this.mMatchingScanResults.isEmpty() && !this.mMatchingWifiConfigs.isEmpty()) {
            for (Integer intValue : securityTypes) {
                int intValue2 = intValue.intValue();
                if (this.mMatchingScanResults.containsKey(Integer.valueOf(intValue2)) && this.mMatchingWifiConfigs.containsKey(Integer.valueOf(intValue2)) && isSecurityTypeSupported(intValue2)) {
                    treeSet.add(Integer.valueOf(intValue2));
                }
            }
        }
        if (treeSet.isEmpty() && !this.mMatchingScanResults.isEmpty()) {
            for (Integer intValue3 : securityTypes) {
                int intValue4 = intValue3.intValue();
                if (this.mMatchingScanResults.containsKey(Integer.valueOf(intValue4)) && isSecurityTypeSupported(intValue4)) {
                    treeSet.add(Integer.valueOf(intValue4));
                }
            }
        }
        if (treeSet.isEmpty() && !this.mMatchingWifiConfigs.isEmpty()) {
            for (Integer intValue5 : securityTypes) {
                int intValue6 = intValue5.intValue();
                if (this.mMatchingWifiConfigs.containsKey(Integer.valueOf(intValue6)) && isSecurityTypeSupported(intValue6)) {
                    treeSet.add(Integer.valueOf(intValue6));
                }
            }
        }
        if (treeSet.isEmpty()) {
            treeSet.addAll(securityTypes);
        }
        if (treeSet.contains(4)) {
            this.mTargetSecurity = 4;
        } else {
            this.mTargetSecurity = ((Integer) treeSet.first()).intValue();
        }
        this.mTargetWifiConfig = this.mMatchingWifiConfigs.get(Integer.valueOf(this.mTargetSecurity));
        synchronized (this.mLock) {
            this.mTargetScanResults.clear();
            if (this.mMatchingScanResults.containsKey(Integer.valueOf(this.mTargetSecurity))) {
                this.mTargetScanResults.addAll(this.mMatchingScanResults.get(Integer.valueOf(this.mTargetSecurity)));
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void setUserShareable(boolean z) {
        this.mIsUserShareable = z;
    }

    /* access modifiers changed from: package-private */
    public boolean isUserShareable() {
        return this.mIsUserShareable;
    }

    /* access modifiers changed from: protected */
    public boolean connectionInfoMatches(WifiInfo wifiInfo, NetworkInfo networkInfo) {
        if (!wifiInfo.isPasspointAp() && !wifiInfo.isOsuAp()) {
            for (WifiConfiguration wifiConfiguration : this.mMatchingWifiConfigs.values()) {
                if (wifiConfiguration.networkId == wifiInfo.getNetworkId()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void updateRecommendationServiceLabel() {
        NetworkScorerAppData activeScorer = ((NetworkScoreManager) this.mContext.getSystemService("network_score")).getActiveScorer();
        if (activeScorer != null) {
            this.mRecommendationServiceLabel = activeScorer.getRecommendationServiceLabel();
        }
    }

    /* access modifiers changed from: protected */
    public String getScanResultDescription() {
        synchronized (this.mLock) {
            if (this.mTargetScanResults.size() == 0) {
                return "";
            }
            return "[" + getScanResultDescription(2400, 2500) + ";" + getScanResultDescription(4900, 5900) + ";" + getScanResultDescription(5925, 7125) + ";" + getScanResultDescription(58320, 70200) + "]";
        }
    }

    private String getScanResultDescription(int i, int i2) {
        List list;
        synchronized (this.mLock) {
            list = (List) this.mTargetScanResults.stream().filter(new StandardWifiEntry$$ExternalSyntheticLambda4(i, i2)).sorted(Comparator.comparingInt(StandardWifiEntry$$ExternalSyntheticLambda6.INSTANCE)).collect(Collectors.toList());
        }
        int size = list.size();
        if (size == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(size);
        sb.append(")");
        if (size > 4) {
            int asInt = list.stream().mapToInt(StandardWifiEntry$$ExternalSyntheticLambda5.INSTANCE).max().getAsInt();
            sb.append("max=");
            sb.append(asInt);
            sb.append(",");
        }
        list.forEach(new StandardWifiEntry$$ExternalSyntheticLambda3(this, sb, SystemClock.elapsedRealtime()));
        return sb.toString();
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getScanResultDescription$3(int i, int i2, ScanResult scanResult) {
        int i3 = scanResult.frequency;
        return i3 >= i && i3 <= i2;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ int lambda$getScanResultDescription$4(ScanResult scanResult) {
        return scanResult.level * -1;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$getScanResultDescription$6(StringBuilder sb, long j, ScanResult scanResult) {
        sb.append(getScanResultDescription(scanResult, j));
    }

    private String getScanResultDescription(ScanResult scanResult, long j) {
        StringBuilder sb = new StringBuilder();
        sb.append(" \n{");
        sb.append(scanResult.BSSID);
        WifiInfo wifiInfo = this.mWifiInfo;
        if (wifiInfo != null && scanResult.BSSID.equals(wifiInfo.getBSSID())) {
            sb.append("*");
        }
        sb.append("=");
        sb.append(scanResult.frequency);
        sb.append(",");
        sb.append(scanResult.level);
        sb.append(",");
        sb.append(((int) (j - (scanResult.timestamp / 1000))) / 1000);
        sb.append("s");
        sb.append("}");
        return sb.toString();
    }

    /* access modifiers changed from: package-private */
    public String getNetworkSelectionDescription() {
        return Utils.getNetworkSelectionDescription(getWifiConfiguration());
    }

    static class StandardWifiEntryKey {
        private boolean mIsNetworkRequest;
        private ScanResultKey mScanResultKey;
        private String mSuggestionProfileKey;

        StandardWifiEntryKey(ScanResultKey scanResultKey) {
            this.mScanResultKey = scanResultKey;
        }

        StandardWifiEntryKey(WifiConfiguration wifiConfiguration) {
            this.mScanResultKey = new ScanResultKey(wifiConfiguration);
            if (wifiConfiguration.fromWifiNetworkSuggestion) {
                this.mSuggestionProfileKey = new StringJoiner(",").add(wifiConfiguration.creatorName).add(String.valueOf(wifiConfiguration.carrierId)).add(String.valueOf(wifiConfiguration.subscriptionId)).toString();
            } else if (wifiConfiguration.fromWifiNetworkSpecifier) {
                this.mIsNetworkRequest = true;
            }
        }

        StandardWifiEntryKey(String str) {
            this.mScanResultKey = new ScanResultKey();
            if (!str.startsWith("StandardWifiEntry:")) {
                Log.e("StandardWifiEntry", "String key does not start with key prefix!");
                return;
            }
            try {
                JSONObject jSONObject = new JSONObject(str.substring(18));
                if (jSONObject.has("SCAN_RESULT_KEY")) {
                    this.mScanResultKey = new ScanResultKey(jSONObject.getString("SCAN_RESULT_KEY"));
                }
                if (jSONObject.has("SUGGESTION_PROFILE_KEY")) {
                    this.mSuggestionProfileKey = jSONObject.getString("SUGGESTION_PROFILE_KEY");
                }
                if (jSONObject.has("IS_NETWORK_REQUEST")) {
                    this.mIsNetworkRequest = jSONObject.getBoolean("IS_NETWORK_REQUEST");
                }
            } catch (JSONException e) {
                Log.e("StandardWifiEntry", "JSONException while converting StandardWifiEntryKey to string: " + e);
            }
        }

        public String toString() {
            JSONObject jSONObject = new JSONObject();
            try {
                ScanResultKey scanResultKey = this.mScanResultKey;
                if (scanResultKey != null) {
                    jSONObject.put("SCAN_RESULT_KEY", scanResultKey.toString());
                }
                String str = this.mSuggestionProfileKey;
                if (str != null) {
                    jSONObject.put("SUGGESTION_PROFILE_KEY", str);
                }
                boolean z = this.mIsNetworkRequest;
                if (z) {
                    jSONObject.put("IS_NETWORK_REQUEST", z);
                }
            } catch (JSONException e) {
                Log.wtf("StandardWifiEntry", "JSONException while converting StandardWifiEntryKey to string: " + e);
            }
            return "StandardWifiEntry:" + jSONObject.toString();
        }

        /* access modifiers changed from: package-private */
        public ScanResultKey getScanResultKey() {
            return this.mScanResultKey;
        }

        /* access modifiers changed from: package-private */
        public boolean isNetworkRequest() {
            return this.mIsNetworkRequest;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || StandardWifiEntryKey.class != obj.getClass()) {
                return false;
            }
            StandardWifiEntryKey standardWifiEntryKey = (StandardWifiEntryKey) obj;
            if (!Objects.equals(this.mScanResultKey, standardWifiEntryKey.mScanResultKey) || !TextUtils.equals(this.mSuggestionProfileKey, standardWifiEntryKey.mSuggestionProfileKey) || this.mIsNetworkRequest != standardWifiEntryKey.mIsNetworkRequest) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return Objects.hash(new Object[]{this.mScanResultKey, this.mSuggestionProfileKey, Boolean.valueOf(this.mIsNetworkRequest)});
        }
    }

    static class ScanResultKey {
        private Set<Integer> mSecurityTypes;
        private String mSsid;

        ScanResultKey() {
            this.mSecurityTypes = new HashSet();
        }

        ScanResultKey(String str, List<Integer> list) {
            this.mSecurityTypes = new HashSet();
            this.mSsid = str;
            for (Integer intValue : list) {
                int intValue2 = intValue.intValue();
                this.mSecurityTypes.add(Integer.valueOf(intValue2));
                if (intValue2 == 0) {
                    this.mSecurityTypes.add(4);
                } else if (intValue2 == 2) {
                    this.mSecurityTypes.add(5);
                } else if (intValue2 == 4) {
                    this.mSecurityTypes.add(0);
                } else if (intValue2 == 5) {
                    this.mSecurityTypes.add(2);
                }
            }
        }

        ScanResultKey(ScanResult scanResult) {
            this(scanResult.SSID, Utils.getSecurityTypesFromScanResult(scanResult));
        }

        ScanResultKey(WifiConfiguration wifiConfiguration) {
            this(WifiInfo.sanitizeSsid(wifiConfiguration.SSID), Collections.singletonList(Integer.valueOf(Utils.getSecurityTypeFromWifiConfiguration(wifiConfiguration))));
        }

        ScanResultKey(String str) {
            this.mSecurityTypes = new HashSet();
            try {
                JSONObject jSONObject = new JSONObject(str);
                this.mSsid = jSONObject.getString("SSID");
                JSONArray jSONArray = jSONObject.getJSONArray("SECURITY_TYPES");
                for (int i = 0; i < jSONArray.length(); i++) {
                    this.mSecurityTypes.add(Integer.valueOf(jSONArray.getInt(i)));
                }
            } catch (JSONException e) {
                Log.wtf("StandardWifiEntry", "JSONException while constructing ScanResultKey from string: " + e);
            }
        }

        public String toString() {
            JSONObject jSONObject = new JSONObject();
            try {
                String str = this.mSsid;
                if (str != null) {
                    jSONObject.put("SSID", str);
                }
                if (!this.mSecurityTypes.isEmpty()) {
                    JSONArray jSONArray = new JSONArray();
                    for (Integer intValue : this.mSecurityTypes) {
                        jSONArray.put(intValue.intValue());
                    }
                    jSONObject.put("SECURITY_TYPES", jSONArray);
                }
            } catch (JSONException e) {
                Log.e("StandardWifiEntry", "JSONException while converting ScanResultKey to string: " + e);
            }
            return jSONObject.toString();
        }

        /* access modifiers changed from: package-private */
        public String getSsid() {
            return this.mSsid;
        }

        /* access modifiers changed from: package-private */
        public Set<Integer> getSecurityTypes() {
            return this.mSecurityTypes;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || ScanResultKey.class != obj.getClass()) {
                return false;
            }
            ScanResultKey scanResultKey = (ScanResultKey) obj;
            if (!TextUtils.equals(this.mSsid, scanResultKey.mSsid) || !this.mSecurityTypes.equals(scanResultKey.mSecurityTypes)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return Objects.hash(new Object[]{this.mSsid, this.mSecurityTypes});
        }
    }
}
