package com.android.settings.development.storage;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import java.util.Locale;

class SharedDataUtils {
    private static final Calendar CALENDAR = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss z");

    static String formatTime(long j) {
        Calendar calendar = CALENDAR;
        calendar.setTimeInMillis(j);
        return FORMATTER.format(calendar.getTime());
    }

    static String formatSize(long j) {
        return String.format("%.2f MB", new Object[]{Double.valueOf(((double) j) / 1048576.0d)});
    }
}
