package com.android.settings.datausage;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.NetworkTemplate;
import android.os.Bundle;
import android.os.UserHandle;
import android.telephony.SubscriptionManager;
import android.util.ArraySet;
import android.util.IconDrawableFactory;
import android.view.View;
import android.widget.AdapterView;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import com.android.settings.R;
import com.android.settings.datausage.DataSaverBackend;
import com.android.settingslib.AppItem;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.RestrictedSwitchPreference;
import com.android.settingslib.net.NetworkCycleDataForUid;
import com.android.settingslib.net.NetworkCycleDataForUidLoader;
import com.android.settingslib.net.UidDetail;
import com.android.settingslib.net.UidDetailProvider;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AppDataUsage extends DataUsageBaseFragment implements Preference.OnPreferenceChangeListener, DataSaverBackend.Listener {
    /* access modifiers changed from: private */
    public AppItem mAppItem;
    /* access modifiers changed from: private */
    public PreferenceCategory mAppList;
    private final LoaderManager.LoaderCallbacks<ArraySet<Preference>> mAppPrefCallbacks = new LoaderManager.LoaderCallbacks<ArraySet<Preference>>() {
        public void onLoaderReset(Loader<ArraySet<Preference>> loader) {
        }

        public Loader<ArraySet<Preference>> onCreateLoader(int i, Bundle bundle) {
            return new AppPrefLoader(AppDataUsage.this.getPrefContext(), AppDataUsage.this.mPackages, AppDataUsage.this.getPackageManager());
        }

        public void onLoadFinished(Loader<ArraySet<Preference>> loader, ArraySet<Preference> arraySet) {
            if (arraySet != null && AppDataUsage.this.mAppList != null) {
                Iterator<Preference> it = arraySet.iterator();
                while (it.hasNext()) {
                    AppDataUsage.this.mAppList.addPreference(it.next());
                }
            }
        }
    };
    private Preference mAppSettings;
    private Intent mAppSettingsIntent;
    private Preference mBackgroundUsage;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public SpinnerPreference mCycle;
    /* access modifiers changed from: private */
    public CycleAdapter mCycleAdapter;
    private AdapterView.OnItemSelectedListener mCycleListener = new AdapterView.OnItemSelectedListener() {
        public void onNothingSelected(AdapterView<?> adapterView) {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
            AppDataUsage.this.bindData(i);
        }
    };
    /* access modifiers changed from: private */
    public ArrayList<Long> mCycles;
    private DataSaverBackend mDataSaverBackend;
    private Preference mForegroundUsage;
    private Drawable mIcon;
    CharSequence mLabel;
    private PackageManager mPackageManager;
    String mPackageName;
    /* access modifiers changed from: private */
    public final ArraySet<String> mPackages = new ArraySet<>();
    private RestrictedSwitchPreference mRestrictBackground;
    /* access modifiers changed from: private */
    public long mSelectedCycle;
    NetworkTemplate mTemplate;
    private Preference mTotalUsage;
    final LoaderManager.LoaderCallbacks<List<NetworkCycleDataForUid>> mUidDataCallbacks = new LoaderManager.LoaderCallbacks<List<NetworkCycleDataForUid>>() {
        public void onLoaderReset(Loader<List<NetworkCycleDataForUid>> loader) {
        }

        public Loader<List<NetworkCycleDataForUid>> onCreateLoader(int i, Bundle bundle) {
            NetworkCycleDataForUidLoader.Builder builder = NetworkCycleDataForUidLoader.builder(AppDataUsage.this.mContext);
            builder.setRetrieveDetail(true).setNetworkTemplate(AppDataUsage.this.mTemplate);
            if (AppDataUsage.this.mAppItem.category == 0) {
                for (int i2 = 0; i2 < AppDataUsage.this.mAppItem.uids.size(); i2++) {
                    builder.addUid(AppDataUsage.this.mAppItem.uids.keyAt(i2));
                }
            } else {
                builder.addUid(AppDataUsage.this.mAppItem.key);
            }
            if (AppDataUsage.this.mCycles != null) {
                builder.setCycles(AppDataUsage.this.mCycles);
            }
            return builder.build();
        }

        public void onLoadFinished(Loader<List<NetworkCycleDataForUid>> loader, List<NetworkCycleDataForUid> list) {
            List unused = AppDataUsage.this.mUsageData = list;
            AppDataUsage.this.mCycleAdapter.updateCycleList(list);
            int i = 0;
            if (AppDataUsage.this.mSelectedCycle > 0) {
                int size = list.size();
                int i2 = 0;
                while (true) {
                    if (i2 >= size) {
                        break;
                    } else if (list.get(i2).getEndTime() == AppDataUsage.this.mSelectedCycle) {
                        i = i2;
                        break;
                    } else {
                        i2++;
                    }
                }
                if (i > 0) {
                    AppDataUsage.this.mCycle.setSelection(i);
                }
                AppDataUsage.this.bindData(i);
                return;
            }
            AppDataUsage.this.bindData(0);
        }
    };
    private RestrictedSwitchPreference mUnrestrictedData;
    /* access modifiers changed from: private */
    public List<NetworkCycleDataForUid> mUsageData;

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "AppDataUsage";
    }

    public int getMetricsCategory() {
        return 343;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.app_data_usage;
    }

    public void onDataSaverChanged(boolean z) {
    }

    public void onCreate(Bundle bundle) {
        int i;
        super.onCreate(bundle);
        this.mContext = getContext();
        this.mPackageManager = getPackageManager();
        Bundle arguments = getArguments();
        this.mAppItem = arguments != null ? (AppItem) arguments.getParcelable("app_item") : null;
        this.mTemplate = arguments != null ? (NetworkTemplate) arguments.getParcelable("network_template") : null;
        this.mCycles = arguments != null ? (ArrayList) arguments.getSerializable("network_cycles") : null;
        this.mSelectedCycle = arguments != null ? arguments.getLong("selected_cycle") : 0;
        if (this.mTemplate == null) {
            this.mTemplate = DataUsageUtils.getDefaultTemplate(this.mContext, SubscriptionManager.getDefaultDataSubscriptionId());
        }
        boolean z = false;
        if (this.mAppItem == null) {
            if (arguments != null) {
                i = arguments.getInt("uid", -1);
            } else {
                i = getActivity().getIntent().getIntExtra("uid", -1);
            }
            if (i == -1) {
                getActivity().finish();
            } else {
                addUid(i);
                AppItem appItem = new AppItem(i);
                this.mAppItem = appItem;
                appItem.addUid(i);
            }
        } else {
            for (int i2 = 0; i2 < this.mAppItem.uids.size(); i2++) {
                addUid(this.mAppItem.uids.keyAt(i2));
            }
        }
        this.mTotalUsage = findPreference("total_usage");
        this.mForegroundUsage = findPreference("foreground_usage");
        this.mBackgroundUsage = findPreference("background_usage");
        this.mCycle = (SpinnerPreference) findPreference("cycle");
        this.mCycleAdapter = new CycleAdapter(this.mContext, this.mCycle, this.mCycleListener);
        UidDetailProvider uidDetailProvider = getUidDetailProvider();
        int i3 = this.mAppItem.key;
        if (i3 > 0) {
            if (!UserHandle.isApp(i3)) {
                UidDetail uidDetail = uidDetailProvider.getUidDetail(this.mAppItem.key, true);
                this.mIcon = uidDetail.icon;
                this.mLabel = uidDetail.label;
                removePreference("unrestricted_data_saver");
                removePreference("restrict_background");
            } else {
                if (this.mPackages.size() != 0) {
                    try {
                        ApplicationInfo applicationInfoAsUser = this.mPackageManager.getApplicationInfoAsUser(this.mPackages.valueAt(0), 0, UserHandle.getUserId(this.mAppItem.key));
                        this.mIcon = IconDrawableFactory.newInstance(getActivity()).getBadgedIcon(applicationInfoAsUser);
                        this.mLabel = applicationInfoAsUser.loadLabel(this.mPackageManager);
                        this.mPackageName = applicationInfoAsUser.packageName;
                    } catch (PackageManager.NameNotFoundException unused) {
                    }
                }
                RestrictedSwitchPreference restrictedSwitchPreference = (RestrictedSwitchPreference) findPreference("restrict_background");
                this.mRestrictBackground = restrictedSwitchPreference;
                restrictedSwitchPreference.setOnPreferenceChangeListener(this);
                RestrictedSwitchPreference restrictedSwitchPreference2 = (RestrictedSwitchPreference) findPreference("unrestricted_data_saver");
                this.mUnrestrictedData = restrictedSwitchPreference2;
                restrictedSwitchPreference2.setOnPreferenceChangeListener(this);
            }
            this.mDataSaverBackend = new DataSaverBackend(this.mContext);
            this.mAppSettings = findPreference("app_settings");
            Intent intent = new Intent("android.intent.action.MANAGE_NETWORK_USAGE");
            this.mAppSettingsIntent = intent;
            intent.addCategory("android.intent.category.DEFAULT");
            PackageManager packageManager = getPackageManager();
            Iterator<String> it = this.mPackages.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                this.mAppSettingsIntent.setPackage(it.next());
                if (packageManager.resolveActivity(this.mAppSettingsIntent, 0) != null) {
                    z = true;
                    break;
                }
            }
            if (!z) {
                removePreference("app_settings");
                this.mAppSettings = null;
            }
            if (this.mPackages.size() > 1) {
                this.mAppList = (PreferenceCategory) findPreference("app_list");
                LoaderManager.getInstance(this).restartLoader(3, Bundle.EMPTY, this.mAppPrefCallbacks);
                return;
            }
            removePreference("app_list");
            return;
        }
        FragmentActivity activity = getActivity();
        UidDetail uidDetail2 = uidDetailProvider.getUidDetail(this.mAppItem.key, true);
        this.mIcon = uidDetail2.icon;
        this.mLabel = uidDetail2.label;
        this.mPackageName = activity.getPackageName();
        removePreference("unrestricted_data_saver");
        removePreference("app_settings");
        removePreference("restrict_background");
        removePreference("app_list");
    }

    public void onResume() {
        super.onResume();
        DataSaverBackend dataSaverBackend = this.mDataSaverBackend;
        if (dataSaverBackend != null) {
            dataSaverBackend.addListener(this);
        }
        LoaderManager.getInstance(this).restartLoader(2, (Bundle) null, this.mUidDataCallbacks);
        updatePrefs();
    }

    public void onPause() {
        super.onPause();
        DataSaverBackend dataSaverBackend = this.mDataSaverBackend;
        if (dataSaverBackend != null) {
            dataSaverBackend.remListener(this);
        }
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        if (preference == this.mRestrictBackground) {
            this.mDataSaverBackend.setIsDenylisted(this.mAppItem.key, this.mPackageName, !((Boolean) obj).booleanValue());
            updatePrefs();
            return true;
        } else if (preference != this.mUnrestrictedData) {
            return false;
        } else {
            this.mDataSaverBackend.setIsAllowlisted(this.mAppItem.key, this.mPackageName, ((Boolean) obj).booleanValue());
            return true;
        }
    }

    public boolean onPreferenceTreeClick(Preference preference) {
        if (preference != this.mAppSettings) {
            return super.onPreferenceTreeClick(preference);
        }
        getActivity().startActivityAsUser(this.mAppSettingsIntent, new UserHandle(UserHandle.getUserId(this.mAppItem.key)));
        return true;
    }

    /* access modifiers changed from: package-private */
    public void updatePrefs() {
        updatePrefs(getAppRestrictBackground(), getUnrestrictData());
    }

    /* access modifiers changed from: package-private */
    public UidDetailProvider getUidDetailProvider() {
        return new UidDetailProvider(this.mContext);
    }

    private void updatePrefs(boolean z, boolean z2) {
        RestrictedLockUtils.EnforcedAdmin checkIfMeteredDataRestricted = RestrictedLockUtilsInternal.checkIfMeteredDataRestricted(this.mContext, this.mPackageName, UserHandle.getUserId(this.mAppItem.key));
        RestrictedSwitchPreference restrictedSwitchPreference = this.mRestrictBackground;
        if (restrictedSwitchPreference != null) {
            restrictedSwitchPreference.setChecked(!z);
            this.mRestrictBackground.setDisabledByAdmin(checkIfMeteredDataRestricted);
        }
        RestrictedSwitchPreference restrictedSwitchPreference2 = this.mUnrestrictedData;
        if (restrictedSwitchPreference2 == null) {
            return;
        }
        if (z) {
            restrictedSwitchPreference2.setVisible(false);
            return;
        }
        restrictedSwitchPreference2.setVisible(true);
        this.mUnrestrictedData.setChecked(z2);
        this.mUnrestrictedData.setDisabledByAdmin(checkIfMeteredDataRestricted);
    }

    private void addUid(int i) {
        String[] packagesForUid = this.mPackageManager.getPackagesForUid(i);
        if (packagesForUid != null) {
            for (String add : packagesForUid) {
                this.mPackages.add(add);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void bindData(int i) {
        long j;
        long j2;
        List<NetworkCycleDataForUid> list = this.mUsageData;
        if (list == null || i >= list.size()) {
            j2 = 0;
            this.mCycle.setVisible(false);
            j = 0;
        } else {
            this.mCycle.setVisible(true);
            NetworkCycleDataForUid networkCycleDataForUid = this.mUsageData.get(i);
            j2 = networkCycleDataForUid.getBackgroudUsage();
            j = networkCycleDataForUid.getForegroudUsage();
        }
        this.mTotalUsage.setSummary(DataUsageUtils.formatDataUsage(this.mContext, j2 + j));
        this.mForegroundUsage.setSummary(DataUsageUtils.formatDataUsage(this.mContext, j));
        this.mBackgroundUsage.setSummary(DataUsageUtils.formatDataUsage(this.mContext, j2));
    }

    private boolean getAppRestrictBackground() {
        return (this.services.mPolicyManager.getUidPolicy(this.mAppItem.key) & 1) != 0;
    }

    private boolean getUnrestrictData() {
        DataSaverBackend dataSaverBackend = this.mDataSaverBackend;
        if (dataSaverBackend != null) {
            return dataSaverBackend.isAllowlisted(this.mAppItem.key);
        }
        return false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0045  */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0047  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onViewCreated(android.view.View r7, android.os.Bundle r8) {
        /*
            r6 = this;
            super.onViewCreated(r7, r8)
            android.util.ArraySet<java.lang.String> r7 = r6.mPackages
            int r7 = r7.size()
            r8 = 0
            r0 = 0
            if (r7 == 0) goto L_0x0016
            android.util.ArraySet<java.lang.String> r7 = r6.mPackages
            java.lang.Object r7 = r7.valueAt(r0)
            java.lang.String r7 = (java.lang.String) r7
            goto L_0x0017
        L_0x0016:
            r7 = r8
        L_0x0017:
            if (r7 == 0) goto L_0x003e
            android.content.pm.PackageManager r1 = r6.mPackageManager     // Catch:{ NameNotFoundException -> 0x0028 }
            com.android.settingslib.AppItem r2 = r6.mAppItem     // Catch:{ NameNotFoundException -> 0x0028 }
            int r2 = r2.key     // Catch:{ NameNotFoundException -> 0x0028 }
            int r2 = android.os.UserHandle.getUserId(r2)     // Catch:{ NameNotFoundException -> 0x0028 }
            int r1 = r1.getPackageUidAsUser(r7, r2)     // Catch:{ NameNotFoundException -> 0x0028 }
            goto L_0x003f
        L_0x0028:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Skipping UID because cannot find package "
            r1.append(r2)
            r1.append(r7)
            java.lang.String r1 = r1.toString()
            java.lang.String r2 = "AppDataUsage"
            android.util.Log.w(r2, r1)
        L_0x003e:
            r1 = r0
        L_0x003f:
            com.android.settingslib.AppItem r2 = r6.mAppItem
            int r2 = r2.key
            if (r2 <= 0) goto L_0x0047
            r2 = 1
            goto L_0x0048
        L_0x0047:
            r2 = r0
        L_0x0048:
            androidx.fragment.app.FragmentActivity r3 = r6.getActivity()
            com.android.settings.widget.EntityHeaderController r8 = com.android.settings.widget.EntityHeaderController.newInstance(r3, r6, r8)
            androidx.recyclerview.widget.RecyclerView r4 = r6.getListView()
            com.android.settingslib.core.lifecycle.Lifecycle r5 = r6.getSettingsLifecycle()
            com.android.settings.widget.EntityHeaderController r8 = r8.setRecyclerView(r4, r5)
            com.android.settings.widget.EntityHeaderController r8 = r8.setUid(r1)
            com.android.settings.widget.EntityHeaderController r8 = r8.setHasAppInfoLink(r2)
            com.android.settings.widget.EntityHeaderController r8 = r8.setButtonActions(r0, r0)
            android.graphics.drawable.Drawable r0 = r6.mIcon
            com.android.settings.widget.EntityHeaderController r8 = r8.setIcon((android.graphics.drawable.Drawable) r0)
            java.lang.CharSequence r0 = r6.mLabel
            com.android.settings.widget.EntityHeaderController r8 = r8.setLabel((java.lang.CharSequence) r0)
            com.android.settings.widget.EntityHeaderController r7 = r8.setPackageName(r7)
            android.content.Context r8 = r6.getPrefContext()
            com.android.settingslib.widget.LayoutPreference r7 = r7.done((android.app.Activity) r3, (android.content.Context) r8)
            androidx.preference.PreferenceScreen r6 = r6.getPreferenceScreen()
            r6.addPreference(r7)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.datausage.AppDataUsage.onViewCreated(android.view.View, android.os.Bundle):void");
    }

    public void onAllowlistStatusChanged(int i, boolean z) {
        if (this.mAppItem.uids.get(i, false)) {
            updatePrefs(getAppRestrictBackground(), z);
        }
    }

    public void onDenylistStatusChanged(int i, boolean z) {
        if (this.mAppItem.uids.get(i, false)) {
            updatePrefs(z, getUnrestrictData());
        }
    }
}
