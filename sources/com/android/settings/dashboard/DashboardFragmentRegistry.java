package com.android.settings.dashboard;

import android.util.ArrayMap;
import com.android.settings.DisplaySettings;
import com.android.settings.LegalSettings;
import com.android.settings.accounts.AccountDashboardFragment;
import com.android.settings.accounts.AccountDetailDashboardFragment;
import com.android.settings.applications.AppAndNotificationDashboardFragment;
import com.android.settings.connecteddevice.AdvancedConnectedDeviceDashboardFragment;
import com.android.settings.connecteddevice.ConnectedDeviceDashboardFragment;
import com.android.settings.development.DevelopmentSettingsDashboardFragment;
import com.android.settings.deviceinfo.StorageDashboardFragment;
import com.android.settings.deviceinfo.aboutphone.MyDeviceInfoFragment;
import com.android.settings.display.NightDisplaySettings;
import com.android.settings.emergency.EmergencyDashboardFragment;
import com.android.settings.enterprise.EnterprisePrivacySettings;
import com.android.settings.fuelgauge.PowerUsageSummary;
import com.android.settings.fuelgauge.SmartBatterySettings;
import com.android.settings.fuelgauge.batterysaver.BatterySaverSettings;
import com.android.settings.gestures.GestureSettings;
import com.android.settings.homepage.TopLevelSettings;
import com.android.settings.language.LanguageAndInputSettings;
import com.android.settings.network.NetworkDashboardFragment;
import com.android.settings.notification.ConfigureNotificationSettings;
import com.android.settings.notification.SoundSettings;
import com.android.settings.notification.zen.ZenModeSettings;
import com.android.settings.privacy.PrivacyDashboardFragment;
import com.android.settings.security.LockscreenDashboardFragment;
import com.android.settings.security.SecuritySettings;
import com.android.settings.system.SystemDashboardFragment;
import java.util.Map;

public class DashboardFragmentRegistry {
    public static final Map<String, String> CATEGORY_KEY_TO_PARENT_MAP;
    public static final Map<String, String> PARENT_TO_CATEGORY_KEY_MAP;

    static {
        ArrayMap arrayMap = new ArrayMap();
        PARENT_TO_CATEGORY_KEY_MAP = arrayMap;
        arrayMap.put(TopLevelSettings.class.getName(), "com.android.settings.category.ia.homepage");
        arrayMap.put(NetworkDashboardFragment.class.getName(), "com.android.settings.category.ia.wireless");
        arrayMap.put(ConnectedDeviceDashboardFragment.class.getName(), "com.android.settings.category.ia.connect");
        arrayMap.put(AdvancedConnectedDeviceDashboardFragment.class.getName(), "com.android.settings.category.ia.device");
        arrayMap.put(AppAndNotificationDashboardFragment.class.getName(), "com.android.settings.category.ia.apps");
        arrayMap.put(PowerUsageSummary.class.getName(), "com.android.settings.category.ia.battery");
        arrayMap.put(DisplaySettings.class.getName(), "com.android.settings.category.ia.display");
        arrayMap.put(EmergencyDashboardFragment.class.getName(), "com.android.settings.category.ia.emergency");
        arrayMap.put(SoundSettings.class.getName(), "com.android.settings.category.ia.sound");
        arrayMap.put(StorageDashboardFragment.class.getName(), "com.android.settings.category.ia.storage");
        arrayMap.put(SecuritySettings.class.getName(), "com.android.settings.category.ia.security");
        arrayMap.put(AccountDetailDashboardFragment.class.getName(), "com.android.settings.category.ia.account_detail");
        arrayMap.put(AccountDashboardFragment.class.getName(), "com.android.settings.category.ia.accounts");
        arrayMap.put(SystemDashboardFragment.class.getName(), "com.android.settings.category.ia.system");
        arrayMap.put(LanguageAndInputSettings.class.getName(), "com.android.settings.category.ia.language");
        arrayMap.put(DevelopmentSettingsDashboardFragment.class.getName(), "com.android.settings.category.ia.development");
        arrayMap.put(ConfigureNotificationSettings.class.getName(), "com.android.settings.category.ia.notifications");
        arrayMap.put(LockscreenDashboardFragment.class.getName(), "com.android.settings.category.ia.lockscreen");
        arrayMap.put(ZenModeSettings.class.getName(), "com.android.settings.category.ia.dnd");
        arrayMap.put(GestureSettings.class.getName(), "com.android.settings.category.ia.gestures");
        arrayMap.put(NightDisplaySettings.class.getName(), "com.android.settings.category.ia.night_display");
        arrayMap.put(PrivacyDashboardFragment.class.getName(), "com.android.settings.category.ia.privacy");
        arrayMap.put(EnterprisePrivacySettings.class.getName(), "com.android.settings.category.ia.enterprise_privacy");
        arrayMap.put(LegalSettings.class.getName(), "com.android.settings.category.ia.about_legal");
        arrayMap.put(MyDeviceInfoFragment.class.getName(), "com.android.settings.category.ia.my_device_info");
        arrayMap.put(BatterySaverSettings.class.getName(), "com.android.settings.category.ia.battery_saver_settings");
        arrayMap.put(SmartBatterySettings.class.getName(), "com.android.settings.category.ia.smart_battery_settings");
        CATEGORY_KEY_TO_PARENT_MAP = new ArrayMap(arrayMap.size());
        for (Map.Entry entry : arrayMap.entrySet()) {
            CATEGORY_KEY_TO_PARENT_MAP.put((String) entry.getValue(), (String) entry.getKey());
        }
        CATEGORY_KEY_TO_PARENT_MAP.put("com.android.settings.category.ia.account_detail", AccountDashboardFragment.class.getName());
    }
}
