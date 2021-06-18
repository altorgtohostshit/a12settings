package com.google.android.settings.overlay;

import android.app.AppGlobals;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.util.Log;
import com.android.settings.accounts.AccountFeatureProvider;
import com.android.settings.applications.ApplicationFeatureProvider;
import com.android.settings.applications.appinfo.ExtraAppInfoFeatureProvider;
import com.android.settings.aware.AwareFeatureProvider;
import com.android.settings.biometrics.face.FaceFeatureProvider;
import com.android.settings.dashboard.suggestions.SuggestionFeatureProvider;
import com.android.settings.fuelgauge.BatterySettingsFeatureProvider;
import com.android.settings.fuelgauge.BatteryStatusFeatureProvider;
import com.android.settings.fuelgauge.PowerUsageFeatureProvider;
import com.android.settings.gestures.AssistGestureFeatureProvider;
import com.android.settings.overlay.DockUpdaterFeatureProvider;
import com.android.settings.overlay.SupportFeatureProvider;
import com.android.settings.overlay.SurveyFeatureProvider;
import com.android.settings.search.SearchFeatureProvider;
import com.android.settings.security.SecuritySettingsFeatureProvider;
import com.android.settings.wifi.WifiTrackerLibProvider;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.google.android.settings.accounts.AccountFeatureProviderGoogleImpl;
import com.google.android.settings.applications.ApplicationFeatureProviderGoogleImpl;
import com.google.android.settings.aware.AwareFeatureProviderGoogleImpl;
import com.google.android.settings.biometrics.face.FaceFeatureProviderGoogleImpl;
import com.google.android.settings.connecteddevice.dock.DockUpdaterFeatureProviderGoogleImpl;
import com.google.android.settings.core.instrumentation.SettingsGoogleMetricsFeatureProvider;
import com.google.android.settings.dashboard.suggestions.SuggestionFeatureProviderGoogleImpl;
import com.google.android.settings.experiments.GServicesProxy;
import com.google.android.settings.fuelgauge.BatterySettingsFeatureProviderGoogleImpl;
import com.google.android.settings.fuelgauge.BatteryStatusFeatureProviderGoogleImpl;
import com.google.android.settings.fuelgauge.PowerUsageFeatureProviderGoogleImpl;
import com.google.android.settings.gamemode.GameModeFeatureProviderGoogleImpl;
import com.google.android.settings.gestures.assist.AssistGestureFeatureProviderGoogleImpl;
import com.google.android.settings.search.SearchFeatureProviderGoogleImpl;
import com.google.android.settings.security.SecuritySettingsFeatureProviderGoogleImpl;
import com.google.android.settings.support.SupportFeatureProviderImpl;
import com.google.android.settings.survey.SurveyFeatureProviderImpl;
import com.google.android.settings.wifi.WifiTrackerLibProviderGoogleImpl;

public final class FeatureFactoryImpl extends com.android.settings.overlay.FeatureFactoryImpl {
    private AccountFeatureProvider mAccountFeatureProvider;
    private ApplicationFeatureProvider mApplicationFeatureProvider;
    private AssistGestureFeatureProvider mAssistGestureFeatureProvider;
    private AwareFeatureProvider mAwareFeatureProvider;
    private BatterySettingsFeatureProvider mBatterySettingsFeatureProvider;
    private BatteryStatusFeatureProvider mBatteryStatusFeatureProvider;
    private DockUpdaterFeatureProvider mDockUpdaterFeatureProvider;
    private ExtraAppInfoFeatureProvider mExtraAppInfoFeatureProvider;
    private FaceFeatureProvider mFaceFeatureProvider;
    private MetricsFeatureProvider mMetricsFeatureProvider;
    private PowerUsageFeatureProvider mPowerUsageProvider;
    private SearchFeatureProvider mSearchFeatureProvider;
    private SecuritySettingsFeatureProvider mSecuritySettingsFeatureProvider;
    private SuggestionFeatureProvider mSuggestionFeatureProvider;
    private SupportFeatureProvider mSupportProvider;
    private SurveyFeatureProvider mSurveyFeatureProvider;
    private WifiTrackerLibProvider mWifiTrackerLibProvider;

    public ApplicationFeatureProvider getApplicationFeatureProvider(Context context) {
        if (this.mApplicationFeatureProvider == null) {
            Context applicationContext = context.getApplicationContext();
            this.mApplicationFeatureProvider = new ApplicationFeatureProviderGoogleImpl(applicationContext, applicationContext.getPackageManager(), AppGlobals.getPackageManager(), (DevicePolicyManager) applicationContext.getSystemService("device_policy"));
        }
        return this.mApplicationFeatureProvider;
    }

    public MetricsFeatureProvider getMetricsFeatureProvider() {
        if (this.mMetricsFeatureProvider == null) {
            this.mMetricsFeatureProvider = new SettingsGoogleMetricsFeatureProvider();
        }
        return this.mMetricsFeatureProvider;
    }

