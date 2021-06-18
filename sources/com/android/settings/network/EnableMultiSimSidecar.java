package com.android.settings.network;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.telephony.UiccSlotInfo;
import android.util.ArraySet;
import android.util.Log;
import com.android.settings.AsyncTaskSidecar;
import com.android.settings.SidecarFragment;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class EnableMultiSimSidecar extends AsyncTaskSidecar<Void, Boolean> {
    private final BroadcastReceiver mCarrierConfigChangeReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            int access$000 = EnableMultiSimSidecar.this.getReadySimsCount();
            int access$100 = EnableMultiSimSidecar.this.getActiveSlotsCount();
            if (access$000 == EnableMultiSimSidecar.this.mNumOfActiveSim && access$100 == EnableMultiSimSidecar.this.mNumOfActiveSim) {
                Log.i("EnableMultiSimSidecar", String.format("%d slots are active and ready.", new Object[]{Integer.valueOf(EnableMultiSimSidecar.this.mNumOfActiveSim)}));
                EnableMultiSimSidecar.this.mSimCardStateChangedLatch.countDown();
                return;
            }
            Log.i("EnableMultiSimSidecar", String.format("%d slots are active and %d SIMs are ready. Keep waiting until timeout.", new Object[]{Integer.valueOf(access$100), Integer.valueOf(access$000)}));
        }
    };
    /* access modifiers changed from: private */
    public int mNumOfActiveSim = 0;
    final CountDownLatch mSimCardStateChangedLatch = new CountDownLatch(1);
    private TelephonyManager mTelephonyManager;

    public static EnableMultiSimSidecar get(FragmentManager fragmentManager) {
        return (EnableMultiSimSidecar) SidecarFragment.get(fragmentManager, "EnableMultiSimSidecar", EnableMultiSimSidecar.class, (Bundle) null);
    }

    /* access modifiers changed from: protected */
    public Boolean doInBackground(Void voidR) {
        return Boolean.valueOf(updateMultiSimConfig());
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(Boolean bool) {
        if (bool.booleanValue()) {
            setState(2, 0);
        } else {
            setState(3, 0);
        }
    }

    public void run(int i) {
        TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(TelephonyManager.class);
        this.mTelephonyManager = telephonyManager;
        this.mNumOfActiveSim = i;
        if (i > telephonyManager.getSupportedModemCount()) {
            Log.e("EnableMultiSimSidecar", "Requested number of active SIM is greater than supported modem count.");
            setState(3, 0);
        } else if (this.mTelephonyManager.doesSwitchMultiSimConfigTriggerReboot()) {
            Log.e("EnableMultiSimSidecar", "The device does not support reboot free DSDS.");
            setState(3, 0);
        } else {
            super.run(null);
        }
    }

    private boolean updateMultiSimConfig() {
        try {
            getContext().registerReceiver(this.mCarrierConfigChangeReceiver, new IntentFilter("android.telephony.action.CARRIER_CONFIG_CHANGED"));
            this.mTelephonyManager.switchMultiSimConfig(this.mNumOfActiveSim);
            if (this.mSimCardStateChangedLatch.await(Settings.Global.getLong(getContext().getContentResolver(), "enable_multi_slot_timeout_millis", 40000), TimeUnit.MILLISECONDS)) {
                Log.i("EnableMultiSimSidecar", "Multi SIM were successfully enabled.");
                getContext().unregisterReceiver(this.mCarrierConfigChangeReceiver);
                return true;
            }
            Log.e("EnableMultiSimSidecar", "Timeout for waiting SIM status.");
            getContext().unregisterReceiver(this.mCarrierConfigChangeReceiver);
            return false;
        } catch (InterruptedException e) {
            Log.e("EnableMultiSimSidecar", "Failed to enable multiple SIM due to InterruptedException", e);
        } catch (Throwable th) {
            getContext().unregisterReceiver(this.mCarrierConfigChangeReceiver);
            throw th;
        }
    }

    /* access modifiers changed from: private */
    public int getReadySimsCount() {
        int activeModemCount = this.mTelephonyManager.getActiveModemCount();
        Set<Integer> activeRemovableLogicalSlotIds = getActiveRemovableLogicalSlotIds();
        int i = 0;
        for (int i2 = 0; i2 < activeModemCount; i2++) {
            int simState = this.mTelephonyManager.getSimState(i2);
            if (simState == 5 || simState == 6 || simState == 10 || (simState == 1 && activeRemovableLogicalSlotIds.contains(Integer.valueOf(i2)))) {
                i++;
            }
        }
        return i;
    }

    /* access modifiers changed from: private */
    public int getActiveSlotsCount() {
        UiccSlotInfo[] uiccSlotsInfo = this.mTelephonyManager.getUiccSlotsInfo();
        if (uiccSlotsInfo == null) {
            return 0;
        }
        int i = 0;
        for (UiccSlotInfo uiccSlotInfo : uiccSlotsInfo) {
            if (uiccSlotInfo != null && uiccSlotInfo.getIsActive()) {
                i++;
            }
        }
        return i;
    }

    public Set<Integer> getActiveRemovableLogicalSlotIds() {
        UiccSlotInfo[] uiccSlotsInfo = this.mTelephonyManager.getUiccSlotsInfo();
        if (uiccSlotsInfo == null) {
            return Collections.emptySet();
        }
        ArraySet arraySet = new ArraySet();
        for (UiccSlotInfo uiccSlotInfo : uiccSlotsInfo) {
            if (uiccSlotInfo != null && uiccSlotInfo.getIsActive() && uiccSlotInfo.isRemovable()) {
                arraySet.add(Integer.valueOf(uiccSlotInfo.getLogicalSlotIdx()));
            }
        }
        return arraySet;
    }
}
