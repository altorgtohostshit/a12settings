package com.android.settings.datetime.timezone;

import com.android.settings.datetime.timezone.model.TimeZoneData;
import com.android.settings.datetime.timezone.model.TimeZoneDataLoader;

public final /* synthetic */ class TimeZoneSettings$$ExternalSyntheticLambda3 implements TimeZoneDataLoader.OnDataReadyCallback {
    public final /* synthetic */ TimeZoneSettings f$0;

    public /* synthetic */ TimeZoneSettings$$ExternalSyntheticLambda3(TimeZoneSettings timeZoneSettings) {
        this.f$0 = timeZoneSettings;
    }

    public final void onTimeZoneDataReady(TimeZoneData timeZoneData) {
        this.f$0.onTimeZoneDataReady(timeZoneData);
    }
}
