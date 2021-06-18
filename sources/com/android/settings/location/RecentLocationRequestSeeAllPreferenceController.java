package com.android.settings.location;

import android.content.Context;
import android.content.IntentFilter;
import android.os.UserManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.location.RecentLocationApps;
import com.android.settingslib.widget.AppPreference;
import java.util.ArrayList;

public class RecentLocationRequestSeeAllPreferenceController extends LocationBasePreferenceController {
    private PreferenceScreen mCategoryAllRecentLocationRequests;
    private Preference mPreference;
    private RecentLocationApps mRecentLocationApps;
    private boolean mShowSystem = false;
    private int mType = 3;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public RecentLocationRequestSeeAllPreferenceController(Context context, String str) {
        super(context, str);
        this.mRecentLocationApps = new RecentLocationApps(context);
    }

    public void onLocationModeChanged(int i, boolean z) {
        this.mCategoryAllRecentLocationRequests.setEnabled(this.mLocationEnabler.isEnabled(i));
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mCategoryAllRecentLocationRequests = (PreferenceScreen) preferenceScreen.findPreference(getPreferenceKey());
    }

    public void updateState(Preference preference) {
        this.mCategoryAllRecentLocationRequests.removeAll();
        this.mPreference = preference;
        UserManager userManager = UserManager.get(this.mContext);
        ArrayList<RecentLocationApps.Request> arrayList = new ArrayList<>();
        for (RecentLocationApps.Request next : this.mRecentLocationApps.getAppListSorted(this.mShowSystem)) {
            if (RecentLocationRequestPreferenceController.isRequestMatchesProfileType(userManager, next, this.mType)) {
                arrayList.add(next);
            }
        }
        if (arrayList.isEmpty()) {
            AppPreference appPreference = new AppPreference(this.mContext);
            appPreference.setTitle((int) R.string.location_no_recent_apps);
            appPreference.setSelectable(false);
            this.mCategoryAllRecentLocationRequests.addPreference(appPreference);
            return;
        }
        for (RecentLocationApps.Request createAppPreference : arrayList) {
            this.mCategoryAllRecentLocationRequests.addPreference(RecentLocationRequestPreferenceController.createAppPreference(preference.getContext(), createAppPreference, this.mFragment));
        }
    }

    public void setProfileType(int i) {
        this.mType = i;
    }

    public void setShowSystem(boolean z) {
        this.mShowSystem = z;
        Preference preference = this.mPreference;
        if (preference != null) {
            updateState(preference);
        }
    }
}
