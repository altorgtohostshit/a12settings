package com.android.settings.sim.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.telephony.UiccCardInfo;
import android.telephony.UiccSlotInfo;
import android.telephony.euicc.EuiccManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.settings.R;
import com.android.settingslib.utils.ThreadUtils;
import java.util.List;
import java.util.Objects;

public class SimSlotChangeReceiver extends BroadcastReceiver {
    private final Object mLock = new Object();
    private final SimSlotChangeHandler mSlotChangeHandler = SimSlotChangeHandler.get();

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (!"android.telephony.action.SIM_SLOT_STATUS_CHANGED".equals(action)) {
            Log.e("SlotChangeReceiver", "Ignore slot changes due to unexpected action: " + action);
            return;
        }
        ThreadUtils.postOnBackgroundThread((Runnable) new SimSlotChangeReceiver$$ExternalSyntheticLambda1(this, context, goAsync()));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onReceive$0(Context context, BroadcastReceiver.PendingResult pendingResult) {
        synchronized (this.mLock) {
            if (shouldHandleSlotChange(context)) {
                this.mSlotChangeHandler.onSlotsStatusChange(context.getApplicationContext());
            }
        }
        Objects.requireNonNull(pendingResult);
        ThreadUtils.postOnMainThread(new SimSlotChangeReceiver$$ExternalSyntheticLambda0(pendingResult));
    }

    private boolean shouldHandleSlotChange(Context context) {
        if (!context.getResources().getBoolean(R.bool.config_handle_sim_slot_change)) {
            Log.i("SlotChangeReceiver", "The flag is off. Ignore slot changes.");
            return false;
        }
        EuiccManager euiccManager = (EuiccManager) context.getSystemService(EuiccManager.class);
        if (euiccManager == null || !euiccManager.isEnabled()) {
            Log.i("SlotChangeReceiver", "Ignore slot changes because EuiccManager is disabled.");
            return false;
        } else if (euiccManager.getOtaStatus() == 1) {
            Log.i("SlotChangeReceiver", "Ignore slot changes because eSIM OTA is in progress.");
            return false;
        } else if (isSimSlotStateValid(context)) {
            return true;
        } else {
            Log.i("SlotChangeReceiver", "Ignore slot changes because SIM states are not valid.");
            return false;
        }
    }

    private boolean isSimSlotStateValid(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
        UiccSlotInfo[] uiccSlotsInfo = telephonyManager.getUiccSlotsInfo();
        if (uiccSlotsInfo == null) {
            Log.e("SlotChangeReceiver", "slotInfos is null. Unable to get slot infos.");
            return false;
        }
        boolean z = true;
        for (int i = 0; i < uiccSlotsInfo.length; i++) {
            UiccSlotInfo uiccSlotInfo = uiccSlotsInfo[i];
            if (uiccSlotInfo == null) {
                return false;
            }
            if (uiccSlotInfo.getCardStateInfo() == 3 || uiccSlotInfo.getCardStateInfo() == 4) {
                Log.i("SlotChangeReceiver", "The SIM state is in an error. Drop the event. SIM info: " + uiccSlotInfo);
                return false;
            }
            UiccCardInfo findUiccCardInfoBySlot = findUiccCardInfoBySlot(telephonyManager, i);
            if (findUiccCardInfoBySlot != null && (!TextUtils.isEmpty(uiccSlotInfo.getCardId()) || !TextUtils.isEmpty(findUiccCardInfoBySlot.getIccId()))) {
                z = false;
            }
        }
        if (!z) {
            return true;
        }
        Log.i("SlotChangeReceiver", "All UICC card strings are empty. Drop this event.");
        return false;
    }

    private UiccCardInfo findUiccCardInfoBySlot(TelephonyManager telephonyManager, int i) {
        List<UiccCardInfo> uiccCardsInfo = telephonyManager.getUiccCardsInfo();
        if (uiccCardsInfo == null) {
            return null;
        }
        return (UiccCardInfo) uiccCardsInfo.stream().filter(new SimSlotChangeReceiver$$ExternalSyntheticLambda2(i)).findFirst().orElse((Object) null);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$findUiccCardInfoBySlot$1(int i, UiccCardInfo uiccCardInfo) {
        return uiccCardInfo.getSlotIndex() == i;
    }
}
