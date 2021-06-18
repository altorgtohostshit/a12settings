package com.google.android.settings.gestures.columbus;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.FrameLayout;

public class ColumbusGestureIndicatorView extends FrameLayout {
    private final Handler mHandler = new Handler(Looper.myLooper());

    public ColumbusGestureIndicatorView(Context context) {
        super(context);
    }
}
