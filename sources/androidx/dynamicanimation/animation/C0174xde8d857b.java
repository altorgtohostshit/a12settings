package androidx.dynamicanimation.animation;

import android.view.Choreographer;

/* renamed from: androidx.dynamicanimation.animation.AnimationHandler$FrameCallbackScheduler16$$ExternalSyntheticLambda0 */
public final /* synthetic */ class C0174xde8d857b implements Choreographer.FrameCallback {
    public final /* synthetic */ Runnable f$0;

    public /* synthetic */ C0174xde8d857b(Runnable runnable) {
        this.f$0 = runnable;
    }

    public final void doFrame(long j) {
        this.f$0.run();
    }
}
