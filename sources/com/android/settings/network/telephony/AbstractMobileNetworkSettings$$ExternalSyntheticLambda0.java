package com.android.settings.network.telephony;

import androidx.preference.PreferenceScreen;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.function.Consumer;

public final /* synthetic */ class AbstractMobileNetworkSettings$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ PreferenceScreen f$0;

    public /* synthetic */ AbstractMobileNetworkSettings$$ExternalSyntheticLambda0(PreferenceScreen preferenceScreen) {
        this.f$0 = preferenceScreen;
    }

    public final void accept(Object obj) {
        ((AbstractPreferenceController) obj).updateState(this.f$0.findPreference(((AbstractPreferenceController) obj).getPreferenceKey()));
    }
}
