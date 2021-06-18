package com.android.settings.applications.specialaccess.deviceadmin;

import android.content.ComponentName;
import java.util.function.Predicate;

public final /* synthetic */ class DeviceAdminAdd$$ExternalSyntheticLambda0 implements Predicate {
    public final /* synthetic */ String f$0;

    public /* synthetic */ DeviceAdminAdd$$ExternalSyntheticLambda0(String str) {
        this.f$0 = str;
    }

    public final boolean test(Object obj) {
        return ((ComponentName) obj).getPackageName().equals(this.f$0);
    }
}
