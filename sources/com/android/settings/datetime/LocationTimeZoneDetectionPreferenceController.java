package com.android.settings.datetime;

import android.app.time.TimeManager;
import android.app.time.TimeZoneCapabilitiesAndConfig;
import android.app.time.TimeZoneConfiguration;
import android.content.Context;
import android.content.IntentFilter;
import android.location.LocationManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.InstrumentedPreferenceFragment;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;

public class LocationTimeZoneDetectionPreferenceController extends TogglePreferenceController implements LifecycleObserver, OnStart, OnStop, TimeManager.TimeZoneDetectorListener {
    private static final String TAG = "location_time_zone_detection";
    private InstrumentedPreferenceFragment mFragment;
    private final LocationManager mLocationManager;
    private Preference mPreference;
    private final TimeManager mTimeManager;
    private TimeZoneCapabilitiesAndConfig mTimeZoneCapabilitiesAndConfig;

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

    public boolean isSliceable() {
        return false;
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public LocationTimeZoneDetectionPreferenceController(Context context) {
        super(context, TAG);
        this.mTimeManager = (TimeManager) context.getSystemService(TimeManager.class);
        this.mLocationManager = (LocationManager) context.getSystemService(LocationManager.class);
    }

    /* access modifiers changed from: package-private */
    public void setFragment(InstrumentedPreferenceFragment instrumentedPreferenceFragment) {
        this.mFragment = instrumentedPreferenceFragment;
    }

    public boolean isChecked() {
        return this.mTimeManager.getTimeZoneCapabilitiesAndConfig().getConfiguration().isGeoDetectionEnabled();
    }

    public boolean setChecked(boolean z) {
        if (!z || this.mLocationManager.isLocationEnabled()) {
            return this.mTimeManager.updateTimeZoneConfiguration(new TimeZoneConfiguration.Builder().setGeoDetectionEnabled(z).build());
        }
        new LocationToggleDisabledDialogFragment(this.mContext).show(this.mFragment.getFragmentManager(), TAG);
        return false;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    public void onStart() {
        this.mTimeManager.addTimeZoneDetectorListener(this.mContext.getMainExecutor(), this);
        refreshUi();
    }

    public void onStop() {
        this.mTimeManager.removeTimeZoneDetectorListener(this);
    }

    public int getAvailabilityStatus() {
        int configureGeoDetectionEnabledCapability = getTimeZoneCapabilitiesAndConfig(false).getCapabilities().getConfigureGeoDetectionEnabledCapability();
        if (configureGeoDetectionEnabledCapability == 10 || configureGeoDetectionEnabledCapability == 20) {
            return 3;
        }
        if (configureGeoDetectionEnabledCapability == 30 || configureGeoDetectionEnabledCapability == 40) {
            return 0;
        }
        throw new IllegalStateException("Unknown capability=" + configureGeoDetectionEnabledCapability);
    }

    public CharSequence getSummary() {
        int i;
        TimeZoneCapabilitiesAndConfig timeZoneCapabilitiesAndConfig = getTimeZoneCapabilitiesAndConfig(false);
        int configureGeoDetectionEnabledCapability = timeZoneCapabilitiesAndConfig.getCapabilities().getConfigureGeoDetectionEnabledCapability();
        TimeZoneConfiguration configuration = timeZoneCapabilitiesAndConfig.getConfiguration();
        if (configureGeoDetectionEnabledCapability == 10) {
            i = R.string.location_time_zone_detection_not_supported;
        } else if (configureGeoDetectionEnabledCapability == 20) {
            i = R.string.location_time_zone_detection_not_allowed;
        } else if (configureGeoDetectionEnabledCapability == 30) {
            i = !this.mLocationManager.isLocationEnabled() ? R.string.location_app_permission_summary_location_off : !configuration.isAutoDetectionEnabled() ? R.string.location_time_zone_detection_auto_is_off : R.string.location_time_zone_detection_not_applicable;
        } else if (configureGeoDetectionEnabledCapability == 40) {
            return "";
        } else {
            throw new IllegalStateException("Unexpected configureGeoDetectionEnabledCapability=" + configureGeoDetectionEnabledCapability);
        }
        return this.mContext.getString(i);
    }

    public void onChange() {
        refreshUi();
    }

    private void refreshUi() {
        getTimeZoneCapabilitiesAndConfig(true);
        refreshSummary(this.mPreference);
    }

    private TimeZoneCapabilitiesAndConfig getTimeZoneCapabilitiesAndConfig(boolean z) {
        if (z || this.mTimeZoneCapabilitiesAndConfig == null) {
            this.mTimeZoneCapabilitiesAndConfig = this.mTimeManager.getTimeZoneCapabilitiesAndConfig();
        }
        return this.mTimeZoneCapabilitiesAndConfig;
    }
}
