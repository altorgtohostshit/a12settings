package com.android.settings.notification.app;

import android.app.NotificationChannelGroup;
import androidx.preference.Preference;

public final /* synthetic */ class ChannelListPreferenceController$$ExternalSyntheticLambda1 implements Preference.OnPreferenceClickListener {
    public final /* synthetic */ ChannelListPreferenceController f$0;
    public final /* synthetic */ NotificationChannelGroup f$1;

    public /* synthetic */ ChannelListPreferenceController$$ExternalSyntheticLambda1(ChannelListPreferenceController channelListPreferenceController, NotificationChannelGroup notificationChannelGroup) {
        this.f$0 = channelListPreferenceController;
        this.f$1 = notificationChannelGroup;
    }

    public final boolean onPreferenceClick(Preference preference) {
        return this.f$0.lambda$addOrUpdateGroupToggle$0(this.f$1, preference);
    }
}
