package com.android.settings.network;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothPan;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.TetheringManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.FeatureFlagUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.TetherUtil;
import com.android.settingslib.Utils;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnCreate;
import com.android.settingslib.core.lifecycle.events.OnDestroy;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;
import java.util.concurrent.atomic.AtomicReference;

public class TetherPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin, LifecycleObserver, OnCreate, OnResume, OnPause, OnDestroy {
    private final boolean mAdminDisallowedTetherConfig;
    private SettingObserver mAirplaneModeObserver;
    private final BluetoothAdapter mBluetoothAdapter;
    /* access modifiers changed from: private */
    public final AtomicReference<BluetoothPan> mBluetoothPan;
    final BluetoothProfile.ServiceListener mBtProfileServiceListener;
    private Preference mPreference;
    private TetherBroadcastReceiver mTetherReceiver;
    private final TetheringManager mTetheringManager;

    public String getPreferenceKey() {
        return "tether_settings";
    }

    TetherPreferenceController() {
        super((Context) null);
        this.mBtProfileServiceListener = new BluetoothProfile.ServiceListener() {
            public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
                TetherPreferenceController.this.mBluetoothPan.set((BluetoothPan) bluetoothProfile);
                TetherPreferenceController.this.updateSummary();
            }

            public void onServiceDisconnected(int i) {
                TetherPreferenceController.this.mBluetoothPan.set((Object) null);
            }
        };
        this.mAdminDisallowedTetherConfig = false;
        this.mBluetoothPan = new AtomicReference<>();
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mTetheringManager = null;
    }

    public TetherPreferenceController(Context context, Lifecycle lifecycle) {
        super(context);
        this.mBtProfileServiceListener = new BluetoothProfile.ServiceListener() {
            public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
                TetherPreferenceController.this.mBluetoothPan.set((BluetoothPan) bluetoothProfile);
                TetherPreferenceController.this.updateSummary();
            }

            public void onServiceDisconnected(int i) {
                TetherPreferenceController.this.mBluetoothPan.set((Object) null);
            }
        };
        this.mBluetoothPan = new AtomicReference<>();
        this.mAdminDisallowedTetherConfig = isTetherConfigDisallowed(context);
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mTetheringManager = (TetheringManager) context.getSystemService(TetheringManager.class);
        if (lifecycle != null) {
            lifecycle.addObserver(this);
        }
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        Preference findPreference = preferenceScreen.findPreference("tether_settings");
        this.mPreference = findPreference;
        if (findPreference != null && !this.mAdminDisallowedTetherConfig) {
            findPreference.setTitle(Utils.getTetheringLabel(this.mTetheringManager));
        }
    }

    public boolean isAvailable() {
        return TetherUtil.isTetherAvailable(this.mContext) && !FeatureFlagUtils.isEnabled(this.mContext, "settings_tether_all_in_one");
    }

    public void updateState(Preference preference) {
        updateSummary();
    }

    public void onCreate(Bundle bundle) {
        BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
        if (bluetoothAdapter != null && bluetoothAdapter.getState() == 12) {
            this.mBluetoothAdapter.getProfileProxy(this.mContext, this.mBtProfileServiceListener, 5);
        }
    }

    public void onResume() {
        if (this.mAirplaneModeObserver == null) {
            this.mAirplaneModeObserver = new SettingObserver();
        }
        if (this.mTetherReceiver == null) {
            this.mTetherReceiver = new TetherBroadcastReceiver();
        }
        this.mContext.registerReceiver(this.mTetherReceiver, new IntentFilter("android.net.conn.TETHER_STATE_CHANGED"));
        ContentResolver contentResolver = this.mContext.getContentResolver();
        SettingObserver settingObserver = this.mAirplaneModeObserver;
        contentResolver.registerContentObserver(settingObserver.uri, false, settingObserver);
    }

    public void onPause() {
        if (this.mAirplaneModeObserver != null) {
            this.mContext.getContentResolver().unregisterContentObserver(this.mAirplaneModeObserver);
        }
        TetherBroadcastReceiver tetherBroadcastReceiver = this.mTetherReceiver;
        if (tetherBroadcastReceiver != null) {
            this.mContext.unregisterReceiver(tetherBroadcastReceiver);
        }
    }

    public void onDestroy() {
        BluetoothAdapter bluetoothAdapter;
        BluetoothProfile andSet = this.mBluetoothPan.getAndSet((Object) null);
        if (andSet != null && (bluetoothAdapter = this.mBluetoothAdapter) != null) {
            bluetoothAdapter.closeProfileProxy(5, andSet);
        }
    }

    public static boolean isTetherConfigDisallowed(Context context) {
        return RestrictedLockUtilsInternal.checkIfRestrictionEnforced(context, "no_config_tethering", UserHandle.myUserId()) != null;
    }

    /* access modifiers changed from: package-private */
    public void updateSummary() {
        boolean z;
        boolean z2;
        BluetoothAdapter bluetoothAdapter;
        if (this.mPreference != null) {
            String[] tetheredIfaces = this.mTetheringManager.getTetheredIfaces();
            String[] tetherableWifiRegexs = this.mTetheringManager.getTetherableWifiRegexs();
            String[] tetherableBluetoothRegexs = this.mTetheringManager.getTetherableBluetoothRegexs();
            boolean z3 = false;
            if (tetheredIfaces != null) {
                if (tetherableWifiRegexs != null) {
                    z = false;
                    for (String str : tetheredIfaces) {
                        int length = tetherableWifiRegexs.length;
                        int i = 0;
                        while (true) {
                            if (i >= length) {
                                break;
                            } else if (str.matches(tetherableWifiRegexs[i])) {
                                z = true;
                                break;
                            } else {
                                i++;
                            }
                        }
                    }
                } else {
                    z = false;
                }
                z2 = tetheredIfaces.length > 1 ? true : tetheredIfaces.length == 1 ? !z : false;
            } else {
                z2 = false;
                z = false;
            }
            if (!z2 && tetherableBluetoothRegexs != null && tetherableBluetoothRegexs.length > 0 && (bluetoothAdapter = this.mBluetoothAdapter) != null && bluetoothAdapter.getState() == 12) {
                BluetoothPan bluetoothPan = this.mBluetoothPan.get();
                if (bluetoothPan != null && bluetoothPan.isTetheringOn()) {
                    z3 = true;
                }
                z2 = z3;
            }
            if (!z && !z2) {
                this.mPreference.setSummary((int) R.string.switch_off_text);
            } else if (z && z2) {
                this.mPreference.setSummary((int) R.string.tether_settings_summary_hotspot_on_tether_on);
            } else if (z) {
                this.mPreference.setSummary((int) R.string.tether_settings_summary_hotspot_on_tether_off);
            } else {
                this.mPreference.setSummary((int) R.string.tether_settings_summary_hotspot_off_tether_on);
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateSummaryToOff() {
        Preference preference = this.mPreference;
        if (preference != null) {
            preference.setSummary((int) R.string.switch_off_text);
        }
    }

    class SettingObserver extends ContentObserver {
        public final Uri uri = Settings.Global.getUriFor("airplane_mode_on");

        public SettingObserver() {
            super(new Handler());
        }

        public void onChange(boolean z, Uri uri2) {
            super.onChange(z, uri2);
            if (this.uri.equals(uri2)) {
                boolean z2 = false;
                if (Settings.Global.getInt(TetherPreferenceController.this.mContext.getContentResolver(), "airplane_mode_on", 0) != 0) {
                    z2 = true;
                }
                if (z2) {
                    TetherPreferenceController.this.updateSummaryToOff();
                }
            }
        }
    }

    class TetherBroadcastReceiver extends BroadcastReceiver {
        TetherBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            TetherPreferenceController.this.updateSummary();
        }
    }
}
