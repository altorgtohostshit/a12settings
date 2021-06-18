package com.android.settings.homepage.contextualcards.conditional;

import android.content.Context;
import android.os.PowerManager;
import com.android.settings.R;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.fuelgauge.BatterySaverReceiver;
import com.android.settings.fuelgauge.batterysaver.BatterySaverSettings;
import com.android.settings.homepage.contextualcards.ContextualCard;
import com.android.settings.homepage.contextualcards.conditional.ConditionalContextualCard;
import com.android.settingslib.fuelgauge.BatterySaverUtils;
import java.util.Objects;

public class BatterySaverConditionController implements ConditionalCardController, BatterySaverReceiver.BatterySaverListener {

    /* renamed from: ID */
    static final int f75ID = Objects.hash(new Object[]{"BatterySaverConditionController"});
    private final Context mAppContext;
    private final ConditionManager mConditionManager;
    private final PowerManager mPowerManager;
    private final BatterySaverReceiver mReceiver;

    public void onBatteryChanged(boolean z) {
    }

    public BatterySaverConditionController(Context context, ConditionManager conditionManager) {
        this.mAppContext = context;
        this.mConditionManager = conditionManager;
        this.mPowerManager = (PowerManager) context.getSystemService(PowerManager.class);
        BatterySaverReceiver batterySaverReceiver = new BatterySaverReceiver(context);
        this.mReceiver = batterySaverReceiver;
        batterySaverReceiver.setBatterySaverListener(this);
    }

    public long getId() {
        return (long) f75ID;
    }

    public boolean isDisplayable() {
        return this.mPowerManager.isPowerSaveMode();
    }

    public void onPrimaryClick(Context context) {
        new SubSettingLauncher(context).setDestination(BatterySaverSettings.class.getName()).setSourceMetricsCategory(35).setTitleRes(R.string.battery_saver).launch();
    }

    public void onActionClick() {
        BatterySaverUtils.setPowerSaveMode(this.mAppContext, false, false);
    }

    public ContextualCard buildContextualCard() {
        ConditionalContextualCard.Builder actionText = new ConditionalContextualCard.Builder().setConditionId((long) f75ID).setMetricsConstant(379).setActionText(this.mAppContext.getText(R.string.condition_turn_off));
        return actionText.setName(this.mAppContext.getPackageName() + "/" + this.mAppContext.getText(R.string.condition_battery_title)).setTitleText(this.mAppContext.getText(R.string.condition_battery_title).toString()).setSummaryText(this.mAppContext.getText(R.string.condition_battery_summary).toString()).setIconDrawable(this.mAppContext.getDrawable(R.drawable.ic_battery_saver_accent_24dp)).setViewType(R.layout.conditional_card_half_tile).build();
    }

    public void startMonitoringStateChange() {
        this.mReceiver.setListening(true);
    }

    public void stopMonitoringStateChange() {
        this.mReceiver.setListening(false);
    }

    public void onPowerSaveModeChanged() {
        this.mConditionManager.onConditionChanged();
    }
}
