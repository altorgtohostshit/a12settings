package com.android.settingslib.applications;

import com.android.settingslib.applications.ApplicationsState;

public final /* synthetic */ class ApplicationsState$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ ApplicationsState f$0;
    public final /* synthetic */ ApplicationsState.AppEntry f$1;
    public final /* synthetic */ String f$2;
    public final /* synthetic */ int f$3;

    public /* synthetic */ ApplicationsState$$ExternalSyntheticLambda0(ApplicationsState applicationsState, ApplicationsState.AppEntry appEntry, String str, int i) {
        this.f$0 = applicationsState;
        this.f$1 = appEntry;
        this.f$2 = str;
        this.f$3 = i;
    }

    public final void run() {
        this.f$0.lambda$requestSize$0(this.f$1, this.f$2, this.f$3);
    }
}
