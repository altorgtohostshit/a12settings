package com.android.settingslib.core.instrumentation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnAttach;

public class VisibilityLoggerMixin implements LifecycleObserver, OnAttach {
    private long mCreationTimestamp;
    private final int mMetricsCategory;
    private MetricsFeatureProvider mMetricsFeature;
    private int mSourceMetricsCategory = 0;
    private long mVisibleTimestamp;

    public VisibilityLoggerMixin(int i, MetricsFeatureProvider metricsFeatureProvider) {
        this.mMetricsCategory = i;
        this.mMetricsFeature = metricsFeatureProvider;
    }

    public void onAttach() {
        this.mCreationTimestamp = SystemClock.elapsedRealtime();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        if (this.mMetricsFeature != null && this.mMetricsCategory != 0) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            this.mVisibleTimestamp = elapsedRealtime;
            long j = this.mCreationTimestamp;
            if (j != 0) {
                this.mMetricsFeature.visible((Context) null, this.mSourceMetricsCategory, this.mMetricsCategory, (int) (elapsedRealtime - j));
                return;
            }
            this.mMetricsFeature.visible((Context) null, this.mSourceMetricsCategory, this.mMetricsCategory, 0);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        this.mCreationTimestamp = 0;
        if (this.mMetricsFeature != null && this.mMetricsCategory != 0) {
            this.mMetricsFeature.hidden((Context) null, this.mMetricsCategory, (int) (SystemClock.elapsedRealtime() - this.mVisibleTimestamp));
        }
    }

    public void writeElapsedTimeMetric(int i, String str) {
        if (this.mMetricsFeature != null && this.mMetricsCategory != 0 && this.mCreationTimestamp != 0) {
            int i2 = i;
            this.mMetricsFeature.action(0, i2, this.mMetricsCategory, str, (int) (SystemClock.elapsedRealtime() - this.mCreationTimestamp));
        }
    }

    public void setSourceMetricsCategory(Activity activity) {
        Intent intent;
        if (this.mSourceMetricsCategory == 0 && activity != null && (intent = activity.getIntent()) != null) {
            this.mSourceMetricsCategory = intent.getIntExtra(":settings:source_metrics", 0);
        }
    }
}
