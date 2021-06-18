package com.android.settings.accessibility;

import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.ArrayMap;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;
import com.google.common.primitives.Ints;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class FloatingMenuSizePreferenceController extends BasePreferenceController implements Preference.OnPreferenceChangeListener, LifecycleObserver, OnResume, OnPause {
    final ContentObserver mContentObserver;
    private final ContentResolver mContentResolver;
    private int mDefaultSize;
    ListPreference mPreference;
    private final ArrayMap<String, String> mValueTitleMap = new ArrayMap<>();

    @Retention(RetentionPolicy.SOURCE)
    @interface Size {
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

    public FloatingMenuSizePreferenceController(Context context, String str) {
        super(context, str);
        this.mContentResolver = context.getContentResolver();
        this.mContentObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
            public void onChange(boolean z) {
                FloatingMenuSizePreferenceController.this.updateAvailabilityStatus();
            }
        };
        initValueTitleMap();
    }

    public int getAvailabilityStatus() {
        return AccessibilityUtil.isFloatingMenuEnabled(this.mContext) ? 0 : 5;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = (ListPreference) preferenceScreen.findPreference(getPreferenceKey());
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        ListPreference listPreference = (ListPreference) preference;
        Integer tryParse = Ints.tryParse((String) obj);
        if (tryParse == null) {
            return true;
        }
        putAccessibilityFloatingMenuSize(tryParse.intValue());
        updateState(listPreference);
        return true;
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        ((ListPreference) preference).setValue(String.valueOf(getAccessibilityFloatingMenuSize(this.mDefaultSize)));
    }

    public void onResume() {
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor("accessibility_button_mode"), false, this.mContentObserver);
    }

    public void onPause() {
        this.mContentResolver.unregisterContentObserver(this.mContentObserver);
    }

    /* access modifiers changed from: private */
    public void updateAvailabilityStatus() {
        this.mPreference.setEnabled(AccessibilityUtil.isFloatingMenuEnabled(this.mContext));
    }

    private void initValueTitleMap() {
        if (this.mValueTitleMap.size() == 0) {
            String[] stringArray = this.mContext.getResources().getStringArray(R.array.accessibility_button_size_selector_values);
            String[] stringArray2 = this.mContext.getResources().getStringArray(R.array.accessibility_button_size_selector_titles);
            int length = stringArray.length;
            this.mDefaultSize = Integer.parseInt(stringArray[0]);
            for (int i = 0; i < length; i++) {
                this.mValueTitleMap.put(stringArray[i], stringArray2[i]);
            }
        }
    }

    private int getAccessibilityFloatingMenuSize(int i) {
        return Settings.Secure.getInt(this.mContentResolver, "accessibility_floating_menu_size", i);
    }

    private void putAccessibilityFloatingMenuSize(int i) {
        Settings.Secure.putInt(this.mContentResolver, "accessibility_floating_menu_size", i);
    }
}
