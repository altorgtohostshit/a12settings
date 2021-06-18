package com.android.settings.datetime.timezone;

import com.android.settings.datetime.timezone.BaseTimeZoneAdapter;
import com.android.settings.datetime.timezone.BaseTimeZoneInfoPicker;
import com.android.settings.datetime.timezone.BaseTimeZonePicker;

public final /* synthetic */ class BaseTimeZoneInfoPicker$$ExternalSyntheticLambda0 implements BaseTimeZonePicker.OnListItemClickListener {
    public final /* synthetic */ BaseTimeZoneInfoPicker f$0;

    public /* synthetic */ BaseTimeZoneInfoPicker$$ExternalSyntheticLambda0(BaseTimeZoneInfoPicker baseTimeZoneInfoPicker) {
        this.f$0 = baseTimeZoneInfoPicker;
    }

    public final void onListItemClick(BaseTimeZoneAdapter.AdapterItem adapterItem) {
        this.f$0.onListItemClick((BaseTimeZoneInfoPicker.TimeZoneInfoItem) adapterItem);
    }
}
