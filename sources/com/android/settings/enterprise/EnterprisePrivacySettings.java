package com.android.settings.enterprise;

import android.content.Context;
import android.provider.SearchIndexableResource;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.List;

public class EnterprisePrivacySettings extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider() {
        private PrivacySettingsPreference mPrivacySettingsPreference;

        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            return EnterprisePrivacySettings.isPageEnabled(context);
        }

        public List<SearchIndexableResource> getXmlResourcesToIndex(Context context, boolean z) {
            PrivacySettingsPreference createPrivacySettingsPreference = PrivacySettingsPreferenceFactory.createPrivacySettingsPreference(context);
            this.mPrivacySettingsPreference = createPrivacySettingsPreference;
            return createPrivacySettingsPreference.getXmlResourcesToIndex();
        }

        public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
            PrivacySettingsPreference createPrivacySettingsPreference = PrivacySettingsPreferenceFactory.createPrivacySettingsPreference(context);
            this.mPrivacySettingsPreference = createPrivacySettingsPreference;
            return createPrivacySettingsPreference.createPreferenceControllers(false);
        }
    };
    @VisibleForTesting
    PrivacySettingsPreference mPrivacySettingsPreference;

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "EnterprisePrivacySettings";
    }

    public int getMetricsCategory() {
        return 628;
    }

    public void onAttach(Context context) {
        this.mPrivacySettingsPreference = PrivacySettingsPreferenceFactory.createPrivacySettingsPreference(context);
        super.onAttach(context);
    }

    public void onDetach() {
        this.mPrivacySettingsPreference = null;
        super.onDetach();
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return this.mPrivacySettingsPreference.getPreferenceScreenResId();
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return this.mPrivacySettingsPreference.createPreferenceControllers(true);
    }

    public static boolean isPageEnabled(Context context) {
        return FeatureFactory.getFactory(context).getEnterprisePrivacyFeatureProvider(context).hasDeviceOwner();
    }
}
