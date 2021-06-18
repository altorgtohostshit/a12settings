package com.android.settings.connecteddevice.dock;

import android.content.Context;

public interface DockUpdater {
    void forceUpdate() {
    }

    void registerCallback() {
    }

    void setPreferenceContext(Context context) {
    }

    void unregisterCallback() {
    }
}
