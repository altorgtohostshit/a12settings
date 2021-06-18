package com.google.android.settings.security;

import android.os.SystemProperties;
import com.android.settings.security.SecuritySettingsFeatureProvider;

public class SecuritySettingsFeatureProviderGoogleImpl implements SecuritySettingsFeatureProvider {
    private final boolean mEnableSecurityHubShellDebug = SystemProperties.getBoolean("persist.debug.security_hub_enabled", false);

    public boolean hasAlternativeSecuritySettingsFragment() {
        return this.mEnableSecurityHubShellDebug;
    }

    public String getAlternativeSecuritySettingsFragmentClassname() {
        return SecurityHubDashboard.class.getName();
    }
}
