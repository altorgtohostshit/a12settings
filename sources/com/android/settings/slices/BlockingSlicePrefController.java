package com.android.settings.slices;

import android.content.Context;
import android.content.IntentFilter;
import androidx.slice.Slice;
import com.android.settings.core.BasePreferenceController;

public class BlockingSlicePrefController extends SlicePreferenceController implements BasePreferenceController.UiBlocker {
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public BlockingSlicePrefController(Context context, String str) {
        super(context, str);
    }

    public void onChanged(Slice slice) {
        super.onChanged(slice);
        BasePreferenceController.UiBlockListener uiBlockListener = this.mUiBlockListener;
        if (uiBlockListener != null) {
            uiBlockListener.onBlockerWorkFinished(this);
        }
    }
}
