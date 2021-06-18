package com.android.settings.network.telephony;

import com.android.internal.telephony.OperatorInfo;
import java.util.function.Function;

public final /* synthetic */ class NetworkScanHelper$NetworkScanSyncTask$$ExternalSyntheticLambda0 implements Function {
    public static final /* synthetic */ NetworkScanHelper$NetworkScanSyncTask$$ExternalSyntheticLambda0 INSTANCE = new NetworkScanHelper$NetworkScanSyncTask$$ExternalSyntheticLambda0();

    private /* synthetic */ NetworkScanHelper$NetworkScanSyncTask$$ExternalSyntheticLambda0() {
    }

    public final Object apply(Object obj) {
        return CellInfoUtil.convertOperatorInfoToCellInfo((OperatorInfo) obj);
    }
}
