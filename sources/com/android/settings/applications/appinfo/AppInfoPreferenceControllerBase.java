package com.android.settings.applications.appinfo;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.applications.appinfo.AppInfoDashboardFragment;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;

public abstract class AppInfoPreferenceControllerBase extends BasePreferenceController implements AppInfoDashboardFragment.Callback {
    private final Class<? extends SettingsPreferenceFragment> mDetailFragmentClass = getDetailFragmentClass();
    protected AppInfoDashboardFragment mParent;
    protected Preference mPreference;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    /* access modifiers changed from: protected */
    public Bundle getArguments() {
        return null;
    }

    public int getAvailabilityStatus() {
        return 0;
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    /* access modifiers changed from: protected */
    public Class<? extends SettingsPreferenceFragment> getDetailFragmentClass() {
        return null;
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

    public AppInfoPreferenceControllerBase(Context context, String str) {
        super(context, str);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        Class<? extends SettingsPreferenceFragment> cls;
        if (!TextUtils.equals(preference.getKey(), this.mPreferenceKey) || (cls = this.mDetailFragmentClass) == null) {
            return false;
        }
        Bundle arguments = getArguments();
        AppInfoDashboardFragment appInfoDashboardFragment = this.mParent;
        AppInfoDashboardFragment.startAppInfoFragment(cls, -1, arguments, appInfoDashboardFragment, appInfoDashboardFragment.getAppEntry());
        return true;
    }

    public void refreshUi() {
        updateState(this.mPreference);
    }

    public void setParentFragment(AppInfoDashboardFragment appInfoDashboardFragment) {
        this.mParent = appInfoDashboardFragment;
        appInfoDashboardFragment.addToCallbackList(this);
    }
}
