package com.android.settings.bluetooth;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.android.settings.R;

public final class DevicePickerActivity extends FragmentActivity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addSystemFlags(524288);
        setContentView((int) R.layout.bluetooth_device_picker);
    }
}
