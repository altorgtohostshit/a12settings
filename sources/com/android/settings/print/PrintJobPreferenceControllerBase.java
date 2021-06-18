package com.android.settings.print;

import android.content.Context;
import android.content.IntentFilter;
import android.print.PrintJob;
import android.print.PrintJobId;
import android.print.PrintManager;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;

public abstract class PrintJobPreferenceControllerBase extends BasePreferenceController implements LifecycleObserver, OnStart, OnStop, PrintManager.PrintJobStateChangeListener {
    private static final String EXTRA_PRINT_JOB_ID = "EXTRA_PRINT_JOB_ID";
    private static final String TAG = "PrintJobPrefCtrlBase";
    protected PrintJobSettingsFragment mFragment;
    protected Preference mPreference;
    protected PrintJobId mPrintJobId;
    private final PrintManager mPrintManager = ((PrintManager) this.mContext.getSystemService("print")).getGlobalPrintManagerForUser(this.mContext.getUserId());

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

    /* access modifiers changed from: protected */
    public abstract void updateUi();

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public PrintJobPreferenceControllerBase(Context context, String str) {
        super(context, str);
    }

    public void onStart() {
        this.mPrintManager.addPrintJobStateChangeListener(this);
        updateUi();
    }

    public void onStop() {
        this.mPrintManager.removePrintJobStateChangeListener(this);
    }

    public void onPrintJobStateChanged(PrintJobId printJobId) {
        updateUi();
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    public void init(PrintJobSettingsFragment printJobSettingsFragment) {
        this.mFragment = printJobSettingsFragment;
        processArguments();
    }

    /* access modifiers changed from: protected */
    public PrintJob getPrintJob() {
        return this.mPrintManager.getPrintJob(this.mPrintJobId);
    }

    private void processArguments() {
        String string = this.mFragment.getArguments().getString(EXTRA_PRINT_JOB_ID);
        if (string == null && (string = this.mFragment.getActivity().getIntent().getStringExtra(EXTRA_PRINT_JOB_ID)) == null) {
            Log.w(TAG, "EXTRA_PRINT_JOB_ID not set");
            this.mFragment.finish();
            return;
        }
        this.mPrintJobId = PrintJobId.unflattenFromString(string);
    }
}
