package com.android.settings.slices;

import android.net.Uri;
import android.util.Log;
import androidx.slice.widget.SliceLiveData;

public final /* synthetic */ class SlicePreferenceController$$ExternalSyntheticLambda0 implements SliceLiveData.OnErrorListener {
    public final /* synthetic */ Uri f$0;

    public /* synthetic */ SlicePreferenceController$$ExternalSyntheticLambda0(Uri uri) {
        this.f$0 = uri;
    }

    public final void onSliceError(int i, Throwable th) {
        Log.w(SlicePreferenceController.TAG, "Slice may be null. uri = " + this.f$0 + ", error = " + i);
    }
}
