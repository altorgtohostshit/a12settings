package com.android.settings.widget;

import android.content.Context;
import android.content.IntentFilter;
import android.widget.Switch;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.widget.MainSwitchPreference;
import com.android.settingslib.widget.OnMainSwitchChangeListener;

public abstract class SettingsMainSwitchPreferenceController extends TogglePreferenceController implements OnMainSwitchChangeListener {
    protected MainSwitchPreference mSwitchPreference;

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

    public SettingsMainSwitchPreferenceController(Context context, String str) {
        super(context, str);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        Preference findPreference = preferenceScreen.findPreference(getPreferenceKey());
        if (findPreference != null && (findPreference instanceof MainSwitchPreference)) {
            MainSwitchPreference mainSwitchPreference = (MainSwitchPreference) findPreference;
            this.mSwitchPreference = mainSwitchPreference;
            mainSwitchPreference.addOnSwitchChangeListener(this);
        }
    }

    public void onSwitchChanged(Switch switchR, boolean z) {
        this.mSwitchPreference.setChecked(z);
        setChecked(z);
    }
}
