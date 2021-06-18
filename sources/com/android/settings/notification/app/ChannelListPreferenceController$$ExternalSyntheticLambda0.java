package com.android.settings.notification.app;

import android.app.NotificationChannel;
import androidx.preference.Preference;

public final /* synthetic */ class ChannelListPreferenceController$$ExternalSyntheticLambda0 implements Preference.OnPreferenceChangeListener {
    public final /* synthetic */ ChannelListPreferenceController f$0;
    public final /* synthetic */ NotificationChannel f$1;

    public /* synthetic */ ChannelListPreferenceController$$ExternalSyntheticLambda0(ChannelListPreferenceController channelListPreferenceController, NotificationChannel notificationChannel) {
        this.f$0 = channelListPreferenceController;
        this.f$1 = notificationChannel;
    }

    public final boolean onPreferenceChange(Preference preference, Object obj) {
        return this.f$0.lambda$updateSingleChannelPrefs$1(this.f$1, preference, obj);
    }
}
