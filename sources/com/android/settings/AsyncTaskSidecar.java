package com.android.settings;

import com.android.settingslib.utils.ThreadUtils;
import java.util.concurrent.Future;

public abstract class AsyncTaskSidecar<Param, Result> extends SidecarFragment {
    private Future<Result> mAsyncTask;

    /* access modifiers changed from: protected */
    public abstract Result doInBackground(Param param);

    /* access modifiers changed from: protected */
    /* renamed from: onPostExecute */
    public abstract void lambda$run$0(Result result);

    public void onDestroy() {
        Future<Result> future = this.mAsyncTask;
        if (future != null) {
            future.cancel(true);
        }
        super.onDestroy();
    }

    public void run(Param param) {
        setState(1, 0);
        Future<Result> future = this.mAsyncTask;
        if (future != null) {
            future.cancel(true);
        }
        this.mAsyncTask = ThreadUtils.postOnBackgroundThread((Runnable) new AsyncTaskSidecar$$ExternalSyntheticLambda0(this, param));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$run$1(Object obj) {
        ThreadUtils.postOnMainThread(new AsyncTaskSidecar$$ExternalSyntheticLambda1(this, doInBackground(obj)));
    }
}
