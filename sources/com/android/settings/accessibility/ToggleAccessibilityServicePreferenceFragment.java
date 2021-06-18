package com.android.settings.accessibility;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.os.storage.StorageManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Switch;
import androidx.preference.Preference;
import com.android.internal.widget.LockPatternUtils;
import com.android.settings.R;
import com.android.settings.password.ConfirmDeviceCredentialActivity;
import com.android.settings.widget.SettingsMainSwitchPreference;
import com.android.settingslib.accessibility.AccessibilityUtils;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ToggleAccessibilityServicePreferenceFragment extends ToggleFeaturePreferenceFragment {
    private Dialog mDialog;
    private AtomicBoolean mIsDialogShown = new AtomicBoolean(false);
    private LockPatternUtils mLockPatternUtils;
    private BroadcastReceiver mPackageRemovedReceiver;
    private final SettingsContentObserver mSettingsContentObserver = new SettingsContentObserver(new Handler()) {
        public void onChange(boolean z, Uri uri) {
            ToggleAccessibilityServicePreferenceFragment.this.updateSwitchBarToggleSwitch();
        }
    };

    public int getMetricsCategory() {
        return 4;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mLockPatternUtils = new LockPatternUtils(getPrefContext());
    }

    public void onStart() {
        super.onStart();
        AccessibilityServiceInfo accessibilityServiceInfo = getAccessibilityServiceInfo();
        if (accessibilityServiceInfo == null) {
            getActivity().finishAndRemoveTask();
        } else if (!AccessibilityUtil.isSystemApp(accessibilityServiceInfo)) {
            registerPackageRemoveReceiver();
        }
    }

    public void onResume() {
        super.onResume();
        updateSwitchBarToggleSwitch();
        this.mSettingsContentObserver.register(getContentResolver());
    }

    public void onPreferenceToggled(String str, boolean z) {
        ComponentName unflattenFromString = ComponentName.unflattenFromString(str);
        AccessibilityStatsLogUtils.logAccessibilityServiceEnabled(unflattenFromString, z);
        AccessibilityUtils.setAccessibilityServiceState(getPrefContext(), unflattenFromString, z);
    }

    /* access modifiers changed from: package-private */
    public AccessibilityServiceInfo getAccessibilityServiceInfo() {
        List<AccessibilityServiceInfo> installedAccessibilityServiceList = AccessibilityManager.getInstance(getPrefContext()).getInstalledAccessibilityServiceList();
        int size = installedAccessibilityServiceList.size();
        for (int i = 0; i < size; i++) {
            AccessibilityServiceInfo accessibilityServiceInfo = installedAccessibilityServiceList.get(i);
            ResolveInfo resolveInfo = accessibilityServiceInfo.getResolveInfo();
            if (this.mComponentName.getPackageName().equals(resolveInfo.serviceInfo.packageName) && this.mComponentName.getClassName().equals(resolveInfo.serviceInfo.name)) {
                return accessibilityServiceInfo;
            }
        }
        return null;
    }

    public Dialog onCreateDialog(int i) {
        switch (i) {
            case 1002:
                AccessibilityServiceInfo accessibilityServiceInfo = getAccessibilityServiceInfo();
                if (accessibilityServiceInfo != null) {
                    this.mDialog = AccessibilityServiceWarning.createCapabilitiesDialog(getPrefContext(), accessibilityServiceInfo, new C0619xc0ece419(this), new C0620xc0ece41a(this));
                    break;
                } else {
                    return null;
                }
            case 1003:
                AccessibilityServiceInfo accessibilityServiceInfo2 = getAccessibilityServiceInfo();
                if (accessibilityServiceInfo2 != null) {
                    this.mDialog = AccessibilityServiceWarning.createCapabilitiesDialog(getPrefContext(), accessibilityServiceInfo2, new C0617xc0ece417(this), new C0620xc0ece41a(this));
                    break;
                } else {
                    return null;
                }
            case 1004:
                AccessibilityServiceInfo accessibilityServiceInfo3 = getAccessibilityServiceInfo();
                if (accessibilityServiceInfo3 != null) {
                    this.mDialog = AccessibilityServiceWarning.createCapabilitiesDialog(getPrefContext(), accessibilityServiceInfo3, new C0618xc0ece418(this), new C0620xc0ece41a(this));
                    break;
                } else {
                    return null;
                }
            case 1005:
                AccessibilityServiceInfo accessibilityServiceInfo4 = getAccessibilityServiceInfo();
                if (accessibilityServiceInfo4 != null) {
                    this.mDialog = AccessibilityServiceWarning.createDisableDialog(getPrefContext(), accessibilityServiceInfo4, new C0615xc0ece415(this));
                    break;
                } else {
                    return null;
                }
            default:
                this.mDialog = super.onCreateDialog(i);
                break;
        }
        return this.mDialog;
    }

    public int getDialogMetricsCategory(int i) {
        if (i == 1008) {
            return 1810;
        }
        switch (i) {
            case 1002:
            case 1003:
            case 1004:
                return 583;
            case 1005:
                return 584;
            default:
                return super.getDialogMetricsCategory(i);
        }
    }

    /* access modifiers changed from: package-private */
    public int getUserShortcutTypes() {
        return AccessibilityUtil.getUserShortcutTypesFromSettings(getPrefContext(), this.mComponentName);
    }

    /* access modifiers changed from: protected */
    public void updateToggleServiceTitle(SettingsMainSwitchPreference settingsMainSwitchPreference) {
        String str;
        AccessibilityServiceInfo accessibilityServiceInfo = getAccessibilityServiceInfo();
        if (accessibilityServiceInfo == null) {
            str = "";
        } else {
            str = getString(R.string.accessibility_service_primary_switch_title, accessibilityServiceInfo.getResolveInfo().loadLabel(getPackageManager()));
        }
        settingsMainSwitchPreference.setTitle(str);
    }

    /* access modifiers changed from: protected */
    public void updateSwitchBarToggleSwitch() {
        boolean contains = AccessibilityUtils.getEnabledServicesFromSettings(getPrefContext()).contains(this.mComponentName);
        if (this.mToggleServiceSwitchPreference.isChecked() != contains) {
            this.mToggleServiceSwitchPreference.setChecked(contains);
        }
    }

    private boolean isFullDiskEncrypted() {
        return StorageManager.isNonDefaultBlockEncrypted();
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (i != 1) {
            return;
        }
        if (i2 == -1) {
            handleConfirmServiceEnabled(true);
            if (isFullDiskEncrypted()) {
                this.mLockPatternUtils.clearEncryptionPassword();
                Settings.Global.putInt(getContentResolver(), "require_password_to_decrypt", 0);
                return;
            }
            return;
        }
        handleConfirmServiceEnabled(false);
    }

    private void registerPackageRemoveReceiver() {
        if (this.mPackageRemovedReceiver == null && getContext() != null) {
            this.mPackageRemovedReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    if (TextUtils.equals(ToggleAccessibilityServicePreferenceFragment.this.mComponentName.getPackageName(), intent.getData().getSchemeSpecificPart())) {
                        ToggleAccessibilityServicePreferenceFragment.this.getActivity().finishAndRemoveTask();
                    }
                }
            };
            IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_REMOVED");
            intentFilter.addDataScheme("package");
            getContext().registerReceiver(this.mPackageRemovedReceiver, intentFilter);
        }
    }

    private void unregisterPackageRemoveReceiver() {
        if (this.mPackageRemovedReceiver != null && getContext() != null) {
            getContext().unregisterReceiver(this.mPackageRemovedReceiver);
            this.mPackageRemovedReceiver = null;
        }
    }

    private boolean isServiceSupportAccessibilityButton() {
        ServiceInfo serviceInfo;
        for (AccessibilityServiceInfo next : ((AccessibilityManager) getPrefContext().getSystemService(AccessibilityManager.class)).getInstalledAccessibilityServiceList()) {
            if ((next.flags & 256) != 0 && (serviceInfo = next.getResolveInfo().serviceInfo) != null && TextUtils.equals(serviceInfo.name, getAccessibilityServiceInfo().getResolveInfo().serviceInfo.name)) {
                return true;
            }
        }
        return false;
    }

    private void handleConfirmServiceEnabled(boolean z) {
        this.mToggleServiceSwitchPreference.setChecked(z);
        getArguments().putBoolean("checked", z);
        onPreferenceToggled(this.mPreferenceKey, z);
    }

    private String createConfirmCredentialReasonMessage() {
        int keyguardStoredPasswordQuality = this.mLockPatternUtils.getKeyguardStoredPasswordQuality(UserHandle.myUserId());
        return getString(keyguardStoredPasswordQuality != 65536 ? (keyguardStoredPasswordQuality == 131072 || keyguardStoredPasswordQuality == 196608) ? R.string.enable_service_pin_reason : R.string.enable_service_password_reason : R.string.enable_service_pattern_reason, getAccessibilityServiceInfo().getResolveInfo().loadLabel(getPackageManager()));
    }

    public void onSwitchChanged(Switch switchR, boolean z) {
        if (z != this.mToggleServiceSwitchPreference.isChecked()) {
            onPreferenceClick(this.mToggleServiceSwitchPreference);
        }
    }

    public void onToggleClicked(ShortcutPreference shortcutPreference) {
        int retrieveUserShortcutType = PreferredShortcuts.retrieveUserShortcutType(getPrefContext(), this.mComponentName.flattenToString(), 1);
        if (!shortcutPreference.isChecked()) {
            AccessibilityUtil.optOutAllValuesFromSettings(getPrefContext(), retrieveUserShortcutType, this.mComponentName);
        } else if (!this.mToggleServiceSwitchPreference.isChecked()) {
            shortcutPreference.setChecked(false);
            showPopupDialog(1004);
        } else {
            AccessibilityUtil.optInAllValuesToSettings(getPrefContext(), retrieveUserShortcutType, this.mComponentName);
            showPopupDialog(1008);
        }
        this.mShortcutPreference.setSummary(getShortcutTypeSummary(getPrefContext()));
    }

    public void onSettingsClicked(ShortcutPreference shortcutPreference) {
        int i = 1;
        if (!(this.mShortcutPreference.isChecked() || this.mToggleServiceSwitchPreference.isChecked())) {
            i = 1003;
        }
        showPopupDialog(i);
    }

    /* access modifiers changed from: protected */
    public void onProcessArguments(Bundle bundle) {
        super.onProcessArguments(bundle);
        String string = bundle.getString("settings_title");
        String string2 = bundle.getString("settings_component_name");
        if (!TextUtils.isEmpty(string) && !TextUtils.isEmpty(string2)) {
            Intent component = new Intent("android.intent.action.MAIN").setComponent(ComponentName.unflattenFromString(string2.toString()));
            if (!getPackageManager().queryIntentActivities(component, 0).isEmpty()) {
                this.mSettingsTitle = string;
                this.mSettingsIntent = component;
                setHasOptionsMenu(true);
            }
        }
        this.mComponentName = (ComponentName) bundle.getParcelable("component_name");
        this.mImageUri = new Uri.Builder().scheme("android.resource").authority(this.mComponentName.getPackageName()).appendPath(String.valueOf(bundle.getInt("animated_image_res"))).build();
        this.mPackageName = getAccessibilityServiceInfo().getResolveInfo().loadLabel(getPackageManager());
    }

    /* access modifiers changed from: private */
    public void onDialogButtonFromDisableToggleClicked(DialogInterface dialogInterface, int i) {
        if (i == -2) {
            handleConfirmServiceEnabled(true);
        } else if (i == -1) {
            handleConfirmServiceEnabled(false);
        } else {
            throw new IllegalArgumentException("Unexpected button identifier");
        }
    }

    /* access modifiers changed from: private */
    public void onDialogButtonFromEnableToggleClicked(View view) {
        int id = view.getId();
        if (id == R.id.permission_enable_allow_button) {
            onAllowButtonFromEnableToggleClicked();
        } else if (id == R.id.permission_enable_deny_button) {
            onDenyButtonFromEnableToggleClicked();
        } else {
            throw new IllegalArgumentException("Unexpected view id");
        }
    }

    /* access modifiers changed from: private */
    public void onDialogButtonFromUninstallClicked() {
        this.mDialog.dismiss();
        Intent createUninstallPackageActivityIntent = createUninstallPackageActivityIntent();
        if (createUninstallPackageActivityIntent != null) {
            startActivity(createUninstallPackageActivityIntent);
        }
    }

    private Intent createUninstallPackageActivityIntent() {
        AccessibilityServiceInfo accessibilityServiceInfo = getAccessibilityServiceInfo();
        if (accessibilityServiceInfo == null) {
            Log.w("ToggleAccessibilityServicePreferenceFragment", "createUnInstallIntent -- invalid a11yServiceInfo");
            return null;
        }
        ApplicationInfo applicationInfo = accessibilityServiceInfo.getResolveInfo().serviceInfo.applicationInfo;
        return new Intent("android.intent.action.UNINSTALL_PACKAGE", Uri.parse("package:" + applicationInfo.packageName));
    }

    public void onStop() {
        super.onStop();
        unregisterPackageRemoveReceiver();
    }

    private void onAllowButtonFromEnableToggleClicked() {
        if (isFullDiskEncrypted()) {
            startActivityForResult(ConfirmDeviceCredentialActivity.createIntent(createConfirmCredentialReasonMessage(), (CharSequence) null), 1);
        } else {
            handleConfirmServiceEnabled(true);
            if (isServiceSupportAccessibilityButton()) {
                this.mIsDialogShown.set(false);
                showPopupDialog(1008);
            }
        }
        this.mDialog.dismiss();
    }

    private void onDenyButtonFromEnableToggleClicked() {
        handleConfirmServiceEnabled(false);
        this.mDialog.dismiss();
    }

    /* access modifiers changed from: package-private */
    public void onDialogButtonFromShortcutToggleClicked(View view) {
        int id = view.getId();
        if (id == R.id.permission_enable_allow_button) {
            onAllowButtonFromShortcutToggleClicked();
        } else if (id == R.id.permission_enable_deny_button) {
            onDenyButtonFromShortcutToggleClicked();
        } else {
            throw new IllegalArgumentException("Unexpected view id");
        }
    }

    private void onAllowButtonFromShortcutToggleClicked() {
        this.mShortcutPreference.setChecked(true);
        AccessibilityUtil.optInAllValuesToSettings(getPrefContext(), PreferredShortcuts.retrieveUserShortcutType(getPrefContext(), this.mComponentName.flattenToString(), 1), this.mComponentName);
        this.mIsDialogShown.set(false);
        showPopupDialog(1008);
        this.mDialog.dismiss();
        this.mShortcutPreference.setSummary(getShortcutTypeSummary(getPrefContext()));
    }

    private void onDenyButtonFromShortcutToggleClicked() {
        this.mShortcutPreference.setChecked(false);
        this.mDialog.dismiss();
    }

    /* access modifiers changed from: package-private */
    public void onDialogButtonFromShortcutClicked(View view) {
        int id = view.getId();
        if (id == R.id.permission_enable_allow_button) {
            onAllowButtonFromShortcutClicked();
        } else if (id == R.id.permission_enable_deny_button) {
            onDenyButtonFromShortcutClicked();
        } else {
            throw new IllegalArgumentException("Unexpected view id");
        }
    }

    private void onAllowButtonFromShortcutClicked() {
        this.mIsDialogShown.set(false);
        showPopupDialog(1);
        this.mDialog.dismiss();
    }

    private void onDenyButtonFromShortcutClicked() {
        this.mDialog.dismiss();
    }

    private boolean onPreferenceClick(Preference preference) {
        if (((SettingsMainSwitchPreference) preference).isChecked()) {
            this.mToggleServiceSwitchPreference.setChecked(true);
            getArguments().putBoolean("checked", true);
            showDialog(1005);
        } else {
            this.mToggleServiceSwitchPreference.setChecked(false);
            getArguments().putBoolean("checked", false);
            if (!this.mShortcutPreference.isChecked()) {
                showPopupDialog(1002);
            } else {
                handleConfirmServiceEnabled(true);
                if (isServiceSupportAccessibilityButton()) {
                    showPopupDialog(1008);
                }
            }
        }
        return true;
    }

    private void showPopupDialog(int i) {
        if (this.mIsDialogShown.compareAndSet(false, true)) {
            showDialog(i);
            setOnDismissListener(new C0616xc0ece416(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showPopupDialog$0(DialogInterface dialogInterface) {
        this.mIsDialogShown.compareAndSet(true, false);
    }
}
