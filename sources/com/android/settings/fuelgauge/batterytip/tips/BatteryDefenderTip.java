package com.android.settings.fuelgauge.batterytip.tips;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.settings.R;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;

public class BatteryDefenderTip extends BatteryTip {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public BatteryTip createFromParcel(Parcel parcel) {
            return new BatteryDefenderTip(parcel);
        }

        public BatteryTip[] newArray(int i) {
            return new BatteryDefenderTip[i];
        }
    };

    public int getIconId() {
        return R.drawable.ic_battery_status_good_24dp;
    }

    public void log(Context context, MetricsFeatureProvider metricsFeatureProvider) {
    }

    public BatteryDefenderTip(int i) {
        super(8, i, false);
    }

    private BatteryDefenderTip(Parcel parcel) {
        super(parcel);
    }

    public CharSequence getTitle(Context context) {
        return context.getString(R.string.battery_tip_limited_temporarily_title);
    }

    public CharSequence getSummary(Context context) {
        return context.getString(R.string.battery_tip_limited_temporarily_summary);
    }

    public void updateState(BatteryTip batteryTip) {
        this.mState = batteryTip.mState;
    }
}
