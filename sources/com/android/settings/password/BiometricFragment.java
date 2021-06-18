package com.android.settings.password;

import android.hardware.biometrics.BiometricPrompt;
import android.hardware.biometrics.PromptInfo;
import android.os.Bundle;
import android.os.CancellationSignal;
import com.android.settings.core.InstrumentedFragment;
import java.util.concurrent.Executor;

public class BiometricFragment extends InstrumentedFragment {
    private BiometricPrompt.AuthenticationCallback mAuthenticationCallback = new BiometricPrompt.AuthenticationCallback() {
        public void onAuthenticationError(int i, CharSequence charSequence) {
            BiometricFragment.this.mClientExecutor.execute(new BiometricFragment$1$$ExternalSyntheticLambda2(this, i, charSequence));
            BiometricFragment.this.cleanup();
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onAuthenticationError$0(int i, CharSequence charSequence) {
            BiometricFragment.this.mClientCallback.onAuthenticationError(i, charSequence);
        }

        public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult authenticationResult) {
            BiometricFragment.this.mClientExecutor.execute(new BiometricFragment$1$$ExternalSyntheticLambda3(this, authenticationResult));
            BiometricFragment.this.cleanup();
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onAuthenticationSucceeded$1(BiometricPrompt.AuthenticationResult authenticationResult) {
            BiometricFragment.this.mClientCallback.onAuthenticationSucceeded(authenticationResult);
        }

        public void onAuthenticationFailed() {
            BiometricFragment.this.mClientExecutor.execute(new BiometricFragment$1$$ExternalSyntheticLambda0(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onAuthenticationFailed$2() {
            BiometricFragment.this.mClientCallback.onAuthenticationFailed();
        }

        public void onSystemEvent(int i) {
            BiometricFragment.this.mClientExecutor.execute(new BiometricFragment$1$$ExternalSyntheticLambda1(this, i));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onSystemEvent$3(int i) {
            BiometricFragment.this.mClientCallback.onSystemEvent(i);
        }
    };
    private BiometricPrompt mBiometricPrompt;
    private CancellationSignal mCancellationSignal;
    /* access modifiers changed from: private */
    public BiometricPrompt.AuthenticationCallback mClientCallback;
    /* access modifiers changed from: private */
    public Executor mClientExecutor;
    private int mUserId;

    public int getMetricsCategory() {
        return 1585;
    }

    public static BiometricFragment newInstance(PromptInfo promptInfo) {
        BiometricFragment biometricFragment = new BiometricFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("prompt_info", promptInfo);
        biometricFragment.setArguments(bundle);
        return biometricFragment;
    }

    public void setCallbacks(Executor executor, BiometricPrompt.AuthenticationCallback authenticationCallback) {
        this.mClientExecutor = executor;
        this.mClientCallback = authenticationCallback;
    }

    public void setUser(int i) {
        this.mUserId = i;
    }

    /* access modifiers changed from: private */
    public void cleanup() {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
        PromptInfo parcelable = getArguments().getParcelable("prompt_info");
        this.mBiometricPrompt = new BiometricPrompt.Builder(getContext()).setTitle(parcelable.getTitle()).setUseDefaultTitle().setDeviceCredentialAllowed(true).setSubtitle(parcelable.getSubtitle()).setDescription(parcelable.getDescription()).setTextForDeviceCredential(parcelable.getDeviceCredentialTitle(), parcelable.getDeviceCredentialSubtitle(), parcelable.getDeviceCredentialDescription()).setConfirmationRequired(parcelable.isConfirmationRequested()).setDisallowBiometricsIfPolicyExists(parcelable.isDisallowBiometricsIfPolicyExists()).setReceiveSystemEvents(true).build();
        CancellationSignal cancellationSignal = new CancellationSignal();
        this.mCancellationSignal = cancellationSignal;
        this.mBiometricPrompt.authenticateUser(cancellationSignal, this.mClientExecutor, this.mAuthenticationCallback, this.mUserId);
    }
}
