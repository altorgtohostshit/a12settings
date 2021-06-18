package com.android.settings.applications.specialaccess.zenaccess;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;

public class ZenAccessSettingObserverMixin extends ContentObserver implements LifecycleObserver, OnStart, OnStop {
    private final Context mContext;
    private final Listener mListener;

    public interface Listener {
        void onZenAccessPolicyChanged();
    }

    public ZenAccessSettingObserverMixin(Context context, Listener listener) {
        super(new Handler(Looper.getMainLooper()));
        this.mContext = context;
        this.mListener = listener;
    }

    public void onChange(boolean z, Uri uri) {
        Listener listener = this.mListener;
        if (listener != null) {
            listener.onZenAccessPolicyChanged();
        }
    }

    public void onStart() {
        this.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("enabled_notification_listeners"), false, this);
    }

    public void onStop() {
        this.mContext.getContentResolver().unregisterContentObserver(this);
    }
}
