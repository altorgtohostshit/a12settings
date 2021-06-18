package com.google.android.settings.connecteddevice.dock;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import com.android.internal.annotations.VisibleForTesting;

public class DockContract {
    private static final ComponentName DOCK_COMPONENT = new ComponentName("com.google.android.apps.dreamliner", "com.google.android.apps.dreamliner.settings.DockDetailSettings");
    public static final String[] DOCK_PROJECTION = {"dockId", "dockName"};
    @VisibleForTesting
    static final int DOCK_PROVIDER_CONNECTED_TOKEN = 1;
    public static final Uri DOCK_PROVIDER_CONNECTED_URI = new Uri.Builder().scheme("content").authority("com.google.android.apps.dreamliner.provider").appendPath("connected").build();
    @VisibleForTesting
    static final int DOCK_PROVIDER_SAVED_TOKEN = 2;
    public static final Uri DOCK_PROVIDER_SAVED_URI = new Uri.Builder().scheme("content").authority("com.google.android.apps.dreamliner.provider").appendPath("saved").build();

    public static Intent buildDockSettingIntent(String str) {
        return new Intent().setComponent(DOCK_COMPONENT).putExtra("dockId", str);
    }
}
