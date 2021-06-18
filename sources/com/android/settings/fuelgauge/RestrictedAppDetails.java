package com.android.settings.fuelgauge;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.UserHandle;
import android.util.IconDrawableFactory;
import android.util.Log;
import android.util.SparseLongArray;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.core.InstrumentedPreferenceFragment;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.fuelgauge.batterytip.AppInfo;
import com.android.settings.fuelgauge.batterytip.BatteryDatabaseManager;
import com.android.settings.fuelgauge.batterytip.BatteryTipDialogFragment;
import com.android.settings.fuelgauge.batterytip.BatteryTipPreferenceController;
import com.android.settings.fuelgauge.batterytip.tips.BatteryTip;
import com.android.settings.fuelgauge.batterytip.tips.RestrictAppTip;
import com.android.settings.fuelgauge.batterytip.tips.UnrestrictAppTip;
import com.android.settings.widget.AppCheckBoxPreference;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.utils.StringUtil;
import java.util.List;

public class RestrictedAppDetails extends DashboardFragment implements BatteryTipPreferenceController.BatteryTipListener {
    static final String EXTRA_APP_INFO_LIST = "app_info_list";
    List<AppInfo> mAppInfos;
    BatteryDatabaseManager mBatteryDatabaseManager;
    BatteryUtils mBatteryUtils;
    IconDrawableFactory mIconDrawableFactory;
    PackageManager mPackageManager;
    PreferenceGroup mRestrictedAppListGroup;

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return null;
    }

    public int getHelpResource() {
        return R.string.help_uri_restricted_apps;
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "RestrictedAppDetails";
    }

    public int getMetricsCategory() {
        return 1285;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.restricted_apps_detail;
    }

    public static void startRestrictedAppDetails(InstrumentedPreferenceFragment instrumentedPreferenceFragment, List<AppInfo> list) {
        Bundle bundle = new Bundle();
        bundle.putParcelableList(EXTRA_APP_INFO_LIST, list);
        new SubSettingLauncher(instrumentedPreferenceFragment.getContext()).setDestination(RestrictedAppDetails.class.getName()).setArguments(bundle).setTitleRes(R.string.restricted_app_title).setSourceMetricsCategory(instrumentedPreferenceFragment.getMetricsCategory()).launch();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Context context = getContext();
        this.mRestrictedAppListGroup = (PreferenceGroup) findPreference("restrict_app_list");
        this.mAppInfos = getArguments().getParcelableArrayList(EXTRA_APP_INFO_LIST);
        this.mPackageManager = context.getPackageManager();
        this.mIconDrawableFactory = IconDrawableFactory.newInstance(context);
        this.mBatteryUtils = BatteryUtils.getInstance(context);
        this.mBatteryDatabaseManager = BatteryDatabaseManager.getInstance(context);
        refreshUi();
    }

    public boolean onPreferenceTreeClick(Preference preference) {
        return super.onPreferenceTreeClick(preference);
    }

    /* access modifiers changed from: package-private */
    public void refreshUi() {
        this.mRestrictedAppListGroup.removeAll();
        Context prefContext = getPrefContext();
        SparseLongArray queryActionTime = this.mBatteryDatabaseManager.queryActionTime(0);
        long currentTimeMillis = System.currentTimeMillis();
        int size = this.mAppInfos.size();
        for (int i = 0; i < size; i++) {
            AppCheckBoxPreference appCheckBoxPreference = new AppCheckBoxPreference(prefContext);
            AppInfo appInfo = this.mAppInfos.get(i);
            try {
                ApplicationInfo applicationInfoAsUser = this.mPackageManager.getApplicationInfoAsUser(appInfo.packageName, 0, UserHandle.getUserId(appInfo.uid));
                appCheckBoxPreference.setChecked(this.mBatteryUtils.isForceAppStandbyEnabled(appInfo.uid, appInfo.packageName));
                appCheckBoxPreference.setTitle(this.mPackageManager.getApplicationLabel(applicationInfoAsUser));
                appCheckBoxPreference.setIcon(Utils.getBadgedIcon(this.mIconDrawableFactory, this.mPackageManager, appInfo.packageName, UserHandle.getUserId(appInfo.uid)));
                appCheckBoxPreference.setKey(getKeyFromAppInfo(appInfo));
                appCheckBoxPreference.setOnPreferenceChangeListener(new RestrictedAppDetails$$ExternalSyntheticLambda0(this, appInfo));
                long j = queryActionTime.get(appInfo.uid, -1);
                if (j != -1) {
                    appCheckBoxPreference.setSummary((CharSequence) getString(R.string.restricted_app_time_summary, StringUtil.formatRelativeTime(prefContext, (double) (currentTimeMillis - j), false)));
                }
                appCheckBoxPreference.getSummaryOn();
                this.mRestrictedAppListGroup.addPreference(appCheckBoxPreference);
            } catch (PackageManager.NameNotFoundException unused) {
                Log.e("RestrictedAppDetails", "Can't find package: " + appInfo.packageName);
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$refreshUi$0(AppInfo appInfo, Preference preference, Object obj) {
        BatteryTipDialogFragment createDialogFragment = createDialogFragment(appInfo, ((Boolean) obj).booleanValue());
        createDialogFragment.setTargetFragment(this, 0);
        createDialogFragment.show(getFragmentManager(), "RestrictedAppDetails");
        return false;
    }

    public void onBatteryTipHandled(BatteryTip batteryTip) {
        AppInfo appInfo;
        boolean z = batteryTip instanceof RestrictAppTip;
        if (z) {
            appInfo = ((RestrictAppTip) batteryTip).getRestrictAppList().get(0);
        } else {
            appInfo = ((UnrestrictAppTip) batteryTip).getUnrestrictAppInfo();
        }
        CheckBoxPreference checkBoxPreference = (CheckBoxPreference) this.mRestrictedAppListGroup.findPreference(getKeyFromAppInfo(appInfo));
        if (checkBoxPreference != null) {
            checkBoxPreference.setChecked(z);
        }
    }

    /* access modifiers changed from: package-private */
    public BatteryTipDialogFragment createDialogFragment(AppInfo appInfo, boolean z) {
        BatteryTip batteryTip;
        if (z) {
            batteryTip = new RestrictAppTip(0, appInfo);
        } else {
            batteryTip = new UnrestrictAppTip(0, appInfo);
        }
        return BatteryTipDialogFragment.newInstance(batteryTip, getMetricsCategory());
    }

    /* access modifiers changed from: package-private */
    public String getKeyFromAppInfo(AppInfo appInfo) {
        return appInfo.uid + "," + appInfo.packageName;
    }
}
