package com.android.settings.wifi;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.SimpleClock;
import android.os.SystemClock;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.wifi.details2.WifiNetworkDetailsFragment2;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.wifi.WifiEntryPreference;
import com.android.wifitrackerlib.WifiEntry;
import com.android.wifitrackerlib.WifiPickerTracker;
import java.time.ZoneOffset;

public class WifiConnectionPreferenceController extends AbstractPreferenceController implements WifiPickerTracker.WifiPickerTrackerCallback, LifecycleObserver {
    private int mMetricsCategory;
    private Context mPrefContext;
    private WifiEntryPreference mPreference;
    private PreferenceGroup mPreferenceGroup;
    private String mPreferenceGroupKey;
    private UpdateListener mUpdateListener;
    public WifiPickerTracker mWifiPickerTracker;
    private HandlerThread mWorkerThread;
    private int order;

    public interface UpdateListener {
        void onChildrenUpdated();
    }

    public String getPreferenceKey() {
        return "active_wifi_connection";
    }

    public void onNumSavedNetworksChanged() {
    }

    public void onNumSavedSubscriptionsChanged() {
    }

    /* JADX WARNING: type inference failed for: r5v0, types: [com.android.settings.wifi.WifiConnectionPreferenceController$1, java.time.Clock] */
    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public WifiConnectionPreferenceController(Context context, Lifecycle lifecycle, UpdateListener updateListener, String str, int i, int i2) {
        super(context);
        lifecycle.addObserver(this);
        this.mUpdateListener = updateListener;
        this.mPreferenceGroupKey = str;
        this.order = i;
        this.mMetricsCategory = i2;
        HandlerThread handlerThread = new HandlerThread("WifiConnPrefCtrl{" + Integer.toHexString(System.identityHashCode(this)) + "}", 10);
        this.mWorkerThread = handlerThread;
        handlerThread.start();
        ? r5 = new SimpleClock(ZoneOffset.UTC) {
            public long millis() {
                return SystemClock.elapsedRealtime();
            }
        };
        this.mWifiPickerTracker = FeatureFactory.getFactory(context).getWifiTrackerLibProvider().createWifiPickerTracker(lifecycle, context, new Handler(Looper.getMainLooper()), this.mWorkerThread.getThreadHandler(), r5, 15000, 10000, this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        this.mWorkerThread.quit();
    }

    public boolean isAvailable() {
        return this.mWifiPickerTracker.getConnectedWifiEntry() != null;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreferenceGroup = (PreferenceGroup) preferenceScreen.findPreference(this.mPreferenceGroupKey);
        this.mPrefContext = preferenceScreen.getContext();
        update();
    }

    private void updatePreference(WifiEntry wifiEntry) {
        WifiEntryPreference wifiEntryPreference = this.mPreference;
        if (wifiEntryPreference != null) {
            this.mPreferenceGroup.removePreference(wifiEntryPreference);
            this.mPreference = null;
        }
        if (wifiEntry != null && this.mPrefContext != null) {
            WifiEntryPreference wifiEntryPreference2 = new WifiEntryPreference(this.mPrefContext, wifiEntry);
            this.mPreference = wifiEntryPreference2;
            wifiEntryPreference2.setKey("active_wifi_connection");
            this.mPreference.refresh();
            this.mPreference.setOrder(this.order);
            this.mPreference.setOnPreferenceClickListener(new WifiConnectionPreferenceController$$ExternalSyntheticLambda0(this, wifiEntry));
            this.mPreferenceGroup.addPreference(this.mPreference);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$updatePreference$0(WifiEntry wifiEntry, Preference preference) {
        Bundle bundle = new Bundle();
        bundle.putString("key_chosen_wifientry_key", wifiEntry.getKey());
        new SubSettingLauncher(this.mPrefContext).setTitleRes(R.string.pref_title_network_details).setDestination(WifiNetworkDetailsFragment2.class.getName()).setArguments(bundle).setSourceMetricsCategory(this.mMetricsCategory).launch();
        return true;
    }

    private void update() {
        WifiEntry connectedWifiEntry = this.mWifiPickerTracker.getConnectedWifiEntry();
        if (connectedWifiEntry == null) {
            updatePreference((WifiEntry) null);
        } else {
            WifiEntryPreference wifiEntryPreference = this.mPreference;
            if (wifiEntryPreference == null || !wifiEntryPreference.getWifiEntry().equals(connectedWifiEntry)) {
                updatePreference(connectedWifiEntry);
            } else {
                WifiEntryPreference wifiEntryPreference2 = this.mPreference;
                if (wifiEntryPreference2 != null) {
                    wifiEntryPreference2.refresh();
                }
            }
        }
        this.mUpdateListener.onChildrenUpdated();
    }

    public void onWifiStateChanged() {
        update();
    }

    public void onWifiEntriesChanged() {
        update();
    }
}
