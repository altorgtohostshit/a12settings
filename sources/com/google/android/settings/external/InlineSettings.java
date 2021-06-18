package com.google.android.settings.external;

import com.google.android.settings.external.specialcase.AccessibilitySetting;
import com.google.android.settings.external.specialcase.ColorInversionSetting;
import com.google.android.settings.external.specialcase.DataSaverSetting;
import com.google.android.settings.external.specialcase.HotspotSetting;
import com.google.android.settings.external.specialcase.LocationSetting;
import com.google.android.settings.external.specialcase.MagnificationSetting;
import com.google.android.settings.external.specialcase.MobileDataSetting;
import com.google.android.settings.external.specialcase.NfcSetting;
import com.google.android.settings.external.specialcase.NightDisplaySetting;
import com.google.android.settings.external.specialcase.WifiSetting;

public class InlineSettings {
    public static final ColorInversionSetting COLOR_INVERSION_SETTING = new ColorInversionSetting();
    public static final DataSaverSetting DATA_SAVER_SETTING = new DataSaverSetting();
    public static final HotspotSetting HOTSPOT_SETTING = new HotspotSetting();
    public static final LocationSetting LOCATION_SETTING = new LocationSetting();
    public static final MagnificationSetting MAGNIFY_GESTURE_SETTING = new MagnificationSetting("magnify_gesture", "accessibility_display_magnification_enabled");
    public static final MagnificationSetting MAGNIFY_NAVBAR_SETTING = new MagnificationSetting("magnify_navbar", "accessibility_display_magnification_navbar_enabled");
    public static final MobileDataSetting MOBILE_DATA_SETTING = new MobileDataSetting();
    public static final NfcSetting NFC_SETTING = new NfcSetting();
    public static final NightDisplaySetting NIGHTDISPLAY_SETTING = new NightDisplaySetting();
    public static final AccessibilitySetting TALKBACK_SETTING = new AccessibilitySetting("talkback", "com.google.android.marvin.talkback/com.google.android.marvin.talkback.TalkBackService");
    public static final WifiSetting WIFI_SETTING = new WifiSetting();
}
