package com.android.settings.connecteddevice.usb;

import android.content.Context;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settingslib.widget.RadioButtonPreference;

public class UsbDetailsDataRoleController extends UsbDetailsController implements RadioButtonPreference.OnClickListener {
    private RadioButtonPreference mDevicePref;
    private final Runnable mFailureCallback = new UsbDetailsDataRoleController$$ExternalSyntheticLambda0(this);
    private RadioButtonPreference mHostPref;
    private RadioButtonPreference mNextRolePref;
    private PreferenceCategory mPreferenceCategory;

    public String getPreferenceKey() {
        return "usb_details_data_role";
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        RadioButtonPreference radioButtonPreference = this.mNextRolePref;
        if (radioButtonPreference != null) {
            radioButtonPreference.setSummary((int) R.string.usb_switching_failed);
            this.mNextRolePref = null;
        }
    }

    public UsbDetailsDataRoleController(Context context, UsbDetailsFragment usbDetailsFragment, UsbBackend usbBackend) {
        super(context, usbDetailsFragment, usbBackend);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreferenceCategory = (PreferenceCategory) preferenceScreen.findPreference(getPreferenceKey());
        this.mHostPref = makeRadioPreference(UsbBackend.dataRoleToString(1), R.string.usb_control_host);
        this.mDevicePref = makeRadioPreference(UsbBackend.dataRoleToString(2), R.string.usb_control_device);
    }

    /* access modifiers changed from: protected */
    public void refresh(boolean z, long j, int i, int i2) {
        if (i2 == 2) {
            this.mDevicePref.setChecked(true);
            this.mHostPref.setChecked(false);
            this.mPreferenceCategory.setEnabled(true);
        } else if (i2 == 1) {
            this.mDevicePref.setChecked(false);
            this.mHostPref.setChecked(true);
            this.mPreferenceCategory.setEnabled(true);
        } else if (!z || i2 == 0) {
            this.mPreferenceCategory.setEnabled(false);
            if (this.mNextRolePref == null) {
                this.mHostPref.setSummary((CharSequence) "");
                this.mDevicePref.setSummary((CharSequence) "");
            }
        }
        RadioButtonPreference radioButtonPreference = this.mNextRolePref;
        if (radioButtonPreference != null && i2 != 0) {
            if (UsbBackend.dataRoleFromString(radioButtonPreference.getKey()) == i2) {
                this.mNextRolePref.setSummary((CharSequence) "");
            } else {
                this.mNextRolePref.setSummary((int) R.string.usb_switching_failed);
            }
            this.mNextRolePref = null;
            this.mHandler.removeCallbacks(this.mFailureCallback);
        }
    }

    public void onRadioButtonClicked(RadioButtonPreference radioButtonPreference) {
        int dataRoleFromString = UsbBackend.dataRoleFromString(radioButtonPreference.getKey());
        if (dataRoleFromString != this.mUsbBackend.getDataRole() && this.mNextRolePref == null && !Utils.isMonkeyRunning()) {
            this.mUsbBackend.setDataRole(dataRoleFromString);
            this.mNextRolePref = radioButtonPreference;
            radioButtonPreference.setSummary((int) R.string.usb_switching);
            this.mHandler.postDelayed(this.mFailureCallback, this.mUsbBackend.areAllRolesSupported() ? 3000 : 15000);
        }
    }

    public boolean isAvailable() {
        return !Utils.isMonkeyRunning();
    }

    private RadioButtonPreference makeRadioPreference(String str, int i) {
        RadioButtonPreference radioButtonPreference = new RadioButtonPreference(this.mPreferenceCategory.getContext());
        radioButtonPreference.setKey(str);
        radioButtonPreference.setTitle(i);
        radioButtonPreference.setOnClickListener(this);
        this.mPreferenceCategory.addPreference(radioButtonPreference);
        return radioButtonPreference;
    }
}
