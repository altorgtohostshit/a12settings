package com.android.settings.bluetooth;

import android.content.Context;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.widget.FooterPreference;

public class BluetoothDetailsMacAddressController extends BluetoothDetailsController {
    private FooterPreference mFooterPreference;

    public String getPreferenceKey() {
        return "device_details_footer";
    }

    public BluetoothDetailsMacAddressController(Context context, PreferenceFragmentCompat preferenceFragmentCompat, CachedBluetoothDevice cachedBluetoothDevice, Lifecycle lifecycle) {
        super(context, preferenceFragmentCompat, cachedBluetoothDevice, lifecycle);
    }

    /* access modifiers changed from: protected */
    public void init(PreferenceScreen preferenceScreen) {
        FooterPreference footerPreference = (FooterPreference) preferenceScreen.findPreference("device_details_footer");
        this.mFooterPreference = footerPreference;
        footerPreference.setTitle((CharSequence) this.mContext.getString(R.string.bluetooth_device_mac_address, new Object[]{this.mCachedDevice.getAddress()}));
    }

    /* access modifiers changed from: protected */
    public void refresh() {
        this.mFooterPreference.setTitle((CharSequence) this.mContext.getString(R.string.bluetooth_device_mac_address, new Object[]{this.mCachedDevice.getAddress()}));
    }
}
