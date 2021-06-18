package com.android.settings.deviceinfo.aboutphone;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import com.android.settings.R;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;

public class DeviceNameWarningDialog extends InstrumentedDialogFragment implements DialogInterface.OnClickListener {
    public int getMetricsCategory() {
        return 1219;
    }

    public static void show(MyDeviceInfoFragment myDeviceInfoFragment) {
        FragmentManager supportFragmentManager = myDeviceInfoFragment.getActivity().getSupportFragmentManager();
        if (supportFragmentManager.findFragmentByTag("DeviceNameWarningDlg") == null) {
            DeviceNameWarningDialog deviceNameWarningDialog = new DeviceNameWarningDialog();
            deviceNameWarningDialog.setTargetFragment(myDeviceInfoFragment, 0);
            deviceNameWarningDialog.show(supportFragmentManager, "DeviceNameWarningDlg");
        }
    }

    public Dialog onCreateDialog(Bundle bundle) {
        return new AlertDialog.Builder(getActivity()).setTitle((int) R.string.my_device_info_device_name_preference_title).setMessage((int) R.string.about_phone_device_name_warning).setCancelable(false).setPositiveButton(17039370, (DialogInterface.OnClickListener) this).setNegativeButton(17039360, (DialogInterface.OnClickListener) this).create();
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        MyDeviceInfoFragment myDeviceInfoFragment = (MyDeviceInfoFragment) getTargetFragment();
        if (i == -1) {
            myDeviceInfoFragment.onSetDeviceNameConfirm(true);
        } else {
            myDeviceInfoFragment.onSetDeviceNameConfirm(false);
        }
    }
}
