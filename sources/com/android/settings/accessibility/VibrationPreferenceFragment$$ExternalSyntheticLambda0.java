package com.android.settings.accessibility;

import com.android.settings.accessibility.VibrationPreferenceFragment;
import java.util.function.Function;

public final /* synthetic */ class VibrationPreferenceFragment$$ExternalSyntheticLambda0 implements Function {
    public static final /* synthetic */ VibrationPreferenceFragment$$ExternalSyntheticLambda0 INSTANCE = new VibrationPreferenceFragment$$ExternalSyntheticLambda0();

    private /* synthetic */ VibrationPreferenceFragment$$ExternalSyntheticLambda0() {
    }

    public final Object apply(Object obj) {
        return Integer.valueOf(((VibrationPreferenceFragment.VibrationIntensityCandidateInfo) obj).getIntensity());
    }
}
