package com.android.settings.media;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RoutingSessionInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.Utils;
import com.android.settingslib.media.LocalMediaManager;
import com.android.settingslib.media.MediaDevice;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MediaDeviceUpdateWorker extends SliceBackgroundWorker implements LocalMediaManager.DeviceCallback {
    private static final boolean DEBUG = Log.isLoggable("MediaDeviceUpdateWorker", 3);
    protected final Context mContext;
    private boolean mIsTouched;
    LocalMediaManager mLocalMediaManager;
    protected final Collection<MediaDevice> mMediaDevices = new CopyOnWriteArrayList();
    private final String mPackageName;
    private final DevicesChangedBroadcastReceiver mReceiver;

    public MediaDeviceUpdateWorker(Context context, Uri uri) {
        super(context, uri);
        this.mContext = context;
        this.mPackageName = uri.getQueryParameter("media_package_name");
        this.mReceiver = new DevicesChangedBroadcastReceiver();
    }

    /* access modifiers changed from: protected */
    public void onSlicePinned() {
        this.mMediaDevices.clear();
        this.mIsTouched = false;
        LocalMediaManager localMediaManager = this.mLocalMediaManager;
        if (localMediaManager == null || !TextUtils.equals(this.mPackageName, localMediaManager.getPackageName())) {
            this.mLocalMediaManager = new LocalMediaManager(this.mContext, this.mPackageName, (Notification) null);
        }
        this.mLocalMediaManager.registerCallback(this);
        this.mContext.registerReceiver(this.mReceiver, new IntentFilter("android.media.STREAM_DEVICES_CHANGED_ACTION"));
        this.mLocalMediaManager.startScan();
    }

    /* access modifiers changed from: protected */
    public void onSliceUnpinned() {
        this.mLocalMediaManager.unregisterCallback(this);
        this.mContext.unregisterReceiver(this.mReceiver);
        this.mLocalMediaManager.stopScan();
    }

    public void close() {
        this.mLocalMediaManager = null;
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

    public void onRequestFailed(int i) {
        notifySliceChange();
    }

    /* access modifiers changed from: package-private */
    public void adjustSessionVolume(String str, int i) {
        this.mLocalMediaManager.adjustSessionVolume(str, i);
    }

    /* access modifiers changed from: package-private */
    public List<RoutingSessionInfo> getActiveRemoteMediaDevice() {
        ArrayList arrayList = new ArrayList();
        for (RoutingSessionInfo next : this.mLocalMediaManager.getActiveMediaSession()) {
            if (!next.isSystemSession()) {
                if (DEBUG) {
                    Log.d("MediaDeviceUpdateWorker", "getActiveRemoteMediaDevice() info : " + next.toString() + ", package name : " + next.getClientPackageName());
                }
                arrayList.add(next);
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: package-private */
    public boolean shouldDisableMediaOutput(String str) {
        return this.mLocalMediaManager.shouldDisableMediaOutput(str);
    }

    private class DevicesChangedBroadcastReceiver extends BroadcastReceiver {
        private DevicesChangedBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals("android.media.STREAM_DEVICES_CHANGED_ACTION", intent.getAction()) && Utils.isAudioModeOngoingCall(MediaDeviceUpdateWorker.this.mContext)) {
                MediaDeviceUpdateWorker.this.notifySliceChange();
            }
        }
    }
}
