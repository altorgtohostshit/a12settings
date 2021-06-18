package com.android.settings.development;

import android.content.Context;
import android.debug.PairDevice;
import android.os.Bundle;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.ArrayList;
import java.util.List;

public class AdbDeviceDetailsFragment extends DashboardFragment {
    private PairDevice mPairedDevice;

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "AdbDeviceDetailsFrag";
    }

    public int getMetricsCategory() {
        return 1836;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.adb_device_details_fragment;
    }

    public void onAttach(Context context) {
        Bundle arguments = getArguments();
        if (arguments.containsKey("paired_device")) {
            this.mPairedDevice = arguments.getParcelable("paired_device");
        }
        super.onAttach(context);
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new AdbDeviceDetailsHeaderController(this.mPairedDevice, context, this));
        arrayList.add(new AdbDeviceDetailsActionController(this.mPairedDevice, context, this));
        arrayList.add(new AdbDeviceDetailsFingerprintController(this.mPairedDevice, context, this));
        return arrayList;
    }
}
