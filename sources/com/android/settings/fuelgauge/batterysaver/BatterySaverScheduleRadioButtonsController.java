package com.android.settings.fuelgauge.batterysaver;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import com.android.settingslib.fuelgauge.BatterySaverUtils;

public class BatterySaverScheduleRadioButtonsController {
    private Context mContext;
    private BatterySaverScheduleSeekBarController mSeekBarController;

    public BatterySaverScheduleRadioButtonsController(Context context, BatterySaverScheduleSeekBarController batterySaverScheduleSeekBarController) {
        this.mContext = context;
        this.mSeekBarController = batterySaverScheduleSeekBarController;
    }

    public String getDefaultKey() {
        ContentResolver contentResolver = this.mContext.getContentResolver();
        if (Settings.Global.getInt(contentResolver, "automatic_power_save_mode", 0) == 0) {
            return Settings.Global.getInt(contentResolver, "low_power_trigger_level", 0) <= 0 ? "key_battery_saver_no_schedule" : "key_battery_saver_percentage";
        }
        return "key_battery_saver_routine";
    }

    public boolean setDefaultKey(String str) {
        int i;
        int i2;
        int i3 = 0;
        if (str == null) {
            return false;
        }
        ContentResolver contentResolver = this.mContext.getContentResolver();
        Bundle bundle = new Bundle(3);
        char c = 65535;
        switch (str.hashCode()) {
            case 188396680:
                if (str.equals("key_battery_saver_routine")) {
                    c = 0;
                    break;
                }
                break;
            case 225734937:
                if (str.equals("key_battery_saver_no_schedule")) {
                    c = 1;
                    break;
                }
                break;
            case 1335810134:
                if (str.equals("key_battery_saver_percentage")) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                bundle.putBoolean("extra_confirm_only", true);
                bundle.putInt("extra_power_save_mode_trigger", 1);
                i2 = 0;
                i = 1;
                break;
            case 1:
                i2 = 0;
                i = 0;
                break;
            case 2:
                i2 = 5;
                bundle.putBoolean("extra_confirm_only", true);
                bundle.putInt("extra_power_save_mode_trigger", 0);
                bundle.putInt("extra_power_save_mode_trigger_level", 5);
                i = 0;
                break;
            default:
                throw new IllegalStateException("Not a valid key for " + BatterySaverScheduleRadioButtonsController.class.getSimpleName());
        }
        if (TextUtils.equals(str, "key_battery_saver_no_schedule") || !BatterySaverUtils.maybeShowBatterySaverConfirmation(this.mContext, bundle)) {
            i3 = i;
        } else {
            i2 = 0;
        }
        Settings.Global.putInt(contentResolver, "automatic_power_save_mode", i3);
        if (i3 != 1) {
            Settings.Global.putInt(contentResolver, "low_power_trigger_level", i2);
        }
        if (i3 == 1 || i2 != 0) {
            BatterySaverUtils.suppressAutoBatterySaver(this.mContext);
        }
        this.mSeekBarController.updateSeekBar();
        return true;
    }
}
