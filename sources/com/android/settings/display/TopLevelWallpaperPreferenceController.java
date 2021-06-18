package com.android.settings.display;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.FeatureFlagUtils;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.homepage.RestrictedHomepagePreference;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.RestrictedTopLevelPreference;
import java.util.List;

public class TopLevelWallpaperPreferenceController extends BasePreferenceController {
    private static final String LAUNCHED_SETTINGS = "app_launched_settings";
    private static final String TAG = "TopLevelWallpaperPreferenceController";
    private final String mStylesAndWallpaperClass = this.mContext.getString(R.string.config_styles_and_wallpaper_picker_class);
    private final String mWallpaperClass = this.mContext.getString(R.string.config_wallpaper_picker_class);
    private final String mWallpaperLaunchExtra = this.mContext.getString(R.string.config_wallpaper_picker_launch_extra);
    private final String mWallpaperPackage = this.mContext.getString(R.string.config_wallpaper_picker_package);

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

    public TopLevelWallpaperPreferenceController(Context context, String str) {
        super(context, str);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        preferenceScreen.findPreference(getPreferenceKey()).setTitle((CharSequence) getTitle());
    }

    public String getTitle() {
        return this.mContext.getString(areStylesAvailable() ? R.string.style_and_wallpaper_settings_title : R.string.wallpaper_settings_title);
    }

    public ComponentName getComponentName() {
        return new ComponentName(this.mWallpaperPackage, getComponentClassString());
    }

    public String getComponentClassString() {
        return areStylesAvailable() ? this.mStylesAndWallpaperClass : this.mWallpaperClass;
    }

    public int getAvailabilityStatus() {
        if ((!TextUtils.isEmpty(this.mWallpaperClass) || !TextUtils.isEmpty(this.mStylesAndWallpaperClass)) && !TextUtils.isEmpty(this.mWallpaperPackage)) {
            return canResolveWallpaperComponent(getComponentClassString()) ? 1 : 2;
        }
        Log.e(TAG, "No Wallpaper picker specified!");
        return 3;
    }

    public void updateState(Preference preference) {
        if (FeatureFlagUtils.isEnabled(this.mContext, "settings_silky_home")) {
            disablePreferenceIfManaged((RestrictedHomepagePreference) preference);
        } else {
            disablePreferenceIfManaged((RestrictedTopLevelPreference) preference);
        }
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!getPreferenceKey().equals(preference.getKey())) {
            return super.handlePreferenceTreeClick(preference);
        }
        Intent putExtra = new Intent().setComponent(getComponentName()).putExtra(this.mWallpaperLaunchExtra, LAUNCHED_SETTINGS);
        if (areStylesAvailable()) {
            putExtra.setFlags(268468224);
        }
        preference.getContext().startActivity(putExtra);
        return true;
    }

    public boolean areStylesAvailable() {
        return !TextUtils.isEmpty(this.mStylesAndWallpaperClass) && canResolveWallpaperComponent(this.mStylesAndWallpaperClass);
    }

    private boolean canResolveWallpaperComponent(String str) {
        List<ResolveInfo> queryIntentActivities = this.mContext.getPackageManager().queryIntentActivities(new Intent().setComponent(new ComponentName(this.mWallpaperPackage, str)), 0);
        if (queryIntentActivities == null || queryIntentActivities.isEmpty()) {
            return false;
        }
        return true;
    }

    private void disablePreferenceIfManaged(RestrictedTopLevelPreference restrictedTopLevelPreference) {
        if (restrictedTopLevelPreference != null) {
            restrictedTopLevelPreference.setDisabledByAdmin((RestrictedLockUtils.EnforcedAdmin) null);
            if (RestrictedLockUtilsInternal.hasBaseUserRestriction(this.mContext, "no_set_wallpaper", UserHandle.myUserId())) {
                restrictedTopLevelPreference.setEnabled(false);
            } else {
                restrictedTopLevelPreference.checkRestrictionAndSetDisabled("no_set_wallpaper");
            }
        }
    }

    private void disablePreferenceIfManaged(RestrictedHomepagePreference restrictedHomepagePreference) {
        if (restrictedHomepagePreference != null) {
            restrictedHomepagePreference.setDisabledByAdmin((RestrictedLockUtils.EnforcedAdmin) null);
            if (RestrictedLockUtilsInternal.hasBaseUserRestriction(this.mContext, "no_set_wallpaper", UserHandle.myUserId())) {
                restrictedHomepagePreference.setEnabled(false);
            } else {
                restrictedHomepagePreference.checkRestrictionAndSetDisabled("no_set_wallpaper");
            }
        }
    }
}
