package com.android.settings.sim.smartForwarding;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.CallForwardingInfo;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import com.android.settings.sim.smartForwarding.EnableSmartForwardingTask;

public class SmartForwardingUtils {
    public static boolean getBackupCallWaitingStatus(Context context, int i) {
        return context.getSharedPreferences("smart_forwarding_pref_" + i, 0).getBoolean("call_waiting_key", false);
    }

    public static CallForwardingInfo getBackupCallForwardingStatus(Context context, int i) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("smart_forwarding_pref_" + i, 0);
        if (sharedPreferences.contains("call_forwarding_enabled_key")) {
            return new CallForwardingInfo(sharedPreferences.getBoolean("call_forwarding_enabled_key", false), sharedPreferences.getInt("call_forwarding_reason_key", 0), sharedPreferences.getString("call_forwarding_number_key", ""), sharedPreferences.getInt("call_forwarding_timekey", 1));
        }
        return null;
    }

    public static void saveCallWaitingStatus(Context context, int i, boolean z) {
        context.getSharedPreferences("smart_forwarding_pref_" + i, 0).edit().putBoolean("call_waiting_key", z).commit();
    }

    public static void saveCallForwardingStatus(Context context, int i, CallForwardingInfo callForwardingInfo) {
        SharedPreferences.Editor edit = context.getSharedPreferences("smart_forwarding_pref_" + i, 0).edit();
        edit.putBoolean("call_forwarding_enabled_key", callForwardingInfo.isEnabled()).commit();
        edit.putInt("call_forwarding_reason_key", callForwardingInfo.getReason()).commit();
        edit.putString("call_forwarding_number_key", callForwardingInfo.getNumber()).commit();
        edit.putInt("call_forwarding_timekey", callForwardingInfo.getTimeoutSeconds()).commit();
    }

    public static void clearBackupData(Context context, int i) {
        context.getSharedPreferences("smart_forwarding_pref_" + i, 0).edit().clear().commit();
    }

    public static boolean[] getAllSlotCallWaitingStatus(Context context, SubscriptionManager subscriptionManager, TelephonyManager telephonyManager) {
        int activeModemCount = telephonyManager.getActiveModemCount();
        boolean[] zArr = new boolean[activeModemCount];
        for (int i = 0; i < activeModemCount; i++) {
            zArr[i] = getBackupCallWaitingStatus(context, subscriptionManager.getSubscriptionIds(i)[0]);
        }
        return zArr;
    }

    public static CallForwardingInfo[] getAllSlotCallForwardingStatus(Context context, SubscriptionManager subscriptionManager, TelephonyManager telephonyManager) {
        int activeModemCount = telephonyManager.getActiveModemCount();
        CallForwardingInfo[] callForwardingInfoArr = new CallForwardingInfo[activeModemCount];
        for (int i = 0; i < activeModemCount; i++) {
            callForwardingInfoArr[i] = getBackupCallForwardingStatus(context, subscriptionManager.getSubscriptionIds(i)[0]);
        }
        return callForwardingInfoArr;
    }

    public static void clearAllBackupData(Context context, SubscriptionManager subscriptionManager, TelephonyManager telephonyManager) {
        int activeModemCount = telephonyManager.getActiveModemCount();
        for (int i = 0; i < activeModemCount; i++) {
            clearBackupData(context, subscriptionManager.getSubscriptionIds(i)[0]);
        }
    }

    public static void backupPrevStatus(Context context, EnableSmartForwardingTask.SlotUTData[] slotUTDataArr) {
        for (int i = 0; i < slotUTDataArr.length; i++) {
            int i2 = slotUTDataArr[i].mQueryCallWaiting.result;
            int i3 = slotUTDataArr[i].subId;
            boolean z = true;
            if (i2 != 1) {
                z = false;
            }
            saveCallWaitingStatus(context, i3, z);
            saveCallForwardingStatus(context, slotUTDataArr[i].subId, slotUTDataArr[i].mQueryCallForwarding.result);
        }
    }

    public static String getPhoneNumber(Context context, int i) {
        SubscriptionInfo activeSubscriptionInfo;
        SubscriptionManager subscriptionManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
        int[] subscriptionIds = subscriptionManager.getSubscriptionIds(i);
        if (subscriptionIds == null || (activeSubscriptionInfo = subscriptionManager.getActiveSubscriptionInfo(subscriptionIds[0])) == null) {
            return "";
        }
        return activeSubscriptionInfo.getNumber();
    }
}
