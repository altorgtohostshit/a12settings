package com.android.settings.security;

import androidx.preference.PreferenceScreen;
import com.android.settingslib.RestrictedPreference;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnResume;
import java.security.KeyStore;
import java.security.KeyStoreException;

public class ResetCredentialsPreferenceController extends RestrictedEncryptionPreferenceController implements LifecycleObserver, OnResume {
    private final KeyStore mKeyStore;
    private RestrictedPreference mPreference;

    public String getPreferenceKey() {
        return "credentials_reset";
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0016  */
    /* JADX WARNING: Removed duplicated region for block: B:13:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ResetCredentialsPreferenceController(android.content.Context r2, com.android.settingslib.core.lifecycle.Lifecycle r3) {
        /*
            r1 = this;
            java.lang.String r0 = "no_config_credentials"
            r1.<init>(r2, r0)
            r2 = 0
            java.lang.String r0 = "AndroidKeyStore"
            java.security.KeyStore r0 = java.security.KeyStore.getInstance(r0)     // Catch:{ Exception -> 0x0011 }
            r0.load(r2)     // Catch:{ Exception -> 0x0010 }
            goto L_0x0012
        L_0x0010:
            r2 = r0
        L_0x0011:
            r0 = r2
        L_0x0012:
            r1.mKeyStore = r0
            if (r3 == 0) goto L_0x0019
            r3.addObserver(r1)
        L_0x0019:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.security.ResetCredentialsPreferenceController.<init>(android.content.Context, com.android.settingslib.core.lifecycle.Lifecycle):void");
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = (RestrictedPreference) preferenceScreen.findPreference(getPreferenceKey());
    }

    public void onResume() {
        RestrictedPreference restrictedPreference = this.mPreference;
        if (restrictedPreference != null && !restrictedPreference.isDisabledByAdmin()) {
            boolean z = false;
            try {
                KeyStore keyStore = this.mKeyStore;
                if (keyStore != null) {
                    z = keyStore.aliases().hasMoreElements();
                }
            } catch (KeyStoreException unused) {
            }
            this.mPreference.setEnabled(z);
        }
    }
}
