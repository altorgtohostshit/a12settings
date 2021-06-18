package com.android.settings.development.transcode;

import android.content.Context;
import android.content.IntentFilter;
import android.os.SystemProperties;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;

public class TranscodeDefaultOptionPreferenceController extends TogglePreferenceController {
    private static final String TRANSCODE_DEFAULT_SYS_PROP_KEY = "persist.sys.fuse.transcode_default";

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public int getAvailabilityStatus() {
        return 0;
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

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public TranscodeDefaultOptionPreferenceController(Context context, String str) {
        super(context, str);
    }

    public boolean isChecked() {
        return !SystemProperties.getBoolean(TRANSCODE_DEFAULT_SYS_PROP_KEY, false);
    }

    public boolean setChecked(boolean z) {
        SystemProperties.set(TRANSCODE_DEFAULT_SYS_PROP_KEY, String.valueOf(!z));
        return true;
    }
}
