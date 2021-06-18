package com.android.settings.applications.specialaccess.notificationaccess;

import androidx.preference.PreferenceScreen;
import java.util.List;
import java.util.function.Consumer;

public final /* synthetic */ class NotificationAccessDetails$$ExternalSyntheticLambda4 implements Consumer {
    public final /* synthetic */ PreferenceScreen f$0;

    public /* synthetic */ NotificationAccessDetails$$ExternalSyntheticLambda4(PreferenceScreen preferenceScreen) {
        this.f$0 = preferenceScreen;
    }

    public final void accept(Object obj) {
        ((List) obj).forEach(new NotificationAccessDetails$$ExternalSyntheticLambda2(this.f$0));
    }
}
