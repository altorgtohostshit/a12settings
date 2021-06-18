package com.android.settings;

import com.android.settings.TrustedCredentialsDialogBuilder;
import java.util.function.IntConsumer;

/* renamed from: com.android.settings.TrustedCredentialsDialogBuilder$DialogEventHandler$$ExternalSyntheticLambda0 */
public final /* synthetic */ class C0584xaae824fd implements IntConsumer {
    public final /* synthetic */ TrustedCredentialsDialogBuilder.DialogEventHandler f$0;

    public /* synthetic */ C0584xaae824fd(TrustedCredentialsDialogBuilder.DialogEventHandler dialogEventHandler) {
        this.f$0 = dialogEventHandler;
    }

    public final void accept(int i) {
        this.f$0.onCredentialConfirmed(i);
    }
}
