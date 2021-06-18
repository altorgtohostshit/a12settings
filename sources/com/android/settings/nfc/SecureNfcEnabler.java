package com.android.settings.nfc;

import android.content.Context;
import androidx.preference.SwitchPreference;
import com.android.settings.R;

public class SecureNfcEnabler extends BaseNfcEnabler {
    private final SwitchPreference mPreference;

    public SecureNfcEnabler(Context context, SwitchPreference switchPreference) {
        super(context);
        this.mPreference = switchPreference;
    }

    /* access modifiers changed from: protected */
    public void handleNfcStateChanged(int i) {
        if (i == 1) {
            this.mPreference.setSummary((int) R.string.nfc_disabled_summary);
            this.mPreference.setEnabled(false);
        } else if (i == 2) {
            this.mPreference.setEnabled(false);
        } else if (i == 3) {
            this.mPreference.setSummary((int) R.string.nfc_secure_toggle_summary);
            SwitchPreference switchPreference = this.mPreference;
            switchPreference.setChecked(switchPreference.isChecked());
            this.mPreference.setEnabled(true);
        } else if (i == 4) {
            this.mPreference.setEnabled(false);
        }
    }
}
