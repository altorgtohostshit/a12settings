package com.android.settings.wallpaper;

import android.content.pm.ResolveInfo;
import android.text.TextUtils;
import androidx.preference.Preference;
import java.util.function.Predicate;

public final /* synthetic */ class WallpaperTypePreferenceController$$ExternalSyntheticLambda0 implements Predicate {
    public final /* synthetic */ Preference f$0;

    public /* synthetic */ WallpaperTypePreferenceController$$ExternalSyntheticLambda0(Preference preference) {
        this.f$0 = preference;
    }

    public final boolean test(Object obj) {
        return TextUtils.equals(this.f$0.getKey(), ((ResolveInfo) obj).activityInfo.packageName);
    }
}
