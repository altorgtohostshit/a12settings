package com.android.settingslib;

public class AccessibilityContentDescriptions {
    public static final int[] DATA_CONNECTION_STRENGTH = {R$string.accessibility_no_data, R$string.accessibility_data_one_bar, R$string.accessibility_data_two_bars, R$string.accessibility_data_three_bars, R$string.accessibility_data_signal_full};
    public static final int[] ETHERNET_CONNECTION_VALUES = {R$string.accessibility_ethernet_disconnected, R$string.accessibility_ethernet_connected};
    public static final int NO_CALLING = R$string.accessibility_no_calling;
    public static final int[] PHONE_SIGNAL_STRENGTH = {R$string.accessibility_no_phone, R$string.accessibility_phone_one_bar, R$string.accessibility_phone_two_bars, R$string.accessibility_phone_three_bars, R$string.accessibility_phone_signal_full};
    public static final int[] WIFI_CONNECTION_STRENGTH;
    public static final int WIFI_NO_CONNECTION;

    static {
        int i = R$string.accessibility_no_wifi;
        WIFI_CONNECTION_STRENGTH = new int[]{i, R$string.accessibility_wifi_one_bar, R$string.accessibility_wifi_two_bars, R$string.accessibility_wifi_three_bars, R$string.accessibility_wifi_signal_full};
        WIFI_NO_CONNECTION = i;
    }
}
