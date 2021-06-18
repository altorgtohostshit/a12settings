package com.android.settings.panel;

import android.content.Context;
import android.os.Bundle;
import com.android.settings.Utils;

public class PanelFeatureProviderImpl implements PanelFeatureProvider {
    public PanelContent getPanel(Context context, Bundle bundle) {
        if (context == null) {
            return null;
        }
        String string = bundle.getString("PANEL_TYPE_ARGUMENT");
        bundle.getString("PANEL_MEDIA_PACKAGE_NAME");
        string.hashCode();
        char c = 65535;
        switch (string.hashCode()) {
            case 66351017:
                if (string.equals("android.settings.panel.action.NFC")) {
                    c = 0;
                    break;
                }
                break;
            case 464243859:
                if (string.equals("android.settings.panel.action.INTERNET_CONNECTIVITY")) {
                    c = 1;
                    break;
                }
                break;
            case 1215888444:
                if (string.equals("android.settings.panel.action.VOLUME")) {
                    c = 2;
                    break;
                }
                break;
            case 2057152695:
                if (string.equals("android.settings.panel.action.WIFI")) {
                    c = 3;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return NfcPanel.create(context);
            case 1:
                return InternetConnectivityPanel.create(context);
            case 2:
                return VolumePanel.create(context);
            case 3:
                if (Utils.isProviderModelEnabled(context)) {
                    return InternetConnectivityPanel.create(context);
                }
                return WifiPanel.create(context);
            default:
                throw new IllegalStateException("No matching panel for: " + string);
        }
    }
}
