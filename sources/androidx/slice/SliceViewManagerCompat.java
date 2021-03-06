package androidx.slice;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.slice.compat.SliceProviderCompat;
import androidx.slice.widget.SliceLiveData;
import java.util.Collection;

class SliceViewManagerCompat extends SliceViewManagerBase {
    SliceViewManagerCompat(Context context) {
        super(context);
    }

    public void pinSlice(Uri uri) {
        SliceProviderCompat.pinSlice(this.mContext, uri, SliceLiveData.SUPPORTED_SPECS);
    }

    public void unpinSlice(Uri uri) {
        SliceProviderCompat.unpinSlice(this.mContext, uri, SliceLiveData.SUPPORTED_SPECS);
    }

    public Slice bindSlice(Uri uri) {
        return SliceProviderCompat.bindSlice(this.mContext, uri, SliceLiveData.SUPPORTED_SPECS);
    }

    public Slice bindSlice(Intent intent) {
        return SliceProviderCompat.bindSlice(this.mContext, intent, SliceLiveData.SUPPORTED_SPECS);
    }

    public Collection<Uri> getSliceDescendants(Uri uri) {
        return SliceProviderCompat.getSliceDescendants(this.mContext, uri);
    }
}
