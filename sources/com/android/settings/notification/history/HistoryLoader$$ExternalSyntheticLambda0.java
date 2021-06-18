package com.android.settings.notification.history;

import com.android.settings.notification.history.HistoryLoader;
import java.util.List;

public final /* synthetic */ class HistoryLoader$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ HistoryLoader.OnHistoryLoaderListener f$0;
    public final /* synthetic */ List f$1;

    public /* synthetic */ HistoryLoader$$ExternalSyntheticLambda0(HistoryLoader.OnHistoryLoaderListener onHistoryLoaderListener, List list) {
        this.f$0 = onHistoryLoaderListener;
        this.f$1 = list;
    }

    public final void run() {
        this.f$0.onHistoryLoaded(this.f$1);
    }
}
