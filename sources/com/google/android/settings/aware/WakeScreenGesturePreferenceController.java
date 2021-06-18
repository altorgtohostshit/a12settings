package com.google.android.settings.aware;

import android.content.Context;
import android.content.IntentFilter;
import android.hardware.display.AmbientDisplayConfiguration;
import android.net.Uri;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.TextUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.aware.AwareFeatureProvider;
import com.android.settings.gestures.GesturePreferenceController;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.slices.SliceBackgroundWorker;
import com.google.android.settings.aware.AwareHelper;

public class WakeScreenGesturePreferenceController extends GesturePreferenceController implements AwareHelper.Callback {
    private static final int OFF = 0;

    /* renamed from: ON */
    private static final int f125ON = 1;
    private static final String PREF_KEY_VIDEO = "gesture_wake_screen_video";
    private AmbientDisplayConfiguration mAmbientConfig;
    private final AwareFeatureProvider mFeatureProvider;
    private AwareHelper mHelper;
    private Preference mPreference;
    private final int mUserId = UserHandle.myUserId();

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    /* access modifiers changed from: protected */
    public String getVideoPrefKey() {
        return PREF_KEY_VIDEO;
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public boolean isPublicSlice() {
        return true;
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public WakeScreenGesturePreferenceController(Context context, String str) {
        super(context, str);
        this.mFeatureProvider = FeatureFactory.getFactory(context).getAwareFeatureProvider();
        this.mHelper = new AwareHelper(context);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    public int getAvailabilityStatus() {
        if (!getAmbientConfig().wakeScreenGestureAvailable() || !this.mHelper.isSupported()) {
            return 3;
        }
        return !this.mHelper.isGestureConfigurable() ? 5 : 0;
    }

    public boolean isSliceable() {
        return TextUtils.equals(getPreferenceKey(), "gesture_wake_screen");
    }

    public boolean isChecked() {
        return getAmbientConfig().wakeLockScreenGestureEnabled(this.mUserId) && this.mFeatureProvider.isEnabled(this.mContext);
    }

    public boolean setChecked(boolean z) {
        this.mHelper.writeFeatureEnabled("doze_wake_screen_gesture", z);
        return Settings.Secure.putInt(this.mContext.getContentResolver(), "doze_wake_screen_gesture", z ? 1 : 0);
    }

    @VisibleForTesting
    public void setConfig(AmbientDisplayConfiguration ambientDisplayConfiguration) {
        this.mAmbientConfig = ambientDisplayConfiguration;
    }

    public void onStart() {
        this.mHelper.register(this);
    }

    public void onStop() {
        this.mHelper.unregister();
    }

    public void onChange(Uri uri) {
        updateState(this.mPreference);
    }

    /* access modifiers changed from: protected */
    public boolean canHandleClicks() {
        return this.mHelper.isGestureConfigurable();
    }

    private AmbientDisplayConfiguration getAmbientConfig() {
        if (this.mAmbientConfig == null) {
            this.mAmbientConfig = new AmbientDisplayConfiguration(this.mContext);
        }
        return this.mAmbientConfig;
    }
}
