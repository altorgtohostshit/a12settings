package com.android.settings.network.telephony;

import android.util.Log;
import com.android.settings.core.BasePreferenceController;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.utils.ThreadUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class TelephonyStatusControlSession implements AutoCloseable {
    private Collection<AbstractPreferenceController> mControllers;
    private Collection<Future<Boolean>> mResult;

    public static class Builder {
        private Collection<AbstractPreferenceController> mControllers;

        public Builder(Collection<AbstractPreferenceController> collection) {
            this.mControllers = collection;
        }

        public TelephonyStatusControlSession build() {
            return new TelephonyStatusControlSession(this.mControllers);
        }
    }

    private TelephonyStatusControlSession(Collection<AbstractPreferenceController> collection) {
        this.mResult = new ArrayList();
        this.mControllers = collection;
        collection.forEach(new TelephonyStatusControlSession$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(AbstractPreferenceController abstractPreferenceController) {
        this.mResult.add(ThreadUtils.postOnBackgroundThread((Callable) new TelephonyStatusControlSession$$ExternalSyntheticLambda0(this, abstractPreferenceController)));
    }

    public void close() {
        for (Future future : this.mResult) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                Log.e("TelephonyStatusControlSS", "setup availability status failed!", e);
            }
        }
        unsetAvailabilityStatus(this.mControllers);
    }

    /* access modifiers changed from: private */
    /* renamed from: setupAvailabilityStatus */
    public Boolean lambda$new$0(AbstractPreferenceController abstractPreferenceController) {
        try {
            if (abstractPreferenceController instanceof TelephonyAvailabilityHandler) {
                ((TelephonyAvailabilityHandler) abstractPreferenceController).setAvailabilityStatus(((BasePreferenceController) abstractPreferenceController).getAvailabilityStatus());
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            Log.e("TelephonyStatusControlSS", "Setup availability status failed!", e);
            return Boolean.FALSE;
        }
    }

    private void unsetAvailabilityStatus(Collection<AbstractPreferenceController> collection) {
        collection.stream().filter(TelephonyStatusControlSession$$ExternalSyntheticLambda4.INSTANCE).map(new TelephonyStatusControlSession$$ExternalSyntheticLambda3(TelephonyAvailabilityHandler.class)).forEach(TelephonyStatusControlSession$$ExternalSyntheticLambda2.INSTANCE);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$unsetAvailabilityStatus$2(AbstractPreferenceController abstractPreferenceController) {
        return abstractPreferenceController instanceof TelephonyAvailabilityHandler;
    }
}
