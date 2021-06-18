package com.android.settings.notification.history;

import com.android.settings.notification.history.HistoryLoader;

public final /* synthetic */ class HistoryLoader$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ HistoryLoader f$0;
    public final /* synthetic */ HistoryLoader.OnHistoryLoaderListener f$1;

    public /* synthetic */ HistoryLoader$$ExternalSyntheticLambda1(HistoryLoader historyLoader, HistoryLoader.OnHistoryLoaderListener onHistoryLoaderListener) {
        this.f$0 = historyLoader;
        this.f$1 = onHistoryLoaderListener;
    }

    public final void run() {
        this.f$0.lambda$load$2(this.f$1);
    }
}
