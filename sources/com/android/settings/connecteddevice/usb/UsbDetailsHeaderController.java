package com.android.settings.connecteddevice.usb;

import android.app.Activity;
import android.content.Context;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.widget.EntityHeaderController;
import com.android.settingslib.widget.LayoutPreference;

public class UsbDetailsHeaderController extends UsbDetailsController {
    private EntityHeaderController mHeaderController;

    public String getPreferenceKey() {
        return "usb_device_header";
    }

    public UsbDetailsHeaderController(Context context, UsbDetailsFragment usbDetailsFragment, UsbBackend usbBackend) {
        super(context, usbDetailsFragment, usbBackend);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mHeaderController = EntityHeaderController.newInstance(this.mFragment.getActivity(), this.mFragment, ((LayoutPreference) preferenceScreen.findPreference("usb_device_header")).findViewById(R.id.entity_header));
    }

    /* access modifiers changed from: protected */
    public void refresh(boolean z, long j, int i, int i2) {
        this.mHeaderController.setLabel((CharSequence) this.mContext.getString(R.string.usb_pref));
        this.mHeaderController.setIcon(this.mContext.getDrawable(R.drawable.ic_usb));
        this.mHeaderController.done((Activity) this.mFragment.getActivity(), true);
    }
}
