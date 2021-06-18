package com.google.android.wifitrackerlib;

import android.net.wifi.WifiConfiguration;
import android.text.TextUtils;
import java.util.function.Predicate;

public final /* synthetic */ class WsuPostProvisioningReceiver$$ExternalSyntheticLambda4 implements Predicate {
    public final /* synthetic */ String f$0;

    public /* synthetic */ WsuPostProvisioningReceiver$$ExternalSyntheticLambda4(String str) {
        this.f$0 = str;
    }

    public final boolean test(Object obj) {
        return TextUtils.equals(((WifiConfiguration) obj).creatorName, this.f$0);
    }
}
