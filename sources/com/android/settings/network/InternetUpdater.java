package com.android.settings.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.wifi.WifiManager;
import android.util.Log;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import com.android.settings.AirplaneModeEnabler;
import java.util.HashMap;
import java.util.Map;

public class InternetUpdater implements AirplaneModeEnabler.OnAirplaneModeChangedListener, LifecycleObserver {
    private static Map<Integer, Integer> sTransportMap;
    AirplaneModeEnabler mAirplaneModeEnabler;
    private final ConnectivityManager mConnectivityManager;
    private final Context mContext;
    boolean mInternetAvailable;
    private int mInternetType;
    /* access modifiers changed from: private */
    public InternetChangeListener mListener;
    private ConnectivityManager.NetworkCallback mNetworkCallback = new ConnectivityManager.NetworkCallback() {
        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            InternetUpdater.this.updateInternetAvailable(networkCapabilities);
        }

        public void onLost(Network network) {
            InternetUpdater internetUpdater = InternetUpdater.this;
            internetUpdater.mInternetAvailable = false;
            internetUpdater.updateInternetType();
        }
    };
    int mTransport;
    /* access modifiers changed from: private */
    public final WifiManager mWifiManager;
    private final IntentFilter mWifiStateFilter;
    private final BroadcastReceiver mWifiStateReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            InternetUpdater.this.fetchActiveNetwork();
            if (InternetUpdater.this.mListener != null) {
                InternetUpdater.this.mListener.onWifiEnabledChanged(InternetUpdater.this.mWifiManager.isWifiEnabled());
            }
        }
    };

    public interface InternetChangeListener {
        void onAirplaneModeChanged(boolean z) {
        }

        void onInternetTypeChanged(int i) {
        }

        void onWifiEnabledChanged(boolean z) {
        }
    }

    static {
        HashMap hashMap = new HashMap();
        sTransportMap = hashMap;
        hashMap.put(1, 2);
        sTransportMap.put(0, 3);
        sTransportMap.put(3, 4);
    }

    public InternetUpdater(Context context, Lifecycle lifecycle, InternetChangeListener internetChangeListener) {
        this.mContext = context;
        this.mAirplaneModeEnabler = new AirplaneModeEnabler(context, this);
        this.mConnectivityManager = (ConnectivityManager) context.getSystemService(ConnectivityManager.class);
        this.mWifiManager = (WifiManager) context.getSystemService(WifiManager.class);
        this.mWifiStateFilter = new IntentFilter("android.net.wifi.WIFI_STATE_CHANGED");
        this.mListener = internetChangeListener;
        fetchActiveNetwork();
        if (lifecycle != null) {
            lifecycle.addObserver(this);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        this.mAirplaneModeEnabler.start();
        this.mConnectivityManager.registerDefaultNetworkCallback(this.mNetworkCallback);
        this.mContext.registerReceiver(this.mWifiStateReceiver, this.mWifiStateFilter);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        this.mAirplaneModeEnabler.stop();
        this.mConnectivityManager.unregisterNetworkCallback(this.mNetworkCallback);
        this.mContext.unregisterReceiver(this.mWifiStateReceiver);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        this.mAirplaneModeEnabler.close();
    }

    public void onAirplaneModeChanged(boolean z) {
        fetchActiveNetwork();
        InternetChangeListener internetChangeListener = this.mListener;
        if (internetChangeListener != null) {
            internetChangeListener.onAirplaneModeChanged(z);
        }
    }

    /* access modifiers changed from: private */
    public void fetchActiveNetwork() {
        Network activeNetwork = this.mConnectivityManager.getActiveNetwork();
        if (activeNetwork == null) {
            this.mInternetAvailable = false;
            updateInternetType();
            return;
        }
        NetworkCapabilities networkCapabilities = this.mConnectivityManager.getNetworkCapabilities(activeNetwork);
        if (networkCapabilities == null) {
            this.mInternetAvailable = false;
            updateInternetType();
            return;
        }
        updateInternetAvailable(networkCapabilities);
    }

    /* access modifiers changed from: package-private */
    public void updateInternetAvailable(NetworkCapabilities networkCapabilities) {
        boolean z = false;
        if (networkCapabilities.hasCapability(12) && networkCapabilities.hasCapability(16)) {
            int[] transportTypes = networkCapabilities.getTransportTypes();
            int length = transportTypes.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                int i2 = transportTypes[i];
                if (sTransportMap.containsKey(Integer.valueOf(i2))) {
                    this.mTransport = i2;
                    Log.i("InternetUpdater", "Detect an internet available network with transport type: " + this.mTransport);
                    z = true;
                    break;
                }
                i++;
            }
        }
        this.mInternetAvailable = z;
        updateInternetType();
    }

    /* access modifiers changed from: package-private */
    public void updateInternetType() {
        int i;
        if (this.mInternetAvailable) {
            i = sTransportMap.get(Integer.valueOf(this.mTransport)).intValue();
        } else {
            i = (!this.mAirplaneModeEnabler.isAirplaneModeOn() || this.mWifiManager.getWifiState() == 3) ? 1 : 0;
        }
        this.mInternetType = i;
        InternetChangeListener internetChangeListener = this.mListener;
        if (internetChangeListener != null) {
            internetChangeListener.onInternetTypeChanged(i);
        }
    }

    public int getInternetType() {
        return this.mInternetType;
    }

    public boolean isAirplaneModeOn() {
        return this.mAirplaneModeEnabler.isAirplaneModeOn();
    }

    public boolean isWifiEnabled() {
        return this.mWifiManager.isWifiEnabled();
    }
}
