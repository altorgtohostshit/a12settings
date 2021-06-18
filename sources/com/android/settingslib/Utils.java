package com.android.settingslib;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.UserInfo;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.NetworkCapabilities;
import android.net.TetheringManager;
import android.net.vcn.VcnTransportInfo;
import android.net.wifi.WifiInfo;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.UserIcons;
import com.android.launcher3.icons.IconFactory;
import com.android.settingslib.drawable.UserIconDrawable;
import com.android.settingslib.fuelgauge.BatteryStatus;
import java.text.NumberFormat;

public class Utils {
    static final int[] SHOW_X_WIFI_PIE = {R$drawable.ic_show_x_wifi_signal_0, R$drawable.ic_show_x_wifi_signal_1, R$drawable.ic_show_x_wifi_signal_2, R$drawable.ic_show_x_wifi_signal_3, R$drawable.ic_show_x_wifi_signal_4};
    @VisibleForTesting
    static final String STORAGE_MANAGER_ENABLED_PROPERTY = "ro.storage_manager.enabled";
    static final int[] WIFI_PIE = {17302883, 17302884, 17302885, 17302886, 17302887};
    private static String sPermissionControllerPackageName;
    private static String sServicesSystemSharedLibPackageName;
    private static String sSharedSystemSharedLibPackageName;
    private static Signature[] sSystemSignature;

    public static void updateLocationEnabled(Context context, boolean z, int i, int i2) {
        Settings.Secure.putIntForUser(context.getContentResolver(), "location_changer", i2, i);
        ((LocationManager) context.getSystemService(LocationManager.class)).setLocationEnabledForUser(z, UserHandle.of(i));
    }

    public static int getTetheringLabel(TetheringManager tetheringManager) {
        String[] tetherableUsbRegexs = tetheringManager.getTetherableUsbRegexs();
        String[] tetherableWifiRegexs = tetheringManager.getTetherableWifiRegexs();
        String[] tetherableBluetoothRegexs = tetheringManager.getTetherableBluetoothRegexs();
        boolean z = true;
        boolean z2 = tetherableUsbRegexs.length != 0;
        boolean z3 = tetherableWifiRegexs.length != 0;
        if (tetherableBluetoothRegexs.length == 0) {
            z = false;
        }
        if (z3 && z2 && z) {
            return R$string.tether_settings_title_all;
        }
        if (z3 && z2) {
            return R$string.tether_settings_title_all;
        }
        if (z3 && z) {
            return R$string.tether_settings_title_all;
        }
        if (z3) {
            return R$string.tether_settings_title_wifi;
        }
        if (z2 && z) {
            return R$string.tether_settings_title_usb_bluetooth;
        }
        if (z2) {
            return R$string.tether_settings_title_usb;
        }
        return R$string.tether_settings_title_bluetooth;
    }

    public static String getUserLabel(Context context, UserInfo userInfo) {
        String str = userInfo != null ? userInfo.name : null;
        if (userInfo.isManagedProfile()) {
            return context.getString(R$string.managed_user_title);
        }
        if (userInfo.isGuest()) {
            str = context.getString(R$string.user_guest);
        }
        if (str == null) {
            str = Integer.toString(userInfo.id);
        }
        return context.getResources().getString(R$string.running_process_item_user_label, new Object[]{str});
    }

    public static Drawable getUserIcon(Context context, UserManager userManager, UserInfo userInfo) {
        Bitmap userIcon;
        int sizeForList = UserIconDrawable.getSizeForList(context);
        if (userInfo.isManagedProfile()) {
            Drawable managedUserDrawable = UserIconDrawable.getManagedUserDrawable(context);
            managedUserDrawable.setBounds(0, 0, sizeForList, sizeForList);
            return managedUserDrawable;
        } else if (userInfo.iconPath == null || (userIcon = userManager.getUserIcon(userInfo.id)) == null) {
            return new UserIconDrawable(sizeForList).setIconDrawable(UserIcons.getDefaultUserIcon(context.getResources(), userInfo.id, false)).bake();
        } else {
            return new UserIconDrawable(sizeForList).setIcon(userIcon).bake();
        }
    }

    public static String formatPercentage(double d, boolean z) {
        return formatPercentage(z ? Math.round((float) d) : (int) d);
    }

    public static String formatPercentage(long j, long j2) {
        return formatPercentage(((double) j) / ((double) j2));
    }

    public static String formatPercentage(int i) {
        return formatPercentage(((double) i) / 100.0d);
    }

    public static String formatPercentage(double d) {
        return NumberFormat.getPercentInstance().format(d);
    }

