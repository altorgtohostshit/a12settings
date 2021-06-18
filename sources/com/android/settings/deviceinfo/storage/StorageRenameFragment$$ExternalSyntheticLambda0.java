package com.android.settings.deviceinfo.storage;

import android.content.DialogInterface;
import android.os.storage.StorageManager;
import android.widget.EditText;

public final /* synthetic */ class StorageRenameFragment$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public final /* synthetic */ StorageManager f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ EditText f$2;

    public /* synthetic */ StorageRenameFragment$$ExternalSyntheticLambda0(StorageManager storageManager, String str, EditText editText) {
        this.f$0 = storageManager;
        this.f$1 = str;
        this.f$2 = editText;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.setVolumeNickname(this.f$1, this.f$2.getText().toString());
    }
}
