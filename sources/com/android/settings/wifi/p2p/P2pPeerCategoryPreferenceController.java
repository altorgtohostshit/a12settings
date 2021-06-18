package com.android.settings.wifi.p2p;

import android.content.Context;

public class P2pPeerCategoryPreferenceController extends P2pCategoryPreferenceController {
    public String getPreferenceKey() {
        return "p2p_peer_devices";
    }

    public P2pPeerCategoryPreferenceController(Context context) {
        super(context);
    }
}
