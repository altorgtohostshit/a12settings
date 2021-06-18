package com.android.settings.users;

import android.content.pm.UserInfo;
import java.util.function.Predicate;

public final /* synthetic */ class UserSettings$$ExternalSyntheticLambda6 implements Predicate {
    public static final /* synthetic */ UserSettings$$ExternalSyntheticLambda6 INSTANCE = new UserSettings$$ExternalSyntheticLambda6();

    private /* synthetic */ UserSettings$$ExternalSyntheticLambda6() {
    }

    public final boolean test(Object obj) {
        return UserSettings.lambda$getRealUsersCount$4((UserInfo) obj);
    }
}
