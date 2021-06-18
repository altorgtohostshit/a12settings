package com.android.settings.biometrics.fingerprint;

import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.os.Handler;
import com.android.settings.core.InstrumentedFragment;

public class FingerprintAuthenticateSidecar extends InstrumentedFragment {
    private FingerprintManager.AuthenticationCallback mAuthenticationCallback = new FingerprintManager.AuthenticationCallback() {
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult authenticationResult) {
            CancellationSignal unused = FingerprintAuthenticateSidecar.this.mCancellationSignal = null;
            if (FingerprintAuthenticateSidecar.this.mListener != null) {
                FingerprintAuthenticateSidecar.this.mListener.onAuthenticationSucceeded(authenticationResult);
                return;
            }
            FingerprintManager.AuthenticationResult unused2 = FingerprintAuthenticateSidecar.this.mAuthenticationResult = authenticationResult;
            AuthenticationError unused3 = FingerprintAuthenticateSidecar.this.mAuthenticationError = null;
        }

        public void onAuthenticationFailed() {
            if (FingerprintAuthenticateSidecar.this.mListener != null) {
                FingerprintAuthenticateSidecar.this.mListener.onAuthenticationFailed();
            }
        }

        public void onAuthenticationError(int i, CharSequence charSequence) {
            CancellationSignal unused = FingerprintAuthenticateSidecar.this.mCancellationSignal = null;
            if (FingerprintAuthenticateSidecar.this.mListener != null) {
                FingerprintAuthenticateSidecar.this.mListener.onAuthenticationError(i, charSequence);
                return;
            }
            FingerprintAuthenticateSidecar fingerprintAuthenticateSidecar = FingerprintAuthenticateSidecar.this;
            AuthenticationError unused2 = fingerprintAuthenticateSidecar.mAuthenticationError = new AuthenticationError(i, charSequence);
            FingerprintManager.AuthenticationResult unused3 = FingerprintAuthenticateSidecar.this.mAuthenticationResult = null;
        }

        public void onAuthenticationHelp(int i, CharSequence charSequence) {
            if (FingerprintAuthenticateSidecar.this.mListener != null) {
                FingerprintAuthenticateSidecar.this.mListener.onAuthenticationHelp(i, charSequence);
            }
        }
    };
    /* access modifiers changed from: private */
    public AuthenticationError mAuthenticationError;
    /* access modifiers changed from: private */
    public FingerprintManager.AuthenticationResult mAuthenticationResult;
    /* access modifiers changed from: private */
    public CancellationSignal mCancellationSignal;
    private FingerprintManager mFingerprintManager;
    /* access modifiers changed from: private */
    public Listener mListener;

    public interface Listener {
        void onAuthenticationError(int i, CharSequence charSequence);

        void onAuthenticationFailed();

        void onAuthenticationHelp(int i, CharSequence charSequence);

        void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult authenticationResult);
    }

    public int getMetricsCategory() {
        return 1221;
    }

    private class AuthenticationError {
        int error;
        CharSequence errorString;

        public AuthenticationError(int i, CharSequence charSequence) {
            this.error = i;
            this.errorString = charSequence;
        }
    }

    public void setFingerprintManager(FingerprintManager fingerprintManager) {
        this.mFingerprintManager = fingerprintManager;
    }

    public void startAuthentication(int i) {
        CancellationSignal cancellationSignal = new CancellationSignal();
        this.mCancellationSignal = cancellationSignal;
        this.mFingerprintManager.authenticate((FingerprintManager.CryptoObject) null, cancellationSignal, this.mAuthenticationCallback, (Handler) null, i);
    }

    public void stopAuthentication() {
        CancellationSignal cancellationSignal = this.mCancellationSignal;
        if (cancellationSignal != null && !cancellationSignal.isCanceled()) {
            this.mCancellationSignal.cancel();
        }
        this.mCancellationSignal = null;
    }

    public void setListener(Listener listener) {
        int i;
        if (this.mListener == null && listener != null) {
            FingerprintManager.AuthenticationResult authenticationResult = this.mAuthenticationResult;
            if (authenticationResult != null) {
                listener.onAuthenticationSucceeded(authenticationResult);
                this.mAuthenticationResult = null;
            }
            AuthenticationError authenticationError = this.mAuthenticationError;
            if (!(authenticationError == null || (i = authenticationError.error) == 5)) {
                listener.onAuthenticationError(i, authenticationError.errorString);
                this.mAuthenticationError = null;
            }
        }
        this.mListener = listener;
    }
}
