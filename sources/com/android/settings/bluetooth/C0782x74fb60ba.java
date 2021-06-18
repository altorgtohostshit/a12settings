package com.android.settings.bluetooth;

import android.companion.Association;
import com.google.common.base.Objects;
import java.util.function.Predicate;

/* renamed from: com.android.settings.bluetooth.BluetoothDetailsCompanionAppsController$$ExternalSyntheticLambda5 */
public final /* synthetic */ class C0782x74fb60ba implements Predicate {
    public final /* synthetic */ String f$0;

    public /* synthetic */ C0782x74fb60ba(String str) {
        this.f$0 = str;
    }

    public final boolean test(Object obj) {
        return Objects.equal(this.f$0, ((Association) obj).getDeviceMacAddress());
    }
}
