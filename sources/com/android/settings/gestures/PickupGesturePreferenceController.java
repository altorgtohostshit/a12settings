package com.android.settings.gestures;

import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.display.AmbientDisplayConfiguration;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.TextUtils;
import com.android.settings.slices.SliceBackgroundWorker;

public class PickupGesturePreferenceController extends GesturePreferenceController {
    private static final int OFF = 0;

    /* renamed from: ON */
    private static final int f71ON = 1;
    private static final String PREF_KEY_VIDEO = "gesture_pick_up_video";
    private final String SECURE_KEY = "doze_pulse_on_pick_up";
    private AmbientDisplayConfiguration mAmbientConfig;
    private final String mPickUpPrefKey;
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

    public PickupGesturePreferenceController(Context context, String str) {
        super(context, str);
        this.mPickUpPrefKey = str;
    }

    public PickupGesturePreferenceController setConfig(AmbientDisplayConfiguration ambientDisplayConfiguration) {
        this.mAmbientConfig = ambientDisplayConfiguration;
        return this;
    }

    public static boolean isSuggestionComplete(Context context, SharedPreferences sharedPreferences) {
        AmbientDisplayConfiguration ambientDisplayConfiguration = new AmbientDisplayConfiguration(context);
        if (sharedPreferences.getBoolean("pref_pickup_gesture_suggestion_complete", false) || !ambientDisplayConfiguration.dozePickupSensorAvailable()) {
            return true;
        }
        return false;
    }

    public int getAvailabilityStatus() {
        return !getAmbientConfig().dozePickupSensorAvailable() ? 3 : 0;
    }

    public boolean isSliceable() {
        return TextUtils.equals(getPreferenceKey(), "gesture_pick_up");
    }

    public boolean isChecked() {
        return getAmbientConfig().pickupGestureEnabled(this.mUserId);
    }

    public String getPreferenceKey() {
        return this.mPickUpPrefKey;
    }

    public boolean setChecked(boolean z) {
        return Settings.Secure.putInt(this.mContext.getContentResolver(), "doze_pulse_on_pick_up", z ? 1 : 0);
    }

    private AmbientDisplayConfiguration getAmbientConfig() {
        if (this.mAmbientConfig == null) {
            this.mAmbientConfig = new AmbientDisplayConfiguration(this.mContext);
        }
        return this.mAmbientConfig;
    }
}
