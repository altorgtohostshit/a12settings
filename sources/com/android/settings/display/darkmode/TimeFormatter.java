package com.android.settings.display.darkmode;

import android.content.Context;
import java.text.DateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.TimeZone;

public class TimeFormatter {
    private final Context mContext;
    private final DateFormat mFormatter;

    public TimeFormatter(Context context) {
        this.mContext = context;
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(context);
        this.mFormatter = timeFormat;
        timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    /* renamed from: of */
    public String mo11836of(LocalTime localTime) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeZone(this.mFormatter.getTimeZone());
        instance.set(11, localTime.getHour());
        instance.set(12, localTime.getMinute());
        instance.set(13, 0);
        instance.set(14, 0);
        return this.mFormatter.format(instance.getTime());
    }

    public boolean is24HourFormat() {
        return android.text.format.DateFormat.is24HourFormat(this.mContext);
    }
}
