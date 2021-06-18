package com.android.settings.applications.specialaccess.interactacrossprofiles;

import android.content.pm.PackageInfo;
import java.util.function.Predicate;

public final /* synthetic */ class InteractAcrossProfilesSettings$$ExternalSyntheticLambda1 implements Predicate {
    public final /* synthetic */ PackageInfo f$0;

    public /* synthetic */ InteractAcrossProfilesSettings$$ExternalSyntheticLambda1(PackageInfo packageInfo) {
        this.f$0 = packageInfo;
    }

    public final boolean test(Object obj) {
        return this.f$0.packageName.equals(((PackageInfo) obj).packageName);
    }
}
