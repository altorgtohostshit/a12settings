package com.android.settings.enterprise;

import android.content.Context;
import android.os.UserHandle;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.applications.ApplicationFeatureProvider;
import com.android.settings.applications.EnterpriseDefaultApps;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.users.UserFeatureProvider;
import com.android.settingslib.core.AbstractPreferenceController;

public class EnterpriseSetDefaultAppsPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin {
    private final ApplicationFeatureProvider mApplicationFeatureProvider;
    private final UserFeatureProvider mUserFeatureProvider;

    public String getPreferenceKey() {
        return "number_enterprise_set_default_apps";
    }

    public EnterpriseSetDefaultAppsPreferenceController(Context context) {
        super(context);
        FeatureFactory factory = FeatureFactory.getFactory(context);
        this.mApplicationFeatureProvider = factory.getApplicationFeatureProvider(context);
        this.mUserFeatureProvider = factory.getUserFeatureProvider(context);
    }

    public void updateState(Preference preference) {
        int numberOfEnterpriseSetDefaultApps = getNumberOfEnterpriseSetDefaultApps();
        preference.setSummary((CharSequence) this.mContext.getResources().getQuantityString(R.plurals.enterprise_privacy_number_packages, numberOfEnterpriseSetDefaultApps, new Object[]{Integer.valueOf(numberOfEnterpriseSetDefaultApps)}));
    }

    public boolean isAvailable() {
        return getNumberOfEnterpriseSetDefaultApps() > 0;
    }

    private int getNumberOfEnterpriseSetDefaultApps() {
        int i = 0;
        for (UserHandle next : this.mUserFeatureProvider.getUserProfiles()) {
            for (EnterpriseDefaultApps intents : EnterpriseDefaultApps.values()) {
                i += this.mApplicationFeatureProvider.findPersistentPreferredActivities(next.getIdentifier(), intents.getIntents()).size();
            }
        }
        return i;
    }
}
