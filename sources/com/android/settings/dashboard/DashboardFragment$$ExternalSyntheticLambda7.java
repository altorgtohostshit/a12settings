package com.android.settings.dashboard;

import com.android.settingslib.core.AbstractPreferenceController;
import java.util.List;
import java.util.function.Consumer;

public final /* synthetic */ class DashboardFragment$$ExternalSyntheticLambda7 implements Consumer {
    public final /* synthetic */ DashboardFragment f$0;
    public final /* synthetic */ List f$1;

    public /* synthetic */ DashboardFragment$$ExternalSyntheticLambda7(DashboardFragment dashboardFragment, List list) {
        this.f$0 = dashboardFragment;
        this.f$1 = list;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$checkUiBlocker$2(this.f$1, (AbstractPreferenceController) obj);
    }
}
