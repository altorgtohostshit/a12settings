package com.android.settings.enterprise;

import android.app.admin.DevicePolicyManager;
import android.content.Context;

public class PrivacySettingsPreferenceFactory {
    public static PrivacySettingsPreference createPrivacySettingsPreference(Context context) {
        if (isFinancedDevice(context)) {
            return createPrivacySettingsFinancedPreference(context);
        }
        return createPrivacySettingsEnterprisePreference(context);
    }

    private static PrivacySettingsEnterprisePreference createPrivacySettingsEnterprisePreference(Context context) {
        return new PrivacySettingsEnterprisePreference(context);
    }

    private static PrivacySettingsFinancedPreference createPrivacySettingsFinancedPreference(Context context) {
        return new PrivacySettingsFinancedPreference(context);
    }

    private static boolean isFinancedDevice(Context context) {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(DevicePolicyManager.class);
        return devicePolicyManager.isDeviceManaged() && devicePolicyManager.getDeviceOwnerType(devicePolicyManager.getDeviceOwnerComponentOnAnyUser()) == 1;
    }
}
