package com.android.settings.datetime.timezone;

import com.android.settings.datetime.timezone.model.TimeZoneData;
import com.android.settings.datetime.timezone.model.TimeZoneDataLoader;

public final /* synthetic */ class BaseTimeZonePicker$$ExternalSyntheticLambda0 implements TimeZoneDataLoader.OnDataReadyCallback {
    public final /* synthetic */ BaseTimeZonePicker f$0;

    public /* synthetic */ BaseTimeZonePicker$$ExternalSyntheticLambda0(BaseTimeZonePicker baseTimeZonePicker) {
        this.f$0 = baseTimeZonePicker;
    }

    public final void onTimeZoneDataReady(TimeZoneData timeZoneData) {
        this.f$0.onTimeZoneDataReady(timeZoneData);
    }
}
