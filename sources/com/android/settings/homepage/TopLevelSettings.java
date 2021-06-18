package com.android.settings.homepage;

import android.content.Context;
import android.os.Bundle;
import android.provider.SearchIndexableResource;
import android.util.FeatureFlagUtils;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import com.android.settings.R;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.support.SupportPreferenceController;
import com.android.settingslib.core.instrumentation.Instrumentable;
import java.util.Arrays;
import java.util.List;

public class TopLevelSettings extends DashboardFragment implements PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider() {
        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            return false;
        }

        public List<SearchIndexableResource> getXmlResourcesToIndex(Context context, boolean z) {
            SearchIndexableResource searchIndexableResource = new SearchIndexableResource(context);
            searchIndexableResource.xmlResId = FeatureFlagUtils.isEnabled(context, "settings_silky_home") ? R.xml.top_level_settings_grouped : R.xml.top_level_settings;
            return Arrays.asList(new SearchIndexableResource[]{searchIndexableResource});
        }
    };

    public Fragment getCallbackFragment() {
        return this;
    }

    public int getHelpResource() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "TopLevelSettings";
    }

    public int getMetricsCategory() {
        return 35;
    }

    public TopLevelSettings() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("need_search_icon_in_action_bar", false);
        setArguments(bundle);
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return FeatureFlagUtils.isEnabled(getContext(), "settings_silky_home") ? R.xml.top_level_settings_grouped : R.xml.top_level_settings;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        ((SupportPreferenceController) use(SupportPreferenceController.class)).setActivity(getActivity());
    }

    public boolean onPreferenceStartFragment(PreferenceFragmentCompat preferenceFragmentCompat, Preference preference) {
        new SubSettingLauncher(getActivity()).setDestination(preference.getFragment()).setArguments(preference.getExtras()).setSourceMetricsCategory(preferenceFragmentCompat instanceof Instrumentable ? ((Instrumentable) preferenceFragmentCompat).getMetricsCategory() : 0).setTitleRes(-1).launch();
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean shouldForceRoundedIcon() {
        return getContext().getResources().getBoolean(R.bool.config_force_rounded_icon_TopLevelSettings);
    }
}
