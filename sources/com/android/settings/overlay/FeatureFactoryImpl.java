package com.android.settings.overlay;

import android.app.AppGlobals;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.VpnManager;
import android.os.UserManager;
import androidx.annotation.Keep;
import com.android.settings.accounts.AccountFeatureProvider;
import com.android.settings.accounts.AccountFeatureProviderImpl;
import com.android.settings.applications.ApplicationFeatureProvider;
import com.android.settings.applications.ApplicationFeatureProviderImpl;
import com.android.settings.applications.appinfo.ExtraAppInfoFeatureProvider;
import com.android.settings.applications.appinfo.ExtraAppInfoFeatureProviderImpl;
import com.android.settings.aware.AwareFeatureProvider;
import com.android.settings.aware.AwareFeatureProviderImpl;
import com.android.settings.biometrics.face.FaceFeatureProvider;
import com.android.settings.biometrics.face.FaceFeatureProviderImpl;
import com.android.settings.bluetooth.BluetoothFeatureProvider;
import com.android.settings.bluetooth.BluetoothFeatureProviderImpl;
import com.android.settings.connecteddevice.dock.DockUpdaterFeatureProviderImpl;
import com.android.settings.core.instrumentation.SettingsMetricsFeatureProvider;
import com.android.settings.dashboard.DashboardFeatureProvider;
import com.android.settings.dashboard.DashboardFeatureProviderImpl;
import com.android.settings.dashboard.suggestions.SuggestionFeatureProvider;
import com.android.settings.dashboard.suggestions.SuggestionFeatureProviderImpl;
import com.android.settings.enterprise.EnterprisePrivacyFeatureProvider;
import com.android.settings.enterprise.EnterprisePrivacyFeatureProviderImpl;
import com.android.settings.fuelgauge.BatterySettingsFeatureProvider;
import com.android.settings.fuelgauge.BatterySettingsFeatureProviderImpl;
import com.android.settings.fuelgauge.BatteryStatusFeatureProvider;
import com.android.settings.fuelgauge.BatteryStatusFeatureProviderImpl;
import com.android.settings.fuelgauge.PowerUsageFeatureProvider;
import com.android.settings.fuelgauge.PowerUsageFeatureProviderImpl;
import com.android.settings.gestures.AssistGestureFeatureProvider;
import com.android.settings.gestures.AssistGestureFeatureProviderImpl;
import com.android.settings.homepage.contextualcards.ContextualCardFeatureProvider;
import com.android.settings.homepage.contextualcards.ContextualCardFeatureProviderImpl;
import com.android.settings.localepicker.LocaleFeatureProvider;
import com.android.settings.localepicker.LocaleFeatureProviderImpl;
import com.android.settings.panel.PanelFeatureProvider;
import com.android.settings.panel.PanelFeatureProviderImpl;
import com.android.settings.search.SearchFeatureProvider;
import com.android.settings.search.SearchFeatureProviderImpl;
import com.android.settings.security.SecurityFeatureProvider;
import com.android.settings.security.SecurityFeatureProviderImpl;
import com.android.settings.security.SecuritySettingsFeatureProvider;
import com.android.settings.security.SecuritySettingsFeatureProviderImpl;
import com.android.settings.slices.SlicesFeatureProvider;
import com.android.settings.slices.SlicesFeatureProviderImpl;
import com.android.settings.users.UserFeatureProvider;
import com.android.settings.users.UserFeatureProviderImpl;
import com.android.settings.wifi.WifiTrackerLibProvider;
import com.android.settings.wifi.WifiTrackerLibProviderImpl;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;

