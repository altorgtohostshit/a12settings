package com.android.settingslib.users;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.widget.EditText;
import java.util.function.BiConsumer;

public final /* synthetic */ class EditUserInfoController$$ExternalSyntheticLambda1 implements DialogInterface.OnClickListener {
    public final /* synthetic */ EditUserInfoController f$0;
    public final /* synthetic */ Drawable f$1;
    public final /* synthetic */ EditText f$2;
    public final /* synthetic */ String f$3;
    public final /* synthetic */ BiConsumer f$4;

    public /* synthetic */ EditUserInfoController$$ExternalSyntheticLambda1(EditUserInfoController editUserInfoController, Drawable drawable, EditText editText, String str, BiConsumer biConsumer) {
        this.f$0 = editUserInfoController;
        this.f$1 = drawable;
        this.f$2 = editText;
        this.f$3 = str;
        this.f$4 = biConsumer;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$buildDialog$0(this.f$1, this.f$2, this.f$3, this.f$4, dialogInterface, i);
    }
}
