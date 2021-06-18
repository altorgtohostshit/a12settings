package com.android.settings.development;

import android.content.om.OverlayInfo;
import java.util.function.Predicate;

public final /* synthetic */ class OverlayCategoryPreferenceController$$ExternalSyntheticLambda1 implements Predicate {
    public static final /* synthetic */ OverlayCategoryPreferenceController$$ExternalSyntheticLambda1 INSTANCE = new OverlayCategoryPreferenceController$$ExternalSyntheticLambda1();

    private /* synthetic */ OverlayCategoryPreferenceController$$ExternalSyntheticLambda1() {
    }

    public final boolean test(Object obj) {
        return ((OverlayInfo) obj).isEnabled();
    }
}
