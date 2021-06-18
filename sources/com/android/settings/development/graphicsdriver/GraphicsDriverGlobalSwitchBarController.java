package com.android.settings.development.graphicsdriver;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import com.android.settings.development.graphicsdriver.GraphicsDriverContentObserver;
import com.android.settings.widget.SwitchWidgetController;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import com.android.settingslib.development.DevelopmentSettingsEnabler;

public class GraphicsDriverGlobalSwitchBarController implements SwitchWidgetController.OnSwitchChangeListener, GraphicsDriverContentObserver.OnGraphicsDriverContentChangedListener, LifecycleObserver, OnStart, OnStop {
    private final ContentResolver mContentResolver;
    private final Context mContext;
    GraphicsDriverContentObserver mGraphicsDriverContentObserver = new GraphicsDriverContentObserver(new Handler(Looper.getMainLooper()), this);
    SwitchWidgetController mSwitchWidgetController;

    GraphicsDriverGlobalSwitchBarController(Context context, SwitchWidgetController switchWidgetController) {
        this.mContext = context;
        ContentResolver contentResolver = context.getContentResolver();
        this.mContentResolver = contentResolver;
        this.mSwitchWidgetController = switchWidgetController;
        switchWidgetController.setEnabled(DevelopmentSettingsEnabler.isDevelopmentSettingsEnabled(context));
        this.mSwitchWidgetController.setChecked(Settings.Global.getInt(contentResolver, "updatable_driver_all_apps", 0) != 3);
        this.mSwitchWidgetController.setListener(this);
    }

    public void onStart() {
        this.mSwitchWidgetController.startListening();
        this.mGraphicsDriverContentObserver.register(this.mContentResolver);
    }

    public void onStop() {
        this.mSwitchWidgetController.stopListening();
        this.mGraphicsDriverContentObserver.unregister(this.mContentResolver);
    }

    public boolean onSwitchToggled(boolean z) {
        int i = 0;
        int i2 = Settings.Global.getInt(this.mContentResolver, "updatable_driver_all_apps", 0);
        if (z && (i2 == 0 || i2 == 1 || i2 == 2)) {
            return true;
        }
        if (!z && i2 == 3) {
            return true;
        }
        ContentResolver contentResolver = this.mContentResolver;
        if (!z) {
            i = 3;
        }
        Settings.Global.putInt(contentResolver, "updatable_driver_all_apps", i);
        return true;
    }

    public void onGraphicsDriverContentChanged() {
        SwitchWidgetController switchWidgetController = this.mSwitchWidgetController;
        boolean z = false;
        if (Settings.Global.getInt(this.mContentResolver, "updatable_driver_all_apps", 0) != 3) {
            z = true;
        }
        switchWidgetController.setChecked(z);
    }
}
