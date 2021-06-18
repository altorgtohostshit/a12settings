package com.android.settings.network.telephony;

import android.telephony.CellInfo;
import java.util.function.Function;

public final /* synthetic */ class CellInfoUtil$$ExternalSyntheticLambda0 implements Function {
    public static final /* synthetic */ CellInfoUtil$$ExternalSyntheticLambda0 INSTANCE = new CellInfoUtil$$ExternalSyntheticLambda0();

    private /* synthetic */ CellInfoUtil$$ExternalSyntheticLambda0() {
    }

    public final Object apply(Object obj) {
        return CellInfoUtil.cellInfoToString((CellInfo) obj);
    }
}
