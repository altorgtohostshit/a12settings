package com.android.settings.development;

import android.content.Context;
import android.text.format.Formatter;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.applications.ProcStatsData;
import com.android.settings.applications.ProcessStatsBase;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.development.DeveloperOptionsPreferenceController;
import com.android.settingslib.utils.ThreadUtils;

public class MemoryUsagePreferenceController extends DeveloperOptionsPreferenceController implements PreferenceControllerMixin {
    private ProcStatsData mProcStatsData;

    public String getPreferenceKey() {
        return "memory";
    }

    public MemoryUsagePreferenceController(Context context) {
        super(context);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mProcStatsData = getProcStatsData();
        setDuration();
    }

    public void updateState(Preference preference) {
        ThreadUtils.postOnBackgroundThread((Runnable) new MemoryUsagePreferenceController$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateState$1() {
        this.mProcStatsData.refreshStats(true);
        ProcStatsData.MemInfo memInfo = this.mProcStatsData.getMemInfo();
        ThreadUtils.postOnMainThread(new MemoryUsagePreferenceController$$ExternalSyntheticLambda1(this, Formatter.formatShortFileSize(this.mContext, (long) memInfo.realUsedRam), Formatter.formatShortFileSize(this.mContext, (long) memInfo.realTotalRam)));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateState$0(String str, String str2) {
        this.mPreference.setSummary((CharSequence) this.mContext.getString(R.string.memory_summary, new Object[]{str, str2}));
    }

    /* access modifiers changed from: package-private */
    public void setDuration() {
        this.mProcStatsData.setDuration(ProcessStatsBase.sDurations[0]);
    }

    /* access modifiers changed from: package-private */
    public ProcStatsData getProcStatsData() {
        return new ProcStatsData(this.mContext, false);
    }
}
