package com.android.settings.notification.history;

import android.service.notification.StatusBarNotification;
import android.view.View;

public final /* synthetic */ class NotificationSbnAdapter$$ExternalSyntheticLambda0 implements View.OnLongClickListener {
    public final /* synthetic */ StatusBarNotification f$0;
    public final /* synthetic */ NotificationSbnViewHolder f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ NotificationSbnAdapter$$ExternalSyntheticLambda0(StatusBarNotification statusBarNotification, NotificationSbnViewHolder notificationSbnViewHolder, int i) {
        this.f$0 = statusBarNotification;
        this.f$1 = notificationSbnViewHolder;
        this.f$2 = i;
    }

    public final boolean onLongClick(View view) {
        return NotificationSbnAdapter.lambda$onBindViewHolder$0(this.f$0, this.f$1, this.f$2, view);
    }
}
