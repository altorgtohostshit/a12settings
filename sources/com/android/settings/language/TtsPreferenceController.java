package com.android.settings.language;

import android.content.Context;
import android.content.IntentFilter;
import android.speech.tts.TtsEngines;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;

public class TtsPreferenceController extends BasePreferenceController {
    TtsEngines mTtsEngines;

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

    public TtsPreferenceController(Context context, String str) {
        super(context, str);
        this.mTtsEngines = new TtsEngines(context);
    }

    public int getAvailabilityStatus() {
        return (this.mTtsEngines.getEngines().isEmpty() || !this.mContext.getResources().getBoolean(R.bool.config_show_tts_settings_summary)) ? 2 : 0;
    }
}
