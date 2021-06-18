package com.android.settings.wifi.savedaccesspoints2;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkScoreManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.SimpleClock;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import com.android.settings.R;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.wifi.details2.WifiNetworkDetailsFragment2;
import com.android.wifitrackerlib.SavedNetworkTracker;
import java.time.ZoneOffset;

public class SavedAccessPointsWifiSettings2 extends DashboardFragment implements SavedNetworkTracker.SavedNetworkTrackerCallback {
    static final String TAG = "SavedAccessPoints2";
    SavedNetworkTracker mSavedNetworkTracker;
    HandlerThread mWorkerThread;

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return TAG;
    }

    public int getMetricsCategory() {
        return 106;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.wifi_display_saved_access_points2;
    }

    public void onWifiStateChanged() {
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        ((SavedAccessPointsPreferenceController2) use(SavedAccessPointsPreferenceController2.class)).setHost(this);
        ((SubscribedAccessPointsPreferenceController2) use(SubscribedAccessPointsPreferenceController2.class)).setHost(this);
    }

    /* JADX WARNING: type inference failed for: r8v0, types: [java.time.Clock, com.android.settings.wifi.savedaccesspoints2.SavedAccessPointsWifiSettings2$1] */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Context context = getContext();
        HandlerThread handlerThread = new HandlerThread("SavedAccessPoints2{" + Integer.toHexString(System.identityHashCode(this)) + "}", 10);
        this.mWorkerThread = handlerThread;
        handlerThread.start();
        this.mSavedNetworkTracker = new SavedNetworkTracker(getSettingsLifecycle(), context, (WifiManager) context.getSystemService(WifiManager.class), (ConnectivityManager) context.getSystemService(ConnectivityManager.class), (NetworkScoreManager) context.getSystemService(NetworkScoreManager.class), new Handler(Looper.getMainLooper()), this.mWorkerThread.getThreadHandler(), new SimpleClock(ZoneOffset.UTC) {
            public long millis() {
                return SystemClock.elapsedRealtime();
            }
        }, 15000, 10000, this);
    }

    public void onStart() {
        super.onStart();
        onSavedWifiEntriesChanged();
        onSubscriptionWifiEntriesChanged();
    }

    public void onDestroy() {
        this.mWorkerThread.quit();
        super.onDestroy();
    }

    public void showWifiPage(String str, CharSequence charSequence) {
        removeDialog(1);
        if (TextUtils.isEmpty(str)) {
            Log.e(TAG, "Not able to show WifiEntry of an empty key");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString("key_chosen_wifientry_key", str);
        new SubSettingLauncher(getContext()).setTitleText(charSequence).setDestination(WifiNetworkDetailsFragment2.class.getName()).setArguments(bundle).setSourceMetricsCategory(getMetricsCategory()).launch();
    }

    public void onSavedWifiEntriesChanged() {
        if (!isFinishingOrDestroyed()) {
            ((SavedAccessPointsPreferenceController2) use(SavedAccessPointsPreferenceController2.class)).displayPreference(getPreferenceScreen(), this.mSavedNetworkTracker.getSavedWifiEntries());
        }
    }

    public void onSubscriptionWifiEntriesChanged() {
        if (!isFinishingOrDestroyed()) {
            ((SubscribedAccessPointsPreferenceController2) use(SubscribedAccessPointsPreferenceController2.class)).displayPreference(getPreferenceScreen(), this.mSavedNetworkTracker.getSubscriptionWifiEntries());
        }
    }
}
