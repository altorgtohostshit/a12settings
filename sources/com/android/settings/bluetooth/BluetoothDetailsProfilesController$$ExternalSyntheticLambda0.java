package com.android.settings.bluetooth;

import androidx.preference.Preference;
import com.android.settingslib.bluetooth.A2dpProfile;

public final /* synthetic */ class BluetoothDetailsProfilesController$$ExternalSyntheticLambda0 implements Preference.OnPreferenceClickListener {
    public final /* synthetic */ BluetoothDetailsProfilesController f$0;
    public final /* synthetic */ A2dpProfile f$1;

    public /* synthetic */ BluetoothDetailsProfilesController$$ExternalSyntheticLambda0(BluetoothDetailsProfilesController bluetoothDetailsProfilesController, A2dpProfile a2dpProfile) {
        this.f$0 = bluetoothDetailsProfilesController;
        this.f$1 = a2dpProfile;
    }

    public final boolean onPreferenceClick(Preference preference) {
        return this.f$0.lambda$maybeAddHighQualityAudioPref$0(this.f$1, preference);
    }
}
