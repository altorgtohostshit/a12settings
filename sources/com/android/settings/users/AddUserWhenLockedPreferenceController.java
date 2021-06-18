package com.android.settings.users;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import androidx.preference.Preference;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.RestrictedSwitchPreference;

public class AddUserWhenLockedPreferenceController extends TogglePreferenceController {
    private final UserCapabilities mUserCaps;

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

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public AddUserWhenLockedPreferenceController(Context context, String str) {
        super(context, str);
        this.mUserCaps = UserCapabilities.create(context);
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        this.mUserCaps.updateAddUserCapabilities(this.mContext);
        RestrictedSwitchPreference restrictedSwitchPreference = (RestrictedSwitchPreference) preference;
        if (!isAvailable()) {
            restrictedSwitchPreference.setVisible(false);
            return;
        }
        restrictedSwitchPreference.setDisabledByAdmin(this.mUserCaps.disallowAddUser() ? this.mUserCaps.getEnforcedAdmin() : null);
        restrictedSwitchPreference.setVisible(this.mUserCaps.mUserSwitcherEnabled);
    }

    public int getAvailabilityStatus() {
        if (this.mUserCaps.isAdmin() && !this.mUserCaps.disallowAddUser() && !this.mUserCaps.disallowAddUserSetByAdmin()) {
            return this.mUserCaps.mUserSwitcherEnabled ? 0 : 2;
        }
        return 4;
    }

    public boolean isChecked() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "add_users_when_locked", 0) == 1;
    }

    public boolean setChecked(boolean z) {
        return Settings.Global.putInt(this.mContext.getContentResolver(), "add_users_when_locked", z ? 1 : 0);
    }
}
