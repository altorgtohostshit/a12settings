package com.android.settings.development.featureflags;

import android.content.Context;
import java.util.function.Consumer;

public final /* synthetic */ class FeatureFlagsPreferenceController$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ FeatureFlagsPreferenceController f$0;
    public final /* synthetic */ Context f$1;

    public /* synthetic */ FeatureFlagsPreferenceController$$ExternalSyntheticLambda0(FeatureFlagsPreferenceController featureFlagsPreferenceController, Context context) {
        this.f$0 = featureFlagsPreferenceController;
        this.f$1 = context;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$displayPreference$0(this.f$1, (String) obj);
    }
}
