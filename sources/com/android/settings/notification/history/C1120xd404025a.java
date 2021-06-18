package com.android.settings.notification.history;

import android.view.View;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.notification.history.NotificationStation;

/* renamed from: com.android.settings.notification.history.NotificationStation$HistoricalNotificationPreference$$ExternalSyntheticLambda0 */
public final /* synthetic */ class C1120xd404025a implements View.OnLongClickListener {
    public final /* synthetic */ NotificationStation.HistoricalNotificationPreference f$0;
    public final /* synthetic */ PreferenceViewHolder f$1;

    public /* synthetic */ C1120xd404025a(NotificationStation.HistoricalNotificationPreference historicalNotificationPreference, PreferenceViewHolder preferenceViewHolder) {
        this.f$0 = historicalNotificationPreference;
        this.f$1 = preferenceViewHolder;
    }

    public final boolean onLongClick(View view) {
        return this.f$0.lambda$onBindViewHolder$0(this.f$1, view);
    }
}
