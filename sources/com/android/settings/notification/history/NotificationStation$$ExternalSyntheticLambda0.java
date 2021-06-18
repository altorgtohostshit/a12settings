package com.android.settings.notification.history;

import com.android.settings.notification.history.NotificationStation;
import java.util.Comparator;

public final /* synthetic */ class NotificationStation$$ExternalSyntheticLambda0 implements Comparator {
    public static final /* synthetic */ NotificationStation$$ExternalSyntheticLambda0 INSTANCE = new NotificationStation$$ExternalSyntheticLambda0();

    private /* synthetic */ NotificationStation$$ExternalSyntheticLambda0() {
    }

    public final int compare(Object obj, Object obj2) {
        return Long.compare(((NotificationStation.HistoricalNotificationInfo) obj2).timestamp, ((NotificationStation.HistoricalNotificationInfo) obj).timestamp);
    }
}
