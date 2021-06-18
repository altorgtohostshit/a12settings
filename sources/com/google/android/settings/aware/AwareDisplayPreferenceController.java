package com.google.android.settings.aware;

import android.content.Context;
import android.content.IntentFilter;
import android.hardware.display.AmbientDisplayConfiguration;
import android.net.Uri;
import android.os.SystemProperties;
import android.os.UserHandle;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.display.AmbientDisplayAlwaysOnPreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import com.google.android.settings.aware.AwareHelper;

public class AwareDisplayPreferenceController extends BasePreferenceController implements LifecycleObserver, OnStart, OnStop, AwareHelper.Callback {
    private static final int MY_USER = UserHandle.myUserId();
    private static final String PROP_AWARE_AVAILABLE = "ro.vendor.aware_available";
    private final AmbientDisplayConfiguration mConfig;
    private final AwareHelper mHelper;
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

    public AwareDisplayPreferenceController(Context context, String str) {
        super(context, str);
        this.mHelper = new AwareHelper(context);
        this.mConfig = new AmbientDisplayConfiguration(context);
    }

    public int getAvailabilityStatus() {
        boolean alwaysOnAvailable = this.mConfig.alwaysOnAvailable();
        boolean z = SystemProperties.getBoolean(PROP_AWARE_AVAILABLE, false);
        if (alwaysOnAvailable || z) {
            return 0;
        }
        return 3;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    public CharSequence getSummary() {
        AmbientDisplayConfiguration ambientDisplayConfiguration = this.mConfig;
        int i = MY_USER;
        boolean wakeDisplayGestureEnabled = ambientDisplayConfiguration.wakeDisplayGestureEnabled(i);
        boolean alwaysOnEnabled = this.mConfig.alwaysOnEnabled(i);
        if (AmbientDisplayAlwaysOnPreferenceController.isAodSuppressedByBedtime(this.mContext)) {
            return this.mContext.getText(R.string.aware_summary_when_bedtime_on);
        }
        if (wakeDisplayGestureEnabled && this.mHelper.isGestureConfigurable()) {
            return this.mContext.getText(R.string.aware_wake_display_title);
        }
        if (alwaysOnEnabled) {
            return this.mContext.getText(R.string.doze_always_on_title);
        }
        return this.mContext.getText(R.string.switch_off_text);
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
