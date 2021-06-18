package com.android.settings.display;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.hardware.display.ColorDisplayManager;
import android.hardware.display.NightDisplayListener;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import java.time.LocalTime;

public class NightDisplaySettings extends DashboardFragment implements NightDisplayListener.Callback {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.night_display_settings) {
        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            return ColorDisplayManager.isNightDisplayAvailable(context);
        }
    };
    private ColorDisplayManager mColorDisplayManager;
    private NightDisplayListener mNightDisplayListener;

    public int getDialogMetricsCategory(int i) {
        if (i != 0) {
            return i != 1 ? 0 : 589;
        }
        return 588;
    }

    public int getHelpResource() {
        return R.string.help_url_night_display;
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "NightDisplaySettings";
    }

    public int getMetricsCategory() {
        return 488;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.night_display_settings;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Context context = getContext();
        this.mColorDisplayManager = (ColorDisplayManager) context.getSystemService(ColorDisplayManager.class);
        this.mNightDisplayListener = new NightDisplayListener(context);
    }

    public void onStart() {
        super.onStart();
        this.mNightDisplayListener.setCallback(this);
    }

    public void onStop() {
        super.onStop();
        this.mNightDisplayListener.setCallback((NightDisplayListener.Callback) null);
    }

    public boolean onPreferenceTreeClick(Preference preference) {
        if ("night_display_end_time".equals(preference.getKey())) {
            writePreferenceClickMetric(preference);
            showDialog(1);
            return true;
        } else if (!"night_display_start_time".equals(preference.getKey())) {
            return super.onPreferenceTreeClick(preference);
        } else {
            writePreferenceClickMetric(preference);
            showDialog(0);
            return true;
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$0(int i, TimePicker timePicker, int i2, int i3) {
        LocalTime of = LocalTime.of(i2, i3);
        if (i == 0) {
            this.mColorDisplayManager.setNightDisplayCustomStartTime(of);
        } else {
            this.mColorDisplayManager.setNightDisplayCustomEndTime(of);
        }
    }

    public Dialog onCreateDialog(int i) {
        LocalTime localTime;
        if (i != 0 && i != 1) {
            return super.onCreateDialog(i);
        }
        if (i == 0) {
            localTime = this.mColorDisplayManager.getNightDisplayCustomStartTime();
        } else {
            localTime = this.mColorDisplayManager.getNightDisplayCustomEndTime();
        }
        Context context = getContext();
        return new TimePickerDialog(context, new NightDisplaySettings$$ExternalSyntheticLambda0(this, i), localTime.getHour(), localTime.getMinute(), DateFormat.is24HourFormat(context));
    }

    public void onActivated(boolean z) {
        updatePreferenceStates();
    }

    public void onAutoModeChanged(int i) {
        updatePreferenceStates();
    }

    public void onColorTemperatureChanged(int i) {
        updatePreferenceStates();
    }

    public void onCustomStartTimeChanged(LocalTime localTime) {
        updatePreferenceStates();
    }

    public void onCustomEndTimeChanged(LocalTime localTime) {
        updatePreferenceStates();
    }
}
