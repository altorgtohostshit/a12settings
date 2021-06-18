package com.android.settings.wifi.dpp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkScoreManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.SimpleClock;
import android.os.SystemClock;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.wifi.AddNetworkFragment;
import com.android.settingslib.wifi.WifiEntryPreference;
import com.android.wifitrackerlib.SavedNetworkTracker;
import com.android.wifitrackerlib.WifiEntry;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

public class WifiNetworkListFragment extends SettingsPreferenceFragment implements SavedNetworkTracker.SavedNetworkTrackerCallback, Preference.OnPreferenceClickListener {
    static final int ADD_NETWORK_REQUEST = 1;
    static final String WIFI_CONFIG_KEY = "wifi_config_key";
    Preference mAddPreference;
    OnChooseNetworkListener mOnChooseNetworkListener;
    PreferenceCategory mPreferenceGroup;
    private WifiManager.ActionListener mSaveListener;
    SavedNetworkTracker mSavedNetworkTracker;
    WifiManager mWifiManager;
    HandlerThread mWorkerThread;

    public interface OnChooseNetworkListener {
        void onChooseNetwork(WifiNetworkConfig wifiNetworkConfig);
    }

    public int getMetricsCategory() {
        return 1595;
    }

    public void onSubscriptionWifiEntriesChanged() {
    }

    public void onWifiStateChanged() {
    }

    private static class DisableUnreachableWifiEntryPreference extends WifiEntryPreference {
        DisableUnreachableWifiEntryPreference(Context context, WifiEntry wifiEntry) {
            super(context, wifiEntry);
        }

        public void onUpdated() {
            super.onUpdated();
            setEnabled(getWifiEntry().getLevel() != -1);
        }
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnChooseNetworkListener) {
            this.mOnChooseNetworkListener = (OnChooseNetworkListener) context;
            return;
        }
        throw new IllegalArgumentException("Invalid context type");
    }

    public void onDetach() {
        this.mOnChooseNetworkListener = null;
        super.onDetach();
    }

    /* JADX WARNING: type inference failed for: r8v0, types: [com.android.settings.wifi.dpp.WifiNetworkListFragment$2, java.time.Clock] */
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        Context context = getContext();
        this.mWifiManager = (WifiManager) context.getSystemService(WifiManager.class);
        this.mSaveListener = new WifiManager.ActionListener() {
            public void onSuccess() {
            }

            public void onFailure(int i) {
                FragmentActivity activity = WifiNetworkListFragment.this.getActivity();
                if (activity != null && !activity.isFinishing()) {
                    Toast.makeText(activity, R.string.wifi_failed_save_message, 0).show();
                }
            }
        };
        HandlerThread handlerThread = new HandlerThread("WifiNetworkListFragment{" + Integer.toHexString(System.identityHashCode(this)) + "}", 10);
        this.mWorkerThread = handlerThread;
        handlerThread.start();
        this.mSavedNetworkTracker = new SavedNetworkTracker(getSettingsLifecycle(), context, (WifiManager) context.getSystemService(WifiManager.class), (ConnectivityManager) context.getSystemService(ConnectivityManager.class), (NetworkScoreManager) context.getSystemService(NetworkScoreManager.class), new Handler(Looper.getMainLooper()), this.mWorkerThread.getThreadHandler(), new SimpleClock(ZoneOffset.UTC) {
            public long millis() {
                return SystemClock.elapsedRealtime();
            }
        }, 15000, 10000, this);
    }

    public void onDestroyView() {
        this.mWorkerThread.quit();
        super.onDestroyView();
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        WifiConfiguration wifiConfiguration;
        super.onActivityResult(i, i2, intent);
        if (i == 1 && i2 == -1 && (wifiConfiguration = (WifiConfiguration) intent.getParcelableExtra(WIFI_CONFIG_KEY)) != null) {
            this.mWifiManager.save(wifiConfiguration, this.mSaveListener);
        }
    }

    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.wifi_dpp_network_list);
        this.mPreferenceGroup = (PreferenceCategory) findPreference("access_points");
        Preference preference = new Preference(getPrefContext());
        this.mAddPreference = preference;
        preference.setIcon((int) R.drawable.ic_add_24dp);
        this.mAddPreference.setTitle((int) R.string.wifi_add_network);
        this.mAddPreference.setOnPreferenceClickListener(this);
    }

    public void onSavedWifiEntriesChanged() {
        this.mPreferenceGroup.removeAll();
        int i = 0;
        for (WifiEntry wifiEntry : (List) this.mSavedNetworkTracker.getSavedWifiEntries().stream().filter(new WifiNetworkListFragment$$ExternalSyntheticLambda0(this)).collect(Collectors.toList())) {
            DisableUnreachableWifiEntryPreference disableUnreachableWifiEntryPreference = new DisableUnreachableWifiEntryPreference(getContext(), wifiEntry);
            disableUnreachableWifiEntryPreference.setOnPreferenceClickListener(this);
            disableUnreachableWifiEntryPreference.setEnabled(wifiEntry.getLevel() != -1);
            disableUnreachableWifiEntryPreference.setOrder(i);
            this.mPreferenceGroup.addPreference(disableUnreachableWifiEntryPreference);
            i++;
        }
        this.mAddPreference.setOrder(i);
        this.mPreferenceGroup.addPreference(this.mAddPreference);
    }

    public boolean onPreferenceClick(Preference preference) {
        if (preference instanceof WifiEntryPreference) {
            WifiEntry wifiEntry = ((WifiEntryPreference) preference).getWifiEntry();
            WifiConfiguration wifiConfiguration = wifiEntry.getWifiConfiguration();
            if (wifiConfiguration != null) {
                WifiNetworkConfig validConfigOrNull = WifiNetworkConfig.getValidConfigOrNull(WifiDppUtils.getSecurityString(wifiEntry), wifiConfiguration.getPrintableSsid(), wifiConfiguration.preSharedKey, wifiConfiguration.hiddenSSID, wifiConfiguration.networkId, false);
                OnChooseNetworkListener onChooseNetworkListener = this.mOnChooseNetworkListener;
                if (onChooseNetworkListener != null) {
                    onChooseNetworkListener.onChooseNetwork(validConfigOrNull);
                }
            } else {
                throw new IllegalArgumentException("Invalid access point");
            }
        } else if (preference != this.mAddPreference) {
            return super.onPreferenceTreeClick(preference);
        } else {
            new SubSettingLauncher(getContext()).setTitleRes(R.string.wifi_add_network).setDestination(AddNetworkFragment.class.getName()).setSourceMetricsCategory(getMetricsCategory()).setResultListener(this, 1).launch();
        }
        return true;
    }

    /* access modifiers changed from: private */
    /* renamed from: isValidForDppConfiguration */
    public boolean lambda$onSavedWifiEntriesChanged$0(WifiEntry wifiEntry) {
        int security = wifiEntry.getSecurity();
        return security == 2 || security == 5;
    }
}
