package com.android.settings.development;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothCodecConfig;
import android.bluetooth.BluetoothCodecStatus;
import android.bluetooth.BluetoothDevice;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnDestroy;
import com.android.settingslib.development.DeveloperOptionsPreferenceController;

public abstract class AbstractBluetoothA2dpPreferenceController extends DeveloperOptionsPreferenceController implements Preference.OnPreferenceChangeListener, PreferenceControllerMixin, BluetoothServiceConnectionListener, LifecycleObserver, OnDestroy {
    static final int STREAMING_LABEL_ID = 2130969786;
    protected BluetoothA2dp mBluetoothA2dp;
    protected final BluetoothA2dpConfigStore mBluetoothA2dpConfigStore;
    private final String[] mListSummaries;
    private final String[] mListValues;
    protected ListPreference mPreference;

    /* access modifiers changed from: protected */
    public abstract int getCurrentA2dpSettingIndex(BluetoothCodecConfig bluetoothCodecConfig);

    /* access modifiers changed from: protected */
    public abstract int getDefaultIndex();

    public void onBluetoothCodecUpdated() {
    }

    /* access modifiers changed from: protected */
    public abstract void writeConfigurationValues(Object obj);

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        ListPreference listPreference = (ListPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = listPreference;
        listPreference.setValue(this.mListValues[getDefaultIndex()]);
        this.mPreference.setSummary(this.mListSummaries[getDefaultIndex()]);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0022, code lost:
        r6 = r5.mPreference.findIndexOfValue(r7.toString());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0031, code lost:
        if (r6 != getDefaultIndex()) goto L_0x003d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0033, code lost:
        r5.mPreference.setSummary(r5.mListSummaries[r6]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x003d, code lost:
        r5.mPreference.setSummary(r5.mContext.getResources().getString(com.android.settings.R.string.bluetooth_select_a2dp_codec_streaming_label, new java.lang.Object[]{r5.mListSummaries[r6]}));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0057, code lost:
        return true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onPreferenceChange(androidx.preference.Preference r6, java.lang.Object r7) {
        /*
            r5 = this;
            android.bluetooth.BluetoothA2dp r6 = r5.mBluetoothA2dp
            r0 = 0
            if (r6 != 0) goto L_0x0006
            return r0
        L_0x0006:
            r5.writeConfigurationValues(r7)
            com.android.settings.development.BluetoothA2dpConfigStore r6 = r5.mBluetoothA2dpConfigStore
            android.bluetooth.BluetoothCodecConfig r6 = r6.createCodecConfig()
            com.android.settings.development.BluetoothA2dpConfigStore r1 = r5.mBluetoothA2dpConfigStore
            monitor-enter(r1)
            android.bluetooth.BluetoothA2dp r2 = r5.mBluetoothA2dp     // Catch:{ all -> 0x0058 }
            if (r2 == 0) goto L_0x0021
            android.bluetooth.BluetoothDevice r2 = r2.getActiveDevice()     // Catch:{ all -> 0x0058 }
            if (r2 != 0) goto L_0x001e
            monitor-exit(r1)     // Catch:{ all -> 0x0058 }
            return r0
        L_0x001e:
            r5.setCodecConfigPreference(r2, r6)     // Catch:{ all -> 0x0058 }
        L_0x0021:
            monitor-exit(r1)     // Catch:{ all -> 0x0058 }
            androidx.preference.ListPreference r6 = r5.mPreference
            java.lang.String r7 = r7.toString()
            int r6 = r6.findIndexOfValue(r7)
            int r7 = r5.getDefaultIndex()
            r1 = 1
            if (r6 != r7) goto L_0x003d
            androidx.preference.ListPreference r7 = r5.mPreference
            java.lang.String[] r5 = r5.mListSummaries
            r5 = r5[r6]
            r7.setSummary(r5)
            goto L_0x0057
        L_0x003d:
            androidx.preference.ListPreference r7 = r5.mPreference
            android.content.Context r2 = r5.mContext
            android.content.res.Resources r2 = r2.getResources()
            r3 = 2130969786(0x7f0404ba, float:1.7548264E38)
            java.lang.Object[] r4 = new java.lang.Object[r1]
            java.lang.String[] r5 = r5.mListSummaries
            r5 = r5[r6]
            r4[r0] = r5
            java.lang.String r5 = r2.getString(r3, r4)
            r7.setSummary(r5)
        L_0x0057:
            return r1
        L_0x0058:
            r5 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x0058 }
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.development.AbstractBluetoothA2dpPreferenceController.onPreferenceChange(androidx.preference.Preference, java.lang.Object):boolean");
    }

    public void updateState(Preference preference) {
        BluetoothCodecConfig codecConfig;
        BluetoothDevice activeDevice = this.mBluetoothA2dp.getActiveDevice();
        if (activeDevice != null && getCodecConfig(activeDevice) != null && this.mPreference != null) {
            synchronized (this.mBluetoothA2dpConfigStore) {
                codecConfig = getCodecConfig(activeDevice);
            }
            int currentA2dpSettingIndex = getCurrentA2dpSettingIndex(codecConfig);
            this.mPreference.setValue(this.mListValues[currentA2dpSettingIndex]);
            if (currentA2dpSettingIndex == getDefaultIndex()) {
                this.mPreference.setSummary(this.mListSummaries[currentA2dpSettingIndex]);
            } else {
                this.mPreference.setSummary(this.mContext.getResources().getString(R.string.bluetooth_select_a2dp_codec_streaming_label, new Object[]{this.mListSummaries[currentA2dpSettingIndex]}));
            }
            writeConfigurationValues(this.mListValues[currentA2dpSettingIndex]);
        }
    }

    public void onBluetoothServiceConnected(BluetoothA2dp bluetoothA2dp) {
        this.mBluetoothA2dp = bluetoothA2dp;
        updateState(this.mPreference);
    }

    public void onBluetoothServiceDisconnected() {
        this.mBluetoothA2dp = null;
    }

    public void onDestroy() {
        this.mBluetoothA2dp = null;
    }

    /* access modifiers changed from: package-private */
    public void setCodecConfigPreference(BluetoothDevice bluetoothDevice, BluetoothCodecConfig bluetoothCodecConfig) {
        if (bluetoothDevice == null) {
            bluetoothDevice = this.mBluetoothA2dp.getActiveDevice();
        }
        if (bluetoothDevice != null) {
            this.mBluetoothA2dp.setCodecConfigPreference(bluetoothDevice, bluetoothCodecConfig);
        }
    }

    /* access modifiers changed from: package-private */
    public BluetoothCodecConfig getCodecConfig(BluetoothDevice bluetoothDevice) {
        BluetoothCodecStatus codecStatus;
        BluetoothA2dp bluetoothA2dp = this.mBluetoothA2dp;
        if (bluetoothA2dp != null) {
            if (bluetoothDevice == null) {
                bluetoothDevice = bluetoothA2dp.getActiveDevice();
            }
            if (!(bluetoothDevice == null || (codecStatus = this.mBluetoothA2dp.getCodecStatus(bluetoothDevice)) == null)) {
                return codecStatus.getCodecConfig();
            }
        }
        return null;
    }
}
