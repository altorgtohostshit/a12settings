package com.android.settings.applications.specialaccess.notificationaccess;

import com.android.settings.notification.NotificationBackend;
import java.util.List;
import java.util.function.Consumer;

public final /* synthetic */ class NotificationAccessDetails$$ExternalSyntheticLambda6 implements Consumer {
    public final /* synthetic */ NotificationAccessDetails f$0;
    public final /* synthetic */ NotificationBackend f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ NotificationAccessDetails$$ExternalSyntheticLambda6(NotificationAccessDetails notificationAccessDetails, NotificationBackend notificationBackend, int i) {
        this.f$0 = notificationAccessDetails;
        this.f$1 = notificationBackend;
        this.f$2 = i;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$onAttach$1(this.f$1, this.f$2, (List) obj);
    }
}
