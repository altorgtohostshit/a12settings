package com.android.settings.network.telephony;

import com.android.internal.telephony.OperatorInfo;

public final /* synthetic */ class NetworkSelectSettings$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ NetworkSelectSettings f$0;
    public final /* synthetic */ OperatorInfo f$1;

    public /* synthetic */ NetworkSelectSettings$$ExternalSyntheticLambda0(NetworkSelectSettings networkSelectSettings, OperatorInfo operatorInfo) {
        this.f$0 = networkSelectSettings;
        this.f$1 = operatorInfo;
    }

    public final void run() {
        this.f$0.lambda$onPreferenceTreeClick$0(this.f$1);
    }
}
