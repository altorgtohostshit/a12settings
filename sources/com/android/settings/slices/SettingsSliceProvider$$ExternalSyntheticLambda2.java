package com.android.settings.slices;

import android.net.Uri;
import com.android.settings.core.BasePreferenceController;

public final /* synthetic */ class SettingsSliceProvider$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ SettingsSliceProvider f$0;
    public final /* synthetic */ BasePreferenceController f$1;
    public final /* synthetic */ Uri f$2;

    public /* synthetic */ SettingsSliceProvider$$ExternalSyntheticLambda2(SettingsSliceProvider settingsSliceProvider, BasePreferenceController basePreferenceController, Uri uri) {
        this.f$0 = settingsSliceProvider;
        this.f$1 = basePreferenceController;
        this.f$2 = uri;
    }

    public final void run() {
        this.f$0.lambda$loadSlice$4(this.f$1, this.f$2);
    }
}
