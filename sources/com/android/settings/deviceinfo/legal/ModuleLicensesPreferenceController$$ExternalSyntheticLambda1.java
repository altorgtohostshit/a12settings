package com.android.settings.deviceinfo.legal;

import android.content.pm.ModuleInfo;
import java.util.function.Function;

public final /* synthetic */ class ModuleLicensesPreferenceController$$ExternalSyntheticLambda1 implements Function {
    public static final /* synthetic */ ModuleLicensesPreferenceController$$ExternalSyntheticLambda1 INSTANCE = new ModuleLicensesPreferenceController$$ExternalSyntheticLambda1();

    private /* synthetic */ ModuleLicensesPreferenceController$$ExternalSyntheticLambda1() {
    }

    public final Object apply(Object obj) {
        return ((ModuleInfo) obj).getName().toString();
    }
}
