package com.android.settings.enterprise;

import android.content.Context;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.AbstractPreferenceController;

public abstract class FailedPasswordWipePreferenceControllerBase extends AbstractPreferenceController implements PreferenceControllerMixin {
    protected final EnterprisePrivacyFeatureProvider mFeatureProvider;

    /* access modifiers changed from: protected */
    public abstract int getMaximumFailedPasswordsBeforeWipe();

    public FailedPasswordWipePreferenceControllerBase(Context context) {
        super(context);
        this.mFeatureProvider = FeatureFactory.getFactory(context).getEnterprisePrivacyFeatureProvider(context);
    }

    public void updateState(Preference preference) {
        int maximumFailedPasswordsBeforeWipe = getMaximumFailedPasswordsBeforeWipe();
        preference.setSummary((CharSequence) this.mContext.getResources().getQuantityString(R.plurals.enterprise_privacy_number_failed_password_wipe, maximumFailedPasswordsBeforeWipe, new Object[]{Integer.valueOf(maximumFailedPasswordsBeforeWipe)}));
    }

    public boolean isAvailable() {
        return getMaximumFailedPasswordsBeforeWipe() > 0;
    }
}
