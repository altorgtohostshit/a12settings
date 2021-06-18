package com.android.settings.bluetooth;

import android.content.Context;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;

public abstract class BluetoothDetailsController extends AbstractPreferenceController implements PreferenceControllerMixin, CachedBluetoothDevice.Callback, LifecycleObserver, OnPause, OnResume {
    protected final CachedBluetoothDevice mCachedDevice;
    protected final Context mContext;
    protected final PreferenceFragmentCompat mFragment;

    /* access modifiers changed from: protected */
    public abstract void init(PreferenceScreen preferenceScreen);

    public boolean isAvailable() {
        return true;
    }

    /* access modifiers changed from: protected */
    public abstract void refresh();

    public BluetoothDetailsController(Context context, PreferenceFragmentCompat preferenceFragmentCompat, CachedBluetoothDevice cachedBluetoothDevice, Lifecycle lifecycle) {
        super(context);
        this.mContext = context;
        this.mFragment = preferenceFragmentCompat;
        this.mCachedDevice = cachedBluetoothDevice;
        lifecycle.addObserver(this);
    }

    public void onPause() {
        this.mCachedDevice.unregisterCallback(this);
    }

    public void onResume() {
        this.mCachedDevice.registerCallback(this);
        refresh();
    }

    public void onDeviceAttributesChanged() {
        refresh();
    }

    public final void displayPreference(PreferenceScreen preferenceScreen) {
        init(preferenceScreen);
        super.displayPreference(preferenceScreen);
    }
}
