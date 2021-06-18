package com.android.settings.enterprise;

import android.content.Context;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.applications.ApplicationFeatureProvider;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.AbstractPreferenceController;

public class EnterpriseInstalledPackagesPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin {
    private final boolean mAsync;
    private final ApplicationFeatureProvider mFeatureProvider;

    public String getPreferenceKey() {
        return "number_enterprise_installed_packages";
    }

    public EnterpriseInstalledPackagesPreferenceController(Context context, boolean z) {
        super(context);
        this.mFeatureProvider = FeatureFactory.getFactory(context).getApplicationFeatureProvider(context);
        this.mAsync = z;
    }

    public void updateState(Preference preference) {
        this.mFeatureProvider.calculateNumberOfPolicyInstalledApps(true, new C0912xc1662b8c(this, preference));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateState$0(Preference preference, int i) {
        boolean z = false;
        if (i != 0) {
            preference.setSummary((CharSequence) this.mContext.getResources().getQuantityString(R.plurals.enterprise_privacy_number_packages_lower_bound, i, new Object[]{Integer.valueOf(i)}));
            z = true;
        }
        preference.setVisible(z);
    }

    public boolean isAvailable() {
        if (this.mAsync) {
            return true;
        }
        Boolean[] boolArr = {null};
        this.mFeatureProvider.calculateNumberOfPolicyInstalledApps(false, new C0913xc1662b8d(boolArr));
        return boolArr[0].booleanValue();
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$isAvailable$1(Boolean[] boolArr, int i) {
        boolArr[0] = Boolean.valueOf(i > 0);
    }
}
