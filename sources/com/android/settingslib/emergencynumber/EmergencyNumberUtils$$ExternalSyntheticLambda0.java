package com.android.settingslib.emergencynumber;

import android.telephony.emergency.EmergencyNumber;
import java.util.function.Function;

public final /* synthetic */ class EmergencyNumberUtils$$ExternalSyntheticLambda0 implements Function {
    public final /* synthetic */ EmergencyNumberUtils f$0;
    public final /* synthetic */ String[] f$1;

    public /* synthetic */ EmergencyNumberUtils$$ExternalSyntheticLambda0(EmergencyNumberUtils emergencyNumberUtils, String[] strArr) {
        this.f$0 = emergencyNumberUtils;
        this.f$1 = strArr;
    }

    public final Object apply(Object obj) {
        return this.f$0.lambda$sanitizeEmergencyNumbers$0(this.f$1, (EmergencyNumber) obj);
    }
}
