package com.android.settingslib.core.lifecycle;

import android.app.Activity;
import android.provider.Settings;
import android.util.EventLog;
import android.view.Window;
import android.view.WindowManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

public class HideNonSystemOverlayMixin implements LifecycleObserver {
    private final Activity mActivity;

    public HideNonSystemOverlayMixin(Activity activity) {
        this.mActivity = activity;
    }

    /* access modifiers changed from: package-private */
    public boolean isEnabled() {
        return Settings.Secure.getInt(this.mActivity.getContentResolver(), "secure_overlay_settings", 0) == 0;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        if (this.mActivity != null && isEnabled()) {
            this.mActivity.getWindow().addSystemFlags(524288);
            EventLog.writeEvent(1397638484, new Object[]{"120484087", -1, ""});
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        if (this.mActivity != null && isEnabled()) {
            Window window = this.mActivity.getWindow();
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.privateFlags &= -524289;
            window.setAttributes(attributes);
        }
    }
}
