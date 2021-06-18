package com.android.settings.biometrics.combination;

import android.content.Context;
import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;

public class CombinedBiometricProfileSettings extends BiometricsSettingsBase {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.security_settings_combined_biometric_profile);

    public String getFacePreferenceKey() {
        return "biometric_face_settings_profile";
    }

    public String getFingerprintPreferenceKey() {
        return "biometric_fingerprint_settings_profile";
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "BiometricProfileSetting";
    }

    public int getMetricsCategory() {
        return 1879;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.security_settings_combined_biometric_profile;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        ((BiometricSettingsAppPreferenceController) use(BiometricSettingsAppPreferenceController.class)).setUserId(this.mUserId);
    }
}
