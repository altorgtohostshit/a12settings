package com.android.settingslib.media;

import android.content.Context;
import android.media.MediaRoute2Info;
import android.media.MediaRouter2Manager;
import com.android.settingslib.R$drawable;
import com.android.settingslib.R$string;

public class PhoneMediaDevice extends MediaDevice {
    private String mSummary = "";

    public boolean isConnected() {
        return true;
    }

    PhoneMediaDevice(Context context, MediaRouter2Manager mediaRouter2Manager, MediaRoute2Info mediaRoute2Info, String str) {
        super(context, mediaRouter2Manager, mediaRoute2Info, str);
        initDeviceRecord();
    }

    public String getName() {
        CharSequence charSequence;
        int type = this.mRouteInfo.getType();
        if (!(type == 3 || type == 4)) {
            if (type != 9) {
                if (type != 22) {
                    switch (type) {
                        case 11:
                        case 12:
                            break;
                        case 13:
                            break;
                        default:
                            charSequence = this.mContext.getString(R$string.media_transfer_this_device_name);
                            break;
                    }
                }
            }
            charSequence = this.mRouteInfo.getName();
            return charSequence.toString();
        }
        charSequence = this.mContext.getString(R$string.media_transfer_wired_usb_device_name);
        return charSequence.toString();
    }

    /* access modifiers changed from: package-private */
    public int getDrawableResId() {
        int type = this.mRouteInfo.getType();
        if (!(type == 3 || type == 4 || type == 9 || type == 22)) {
            switch (type) {
                case 11:
                case 12:
                case 13:
                    break;
                default:
                    return R$drawable.ic_smartphone;
            }
        }
        return R$drawable.ic_headphone;
    }

    public String getId() {
        int type = this.mRouteInfo.getType();
        if (type == 3 || type == 4) {
            return "wired_headset_media_device_id";
        }
        if (!(type == 9 || type == 22)) {
            switch (type) {
                case 11:
                case 12:
                case 13:
                    break;
                default:
                    return "phone_media_device_id";
            }
        }
        return "usb_headset_media_device_id";
    }
}
