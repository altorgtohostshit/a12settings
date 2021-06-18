package com.android.settings.enterprise;

import android.content.Context;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.AbstractPreferenceController;

public abstract class CaCertsPreferenceControllerBase extends AbstractPreferenceController implements PreferenceControllerMixin {
    protected final EnterprisePrivacyFeatureProvider mFeatureProvider;

    /* access modifiers changed from: protected */
    public abstract int getNumberOfCaCerts();

    public CaCertsPreferenceControllerBase(Context context) {
        super(context);
        this.mFeatureProvider = FeatureFactory.getFactory(context).getEnterprisePrivacyFeatureProvider(context);
    }

    public void updateState(Preference preference) {
        int numberOfCaCerts = getNumberOfCaCerts();
        preference.setSummary((CharSequence) this.mContext.getResources().getQuantityString(R.plurals.enterprise_privacy_number_ca_certs, numberOfCaCerts, new Object[]{Integer.valueOf(numberOfCaCerts)}));
    }

    public boolean isAvailable() {
        return getNumberOfCaCerts() > 0;
    }
}
