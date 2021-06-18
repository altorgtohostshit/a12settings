package com.android.settings.location;

import android.content.Context;
import android.content.IntentFilter;
import android.os.UserManager;
import android.text.TextUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedSwitchPreference;

public class LocationForWorkPreferenceController extends LocationBasePreferenceController {
    private RestrictedSwitchPreference mPreference;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public int getAvailabilityStatus() {
        return 0;
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

    public LocationForWorkPreferenceController(Context context, String str) {
        super(context, str);
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!TextUtils.equals(preference.getKey(), getPreferenceKey())) {
            return false;
        }
        boolean isChecked = this.mPreference.isChecked();
        UserManager userManager = this.mUserManager;
        userManager.setUserRestriction("no_share_location", !isChecked, Utils.getManagedProfile(userManager));
        this.mPreference.setSummary(isChecked ? R.string.switch_on_text : R.string.switch_off_text);
        return true;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = (RestrictedSwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
    }

    public void onLocationModeChanged(int i, boolean z) {
        int i2;
        if (this.mPreference.isVisible() && isAvailable()) {
            RestrictedLockUtils.EnforcedAdmin shareLocationEnforcedAdmin = this.mLocationEnabler.getShareLocationEnforcedAdmin(Utils.getManagedProfile(this.mUserManager).getIdentifier());
            if (shareLocationEnforcedAdmin != null) {
                this.mPreference.setDisabledByAdmin(shareLocationEnforcedAdmin);
                return;
            }
            boolean isEnabled = this.mLocationEnabler.isEnabled(i);
            this.mPreference.setEnabled(isEnabled);
            if (this.mLocationEnabler.isManagedProfileRestrictedByBase() || !isEnabled) {
                this.mPreference.setChecked(false);
                i2 = isEnabled ? R.string.switch_off_text : R.string.location_app_permission_summary_location_off;
            } else {
                this.mPreference.setChecked(true);
                i2 = R.string.switch_on_text;
            }
            this.mPreference.setSummary(i2);
        }
    }
}