    public SupportFeatureProvider getSupportFeatureProvider(Context context) {
        if (this.mSupportProvider == null) {
            this.mSupportProvider = new SupportFeatureProviderImpl(context.getApplicationContext());
        }
        return this.mSupportProvider;
    }

    public BatteryStatusFeatureProvider getBatteryStatusFeatureProvider(Context context) {
        if (this.mBatteryStatusFeatureProvider == null) {
            this.mBatteryStatusFeatureProvider = new BatteryStatusFeatureProviderGoogleImpl(context.getApplicationContext());
        }
        return this.mBatteryStatusFeatureProvider;
    }

    public BatterySettingsFeatureProvider getBatterySettingsFeatureProvider(Context context) {
        if (this.mBatterySettingsFeatureProvider == null) {
            this.mBatterySettingsFeatureProvider = new BatterySettingsFeatureProviderGoogleImpl(context.getApplicationContext());
        }
        return this.mBatterySettingsFeatureProvider;
    }

    public PowerUsageFeatureProvider getPowerUsageFeatureProvider(Context context) {
        if (this.mPowerUsageProvider == null) {
            this.mPowerUsageProvider = new PowerUsageFeatureProviderGoogleImpl(context.getApplicationContext());
        }
        return this.mPowerUsageProvider;
    }

    public DockUpdaterFeatureProvider getDockUpdaterFeatureProvider() {
        if (this.mDockUpdaterFeatureProvider == null) {
            this.mDockUpdaterFeatureProvider = new DockUpdaterFeatureProviderGoogleImpl();
        }
        return this.mDockUpdaterFeatureProvider;
    }

    public SearchFeatureProvider getSearchFeatureProvider() {
        if (this.mSearchFeatureProvider == null) {
            this.mSearchFeatureProvider = new SearchFeatureProviderGoogleImpl();
        }
        return this.mSearchFeatureProvider;
    }

    public SurveyFeatureProvider getSurveyFeatureProvider(Context context) {
        boolean z = false;
        try {
            z = GServicesProxy.getBoolean(context.getContentResolver(), "settingsgoogle:surveys_enabled", false);
        } catch (SecurityException e) {
            Log.w("FeatureFactoryImpl", "Error reading survey feature enabled state", e);
        }
        if (!z) {
            return null;
        }
        if (this.mSurveyFeatureProvider == null) {
            this.mSurveyFeatureProvider = new SurveyFeatureProviderImpl(context);
        }
        return this.mSurveyFeatureProvider;
    }

    public SuggestionFeatureProvider getSuggestionFeatureProvider(Context context) {
        if (this.mSuggestionFeatureProvider == null) {
            this.mSuggestionFeatureProvider = new SuggestionFeatureProviderGoogleImpl(context.getApplicationContext());
        }
        return this.mSuggestionFeatureProvider;
    }

    public AssistGestureFeatureProvider getAssistGestureFeatureProvider() {
        if (this.mAssistGestureFeatureProvider == null) {
            this.mAssistGestureFeatureProvider = new AssistGestureFeatureProviderGoogleImpl();
        }
        return this.mAssistGestureFeatureProvider;
    }

    public AccountFeatureProvider getAccountFeatureProvider() {
        if (this.mAccountFeatureProvider == null) {
            this.mAccountFeatureProvider = new AccountFeatureProviderGoogleImpl();
        }
        return this.mAccountFeatureProvider;
    }

    public AwareFeatureProvider getAwareFeatureProvider() {
        if (this.mAwareFeatureProvider == null) {
            this.mAwareFeatureProvider = new AwareFeatureProviderGoogleImpl();
        }
        return this.mAwareFeatureProvider;
    }

    public FaceFeatureProvider getFaceFeatureProvider() {
        if (this.mFaceFeatureProvider == null) {
            this.mFaceFeatureProvider = new FaceFeatureProviderGoogleImpl();
        }
        return this.mFaceFeatureProvider;
    }

    public WifiTrackerLibProvider getWifiTrackerLibProvider() {
        if (this.mWifiTrackerLibProvider == null) {
            this.mWifiTrackerLibProvider = new WifiTrackerLibProviderGoogleImpl();
        }
        return this.mWifiTrackerLibProvider;
    }

    public ExtraAppInfoFeatureProvider getExtraAppInfoFeatureProvider() {
        if (this.mExtraAppInfoFeatureProvider == null) {
            this.mExtraAppInfoFeatureProvider = new GameModeFeatureProviderGoogleImpl();
        }
        return this.mExtraAppInfoFeatureProvider;
    }

    public SecuritySettingsFeatureProvider getSecuritySettingsFeatureProvider() {
        if (this.mSecuritySettingsFeatureProvider == null) {
            this.mSecuritySettingsFeatureProvider = new SecuritySettingsFeatureProviderGoogleImpl();
        }
        return this.mSecuritySettingsFeatureProvider;
    }
}
