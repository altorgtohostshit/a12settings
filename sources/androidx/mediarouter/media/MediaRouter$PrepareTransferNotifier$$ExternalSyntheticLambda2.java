package androidx.mediarouter.media;

import androidx.mediarouter.media.MediaRouter;
import java.util.concurrent.Executor;

public final /* synthetic */ class MediaRouter$PrepareTransferNotifier$$ExternalSyntheticLambda2 implements Executor {
    public final /* synthetic */ MediaRouter.GlobalMediaRouter.CallbackHandler f$0;

    public /* synthetic */ MediaRouter$PrepareTransferNotifier$$ExternalSyntheticLambda2(MediaRouter.GlobalMediaRouter.CallbackHandler callbackHandler) {
        this.f$0 = callbackHandler;
    }

    public final void execute(Runnable runnable) {
        this.f$0.post(runnable);
    }
}
