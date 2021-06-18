package com.google.android.settings.gestures.columbus;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import java.util.function.Function;

public final /* synthetic */ class ColumbusGestureTrainingLaunchActivity$$ExternalSyntheticLambda2 implements Function {
    public final /* synthetic */ PackageManager f$0;

    public /* synthetic */ ColumbusGestureTrainingLaunchActivity$$ExternalSyntheticLambda2(PackageManager packageManager) {
        this.f$0 = packageManager;
    }

    public final Object apply(Object obj) {
        return this.f$0.getApplicationLabel((ApplicationInfo) obj).toString();
    }
}
