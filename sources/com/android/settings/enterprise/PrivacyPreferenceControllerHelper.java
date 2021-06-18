package com.android.settings.enterprise;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.overlay.FeatureFactory;
import java.util.Objects;

class PrivacyPreferenceControllerHelper {
    private final Context mContext;
    private final DevicePolicyManager mDevicePolicyManager;
    private final EnterprisePrivacyFeatureProvider mFeatureProvider;

    PrivacyPreferenceControllerHelper(Context context) {
        Objects.requireNonNull(context);
        this.mContext = context;
        this.mFeatureProvider = FeatureFactory.getFactory(context).getEnterprisePrivacyFeatureProvider(context);
        this.mDevicePolicyManager = (DevicePolicyManager) context.getSystemService(DevicePolicyManager.class);
    }

    /* access modifiers changed from: package-private */
    public void updateState(Preference preference) {
        if (preference != null) {
            String deviceOwnerOrganizationName = this.mFeatureProvider.getDeviceOwnerOrganizationName();
            if (deviceOwnerOrganizationName == null) {
                preference.setSummary((int) R.string.enterprise_privacy_settings_summary_generic);
                return;
            }
            preference.setSummary((CharSequence) this.mContext.getResources().getString(R.string.enterprise_privacy_settings_summary_with_name, new Object[]{deviceOwnerOrganizationName}));
        }
    }

    /* access modifiers changed from: package-private */
    public boolean hasDeviceOwner() {
        return this.mFeatureProvider.hasDeviceOwner();
    }

    /* access modifiers changed from: package-private */
    public boolean isFinancedDevice() {
        if (this.mDevicePolicyManager.isDeviceManaged()) {
            DevicePolicyManager devicePolicyManager = this.mDevicePolicyManager;
            if (devicePolicyManager.getDeviceOwnerType(devicePolicyManager.getDeviceOwnerComponentOnAnyUser()) == 1) {
                return true;
            }
        }
        return false;
    }
}
