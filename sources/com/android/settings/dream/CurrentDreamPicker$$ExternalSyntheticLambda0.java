package com.android.settings.dream;

import com.android.settingslib.dream.DreamBackend;
import java.util.Map;
import java.util.function.Consumer;

public final /* synthetic */ class CurrentDreamPicker$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ Map f$0;

    public /* synthetic */ CurrentDreamPicker$$ExternalSyntheticLambda0(Map map) {
        this.f$0 = map;
    }

    public final void accept(Object obj) {
        this.f$0.put(((DreamBackend.DreamInfo) obj).componentName.flattenToString(), ((DreamBackend.DreamInfo) obj).componentName);
    }
}
