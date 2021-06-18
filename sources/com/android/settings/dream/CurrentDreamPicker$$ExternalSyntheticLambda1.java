package com.android.settings.dream;

import com.android.settings.dream.CurrentDreamPicker;
import com.android.settingslib.dream.DreamBackend;
import java.util.function.Function;

public final /* synthetic */ class CurrentDreamPicker$$ExternalSyntheticLambda1 implements Function {
    public static final /* synthetic */ CurrentDreamPicker$$ExternalSyntheticLambda1 INSTANCE = new CurrentDreamPicker$$ExternalSyntheticLambda1();

    private /* synthetic */ CurrentDreamPicker$$ExternalSyntheticLambda1() {
    }

    public final Object apply(Object obj) {
        return new CurrentDreamPicker.DreamCandidateInfo((DreamBackend.DreamInfo) obj);
    }
}
