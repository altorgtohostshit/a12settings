package com.android.settings.development;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.debug.IAdbManager;
import android.debug.PairDevice;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import com.android.settings.R;
import com.android.settings.SettingsActivity;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.development.AdbWirelessDialog;
import com.android.settings.development.WirelessDebuggingEnabler;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.widget.MainSwitchBarController;
import com.android.settings.widget.SettingsMainSwitchBar;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.development.DevelopmentSettingsEnabler;
import com.android.settingslib.widget.FooterPreference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WirelessDebuggingFragment extends DashboardFragment implements WirelessDebuggingEnabler.OnEnabledListener {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.adb_wireless_settings) {
        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            return DevelopmentSettingsEnabler.isDevelopmentSettingsEnabled(context);
        }
    };
    /* access modifiers changed from: private */
    public static AdbIpAddressPreferenceController sAdbIpAddressPreferenceController;
    /* access modifiers changed from: private */
    public IAdbManager mAdbManager;
    private Preference mCodePairingPreference;
    private int mConnectionPort;
    private Preference mDeviceNamePreference;
    private PreferenceCategory mFooterCategory;
    private IntentFilter mIntentFilter;
    /* access modifiers changed from: private */
    public Preference mIpAddrPreference;
    private FooterPreference mOffMessagePreference;
    private Map<String, AdbPairedDevicePreference> mPairedDevicePreferences;
    private PreferenceCategory mPairedDevicesCategory;
    /* access modifiers changed from: private */
    public AdbWirelessDialog mPairingCodeDialog;
    private final PairingCodeDialogListener mPairingCodeDialogListener = new PairingCodeDialogListener();
    private PreferenceCategory mPairingMethodsCategory;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("com.android.server.adb.WIRELESS_DEBUG_PAIRED_DEVICES".equals(action)) {
                WirelessDebuggingFragment.this.updatePairedDevicePreferences((HashMap) intent.getSerializableExtra("devices_map"));
            } else if ("com.android.server.adb.WIRELESS_DEBUG_STATUS".equals(action)) {
                int intExtra = intent.getIntExtra("status", 5);
                if (intExtra == 4 || intExtra == 5) {
                    WirelessDebuggingFragment.sAdbIpAddressPreferenceController.updateState(WirelessDebuggingFragment.this.mIpAddrPreference);
                }
            } else if ("com.android.server.adb.WIRELESS_DEBUG_PAIRING_RESULT".equals(action)) {
                Integer valueOf = Integer.valueOf(intent.getIntExtra("status", 0));
                if (valueOf.equals(3)) {
                    String stringExtra = intent.getStringExtra("pairing_code");
                    if (WirelessDebuggingFragment.this.mPairingCodeDialog != null) {
                        WirelessDebuggingFragment.this.mPairingCodeDialog.getController().setPairingCode(stringExtra);
                    }
                } else if (valueOf.equals(1)) {
                    WirelessDebuggingFragment.this.removeDialog(0);
                    AdbWirelessDialog unused = WirelessDebuggingFragment.this.mPairingCodeDialog = null;
                } else if (valueOf.equals(0)) {
                    WirelessDebuggingFragment.this.removeDialog(0);
                    AdbWirelessDialog unused2 = WirelessDebuggingFragment.this.mPairingCodeDialog = null;
                    WirelessDebuggingFragment.this.showDialog(2);
                } else if (valueOf.equals(4)) {
                    int intExtra2 = intent.getIntExtra("adb_port", 0);
                    Log.i("WirelessDebuggingFrag", "Got pairing code port=" + intExtra2);
                    String str = WirelessDebuggingFragment.sAdbIpAddressPreferenceController.getIpv4Address() + ":" + intExtra2;
                    if (WirelessDebuggingFragment.this.mPairingCodeDialog != null) {
                        WirelessDebuggingFragment.this.mPairingCodeDialog.getController().setIpAddr(str);
                    }
                }
            }
        }
    };
    private WirelessDebuggingEnabler mWifiDebuggingEnabler;

    public int getDialogMetricsCategory(int i) {
        return 1832;
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "WirelessDebuggingFrag";
    }

    public int getMetricsCategory() {
        return 1831;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.adb_wireless_settings;
    }

    class PairingCodeDialogListener implements AdbWirelessDialog.AdbWirelessDialogListener {
        PairingCodeDialogListener() {
        }

        public void onDismiss() {
            Log.i("WirelessDebuggingFrag", "onDismiss");
            AdbWirelessDialog unused = WirelessDebuggingFragment.this.mPairingCodeDialog = null;
            try {
                WirelessDebuggingFragment.this.mAdbManager.disablePairing();
            } catch (RemoteException unused2) {
                Log.e("WirelessDebuggingFrag", "Unable to cancel pairing");
            }
        }
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        ((AdbQrCodePreferenceController) use(AdbQrCodePreferenceController.class)).setParentFragment(this);
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        SettingsActivity settingsActivity = (SettingsActivity) getActivity();
        SettingsMainSwitchBar switchBar = settingsActivity.getSwitchBar();
        switchBar.setTitle(getContext().getString(R.string.wireless_debugging_main_switch_title));
        this.mWifiDebuggingEnabler = new WirelessDebuggingEnabler(settingsActivity, new MainSwitchBarController(switchBar), this, getSettingsLifecycle());
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        addPreferences();
        IntentFilter intentFilter = new IntentFilter("com.android.server.adb.WIRELESS_DEBUG_PAIRED_DEVICES");
        this.mIntentFilter = intentFilter;
        intentFilter.addAction("com.android.server.adb.WIRELESS_DEBUG_STATUS");
        this.mIntentFilter.addAction("com.android.server.adb.WIRELESS_DEBUG_PAIRING_RESULT");
    }

    private void addPreferences() {
        this.mDeviceNamePreference = findPreference("adb_device_name_pref");
        this.mIpAddrPreference = findPreference("adb_ip_addr_pref");
        this.mPairingMethodsCategory = (PreferenceCategory) findPreference("adb_pairing_methods_category");
        Preference findPreference = findPreference("adb_pair_method_code_pref");
        this.mCodePairingPreference = findPreference;
        findPreference.setOnPreferenceClickListener(new WirelessDebuggingFragment$$ExternalSyntheticLambda0(this));
        this.mPairedDevicesCategory = (PreferenceCategory) findPreference("adb_paired_devices_category");
        PreferenceCategory preferenceCategory = (PreferenceCategory) findPreference("adb_wireless_footer_category");
        this.mFooterCategory = preferenceCategory;
        this.mOffMessagePreference = new FooterPreference(preferenceCategory.getContext());
        this.mOffMessagePreference.setTitle(getText(R.string.adb_wireless_list_empty_off));
        this.mFooterCategory.addPreference(this.mOffMessagePreference);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$addPreferences$0(Preference preference) {
        showDialog(0);
        return true;
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.mWifiDebuggingEnabler.teardownSwitchController();
    }

    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(this.mReceiver, this.mIntentFilter);
    }

    public void onPause() {
        super.onPause();
        removeDialog(0);
        getActivity().unregisterReceiver(this.mReceiver);
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 0) {
            handlePairedDeviceRequest(i2, intent);
        } else if (i == 1) {
            handlePairingDeviceRequest(i2, intent);
        }
    }

    public Dialog onCreateDialog(int i) {
        AdbWirelessDialog createModal = AdbWirelessDialog.createModal(getActivity(), i == 0 ? this.mPairingCodeDialogListener : null, i);
        if (i == 0) {
            this.mPairingCodeDialog = createModal;
            try {
                this.mAdbManager.enablePairingByPairingCode();
            } catch (RemoteException unused) {
                Log.e("WirelessDebuggingFrag", "Unable to enable pairing");
                this.mPairingCodeDialog = null;
                createModal = AdbWirelessDialog.createModal(getActivity(), (AdbWirelessDialog.AdbWirelessDialogListener) null, 2);
            }
        }
        if (createModal != null) {
            return createModal;
        }
        return super.onCreateDialog(i);
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return buildPreferenceControllers(context, getActivity(), this, getSettingsLifecycle());
    }

    private static List<AbstractPreferenceController> buildPreferenceControllers(Context context, Activity activity, WirelessDebuggingFragment wirelessDebuggingFragment, Lifecycle lifecycle) {
        ArrayList arrayList = new ArrayList();
        AdbIpAddressPreferenceController adbIpAddressPreferenceController = new AdbIpAddressPreferenceController(context, lifecycle);
        sAdbIpAddressPreferenceController = adbIpAddressPreferenceController;
        arrayList.add(adbIpAddressPreferenceController);
        return arrayList;
    }

    public void onEnabled(boolean z) {
        if (z) {
            showDebuggingPreferences();
            IAdbManager asInterface = IAdbManager.Stub.asInterface(ServiceManager.getService("adb"));
            this.mAdbManager = asInterface;
            try {
                updatePairedDevicePreferences(asInterface.getPairedDevices());
                int adbWirelessPort = this.mAdbManager.getAdbWirelessPort();
                this.mConnectionPort = adbWirelessPort;
                if (adbWirelessPort > 0) {
                    Log.i("WirelessDebuggingFrag", "onEnabled(): connect_port=" + this.mConnectionPort);
                }
            } catch (RemoteException unused) {
                Log.e("WirelessDebuggingFrag", "Unable to request the paired list for Adb wireless");
            }
            sAdbIpAddressPreferenceController.updateState(this.mIpAddrPreference);
            return;
        }
        showOffMessage();
    }

    private void showOffMessage() {
        this.mDeviceNamePreference.setVisible(false);
        this.mIpAddrPreference.setVisible(false);
        this.mPairingMethodsCategory.setVisible(false);
        this.mPairedDevicesCategory.setVisible(false);
        this.mFooterCategory.setVisible(true);
    }

    private void showDebuggingPreferences() {
        this.mDeviceNamePreference.setVisible(true);
        this.mIpAddrPreference.setVisible(true);
        this.mPairingMethodsCategory.setVisible(true);
        this.mPairedDevicesCategory.setVisible(true);
        this.mFooterCategory.setVisible(false);
    }

    /* access modifiers changed from: private */
    public void updatePairedDevicePreferences(Map<String, PairDevice> map) {
        if (map == null) {
            this.mPairedDevicesCategory.removeAll();
            return;
        }
        if (this.mPairedDevicePreferences == null) {
            this.mPairedDevicePreferences = new HashMap();
        }
        if (this.mPairedDevicePreferences.isEmpty()) {
            for (Map.Entry next : map.entrySet()) {
                AdbPairedDevicePreference adbPairedDevicePreference = new AdbPairedDevicePreference((PairDevice) next.getValue(), this.mPairedDevicesCategory.getContext());
                this.mPairedDevicePreferences.put((String) next.getKey(), adbPairedDevicePreference);
                adbPairedDevicePreference.setOnPreferenceClickListener(new WirelessDebuggingFragment$$ExternalSyntheticLambda2(this));
                this.mPairedDevicesCategory.addPreference(adbPairedDevicePreference);
            }
            return;
        }
        this.mPairedDevicePreferences.entrySet().removeIf(new WirelessDebuggingFragment$$ExternalSyntheticLambda3(this, map));
        for (Map.Entry next2 : map.entrySet()) {
            if (this.mPairedDevicePreferences.get(next2.getKey()) == null) {
                AdbPairedDevicePreference adbPairedDevicePreference2 = new AdbPairedDevicePreference((PairDevice) next2.getValue(), this.mPairedDevicesCategory.getContext());
                this.mPairedDevicePreferences.put((String) next2.getKey(), adbPairedDevicePreference2);
                adbPairedDevicePreference2.setOnPreferenceClickListener(new WirelessDebuggingFragment$$ExternalSyntheticLambda1(this));
                this.mPairedDevicesCategory.addPreference(adbPairedDevicePreference2);
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$updatePairedDevicePreferences$1(Preference preference) {
        launchPairedDeviceDetailsFragment((AdbPairedDevicePreference) preference);
        return true;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$updatePairedDevicePreferences$2(Map map, Map.Entry entry) {
        if (map.get(entry.getKey()) == null) {
            this.mPairedDevicesCategory.removePreference((Preference) entry.getValue());
            return true;
        }
        AdbPairedDevicePreference adbPairedDevicePreference = (AdbPairedDevicePreference) entry.getValue();
        adbPairedDevicePreference.setPairedDevice((PairDevice) map.get(entry.getKey()));
        adbPairedDevicePreference.refresh();
        return false;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$updatePairedDevicePreferences$3(Preference preference) {
        launchPairedDeviceDetailsFragment((AdbPairedDevicePreference) preference);
        return true;
    }

    private void launchPairedDeviceDetailsFragment(AdbPairedDevicePreference adbPairedDevicePreference) {
        adbPairedDevicePreference.savePairedDeviceToExtras(adbPairedDevicePreference.getExtras());
        new SubSettingLauncher(getContext()).setTitleRes(R.string.adb_wireless_device_details_title).setDestination(AdbDeviceDetailsFragment.class.getName()).setArguments(adbPairedDevicePreference.getExtras()).setSourceMetricsCategory(getMetricsCategory()).setResultListener(this, 0).launch();
    }

    /* access modifiers changed from: package-private */
    public void handlePairedDeviceRequest(int i, Intent intent) {
        if (i == -1) {
            Log.i("WirelessDebuggingFrag", "Processing paired device request");
            if (intent.getIntExtra("request_type", -1) == 0) {
                try {
                    this.mAdbManager.unpairDevice(intent.getParcelableExtra("paired_device").getGuid());
                } catch (RemoteException unused) {
                    Log.e("WirelessDebuggingFrag", "Unable to forget the device");
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void handlePairingDeviceRequest(int i, Intent intent) {
        if (i == -1) {
            if (intent.getIntExtra("request_type_pairing", -1) != 1) {
                Log.d("WirelessDebuggingFrag", "Successfully paired device");
            } else {
                showDialog(2);
            }
        }
    }
}
