package com.android.settingslib;

import android.app.AppGlobals;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.UserInfo;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.widget.TextView;
import com.android.internal.widget.LockPatternUtils;
import com.android.settingslib.RestrictedLockUtils;
import java.util.List;

public class RestrictedLockUtilsInternal extends RestrictedLockUtils {
    static Proxy sProxy = new Proxy();

    private interface LockSettingCheck {
        boolean isEnforcing(DevicePolicyManager devicePolicyManager, ComponentName componentName, int i);
    }

    public static Drawable getRestrictedPadlock(Context context) {
        Drawable drawable = context.getDrawable(17301684);
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(17104903);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{16843829});
        int color = obtainStyledAttributes.getColor(0, 0);
        obtainStyledAttributes.recycle();
        drawable.setTint(color);
        drawable.setBounds(0, 0, dimensionPixelSize, dimensionPixelSize);
        return drawable;
    }

    public static RestrictedLockUtils.EnforcedAdmin checkIfRestrictionEnforced(Context context, String str, int i) {
        if (((DevicePolicyManager) context.getSystemService("device_policy")) == null) {
            return null;
        }
        UserManager userManager = UserManager.get(context);
        List userRestrictionSources = userManager.getUserRestrictionSources(str, UserHandle.of(i));
        if (userRestrictionSources.isEmpty()) {
            return null;
        }
        if (userRestrictionSources.size() > 1) {
            return RestrictedLockUtils.EnforcedAdmin.createDefaultEnforcedAdminWithRestriction(str);
        }
        int userRestrictionSource = ((UserManager.EnforcingUser) userRestrictionSources.get(0)).getUserRestrictionSource();
        int identifier = ((UserManager.EnforcingUser) userRestrictionSources.get(0)).getUserHandle().getIdentifier();
        if (userRestrictionSource == 4) {
            if (identifier == i) {
                return getProfileOwner(context, str, identifier);
            }
            UserInfo profileParent = userManager.getProfileParent(identifier);
            if (profileParent == null || profileParent.id != i) {
                return RestrictedLockUtils.EnforcedAdmin.createDefaultEnforcedAdminWithRestriction(str);
            }
            return getProfileOwner(context, str, identifier);
        } else if (userRestrictionSource != 2) {
            return null;
        } else {
            if (identifier == i) {
                return getDeviceOwner(context, str);
            }
            return RestrictedLockUtils.EnforcedAdmin.createDefaultEnforcedAdminWithRestriction(str);
        }
    }

    public static boolean hasBaseUserRestriction(Context context, String str, int i) {
        return ((UserManager) context.getSystemService("user")).hasBaseUserRestriction(str, UserHandle.of(i));
    }

    public static RestrictedLockUtils.EnforcedAdmin checkIfKeyguardFeaturesDisabled(Context context, int i, int i2) {
        RestrictedLockUtilsInternal$$ExternalSyntheticLambda0 restrictedLockUtilsInternal$$ExternalSyntheticLambda0 = new RestrictedLockUtilsInternal$$ExternalSyntheticLambda0(i2, i);
        if (!UserManager.get(context).getUserInfo(i2).isManagedProfile()) {
            return checkForLockSetting(context, i2, restrictedLockUtilsInternal$$ExternalSyntheticLambda0);
        }
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService("device_policy");
        return findEnforcedAdmin(devicePolicyManager.getActiveAdminsAsUser(i2), devicePolicyManager, i2, restrictedLockUtilsInternal$$ExternalSyntheticLambda0);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$checkIfKeyguardFeaturesDisabled$0(int i, int i2, DevicePolicyManager devicePolicyManager, ComponentName componentName, int i3) {
        int keyguardDisabledFeatures = devicePolicyManager.getKeyguardDisabledFeatures(componentName, i3);
        if (i3 != i) {
            keyguardDisabledFeatures &= 438;
        }
        return (keyguardDisabledFeatures & i2) != 0;
    }

    private static UserHandle getUserHandleOf(int i) {
        if (i == -10000) {
            return null;
        }
        return UserHandle.of(i);
    }

    private static RestrictedLockUtils.EnforcedAdmin findEnforcedAdmin(List<ComponentName> list, DevicePolicyManager devicePolicyManager, int i, LockSettingCheck lockSettingCheck) {
        RestrictedLockUtils.EnforcedAdmin enforcedAdmin = null;
        if (list == null) {
            return null;
        }
        UserHandle userHandleOf = getUserHandleOf(i);
        for (ComponentName next : list) {
            if (lockSettingCheck.isEnforcing(devicePolicyManager, next, i)) {
                if (enforcedAdmin != null) {
                    return RestrictedLockUtils.EnforcedAdmin.MULTIPLE_ENFORCED_ADMIN;
                }
                enforcedAdmin = new RestrictedLockUtils.EnforcedAdmin(next, userHandleOf);
            }
        }
        return enforcedAdmin;
    }

    public static RestrictedLockUtils.EnforcedAdmin checkIfUninstallBlocked(Context context, String str, int i) {
        RestrictedLockUtils.EnforcedAdmin checkIfRestrictionEnforced = checkIfRestrictionEnforced(context, "no_control_apps", i);
        if (checkIfRestrictionEnforced != null) {
            return checkIfRestrictionEnforced;
        }
        RestrictedLockUtils.EnforcedAdmin checkIfRestrictionEnforced2 = checkIfRestrictionEnforced(context, "no_uninstall_apps", i);
        if (checkIfRestrictionEnforced2 != null) {
            return checkIfRestrictionEnforced2;
        }
        try {
            if (AppGlobals.getPackageManager().getBlockUninstallForUser(str, i)) {
                return RestrictedLockUtils.getProfileOrDeviceOwner(context, getUserHandleOf(i));
            }
            return null;
        } catch (RemoteException unused) {
            return null;
        }
    }

    public static RestrictedLockUtils.EnforcedAdmin checkIfApplicationIsSuspended(Context context, String str, int i) {
        try {
            if (AppGlobals.getPackageManager().isPackageSuspendedForUser(str, i)) {
                return RestrictedLockUtils.getProfileOrDeviceOwner(context, getUserHandleOf(i));
            }
            return null;
        } catch (RemoteException | IllegalArgumentException unused) {
            return null;
        }
    }

    public static RestrictedLockUtils.EnforcedAdmin checkIfInputMethodDisallowed(Context context, String str, int i) {
        RestrictedLockUtils.EnforcedAdmin enforcedAdmin;
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService("device_policy");
        if (devicePolicyManager == null) {
            return null;
        }
        RestrictedLockUtils.EnforcedAdmin profileOrDeviceOwner = RestrictedLockUtils.getProfileOrDeviceOwner(context, getUserHandleOf(i));
        boolean z = true;
        boolean isInputMethodPermittedByAdmin = profileOrDeviceOwner != null ? devicePolicyManager.isInputMethodPermittedByAdmin(profileOrDeviceOwner.component, str, i) : true;
        int managedProfileId = getManagedProfileId(context, i);
        if (managedProfileId != -10000) {
            enforcedAdmin = RestrictedLockUtils.getProfileOrDeviceOwner(context, getUserHandleOf(managedProfileId));
            if (enforcedAdmin != null && devicePolicyManager.isOrganizationOwnedDeviceWithManagedProfile()) {
                z = sProxy.getParentProfileInstance(devicePolicyManager, UserManager.get(context).getUserInfo(managedProfileId)).isInputMethodPermittedByAdmin(enforcedAdmin.component, str, managedProfileId);
            }
        } else {
            enforcedAdmin = null;
        }
        if (!isInputMethodPermittedByAdmin && !z) {
            return RestrictedLockUtils.EnforcedAdmin.MULTIPLE_ENFORCED_ADMIN;
        }
        if (!isInputMethodPermittedByAdmin) {
            return profileOrDeviceOwner;
        }
        if (!z) {
            return enforcedAdmin;
        }
        return null;
    }

    public static RestrictedLockUtils.EnforcedAdmin checkIfRemoteContactSearchDisallowed(Context context, int i) {
        RestrictedLockUtils.EnforcedAdmin profileOwner;
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService("device_policy");
        if (devicePolicyManager == null || (profileOwner = getProfileOwner(context, i)) == null) {
            return null;
        }
        UserHandle of = UserHandle.of(i);
        if (!devicePolicyManager.getCrossProfileContactsSearchDisabled(of) || !devicePolicyManager.getCrossProfileCallerIdDisabled(of)) {
            return null;
        }
        return profileOwner;
    }

    public static RestrictedLockUtils.EnforcedAdmin checkIfAccessibilityServiceDisallowed(Context context, String str, int i) {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService("device_policy");
        if (devicePolicyManager == null) {
            return null;
        }
        RestrictedLockUtils.EnforcedAdmin profileOrDeviceOwner = RestrictedLockUtils.getProfileOrDeviceOwner(context, getUserHandleOf(i));
        boolean z = true;
        boolean isAccessibilityServicePermittedByAdmin = profileOrDeviceOwner != null ? devicePolicyManager.isAccessibilityServicePermittedByAdmin(profileOrDeviceOwner.component, str, i) : true;
        int managedProfileId = getManagedProfileId(context, i);
        RestrictedLockUtils.EnforcedAdmin profileOrDeviceOwner2 = RestrictedLockUtils.getProfileOrDeviceOwner(context, getUserHandleOf(managedProfileId));
        if (profileOrDeviceOwner2 != null) {
            z = devicePolicyManager.isAccessibilityServicePermittedByAdmin(profileOrDeviceOwner2.component, str, managedProfileId);
        }
        if (!isAccessibilityServicePermittedByAdmin && !z) {
            return RestrictedLockUtils.EnforcedAdmin.MULTIPLE_ENFORCED_ADMIN;
        }
        if (!isAccessibilityServicePermittedByAdmin) {
            return profileOrDeviceOwner;
        }
        if (!z) {
            return profileOrDeviceOwner2;
        }
        return null;
    }

    private static int getManagedProfileId(Context context, int i) {
        for (UserInfo userInfo : ((UserManager) context.getSystemService("user")).getProfiles(i)) {
            if (userInfo.id != i && userInfo.isManagedProfile()) {
                return userInfo.id;
            }
        }
        return -10000;
    }

    public static RestrictedLockUtils.EnforcedAdmin checkIfAccountManagementDisabled(Context context, String str, int i) {
        if (str == null) {
            return null;
        }
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService("device_policy");
        if (!context.getPackageManager().hasSystemFeature("android.software.device_admin") || devicePolicyManager == null) {
            return null;
        }
        String[] accountTypesWithManagementDisabledAsUser = devicePolicyManager.getAccountTypesWithManagementDisabledAsUser(i);
        int length = accountTypesWithManagementDisabledAsUser.length;
        boolean z = false;
        int i2 = 0;
        while (true) {
            if (i2 >= length) {
                break;
            } else if (str.equals(accountTypesWithManagementDisabledAsUser[i2])) {
                z = true;
                break;
            } else {
                i2++;
            }
        }
        if (!z) {
            return null;
        }
        return RestrictedLockUtils.getProfileOrDeviceOwner(context, getUserHandleOf(i));
    }

    public static RestrictedLockUtils.EnforcedAdmin checkIfUsbDataSignalingIsDisabled(Context context, int i) {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(DevicePolicyManager.class);
        if (devicePolicyManager == null || devicePolicyManager.isUsbDataSignalingEnabledForUser(i)) {
            return null;
        }
        RestrictedLockUtils.EnforcedAdmin profileOrDeviceOwner = RestrictedLockUtils.getProfileOrDeviceOwner(context, getUserHandleOf(i));
        int managedProfileId = getManagedProfileId(context, i);
        return (profileOrDeviceOwner != null || managedProfileId == -10000) ? profileOrDeviceOwner : RestrictedLockUtils.getProfileOrDeviceOwner(context, getUserHandleOf(managedProfileId));
    }

    public static RestrictedLockUtils.EnforcedAdmin checkIfMeteredDataRestricted(Context context, String str, int i) {
        RestrictedLockUtils.EnforcedAdmin profileOrDeviceOwner = RestrictedLockUtils.getProfileOrDeviceOwner(context, getUserHandleOf(i));
        if (profileOrDeviceOwner == null) {
            return null;
        }
        if (((DevicePolicyManager) context.getSystemService("device_policy")).isMeteredDataDisabledPackageForUser(profileOrDeviceOwner.component, str, i)) {
            return profileOrDeviceOwner;
        }
        return null;
    }

    public static RestrictedLockUtils.EnforcedAdmin checkIfPasswordQualityIsSet(Context context, int i) {
        RestrictedLockUtilsInternal$$ExternalSyntheticLambda1 restrictedLockUtilsInternal$$ExternalSyntheticLambda1 = RestrictedLockUtilsInternal$$ExternalSyntheticLambda1.INSTANCE;
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService("device_policy");
        RestrictedLockUtils.EnforcedAdmin enforcedAdmin = null;
        if (devicePolicyManager == null) {
            return null;
        }
        LockPatternUtils lockPatternUtils = new LockPatternUtils(context);
        int aggregatedPasswordComplexityForUser = devicePolicyManager.getAggregatedPasswordComplexityForUser(i);
        if (aggregatedPasswordComplexityForUser > 0) {
            UserHandle deviceOwnerUser = devicePolicyManager.getDeviceOwnerUser();
            if (deviceOwnerUser != null) {
                return new RestrictedLockUtils.EnforcedAdmin(devicePolicyManager.getDeviceOwnerComponentOnAnyUser(), deviceOwnerUser);
            }
            for (UserInfo userInfo : UserManager.get(context).getProfiles(i)) {
                ComponentName profileOwnerAsUser = devicePolicyManager.getProfileOwnerAsUser(userInfo.id);
                if (profileOwnerAsUser != null) {
                    return new RestrictedLockUtils.EnforcedAdmin(profileOwnerAsUser, getUserHandleOf(userInfo.id));
                }
            }
            throw new IllegalStateException(String.format("Could not find admin enforcing complexity %d for user %d", new Object[]{Integer.valueOf(aggregatedPasswordComplexityForUser), Integer.valueOf(i)}));
        } else if (!sProxy.isSeparateProfileChallengeEnabled(lockPatternUtils, i)) {
            return checkForLockSetting(context, i, restrictedLockUtilsInternal$$ExternalSyntheticLambda1);
        } else {
            List<ComponentName> activeAdminsAsUser = devicePolicyManager.getActiveAdminsAsUser(i);
            if (activeAdminsAsUser == null) {
                return null;
            }
            UserHandle userHandleOf = getUserHandleOf(i);
            for (ComponentName componentName : activeAdminsAsUser) {
                if (restrictedLockUtilsInternal$$ExternalSyntheticLambda1.isEnforcing(devicePolicyManager, componentName, i)) {
                    if (enforcedAdmin != null) {
                        return RestrictedLockUtils.EnforcedAdmin.MULTIPLE_ENFORCED_ADMIN;
                    }
                    enforcedAdmin = new RestrictedLockUtils.EnforcedAdmin(componentName, userHandleOf);
                }
            }
            return enforcedAdmin;
        }
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$checkIfPasswordQualityIsSet$1(DevicePolicyManager devicePolicyManager, ComponentName componentName, int i) {
        return devicePolicyManager.getPasswordQuality(componentName, i) > 0;
    }

    public static RestrictedLockUtils.EnforcedAdmin checkIfMaximumTimeToLockIsSet(Context context) {
        return checkForLockSetting(context, UserHandle.myUserId(), RestrictedLockUtilsInternal$$ExternalSyntheticLambda2.INSTANCE);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$checkIfMaximumTimeToLockIsSet$2(DevicePolicyManager devicePolicyManager, ComponentName componentName, int i) {
        return devicePolicyManager.getMaximumTimeToLock(componentName, i) > 0;
    }

    private static RestrictedLockUtils.EnforcedAdmin checkForLockSetting(Context context, int i, LockSettingCheck lockSettingCheck) {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService("device_policy");
        RestrictedLockUtils.EnforcedAdmin enforcedAdmin = null;
        if (devicePolicyManager == null) {
            return null;
        }
        LockPatternUtils lockPatternUtils = new LockPatternUtils(context);
        for (UserInfo userInfo : UserManager.get(context).getProfiles(i)) {
            List<ComponentName> activeAdminsAsUser = devicePolicyManager.getActiveAdminsAsUser(userInfo.id);
            if (activeAdminsAsUser != null) {
                UserHandle userHandleOf = getUserHandleOf(userInfo.id);
                boolean isSeparateProfileChallengeEnabled = sProxy.isSeparateProfileChallengeEnabled(lockPatternUtils, userInfo.id);
                for (ComponentName componentName : activeAdminsAsUser) {
                    if (isSeparateProfileChallengeEnabled || !lockSettingCheck.isEnforcing(devicePolicyManager, componentName, userInfo.id)) {
                        if (userInfo.isManagedProfile() && lockSettingCheck.isEnforcing(sProxy.getParentProfileInstance(devicePolicyManager, userInfo), componentName, userInfo.id)) {
                            if (enforcedAdmin != null) {
                                return RestrictedLockUtils.EnforcedAdmin.MULTIPLE_ENFORCED_ADMIN;
                            }
                            enforcedAdmin = new RestrictedLockUtils.EnforcedAdmin(componentName, userHandleOf);
                        }
                    } else if (enforcedAdmin != null) {
                        return RestrictedLockUtils.EnforcedAdmin.MULTIPLE_ENFORCED_ADMIN;
                    } else {
                        enforcedAdmin = new RestrictedLockUtils.EnforcedAdmin(componentName, userHandleOf);
                    }
                }
                continue;
            }
        }
        return enforcedAdmin;
    }

    public static RestrictedLockUtils.EnforcedAdmin getDeviceOwner(Context context) {
        return getDeviceOwner(context, (String) null);
    }

    private static RestrictedLockUtils.EnforcedAdmin getDeviceOwner(Context context, String str) {
        ComponentName deviceOwnerComponentOnAnyUser;
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService("device_policy");
        if (devicePolicyManager == null || (deviceOwnerComponentOnAnyUser = devicePolicyManager.getDeviceOwnerComponentOnAnyUser()) == null) {
            return null;
        }
        return new RestrictedLockUtils.EnforcedAdmin(deviceOwnerComponentOnAnyUser, str, devicePolicyManager.getDeviceOwnerUser());
    }

    private static RestrictedLockUtils.EnforcedAdmin getProfileOwner(Context context, int i) {
        return getProfileOwner(context, (String) null, i);
    }

    private static RestrictedLockUtils.EnforcedAdmin getProfileOwner(Context context, String str, int i) {
        DevicePolicyManager devicePolicyManager;
        ComponentName profileOwnerAsUser;
        if (i == -10000 || (devicePolicyManager = (DevicePolicyManager) context.getSystemService("device_policy")) == null || (profileOwnerAsUser = devicePolicyManager.getProfileOwnerAsUser(i)) == null) {
            return null;
        }
        return new RestrictedLockUtils.EnforcedAdmin(profileOwnerAsUser, str, getUserHandleOf(i));
    }

    public static void setMenuItemAsDisabledByAdmin(final Context context, MenuItem menuItem, final RestrictedLockUtils.EnforcedAdmin enforcedAdmin) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(menuItem.getTitle());
        removeExistingRestrictedSpans(spannableStringBuilder);
        if (enforcedAdmin != null) {
            spannableStringBuilder.setSpan(new ForegroundColorSpan(context.getColor(R$color.disabled_text_color)), 0, spannableStringBuilder.length(), 33);
            spannableStringBuilder.append(" ", new RestrictedLockImageSpan(context), 33);
            menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem menuItem) {
                    RestrictedLockUtils.sendShowAdminSupportDetailsIntent(context, enforcedAdmin);
                    return true;
                }
            });
        } else {
            menuItem.setOnMenuItemClickListener((MenuItem.OnMenuItemClickListener) null);
        }
        menuItem.setTitle(spannableStringBuilder);
    }

    private static void removeExistingRestrictedSpans(SpannableStringBuilder spannableStringBuilder) {
        int length = spannableStringBuilder.length();
        for (RestrictedLockImageSpan restrictedLockImageSpan : (RestrictedLockImageSpan[]) spannableStringBuilder.getSpans(length - 1, length, RestrictedLockImageSpan.class)) {
            int spanStart = spannableStringBuilder.getSpanStart(restrictedLockImageSpan);
            int spanEnd = spannableStringBuilder.getSpanEnd(restrictedLockImageSpan);
            spannableStringBuilder.removeSpan(restrictedLockImageSpan);
            spannableStringBuilder.delete(spanStart, spanEnd);
        }
        for (ForegroundColorSpan removeSpan : (ForegroundColorSpan[]) spannableStringBuilder.getSpans(0, length, ForegroundColorSpan.class)) {
            spannableStringBuilder.removeSpan(removeSpan);
        }
    }

    public static boolean isAdminInCurrentUserOrProfile(Context context, ComponentName componentName) {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService("device_policy");
        for (UserInfo userInfo : UserManager.get(context).getProfiles(UserHandle.myUserId())) {
            if (devicePolicyManager.isAdminActiveAsUser(componentName, userInfo.id)) {
                return true;
            }
        }
        return false;
    }

    public static void setTextViewAsDisabledByAdmin(Context context, TextView textView, boolean z) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(textView.getText());
        removeExistingRestrictedSpans(spannableStringBuilder);
        if (z) {
            spannableStringBuilder.setSpan(new ForegroundColorSpan(Utils.getDisabled(context, textView.getCurrentTextColor())), 0, spannableStringBuilder.length(), 33);
            textView.setCompoundDrawables((Drawable) null, (Drawable) null, getRestrictedPadlock(context), (Drawable) null);
            textView.setCompoundDrawablePadding(context.getResources().getDimensionPixelSize(R$dimen.restricted_icon_padding));
        } else {
            textView.setCompoundDrawables((Drawable) null, (Drawable) null, (Drawable) null, (Drawable) null);
        }
        textView.setText(spannableStringBuilder);
    }

    static class Proxy {
        Proxy() {
        }

        public boolean isSeparateProfileChallengeEnabled(LockPatternUtils lockPatternUtils, int i) {
            return lockPatternUtils.isSeparateProfileChallengeEnabled(i);
        }

        public DevicePolicyManager getParentProfileInstance(DevicePolicyManager devicePolicyManager, UserInfo userInfo) {
            return devicePolicyManager.getParentProfileInstance(userInfo);
        }
    }
}
