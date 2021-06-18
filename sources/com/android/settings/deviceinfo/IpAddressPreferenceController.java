package com.android.settings.deviceinfo;

import android.content.Context;
import com.android.settings.R;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.deviceinfo.AbstractIpAddressPreferenceController;

public class IpAddressPreferenceController extends AbstractIpAddressPreferenceController implements PreferenceControllerMixin {
    public IpAddressPreferenceController(Context context, Lifecycle lifecycle) {
        super(context, lifecycle);
    }

    public boolean isAvailable() {
        return this.mContext.getResources().getBoolean(R.bool.config_show_wifi_ip_address);
    }
}
