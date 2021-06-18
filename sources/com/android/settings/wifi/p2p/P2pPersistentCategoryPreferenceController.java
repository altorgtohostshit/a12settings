package com.android.settings.wifi.p2p;

import android.content.Context;

public class P2pPersistentCategoryPreferenceController extends P2pCategoryPreferenceController {
    public String getPreferenceKey() {
        return "p2p_persistent_group";
    }

    public P2pPersistentCategoryPreferenceController(Context context) {
        super(context);
    }
}
