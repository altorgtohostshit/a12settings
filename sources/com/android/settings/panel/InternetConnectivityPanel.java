package com.android.settings.panel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.Looper;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.network.AirplaneModePreferenceController;
import com.android.settings.network.InternetUpdater;
import com.android.settings.network.ProviderModelSliceHelper;
import com.android.settings.network.SubscriptionsChangeListener;
import com.android.settings.network.telephony.DataConnectivityListener;
import com.android.settings.slices.CustomSliceRegistry;
import com.android.settings.slices.CustomSliceable;
import java.util.ArrayList;
import java.util.List;

public class InternetConnectivityPanel implements PanelContent, LifecycleObserver, InternetUpdater.InternetChangeListener, DataConnectivityListener.Client, SubscriptionsChangeListener.SubscriptionsChangeListenerClient {
    private PanelContentCallback mCallback;
    private DataConnectivityListener mConnectivityListener;
    private final Context mContext;
    private int mDefaultDataSubid = -1;
    protected final Runnable mHideProgressBarRunnable = new InternetConnectivityPanel$$ExternalSyntheticLambda0(this);
    InternetUpdater mInternetUpdater;
    protected boolean mIsProgressBarVisible;
    boolean mIsProviderModelEnabled;
    ProviderModelSliceHelper mProviderModelSliceHelper;
    private SubscriptionsChangeListener mSubscriptionsListener;
    private int mSubtitle = -1;
    private final NetworkProviderTelephonyCallback mTelephonyCallback;
    private TelephonyManager mTelephonyManager;
    private final WifiManager mWifiManager;
    private final IntentFilter mWifiStateFilter;
    private final BroadcastReceiver mWifiStateReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (TextUtils.equals(intent.getAction(), "android.net.wifi.SCAN_RESULTS")) {
                    InternetConnectivityPanel.this.showProgressBar();
                    InternetConnectivityPanel.this.updatePanelTitle();
                } else if (TextUtils.equals(intent.getAction(), "android.net.wifi.STATE_CHANGE")) {
                    InternetConnectivityPanel.this.updatePanelTitle();
                }
            }
        }
    };

    public int getMetricsCategory() {
        return 1654;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        setProgressBarVisible(false);
    }

    private InternetConnectivityPanel(Context context) {
        Context applicationContext = context.getApplicationContext();
        this.mContext = applicationContext;
        this.mIsProviderModelEnabled = Utils.isProviderModelEnabled(applicationContext);
        this.mInternetUpdater = new InternetUpdater(context, (Lifecycle) null, this);
        this.mSubscriptionsListener = new SubscriptionsChangeListener(context, this);
        this.mConnectivityListener = new DataConnectivityListener(context, this);
        this.mTelephonyCallback = new NetworkProviderTelephonyCallback();
        this.mDefaultDataSubid = getDefaultDataSubscriptionId();
        this.mTelephonyManager = (TelephonyManager) applicationContext.getSystemService(TelephonyManager.class);
        this.mWifiManager = (WifiManager) applicationContext.getSystemService(WifiManager.class);
        IntentFilter intentFilter = new IntentFilter("android.net.wifi.STATE_CHANGE");
        this.mWifiStateFilter = intentFilter;
        intentFilter.addAction("android.net.wifi.SCAN_RESULTS");
        this.mProviderModelSliceHelper = new ProviderModelSliceHelper(applicationContext, (CustomSliceable) null);
    }

    public static InternetConnectivityPanel create(Context context) {
        return new InternetConnectivityPanel(context);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        if (this.mIsProviderModelEnabled) {
            this.mInternetUpdater.onResume();
            this.mSubscriptionsListener.start();
            this.mConnectivityListener.start();
            this.mTelephonyManager.registerTelephonyCallback(new HandlerExecutor(new Handler(Looper.getMainLooper())), this.mTelephonyCallback);
            this.mContext.registerReceiver(this.mWifiStateReceiver, this.mWifiStateFilter);
            showProgressBar();
            updatePanelTitle();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        if (this.mIsProviderModelEnabled) {
            this.mInternetUpdater.onPause();
            this.mSubscriptionsListener.stop();
            this.mConnectivityListener.stop();
            this.mTelephonyManager.unregisterTelephonyCallback(this.mTelephonyCallback);
            this.mContext.unregisterReceiver(this.mWifiStateReceiver);
            this.mContext.getMainThreadHandler().removeCallbacks(this.mHideProgressBarRunnable);
        }
    }

    public CharSequence getTitle() {
        if (!this.mIsProviderModelEnabled) {
            return this.mContext.getText(R.string.internet_connectivity_panel_title);
        }
        return this.mContext.getText(this.mInternetUpdater.isAirplaneModeOn() ? R.string.airplane_mode : R.string.provider_internet_settings);
    }

    public CharSequence getSubTitle() {
        int i;
        if (!this.mIsProviderModelEnabled || (i = this.mSubtitle) == -1) {
            return null;
        }
        return this.mContext.getText(i);
    }

    public List<Uri> getSlices() {
        ArrayList arrayList = new ArrayList();
        if (this.mIsProviderModelEnabled) {
            arrayList.add(CustomSliceRegistry.PROVIDER_MODEL_SLICE_URI);
            arrayList.add(CustomSliceRegistry.TURN_ON_WIFI_SLICE_URI);
        } else {
            arrayList.add(CustomSliceRegistry.WIFI_SLICE_URI);
            arrayList.add(CustomSliceRegistry.MOBILE_DATA_SLICE_URI);
            arrayList.add(AirplaneModePreferenceController.SLICE_URI);
        }
        return arrayList;
    }

    public Intent getSeeMoreIntent() {
        return new Intent(this.mIsProviderModelEnabled ? "android.settings.NETWORK_PROVIDER_SETTINGS" : "android.settings.WIRELESS_SETTINGS").addFlags(268435456);
    }

    public boolean isCustomizedButtonUsed() {
        return this.mIsProviderModelEnabled;
    }

    public CharSequence getCustomizedButtonTitle() {
        if (!this.mInternetUpdater.isAirplaneModeOn() || this.mInternetUpdater.isWifiEnabled()) {
            return this.mContext.getText(R.string.settings_button);
        }
        return null;
    }

    public void onClickCustomizedButton() {
        this.mContext.startActivity(getSeeMoreIntent());
    }

    public boolean isProgressBarVisible() {
        return this.mIsProgressBarVisible;
    }

    public void registerCallback(PanelContentCallback panelContentCallback) {
        this.mCallback = panelContentCallback;
    }

    public void onAirplaneModeChanged(boolean z) {
        updatePanelTitle();
    }

    public void onWifiEnabledChanged(boolean z) {
        updatePanelTitle();
    }

    public void onSubscriptionsChanged() {
        int defaultDataSubscriptionId = getDefaultDataSubscriptionId();
        log("onSubscriptionsChanged: defaultDataSubId:" + defaultDataSubscriptionId);
        if (this.mDefaultDataSubid != defaultDataSubscriptionId) {
            if (SubscriptionManager.isUsableSubscriptionId(defaultDataSubscriptionId)) {
                this.mTelephonyManager.unregisterTelephonyCallback(this.mTelephonyCallback);
                this.mTelephonyManager.registerTelephonyCallback(new HandlerExecutor(new Handler(Looper.getMainLooper())), this.mTelephonyCallback);
            }
            updatePanelTitle();
        }
    }

    public void onDataConnectivityChange() {
        log("onDataConnectivityChange");
        updatePanelTitle();
    }

    /* access modifiers changed from: package-private */
    public void updatePanelTitle() {
        if (this.mCallback != null) {
            updateSubtitleText();
            log("Subtitle:" + this.mSubtitle);
            if (this.mSubtitle != -1) {
                this.mCallback.onHeaderChanged();
            } else {
                this.mCallback.onTitleChanged();
            }
            this.mCallback.onCustomizedButtonStateChanged();
        }
    }

    /* access modifiers changed from: package-private */
    public int getDefaultDataSubscriptionId() {
        return SubscriptionManager.getDefaultDataSubscriptionId();
    }

    private void updateSubtitleText() {
        this.mSubtitle = -1;
        if (this.mInternetUpdater.isWifiEnabled()) {
            if (this.mInternetUpdater.isAirplaneModeOn()) {
                log("Airplane mode is on + Wi-Fi on.");
                this.mSubtitle = R.string.wifi_is_turned_on_subtitle;
                return;
            }
            List<ScanResult> scanResults = this.mWifiManager.getScanResults();
            if (scanResults != null && scanResults.size() != 0) {
                this.mSubtitle = R.string.select_network_to_connect_internet;
            } else if (this.mIsProgressBarVisible) {
                this.mSubtitle = R.string.wifi_empty_list_wifi_on;
            } else {
                log("No Wi-Fi item.");
                if (!this.mProviderModelSliceHelper.hasCarrier() || (!this.mProviderModelSliceHelper.isVoiceStateInService() && !this.mProviderModelSliceHelper.isDataStateInService())) {
                    log("no carrier or service is out of service.");
                    this.mSubtitle = R.string.all_network_unavailable;
                } else if (!this.mProviderModelSliceHelper.isMobileDataEnabled()) {
                    log("mobile data off");
                    this.mSubtitle = R.string.non_carrier_network_unavailable;
                } else if (!this.mProviderModelSliceHelper.isDataSimActive()) {
                    log("no carrier data.");
                    this.mSubtitle = R.string.all_network_unavailable;
                } else {
                    this.mSubtitle = R.string.non_carrier_network_unavailable;
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void showProgressBar() {
        if (this.mWifiManager == null || !this.mInternetUpdater.isWifiEnabled()) {
            setProgressBarVisible(false);
            return;
        }
        setProgressBarVisible(true);
        List<ScanResult> scanResults = this.mWifiManager.getScanResults();
        if (scanResults != null && scanResults.size() > 0) {
            this.mContext.getMainThreadHandler().postDelayed(this.mHideProgressBarRunnable, 2000);
        }
    }

    /* access modifiers changed from: protected */
    public void setProgressBarVisible(boolean z) {
        if (this.mIsProgressBarVisible != z) {
            this.mIsProgressBarVisible = z;
            PanelContentCallback panelContentCallback = this.mCallback;
            if (panelContentCallback != null) {
                panelContentCallback.onProgressBarVisibleChanged();
                updatePanelTitle();
            }
        }
    }

    private class NetworkProviderTelephonyCallback extends TelephonyCallback implements TelephonyCallback.DataConnectionStateListener, TelephonyCallback.ServiceStateListener {
        private NetworkProviderTelephonyCallback() {
        }

        public void onServiceStateChanged(ServiceState serviceState) {
            InternetConnectivityPanel.log("onServiceStateChanged voiceState=" + serviceState.getState() + " dataState=" + serviceState.getDataRegistrationState());
            InternetConnectivityPanel.this.updatePanelTitle();
        }

        public void onDataConnectionStateChanged(int i, int i2) {
            InternetConnectivityPanel.log("onDataConnectionStateChanged: networkType=" + i2 + " state=" + i);
            InternetConnectivityPanel.this.updatePanelTitle();
        }
    }

    /* access modifiers changed from: private */
    public static void log(String str) {
        Log.d("InternetConnectivityPanel", str);
    }
}
