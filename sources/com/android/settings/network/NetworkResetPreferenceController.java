package com.android.settings.network;

import android.content.Context;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.AbstractPreferenceController;

public class NetworkResetPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin {
    private final NetworkResetRestrictionChecker mRestrictionChecker;

    public String getPreferenceKey() {
        return "network_reset_pref";
    }

    public NetworkResetPreferenceController(Context context) {
        super(context);
        this.mRestrictionChecker = new NetworkResetRestrictionChecker(context);
    }

    public boolean isAvailable() {
        return !this.mRestrictionChecker.hasUserRestriction();
    }
}
