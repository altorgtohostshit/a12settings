package com.android.settings.dream;

import com.android.settingslib.dream.DreamBackend;
import java.util.function.Predicate;

public final /* synthetic */ class CurrentDreamPreferenceController$$ExternalSyntheticLambda1 implements Predicate {
    public static final /* synthetic */ CurrentDreamPreferenceController$$ExternalSyntheticLambda1 INSTANCE = new CurrentDreamPreferenceController$$ExternalSyntheticLambda1();

    private /* synthetic */ CurrentDreamPreferenceController$$ExternalSyntheticLambda1() {
    }

    public final boolean test(Object obj) {
        return ((DreamBackend.DreamInfo) obj).isActive;
    }
}
