package com.android.settings.security;

import androidx.preference.Preference;

/* renamed from: com.android.settings.security.CredentialManagementAppPreferenceController$$ExternalSyntheticLambda1 */
public final /* synthetic */ class C1252xc202846e implements Runnable {
    public final /* synthetic */ CredentialManagementAppPreferenceController f$0;
    public final /* synthetic */ Preference f$1;

    public /* synthetic */ C1252xc202846e(CredentialManagementAppPreferenceController credentialManagementAppPreferenceController, Preference preference) {
        this.f$0 = credentialManagementAppPreferenceController;
        this.f$1 = preference;
    }

    public final void run() {
        this.f$0.lambda$updateState$1(this.f$1);
    }
}
