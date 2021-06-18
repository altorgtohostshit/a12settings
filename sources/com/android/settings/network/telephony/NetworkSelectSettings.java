package com.android.settings.network.telephony;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import android.telephony.CellIdentity;
import android.telephony.CellInfo;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import com.android.internal.telephony.OperatorInfo;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.network.telephony.NetworkScanHelper;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.utils.ThreadUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkSelectSettings extends DashboardFragment {
    private final NetworkScanHelper.NetworkScanCallback mCallback = new NetworkScanHelper.NetworkScanCallback() {
        public void onResults(List<CellInfo> list) {
            NetworkSelectSettings.this.mHandler.obtainMessage(2, list).sendToTarget();
        }

        public void onComplete() {
            NetworkSelectSettings.this.mHandler.obtainMessage(4).sendToTarget();
        }

        public void onError(int i) {
            NetworkSelectSettings.this.mHandler.obtainMessage(3, i, 0).sendToTarget();
        }
    };
    List<CellInfo> mCellInfoList;
    private List<String> mForbiddenPlmns;
    /* access modifiers changed from: private */
    public final Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                boolean booleanValue = ((Boolean) message.obj).booleanValue();
                NetworkSelectSettings.this.stopNetworkQuery();
                NetworkSelectSettings.this.setProgressBarVisible(false);
                NetworkSelectSettings.this.getPreferenceScreen().setEnabled(true);
                NetworkOperatorPreference networkOperatorPreference = NetworkSelectSettings.this.mSelectedPreference;
                if (networkOperatorPreference != null) {
                    networkOperatorPreference.setSummary(booleanValue ? R.string.network_connected : R.string.network_could_not_connect);
                } else {
                    Log.e("NetworkSelectSettings", "No preference to update!");
                }
            } else if (i == 2) {
                List list = (List) message.obj;
                if (NetworkSelectSettings.this.mRequestIdManualNetworkScan < NetworkSelectSettings.this.mRequestIdManualNetworkSelect) {
                    Log.d("NetworkSelectSettings", "CellInfoList (drop): " + CellInfoUtil.cellInfoListToString(new ArrayList(list)));
                    return;
                }
                NetworkSelectSettings.access$310(NetworkSelectSettings.this);
                if (NetworkSelectSettings.this.mWaitingForNumberOfScanResults <= 0 && !NetworkSelectSettings.this.isResumed()) {
                    NetworkSelectSettings.this.stopNetworkQuery();
                }
                NetworkSelectSettings.this.mCellInfoList = new ArrayList(list);
                Log.d("NetworkSelectSettings", "CellInfoList: " + CellInfoUtil.cellInfoListToString(NetworkSelectSettings.this.mCellInfoList));
                List<CellInfo> list2 = NetworkSelectSettings.this.mCellInfoList;
                if (list2 != null && list2.size() != 0) {
                    NetworkOperatorPreference updateAllPreferenceCategory = NetworkSelectSettings.this.updateAllPreferenceCategory();
                    if (updateAllPreferenceCategory != null) {
                        NetworkSelectSettings networkSelectSettings = NetworkSelectSettings.this;
                        if (networkSelectSettings.mSelectedPreference != null) {
                            networkSelectSettings.mSelectedPreference = updateAllPreferenceCategory;
                        }
                    } else if (!NetworkSelectSettings.this.getPreferenceScreen().isEnabled() && updateAllPreferenceCategory == null) {
                        NetworkSelectSettings.this.mSelectedPreference.setSummary((int) R.string.network_connecting);
                    }
                    NetworkSelectSettings.this.getPreferenceScreen().setEnabled(true);
                } else if (NetworkSelectSettings.this.getPreferenceScreen().isEnabled()) {
                    NetworkSelectSettings.this.addMessagePreference(R.string.empty_networks_list);
                    NetworkSelectSettings.this.setProgressBarVisible(true);
                }
            } else if (i == 3) {
                NetworkSelectSettings.this.stopNetworkQuery();
                Log.i("NetworkSelectSettings", "Network scan failure " + message.arg1 + ": scan request 0x" + Long.toHexString(NetworkSelectSettings.this.mRequestIdManualNetworkScan) + ", waiting for scan results = " + NetworkSelectSettings.this.mWaitingForNumberOfScanResults + ", select request 0x" + Long.toHexString(NetworkSelectSettings.this.mRequestIdManualNetworkSelect));
                if (NetworkSelectSettings.this.mRequestIdManualNetworkScan >= NetworkSelectSettings.this.mRequestIdManualNetworkSelect) {
                    if (!NetworkSelectSettings.this.getPreferenceScreen().isEnabled()) {
                        NetworkSelectSettings.this.clearPreferenceSummary();
                        NetworkSelectSettings.this.getPreferenceScreen().setEnabled(true);
                        return;
                    }
                    NetworkSelectSettings.this.addMessagePreference(R.string.network_query_error);
                }
            } else if (i == 4) {
                NetworkSelectSettings.this.stopNetworkQuery();
                Log.d("NetworkSelectSettings", "Network scan complete: scan request 0x" + Long.toHexString(NetworkSelectSettings.this.mRequestIdManualNetworkScan) + ", waiting for scan results = " + NetworkSelectSettings.this.mWaitingForNumberOfScanResults + ", select request 0x" + Long.toHexString(NetworkSelectSettings.this.mRequestIdManualNetworkSelect));
                if (NetworkSelectSettings.this.mRequestIdManualNetworkScan >= NetworkSelectSettings.this.mRequestIdManualNetworkSelect) {
                    if (!NetworkSelectSettings.this.getPreferenceScreen().isEnabled()) {
                        NetworkSelectSettings.this.clearPreferenceSummary();
                        NetworkSelectSettings.this.getPreferenceScreen().setEnabled(true);
                        return;
                    }
                    NetworkSelectSettings networkSelectSettings2 = NetworkSelectSettings.this;
                    if (networkSelectSettings2.mCellInfoList == null) {
                        networkSelectSettings2.addMessagePreference(R.string.empty_networks_list);
                    }
                }
            }
        }
    };
    private MetricsFeatureProvider mMetricsFeatureProvider;
    private final ExecutorService mNetworkScanExecutor = Executors.newFixedThreadPool(1);
    private NetworkScanHelper mNetworkScanHelper;
    PreferenceCategory mPreferenceCategory;
    private View mProgressHeader;
    /* access modifiers changed from: private */
    public long mRequestIdManualNetworkScan;
    /* access modifiers changed from: private */
    public long mRequestIdManualNetworkSelect;
    NetworkOperatorPreference mSelectedPreference;
    private boolean mShow4GForLTE = false;
    private Preference mStatusMessagePreference;
    private int mSubId = -1;
    TelephonyManager mTelephonyManager;
    private boolean mUseNewApi;
    /* access modifiers changed from: private */
    public long mWaitingForNumberOfScanResults;

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "NetworkSelectSettings";
    }

    public int getMetricsCategory() {
        return 1581;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.choose_network;
    }

    static /* synthetic */ long access$310(NetworkSelectSettings networkSelectSettings) {
        long j = networkSelectSettings.mWaitingForNumberOfScanResults;
        networkSelectSettings.mWaitingForNumberOfScanResults = j - 1;
        return j;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mUseNewApi = getContext().getResources().getBoolean(17891539);
        this.mSubId = getArguments().getInt("android.provider.extra.SUB_ID");
        this.mPreferenceCategory = (PreferenceCategory) findPreference("network_operators_preference");
        Preference preference = new Preference(getContext());
        this.mStatusMessagePreference = preference;
        preference.setSelectable(false);
        this.mSelectedPreference = null;
        TelephonyManager createForSubscriptionId = ((TelephonyManager) getContext().getSystemService(TelephonyManager.class)).createForSubscriptionId(this.mSubId);
        this.mTelephonyManager = createForSubscriptionId;
        this.mNetworkScanHelper = new NetworkScanHelper(createForSubscriptionId, this.mCallback, this.mNetworkScanExecutor);
        PersistableBundle configForSubId = ((CarrierConfigManager) getContext().getSystemService("carrier_config")).getConfigForSubId(this.mSubId);
        if (configForSubId != null) {
            this.mShow4GForLTE = configForSubId.getBoolean("show_4g_for_lte_data_icon_bool");
        }
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(getContext()).getMetricsFeatureProvider();
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (getActivity() != null) {
            this.mProgressHeader = setPinnedHeaderView((int) R.layout.progress_header).findViewById(R.id.progress_bar_animation);
            setProgressBarVisible(false);
        }
        forceUpdateConnectedPreferenceCategory();
    }

    public void onStart() {
        super.onStart();
        updateForbiddenPlmns();
        if (!isProgressBarVisible() && this.mWaitingForNumberOfScanResults <= 0) {
            startNetworkQuery();
        }
    }

    /* access modifiers changed from: package-private */
    public void updateForbiddenPlmns() {
        List<String> list;
        String[] forbiddenPlmns = this.mTelephonyManager.getForbiddenPlmns();
        if (forbiddenPlmns != null) {
            list = Arrays.asList(forbiddenPlmns);
        } else {
            list = new ArrayList<>();
        }
        this.mForbiddenPlmns = list;
    }

    public void onStop() {
        super.onStop();
        if (this.mWaitingForNumberOfScanResults <= 0) {
            stopNetworkQuery();
        }
    }

    public boolean onPreferenceTreeClick(Preference preference) {
        if (preference != this.mSelectedPreference) {
            stopNetworkQuery();
            clearPreferenceSummary();
            NetworkOperatorPreference networkOperatorPreference = this.mSelectedPreference;
            if (networkOperatorPreference != null) {
                networkOperatorPreference.setSummary((int) R.string.network_disconnected);
            }
            NetworkOperatorPreference networkOperatorPreference2 = (NetworkOperatorPreference) preference;
            this.mSelectedPreference = networkOperatorPreference2;
            networkOperatorPreference2.setSummary((int) R.string.network_connecting);
            this.mMetricsFeatureProvider.action(getContext(), 1210, (Pair<Integer, Object>[]) new Pair[0]);
            setProgressBarVisible(true);
            getPreferenceScreen().setEnabled(false);
            this.mRequestIdManualNetworkSelect = getNewRequestId();
            this.mWaitingForNumberOfScanResults = 2;
            ThreadUtils.postOnBackgroundThread((Runnable) new NetworkSelectSettings$$ExternalSyntheticLambda0(this, this.mSelectedPreference.getOperatorInfo()));
        }
        return true;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onPreferenceTreeClick$0(OperatorInfo operatorInfo) {
        Message obtainMessage = this.mHandler.obtainMessage(1);
        obtainMessage.obj = Boolean.valueOf(this.mTelephonyManager.setNetworkSelectionModeManual(operatorInfo, true));
        obtainMessage.sendToTarget();
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0048  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0072  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x007a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.android.settings.network.telephony.NetworkOperatorPreference updateAllPreferenceCategory() {
        /*
            r10 = this;
            androidx.preference.PreferenceCategory r0 = r10.mPreferenceCategory
            int r0 = r0.getPreferenceCount()
        L_0x0006:
            java.util.List<android.telephony.CellInfo> r1 = r10.mCellInfoList
            int r1 = r1.size()
            if (r0 <= r1) goto L_0x001a
            int r0 = r0 + -1
            androidx.preference.PreferenceCategory r1 = r10.mPreferenceCategory
            androidx.preference.Preference r2 = r1.getPreference(r0)
            r1.removePreference(r2)
            goto L_0x0006
        L_0x001a:
            r1 = 0
            r2 = 0
            r3 = r1
            r4 = r2
        L_0x001e:
            java.util.List<android.telephony.CellInfo> r5 = r10.mCellInfoList
            int r5 = r5.size()
            if (r3 >= r5) goto L_0x0080
            java.util.List<android.telephony.CellInfo> r5 = r10.mCellInfoList
            java.lang.Object r5 = r5.get(r3)
            android.telephony.CellInfo r5 = (android.telephony.CellInfo) r5
            if (r3 >= r0) goto L_0x0045
            androidx.preference.PreferenceCategory r6 = r10.mPreferenceCategory
            androidx.preference.Preference r6 = r6.getPreference(r3)
            boolean r7 = r6 instanceof com.android.settings.network.telephony.NetworkOperatorPreference
            if (r7 == 0) goto L_0x0040
            com.android.settings.network.telephony.NetworkOperatorPreference r6 = (com.android.settings.network.telephony.NetworkOperatorPreference) r6
            r6.updateCell(r5)
            goto L_0x0046
        L_0x0040:
            androidx.preference.PreferenceCategory r7 = r10.mPreferenceCategory
            r7.removePreference(r6)
        L_0x0045:
            r6 = r2
        L_0x0046:
            if (r6 != 0) goto L_0x005d
            com.android.settings.network.telephony.NetworkOperatorPreference r6 = new com.android.settings.network.telephony.NetworkOperatorPreference
            android.content.Context r7 = r10.getPrefContext()
            java.util.List<java.lang.String> r8 = r10.mForbiddenPlmns
            boolean r9 = r10.mShow4GForLTE
            r6.<init>((android.content.Context) r7, (android.telephony.CellInfo) r5, (java.util.List<java.lang.String>) r8, (boolean) r9)
            r6.setOrder(r3)
            androidx.preference.PreferenceCategory r5 = r10.mPreferenceCategory
            r5.addPreference(r6)
        L_0x005d:
            java.lang.String r5 = r6.getOperatorName()
            r6.setKey(r5)
            java.util.List<android.telephony.CellInfo> r5 = r10.mCellInfoList
            java.lang.Object r5 = r5.get(r3)
            android.telephony.CellInfo r5 = (android.telephony.CellInfo) r5
            boolean r5 = r5.isRegistered()
            if (r5 == 0) goto L_0x007a
            r4 = 2130971941(0x7f040d25, float:1.7552635E38)
            r6.setSummary((int) r4)
            r4 = r6
            goto L_0x007d
        L_0x007a:
            r6.setSummary((java.lang.CharSequence) r2)
        L_0x007d:
            int r3 = r3 + 1
            goto L_0x001e
        L_0x0080:
            java.util.List<android.telephony.CellInfo> r0 = r10.mCellInfoList
            int r0 = r0.size()
            if (r1 >= r0) goto L_0x00a7
            java.util.List<android.telephony.CellInfo> r0 = r10.mCellInfoList
            java.lang.Object r0 = r0.get(r1)
            android.telephony.CellInfo r0 = (android.telephony.CellInfo) r0
            com.android.settings.network.telephony.NetworkOperatorPreference r2 = r10.mSelectedPreference
            if (r2 == 0) goto L_0x00a4
            boolean r0 = r2.isSameCell(r0)
            if (r0 == 0) goto L_0x00a4
            androidx.preference.PreferenceCategory r0 = r10.mPreferenceCategory
            androidx.preference.Preference r0 = r0.getPreference(r1)
            com.android.settings.network.telephony.NetworkOperatorPreference r0 = (com.android.settings.network.telephony.NetworkOperatorPreference) r0
            r10.mSelectedPreference = r0
        L_0x00a4:
            int r1 = r1 + 1
            goto L_0x0080
        L_0x00a7:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.network.telephony.NetworkSelectSettings.updateAllPreferenceCategory():com.android.settings.network.telephony.NetworkOperatorPreference");
    }

    private void forceUpdateConnectedPreferenceCategory() {
        ServiceState serviceState;
        List<NetworkRegistrationInfo> networkRegistrationInfoListForTransportType;
        int dataState = this.mTelephonyManager.getDataState();
        TelephonyManager telephonyManager = this.mTelephonyManager;
        if (dataState == 2 && (serviceState = telephonyManager.getServiceState()) != null && (networkRegistrationInfoListForTransportType = serviceState.getNetworkRegistrationInfoListForTransportType(1)) != null && networkRegistrationInfoListForTransportType.size() != 0) {
            for (NetworkRegistrationInfo cellIdentity : networkRegistrationInfoListForTransportType) {
                CellIdentity cellIdentity2 = cellIdentity.getCellIdentity();
                if (cellIdentity2 != null) {
                    NetworkOperatorPreference networkOperatorPreference = new NetworkOperatorPreference(getPrefContext(), cellIdentity2, this.mForbiddenPlmns, this.mShow4GForLTE);
                    networkOperatorPreference.setSummary((int) R.string.network_connected);
                    networkOperatorPreference.setIcon(4);
                    this.mPreferenceCategory.addPreference(networkOperatorPreference);
                    return;
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void clearPreferenceSummary() {
        int preferenceCount = this.mPreferenceCategory.getPreferenceCount();
        while (preferenceCount > 0) {
            preferenceCount--;
            ((NetworkOperatorPreference) this.mPreferenceCategory.getPreference(preferenceCount)).setSummary((CharSequence) null);
        }
    }

    private long getNewRequestId() {
        return Math.max(this.mRequestIdManualNetworkSelect, this.mRequestIdManualNetworkScan) + 1;
    }

    private boolean isProgressBarVisible() {
        View view = this.mProgressHeader;
        if (view != null && view.getVisibility() == 0) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void setProgressBarVisible(boolean z) {
        View view = this.mProgressHeader;
        if (view != null) {
            view.setVisibility(z ? 0 : 8);
        }
    }

    /* access modifiers changed from: private */
    public void addMessagePreference(int i) {
        setProgressBarVisible(false);
        this.mStatusMessagePreference.setTitle(i);
        this.mPreferenceCategory.removeAll();
        this.mPreferenceCategory.addPreference(this.mStatusMessagePreference);
    }

    private void startNetworkQuery() {
        int i = 1;
        setProgressBarVisible(true);
        if (this.mNetworkScanHelper != null) {
            this.mRequestIdManualNetworkScan = getNewRequestId();
            this.mWaitingForNumberOfScanResults = 2;
            NetworkScanHelper networkScanHelper = this.mNetworkScanHelper;
            if (this.mUseNewApi) {
                i = 2;
            }
            networkScanHelper.startNetworkScan(i);
        }
    }

    /* access modifiers changed from: private */
    public void stopNetworkQuery() {
        setProgressBarVisible(false);
        NetworkScanHelper networkScanHelper = this.mNetworkScanHelper;
        if (networkScanHelper != null) {
            this.mWaitingForNumberOfScanResults = 0;
            networkScanHelper.stopNetworkQuery();
        }
    }

    public void onDestroy() {
        stopNetworkQuery();
        this.mNetworkScanExecutor.shutdown();
        super.onDestroy();
    }
}
