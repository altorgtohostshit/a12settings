package com.android.settings.display.darkmode;

import android.app.TimePickerDialog;
import android.app.UiModeManager;
import android.content.Context;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.widget.TimePicker;
import androidx.preference.Preference;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DarkModeCustomPreferenceController extends BasePreferenceController {
    private static final String END_TIME_KEY = "dark_theme_end_time";
    private static final String START_TIME_KEY = "dark_theme_start_time";
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
    private TimeFormatter mFormat;
    private DarkModeSettingsFragment mFragmet;
    private final UiModeManager mUiModeManager;

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

    public DarkModeCustomPreferenceController(Context context, String str) {
        super(context, str);
        this.mFormat = new TimeFormatter(this.mContext);
        this.mUiModeManager = (UiModeManager) context.getSystemService(UiModeManager.class);
    }

    public DarkModeCustomPreferenceController(Context context, String str, DarkModeSettingsFragment darkModeSettingsFragment) {
        this(context, str);
        this.mFragmet = darkModeSettingsFragment;
    }

    public DarkModeCustomPreferenceController(Context context, String str, DarkModeSettingsFragment darkModeSettingsFragment, TimeFormatter timeFormatter) {
        this(context, str, darkModeSettingsFragment);
        this.mFormat = timeFormatter;
    }

    public TimePickerDialog getDialog() {
        LocalTime localTime;
        if (TextUtils.equals(getPreferenceKey(), START_TIME_KEY)) {
            localTime = this.mUiModeManager.getCustomNightModeStart();
        } else {
            localTime = this.mUiModeManager.getCustomNightModeEnd();
        }
        return new TimePickerDialog(this.mContext, new DarkModeCustomPreferenceController$$ExternalSyntheticLambda0(this), localTime.getHour(), localTime.getMinute(), this.mFormat.is24HourFormat());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$getDialog$0(TimePicker timePicker, int i, int i2) {
        LocalTime of = LocalTime.of(i, i2);
        if (TextUtils.equals(getPreferenceKey(), START_TIME_KEY)) {
            this.mUiModeManager.setCustomNightModeStart(of);
        } else {
            this.mUiModeManager.setCustomNightModeEnd(of);
        }
        DarkModeSettingsFragment darkModeSettingsFragment = this.mFragmet;
        if (darkModeSettingsFragment != null) {
            darkModeSettingsFragment.refresh();
        }
    }

    /* access modifiers changed from: protected */
    public void refreshSummary(Preference preference) {
        LocalTime localTime;
        if (this.mUiModeManager.getNightMode() != 3) {
            preference.setVisible(false);
            return;
        }
        preference.setVisible(true);
        if (TextUtils.equals(getPreferenceKey(), START_TIME_KEY)) {
            localTime = this.mUiModeManager.getCustomNightModeStart();
        } else {
            localTime = this.mUiModeManager.getCustomNightModeEnd();
        }
        preference.setSummary((CharSequence) this.mFormat.mo11836of(localTime));
    }
}
