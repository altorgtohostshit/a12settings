package com.android.settings.applications.manageapplications;

import com.android.settings.applications.manageapplications.ManageApplications;
import com.android.settingslib.applications.ApplicationsState;
import java.util.Comparator;

public final /* synthetic */ class ManageApplications$ApplicationsAdapter$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ ManageApplications.ApplicationsAdapter f$0;
    public final /* synthetic */ ApplicationsState.AppFilter f$1;
    public final /* synthetic */ Comparator f$2;

    public /* synthetic */ ManageApplications$ApplicationsAdapter$$ExternalSyntheticLambda0(ManageApplications.ApplicationsAdapter applicationsAdapter, ApplicationsState.AppFilter appFilter, Comparator comparator) {
        this.f$0 = applicationsAdapter;
        this.f$1 = appFilter;
        this.f$2 = comparator;
    }

    public final void run() {
        this.f$0.lambda$rebuild$0(this.f$1, this.f$2);
    }
}
