package com.google.android.wifitrackerlib;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.text.TextUtils;
import android.util.ArraySet;
import com.android.internal.annotations.VisibleForTesting;
import com.android.wifitrackerlib.WifiEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@VisibleForTesting
public class WsuManager {
    private final Context mContext;
    private final WifiManager mWifiManager;
    private final Handler mWorkerHandler;
    private final Set<WsuProvidersLoadCallback> mWsuProvidersLoadCallbacks = new ArraySet();
    private final Set<WsuProvisionStatusUpdateCallback> mWsuProvisionStatusUpdateCallbacks = new ArraySet();
    private final List<WsuServiceClient> mWsuServiceClients = new ArrayList();

    public interface WsuProvidersLoadCallback {
        void onLoaded();
    }

    public interface WsuProvisionStatusUpdateCallback {
        void onProvisionStatusChanged(WsuProvider wsuProvider, int i);
    }

    public interface WsuSignupAction {
        void onExecute();
    }

    WsuManager(Context context, WifiManager wifiManager, Handler handler) {
        this.mContext = context;
        this.mWifiManager = wifiManager;
        this.mWorkerHandler = handler;
        for (String wsuServiceClient : loadWsuServicePkgs()) {
            this.mWsuServiceClients.add(new WsuServiceClient(this.mContext, this.mWorkerHandler, this.mWifiManager, this, wsuServiceClient));
        }
    }

    private List<String> loadWsuServicePkgs() {
        return Arrays.asList(this.mContext.getResources().getStringArray(R$array.wifitrackerlib_wsu_service_provider_packages));
    }

    @VisibleForTesting
    public void bindAllServices() {
        for (WsuServiceClient bindWsuService : this.mWsuServiceClients) {
            bindWsuService.bindWsuService();
        }
    }

    @VisibleForTesting
    public void unbindAllServices() {
        for (WsuServiceClient unbindWsuService : this.mWsuServiceClients) {
            unbindWsuService.unbindWsuService();
        }
    }

    @VisibleForTesting
    public Map<WsuProvider, List<ScanResult>> getMatchingWsuProviders(List<ScanResult> list) {
        HashMap hashMap = new HashMap();
        for (WsuServiceClient matchingWsuProviders : this.mWsuServiceClients) {
            hashMap.putAll(matchingWsuProviders.getMatchingWsuProviders(list));
        }
        return hashMap;
    }

    @VisibleForTesting
    public void addWsuProvidersLoadCallback(WsuProvidersLoadCallback wsuProvidersLoadCallback) {
        this.mWsuProvidersLoadCallbacks.add(wsuProvidersLoadCallback);
    }

    @VisibleForTesting
    public void removeWsuProvidersLoadCallback(WsuProvidersLoadCallback wsuProvidersLoadCallback) {
        this.mWsuProvidersLoadCallbacks.remove(wsuProvidersLoadCallback);
    }

    @VisibleForTesting
    public void nofityWsuProvidersLoaded() {
        for (WsuProvidersLoadCallback onLoaded : this.mWsuProvidersLoadCallbacks) {
            onLoaded.onLoaded();
        }
    }

    @VisibleForTesting
    public void addWsuProvisionStatusUpdateCallback(WsuProvisionStatusUpdateCallback wsuProvisionStatusUpdateCallback) {
        this.mWsuProvisionStatusUpdateCallbacks.add(wsuProvisionStatusUpdateCallback);
    }

    @VisibleForTesting
    public void removeWsuProvisionStatusUpdateCallback(WsuProvisionStatusUpdateCallback wsuProvisionStatusUpdateCallback) {
        this.mWsuProvisionStatusUpdateCallbacks.remove(wsuProvisionStatusUpdateCallback);
    }

    @VisibleForTesting
    public void notifyWsuProvisionStatusUpdated(WsuProvider wsuProvider, int i) {
        for (WsuProvisionStatusUpdateCallback onProvisionStatusChanged : this.mWsuProvisionStatusUpdateCallbacks) {
            onProvisionStatusChanged.onProvisionStatusChanged(wsuProvider, i);
        }
    }

    @VisibleForTesting
    public WsuSignupAction createSignupAction(WsuProvider wsuProvider) {
        for (WsuServiceClient next : this.mWsuServiceClients) {
            if (next.getPackageName().equals(wsuProvider.servicePackageName)) {
                return next.createSignupAction(wsuProvider.networkGroupIdentity);
            }
        }
        return null;
    }

    @VisibleForTesting
    public WifiEntry.ManageSubscriptionAction tryGetManageSubscriptionAction(WifiEntry wifiEntry) {
        WifiEntry.ManageSubscriptionAction manageSubscriptionAction = null;
        for (WsuServiceClient tryGetManageSubscriptionAction : this.mWsuServiceClients) {
            manageSubscriptionAction = tryGetManageSubscriptionAction.tryGetManageSubscriptionAction(wifiEntry);
        }
        return manageSubscriptionAction;
    }

    @VisibleForTesting
    public String tryGetOverrideConnectedSummary(WifiEntry wifiEntry) {
        for (WsuServiceClient tryGetOverrideConnectedSummary : this.mWsuServiceClients) {
            String tryGetOverrideConnectedSummary2 = tryGetOverrideConnectedSummary.tryGetOverrideConnectedSummary(wifiEntry);
            if (!TextUtils.isEmpty(tryGetOverrideConnectedSummary2)) {
                return tryGetOverrideConnectedSummary2;
            }
        }
        return null;
    }
}
