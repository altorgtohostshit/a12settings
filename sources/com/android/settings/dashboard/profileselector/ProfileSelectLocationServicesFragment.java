package com.android.settings.dashboard.profileselector;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.android.settings.location.LocationServices;
import com.android.settings.location.LocationServicesForWork;

public class ProfileSelectLocationServicesFragment extends ProfileSelectFragment {
    public Fragment[] getFragments() {
        Bundle bundle = new Bundle();
        bundle.putInt("profile", 2);
        LocationServicesForWork locationServicesForWork = new LocationServicesForWork();
        locationServicesForWork.setArguments(bundle);
        Bundle bundle2 = new Bundle();
        bundle2.putInt("profile", 1);
        LocationServices locationServices = new LocationServices();
        locationServices.setArguments(bundle2);
        return new Fragment[]{locationServices, locationServicesForWork};
    }
}
