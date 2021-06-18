package com.android.settings.accessibility;

import java.util.function.Predicate;

public final /* synthetic */ class PreferredShortcuts$$ExternalSyntheticLambda1 implements Predicate {
    public final /* synthetic */ String f$0;

    public /* synthetic */ PreferredShortcuts$$ExternalSyntheticLambda1(String str) {
        this.f$0 = str;
    }

    public final boolean test(Object obj) {
        return ((String) obj).contains(this.f$0);
    }
}
