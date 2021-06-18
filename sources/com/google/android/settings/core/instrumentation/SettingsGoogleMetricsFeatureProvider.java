package com.google.android.settings.core.instrumentation;

import com.android.settings.core.instrumentation.SettingsMetricsFeatureProvider;

public class SettingsGoogleMetricsFeatureProvider extends SettingsMetricsFeatureProvider {
    /* access modifiers changed from: protected */
    public void installLogWriters() {
        super.installLogWriters();
        this.mLoggerWriters.add(new SearchResultTraceLogWriter());
    }
}
