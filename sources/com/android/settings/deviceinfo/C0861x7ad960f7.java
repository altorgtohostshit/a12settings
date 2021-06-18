package com.android.settings.deviceinfo;

import android.util.SparseArray;
import com.android.settings.deviceinfo.storage.UserIconLoader;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.function.Consumer;

/* renamed from: com.android.settings.deviceinfo.StorageDashboardFragment$IconLoaderCallbacks$$ExternalSyntheticLambda1 */
public final /* synthetic */ class C0861x7ad960f7 implements Consumer {
    public final /* synthetic */ SparseArray f$0;

    public /* synthetic */ C0861x7ad960f7(SparseArray sparseArray) {
        this.f$0 = sparseArray;
    }

    public final void accept(Object obj) {
        ((UserIconLoader.UserIconHandler) ((AbstractPreferenceController) obj)).handleUserIcons(this.f$0);
    }
}
