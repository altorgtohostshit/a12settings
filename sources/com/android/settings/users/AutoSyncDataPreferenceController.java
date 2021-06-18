package com.android.settings.users;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Process;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;
import com.android.settings.R;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;
import com.android.settingslib.core.AbstractPreferenceController;

public class AutoSyncDataPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin {
    private final Fragment mParentFragment;
    protected UserHandle mUserHandle = Process.myUserHandle();
    protected final UserManager mUserManager;

    public String getPreferenceKey() {
        return "auto_sync_account_data";
    }

    public AutoSyncDataPreferenceController(Context context, Fragment fragment) {
        super(context);
        this.mUserManager = (UserManager) context.getSystemService("user");
        this.mParentFragment = fragment;
    }

    public void updateState(Preference preference) {
        ((SwitchPreference) preference).setChecked(ContentResolver.getMasterSyncAutomaticallyAsUser(this.mUserHandle.getIdentifier()));
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!getPreferenceKey().equals(preference.getKey())) {
            return false;
        }
        SwitchPreference switchPreference = (SwitchPreference) preference;
        boolean isChecked = switchPreference.isChecked();
        switchPreference.setChecked(!isChecked);
        if (ActivityManager.isUserAMonkey()) {
            Log.d("AutoSyncDataController", "ignoring monkey's attempt to flip sync state");
            return true;
        }
        ConfirmAutoSyncChangeFragment.show(this.mParentFragment, isChecked, this.mUserHandle, switchPreference);
        return true;
    }

    public boolean isAvailable() {
        if (this.mUserManager.isManagedProfile() || (!this.mUserManager.isRestrictedProfile() && this.mUserManager.getProfiles(UserHandle.myUserId()).size() != 1)) {
            return false;
        }
        return true;
    }

    public static class ConfirmAutoSyncChangeFragment extends InstrumentedDialogFragment implements DialogInterface.OnClickListener {
        boolean mEnabling;
        SwitchPreference mPreference;
        UserHandle mUserHandle;

        public int getMetricsCategory() {
            return 535;
        }

        public static void show(Fragment fragment, boolean z, UserHandle userHandle, SwitchPreference switchPreference) {
            if (fragment.isAdded()) {
                ConfirmAutoSyncChangeFragment confirmAutoSyncChangeFragment = new ConfirmAutoSyncChangeFragment();
                confirmAutoSyncChangeFragment.mEnabling = z;
                confirmAutoSyncChangeFragment.mUserHandle = userHandle;
                confirmAutoSyncChangeFragment.setTargetFragment(fragment, 0);
                confirmAutoSyncChangeFragment.mPreference = switchPreference;
                confirmAutoSyncChangeFragment.show(fragment.getFragmentManager(), "confirmAutoSyncChange");
            }
        }

        public Dialog onCreateDialog(Bundle bundle) {
            FragmentActivity activity = getActivity();
            if (bundle != null) {
                this.mEnabling = bundle.getBoolean("enabling");
                this.mUserHandle = (UserHandle) bundle.getParcelable("userHandle");
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            if (!this.mEnabling) {
                builder.setTitle((int) R.string.data_usage_auto_sync_off_dialog_title);
                builder.setMessage((int) R.string.data_usage_auto_sync_off_dialog);
            } else {
                builder.setTitle((int) R.string.data_usage_auto_sync_on_dialog_title);
                builder.setMessage((int) R.string.data_usage_auto_sync_on_dialog);
            }
            builder.setPositiveButton(17039370, (DialogInterface.OnClickListener) this);
            builder.setNegativeButton(17039360, (DialogInterface.OnClickListener) null);
            return builder.create();
        }

        public void onSaveInstanceState(Bundle bundle) {
            super.onSaveInstanceState(bundle);
            bundle.putBoolean("enabling", this.mEnabling);
            bundle.putParcelable("userHandle", this.mUserHandle);
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            if (i == -1) {
                ContentResolver.setMasterSyncAutomaticallyAsUser(this.mEnabling, this.mUserHandle.getIdentifier());
                SwitchPreference switchPreference = this.mPreference;
                if (switchPreference != null) {
                    switchPreference.setChecked(this.mEnabling);
                }
            }
        }
    }
}
