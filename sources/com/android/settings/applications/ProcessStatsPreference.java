package com.android.settings.applications;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.AttributeSet;
import android.util.Log;
import com.android.settingslib.widget.AppPreference;

public class ProcessStatsPreference extends AppPreference {
    private ProcStatsPackageEntry mEntry;

    public ProcessStatsPreference(Context context) {
        super(context, (AttributeSet) null);
    }

    public void init(ProcStatsPackageEntry procStatsPackageEntry, PackageManager packageManager, double d, double d2, double d3, boolean z) {
        double d4;
        this.mEntry = procStatsPackageEntry;
        String str = TextUtils.isEmpty(procStatsPackageEntry.mUiLabel) ? procStatsPackageEntry.mPackage : procStatsPackageEntry.mUiLabel;
        setTitle((CharSequence) str);
        if (TextUtils.isEmpty(str)) {
            Log.d("ProcessStatsPreference", "PackageEntry contained no package name or uiLabel");
        }
        ApplicationInfo applicationInfo = procStatsPackageEntry.mUiTargetApp;
        if (applicationInfo != null) {
            setIcon(applicationInfo.loadIcon(packageManager));
        } else {
            setIcon(packageManager.getDefaultActivityIcon());
        }
        double d5 = procStatsPackageEntry.mRunWeight;
        double d6 = procStatsPackageEntry.mBgWeight;
        boolean z2 = d5 > d6;
        if (z) {
            if (!z2) {
                d5 = d6;
            }
            d4 = d5 * d2;
        } else {
            d4 = ((double) (z2 ? procStatsPackageEntry.mMaxRunMem : procStatsPackageEntry.mMaxBgMem)) * d3 * 1024.0d;
        }
        setSummary((CharSequence) Formatter.formatShortFileSize(getContext(), (long) d4));
        setProgress((int) ((d4 * 100.0d) / d));
    }

    public ProcStatsPackageEntry getEntry() {
        return this.mEntry;
    }
}
