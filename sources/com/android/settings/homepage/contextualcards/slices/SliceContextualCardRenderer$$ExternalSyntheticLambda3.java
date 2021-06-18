package com.android.settings.homepage.contextualcards.slices;

import android.net.Uri;
import androidx.slice.widget.SliceLiveData;

public final /* synthetic */ class SliceContextualCardRenderer$$ExternalSyntheticLambda3 implements SliceLiveData.OnErrorListener {
    public final /* synthetic */ SliceContextualCardRenderer f$0;
    public final /* synthetic */ Uri f$1;

    public /* synthetic */ SliceContextualCardRenderer$$ExternalSyntheticLambda3(SliceContextualCardRenderer sliceContextualCardRenderer, Uri uri) {
        this.f$0 = sliceContextualCardRenderer;
        this.f$1 = uri;
    }

    public final void onSliceError(int i, Throwable th) {
        this.f$0.lambda$bindView$1(this.f$1, i, th);
    }
}
