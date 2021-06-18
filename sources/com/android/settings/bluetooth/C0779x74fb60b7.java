package com.android.settings.bluetooth;

import android.content.Context;
import android.content.Intent;
import androidx.preference.Preference;

/* renamed from: com.android.settings.bluetooth.BluetoothDetailsCompanionAppsController$$ExternalSyntheticLambda2 */
public final /* synthetic */ class C0779x74fb60b7 implements Preference.OnPreferenceClickListener {
    public final /* synthetic */ Context f$0;
    public final /* synthetic */ Intent f$1;

    public /* synthetic */ C0779x74fb60b7(Context context, Intent intent) {
        this.f$0 = context;
        this.f$1 = intent;
    }

    public final boolean onPreferenceClick(Preference preference) {
        return this.f$0.startActivity(this.f$1);
    }
}
