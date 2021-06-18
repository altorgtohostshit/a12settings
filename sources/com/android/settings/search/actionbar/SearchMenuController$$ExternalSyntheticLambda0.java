package com.android.settings.search.actionbar;

import android.app.Activity;
import android.view.MenuItem;

public final /* synthetic */ class SearchMenuController$$ExternalSyntheticLambda0 implements MenuItem.OnMenuItemClickListener {
    public final /* synthetic */ SearchMenuController f$0;
    public final /* synthetic */ Activity f$1;

    public /* synthetic */ SearchMenuController$$ExternalSyntheticLambda0(SearchMenuController searchMenuController, Activity activity) {
        this.f$0 = searchMenuController;
        this.f$1 = activity;
    }

    public final boolean onMenuItemClick(MenuItem menuItem) {
        return this.f$0.lambda$onCreateOptionsMenu$0(this.f$1, menuItem);
    }
}
