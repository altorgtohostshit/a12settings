package com.android.settings.biometrics.face;

import android.content.Context;
import android.content.IntentFilter;
import android.hardware.face.FaceManager;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import androidx.preference.Preference;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.Utils;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedSwitchPreference;

public class FaceSettingsLockscreenBypassPreferenceController extends FaceSettingsPreferenceController {
    @VisibleForTesting
    protected FaceManager mFaceManager;
    private UserManager mUserManager;

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

    public FaceSettingsLockscreenBypassPreferenceController(Context context, String str) {
        super(context, str);
        if (context.getPackageManager().hasSystemFeature("android.hardware.biometrics.face")) {
            this.mFaceManager = (FaceManager) context.getSystemService(FaceManager.class);
        }
        this.mUserManager = (UserManager) context.getSystemService(UserManager.class);
    }

    public boolean isChecked() {
        if (!FaceSettings.isFaceHardwareDetected(this.mContext) || getRestrictingAdmin() != null) {
            return false;
        }
        boolean z = this.mContext.getResources().getBoolean(17891553);
        if (Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "face_unlock_dismisses_keyguard", z ? 1 : 0, getUserId()) != 0) {
            return true;
        }
        return false;
    }

    public boolean setChecked(boolean z) {
        Settings.Secure.putInt(this.mContext.getContentResolver(), "face_unlock_dismisses_keyguard", z ? 1 : 0);
        return true;
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        if (!FaceSettings.isFaceHardwareDetected(this.mContext)) {
            preference.setEnabled(false);
            return;
        }
        RestrictedLockUtils.EnforcedAdmin restrictingAdmin = getRestrictingAdmin();
        if (restrictingAdmin != null) {
            ((RestrictedSwitchPreference) preference).setDisabledByAdmin(restrictingAdmin);
        } else if (!this.mFaceManager.hasEnrolledTemplates(getUserId())) {
            preference.setEnabled(false);
        } else {
            preference.setEnabled(true);
        }
    }

    public int getAvailabilityStatus() {
        FaceManager faceManager;
        if (Utils.isMultipleBiometricsSupported(this.mContext)) {
            return 0;
        }
        if (this.mUserManager.isManagedProfile(UserHandle.myUserId()) || (faceManager = this.mFaceManager) == null || !faceManager.isHardwareDetected()) {
            return 3;
        }
        if (this.mFaceManager.hasEnrolledTemplates(getUserId())) {
            return 0;
        }
        return 5;
    }
}
