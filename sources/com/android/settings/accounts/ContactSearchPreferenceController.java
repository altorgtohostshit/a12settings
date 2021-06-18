package com.android.settings.accounts;

import android.content.Context;
import android.content.IntentFilter;
import android.os.UserHandle;
import android.provider.Settings;
import androidx.preference.Preference;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.RestrictedSwitchPreference;

public class ContactSearchPreferenceController extends BasePreferenceController implements Preference.OnPreferenceChangeListener {
    private UserHandle mManagedUser;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public int getSliceType() {
        return 1;
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

    public ContactSearchPreferenceController(Context context, String str) {
        super(context, str);
    }

    public void setManagedUser(UserHandle userHandle) {
        this.mManagedUser = userHandle;
    }

    public int getAvailabilityStatus() {
        return this.mManagedUser != null ? 0 : 4;
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        if (preference instanceof RestrictedSwitchPreference) {
            RestrictedSwitchPreference restrictedSwitchPreference = (RestrictedSwitchPreference) preference;
            restrictedSwitchPreference.setChecked(isChecked());
            UserHandle userHandle = this.mManagedUser;
            if (userHandle != null) {
                restrictedSwitchPreference.setDisabledByAdmin(RestrictedLockUtilsInternal.checkIfRemoteContactSearchDisallowed(this.mContext, userHandle.getIdentifier()));
            }
        }
    }

    private boolean isChecked() {
        if (this.mManagedUser == null || Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "managed_profile_contact_remote_search", 0, this.mManagedUser.getIdentifier()) == 0) {
            return false;
        }
        return true;
    }

    private boolean setChecked(boolean z) {
        if (this.mManagedUser == null) {
            return true;
        }
        Settings.Secure.putIntForUser(this.mContext.getContentResolver(), "managed_profile_contact_remote_search", z ? 1 : 0, this.mManagedUser.getIdentifier());
        return true;
    }

    public final boolean onPreferenceChange(Preference preference, Object obj) {
        return setChecked(((Boolean) obj).booleanValue());
    }
}
