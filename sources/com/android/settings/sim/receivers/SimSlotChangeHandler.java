package com.android.settings.sim.receivers;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.UiccSlotInfo;
import android.util.Log;
import com.android.settings.network.SubscriptionUtil;
import com.android.settings.network.SwitchToRemovableSlotSidecar$$ExternalSyntheticLambda0;
import com.android.settings.network.UiccSlotUtil;
import com.android.settings.network.UiccSlotsException;
import com.android.settings.sim.ChooseSimActivity;
import com.android.settings.sim.DsdsDialogActivity;
import com.android.settings.sim.SimNotificationService;
import com.android.settings.sim.SwitchToEsimConfirmDialogActivity;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SimSlotChangeHandler {
    private static volatile SimSlotChangeHandler sSlotChangeHandler;
    private Context mContext;
    private SubscriptionManager mSubMgr;
    private TelephonyManager mTelMgr;

    public static SimSlotChangeHandler get() {
        if (sSlotChangeHandler == null) {
            synchronized (SimSlotChangeHandler.class) {
                if (sSlotChangeHandler == null) {
                    sSlotChangeHandler = new SimSlotChangeHandler();
                }
            }
        }
        return sSlotChangeHandler;
    }

    /* access modifiers changed from: package-private */
    public void onSlotsStatusChange(Context context) {
        init(context);
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new IllegalStateException("Cannot be called from main thread.");
        } else if (this.mTelMgr.getActiveModemCount() > 1) {
            Log.i("SimSlotChangeHandler", "The device is already in DSDS mode. Do nothing.");
        } else {
            UiccSlotInfo removableUiccSlotInfo = getRemovableUiccSlotInfo();
            if (removableUiccSlotInfo == null) {
                Log.e("SimSlotChangeHandler", "Unable to find the removable slot. Do nothing.");
                return;
            }
            int lastRemovableSimSlotState = getLastRemovableSimSlotState(this.mContext);
            int cardStateInfo = removableUiccSlotInfo.getCardStateInfo();
            setRemovableSimSlotState(this.mContext, cardStateInfo);
            if (lastRemovableSimSlotState == 1 && cardStateInfo == 2) {
                handleSimInsert(removableUiccSlotInfo);
            } else if (lastRemovableSimSlotState == 2 && cardStateInfo == 1) {
                handleSimRemove(removableUiccSlotInfo);
            } else {
                Log.i("SimSlotChangeHandler", "Do nothing on slot status changes.");
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void onSuwFinish(Context context) {
        init(context);
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new IllegalStateException("Cannot be called from main thread.");
        } else if (this.mTelMgr.getActiveModemCount() > 1) {
            Log.i("SimSlotChangeHandler", "The device is already in DSDS mode. Do nothing.");
        } else {
            UiccSlotInfo removableUiccSlotInfo = getRemovableUiccSlotInfo();
            if (removableUiccSlotInfo == null) {
                Log.e("SimSlotChangeHandler", "Unable to find the removable slot. Do nothing.");
                return;
            }
            boolean z = getGroupedEmbeddedSubscriptions().size() != 0;
            int suwRemovableSlotAction = getSuwRemovableSlotAction(this.mContext);
            setSuwRemovableSlotAction(this.mContext, 0);
            if (!z || removableUiccSlotInfo.getCardStateInfo() != 2) {
                if (suwRemovableSlotAction == 2) {
                    handleSimRemove(removableUiccSlotInfo);
                }
            } else if (this.mTelMgr.isMultiSimSupported() == 0) {
                Log.i("SimSlotChangeHandler", "DSDS condition satisfied. Show notification.");
                SimNotificationService.scheduleSimNotification(this.mContext, 3);
            } else if (suwRemovableSlotAction == 1) {
                Log.i("SimSlotChangeHandler", "Both removable SIM and eSIM are present. DSDS condition doesn't satisfied. User inserted pSIM during SUW. Show choose SIM screen.");
                startChooseSimActivity(true);
            }
        }
    }

    private void init(Context context) {
        this.mSubMgr = (SubscriptionManager) context.getSystemService("telephony_subscription_service");
        this.mTelMgr = (TelephonyManager) context.getSystemService(TelephonyManager.class);
        this.mContext = context;
    }

    private void handleSimInsert(UiccSlotInfo uiccSlotInfo) {
        Log.i("SimSlotChangeHandler", "Handle SIM inserted.");
        if (!isSuwFinished(this.mContext)) {
            Log.i("SimSlotChangeHandler", "Still in SUW. Handle SIM insertion after SUW is finished");
            setSuwRemovableSlotAction(this.mContext, 1);
        } else if (uiccSlotInfo.getIsActive()) {
            Log.i("SimSlotChangeHandler", "The removable slot is already active. Do nothing.");
        } else if (!hasActiveEsimSubscription()) {
            Log.i("SimSlotChangeHandler", "No enabled eSIM profile. Ready to switch to removable slot and show notification.");
            try {
                UiccSlotUtil.switchToRemovableSlot(-1, this.mContext.getApplicationContext());
                SimNotificationService.scheduleSimNotification(this.mContext, 2);
            } catch (UiccSlotsException unused) {
                Log.e("SimSlotChangeHandler", "Failed to switch to removable slot.");
            }
        } else if (this.mTelMgr.isMultiSimSupported() == 0) {
            Log.i("SimSlotChangeHandler", "Enabled profile exists. DSDS condition satisfied.");
            startDsdsDialogActivity();
        } else {
            Log.i("SimSlotChangeHandler", "Enabled profile exists. DSDS condition not satisfied.");
            startChooseSimActivity(true);
        }
    }

    private void handleSimRemove(UiccSlotInfo uiccSlotInfo) {
        Log.i("SimSlotChangeHandler", "Handle SIM removed.");
        if (!isSuwFinished(this.mContext)) {
            Log.i("SimSlotChangeHandler", "Still in SUW. Handle SIM removal after SUW is finished");
            setSuwRemovableSlotAction(this.mContext, 2);
            return;
        }
        List<SubscriptionInfo> groupedEmbeddedSubscriptions = getGroupedEmbeddedSubscriptions();
        if (groupedEmbeddedSubscriptions.size() == 0 || !uiccSlotInfo.getIsActive()) {
            Log.i("SimSlotChangeHandler", "eSIM slot is active or no subscriptions exist. Do nothing.");
        } else if (groupedEmbeddedSubscriptions.size() == 1) {
            Log.i("SimSlotChangeHandler", "Only 1 eSIM profile found. Ask user's consent to switch.");
            startSwitchSlotConfirmDialogActivity(groupedEmbeddedSubscriptions.get(0));
        } else {
            Log.i("SimSlotChangeHandler", "Multiple eSIM profiles found. Ask user which subscription to use.");
            startChooseSimActivity(false);
        }
    }

    private int getLastRemovableSimSlotState(Context context) {
        return context.getSharedPreferences("euicc_prefs", 0).getInt("removable_slot_state", 1);
    }

    private void setRemovableSimSlotState(Context context, int i) {
        context.getSharedPreferences("euicc_prefs", 0).edit().putInt("removable_slot_state", i).apply();
    }

    private int getSuwRemovableSlotAction(Context context) {
        return context.getSharedPreferences("euicc_prefs", 0).getInt("suw_psim_action", 0);
    }

    private void setSuwRemovableSlotAction(Context context, int i) {
        context.getSharedPreferences("euicc_prefs", 0).edit().putInt("suw_psim_action", i).apply();
    }

    private UiccSlotInfo getRemovableUiccSlotInfo() {
        UiccSlotInfo[] uiccSlotsInfo = this.mTelMgr.getUiccSlotsInfo();
        if (uiccSlotsInfo == null) {
            Log.e("SimSlotChangeHandler", "slotInfos is null. Unable to get slot infos.");
            return null;
        }
        for (UiccSlotInfo uiccSlotInfo : uiccSlotsInfo) {
            if (uiccSlotInfo != null && uiccSlotInfo.isRemovable()) {
                return uiccSlotInfo;
            }
        }
        return null;
    }

    private static boolean isSuwFinished(Context context) {
        try {
            if (Settings.Global.getInt(context.getContentResolver(), "device_provisioned") == 1) {
                return true;
            }
            return false;
        } catch (Settings.SettingNotFoundException e) {
            Log.e("SimSlotChangeHandler", "Cannot get DEVICE_PROVISIONED from the device.", e);
            return false;
        }
    }

    private boolean hasActiveEsimSubscription() {
        return SubscriptionUtil.getActiveSubscriptions(this.mSubMgr).stream().anyMatch(SwitchToRemovableSlotSidecar$$ExternalSyntheticLambda0.INSTANCE);
    }

    private List<SubscriptionInfo> getGroupedEmbeddedSubscriptions() {
        List<SubscriptionInfo> selectableSubscriptionInfoList = SubscriptionUtil.getSelectableSubscriptionInfoList(this.mContext);
        if (selectableSubscriptionInfoList == null) {
            return ImmutableList.m23of();
        }
        return ImmutableList.copyOf((Collection) selectableSubscriptionInfoList.stream().filter(SimSlotChangeHandler$$ExternalSyntheticLambda0.INSTANCE).collect(Collectors.toList()));
    }

    private void startChooseSimActivity(boolean z) {
        Intent intent = ChooseSimActivity.getIntent(this.mContext);
        intent.setFlags(268435456);
        intent.putExtra("has_psim", z);
        this.mContext.startActivity(intent);
    }

    private void startSwitchSlotConfirmDialogActivity(SubscriptionInfo subscriptionInfo) {
        Intent intent = new Intent(this.mContext, SwitchToEsimConfirmDialogActivity.class);
        intent.setFlags(268435456);
        intent.putExtra("sub_to_enable", subscriptionInfo);
        this.mContext.startActivity(intent);
    }

    private void startDsdsDialogActivity() {
        Intent intent = new Intent(this.mContext, DsdsDialogActivity.class);
        intent.setFlags(268435456);
        this.mContext.startActivity(intent);
    }

    private SimSlotChangeHandler() {
    }
}
