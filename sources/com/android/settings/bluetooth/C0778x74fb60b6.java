package com.android.settings.bluetooth;

import android.content.Context;
import android.view.View;
import androidx.preference.PreferenceCategory;

/* renamed from: com.android.settings.bluetooth.BluetoothDetailsCompanionAppsController$$ExternalSyntheticLambda1 */
public final /* synthetic */ class C0778x74fb60b6 implements View.OnClickListener {
    public final /* synthetic */ BluetoothDetailsCompanionAppsController f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ String f$2;
    public final /* synthetic */ PreferenceCategory f$3;
    public final /* synthetic */ CharSequence f$4;
    public final /* synthetic */ Context f$5;

    public /* synthetic */ C0778x74fb60b6(BluetoothDetailsCompanionAppsController bluetoothDetailsCompanionAppsController, String str, String str2, PreferenceCategory preferenceCategory, CharSequence charSequence, Context context) {
        this.f$0 = bluetoothDetailsCompanionAppsController;
        this.f$1 = str;
        this.f$2 = str2;
        this.f$3 = preferenceCategory;
        this.f$4 = charSequence;
        this.f$5 = context;
    }

    public final void onClick(View view) {
        this.f$0.lambda$updatePreferences$3(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, view);
    }
}
