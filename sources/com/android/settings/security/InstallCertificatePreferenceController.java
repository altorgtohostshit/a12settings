package com.android.settings.security;

import android.content.Context;

public class InstallCertificatePreferenceController extends RestrictedEncryptionPreferenceController {
    public String getPreferenceKey() {
        return "install_certificate";
    }

    public InstallCertificatePreferenceController(Context context) {
        super(context, "no_config_credentials");
    }
}
