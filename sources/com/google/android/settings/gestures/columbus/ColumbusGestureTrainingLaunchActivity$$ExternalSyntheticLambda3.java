package com.google.android.settings.gestures.columbus;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import java.util.function.Predicate;

public final /* synthetic */ class ColumbusGestureTrainingLaunchActivity$$ExternalSyntheticLambda3 implements Predicate {
    public final /* synthetic */ PackageManager f$0;

    public /* synthetic */ ColumbusGestureTrainingLaunchActivity$$ExternalSyntheticLambda3(PackageManager packageManager) {
        this.f$0 = packageManager;
    }

    public final boolean test(Object obj) {
        return ColumbusGestureTrainingLaunchActivity.lambda$onCreate$0(this.f$0, (ApplicationInfo) obj);
    }
}
