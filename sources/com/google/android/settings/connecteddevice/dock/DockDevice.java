package com.google.android.settings.connecteddevice.dock;

public class DockDevice {
    private String mId;
    private String mName;

    private DockDevice() {
    }

    DockDevice(String str, String str2) {
        this.mId = str;
        this.mName = str2;
    }

    public String getName() {
        return this.mName;
    }

    public String getId() {
        return this.mId;
    }
}
