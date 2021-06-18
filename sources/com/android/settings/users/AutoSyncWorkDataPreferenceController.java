package com.android.settings.users;

import android.content.Context;
import android.os.UserHandle;
import androidx.fragment.app.Fragment;
import com.android.settings.Utils;

public class AutoSyncWorkDataPreferenceController extends AutoSyncPersonalDataPreferenceController {
    public String getPreferenceKey() {
        return "auto_sync_work_account_data";
    }

    public AutoSyncWorkDataPreferenceController(Context context, Fragment fragment) {
        super(context, fragment);
        this.mUserHandle = Utils.getManagedProfileWithDisabled(this.mUserManager);
    }

    public boolean isAvailable() {
        if (this.mUserHandle == null || this.mUserManager.isManagedProfile() || this.mUserManager.isLinkedUser() || this.mUserManager.getProfiles(UserHandle.myUserId()).size() <= 1) {
            return false;
        }
        return true;
    }
}
