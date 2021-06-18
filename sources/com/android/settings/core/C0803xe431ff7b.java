package com.android.settings.core;

import android.content.ComponentName;
import com.android.settings.core.SettingsBaseActivity;
import java.util.Set;
import java.util.function.Consumer;

/* renamed from: com.android.settings.core.SettingsBaseActivity$CategoriesUpdateTask$$ExternalSyntheticLambda1 */
public final /* synthetic */ class C0803xe431ff7b implements Consumer {
    public final /* synthetic */ SettingsBaseActivity.CategoriesUpdateTask f$0;
    public final /* synthetic */ Set f$1;

    public /* synthetic */ C0803xe431ff7b(SettingsBaseActivity.CategoriesUpdateTask categoriesUpdateTask, Set set) {
        this.f$0 = categoriesUpdateTask;
        this.f$1 = set;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$getChangedCategories$1(this.f$1, (ComponentName) obj);
    }
}
