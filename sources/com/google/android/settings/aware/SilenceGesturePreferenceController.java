package com.google.android.settings.aware;

import android.content.Context;
import android.content.IntentFilter;
import android.net.Uri;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.aware.AwareFeatureProvider;
import com.android.settings.gestures.GesturePreferenceController;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.widget.VideoPreference;
import com.google.android.settings.aware.AwareHelper;

public class SilenceGesturePreferenceController extends GesturePreferenceController implements AwareHelper.Callback {
    private static final int OFF = 0;

    /* renamed from: ON */
    private static final int f122ON = 1;
    private static final String PREF_KEY_VIDEO = "gesture_silence_video";
    public static final float VIDEO_HEIGHT_DP = 310.0f;
    private final AwareFeatureProvider mFeatureProvider;
    private AwareHelper mHelper;
    private Preference mPreference;

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

    public SilenceGesturePreferenceController(Context context, String str) {
        super(context, str);
        this.mFeatureProvider = FeatureFactory.getFactory(context).getAwareFeatureProvider();
        this.mHelper = new AwareHelper(context);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        ((VideoPreference) preferenceScreen.findPreference(PREF_KEY_VIDEO)).setHeight(310.0f);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    public int getAvailabilityStatus() {
        if (!this.mFeatureProvider.isSupported(this.mContext)) {
            return 3;
        }
        return !this.mHelper.isGestureConfigurable() ? 5 : 0;
    }

    /* access modifiers changed from: protected */
    public boolean canHandleClicks() {
        return this.mHelper.isGestureConfigurable();
    }

    public boolean isChecked() {
        if (!this.mFeatureProvider.isEnabled(this.mContext) || Settings.Secure.getInt(this.mContext.getContentResolver(), "silence_gesture", 1) != 1) {
            return false;
        }
        return true;
    }

    public boolean setChecked(boolean z) {
        this.mHelper.writeFeatureEnabled("silence_gesture", z);
        return Settings.Secure.putInt(this.mContext.getContentResolver(), "silence_gesture", z ? 1 : 0);
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
}
