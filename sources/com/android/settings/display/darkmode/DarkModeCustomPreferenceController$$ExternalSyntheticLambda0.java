package com.android.settings.display.darkmode;

import android.app.TimePickerDialog;
import android.widget.TimePicker;

public final /* synthetic */ class DarkModeCustomPreferenceController$$ExternalSyntheticLambda0 implements TimePickerDialog.OnTimeSetListener {
    public final /* synthetic */ DarkModeCustomPreferenceController f$0;

    public /* synthetic */ DarkModeCustomPreferenceController$$ExternalSyntheticLambda0(DarkModeCustomPreferenceController darkModeCustomPreferenceController) {
        this.f$0 = darkModeCustomPreferenceController;
    }

    public final void onTimeSet(TimePicker timePicker, int i, int i2) {
        this.f$0.lambda$getDialog$0(timePicker, i, i2);
    }
}
