package com.android.settings.datetime.timezone;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.text.format.DateFormat;
import com.android.settings.R;
import com.android.settings.datetime.timezone.BaseTimeZoneAdapter;
import com.android.settings.datetime.timezone.BaseTimeZonePicker;
import com.android.settings.datetime.timezone.model.TimeZoneData;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public abstract class BaseTimeZoneInfoPicker extends BaseTimeZonePicker {
    protected ZoneAdapter mAdapter;

    public abstract List<TimeZoneInfo> getAllTimeZoneInfos(TimeZoneData timeZoneData);

    /* access modifiers changed from: protected */
    public CharSequence getHeaderText() {
        return null;
    }

    protected BaseTimeZoneInfoPicker(int i, int i2, boolean z, boolean z2) {
        super(i, i2, z, z2);
    }

    /* access modifiers changed from: protected */
    public BaseTimeZoneAdapter createAdapter(TimeZoneData timeZoneData) {
        ZoneAdapter zoneAdapter = new ZoneAdapter(getContext(), getAllTimeZoneInfos(timeZoneData), new BaseTimeZoneInfoPicker$$ExternalSyntheticLambda0(this), getLocale(), getHeaderText());
        this.mAdapter = zoneAdapter;
        return zoneAdapter;
    }

    /* access modifiers changed from: private */
    public void onListItemClick(TimeZoneInfoItem timeZoneInfoItem) {
        getActivity().setResult(-1, prepareResultData(timeZoneInfoItem.mTimeZoneInfo));
        getActivity().finish();
    }

    /* access modifiers changed from: protected */
    public Intent prepareResultData(TimeZoneInfo timeZoneInfo) {
        return new Intent().putExtra("com.android.settings.datetime.timezone.result_time_zone_id", timeZoneInfo.getId());
    }

    protected static class ZoneAdapter extends BaseTimeZoneAdapter<TimeZoneInfoItem> {
        public ZoneAdapter(Context context, List<TimeZoneInfo> list, BaseTimeZonePicker.OnListItemClickListener<TimeZoneInfoItem> onListItemClickListener, Locale locale, CharSequence charSequence) {
            super(createTimeZoneInfoItems(context, list, locale), onListItemClickListener, locale, true, charSequence);
        }

        private static List<TimeZoneInfoItem> createTimeZoneInfoItems(Context context, List<TimeZoneInfo> list, Locale locale) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateFormat.getTimeFormatString(context), locale);
            ArrayList arrayList = new ArrayList(list.size());
            Resources resources = context.getResources();
            long j = 0;
            for (TimeZoneInfo timeZoneInfoItem : list) {
                arrayList.add(new TimeZoneInfoItem(j, timeZoneInfoItem, resources, simpleDateFormat));
                j++;
            }
            return arrayList;
        }
    }

    private static class TimeZoneInfoItem implements BaseTimeZoneAdapter.AdapterItem {
        private final long mItemId;
        private final Resources mResources;
        private final String[] mSearchKeys;
        private final android.icu.text.DateFormat mTimeFormat;
        /* access modifiers changed from: private */
        public final TimeZoneInfo mTimeZoneInfo;
        private final String mTitle;

        public String getIconText() {
            return null;
        }

        private TimeZoneInfoItem(long j, TimeZoneInfo timeZoneInfo, Resources resources, android.icu.text.DateFormat dateFormat) {
            this.mItemId = j;
            this.mTimeZoneInfo = timeZoneInfo;
            this.mResources = resources;
            this.mTimeFormat = dateFormat;
            String createTitle = createTitle(timeZoneInfo);
            this.mTitle = createTitle;
            this.mSearchKeys = new String[]{createTitle};
        }

        private static String createTitle(TimeZoneInfo timeZoneInfo) {
            String exemplarLocation = timeZoneInfo.getExemplarLocation();
            if (exemplarLocation == null) {
                exemplarLocation = timeZoneInfo.getGenericName();
            }
            if (exemplarLocation == null && timeZoneInfo.getTimeZone().inDaylightTime(new Date())) {
                exemplarLocation = timeZoneInfo.getDaylightName();
            }
            if (exemplarLocation == null) {
                exemplarLocation = timeZoneInfo.getStandardName();
            }
            return exemplarLocation == null ? String.valueOf(timeZoneInfo.getGmtOffset()) : exemplarLocation;
        }

        public CharSequence getTitle() {
            return this.mTitle;
        }

        public CharSequence getSummary() {
            String genericName = this.mTimeZoneInfo.getGenericName();
            if (genericName == null) {
                if (this.mTimeZoneInfo.getTimeZone().inDaylightTime(new Date())) {
                    genericName = this.mTimeZoneInfo.getDaylightName();
                } else {
                    genericName = this.mTimeZoneInfo.getStandardName();
                }
            }
            if (genericName == null || genericName.equals(this.mTitle)) {
                CharSequence gmtOffset = this.mTimeZoneInfo.getGmtOffset();
                return (gmtOffset == null || gmtOffset.toString().equals(this.mTitle)) ? "" : gmtOffset;
            }
            return SpannableUtil.getResourcesText(this.mResources, R.string.zone_info_offset_and_name, this.mTimeZoneInfo.getGmtOffset(), genericName);
        }

        public String getCurrentTime() {
            return this.mTimeFormat.format(Calendar.getInstance(this.mTimeZoneInfo.getTimeZone()));
        }

        public long getItemId() {
            return this.mItemId;
        }

        public String[] getSearchKeys() {
            return this.mSearchKeys;
        }
    }
}
