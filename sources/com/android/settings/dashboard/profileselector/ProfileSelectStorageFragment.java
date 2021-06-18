package com.android.settings.dashboard.profileselector;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.android.settings.deviceinfo.StorageDashboardFragment;

public class ProfileSelectStorageFragment extends ProfileSelectFragment {
    public Fragment[] getFragments() {
        Bundle bundle = new Bundle();
        bundle.putInt("profile", 2);
        StorageDashboardFragment storageDashboardFragment = new StorageDashboardFragment();
        storageDashboardFragment.setArguments(bundle);
        Bundle bundle2 = new Bundle();
        bundle2.putInt("profile", 1);
        StorageDashboardFragment storageDashboardFragment2 = new StorageDashboardFragment();
        storageDashboardFragment2.setArguments(bundle2);
        return new Fragment[]{storageDashboardFragment2, storageDashboardFragment};
    }
}
