package com.android.settings.users;

import android.content.pm.UserInfo;
import android.graphics.drawable.Drawable;

public final /* synthetic */ class UserSettings$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ UserSettings f$0;
    public final /* synthetic */ UserInfo f$1;
    public final /* synthetic */ Drawable f$2;

    public /* synthetic */ UserSettings$$ExternalSyntheticLambda2(UserSettings userSettings, UserInfo userInfo, Drawable drawable) {
        this.f$0 = userSettings;
        this.f$1 = userInfo;
        this.f$2 = drawable;
    }

    public final void run() {
        this.f$0.lambda$buildEditCurrentUserDialog$0(this.f$1, this.f$2);
    }
}
