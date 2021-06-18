package com.android.settings.search;

import android.util.ArrayMap;
import com.android.settings.DisplaySettings;
import com.android.settings.backup.UserBackupSettingsActivity;
import com.android.settings.connecteddevice.ConnectedDeviceDashboardFragment;
import com.android.settings.connecteddevice.usb.UsbDetailsFragment;
import com.android.settings.fuelgauge.PowerUsageAdvanced;
import com.android.settings.fuelgauge.PowerUsageSummary;
import com.android.settings.gestures.GestureNavigationSettingsFragment;
import com.android.settings.gestures.SystemNavigationGestureSettings;
import com.android.settings.location.LocationSettings;
import com.android.settings.location.RecentLocationAccessSeeAllFragment;
import com.android.settings.network.NetworkDashboardFragment;
import com.android.settings.notification.zen.ZenModeBlockedEffectsSettings;
import com.android.settings.notification.zen.ZenModeRestrictNotificationsSettings;
import com.android.settings.security.SecuritySettings;
import com.android.settings.security.screenlock.ScreenLockSettings;
import com.android.settings.system.SystemDashboardFragment;
import com.android.settings.wallpaper.WallpaperSuggestionActivity;
import com.android.settings.wifi.WifiSettings;
import java.util.Map;

public class CustomSiteMapRegistry {
    public static final Map<String, String> CUSTOM_SITE_MAP;

    static {
        ArrayMap arrayMap = new ArrayMap();
        CUSTOM_SITE_MAP = arrayMap;
        arrayMap.put(ScreenLockSettings.class.getName(), SecuritySettings.class.getName());
        arrayMap.put(WallpaperSuggestionActivity.class.getName(), DisplaySettings.class.getName());
        arrayMap.put(WifiSettings.class.getName(), NetworkDashboardFragment.class.getName());
        arrayMap.put(PowerUsageAdvanced.class.getName(), PowerUsageSummary.class.getName());
        arrayMap.put(RecentLocationAccessSeeAllFragment.class.getName(), LocationSettings.class.getName());
        arrayMap.put(UsbDetailsFragment.class.getName(), ConnectedDeviceDashboardFragment.class.getName());
        arrayMap.put(UserBackupSettingsActivity.class.getName(), SystemDashboardFragment.class.getName());
        arrayMap.put(ZenModeBlockedEffectsSettings.class.getName(), ZenModeRestrictNotificationsSettings.class.getName());
        arrayMap.put(GestureNavigationSettingsFragment.class.getName(), SystemNavigationGestureSettings.class.getName());
    }
}