@Keep
public class FeatureFactoryImpl extends FeatureFactory {
    private AccountFeatureProvider mAccountFeatureProvider;
    private ApplicationFeatureProvider mApplicationFeatureProvider;
    private AssistGestureFeatureProvider mAssistGestureFeatureProvider;
    private AwareFeatureProvider mAwareFeatureProvider;
    private BatterySettingsFeatureProvider mBatterySettingsFeatureProvider;
    private BatteryStatusFeatureProvider mBatteryStatusFeatureProvider;
    private BluetoothFeatureProvider mBluetoothFeatureProvider;
    private ContextualCardFeatureProvider mContextualCardFeatureProvider;
    private DashboardFeatureProviderImpl mDashboardFeatureProvider;
    private DockUpdaterFeatureProvider mDockUpdaterFeatureProvider;
    private EnterprisePrivacyFeatureProvider mEnterprisePrivacyFeatureProvider;
    private ExtraAppInfoFeatureProvider mExtraAppInfoFeatureProvider;
    private FaceFeatureProvider mFaceFeatureProvider;
    private LocaleFeatureProvider mLocaleFeatureProvider;
    private MetricsFeatureProvider mMetricsFeatureProvider;
    private PanelFeatureProvider mPanelFeatureProvider;
    private PowerUsageFeatureProvider mPowerUsageFeatureProvider;
    private SearchFeatureProvider mSearchFeatureProvider;
    private SecurityFeatureProvider mSecurityFeatureProvider;
    private SecuritySettingsFeatureProvider mSecuritySettingsFeatureProvider;
    private SlicesFeatureProvider mSlicesFeatureProvider;
    private SuggestionFeatureProvider mSuggestionFeatureProvider;
    private UserFeatureProvider mUserFeatureProvider;
    private WifiTrackerLibProvider mWifiTrackerLibProvider;

    public SupportFeatureProvider getSupportFeatureProvider(Context context) {
        return null;
    }

    public SurveyFeatureProvider getSurveyFeatureProvider(Context context) {
        return null;
    }

    public MetricsFeatureProvider getMetricsFeatureProvider() {
        if (this.mMetricsFeatureProvider == null) {
            this.mMetricsFeatureProvider = new SettingsMetricsFeatureProvider();
        }
        return this.mMetricsFeatureProvider;
    }

    public PowerUsageFeatureProvider getPowerUsageFeatureProvider(Context context) {
        if (this.mPowerUsageFeatureProvider == null) {
            this.mPowerUsageFeatureProvider = new PowerUsageFeatureProviderImpl(context.getApplicationContext());
        }
        return this.mPowerUsageFeatureProvider;
    }

    public BatteryStatusFeatureProvider getBatteryStatusFeatureProvider(Context context) {
        if (this.mBatteryStatusFeatureProvider == null) {
            this.mBatteryStatusFeatureProvider = new BatteryStatusFeatureProviderImpl(context.getApplicationContext());
        }
        return this.mBatteryStatusFeatureProvider;
    }

    public BatterySettingsFeatureProvider getBatterySettingsFeatureProvider(Context context) {
        if (this.mBatterySettingsFeatureProvider == null) {
            this.mBatterySettingsFeatureProvider = new BatterySettingsFeatureProviderImpl(context);
        }
        return this.mBatterySettingsFeatureProvider;
    }

    public DashboardFeatureProvider getDashboardFeatureProvider(Context context) {
        if (this.mDashboardFeatureProvider == null) {
            this.mDashboardFeatureProvider = new DashboardFeatureProviderImpl(context.getApplicationContext());
        }
        return this.mDashboardFeatureProvider;
    }

    public DockUpdaterFeatureProvider getDockUpdaterFeatureProvider() {
        if (this.mDockUpdaterFeatureProvider == null) {
            this.mDockUpdaterFeatureProvider = new DockUpdaterFeatureProviderImpl();
        }
        return this.mDockUpdaterFeatureProvider;
    }

    public ApplicationFeatureProvider getApplicationFeatureProvider(Context context) {
        if (this.mApplicationFeatureProvider == null) {
            Context applicationContext = context.getApplicationContext();
            this.mApplicationFeatureProvider = new ApplicationFeatureProviderImpl(applicationContext, applicationContext.getPackageManager(), AppGlobals.getPackageManager(), (DevicePolicyManager) applicationContext.getSystemService("device_policy"));
        }
        return this.mApplicationFeatureProvider;
    }

    public LocaleFeatureProvider getLocaleFeatureProvider() {
        if (this.mLocaleFeatureProvider == null) {
            this.mLocaleFeatureProvider = new LocaleFeatureProviderImpl();
        }
        return this.mLocaleFeatureProvider;
    }

    public EnterprisePrivacyFeatureProvider getEnterprisePrivacyFeatureProvider(Context context) {
        if (this.mEnterprisePrivacyFeatureProvider == null) {
            Context applicationContext = context.getApplicationContext();
            this.mEnterprisePrivacyFeatureProvider = new EnterprisePrivacyFeatureProviderImpl(applicationContext, (DevicePolicyManager) applicationContext.getSystemService("device_policy"), applicationContext.getPackageManager(), UserManager.get(applicationContext), (ConnectivityManager) applicationContext.getSystemService(ConnectivityManager.class), (VpnManager) applicationContext.getSystemService(VpnManager.class), applicationContext.getResources());
        }
        return this.mEnterprisePrivacyFeatureProvider;
    }

