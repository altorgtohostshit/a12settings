package com.android.settings.notification.history;

import android.app.NotificationHistory;
import android.view.View;

public final /* synthetic */ class NotificationHistoryAdapter$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ NotificationHistoryAdapter f$0;
    public final /* synthetic */ NotificationHistory.HistoricalNotification f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ NotificationHistoryViewHolder f$3;

    public /* synthetic */ NotificationHistoryAdapter$$ExternalSyntheticLambda0(NotificationHistoryAdapter notificationHistoryAdapter, NotificationHistory.HistoricalNotification historicalNotification, int i, NotificationHistoryViewHolder notificationHistoryViewHolder) {
        this.f$0 = notificationHistoryAdapter;
        this.f$1 = historicalNotification;
        this.f$2 = i;
        this.f$3 = notificationHistoryViewHolder;
    }

    public final void onClick(View view) {
        this.f$0.lambda$onBindViewHolder$0(this.f$1, this.f$2, this.f$3, view);
    }
}
