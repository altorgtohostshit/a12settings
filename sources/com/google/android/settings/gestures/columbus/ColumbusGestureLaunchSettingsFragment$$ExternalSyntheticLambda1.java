package com.google.android.settings.gestures.columbus;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import java.util.function.Predicate;

public final /* synthetic */ class ColumbusGestureLaunchSettingsFragment$$ExternalSyntheticLambda1 implements Predicate {
    public final /* synthetic */ PackageManager f$0;

    public /* synthetic */ ColumbusGestureLaunchSettingsFragment$$ExternalSyntheticLambda1(PackageManager packageManager) {
        this.f$0 = packageManager;
    }

    public final boolean test(Object obj) {
        return ColumbusGestureLaunchSettingsFragment.lambda$onResume$0(this.f$0, (ApplicationInfo) obj);
    }
}
