package com.android.settings.wifi.savedaccesspoints2;

import android.text.TextUtils;
import com.android.wifitrackerlib.WifiEntry;
import java.util.function.Predicate;

public final /* synthetic */ class SavedAccessPointsPreferenceController2$$ExternalSyntheticLambda0 implements Predicate {
    public final /* synthetic */ String f$0;

    public /* synthetic */ SavedAccessPointsPreferenceController2$$ExternalSyntheticLambda0(String str) {
        this.f$0 = str;
    }

    public final boolean test(Object obj) {
        return TextUtils.equals(this.f$0, ((WifiEntry) obj).getKey());
    }
}
