package com.android.settings.security;

import android.content.Context;
import android.content.Intent;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.biometrics.combination.CombinedBiometricProfileStatusPreferenceController;
import com.android.settings.biometrics.combination.CombinedBiometricStatusPreferenceController;
import com.android.settings.biometrics.face.FaceProfileStatusPreferenceController;
import com.android.settings.biometrics.face.FaceStatusPreferenceController;
import com.android.settings.biometrics.fingerprint.FingerprintProfileStatusPreferenceController;
import com.android.settings.biometrics.fingerprint.FingerprintStatusPreferenceController;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.enterprise.EnterprisePrivacyPreferenceController;
import com.android.settings.enterprise.FinancedPrivacyPreferenceController;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.security.trustagent.ManageTrustAgentsPreferenceController;
import com.android.settings.security.trustagent.TrustAgentListPreferenceController;
import com.android.settings.widget.PreferenceCategoryController;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.ArrayList;
import java.util.List;

public class SecuritySettings extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.security_dashboard_settings) {
        public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
            return SecuritySettings.buildPreferenceControllers(context, (Lifecycle) null, (SecuritySettings) null);
        }
    };

    public int getHelpResource() {
        return R.string.help_url_security;
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "SecuritySettings";
    }

    public int getMetricsCategory() {
        return 87;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.security_dashboard_settings;
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return buildPreferenceControllers(context, getSettingsLifecycle(), this);
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (!((TrustAgentListPreferenceController) use(TrustAgentListPreferenceController.class)).handleActivityResult(i, i2) && !((LockUnificationPreferenceController) use(LockUnificationPreferenceController.class)).handleActivityResult(i, i2, intent)) {
            super.onActivityResult(i, i2, intent);
        }
    }

    /* access modifiers changed from: package-private */
    public void startUnification() {
        ((LockUnificationPreferenceController) use(LockUnificationPreferenceController.class)).startUnification();
    }

    /* access modifiers changed from: package-private */
    public void updateUnificationPreference() {
        ((LockUnificationPreferenceController) use(LockUnificationPreferenceController.class)).updateState((Preference) null);
    }

    /* access modifiers changed from: private */
    public static List<AbstractPreferenceController> buildPreferenceControllers(Context context, Lifecycle lifecycle, SecuritySettings securitySettings) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new EnterprisePrivacyPreferenceController(context));
        arrayList.add(new FinancedPrivacyPreferenceController(context));
        arrayList.add(new ManageTrustAgentsPreferenceController(context));
        arrayList.add(new ScreenPinningPreferenceController(context));
        arrayList.add(new SimLockPreferenceController(context));
        arrayList.add(new EncryptionStatusPreferenceController(context, EncryptionStatusPreferenceController.PREF_KEY_ENCRYPTION_SECURITY_PAGE));
        arrayList.add(new TrustAgentListPreferenceController(context, securitySettings, lifecycle));
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(new FaceStatusPreferenceController(context));
        arrayList2.add(new FingerprintStatusPreferenceController(context));
        arrayList2.add(new CombinedBiometricStatusPreferenceController(context));
        arrayList2.add(new ChangeScreenLockPreferenceController(context, securitySettings));
        arrayList.add(new PreferenceCategoryController(context, "security_category").setChildren(arrayList2));
        arrayList.addAll(arrayList2);
        ArrayList arrayList3 = new ArrayList();
        arrayList3.add(new ChangeProfileScreenLockPreferenceController(context, securitySettings));
        arrayList3.add(new LockUnificationPreferenceController(context, securitySettings));
        arrayList3.add(new VisiblePatternProfilePreferenceController(context, lifecycle));
        arrayList3.add(new FaceProfileStatusPreferenceController(context));
        arrayList3.add(new FingerprintProfileStatusPreferenceController(context));
        arrayList3.add(new CombinedBiometricProfileStatusPreferenceController(context));
        arrayList.add(new PreferenceCategoryController(context, "security_category_profile").setChildren(arrayList3));
        arrayList.addAll(arrayList3);
        return arrayList;
    }
}
