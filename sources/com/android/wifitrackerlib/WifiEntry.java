package com.android.wifitrackerlib;

import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.RouteInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkScoreCache;
import android.os.Handler;
import androidx.core.util.Preconditions;
import com.android.net.module.util.NetUtils;
import com.android.settings.wifi.details2.WifiDetailPreferenceController2$$ExternalSyntheticLambda8;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class WifiEntry implements Comparable<WifiEntry> {
    protected Handler mCallbackHandler;
    protected boolean mCalledConnect = false;
    protected boolean mCalledDisconnect = false;
    protected ConnectCallback mConnectCallback;
    protected ConnectedInfo mConnectedInfo;
    protected DisconnectCallback mDisconnectCallback;
    final boolean mForSavedNetworksPage;
    protected ForgetCallback mForgetCallback;
    private boolean mIsDefaultNetwork;
    protected boolean mIsLowQuality;
    private boolean mIsValidated;
    protected int mLevel = -1;
    private WifiEntryCallback mListener;
    private Optional<ManageSubscriptionAction> mManageSubscriptionAction = Optional.empty();
    protected NetworkCapabilities mNetworkCapabilities;
    protected NetworkInfo mNetworkInfo;
    protected WifiNetworkScoreCache mScoreCache;
    protected int mSpeed = 0;
    protected WifiInfo mWifiInfo;
    protected final WifiManager mWifiManager;

    public interface ConnectCallback {
        void onConnectResult(int i);
    }

    public static class ConnectedInfo {
        public List<String> dnsServers = new ArrayList();
        public int frequencyMhz;
        public String gateway;
        public String ipAddress;
        public List<String> ipv6Addresses = new ArrayList();
        public int linkSpeedMbps;
        public String subnetMask;
        public int wifiStandard = 0;
    }

    public interface DisconnectCallback {
        void onDisconnectResult(int i);
    }

    public interface ForgetCallback {
        void onForgetResult(int i);
    }

    public interface ManageSubscriptionAction {
        void onExecute();
    }

    public interface SignInCallback {
    }

    public interface WifiEntryCallback {
        void onUpdated();
    }

    public boolean canConnect() {
        return false;
    }

    public boolean canDisconnect() {
        return false;
    }

    public boolean canEasyConnect() {
        return false;
    }

    public boolean canForget() {
        return false;
    }

    public boolean canSetAutoJoinEnabled() {
        return false;
    }

    public boolean canSetMeteredChoice() {
        return false;
    }

    public boolean canSetPrivacy() {
        return false;
    }

    public boolean canShare() {
        return false;
    }

    public boolean canSignIn() {
        return false;
    }

    public void connect(ConnectCallback connectCallback) {
    }

    /* access modifiers changed from: protected */
    public boolean connectionInfoMatches(WifiInfo wifiInfo, NetworkInfo networkInfo) {
        return false;
    }

    public void disconnect(DisconnectCallback disconnectCallback) {
    }

    public void forget(ForgetCallback forgetCallback) {
    }

    public String getHelpUriString() {
        return null;
    }

    public String getKey() {
        return "";
    }

    public String getMacAddress() {
        return null;
    }

    public int getMeteredChoice() {
        return 0;
    }

    /* access modifiers changed from: package-private */
    public String getNetworkSelectionDescription() {
        return "";
    }

    public int getPrivacy() {
        return 2;
    }

    /* access modifiers changed from: protected */
    public String getScanResultDescription() {
        return "";
    }

    public CharSequence getSecondSummary() {
        return "";
    }

    public int getSecurity() {
        return 0;
    }

    public String getSecurityString(boolean z) {
        return "";
    }

    public String getSsid() {
        return null;
    }

    public String getSummary(boolean z) {
        return "";
    }

    public String getTitle() {
        return "";
    }

    public WifiConfiguration getWifiConfiguration() {
        return null;
    }

    public boolean isAutoJoinEnabled() {
        return false;
    }

    public boolean isMetered() {
        return false;
    }

    public boolean isSaved() {
        return false;
    }

    public boolean isSubscription() {
        return false;
    }

    public boolean isSuggestion() {
        return false;
    }

    public void setAutoJoinEnabled(boolean z) {
    }

    public void setMeteredChoice(int i) {
    }

    public void setPrivacy(int i) {
    }

    public boolean shouldEditBeforeConnect() {
        return false;
    }

    public void signIn(SignInCallback signInCallback) {
    }

    public WifiEntry(Handler handler, WifiManager wifiManager, WifiNetworkScoreCache wifiNetworkScoreCache, boolean z) throws IllegalArgumentException {
        Preconditions.checkNotNull(handler, "Cannot construct with null handler!");
        Preconditions.checkNotNull(wifiManager, "Cannot construct with null WifiManager!");
        this.mCallbackHandler = handler;
        this.mForSavedNetworksPage = z;
        this.mWifiManager = wifiManager;
        this.mScoreCache = wifiNetworkScoreCache;
    }

    public int getConnectedState() {
        NetworkInfo networkInfo = this.mNetworkInfo;
        if (networkInfo == null) {
            return 0;
        }
        switch (C15331.$SwitchMap$android$net$NetworkInfo$DetailedState[networkInfo.getDetailedState().ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                return 1;
            case 7:
                return 2;
            default:
                return 0;
        }
    }

    /* renamed from: com.android.wifitrackerlib.WifiEntry$1 */
    static /* synthetic */ class C15331 {
        static final /* synthetic */ int[] $SwitchMap$android$net$NetworkInfo$DetailedState;

        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|1|2|3|4|5|6|7|8|9|10|11|12|(3:13|14|16)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(16:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|16) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0049 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                android.net.NetworkInfo$DetailedState[] r0 = android.net.NetworkInfo.DetailedState.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$android$net$NetworkInfo$DetailedState = r0
                android.net.NetworkInfo$DetailedState r1 = android.net.NetworkInfo.DetailedState.SCANNING     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$android$net$NetworkInfo$DetailedState     // Catch:{ NoSuchFieldError -> 0x001d }
                android.net.NetworkInfo$DetailedState r1 = android.net.NetworkInfo.DetailedState.CONNECTING     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$android$net$NetworkInfo$DetailedState     // Catch:{ NoSuchFieldError -> 0x0028 }
                android.net.NetworkInfo$DetailedState r1 = android.net.NetworkInfo.DetailedState.AUTHENTICATING     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$android$net$NetworkInfo$DetailedState     // Catch:{ NoSuchFieldError -> 0x0033 }
                android.net.NetworkInfo$DetailedState r1 = android.net.NetworkInfo.DetailedState.OBTAINING_IPADDR     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$android$net$NetworkInfo$DetailedState     // Catch:{ NoSuchFieldError -> 0x003e }
                android.net.NetworkInfo$DetailedState r1 = android.net.NetworkInfo.DetailedState.VERIFYING_POOR_LINK     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$android$net$NetworkInfo$DetailedState     // Catch:{ NoSuchFieldError -> 0x0049 }
                android.net.NetworkInfo$DetailedState r1 = android.net.NetworkInfo.DetailedState.CAPTIVE_PORTAL_CHECK     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                int[] r0 = $SwitchMap$android$net$NetworkInfo$DetailedState     // Catch:{ NoSuchFieldError -> 0x0054 }
                android.net.NetworkInfo$DetailedState r1 = android.net.NetworkInfo.DetailedState.CONNECTED     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.WifiEntry.C15331.<clinit>():void");
        }
    }

    public String getSummary() {
        return getSummary(true);
    }

    public int getLevel() {
        return this.mLevel;
    }

    public boolean shouldShowXLevelIcon() {
        return getConnectedState() != 0 && (!this.mIsValidated || !this.mIsDefaultNetwork) && !canSignIn();
    }

    public boolean hasInternetAccess() {
        return this.mIsValidated;
    }

    public int getSpeed() {
        return this.mSpeed;
    }

    public ConnectedInfo getConnectedInfo() {
        if (getConnectedState() != 2) {
            return null;
        }
        return this.mConnectedInfo;
    }

    public boolean canManageSubscription() {
        return this.mManageSubscriptionAction.isPresent();
    }

    public void manageSubscription() {
        this.mManageSubscriptionAction.ifPresent(WifiEntry$$ExternalSyntheticLambda3.INSTANCE);
    }

    public void setManageSubscriptionAction(ManageSubscriptionAction manageSubscriptionAction) {
        boolean z = !this.mManageSubscriptionAction.isPresent();
        this.mManageSubscriptionAction = Optional.of(manageSubscriptionAction);
        if (z) {
            notifyOnUpdated();
        }
    }

    /* access modifiers changed from: package-private */
    public String getNetworkCapabilityDescription() {
        StringBuilder sb = new StringBuilder();
        if (getConnectedState() == 2) {
            sb.append("isValidated:");
            sb.append(this.mIsValidated);
            sb.append(", isDefaultNetwork:");
            sb.append(this.mIsDefaultNetwork);
            sb.append(", isLowQuality:");
            sb.append(this.mIsLowQuality);
        }
        return sb.toString();
    }

    public void setListener(WifiEntryCallback wifiEntryCallback) {
        this.mListener = wifiEntryCallback;
    }

    /* access modifiers changed from: protected */
    public void notifyOnUpdated() {
        if (this.mListener != null) {
            this.mCallbackHandler.post(new WifiEntry$$ExternalSyntheticLambda2(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyOnUpdated$0() {
        WifiEntryCallback wifiEntryCallback = this.mListener;
        if (wifiEntryCallback != null) {
            wifiEntryCallback.onUpdated();
        }
    }

    /* access modifiers changed from: package-private */
    public void updateConnectionInfo(WifiInfo wifiInfo, NetworkInfo networkInfo) {
        if (wifiInfo == null || networkInfo == null || !connectionInfoMatches(wifiInfo, networkInfo)) {
            this.mNetworkInfo = null;
            this.mNetworkCapabilities = null;
            this.mConnectedInfo = null;
            this.mIsValidated = false;
            this.mIsDefaultNetwork = false;
            this.mIsLowQuality = false;
            if (this.mCalledDisconnect) {
                this.mCalledDisconnect = false;
                this.mCallbackHandler.post(new WifiEntry$$ExternalSyntheticLambda0(this));
            }
        } else {
            this.mWifiInfo = wifiInfo;
            this.mNetworkInfo = networkInfo;
            int rssi = wifiInfo.getRssi();
            if (rssi != -127) {
                this.mLevel = this.mWifiManager.calculateSignalLevel(rssi);
                this.mSpeed = Utils.getSpeedFromWifiInfo(this.mScoreCache, wifiInfo);
            }
            if (getConnectedState() == 2) {
                if (this.mCalledConnect) {
                    this.mCalledConnect = false;
                    this.mCallbackHandler.post(new WifiEntry$$ExternalSyntheticLambda1(this));
                }
                if (this.mConnectedInfo == null) {
                    this.mConnectedInfo = new ConnectedInfo();
                }
                this.mConnectedInfo.frequencyMhz = wifiInfo.getFrequency();
                this.mConnectedInfo.linkSpeedMbps = wifiInfo.getLinkSpeed();
                this.mConnectedInfo.wifiStandard = wifiInfo.getWifiStandard();
            }
        }
        notifyOnUpdated();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateConnectionInfo$1() {
        ConnectCallback connectCallback = this.mConnectCallback;
        if (connectCallback != null) {
            connectCallback.onConnectResult(0);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateConnectionInfo$2() {
        DisconnectCallback disconnectCallback = this.mDisconnectCallback;
        if (disconnectCallback != null) {
            disconnectCallback.onDisconnectResult(0);
        }
    }

    /* access modifiers changed from: package-private */
    public void updateLinkProperties(LinkProperties linkProperties) {
        if (linkProperties == null || getConnectedState() != 2) {
            this.mConnectedInfo = null;
            notifyOnUpdated();
            return;
        }
        if (this.mConnectedInfo == null) {
            this.mConnectedInfo = new ConnectedInfo();
        }
        ArrayList arrayList = new ArrayList();
        for (LinkAddress next : linkProperties.getLinkAddresses()) {
            if (next.getAddress() instanceof Inet4Address) {
                this.mConnectedInfo.ipAddress = next.getAddress().getHostAddress();
                try {
                    InetAddress byAddress = InetAddress.getByAddress(new byte[]{-1, -1, -1, -1});
                    this.mConnectedInfo.subnetMask = NetUtils.getNetworkPart(byAddress, next.getPrefixLength()).getHostAddress();
                } catch (UnknownHostException unused) {
                }
            } else if (next.getAddress() instanceof Inet6Address) {
                arrayList.add(next.getAddress().getHostAddress());
            }
        }
        this.mConnectedInfo.ipv6Addresses = arrayList;
        Iterator<RouteInfo> it = linkProperties.getRoutes().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            RouteInfo next2 = it.next();
            if (next2.isDefaultRoute() && (next2.getDestination().getAddress() instanceof Inet4Address) && next2.hasGateway()) {
                this.mConnectedInfo.gateway = next2.getGateway().getHostAddress();
                break;
            }
        }
        this.mConnectedInfo.dnsServers = (List) linkProperties.getDnsServers().stream().map(WifiDetailPreferenceController2$$ExternalSyntheticLambda8.INSTANCE).collect(Collectors.toList());
        notifyOnUpdated();
    }

    /* access modifiers changed from: package-private */
    public void setIsDefaultNetwork(boolean z) {
        this.mIsDefaultNetwork = z;
        notifyOnUpdated();
    }

    /* access modifiers changed from: package-private */
    public void setIsLowQuality(boolean z) {
        this.mIsLowQuality = z;
    }

    /* access modifiers changed from: package-private */
    public void updateNetworkCapabilities(NetworkCapabilities networkCapabilities) {
        this.mNetworkCapabilities = networkCapabilities;
        if (this.mConnectedInfo != null) {
            this.mIsValidated = networkCapabilities != null && networkCapabilities.hasCapability(16);
            notifyOnUpdated();
        }
    }

    /* access modifiers changed from: package-private */
    public String getWifiInfoDescription() {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (getConnectedState() == 2 && this.mWifiInfo != null) {
            stringJoiner.add("f = " + this.mWifiInfo.getFrequency());
            String bssid = this.mWifiInfo.getBSSID();
            if (bssid != null) {
                stringJoiner.add(bssid);
            }
            stringJoiner.add("standard = " + this.mWifiInfo.getWifiStandard());
            stringJoiner.add("rssi = " + this.mWifiInfo.getRssi());
            stringJoiner.add("score = " + this.mWifiInfo.getScore());
            stringJoiner.add(String.format(" tx=%.1f,", new Object[]{Double.valueOf(this.mWifiInfo.getSuccessfulTxPacketsPerSecond())}));
            stringJoiner.add(String.format("%.1f,", new Object[]{Double.valueOf(this.mWifiInfo.getRetriedTxPacketsPerSecond())}));
            stringJoiner.add(String.format("%.1f ", new Object[]{Double.valueOf(this.mWifiInfo.getLostTxPacketsPerSecond())}));
            stringJoiner.add(String.format("rx=%.1f", new Object[]{Double.valueOf(this.mWifiInfo.getSuccessfulRxPacketsPerSecond())}));
        }
        return stringJoiner.toString();
    }

    protected class ConnectActionListener implements WifiManager.ActionListener {
        protected ConnectActionListener() {
        }

        public void onSuccess() {
            WifiEntry wifiEntry = WifiEntry.this;
            wifiEntry.mCalledConnect = true;
            wifiEntry.mCallbackHandler.postDelayed(new WifiEntry$ConnectActionListener$$ExternalSyntheticLambda0(this), 10000);
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onSuccess$0() {
            WifiEntry wifiEntry = WifiEntry.this;
            if (wifiEntry.mConnectCallback != null && wifiEntry.mCalledConnect && wifiEntry.getConnectedState() == 0) {
                WifiEntry.this.mConnectCallback.onConnectResult(2);
                WifiEntry.this.mCalledConnect = false;
            }
        }

        public void onFailure(int i) {
            WifiEntry.this.mCallbackHandler.post(new WifiEntry$ConnectActionListener$$ExternalSyntheticLambda1(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onFailure$1() {
            ConnectCallback connectCallback = WifiEntry.this.mConnectCallback;
            if (connectCallback != null) {
                connectCallback.onConnectResult(2);
            }
        }
    }

    protected class ForgetActionListener implements WifiManager.ActionListener {
        protected ForgetActionListener() {
        }

        public void onSuccess() {
            WifiEntry.this.mCallbackHandler.post(new WifiEntry$ForgetActionListener$$ExternalSyntheticLambda0(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onSuccess$0() {
            ForgetCallback forgetCallback = WifiEntry.this.mForgetCallback;
            if (forgetCallback != null) {
                forgetCallback.onForgetResult(0);
            }
        }

        public void onFailure(int i) {
            WifiEntry.this.mCallbackHandler.post(new WifiEntry$ForgetActionListener$$ExternalSyntheticLambda1(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onFailure$1() {
            ForgetCallback forgetCallback = WifiEntry.this.mForgetCallback;
            if (forgetCallback != null) {
                forgetCallback.onForgetResult(1);
            }
        }
    }

    public int compareTo(WifiEntry wifiEntry) {
        if (getLevel() != -1 && wifiEntry.getLevel() == -1) {
            return -1;
        }
        if (getLevel() == -1 && wifiEntry.getLevel() != -1) {
            return 1;
        }
        if (isSubscription() && !wifiEntry.isSubscription()) {
            return -1;
        }
        if (!isSubscription() && wifiEntry.isSubscription()) {
            return 1;
        }
        if (isSaved() && !wifiEntry.isSaved()) {
            return -1;
        }
        if (!isSaved() && wifiEntry.isSaved()) {
            return 1;
        }
        if (isSuggestion() && !wifiEntry.isSuggestion()) {
            return -1;
        }
        if (!isSuggestion() && wifiEntry.isSuggestion()) {
            return 1;
        }
        if (getLevel() > wifiEntry.getLevel()) {
            return -1;
        }
        if (getLevel() < wifiEntry.getLevel()) {
            return 1;
        }
        return getTitle().compareTo(wifiEntry.getTitle());
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof WifiEntry)) {
            return false;
        }
        return getKey().equals(((WifiEntry) obj).getKey());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getKey());
        sb.append(",title:");
        sb.append(getTitle());
        sb.append(",summary:");
        sb.append(getSummary());
        sb.append(",isSaved:");
        sb.append(isSaved());
        sb.append(",isSubscription:");
        sb.append(isSubscription());
        sb.append(",isSuggestion:");
        sb.append(isSuggestion());
        sb.append(",level:");
        sb.append(getLevel());
        sb.append(shouldShowXLevelIcon() ? "X" : "");
        sb.append(",security:");
        sb.append(getSecurity());
        sb.append(",connected:");
        sb.append(getConnectedState() == 2 ? "true" : "false");
        sb.append(",connectedInfo:");
        sb.append(getConnectedInfo());
        sb.append(",isValidated:");
        sb.append(this.mIsValidated);
        sb.append(",isDefaultNetwork:");
        sb.append(this.mIsDefaultNetwork);
        return sb.toString();
    }
}
