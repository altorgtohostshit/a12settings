package com.android.launcher3.icons;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.UserHandle;

public class BitmapInfo {
    public static final Bitmap LOW_RES_ICON;
    public static final BitmapInfo LOW_RES_INFO;
    public final int color;
    public final Bitmap icon;

    static {
        Bitmap createBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ALPHA_8);
        LOW_RES_ICON = createBitmap;
        LOW_RES_INFO = fromBitmap(createBitmap);
    }

    public BitmapInfo(Bitmap bitmap, int i) {
        this.icon = bitmap;
        this.color = i;
    }

    public static BitmapInfo fromBitmap(Bitmap bitmap) {
        return m9of(bitmap, 0);
    }

    /* renamed from: of */
    public static BitmapInfo m9of(Bitmap bitmap, int i) {
        return new BitmapInfo(bitmap, i);
    }

    public interface Extender {
        void drawForPersistence(Canvas canvas) {
        }

        BitmapInfo getExtendedInfo(Bitmap bitmap, int i, BaseIconFactory baseIconFactory, float f, UserHandle userHandle) {
            return BitmapInfo.m9of(bitmap, i);
        }
    }
}
