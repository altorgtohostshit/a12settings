package androidx.mediarouter.media;

import android.os.Handler;
import java.util.concurrent.Executor;

public final /* synthetic */ class MediaRoute2Provider$$ExternalSyntheticLambda0 implements Executor {
    public final /* synthetic */ Handler f$0;

    public /* synthetic */ MediaRoute2Provider$$ExternalSyntheticLambda0(Handler handler) {
        this.f$0 = handler;
    }

    public final void execute(Runnable runnable) {
        this.f$0.post(runnable);
    }
}
