package com.android.settings.accessibility;

import java.util.function.Predicate;

public final /* synthetic */ class PreferredShortcuts$$ExternalSyntheticLambda0 implements Predicate {
    public final /* synthetic */ String f$0;

    public /* synthetic */ PreferredShortcuts$$ExternalSyntheticLambda0(String str) {
        this.f$0 = str;
    }

    public final boolean test(Object obj) {
        return PreferredShortcuts.lambda$retrieveUserShortcutType$0(this.f$0, (String) obj);
    }
}
