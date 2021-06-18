package com.android.settings.print;

import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.print.PrintJob;
import android.print.PrintJobId;
import android.print.PrintJobInfo;
import android.print.PrintManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.RestrictedPreference;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import java.util.List;

public class PrintSettingPreferenceController extends BasePreferenceController implements LifecycleObserver, OnStart, OnStop, PrintManager.PrintJobStateChangeListener {
    private static final String KEY_PRINTING_SETTINGS = "connected_device_printing";
    private final PackageManager mPackageManager;
    private Preference mPreference;
    private final PrintManager mPrintManager;

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

    public PrintSettingPreferenceController(Context context) {
        super(context, KEY_PRINTING_SETTINGS);
        this.mPackageManager = context.getPackageManager();
        this.mPrintManager = ((PrintManager) context.getSystemService("print")).getGlobalPrintManagerForUser(context.getUserId());
    }

    public int getAvailabilityStatus() {
        return (!this.mPackageManager.hasSystemFeature("android.software.print") || this.mPrintManager == null) ? 3 : 0;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    public void onStart() {
        PrintManager printManager = this.mPrintManager;
        if (printManager != null) {
            printManager.addPrintJobStateChangeListener(this);
        }
    }

    public void onStop() {
        PrintManager printManager = this.mPrintManager;
        if (printManager != null) {
            printManager.removePrintJobStateChangeListener(this);
        }
    }

    public void onPrintJobStateChanged(PrintJobId printJobId) {
        updateState(this.mPreference);
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        ((RestrictedPreference) preference).checkRestrictionAndSetDisabled("no_printing");
    }

    public CharSequence getSummary() {
        int i;
        List<PrintJob> printJobs = this.mPrintManager.getPrintJobs();
        if (printJobs != null) {
            i = 0;
            for (PrintJob info : printJobs) {
                if (shouldShowToUser(info.getInfo())) {
                    i++;
                }
            }
        } else {
            i = 0;
        }
        if (i > 0) {
            return this.mContext.getResources().getQuantityString(R.plurals.print_jobs_summary, i, new Object[]{Integer.valueOf(i)});
        }
        List printServices = this.mPrintManager.getPrintServices(1);
        if (printServices == null || printServices.isEmpty()) {
            return this.mContext.getText(R.string.print_settings_summary_no_service);
        }
        int size = printServices.size();
        return this.mContext.getResources().getQuantityString(R.plurals.print_settings_summary, size, new Object[]{Integer.valueOf(size)});
    }

    static boolean shouldShowToUser(PrintJobInfo printJobInfo) {
        int state = printJobInfo.getState();
        return state == 2 || state == 3 || state == 4 || state == 6;
    }
}
