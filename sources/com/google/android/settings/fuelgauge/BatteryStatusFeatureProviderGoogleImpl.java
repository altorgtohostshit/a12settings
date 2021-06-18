package com.google.android.settings.fuelgauge;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import android.text.format.DateFormat;
import com.android.settings.R;
import com.android.settings.fuelgauge.BatteryInfo;
import com.android.settings.fuelgauge.BatteryPreferenceController;
import com.android.settings.fuelgauge.BatteryStatusFeatureProviderImpl;
import com.android.settings.fuelgauge.BatteryUtils;
import com.google.android.settings.fuelgauge.reversecharging.ReverseChargingManager;
import com.google.android.systemui.adaptivecharging.AdaptiveChargingManager;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class BatteryStatusFeatureProviderGoogleImpl extends BatteryStatusFeatureProviderImpl {
    private boolean mAdaptiveChargingEnabledInSettings;
    private AdaptiveChargingManager mAdaptiveChargingManager;
    private ReverseChargingManager mReverseChargingManager;

    public BatteryStatusFeatureProviderGoogleImpl(Context context) {
        super(context);
        this.mAdaptiveChargingManager = new AdaptiveChargingManager(context);
        this.mReverseChargingManager = ReverseChargingManager.getInstance(context);
        this.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("adaptive_charging_enabled"), false, new ContentObserver((Handler) null) {
            public void onChange(boolean z) {
                BatteryStatusFeatureProviderGoogleImpl.this.refreshAdaptiveChargingEnabled();
            }
        });
        refreshAdaptiveChargingEnabled();
    }

    /* access modifiers changed from: private */
    public void refreshAdaptiveChargingEnabled() {
        this.mAdaptiveChargingEnabledInSettings = this.mAdaptiveChargingManager.isAvailable() && this.mAdaptiveChargingManager.isEnabled();
    }

    public boolean triggerBatteryStatusUpdate(final BatteryPreferenceController batteryPreferenceController, final BatteryInfo batteryInfo) {
        if (this.mReverseChargingManager.isReverseChargingOn() && !BatteryUtils.isBatteryDefenderOn(batteryInfo)) {
            String string = this.mContext.getString(batteryInfo.discharging ? R.string.reverse_charging_is_on_and_discharging_summary : R.string.reverse_charging_is_on_and_charging_summary);
            batteryInfo.statusLabel = string;
            CharSequence charSequence = batteryInfo.remainingLabel;
            if (charSequence != null) {
                string = this.mContext.getString(R.string.battery_state_and_duration, new Object[]{string, charSequence});
            }
            batteryPreferenceController.updateBatteryStatus(string, batteryInfo);
            return true;
        } else if (batteryInfo.discharging || BatteryUtils.isBatteryDefenderOn(batteryInfo) || !this.mAdaptiveChargingEnabledInSettings) {
            return false;
        } else {
            this.mAdaptiveChargingManager.queryStatus(new AdaptiveChargingManager.AdaptiveChargingStatusReceiver() {
                private boolean mSetStatus;

                public void onReceiveStatus(String str, int i) {
                    if (AdaptiveChargingManager.isStageActiveOrEnabled(str) && i > 0) {
                        batteryPreferenceController.updateBatteryStatus(BatteryStatusFeatureProviderGoogleImpl.this.mContext.getResources().getString(R.string.adaptive_charging_time_estimate, new Object[]{DateFormat.format(DateFormat.getBestDateTimePattern(Locale.getDefault(), DateFormat.is24HourFormat(BatteryStatusFeatureProviderGoogleImpl.this.mContext) ? "Hm" : "hma"), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis((long) (i + 29))).toString()}), batteryInfo);
                        this.mSetStatus = true;
                    }
                }

                public void onDestroyInterface() {
                    if (!this.mSetStatus) {
                        batteryPreferenceController.updateBatteryStatus((String) null, batteryInfo);
                    }
                }
            });
            return true;
        }
    }
}
