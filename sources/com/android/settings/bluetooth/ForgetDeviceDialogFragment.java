package com.android.settings.bluetooth;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import com.android.settings.R;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;
import com.android.settingslib.bluetooth.BluetoothUtils;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.LocalBluetoothManager;

public class ForgetDeviceDialogFragment extends InstrumentedDialogFragment {
    private CachedBluetoothDevice mDevice;

    public int getMetricsCategory() {
        return 1031;
    }

    public static ForgetDeviceDialogFragment newInstance(String str) {
        Bundle bundle = new Bundle(1);
        bundle.putString("device_address", str);
        ForgetDeviceDialogFragment forgetDeviceDialogFragment = new ForgetDeviceDialogFragment();
        forgetDeviceDialogFragment.setArguments(bundle);
        return forgetDeviceDialogFragment;
    }

    /* access modifiers changed from: package-private */
    public CachedBluetoothDevice getDevice(Context context) {
        String string = getArguments().getString("device_address");
        LocalBluetoothManager localBtManager = Utils.getLocalBtManager(context);
        return localBtManager.getCachedDeviceManager().findDevice(localBtManager.getBluetoothAdapter().getRemoteDevice(string));
    }

    public Dialog onCreateDialog(Bundle bundle) {
        ForgetDeviceDialogFragment$$ExternalSyntheticLambda0 forgetDeviceDialogFragment$$ExternalSyntheticLambda0 = new ForgetDeviceDialogFragment$$ExternalSyntheticLambda0(this);
        Context context = getContext();
        CachedBluetoothDevice device = getDevice(context);
        this.mDevice = device;
        boolean booleanMetaData = BluetoothUtils.getBooleanMetaData(device.getDevice(), 6);
        AlertDialog create = new AlertDialog.Builder(context).setPositiveButton((int) R.string.bluetooth_unpair_dialog_forget_confirm_button, (DialogInterface.OnClickListener) forgetDeviceDialogFragment$$ExternalSyntheticLambda0).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).create();
        create.setTitle((int) R.string.bluetooth_unpair_dialog_title);
        create.setMessage(context.getString(booleanMetaData ? R.string.bluetooth_untethered_unpair_dialog_body : R.string.bluetooth_unpair_dialog_body, new Object[]{this.mDevice.getName()}));
        return create;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$0(DialogInterface dialogInterface, int i) {
        this.mDevice.unpair();
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }
}
