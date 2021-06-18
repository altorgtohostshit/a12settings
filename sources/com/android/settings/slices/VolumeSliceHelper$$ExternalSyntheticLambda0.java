package com.android.settings.slices;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import java.util.function.BiConsumer;

public final /* synthetic */ class VolumeSliceHelper$$ExternalSyntheticLambda0 implements BiConsumer {
    public final /* synthetic */ Context f$0;

    public /* synthetic */ VolumeSliceHelper$$ExternalSyntheticLambda0(Context context) {
        this.f$0 = context;
    }

    public final void accept(Object obj, Object obj2) {
        this.f$0.getContentResolver().notifyChange((Uri) obj, (ContentObserver) null);
    }
}
