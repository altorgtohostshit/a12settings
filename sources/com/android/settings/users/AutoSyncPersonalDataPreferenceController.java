package com.android.settings.users;

import android.content.Context;
import android.os.UserHandle;
import androidx.fragment.app.Fragment;

public class AutoSyncPersonalDataPreferenceController extends AutoSyncDataPreferenceController {
    public String getPreferenceKey() {
        return "auto_sync_personal_account_data";
    }

    public AutoSyncPersonalDataPreferenceController(Context context, Fragment fragment) {
        super(context, fragment);
    }

    public boolean isAvailable() {
        if (this.mUserManager.isManagedProfile() || this.mUserManager.isLinkedUser() || this.mUserManager.getProfiles(UserHandle.myUserId()).size() <= 1) {
            return false;
        }
        return true;
    }
}
