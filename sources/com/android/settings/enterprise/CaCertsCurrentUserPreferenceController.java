package com.android.settings.enterprise;

import android.content.Context;
import androidx.preference.Preference;
import com.android.settings.R;

public class CaCertsCurrentUserPreferenceController extends CaCertsPreferenceControllerBase {
    static final String CA_CERTS_CURRENT_USER = "ca_certs_current_user";

    public String getPreferenceKey() {
        return CA_CERTS_CURRENT_USER;
    }

    public CaCertsCurrentUserPreferenceController(Context context) {
        super(context);
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        preference.setTitle(this.mFeatureProvider.isInCompMode() ? R.string.enterprise_privacy_ca_certs_personal : R.string.enterprise_privacy_ca_certs_device);
    }

    /* access modifiers changed from: protected */
    public int getNumberOfCaCerts() {
        return this.mFeatureProvider.getNumberOfOwnerInstalledCaCertsForCurrentUser();
    }
}
