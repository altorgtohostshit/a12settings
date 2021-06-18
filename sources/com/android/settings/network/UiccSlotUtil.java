package com.android.settings.network;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.telephony.UiccSlotInfo;
import android.util.Log;
import com.google.common.collect.ImmutableList;

public class UiccSlotUtil {
    public static ImmutableList<UiccSlotInfo> getSlotInfos(TelephonyManager telephonyManager) {
        UiccSlotInfo[] uiccSlotsInfo = telephonyManager.getUiccSlotsInfo();
        if (uiccSlotsInfo == null) {
            return ImmutableList.m23of();
        }
        return ImmutableList.copyOf((E[]) uiccSlotsInfo);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:37:0x006c, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void switchToRemovableSlot(int r4, android.content.Context r5) throws com.android.settings.network.UiccSlotsException {
        /*
            java.lang.Class<com.android.settings.network.UiccSlotUtil> r0 = com.android.settings.network.UiccSlotUtil.class
            monitor-enter(r0)
            boolean r1 = com.android.settingslib.utils.ThreadUtils.isMainThread()     // Catch:{ all -> 0x008c }
            if (r1 != 0) goto L_0x0084
            java.lang.Class<android.telephony.TelephonyManager> r1 = android.telephony.TelephonyManager.class
            java.lang.Object r1 = r5.getSystemService(r1)     // Catch:{ all -> 0x008c }
            android.telephony.TelephonyManager r1 = (android.telephony.TelephonyManager) r1     // Catch:{ all -> 0x008c }
            boolean r2 = r1.isMultiSimEnabled()     // Catch:{ all -> 0x008c }
            if (r2 == 0) goto L_0x0020
            java.lang.String r4 = "UiccSlotUtil"
            java.lang.String r5 = "Multiple active slots supported. Not calling switchSlots."
            android.util.Log.i(r4, r5)     // Catch:{ all -> 0x008c }
            monitor-exit(r0)
            return
        L_0x0020:
            android.telephony.UiccSlotInfo[] r1 = r1.getUiccSlotsInfo()     // Catch:{ all -> 0x008c }
            r2 = -1
            if (r4 != r2) goto L_0x0055
            r4 = 0
        L_0x0028:
            int r2 = r1.length     // Catch:{ all -> 0x008c }
            if (r4 >= r2) goto L_0x006b
            r2 = r1[r4]     // Catch:{ all -> 0x008c }
            boolean r2 = r2.isRemovable()     // Catch:{ all -> 0x008c }
            if (r2 == 0) goto L_0x0052
            r2 = r1[r4]     // Catch:{ all -> 0x008c }
            boolean r2 = r2.getIsActive()     // Catch:{ all -> 0x008c }
            if (r2 != 0) goto L_0x0052
            r2 = r1[r4]     // Catch:{ all -> 0x008c }
            int r2 = r2.getCardStateInfo()     // Catch:{ all -> 0x008c }
            r3 = 3
            if (r2 == r3) goto L_0x0052
            r2 = r1[r4]     // Catch:{ all -> 0x008c }
            int r2 = r2.getCardStateInfo()     // Catch:{ all -> 0x008c }
            r3 = 4
            if (r2 == r3) goto L_0x0052
            performSwitchToRemovableSlot(r4, r5)     // Catch:{ all -> 0x008c }
            monitor-exit(r0)
            return
        L_0x0052:
            int r4 = r4 + 1
            goto L_0x0028
        L_0x0055:
            int r2 = r1.length     // Catch:{ all -> 0x008c }
            if (r4 >= r2) goto L_0x006d
            r2 = r1[r4]     // Catch:{ all -> 0x008c }
            boolean r2 = r2.isRemovable()     // Catch:{ all -> 0x008c }
            if (r2 == 0) goto L_0x006d
            r1 = r1[r4]     // Catch:{ all -> 0x008c }
            boolean r1 = r1.getIsActive()     // Catch:{ all -> 0x008c }
            if (r1 != 0) goto L_0x006b
            performSwitchToRemovableSlot(r4, r5)     // Catch:{ all -> 0x008c }
        L_0x006b:
            monitor-exit(r0)
            return
        L_0x006d:
            com.android.settings.network.UiccSlotsException r5 = new com.android.settings.network.UiccSlotsException     // Catch:{ all -> 0x008c }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x008c }
            r1.<init>()     // Catch:{ all -> 0x008c }
            java.lang.String r2 = "The given slotId is not a removable slot: "
            r1.append(r2)     // Catch:{ all -> 0x008c }
            r1.append(r4)     // Catch:{ all -> 0x008c }
            java.lang.String r4 = r1.toString()     // Catch:{ all -> 0x008c }
            r5.<init>(r4)     // Catch:{ all -> 0x008c }
            throw r5     // Catch:{ all -> 0x008c }
        L_0x0084:
            java.lang.IllegalThreadStateException r4 = new java.lang.IllegalThreadStateException     // Catch:{ all -> 0x008c }
            java.lang.String r5 = "Do not call switchToRemovableSlot on the main thread."
            r4.<init>(r5)     // Catch:{ all -> 0x008c }
            throw r4     // Catch:{ all -> 0x008c }
        L_0x008c:
            r4 = move-exception
            monitor-exit(r0)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.network.UiccSlotUtil.switchToRemovableSlot(int, android.content.Context):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0045  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x004b  */
    /* JADX WARNING: Removed duplicated region for block: B:21:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void performSwitchToRemovableSlot(int r6, android.content.Context r7) throws com.android.settings.network.UiccSlotsException {
        /*
            android.content.ContentResolver r0 = r7.getContentResolver()
            java.lang.String r1 = "euicc_switch_slot_timeout_millis"
            r2 = 25000(0x61a8, double:1.23516E-319)
            long r0 = android.provider.Settings.Global.getLong(r0, r1, r2)
            r2 = 0
            java.util.concurrent.CountDownLatch r3 = new java.util.concurrent.CountDownLatch     // Catch:{ InterruptedException -> 0x0034 }
            r4 = 1
            r3.<init>(r4)     // Catch:{ InterruptedException -> 0x0034 }
            com.android.settings.network.CarrierConfigChangedReceiver r5 = new com.android.settings.network.CarrierConfigChangedReceiver     // Catch:{ InterruptedException -> 0x0034 }
            r5.<init>(r3)     // Catch:{ InterruptedException -> 0x0034 }
            r5.registerOn(r7)     // Catch:{ InterruptedException -> 0x002f, all -> 0x002c }
            int[] r2 = new int[r4]     // Catch:{ InterruptedException -> 0x002f, all -> 0x002c }
            r4 = 0
            r2[r4] = r6     // Catch:{ InterruptedException -> 0x002f, all -> 0x002c }
            switchSlots(r7, r2)     // Catch:{ InterruptedException -> 0x002f, all -> 0x002c }
            java.util.concurrent.TimeUnit r6 = java.util.concurrent.TimeUnit.MILLISECONDS     // Catch:{ InterruptedException -> 0x002f, all -> 0x002c }
            r3.await(r0, r6)     // Catch:{ InterruptedException -> 0x002f, all -> 0x002c }
            r7.unregisterReceiver(r5)
            goto L_0x0048
        L_0x002c:
            r6 = move-exception
            r2 = r5
            goto L_0x0049
        L_0x002f:
            r6 = move-exception
            r2 = r5
            goto L_0x0035
        L_0x0032:
            r6 = move-exception
            goto L_0x0049
        L_0x0034:
            r6 = move-exception
        L_0x0035:
            java.lang.Thread r0 = java.lang.Thread.currentThread()     // Catch:{ all -> 0x0032 }
            r0.interrupt()     // Catch:{ all -> 0x0032 }
            java.lang.String r0 = "UiccSlotUtil"
            java.lang.String r1 = "Failed switching to physical slot."
            android.util.Log.e(r0, r1, r6)     // Catch:{ all -> 0x0032 }
            if (r2 == 0) goto L_0x0048
            r7.unregisterReceiver(r2)
        L_0x0048:
            return
        L_0x0049:
            if (r2 == 0) goto L_0x004e
            r7.unregisterReceiver(r2)
        L_0x004e:
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.network.UiccSlotUtil.performSwitchToRemovableSlot(int, android.content.Context):void");
    }

    private static void switchSlots(Context context, int... iArr) throws UiccSlotsException {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
        if (telephonyManager.isMultiSimEnabled()) {
            Log.i("UiccSlotUtil", "Multiple active slots supported. Not calling switchSlots.");
        } else if (!telephonyManager.switchSlots(iArr)) {
            throw new UiccSlotsException("Failed to switch slots");
        }
    }
}
