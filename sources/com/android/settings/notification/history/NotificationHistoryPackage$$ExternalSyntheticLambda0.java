package com.android.settings.notification.history;

import android.app.NotificationHistory;
import java.util.Comparator;

public final /* synthetic */ class NotificationHistoryPackage$$ExternalSyntheticLambda0 implements Comparator {
    public static final /* synthetic */ NotificationHistoryPackage$$ExternalSyntheticLambda0 INSTANCE = new NotificationHistoryPackage$$ExternalSyntheticLambda0();

    private /* synthetic */ NotificationHistoryPackage$$ExternalSyntheticLambda0() {
    }

    public final int compare(Object obj, Object obj2) {
        return Long.compare(((NotificationHistory.HistoricalNotification) obj2).getPostedTimeMs(), ((NotificationHistory.HistoricalNotification) obj).getPostedTimeMs());
    }
}
