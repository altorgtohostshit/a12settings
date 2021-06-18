package com.google.android.settings.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.SystemProperties;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.android.settings.R;

public class MarlinColorImageView extends ImageView {
    public MarlinColorImageView(Context context) {
        super(context);
    }

    public MarlinColorImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setImageDrawable(Drawable drawable) {
        Drawable mutate = drawable.mutate();
        mutate.applyTheme(getDeviceColorTheme());
        super.setImageDrawable(mutate);
    }

    private Resources.Theme getDeviceColorTheme() {
        Resources.Theme newTheme = getResources().newTheme();
        String str = SystemProperties.get("ro.boot.hardware.color");
        if ("BLU00".equals(str)) {
            newTheme.applyStyle(2131951951, true);
        } else if ("SLV00".equals(str)) {
            newTheme.applyStyle(2131951953, true);
        } else if ("GRA00".equals(str)) {
            newTheme.applyStyle(2131951952, true);
        } else {
            newTheme.applyStyle(R.style.MarlinColors, true);
        }
        return newTheme;
    }
}
