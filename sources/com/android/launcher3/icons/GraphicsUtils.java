package com.android.launcher3.icons;

import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.RegionIterator;

public class GraphicsUtils {
    public static Runnable sOnNewBitmapRunnable = GraphicsUtils$$ExternalSyntheticLambda0.INSTANCE;

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$static$0() {
    }

    public static int getArea(Region region) {
        RegionIterator regionIterator = new RegionIterator(region);
        Rect rect = new Rect();
        int i = 0;
        while (regionIterator.next(rect)) {
            i += rect.width() * rect.height();
        }
        return i;
    }
}
