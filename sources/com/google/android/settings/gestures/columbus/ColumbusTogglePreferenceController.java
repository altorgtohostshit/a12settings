package com.google.android.settings.gestures.columbus;

import android.content.Context;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;

public abstract class ColumbusTogglePreferenceController extends TogglePreferenceController implements LifecycleObserver, OnStart, OnStop {
    /* access modifiers changed from: private */
    public static final Uri COLUMBUS_ENABLED_URI = Settings.Secure.getUriFor("columbus_enabled");
    private static final int DISABLED = 0;
    private static final int ENABLED = 1;
    /* access modifiers changed from: private */
    public final Context mContext;
    private final MetricsFeatureProvider mMetricsFeatureProvider;
    private SettingObserver mSettingObserver;
    private SwitchPreference mSwitchPreference;

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

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public ColumbusTogglePreferenceController(Context context, String str, int i) {
        super(context, str);
        this.mContext = context;
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(context).getMetricsFeatureProvider();
        setMetricsCategory(i);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        SwitchPreference switchPreference = (SwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mSwitchPreference = switchPreference;
        if (switchPreference != null) {
            this.mSettingObserver = new SettingObserver(this.mSwitchPreference);
        }
    }

    public boolean isChecked() {
        if (Settings.Secure.getInt(this.mContext.getContentResolver(), getPreferenceKey(), 0) != 0) {
            return true;
        }
        return false;
    }

    public boolean setChecked(boolean z) {
        this.mMetricsFeatureProvider.action(this.mContext, getMetricsCategory(), z);
        return Settings.Secure.putInt(this.mContext.getContentResolver(), getPreferenceKey(), z ? 1 : 0);
    }

    public int getAvailabilityStatus() {
        return ColumbusPreferenceController.isColumbusSupported(this.mContext) ? 0 : 3;
    }

    public void onStart() {
        SettingObserver settingObserver = this.mSettingObserver;
        if (settingObserver != null) {
            settingObserver.register();
        }
    }

    public void onStop() {
        SettingObserver settingObserver = this.mSettingObserver;
        if (settingObserver != null) {
            settingObserver.unregister();
        }
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        SwitchPreference switchPreference = this.mSwitchPreference;
        if (switchPreference != null) {
            switchPreference.setEnabled(ColumbusPreferenceController.isColumbusEnabled(this.mContext));
        }
    }

    private class SettingObserver extends ContentObserver {
        private final Preference mPreference;

        SettingObserver(Preference preference) {
            super(new Handler(Looper.myLooper()));
            this.mPreference = preference;
        }

        public void register() {
            ColumbusTogglePreferenceController.this.mContext.getContentResolver().registerContentObserver(ColumbusTogglePreferenceController.COLUMBUS_ENABLED_URI, false, this);
        }

        public void unregister() {
            ColumbusTogglePreferenceController.this.mContext.getContentResolver().unregisterContentObserver(this);
        }

        public void onChange(boolean z) {
            ColumbusTogglePreferenceController.this.updateState(this.mPreference);
        }
    }
}
