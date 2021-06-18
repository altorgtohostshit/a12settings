package com.android.settings.media;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.session.MediaController;
import android.media.session.MediaSessionManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.android.settings.bluetooth.Utils;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.bluetooth.BluetoothCallback;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.media.LocalMediaManager;
import com.android.settingslib.media.MediaDevice;
import com.android.settingslib.utils.ThreadUtils;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MediaOutputIndicatorWorker extends SliceBackgroundWorker implements BluetoothCallback, LocalMediaManager.DeviceCallback {
    private static final boolean DEBUG = Log.isLoggable("MediaOutputIndWorker", 3);
    private final Context mContext;
    private LocalBluetoothManager mLocalBluetoothManager;
    LocalMediaManager mLocalMediaManager;
    private final Collection<MediaDevice> mMediaDevices = new CopyOnWriteArrayList();
    private String mPackageName;
    private final DevicesChangedBroadcastReceiver mReceiver = new DevicesChangedBroadcastReceiver();

    public MediaOutputIndicatorWorker(Context context, Uri uri) {
        super(context, uri);
        this.mContext = context;
    }

    /* access modifiers changed from: protected */
    public void onSlicePinned() {
        this.mMediaDevices.clear();
        LocalBluetoothManager localBtManager = Utils.getLocalBtManager(getContext());
        this.mLocalBluetoothManager = localBtManager;
        if (localBtManager == null) {
            Log.e("MediaOutputIndWorker", "Bluetooth is not supported on this device");
            return;
        }
        this.mContext.registerReceiver(this.mReceiver, new IntentFilter("android.media.STREAM_DEVICES_CHANGED_ACTION"));
        this.mLocalBluetoothManager.getEventManager().registerCallback(this);
        ThreadUtils.postOnBackgroundThread((Runnable) new MediaOutputIndicatorWorker$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onSlicePinned$0() {
        MediaController activeLocalMediaController = getActiveLocalMediaController();
        if (activeLocalMediaController == null) {
            this.mPackageName = null;
        } else {
            this.mPackageName = activeLocalMediaController.getPackageName();
        }
        LocalMediaManager localMediaManager = this.mLocalMediaManager;
        if (localMediaManager == null || !TextUtils.equals(this.mPackageName, localMediaManager.getPackageName())) {
            this.mLocalMediaManager = new LocalMediaManager(this.mContext, this.mPackageName, (Notification) null);
        }
        this.mLocalMediaManager.registerCallback(this);
        this.mLocalMediaManager.startScan();
    }

    /* access modifiers changed from: protected */
    public void onSliceUnpinned() {
        LocalMediaManager localMediaManager = this.mLocalMediaManager;
        if (localMediaManager != null) {
            localMediaManager.unregisterCallback(this);
            this.mLocalMediaManager.stopScan();
        }
        LocalBluetoothManager localBluetoothManager = this.mLocalBluetoothManager;
        if (localBluetoothManager == null) {
            Log.e("MediaOutputIndWorker", "Bluetooth is not supported on this device");
            return;
        }
        localBluetoothManager.getEventManager().unregisterCallback(this);
        this.mContext.unregisterReceiver(this.mReceiver);
    }

    public void close() {
        this.mLocalBluetoothManager = null;
        this.mLocalMediaManager = null;
    }

    public void onAudioModeChanged() {
        notifySliceChange();
    }

    /* access modifiers changed from: package-private */
    public MediaController getActiveLocalMediaController() {
        return MediaOutputUtils.getActiveLocalMediaController((MediaSessionManager) this.mContext.getSystemService(MediaSessionManager.class));
    }

    public void onDeviceListUpdate(List<MediaDevice> list) {
        buildMediaDevices(list);
        notifySliceChange();
    }

    private void buildMediaDevices(List<MediaDevice> list) {
        this.mMediaDevices.clear();
        this.mMediaDevices.addAll(list);
    }

    public void onSelectedDeviceStateChanged(MediaDevice mediaDevice, int i) {
        notifySliceChange();
    }

    public void onDeviceAttributesChanged() {
        notifySliceChange();
    }

    /* access modifiers changed from: package-private */
    public Collection<MediaDevice> getMediaDevices() {
        return this.mMediaDevices;
    }

    /* access modifiers changed from: package-private */
    public MediaDevice getCurrentConnectedMediaDevice() {
        return this.mLocalMediaManager.getCurrentConnectedDevice();
    }

    /* access modifiers changed from: package-private */
    public String getPackageName() {
        return this.mPackageName;
    }

    private class DevicesChangedBroadcastReceiver extends BroadcastReceiver {
        private DevicesChangedBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals("android.media.STREAM_DEVICES_CHANGED_ACTION", intent.getAction())) {
                MediaOutputIndicatorWorker.this.notifySliceChange();
            }
        }
    }
}
