package com.android.settings.sim.receivers;

import android.telephony.UiccCardInfo;
import java.util.function.Predicate;

public final /* synthetic */ class SimSlotChangeReceiver$$ExternalSyntheticLambda2 implements Predicate {
    public final /* synthetic */ int f$0;

    public /* synthetic */ SimSlotChangeReceiver$$ExternalSyntheticLambda2(int i) {
        this.f$0 = i;
    }

    public final boolean test(Object obj) {
        return SimSlotChangeReceiver.lambda$findUiccCardInfoBySlot$1(this.f$0, (UiccCardInfo) obj);
    }
}
