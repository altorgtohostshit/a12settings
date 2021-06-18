package com.google.android.settings.gestures.assist;

import android.content.Context;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.settings.gestures.AssistGestureFeatureProvider;
import com.android.settings.gestures.GesturePreferenceController;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;

public class AssistGestureWakePreferenceController extends GesturePreferenceController implements OnPause, OnResume {
    private static final String PREF_KEY_VIDEO = "gesture_assist_video";
    private final AssistGestureFeatureProvider mFeatureProvider;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler(Looper.getMainLooper());
    private SwitchPreference mPreference;
    private PreferenceScreen mScreen;
    private final SettingObserver mSettingObserver = new SettingObserver();

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

    public AssistGestureWakePreferenceController(Context context, String str) {
        super(context, str);
        this.mFeatureProvider = FeatureFactory.getFactory(context).getAssistGestureFeatureProvider();
    }

    public int getAvailabilityStatus() {
        return this.mFeatureProvider.isSensorAvailable(this.mContext) ? 0 : 3;
    }

    public boolean isSliceable() {
        return TextUtils.equals(getPreferenceKey(), "gesture_assist_wake");
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        this.mScreen = preferenceScreen;
        this.mPreference = (SwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
        if (!this.mFeatureProvider.isSupported(this.mContext)) {
            this.mScreen.removePreference(this.mPreference);
        } else {
            super.displayPreference(preferenceScreen);
        }
    }

    public boolean setChecked(boolean z) {
        return Settings.Secure.putInt(this.mContext.getContentResolver(), "assist_gesture_wake_enabled", z ? 1 : 0);
    }

    public boolean isChecked() {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), "assist_gesture_wake_enabled", 1) != 0;
    }

    public void onPause() {
        this.mSettingObserver.unregister();
    }

    public void onResume() {
        this.mSettingObserver.register();
        updatePreference();
    }

    /* access modifiers changed from: protected */
    public boolean canHandleClicks() {
        if (Settings.Secure.getInt(this.mContext.getContentResolver(), "assist_gesture_enabled", 1) != 0) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: private */
    public void updatePreference() {
        if (this.mPreference != null) {
            if (this.mFeatureProvider.isSupported(this.mContext)) {
                if (this.mScreen.findPreference(getPreferenceKey()) == null) {
                    this.mScreen.addPreference(this.mPreference);
                }
                this.mPreference.setEnabled(canHandleClicks());
                return;
            }
            this.mScreen.removePreference(this.mPreference);
        }
    }

    class SettingObserver extends ContentObserver {
        private final Uri ASSIST_GESTURE_ENABLED_URI = Settings.Secure.getUriFor("assist_gesture_enabled");

        public SettingObserver() {
            super(AssistGestureWakePreferenceController.this.mHandler);
        }

        public void register() {
            AssistGestureWakePreferenceController.this.mContext.getContentResolver().registerContentObserver(this.ASSIST_GESTURE_ENABLED_URI, false, this);
        }

        public void unregister() {
            AssistGestureWakePreferenceController.this.mContext.getContentResolver().unregisterContentObserver(this);
        }

        public void onChange(boolean z) {
            AssistGestureWakePreferenceController.this.updatePreference();
        }
    }
}
