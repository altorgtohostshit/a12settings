package com.google.android.settings.widget;

import android.content.Context;
import android.os.SystemProperties;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import com.android.settings.R;
import com.google.android.setupdesign.view.IllustrationVideoView;

public class MarlinColorFingerprintLocationAnimationVideoView extends IllustrationVideoView {
    public MarlinColorFingerprintLocationAnimationVideoView(Context context, AttributeSet attributeSet) {
        super(getDeviceColorTheme(context), attributeSet);
    }

    private static Context getDeviceColorTheme(Context context) {
        int i;
        String str = SystemProperties.get("ro.boot.hardware.color");
        if ("BLU00".equals(str)) {
            i = 2131951951;
        } else if ("SLV00".equals(str)) {
            i = 2131951953;
        } else {
            i = "GRA00".equals(str) ? 2131951952 : R.style.MarlinColors;
        }
        return new ContextThemeWrapper(context, i);
    }
}
