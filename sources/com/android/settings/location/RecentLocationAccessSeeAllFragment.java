package com.android.settings.location;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;

public class RecentLocationAccessSeeAllFragment extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.location_recent_access_see_all);
    private RecentLocationAccessSeeAllPreferenceController mController;
    private MenuItem mHideSystemMenu;
    private boolean mShowSystem = false;
    private MenuItem mShowSystemMenu;

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "RecentLocAccessSeeAll";
    }

    public int getMetricsCategory() {
        return 1325;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.location_recent_access_see_all;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        int i = getArguments().getInt("profile");
        RecentLocationAccessSeeAllPreferenceController recentLocationAccessSeeAllPreferenceController = (RecentLocationAccessSeeAllPreferenceController) use(RecentLocationAccessSeeAllPreferenceController.class);
        this.mController = recentLocationAccessSeeAllPreferenceController;
        recentLocationAccessSeeAllPreferenceController.init(this);
        if (i != 0) {
            this.mController.setProfileType(i);
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            this.mShowSystem = bundle.getBoolean("show_system", this.mShowSystem);
        }
        RecentLocationAccessSeeAllPreferenceController recentLocationAccessSeeAllPreferenceController = this.mController;
        if (recentLocationAccessSeeAllPreferenceController != null) {
            recentLocationAccessSeeAllPreferenceController.setShowSystem(this.mShowSystem);
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("show_system", this.mShowSystem);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId != 2 && itemId != 3) {
            return super.onOptionsItemSelected(menuItem);
        }
        this.mShowSystem = menuItem.getItemId() == 2;
        updateMenu();
        RecentLocationAccessSeeAllPreferenceController recentLocationAccessSeeAllPreferenceController = this.mController;
        if (recentLocationAccessSeeAllPreferenceController != null) {
            recentLocationAccessSeeAllPreferenceController.setShowSystem(this.mShowSystem);
        }
        return true;
    }

    private void updateMenu() {
        this.mShowSystemMenu.setVisible(!this.mShowSystem);
        this.mHideSystemMenu.setVisible(this.mShowSystem);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        this.mShowSystemMenu = menu.add(0, 2, 0, R.string.menu_show_system);
        this.mHideSystemMenu = menu.add(0, 3, 0, R.string.menu_hide_system);
        updateMenu();
    }
}
