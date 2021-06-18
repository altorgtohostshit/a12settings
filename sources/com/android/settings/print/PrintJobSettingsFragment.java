package com.android.settings.print;

import android.content.Context;
import android.os.Bundle;
import android.print.PrintJob;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;

public class PrintJobSettingsFragment extends DashboardFragment {
    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "PrintJobSettingsFragment";
    }

    public int getMetricsCategory() {
        return 78;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.print_job_settings;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        ((PrintJobPreferenceController) use(PrintJobPreferenceController.class)).init(this);
        ((PrintJobMessagePreferenceController) use(PrintJobMessagePreferenceController.class)).init(this);
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        getListView().setEnabled(false);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        PrintJob printJob = ((PrintJobPreferenceController) use(PrintJobPreferenceController.class)).getPrintJob();
        if (printJob != null) {
            if (!printJob.getInfo().isCancelling()) {
                menu.add(0, 1, 0, getString(R.string.print_cancel)).setShowAsAction(1);
            }
            if (printJob.isFailed()) {
                menu.add(0, 2, 0, getString(R.string.print_restart)).setShowAsAction(1);
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        PrintJob printJob = ((PrintJobPreferenceController) use(PrintJobPreferenceController.class)).getPrintJob();
        if (printJob != null) {
            int itemId = menuItem.getItemId();
            if (itemId == 1) {
                printJob.cancel();
                finish();
                return true;
            } else if (itemId == 2) {
                printJob.restart();
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