    public static int getBatteryLevel(Intent intent) {
        return (intent.getIntExtra("level", 0) * 100) / intent.getIntExtra("scale", 100);
    }

    public static String getBatteryStatus(Context context, Intent intent) {
        int intExtra = intent.getIntExtra("status", 1);
        Resources resources = context.getResources();
        String string = resources.getString(R$string.battery_info_status_unknown);
        BatteryStatus batteryStatus = new BatteryStatus(intent);
        if (batteryStatus.isCharged()) {
            return resources.getString(R$string.battery_info_status_full);
        }
        if (intExtra == 2) {
            if (!batteryStatus.isPluggedInWired()) {
                return resources.getString(R$string.battery_info_status_charging_wireless);
            }
            int chargingSpeed = batteryStatus.getChargingSpeed(context);
            if (chargingSpeed != 0) {
                return chargingSpeed != 2 ? resources.getString(R$string.battery_info_status_charging) : resources.getString(R$string.battery_info_status_charging_fast);
            }
            return resources.getString(R$string.battery_info_status_charging_slow);
        } else if (intExtra == 3) {
            return resources.getString(R$string.battery_info_status_discharging);
        } else {
            return intExtra == 4 ? resources.getString(R$string.battery_info_status_not_charging) : string;
        }
    }

    public static ColorStateList getColorAccent(Context context) {
        return getColorAttr(context, 16843829);
    }

    public static int getColorAccentDefaultColor(Context context) {
        return getColorAttrDefaultColor(context, 16843829);
    }

    public static int getColorErrorDefaultColor(Context context) {
        return getColorAttrDefaultColor(context, 16844099);
    }

    public static int getColorStateListDefaultColor(Context context, int i) {
        return context.getResources().getColorStateList(i, context.getTheme()).getDefaultColor();
    }

    public static int getDisabled(Context context, int i) {
        return applyAlphaAttr(context, 16842803, i);
    }

