package com.android.settings.emergency;

import android.content.Context;
import android.content.IntentFilter;
import android.widget.Switch;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.emergencynumber.EmergencyNumberUtils;
import com.android.settingslib.widget.MainSwitchPreference;
import com.android.settingslib.widget.OnMainSwitchChangeListener;

public class EmergencyGesturePreferenceController extends BasePreferenceController implements OnMainSwitchChangeListener {
    EmergencyNumberUtils mEmergencyNumberUtils;
    private MainSwitchPreference mSwitchBar;

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

    public EmergencyGesturePreferenceController(Context context, String str) {
        super(context, str);
        this.mEmergencyNumberUtils = new EmergencyNumberUtils(context);
    }

    public int getAvailabilityStatus() {
        return !this.mContext.getResources().getBoolean(R.bool.config_show_emergency_gesture_settings) ? 3 : 0;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        MainSwitchPreference mainSwitchPreference = (MainSwitchPreference) preferenceScreen.findPreference(this.mPreferenceKey);
        this.mSwitchBar = mainSwitchPreference;
        mainSwitchPreference.setTitle(this.mContext.getString(R.string.emergency_gesture_switchbar_title));
        this.mSwitchBar.addOnSwitchChangeListener(this);
        this.mSwitchBar.updateStatus(isChecked());
    }

    public boolean isChecked() {
        return this.mEmergencyNumberUtils.getEmergencyGestureEnabled();
    }

    public void onSwitchChanged(Switch switchR, boolean z) {
        this.mEmergencyNumberUtils.setEmergencyGestureEnabled(z);
    }
}
