package com.android.settings.applications.specialaccess.interactacrossprofiles;

import android.content.Context;
import android.content.pm.CrossProfileApps;
import android.util.Pair;
import java.util.function.Predicate;

public final /* synthetic */ class InteractAcrossProfilesSettings$$ExternalSyntheticLambda0 implements Predicate {
    public final /* synthetic */ Context f$0;
    public final /* synthetic */ CrossProfileApps f$1;

    public /* synthetic */ InteractAcrossProfilesSettings$$ExternalSyntheticLambda0(Context context, CrossProfileApps crossProfileApps) {
        this.f$0 = context;
        this.f$1 = crossProfileApps;
    }

    public final boolean test(Object obj) {
        return InteractAcrossProfilesSettings.lambda$getNumberOfEnabledApps$1(this.f$0, this.f$1, (Pair) obj);
    }
}
