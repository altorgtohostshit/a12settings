package com.android.settings.applications.specialaccess.interactacrossprofiles;

import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.CrossProfileApps;
import androidx.preference.Preference;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.applications.appinfo.AppInfoPreferenceControllerBase;
import com.android.settings.slices.SliceBackgroundWorker;

public class InteractAcrossProfilesDetailsPreferenceController extends AppInfoPreferenceControllerBase {
    private String mPackageName;

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

    public InteractAcrossProfilesDetailsPreferenceController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        return canUserAttemptToConfigureInteractAcrossProfiles() ? 0 : 4;
    }

    public void updateState(Preference preference) {
        preference.setSummary(getPreferenceSummary());
    }

    /* access modifiers changed from: protected */
    public Class<? extends SettingsPreferenceFragment> getDetailFragmentClass() {
        return InteractAcrossProfilesDetails.class;
    }

    private CharSequence getPreferenceSummary() {
        return InteractAcrossProfilesDetails.getPreferenceSummary(this.mContext, this.mPackageName);
    }

    private boolean canUserAttemptToConfigureInteractAcrossProfiles() {
        return ((CrossProfileApps) this.mContext.getSystemService(CrossProfileApps.class)).canUserAttemptToConfigureInteractAcrossProfiles(this.mPackageName);
    }

    public void setPackageName(String str) {
        this.mPackageName = str;
    }
}
