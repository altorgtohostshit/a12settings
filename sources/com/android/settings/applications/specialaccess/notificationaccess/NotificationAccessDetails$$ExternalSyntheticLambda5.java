package com.android.settings.applications.specialaccess.notificationaccess;

import com.android.settings.notification.NotificationBackend;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.function.Consumer;

public final /* synthetic */ class NotificationAccessDetails$$ExternalSyntheticLambda5 implements Consumer {
    public final /* synthetic */ NotificationAccessDetails f$0;
    public final /* synthetic */ NotificationBackend f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ NotificationAccessDetails$$ExternalSyntheticLambda5(NotificationAccessDetails notificationAccessDetails, NotificationBackend notificationBackend, int i) {
        this.f$0 = notificationAccessDetails;
        this.f$1 = notificationBackend;
        this.f$2 = i;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$onAttach$0(this.f$1, this.f$2, (AbstractPreferenceController) obj);
    }
}
