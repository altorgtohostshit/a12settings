package com.android.settings.applications;

import android.app.ActivityManager;
import android.app.AppGlobals;
import android.app.GrantedUriPermission;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.storage.StorageManager;
import android.os.storage.VolumeInfo;
import android.util.Log;
import android.util.MutableInt;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.content.Loader;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.applications.AppStorageSizesController;
import com.android.settings.deviceinfo.StorageWizardMoveConfirm;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.applications.AppUtils;
import com.android.settingslib.applications.ApplicationsState;
import com.android.settingslib.applications.StorageStatsSource;
import com.android.settingslib.widget.ActionButtonsPreference;
import com.android.settingslib.widget.LayoutPreference;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class AppStorageSettings extends AppInfoWithHeader implements View.OnClickListener, ApplicationsState.Callbacks, DialogInterface.OnClickListener {
    private static final String TAG = AppStorageSettings.class.getSimpleName();
    ActionButtonsPreference mButtonsPref;
    /* access modifiers changed from: private */
    public boolean mCacheCleared;
    private boolean mCanClearData = true;
    private VolumeInfo[] mCandidates;
    private Button mChangeStorageButton;
    private ClearCacheObserver mClearCacheObserver;
    private ClearUserDataObserver mClearDataObserver;
    private LayoutPreference mClearUri;
    private Button mClearUriButton;
    /* access modifiers changed from: private */
    public boolean mDataCleared;
    private AlertDialog.Builder mDialogBuilder;
    /* access modifiers changed from: private */
    public final Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            if (AppStorageSettings.this.getView() != null) {
                int i = message.what;
                if (i == 1) {
                    boolean unused = AppStorageSettings.this.mDataCleared = true;
                    boolean unused2 = AppStorageSettings.this.mCacheCleared = true;
                    AppStorageSettings.this.processClearMsg(message);
                } else if (i == 3) {
                    boolean unused3 = AppStorageSettings.this.mCacheCleared = true;
                    AppStorageSettings.this.updateSize();
                }
            }
        }
    };
    private ApplicationInfo mInfo;
    AppStorageSizesController mSizeController;
    private Preference mStorageUsed;
    private PreferenceCategory mUri;

    public int getMetricsCategory() {
        return 19;
    }

    public void onLoaderReset(Loader<StorageStatsSource.AppStorageStats> loader) {
    }

    public void onPackageSizeChanged(String str) {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            boolean z = false;
            this.mCacheCleared = bundle.getBoolean("cache_cleared", false);
            boolean z2 = bundle.getBoolean("data_cleared", false);
            this.mDataCleared = z2;
            if (this.mCacheCleared || z2) {
                z = true;
            }
            this.mCacheCleared = z;
        }
        addPreferencesFromResource(R.xml.app_storage_settings);
        setupViews();
        initMoveDialog();
    }

    public void onResume() {
        super.onResume();
        updateSize();
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("cache_cleared", this.mCacheCleared);
        bundle.putBoolean("data_cleared", this.mDataCleared);
    }

    private void setupViews() {
        this.mSizeController = new AppStorageSizesController.Builder().setTotalSizePreference(findPreference("total_size")).setAppSizePreference(findPreference("app_size")).setDataSizePreference(findPreference("data_size")).setCacheSizePreference(findPreference("cache_size")).setComputingString(R.string.computing_size).setErrorString(R.string.invalid_size_value).build();
        this.mButtonsPref = (ActionButtonsPreference) findPreference("header_view");
        this.mStorageUsed = findPreference("storage_used");
        Button button = (Button) ((LayoutPreference) findPreference("change_storage_button")).findViewById(R.id.button);
        this.mChangeStorageButton = button;
        button.setText(R.string.change);
        this.mChangeStorageButton.setOnClickListener(this);
        this.mButtonsPref.setButton2Text(R.string.clear_cache_btn_text).setButton2Icon(R.drawable.ic_settings_delete);
        PreferenceCategory preferenceCategory = (PreferenceCategory) findPreference("uri_category");
        this.mUri = preferenceCategory;
        LayoutPreference layoutPreference = (LayoutPreference) preferenceCategory.findPreference("clear_uri_button");
        this.mClearUri = layoutPreference;
        Button button2 = (Button) layoutPreference.findViewById(R.id.button);
        this.mClearUriButton = button2;
        button2.setText(R.string.clear_uri_btn_text);
        this.mClearUriButton.setOnClickListener(this);
    }

    /* access modifiers changed from: package-private */
    public void handleClearCacheClick() {
        if (this.mAppsControlDisallowedAdmin == null || this.mAppsControlDisallowedBySystem) {
            if (this.mClearCacheObserver == null) {
                this.mClearCacheObserver = new ClearCacheObserver();
            }
            this.mMetricsFeatureProvider.action(getContext(), 877, (Pair<Integer, Object>[]) new Pair[0]);
            this.mPm.deleteApplicationCacheFiles(this.mPackageName, this.mClearCacheObserver);
            return;
        }
        RestrictedLockUtils.sendShowAdminSupportDetailsIntent(getActivity(), this.mAppsControlDisallowedAdmin);
    }

    /* access modifiers changed from: package-private */
    public void handleClearDataClick() {
        if (this.mAppsControlDisallowedAdmin != null && !this.mAppsControlDisallowedBySystem) {
            RestrictedLockUtils.sendShowAdminSupportDetailsIntent(getActivity(), this.mAppsControlDisallowedAdmin);
        } else if (this.mAppEntry.info.manageSpaceActivityName == null) {
            showDialogInner(1, 0);
        } else if (!Utils.isMonkeyRunning()) {
            Intent intent = new Intent("android.intent.action.VIEW");
            ApplicationInfo applicationInfo = this.mAppEntry.info;
            intent.setClassName(applicationInfo.packageName, applicationInfo.manageSpaceActivityName);
            startActivityForResult(intent, 2);
        }
    }

    public void onClick(View view) {
        if (view == this.mChangeStorageButton && this.mDialogBuilder != null && !isMoveInProgress()) {
            this.mDialogBuilder.show();
        } else if (view != this.mClearUriButton) {
        } else {
            if (this.mAppsControlDisallowedAdmin == null || this.mAppsControlDisallowedBySystem) {
                clearUriPermissions();
            } else {
                RestrictedLockUtils.sendShowAdminSupportDetailsIntent(getActivity(), this.mAppsControlDisallowedAdmin);
            }
        }
    }

    private boolean isMoveInProgress() {
        try {
            AppGlobals.getPackageManager().checkPackageStartable(this.mPackageName, UserHandle.myUserId());
            return false;
        } catch (RemoteException | SecurityException unused) {
            return true;
        }
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        FragmentActivity activity = getActivity();
        VolumeInfo volumeInfo = this.mCandidates[i];
        if (!Objects.equals(volumeInfo, activity.getPackageManager().getPackageCurrentVolume(this.mAppEntry.info))) {
            Intent intent = new Intent(activity, StorageWizardMoveConfirm.class);
            intent.putExtra("android.os.storage.extra.VOLUME_ID", volumeInfo.getId());
            intent.putExtra("android.intent.extra.PACKAGE_NAME", this.mAppEntry.info.packageName);
            startActivity(intent);
        }
        dialogInterface.dismiss();
    }

    /* access modifiers changed from: protected */
    public boolean refreshUi() {
        retrieveAppEntry();
        if (this.mAppEntry == null) {
            return false;
        }
        updateUiWithSize(this.mSizeController.getLastResult());
        refreshGrantedUriPermissions();
        this.mStorageUsed.setSummary((CharSequence) ((StorageManager) getContext().getSystemService(StorageManager.class)).getBestVolumeDescription(getActivity().getPackageManager().getPackageCurrentVolume(this.mAppEntry.info)));
        refreshButtons();
        return true;
    }

    private void refreshButtons() {
        initMoveDialog();
        initDataButtons();
    }

    private void initDataButtons() {
        boolean z = true;
        boolean z2 = this.mAppEntry.info.manageSpaceActivityName != null;
        boolean z3 = ((this.mAppEntry.info.flags & 65) == 1) || this.mDpm.packageHasActiveAdmins(this.mPackageName);
        Intent intent = new Intent("android.intent.action.VIEW");
        if (z2) {
            ApplicationInfo applicationInfo = this.mAppEntry.info;
            intent.setClassName(applicationInfo.packageName, applicationInfo.manageSpaceActivityName);
        }
        if (getPackageManager().resolveActivity(intent, 0) == null) {
            z = false;
        }
        if ((z2 || !z3) && z) {
            if (z2) {
                this.mButtonsPref.setButton1Text(R.string.manage_space_text);
            } else {
                this.mButtonsPref.setButton1Text(R.string.clear_user_data_text);
            }
            this.mButtonsPref.setButton1Icon(R.drawable.ic_settings_delete).setButton1OnClickListener(new AppStorageSettings$$ExternalSyntheticLambda0(this));
        } else {
            this.mButtonsPref.setButton1Text(R.string.clear_user_data_text).setButton1Icon(R.drawable.ic_settings_delete).setButton1Enabled(false);
            this.mCanClearData = false;
        }
        if (this.mAppsControlDisallowedBySystem || AppUtils.isMainlineModule(this.mPm, this.mPackageName)) {
            this.mButtonsPref.setButton1Enabled(false);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initDataButtons$0(View view) {
        handleClearDataClick();
    }

    private void initMoveDialog() {
        FragmentActivity activity = getActivity();
        StorageManager storageManager = (StorageManager) activity.getSystemService(StorageManager.class);
        List packageCandidateVolumes = activity.getPackageManager().getPackageCandidateVolumes(this.mAppEntry.info);
        if (packageCandidateVolumes.size() > 1) {
            Collections.sort(packageCandidateVolumes, VolumeInfo.getDescriptionComparator());
            CharSequence[] charSequenceArr = new CharSequence[packageCandidateVolumes.size()];
            int i = -1;
            for (int i2 = 0; i2 < packageCandidateVolumes.size(); i2++) {
                String bestVolumeDescription = storageManager.getBestVolumeDescription((VolumeInfo) packageCandidateVolumes.get(i2));
                if (Objects.equals(bestVolumeDescription, this.mStorageUsed.getSummary())) {
                    i = i2;
                }
                charSequenceArr[i2] = bestVolumeDescription;
            }
            this.mCandidates = (VolumeInfo[]) packageCandidateVolumes.toArray(new VolumeInfo[packageCandidateVolumes.size()]);
            this.mDialogBuilder = new AlertDialog.Builder(getContext()).setTitle((int) R.string.change_storage).setSingleChoiceItems(charSequenceArr, i, (DialogInterface.OnClickListener) this).setNegativeButton((int) R.string.cancel, (DialogInterface.OnClickListener) null);
            return;
        }
        removePreference("storage_used");
        removePreference("change_storage_button");
        removePreference("storage_space");
    }

    /* access modifiers changed from: private */
    public void initiateClearUserData() {
        this.mMetricsFeatureProvider.action(getContext(), 876, (Pair<Integer, Object>[]) new Pair[0]);
        this.mButtonsPref.setButton1Enabled(false);
        String str = this.mAppEntry.info.packageName;
        String str2 = TAG;
        Log.i(str2, "Clearing user data for package : " + str);
        if (this.mClearDataObserver == null) {
            this.mClearDataObserver = new ClearUserDataObserver();
        }
        if (!((ActivityManager) getActivity().getSystemService("activity")).clearApplicationUserData(str, this.mClearDataObserver)) {
            Log.i(str2, "Couldn't clear application user data for package:" + str);
            showDialogInner(2, 0);
            return;
        }
        this.mButtonsPref.setButton1Text(R.string.recompute_size);
    }

    /* access modifiers changed from: private */
    public void processClearMsg(Message message) {
        int i = message.arg1;
        String str = this.mAppEntry.info.packageName;
        this.mButtonsPref.setButton1Text(R.string.clear_user_data_text).setButton1Icon(R.drawable.ic_settings_delete);
        if (i == 1) {
            String str2 = TAG;
            Log.i(str2, "Cleared user data for package : " + str);
            updateSize();
            return;
        }
        this.mButtonsPref.setButton1Enabled(true);
    }

    private void refreshGrantedUriPermissions() {
        removeUriPermissionsFromUi();
        List<GrantedUriPermission> list = ((ActivityManager) getActivity().getSystemService("activity")).getGrantedUriPermissions(this.mAppEntry.info.packageName).getList();
        if (list.isEmpty()) {
            this.mClearUriButton.setVisibility(8);
            return;
        }
        PackageManager packageManager = getActivity().getPackageManager();
        TreeMap treeMap = new TreeMap();
        for (GrantedUriPermission grantedUriPermission : list) {
            ProviderInfo resolveContentProvider = packageManager.resolveContentProvider(grantedUriPermission.uri.getAuthority(), 0);
            if (resolveContentProvider != null) {
                CharSequence loadLabel = resolveContentProvider.applicationInfo.loadLabel(packageManager);
                MutableInt mutableInt = (MutableInt) treeMap.get(loadLabel);
                if (mutableInt == null) {
                    treeMap.put(loadLabel, new MutableInt(1));
                } else {
                    mutableInt.value++;
                }
            }
        }
        for (Map.Entry entry : treeMap.entrySet()) {
            int i = ((MutableInt) entry.getValue()).value;
            Preference preference = new Preference(getPrefContext());
            preference.setTitle((CharSequence) entry.getKey());
            preference.setSummary((CharSequence) getPrefContext().getResources().getQuantityString(R.plurals.uri_permissions_text, i, new Object[]{Integer.valueOf(i)}));
            preference.setSelectable(false);
            preference.setLayoutResource(R.layout.horizontal_preference);
            preference.setOrder(0);
            Log.v(TAG, "Adding preference '" + preference + "' at order " + 0);
            this.mUri.addPreference(preference);
        }
        if (this.mAppsControlDisallowedBySystem) {
            this.mClearUriButton.setEnabled(false);
        }
        this.mClearUri.setOrder(0);
        this.mClearUriButton.setVisibility(0);
    }

    private void clearUriPermissions() {
        FragmentActivity activity = getActivity();
        ((ActivityManager) activity.getSystemService("activity")).clearGrantedUriPermissions(this.mAppEntry.info.packageName);
        refreshGrantedUriPermissions();
    }

    private void removeUriPermissionsFromUi() {
        for (int preferenceCount = this.mUri.getPreferenceCount() - 1; preferenceCount >= 0; preferenceCount--) {
            Preference preference = this.mUri.getPreference(preferenceCount);
            if (preference != this.mClearUri) {
                this.mUri.removePreference(preference);
            }
        }
    }

    /* access modifiers changed from: protected */
    public AlertDialog createDialog(int i, int i2) {
        if (i == 1) {
            return new AlertDialog.Builder(getActivity()).setTitle(getActivity().getText(R.string.clear_data_dlg_title)).setMessage(getActivity().getText(R.string.clear_data_dlg_text)).setPositiveButton((int) R.string.dlg_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    AppStorageSettings.this.initiateClearUserData();
                }
            }).setNegativeButton((int) R.string.dlg_cancel, (DialogInterface.OnClickListener) null).create();
        }
        if (i != 2) {
            return null;
        }
        return new AlertDialog.Builder(getActivity()).setTitle(getActivity().getText(R.string.clear_user_data_text)).setMessage(getActivity().getText(R.string.clear_failed_dlg_text)).setNeutralButton(R.string.dlg_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                AppStorageSettings.this.mButtonsPref.setButton1Enabled(false);
                AppStorageSettings.this.setIntentAndFinish(false);
            }
        }).create();
    }

    public Loader<StorageStatsSource.AppStorageStats> onCreateLoader(int i, Bundle bundle) {
        Context context = getContext();
        return new FetchPackageStorageAsyncLoader(context, new StorageStatsSource(context), this.mInfo, UserHandle.of(this.mUserId));
    }

    public void onLoadFinished(Loader<StorageStatsSource.AppStorageStats> loader, StorageStatsSource.AppStorageStats appStorageStats) {
        this.mSizeController.setResult(appStorageStats);
        updateUiWithSize(appStorageStats);
    }

    /* JADX WARNING: type inference failed for: r3v0, types: [com.android.settings.SettingsPreferenceFragment, com.android.settings.applications.AppInfoBase, androidx.fragment.app.Fragment, androidx.loader.app.LoaderManager$LoaderCallbacks, com.android.settings.applications.AppStorageSettings] */
    /* access modifiers changed from: private */
    public void updateSize() {
        try {
            this.mInfo = getPackageManager().getApplicationInfo(this.mPackageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Could not find package", e);
        }
        if (this.mInfo != null) {
            getLoaderManager().restartLoader(1, Bundle.EMPTY, this);
        }
    }

    /* access modifiers changed from: package-private */
    public void updateUiWithSize(StorageStatsSource.AppStorageStats appStorageStats) {
        if (this.mCacheCleared) {
            this.mSizeController.setCacheCleared(true);
        }
        if (this.mDataCleared) {
            this.mSizeController.setDataCleared(true);
        }
        this.mSizeController.updateUi(getContext());
        if (appStorageStats == null) {
            this.mButtonsPref.setButton1Enabled(false).setButton2Enabled(false);
        } else {
            long cacheBytes = appStorageStats.getCacheBytes();
            if (appStorageStats.getDataBytes() - cacheBytes <= 0 || !this.mCanClearData || this.mDataCleared) {
                this.mButtonsPref.setButton1Enabled(false);
            } else {
                this.mButtonsPref.setButton1Enabled(true).setButton1OnClickListener(new AppStorageSettings$$ExternalSyntheticLambda1(this));
            }
            if (cacheBytes <= 0 || this.mCacheCleared) {
                this.mButtonsPref.setButton2Enabled(false);
            } else {
                this.mButtonsPref.setButton2Enabled(true).setButton2OnClickListener(new AppStorageSettings$$ExternalSyntheticLambda2(this));
            }
        }
        if (this.mAppsControlDisallowedBySystem || AppUtils.isMainlineModule(this.mPm, this.mPackageName)) {
            this.mButtonsPref.setButton1Enabled(false).setButton2Enabled(false);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateUiWithSize$1(View view) {
        handleClearDataClick();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateUiWithSize$2(View view) {
        handleClearCacheClick();
    }

    class ClearCacheObserver extends IPackageDataObserver.Stub {
        ClearCacheObserver() {
        }

        public void onRemoveCompleted(String str, boolean z) {
            Message obtainMessage = AppStorageSettings.this.mHandler.obtainMessage(3);
            obtainMessage.arg1 = z ? 1 : 2;
            AppStorageSettings.this.mHandler.sendMessage(obtainMessage);
        }
    }

    class ClearUserDataObserver extends IPackageDataObserver.Stub {
        ClearUserDataObserver() {
        }

        public void onRemoveCompleted(String str, boolean z) {
            int i = 1;
            Message obtainMessage = AppStorageSettings.this.mHandler.obtainMessage(1);
            if (!z) {
                i = 2;
            }
            obtainMessage.arg1 = i;
            AppStorageSettings.this.mHandler.sendMessage(obtainMessage);
        }
    }
}
