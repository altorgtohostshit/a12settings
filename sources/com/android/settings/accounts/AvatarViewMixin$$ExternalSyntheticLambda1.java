package com.android.settings.accounts;

import android.graphics.Bitmap;
import android.widget.ImageView;
import androidx.lifecycle.Observer;

public final /* synthetic */ class AvatarViewMixin$$ExternalSyntheticLambda1 implements Observer {
    public final /* synthetic */ ImageView f$0;

    public /* synthetic */ AvatarViewMixin$$ExternalSyntheticLambda1(ImageView imageView) {
        this.f$0 = imageView;
    }

    public final void onChanged(Object obj) {
        this.f$0.setImageBitmap((Bitmap) obj);
    }
}
