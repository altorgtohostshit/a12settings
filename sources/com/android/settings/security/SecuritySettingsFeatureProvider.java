package com.android.settings.security;

public interface SecuritySettingsFeatureProvider {
    String getAlternativeSecuritySettingsFragmentClassname();

    boolean hasAlternativeSecuritySettingsFragment();
}
