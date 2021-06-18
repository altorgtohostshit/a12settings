package com.android.settings.datetime.timezone.model;

import android.util.ArraySet;
import android.util.TimeUtils;
import com.android.i18n.timezone.CountryTimeZones;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class FilteredCountryTimeZones {
    private final Set<String> mAlternativeTimeZoneIds;
    private final CountryTimeZones mCountryTimeZones;
    private final List<String> mPreferredTimeZoneIds;

    public FilteredCountryTimeZones(CountryTimeZones countryTimeZones) {
        this.mCountryTimeZones = countryTimeZones;
        ArrayList arrayList = new ArrayList();
        ArraySet arraySet = new ArraySet();
        for (CountryTimeZones.TimeZoneMapping timeZoneMapping : countryTimeZones.getTimeZoneMappings()) {
            if (timeZoneMapping.isShownInPickerAt(TimeUtils.MIN_USE_DATE_OF_TIMEZONE)) {
                arrayList.add(timeZoneMapping.getTimeZoneId());
                arraySet.addAll(timeZoneMapping.getAlternativeIds());
            }
        }
        this.mPreferredTimeZoneIds = Collections.unmodifiableList(arrayList);
        this.mAlternativeTimeZoneIds = Collections.unmodifiableSet(arraySet);
    }

    public List<String> getPreferredTimeZoneIds() {
        return this.mPreferredTimeZoneIds;
    }

    public boolean matches(String str) {
        return this.mPreferredTimeZoneIds.contains(str) || this.mAlternativeTimeZoneIds.contains(str);
    }

    public String getRegionId() {
        return TimeZoneData.normalizeRegionId(this.mCountryTimeZones.getCountryIso());
    }
}
