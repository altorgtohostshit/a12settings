package com.android.settingslib;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.UserHandle;

public class TetherUtil {
    public static boolean isTetherAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(ConnectivityManager.class);
        boolean z = RestrictedLockUtilsInternal.checkIfRestrictionEnforced(context, "no_config_tethering", UserHandle.myUserId()) != null;
        boolean hasBaseUserRestriction = RestrictedLockUtilsInternal.hasBaseUserRestriction(context, "no_config_tethering", UserHandle.myUserId());
        if ((connectivityManager.isTetheringSupported() || z) && !hasBaseUserRestriction) {
            return true;
        }
        return false;
    }
}
