package com.android.settings.development;

import android.content.Context;
import androidx.preference.Preference;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.development.AbstractLogdSizePreferenceController;

public class LogdSizePreferenceController extends AbstractLogdSizePreferenceController implements PreferenceControllerMixin {
    public LogdSizePreferenceController(Context context) {
        super(context);
    }

    public void updateState(Preference preference) {
        updateLogdSizeValues();
    }

    /* access modifiers changed from: protected */
    public void onDeveloperOptionsSwitchDisabled() {
        super.onDeveloperOptionsSwitchDisabled();
        writeLogdSizeOption((Object) null);
    }
}