    public SearchFeatureProvider getSearchFeatureProvider() {
        if (this.mSearchFeatureProvider == null) {
            this.mSearchFeatureProvider = new SearchFeatureProviderImpl();
        }
        return this.mSearchFeatureProvider;
    }

    public SecurityFeatureProvider getSecurityFeatureProvider() {
        if (this.mSecurityFeatureProvider == null) {
            this.mSecurityFeatureProvider = new SecurityFeatureProviderImpl();
        }
        return this.mSecurityFeatureProvider;
    }

    public SuggestionFeatureProvider getSuggestionFeatureProvider(Context context) {
        if (this.mSuggestionFeatureProvider == null) {
            this.mSuggestionFeatureProvider = new SuggestionFeatureProviderImpl(context.getApplicationContext());
        }
        return this.mSuggestionFeatureProvider;
    }

    public UserFeatureProvider getUserFeatureProvider(Context context) {
        if (this.mUserFeatureProvider == null) {
            this.mUserFeatureProvider = new UserFeatureProviderImpl(context.getApplicationContext());
        }
        return this.mUserFeatureProvider;
    }

    public AssistGestureFeatureProvider getAssistGestureFeatureProvider() {
        if (this.mAssistGestureFeatureProvider == null) {
            this.mAssistGestureFeatureProvider = new AssistGestureFeatureProviderImpl();
        }
        return this.mAssistGestureFeatureProvider;
    }

    public SlicesFeatureProvider getSlicesFeatureProvider() {
        if (this.mSlicesFeatureProvider == null) {
            this.mSlicesFeatureProvider = new SlicesFeatureProviderImpl();
        }
        return this.mSlicesFeatureProvider;
    }

    public AccountFeatureProvider getAccountFeatureProvider() {
        if (this.mAccountFeatureProvider == null) {
            this.mAccountFeatureProvider = new AccountFeatureProviderImpl();
        }
        return this.mAccountFeatureProvider;
    }

    public PanelFeatureProvider getPanelFeatureProvider() {
        if (this.mPanelFeatureProvider == null) {
            this.mPanelFeatureProvider = new PanelFeatureProviderImpl();
        }
        return this.mPanelFeatureProvider;
    }

    public ContextualCardFeatureProvider getContextualCardFeatureProvider(Context context) {
        if (this.mContextualCardFeatureProvider == null) {
            this.mContextualCardFeatureProvider = new ContextualCardFeatureProviderImpl(context.getApplicationContext());
        }
        return this.mContextualCardFeatureProvider;
    }

    public BluetoothFeatureProvider getBluetoothFeatureProvider(Context context) {
        if (this.mBluetoothFeatureProvider == null) {
            this.mBluetoothFeatureProvider = new BluetoothFeatureProviderImpl(context.getApplicationContext());
        }
        return this.mBluetoothFeatureProvider;
    }

    public AwareFeatureProvider getAwareFeatureProvider() {
        if (this.mAwareFeatureProvider == null) {
            this.mAwareFeatureProvider = new AwareFeatureProviderImpl();
        }
        return this.mAwareFeatureProvider;
    }

    public FaceFeatureProvider getFaceFeatureProvider() {
        if (this.mFaceFeatureProvider == null) {
            this.mFaceFeatureProvider = new FaceFeatureProviderImpl();
        }
        return this.mFaceFeatureProvider;
    }

    public WifiTrackerLibProvider getWifiTrackerLibProvider() {
        if (this.mWifiTrackerLibProvider == null) {
            this.mWifiTrackerLibProvider = new WifiTrackerLibProviderImpl();
        }
        return this.mWifiTrackerLibProvider;
    }

    public ExtraAppInfoFeatureProvider getExtraAppInfoFeatureProvider() {
        if (this.mExtraAppInfoFeatureProvider == null) {
            this.mExtraAppInfoFeatureProvider = new ExtraAppInfoFeatureProviderImpl();
        }
        return this.mExtraAppInfoFeatureProvider;
    }

    public SecuritySettingsFeatureProvider getSecuritySettingsFeatureProvider() {
        if (this.mSecuritySettingsFeatureProvider == null) {
            this.mSecuritySettingsFeatureProvider = new SecuritySettingsFeatureProviderImpl();
        }
        return this.mSecuritySettingsFeatureProvider;
    }
}
