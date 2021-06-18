package com.google.android.settings.biometrics.face.anim;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;

public class Cutout {
    public static Bitmap createCutoutBitmap(Context context, int i, int i2, boolean z) {
        Bitmap createBitmap = Bitmap.createBitmap(i, i, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        float f = (float) i;
        RectF rectF = new RectF(0.0f, 0.0f, f, f);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        if ((context.getResources().getConfiguration().uiMode & 48) != 32 || z) {
            paint.setColor(-1);
        } else {
            paint.setColor(-16777216);
        }
        canvas.drawRect(rectF, paint);
        paint.setColor(0);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        float f2 = (float) (i / 2);
        canvas.drawCircle(f2, f2, (float) i2, paint);
        return createBitmap;
    }
}
