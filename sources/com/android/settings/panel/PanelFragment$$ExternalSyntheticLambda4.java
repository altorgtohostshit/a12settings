package com.android.settings.panel;

import android.net.Uri;
import androidx.lifecycle.Observer;
import androidx.slice.Slice;

public final /* synthetic */ class PanelFragment$$ExternalSyntheticLambda4 implements Observer {
    public final /* synthetic */ PanelFragment f$0;
    public final /* synthetic */ Uri f$1;

    public /* synthetic */ PanelFragment$$ExternalSyntheticLambda4(PanelFragment panelFragment, Uri uri) {
        this.f$0 = panelFragment;
        this.f$1 = uri;
    }

    public final void onChanged(Object obj) {
        this.f$0.lambda$loadAllSlices$3(this.f$1, (Slice) obj);
    }
}
