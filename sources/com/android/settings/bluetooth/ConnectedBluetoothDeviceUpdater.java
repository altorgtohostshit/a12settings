package com.android.settings.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.media.AudioManager;
import android.util.Log;
import androidx.preference.Preference;
import com.android.settings.connecteddevice.DevicePreferenceCallback;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.widget.GearPreference;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;

public class ConnectedBluetoothDeviceUpdater extends BluetoothDeviceUpdater {
    private static final boolean DBG = Log.isLoggable("ConnBluetoothDeviceUpdater", 3);
    private final AudioManager mAudioManager;

    /* access modifiers changed from: protected */
    public String getPreferenceKey() {
        return "connected_bt";
    }

    public ConnectedBluetoothDeviceUpdater(Context context, DashboardFragment dashboardFragment, DevicePreferenceCallback devicePreferenceCallback) {
        super(context, dashboardFragment, devicePreferenceCallback);
        this.mAudioManager = (AudioManager) context.getSystemService("audio");
    }

    public void onAudioModeChanged() {
        forceUpdate();
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x004d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isFilterMatched(com.android.settingslib.bluetooth.CachedBluetoothDevice r8) {
        /*
            r7 = this;
            android.media.AudioManager r0 = r7.mAudioManager
            int r0 = r0.getMode()
            r1 = 2
            r2 = 1
            if (r0 == r2) goto L_0x0012
            if (r0 == r1) goto L_0x0012
            r3 = 3
            if (r0 != r3) goto L_0x0010
            goto L_0x0012
        L_0x0010:
            r0 = r1
            goto L_0x0013
        L_0x0012:
            r0 = r2
        L_0x0013:
            boolean r7 = r7.isDeviceConnected(r8)
            r3 = 0
            if (r7 == 0) goto L_0x006d
            boolean r7 = DBG
            java.lang.String r4 = "ConnBluetoothDeviceUpdater"
            if (r7 == 0) goto L_0x0034
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "isFilterMatched() current audio profile : "
            r5.append(r6)
            r5.append(r0)
            java.lang.String r5 = r5.toString()
            android.util.Log.d(r4, r5)
        L_0x0034:
            boolean r5 = r8.isConnectedHearingAidDevice()
            if (r5 == 0) goto L_0x003b
            return r3
        L_0x003b:
            if (r0 == r2) goto L_0x0045
            if (r0 == r1) goto L_0x0040
            goto L_0x004b
        L_0x0040:
            boolean r0 = r8.isConnectedA2dpDevice()
            goto L_0x0049
        L_0x0045:
            boolean r0 = r8.isConnectedHfpDevice()
        L_0x0049:
            r0 = r0 ^ r2
            r3 = r0
        L_0x004b:
            if (r7 == 0) goto L_0x006d
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r0 = "isFilterMatched() device : "
            r7.append(r0)
            java.lang.String r8 = r8.getName()
            r7.append(r8)
            java.lang.String r8 = ", isFilterMatched : "
            r7.append(r8)
            r7.append(r3)
            java.lang.String r7 = r7.toString()
            android.util.Log.d(r4, r7)
        L_0x006d:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.bluetooth.ConnectedBluetoothDeviceUpdater.isFilterMatched(com.android.settingslib.bluetooth.CachedBluetoothDevice):boolean");
    }

    /* access modifiers changed from: protected */
    public void addPreference(CachedBluetoothDevice cachedBluetoothDevice) {
        super.addPreference(cachedBluetoothDevice);
        BluetoothDevice device = cachedBluetoothDevice.getDevice();
        if (this.mPreferenceMap.containsKey(device)) {
            BluetoothDevicePreference bluetoothDevicePreference = (BluetoothDevicePreference) this.mPreferenceMap.get(device);
            bluetoothDevicePreference.setOnGearClickListener((GearPreference.OnGearClickListener) null);
            bluetoothDevicePreference.hideSecondTarget(true);
            bluetoothDevicePreference.setOnPreferenceClickListener(new ConnectedBluetoothDeviceUpdater$$ExternalSyntheticLambda0(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$addPreference$0(Preference preference) {
        lambda$new$0(preference);
        return true;
    }
}
