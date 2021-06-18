package com.android.settings.accessibility;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import android.util.ArrayMap;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.google.common.primitives.Ints;

public class AccessibilityButtonLocationPreferenceController extends BasePreferenceController implements Preference.OnPreferenceChangeListener {
    private int mDefaultLocation;
    private final ArrayMap<String, String> mValueTitleMap = new ArrayMap<>();

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

    public AccessibilityButtonLocationPreferenceController(Context context, String str) {
        super(context, str);
        initValueTitleMap();
    }

    public int getAvailabilityStatus() {
        return AccessibilityUtil.isGestureNavigateEnabled(this.mContext) ? 2 : 0;
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        ListPreference listPreference = (ListPreference) preference;
        Integer tryParse = Ints.tryParse((String) obj);
        if (tryParse == null) {
            return true;
        }
        Settings.Secure.putInt(this.mContext.getContentResolver(), "accessibility_button_mode", tryParse.intValue());
        updateState(listPreference);
        return true;
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        ((ListPreference) preference).setValue(getCurrentAccessibilityButtonMode());
    }

    private String getCurrentAccessibilityButtonMode() {
        return String.valueOf(Settings.Secure.getInt(this.mContext.getContentResolver(), "accessibility_button_mode", this.mDefaultLocation));
    }

    private void initValueTitleMap() {
        if (this.mValueTitleMap.size() == 0) {
            String[] stringArray = this.mContext.getResources().getStringArray(R.array.accessibility_button_location_selector_values);
            String[] stringArray2 = this.mContext.getResources().getStringArray(R.array.accessibility_button_location_selector_titles);
            int length = stringArray.length;
            this.mDefaultLocation = Integer.parseInt(stringArray[0]);
            for (int i = 0; i < length; i++) {
                this.mValueTitleMap.put(stringArray[i], stringArray2[i]);
            }
        }
    }
}
