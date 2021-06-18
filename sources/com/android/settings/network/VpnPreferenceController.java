package com.android.settings.network;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.net.VpnManager;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.security.LegacyVpnProfileStore;
import android.util.Log;
import android.util.SparseArray;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.internal.net.LegacyVpnInfo;
import com.android.internal.net.VpnConfig;
import com.android.internal.net.VpnProfile;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.vpn2.VpnInfoPreference;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;
import com.android.settingslib.utils.ThreadUtils;

public class VpnPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin, LifecycleObserver, OnResume, OnPause {
    private static final NetworkRequest REQUEST = new NetworkRequest.Builder().removeCapability(15).removeCapability(13).removeCapability(14).build();
    private final ConnectivityManager mConnectivityManager;
    private final ConnectivityManager.NetworkCallback mNetworkCallback = new ConnectivityManager.NetworkCallback() {
        public void onAvailable(Network network) {
            VpnPreferenceController.this.updateSummary();
        }

        public void onLost(Network network) {
            VpnPreferenceController.this.updateSummary();
        }
    };
    private Preference mPreference;
    private final String mToggleable;
    private final UserManager mUserManager;
    private final VpnManager mVpnManager;

    public String getPreferenceKey() {
        return "vpn_settings";
    }

    public VpnPreferenceController(Context context) {
        super(context);
        this.mToggleable = Settings.Global.getString(context.getContentResolver(), "airplane_mode_toggleable_radios");
        this.mUserManager = (UserManager) context.getSystemService("user");
        this.mConnectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        this.mVpnManager = (VpnManager) context.getSystemService(VpnManager.class);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        Preference preference;
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference("vpn_settings");
        String str = this.mToggleable;
        if ((str == null || !str.contains("wifi")) && (preference = this.mPreference) != null) {
            preference.setDependency("airplane_mode");
        }
    }

    public boolean isAvailable() {
        return !RestrictedLockUtilsInternal.hasBaseUserRestriction(this.mContext, "no_config_vpn", UserHandle.myUserId());
    }

    public void onPause() {
        if (isAvailable()) {
            this.mConnectivityManager.unregisterNetworkCallback(this.mNetworkCallback);
        }
    }

    public void onResume() {
        if (isAvailable()) {
            this.mConnectivityManager.registerNetworkCallback(REQUEST, this.mNetworkCallback);
        }
    }

    /* access modifiers changed from: package-private */
    public void updateSummary() {
        int i;
        String str;
        if (this.mPreference != null) {
            SparseArray sparseArray = new SparseArray();
            int i2 = 0;
            for (UserInfo userInfo : this.mUserManager.getUsers()) {
                VpnConfig vpnConfig = this.mVpnManager.getVpnConfig(userInfo.id);
                if (vpnConfig != null) {
                    if (vpnConfig.legacy) {
                        LegacyVpnInfo legacyVpnInfo = this.mVpnManager.getLegacyVpnInfo(userInfo.id);
                        if (legacyVpnInfo != null && legacyVpnInfo.state == 3) {
                            i2++;
                        }
                    }
                    sparseArray.put(userInfo.id, vpnConfig);
                }
            }
            UserInfo userInfo2 = this.mUserManager.getUserInfo(UserHandle.myUserId());
            if (userInfo2.isRestricted()) {
                i = userInfo2.restrictedProfileParentId;
            } else {
                i = userInfo2.id;
            }
            VpnConfig vpnConfig2 = (VpnConfig) sparseArray.get(i);
            if (vpnConfig2 == null) {
                str = this.mContext.getString(R.string.vpn_disconnected_summary);
            } else {
                str = getNameForVpnConfig(vpnConfig2, UserHandle.of(i));
            }
            if (Utils.isProviderModelEnabled(this.mContext) && (this.mPreference instanceof VpnInfoPreference)) {
                int insecureVpnCount = getInsecureVpnCount();
                boolean z = insecureVpnCount > 0;
                ((VpnInfoPreference) this.mPreference).setInsecureVpn(z);
                if (z) {
                    if ((sparseArray.size() + LegacyVpnProfileStore.list("VPN_").length) - i2 == 1) {
                        str = this.mContext.getString(R.string.vpn_settings_insecure_single);
                    } else if (insecureVpnCount == 1) {
                        str = this.mContext.getString(R.string.vpn_settings_single_insecure_multiple_total, new Object[]{Integer.valueOf(insecureVpnCount)});
                    } else {
                        str = this.mContext.getString(R.string.vpn_settings_multiple_insecure_multiple_total, new Object[]{Integer.valueOf(insecureVpnCount)});
                    }
                }
            }
            ThreadUtils.postOnMainThread(new VpnPreferenceController$$ExternalSyntheticLambda0(this, str));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateSummary$0(String str) {
        this.mPreference.setSummary((CharSequence) str);
    }

    /* access modifiers changed from: package-private */
    public String getNameForVpnConfig(VpnConfig vpnConfig, UserHandle userHandle) {
        if (vpnConfig.legacy) {
            return this.mContext.getString(R.string.wifi_display_status_connected);
        }
        String str = vpnConfig.user;
        try {
            Context context = this.mContext;
            return VpnConfig.getVpnLabel(context.createPackageContextAsUser(context.getPackageName(), 0, userHandle), str).toString();
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("VpnPreferenceController", "Package " + str + " is not present", e);
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public int getInsecureVpnCount() {
        int i = 0;
        for (String str : LegacyVpnProfileStore.list("VPN_")) {
            if (VpnProfile.isLegacyType(VpnProfile.decode(str, LegacyVpnProfileStore.get("VPN_" + str)).type)) {
                i++;
            }
        }
        return i;
    }
}
