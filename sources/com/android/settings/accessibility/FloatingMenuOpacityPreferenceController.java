package com.android.settings.accessibility;

import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.SliderPreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.widget.SeekBarPreference;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;

public class FloatingMenuOpacityPreferenceController extends SliderPreferenceController implements LifecycleObserver, OnResume, OnPause {
    static final float DEFAULT_OPACITY = 0.55f;
    private static final int FADE_ENABLED = 1;
    private static final float MAX_PROGRESS = 100.0f;
    private static final float MIN_PROGRESS = 10.0f;
    static final float PRECISION = 100.0f;
    final ContentObserver mContentObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
        public void onChange(boolean z) {
            FloatingMenuOpacityPreferenceController.this.updateAvailabilityStatus();
        }
    };
    private final ContentResolver mContentResolver;
    SeekBarPreference mPreference;

    private float convertOpacityIntToFloat(int i) {
        return ((float) i) / 100.0f;
    }

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public int getMax() {
        return 100;
    }

    public int getMin() {
        return 10;
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

    public FloatingMenuOpacityPreferenceController(Context context, String str) {
        super(context, str);
        this.mContentResolver = context.getContentResolver();
    }

    public int getAvailabilityStatus() {
        return AccessibilityUtil.isFloatingMenuEnabled(this.mContext) ? 0 : 5;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        SeekBarPreference seekBarPreference = (SeekBarPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = seekBarPreference;
        seekBarPreference.setContinuousUpdates(true);
        this.mPreference.setMax(getMax());
        this.mPreference.setMin(getMin());
        this.mPreference.setHapticFeedbackMode(2);
        updateState(this.mPreference);
    }

    public void onResume() {
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor("accessibility_button_mode"), false, this.mContentObserver);
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor("accessibility_floating_menu_fade_enabled"), false, this.mContentObserver);
    }

    public void onPause() {
        this.mContentResolver.unregisterContentObserver(this.mContentObserver);
    }

    public int getSliderPosition() {
        return convertOpacityFloatToInt(getOpacity());
    }

    public boolean setSliderPosition(int i) {
        return Settings.Secure.putFloat(this.mContentResolver, "accessibility_floating_menu_opacity", convertOpacityIntToFloat(i));
    }

    /* access modifiers changed from: private */
    public void updateAvailabilityStatus() {
        boolean z = true;
        boolean z2 = Settings.Secure.getInt(this.mContentResolver, "accessibility_floating_menu_fade_enabled", 1) == 1;
        SeekBarPreference seekBarPreference = this.mPreference;
        if (!AccessibilityUtil.isFloatingMenuEnabled(this.mContext) || !z2) {
            z = false;
        }
        seekBarPreference.setEnabled(z);
    }

    private int convertOpacityFloatToInt(float f) {
        return Math.round(f * 100.0f);
    }

    private float getOpacity() {
        float f = Settings.Secure.getFloat(this.mContentResolver, "accessibility_floating_menu_opacity", DEFAULT_OPACITY);
        return (f < 0.1f || f > 1.0f) ? DEFAULT_OPACITY : f;
    }
}
