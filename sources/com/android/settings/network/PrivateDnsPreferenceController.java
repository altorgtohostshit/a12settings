package com.android.settings.network;

import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.net.ConnectivityManager;
import android.net.ConnectivitySettingsManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.internal.util.ArrayUtils;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import java.util.List;

public class PrivateDnsPreferenceController extends BasePreferenceController implements PreferenceControllerMixin, LifecycleObserver, OnStart, OnStop {
    private static final String KEY_PRIVATE_DNS_SETTINGS = "private_dns_settings";
    private static final Uri[] SETTINGS_URIS = {Settings.Global.getUriFor("private_dns_mode"), Settings.Global.getUriFor("private_dns_default_mode"), Settings.Global.getUriFor("private_dns_specifier")};
    private final ConnectivityManager mConnectivityManager;
    private final Handler mHandler;
    /* access modifiers changed from: private */
    public LinkProperties mLatestLinkProperties;
    private final ConnectivityManager.NetworkCallback mNetworkCallback = new ConnectivityManager.NetworkCallback() {
        public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
            LinkProperties unused = PrivateDnsPreferenceController.this.mLatestLinkProperties = linkProperties;
            if (PrivateDnsPreferenceController.this.mPreference != null) {
                PrivateDnsPreferenceController privateDnsPreferenceController = PrivateDnsPreferenceController.this;
                privateDnsPreferenceController.updateState(privateDnsPreferenceController.mPreference);
            }
        }

        public void onLost(Network network) {
            LinkProperties unused = PrivateDnsPreferenceController.this.mLatestLinkProperties = null;
            if (PrivateDnsPreferenceController.this.mPreference != null) {
                PrivateDnsPreferenceController privateDnsPreferenceController = PrivateDnsPreferenceController.this;
                privateDnsPreferenceController.updateState(privateDnsPreferenceController.mPreference);
            }
        }
    };
    /* access modifiers changed from: private */
    public Preference mPreference;
    private final ContentObserver mSettingsObserver;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public String getPreferenceKey() {
        return KEY_PRIVATE_DNS_SETTINGS;
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public PrivateDnsPreferenceController(Context context) {
        super(context, KEY_PRIVATE_DNS_SETTINGS);
        Handler handler = new Handler(Looper.getMainLooper());
        this.mHandler = handler;
        this.mSettingsObserver = new PrivateDnsSettingsObserver(handler);
        this.mConnectivityManager = (ConnectivityManager) context.getSystemService(ConnectivityManager.class);
    }

    public int getAvailabilityStatus() {
        return this.mContext.getResources().getBoolean(R.bool.config_show_private_dns_settings) ? 0 : 3;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    public void onStart() {
        for (Uri registerContentObserver : SETTINGS_URIS) {
            this.mContext.getContentResolver().registerContentObserver(registerContentObserver, false, this.mSettingsObserver);
        }
        Network activeNetwork = this.mConnectivityManager.getActiveNetwork();
        if (activeNetwork != null) {
            this.mLatestLinkProperties = this.mConnectivityManager.getLinkProperties(activeNetwork);
        }
        this.mConnectivityManager.registerDefaultNetworkCallback(this.mNetworkCallback, this.mHandler);
    }

    public void onStop() {
        this.mContext.getContentResolver().unregisterContentObserver(this.mSettingsObserver);
        this.mConnectivityManager.unregisterNetworkCallback(this.mNetworkCallback);
    }

    public CharSequence getSummary() {
        List list;
        Resources resources = this.mContext.getResources();
        ContentResolver contentResolver = this.mContext.getContentResolver();
        int privateDnsMode = ConnectivitySettingsManager.getPrivateDnsMode(this.mContext);
        LinkProperties linkProperties = this.mLatestLinkProperties;
        if (linkProperties == null) {
            list = null;
        } else {
            list = linkProperties.getValidatedPrivateDnsServers();
        }
        boolean z = !ArrayUtils.isEmpty(list);
        if (privateDnsMode == 1) {
            return resources.getString(R.string.private_dns_mode_off);
        }
        if (privateDnsMode != 2) {
            if (privateDnsMode != 3) {
                return "";
            }
            if (z) {
                return PrivateDnsModeDialogPreference.getHostnameFromSettings(contentResolver);
            }
            return resources.getString(R.string.private_dns_mode_provider_failure);
        } else if (z) {
            return resources.getString(R.string.private_dns_mode_on);
        } else {
            return resources.getString(R.string.private_dns_mode_opportunistic);
        }
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        preference.setEnabled(!isManagedByAdmin());
    }

    private boolean isManagedByAdmin() {
        return RestrictedLockUtilsInternal.checkIfRestrictionEnforced(this.mContext, "disallow_config_private_dns", UserHandle.myUserId()) != null;
    }

    private class PrivateDnsSettingsObserver extends ContentObserver {
        public PrivateDnsSettingsObserver(Handler handler) {
            super(handler);
        }

        public void onChange(boolean z) {
            if (PrivateDnsPreferenceController.this.mPreference != null) {
                PrivateDnsPreferenceController privateDnsPreferenceController = PrivateDnsPreferenceController.this;
                privateDnsPreferenceController.updateState(privateDnsPreferenceController.mPreference);
            }
        }
    }
}
