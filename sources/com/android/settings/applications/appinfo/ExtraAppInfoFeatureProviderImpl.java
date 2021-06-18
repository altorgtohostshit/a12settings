package com.android.settings.applications.appinfo;

import android.content.Context;

public class ExtraAppInfoFeatureProviderImpl implements ExtraAppInfoFeatureProvider {
    public String getSummary(Context context) {
        return "";
    }

    public boolean isSupported(Context context) {
        return false;
    }

    public void launchExtraAppInfoSettings(Context context) {
    }

    public void setPackageName(String str) {
    }
}
