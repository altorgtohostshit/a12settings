package com.android.settings.accessibility;

import android.accessibilityservice.AccessibilityServiceInfo;
import java.util.List;
import java.util.function.Predicate;

public final /* synthetic */ class AccessibilitySettings$$ExternalSyntheticLambda0 implements Predicate {
    public final /* synthetic */ AccessibilitySettings f$0;
    public final /* synthetic */ List f$1;

    public /* synthetic */ AccessibilitySettings$$ExternalSyntheticLambda0(AccessibilitySettings accessibilitySettings, List list) {
        this.f$0 = accessibilitySettings;
        this.f$1 = list;
    }

    public final boolean test(Object obj) {
        return this.f$0.lambda$getInstalledAccessibilityList$0(this.f$1, (AccessibilityServiceInfo) obj);
    }
}
