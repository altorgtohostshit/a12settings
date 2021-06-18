package com.android.settings.network;

import android.content.Context;
import android.content.IntentFilter;
import android.net.EthernetManager;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.slices.SliceBackgroundWorker;

public final class EthernetTetherPreferenceController extends TetherBasePreferenceController {
    @VisibleForTesting
    EthernetManager.Listener mEthernetListener;
    private final EthernetManager mEthernetManager;
    private final String mEthernetRegex;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public int getTetherType() {
        return 5;
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public EthernetTetherPreferenceController(Context context, String str) {
        super(context, str);
        this.mEthernetRegex = context.getString(17039932);
        this.mEthernetManager = (EthernetManager) context.getSystemService("ethernet");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        C10121 r0 = new EthernetManager.Listener() {
            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onAvailabilityChanged$0() {
                EthernetTetherPreferenceController ethernetTetherPreferenceController = EthernetTetherPreferenceController.this;
                ethernetTetherPreferenceController.updateState(ethernetTetherPreferenceController.mPreference);
            }

            public void onAvailabilityChanged(String str, boolean z) {
                new Handler(Looper.getMainLooper()).post(new EthernetTetherPreferenceController$1$$ExternalSyntheticLambda0(this));
            }
        };
        this.mEthernetListener = r0;
        this.mEthernetManager.addListener(r0);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        this.mEthernetManager.removeListener(this.mEthernetListener);
        this.mEthernetListener = null;
    }

    public boolean shouldEnable() {
        for (String matches : this.mTm.getTetherableIfaces()) {
            if (matches.matches(this.mEthernetRegex)) {
                return true;
            }
        }
        return false;
    }

    public boolean shouldShow() {
        return !TextUtils.isEmpty(this.mEthernetRegex);
    }
}
