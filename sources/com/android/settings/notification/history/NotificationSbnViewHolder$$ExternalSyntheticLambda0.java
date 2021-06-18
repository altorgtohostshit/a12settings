package com.android.settings.notification.history;

import android.app.PendingIntent;
import android.content.Intent;
import android.view.View;
import com.android.internal.logging.InstanceId;
import com.android.internal.logging.UiEventLogger;

public final /* synthetic */ class NotificationSbnViewHolder$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ NotificationSbnViewHolder f$0;
    public final /* synthetic */ UiEventLogger f$1;
    public final /* synthetic */ int f$10;
    public final /* synthetic */ boolean f$2;
    public final /* synthetic */ int f$3;
    public final /* synthetic */ String f$4;
    public final /* synthetic */ InstanceId f$5;
    public final /* synthetic */ int f$6;
    public final /* synthetic */ PendingIntent f$7;
    public final /* synthetic */ boolean f$8;
    public final /* synthetic */ Intent f$9;

    public /* synthetic */ NotificationSbnViewHolder$$ExternalSyntheticLambda0(NotificationSbnViewHolder notificationSbnViewHolder, UiEventLogger uiEventLogger, boolean z, int i, String str, InstanceId instanceId, int i2, PendingIntent pendingIntent, boolean z2, Intent intent, int i3) {
        this.f$0 = notificationSbnViewHolder;
        this.f$1 = uiEventLogger;
        this.f$2 = z;
        this.f$3 = i;
        this.f$4 = str;
        this.f$5 = instanceId;
        this.f$6 = i2;
        this.f$7 = pendingIntent;
        this.f$8 = z2;
        this.f$9 = intent;
        this.f$10 = i3;
    }

    public final void onClick(View view) {
        this.f$0.lambda$addOnClick$0(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, this.f$9, this.f$10, view);
    }
}
