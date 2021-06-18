package com.android.settings.users;

import android.content.pm.UserInfo;
import android.graphics.drawable.Drawable;
import java.util.function.BiConsumer;

public final /* synthetic */ class UserSettings$$ExternalSyntheticLambda4 implements BiConsumer {
    public final /* synthetic */ UserSettings f$0;
    public final /* synthetic */ Drawable f$1;
    public final /* synthetic */ UserInfo f$2;

    public /* synthetic */ UserSettings$$ExternalSyntheticLambda4(UserSettings userSettings, Drawable drawable, UserInfo userInfo) {
        this.f$0 = userSettings;
        this.f$1 = drawable;
        this.f$2 = userInfo;
    }

    public final void accept(Object obj, Object obj2) {
        this.f$0.lambda$buildEditCurrentUserDialog$1(this.f$1, this.f$2, (String) obj, (Drawable) obj2);
    }
}
