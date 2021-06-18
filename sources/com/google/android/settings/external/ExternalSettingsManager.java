package com.google.android.settings.external;

import android.content.Context;
import android.net.Uri;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.instrumentation.SharedPreferencesLogger;

public class ExternalSettingsManager {
    public static String getNewSettingValueQueryParameter(Uri uri) {
        return uri.getQueryParameter("new_setting_value");
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.database.Cursor getAccessCursorForSpecialSetting(android.content.Context r2, java.lang.String r3, java.lang.String r4, com.android.settings.slices.SliceData r5) {
        /*
            r4.hashCode()
            int r0 = r4.hashCode()
            r1 = -1
            switch(r0) {
                case -1903108977: goto L_0x012b;
                case -1552376189: goto L_0x011f;
                case -1525260053: goto L_0x0113;
                case -1314247385: goto L_0x0107;
                case -1272444437: goto L_0x00fb;
                case -1001942051: goto L_0x00ef;
                case -610139245: goto L_0x00e3;
                case -608350689: goto L_0x00d7;
                case -382039141: goto L_0x00c9;
                case -315259171: goto L_0x00bb;
                case -147951540: goto L_0x00ad;
                case 108971: goto L_0x009f;
                case 15719777: goto L_0x0091;
                case 392597729: goto L_0x0083;
                case 536948395: goto L_0x0076;
                case 556955191: goto L_0x0069;
                case 999778018: goto L_0x005c;
                case 1043529839: goto L_0x004f;
                case 1436281521: goto L_0x0042;
                case 1599575662: goto L_0x0035;
                case 1619122624: goto L_0x0028;
                case 1649710144: goto L_0x001b;
                case 1901043637: goto L_0x000e;
                default: goto L_0x000b;
            }
        L_0x000b:
            r0 = r1
            goto L_0x0136
        L_0x000e:
            java.lang.String r0 = "location"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x0017
            goto L_0x000b
        L_0x0017:
            r0 = 22
            goto L_0x0136
        L_0x001b:
            java.lang.String r0 = "gesture_double_tap_screen"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x0024
            goto L_0x000b
        L_0x0024:
            r0 = 21
            goto L_0x0136
        L_0x0028:
            java.lang.String r0 = "data_saver"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x0031
            goto L_0x000b
        L_0x0031:
            r0 = 20
            goto L_0x0136
        L_0x0035:
            java.lang.String r0 = "magnify_navbar"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x003e
            goto L_0x000b
        L_0x003e:
            r0 = 19
            goto L_0x0136
        L_0x0042:
            java.lang.String r0 = "gesture_double_tap_power"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x004b
            goto L_0x000b
        L_0x004b:
            r0 = 18
            goto L_0x0136
        L_0x004f:
            java.lang.String r0 = "gesture_double_twist"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x0058
            goto L_0x000b
        L_0x0058:
            r0 = 17
            goto L_0x0136
        L_0x005c:
            java.lang.String r0 = "gesture_swipe_down_fingerprint"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x0065
            goto L_0x000b
        L_0x0065:
            r0 = 16
            goto L_0x0136
        L_0x0069:
            java.lang.String r0 = "color_inversion"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x0072
            goto L_0x000b
        L_0x0072:
            r0 = 15
            goto L_0x0136
        L_0x0076:
            java.lang.String r0 = "magnify_gesture"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x007f
            goto L_0x000b
        L_0x007f:
            r0 = 14
            goto L_0x0136
        L_0x0083:
            java.lang.String r0 = "auto_brightness"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x008d
            goto L_0x000b
        L_0x008d:
            r0 = 13
            goto L_0x0136
        L_0x0091:
            java.lang.String r0 = "master_wifi_toggle"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x009b
            goto L_0x000b
        L_0x009b:
            r0 = 12
            goto L_0x0136
        L_0x009f:
            java.lang.String r0 = "nfc"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x00a9
            goto L_0x000b
        L_0x00a9:
            r0 = 11
            goto L_0x0136
        L_0x00ad:
            java.lang.String r0 = "notification_badging"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x00b7
            goto L_0x000b
        L_0x00b7:
            r0 = 10
            goto L_0x0136
        L_0x00bb:
            java.lang.String r0 = "enable_wifi_ap"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x00c5
            goto L_0x000b
        L_0x00c5:
            r0 = 9
            goto L_0x0136
        L_0x00c9:
            java.lang.String r0 = "night_display"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x00d3
            goto L_0x000b
        L_0x00d3:
            r0 = 8
            goto L_0x0136
        L_0x00d7:
            java.lang.String r0 = "gesture_assist"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x00e1
            goto L_0x000b
        L_0x00e1:
            r0 = 7
            goto L_0x0136
        L_0x00e3:
            java.lang.String r0 = "talkback"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x00ed
            goto L_0x000b
        L_0x00ed:
            r0 = 6
            goto L_0x0136
        L_0x00ef:
            java.lang.String r0 = "toggle_airplane"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x00f9
            goto L_0x000b
        L_0x00f9:
            r0 = 5
            goto L_0x0136
        L_0x00fb:
            java.lang.String r0 = "auto_rotate"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x0105
            goto L_0x000b
        L_0x0105:
            r0 = 4
            goto L_0x0136
        L_0x0107:
            java.lang.String r0 = "mobile_data"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x0111
            goto L_0x000b
        L_0x0111:
            r0 = 3
            goto L_0x0136
        L_0x0113:
            java.lang.String r0 = "ambient_display_always_on"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x011d
            goto L_0x000b
        L_0x011d:
            r0 = 2
            goto L_0x0136
        L_0x011f:
            java.lang.String r0 = "gesture_pick_up"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x0129
            goto L_0x000b
        L_0x0129:
            r0 = 1
            goto L_0x0136
        L_0x012b:
            java.lang.String r0 = "ambient_display_notification"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x0135
            goto L_0x000b
        L_0x0135:
            r0 = 0
        L_0x0136:
            switch(r0) {
                case 0: goto L_0x0188;
                case 1: goto L_0x0188;
                case 2: goto L_0x0188;
                case 3: goto L_0x0181;
                case 4: goto L_0x0188;
                case 5: goto L_0x0188;
                case 6: goto L_0x017a;
                case 7: goto L_0x0188;
                case 8: goto L_0x0173;
                case 9: goto L_0x016c;
                case 10: goto L_0x0188;
                case 11: goto L_0x0165;
                case 12: goto L_0x015e;
                case 13: goto L_0x0188;
                case 14: goto L_0x0157;
                case 15: goto L_0x0150;
                case 16: goto L_0x0188;
                case 17: goto L_0x0188;
                case 18: goto L_0x0188;
                case 19: goto L_0x0149;
                case 20: goto L_0x0142;
                case 21: goto L_0x0188;
                case 22: goto L_0x013b;
                default: goto L_0x0139;
            }
        L_0x0139:
            r5 = 0
            goto L_0x0191
        L_0x013b:
            com.google.android.settings.external.specialcase.LocationSetting r0 = com.google.android.settings.external.InlineSettings.LOCATION_SETTING
            android.database.Cursor r5 = r0.getAccessCursor(r2, r5)
            goto L_0x0191
        L_0x0142:
            com.google.android.settings.external.specialcase.DataSaverSetting r0 = com.google.android.settings.external.InlineSettings.DATA_SAVER_SETTING
            android.database.Cursor r5 = r0.getAccessCursor(r2, r5)
            goto L_0x0191
        L_0x0149:
            com.google.android.settings.external.specialcase.MagnificationSetting r0 = com.google.android.settings.external.InlineSettings.MAGNIFY_NAVBAR_SETTING
            android.database.Cursor r5 = r0.getAccessCursor(r2, r5)
            goto L_0x0191
        L_0x0150:
            com.google.android.settings.external.specialcase.ColorInversionSetting r0 = com.google.android.settings.external.InlineSettings.COLOR_INVERSION_SETTING
            android.database.Cursor r5 = r0.getAccessCursor(r2, r5)
            goto L_0x0191
        L_0x0157:
            com.google.android.settings.external.specialcase.MagnificationSetting r0 = com.google.android.settings.external.InlineSettings.MAGNIFY_GESTURE_SETTING
            android.database.Cursor r5 = r0.getAccessCursor(r2, r5)
            goto L_0x0191
        L_0x015e:
            com.google.android.settings.external.specialcase.WifiSetting r0 = com.google.android.settings.external.InlineSettings.WIFI_SETTING
            android.database.Cursor r5 = r0.getAccessCursor(r2, r5)
            goto L_0x0191
        L_0x0165:
            com.google.android.settings.external.specialcase.NfcSetting r0 = com.google.android.settings.external.InlineSettings.NFC_SETTING
            android.database.Cursor r5 = r0.getAccessCursor(r2, r5)
            goto L_0x0191
        L_0x016c:
            com.google.android.settings.external.specialcase.HotspotSetting r0 = com.google.android.settings.external.InlineSettings.HOTSPOT_SETTING
            android.database.Cursor r5 = r0.getAccessCursor(r2, r5)
            goto L_0x0191
        L_0x0173:
            com.google.android.settings.external.specialcase.NightDisplaySetting r0 = com.google.android.settings.external.InlineSettings.NIGHTDISPLAY_SETTING
            android.database.Cursor r5 = r0.getAccessCursor(r2, r5)
            goto L_0x0191
        L_0x017a:
            com.google.android.settings.external.specialcase.AccessibilitySetting r0 = com.google.android.settings.external.InlineSettings.TALKBACK_SETTING
            android.database.Cursor r5 = r0.getAccessCursor(r2, r5)
            goto L_0x0191
        L_0x0181:
            com.google.android.settings.external.specialcase.MobileDataSetting r0 = com.google.android.settings.external.InlineSettings.MOBILE_DATA_SETTING
            android.database.Cursor r5 = r0.getAccessCursor(r2, r5)
            goto L_0x0191
        L_0x0188:
            com.google.android.settings.external.specialcase.SliceBasedSetting r0 = new com.google.android.settings.external.specialcase.SliceBasedSetting
            r0.<init>()
            android.database.Cursor r5 = r0.getAccessCursor(r2, r5)
        L_0x0191:
            if (r5 == 0) goto L_0x01a9
            boolean r0 = r5.moveToFirst()
            if (r0 == 0) goto L_0x01a9
            java.lang.String r0 = "existing_value"
            int r0 = r5.getColumnIndex(r0)
            if (r0 < 0) goto L_0x01a5
            int r1 = r5.getInt(r0)
        L_0x01a5:
            logAccessSetting(r2, r3, r4, r1)
            return r5
        L_0x01a9:
            java.lang.IllegalArgumentException r2 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r5 = "Invalid access special case key: "
            r3.append(r5)
            r3.append(r4)
            java.lang.String r3 = r3.toString()
            r2.<init>(r3)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.settings.external.ExternalSettingsManager.getAccessCursorForSpecialSetting(android.content.Context, java.lang.String, java.lang.String, com.android.settings.slices.SliceData):android.database.Cursor");
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.database.Cursor getUpdateCursorForSpecialSetting(android.content.Context r2, java.lang.String r3, java.lang.String r4, java.lang.String r5, com.android.settings.slices.SliceData r6) {
        /*
            r4.hashCode()
            int r0 = r4.hashCode()
            r1 = -1
            switch(r0) {
                case -1903108977: goto L_0x012b;
                case -1552376189: goto L_0x011f;
                case -1525260053: goto L_0x0113;
                case -1314247385: goto L_0x0107;
                case -1272444437: goto L_0x00fb;
                case -1001942051: goto L_0x00ef;
                case -610139245: goto L_0x00e3;
                case -608350689: goto L_0x00d7;
                case -382039141: goto L_0x00c9;
                case -315259171: goto L_0x00bb;
                case -147951540: goto L_0x00ad;
                case 108971: goto L_0x009f;
                case 15719777: goto L_0x0091;
                case 392597729: goto L_0x0083;
                case 536948395: goto L_0x0076;
                case 556955191: goto L_0x0069;
                case 999778018: goto L_0x005c;
                case 1043529839: goto L_0x004f;
                case 1436281521: goto L_0x0042;
                case 1599575662: goto L_0x0035;
                case 1619122624: goto L_0x0028;
                case 1649710144: goto L_0x001b;
                case 1901043637: goto L_0x000e;
                default: goto L_0x000b;
            }
        L_0x000b:
            r0 = r1
            goto L_0x0136
        L_0x000e:
            java.lang.String r0 = "location"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x0017
            goto L_0x000b
        L_0x0017:
            r0 = 22
            goto L_0x0136
        L_0x001b:
            java.lang.String r0 = "gesture_double_tap_screen"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x0024
            goto L_0x000b
        L_0x0024:
            r0 = 21
            goto L_0x0136
        L_0x0028:
            java.lang.String r0 = "data_saver"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x0031
            goto L_0x000b
        L_0x0031:
            r0 = 20
            goto L_0x0136
        L_0x0035:
            java.lang.String r0 = "magnify_navbar"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x003e
            goto L_0x000b
        L_0x003e:
            r0 = 19
            goto L_0x0136
        L_0x0042:
            java.lang.String r0 = "gesture_double_tap_power"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x004b
            goto L_0x000b
        L_0x004b:
            r0 = 18
            goto L_0x0136
        L_0x004f:
            java.lang.String r0 = "gesture_double_twist"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x0058
            goto L_0x000b
        L_0x0058:
            r0 = 17
            goto L_0x0136
        L_0x005c:
            java.lang.String r0 = "gesture_swipe_down_fingerprint"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x0065
            goto L_0x000b
        L_0x0065:
            r0 = 16
            goto L_0x0136
        L_0x0069:
            java.lang.String r0 = "color_inversion"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x0072
            goto L_0x000b
        L_0x0072:
            r0 = 15
            goto L_0x0136
        L_0x0076:
            java.lang.String r0 = "magnify_gesture"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x007f
            goto L_0x000b
        L_0x007f:
            r0 = 14
            goto L_0x0136
        L_0x0083:
            java.lang.String r0 = "auto_brightness"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x008d
            goto L_0x000b
        L_0x008d:
            r0 = 13
            goto L_0x0136
        L_0x0091:
            java.lang.String r0 = "master_wifi_toggle"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x009b
            goto L_0x000b
        L_0x009b:
            r0 = 12
            goto L_0x0136
        L_0x009f:
            java.lang.String r0 = "nfc"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x00a9
            goto L_0x000b
        L_0x00a9:
            r0 = 11
            goto L_0x0136
        L_0x00ad:
            java.lang.String r0 = "notification_badging"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x00b7
            goto L_0x000b
        L_0x00b7:
            r0 = 10
            goto L_0x0136
        L_0x00bb:
            java.lang.String r0 = "enable_wifi_ap"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x00c5
            goto L_0x000b
        L_0x00c5:
            r0 = 9
            goto L_0x0136
        L_0x00c9:
            java.lang.String r0 = "night_display"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x00d3
            goto L_0x000b
        L_0x00d3:
            r0 = 8
            goto L_0x0136
        L_0x00d7:
            java.lang.String r0 = "gesture_assist"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x00e1
            goto L_0x000b
        L_0x00e1:
            r0 = 7
            goto L_0x0136
        L_0x00e3:
            java.lang.String r0 = "talkback"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x00ed
            goto L_0x000b
        L_0x00ed:
            r0 = 6
            goto L_0x0136
        L_0x00ef:
            java.lang.String r0 = "toggle_airplane"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x00f9
            goto L_0x000b
        L_0x00f9:
            r0 = 5
            goto L_0x0136
        L_0x00fb:
            java.lang.String r0 = "auto_rotate"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x0105
            goto L_0x000b
        L_0x0105:
            r0 = 4
            goto L_0x0136
        L_0x0107:
            java.lang.String r0 = "mobile_data"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x0111
            goto L_0x000b
        L_0x0111:
            r0 = 3
            goto L_0x0136
        L_0x0113:
            java.lang.String r0 = "ambient_display_always_on"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x011d
            goto L_0x000b
        L_0x011d:
            r0 = 2
            goto L_0x0136
        L_0x011f:
            java.lang.String r0 = "gesture_pick_up"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x0129
            goto L_0x000b
        L_0x0129:
            r0 = 1
            goto L_0x0136
        L_0x012b:
            java.lang.String r0 = "ambient_display_notification"
            boolean r0 = r4.equals(r0)
            if (r0 != 0) goto L_0x0135
            goto L_0x000b
        L_0x0135:
            r0 = 0
        L_0x0136:
            switch(r0) {
                case 0: goto L_0x0188;
                case 1: goto L_0x0188;
                case 2: goto L_0x0188;
                case 3: goto L_0x0181;
                case 4: goto L_0x0188;
                case 5: goto L_0x0188;
                case 6: goto L_0x017a;
                case 7: goto L_0x0188;
                case 8: goto L_0x0173;
                case 9: goto L_0x016c;
                case 10: goto L_0x0188;
                case 11: goto L_0x0165;
                case 12: goto L_0x015e;
                case 13: goto L_0x0188;
                case 14: goto L_0x0157;
                case 15: goto L_0x0150;
                case 16: goto L_0x0188;
                case 17: goto L_0x0188;
                case 18: goto L_0x0188;
                case 19: goto L_0x0149;
                case 20: goto L_0x0142;
                case 21: goto L_0x0188;
                case 22: goto L_0x013b;
                default: goto L_0x0139;
            }
        L_0x0139:
            r5 = 0
            goto L_0x0191
        L_0x013b:
            com.google.android.settings.external.specialcase.LocationSetting r0 = com.google.android.settings.external.InlineSettings.LOCATION_SETTING
            android.database.Cursor r5 = r0.getUpdateCursor((android.content.Context) r2, (com.android.settings.slices.SliceData) r6, (java.lang.String) r5)
            goto L_0x0191
        L_0x0142:
            com.google.android.settings.external.specialcase.DataSaverSetting r0 = com.google.android.settings.external.InlineSettings.DATA_SAVER_SETTING
            android.database.Cursor r5 = r0.getUpdateCursor((android.content.Context) r2, (com.android.settings.slices.SliceData) r6, (java.lang.String) r5)
            goto L_0x0191
        L_0x0149:
            com.google.android.settings.external.specialcase.MagnificationSetting r0 = com.google.android.settings.external.InlineSettings.MAGNIFY_NAVBAR_SETTING
            android.database.Cursor r5 = r0.getUpdateCursor((android.content.Context) r2, (com.android.settings.slices.SliceData) r6, (java.lang.String) r5)
            goto L_0x0191
        L_0x0150:
            com.google.android.settings.external.specialcase.ColorInversionSetting r0 = com.google.android.settings.external.InlineSettings.COLOR_INVERSION_SETTING
            android.database.Cursor r5 = r0.getUpdateCursor((android.content.Context) r2, (com.android.settings.slices.SliceData) r6, (java.lang.String) r5)
            goto L_0x0191
        L_0x0157:
            com.google.android.settings.external.specialcase.MagnificationSetting r0 = com.google.android.settings.external.InlineSettings.MAGNIFY_GESTURE_SETTING
            android.database.Cursor r5 = r0.getUpdateCursor((android.content.Context) r2, (com.android.settings.slices.SliceData) r6, (java.lang.String) r5)
            goto L_0x0191
        L_0x015e:
            com.google.android.settings.external.specialcase.WifiSetting r0 = com.google.android.settings.external.InlineSettings.WIFI_SETTING
            android.database.Cursor r5 = r0.getUpdateCursor((android.content.Context) r2, (com.android.settings.slices.SliceData) r6, (java.lang.String) r5)
            goto L_0x0191
        L_0x0165:
            com.google.android.settings.external.specialcase.NfcSetting r0 = com.google.android.settings.external.InlineSettings.NFC_SETTING
            android.database.Cursor r5 = r0.getUpdateCursor((android.content.Context) r2, (com.android.settings.slices.SliceData) r6, (java.lang.String) r5)
            goto L_0x0191
        L_0x016c:
            com.google.android.settings.external.specialcase.HotspotSetting r0 = com.google.android.settings.external.InlineSettings.HOTSPOT_SETTING
            android.database.Cursor r5 = r0.getUpdateCursor((android.content.Context) r2, (com.android.settings.slices.SliceData) r6, (java.lang.String) r5)
            goto L_0x0191
        L_0x0173:
            com.google.android.settings.external.specialcase.NightDisplaySetting r0 = com.google.android.settings.external.InlineSettings.NIGHTDISPLAY_SETTING
            android.database.Cursor r5 = r0.getUpdateCursor((android.content.Context) r2, (com.android.settings.slices.SliceData) r6, (java.lang.String) r5)
            goto L_0x0191
        L_0x017a:
            com.google.android.settings.external.specialcase.AccessibilitySetting r0 = com.google.android.settings.external.InlineSettings.TALKBACK_SETTING
            android.database.Cursor r5 = r0.getUpdateCursor((android.content.Context) r2, (com.android.settings.slices.SliceData) r6, (java.lang.String) r5)
            goto L_0x0191
        L_0x0181:
            com.google.android.settings.external.specialcase.MobileDataSetting r0 = com.google.android.settings.external.InlineSettings.MOBILE_DATA_SETTING
            android.database.Cursor r5 = r0.getUpdateCursor((android.content.Context) r2, (com.android.settings.slices.SliceData) r6, (java.lang.String) r5)
            goto L_0x0191
        L_0x0188:
            com.google.android.settings.external.specialcase.SliceBasedSetting r0 = new com.google.android.settings.external.specialcase.SliceBasedSetting
            r0.<init>()
            android.database.Cursor r5 = r0.getUpdateCursor((android.content.Context) r2, (com.android.settings.slices.SliceData) r6, (java.lang.String) r5)
        L_0x0191:
            if (r5 == 0) goto L_0x01a9
            boolean r6 = r5.moveToFirst()
            if (r6 == 0) goto L_0x01a9
            java.lang.String r6 = "newValue"
            int r6 = r5.getColumnIndex(r6)
            if (r6 < 0) goto L_0x01a5
            int r1 = r5.getInt(r6)
        L_0x01a5:
            logUpdateSetting(r2, r3, r4, r1)
            return r5
        L_0x01a9:
            java.lang.IllegalArgumentException r2 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r5 = "Invalid update special case key: "
            r3.append(r5)
            r3.append(r4)
            java.lang.String r3 = r3.toString()
            r2.<init>(r3)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.settings.external.ExternalSettingsManager.getUpdateCursorForSpecialSetting(android.content.Context, java.lang.String, java.lang.String, java.lang.String, com.android.settings.slices.SliceData):android.database.Cursor");
    }

    private static void logAccessSetting(Context context, String str, String str2, int i) {
        FeatureFactory.getFactory(context).getMetricsFeatureProvider().action(0, 853, 0, SharedPreferencesLogger.buildPrefKey(str + "/access", str2), i);
    }

    private static void logUpdateSetting(Context context, String str, String str2, int i) {
        FeatureFactory.getFactory(context).getMetricsFeatureProvider().action(0, 853, 0, SharedPreferencesLogger.buildPrefKey(str, str2), i);
    }
}
