package com.android.settings.location;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import androidx.preference.Preference;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.widget.RestrictedAppPreference;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.location.InjectedSetting;
import com.android.settingslib.location.SettingsInjector;
import com.android.settingslib.widget.AppPreference;

public class AppSettingsInjector extends SettingsInjector {
    private final int mMetricsCategory;
    private final MetricsFeatureProvider mMetricsFeatureProvider;

    public AppSettingsInjector(Context context, int i) {
        super(context);
        this.mMetricsCategory = i;
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(context).getMetricsFeatureProvider();
    }

    /* access modifiers changed from: protected */
    public Preference createPreference(Context context, InjectedSetting injectedSetting) {
        if (TextUtils.isEmpty(injectedSetting.userRestriction)) {
            return new AppPreference(context);
        }
        return new RestrictedAppPreference(context, injectedSetting.userRestriction);
    }

    /* access modifiers changed from: protected */
    public void logPreferenceClick(Intent intent) {
        this.mMetricsFeatureProvider.logStartedIntent(intent, this.mMetricsCategory);
    }
}
