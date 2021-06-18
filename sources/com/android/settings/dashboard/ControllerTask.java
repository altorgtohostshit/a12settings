package com.android.settings.dashboard;

import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.utils.ThreadUtils;

public class ControllerTask implements Runnable {
    private final AbstractPreferenceController mController;
    private final int mMetricsCategory;
    private final MetricsFeatureProvider mMetricsFeature;
    private final PreferenceScreen mScreen;

    public ControllerTask(AbstractPreferenceController abstractPreferenceController, PreferenceScreen preferenceScreen, MetricsFeatureProvider metricsFeatureProvider, int i) {
        this.mController = abstractPreferenceController;
        this.mScreen = preferenceScreen;
        this.mMetricsFeature = metricsFeatureProvider;
        this.mMetricsCategory = i;
    }

    public void run() {
        if (this.mController.isAvailable()) {
            String preferenceKey = this.mController.getPreferenceKey();
            if (TextUtils.isEmpty(preferenceKey)) {
                Log.d("ControllerTask", String.format("Preference key is %s in Controller %s", new Object[]{preferenceKey, this.mController.getClass().getSimpleName()}));
                return;
            }
            Preference findPreference = this.mScreen.findPreference(preferenceKey);
            if (findPreference == null) {
                Log.d("ControllerTask", String.format("Cannot find preference with key %s in Controller %s", new Object[]{preferenceKey, this.mController.getClass().getSimpleName()}));
                return;
            }
            ThreadUtils.postOnMainThread(new ControllerTask$$ExternalSyntheticLambda0(this, findPreference));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$run$0(Preference preference) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        this.mController.updateState(preference);
        int elapsedRealtime2 = (int) (SystemClock.elapsedRealtime() - elapsedRealtime);
        if (elapsedRealtime2 > 50) {
            Log.w("ControllerTask", "The updateState took " + elapsedRealtime2 + " ms in Controller " + this.mController.getClass().getSimpleName());
            MetricsFeatureProvider metricsFeatureProvider = this.mMetricsFeature;
            if (metricsFeatureProvider != null) {
                metricsFeatureProvider.action(0, 1728, this.mMetricsCategory, this.mController.getClass().getSimpleName(), elapsedRealtime2);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public AbstractPreferenceController getController() {
        return this.mController;
    }
}
