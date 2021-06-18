package com.android.settings.security;

import android.content.Context;

public class UserCredentialsPreferenceController extends RestrictedEncryptionPreferenceController {
    public String getPreferenceKey() {
        return "user_credentials";
    }

    public UserCredentialsPreferenceController(Context context) {
        super(context, "no_config_credentials");
    }
}
