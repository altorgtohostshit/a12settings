package com.android.settings.accessibility;

import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.provider.Settings;
import androidx.lifecycle.LifecycleObserver;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.widget.RadioButtonPreference;
import com.google.common.primitives.Ints;
import java.util.HashMap;
import java.util.Map;

public class DaltonizerRadioButtonPreferenceController extends BasePreferenceController implements LifecycleObserver, RadioButtonPreference.OnClickListener {
    private static final String TYPE = "accessibility_display_daltonizer";
    private final Map<String, Integer> mAccessibilityDaltonizerKeyToValueMap = new HashMap();
    private int mAccessibilityDaltonizerValue;
    private final ContentResolver mContentResolver;
    private OnChangeListener mOnChangeListener;
    private RadioButtonPreference mPreference;
    private final Resources mResources;

    public interface OnChangeListener {
        void onCheckedChanged(Preference preference);
    }

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public int getAvailabilityStatus() {
        return 0;
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

    public DaltonizerRadioButtonPreferenceController(Context context, String str) {
        super(context, str);
        this.mContentResolver = context.getContentResolver();
        this.mResources = context.getResources();
    }

    public DaltonizerRadioButtonPreferenceController(Context context, Lifecycle lifecycle, String str) {
        super(context, str);
        this.mContentResolver = context.getContentResolver();
        this.mResources = context.getResources();
        if (lifecycle != null) {
            lifecycle.addObserver(this);
        }
    }

    protected static int getSecureAccessibilityDaltonizerValue(ContentResolver contentResolver, String str) {
        Integer tryParse;
        String string = Settings.Secure.getString(contentResolver, str);
        if (string == null || (tryParse = Ints.tryParse(string)) == null) {
            return 12;
        }
        return tryParse.intValue();
    }

    public void setOnChangeListener(OnChangeListener onChangeListener) {
        this.mOnChangeListener = onChangeListener;
    }

    private Map<String, Integer> getDaltonizerValueToKeyMap() {
        if (this.mAccessibilityDaltonizerKeyToValueMap.size() == 0) {
            String[] stringArray = this.mResources.getStringArray(R.array.daltonizer_mode_keys);
            int[] intArray = this.mResources.getIntArray(R.array.daltonizer_type_values);
            int length = intArray.length;
            for (int i = 0; i < length; i++) {
                this.mAccessibilityDaltonizerKeyToValueMap.put(stringArray[i], Integer.valueOf(intArray[i]));
            }
        }
        return this.mAccessibilityDaltonizerKeyToValueMap;
    }

    private void putSecureString(String str, String str2) {
        Settings.Secure.putString(this.mContentResolver, str, str2);
    }

    private void handlePreferenceChange(String str) {
        putSecureString(TYPE, str);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        RadioButtonPreference radioButtonPreference = (RadioButtonPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = radioButtonPreference;
        radioButtonPreference.setOnClickListener(this);
        this.mPreference.setAppendixVisibility(8);
        updateState(this.mPreference);
    }

    public void onRadioButtonClicked(RadioButtonPreference radioButtonPreference) {
        handlePreferenceChange(String.valueOf(getDaltonizerValueToKeyMap().get(this.mPreferenceKey).intValue()));
        OnChangeListener onChangeListener = this.mOnChangeListener;
        if (onChangeListener != null) {
            onChangeListener.onCheckedChanged(this.mPreference);
        }
    }

    private int getAccessibilityDaltonizerValue() {
        return getSecureAccessibilityDaltonizerValue(this.mContentResolver, TYPE);
    }

    /* access modifiers changed from: protected */
    public void updatePreferenceCheckedState(int i) {
        if (this.mAccessibilityDaltonizerValue == i) {
            this.mPreference.setChecked(true);
        }
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        this.mAccessibilityDaltonizerValue = getAccessibilityDaltonizerValue();
        this.mPreference.setChecked(false);
        updatePreferenceCheckedState(getDaltonizerValueToKeyMap().get(this.mPreference.getKey()).intValue());
    }
}
