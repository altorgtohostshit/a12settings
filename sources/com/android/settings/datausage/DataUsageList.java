package com.android.settings.datausage;

import android.app.ActivityManager;
import android.app.usage.NetworkStats;
import android.content.Intent;
import android.graphics.Color;
import android.net.NetworkPolicy;
import android.net.NetworkTemplate;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.UserHandle;
import android.os.UserManager;
import android.telephony.SubscriptionInfo;
import android.util.FeatureFlagUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import com.android.settings.R;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.datausage.CycleAdapter;
import com.android.settings.network.MobileDataEnabledListener;
import com.android.settings.network.ProxySubscriptionManager;
import com.android.settings.widget.LoadingViewController;
import com.android.settingslib.AppItem;
import com.android.settingslib.net.NetworkCycleChartData;
import com.android.settingslib.net.NetworkCycleChartDataLoader;
import com.android.settingslib.net.NetworkStatsSummaryLoader;
import com.android.settingslib.net.UidDetailProvider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataUsageList extends DataUsageBaseFragment implements MobileDataEnabledListener.Client {
    static final int LOADER_CHART_DATA = 2;
    static final int LOADER_SUMMARY = 3;
    /* access modifiers changed from: private */
    public PreferenceGroup mApps;
    /* access modifiers changed from: private */
    public ChartDataUsagePreference mChart;
    private CycleAdapter mCycleAdapter;
    /* access modifiers changed from: private */
    public List<NetworkCycleChartData> mCycleData;
    private AdapterView.OnItemSelectedListener mCycleListener = new AdapterView.OnItemSelectedListener() {
        public void onNothingSelected(AdapterView<?> adapterView) {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
            CycleAdapter.CycleItem cycleItem = (CycleAdapter.CycleItem) DataUsageList.this.mCycleSpinner.getSelectedItem();
            DataUsageList.this.mChart.setNetworkCycleData((NetworkCycleChartData) DataUsageList.this.mCycleData.get(i));
            DataUsageList.this.updateDetailData();
        }
    };
    Spinner mCycleSpinner;
    private ArrayList<Long> mCycles;
    MobileDataEnabledListener mDataStateListener;
    private View mHeader;
    LoadingViewController mLoadingViewController;
    final LoaderManager.LoaderCallbacks<List<NetworkCycleChartData>> mNetworkCycleDataCallbacks = new LoaderManager.LoaderCallbacks<List<NetworkCycleChartData>>() {
        public Loader<List<NetworkCycleChartData>> onCreateLoader(int i, Bundle bundle) {
            return NetworkCycleChartDataLoader.builder(DataUsageList.this.getContext()).setNetworkTemplate(DataUsageList.this.mTemplate).build();
        }

        public void onLoadFinished(Loader<List<NetworkCycleChartData>> loader, List<NetworkCycleChartData> list) {
            DataUsageList.this.mLoadingViewController.showContent(false);
            List unused = DataUsageList.this.mCycleData = list;
            DataUsageList.this.updatePolicy();
            DataUsageList.this.mCycleSpinner.setVisibility(0);
        }

        public void onLoaderReset(Loader<List<NetworkCycleChartData>> loader) {
            List unused = DataUsageList.this.mCycleData = null;
        }
    };
    private final LoaderManager.LoaderCallbacks<NetworkStats> mNetworkStatsDetailCallbacks = new LoaderManager.LoaderCallbacks<NetworkStats>() {
        public Loader<NetworkStats> onCreateLoader(int i, Bundle bundle) {
            return new NetworkStatsSummaryLoader.Builder(DataUsageList.this.getContext()).setStartTime(DataUsageList.this.mChart.getInspectStart()).setEndTime(DataUsageList.this.mChart.getInspectEnd()).setNetworkTemplate(DataUsageList.this.mTemplate).build();
        }

        public void onLoadFinished(Loader<NetworkStats> loader, NetworkStats networkStats) {
            DataUsageList.this.bindStats(networkStats, DataUsageList.this.services.mPolicyManager.getUidsWithPolicy(1));
            updateEmptyVisible();
        }

        public void onLoaderReset(Loader<NetworkStats> loader) {
            DataUsageList.this.bindStats((NetworkStats) null, new int[0]);
            updateEmptyVisible();
        }

        private void updateEmptyVisible() {
            boolean z = true;
            boolean z2 = DataUsageList.this.mApps.getPreferenceCount() != 0;
            if (DataUsageList.this.getPreferenceScreen().getPreferenceCount() == 0) {
                z = false;
            }
            if (z2 == z) {
                return;
            }
            if (DataUsageList.this.mApps.getPreferenceCount() != 0) {
                DataUsageList.this.getPreferenceScreen().addPreference(DataUsageList.this.mUsageAmount);
                DataUsageList.this.getPreferenceScreen().addPreference(DataUsageList.this.mApps);
                return;
            }
            DataUsageList.this.getPreferenceScreen().removeAll();
        }
    };
    int mNetworkType;
    int mSubId = -1;
    NetworkTemplate mTemplate;
    private UidDetailProvider mUidDetailProvider;
    /* access modifiers changed from: private */
    public Preference mUsageAmount;

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "DataUsageList";
    }

    public int getMetricsCategory() {
        return 341;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.data_usage_list;
    }

    public void onCreate(Bundle bundle) {
        Preference preference;
        super.onCreate(bundle);
        FragmentActivity activity = getActivity();
        if (!isBandwidthControlEnabled()) {
            Log.w("DataUsageList", "No bandwidth control; leaving");
            activity.finish();
            return;
        }
        this.mUidDetailProvider = new UidDetailProvider(activity);
        this.mUsageAmount = findPreference("usage_amount");
        this.mChart = (ChartDataUsagePreference) findPreference("chart_data");
        this.mApps = (PreferenceGroup) findPreference("apps_group");
        if (FeatureFlagUtils.isEnabled(getContext(), "settings_provider_model")) {
            preference = findPreference("operator_warning");
        } else {
            preference = findPreference("non_carrier_data_usage_warning");
        }
        if (preference != null) {
            preference.setVisible(false);
        }
        processArgument();
        this.mDataStateListener = new MobileDataEnabledListener(activity, this);
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        View pinnedHeaderView = setPinnedHeaderView((int) R.layout.apps_filter_spinner);
        this.mHeader = pinnedHeaderView;
        pinnedHeaderView.findViewById(R.id.filter_settings).setOnClickListener(new DataUsageList$$ExternalSyntheticLambda0(this));
        Spinner spinner = (Spinner) this.mHeader.findViewById(R.id.filter_spinner);
        this.mCycleSpinner = spinner;
        spinner.setVisibility(8);
        this.mCycleAdapter = new CycleAdapter(this.mCycleSpinner.getContext(), new CycleAdapter.SpinnerInterface() {
            public void setAdapter(CycleAdapter cycleAdapter) {
                DataUsageList.this.mCycleSpinner.setAdapter(cycleAdapter);
            }

            public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener onItemSelectedListener) {
                DataUsageList.this.mCycleSpinner.setOnItemSelectedListener(onItemSelectedListener);
            }

            public Object getSelectedItem() {
                return DataUsageList.this.mCycleSpinner.getSelectedItem();
            }

            public void setSelection(int i) {
                DataUsageList.this.mCycleSpinner.setSelection(i);
            }
        }, this.mCycleListener);
        this.mCycleSpinner.setAccessibilityDelegate(new View.AccessibilityDelegate() {
            public void sendAccessibilityEvent(View view, int i) {
                if (i != 4) {
                    super.sendAccessibilityEvent(view, i);
                }
            }
        });
        LoadingViewController loadingViewController = new LoadingViewController(getView().findViewById(R.id.loading_container), getListView());
        this.mLoadingViewController = loadingViewController;
        loadingViewController.showLoadingViewDelayed();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$0(View view) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("network_template", this.mTemplate);
        new SubSettingLauncher(getContext()).setDestination(BillingCycleSettings.class.getName()).setTitleRes(R.string.billing_cycle).setSourceMetricsCategory(getMetricsCategory()).setArguments(bundle).launch();
    }

    public void onResume() {
        super.onResume();
        this.mDataStateListener.start(this.mSubId);
        getLoaderManager().restartLoader(2, buildArgs(this.mTemplate), this.mNetworkCycleDataCallbacks);
        updateBody();
    }

    public void onPause() {
        super.onPause();
        this.mDataStateListener.stop();
        getLoaderManager().destroyLoader(2);
        getLoaderManager().destroyLoader(3);
    }

    public void onDestroy() {
        this.mUidDetailProvider.clearCache();
        this.mUidDetailProvider = null;
        super.onDestroy();
    }

    /* access modifiers changed from: package-private */
    public void processArgument() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mSubId = arguments.getInt("sub_id", -1);
            this.mTemplate = arguments.getParcelable("network_template");
            this.mNetworkType = arguments.getInt("network_type", 0);
        }
        if (this.mTemplate == null && this.mSubId == -1) {
            Intent intent = getIntent();
            this.mSubId = intent.getIntExtra("android.provider.extra.SUB_ID", -1);
            this.mTemplate = intent.getParcelableExtra("network_template");
        }
    }

    public void onMobileDataEnabledChange() {
        updatePolicy();
    }

    private void updateBody() {
        SubscriptionInfo activeSubscriptionInfo;
        if (isAdded()) {
            FragmentActivity activity = getActivity();
            getActivity().invalidateOptionsMenu();
            int color = activity.getColor(R.color.sim_noitification);
            if (!(this.mSubId == -1 || (activeSubscriptionInfo = ProxySubscriptionManager.getInstance(activity).getActiveSubscriptionInfo(this.mSubId)) == null)) {
                color = activeSubscriptionInfo.getIconTint();
            }
            this.mChart.setColors(color, Color.argb(127, Color.red(color), Color.green(color), Color.blue(color)));
        }
    }

    private Bundle buildArgs(NetworkTemplate networkTemplate) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("template", networkTemplate);
        bundle.putParcelable("app", (Parcelable) null);
        bundle.putInt("fields", 10);
        return bundle;
    }

    /* access modifiers changed from: package-private */
    public void updatePolicy() {
        NetworkPolicy policy = this.services.mPolicyEditor.getPolicy(this.mTemplate);
        View findViewById = this.mHeader.findViewById(R.id.filter_settings);
        if (!isNetworkPolicyModifiable(policy, this.mSubId) || !isMobileDataAvailable(this.mSubId)) {
            this.mChart.setNetworkPolicy((NetworkPolicy) null);
            findViewById.setVisibility(8);
        } else {
            this.mChart.setNetworkPolicy(policy);
            findViewById.setVisibility(0);
            ((ImageView) findViewById).setColorFilter(17170443);
        }
        if (this.mCycleAdapter.updateCycleList(this.mCycleData)) {
            updateDetailData();
        }
    }

    /* access modifiers changed from: private */
    public void updateDetailData() {
        getLoaderManager().restartLoader(3, (Bundle) null, this.mNetworkStatsDetailCallbacks);
        List<NetworkCycleChartData> list = this.mCycleData;
        this.mUsageAmount.setTitle((CharSequence) getString(R.string.data_used_template, DataUsageUtils.formatDataUsage(getActivity(), (list == null || list.isEmpty()) ? 0 : this.mCycleData.get(this.mCycleSpinner.getSelectedItemPosition()).getTotalUsage())));
    }

    /* access modifiers changed from: private */
    public void bindStats(NetworkStats networkStats, int[] iArr) {
        int i;
        int i2;
        int i3;
        NetworkStats networkStats2 = networkStats;
        int[] iArr2 = iArr;
        this.mApps.removeAll();
        if (networkStats2 != null) {
            ArrayList arrayList = new ArrayList();
            int currentUser = ActivityManager.getCurrentUser();
            UserManager userManager = UserManager.get(getContext());
            List<UserHandle> userProfiles = userManager.getUserProfiles();
            SparseArray sparseArray = new SparseArray();
            NetworkStats.Bucket bucket = new NetworkStats.Bucket();
            long j = 0;
            while (true) {
                int i4 = 0;
                if (!networkStats.hasNextBucket() || !networkStats2.getNextBucket(bucket)) {
                    networkStats.close();
                } else {
                    int uid = bucket.getUid();
                    int userId = UserHandle.getUserId(uid);
                    int i5 = -4;
                    if (!UserHandle.isApp(uid)) {
                        i2 = uid;
                        if (!(i2 == -4 || i2 == -5 || i2 == 1061)) {
                            i2 = 1000;
                        }
                        i = 2;
                    } else if (userProfiles.contains(new UserHandle(userId))) {
                        if (userId != currentUser) {
                            i3 = uid;
                            j = accumulate(UidDetailProvider.buildKeyForUser(userId), sparseArray, bucket, 0, arrayList, j);
                        } else {
                            i3 = uid;
                        }
                        i = 2;
                        i2 = i3;
                    } else {
                        if (userManager.getUserInfo(userId) == null) {
                            i4 = 2;
                        } else {
                            i5 = UidDetailProvider.buildKeyForUser(userId);
                        }
                        i2 = i5;
                        i = i4;
                    }
                    j = accumulate(i2, sparseArray, bucket, i, arrayList, j);
                }
            }
            networkStats.close();
            for (int i6 : iArr2) {
                if (userProfiles.contains(new UserHandle(UserHandle.getUserId(i6)))) {
                    AppItem appItem = (AppItem) sparseArray.get(i6);
                    if (appItem == null) {
                        appItem = new AppItem(i6);
                        appItem.total = -1;
                        arrayList.add(appItem);
                        sparseArray.put(appItem.key, appItem);
                    }
                    appItem.restricted = true;
                }
            }
            Collections.sort(arrayList);
            for (int i7 = 0; i7 < arrayList.size(); i7++) {
                AppDataUsagePreference appDataUsagePreference = new AppDataUsagePreference(getContext(), (AppItem) arrayList.get(i7), j != 0 ? (int) ((((AppItem) arrayList.get(i7)).total * 100) / j) : 0, this.mUidDetailProvider);
                appDataUsagePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    public boolean onPreferenceClick(Preference preference) {
                        DataUsageList.this.startAppDataUsage(((AppDataUsagePreference) preference).getItem());
                        return true;
                    }
                });
                this.mApps.addPreference(appDataUsagePreference);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void startAppDataUsage(AppItem appItem) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("app_item", appItem);
        bundle.putParcelable("network_template", this.mTemplate);
        if (this.mCycles == null) {
            this.mCycles = new ArrayList<>();
            for (NetworkCycleChartData next : this.mCycleData) {
                if (this.mCycles.isEmpty()) {
                    this.mCycles.add(Long.valueOf(next.getEndTime()));
                }
                this.mCycles.add(Long.valueOf(next.getStartTime()));
            }
        }
        bundle.putSerializable("network_cycles", this.mCycles);
        bundle.putLong("selected_cycle", this.mCycleData.get(this.mCycleSpinner.getSelectedItemPosition()).getEndTime());
        new SubSettingLauncher(getContext()).setDestination(AppDataUsage.class.getName()).setTitleRes(R.string.data_usage_app_summary_title).setArguments(bundle).setSourceMetricsCategory(getMetricsCategory()).launch();
    }

    private static long accumulate(int i, SparseArray<AppItem> sparseArray, NetworkStats.Bucket bucket, int i2, ArrayList<AppItem> arrayList, long j) {
        int uid = bucket.getUid();
        AppItem appItem = sparseArray.get(i);
        if (appItem == null) {
            appItem = new AppItem(i);
            appItem.category = i2;
            arrayList.add(appItem);
            sparseArray.put(appItem.key, appItem);
        }
        appItem.addUid(uid);
        long rxBytes = appItem.total + bucket.getRxBytes() + bucket.getTxBytes();
        appItem.total = rxBytes;
        return Math.max(j, rxBytes);
    }
}
