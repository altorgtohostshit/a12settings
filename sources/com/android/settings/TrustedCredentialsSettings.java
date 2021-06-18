package com.android.settings;

import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.UserInfo;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.net.http.SslCertificate;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.security.IKeyChainService;
import android.security.KeyChain;
import android.util.ArraySet;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import com.android.internal.R;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.app.UnlaunchableAppActivity;
import com.android.internal.widget.LockPatternUtils;
import com.android.settings.TrustedCredentialsDialogBuilder;
import com.android.settings.core.InstrumentedFragment;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.IntConsumer;

public class TrustedCredentialsSettings extends InstrumentedFragment implements TrustedCredentialsDialogBuilder.DelegateInterface {
    /* access modifiers changed from: private */
    public Set<AdapterData.AliasLoader> mAliasLoaders = new ArraySet(2);
    /* access modifiers changed from: private */
    public AliasOperation mAliasOperation;
    private ArraySet<Integer> mConfirmedCredentialUsers;
    private IntConsumer mConfirmingCredentialListener;
    private int mConfirmingCredentialUser;
    /* access modifiers changed from: private */
    public ArrayList<GroupAdapter> mGroupAdapters = new ArrayList<>(2);
    /* access modifiers changed from: private */
    @GuardedBy({"mKeyChainConnectionByProfileId"})
    public final SparseArray<KeyChain.KeyChainConnection> mKeyChainConnectionByProfileId = new SparseArray<>();
    private KeyguardManager mKeyguardManager;
    /* access modifiers changed from: private */
    public TabHost mTabHost;
    /* access modifiers changed from: private */
    public int mTrustAllCaUserId;
    /* access modifiers changed from: private */
    public UserManager mUserManager;
    private BroadcastReceiver mWorkProfileChangedReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.intent.action.MANAGED_PROFILE_AVAILABLE".equals(action) || "android.intent.action.MANAGED_PROFILE_UNAVAILABLE".equals(action) || "android.intent.action.MANAGED_PROFILE_UNLOCKED".equals(action)) {
                Iterator it = TrustedCredentialsSettings.this.mGroupAdapters.iterator();
                while (it.hasNext()) {
                    ((GroupAdapter) it.next()).load();
                }
            }
        }
    };

    public int getMetricsCategory() {
        return 92;
    }

    private enum Tab {
        SYSTEM("system", R.string.trusted_credentials_system_tab, R.id.system_tab, R.id.system_progress, R.id.system_content, true),
        USER("user", R.string.trusted_credentials_user_tab, R.id.user_tab, R.id.user_progress, R.id.user_content, false);
        
        /* access modifiers changed from: private */
        public final int mContentView;
        /* access modifiers changed from: private */
        public final int mLabel;
        /* access modifiers changed from: private */
        public final int mProgress;
        /* access modifiers changed from: private */
        public final boolean mSwitch;
        /* access modifiers changed from: private */
        public final String mTag;
        /* access modifiers changed from: private */
        public final int mView;

        private Tab(String str, int i, int i2, int i3, int i4, boolean z) {
            this.mTag = str;
            this.mLabel = i;
            this.mView = i2;
            this.mProgress = i3;
            this.mContentView = i4;
            this.mSwitch = z;
        }

        /* access modifiers changed from: private */
        public List<String> getAliases(IKeyChainService iKeyChainService) throws RemoteException {
            int i = C05873.$SwitchMap$com$android$settings$TrustedCredentialsSettings$Tab[ordinal()];
            if (i == 1) {
                return iKeyChainService.getSystemCaAliases().getList();
            }
            if (i == 2) {
                return iKeyChainService.getUserCaAliases().getList();
            }
            throw new AssertionError();
        }

        /* access modifiers changed from: private */
        public boolean deleted(IKeyChainService iKeyChainService, String str) throws RemoteException {
            int i = C05873.$SwitchMap$com$android$settings$TrustedCredentialsSettings$Tab[ordinal()];
            if (i == 1) {
                return !iKeyChainService.containsCaAlias(str);
            }
            if (i == 2) {
                return false;
            }
            throw new AssertionError();
        }
    }

    /* renamed from: com.android.settings.TrustedCredentialsSettings$3 */
    static /* synthetic */ class C05873 {
        static final /* synthetic */ int[] $SwitchMap$com$android$settings$TrustedCredentialsSettings$Tab;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        static {
            /*
                com.android.settings.TrustedCredentialsSettings$Tab[] r0 = com.android.settings.TrustedCredentialsSettings.Tab.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$android$settings$TrustedCredentialsSettings$Tab = r0
                com.android.settings.TrustedCredentialsSettings$Tab r1 = com.android.settings.TrustedCredentialsSettings.Tab.SYSTEM     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$com$android$settings$TrustedCredentialsSettings$Tab     // Catch:{ NoSuchFieldError -> 0x001d }
                com.android.settings.TrustedCredentialsSettings$Tab r1 = com.android.settings.TrustedCredentialsSettings.Tab.USER     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.settings.TrustedCredentialsSettings.C05873.<clinit>():void");
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        FragmentActivity activity = getActivity();
        this.mUserManager = (UserManager) activity.getSystemService("user");
        this.mKeyguardManager = (KeyguardManager) activity.getSystemService("keyguard");
        this.mTrustAllCaUserId = activity.getIntent().getIntExtra("ARG_SHOW_NEW_FOR_USER", -10000);
        this.mConfirmedCredentialUsers = new ArraySet<>(2);
        this.mConfirmingCredentialUser = -10000;
        if (bundle != null) {
            this.mConfirmingCredentialUser = bundle.getInt("ConfirmingCredentialUser", -10000);
            ArrayList<Integer> integerArrayList = bundle.getIntegerArrayList("ConfirmedCredentialUsers");
            if (integerArrayList != null) {
                this.mConfirmedCredentialUsers.addAll(integerArrayList);
            }
        }
        this.mConfirmingCredentialListener = null;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.MANAGED_PROFILE_AVAILABLE");
        intentFilter.addAction("android.intent.action.MANAGED_PROFILE_UNAVAILABLE");
        intentFilter.addAction("android.intent.action.MANAGED_PROFILE_UNLOCKED");
        activity.registerReceiver(this.mWorkProfileChangedReceiver, intentFilter);
        activity.setTitle(R.string.trusted_credentials);
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putIntegerArrayList("ConfirmedCredentialUsers", new ArrayList(this.mConfirmedCredentialUsers));
        bundle.putInt("ConfirmingCredentialUser", this.mConfirmingCredentialUser);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        TabHost tabHost = (TabHost) layoutInflater.inflate(R.layout.trusted_credentials, viewGroup, false);
        this.mTabHost = tabHost;
        tabHost.setup();
        addTab(Tab.SYSTEM);
        Tab tab = Tab.USER;
        addTab(tab);
        if (getActivity().getIntent() != null && "com.android.settings.TRUSTED_CREDENTIALS_USER".equals(getActivity().getIntent().getAction())) {
            this.mTabHost.setCurrentTabByTag(tab.mTag);
        }
        return this.mTabHost;
    }

    public void onDestroy() {
        getActivity().unregisterReceiver(this.mWorkProfileChangedReceiver);
        for (AdapterData.AliasLoader cancel : this.mAliasLoaders) {
            cancel.cancel(true);
        }
        this.mAliasLoaders.clear();
        this.mGroupAdapters.clear();
        AliasOperation aliasOperation = this.mAliasOperation;
        if (aliasOperation != null) {
            aliasOperation.cancel(true);
            this.mAliasOperation = null;
        }
        closeKeyChainConnections();
        super.onDestroy();
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 1) {
            int i3 = this.mConfirmingCredentialUser;
            IntConsumer intConsumer = this.mConfirmingCredentialListener;
            this.mConfirmingCredentialUser = -10000;
            this.mConfirmingCredentialListener = null;
            if (i2 == -1) {
                this.mConfirmedCredentialUsers.add(Integer.valueOf(i3));
                if (intConsumer != null) {
                    intConsumer.accept(i3);
                }
            }
        }
    }

    private void closeKeyChainConnections() {
        synchronized (this.mKeyChainConnectionByProfileId) {
            int size = this.mKeyChainConnectionByProfileId.size();
            for (int i = 0; i < size; i++) {
                this.mKeyChainConnectionByProfileId.valueAt(i).close();
            }
            this.mKeyChainConnectionByProfileId.clear();
        }
    }

    private void addTab(Tab tab) {
        this.mTabHost.addTab(this.mTabHost.newTabSpec(tab.mTag).setIndicator(getActivity().getString(tab.mLabel)).setContent(tab.mView));
        GroupAdapter groupAdapter = new GroupAdapter(tab);
        this.mGroupAdapters.add(groupAdapter);
        int groupCount = groupAdapter.getGroupCount();
        ViewGroup viewGroup = (ViewGroup) this.mTabHost.findViewById(tab.mContentView);
        viewGroup.getLayoutTransition().enableTransitionType(4);
        LayoutInflater from = LayoutInflater.from(getActivity());
        for (int i = 0; i < groupAdapter.getGroupCount(); i++) {
            boolean isManagedProfile = groupAdapter.getUserInfoByGroup(i).isManagedProfile();
            ChildAdapter childAdapter = groupAdapter.getChildAdapter(i);
            LinearLayout linearLayout = (LinearLayout) from.inflate(R.layout.trusted_credential_list_container, viewGroup, false);
            childAdapter.setContainerView(linearLayout);
            boolean z = true;
            childAdapter.showHeader(groupCount > 1);
            childAdapter.showDivider(isManagedProfile);
            if (groupCount > 2 && isManagedProfile) {
                z = false;
            }
            childAdapter.setExpandIfAvailable(z);
            if (isManagedProfile) {
                viewGroup.addView(linearLayout);
            } else {
                viewGroup.addView(linearLayout, 0);
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean startConfirmCredential(int i) {
        Intent createConfirmDeviceCredentialIntent = this.mKeyguardManager.createConfirmDeviceCredentialIntent((CharSequence) null, (CharSequence) null, i);
        if (createConfirmDeviceCredentialIntent == null) {
            return false;
        }
        this.mConfirmingCredentialUser = i;
        startActivityForResult(createConfirmDeviceCredentialIntent, 1);
        return true;
    }

    private class GroupAdapter extends BaseExpandableListAdapter implements ExpandableListView.OnGroupClickListener, ExpandableListView.OnChildClickListener, View.OnClickListener {
        private final AdapterData mData;

        public long getChildId(int i, int i2) {
            return (long) i2;
        }

        public boolean hasStableIds() {
            return false;
        }

        public boolean isChildSelectable(int i, int i2) {
            return true;
        }

        private GroupAdapter(Tab tab) {
            this.mData = new AdapterData(tab, this);
            load();
        }

        public int getGroupCount() {
            return this.mData.mCertHoldersByUserId.size();
        }

        public int getChildrenCount(int i) {
            List list = (List) this.mData.mCertHoldersByUserId.valueAt(i);
            if (list != null) {
                return list.size();
            }
            return 0;
        }

        public UserHandle getGroup(int i) {
            return new UserHandle(this.mData.mCertHoldersByUserId.keyAt(i));
        }

        public CertHolder getChild(int i, int i2) {
            return (CertHolder) ((List) this.mData.mCertHoldersByUserId.get(getUserIdByGroup(i))).get(i2);
        }

        public long getGroupId(int i) {
            return (long) getUserIdByGroup(i);
        }

        private int getUserIdByGroup(int i) {
            return this.mData.mCertHoldersByUserId.keyAt(i);
        }

        public UserInfo getUserInfoByGroup(int i) {
            return TrustedCredentialsSettings.this.mUserManager.getUserInfo(getUserIdByGroup(i));
        }

        public View getGroupView(int i, boolean z, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = Utils.inflateCategoryHeader((LayoutInflater) TrustedCredentialsSettings.this.getActivity().getSystemService("layout_inflater"), viewGroup);
            }
            TextView textView = (TextView) view.findViewById(16908310);
            if (getUserInfoByGroup(i).isManagedProfile()) {
                textView.setText(R.string.category_work);
            } else {
                textView.setText(R.string.category_personal);
            }
            textView.setTextAlignment(6);
            return view;
        }

        public View getChildView(int i, int i2, boolean z, View view, ViewGroup viewGroup) {
            return getViewForCertificate(getChild(i, i2), this.mData.mTab, view, viewGroup);
        }

        public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i2, long j) {
            TrustedCredentialsSettings.this.showCertDialog(getChild(i, i2));
            return true;
        }

        public void onClick(View view) {
            TrustedCredentialsSettings.this.removeOrInstallCert((CertHolder) view.getTag());
        }

        public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long j) {
            return !checkGroupExpandableAndStartWarningActivity(i);
        }

        public void load() {
            AdapterData adapterData = this.mData;
            Objects.requireNonNull(adapterData);
            new AdapterData.AliasLoader().execute(new Void[0]);
        }

        public void remove(CertHolder certHolder) {
            this.mData.remove(certHolder);
        }

        public ChildAdapter getChildAdapter(int i) {
            return new ChildAdapter(this, i);
        }

        public boolean checkGroupExpandableAndStartWarningActivity(int i) {
            return checkGroupExpandableAndStartWarningActivity(i, true);
        }

        public boolean checkGroupExpandableAndStartWarningActivity(int i, boolean z) {
            UserHandle group = getGroup(i);
            int identifier = group.getIdentifier();
            if (TrustedCredentialsSettings.this.mUserManager.isQuietModeEnabled(group)) {
                Intent createInQuietModeDialogIntent = UnlaunchableAppActivity.createInQuietModeDialogIntent(identifier);
                if (z) {
                    TrustedCredentialsSettings.this.getActivity().startActivity(createInQuietModeDialogIntent);
                }
                return false;
            } else if (TrustedCredentialsSettings.this.mUserManager.isUserUnlocked(group) || !new LockPatternUtils(TrustedCredentialsSettings.this.getActivity()).isSeparateProfileChallengeEnabled(identifier)) {
                return true;
            } else {
                if (z) {
                    boolean unused = TrustedCredentialsSettings.this.startConfirmCredential(identifier);
                }
                return false;
            }
        }

        private View getViewForCertificate(CertHolder certHolder, Tab tab, View view, ViewGroup viewGroup) {
            View view2;
            ViewHolder viewHolder;
            if (view == null) {
                viewHolder = new ViewHolder();
                view2 = LayoutInflater.from(TrustedCredentialsSettings.this.getActivity()).inflate(R.layout.trusted_credential, viewGroup, false);
                view2.setTag(viewHolder);
                TextView unused = viewHolder.mSubjectPrimaryView = (TextView) view2.findViewById(R.id.trusted_credential_subject_primary);
                TextView unused2 = viewHolder.mSubjectSecondaryView = (TextView) view2.findViewById(R.id.trusted_credential_subject_secondary);
                Switch unused3 = viewHolder.mSwitch = (Switch) view2.findViewById(R.id.trusted_credential_status);
                viewHolder.mSwitch.setOnClickListener(this);
            } else {
                view2 = view;
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.mSubjectPrimaryView.setText(certHolder.mSubjectPrimary);
            viewHolder.mSubjectSecondaryView.setText(certHolder.mSubjectSecondary);
            if (tab.mSwitch) {
                viewHolder.mSwitch.setChecked(!certHolder.mDeleted);
                viewHolder.mSwitch.setEnabled(!TrustedCredentialsSettings.this.mUserManager.hasUserRestriction("no_config_credentials", new UserHandle(certHolder.mProfileId)));
                viewHolder.mSwitch.setVisibility(0);
                viewHolder.mSwitch.setTag(certHolder);
            }
            return view2;
        }

        private class ViewHolder {
            /* access modifiers changed from: private */
            public TextView mSubjectPrimaryView;
            /* access modifiers changed from: private */
            public TextView mSubjectSecondaryView;
            /* access modifiers changed from: private */
            public Switch mSwitch;

            private ViewHolder() {
            }
        }
    }

    private class ChildAdapter extends BaseAdapter implements View.OnClickListener, AdapterView.OnItemClickListener {
        private final int[] EMPTY_STATE_SET;
        private final int[] GROUP_EXPANDED_STATE_SET;
        private final LinearLayout.LayoutParams HIDE_CONTAINER_LAYOUT_PARAMS;
        private final LinearLayout.LayoutParams HIDE_LIST_LAYOUT_PARAMS;
        private final LinearLayout.LayoutParams SHOW_LAYOUT_PARAMS;
        private LinearLayout mContainerView;
        private final int mGroupPosition;
        private ViewGroup mHeaderView;
        private ImageView mIndicatorView;
        private boolean mIsListExpanded;
        private ListView mListView;
        private final DataSetObserver mObserver;
        private final GroupAdapter mParent;

        private ChildAdapter(GroupAdapter groupAdapter, int i) {
            this.GROUP_EXPANDED_STATE_SET = new int[]{16842920};
            this.EMPTY_STATE_SET = new int[0];
            this.HIDE_CONTAINER_LAYOUT_PARAMS = new LinearLayout.LayoutParams(-1, -2, 0.0f);
            this.HIDE_LIST_LAYOUT_PARAMS = new LinearLayout.LayoutParams(-1, 0);
            this.SHOW_LAYOUT_PARAMS = new LinearLayout.LayoutParams(-1, -1, 1.0f);
            C05881 r0 = new DataSetObserver() {
                public void onChanged() {
                    super.onChanged();
                    ChildAdapter.super.notifyDataSetChanged();
                }

                public void onInvalidated() {
                    super.onInvalidated();
                    ChildAdapter.super.notifyDataSetInvalidated();
                }
            };
            this.mObserver = r0;
            this.mIsListExpanded = true;
            this.mParent = groupAdapter;
            this.mGroupPosition = i;
            groupAdapter.registerDataSetObserver(r0);
        }

        public int getCount() {
            return this.mParent.getChildrenCount(this.mGroupPosition);
        }

        public CertHolder getItem(int i) {
            return this.mParent.getChild(this.mGroupPosition, i);
        }

        public long getItemId(int i) {
            return this.mParent.getChildId(this.mGroupPosition, i);
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            return this.mParent.getChildView(this.mGroupPosition, i, false, view, viewGroup);
        }

        public void notifyDataSetChanged() {
            this.mParent.notifyDataSetChanged();
        }

        public void notifyDataSetInvalidated() {
            this.mParent.notifyDataSetInvalidated();
        }

        public void onClick(View view) {
            this.mIsListExpanded = checkGroupExpandableAndStartWarningActivity() && !this.mIsListExpanded;
            refreshViews();
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            TrustedCredentialsSettings.this.showCertDialog(getItem(i));
        }

        public void setContainerView(LinearLayout linearLayout) {
            this.mContainerView = linearLayout;
            ListView listView = (ListView) linearLayout.findViewById(R.id.cert_list);
            this.mListView = listView;
            listView.setAdapter(this);
            this.mListView.setOnItemClickListener(this);
            this.mListView.setItemsCanFocus(true);
            ViewGroup viewGroup = (ViewGroup) this.mContainerView.findViewById(R.id.header_view);
            this.mHeaderView = viewGroup;
            viewGroup.setOnClickListener(this);
            ImageView imageView = (ImageView) this.mHeaderView.findViewById(R.id.group_indicator);
            this.mIndicatorView = imageView;
            imageView.setImageDrawable(getGroupIndicator());
            FrameLayout frameLayout = (FrameLayout) this.mHeaderView.findViewById(R.id.header_content_container);
            frameLayout.addView(this.mParent.getGroupView(this.mGroupPosition, true, (View) null, frameLayout));
        }

        public void showHeader(boolean z) {
            this.mHeaderView.setVisibility(z ? 0 : 8);
        }

        public void showDivider(boolean z) {
            this.mHeaderView.findViewById(R.id.header_divider).setVisibility(z ? 0 : 8);
        }

        public void setExpandIfAvailable(boolean z) {
            boolean z2 = false;
            if (z && this.mParent.checkGroupExpandableAndStartWarningActivity(this.mGroupPosition, false)) {
                z2 = true;
            }
            this.mIsListExpanded = z2;
            refreshViews();
        }

        private boolean checkGroupExpandableAndStartWarningActivity() {
            return this.mParent.checkGroupExpandableAndStartWarningActivity(this.mGroupPosition);
        }

        private void refreshViews() {
            int[] iArr;
            LinearLayout.LayoutParams layoutParams;
            LinearLayout.LayoutParams layoutParams2;
            ImageView imageView = this.mIndicatorView;
            if (this.mIsListExpanded) {
                iArr = this.GROUP_EXPANDED_STATE_SET;
            } else {
                iArr = this.EMPTY_STATE_SET;
            }
            imageView.setImageState(iArr, false);
            ListView listView = this.mListView;
            if (this.mIsListExpanded) {
                layoutParams = this.SHOW_LAYOUT_PARAMS;
            } else {
                layoutParams = this.HIDE_LIST_LAYOUT_PARAMS;
            }
            listView.setLayoutParams(layoutParams);
            LinearLayout linearLayout = this.mContainerView;
            if (this.mIsListExpanded) {
                layoutParams2 = this.SHOW_LAYOUT_PARAMS;
            } else {
                layoutParams2 = this.HIDE_CONTAINER_LAYOUT_PARAMS;
            }
            linearLayout.setLayoutParams(layoutParams2);
        }

        private Drawable getGroupIndicator() {
            TypedArray obtainStyledAttributes = TrustedCredentialsSettings.this.getActivity().obtainStyledAttributes((AttributeSet) null, R.styleable.ExpandableListView, 16842863, 0);
            Drawable drawable = obtainStyledAttributes.getDrawable(0);
            obtainStyledAttributes.recycle();
            return drawable;
        }
    }

    private class AdapterData {
        /* access modifiers changed from: private */
        public final GroupAdapter mAdapter;
        /* access modifiers changed from: private */
        public final SparseArray<List<CertHolder>> mCertHoldersByUserId;
        /* access modifiers changed from: private */
        public final Tab mTab;

        private AdapterData(Tab tab, GroupAdapter groupAdapter) {
            this.mCertHoldersByUserId = new SparseArray<>();
            this.mAdapter = groupAdapter;
            this.mTab = tab;
        }

        private class AliasLoader extends AsyncTask<Void, Integer, SparseArray<List<CertHolder>>> {
            private View mContentView;
            private Context mContext;
            private ProgressBar mProgressBar;

            public AliasLoader() {
                this.mContext = TrustedCredentialsSettings.this.getActivity();
                TrustedCredentialsSettings.this.mAliasLoaders.add(this);
                for (UserHandle identifier : TrustedCredentialsSettings.this.mUserManager.getUserProfiles()) {
                    AdapterData.this.mCertHoldersByUserId.put(identifier.getIdentifier(), new ArrayList());
                }
            }

            private boolean shouldSkipProfile(UserHandle userHandle) {
                return TrustedCredentialsSettings.this.mUserManager.isQuietModeEnabled(userHandle) || !TrustedCredentialsSettings.this.mUserManager.isUserUnlocked(userHandle.getIdentifier());
            }

            /* access modifiers changed from: protected */
            public void onPreExecute() {
                FrameLayout tabContentView = TrustedCredentialsSettings.this.mTabHost.getTabContentView();
                this.mProgressBar = (ProgressBar) tabContentView.findViewById(AdapterData.this.mTab.mProgress);
                this.mContentView = tabContentView.findViewById(AdapterData.this.mTab.mContentView);
                this.mProgressBar.setVisibility(0);
                this.mContentView.setVisibility(8);
            }

            /* access modifiers changed from: protected */
            public SparseArray<List<CertHolder>> doInBackground(Void... voidArr) {
                SparseArray sparseArray;
                int i;
                List<UserHandle> list;
                SparseArray<List<CertHolder>> sparseArray2 = new SparseArray<>();
                try {
                    synchronized (TrustedCredentialsSettings.this.mKeyChainConnectionByProfileId) {
                        List<UserHandle> userProfiles = TrustedCredentialsSettings.this.mUserManager.getUserProfiles();
                        int size = userProfiles.size();
                        SparseArray sparseArray3 = new SparseArray(size);
                        int i2 = 0;
                        for (int i3 = 0; i3 < size; i3++) {
                            UserHandle userHandle = userProfiles.get(i3);
                            int identifier = userHandle.getIdentifier();
                            if (!shouldSkipProfile(userHandle)) {
                                KeyChain.KeyChainConnection bindAsUser = KeyChain.bindAsUser(this.mContext, userHandle);
                                TrustedCredentialsSettings.this.mKeyChainConnectionByProfileId.put(identifier, bindAsUser);
                                List access$2700 = AdapterData.this.mTab.getAliases(bindAsUser.getService());
                                if (isCancelled()) {
                                    SparseArray<List<CertHolder>> sparseArray4 = new SparseArray<>();
                                    return sparseArray4;
                                }
                                i2 += access$2700.size();
                                sparseArray3.put(identifier, access$2700);
                            }
                        }
                        int i4 = 0;
                        int i5 = 0;
                        while (i4 < size) {
                            UserHandle userHandle2 = userProfiles.get(i4);
                            int identifier2 = userHandle2.getIdentifier();
                            List list2 = (List) sparseArray3.get(identifier2);
                            if (isCancelled()) {
                                SparseArray<List<CertHolder>> sparseArray5 = new SparseArray<>();
                                return sparseArray5;
                            }
                            KeyChain.KeyChainConnection keyChainConnection = (KeyChain.KeyChainConnection) TrustedCredentialsSettings.this.mKeyChainConnectionByProfileId.get(identifier2);
                            if (!shouldSkipProfile(userHandle2) && list2 != null) {
                                if (keyChainConnection != null) {
                                    IKeyChainService service = keyChainConnection.getService();
                                    ArrayList arrayList = new ArrayList(i2);
                                    int size2 = list2.size();
                                    int i6 = 0;
                                    while (i6 < size2) {
                                        String str = (String) list2.get(i6);
                                        List<UserHandle> list3 = userProfiles;
                                        X509Certificate certificate = KeyChain.toCertificate(service.getEncodedCaCertificate(str, true));
                                        int i7 = size;
                                        SparseArray sparseArray6 = sparseArray3;
                                        int i8 = size2;
                                        IKeyChainService iKeyChainService = service;
                                        IKeyChainService iKeyChainService2 = service;
                                        ArrayList arrayList2 = arrayList;
                                        GroupAdapter access$2800 = AdapterData.this.mAdapter;
                                        List list4 = list2;
                                        int i9 = identifier2;
                                        arrayList2.add(new CertHolder(iKeyChainService, access$2800, AdapterData.this.mTab, str, certificate, i9));
                                        i5++;
                                        publishProgress(new Integer[]{Integer.valueOf(i5), Integer.valueOf(i2)});
                                        i6++;
                                        list2 = list4;
                                        identifier2 = i9;
                                        arrayList = arrayList2;
                                        userProfiles = list3;
                                        size = i7;
                                        sparseArray3 = sparseArray6;
                                        size2 = i8;
                                        service = iKeyChainService2;
                                    }
                                    list = userProfiles;
                                    i = size;
                                    sparseArray = sparseArray3;
                                    ArrayList arrayList3 = arrayList;
                                    Collections.sort(arrayList3);
                                    sparseArray2.put(identifier2, arrayList3);
                                    i4++;
                                    userProfiles = list;
                                    size = i;
                                    sparseArray3 = sparseArray;
                                }
                            }
                            list = userProfiles;
                            i = size;
                            sparseArray = sparseArray3;
                            sparseArray2.put(identifier2, new ArrayList(0));
                            i4++;
                            userProfiles = list;
                            size = i;
                            sparseArray3 = sparseArray;
                        }
                        return sparseArray2;
                    }
                } catch (RemoteException e) {
                    Log.e("TrustedCredentialsSettings", "Remote exception while loading aliases.", e);
                    return new SparseArray<>();
                } catch (InterruptedException e2) {
                    Log.e("TrustedCredentialsSettings", "InterruptedException while loading aliases.", e2);
                    return new SparseArray<>();
                }
            }

            /* access modifiers changed from: protected */
            public void onProgressUpdate(Integer... numArr) {
                int intValue = numArr[0].intValue();
                int intValue2 = numArr[1].intValue();
                if (intValue2 != this.mProgressBar.getMax()) {
                    this.mProgressBar.setMax(intValue2);
                }
                this.mProgressBar.setProgress(intValue);
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(SparseArray<List<CertHolder>> sparseArray) {
                AdapterData.this.mCertHoldersByUserId.clear();
                int size = sparseArray.size();
                for (int i = 0; i < size; i++) {
                    AdapterData.this.mCertHoldersByUserId.put(sparseArray.keyAt(i), sparseArray.valueAt(i));
                }
                AdapterData.this.mAdapter.notifyDataSetChanged();
                this.mProgressBar.setVisibility(8);
                this.mContentView.setVisibility(0);
                this.mProgressBar.setProgress(0);
                TrustedCredentialsSettings.this.mAliasLoaders.remove(this);
                showTrustAllCaDialogIfNeeded();
            }

            private boolean isUserTabAndTrustAllCertMode() {
                return TrustedCredentialsSettings.this.isTrustAllCaCertModeInProgress() && AdapterData.this.mTab == Tab.USER;
            }

            private void showTrustAllCaDialogIfNeeded() {
                List<CertHolder> list;
                if (isUserTabAndTrustAllCertMode() && (list = (List) AdapterData.this.mCertHoldersByUserId.get(TrustedCredentialsSettings.this.mTrustAllCaUserId)) != null) {
                    ArrayList arrayList = new ArrayList();
                    DevicePolicyManager devicePolicyManager = (DevicePolicyManager) this.mContext.getSystemService(DevicePolicyManager.class);
                    for (CertHolder certHolder : list) {
                        if (certHolder != null && !devicePolicyManager.isCaCertApproved(certHolder.mAlias, TrustedCredentialsSettings.this.mTrustAllCaUserId)) {
                            arrayList.add(certHolder);
                        }
                    }
                    if (arrayList.size() == 0) {
                        Log.w("TrustedCredentialsSettings", "no cert is pending approval for user " + TrustedCredentialsSettings.this.mTrustAllCaUserId);
                        return;
                    }
                    TrustedCredentialsSettings.this.showTrustAllCaDialog(arrayList);
                }
            }
        }

        public void remove(CertHolder certHolder) {
            List list;
            SparseArray<List<CertHolder>> sparseArray = this.mCertHoldersByUserId;
            if (sparseArray != null && (list = sparseArray.get(certHolder.mProfileId)) != null) {
                list.remove(certHolder);
            }
        }
    }

    static class CertHolder implements Comparable<CertHolder> {
        /* access modifiers changed from: private */
        public final GroupAdapter mAdapter;
        /* access modifiers changed from: private */
        public final String mAlias;
        /* access modifiers changed from: private */
        public boolean mDeleted;
        public int mProfileId;
        private final IKeyChainService mService;
        private final SslCertificate mSslCert;
        /* access modifiers changed from: private */
        public final String mSubjectPrimary;
        /* access modifiers changed from: private */
        public final String mSubjectSecondary;
        /* access modifiers changed from: private */
        public final Tab mTab;
        /* access modifiers changed from: private */
        public final X509Certificate mX509Cert;

        private CertHolder(IKeyChainService iKeyChainService, GroupAdapter groupAdapter, Tab tab, String str, X509Certificate x509Certificate, int i) {
            this.mProfileId = i;
            this.mService = iKeyChainService;
            this.mAdapter = groupAdapter;
            this.mTab = tab;
            this.mAlias = str;
            this.mX509Cert = x509Certificate;
            SslCertificate sslCertificate = new SslCertificate(x509Certificate);
            this.mSslCert = sslCertificate;
            String cName = sslCertificate.getIssuedTo().getCName();
            String oName = sslCertificate.getIssuedTo().getOName();
            String uName = sslCertificate.getIssuedTo().getUName();
            if (!oName.isEmpty()) {
                if (!cName.isEmpty()) {
                    this.mSubjectPrimary = oName;
                    this.mSubjectSecondary = cName;
                } else {
                    this.mSubjectPrimary = oName;
                    this.mSubjectSecondary = uName;
                }
            } else if (!cName.isEmpty()) {
                this.mSubjectPrimary = cName;
                this.mSubjectSecondary = "";
            } else {
                this.mSubjectPrimary = sslCertificate.getIssuedTo().getDName();
                this.mSubjectSecondary = "";
            }
            try {
                this.mDeleted = tab.deleted(iKeyChainService, str);
            } catch (RemoteException e) {
                Log.e("TrustedCredentialsSettings", "Remote exception while checking if alias " + this.mAlias + " is deleted.", e);
                this.mDeleted = false;
            }
        }

        public int compareTo(CertHolder certHolder) {
            int compareToIgnoreCase = this.mSubjectPrimary.compareToIgnoreCase(certHolder.mSubjectPrimary);
            if (compareToIgnoreCase != 0) {
                return compareToIgnoreCase;
            }
            return this.mSubjectSecondary.compareToIgnoreCase(certHolder.mSubjectSecondary);
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof CertHolder)) {
                return false;
            }
            return this.mAlias.equals(((CertHolder) obj).mAlias);
        }

        public int hashCode() {
            return this.mAlias.hashCode();
        }

        public int getUserId() {
            return this.mProfileId;
        }

        public String getAlias() {
            return this.mAlias;
        }

        public boolean isSystemCert() {
            return this.mTab == Tab.SYSTEM;
        }

        public boolean isDeleted() {
            return this.mDeleted;
        }
    }

    /* access modifiers changed from: private */
    public boolean isTrustAllCaCertModeInProgress() {
        return this.mTrustAllCaUserId != -10000;
    }

    /* access modifiers changed from: private */
    public void showTrustAllCaDialog(List<CertHolder> list) {
        new TrustedCredentialsDialogBuilder(getActivity(), this).setCertHolders((CertHolder[]) list.toArray(new CertHolder[list.size()])).setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialogInterface) {
                TrustedCredentialsSettings.this.getActivity().getIntent().removeExtra("ARG_SHOW_NEW_FOR_USER");
                int unused = TrustedCredentialsSettings.this.mTrustAllCaUserId = -10000;
            }
        }).show();
    }

    /* access modifiers changed from: private */
    public void showCertDialog(CertHolder certHolder) {
        new TrustedCredentialsDialogBuilder(getActivity(), this).setCertHolder(certHolder).show();
    }

    public List<X509Certificate> getX509CertsFromCertHolder(CertHolder certHolder) {
        ArrayList arrayList = null;
        try {
            synchronized (this.mKeyChainConnectionByProfileId) {
                try {
                    IKeyChainService service = this.mKeyChainConnectionByProfileId.get(certHolder.mProfileId).getService();
                    List caCertificateChainAliases = service.getCaCertificateChainAliases(certHolder.mAlias, true);
                    int size = caCertificateChainAliases.size();
                    ArrayList arrayList2 = new ArrayList(size);
                    int i = 0;
                    while (i < size) {
                        try {
                            arrayList2.add(KeyChain.toCertificate(service.getEncodedCaCertificate((String) caCertificateChainAliases.get(i), true)));
                            i++;
                        } catch (Throwable th) {
                            th = th;
                            arrayList = arrayList2;
                            throw th;
                        }
                    }
                    return arrayList2;
                } catch (Throwable th2) {
                    th = th2;
                    throw th;
                }
            }
        } catch (RemoteException e) {
            Log.e("TrustedCredentialsSettings", "RemoteException while retrieving certificate chain for root " + certHolder.mAlias, e);
            return arrayList;
        }
    }

    public void removeOrInstallCert(CertHolder certHolder) {
        new AliasOperation(certHolder).execute(new Void[0]);
    }

    public boolean startConfirmCredentialIfNotConfirmed(int i, IntConsumer intConsumer) {
        if (this.mConfirmedCredentialUsers.contains(Integer.valueOf(i))) {
            return false;
        }
        boolean startConfirmCredential = startConfirmCredential(i);
        if (startConfirmCredential) {
            this.mConfirmingCredentialListener = intConsumer;
        }
        return startConfirmCredential;
    }

    private class AliasOperation extends AsyncTask<Void, Void, Boolean> {
        private final CertHolder mCertHolder;

        private AliasOperation(CertHolder certHolder) {
            this.mCertHolder = certHolder;
            AliasOperation unused = TrustedCredentialsSettings.this.mAliasOperation = this;
        }

        /* access modifiers changed from: protected */
        public Boolean doInBackground(Void... voidArr) {
            try {
                synchronized (TrustedCredentialsSettings.this.mKeyChainConnectionByProfileId) {
                    IKeyChainService service = ((KeyChain.KeyChainConnection) TrustedCredentialsSettings.this.mKeyChainConnectionByProfileId.get(this.mCertHolder.mProfileId)).getService();
                    if (this.mCertHolder.mDeleted) {
                        service.installCaCertificate(this.mCertHolder.mX509Cert.getEncoded());
                        Boolean bool = Boolean.TRUE;
                        return bool;
                    }
                    Boolean valueOf = Boolean.valueOf(service.deleteCaCertificate(this.mCertHolder.mAlias));
                    return valueOf;
                }
            } catch (RemoteException | IllegalStateException | SecurityException | CertificateEncodingException e) {
                Log.w("TrustedCredentialsSettings", "Error while toggling alias " + this.mCertHolder.mAlias, e);
                return Boolean.FALSE;
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Boolean bool) {
            if (bool.booleanValue()) {
                if (this.mCertHolder.mTab.mSwitch) {
                    CertHolder certHolder = this.mCertHolder;
                    boolean unused = certHolder.mDeleted = !certHolder.mDeleted;
                } else {
                    this.mCertHolder.mAdapter.remove(this.mCertHolder);
                }
                this.mCertHolder.mAdapter.notifyDataSetChanged();
            } else {
                this.mCertHolder.mAdapter.load();
            }
            AliasOperation unused2 = TrustedCredentialsSettings.this.mAliasOperation = null;
        }
    }
}
