package com.android.settings.users;

import android.content.pm.UserInfo;
import java.util.function.Predicate;

public final /* synthetic */ class UserSettings$$ExternalSyntheticLambda5 implements Predicate {
    public static final /* synthetic */ UserSettings$$ExternalSyntheticLambda5 INSTANCE = new UserSettings$$ExternalSyntheticLambda5();

    private /* synthetic */ UserSettings$$ExternalSyntheticLambda5() {
    }

    public final boolean test(Object obj) {
        return ((UserInfo) obj).isGuest();
    }
}
