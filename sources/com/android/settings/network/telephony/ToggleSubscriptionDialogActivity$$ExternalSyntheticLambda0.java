package com.android.settings.network.telephony;

import android.telephony.UiccSlotInfo;
import java.util.function.Predicate;

public final /* synthetic */ class ToggleSubscriptionDialogActivity$$ExternalSyntheticLambda0 implements Predicate {
    public static final /* synthetic */ ToggleSubscriptionDialogActivity$$ExternalSyntheticLambda0 INSTANCE = new ToggleSubscriptionDialogActivity$$ExternalSyntheticLambda0();

    private /* synthetic */ ToggleSubscriptionDialogActivity$$ExternalSyntheticLambda0() {
    }

    public final boolean test(Object obj) {
        return ToggleSubscriptionDialogActivity.lambda$isDsdsConditionSatisfied$0((UiccSlotInfo) obj);
    }
}
