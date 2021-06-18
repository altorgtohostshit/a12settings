package com.android.settings.slices;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.IntentFilter;
import android.widget.Toast;
import com.android.settings.R;

public interface Sliceable {
    void copy() {
    }

    Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return null;
    }

    IntentFilter getIntentFilter() {
        return null;
    }

    boolean hasAsyncUpdate() {
        return false;
    }

    boolean isCopyableSlice() {
        return false;
    }

    boolean isPublicSlice() {
        return false;
    }

    boolean isSliceable() {
        return false;
    }

    boolean useDynamicSliceSummary() {
        return false;
    }

    static void setCopyContent(Context context, CharSequence charSequence, CharSequence charSequence2) {
        ((ClipboardManager) context.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("text", charSequence));
        Toast.makeText(context, context.getString(R.string.copyable_slice_toast, new Object[]{charSequence2}), 0).show();
    }
}
