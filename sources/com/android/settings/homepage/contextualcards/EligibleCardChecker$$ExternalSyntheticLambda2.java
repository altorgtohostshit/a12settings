package com.android.settings.homepage.contextualcards;

import android.net.Uri;
import android.os.AsyncTask;
import androidx.slice.SliceViewManager;

public final /* synthetic */ class EligibleCardChecker$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ SliceViewManager f$0;
    public final /* synthetic */ Uri f$1;
    public final /* synthetic */ SliceViewManager.SliceCallback f$2;

    public /* synthetic */ EligibleCardChecker$$ExternalSyntheticLambda2(SliceViewManager sliceViewManager, Uri uri, SliceViewManager.SliceCallback sliceCallback) {
        this.f$0 = sliceViewManager;
        this.f$1 = uri;
        this.f$2 = sliceCallback;
    }

    public final void run() {
        AsyncTask.execute(new EligibleCardChecker$$ExternalSyntheticLambda1(this.f$0, this.f$1, this.f$2));
    }
}
