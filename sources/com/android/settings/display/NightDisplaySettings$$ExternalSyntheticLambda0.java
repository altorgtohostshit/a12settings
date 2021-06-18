package com.android.settings.display;

import android.app.TimePickerDialog;
import android.widget.TimePicker;

public final /* synthetic */ class NightDisplaySettings$$ExternalSyntheticLambda0 implements TimePickerDialog.OnTimeSetListener {
    public final /* synthetic */ NightDisplaySettings f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ NightDisplaySettings$$ExternalSyntheticLambda0(NightDisplaySettings nightDisplaySettings, int i) {
        this.f$0 = nightDisplaySettings;
        this.f$1 = i;
    }

    public final void onTimeSet(TimePicker timePicker, int i, int i2) {
        this.f$0.lambda$onCreateDialog$0(this.f$1, timePicker, i, i2);
    }
}
