package com.google.android.settings.gestures.assist;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import android.text.TextUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.gestures.AssistGestureFeatureProvider;
import com.android.settings.gestures.GesturePreferenceController;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.slices.SliceBackgroundWorker;

public class AssistGestureSilenceAlertsPreferenceController extends GesturePreferenceController {
    private static final String ASSIST_GESTURE_SILENCE_ALERTS_PREF_KEY = "gesture_assist_silence";
    private static final String PREF_KEY_VIDEO = "gesture_assist_video";
    private final AssistGestureFeatureProvider mFeatureProvider;

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

    public AssistGestureSilenceAlertsPreferenceController(Context context, String str) {
        super(context, str);
        this.mFeatureProvider = FeatureFactory.getFactory(context).getAssistGestureFeatureProvider();
    }

    public int getAvailabilityStatus() {
        return this.mFeatureProvider.isSensorAvailable(this.mContext) ? 0 : 3;
    }

    public boolean isSliceable() {
        return TextUtils.equals(getPreferenceKey(), ASSIST_GESTURE_SILENCE_ALERTS_PREF_KEY);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        Preference findPreference;
        if (((AssistGestureFeatureProviderGoogleImpl) this.mFeatureProvider).isDeskClockSupported(this.mContext) && (findPreference = preferenceScreen.findPreference(ASSIST_GESTURE_SILENCE_ALERTS_PREF_KEY)) != null) {
            findPreference.setSummary((int) R.string.assist_gesture_setting_enable_ring_alarm_silence_text);
        }
        super.displayPreference(preferenceScreen);
    }

    public boolean setChecked(boolean z) {
        return Settings.Secure.putInt(this.mContext.getContentResolver(), "assist_gesture_silence_alerts_enabled", z ? 1 : 0);
    }

    public boolean isChecked() {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), "assist_gesture_silence_alerts_enabled", 1) != 0;
    }
}
