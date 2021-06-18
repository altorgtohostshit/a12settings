package com.android.settings.vpn2;

import android.content.Context;
import android.net.VpnManager;
import android.os.RemoteException;
import android.provider.Settings;
import android.security.LegacyVpnProfileStore;
import com.android.internal.net.VpnConfig;

public class VpnUtils {
    public static String getLockdownVpn() {
        byte[] bArr = LegacyVpnProfileStore.get("LOCKDOWN_VPN");
        if (bArr == null) {
            return null;
        }
        return new String(bArr);
    }

    public static void clearLockdownVpn(Context context) {
        LegacyVpnProfileStore.remove("LOCKDOWN_VPN");
        getVpnManager(context).updateLockdownVpn();
    }

    public static void setLockdownVpn(Context context, String str) {
        LegacyVpnProfileStore.put("LOCKDOWN_VPN", str.getBytes());
        getVpnManager(context).updateLockdownVpn();
    }

    public static boolean isVpnLockdown(String str) {
        return str.equals(getLockdownVpn());
    }

    public static boolean isAnyLockdownActive(Context context) {
        int userId = context.getUserId();
        if (getLockdownVpn() != null) {
            return true;
        }
        if (getVpnManager(context).getAlwaysOnVpnPackageForUser(userId) == null || Settings.Secure.getIntForUser(context.getContentResolver(), "always_on_vpn_lockdown", 0, userId) == 0) {
            return false;
        }
        return true;
    }

    public static boolean isVpnActive(Context context) throws RemoteException {
        return getVpnManager(context).getVpnConfig(context.getUserId()) != null;
    }

    public static String getConnectedPackage(VpnManager vpnManager, int i) {
        VpnConfig vpnConfig = vpnManager.getVpnConfig(i);
        if (vpnConfig != null) {
            return vpnConfig.user;
        }
        return null;
    }

    private static VpnManager getVpnManager(Context context) {
        return (VpnManager) context.getSystemService(VpnManager.class);
    }

    public static boolean isAlwaysOnVpnSet(VpnManager vpnManager, int i) {
        return vpnManager.getAlwaysOnVpnPackageForUser(i) != null;
    }

    public static boolean disconnectLegacyVpn(Context context) {
        int userId = context.getUserId();
        if (getVpnManager(context).getLegacyVpnInfo(userId) == null) {
            return false;
        }
        clearLockdownVpn(context);
        getVpnManager(context).prepareVpn((String) null, "[Legacy VPN]", userId);
        return true;
    }
}
