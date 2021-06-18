package com.android.settings.fuelgauge.batterysaver;

import android.content.Context;
import android.content.IntentFilter;
import android.icu.text.NumberFormat;
import android.provider.Settings;
import android.text.TextUtils;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;
import com.android.settings.R;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;

public class BatterySaverStickyPreferenceController extends TogglePreferenceController implements PreferenceControllerMixin {
    private Context mContext;

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

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public BatterySaverStickyPreferenceController(Context context, String str) {
        super(context, str);
        this.mContext = context;
    }

    public boolean isChecked() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "low_power_sticky_auto_disable_enabled", 1) == 1;
    }

    public boolean setChecked(boolean z) {
        Settings.Global.putInt(this.mContext.getContentResolver(), "low_power_sticky_auto_disable_enabled", z ? 1 : 0);
        return true;
    }

    /* access modifiers changed from: protected */
    public void refreshSummary(Preference preference) {
        super.refreshSummary(preference);
        int i = Settings.Global.getInt(this.mContext.getContentResolver(), "low_power_sticky_auto_disable_level", 90);
        preference.setSummary(TextUtils.expandTemplate(this.mContext.getString(R.string.battery_saver_sticky_description_new), new CharSequence[]{NumberFormat.getIntegerInstance().format((long) i)}));
    }

    public void updateState(Preference preference) {
        boolean z = true;
        SwitchPreference switchPreference = (SwitchPreference) preference;
        if (Settings.Global.getInt(this.mContext.getContentResolver(), "low_power_sticky_auto_disable_enabled", 1) != 1) {
            z = false;
        }
        switchPreference.setChecked(z);
        refreshSummary(preference);
    }
}
