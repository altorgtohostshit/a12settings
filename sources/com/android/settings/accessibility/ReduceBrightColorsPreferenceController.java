package com.android.settings.accessibility;

import android.content.Context;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.hardware.display.ColorDisplayManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.widget.PrimarySwitchPreference;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;

public class ReduceBrightColorsPreferenceController extends TogglePreferenceController implements LifecycleObserver, OnStart, OnStop {
    private final ColorDisplayManager mColorDisplayManager;
    private final Context mContext;
    /* access modifiers changed from: private */
    public PrimarySwitchPreference mPreference;
    private ContentObserver mSettingsContentObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
        public void onChange(boolean z, Uri uri) {
            if (TextUtils.equals(uri == null ? null : uri.getLastPathSegment(), "reduce_bright_colors_activated")) {
                ReduceBrightColorsPreferenceController reduceBrightColorsPreferenceController = ReduceBrightColorsPreferenceController.this;
                reduceBrightColorsPreferenceController.updateState(reduceBrightColorsPreferenceController.mPreference);
            }
        }
    };

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

    public ReduceBrightColorsPreferenceController(Context context, String str) {
        super(context, str);
        this.mContext = context;
        this.mColorDisplayManager = (ColorDisplayManager) context.getSystemService(ColorDisplayManager.class);
    }

    public boolean isChecked() {
        return this.mColorDisplayManager.isReduceBrightColorsActivated();
    }

    public boolean setChecked(boolean z) {
        return this.mColorDisplayManager.setReduceBrightColorsActivated(z);
    }

    public CharSequence getSummary() {
        return this.mContext.getText(R.string.reduce_bright_colors_preference_summary);
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        refreshSummary(preference);
    }

    public int getAvailabilityStatus() {
        return ColorDisplayManager.isReduceBrightColorsAvailable(this.mContext) ? 0 : 3;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = (PrimarySwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
    }

    public void onStart() {
        this.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("reduce_bright_colors_activated"), false, this.mSettingsContentObserver, -2);
    }

    public void onStop() {
        this.mContext.getContentResolver().unregisterContentObserver(this.mSettingsContentObserver);
    }
}
