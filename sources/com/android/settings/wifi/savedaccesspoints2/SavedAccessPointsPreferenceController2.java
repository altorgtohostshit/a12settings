package com.android.settings.wifi.savedaccesspoints2;

import android.content.Context;
import android.content.IntentFilter;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.wifi.WifiEntryPreference;
import com.android.wifitrackerlib.WifiEntry;
import java.util.ArrayList;
import java.util.List;

public class SavedAccessPointsPreferenceController2 extends BasePreferenceController implements Preference.OnPreferenceClickListener {
    private SavedAccessPointsWifiSettings2 mHost;
    private PreferenceGroup mPreferenceGroup;
    List<WifiEntry> mWifiEntries = new ArrayList();

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

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public SavedAccessPointsPreferenceController2(Context context, String str) {
        super(context, str);
    }

    public SavedAccessPointsPreferenceController2 setHost(SavedAccessPointsWifiSettings2 savedAccessPointsWifiSettings2) {
        this.mHost = savedAccessPointsWifiSettings2;
        return this;
    }

    public int getAvailabilityStatus() {
        return this.mWifiEntries.size() > 0 ? 0 : 2;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        this.mPreferenceGroup = (PreferenceGroup) preferenceScreen.findPreference(getPreferenceKey());
        updatePreference();
        super.displayPreference(preferenceScreen);
    }

    /* access modifiers changed from: package-private */
    public void displayPreference(PreferenceScreen preferenceScreen, List<WifiEntry> list) {
        if (list == null || list.isEmpty()) {
            this.mWifiEntries.clear();
        } else {
            this.mWifiEntries = list;
        }
        displayPreference(preferenceScreen);
    }

    public boolean onPreferenceClick(Preference preference) {
        SavedAccessPointsWifiSettings2 savedAccessPointsWifiSettings2 = this.mHost;
        if (savedAccessPointsWifiSettings2 == null) {
            return false;
        }
        savedAccessPointsWifiSettings2.showWifiPage(preference.getKey(), preference.getTitle());
        return false;
    }

    private void updatePreference() {
        ArrayList<String> arrayList = new ArrayList<>();
        int preferenceCount = this.mPreferenceGroup.getPreferenceCount();
        for (int i = 0; i < preferenceCount; i++) {
            String key = this.mPreferenceGroup.getPreference(i).getKey();
            if (this.mWifiEntries.stream().filter(new SavedAccessPointsPreferenceController2$$ExternalSyntheticLambda0(key)).count() == 0) {
                arrayList.add(key);
            }
        }
        for (String findPreference : arrayList) {
            PreferenceGroup preferenceGroup = this.mPreferenceGroup;
            preferenceGroup.removePreference(preferenceGroup.findPreference(findPreference));
        }
        for (WifiEntry next : this.mWifiEntries) {
            if (this.mPreferenceGroup.findPreference(next.getKey()) == null) {
                WifiEntryPreference wifiEntryPreference = new WifiEntryPreference(this.mContext, next);
                wifiEntryPreference.setKey(next.getKey());
                wifiEntryPreference.setOnPreferenceClickListener(this);
                this.mPreferenceGroup.addPreference(wifiEntryPreference);
            }
        }
    }
}
