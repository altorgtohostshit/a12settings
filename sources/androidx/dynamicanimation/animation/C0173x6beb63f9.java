package androidx.dynamicanimation.animation;

import androidx.dynamicanimation.animation.AnimationHandler;

/* renamed from: androidx.dynamicanimation.animation.AnimationHandler$FrameCallbackScheduler14$$ExternalSyntheticLambda0 */
public final /* synthetic */ class C0173x6beb63f9 implements Runnable {
    public final /* synthetic */ AnimationHandler.FrameCallbackScheduler14 f$0;
    public final /* synthetic */ Runnable f$1;

    public /* synthetic */ C0173x6beb63f9(AnimationHandler.FrameCallbackScheduler14 frameCallbackScheduler14, Runnable runnable) {
        this.f$0 = frameCallbackScheduler14;
        this.f$1 = runnable;
    }

    public final void run() {
        this.f$0.lambda$postFrameCallback$0(this.f$1);
    }
}
