package com.android.settings.development.compat;

import android.app.AlertDialog;
import android.compat.Compatibility;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.ArraySet;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.SwitchPreference;
import com.android.internal.compat.AndroidBuildClassifier;
import com.android.internal.compat.CompatibilityChangeConfig;
import com.android.internal.compat.CompatibilityChangeInfo;
import com.android.internal.compat.IPlatformCompat;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.development.AppPicker;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class PlatformCompatDashboard extends DashboardFragment {
    private AndroidBuildClassifier mAndroidBuildClassifier = new AndroidBuildClassifier();
    private CompatibilityChangeInfo[] mChanges;
    private IPlatformCompat mPlatformCompat;
    String mSelectedApp;

    public int getHelpResource() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "PlatformCompatDashboard";
    }

    public int getMetricsCategory() {
        return 1805;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.platform_compat_settings;
    }

    /* access modifiers changed from: package-private */
    public IPlatformCompat getPlatformCompat() {
        if (this.mPlatformCompat == null) {
            this.mPlatformCompat = IPlatformCompat.Stub.asInterface(ServiceManager.getService("platform_compat"));
        }
        return this.mPlatformCompat;
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        try {
            this.mChanges = getPlatformCompat().listUIChanges();
            startAppPicker();
        } catch (RemoteException e) {
            throw new RuntimeException("Could not list changes!", e);
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString("compat_app", this.mSelectedApp);
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (i != 6) {
            super.onActivityResult(i, i2, intent);
        } else if (i2 == -1) {
            this.mSelectedApp = intent.getAction();
            try {
                addPreferences(getApplicationInfo());
            } catch (PackageManager.NameNotFoundException unused) {
                startAppPicker();
            }
        } else if (i2 == -2) {
            new AlertDialog.Builder(getContext()).setTitle(R.string.platform_compat_dialog_title_no_apps).setMessage(R.string.platform_compat_dialog_text_no_apps).setPositiveButton(R.string.okay, new PlatformCompatDashboard$$ExternalSyntheticLambda0(this)).setOnDismissListener(new PlatformCompatDashboard$$ExternalSyntheticLambda1(this)).setCancelable(false).show();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onActivityResult$0(DialogInterface dialogInterface, int i) {
        finish();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onActivityResult$1(DialogInterface dialogInterface) {
        finish();
    }

    private void addPreferences(ApplicationInfo applicationInfo) {
        List list;
        getPreferenceScreen().removeAll();
        getPreferenceScreen().addPreference(createAppPreference(applicationInfo));
        CompatibilityChangeConfig appChangeMappings = getAppChangeMappings();
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        TreeMap treeMap = new TreeMap();
        for (CompatibilityChangeInfo compatibilityChangeInfo : this.mChanges) {
            if (compatibilityChangeInfo.getEnableSinceTargetSdk() > 0) {
                if (!treeMap.containsKey(Integer.valueOf(compatibilityChangeInfo.getEnableSinceTargetSdk()))) {
                    list = new ArrayList();
                    treeMap.put(Integer.valueOf(compatibilityChangeInfo.getEnableSinceTargetSdk()), list);
                } else {
                    list = (List) treeMap.get(Integer.valueOf(compatibilityChangeInfo.getEnableSinceTargetSdk()));
                }
                list.add(compatibilityChangeInfo);
            } else if (compatibilityChangeInfo.getDisabled()) {
                arrayList2.add(compatibilityChangeInfo);
            } else {
                arrayList.add(compatibilityChangeInfo);
            }
        }
        createChangeCategoryPreference(arrayList, appChangeMappings, getString(R.string.platform_compat_default_enabled_title));
        createChangeCategoryPreference(arrayList2, appChangeMappings, getString(R.string.platform_compat_default_disabled_title));
        for (Integer num : treeMap.keySet()) {
            createChangeCategoryPreference((List) treeMap.get(num), appChangeMappings, getString(R.string.platform_compat_target_sdk_title, num));
        }
    }

    private CompatibilityChangeConfig getAppChangeMappings() {
        try {
            return getPlatformCompat().getAppConfig(getApplicationInfo());
        } catch (PackageManager.NameNotFoundException | RemoteException e) {
            throw new RuntimeException("Could not get app config!", e);
        }
    }

    /* access modifiers changed from: package-private */
    public Preference createPreferenceForChange(Context context, CompatibilityChangeInfo compatibilityChangeInfo, CompatibilityChangeConfig compatibilityChangeConfig) {
        String str;
        boolean isChangeEnabled = compatibilityChangeConfig.isChangeEnabled(compatibilityChangeInfo.getId());
        SwitchPreference switchPreference = new SwitchPreference(context);
        if (compatibilityChangeInfo.getName() != null) {
            str = compatibilityChangeInfo.getName();
        } else {
            str = "Change_" + compatibilityChangeInfo.getId();
        }
        switchPreference.setSummary((CharSequence) str);
        switchPreference.setKey(str);
        try {
            switchPreference.setEnabled(getPlatformCompat().getOverrideValidator().getOverrideAllowedState(compatibilityChangeInfo.getId(), this.mSelectedApp).state == 0);
            switchPreference.setChecked(isChangeEnabled);
            switchPreference.setOnPreferenceChangeListener(new CompatChangePreferenceChangeListener(compatibilityChangeInfo.getId()));
            return switchPreference;
        } catch (RemoteException e) {
            throw new RuntimeException("Could not check if change can be overridden for app.", e);
        }
    }

    /* access modifiers changed from: package-private */
    public ApplicationInfo getApplicationInfo() throws PackageManager.NameNotFoundException {
        return getPackageManager().getApplicationInfo(this.mSelectedApp, 0);
    }

    /* access modifiers changed from: package-private */
    public Preference createAppPreference(ApplicationInfo applicationInfo) {
        Context context = getPreferenceScreen().getContext();
        Drawable loadIcon = applicationInfo.loadIcon(context.getPackageManager());
        Preference preference = new Preference(context);
        preference.setIcon(loadIcon);
        preference.setSummary((CharSequence) getString(R.string.platform_compat_selected_app_summary, this.mSelectedApp, Integer.valueOf(applicationInfo.targetSdkVersion)));
        preference.setKey(this.mSelectedApp);
        preference.setOnPreferenceClickListener(new PlatformCompatDashboard$$ExternalSyntheticLambda2(this));
        return preference;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createAppPreference$2(Preference preference) {
        startAppPicker();
        return true;
    }

    /* access modifiers changed from: package-private */
    public PreferenceCategory createChangeCategoryPreference(List<CompatibilityChangeInfo> list, CompatibilityChangeConfig compatibilityChangeConfig, String str) {
        PreferenceCategory preferenceCategory = new PreferenceCategory(getPreferenceScreen().getContext());
        preferenceCategory.setTitle((CharSequence) str);
        getPreferenceScreen().addPreference(preferenceCategory);
        addChangePreferencesToCategory(list, preferenceCategory, compatibilityChangeConfig);
        return preferenceCategory;
    }

    private void addChangePreferencesToCategory(List<CompatibilityChangeInfo> list, PreferenceCategory preferenceCategory, CompatibilityChangeConfig compatibilityChangeConfig) {
        for (CompatibilityChangeInfo createPreferenceForChange : list) {
            preferenceCategory.addPreference(createPreferenceForChange(getPreferenceScreen().getContext(), createPreferenceForChange, compatibilityChangeConfig));
        }
    }

    private void startAppPicker() {
        Intent putExtra = new Intent(getContext(), AppPicker.class).putExtra("com.android.settings.extra.INCLUDE_NOTHING", false);
        if (!this.mAndroidBuildClassifier.isDebuggableBuild()) {
            putExtra.putExtra("com.android.settings.extra.DEBUGGABLE", true);
        }
        startActivityForResult(putExtra, 6);
    }

    private class CompatChangePreferenceChangeListener implements Preference.OnPreferenceChangeListener {
        private final long changeId;

        CompatChangePreferenceChangeListener(long j) {
            this.changeId = j;
        }

        public boolean onPreferenceChange(Preference preference, Object obj) {
            try {
                ArraySet arraySet = new ArraySet();
                ArraySet arraySet2 = new ArraySet();
                if (((Boolean) obj).booleanValue()) {
                    arraySet.add(Long.valueOf(this.changeId));
                } else {
                    arraySet2.add(Long.valueOf(this.changeId));
                }
                PlatformCompatDashboard.this.getPlatformCompat().setOverrides(new CompatibilityChangeConfig(new Compatibility.ChangeConfig(arraySet, arraySet2)), PlatformCompatDashboard.this.mSelectedApp);
                return true;
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
