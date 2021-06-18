package com.android.settings.datetime;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.DateFormat;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;
import androidx.preference.TwoStatePreference;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.Calendar;

public class TimeFormatPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin {
    private final Calendar mDummyDate = Calendar.getInstance();
    private final boolean mIsFromSUW;
    private final UpdateTimeAndDateCallback mUpdateTimeAndDateCallback;

    public String getPreferenceKey() {
        return "24 hour";
    }

    public TimeFormatPreferenceController(Context context, UpdateTimeAndDateCallback updateTimeAndDateCallback, boolean z) {
        super(context);
        this.mIsFromSUW = z;
        this.mUpdateTimeAndDateCallback = updateTimeAndDateCallback;
    }

    public boolean isAvailable() {
        return !this.mIsFromSUW;
    }

    public void updateState(Preference preference) {
        if (preference instanceof TwoStatePreference) {
            preference.setEnabled(!AutoTimeFormatPreferenceController.isAutoTimeFormatSelection(this.mContext));
            ((TwoStatePreference) preference).setChecked(is24Hour());
            Calendar instance = Calendar.getInstance();
            this.mDummyDate.setTimeZone(instance.getTimeZone());
            this.mDummyDate.set(instance.get(1), 11, 31, 13, 0, 0);
            preference.setSummary((CharSequence) DateFormat.getTimeFormat(this.mContext).format(this.mDummyDate.getTime()));
        }
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!(preference instanceof TwoStatePreference) || !TextUtils.equals("24 hour", preference.getKey())) {
            return false;
        }
        update24HourFormat(this.mContext, Boolean.valueOf(((SwitchPreference) preference).isChecked()));
        this.mUpdateTimeAndDateCallback.updateTimeAndDateDisplay(this.mContext);
        return true;
    }

    private boolean is24Hour() {
        return DateFormat.is24HourFormat(this.mContext);
    }

    static void update24HourFormat(Context context, Boolean bool) {
        set24Hour(context, bool);
        timeUpdated(context, bool);
    }

    static void timeUpdated(Context context, Boolean bool) {
        int i;
        Intent intent = new Intent("android.intent.action.TIME_SET");
        intent.addFlags(16777216);
        if (bool == null) {
            i = 2;
        } else {
            i = bool.booleanValue();
        }
        intent.putExtra("android.intent.extra.TIME_PREF_24_HOUR_FORMAT", i);
        context.sendBroadcast(intent);
    }

    static void set24Hour(Context context, Boolean bool) {
        Settings.System.putString(context.getContentResolver(), "time_12_24", bool == null ? null : bool.booleanValue() ? "24" : "12");
    }
}
