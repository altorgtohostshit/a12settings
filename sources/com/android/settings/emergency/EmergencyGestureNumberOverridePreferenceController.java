package com.android.settings.emergency;

import android.content.Context;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import android.telephony.PhoneNumberUtils;
import android.text.Spannable;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import com.android.settingslib.emergencynumber.EmergencyNumberUtils;

public class EmergencyGestureNumberOverridePreferenceController extends BasePreferenceController implements LifecycleObserver, OnStart, OnStop {
    EmergencyNumberUtils mEmergencyNumberUtils;
    private final Handler mHandler;
    /* access modifiers changed from: private */
    public Preference mPreference;
    private final ContentObserver mSettingsObserver;

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

    public EmergencyGestureNumberOverridePreferenceController(Context context, String str) {
        super(context, str);
        this.mEmergencyNumberUtils = new EmergencyNumberUtils(context);
        Handler handler = new Handler(Looper.getMainLooper());
        this.mHandler = handler;
        this.mSettingsObserver = new EmergencyGestureNumberOverrideSettingsObserver(handler);
    }

    public int getAvailabilityStatus() {
        return this.mContext.getResources().getBoolean(R.bool.config_show_emergency_gesture_settings) ? 0 : 3;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    public CharSequence getSummary() {
        String policeNumber = this.mEmergencyNumberUtils.getPoliceNumber();
        String string = this.mContext.getString(R.string.emergency_gesture_call_for_help_summary, new Object[]{policeNumber});
        int indexOf = string.indexOf(policeNumber);
        if (indexOf < 0) {
            return string;
        }
        Spannable newSpannable = Spannable.Factory.getInstance().newSpannable(string);
        PhoneNumberUtils.addTtsSpan(newSpannable, indexOf, policeNumber.length() + indexOf);
        return newSpannable;
    }

    public void onStart() {
        this.mContext.getContentResolver().registerContentObserver(EmergencyNumberUtils.EMERGENCY_NUMBER_OVERRIDE_AUTHORITY, false, this.mSettingsObserver);
    }

    public void onStop() {
        this.mContext.getContentResolver().unregisterContentObserver(this.mSettingsObserver);
    }

    private class EmergencyGestureNumberOverrideSettingsObserver extends ContentObserver {
        EmergencyGestureNumberOverrideSettingsObserver(Handler handler) {
            super(handler);
        }

        public void onChange(boolean z) {
            if (EmergencyGestureNumberOverridePreferenceController.this.mPreference != null) {
                EmergencyGestureNumberOverridePreferenceController emergencyGestureNumberOverridePreferenceController = EmergencyGestureNumberOverridePreferenceController.this;
                emergencyGestureNumberOverridePreferenceController.updateState(emergencyGestureNumberOverridePreferenceController.mPreference);
            }
        }
    }
}
