package com.android.settings.notification.history;

import android.view.View;
import android.widget.TextView;
import com.android.settings.notification.history.NotificationHistoryAdapter;

public final /* synthetic */ class NotificationHistoryActivity$$ExternalSyntheticLambda2 implements NotificationHistoryAdapter.OnItemDeletedListener {
    public final /* synthetic */ NotificationHistoryActivity f$0;
    public final /* synthetic */ TextView f$1;
    public final /* synthetic */ View f$2;

    public /* synthetic */ NotificationHistoryActivity$$ExternalSyntheticLambda2(NotificationHistoryActivity notificationHistoryActivity, TextView textView, View view) {
        this.f$0 = notificationHistoryActivity;
        this.f$1 = textView;
        this.f$2 = view;
    }

    public final void onItemDeleted(int i) {
        this.f$0.lambda$new$1(this.f$1, this.f$2, i);
    }
}
