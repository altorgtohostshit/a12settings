package com.android.settings.applications.intentpicker;

import android.content.pm.verify.domain.DomainOwner;
import java.util.function.Predicate;

public final /* synthetic */ class SupportedLinkWrapper$$ExternalSyntheticLambda1 implements Predicate {
    public final /* synthetic */ boolean f$0;

    public /* synthetic */ SupportedLinkWrapper$$ExternalSyntheticLambda1(boolean z) {
        this.f$0 = z;
    }

    public final boolean test(Object obj) {
        return SupportedLinkWrapper.lambda$getLastPackageLabel$1(this.f$0, (DomainOwner) obj);
    }
}
