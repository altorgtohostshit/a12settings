package com.android.settings.fuelgauge.batterytip.tips;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.settings.fuelgauge.batterytip.AppInfo;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;

public class UnrestrictAppTip extends BatteryTip {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public BatteryTip createFromParcel(Parcel parcel) {
            return new UnrestrictAppTip(parcel);
        }

        public BatteryTip[] newArray(int i) {
            return new UnrestrictAppTip[i];
        }
    };
    private AppInfo mAppInfo;

    public int getIconId() {
        return 0;
    }

    public CharSequence getSummary(Context context) {
        return null;
    }

    public CharSequence getTitle(Context context) {
        return null;
    }

    public void log(Context context, MetricsFeatureProvider metricsFeatureProvider) {
    }

    public UnrestrictAppTip(int i, AppInfo appInfo) {
        super(7, i, true);
        this.mAppInfo = appInfo;
    }

    UnrestrictAppTip(Parcel parcel) {
        super(parcel);
        this.mAppInfo = (AppInfo) parcel.readParcelable(getClass().getClassLoader());
    }

    public String getPackageName() {
        return this.mAppInfo.packageName;
    }

    public void updateState(BatteryTip batteryTip) {
        this.mState = batteryTip.mState;
    }

    public AppInfo getUnrestrictAppInfo() {
        return this.mAppInfo;
    }

    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeParcelable(this.mAppInfo, i);
    }
}
