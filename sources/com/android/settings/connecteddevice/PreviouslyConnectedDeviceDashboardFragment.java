package com.android.settings.connecteddevice;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.Bundle;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;

public class PreviouslyConnectedDeviceDashboardFragment extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.previously_connected_devices);
    BluetoothAdapter mBluetoothAdapter;

    public int getHelpResource() {
        return R.string.help_url_previously_connected_devices;
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "PreConnectedDeviceFrag";
    }

    public int getMetricsCategory() {
        return 1370;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.previously_connected_devices;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        ((SavedDeviceGroupController) use(SavedDeviceGroupController.class)).init(this);
    }

    public void onStart() {
        super.onStart();
        enableBluetoothIfNecessary();
    }

    /* access modifiers changed from: package-private */
    public void enableBluetoothIfNecessary() {
        BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
            this.mBluetoothAdapter.enable();
        }
    }
}
