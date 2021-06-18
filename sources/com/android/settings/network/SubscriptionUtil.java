package com.android.settings.network;

import android.content.Context;
import android.os.ParcelUuid;
import android.telephony.PhoneNumberUtils;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.UiccCardInfo;
import android.telephony.UiccSlotInfo;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.MccTable;
import com.android.internal.util.CollectionUtils;
import com.android.settings.network.telephony.DeleteEuiccSubscriptionDialogActivity;
import com.android.settings.network.telephony.ToggleSubscriptionDialogActivity;
import com.android.settingslib.DeviceInfoUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SubscriptionUtil {
    private static List<SubscriptionInfo> sActiveResultsForTesting;
    private static List<SubscriptionInfo> sAvailableResultsForTesting;

    public static void setAvailableSubscriptionsForTesting(List<SubscriptionInfo> list) {
        sAvailableResultsForTesting = list;
    }

    public static void setActiveSubscriptionsForTesting(List<SubscriptionInfo> list) {
        sActiveResultsForTesting = list;
    }

    public static List<SubscriptionInfo> getActiveSubscriptions(SubscriptionManager subscriptionManager) {
        List<SubscriptionInfo> list = sActiveResultsForTesting;
        if (list != null) {
            return list;
        }
        if (subscriptionManager == null) {
            return Collections.emptyList();
        }
        List<SubscriptionInfo> activeSubscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();
        return activeSubscriptionInfoList == null ? new ArrayList() : activeSubscriptionInfoList;
    }

    static boolean isInactiveInsertedPSim(UiccSlotInfo uiccSlotInfo) {
        if (uiccSlotInfo != null && !uiccSlotInfo.getIsEuicc() && !uiccSlotInfo.getIsActive() && uiccSlotInfo.getCardStateInfo() == 2) {
            return true;
        }
        return false;
    }

    public static List<SubscriptionInfo> getAvailableSubscriptions(Context context) {
        List<SubscriptionInfo> list = sAvailableResultsForTesting;
        if (list != null) {
            return list;
        }
        return new ArrayList(CollectionUtils.emptyIfNull(getSelectableSubscriptionInfoList(context)));
    }

    public static SubscriptionInfo getAvailableSubscription(Context context, ProxySubscriptionManager proxySubscriptionManager, int i) {
        SubscriptionInfo accessibleSubscriptionInfo = proxySubscriptionManager.getAccessibleSubscriptionInfo(i);
        if (accessibleSubscriptionInfo == null) {
            return null;
        }
        ParcelUuid groupUuid = accessibleSubscriptionInfo.getGroupUuid();
        if (groupUuid == null || isPrimarySubscriptionWithinSameUuid(getUiccSlotsInfo(context), groupUuid, proxySubscriptionManager.getAccessibleSubscriptionsInfo(), i)) {
            return accessibleSubscriptionInfo;
        }
        return null;
    }

    private static UiccSlotInfo[] getUiccSlotsInfo(Context context) {
        return ((TelephonyManager) context.getSystemService(TelephonyManager.class)).getUiccSlotsInfo();
    }

    private static boolean isPrimarySubscriptionWithinSameUuid(UiccSlotInfo[] uiccSlotInfoArr, ParcelUuid parcelUuid, List<SubscriptionInfo> list, int i) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        for (SubscriptionInfo next : list) {
            if (parcelUuid.equals(next.getGroupUuid())) {
                if (!next.isEmbedded()) {
                    arrayList.add(next);
                } else {
                    if (!next.isOpportunistic()) {
                        arrayList2.add(next);
                    }
                    if (next.getSimSlotIndex() != -1) {
                        arrayList3.add(next);
                    } else {
                        arrayList4.add(next);
                    }
                }
            }
        }
        if (uiccSlotInfoArr != null && arrayList.size() > 0) {
            SubscriptionInfo searchForSubscriptionId = searchForSubscriptionId(arrayList, i);
            if (searchForSubscriptionId == null) {
                return false;
            }
            for (UiccSlotInfo uiccSlotInfo : uiccSlotInfoArr) {
                if (uiccSlotInfo != null && !uiccSlotInfo.getIsEuicc() && uiccSlotInfo.getLogicalSlotIdx() == searchForSubscriptionId.getSimSlotIndex()) {
                    return true;
                }
            }
            return false;
        } else if (arrayList2.size() > 0) {
            Iterator it = arrayList2.iterator();
            int i2 = 0;
            boolean z = false;
            while (it.hasNext()) {
                SubscriptionInfo subscriptionInfo = (SubscriptionInfo) it.next();
                boolean z2 = subscriptionInfo.getSubscriptionId() == i;
                if (subscriptionInfo.getSimSlotIndex() == -1) {
                    z |= z2;
                } else if (z2) {
                    return true;
                } else {
                    i2++;
                }
            }
            if (i2 > 0) {
                return false;
            }
            return z;
        } else if (arrayList.size() > 0) {
            return false;
        } else {
            if (arrayList3.size() > 0) {
                if (((SubscriptionInfo) arrayList3.get(0)).getSubscriptionId() == i) {
                    return true;
                }
                return false;
            } else if (((SubscriptionInfo) arrayList4.get(0)).getSubscriptionId() == i) {
                return true;
            } else {
                return false;
            }
        }
    }

    private static SubscriptionInfo searchForSubscriptionId(List<SubscriptionInfo> list, int i) {
        for (SubscriptionInfo next : list) {
            if (next.getSubscriptionId() == i) {
                return next;
            }
        }
        return null;
    }

    public static Map<Integer, CharSequence> getUniqueSubscriptionDisplayNames(Context context) {
        SubscriptionUtil$$ExternalSyntheticLambda12 subscriptionUtil$$ExternalSyntheticLambda12 = new SubscriptionUtil$$ExternalSyntheticLambda12(context);
        HashSet hashSet = new HashSet();
        SubscriptionUtil$$ExternalSyntheticLambda13 subscriptionUtil$$ExternalSyntheticLambda13 = new SubscriptionUtil$$ExternalSyntheticLambda13(subscriptionUtil$$ExternalSyntheticLambda12, (Set) ((Stream) subscriptionUtil$$ExternalSyntheticLambda12.get()).filter(new SubscriptionUtil$$ExternalSyntheticLambda9(hashSet)).map(SubscriptionUtil$$ExternalSyntheticLambda6.INSTANCE).collect(Collectors.toSet()), context);
        hashSet.clear();
        return (Map) ((Stream) subscriptionUtil$$ExternalSyntheticLambda13.get()).map(new SubscriptionUtil$$ExternalSyntheticLambda0((Set) ((Stream) subscriptionUtil$$ExternalSyntheticLambda13.get()).filter(new SubscriptionUtil$$ExternalSyntheticLambda10(hashSet)).map(SubscriptionUtil$$ExternalSyntheticLambda5.INSTANCE).collect(Collectors.toSet()))).collect(Collectors.toMap(SubscriptionUtil$$ExternalSyntheticLambda4.INSTANCE, SubscriptionUtil$$ExternalSyntheticLambda3.INSTANCE));
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getUniqueSubscriptionDisplayNames$0(SubscriptionInfo subscriptionInfo) {
        return (subscriptionInfo == null || subscriptionInfo.getDisplayName() == null) ? false : true;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ AnonymousClass1DisplayInfo lambda$getUniqueSubscriptionDisplayNames$1(SubscriptionInfo subscriptionInfo) {
        AnonymousClass1DisplayInfo r0 = new Object() {
            public CharSequence originalName;
            public SubscriptionInfo subscriptionInfo;
            public CharSequence uniqueName;
        };
        r0.subscriptionInfo = subscriptionInfo;
        r0.originalName = subscriptionInfo.getDisplayName().toString().trim();
        return r0;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getUniqueSubscriptionDisplayNames$3(Set set, AnonymousClass1DisplayInfo r1) {
        return !set.add(r1.originalName);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ AnonymousClass1DisplayInfo lambda$getUniqueSubscriptionDisplayNames$5(Set set, Context context, AnonymousClass1DisplayInfo r3) {
        if (set.contains(r3.originalName)) {
            String bidiFormattedPhoneNumber = DeviceInfoUtils.getBidiFormattedPhoneNumber(context, r3.subscriptionInfo);
            if (bidiFormattedPhoneNumber == null) {
                bidiFormattedPhoneNumber = "";
            } else if (bidiFormattedPhoneNumber.length() > 4) {
                bidiFormattedPhoneNumber = bidiFormattedPhoneNumber.substring(bidiFormattedPhoneNumber.length() - 4);
            }
            if (TextUtils.isEmpty(bidiFormattedPhoneNumber)) {
                r3.uniqueName = r3.originalName;
            } else {
                r3.uniqueName = r3.originalName + " " + bidiFormattedPhoneNumber;
            }
        } else {
            r3.uniqueName = r3.originalName;
        }
        return r3;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getUniqueSubscriptionDisplayNames$7(Set set, AnonymousClass1DisplayInfo r1) {
        return !set.add(r1.uniqueName);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ AnonymousClass1DisplayInfo lambda$getUniqueSubscriptionDisplayNames$9(Set set, AnonymousClass1DisplayInfo r2) {
        if (set.contains(r2.uniqueName)) {
            r2.uniqueName = r2.originalName + " " + r2.subscriptionInfo.getSubscriptionId();
        }
        return r2;
    }

    public static CharSequence getUniqueSubscriptionDisplayName(Integer num, Context context) {
        return getUniqueSubscriptionDisplayNames(context).getOrDefault(num, "");
    }

    public static CharSequence getUniqueSubscriptionDisplayName(SubscriptionInfo subscriptionInfo, Context context) {
        return subscriptionInfo == null ? "" : getUniqueSubscriptionDisplayName(Integer.valueOf(subscriptionInfo.getSubscriptionId()), context);
    }

    public static boolean showToggleForPhysicalSim(SubscriptionManager subscriptionManager) {
        return subscriptionManager.canDisablePhysicalSubscription();
    }

    public static int getPhoneId(Context context, int i) {
        SubscriptionInfo activeSubscriptionInfo;
        SubscriptionManager subscriptionManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
        if (subscriptionManager == null || (activeSubscriptionInfo = subscriptionManager.getActiveSubscriptionInfo(i)) == null) {
            return -1;
        }
        return activeSubscriptionInfo.getSimSlotIndex();
    }

    public static List<SubscriptionInfo> getSelectableSubscriptionInfoList(Context context) {
        SubscriptionManager subscriptionManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
        List<SubscriptionInfo> availableSubscriptionInfoList = subscriptionManager.getAvailableSubscriptionInfoList();
        if (availableSubscriptionInfoList == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        HashMap hashMap = new HashMap();
        for (SubscriptionInfo subscriptionInfo : availableSubscriptionInfoList) {
            if (isSubscriptionVisible(subscriptionManager, context, subscriptionInfo)) {
                ParcelUuid groupUuid = subscriptionInfo.getGroupUuid();
                if (groupUuid == null) {
                    arrayList.add(subscriptionInfo);
                } else if (!hashMap.containsKey(groupUuid) || (((SubscriptionInfo) hashMap.get(groupUuid)).getSimSlotIndex() == -1 && subscriptionInfo.getSimSlotIndex() != -1)) {
                    arrayList.remove(hashMap.get(groupUuid));
                    arrayList.add(subscriptionInfo);
                    hashMap.put(groupUuid, subscriptionInfo);
                }
            }
        }
        return arrayList;
    }

    public static void startToggleSubscriptionDialogActivity(Context context, int i, boolean z) {
        if (!SubscriptionManager.isUsableSubscriptionId(i)) {
            Log.i("SubscriptionUtil", "Unable to toggle subscription due to invalid subscription ID.");
        } else {
            context.startActivity(ToggleSubscriptionDialogActivity.getIntent(context, i, z));
        }
    }

    public static void startDeleteEuiccSubscriptionDialogActivity(Context context, int i) {
        if (!SubscriptionManager.isUsableSubscriptionId(i)) {
            Log.i("SubscriptionUtil", "Unable to delete subscription due to invalid subscription ID.");
        } else {
            context.startActivity(DeleteEuiccSubscriptionDialogActivity.getIntent(context, i));
        }
    }

    public static SubscriptionInfo getSubById(SubscriptionManager subscriptionManager, int i) {
        if (i == -1) {
            return null;
        }
        return (SubscriptionInfo) subscriptionManager.getAllSubscriptionInfoList().stream().filter(new SubscriptionUtil$$ExternalSyntheticLambda7(i)).findFirst().get();
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getSubById$12(int i, SubscriptionInfo subscriptionInfo) {
        return subscriptionInfo.getSubscriptionId() == i;
    }

    private static boolean isSubscriptionVisible(SubscriptionManager subscriptionManager, Context context, SubscriptionInfo subscriptionInfo) {
        if (subscriptionInfo == null) {
            return false;
        }
        if (subscriptionInfo.getGroupUuid() == null || !subscriptionInfo.isOpportunistic()) {
            return true;
        }
        if (((TelephonyManager) context.getSystemService(TelephonyManager.class)).createForSubscriptionId(subscriptionInfo.getSubscriptionId()).hasCarrierPrivileges() || subscriptionManager.canManageSubscription(subscriptionInfo)) {
            return true;
        }
        return false;
    }

    public static List<SubscriptionInfo> findAllSubscriptionsInGroup(SubscriptionManager subscriptionManager, int i) {
        SubscriptionInfo subById = getSubById(subscriptionManager, i);
        if (subById == null) {
            return Collections.emptyList();
        }
        ParcelUuid groupUuid = subById.getGroupUuid();
        List availableSubscriptionInfoList = subscriptionManager.getAvailableSubscriptionInfoList();
        return (availableSubscriptionInfoList == null || availableSubscriptionInfoList.isEmpty() || groupUuid == null) ? Collections.singletonList(subById) : (List) availableSubscriptionInfoList.stream().filter(new SubscriptionUtil$$ExternalSyntheticLambda8(groupUuid)).collect(Collectors.toList());
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$findAllSubscriptionsInGroup$13(ParcelUuid parcelUuid, SubscriptionInfo subscriptionInfo) {
        return subscriptionInfo.isEmbedded() && parcelUuid.equals(subscriptionInfo.getGroupUuid());
    }

    public static String getFormattedPhoneNumber(Context context, SubscriptionInfo subscriptionInfo) {
        if (subscriptionInfo == null) {
            Log.e("SubscriptionUtil", "Invalid subscription.");
            return null;
        }
        String line1Number = ((TelephonyManager) context.getSystemService(TelephonyManager.class)).getLine1Number(subscriptionInfo.getSubscriptionId());
        String countryCodeForMcc = MccTable.countryCodeForMcc(subscriptionInfo.getMccString());
        if (TextUtils.isEmpty(line1Number)) {
            return null;
        }
        return PhoneNumberUtils.formatNumber(line1Number, countryCodeForMcc);
    }

    public static SubscriptionInfo getFirstRemovableSubscription(Context context) {
        SubscriptionManager subscriptionManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
        List<UiccCardInfo> uiccCardsInfo = ((TelephonyManager) context.getSystemService(TelephonyManager.class)).getUiccCardsInfo();
        if (uiccCardsInfo == null) {
            Log.w("SubscriptionUtil", "UICC cards info list is empty.");
            return null;
        }
        List<SubscriptionInfo> allSubscriptionInfoList = subscriptionManager.getAllSubscriptionInfoList();
        if (allSubscriptionInfoList == null) {
            Log.w("SubscriptionUtil", "All subscription info list is empty.");
            return null;
        }
        for (UiccCardInfo next : uiccCardsInfo) {
            if (next == null) {
                Log.w("SubscriptionUtil", "Got null card.");
            } else if (!next.isRemovable() || next.getCardId() == -1) {
                Log.i("SubscriptionUtil", "Skip embedded card or invalid cardId on slot: " + next.getSlotIndex());
            } else {
                Log.i("SubscriptionUtil", "Target removable cardId :" + next.getCardId());
                for (SubscriptionInfo subscriptionInfo : allSubscriptionInfoList) {
                    if (next.getCardId() == subscriptionInfo.getCardId()) {
                        return subscriptionInfo;
                    }
                }
                continue;
            }
        }
        return null;
    }
}
