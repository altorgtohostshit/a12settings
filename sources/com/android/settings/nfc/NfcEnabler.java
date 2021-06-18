package com.android.settings.nfc;

import android.content.Context;
import android.provider.Settings;
import com.android.settingslib.widget.MainSwitchPreference;

public class NfcEnabler extends BaseNfcEnabler {
    private final MainSwitchPreference mPreference;

    public NfcEnabler(Context context, MainSwitchPreference mainSwitchPreference) {
        super(context);
        this.mPreference = mainSwitchPreference;
    }

    /* access modifiers changed from: protected */
    public void handleNfcStateChanged(int i) {
        if (i == 1) {
            this.mPreference.updateStatus(false);
            this.mPreference.setEnabled(isToggleable());
        } else if (i == 2) {
            this.mPreference.updateStatus(true);
            this.mPreference.setEnabled(false);
        } else if (i == 3) {
            this.mPreference.updateStatus(true);
            this.mPreference.setEnabled(true);
        } else if (i == 4) {
            this.mPreference.updateStatus(false);
            this.mPreference.setEnabled(false);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isToggleable() {
        if (NfcPreferenceController.isToggleableInAirplaneMode(this.mContext) || !NfcPreferenceController.shouldTurnOffNFCInAirplaneMode(this.mContext) || Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0) != 1) {
            return true;
        }
        return false;
    }
}
