package com.android.settings.homepage.contextualcards.conditional;

import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.PreciseDataConnectionState;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import com.android.settings.R;
import com.android.settings.Settings;
import com.android.settings.homepage.contextualcards.ContextualCard;
import com.android.settings.homepage.contextualcards.conditional.ConditionalContextualCard;
import com.android.settings.network.GlobalSettingsChangeListener;
import java.util.Objects;

public class CellularDataConditionController implements ConditionalCardController {

    /* renamed from: ID */
    static final int f76ID = Objects.hash(new Object[]{"CellularDataConditionController"});
    /* access modifiers changed from: private */
    public final Context mAppContext;
    /* access modifiers changed from: private */
    public final ConditionManager mConditionManager;
    private final GlobalSettingsChangeListener mDefaultDataSubscriptionIdListener;
    /* access modifiers changed from: private */
    public boolean mIsListeningConnectionChange;
    private final PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
        public void onPreciseDataConnectionStateChanged(PreciseDataConnectionState preciseDataConnectionState) {
            CellularDataConditionController.this.mConditionManager.onConditionChanged();
        }
    };
    /* access modifiers changed from: private */
    public int mSubId;
    private TelephonyManager mTelephonyManager;

    public CellularDataConditionController(Context context, ConditionManager conditionManager) {
        this.mAppContext = context;
        this.mConditionManager = conditionManager;
        int defaultDataSubscriptionId = getDefaultDataSubscriptionId(context);
        this.mSubId = defaultDataSubscriptionId;
        this.mTelephonyManager = getTelephonyManager(context, defaultDataSubscriptionId);
        this.mDefaultDataSubscriptionIdListener = new GlobalSettingsChangeListener(context, "multi_sim_data_call") {
            public void onChanged(String str) {
                CellularDataConditionController cellularDataConditionController = CellularDataConditionController.this;
                int access$100 = cellularDataConditionController.getDefaultDataSubscriptionId(cellularDataConditionController.mAppContext);
                if (access$100 != CellularDataConditionController.this.mSubId) {
                    int unused = CellularDataConditionController.this.mSubId = access$100;
                    if (CellularDataConditionController.this.mIsListeningConnectionChange) {
                        CellularDataConditionController cellularDataConditionController2 = CellularDataConditionController.this;
                        cellularDataConditionController2.restartPhoneStateListener(cellularDataConditionController2.mAppContext, access$100);
                    }
                }
            }
        };
    }

    public long getId() {
        return (long) f76ID;
    }

    public boolean isDisplayable() {
        if (!this.mTelephonyManager.isDataCapable() || this.mTelephonyManager.getSimState() != 5) {
            return false;
        }
        return !this.mTelephonyManager.isDataEnabled();
    }

    public void onPrimaryClick(Context context) {
        context.startActivity(new Intent(context, Settings.DataUsageSummaryActivity.class));
    }

    public void onActionClick() {
        this.mTelephonyManager.setDataEnabled(true);
    }

    public ContextualCard buildContextualCard() {
        ConditionalContextualCard.Builder actionText = new ConditionalContextualCard.Builder().setConditionId((long) f76ID).setMetricsConstant(380).setActionText(this.mAppContext.getText(R.string.condition_turn_on));
        return actionText.setName(this.mAppContext.getPackageName() + "/" + this.mAppContext.getText(R.string.condition_cellular_title)).setTitleText(this.mAppContext.getText(R.string.condition_cellular_title).toString()).setSummaryText(this.mAppContext.getText(R.string.condition_cellular_summary).toString()).setIconDrawable(this.mAppContext.getDrawable(R.drawable.ic_cellular_off)).setViewType(R.layout.conditional_card_half_tile).build();
    }

    public void startMonitoringStateChange() {
        restartPhoneStateListener(this.mAppContext, this.mSubId);
    }

    public void stopMonitoringStateChange() {
        stopPhoneStateListener();
    }

    /* access modifiers changed from: private */
    public int getDefaultDataSubscriptionId(Context context) {
        SubscriptionManager subscriptionManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
        return SubscriptionManager.getDefaultDataSubscriptionId();
    }

    private TelephonyManager getTelephonyManager(Context context, int i) {
        return ((TelephonyManager) context.getSystemService(TelephonyManager.class)).createForSubscriptionId(i);
    }

    private void stopPhoneStateListener() {
        this.mIsListeningConnectionChange = false;
        this.mTelephonyManager.listen(this.mPhoneStateListener, 0);
    }

    /* access modifiers changed from: private */
    public void restartPhoneStateListener(Context context, int i) {
        stopPhoneStateListener();
        this.mIsListeningConnectionChange = true;
        if (SubscriptionManager.isValidSubscriptionId(i)) {
            this.mTelephonyManager = getTelephonyManager(context, i);
        }
        this.mTelephonyManager.listen(this.mPhoneStateListener, 4096);
    }
}
