package com.android.settings.gestures;

import android.content.Context;
import android.content.IntentFilter;
import android.net.Uri;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.gestures.OneHandedSettingsUtils;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import java.util.HashMap;
import java.util.Map;

public class OneHandedTimeoutPreferenceController extends BasePreferenceController implements Preference.OnPreferenceChangeListener, LifecycleObserver, OnStart, OnStop, OneHandedSettingsUtils.TogglesCallback {
    private final Map<String, String> mTimeoutMap = new HashMap();
    private Preference mTimeoutPreference;
    private final OneHandedSettingsUtils mUtils;

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

    public OneHandedTimeoutPreferenceController(Context context, String str) {
        super(context, str);
        initTimeoutMap();
        this.mUtils = new OneHandedSettingsUtils(context);
    }

    public int getAvailabilityStatus() {
        return OneHandedSettingsUtils.isOneHandedModeEnabled(this.mContext) ? 0 : 5;
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        if (!(preference instanceof ListPreference)) {
            return false;
        }
        OneHandedSettingsUtils.setTimeoutValue(this.mContext, Integer.parseInt((String) obj));
        updateState(preference);
        return true;
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        if (preference instanceof ListPreference) {
            ((ListPreference) preference).setValue(getTimeoutValue());
            int availabilityStatus = getAvailabilityStatus();
            boolean z = true;
            if (!(availabilityStatus == 0 || availabilityStatus == 1)) {
                z = false;
            }
            preference.setEnabled(z);
        }
    }

    public CharSequence getSummary() {
        if (OneHandedSettingsUtils.getTimeoutValue(this.mContext) == 0) {
            return this.mContext.getResources().getString(R.string.screensaver_settings_summary_never);
        }
        return String.format(this.mContext.getResources().getString(R.string.screen_timeout_summary), new Object[]{this.mTimeoutMap.get(getTimeoutValue())});
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mTimeoutPreference = preferenceScreen.findPreference(this.mPreferenceKey);
    }

    public void onStart() {
        this.mUtils.registerToggleAwareObserver(this);
    }

    public void onStop() {
        this.mUtils.unregisterToggleAwareObserver();
    }

    public void onChange(Uri uri) {
        updateState(this.mTimeoutPreference);
    }

    private String getTimeoutValue() {
        return String.valueOf(OneHandedSettingsUtils.getTimeoutValue(this.mContext));
    }

    private void initTimeoutMap() {
        if (this.mTimeoutMap.size() == 0) {
            String[] stringArray = this.mContext.getResources().getStringArray(R.array.one_handed_timeout_values);
            String[] stringArray2 = this.mContext.getResources().getStringArray(R.array.one_handed_timeout_title);
            if (stringArray.length == stringArray2.length) {
                for (int i = 0; i < stringArray.length; i++) {
                    this.mTimeoutMap.put(stringArray[i], stringArray2[i]);
                }
            }
        }
    }
}
