package com.android.settings.biometrics.face;

import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.hardware.face.FaceManager;
import android.provider.Settings;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.settings.Utils;
import com.android.settings.slices.SliceBackgroundWorker;

public class FaceSettingsAttentionPreferenceController extends FaceSettingsPreferenceController {
    public static final String KEY = "security_settings_face_require_attention";
    private FaceManager mFaceManager;
    private final FaceManager.GetFeatureCallback mGetFeatureCallback;
    /* access modifiers changed from: private */
    public SwitchPreference mPreference;
    private final FaceManager.SetFeatureCallback mSetFeatureCallback;
    private byte[] mToken;

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

    public boolean isSliceable() {
        return false;
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public FaceSettingsAttentionPreferenceController(Context context, String str) {
        super(context, str);
        this.mSetFeatureCallback = new FaceManager.SetFeatureCallback() {
            public void onCompleted(boolean z, int i) {
                if (i == 1) {
                    FaceSettingsAttentionPreferenceController.this.mPreference.setEnabled(true);
                    if (!z) {
                        FaceSettingsAttentionPreferenceController.this.mPreference.setChecked(!FaceSettingsAttentionPreferenceController.this.mPreference.isChecked());
                        return;
                    }
                    ContentResolver contentResolver = FaceSettingsAttentionPreferenceController.this.mContext.getContentResolver();
                    boolean isChecked = FaceSettingsAttentionPreferenceController.this.mPreference.isChecked();
                    Settings.Secure.putIntForUser(contentResolver, "face_unlock_attention_required", isChecked ? 1 : 0, FaceSettingsAttentionPreferenceController.this.getUserId());
                }
            }
        };
        this.mGetFeatureCallback = new FaceManager.GetFeatureCallback() {
            public void onCompleted(boolean z, int[] iArr, boolean[] zArr) {
                boolean z2 = false;
                for (int i = 0; i < iArr.length; i++) {
                    if (iArr[i] == 1) {
                        z2 = zArr[i];
                    }
                }
                FaceSettingsAttentionPreferenceController.this.mPreference.setEnabled(z);
                FaceSettingsAttentionPreferenceController.this.mPreference.setChecked(z2);
            }
        };
        this.mFaceManager = Utils.getFaceManagerOrNull(context);
    }

    public FaceSettingsAttentionPreferenceController(Context context) {
        this(context, KEY);
    }

    public void setToken(byte[] bArr) {
        this.mToken = bArr;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = (SwitchPreference) preferenceScreen.findPreference(KEY);
    }

    public boolean isChecked() {
        if (!FaceSettings.isFaceHardwareDetected(this.mContext)) {
            return true;
        }
        this.mPreference.setEnabled(false);
        this.mFaceManager.getFeature(getUserId(), 1, this.mGetFeatureCallback);
        return true;
    }

    public boolean setChecked(boolean z) {
        this.mPreference.setEnabled(false);
        this.mPreference.setChecked(z);
        this.mFaceManager.setFeature(getUserId(), 1, z, this.mToken, this.mSetFeatureCallback);
        return true;
    }
}
