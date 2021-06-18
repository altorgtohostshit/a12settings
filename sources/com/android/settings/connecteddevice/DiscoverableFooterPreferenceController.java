package com.android.settings.connecteddevice;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.BidiFormatter;
import android.text.TextUtils;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.bluetooth.AlwaysDiscoverable;
import com.android.settings.bluetooth.Utils;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import com.android.settingslib.widget.FooterPreference;

public class DiscoverableFooterPreferenceController extends BasePreferenceController implements LifecycleObserver, OnStart, OnStop {
    private static final String KEY = "discoverable_footer_preference";
    private AlwaysDiscoverable mAlwaysDiscoverable;
    private BluetoothAdapter mBluetoothAdapter;
    BroadcastReceiver mBluetoothChangedReceiver;
    LocalBluetoothManager mLocalManager;
    private FooterPreference mPreference;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public DiscoverableFooterPreferenceController(Context context, String str) {
        super(context, str);
        LocalBluetoothManager localBtManager = Utils.getLocalBtManager(context);
        this.mLocalManager = localBtManager;
        if (localBtManager != null) {
            this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            this.mAlwaysDiscoverable = new AlwaysDiscoverable(context);
            initReceiver();
        }
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = (FooterPreference) preferenceScreen.findPreference(getPreferenceKey());
    }

    public int getAvailabilityStatus() {
        return this.mContext.getPackageManager().hasSystemFeature("android.hardware.bluetooth") ? 1 : 3;
    }

    public void onStart() {
        if (this.mLocalManager != null) {
            this.mContext.registerReceiver(this.mBluetoothChangedReceiver, new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED"));
            this.mAlwaysDiscoverable.start();
            updateFooterPreferenceTitle(this.mBluetoothAdapter.getState());
        }
    }

    public void onStop() {
        if (this.mLocalManager != null) {
            this.mContext.unregisterReceiver(this.mBluetoothChangedReceiver);
            this.mAlwaysDiscoverable.stop();
        }
    }

    /* access modifiers changed from: private */
    public void updateFooterPreferenceTitle(int i) {
        if (i == 12) {
            this.mPreference.setTitle(getPreferenceTitle());
        } else {
            this.mPreference.setTitle((int) R.string.bluetooth_off_footer);
        }
    }

    private CharSequence getPreferenceTitle() {
        String name = this.mBluetoothAdapter.getName();
        if (TextUtils.isEmpty(name)) {
            return null;
        }
        return TextUtils.expandTemplate(this.mContext.getText(R.string.bluetooth_device_name_summary), new CharSequence[]{BidiFormatter.getInstance().unicodeWrap(name)});
    }

    private void initReceiver() {
        this.mBluetoothChangedReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("android.bluetooth.adapter.action.STATE_CHANGED")) {
                    DiscoverableFooterPreferenceController.this.updateFooterPreferenceTitle(intent.getIntExtra("android.bluetooth.adapter.extra.STATE", Integer.MIN_VALUE));
                }
            }
        };
    }
}
