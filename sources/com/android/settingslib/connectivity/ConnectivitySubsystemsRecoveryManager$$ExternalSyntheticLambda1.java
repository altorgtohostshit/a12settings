package com.android.settingslib.connectivity;

import com.android.settingslib.connectivity.ConnectivitySubsystemsRecoveryManager;

public final /* synthetic */ class ConnectivitySubsystemsRecoveryManager$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ ConnectivitySubsystemsRecoveryManager f$0;
    public final /* synthetic */ ConnectivitySubsystemsRecoveryManager.RecoveryStatusCallback f$1;

    public /* synthetic */ ConnectivitySubsystemsRecoveryManager$$ExternalSyntheticLambda1(ConnectivitySubsystemsRecoveryManager connectivitySubsystemsRecoveryManager, ConnectivitySubsystemsRecoveryManager.RecoveryStatusCallback recoveryStatusCallback) {
        this.f$0 = connectivitySubsystemsRecoveryManager;
        this.f$1 = recoveryStatusCallback;
    }

    public final void run() {
        this.f$0.lambda$triggerSubsystemRestart$3(this.f$1);
    }
}
