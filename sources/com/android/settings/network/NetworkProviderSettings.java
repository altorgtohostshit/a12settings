package com.android.settings.network;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.NetworkTemplate;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.PowerManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.FeatureFlagUtils;
import android.util.Log;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.R$styleable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.AirplaneModeEnabler;
import com.android.settings.R;
import com.android.settings.RestrictedSettingsFragment;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.datausage.DataUsagePreference;
import com.android.settings.datausage.DataUsageUtils;
import com.android.settings.datausage.TemplatePreference;
import com.android.settings.network.InternetUpdater;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.wifi.AddNetworkFragment;
import com.android.settings.wifi.AddWifiNetworkPreference;
import com.android.settings.wifi.ConfigureWifiEntryFragment;
import com.android.settings.wifi.ConnectedWifiEntryPreference;
import com.android.settings.wifi.WifiConnectListener;
import com.android.settings.wifi.WifiDialog2;
import com.android.settings.wifi.WifiPickerTrackerHelper;
import com.android.settings.wifi.WifiUtils;
import com.android.settings.wifi.details2.WifiNetworkDetailsFragment2;
import com.android.settings.wifi.dpp.WifiDppUtils;
import com.android.settingslib.HelpUtils;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.connectivity.ConnectivitySubsystemsRecoveryManager;
import com.android.settingslib.utils.ThreadUtils;
import com.android.settingslib.widget.LayoutPreference;
import com.android.settingslib.wifi.LongPressWifiEntryPreference;
import com.android.settingslib.wifi.WifiEntryPreference;
import com.android.settingslib.wifi.WifiSavedConfigUtils;
import com.android.wifitrackerlib.BaseWifiTracker;
import com.android.wifitrackerlib.WifiEntry;
import com.android.wifitrackerlib.WifiPickerTracker;
import java.util.List;
import java.util.Optional;

