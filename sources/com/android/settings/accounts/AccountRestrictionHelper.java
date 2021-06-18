package com.android.settings.accounts;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.pm.UserInfo;
import android.os.UserManager;
import com.android.settings.AccessiblePreferenceCategory;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.RestrictedPreference;
import java.util.ArrayList;

public class AccountRestrictionHelper {
    private final Context mContext;

    public AccountRestrictionHelper(Context context) {
        this.mContext = context;
    }

    public void enforceRestrictionOnPreference(RestrictedPreference restrictedPreference, String str, int i) {
        if (restrictedPreference != null) {
            if (!hasBaseUserRestriction(str, i)) {
                restrictedPreference.checkRestrictionAndSetDisabled(str, i);
            } else if (!str.equals("no_remove_managed_profile") || !isOrganizationOwnedDevice()) {
                restrictedPreference.setEnabled(false);
            } else {
                restrictedPreference.setDisabledByAdmin(getEnforcedAdmin(str, i));
            }
        }
    }

    public boolean hasBaseUserRestriction(String str, int i) {
        return RestrictedLockUtilsInternal.hasBaseUserRestriction(this.mContext, str, i);
    }

    private boolean isOrganizationOwnedDevice() {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) this.mContext.getSystemService("device_policy");
        if (devicePolicyManager == null) {
            return false;
        }
        return devicePolicyManager.isOrganizationOwnedDeviceWithManagedProfile();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x000e, code lost:
        r2 = getManagedUserId(r4);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.android.settingslib.RestrictedLockUtils.EnforcedAdmin getEnforcedAdmin(java.lang.String r3, int r4) {
        /*
            r2 = this;
            android.content.Context r0 = r2.mContext
            java.lang.String r1 = "device_policy"
            java.lang.Object r0 = r0.getSystemService(r1)
            android.app.admin.DevicePolicyManager r0 = (android.app.admin.DevicePolicyManager) r0
            r1 = 0
            if (r0 != 0) goto L_0x000e
            return r1
        L_0x000e:
            int r2 = r2.getManagedUserId(r4)
            android.content.ComponentName r4 = r0.getProfileOwnerAsUser(r2)
            if (r4 == 0) goto L_0x0022
            com.android.settingslib.RestrictedLockUtils$EnforcedAdmin r0 = new com.android.settingslib.RestrictedLockUtils$EnforcedAdmin
            android.os.UserHandle r2 = android.os.UserHandle.of(r2)
            r0.<init>(r4, r3, r2)
            return r0
        L_0x0022:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.accounts.AccountRestrictionHelper.getEnforcedAdmin(java.lang.String, int):com.android.settingslib.RestrictedLockUtils$EnforcedAdmin");
    }

    private int getManagedUserId(int i) {
        for (UserInfo userInfo : UserManager.get(this.mContext).getProfiles(i)) {
            if (userInfo.id != i && userInfo.isManagedProfile()) {
                return userInfo.id;
            }
        }
        return -1;
    }

    public AccessiblePreferenceCategory createAccessiblePreferenceCategory(Context context) {
        return new AccessiblePreferenceCategory(context);
    }

    public static boolean showAccount(String[] strArr, ArrayList<String> arrayList) {
        if (strArr == null || arrayList == null) {
            return true;
        }
        for (String contains : strArr) {
            if (arrayList.contains(contains)) {
                return true;
            }
        }
        return false;
    }
}
