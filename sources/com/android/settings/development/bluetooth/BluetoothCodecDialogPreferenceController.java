package com.android.settings.development.bluetooth;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothCodecConfig;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import androidx.preference.PreferenceScreen;
import com.android.settings.development.BluetoothA2dpConfigStore;
import com.android.settings.development.bluetooth.AbstractBluetoothPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.ArrayList;
import java.util.List;

public class BluetoothCodecDialogPreferenceController extends AbstractBluetoothDialogPreferenceController {
    private final AbstractBluetoothPreferenceController.Callback mCallback;

    public String getPreferenceKey() {
        return "bluetooth_audio_codec_settings";
    }

    public BluetoothCodecDialogPreferenceController(Context context, Lifecycle lifecycle, BluetoothA2dpConfigStore bluetoothA2dpConfigStore, AbstractBluetoothPreferenceController.Callback callback) {
        super(context, lifecycle, bluetoothA2dpConfigStore);
        this.mCallback = callback;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        ((BaseBluetoothDialogPreference) this.mPreference).setCallback(this);
    }

    public List<Integer> getSelectableIndex() {
        BluetoothCodecConfig[] selectableConfigs;
        ArrayList arrayList = new ArrayList();
        BluetoothA2dp bluetoothA2dp = this.mBluetoothA2dp;
        arrayList.add(Integer.valueOf(getDefaultIndex()));
        if (bluetoothA2dp == null) {
            return arrayList;
        }
        BluetoothDevice activeDevice = bluetoothA2dp.getActiveDevice();
        if (activeDevice == null) {
            Log.d("BtCodecCtr", "Unable to get selectable index. No Active Bluetooth device");
            return arrayList;
        } else if (bluetoothA2dp.isOptionalCodecsEnabled(activeDevice) == 1 && (selectableConfigs = getSelectableConfigs(activeDevice)) != null) {
            return getIndexFromConfig(selectableConfigs);
        } else {
            arrayList.add(Integer.valueOf(convertCfgToBtnIndex(0)));
            return arrayList;
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x003e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void writeConfigurationValues(int r7) {
        /*
            r6 = this;
            r0 = 4
            r1 = 3
            r2 = 2
            r3 = 1
            r4 = 0
            r5 = 1000000(0xf4240, float:1.401298E-39)
            if (r7 == 0) goto L_0x001f
            if (r7 == r3) goto L_0x001d
            if (r7 == r2) goto L_0x001b
            if (r7 == r1) goto L_0x0019
            if (r7 == r0) goto L_0x0017
            r1 = 5
            if (r7 == r1) goto L_0x002d
            r0 = r4
            goto L_0x002e
        L_0x0017:
            r0 = r1
            goto L_0x002d
        L_0x0019:
            r0 = r2
            goto L_0x002d
        L_0x001b:
            r0 = r3
            goto L_0x002d
        L_0x001d:
            r0 = r4
            goto L_0x002d
        L_0x001f:
            android.bluetooth.BluetoothA2dp r7 = r6.mBluetoothA2dp
            android.bluetooth.BluetoothDevice r7 = r7.getActiveDevice()
            android.bluetooth.BluetoothCodecConfig[] r7 = r6.getSelectableConfigs(r7)
            int r0 = com.android.settings.development.bluetooth.AbstractBluetoothDialogPreferenceController.getHighestCodec(r7)
        L_0x002d:
            r4 = r5
        L_0x002e:
            com.android.settings.development.BluetoothA2dpConfigStore r7 = r6.mBluetoothA2dpConfigStore
            r7.setCodecType(r0)
            com.android.settings.development.BluetoothA2dpConfigStore r7 = r6.mBluetoothA2dpConfigStore
            r7.setCodecPriority(r4)
            android.bluetooth.BluetoothCodecConfig r7 = r6.getSelectableByCodecType(r0)
            if (r7 != 0) goto L_0x0045
            java.lang.String r0 = "BtCodecCtr"
            java.lang.String r1 = "Selectable config is null. Unable to reset"
            android.util.Log.d(r0, r1)
        L_0x0045:
            com.android.settings.development.BluetoothA2dpConfigStore r0 = r6.mBluetoothA2dpConfigStore
            int r1 = com.android.settings.development.bluetooth.AbstractBluetoothDialogPreferenceController.getHighestSampleRate(r7)
            r0.setSampleRate(r1)
            com.android.settings.development.BluetoothA2dpConfigStore r0 = r6.mBluetoothA2dpConfigStore
            int r1 = com.android.settings.development.bluetooth.AbstractBluetoothDialogPreferenceController.getHighestBitsPerSample(r7)
            r0.setBitsPerSample(r1)
            com.android.settings.development.BluetoothA2dpConfigStore r6 = r6.mBluetoothA2dpConfigStore
            int r7 = com.android.settings.development.bluetooth.AbstractBluetoothDialogPreferenceController.getHighestChannelMode(r7)
            r6.setChannelMode(r7)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.development.bluetooth.BluetoothCodecDialogPreferenceController.writeConfigurationValues(int):void");
    }

    /* access modifiers changed from: protected */
    public int getCurrentIndexByConfig(BluetoothCodecConfig bluetoothCodecConfig) {
        if (bluetoothCodecConfig == null) {
            Log.e("BtCodecCtr", "Unable to get current config index. Config is null.");
        }
        return convertCfgToBtnIndex(bluetoothCodecConfig.getCodecType());
    }

    public void onIndexUpdated(int i) {
        super.onIndexUpdated(i);
        this.mCallback.onBluetoothCodecChanged();
    }

    private List<Integer> getIndexFromConfig(BluetoothCodecConfig[] bluetoothCodecConfigArr) {
        ArrayList arrayList = new ArrayList();
        for (BluetoothCodecConfig codecType : bluetoothCodecConfigArr) {
            arrayList.add(Integer.valueOf(convertCfgToBtnIndex(codecType.getCodecType())));
        }
        return arrayList;
    }

    /* access modifiers changed from: package-private */
    public int convertCfgToBtnIndex(int i) {
        int defaultIndex = getDefaultIndex();
        if (i == 0) {
            return 1;
        }
        if (i == 1) {
            return 2;
        }
        if (i == 2) {
            return 3;
        }
        if (i == 3) {
            return 4;
        }
        if (i == 4) {
            return 5;
        }
        Log.e("BtCodecCtr", "Unsupported config:" + i);
        return defaultIndex;
    }
}
