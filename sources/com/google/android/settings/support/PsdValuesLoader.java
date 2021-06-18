package com.google.android.settings.support;

import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.SystemProperties;
import android.os.UserManager;
import android.os.storage.StorageManager;
import android.os.storage.VolumeInfo;
import android.provider.Settings;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.ims.ImsMmTelManager;
import android.util.Log;
import com.android.settings.connecteddevice.usb.UsbBackend;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.utils.AsyncLoader;
import com.google.android.settings.experiments.GServicesProxy;
import com.google.android.settings.support.PsdBundle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class PsdValuesLoader extends AsyncLoader<PsdBundle> {
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
    private static final boolean DEBUG = Log.isLoggable("PsdValuesLoader", 3);
    static final String NOE_ALARM_MAX_VOLUME = "noe_alarm_max_volume";
    static final String NOE_BATTERY_LEVEL = "noe_battery_level";
    static final String NOE_BATTERY_SCALE = "noe_battery_scale";
    static final String NOE_BATTERY_STATUS = "noe_battery_status";
    static final String NOE_BRIGHTNESS_LEVEL = "noe_brightness_level";
    static final String NOE_CAMERA_VERSION_CODE = "noe_camera_version_code";
    static final String NOE_CAMERA_VERSION_NAME = "noe_camera_version_name";
    static final String NOE_CELLULAR_DBM = "noe_cellular_dbm";
    static final String NOE_CELLULAR_RADIO_TYPE = "noe_cellular_radio_type";
    static final String NOE_CELLULAR_STRENGTH = "noe_cellular_strength";
    static final String NOE_CHARGING_STATUS = "noe_charging_status";
    static final String NOE_DEVICE_ACTIVATION_TIME = "noe_device_activation_time";
    static final String NOE_IMEI = "noe_imei";
    static final String NOE_LOCATION_MODE = "noe_location_mode";
    static final String NOE_MUSIC_MAX_VOLUME = "noe_music_max_volume";
    static final String NOE_NOTIFICATION_MAX_VOLUME = "noe_notification_max_volume";
    static final String NOE_PREVIOUS_OS = "noe_previous_os";
    static final String NOE_RINGER_MAX_VOLUME = "noe_ringer_max_volume";
    static final String NOE_SYSTEM_MAX_VOLUME = "noe_system_max_volume";
    static final String NOE_USB_DATA_ROLE = "noe_usb_data_role";
    static final String NOE_USB_FUNCTIONS = "noe_usb_functions";
    static final String NOE_USB_POWER_ROLE = "noe_usb_power_role";
    static final String NOE_VOICE_CALL_MAX_VOLUME = "noe_voice_call_max_volume";
    static final String NOE_WEAR_VERSION_CODE = "noe_wear_version_code";
    static final String NOE_WEAR_VERSION_NAME = "noe_wear_version_name";
    static final String NOE_WIFI_AVAILABLE = "noe_wifi_available";
    static final String NOE_WIFI_CONNECTED = "noe_wifi_connected";
    static final String NOE_WIFI_SECURITY_KEY_VALID = "noe_wifi_security_key_valid";
    static final String NOE_WIFI_SPEED = "noe_wifi_speed";
    static final String NOE_WIFI_STRENGTH = "noe_wifi_strength";
    static UserManager sUserManager;

    public static PsdBundle makePsdBundle(Context context, int i) {
        String str;
        long j;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        String str7;
        String str8;
        String str9;
        String str10;
        String str11;
        String str12;
        String str13;
        String[] strArr;
        String str14;
        String str15;
        String str16;
        String str17;
        String str18;
        String str19;
        String str20;
        String str21;
        String str22;
        String str23;
        String str24;
        String str25;
        String str26;
        String str27;
        String[] strArr2;
        String str28;
        String[] strArr3;
        String str29;
        String str30;
        int i2;
        String str31;
        int i3;
        String str32;
        String str33;
        Context context2 = context;
        int i4 = i;
        long currentTimeMillis = System.currentTimeMillis();
        boolean z = (i4 == 0 || i4 == 2) ? true : DEBUG;
        boolean z2 = (i4 == 0 || 1 == i4) ? true : DEBUG;
        ContentResolver contentResolver = context.getContentResolver();
        PackageManager packageManager = context.getPackageManager();
        TelephonyManager telephonyManager = (TelephonyManager) context2.getSystemService(TelephonyManager.class);
        WifiManager wifiManager = (WifiManager) context2.getSystemService("wifi");
        AudioManager audioManager = (AudioManager) context2.getSystemService("audio");
        Intent registerReceiver = context2.registerReceiver((BroadcastReceiver) null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        String string = Settings.System.getString(contentResolver, "screen_brightness_mode");
        String string2 = Settings.System.getString(contentResolver, "screen_off_timeout");
        LocationManager locationManager = (LocationManager) context2.getSystemService("location");
        if (locationManager != null) {
            str = String.valueOf(locationManager.isProviderEnabled("gps"));
        } else {
            str = null;
        }
        if (telephonyManager != null) {
            String networkOperatorName = telephonyManager.getNetworkOperatorName();
            str5 = String.valueOf(telephonyManager.getSimState());
            str4 = String.valueOf(telephonyManager.getNetworkType());
            str3 = String.valueOf(telephonyManager.getPhoneType());
            int defaultSubscriptionId = SubscriptionManager.getDefaultSubscriptionId();
            if (SubscriptionManager.isValidSubscriptionId(defaultSubscriptionId)) {
                str33 = networkOperatorName;
                j = currentTimeMillis;
                try {
                    str7 = String.valueOf(ImsMmTelManager.createForSubscriptionId(defaultSubscriptionId).isAvailable(1, 0));
                    str2 = string2;
                    str6 = str33;
                } catch (Exception unused) {
                }
            } else {
                j = currentTimeMillis;
                str33 = networkOperatorName;
                str2 = string2;
                str6 = str33;
                str7 = null;
            }
        } else {
            j = currentTimeMillis;
            str2 = string2;
            str7 = null;
            str6 = null;
            str5 = null;
            str4 = null;
            str3 = null;
        }
        String str34 = string;
        String valueOf = String.valueOf(GServicesProxy.getLong(contentResolver, "android_id", 0));
        String str35 = SystemProperties.get("persist.sys.timezone");
        if (audioManager != null) {
            String valueOf2 = String.valueOf(audioManager.getStreamMaxVolume(1));
            str12 = String.valueOf(audioManager.getStreamMaxVolume(4));
            str11 = String.valueOf(audioManager.getStreamMaxVolume(3));
            str10 = String.valueOf(audioManager.getStreamMaxVolume(2));
            str9 = String.valueOf(audioManager.getStreamMaxVolume(0));
            str8 = valueOf2;
            str13 = String.valueOf(audioManager.getStreamMaxVolume(5));
        } else {
            str13 = null;
            str12 = null;
            str11 = null;
            str10 = null;
            str9 = null;
            str8 = null;
        }
        if (z) {
            String string3 = Settings.System.getString(contentResolver, "screen_brightness");
            if (telephonyManager != null) {
                String imei = telephonyManager.getImei();
                String[] cellularStrength = getCellularStrength(telephonyManager);
                String str36 = cellularStrength[0];
                str20 = string3;
                i2 = 1;
                String str37 = cellularStrength[1];
                str31 = cellularStrength[2];
                str19 = str36;
                str21 = imei;
                str22 = str37;
            } else {
                str20 = string3;
                i2 = 1;
                str31 = null;
                str22 = null;
                str21 = null;
                str19 = null;
            }
            str18 = dumpBatteryStats(context);
            if (str18 == null || !DEBUG) {
                str16 = str;
                str17 = str31;
                i3 = 0;
            } else {
                str17 = str31;
                Object[] objArr = new Object[i2];
                str16 = str;
                i3 = 0;
                objArr[0] = Integer.valueOf(str18.length());
                Log.d("PsdValuesLoader", String.format("Dump battery stats, length: %d", objArr));
            }
            if (audioManager != null) {
                str26 = String.valueOf(audioManager.getStreamVolume(i3));
                String valueOf3 = String.valueOf(audioManager.getStreamVolume(5));
                str32 = String.valueOf(audioManager.getStreamVolume(1));
                str25 = valueOf3;
                str24 = String.valueOf(audioManager.getStreamVolume(4));
                str23 = String.valueOf(audioManager.getStreamVolume(3));
                String valueOf4 = String.valueOf(audioManager.getStreamVolume(2));
                str27 = String.valueOf(audioManager.isWiredHeadsetOn());
                str15 = valueOf4;
            } else {
                str32 = null;
                str27 = null;
                str26 = null;
                str25 = null;
                str24 = null;
                str23 = null;
                str15 = null;
            }
            str14 = str32;
            str28 = String.valueOf(registerReceiver.getIntExtra("plugged", -1));
            strArr2 = getUsbMode(context);
            strArr = getWifiExtras(wifiManager);
        } else {
            str16 = str;
            str28 = null;
            strArr2 = null;
            str27 = null;
            str26 = null;
            str25 = null;
            str24 = null;
            str23 = null;
            str22 = null;
            str21 = null;
            str20 = null;
            str19 = null;
            str18 = null;
            str17 = null;
            str15 = null;
            str14 = null;
            strArr = null;
        }
        String str38 = "";
        if (z2) {
            long deviceAgeInDays = getDeviceAgeInDays(contentResolver);
            str30 = String.valueOf(deviceAgeInDays);
            String str39 = deviceAgeInDays <= 30 ? "1" : "0";
            strArr3 = strArr2;
            str29 = str39;
        } else {
            strArr3 = strArr2;
            str30 = str38;
            str29 = str30;
        }
        String[] wifiStatus = getWifiStatus(context);
        String str40 = str28;
        String str41 = str27;
        String valueOf5 = String.valueOf(registerReceiver.getIntExtra("scale", -1));
        String str42 = str26;
        String valueOf6 = String.valueOf(registerReceiver.getIntExtra("level", -1));
        String str43 = str29;
        String valueOf7 = String.valueOf(Settings.Secure.getInt(contentResolver, "location_mode", 0));
        String[] readBatteryInfo = readBatteryInfo(registerReceiver, i4);
        String[] readStorage = readStorage(context2, z);
        String[] readRam = readRam(context2, z);
        boolean z3 = z;
        String[] readVersionInfo = readVersionInfo(packageManager, "com.google.android.GoogleCamera");
        String str44 = str30;
        String[] readVersionInfo2 = readVersionInfo(packageManager, "com.google.android.wearable.app");
        String[] readVersionInfo3 = readVersionInfo(packageManager, "com.google.android.googlequicksearchbox");
        String[] strArr4 = readVersionInfo2;
        String[] readVersionInfo4 = readVersionInfo(packageManager, "com.google.android.gms");
        String[] strArr5 = readVersionInfo;
        String[] readVersionInfo5 = readVersionInfo(packageManager, "com.android.vending");
        String string4 = GServicesProxy.getString(contentResolver, "update_url", str38);
        String[] readBluetoothInfo = readBluetoothInfo(context);
        String[] strArr6 = readVersionInfo5;
        String[] strArr7 = readVersionInfo4;
        String str45 = string4;
        PsdBundle.Builder addSignal = new PsdBundle.Builder(context2, GServicesProxy.getLong(contentResolver, "settingsgoogle:psd_values_size_limit_bytes", 400000)).addSignal("noe_display_name", Build.DISPLAY).addSignal("noe_type", Build.TYPE).addSignal("noe_product", Build.PRODUCT).addSignal("noe_sdk_int", String.valueOf(Build.VERSION.SDK_INT)).addSignal("noe_incremental", Build.VERSION.INCREMENTAL).addSignal("noe_codename", Build.VERSION.CODENAME).addSignal("noe_board", Build.BOARD).addSignal("noe_brand", Build.BRAND).addSignal("noe_fingerprint", Build.FINGERPRINT).addSignal("noe_base_os", Build.VERSION.BASE_OS).addSignal("noe_preview_sdk_int", String.valueOf(Build.VERSION.PREVIEW_SDK_INT)).addSignal("noe_security_patch", Build.VERSION.SECURITY_PATCH).addSignal("noe_dump_datetime", DATE_FORMATTER.format(new Date()));
        if (str35 == null) {
            str35 = str38;
        }
        PsdBundle.Builder addSignal2 = addSignal.addSignal("noe_timezone", str35).addSignal("noe_bootloader", Build.BOOTLOADER).addSignal("noe_radio", Build.getRadioVersion());
        if (str6 == null) {
            str6 = str38;
        }
        PsdBundle.Builder addSignal3 = addSignal2.addSignal("noe_network_operator", str6).addSignal("noe_network_type", str4 == null ? str38 : str4).addSignal("noe_phone_type", str3 == null ? str38 : str3);
        if (str7 == null) {
            str7 = str38;
        }
        PsdBundle.Builder addSignal4 = addSignal3.addSignal("noe_is_volte_available", str7).addSignal("noe_build_id", Build.ID).addSignal("noe_decimal_gsf_id", valueOf).addSignal("noe_gps_enabled", str16).addSignal("noe_screen_brightness_mode", str34 == null ? str38 : str34).addSignal(NOE_SYSTEM_MAX_VOLUME, str8 == null ? str38 : str8).addSignal(NOE_ALARM_MAX_VOLUME, str12 == null ? str38 : str12).addSignal(NOE_VOICE_CALL_MAX_VOLUME, str9 == null ? str38 : str9).addSignal(NOE_NOTIFICATION_MAX_VOLUME, str13 == null ? str38 : str13).addSignal(NOE_MUSIC_MAX_VOLUME, str11 == null ? str38 : str11).addSignal(NOE_RINGER_MAX_VOLUME, str10 == null ? str38 : str10).addSignal("noe_display_timeout", str2 == null ? str38 : str2).addSignal("noe_sim_state", str5 == null ? str38 : str5).addSignal("noe_good_reboots_last_day", str38).addSignal("noe_bad_reboots_last_day", str38).addSignal("noe_good_reboots_last_week", str38).addSignal("noe_bad_reboots_last_week", str38).addSignal(NOE_LOCATION_MODE, valueOf7).addSignal("noe_wifi_state", readWifiState(context)).addSignal(NOE_WIFI_SECURITY_KEY_VALID, wifiStatus[1].equals("true") ? wifiStatus[1] : readWifiSecurityKeyValid(context)).addSignal("noe_battery_health", readBatteryInfo[0]).addSignal(NOE_BATTERY_LEVEL, valueOf6).addSignal(NOE_BATTERY_SCALE, valueOf5).addSignal("noe_battery_voltage", readBatteryInfo[2]).addSignal("noe_battery_present", readBatteryInfo[3]).addSignal("noe_storage_available", readStorage[0]).addSignal("noe_storage_total", readStorage[1]).addSignal("noe_ram_available", readRam[0]).addSignal("noe_ram_total", readRam[1]).addSignal("noe_google_app_version_code", readVersionInfo3[0]).addSignal("noe_google_app_version_name", readVersionInfo3[1]).addSignal("noe_google_play_services_version_code", strArr7[0]).addSignal("noe_google_play_services_version_name", strArr7[1]).addSignal("noe_google_play_store_version_code", strArr6[0]).addSignal("noe_google_play_store_version_name", strArr6[1]).addSignal("noe_update_url", str45).addSignal("noe_num_bluetooth_connections", readBluetoothInfo[0]).addSignal("noe_bluetooth_enabled", readBluetoothInfo[1]).addSignal("noe_is_rooted", String.valueOf(isDeviceRooted())).addSignal(NOE_CAMERA_VERSION_CODE, strArr5[0]).addSignal(NOE_CAMERA_VERSION_NAME, strArr5[1]).addSignal(NOE_WEAR_VERSION_CODE, strArr4[0]).addSignal(NOE_WEAR_VERSION_NAME, strArr4[1]);
        int i5 = i;
        if (2 == i5) {
            addSignal4.addPairedBluetoothDevices().addConnectedBluetoothDevicesSignals().addTopBatteryApps();
        }
        if (1 == i5) {
            addSignal4.addSignal(NOE_DEVICE_ACTIVATION_TIME, str44);
        }
        if (i5 == 0) {
            addSignal4.addSignal("noe_device_under_thirty", str43);
        }
        if (z3) {
            PsdBundle.Builder addSignal5 = addSignal4.addSignal("noe_voice_call_volume", str42 == null ? str38 : str42).addSignal("noe_notification_volume", str25 == null ? str38 : str25).addSignal("noe_system_volume", str14 == null ? str38 : str14).addSignal(NOE_BATTERY_STATUS, readBatteryInfo[1]).addSignal("noe_alarm_volume", str24 == null ? str38 : str24).addSignal("noe_music_volume", str23 == null ? str38 : str23).addSignal("noe_ringer_volume", str15 == null ? str38 : str15).addSignal(NOE_BRIGHTNESS_LEVEL, str20 == null ? str38 : str20).addSignal("noe_headset_attached", str41 == null ? str38 : str41).addSignal("noe_battery_stats", str18 == null ? str38 : str18).addSignal(NOE_CHARGING_STATUS, str40 == 0 ? str38 : str40).addSignal(NOE_USB_FUNCTIONS, strArr3[0]).addSignal(NOE_USB_POWER_ROLE, strArr3[1]).addSignal(NOE_USB_DATA_ROLE, strArr3[2]).addSignal(NOE_IMEI, str21 == null ? str38 : str21).addSignal(NOE_CELLULAR_RADIO_TYPE, str19 == null ? str38 : str19).addSignal(NOE_CELLULAR_STRENGTH, str22 == null ? str38 : str22);
            if (str17 != null) {
                str38 = str17;
            }
            addSignal5.addSignal(NOE_CELLULAR_DBM, str38).addSignal(NOE_WIFI_AVAILABLE, wifiStatus[0]).addSignal(NOE_WIFI_CONNECTED, wifiStatus[1]).addSignal(NOE_WIFI_SPEED, strArr[0]).addSignal(NOE_WIFI_STRENGTH, strArr[1]);
            addSignal4.addBatteryAnomalyApps().addTelephonyTroubleshooterDiagnosticSignals().addTelephonyTroubleshooterStatisticsSignals();
        }
        if (context.getResources().getBoolean(17891328)) {
            addSignal4.addSignal("genie-eng:app_pkg_name", "com.google.android.settings.gphone");
        }
        FeatureFactory.getFactory(context).getMetricsFeatureProvider().action(0, 1019, 0, "latency", (int) (System.currentTimeMillis() - j));
        return addSignal4.build();
    }

    static long getDeviceAgeInDays(ContentResolver contentResolver) {
        return roundToDays(System.currentTimeMillis() - GServicesProxy.getLong(contentResolver, "device_registration_time", -1));
    }

    private static long roundToDays(long j) {
        return TimeUnit.MILLISECONDS.toDays(j);
    }

    private static String[] getUsbMode(Context context) {
        UsbBackend usbBackend;
        UserManager userManager = sUserManager;
        if (userManager == null) {
            usbBackend = new UsbBackend(context);
        } else {
            usbBackend = new UsbBackend(context, userManager);
        }
        return new String[]{String.valueOf(usbBackend.getCurrentFunctions()), String.valueOf(usbBackend.getPowerRole()), String.valueOf(usbBackend.getDataRole())};
    }

    private static String[] getCellularStrength(TelephonyManager telephonyManager) {
        List<CellInfo> allCellInfo = telephonyManager.getAllCellInfo();
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        StringBuilder sb3 = new StringBuilder();
        if (allCellInfo == null) {
            return new String[]{"", "", ""};
        }
        for (int i = 0; i < allCellInfo.size(); i++) {
            CellInfo cellInfo = allCellInfo.get(i);
            int i2 = -1;
            int i3 = Integer.MIN_VALUE;
            sb.append(cellInfo.getClass().getSimpleName());
            sb.append(',');
            if (cellInfo instanceof CellInfoGsm) {
                CellSignalStrengthGsm cellSignalStrength = ((CellInfoGsm) cellInfo).getCellSignalStrength();
                i2 = cellSignalStrength.getLevel();
                i3 = cellSignalStrength.getDbm();
            } else if (cellInfo instanceof CellInfoCdma) {
                CellSignalStrengthCdma cellSignalStrength2 = ((CellInfoCdma) cellInfo).getCellSignalStrength();
                i2 = cellSignalStrength2.getLevel();
                i3 = cellSignalStrength2.getDbm();
            } else if (cellInfo instanceof CellInfoLte) {
                CellSignalStrengthLte cellSignalStrength3 = ((CellInfoLte) cellInfo).getCellSignalStrength();
                i2 = cellSignalStrength3.getLevel();
                i3 = cellSignalStrength3.getDbm();
            } else if (cellInfo instanceof CellInfoWcdma) {
                CellSignalStrengthWcdma cellSignalStrength4 = ((CellInfoWcdma) cellInfo).getCellSignalStrength();
                i2 = cellSignalStrength4.getLevel();
                i3 = cellSignalStrength4.getDbm();
            }
            sb2.append(i2);
            sb2.append(',');
            sb3.append(i3);
            sb3.append(',');
        }
        if (allCellInfo.size() > 0) {
            sb.deleteCharAt(sb.length() - 1);
            sb2.deleteCharAt(sb2.length() - 1);
            sb3.deleteCharAt(sb3.length() - 1);
        }
        return new String[]{sb.toString(), sb2.toString(), sb3.toString()};
    }

    private static long roundToMB(long j) {
        return Math.round(((double) j) / 1000000.0d);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:27:0x005d, code lost:
        if (r6 != null) goto L_0x005f;
     */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0067  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected static java.lang.String dumpBatteryStats(android.content.Context r6) {
        /*
            java.lang.String r0 = "batterystats"
            android.content.ContentResolver r1 = r6.getContentResolver()
            java.lang.String r2 = "settingsgoogle:psd_battery_stats"
            r3 = 0
            boolean r1 = com.google.android.settings.experiments.GServicesProxy.getBoolean(r1, r2, r3)
            java.lang.String r2 = "PsdValuesLoader"
            r3 = 0
            if (r1 != 0) goto L_0x001c
            boolean r6 = DEBUG
            if (r6 == 0) goto L_0x001b
            java.lang.String r6 = "Not collecting battery_stats, skip."
            android.util.Log.d(r2, r6)
        L_0x001b:
            return r3
        L_0x001c:
            java.io.File r6 = r6.getFilesDir()     // Catch:{ IOException -> 0x0056, all -> 0x0054 }
            java.io.File r6 = java.io.File.createTempFile(r0, r3, r6)     // Catch:{ IOException -> 0x0056, all -> 0x0054 }
            java.io.FileOutputStream r1 = new java.io.FileOutputStream     // Catch:{ IOException -> 0x0052 }
            r1.<init>(r6)     // Catch:{ IOException -> 0x0052 }
            java.io.FileDescriptor r4 = r1.getFD()     // Catch:{ IOException -> 0x0052 }
            java.lang.String r5 = "-c"
            java.lang.String[] r5 = new java.lang.String[]{r5}     // Catch:{ IOException -> 0x0052 }
            boolean r0 = android.os.Debug.dumpService(r0, r4, r5)     // Catch:{ IOException -> 0x0052 }
            if (r0 != 0) goto L_0x0045
            boolean r0 = DEBUG     // Catch:{ IOException -> 0x0052 }
            if (r0 == 0) goto L_0x0042
            java.lang.String r0 = "Failed to dump battery stats."
            android.util.Log.d(r2, r0)     // Catch:{ IOException -> 0x0052 }
        L_0x0042:
            if (r6 == 0) goto L_0x0062
            goto L_0x005f
        L_0x0045:
            r1.close()     // Catch:{ IOException -> 0x0052 }
            java.lang.String r0 = readFile(r6)     // Catch:{ IOException -> 0x0052 }
            if (r6 == 0) goto L_0x0051
            r6.delete()
        L_0x0051:
            return r0
        L_0x0052:
            r0 = move-exception
            goto L_0x0058
        L_0x0054:
            r0 = move-exception
            goto L_0x0065
        L_0x0056:
            r0 = move-exception
            r6 = r3
        L_0x0058:
            java.lang.String r1 = "IOException while dumping batterystats"
            android.util.Log.e(r2, r1, r0)     // Catch:{ all -> 0x0063 }
            if (r6 == 0) goto L_0x0062
        L_0x005f:
            r6.delete()
        L_0x0062:
            return r3
        L_0x0063:
            r0 = move-exception
            r3 = r6
        L_0x0065:
            if (r3 == 0) goto L_0x006a
            r3.delete()
        L_0x006a:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.settings.support.PsdValuesLoader.dumpBatteryStats(android.content.Context):java.lang.String");
    }

    private static String[] readBatteryInfo(Intent intent, int i) {
        String[] strArr = {"", "", "", ""};
        strArr[0] = String.valueOf(intent.getIntExtra("health", -1));
        if (2 == i || i == 0) {
            strArr[1] = String.valueOf(intent.getIntExtra("status", -1));
        }
        strArr[2] = String.valueOf(intent.getIntExtra("voltage", -1));
        strArr[3] = String.valueOf(intent.getBooleanExtra("present", DEBUG));
        return strArr;
    }

    private static String[] readBluetoothInfo(Context context) {
        BluetoothAdapter adapter;
        String[] strArr = {"", ""};
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService("bluetooth");
        if (!(bluetoothManager == null || (adapter = bluetoothManager.getAdapter()) == null)) {
            int i = 0;
            for (BluetoothDevice isConnected : adapter.getBondedDevices()) {
                if (isConnected.isConnected()) {
                    i++;
                }
            }
            strArr[0] = String.valueOf(i);
            strArr[1] = String.valueOf(adapter.isEnabled());
        }
        Log.d("PsdValuesLoader", "Bluetooth: " + strArr[0]);
        return strArr;
    }

    private static String readFile(File file) throws IOException {
        return readInputStream(new FileInputStream(file));
    }

    private static String[] readVersionInfo(PackageManager packageManager, String str) {
        String[] strArr = new String[2];
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(str, 0);
            if (packageInfo != null) {
                strArr[0] = String.valueOf(packageInfo.versionCode);
                strArr[1] = packageInfo.versionName;
            } else {
                strArr[0] = "";
                strArr[1] = "";
            }
        } catch (PackageManager.NameNotFoundException unused) {
            Log.e("PsdValuesLoader", "Failed to find package");
            strArr[0] = "";
            strArr[1] = "";
        }
        return strArr;
    }

    /* JADX INFO: finally extract failed */
    private static String readInputStream(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        while (true) {
            try {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    sb.append(readLine);
                    sb.append(10);
                } else {
                    bufferedReader.close();
                    return sb.toString();
                }
            } catch (Throwable th) {
                bufferedReader.close();
                throw th;
            }
        }
    }

    protected static String[] readRam(Context context, boolean z) {
        String[] strArr = new String[2];
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        if (activityManager != null) {
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            activityManager.getMemoryInfo(memoryInfo);
            long j = memoryInfo.availMem;
            long j2 = memoryInfo.totalMem;
            if (!z) {
                j = roundToMB(j);
                j2 = roundToMB(j2);
            }
            strArr[0] = String.valueOf(j);
            strArr[1] = String.valueOf(j2);
        } else {
            strArr[0] = "";
            strArr[1] = "";
        }
        return strArr;
    }

    protected static String[] readStorage(Context context, boolean z) {
        long j;
        StorageManager storageManager = (StorageManager) context.getSystemService(StorageManager.class);
        long j2 = 0;
        if (storageManager != null) {
            long j3 = 0;
            for (VolumeInfo volumeInfo : storageManager.getVolumes()) {
                if (volumeInfo.getType() == 1 && volumeInfo.isMountedReadable()) {
                    j2 += volumeInfo.getPath().getFreeSpace();
                    j3 += volumeInfo.getPath().getTotalSpace();
                }
            }
            if (!z) {
                j2 = roundToMB(j2);
                j = roundToMB(j3);
            } else {
                j = j3;
            }
        } else {
            j = 0;
        }
        return new String[]{String.valueOf(j2), String.valueOf(j)};
    }

    private static String[] getWifiExtras(WifiManager wifiManager) {
        WifiInfo connectionInfo;
        if (wifiManager == null || (connectionInfo = wifiManager.getConnectionInfo()) == null) {
            return new String[]{"", ""};
        }
        return new String[]{String.valueOf(connectionInfo.getLinkSpeed()), String.valueOf(connectionInfo.getRssi())};
    }

    private static String[] getWifiStatus(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager != null) {
            boolean z = false;
            boolean z2 = false;
            boolean z3 = false;
            for (Network network : connectivityManager.getAllNetworks()) {
                NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);
                if (network != null && networkInfo.getType() == 1) {
                    if (NetworkInfo.State.CONNECTED.equals(networkInfo.getState())) {
                        z3 = true;
                    }
                    if (networkInfo.isAvailable()) {
                        z = true;
                        z2 = true;
                    } else {
                        z = true;
                    }
                }
            }
            if (z) {
                return new String[]{String.valueOf(z2), String.valueOf(z3)};
            }
        }
        return new String[]{"", ""};
    }

    private static String readWifiSecurityKeyValid(Context context) {
        List<WifiConfiguration> configuredNetworks;
        WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
        if (wifiManager == null || (configuredNetworks = wifiManager.getConfiguredNetworks()) == null) {
            return "";
        }
        for (WifiConfiguration networkSelectionStatus : configuredNetworks) {
            if (networkSelectionStatus.getNetworkSelectionStatus().getNetworkSelectionDisableReason() == 2) {
                return String.valueOf(DEBUG);
            }
        }
        return "";
    }

    private static String readWifiState(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
        if (wifiManager == null) {
            return "";
        }
        return String.valueOf(wifiManager.getWifiState());
    }

    private static boolean isDeviceRooted() {
        if (isDevOrTestKeys() || hasModifiedSystemProperties() || hasSuperUserBinary()) {
            return true;
        }
        return DEBUG;
    }

    private static boolean hasSuperUserBinary() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("/sbin");
        arrayList.add("/system/bin");
        arrayList.add("/system/xbin");
        arrayList.add("/data/local/xbin");
        arrayList.add("/data/local/bin");
        arrayList.add("/system/sd/xbin");
        arrayList.add("/system/bin/failsafe");
        arrayList.add("/data/local");
        arrayList.addAll(Arrays.asList(System.getenv("PATH").split(":")));
        for (String file : arrayList) {
            File file2 = new File(file, "su");
            if (file2.exists() && file2.canExecute()) {
                return true;
            }
        }
        return DEBUG;
    }

    private static boolean isDevOrTestKeys() {
        String str = Build.TAGS;
        if (str == null || (!str.contains("test-keys") && !str.contains("dev-keys"))) {
            return DEBUG;
        }
        return true;
    }

    private static boolean hasModifiedSystemProperties() {
        if (SystemProperties.get("ro.debuggable").equals("1") || SystemProperties.get("ro.secure").equals("0")) {
            return true;
        }
        return DEBUG;
    }
}
