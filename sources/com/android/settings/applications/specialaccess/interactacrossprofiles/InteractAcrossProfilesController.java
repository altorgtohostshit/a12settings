package com.android.settings.applications.specialaccess.interactacrossprofiles;

import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.CrossProfileApps;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.os.UserHandle;
import android.os.UserManager;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;

public class InteractAcrossProfilesController extends BasePreferenceController {
    private final Context mContext;
    private final CrossProfileApps mCrossProfileApps;
    private final PackageManager mPackageManager;
    private final UserManager mUserManager;

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

    public InteractAcrossProfilesController(Context context, String str) {
        super(context, str);
        this.mContext = context;
        this.mUserManager = (UserManager) context.getSystemService(UserManager.class);
        this.mCrossProfileApps = (CrossProfileApps) context.getSystemService(CrossProfileApps.class);
        this.mPackageManager = context.getPackageManager();
    }

    public int getAvailabilityStatus() {
        for (UserInfo isManagedProfile : this.mUserManager.getProfiles(UserHandle.myUserId())) {
            if (isManagedProfile.isManagedProfile()) {
                return 0;
            }
        }
        return 4;
    }

    public CharSequence getSummary() {
        int numberOfEnabledApps = InteractAcrossProfilesSettings.getNumberOfEnabledApps(this.mContext, this.mPackageManager, this.mUserManager, this.mCrossProfileApps);
        if (numberOfEnabledApps == 0) {
            return this.mContext.getResources().getString(R.string.interact_across_profiles_number_of_connected_apps_none);
        }
        return this.mContext.getResources().getQuantityString(R.plurals.interact_across_profiles_number_of_connected_apps, numberOfEnabledApps, new Object[]{Integer.valueOf(numberOfEnabledApps)});
    }
}
