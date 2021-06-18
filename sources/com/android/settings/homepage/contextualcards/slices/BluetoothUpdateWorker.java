package com.android.settings.homepage.contextualcards.slices;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.bluetooth.BluetoothCallback;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.LocalBluetoothManager;

public class BluetoothUpdateWorker extends SliceBackgroundWorker implements BluetoothCallback {
    /* access modifiers changed from: private */
    public static LocalBluetoothManager sLocalBluetoothManager;
    private LoadBtManagerHandler mLoadBtManagerHandler;

    public void close() {
    }

    public BluetoothUpdateWorker(Context context, Uri uri) {
        super(context, uri);
        LoadBtManagerHandler access$000 = LoadBtManagerHandler.getInstance(context);
        this.mLoadBtManagerHandler = access$000;
        if (sLocalBluetoothManager == null) {
            access$000.startLoadingBtManager(this);
        }
    }

    public static void initLocalBtManager(Context context) {
        if (sLocalBluetoothManager == null) {
            LoadBtManagerHandler.getInstance(context).startLoadingBtManager();
        }
    }

    static LocalBluetoothManager getLocalBtManager() {
        return sLocalBluetoothManager;
    }

    /* access modifiers changed from: protected */
    public void onSlicePinned() {
        LocalBluetoothManager access$300 = this.mLoadBtManagerHandler.getLocalBtManager();
        if (access$300 != null) {
            access$300.getEventManager().registerCallback(this);
        }
    }

    /* access modifiers changed from: protected */
    public void onSliceUnpinned() {
        LocalBluetoothManager access$300 = this.mLoadBtManagerHandler.getLocalBtManager();
        if (access$300 != null) {
            access$300.getEventManager().unregisterCallback(this);
        }
    }

    public void onAclConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        notifySliceChange();
    }

    public void onActiveDeviceChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        notifySliceChange();
    }

    public void onBluetoothStateChanged(int i) {
        notifySliceChange();
    }

    public void onConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        notifySliceChange();
    }

    public void onProfileConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i, int i2) {
        notifySliceChange();
    }

    private static class LoadBtManagerHandler extends Handler {
        private static LoadBtManagerHandler sHandler;
        private final Context mContext;
        private final Runnable mLoadBtManagerTask = new C0969x7052c6b4(this);
        private BluetoothUpdateWorker mWorker;

        /* access modifiers changed from: private */
        public static LoadBtManagerHandler getInstance(Context context) {
            if (sHandler == null) {
                HandlerThread handlerThread = new HandlerThread("BluetoothUpdateWorker", 10);
                handlerThread.start();
                sHandler = new LoadBtManagerHandler(context, handlerThread.getLooper());
            }
            return sHandler;
        }

        private LoadBtManagerHandler(Context context, Looper looper) {
            super(looper);
            this.mContext = context;
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0() {
            Log.d("BluetoothUpdateWorker", "LoadBtManagerHandler: start loading...");
            long currentTimeMillis = System.currentTimeMillis();
            LocalBluetoothManager unused = BluetoothUpdateWorker.sLocalBluetoothManager = getLocalBtManager();
            Log.d("BluetoothUpdateWorker", "LoadBtManagerHandler took " + (System.currentTimeMillis() - currentTimeMillis) + " ms");
        }

        /* access modifiers changed from: private */
        public LocalBluetoothManager getLocalBtManager() {
            if (BluetoothUpdateWorker.sLocalBluetoothManager != null) {
                return BluetoothUpdateWorker.sLocalBluetoothManager;
            }
            return LocalBluetoothManager.getInstance(this.mContext, new C0968x7052c6b3(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$getLocalBtManager$1(Context context, LocalBluetoothManager localBluetoothManager) {
            BluetoothUpdateWorker bluetoothUpdateWorker = this.mWorker;
            if (bluetoothUpdateWorker != null) {
                bluetoothUpdateWorker.notifySliceChange();
            }
        }

        /* access modifiers changed from: private */
        public void startLoadingBtManager() {
            if (!hasCallbacks(this.mLoadBtManagerTask)) {
                post(this.mLoadBtManagerTask);
            }
        }

        /* access modifiers changed from: private */
        public void startLoadingBtManager(BluetoothUpdateWorker bluetoothUpdateWorker) {
            this.mWorker = bluetoothUpdateWorker;
            startLoadingBtManager();
        }
    }
}
