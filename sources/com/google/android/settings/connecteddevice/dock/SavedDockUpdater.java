package com.google.android.settings.connecteddevice.dock;

import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.connecteddevice.DevicePreferenceCallback;
import com.android.settings.connecteddevice.dock.DockUpdater;
import com.android.settings.widget.SingleTargetGearPreference;
import com.google.android.settings.connecteddevice.dock.DockAsyncQueryHandler;
import com.google.common.base.Preconditions;
import java.util.List;
import java.util.Map;

public class SavedDockUpdater implements DockUpdater, DockAsyncQueryHandler.OnQueryListener {
    private final DockAsyncQueryHandler mAsyncQueryHandler;
    private String mConnectedDockId = null;
    private final DockObserver mConnectedDockObserver;
    private final Context mContext;
    private final DevicePreferenceCallback mDevicePreferenceCallback;
    @VisibleForTesting
    boolean mIsObserverRegistered;
    private Context mPreferenceContext = null;
    @VisibleForTesting
    final Map<String, SingleTargetGearPreference> mPreferenceMap;
    private Map<String, String> mSavedDevices = null;
    private final DockObserver mSavedDockObserver;

    public SavedDockUpdater(Context context, DevicePreferenceCallback devicePreferenceCallback) {
        this.mContext = context;
        this.mDevicePreferenceCallback = devicePreferenceCallback;
        this.mPreferenceMap = new ArrayMap();
        Handler handler = new Handler(Looper.getMainLooper());
        this.mConnectedDockObserver = new DockObserver(handler, 1, DockContract.DOCK_PROVIDER_CONNECTED_URI);
        this.mSavedDockObserver = new DockObserver(handler, 2, DockContract.DOCK_PROVIDER_SAVED_URI);
        if (isRunningOnMainThread()) {
            DockAsyncQueryHandler dockAsyncQueryHandler = new DockAsyncQueryHandler(context.getContentResolver());
            this.mAsyncQueryHandler = dockAsyncQueryHandler;
            dockAsyncQueryHandler.setOnQueryListener(this);
            return;
        }
        this.mAsyncQueryHandler = null;
    }

    public void registerCallback() {
        ContentResolver contentResolver = this.mContext.getContentResolver();
        Uri uri = DockContract.DOCK_PROVIDER_SAVED_URI;
        ContentProviderClient acquireContentProviderClient = contentResolver.acquireContentProviderClient(uri);
        if (acquireContentProviderClient != null) {
            acquireContentProviderClient.release();
            this.mContext.getContentResolver().registerContentObserver(DockContract.DOCK_PROVIDER_CONNECTED_URI, false, this.mConnectedDockObserver);
            this.mContext.getContentResolver().registerContentObserver(uri, false, this.mSavedDockObserver);
            this.mIsObserverRegistered = true;
            forceUpdate();
        }
    }

    public void unregisterCallback() {
        if (this.mIsObserverRegistered) {
            this.mContext.getContentResolver().unregisterContentObserver(this.mConnectedDockObserver);
            this.mContext.getContentResolver().unregisterContentObserver(this.mSavedDockObserver);
            this.mIsObserverRegistered = false;
        }
    }

    public void forceUpdate() {
        startQuery(1, DockContract.DOCK_PROVIDER_CONNECTED_URI);
        startQuery(2, DockContract.DOCK_PROVIDER_SAVED_URI);
    }

    public void setPreferenceContext(Context context) {
        this.mPreferenceContext = context;
    }

    public void onQueryComplete(int i, List<DockDevice> list) {
        if (list == null) {
            return;
        }
        if (i == 2) {
            updateSavedDevicesList(list);
        } else if (i == 1) {
            updateConnectedDevice(list);
        }
    }

    private SingleTargetGearPreference initPreference(String str, String str2) {
        Preconditions.checkNotNull(this.mPreferenceContext, "Preference context cannot be null");
        SingleTargetGearPreference singleTargetGearPreference = new SingleTargetGearPreference(this.mPreferenceContext, (AttributeSet) null);
        singleTargetGearPreference.setIcon(DockUtils.buildRainbowIcon(this.mPreferenceContext, str));
        singleTargetGearPreference.setTitle((CharSequence) str2);
        if (!TextUtils.isEmpty(str)) {
            singleTargetGearPreference.setIntent(DockContract.buildDockSettingIntent(str));
            singleTargetGearPreference.setSelectable(true);
        }
        return singleTargetGearPreference;
    }

    private boolean isRunningOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    /* access modifiers changed from: private */
    public void startQuery(int i, Uri uri) {
        Cursor query;
        if (isRunningOnMainThread()) {
            this.mAsyncQueryHandler.startQuery(i, this.mContext, uri, DockContract.DOCK_PROJECTION, (String) null, (String[]) null, (String) null);
            return;
        }
        try {
            query = this.mContext.getApplicationContext().getContentResolver().query(uri, DockContract.DOCK_PROJECTION, (String) null, (String[]) null, (String) null);
            onQueryComplete(i, DockAsyncQueryHandler.parseCursorToDockDevice(query));
            if (query != null) {
                query.close();
                return;
            }
            return;
        } catch (Exception e) {
            Log.w("SavedDockUpdater", "Query dockProvider fail", e);
            return;
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        throw th;
    }

    private void updateConnectedDevice(List<DockDevice> list) {
        if (list.isEmpty()) {
            this.mConnectedDockId = null;
            updateDevices();
            return;
        }
        String id = list.get(0).getId();
        this.mConnectedDockId = id;
        if (this.mPreferenceMap.containsKey(id)) {
            this.mDevicePreferenceCallback.onDeviceRemoved(this.mPreferenceMap.get(this.mConnectedDockId));
            this.mPreferenceMap.remove(this.mConnectedDockId);
        }
    }

    private void updateSavedDevicesList(List<DockDevice> list) {
        if (this.mSavedDevices == null) {
            this.mSavedDevices = new ArrayMap();
        }
        this.mSavedDevices.clear();
        for (DockDevice next : list) {
            String name = next.getName();
            if (!TextUtils.isEmpty(name)) {
                this.mSavedDevices.put(next.getId(), name);
            }
        }
        updateDevices();
    }

    private void updateDevices() {
        Map<String, String> map = this.mSavedDevices;
        if (map != null) {
            for (String next : map.keySet()) {
                if (!TextUtils.equals(next, this.mConnectedDockId)) {
                    String str = this.mSavedDevices.get(next);
                    if (this.mPreferenceMap.containsKey(next)) {
                        this.mPreferenceMap.get(next).setTitle((CharSequence) str);
                    } else {
                        this.mPreferenceMap.put(next, initPreference(next, str));
                        this.mDevicePreferenceCallback.onDeviceAdded(this.mPreferenceMap.get(next));
                    }
                }
            }
            this.mPreferenceMap.keySet().removeIf(new SavedDockUpdater$$ExternalSyntheticLambda0(this));
        }
    }

    /* access modifiers changed from: private */
    public boolean hasDeviceBeenRemoved(String str) {
        if (this.mSavedDevices.containsKey(str)) {
            return false;
        }
        this.mDevicePreferenceCallback.onDeviceRemoved(this.mPreferenceMap.get(str));
        return true;
    }

    private class DockObserver extends ContentObserver {
        private final int mToken;
        private final Uri mUri;

        DockObserver(Handler handler, int i, Uri uri) {
            super(handler);
            this.mToken = i;
            this.mUri = uri;
        }

        public void onChange(boolean z) {
            super.onChange(z);
            SavedDockUpdater.this.startQuery(this.mToken, this.mUri);
        }
    }
}
