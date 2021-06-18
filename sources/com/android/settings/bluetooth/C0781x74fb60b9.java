package com.android.settings.bluetooth;

import androidx.preference.PreferenceCategory;
import java.util.function.Predicate;

/* renamed from: com.android.settings.bluetooth.BluetoothDetailsCompanionAppsController$$ExternalSyntheticLambda4 */
public final /* synthetic */ class C0781x74fb60b9 implements Predicate {
    public final /* synthetic */ PreferenceCategory f$0;

    public /* synthetic */ C0781x74fb60b9(PreferenceCategory preferenceCategory) {
        this.f$0 = preferenceCategory;
    }

    public final boolean test(Object obj) {
        return BluetoothDetailsCompanionAppsController.lambda$getPreferencesNeedToShow$2(this.f$0, (String) obj);
    }
}
