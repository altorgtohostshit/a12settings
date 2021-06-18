package com.android.settings.biometrics.fingerprint;

import android.content.DialogInterface;
import com.android.settings.biometrics.fingerprint.FingerprintSettings;

/* renamed from: com.android.settings.biometrics.fingerprint.FingerprintSettings$FingerprintSettingsFragment$$ExternalSyntheticLambda0 */
public final /* synthetic */ class C0769xd2803ea9 implements DialogInterface.OnDismissListener {
    public final /* synthetic */ FingerprintSettings.FingerprintSettingsFragment f$0;

    public /* synthetic */ C0769xd2803ea9(FingerprintSettings.FingerprintSettingsFragment fingerprintSettingsFragment) {
        this.f$0 = fingerprintSettingsFragment;
    }

    public final void onDismiss(DialogInterface dialogInterface) {
        this.f$0.lambda$showRenameDialog$1(dialogInterface);
    }
}
