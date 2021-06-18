package com.android.settings.development;

import android.content.om.OverlayInfo;
import java.util.function.ToIntFunction;

public final /* synthetic */ class OverlayCategoryPreferenceController$$ExternalSyntheticLambda2 implements ToIntFunction {
    public static final /* synthetic */ OverlayCategoryPreferenceController$$ExternalSyntheticLambda2 INSTANCE = new OverlayCategoryPreferenceController$$ExternalSyntheticLambda2();

    private /* synthetic */ OverlayCategoryPreferenceController$$ExternalSyntheticLambda2() {
    }

    public final int applyAsInt(Object obj) {
        return ((OverlayInfo) obj).priority;
    }
}
