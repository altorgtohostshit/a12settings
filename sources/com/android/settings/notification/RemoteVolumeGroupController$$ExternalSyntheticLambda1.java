package com.android.settings.notification;

import androidx.preference.Preference;

public final /* synthetic */ class RemoteVolumeGroupController$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ RemoteVolumeGroupController f$0;
    public final /* synthetic */ Preference f$1;
    public final /* synthetic */ Object f$2;

    public /* synthetic */ RemoteVolumeGroupController$$ExternalSyntheticLambda1(RemoteVolumeGroupController remoteVolumeGroupController, Preference preference, Object obj) {
        this.f$0 = remoteVolumeGroupController;
        this.f$1 = preference;
        this.f$2 = obj;
    }

    public final void run() {
        this.f$0.lambda$onPreferenceChange$0(this.f$1, this.f$2);
    }
}
