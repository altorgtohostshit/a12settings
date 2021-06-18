package com.google.android.settings.security;

import android.content.Context;
import android.content.IntentFilter;
import androidx.preference.Preference;
import com.android.settings.biometrics.face.FaceStatusPreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;

public class FaceStatusGooglePreferenceController extends FaceStatusPreferenceController {
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

    public FaceStatusGooglePreferenceController(Context context) {
        super(context);
    }

    public FaceStatusGooglePreferenceController(Context context, String str) {
        super(context, str);
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        if (preference.isEnabled()) {
            preference.setIcon(SecurityLevel.INFO.getIconResId());
        }
    }
}
