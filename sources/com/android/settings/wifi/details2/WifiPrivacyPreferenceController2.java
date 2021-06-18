package com.android.settings.wifi.details2;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import androidx.preference.DropDownPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.wifi.WifiDialog2;
import com.android.wifitrackerlib.WifiEntry;

public class WifiPrivacyPreferenceController2 extends BasePreferenceController implements Preference.OnPreferenceChangeListener, WifiDialog2.WifiDialog2Listener {
    private static final String KEY_WIFI_PRIVACY = "privacy";
    private static final int PREF_RANDOMIZATION_NONE = 1;
    private static final int PREF_RANDOMIZATION_PERSISTENT = 0;
    private Preference mPreference;
    private WifiEntry mWifiEntry;
    private WifiManager mWifiManager;

    public static int translateMacRandomizedValueToPrefValue(int i) {
        return i == 1 ? 0 : 1;
    }

    public static int translatePrefValueToMacRandomizedValue(int i) {
        return i == 0 ? 1 : 0;
    }

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ void onForget(WifiDialog2 wifiDialog2) {
        super.onForget(wifiDialog2);
    }

    public /* bridge */ /* synthetic */ void onScan(WifiDialog2 wifiDialog2, String str) {
        super.onScan(wifiDialog2, str);
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public WifiPrivacyPreferenceController2(Context context) {
        super(context, KEY_WIFI_PRIVACY);
        this.mWifiManager = (WifiManager) context.getSystemService("wifi");
    }

    public void setWifiEntry(WifiEntry wifiEntry) {
        this.mWifiEntry = wifiEntry;
    }

    public int getAvailabilityStatus() {
        return this.mWifiManager.isConnectedMacRandomizationSupported() ? 0 : 2;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    public void updateState(Preference preference) {
        DropDownPreference dropDownPreference = (DropDownPreference) preference;
        int randomizationValue = getRandomizationValue();
        boolean canSetPrivacy = this.mWifiEntry.canSetPrivacy();
        preference.setSelectable(canSetPrivacy);
        dropDownPreference.setValue(Integer.toString(randomizationValue));
        updateSummary(dropDownPreference, randomizationValue);
        if (!canSetPrivacy) {
            dropDownPreference.setSummary((int) R.string.wifi_privacy_settings_ephemeral_summary);
        }
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        int parseInt = Integer.parseInt((String) obj);
        this.mWifiEntry.setPrivacy(parseInt);
        if (this.mWifiEntry.getConnectedState() == 2) {
            this.mWifiEntry.disconnect((WifiEntry.DisconnectCallback) null);
            this.mWifiEntry.connect((WifiEntry.ConnectCallback) null);
        }
        updateSummary((DropDownPreference) preference, parseInt);
        return true;
    }

    /* access modifiers changed from: package-private */
    public int getRandomizationValue() {
        return this.mWifiEntry.getPrivacy();
    }

    private void updateSummary(DropDownPreference dropDownPreference, int i) {
        dropDownPreference.setSummary(dropDownPreference.getEntries()[translateMacRandomizedValueToPrefValue(i)]);
    }

    public void onSubmit(WifiDialog2 wifiDialog2) {
        WifiConfiguration config;
        if (wifiDialog2.getController() != null && (config = wifiDialog2.getController().getConfig()) != null && getWifiEntryPrivacy(config) != this.mWifiEntry.getPrivacy()) {
            this.mWifiEntry.setPrivacy(getWifiEntryPrivacy(config));
            onPreferenceChange(this.mPreference, String.valueOf(config.macRandomizationSetting));
        }
    }

    private int getWifiEntryPrivacy(WifiConfiguration wifiConfiguration) {
        int i = wifiConfiguration.macRandomizationSetting;
        if (i != 0) {
            return i != 1 ? 2 : 1;
        }
        return 0;
    }
}
