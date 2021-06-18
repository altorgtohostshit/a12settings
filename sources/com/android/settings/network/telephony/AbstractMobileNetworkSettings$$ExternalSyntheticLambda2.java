package com.android.settings.network.telephony;

import androidx.preference.PreferenceScreen;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.function.Consumer;

public final /* synthetic */ class AbstractMobileNetworkSettings$$ExternalSyntheticLambda2 implements Consumer {
    public final /* synthetic */ AbstractMobileNetworkSettings f$0;
    public final /* synthetic */ PreferenceScreen f$1;

    public /* synthetic */ AbstractMobileNetworkSettings$$ExternalSyntheticLambda2(AbstractMobileNetworkSettings abstractMobileNetworkSettings, PreferenceScreen preferenceScreen) {
        this.f$0 = abstractMobileNetworkSettings;
        this.f$1 = preferenceScreen;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$updatePreferenceStates$3(this.f$1, (AbstractPreferenceController) obj);
    }
}
