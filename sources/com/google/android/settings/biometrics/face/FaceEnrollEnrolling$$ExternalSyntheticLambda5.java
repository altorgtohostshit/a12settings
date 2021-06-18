package com.google.android.settings.biometrics.face;

public final /* synthetic */ class FaceEnrollEnrolling$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ FaceEnrollEnrolling f$0;

    public /* synthetic */ FaceEnrollEnrolling$$ExternalSyntheticLambda5(FaceEnrollEnrolling faceEnrollEnrolling) {
        this.f$0 = faceEnrollEnrolling;
    }

    public final void run() {
        this.f$0.onEnrollmentComplete();
    }
}
