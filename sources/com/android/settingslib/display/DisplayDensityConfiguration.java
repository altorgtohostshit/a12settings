package com.android.settingslib.display;

import android.os.AsyncTask;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;
import android.view.WindowManagerGlobal;

public class DisplayDensityConfiguration {
    public static void clearForcedDisplayDensity(int i) {
        AsyncTask.execute(new DisplayDensityConfiguration$$ExternalSyntheticLambda0(i, UserHandle.myUserId()));
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$clearForcedDisplayDensity$0(int i, int i2) {
        try {
            WindowManagerGlobal.getWindowManagerService().clearForcedDisplayDensityForUser(i, i2);
        } catch (RemoteException unused) {
            Log.w("DisplayDensityConfig", "Unable to clear forced display density setting");
        }
    }

    public static void setForcedDisplayDensity(int i, int i2) {
        AsyncTask.execute(new DisplayDensityConfiguration$$ExternalSyntheticLambda1(i, i2, UserHandle.myUserId()));
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$setForcedDisplayDensity$1(int i, int i2, int i3) {
        try {
            WindowManagerGlobal.getWindowManagerService().setForcedDisplayDensityForUser(i, i2, i3);
        } catch (RemoteException unused) {
            Log.w("DisplayDensityConfig", "Unable to save forced display density setting");
        }
    }
}
