package com.android.settings.wifi.p2p;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.text.TextUtils;
import android.widget.ImageView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;

public class WifiP2pPeer extends Preference {
    static final int SIGNAL_LEVELS = 4;
    private static final int[] STATE_SECURED = {R.attr.state_encrypted};
    public WifiP2pDevice device;
    final int mRssi = 60;
    private ImageView mSignal;

    public WifiP2pPeer(Context context, WifiP2pDevice wifiP2pDevice) {
        super(context);
        this.device = wifiP2pDevice;
        setWidgetLayoutResource(R.layout.preference_widget_wifi_signal);
        if (TextUtils.isEmpty(this.device.deviceName)) {
            setTitle((CharSequence) this.device.deviceAddress);
        } else {
            setTitle((CharSequence) this.device.deviceName);
        }
        setSummary((CharSequence) context.getResources().getStringArray(R.array.wifi_p2p_status)[this.device.status]);
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        ImageView imageView = (ImageView) preferenceViewHolder.findViewById(R.id.signal);
        this.mSignal = imageView;
        if (this.mRssi == Integer.MAX_VALUE) {
            imageView.setImageDrawable((Drawable) null);
        } else {
            imageView.setImageResource(R.drawable.wifi_signal);
            this.mSignal.setImageState(STATE_SECURED, true);
        }
        this.mSignal.setImageLevel(getLevel());
    }

    public int compareTo(Preference preference) {
        if (!(preference instanceof WifiP2pPeer)) {
            return 1;
        }
        WifiP2pDevice wifiP2pDevice = this.device;
        int i = wifiP2pDevice.status;
        WifiP2pDevice wifiP2pDevice2 = ((WifiP2pPeer) preference).device;
        int i2 = wifiP2pDevice2.status;
        if (i == i2) {
            String str = wifiP2pDevice.deviceName;
            if (str != null) {
                return str.compareToIgnoreCase(wifiP2pDevice2.deviceName);
            }
            return wifiP2pDevice.deviceAddress.compareToIgnoreCase(wifiP2pDevice2.deviceAddress);
        } else if (i < i2) {
            return -1;
        } else {
            return 1;
        }
    }

    /* access modifiers changed from: package-private */
    public int getLevel() {
        int i = this.mRssi;
        if (i == Integer.MAX_VALUE) {
            return -1;
        }
        return WifiManager.calculateSignalLevel(i, 4);
    }
}
