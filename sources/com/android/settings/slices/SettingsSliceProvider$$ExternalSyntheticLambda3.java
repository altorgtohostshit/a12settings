package com.android.settings.slices;

import android.net.Uri;

public final /* synthetic */ class SettingsSliceProvider$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ SettingsSliceProvider f$0;
    public final /* synthetic */ CustomSliceable f$1;
    public final /* synthetic */ Uri f$2;

    public /* synthetic */ SettingsSliceProvider$$ExternalSyntheticLambda3(SettingsSliceProvider settingsSliceProvider, CustomSliceable customSliceable, Uri uri) {
        this.f$0 = settingsSliceProvider;
        this.f$1 = customSliceable;
        this.f$2 = uri;
    }

    public final void run() {
        this.f$0.lambda$onSlicePinned$0(this.f$1, this.f$2);
    }
}
