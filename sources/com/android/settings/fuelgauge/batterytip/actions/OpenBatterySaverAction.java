package com.android.settings.fuelgauge.batterytip.actions;

import android.content.Context;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.fuelgauge.batterysaver.BatterySaverSettings;

public class OpenBatterySaverAction extends BatteryTipAction {
    public OpenBatterySaverAction(Context context) {
        super(context);
    }

    public void handlePositiveAction(int i) {
        this.mMetricsFeatureProvider.action(this.mContext, 1388, i);
        new SubSettingLauncher(this.mContext).setDestination(BatterySaverSettings.class.getName()).setSourceMetricsCategory(i).launch();
    }
}
