package com.android.settings.biometrics.fingerprint;

import android.hardware.fingerprint.Fingerprint;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.util.Log;
import com.android.settings.core.InstrumentedFragment;
import java.util.LinkedList;
import java.util.Queue;

public class FingerprintRemoveSidecar extends InstrumentedFragment {
    FingerprintManager mFingerprintManager;
    /* access modifiers changed from: private */
    public Fingerprint mFingerprintRemoving;
    /* access modifiers changed from: private */
    public Queue<Object> mFingerprintsRemoved = new LinkedList();
    /* access modifiers changed from: private */
    public Listener mListener;
    private FingerprintManager.RemovalCallback mRemoveCallback = new FingerprintManager.RemovalCallback() {
        public void onRemovalSucceeded(Fingerprint fingerprint, int i) {
            if (FingerprintRemoveSidecar.this.mListener != null) {
                FingerprintRemoveSidecar.this.mListener.onRemovalSucceeded(fingerprint);
            } else {
                FingerprintRemoveSidecar.this.mFingerprintsRemoved.add(fingerprint);
            }
            Fingerprint unused = FingerprintRemoveSidecar.this.mFingerprintRemoving = null;
        }

        public void onRemovalError(Fingerprint fingerprint, int i, CharSequence charSequence) {
            if (FingerprintRemoveSidecar.this.mListener != null) {
                FingerprintRemoveSidecar.this.mListener.onRemovalError(fingerprint, i, charSequence);
            } else {
                FingerprintRemoveSidecar.this.mFingerprintsRemoved.add(new RemovalError(fingerprint, i, charSequence));
            }
            Fingerprint unused = FingerprintRemoveSidecar.this.mFingerprintRemoving = null;
        }
    };

    public interface Listener {
        void onRemovalError(Fingerprint fingerprint, int i, CharSequence charSequence);

        void onRemovalSucceeded(Fingerprint fingerprint);
    }

    public int getMetricsCategory() {
        return 934;
    }

    private class RemovalError {
        int errMsgId;
        CharSequence errString;
        Fingerprint fingerprint;

        public RemovalError(Fingerprint fingerprint2, int i, CharSequence charSequence) {
            this.fingerprint = fingerprint2;
            this.errMsgId = i;
            this.errString = charSequence;
        }
    }

    public void startRemove(Fingerprint fingerprint, int i) {
        if (this.mFingerprintRemoving != null) {
            Log.e("FingerprintRemoveSidecar", "Remove already in progress");
            return;
        }
        this.mFingerprintRemoving = fingerprint;
        this.mFingerprintManager.remove(fingerprint, i, this.mRemoveCallback);
    }

    public void setFingerprintManager(FingerprintManager fingerprintManager) {
        this.mFingerprintManager = fingerprintManager;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
    }

    public void setListener(Listener listener) {
        if (this.mListener == null && listener != null) {
            while (!this.mFingerprintsRemoved.isEmpty()) {
                Object poll = this.mFingerprintsRemoved.poll();
                if (poll instanceof Fingerprint) {
                    listener.onRemovalSucceeded((Fingerprint) poll);
                } else if (poll instanceof RemovalError) {
                    RemovalError removalError = (RemovalError) poll;
                    listener.onRemovalError(removalError.fingerprint, removalError.errMsgId, removalError.errString);
                }
            }
        }
        this.mListener = listener;
    }

    /* access modifiers changed from: package-private */
    public final boolean isRemovingFingerprint(int i) {
        return inProgress() && this.mFingerprintRemoving.getBiometricId() == i;
    }

    /* access modifiers changed from: package-private */
    public final boolean inProgress() {
        return this.mFingerprintRemoving != null;
    }
}
