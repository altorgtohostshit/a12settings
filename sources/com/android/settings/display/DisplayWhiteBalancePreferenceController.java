package com.android.settings.display;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.hardware.display.ColorDisplayManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;

public class DisplayWhiteBalancePreferenceController extends TogglePreferenceController implements LifecycleObserver, OnStart, OnStop {
    private ColorDisplayManager mColorDisplayManager;
    ContentObserver mContentObserver;
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

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public DisplayWhiteBalancePreferenceController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        getColorDisplayManager();
        return ColorDisplayManager.isDisplayWhiteBalanceAvailable(this.mContext) ? 0 : 4;
    }

    public boolean isChecked() {
        return getColorDisplayManager().isDisplayWhiteBalanceEnabled();
    }

    public boolean setChecked(boolean z) {
        return getColorDisplayManager().setDisplayWhiteBalanceEnabled(z);
    }

    public void onStart() {
        if (isAvailable()) {
            ContentResolver contentResolver = this.mContext.getContentResolver();
            this.mContentObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
                public void onChange(boolean z, Uri uri) {
                    super.onChange(z, uri);
                    DisplayWhiteBalancePreferenceController.this.updateVisibility();
                }
            };
            contentResolver.registerContentObserver(Settings.Secure.getUriFor("accessibility_display_inversion_enabled"), false, this.mContentObserver, ActivityManager.getCurrentUser());
            contentResolver.registerContentObserver(Settings.Secure.getUriFor("accessibility_display_daltonizer_enabled"), false, this.mContentObserver, ActivityManager.getCurrentUser());
            contentResolver.registerContentObserver(Settings.System.getUriFor("display_color_mode"), false, this.mContentObserver, ActivityManager.getCurrentUser());
            updateVisibility();
        }
    }

    public void onStop() {
        if (this.mContentObserver != null) {
            this.mContext.getContentResolver().unregisterContentObserver(this.mContentObserver);
            this.mContentObserver = null;
        }
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    /* access modifiers changed from: package-private */
    public ColorDisplayManager getColorDisplayManager() {
        if (this.mColorDisplayManager == null) {
            this.mColorDisplayManager = (ColorDisplayManager) this.mContext.getSystemService(ColorDisplayManager.class);
        }
        return this.mColorDisplayManager;
    }

    /* access modifiers changed from: package-private */
    public void updateVisibility() {
        if (this.mPreference != null) {
            this.mPreference.setVisible(getColorDisplayManager().getColorMode() != 2 && !ColorDisplayManager.areAccessibilityTransformsEnabled(this.mContext));
        }
    }
}
