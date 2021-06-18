package com.android.settings.applications;

import com.android.settings.applications.RecentAppStatsMixin;

public final /* synthetic */ class RecentAppStatsMixin$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ RecentAppStatsMixin f$0;
    public final /* synthetic */ RecentAppStatsMixin.RecentAppStatsListener f$1;

    public /* synthetic */ RecentAppStatsMixin$$ExternalSyntheticLambda1(RecentAppStatsMixin recentAppStatsMixin, RecentAppStatsMixin.RecentAppStatsListener recentAppStatsListener) {
        this.f$0 = recentAppStatsMixin;
        this.f$1 = recentAppStatsListener;
    }

    public final void run() {
        this.f$0.lambda$onStart$0(this.f$1);
    }
}