public class NetworkProviderSettings extends RestrictedSettingsFragment implements WifiPickerTracker.WifiPickerTrackerCallback, WifiDialog2.WifiDialog2Listener, DialogInterface.OnDismissListener, ConnectivitySubsystemsRecoveryManager.RecoveryStatusCallback, AirplaneModeEnabler.OnAirplaneModeChangedListener, InternetUpdater.InternetChangeListener {
    static final int ADD_NETWORK_REQUEST = 2;
    static final int MENU_ID_DISCONNECT = 3;
    static final int MENU_ID_FORGET = 4;
    static final String PREF_KEY_CONNECTED_ACCESS_POINTS = "connected_access_point";
    static final String PREF_KEY_DATA_USAGE = "non_carrier_data_usage";
    static final String PREF_KEY_FIRST_ACCESS_POINTS = "first_access_points";
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.network_provider_settings) {
        public List<String> getNonIndexableKeys(Context context) {
            List<String> nonIndexableKeys = super.getNonIndexableKeys(context);
            if (WifiSavedConfigUtils.getAllConfigsCount(context, (WifiManager) context.getSystemService(WifiManager.class)) == 0) {
                nonIndexableKeys.add("saved_networks");
            }
            if (!DataUsageUtils.hasWifiRadio(context)) {
                nonIndexableKeys.add(NetworkProviderSettings.PREF_KEY_DATA_USAGE);
            }
            return nonIndexableKeys;
        }
    };
    AddWifiNetworkPreference mAddWifiNetworkPreference;
    AirplaneModeEnabler mAirplaneModeEnabler;
    Preference mAirplaneModeMsgPreference;
    /* access modifiers changed from: private */
    public boolean mClickedConnect;
    Preference mConfigureWifiSettingsPreference;
    private WifiManager.ActionListener mConnectListener;
    ConnectedEthernetNetworkController mConnectedEthernetNetworkController;
    PreferenceCategory mConnectedWifiEntryPreferenceCategory;
    protected ConnectivitySubsystemsRecoveryManager mConnectivitySubsystemsRecoveryManager;
    DataUsagePreference mDataUsagePreference;
    private WifiDialog2 mDialog;
    private int mDialogMode;
    private WifiEntry mDialogWifiEntry;
    private String mDialogWifiEntryKey;
    private boolean mEnableNextOnConnection;
    PreferenceCategory mFirstWifiEntryPreferenceCategory;
    private WifiManager.ActionListener mForgetListener;
    private final Runnable mHideProgressBarRunnable = new NetworkProviderSettings$$ExternalSyntheticLambda3(this);
    InternetUpdater mInternetUpdater;
    private boolean mIsRestricted;
    private NetworkMobileProviderController mNetworkMobileProviderController;
    private String mOpenSsid;
    private View mProgressHeader;
    private HandlerThread mRecoveryThread;
    LayoutPreference mResetInternetPreference;
    private WifiManager.ActionListener mSaveListener;
    Preference mSavedNetworksPreference;
    private WifiEntry mSelectedWifiEntry;
    private final Runnable mUpdateWifiEntryPreferencesRunnable = new NetworkProviderSettings$$ExternalSyntheticLambda5(this);
    private PreferenceCategory mWifiEntryPreferenceCategory;
    protected WifiManager mWifiManager;
    WifiPickerTracker mWifiPickerTracker;
    private WifiPickerTrackerHelper mWifiPickerTrackerHelper;
    private WifiSwitchPreferenceController mWifiSwitchPreferenceController;

    public int getDialogMetricsCategory(int i) {
        return i != 1 ? 0 : 603;
    }

    public int getHelpResource() {
        return R.string.help_url_wifi;
    }

    public int getMetricsCategory() {
        return R$styleable.Constraint_layout_goneMarginTop;
    }

    private static boolean isVerboseLoggingEnabled() {
        return BaseWifiTracker.isVerboseLoggingEnabled();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        setProgressBarVisible(false);
    }

    public NetworkProviderSettings() {
        super("no_config_wifi");
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (getActivity() != null) {
            this.mProgressHeader = setPinnedHeaderView((int) R.layout.progress_header).findViewById(R.id.progress_bar_animation);
            setProgressBarVisible(false);
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mAirplaneModeEnabler = new AirplaneModeEnabler(getContext(), this);
        setAnimationAllowed(false);
        addPreferences();
        this.mIsRestricted = isUiRestricted();
    }

    private void addPreferences() {
        addPreferencesFromResource(R.xml.network_provider_settings);
        this.mAirplaneModeMsgPreference = findPreference("airplane_mode_message");
        updateAirplaneModeMsgPreference(this.mAirplaneModeEnabler.isAirplaneModeOn());
        this.mConnectedWifiEntryPreferenceCategory = (PreferenceCategory) findPreference(PREF_KEY_CONNECTED_ACCESS_POINTS);
        this.mFirstWifiEntryPreferenceCategory = (PreferenceCategory) findPreference(PREF_KEY_FIRST_ACCESS_POINTS);
        this.mWifiEntryPreferenceCategory = (PreferenceCategory) findPreference("access_points");
        this.mConfigureWifiSettingsPreference = findPreference("configure_wifi_settings");
        this.mSavedNetworksPreference = findPreference("saved_networks");
        this.mAddWifiNetworkPreference = new AddWifiNetworkPreference(getPrefContext());
        DataUsagePreference dataUsagePreference = (DataUsagePreference) findPreference(PREF_KEY_DATA_USAGE);
        this.mDataUsagePreference = dataUsagePreference;
        dataUsagePreference.setVisible(DataUsageUtils.hasWifiRadio(getContext()));
        this.mDataUsagePreference.setTemplate(NetworkTemplate.buildTemplateWifi(NetworkTemplate.WIFI_NETWORKID_ALL, (String) null), 0, (TemplatePreference.NetworkServices) null);
        LayoutPreference layoutPreference = (LayoutPreference) findPreference("resetting_your_internet");
        this.mResetInternetPreference = layoutPreference;
        if (layoutPreference != null) {
            layoutPreference.setVisible(false);
        }
        addNetworkMobileProviderController();
        addConnectedEthernetNetworkController();
        addWifiSwitchPreferenceController();
    }

    private void updateAirplaneModeMsgPreference(boolean z) {
        Preference preference = this.mAirplaneModeMsgPreference;
        if (preference != null) {
            preference.setVisible(z);
        }
    }

    private void addNetworkMobileProviderController() {
        if (this.mNetworkMobileProviderController == null) {
            this.mNetworkMobileProviderController = new NetworkMobileProviderController(getContext(), NetworkMobileProviderController.PREF_KEY_PROVIDER_MOBILE_NETWORK);
        }
        this.mNetworkMobileProviderController.init(getSettingsLifecycle());
        this.mNetworkMobileProviderController.displayPreference(getPreferenceScreen());
    }

    private void addConnectedEthernetNetworkController() {
        if (this.mConnectedEthernetNetworkController == null) {
            this.mConnectedEthernetNetworkController = new ConnectedEthernetNetworkController(getContext(), getSettingsLifecycle());
        }
        this.mConnectedEthernetNetworkController.displayPreference(getPreferenceScreen());
    }

    private void addWifiSwitchPreferenceController() {
        if (this.mWifiSwitchPreferenceController == null) {
            this.mWifiSwitchPreferenceController = new WifiSwitchPreferenceController(getContext(), getSettingsLifecycle());
        }
        this.mWifiSwitchPreferenceController.displayPreference(getPreferenceScreen());
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        WifiPickerTrackerHelper wifiPickerTrackerHelper = new WifiPickerTrackerHelper(getSettingsLifecycle(), getContext(), this);
        this.mWifiPickerTrackerHelper = wifiPickerTrackerHelper;
        this.mWifiPickerTracker = wifiPickerTrackerHelper.getWifiPickerTracker();
        this.mInternetUpdater = new InternetUpdater(getContext(), getSettingsLifecycle(), this);
        if (getActivity() != null) {
            this.mWifiManager = (WifiManager) getActivity().getSystemService(WifiManager.class);
        }
        this.mConnectListener = new WifiConnectListener(getActivity());
        this.mSaveListener = new WifiManager.ActionListener() {
            public void onSuccess() {
            }

            public void onFailure(int i) {
                FragmentActivity activity = NetworkProviderSettings.this.getActivity();
                if (activity != null) {
                    Toast.makeText(activity, R.string.wifi_failed_save_message, 0).show();
                }
            }
        };
        this.mForgetListener = new WifiManager.ActionListener() {
            public void onSuccess() {
            }

            public void onFailure(int i) {
                FragmentActivity activity = NetworkProviderSettings.this.getActivity();
                if (activity != null) {
                    Toast.makeText(activity, R.string.wifi_failed_forget_message, 0).show();
                }
            }
        };
        setHasOptionsMenu(true);
        if (bundle != null) {
            this.mDialogMode = bundle.getInt("dialog_mode");
            this.mDialogWifiEntryKey = bundle.getString("wifi_ap_key");
        }
        Intent intent = getActivity().getIntent();
        this.mEnableNextOnConnection = intent.getBooleanExtra("wifi_enable_next_on_connect", false);
        if (intent.hasExtra("wifi_start_connect_ssid")) {
            this.mOpenSsid = intent.getStringExtra("wifi_start_connect_ssid");
        }
        NetworkMobileProviderController networkMobileProviderController = this.mNetworkMobileProviderController;
        if (networkMobileProviderController != null) {
            networkMobileProviderController.setWifiPickerTrackerHelper(this.mWifiPickerTrackerHelper);
        }
    }

    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void onStart() {
        super.onStart();
        if (this.mIsRestricted) {
            restrictUi();
        } else {
            this.mAirplaneModeEnabler.start();
        }
    }

    private void restrictUi() {
        if (!isUiRestrictedByOnlyAdmin()) {
            getEmptyTextView().setText(R.string.wifi_empty_list_user_restricted);
        }
        getPreferenceScreen().removeAll();
    }

    public void onResume() {
        getActivity();
        super.onResume();
        boolean z = this.mIsRestricted;
        boolean isUiRestricted = isUiRestricted();
        this.mIsRestricted = isUiRestricted;
        if (!z && isUiRestricted) {
            restrictUi();
        }
        changeNextButtonState(this.mWifiPickerTracker.getConnectedWifiEntry() != null);
    }

    public void onStop() {
        getView().removeCallbacks(this.mUpdateWifiEntryPreferencesRunnable);
        getView().removeCallbacks(this.mHideProgressBarRunnable);
        this.mAirplaneModeEnabler.stop();
        super.onStop();
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        WifiConfiguration wifiConfiguration;
        WifiDialog2 wifiDialog2;
        super.onActivityResult(i, i2, intent);
        if (i == 2) {
            handleAddNetworkRequest(i2, intent);
        } else if (i == 0) {
            if (i2 == -1 && (wifiDialog2 = this.mDialog) != null) {
                wifiDialog2.dismiss();
            }
        } else if (i == 3) {
            if (i2 == -1 && (wifiConfiguration = (WifiConfiguration) intent.getParcelableExtra("network_config_key")) != null) {
                this.mWifiManager.connect(wifiConfiguration, new WifiConnectActionListener());
            }
        } else if (i != 4) {
            boolean z = this.mIsRestricted;
            boolean isUiRestricted = isUiRestricted();
            this.mIsRestricted = isUiRestricted;
            if (z && !isUiRestricted && getPreferenceScreen().getPreferenceCount() == 0) {
                addPreferences();
            }
        }
    }

    /* access modifiers changed from: protected */
    public RecyclerView.Adapter onCreateAdapter(PreferenceScreen preferenceScreen) {
        RecyclerView.Adapter onCreateAdapter = super.onCreateAdapter(preferenceScreen);
        onCreateAdapter.setHasStableIds(true);
        return onCreateAdapter;
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (this.mDialog != null) {
            bundle.putInt("dialog_mode", this.mDialogMode);
            bundle.putString("wifi_ap_key", this.mDialogWifiEntryKey);
        }
    }

    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        Preference preference = (Preference) view.getTag();
        if (preference instanceof LongPressWifiEntryPreference) {
            WifiEntry wifiEntry = ((LongPressWifiEntryPreference) preference).getWifiEntry();
            this.mSelectedWifiEntry = wifiEntry;
            contextMenu.setHeaderTitle(wifiEntry.getTitle());
            if (this.mSelectedWifiEntry.canConnect()) {
                contextMenu.add(0, 2, 0, R.string.wifi_connect);
            }
            if (this.mSelectedWifiEntry.canDisconnect()) {
                contextMenu.add(0, 3, 0, R.string.wifi_disconnect_button_text);
            }
            if (canForgetNetwork()) {
                contextMenu.add(0, 4, 0, R.string.forget);
            }
            if (!WifiUtils.isNetworkLockedDown(getActivity(), this.mSelectedWifiEntry.getWifiConfiguration()) && this.mSelectedWifiEntry.isSaved() && this.mSelectedWifiEntry.getConnectedState() != 2) {
                contextMenu.add(0, 5, 0, R.string.wifi_modify);
            }
        }
    }

    private boolean canForgetNetwork() {
        return this.mSelectedWifiEntry.canForget() && !WifiUtils.isNetworkLockedDown(getActivity(), this.mSelectedWifiEntry.getWifiConfiguration());
    }

    public boolean onContextItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 2) {
            connect(this.mSelectedWifiEntry, true, false);
            return true;
        } else if (itemId == 3) {
            this.mSelectedWifiEntry.disconnect((WifiEntry.DisconnectCallback) null);
            return true;
        } else if (itemId == 4) {
            forget(this.mSelectedWifiEntry);
            return true;
        } else if (itemId != 5) {
            return super.onContextItemSelected(menuItem);
        } else {
            showDialog(this.mSelectedWifiEntry, 2);
            return true;
        }
    }

    public boolean onPreferenceTreeClick(Preference preference) {
        if (preference.getFragment() != null) {
            preference.setOnPreferenceClickListener((Preference.OnPreferenceClickListener) null);
            return super.onPreferenceTreeClick(preference);
        }
        if (preference instanceof LongPressWifiEntryPreference) {
            WifiEntry wifiEntry = ((LongPressWifiEntryPreference) preference).getWifiEntry();
            if (wifiEntry.shouldEditBeforeConnect()) {
                launchConfigNewNetworkFragment(wifiEntry);
                return true;
            }
            connect(wifiEntry, true, true);
        } else if (preference != this.mAddWifiNetworkPreference) {
            return super.onPreferenceTreeClick(preference);
        } else {
            onAddNetworkPressed();
        }
        return true;
    }

    /* access modifiers changed from: private */
    public void showDialog(WifiEntry wifiEntry, int i) {
        if (!WifiUtils.isNetworkLockedDown(getActivity(), wifiEntry.getWifiConfiguration()) || wifiEntry.getConnectedState() != 2) {
            if (this.mDialog != null) {
                removeDialog(1);
                this.mDialog = null;
            }
            this.mDialogWifiEntry = wifiEntry;
            this.mDialogWifiEntryKey = wifiEntry.getKey();
            this.mDialogMode = i;
            showDialog(1);
            return;
        }
        RestrictedLockUtils.sendShowAdminSupportDetailsIntent(getActivity(), RestrictedLockUtilsInternal.getDeviceOwner(getActivity()));
    }

    public Dialog onCreateDialog(int i) {
        if (i != 1) {
            return super.onCreateDialog(i);
        }
        WifiDialog2 createModal = WifiDialog2.createModal(getActivity(), this, this.mDialogWifiEntry, this.mDialogMode);
        this.mDialog = createModal;
        return createModal;
    }

    public void onDialogShowing() {
        super.onDialogShowing();
        setOnDismissListener(this);
    }

    public void onDismiss(DialogInterface dialogInterface) {
        this.mDialog = null;
        this.mDialogWifiEntry = null;
        this.mDialogWifiEntryKey = null;
    }

    public void onInternetTypeChanged(int i) {
        ThreadUtils.postOnMainThread(new NetworkProviderSettings$$ExternalSyntheticLambda4(this));
    }

    /* renamed from: onWifiStateChanged */
    public void lambda$onInternetTypeChanged$2() {
        if (!this.mIsRestricted) {
            int wifiState = this.mWifiPickerTracker.getWifiState();
            if (isVerboseLoggingEnabled()) {
                Log.i("NetworkProviderSettings", "onWifiStateChanged called with wifi state: " + wifiState);
            }
            if (wifiState == 0) {
                removeConnectedWifiEntryPreference();
                removeWifiEntryPreference();
            } else if (wifiState == 1) {
                removeConnectedWifiEntryPreference();
                removeWifiEntryPreference();
                setAdditionalSettingsSummaries();
                setProgressBarVisible(false);
                this.mClickedConnect = false;
            } else if (wifiState == 2) {
                removeConnectedWifiEntryPreference();
                removeWifiEntryPreference();
                setProgressBarVisible(true);
            } else if (wifiState == 3) {
                lambda$new$0();
            }
        }
    }

    public void onWifiEntriesChanged() {
        updateWifiEntryPreferencesDelayed();
        changeNextButtonState(this.mWifiPickerTracker.getConnectedWifiEntry() != null);
        if (this.mOpenSsid != null) {
            Optional findFirst = this.mWifiPickerTracker.getWifiEntries().stream().filter(new NetworkProviderSettings$$ExternalSyntheticLambda6(this)).filter(NetworkProviderSettings$$ExternalSyntheticLambda7.INSTANCE).filter(NetworkProviderSettings$$ExternalSyntheticLambda8.INSTANCE).findFirst();
            if (findFirst.isPresent()) {
                this.mOpenSsid = null;
                launchConfigNewNetworkFragment((WifiEntry) findFirst.get());
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onWifiEntriesChanged$3(WifiEntry wifiEntry) {
        return TextUtils.equals(this.mOpenSsid, wifiEntry.getSsid());
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$onWifiEntriesChanged$4(WifiEntry wifiEntry) {
        return (wifiEntry.getSecurity() == 0 || wifiEntry.getSecurity() == 4) ? false : true;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$onWifiEntriesChanged$5(WifiEntry wifiEntry) {
        return !wifiEntry.isSaved() || isDisabledByWrongPassword(wifiEntry);
    }

    public void onNumSavedNetworksChanged() {
        if (!isFinishingOrDestroyed()) {
            setAdditionalSettingsSummaries();
        }
    }

    public void onNumSavedSubscriptionsChanged() {
        if (!isFinishingOrDestroyed()) {
            setAdditionalSettingsSummaries();
        }
    }

    private void updateWifiEntryPreferencesDelayed() {
        if (getActivity() != null && !this.mIsRestricted && this.mWifiPickerTracker.getWifiState() == 3) {
            View view = getView();
            Handler handler = view.getHandler();
            if (handler == null || !handler.hasCallbacks(this.mUpdateWifiEntryPreferencesRunnable)) {
                setProgressBarVisible(true);
                view.postDelayed(this.mUpdateWifiEntryPreferencesRunnable, 300);
            }
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: updateWifiEntryPreferences */
    public void lambda$new$0() {
        if (this.mWifiPickerTracker.getWifiState() == 3) {
            this.mWifiEntryPreferenceCategory.setVisible(true);
            WifiEntry connectedWifiEntry = this.mWifiPickerTracker.getConnectedWifiEntry();
            PreferenceCategory connectedWifiPreferenceCategory = getConnectedWifiPreferenceCategory();
            connectedWifiPreferenceCategory.setVisible(connectedWifiEntry != null);
            if (connectedWifiEntry != null) {
                LongPressWifiEntryPreference longPressWifiEntryPreference = (LongPressWifiEntryPreference) connectedWifiPreferenceCategory.findPreference(connectedWifiEntry.getKey());
                if (longPressWifiEntryPreference == null || longPressWifiEntryPreference.getWifiEntry() != connectedWifiEntry) {
                    connectedWifiPreferenceCategory.removeAll();
                    ConnectedWifiEntryPreference createConnectedWifiEntryPreference = createConnectedWifiEntryPreference(connectedWifiEntry);
                    createConnectedWifiEntryPreference.setKey(connectedWifiEntry.getKey());
                    createConnectedWifiEntryPreference.refresh();
                    connectedWifiPreferenceCategory.addPreference(createConnectedWifiEntryPreference);
                    createConnectedWifiEntryPreference.setOnPreferenceClickListener(new NetworkProviderSettings$$ExternalSyntheticLambda0(this, connectedWifiEntry, createConnectedWifiEntryPreference));
                    createConnectedWifiEntryPreference.setOnGearClickListener(new NetworkProviderSettings$$ExternalSyntheticLambda1(this, createConnectedWifiEntryPreference));
                    if (this.mClickedConnect) {
                        this.mClickedConnect = false;
                        scrollToPreference(connectedWifiPreferenceCategory);
                    }
                }
            } else {
                connectedWifiPreferenceCategory.removeAll();
            }
            cacheRemoveAllPrefs(this.mWifiEntryPreferenceCategory);
            boolean z = false;
            int i = 0;
            for (WifiEntry next : this.mWifiPickerTracker.getWifiEntries()) {
                String key = next.getKey();
                LongPressWifiEntryPreference longPressWifiEntryPreference2 = (LongPressWifiEntryPreference) getCachedPreference(key);
                if (longPressWifiEntryPreference2 != null) {
                    if (longPressWifiEntryPreference2.getWifiEntry() == next) {
                        longPressWifiEntryPreference2.setOrder(i);
                        i++;
                        z = true;
                    } else {
                        removePreference(key);
                    }
                }
                LongPressWifiEntryPreference createLongPressWifiEntryPreference = createLongPressWifiEntryPreference(next);
                createLongPressWifiEntryPreference.setKey(next.getKey());
                int i2 = i + 1;
                createLongPressWifiEntryPreference.setOrder(i);
                createLongPressWifiEntryPreference.refresh();
                if (next.getHelpUriString() != null) {
                    createLongPressWifiEntryPreference.setOnButtonClickListener(new NetworkProviderSettings$$ExternalSyntheticLambda2(this, next));
                }
                this.mWifiEntryPreferenceCategory.addPreference(createLongPressWifiEntryPreference);
                z = true;
                i = i2;
            }
            removeCachedPrefs(this.mWifiEntryPreferenceCategory);
            if (!z) {
                setProgressBarVisible(true);
                Preference preference = new Preference(getPrefContext());
                preference.setSelectable(false);
                preference.setSummary((int) R.string.wifi_empty_list_wifi_on);
                preference.setOrder(i);
                preference.setKey("wifi_empty_list");
                this.mWifiEntryPreferenceCategory.addPreference(preference);
                i++;
            } else {
                getView().postDelayed(this.mHideProgressBarRunnable, 1700);
            }
            this.mAddWifiNetworkPreference.setOrder(i);
            this.mWifiEntryPreferenceCategory.addPreference(this.mAddWifiNetworkPreference);
            setAdditionalSettingsSummaries();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$updateWifiEntryPreferences$6(WifiEntry wifiEntry, ConnectedWifiEntryPreference connectedWifiEntryPreference, Preference preference) {
        if (wifiEntry.canSignIn()) {
            wifiEntry.signIn((WifiEntry.SignInCallback) null);
            return true;
        }
        launchNetworkDetailsFragment(connectedWifiEntryPreference);
        return true;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateWifiEntryPreferences$7(ConnectedWifiEntryPreference connectedWifiEntryPreference, ConnectedWifiEntryPreference connectedWifiEntryPreference2) {
        launchNetworkDetailsFragment(connectedWifiEntryPreference);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateWifiEntryPreferences$8(WifiEntry wifiEntry, WifiEntryPreference wifiEntryPreference) {
        openSubscriptionHelpPage(wifiEntry);
    }

    /* access modifiers changed from: package-private */
    public PreferenceCategory getConnectedWifiPreferenceCategory() {
        if (this.mInternetUpdater.getInternetType() == 2) {
            this.mFirstWifiEntryPreferenceCategory.setVisible(false);
            this.mFirstWifiEntryPreferenceCategory.removeAll();
            return this.mConnectedWifiEntryPreferenceCategory;
        }
        this.mConnectedWifiEntryPreferenceCategory.setVisible(false);
        this.mConnectedWifiEntryPreferenceCategory.removeAll();
        return this.mFirstWifiEntryPreferenceCategory;
    }

    /* access modifiers changed from: package-private */
    public ConnectedWifiEntryPreference createConnectedWifiEntryPreference(WifiEntry wifiEntry) {
        if (this.mInternetUpdater.getInternetType() == 2) {
            return new ConnectedWifiEntryPreference(getPrefContext(), wifiEntry, this);
        }
        return new FirstWifiEntryPreference(getPrefContext(), wifiEntry, this);
    }

    private void launchNetworkDetailsFragment(LongPressWifiEntryPreference longPressWifiEntryPreference) {
        CharSequence charSequence;
        WifiEntry wifiEntry = longPressWifiEntryPreference.getWifiEntry();
        Context context = getContext();
        if (FeatureFlagUtils.isEnabled(context, "settings_wifi_details_datausage_header")) {
            charSequence = wifiEntry.getTitle();
        } else {
            charSequence = context.getText(R.string.pref_title_network_details);
        }
        Bundle bundle = new Bundle();
        bundle.putString("key_chosen_wifientry_key", wifiEntry.getKey());
        new SubSettingLauncher(context).setTitleText(charSequence).setDestination(WifiNetworkDetailsFragment2.class.getName()).setArguments(bundle).setSourceMetricsCategory(getMetricsCategory()).launch();
    }

    /* access modifiers changed from: package-private */
    public LongPressWifiEntryPreference createLongPressWifiEntryPreference(WifiEntry wifiEntry) {
        return new LongPressWifiEntryPreference(getPrefContext(), wifiEntry, this);
    }

    private void launchAddNetworkFragment() {
        new SubSettingLauncher(getContext()).setTitleRes(R.string.wifi_add_network).setDestination(AddNetworkFragment.class.getName()).setSourceMetricsCategory(getMetricsCategory()).setResultListener(this, 2).launch();
    }

    private void removeConnectedWifiEntryPreference() {
        this.mConnectedWifiEntryPreferenceCategory.removeAll();
        this.mConnectedWifiEntryPreferenceCategory.setVisible(false);
    }

    private void removeWifiEntryPreference() {
        this.mWifiEntryPreferenceCategory.removeAll();
        this.mWifiEntryPreferenceCategory.setVisible(false);
    }

    /* access modifiers changed from: package-private */
    public void setAdditionalSettingsSummaries() {
        if (!FeatureFlagUtils.isEnabled(getContext(), "settings_provider_model")) {
            this.mConfigureWifiSettingsPreference.setSummary((CharSequence) getString(isWifiWakeupEnabled() ? R.string.wifi_configure_settings_preference_summary_wakeup_on : R.string.wifi_configure_settings_preference_summary_wakeup_off));
        }
        int numSavedNetworks = this.mWifiPickerTracker.getNumSavedNetworks();
        int numSavedSubscriptions = this.mWifiPickerTracker.getNumSavedSubscriptions();
        if (numSavedNetworks + numSavedSubscriptions > 0) {
            this.mSavedNetworksPreference.setVisible(true);
            this.mSavedNetworksPreference.setSummary((CharSequence) getSavedNetworkSettingsSummaryText(numSavedNetworks, numSavedSubscriptions));
            return;
        }
        this.mSavedNetworksPreference.setVisible(false);
    }

    private String getSavedNetworkSettingsSummaryText(int i, int i2) {
        if (i2 == 0) {
            return getResources().getQuantityString(R.plurals.wifi_saved_access_points_summary, i, new Object[]{Integer.valueOf(i)});
        } else if (i == 0) {
            return getResources().getQuantityString(R.plurals.wifi_saved_passpoint_access_points_summary, i2, new Object[]{Integer.valueOf(i2)});
        } else {
            int i3 = i + i2;
            return getResources().getQuantityString(R.plurals.wifi_saved_all_access_points_summary, i3, new Object[]{Integer.valueOf(i3)});
        }
    }

    private boolean isWifiWakeupEnabled() {
        Context context = getContext();
        PowerManager powerManager = (PowerManager) context.getSystemService(PowerManager.class);
        ContentResolver contentResolver = context.getContentResolver();
        if (!this.mWifiManager.isAutoWakeupEnabled() || !this.mWifiManager.isScanAlwaysAvailable() || Settings.Global.getInt(contentResolver, "airplane_mode_on", 0) != 0 || powerManager.isPowerSaveMode()) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void setProgressBarVisible(boolean z) {
        View view = this.mProgressHeader;
        if (view != null) {
            view.setVisibility(z ? 0 : 8);
        }
    }

    /* access modifiers changed from: package-private */
    public void handleAddNetworkRequest(int i, Intent intent) {
        if (i == -1) {
            handleAddNetworkSubmitEvent(intent);
        }
    }

    private void handleAddNetworkSubmitEvent(Intent intent) {
        WifiConfiguration wifiConfiguration = (WifiConfiguration) intent.getParcelableExtra("wifi_config_key");
        if (wifiConfiguration != null) {
            this.mWifiManager.save(wifiConfiguration, this.mSaveListener);
        }
    }

    private void onAddNetworkPressed() {
        launchAddNetworkFragment();
    }

    /* access modifiers changed from: package-private */
    public void changeNextButtonState(boolean z) {
        if (this.mEnableNextOnConnection && hasNextButton()) {
            getNextButton().setEnabled(z);
        }
    }

    public void onForget(WifiDialog2 wifiDialog2) {
        forget(wifiDialog2.getWifiEntry());
    }

    public void onSubmit(WifiDialog2 wifiDialog2) {
        int mode = wifiDialog2.getMode();
        WifiConfiguration config = wifiDialog2.getController().getConfig();
        WifiEntry wifiEntry = wifiDialog2.getWifiEntry();
        if (mode == 2) {
            if (config == null) {
                Toast.makeText(getContext(), R.string.wifi_failed_save_message, 0).show();
            } else {
                this.mWifiManager.save(config, this.mSaveListener);
            }
        } else if (mode != 1 && (mode != 0 || !wifiEntry.canConnect())) {
        } else {
            if (config == null) {
                connect(wifiEntry, false, false);
            } else {
                this.mWifiManager.connect(config, new WifiConnectActionListener());
            }
        }
    }

    public void onScan(WifiDialog2 wifiDialog2, String str) {
        startActivityForResult(WifiDppUtils.getEnrolleeQrCodeScannerIntent(str), 0);
    }

    private void forget(WifiEntry wifiEntry) {
        this.mMetricsFeatureProvider.action((Context) getActivity(), 137, (Pair<Integer, Object>[]) new Pair[0]);
        wifiEntry.forget((WifiEntry.ForgetCallback) null);
    }

    /* access modifiers changed from: package-private */
    public void connect(WifiEntry wifiEntry, boolean z, boolean z2) {
        this.mMetricsFeatureProvider.action((Context) getActivity(), 135, wifiEntry.isSaved());
        wifiEntry.connect(new WifiEntryConnectCallback(wifiEntry, z, z2));
    }

    private class WifiConnectActionListener implements WifiManager.ActionListener {
        private WifiConnectActionListener() {
        }

        public void onSuccess() {
            boolean unused = NetworkProviderSettings.this.mClickedConnect = true;
        }

        public void onFailure(int i) {
            if (!NetworkProviderSettings.this.isFinishingOrDestroyed()) {
                Toast.makeText(NetworkProviderSettings.this.getContext(), R.string.wifi_failed_connect_message, 0).show();
            }
        }
    }

    private class WifiEntryConnectCallback implements WifiEntry.ConnectCallback {
        final WifiEntry mConnectWifiEntry;
        final boolean mEditIfNoConfig;
        final boolean mFullScreenEdit;

        WifiEntryConnectCallback(WifiEntry wifiEntry, boolean z, boolean z2) {
            this.mConnectWifiEntry = wifiEntry;
            this.mEditIfNoConfig = z;
            this.mFullScreenEdit = z2;
        }

        public void onConnectResult(int i) {
            if (!NetworkProviderSettings.this.isFinishingOrDestroyed()) {
                if (i == 0) {
                    boolean unused = NetworkProviderSettings.this.mClickedConnect = true;
                } else if (i == 1) {
                    if (!this.mEditIfNoConfig) {
                        return;
                    }
                    if (this.mFullScreenEdit) {
                        NetworkProviderSettings.this.launchConfigNewNetworkFragment(this.mConnectWifiEntry);
                    } else {
                        NetworkProviderSettings.this.showDialog(this.mConnectWifiEntry, 1);
                    }
                } else if (i == 2) {
                    Toast.makeText(NetworkProviderSettings.this.getContext(), R.string.wifi_failed_connect_message, 0).show();
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void launchConfigNewNetworkFragment(WifiEntry wifiEntry) {
        Bundle bundle = new Bundle();
        bundle.putString("key_chosen_wifientry_key", wifiEntry.getKey());
        new SubSettingLauncher(getContext()).setTitleText(wifiEntry.getTitle()).setDestination(ConfigureWifiEntryFragment.class.getName()).setArguments(bundle).setSourceMetricsCategory(getMetricsCategory()).setResultListener(this, 3).launch();
    }

    private static boolean isDisabledByWrongPassword(WifiEntry wifiEntry) {
        WifiConfiguration.NetworkSelectionStatus networkSelectionStatus;
        WifiConfiguration wifiConfiguration = wifiEntry.getWifiConfiguration();
        if (wifiConfiguration == null || (networkSelectionStatus = wifiConfiguration.getNetworkSelectionStatus()) == null || networkSelectionStatus.getNetworkSelectionStatus() == 0 || 8 != networkSelectionStatus.getNetworkSelectionDisableReason()) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public void openSubscriptionHelpPage(WifiEntry wifiEntry) {
        Intent helpIntent = getHelpIntent(getContext(), wifiEntry.getHelpUriString());
        if (helpIntent != null) {
            try {
                startActivityForResult(helpIntent, 4);
            } catch (ActivityNotFoundException unused) {
                Log.e("NetworkProviderSettings", "Activity was not found for intent, " + helpIntent.toString());
            }
        }
    }

    /* access modifiers changed from: package-private */
    public Intent getHelpIntent(Context context, String str) {
        return HelpUtils.getHelpIntent(context, str, context.getClass().getName());
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        MenuItem add = menu.add(0, 6, 0, R.string.fix_connectivity);
        add.setIcon(R.drawable.ic_repair_24dp);
        add.setShowAsAction(2);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 6) {
            return super.onOptionsItemSelected(menuItem);
        }
        if (isPhoneOnCall()) {
            showResetInternetDialog();
            return true;
        }
        fixConnectivity();
        return true;
    }

    /* access modifiers changed from: package-private */
    public void showResetInternetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle((int) R.string.reset_your_internet_title).setMessage((int) R.string.reset_internet_text).setPositiveButton((int) R.string.tts_reset, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                NetworkProviderSettings.this.fixConnectivity();
            }
        }).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).create().show();
    }

    /* access modifiers changed from: package-private */
    public boolean isPhoneOnCall() {
        return ((TelephonyManager) getActivity().getSystemService(TelephonyManager.class)).getCallState() != 0;
    }

    /* access modifiers changed from: private */
    public void fixConnectivity() {
        if (this.mConnectivitySubsystemsRecoveryManager == null) {
            HandlerThread handlerThread = new HandlerThread("NetworkProviderSettings{" + Integer.toHexString(System.identityHashCode(this)) + "}", 10);
            this.mRecoveryThread = handlerThread;
            handlerThread.start();
            this.mConnectivitySubsystemsRecoveryManager = new ConnectivitySubsystemsRecoveryManager(getContext(), this.mRecoveryThread.getThreadHandler());
        }
        if (this.mConnectivitySubsystemsRecoveryManager.isRecoveryAvailable()) {
            this.mConnectivitySubsystemsRecoveryManager.triggerSubsystemRestart((String) null, this);
        }
    }

    public void onSubsystemRestartOperationBegin() {
        LayoutPreference layoutPreference = this.mResetInternetPreference;
        if (layoutPreference != null) {
            layoutPreference.setVisible(true);
        }
        updateAirplaneModeMsgPreference(false);
    }

    public void onSubsystemRestartOperationEnd() {
        LayoutPreference layoutPreference = this.mResetInternetPreference;
        if (layoutPreference != null) {
            layoutPreference.setVisible(false);
        }
        if (this.mAirplaneModeEnabler.isAirplaneModeOn()) {
            updateAirplaneModeMsgPreference(true);
        }
    }

    public void onAirplaneModeChanged(boolean z) {
        updateAirplaneModeMsgPreference(z);
    }

    public class FirstWifiEntryPreference extends ConnectedWifiEntryPreference {
        /* access modifiers changed from: protected */
        public int getIconColorAttr() {
            return 16843817;
        }

        public FirstWifiEntryPreference(Context context, WifiEntry wifiEntry, Fragment fragment) {
            super(context, wifiEntry, fragment);
        }
    }
}
