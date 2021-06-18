package com.android.settings.connecteddevice.usb;

import android.content.Context;
import android.os.SystemProperties;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.settings.R;
import com.android.settings.Utils;

public class UsbDetailsTranscodeMtpController extends UsbDetailsController implements Preference.OnPreferenceClickListener {
    private PreferenceCategory mPreferenceCategory;
    private SwitchPreference mSwitchPreference;

    private static boolean isDeviceInFileTransferMode(long j, int i) {
        return i == 2 && !((4 & j) == 0 && (j & 16) == 0);
    }

    public String getPreferenceKey() {
        return "usb_transcode_mtp";
    }

    public UsbDetailsTranscodeMtpController(Context context, UsbDetailsFragment usbDetailsFragment, UsbBackend usbBackend) {
        super(context, usbDetailsFragment, usbBackend);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        PreferenceCategory preferenceCategory = (PreferenceCategory) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreferenceCategory = preferenceCategory;
        SwitchPreference switchPreference = new SwitchPreference(preferenceCategory.getContext());
        this.mSwitchPreference = switchPreference;
        switchPreference.setTitle((int) R.string.usb_transcode_files);
        this.mSwitchPreference.setOnPreferenceClickListener(this);
        this.mPreferenceCategory.addPreference(this.mSwitchPreference);
    }

    /* access modifiers changed from: protected */
    public void refresh(boolean z, long j, int i, int i2) {
        if (this.mUsbBackend.areFunctionsSupported(20)) {
            this.mFragment.getPreferenceScreen().addPreference(this.mPreferenceCategory);
        } else {
            this.mFragment.getPreferenceScreen().removePreference(this.mPreferenceCategory);
        }
        boolean z2 = false;
        this.mSwitchPreference.setChecked(SystemProperties.getBoolean("sys.fuse.transcode_mtp", false));
        PreferenceCategory preferenceCategory = this.mPreferenceCategory;
        if (z && isDeviceInFileTransferMode(j, i2)) {
            z2 = true;
        }
        preferenceCategory.setEnabled(z2);
    }

    public boolean onPreferenceClick(Preference preference) {
        SystemProperties.set("sys.fuse.transcode_mtp", Boolean.toString(this.mSwitchPreference.isChecked()));
        return true;
    }

    public boolean isAvailable() {
        return !Utils.isMonkeyRunning();
    }
}
