package com.android.settings.dashboard;

import com.android.settingslib.drawer.DashboardCategory;
import java.util.Map;
import java.util.function.Consumer;

public final /* synthetic */ class CategoryManager$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ Map f$0;

    public /* synthetic */ CategoryManager$$ExternalSyntheticLambda0(Map map) {
        this.f$0 = map;
    }

    public final void accept(Object obj) {
        CategoryManager.lambda$getTileByComponentMap$0(this.f$0, (DashboardCategory) obj);
    }
}
