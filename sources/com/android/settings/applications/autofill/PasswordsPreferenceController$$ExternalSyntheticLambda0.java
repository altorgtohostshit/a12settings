package com.android.settings.applications.autofill;

import androidx.lifecycle.Observer;
import com.android.settingslib.widget.AppPreference;

public final /* synthetic */ class PasswordsPreferenceController$$ExternalSyntheticLambda0 implements Observer {
    public final /* synthetic */ PasswordsPreferenceController f$0;
    public final /* synthetic */ AppPreference f$1;

    public /* synthetic */ PasswordsPreferenceController$$ExternalSyntheticLambda0(PasswordsPreferenceController passwordsPreferenceController, AppPreference appPreference) {
        this.f$0 = passwordsPreferenceController;
        this.f$1 = appPreference;
    }

    public final void onChanged(Object obj) {
        this.f$0.lambda$addPasswordPreferences$0(this.f$1, (Integer) obj);
    }
}
