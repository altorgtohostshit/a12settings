package com.android.settings.enterprise;

import android.provider.SearchIndexableResource;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.List;

public interface PrivacySettingsPreference {
    List<AbstractPreferenceController> createPreferenceControllers(boolean z);

    int getPreferenceScreenResId();

    List<SearchIndexableResource> getXmlResourcesToIndex();
}
