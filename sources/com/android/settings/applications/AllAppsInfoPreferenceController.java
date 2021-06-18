package com.android.settings.applications;

import android.app.usage.UsageStats;
import android.content.Context;
import android.content.IntentFilter;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.applications.RecentAppStatsMixin;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import java.util.List;

public class AllAppsInfoPreferenceController extends BasePreferenceController implements RecentAppStatsMixin.RecentAppStatsListener {
    Preference mPreference;

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

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public AllAppsInfoPreferenceController(Context context, String str) {
        super(context, str);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        Preference findPreference = preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = findPreference;
        findPreference.setVisible(false);
    }

    public void onReloadDataCompleted(List<UsageStats> list) {
        if (!list.isEmpty()) {
            this.mPreference.setVisible(false);
            return;
        }
        this.mPreference.setVisible(true);
        Context context = this.mContext;
        new InstalledAppCounter(context, -1, context.getPackageManager()) {
            /* access modifiers changed from: protected */
            public void onCountComplete(int i) {
                AllAppsInfoPreferenceController allAppsInfoPreferenceController = AllAppsInfoPreferenceController.this;
                allAppsInfoPreferenceController.mPreference.setSummary((CharSequence) allAppsInfoPreferenceController.mContext.getString(R.string.apps_summary, new Object[]{Integer.valueOf(i)}));
            }
        }.execute(new Void[0]);
    }
}
