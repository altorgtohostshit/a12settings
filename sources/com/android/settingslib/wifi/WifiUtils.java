package com.android.settingslib.wifi;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.os.SystemClock;
import com.android.settingslib.R$drawable;
import java.util.Iterator;
import java.util.Map;

public class WifiUtils {
    static final int[] NO_INTERNET_WIFI_PIE = {R$drawable.ic_no_internet_wifi_signal_0, R$drawable.ic_no_internet_wifi_signal_1, R$drawable.ic_no_internet_wifi_signal_2, R$drawable.ic_no_internet_wifi_signal_3, R$drawable.ic_no_internet_wifi_signal_4};
    static final int[] WIFI_PIE = {17302883, 17302884, 17302885, 17302886, 17302887};

    static String getVisibilityStatus(AccessPoint accessPoint) {
        String str;
        StringBuilder sb;
        AccessPoint accessPoint2 = accessPoint;
        WifiInfo info = accessPoint.getInfo();
        StringBuilder sb2 = new StringBuilder();
        StringBuilder sb3 = new StringBuilder();
        StringBuilder sb4 = new StringBuilder();
        StringBuilder sb5 = new StringBuilder();
        int i = 0;
        if (!accessPoint.isActive() || info == null) {
            str = null;
        } else {
            str = info.getBSSID();
            if (str != null) {
                sb2.append(" ");
                sb2.append(str);
            }
            sb2.append(" standard = ");
            sb2.append(info.getWifiStandard());
            sb2.append(" rssi=");
            sb2.append(info.getRssi());
            sb2.append(" ");
            sb2.append(" score=");
            sb2.append(info.getScore());
            if (accessPoint.getSpeed() != 0) {
                sb2.append(" speed=");
                sb2.append(accessPoint.getSpeedLabel());
            }
            sb2.append(String.format(" tx=%.1f,", new Object[]{Double.valueOf(info.getSuccessfulTxPacketsPerSecond())}));
            sb2.append(String.format("%.1f,", new Object[]{Double.valueOf(info.getRetriedTxPacketsPerSecond())}));
            sb2.append(String.format("%.1f ", new Object[]{Double.valueOf(info.getLostTxPacketsPerSecond())}));
            sb2.append(String.format("rx=%.1f", new Object[]{Double.valueOf(info.getSuccessfulRxPacketsPerSecond())}));
        }
        long elapsedRealtime = SystemClock.elapsedRealtime();
        Iterator<ScanResult> it = accessPoint.getScanResults().iterator();
        int i2 = 0;
        int i3 = -127;
        int i4 = -127;
        int i5 = -127;
        int i6 = 0;
        while (true) {
            sb = sb2;
            if (!it.hasNext()) {
                break;
            }
            ScanResult next = it.next();
            if (next == null) {
                sb2 = sb;
            } else {
                int i7 = next.frequency;
                Iterator<ScanResult> it2 = it;
                if (i7 >= 4900 && i7 <= 5900) {
                    i6++;
                    int i8 = next.level;
                    if (i8 > i4) {
                        i4 = i8;
                    }
                    if (i6 <= 4) {
                        sb4.append(verboseScanResultSummary(accessPoint2, next, str, elapsedRealtime));
                    }
                } else if (i7 >= 2400 && i7 <= 2500) {
                    i++;
                    int i9 = next.level;
                    if (i9 > i3) {
                        i3 = i9;
                    }
                    if (i <= 4) {
                        sb3.append(verboseScanResultSummary(accessPoint2, next, str, elapsedRealtime));
                    }
                } else if (i7 >= 58320 && i7 <= 70200) {
                    i2++;
                    int i10 = next.level;
                    if (i10 > i5) {
                        i5 = i10;
                    }
                    if (i2 <= 4) {
                        sb5.append(verboseScanResultSummary(accessPoint2, next, str, elapsedRealtime));
                    }
                }
                sb2 = sb;
                it = it2;
            }
        }
        StringBuilder sb6 = sb;
        sb6.append(" [");
        if (i > 0) {
            sb6.append("(");
            sb6.append(i);
            sb6.append(")");
            if (i > 4) {
                sb6.append("max=");
                sb6.append(i3);
                sb6.append(",");
            }
            sb6.append(sb3.toString());
        }
        sb6.append(";");
        if (i6 > 0) {
            sb6.append("(");
            sb6.append(i6);
            sb6.append(")");
            if (i6 > 4) {
                sb6.append("max=");
                sb6.append(i4);
                sb6.append(",");
            }
            sb6.append(sb4.toString());
        }
        sb6.append(";");
        if (i2 > 0) {
            sb6.append("(");
            sb6.append(i2);
            sb6.append(")");
            if (i2 > 4) {
                sb6.append("max=");
                sb6.append(i5);
                sb6.append(",");
            }
            sb6.append(sb5.toString());
        }
        sb6.append("]");
        return sb6.toString();
    }

    static String verboseScanResultSummary(AccessPoint accessPoint, ScanResult scanResult, String str, long j) {
        StringBuilder sb = new StringBuilder();
        sb.append(" \n{");
        sb.append(scanResult.BSSID);
        if (scanResult.BSSID.equals(str)) {
            sb.append("*");
        }
        sb.append("=");
        sb.append(scanResult.frequency);
        sb.append(",");
        sb.append(scanResult.level);
        int specificApSpeed = getSpecificApSpeed(scanResult, accessPoint.getScoredNetworkCache());
        if (specificApSpeed != 0) {
            sb.append(",");
            sb.append(accessPoint.getSpeedLabel(specificApSpeed));
        }
        sb.append(",");
        sb.append(((int) (j - (scanResult.timestamp / 1000))) / 1000);
        sb.append("s");
        sb.append("}");
        return sb.toString();
    }

    private static int getSpecificApSpeed(ScanResult scanResult, Map<String, TimestampedScoredNetwork> map) {
        TimestampedScoredNetwork timestampedScoredNetwork = map.get(scanResult.BSSID);
        if (timestampedScoredNetwork == null) {
            return 0;
        }
        return timestampedScoredNetwork.getScore().calculateBadge(scanResult.level);
    }

    public static int getInternetIconResource(int i, boolean z) {
        if (i >= 0) {
            int[] iArr = WIFI_PIE;
            if (i < iArr.length) {
                return z ? NO_INTERNET_WIFI_PIE[i] : iArr[i];
            }
        }
        throw new IllegalArgumentException("No Wifi icon found for level: " + i);
    }
}
