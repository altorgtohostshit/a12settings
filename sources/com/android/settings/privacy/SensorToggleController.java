package com.android.settings.privacy;

import android.content.Context;
import android.content.IntentFilter;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.utils.SensorPrivacyManagerHelper;
import java.util.concurrent.Executor;

public abstract class SensorToggleController extends TogglePreferenceController {
    private final Executor mCallbackExecutor;
    protected final SensorPrivacyManagerHelper mSensorPrivacyManagerHelper;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public abstract int getSensor();

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public SensorToggleController(Context context, String str) {
        super(context, str);
        this.mSensorPrivacyManagerHelper = SensorPrivacyManagerHelper.getInstance(context);
        this.mCallbackExecutor = context.getMainExecutor();
    }

    public boolean isChecked() {
        return !this.mSensorPrivacyManagerHelper.isSensorBlocked(getSensor());
    }

    public boolean setChecked(boolean z) {
        this.mSensorPrivacyManagerHelper.setSensorBlocked(getSensor(), !z);
        return true;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mSensorPrivacyManagerHelper.addSensorBlockedListener(getSensor(), new SensorToggleController$$ExternalSyntheticLambda0(this, preferenceScreen), this.mCallbackExecutor);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$displayPreference$0(PreferenceScreen preferenceScreen, int i, boolean z) {
        updateState(preferenceScreen.findPreference(this.mPreferenceKey));
    }
}
