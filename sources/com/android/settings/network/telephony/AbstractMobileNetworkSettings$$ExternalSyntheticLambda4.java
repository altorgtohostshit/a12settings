package com.android.settings.network.telephony;

import com.android.settingslib.core.AbstractPreferenceController;
import java.util.function.Predicate;

public final /* synthetic */ class AbstractMobileNetworkSettings$$ExternalSyntheticLambda4 implements Predicate {
    public static final /* synthetic */ AbstractMobileNetworkSettings$$ExternalSyntheticLambda4 INSTANCE = new AbstractMobileNetworkSettings$$ExternalSyntheticLambda4();

    private /* synthetic */ AbstractMobileNetworkSettings$$ExternalSyntheticLambda4() {
    }

    public final boolean test(Object obj) {
        return ((AbstractPreferenceController) obj).isAvailable();
    }
}
