package com.android.settings.bluetooth;

import android.content.Context;
import android.view.View;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.widget.ActionButtonsPreference;

public class BluetoothDetailsButtonsController extends BluetoothDetailsController {
    private ActionButtonsPreference mActionButtons;
    private boolean mConnectButtonInitialized;
    private boolean mIsConnected;

    public String getPreferenceKey() {
        return "action_buttons";
    }

    public BluetoothDetailsButtonsController(Context context, PreferenceFragmentCompat preferenceFragmentCompat, CachedBluetoothDevice cachedBluetoothDevice, Lifecycle lifecycle) {
        super(context, preferenceFragmentCompat, cachedBluetoothDevice, lifecycle);
        this.mIsConnected = cachedBluetoothDevice.isConnected();
    }

    private void onForgetButtonPressed() {
        ForgetDeviceDialogFragment.newInstance(this.mCachedDevice.getAddress()).show(this.mFragment.getFragmentManager(), "ForgetBluetoothDevice");
    }

    /* access modifiers changed from: protected */
    public void init(PreferenceScreen preferenceScreen) {
        this.mActionButtons = ((ActionButtonsPreference) preferenceScreen.findPreference(getPreferenceKey())).setButton1Text(R.string.forget).setButton1Icon(R.drawable.ic_settings_delete).setButton1OnClickListener(new BluetoothDetailsButtonsController$$ExternalSyntheticLambda0(this)).setButton1Enabled(true);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$init$0(View view) {
        onForgetButtonPressed();
    }

    /* access modifiers changed from: protected */
    public void refresh() {
        this.mActionButtons.setButton2Enabled(!this.mCachedDevice.isBusy());
        boolean z = this.mIsConnected;
        boolean isConnected = this.mCachedDevice.isConnected();
        this.mIsConnected = isConnected;
        if (isConnected) {
            if (!this.mConnectButtonInitialized || !z) {
                this.mActionButtons.setButton2Text(R.string.bluetooth_device_context_disconnect).setButton2Icon(R.drawable.ic_settings_close).setButton2OnClickListener(new BluetoothDetailsButtonsController$$ExternalSyntheticLambda2(this));
                this.mConnectButtonInitialized = true;
            }
        } else if (!this.mConnectButtonInitialized || z) {
            this.mActionButtons.setButton2Text(R.string.bluetooth_device_context_connect).setButton2Icon(R.drawable.ic_add_24dp).setButton2OnClickListener(new BluetoothDetailsButtonsController$$ExternalSyntheticLambda1(this));
            this.mConnectButtonInitialized = true;
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$refresh$1(View view) {
        this.mCachedDevice.disconnect();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$refresh$2(View view) {
        this.mCachedDevice.connect();
    }
}
