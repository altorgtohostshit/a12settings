package com.android.settings.display;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.view.View;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.Settings;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.widget.BannerMessagePreference;

public class TwilightLocationPreferenceController extends BasePreferenceController {
    private final LocationManager mLocationManager;
    private final MetricsFeatureProvider mMetricsFeatureProvider;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public TwilightLocationPreferenceController(Context context, String str) {
        super(context, str);
        this.mLocationManager = (LocationManager) context.getSystemService(LocationManager.class);
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(context).getMetricsFeatureProvider();
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        BannerMessagePreference bannerMessagePreference = (BannerMessagePreference) preferenceScreen.findPreference(getPreferenceKey());
        bannerMessagePreference.setPositiveButtonText(R.string.twilight_mode_launch_location).setPositiveButtonOnClickListener(new TwilightLocationPreferenceController$$ExternalSyntheticLambda0(this, bannerMessagePreference));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$displayPreference$0(BannerMessagePreference bannerMessagePreference, View view) {
        this.mMetricsFeatureProvider.logClickedPreference(bannerMessagePreference, getMetricsCategory());
        launchLocationSettings();
    }

    public int getAvailabilityStatus() {
        return this.mLocationManager.isLocationEnabled() ? 2 : 1;
    }

    private void launchLocationSettings() {
        Intent intent = new Intent();
        intent.setClass(this.mContext, Settings.LocationSettingsActivity.class);
        this.mContext.startActivity(intent);
    }
}
