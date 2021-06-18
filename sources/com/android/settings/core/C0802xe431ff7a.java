package com.android.settings.core;

import android.content.ComponentName;
import com.android.settings.core.SettingsBaseActivity;
import com.android.settingslib.drawer.Tile;
import java.util.Set;
import java.util.function.BiConsumer;

/* renamed from: com.android.settings.core.SettingsBaseActivity$CategoriesUpdateTask$$ExternalSyntheticLambda0 */
public final /* synthetic */ class C0802xe431ff7a implements BiConsumer {
    public final /* synthetic */ SettingsBaseActivity.CategoriesUpdateTask f$0;
    public final /* synthetic */ Set f$1;

    public /* synthetic */ C0802xe431ff7a(SettingsBaseActivity.CategoriesUpdateTask categoriesUpdateTask, Set set) {
        this.f$0 = categoriesUpdateTask;
        this.f$1 = set;
    }

    public final void accept(Object obj, Object obj2) {
        this.f$0.lambda$getChangedCategories$0(this.f$1, (ComponentName) obj, (Tile) obj2);
    }
}
