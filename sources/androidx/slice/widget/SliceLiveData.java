package androidx.slice.widget;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import androidx.collection.ArraySet;
import androidx.lifecycle.LiveData;
import androidx.slice.Slice;
import androidx.slice.SliceSpec;
import androidx.slice.SliceSpecs;
import androidx.slice.SliceViewManager;
import java.util.Arrays;
import java.util.Set;

public final class SliceLiveData {
    public static final SliceSpec OLD_BASIC;
    public static final SliceSpec OLD_LIST;
    public static final Set<SliceSpec> SUPPORTED_SPECS;

    public interface OnErrorListener {
        void onSliceError(int i, Throwable th);
    }

    static {
        SliceSpec sliceSpec = new SliceSpec("androidx.app.slice.BASIC", 1);
        OLD_BASIC = sliceSpec;
        SliceSpec sliceSpec2 = new SliceSpec("androidx.app.slice.LIST", 1);
        OLD_LIST = sliceSpec2;
        SUPPORTED_SPECS = new ArraySet(Arrays.asList(new SliceSpec[]{SliceSpecs.BASIC, SliceSpecs.LIST, SliceSpecs.LIST_V2, sliceSpec, sliceSpec2}));
    }

    public static LiveData<Slice> fromUri(Context context, Uri uri, OnErrorListener onErrorListener) {
        return new SliceLiveDataImpl(context.getApplicationContext(), uri, onErrorListener);
    }

    private static class SliceLiveDataImpl extends LiveData<Slice> {
        final Intent mIntent;
        final OnErrorListener mListener;
        final SliceViewManager.SliceCallback mSliceCallback = new SliceLiveData$SliceLiveDataImpl$$ExternalSyntheticLambda0(this);
        final SliceViewManager mSliceViewManager;
        private final Runnable mUpdateSlice = new Runnable() {
            public void run() {
                Slice slice;
                try {
                    SliceLiveDataImpl sliceLiveDataImpl = SliceLiveDataImpl.this;
                    Uri uri = sliceLiveDataImpl.mUri;
                    if (uri != null) {
                        slice = sliceLiveDataImpl.mSliceViewManager.bindSlice(uri);
                    } else {
                        slice = sliceLiveDataImpl.mSliceViewManager.bindSlice(sliceLiveDataImpl.mIntent);
                    }
                    SliceLiveDataImpl sliceLiveDataImpl2 = SliceLiveDataImpl.this;
                    if (sliceLiveDataImpl2.mUri == null && slice != null) {
                        sliceLiveDataImpl2.mUri = slice.getUri();
                        SliceLiveDataImpl sliceLiveDataImpl3 = SliceLiveDataImpl.this;
                        sliceLiveDataImpl3.mSliceViewManager.registerSliceCallback(sliceLiveDataImpl3.mUri, sliceLiveDataImpl3.mSliceCallback);
                    }
                    SliceLiveDataImpl.this.postValue(slice);
                } catch (IllegalArgumentException e) {
                    SliceLiveDataImpl.this.onSliceError(3, e);
                    SliceLiveDataImpl.this.postValue(null);
                } catch (Exception e2) {
                    SliceLiveDataImpl.this.onSliceError(0, e2);
                    SliceLiveDataImpl.this.postValue(null);
                }
            }
        };
        Uri mUri;

        SliceLiveDataImpl(Context context, Uri uri, OnErrorListener onErrorListener) {
            this.mSliceViewManager = SliceViewManager.getInstance(context);
            this.mUri = uri;
            this.mIntent = null;
            this.mListener = onErrorListener;
        }

        /* access modifiers changed from: protected */
        public void onActive() {
            AsyncTask.execute(this.mUpdateSlice);
            Uri uri = this.mUri;
            if (uri != null) {
                this.mSliceViewManager.registerSliceCallback(uri, this.mSliceCallback);
            }
        }

        /* access modifiers changed from: protected */
        public void onInactive() {
            Uri uri = this.mUri;
            if (uri != null) {
                this.mSliceViewManager.unregisterSliceCallback(uri, this.mSliceCallback);
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(Slice slice) {
            postValue(slice);
        }

        /* access modifiers changed from: package-private */
        public void onSliceError(int i, Throwable th) {
            OnErrorListener onErrorListener = this.mListener;
            if (onErrorListener != null) {
                onErrorListener.onSliceError(i, th);
            } else {
                Log.e("SliceLiveData", "Error binding slice", th);
            }
        }
    }
}
