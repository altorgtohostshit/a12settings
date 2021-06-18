package com.android.settings.development.storage;

import android.app.blob.BlobInfo;
import android.content.DialogInterface;

public final /* synthetic */ class BlobInfoListView$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public final /* synthetic */ BlobInfoListView f$0;
    public final /* synthetic */ BlobInfo f$1;

    public /* synthetic */ BlobInfoListView$$ExternalSyntheticLambda0(BlobInfoListView blobInfoListView, BlobInfo blobInfo) {
        this.f$0 = blobInfoListView;
        this.f$1 = blobInfo;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$getDialogOnClickListener$0(this.f$1, dialogInterface, i);
    }
}
