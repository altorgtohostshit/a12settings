package com.android.settings.biometrics.face;

import android.content.Context;
import android.content.IntentFilter;
import android.hardware.face.FaceManager;
import android.provider.Settings;
import androidx.preference.Preference;
import com.android.settings.Utils;
import com.android.settings.slices.SliceBackgroundWorker;

public class FaceSettingsAppPreferenceController extends FaceSettingsPreferenceController {
    private static final int DEFAULT = 1;
    static final String KEY = "security_settings_face_app";
    private static final int OFF = 0;

    /* renamed from: ON */
    private static final int f58ON = 1;
    private FaceManager mFaceManager;

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

    public FaceSettingsAppPreferenceController(Context context, String str) {
        super(context, str);
        this.mFaceManager = Utils.getFaceManagerOrNull(context);
    }

    public FaceSettingsAppPreferenceController(Context context) {
        this(context, KEY);
    }

    public boolean isChecked() {
        if (FaceSettings.isFaceHardwareDetected(this.mContext) && Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "face_unlock_app_enabled", 1, getUserId()) == 1) {
            return true;
        }
        return false;
    }

    public boolean setChecked(boolean z) {
        return Settings.Secure.putIntForUser(this.mContext.getContentResolver(), "face_unlock_app_enabled", z ? 1 : 0, getUserId());
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        if (!FaceSettings.isFaceHardwareDetected(this.mContext)) {
            preference.setEnabled(false);
        } else if (!this.mFaceManager.hasEnrolledTemplates(getUserId())) {
            preference.setEnabled(false);
        } else {
            preference.setEnabled(true);
        }
    }

    public int getAvailabilityStatus() {
        if (Utils.isMultipleBiometricsSupported(this.mContext)) {
            return 3;
        }
        FaceManager faceManager = this.mFaceManager;
        if (faceManager == null) {
            return 1;
        }
        boolean hasEnrolledTemplates = faceManager.hasEnrolledTemplates(getUserId());
        boolean z = Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "face_unlock_app_enabled", 0, getUserId()) == 1;
        if (!hasEnrolledTemplates || z) {
            return 1;
        }
        return 0;
    }
}
