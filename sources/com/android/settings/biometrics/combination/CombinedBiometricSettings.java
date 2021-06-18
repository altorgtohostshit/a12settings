package com.android.settings.biometrics.combination;

import android.content.Context;
import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;

public class CombinedBiometricSettings extends BiometricsSettingsBase {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.security_settings_combined_biometric);

    public String getFacePreferenceKey() {
        return "biometric_face_settings";
    }

    public String getFingerprintPreferenceKey() {
        return "biometric_fingerprint_settings";
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "BiometricSettings";
    }

    public int getMetricsCategory() {
        return 1878;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.security_settings_combined_biometric;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        ((BiometricSettingsKeyguardPreferenceController) use(BiometricSettingsKeyguardPreferenceController.class)).setUserId(this.mUserId);
        ((BiometricSettingsAppPreferenceController) use(BiometricSettingsAppPreferenceController.class)).setUserId(this.mUserId);
    }
}