    public static int applyAlphaAttr(Context context, int i, int i2) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{i});
        float f = obtainStyledAttributes.getFloat(0, 0.0f);
        obtainStyledAttributes.recycle();
        return applyAlpha(f, i2);
    }

    public static int applyAlpha(float f, int i) {
        return Color.argb((int) (f * ((float) Color.alpha(i))), Color.red(i), Color.green(i), Color.blue(i));
    }

    public static int getColorAttrDefaultColor(Context context, int i) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{i});
        int color = obtainStyledAttributes.getColor(0, 0);
        obtainStyledAttributes.recycle();
        return color;
    }

    public static ColorStateList getColorAttr(Context context, int i) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{i});
        try {
            return obtainStyledAttributes.getColorStateList(0);
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    public static ColorMatrix getAlphaInvariantColorMatrixForColor(int i) {
        return new ColorMatrix(new float[]{0.0f, 0.0f, 0.0f, 0.0f, (float) Color.red(i), 0.0f, 0.0f, 0.0f, 0.0f, (float) Color.green(i), 0.0f, 0.0f, 0.0f, 0.0f, (float) Color.blue(i), 0.0f, 0.0f, 0.0f, 1.0f, 0.0f});
    }

    public static ColorFilter getAlphaInvariantColorFilterForColor(int i) {
        return new ColorMatrixColorFilter(getAlphaInvariantColorMatrixForColor(i));
    }

    public static boolean isSystemPackage(Resources resources, PackageManager packageManager, PackageInfo packageInfo) {
        if (sSystemSignature == null) {
            sSystemSignature = new Signature[]{getSystemSignature(packageManager)};
        }
        if (sPermissionControllerPackageName == null) {
            sPermissionControllerPackageName = packageManager.getPermissionControllerPackageName();
        }
        if (sServicesSystemSharedLibPackageName == null) {
            sServicesSystemSharedLibPackageName = packageManager.getServicesSystemSharedLibraryPackageName();
        }
        if (sSharedSystemSharedLibPackageName == null) {
            sSharedSystemSharedLibPackageName = packageManager.getSharedSystemSharedLibraryPackageName();
        }
        Signature[] signatureArr = sSystemSignature;
        if ((signatureArr[0] == null || !signatureArr[0].equals(getFirstSignature(packageInfo))) && !packageInfo.packageName.equals(sPermissionControllerPackageName) && !packageInfo.packageName.equals(sServicesSystemSharedLibPackageName) && !packageInfo.packageName.equals(sSharedSystemSharedLibPackageName) && !packageInfo.packageName.equals("com.android.printspooler") && !isDeviceProvisioningPackage(resources, packageInfo.packageName)) {
            return false;
        }
        return true;
    }

    private static Signature getFirstSignature(PackageInfo packageInfo) {
        Signature[] signatureArr;
        if (packageInfo == null || (signatureArr = packageInfo.signatures) == null || signatureArr.length <= 0) {
            return null;
        }
        return signatureArr[0];
    }

    private static Signature getSystemSignature(PackageManager packageManager) {
        try {
            return getFirstSignature(packageManager.getPackageInfo("android", 64));
        } catch (PackageManager.NameNotFoundException unused) {
            return null;
        }
    }

    public static boolean isDeviceProvisioningPackage(Resources resources, String str) {
        String string = resources.getString(17039916);
        return string != null && string.equals(str);
    }

    public static int getWifiIconResource(int i) {
        return getWifiIconResource(false, i);
    }

    public static int getWifiIconResource(boolean z, int i) {
        if (i >= 0) {
            int[] iArr = WIFI_PIE;
            if (i < iArr.length) {
                return z ? SHOW_X_WIFI_PIE[i] : iArr[i];
            }
        }
        throw new IllegalArgumentException("No Wifi icon found for level: " + i);
    }

    public static int getDefaultStorageManagerDaysToRetain(Resources resources) {
        try {
            return resources.getInteger(17694921);
        } catch (Resources.NotFoundException unused) {
            return 90;
        }
    }

    public static boolean isWifiOnly(Context context) {
        return !((TelephonyManager) context.getSystemService(TelephonyManager.class)).isDataCapable();
    }

    public static boolean isStorageManagerEnabled(Context context) {
        boolean z;
        try {
            z = SystemProperties.getBoolean(STORAGE_MANAGER_ENABLED_PROPERTY, false);
        } catch (Resources.NotFoundException unused) {
            z = false;
        }
        if (Settings.Secure.getInt(context.getContentResolver(), "automatic_storage_manager_enabled", z ? 1 : 0) != 0) {
            return true;
        }
        return false;
    }

    public static boolean isAudioModeOngoingCall(Context context) {
        int mode = ((AudioManager) context.getSystemService(AudioManager.class)).getMode();
        return mode == 1 || mode == 2 || mode == 3;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0004, code lost:
        r3 = getCombinedServiceState(r3);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean isInService(android.telephony.ServiceState r3) {
        /*
            r0 = 0
            if (r3 != 0) goto L_0x0004
            return r0
        L_0x0004:
            int r3 = getCombinedServiceState(r3)
            r1 = 3
            if (r3 == r1) goto L_0x0013
            r1 = 1
            if (r3 == r1) goto L_0x0013
            r2 = 2
            if (r3 != r2) goto L_0x0012
            goto L_0x0013
        L_0x0012:
            return r1
        L_0x0013:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.Utils.isInService(android.telephony.ServiceState):boolean");
    }

    public static int getCombinedServiceState(ServiceState serviceState) {
        if (serviceState == null) {
            return 1;
        }
        int state = serviceState.getState();
        int dataRegistrationState = serviceState.getDataRegistrationState();
        if ((state == 1 || state == 2) && dataRegistrationState == 0 && isNotInIwlan(serviceState)) {
            return 0;
        }
        return state;
    }

    public static Drawable getBadgedIcon(Context context, Drawable drawable, UserHandle userHandle) {
        IconFactory obtain = IconFactory.obtain(context);
        try {
            BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(), obtain.createBadgedIconBitmap(drawable, userHandle, true).icon);
            obtain.close();
            return bitmapDrawable;
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        throw th;
    }

    public static Drawable getBadgedIcon(Context context, ApplicationInfo applicationInfo) {
        return getBadgedIcon(context, applicationInfo.loadUnbadgedIcon(context.getPackageManager()), UserHandle.getUserHandleForUid(applicationInfo.uid));
    }

    private static boolean isNotInIwlan(ServiceState serviceState) {
        NetworkRegistrationInfo networkRegistrationInfo = serviceState.getNetworkRegistrationInfo(2, 2);
        if (networkRegistrationInfo == null) {
            return true;
        }
        return !(networkRegistrationInfo.getRegistrationState() == 1 || networkRegistrationInfo.getRegistrationState() == 5);
    }

    public static WifiInfo tryGetWifiInfoForVcn(NetworkCapabilities networkCapabilities) {
        if (networkCapabilities.getTransportInfo() == null || !(networkCapabilities.getTransportInfo() instanceof VcnTransportInfo)) {
            return null;
        }
        return networkCapabilities.getTransportInfo().getWifiInfo();
    }
}
