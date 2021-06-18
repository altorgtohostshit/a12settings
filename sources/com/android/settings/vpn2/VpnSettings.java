package com.android.settings.vpn2;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.net.VpnManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.UserHandle;
import android.os.UserManager;
import android.security.LegacyVpnProfileStore;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.net.LegacyVpnInfo;
import com.android.internal.net.VpnConfig;
import com.android.internal.net.VpnProfile;
import com.android.settings.R;
import com.android.settings.RestrictedSettingsFragment;
import com.android.settings.Utils;
import com.android.settings.widget.GearPreference;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.google.android.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class VpnSettings extends RestrictedSettingsFragment implements Handler.Callback, Preference.OnPreferenceClickListener {
    private static final NetworkRequest VPN_REQUEST = new NetworkRequest.Builder().removeCapability(15).removeCapability(13).removeCapability(14).build();
    private Map<AppVpnInfo, AppPreference> mAppPreferences = new ArrayMap();
    private LegacyVpnInfo mConnectedLegacyVpn;
    private ConnectivityManager mConnectivityManager;
    private GearPreference.OnGearClickListener mGearListener = new GearPreference.OnGearClickListener() {
        public void onGearClick(GearPreference gearPreference) {
            if (gearPreference instanceof LegacyVpnPreference) {
                ConfigDialogFragment.show(VpnSettings.this, ((LegacyVpnPreference) gearPreference).getProfile(), true, true);
            } else if (gearPreference instanceof AppPreference) {
                AppManagementFragment.show(VpnSettings.this.getPrefContext(), (AppPreference) gearPreference, VpnSettings.this.getMetricsCategory());
            }
        }
    };
    private Map<String, LegacyVpnPreference> mLegacyVpnPreferences = new ArrayMap();
    private ConnectivityManager.NetworkCallback mNetworkCallback = new ConnectivityManager.NetworkCallback() {
        public void onAvailable(Network network) {
            if (VpnSettings.this.mUpdater != null) {
                VpnSettings.this.mUpdater.sendEmptyMessage(0);
            }
        }

        public void onLost(Network network) {
            if (VpnSettings.this.mUpdater != null) {
                VpnSettings.this.mUpdater.sendEmptyMessage(0);
            }
        }
    };
    private boolean mUnavailable;
    /* access modifiers changed from: private */
    @GuardedBy({"this"})
    public Handler mUpdater;
    private HandlerThread mUpdaterThread;
    private UserManager mUserManager;
    private VpnManager mVpnManager;

    public int getHelpResource() {
        return R.string.help_url_vpn;
    }

    public int getMetricsCategory() {
        return 100;
    }

    public VpnSettings() {
        super("no_config_vpn");
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.mUserManager = (UserManager) getSystemService("user");
        this.mConnectivityManager = (ConnectivityManager) getSystemService("connectivity");
        this.mVpnManager = (VpnManager) getSystemService("vpn_management");
        boolean isUiRestricted = isUiRestricted();
        this.mUnavailable = isUiRestricted;
        setHasOptionsMenu(!isUiRestricted);
        addPreferencesFromResource(R.xml.vpn_settings2);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        if (!Utils.isProviderModelEnabled(getContext()) || getContext().getPackageManager().hasSystemFeature("android.software.ipsec_tunnels")) {
            menuInflater.inflate(R.menu.vpn, menu);
        } else {
            Log.w("VpnSettings", "FEATURE_IPSEC_TUNNELS missing from system, cannot create new VPNs");
        }
    }

    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        for (int i = 0; i < menu.size(); i++) {
            if (isUiRestrictedByOnlyAdmin()) {
                RestrictedLockUtilsInternal.setMenuItemAsDisabledByAdmin(getPrefContext(), menu.getItem(i), getRestrictionEnforcedAdmin());
            } else {
                menu.getItem(i).setEnabled(!this.mUnavailable);
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != R.id.vpn_create) {
            return super.onOptionsItemSelected(menuItem);
        }
        long currentTimeMillis = System.currentTimeMillis();
        while (this.mLegacyVpnPreferences.containsKey(Long.toHexString(currentTimeMillis))) {
            currentTimeMillis++;
        }
        ConfigDialogFragment.show(this, new VpnProfile(Long.toHexString(currentTimeMillis)), true, false);
        return true;
    }

    public void onResume() {
        super.onResume();
        boolean hasUserRestriction = this.mUserManager.hasUserRestriction("no_config_vpn");
        this.mUnavailable = hasUserRestriction;
        if (hasUserRestriction) {
            if (!isUiRestrictedByOnlyAdmin()) {
                getEmptyTextView().setText(R.string.vpn_settings_not_available);
            }
            getPreferenceScreen().removeAll();
            return;
        }
        setEmptyView(getEmptyTextView());
        getEmptyTextView().setText(R.string.vpn_no_vpns_added);
        this.mConnectivityManager.registerNetworkCallback(VPN_REQUEST, this.mNetworkCallback);
        HandlerThread handlerThread = new HandlerThread("Refresh VPN list in background");
        this.mUpdaterThread = handlerThread;
        handlerThread.start();
        Handler handler = new Handler(this.mUpdaterThread.getLooper(), this);
        this.mUpdater = handler;
        handler.sendEmptyMessage(0);
    }

    public void onPause() {
        if (this.mUnavailable) {
            super.onPause();
            return;
        }
        this.mConnectivityManager.unregisterNetworkCallback(this.mNetworkCallback);
        synchronized (this) {
            this.mUpdater.removeCallbacksAndMessages((Object) null);
            this.mUpdater = null;
            this.mUpdaterThread.quit();
            this.mUpdaterThread = null;
        }
        super.onPause();
    }

    public boolean handleMessage(Message message) {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return true;
        }
        Context applicationContext = activity.getApplicationContext();
        List<VpnProfile> loadVpnProfiles = loadVpnProfiles();
        List<AppVpnInfo> vpnApps = getVpnApps(applicationContext, true);
        Map<String, LegacyVpnInfo> connectedLegacyVpns = getConnectedLegacyVpns();
        activity.runOnUiThread(new UpdatePreferences(this).legacyVpns(loadVpnProfiles, connectedLegacyVpns, VpnUtils.getLockdownVpn()).appVpns(vpnApps, getConnectedAppVpns(), getAlwaysOnAppVpnInfos()));
        synchronized (this) {
            Handler handler = this.mUpdater;
            if (handler != null) {
                handler.removeMessages(0);
                this.mUpdater.sendEmptyMessageDelayed(0, 1000);
            }
        }
        return true;
    }

    static class UpdatePreferences implements Runnable {
        private Set<AppVpnInfo> alwaysOnAppVpnInfos = Collections.emptySet();
        private Set<AppVpnInfo> connectedAppVpns = Collections.emptySet();
        private Map<String, LegacyVpnInfo> connectedLegacyVpns = Collections.emptyMap();
        private String lockdownVpnKey = null;
        private final VpnSettings mSettings;
        private List<AppVpnInfo> vpnApps = Collections.emptyList();
        private List<VpnProfile> vpnProfiles = Collections.emptyList();

        public UpdatePreferences(VpnSettings vpnSettings) {
            this.mSettings = vpnSettings;
        }

        public final UpdatePreferences legacyVpns(List<VpnProfile> list, Map<String, LegacyVpnInfo> map, String str) {
            this.vpnProfiles = list;
            this.connectedLegacyVpns = map;
            this.lockdownVpnKey = str;
            return this;
        }

        public final UpdatePreferences appVpns(List<AppVpnInfo> list, Set<AppVpnInfo> set, Set<AppVpnInfo> set2) {
            this.vpnApps = list;
            this.connectedAppVpns = set;
            this.alwaysOnAppVpnInfos = set2;
            return this;
        }

        public void run() {
            if (this.mSettings.canAddPreferences()) {
                ArraySet arraySet = new ArraySet();
                Iterator<VpnProfile> it = this.vpnProfiles.iterator();
                while (true) {
                    boolean z = false;
                    if (!it.hasNext()) {
                        break;
                    }
                    VpnProfile next = it.next();
                    LegacyVpnPreference findOrCreatePreference = this.mSettings.findOrCreatePreference(next, true);
                    if (this.connectedLegacyVpns.containsKey(next.key)) {
                        findOrCreatePreference.setState(this.connectedLegacyVpns.get(next.key).state);
                    } else {
                        findOrCreatePreference.setState(ManageablePreference.STATE_NONE);
                    }
                    String str = this.lockdownVpnKey;
                    if (str != null && str.equals(next.key)) {
                        z = true;
                    }
                    findOrCreatePreference.setAlwaysOn(z);
                    findOrCreatePreference.setInsecureVpn(VpnProfile.isLegacyType(next.type));
                    arraySet.add(findOrCreatePreference);
                }
                for (LegacyVpnInfo next2 : this.connectedLegacyVpns.values()) {
                    LegacyVpnPreference findOrCreatePreference2 = this.mSettings.findOrCreatePreference(new VpnProfile(next2.key), false);
                    findOrCreatePreference2.setState(next2.state);
                    String str2 = this.lockdownVpnKey;
                    findOrCreatePreference2.setAlwaysOn(str2 != null && str2.equals(next2.key));
                    arraySet.add(findOrCreatePreference2);
                }
                for (AppVpnInfo next3 : this.vpnApps) {
                    AppPreference findOrCreatePreference3 = this.mSettings.findOrCreatePreference(next3);
                    if (this.connectedAppVpns.contains(next3)) {
                        findOrCreatePreference3.setState(3);
                    } else {
                        findOrCreatePreference3.setState(AppPreference.STATE_DISCONNECTED);
                    }
                    findOrCreatePreference3.setAlwaysOn(this.alwaysOnAppVpnInfos.contains(next3));
                    arraySet.add(findOrCreatePreference3);
                }
                this.mSettings.setShownPreferences(arraySet);
            }
        }
    }

    public boolean canAddPreferences() {
        return isAdded();
    }

    public void setShownPreferences(Collection<Preference> collection) {
        this.mLegacyVpnPreferences.values().retainAll(collection);
        this.mAppPreferences.values().retainAll(collection);
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        for (int preferenceCount = preferenceScreen.getPreferenceCount() - 1; preferenceCount >= 0; preferenceCount--) {
            Preference preference = preferenceScreen.getPreference(preferenceCount);
            if (collection.contains(preference)) {
                collection.remove(preference);
            } else {
                preferenceScreen.removePreference(preference);
            }
        }
        for (Preference addPreference : collection) {
            preferenceScreen.addPreference(addPreference);
        }
    }

    public boolean onPreferenceClick(Preference preference) {
        if (preference instanceof LegacyVpnPreference) {
            VpnProfile profile = ((LegacyVpnPreference) preference).getProfile();
            LegacyVpnInfo legacyVpnInfo = this.mConnectedLegacyVpn;
            if (legacyVpnInfo != null && profile.key.equals(legacyVpnInfo.key)) {
                LegacyVpnInfo legacyVpnInfo2 = this.mConnectedLegacyVpn;
                if (legacyVpnInfo2.state == 3) {
                    try {
                        legacyVpnInfo2.intent.send();
                        return true;
                    } catch (Exception e) {
                        Log.w("VpnSettings", "Starting config intent failed", e);
                    }
                }
            }
            ConfigDialogFragment.show(this, profile, false, true);
            return true;
        } else if (!(preference instanceof AppPreference)) {
            return false;
        } else {
            AppPreference appPreference = (AppPreference) preference;
            boolean z = appPreference.getState() == 3;
            if (!z) {
                try {
                    UserHandle of = UserHandle.of(appPreference.getUserId());
                    Context createPackageContextAsUser = getActivity().createPackageContextAsUser(getActivity().getPackageName(), 0, of);
                    Intent launchIntentForPackage = createPackageContextAsUser.getPackageManager().getLaunchIntentForPackage(appPreference.getPackageName());
                    if (launchIntentForPackage != null) {
                        createPackageContextAsUser.startActivityAsUser(launchIntentForPackage, of);
                        return true;
                    }
                } catch (PackageManager.NameNotFoundException e2) {
                    Log.w("VpnSettings", "VPN provider does not exist: " + appPreference.getPackageName(), e2);
                }
            }
            AppDialogFragment.show(this, appPreference.getPackageInfo(), appPreference.getLabel(), false, z);
            return true;
        }
    }

    public LegacyVpnPreference findOrCreatePreference(VpnProfile vpnProfile, boolean z) {
        boolean z2;
        LegacyVpnPreference legacyVpnPreference = this.mLegacyVpnPreferences.get(vpnProfile.key);
        if (legacyVpnPreference == null) {
            legacyVpnPreference = new LegacyVpnPreference(getPrefContext());
            legacyVpnPreference.setOnGearClickListener(this.mGearListener);
            legacyVpnPreference.setOnPreferenceClickListener(this);
            this.mLegacyVpnPreferences.put(vpnProfile.key, legacyVpnPreference);
            z2 = true;
        } else {
            z2 = false;
        }
        if (z2 || z) {
            legacyVpnPreference.setProfile(vpnProfile);
        }
        return legacyVpnPreference;
    }

    public AppPreference findOrCreatePreference(AppVpnInfo appVpnInfo) {
        AppPreference appPreference = this.mAppPreferences.get(appVpnInfo);
        if (appPreference != null) {
            return appPreference;
        }
        AppPreference appPreference2 = new AppPreference(getPrefContext(), appVpnInfo.userId, appVpnInfo.packageName);
        appPreference2.setOnGearClickListener(this.mGearListener);
        appPreference2.setOnPreferenceClickListener(this);
        this.mAppPreferences.put(appVpnInfo, appPreference2);
        return appPreference2;
    }

    private Map<String, LegacyVpnInfo> getConnectedLegacyVpns() {
        LegacyVpnInfo legacyVpnInfo = this.mVpnManager.getLegacyVpnInfo(UserHandle.myUserId());
        this.mConnectedLegacyVpn = legacyVpnInfo;
        if (legacyVpnInfo != null) {
            return Collections.singletonMap(legacyVpnInfo.key, legacyVpnInfo);
        }
        return Collections.emptyMap();
    }

    private Set<AppVpnInfo> getConnectedAppVpns() {
        ArraySet arraySet = new ArraySet();
        for (UserHandle next : this.mUserManager.getUserProfiles()) {
            VpnConfig vpnConfig = this.mVpnManager.getVpnConfig(next.getIdentifier());
            if (vpnConfig != null && !vpnConfig.legacy) {
                arraySet.add(new AppVpnInfo(next.getIdentifier(), vpnConfig.user));
            }
        }
        return arraySet;
    }

    private Set<AppVpnInfo> getAlwaysOnAppVpnInfos() {
        ArraySet arraySet = new ArraySet();
        for (UserHandle identifier : this.mUserManager.getUserProfiles()) {
            int identifier2 = identifier.getIdentifier();
            String alwaysOnVpnPackageForUser = this.mVpnManager.getAlwaysOnVpnPackageForUser(identifier2);
            if (alwaysOnVpnPackageForUser != null) {
                arraySet.add(new AppVpnInfo(identifier2, alwaysOnVpnPackageForUser));
            }
        }
        return arraySet;
    }

    static List<AppVpnInfo> getVpnApps(Context context, boolean z) {
        Set set;
        ArrayList newArrayList = Lists.newArrayList();
        if (z) {
            set = new ArraySet();
            for (UserHandle identifier : UserManager.get(context).getUserProfiles()) {
                set.add(Integer.valueOf(identifier.getIdentifier()));
            }
        } else {
            set = Collections.singleton(Integer.valueOf(UserHandle.myUserId()));
        }
        List<AppOpsManager.PackageOps> packagesForOps = ((AppOpsManager) context.getSystemService("appops")).getPackagesForOps(new int[]{47, 94});
        if (packagesForOps != null) {
            for (AppOpsManager.PackageOps packageOps : packagesForOps) {
                int userId = UserHandle.getUserId(packageOps.getUid());
                if (set.contains(Integer.valueOf(userId))) {
                    boolean z2 = false;
                    for (AppOpsManager.OpEntry opEntry : packageOps.getOps()) {
                        if ((opEntry.getOp() == 47 || opEntry.getOp() == 94) && opEntry.getMode() == 0) {
                            z2 = true;
                        }
                    }
                    if (z2) {
                        newArrayList.add(new AppVpnInfo(userId, packageOps.getPackageName()));
                    }
                }
            }
        }
        Collections.sort(newArrayList);
        return newArrayList;
    }

    private static List<VpnProfile> loadVpnProfiles() {
        ArrayList newArrayList = Lists.newArrayList();
        for (String str : LegacyVpnProfileStore.list("VPN_")) {
            VpnProfile decode = VpnProfile.decode(str, LegacyVpnProfileStore.get("VPN_" + str));
            if (decode != null) {
                newArrayList.add(decode);
            }
        }
        return newArrayList;
    }
}
