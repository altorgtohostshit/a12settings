package com.android.settingslib.drawer;

import java.util.Comparator;

public final /* synthetic */ class DashboardCategory$$ExternalSyntheticLambda0 implements Comparator {
    public final /* synthetic */ String f$0;

    public /* synthetic */ DashboardCategory$$ExternalSyntheticLambda0(String str) {
        this.f$0 = str;
    }

    public final int compare(Object obj, Object obj2) {
        return DashboardCategory.lambda$sortTiles$0(this.f$0, (Tile) obj, (Tile) obj2);
    }
}
