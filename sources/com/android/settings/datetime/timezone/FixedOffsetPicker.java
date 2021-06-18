package com.android.settings.datetime.timezone;

import android.icu.util.TimeZone;
import com.android.settings.R;
import com.android.settings.datetime.timezone.TimeZoneInfo;
import com.android.settings.datetime.timezone.model.TimeZoneData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FixedOffsetPicker extends BaseTimeZoneInfoPicker {
    public int getMetricsCategory() {
        return 1357;
    }

    public FixedOffsetPicker() {
        super(R.string.date_time_select_fixed_offset_time_zones, R.string.search_settings, false, false);
    }

    public List<TimeZoneInfo> getAllTimeZoneInfos(TimeZoneData timeZoneData) {
        return loadFixedOffsets();
    }

    private List<TimeZoneInfo> loadFixedOffsets() {
        TimeZoneInfo.Formatter formatter = new TimeZoneInfo.Formatter(getLocale(), new Date());
        ArrayList arrayList = new ArrayList();
        arrayList.add(formatter.format(TimeZone.getFrozenTimeZone("Etc/UTC")));
        for (int i = 12; i >= -14; i--) {
            if (i != 0) {
                arrayList.add(formatter.format(TimeZone.getFrozenTimeZone(String.format(Locale.US, "Etc/GMT%+d", new Object[]{Integer.valueOf(i)}))));
            }
        }
        return Collections.unmodifiableList(arrayList);
    }
}
