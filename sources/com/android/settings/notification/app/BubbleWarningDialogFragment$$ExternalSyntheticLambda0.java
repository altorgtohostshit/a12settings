package com.android.settings.notification.app;

import android.content.DialogInterface;

public final /* synthetic */ class BubbleWarningDialogFragment$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public final /* synthetic */ BubbleWarningDialogFragment f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ BubbleWarningDialogFragment$$ExternalSyntheticLambda0(BubbleWarningDialogFragment bubbleWarningDialogFragment, String str, int i) {
        this.f$0 = bubbleWarningDialogFragment;
        this.f$1 = str;
        this.f$2 = i;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$onCreateDialog$1(this.f$1, this.f$2, dialogInterface, i);
    }
}
