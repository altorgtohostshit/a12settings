package com.android.settings.location;

import android.content.Context;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;

public class LocationPersonalSettings extends DashboardFragment {
    public int getHelpResource() {
        return R.string.help_url_location_access;
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "LocationPersonal";
    }

    public int getMetricsCategory() {
        return 63;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.location_settings_personal;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        ((AppLocationPermissionPreferenceController) use(AppLocationPermissionPreferenceController.class)).init(this);
        ((LocationSettingsFooterPreferenceController) use(LocationSettingsFooterPreferenceController.class)).init(this);
        ((RecentLocationAccessSeeAllButtonPreferenceController) use(RecentLocationAccessSeeAllButtonPreferenceController.class)).init(this);
        int i = getArguments().getInt("profile");
        RecentLocationAccessPreferenceController recentLocationAccessPreferenceController = (RecentLocationAccessPreferenceController) use(RecentLocationAccessPreferenceController.class);
        recentLocationAccessPreferenceController.init(this);
        recentLocationAccessPreferenceController.setProfileType(i);
    }
}
