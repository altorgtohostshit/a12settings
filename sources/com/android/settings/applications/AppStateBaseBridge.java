package com.android.settings.applications;

import android.content.pm.ApplicationInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.android.settingslib.applications.ApplicationsState;
import java.util.ArrayList;

public abstract class AppStateBaseBridge implements ApplicationsState.Callbacks {
    protected final ApplicationsState.Session mAppSession;
    protected final ApplicationsState mAppState;
    protected final Callback mCallback;
    protected final BackgroundHandler mHandler;
    protected final MainHandler mMainHandler;

    public interface Callback {
        void onExtraInfoUpdated();
    }

    /* access modifiers changed from: protected */
    public abstract void loadAllExtraInfo();

    public void onAllSizesComputed() {
    }

    public void onLauncherInfoChanged() {
    }

    public void onPackageIconChanged() {
    }

    public void onPackageSizeChanged(String str) {
    }

    public void onRebuildComplete(ArrayList<ApplicationsState.AppEntry> arrayList) {
    }

    public void onRunningStateChanged(boolean z) {
    }

    /* access modifiers changed from: protected */
    public abstract void updateExtraInfo(ApplicationsState.AppEntry appEntry, String str, int i);

    public AppStateBaseBridge(ApplicationsState applicationsState, Callback callback) {
        Looper looper;
        this.mAppState = applicationsState;
        this.mAppSession = applicationsState != null ? applicationsState.newSession(this) : null;
        this.mCallback = callback;
        if (applicationsState != null) {
            looper = applicationsState.getBackgroundLooper();
        } else {
            looper = Looper.getMainLooper();
        }
        this.mHandler = new BackgroundHandler(looper);
        this.mMainHandler = new MainHandler(Looper.getMainLooper());
    }

    public void resume() {
        this.mHandler.sendEmptyMessage(1);
        this.mAppSession.onResume();
    }

    public void pause() {
        this.mAppSession.onPause();
    }

    public void release() {
        this.mAppSession.onDestroy();
    }

    public void forceUpdate(String str, int i) {
        this.mHandler.obtainMessage(2, i, 0, str).sendToTarget();
    }

    public void onPackageListChanged() {
        this.mHandler.sendEmptyMessage(1);
    }

    public void onLoadEntriesCompleted() {
        this.mHandler.sendEmptyMessage(1);
    }

    private class MainHandler extends Handler {
        public MainHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            if (message.what == 1) {
                AppStateBaseBridge.this.mCallback.onExtraInfoUpdated();
            }
        }
    }

    private class BackgroundHandler extends Handler {
        public BackgroundHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                AppStateBaseBridge.this.loadAllExtraInfo();
                AppStateBaseBridge.this.mMainHandler.sendEmptyMessage(1);
            } else if (i == 2) {
                ArrayList<ApplicationsState.AppEntry> allApps = AppStateBaseBridge.this.mAppSession.getAllApps();
                int size = allApps.size();
                String str = (String) message.obj;
                int i2 = message.arg1;
                for (int i3 = 0; i3 < size; i3++) {
                    ApplicationsState.AppEntry appEntry = allApps.get(i3);
                    ApplicationInfo applicationInfo = appEntry.info;
                    if (applicationInfo.uid == i2 && str.equals(applicationInfo.packageName)) {
                        AppStateBaseBridge.this.updateExtraInfo(appEntry, str, i2);
                    }
                }
                AppStateBaseBridge.this.mMainHandler.sendEmptyMessage(1);
            }
        }
    }
}
