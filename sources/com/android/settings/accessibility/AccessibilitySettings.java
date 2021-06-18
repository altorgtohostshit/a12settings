package com.android.settings.accessibility;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.accessibilityservice.AccessibilityShortcutInfo;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.accessibility.AccessibilityManager;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import com.android.internal.accessibility.AccessibilityShortcutController;
import com.android.internal.content.PackageMonitor;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.RestrictedPreference;
import com.android.settingslib.accessibility.AccessibilityUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AccessibilitySettings extends DashboardFragment {
    private static final String[] CATEGORIES = {"screen_reader_category", "captions_category", "audio_category", "display_category", "interaction_control_category", "user_installed_services_category"};
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.accessibility_settings);
    private final Map<String, PreferenceCategory> mCategoryToPrefCategoryMap = new ArrayMap();
    /* access modifiers changed from: private */
    public final Handler mHandler = new Handler();
    private boolean mIsForeground = true;
    private boolean mNeedPreferencesUpdate = false;
    private final Map<ComponentName, PreferenceCategory> mPreBundledServiceComponentToCategoryMap = new ArrayMap();
    private final Map<Preference, PreferenceCategory> mServicePreferenceToPreferenceCategoryMap = new ArrayMap();
    final SettingsContentObserver mSettingsContentObserver;
    private final PackageMonitor mSettingsPackageMonitor = new PackageMonitor() {
        public void onPackageAdded(String str, int i) {
            sendUpdate();
        }

        public void onPackageAppeared(String str, int i) {
            sendUpdate();
        }

        public void onPackageDisappeared(String str, int i) {
            sendUpdate();
        }

        public void onPackageRemoved(String str, int i) {
            sendUpdate();
        }

        private void sendUpdate() {
            AccessibilitySettings.this.mHandler.postDelayed(AccessibilitySettings.this.mUpdateRunnable, 1000);
        }
    };
    /* access modifiers changed from: private */
    public final Runnable mUpdateRunnable = new Runnable() {
        public void run() {
            if (AccessibilitySettings.this.getActivity() != null) {
                AccessibilitySettings.this.onContentChanged();
            }
        }
    };

    public int getHelpResource() {
        return R.string.help_uri_accessibility;
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "AccessibilitySettings";
    }

    public int getMetricsCategory() {
        return 2;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.accessibility_settings;
    }

    /* access modifiers changed from: protected */
    public void updateSystemPreferences() {
    }

    public AccessibilitySettings() {
        Collection<AccessibilityShortcutController.ToggleableFrameworkFeatureInfo> values = AccessibilityShortcutController.getFrameworkShortcutFeaturesMap().values();
        ArrayList arrayList = new ArrayList(values.size());
        for (AccessibilityShortcutController.ToggleableFrameworkFeatureInfo settingKey : values) {
            arrayList.add(settingKey.getSettingKey());
        }
        arrayList.add("accessibility_button_targets");
        arrayList.add("accessibility_shortcut_target_service");
        this.mSettingsContentObserver = new SettingsContentObserver(this.mHandler, arrayList) {
            public void onChange(boolean z, Uri uri) {
                AccessibilitySettings.this.onContentChanged();
            }
        };
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        ((AccessibilityHearingAidPreferenceController) use(AccessibilityHearingAidPreferenceController.class)).setFragmentManager(getFragmentManager());
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initializeAllPreferences();
        updateAllPreferences();
        registerContentMonitors();
    }

    public void onStart() {
        if (this.mNeedPreferencesUpdate) {
            updateAllPreferences();
            this.mNeedPreferencesUpdate = false;
        }
        this.mIsForeground = true;
        super.onStart();
    }

    public void onStop() {
        this.mIsForeground = false;
        super.onStop();
    }

    public void onDestroy() {
        unregisterContentMonitors();
        super.onDestroy();
    }

    static CharSequence getServiceSummary(Context context, AccessibilityServiceInfo accessibilityServiceInfo, boolean z) {
        CharSequence charSequence;
        if (z && accessibilityServiceInfo.crashed) {
            return context.getText(R.string.accessibility_summary_state_stopped);
        }
        if (AccessibilityUtil.getAccessibilityServiceFragmentType(accessibilityServiceInfo) == 1) {
            if (AccessibilityUtil.getUserShortcutTypesFromSettings(context, new ComponentName(accessibilityServiceInfo.getResolveInfo().serviceInfo.packageName, accessibilityServiceInfo.getResolveInfo().serviceInfo.name)) != 0) {
                charSequence = context.getText(R.string.accessibility_summary_shortcut_enabled);
            } else {
                charSequence = context.getText(R.string.accessibility_summary_shortcut_disabled);
            }
        } else if (z) {
            charSequence = context.getText(R.string.accessibility_summary_state_enabled);
        } else {
            charSequence = context.getText(R.string.accessibility_summary_state_disabled);
        }
        CharSequence loadSummary = accessibilityServiceInfo.loadSummary(context.getPackageManager());
        return TextUtils.isEmpty(loadSummary) ? charSequence : context.getString(R.string.preference_summary_default_combination, new Object[]{charSequence, loadSummary});
    }

    static CharSequence getServiceDescription(Context context, AccessibilityServiceInfo accessibilityServiceInfo, boolean z) {
        if (!z || !accessibilityServiceInfo.crashed) {
            return accessibilityServiceInfo.loadDescription(context.getPackageManager());
        }
        return context.getText(R.string.accessibility_description_state_stopped);
    }

    static boolean isRampingRingerEnabled(Context context) {
        if (Settings.Global.getInt(context.getContentResolver(), "apply_ramping_ringer", 0) == 1) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public void onContentChanged() {
        if (this.mIsForeground) {
            updateAllPreferences();
        } else {
            this.mNeedPreferencesUpdate = true;
        }
    }

    private void initializeAllPreferences() {
        int i = 0;
        while (true) {
            String[] strArr = CATEGORIES;
            if (i < strArr.length) {
                this.mCategoryToPrefCategoryMap.put(strArr[i], (PreferenceCategory) findPreference(strArr[i]));
                i++;
            } else {
                return;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void updateAllPreferences() {
        updateSystemPreferences();
        updateServicePreferences();
    }

    private void registerContentMonitors() {
        FragmentActivity activity = getActivity();
        this.mSettingsPackageMonitor.register(activity, activity.getMainLooper(), false);
        this.mSettingsContentObserver.register(getContentResolver());
    }

    private void unregisterContentMonitors() {
        this.mSettingsPackageMonitor.unregister();
        this.mSettingsContentObserver.unregister(getContentResolver());
    }

    /* access modifiers changed from: protected */
    public void updateServicePreferences() {
        ArrayList arrayList = new ArrayList(this.mServicePreferenceToPreferenceCategoryMap.keySet());
        for (int i = 0; i < arrayList.size(); i++) {
            Preference preference = (Preference) arrayList.get(i);
            this.mServicePreferenceToPreferenceCategoryMap.get(preference).removePreference(preference);
        }
        initializePreBundledServicesMapFromArray("screen_reader_category", R.array.config_preinstalled_screen_reader_services);
        initializePreBundledServicesMapFromArray("captions_category", R.array.config_preinstalled_captions_services);
        initializePreBundledServicesMapFromArray("audio_category", R.array.config_preinstalled_audio_services);
        initializePreBundledServicesMapFromArray("display_category", R.array.config_preinstalled_display_services);
        initializePreBundledServicesMapFromArray("interaction_control_category", R.array.config_preinstalled_interaction_control_services);
        List<RestrictedPreference> installedAccessibilityList = getInstalledAccessibilityList(getPrefContext());
        PreferenceCategory preferenceCategory = this.mCategoryToPrefCategoryMap.get("user_installed_services_category");
        int size = installedAccessibilityList.size();
        for (int i2 = 0; i2 < size; i2++) {
            RestrictedPreference restrictedPreference = installedAccessibilityList.get(i2);
            ComponentName componentName = (ComponentName) restrictedPreference.getExtras().getParcelable("component_name");
            PreferenceCategory preferenceCategory2 = this.mPreBundledServiceComponentToCategoryMap.containsKey(componentName) ? this.mPreBundledServiceComponentToCategoryMap.get(componentName) : preferenceCategory;
            preferenceCategory2.addPreference(restrictedPreference);
            this.mServicePreferenceToPreferenceCategoryMap.put(restrictedPreference, preferenceCategory2);
        }
        updateCategoryOrderFromArray("screen_reader_category", R.array.config_order_screen_reader_services);
        updateCategoryOrderFromArray("captions_category", R.array.config_order_captions_services);
        updateCategoryOrderFromArray("audio_category", R.array.config_order_audio_services);
        updateCategoryOrderFromArray("interaction_control_category", R.array.config_order_interaction_control_services);
        updateCategoryOrderFromArray("display_category", R.array.config_order_display_services);
        if (preferenceCategory.getPreferenceCount() == 0) {
            getPreferenceScreen().removePreference(preferenceCategory);
        } else {
            getPreferenceScreen().addPreference(preferenceCategory);
        }
        updatePreferenceCategoryVisibility("screen_reader_category");
    }

    private List<RestrictedPreference> getInstalledAccessibilityList(Context context) {
        AccessibilityManager instance = AccessibilityManager.getInstance(context);
        RestrictedPreferenceHelper restrictedPreferenceHelper = new RestrictedPreferenceHelper(context);
        List installedAccessibilityShortcutListAsUser = instance.getInstalledAccessibilityShortcutListAsUser(context, UserHandle.myUserId());
        ArrayList arrayList = new ArrayList(instance.getInstalledAccessibilityServiceList());
        arrayList.removeIf(new AccessibilitySettings$$ExternalSyntheticLambda0(this, installedAccessibilityShortcutListAsUser));
        List<RestrictedPreference> createAccessibilityActivityPreferenceList = restrictedPreferenceHelper.createAccessibilityActivityPreferenceList(installedAccessibilityShortcutListAsUser);
        List<RestrictedPreference> createAccessibilityServicePreferenceList = restrictedPreferenceHelper.createAccessibilityServicePreferenceList(arrayList);
        ArrayList arrayList2 = new ArrayList();
        arrayList2.addAll(createAccessibilityActivityPreferenceList);
        arrayList2.addAll(createAccessibilityServicePreferenceList);
        return arrayList2;
    }

    /* access modifiers changed from: private */
    /* renamed from: containsTargetNameInList */
    public boolean lambda$getInstalledAccessibilityList$0(List<AccessibilityShortcutInfo> list, AccessibilityServiceInfo accessibilityServiceInfo) {
        ServiceInfo serviceInfo = accessibilityServiceInfo.getResolveInfo().serviceInfo;
        String str = serviceInfo.packageName;
        CharSequence loadLabel = serviceInfo.loadLabel(getPackageManager());
        int size = list.size();
        for (int i = 0; i < size; i++) {
            ActivityInfo activityInfo = list.get(i).getActivityInfo();
            String str2 = activityInfo.packageName;
            CharSequence loadLabel2 = activityInfo.loadLabel(getPackageManager());
            if (str.equals(str2) && loadLabel.equals(loadLabel2)) {
                return true;
            }
        }
        return false;
    }

    private void initializePreBundledServicesMapFromArray(String str, int i) {
        String[] stringArray = getResources().getStringArray(i);
        PreferenceCategory preferenceCategory = this.mCategoryToPrefCategoryMap.get(str);
        for (String unflattenFromString : stringArray) {
            this.mPreBundledServiceComponentToCategoryMap.put(ComponentName.unflattenFromString(unflattenFromString), preferenceCategory);
        }
    }

    private void updateCategoryOrderFromArray(String str, int i) {
        String[] stringArray = getResources().getStringArray(i);
        PreferenceCategory preferenceCategory = this.mCategoryToPrefCategoryMap.get(str);
        int preferenceCount = preferenceCategory.getPreferenceCount();
        int length = stringArray.length;
        for (int i2 = 0; i2 < preferenceCount; i2++) {
            int i3 = 0;
            while (true) {
                if (i3 >= length) {
                    break;
                } else if (preferenceCategory.getPreference(i2).getKey().equals(stringArray[i3])) {
                    preferenceCategory.getPreference(i2).setOrder(i3);
                    break;
                } else {
                    i3++;
                }
            }
        }
    }

    private void updatePreferenceCategoryVisibility(String str) {
        PreferenceCategory preferenceCategory = this.mCategoryToPrefCategoryMap.get(str);
        preferenceCategory.setVisible(preferenceCategory.getPreferenceCount() != 0);
    }

    static class RestrictedPreferenceHelper {
        private final Context mContext;
        private final DevicePolicyManager mDpm;
        private final PackageManager mPm;

        RestrictedPreferenceHelper(Context context) {
            this.mContext = context;
            this.mDpm = (DevicePolicyManager) context.getSystemService(DevicePolicyManager.class);
            this.mPm = context.getPackageManager();
        }

        /* access modifiers changed from: package-private */
        public List<RestrictedPreference> createAccessibilityServicePreferenceList(List<AccessibilityServiceInfo> list) {
            Set<ComponentName> enabledServicesFromSettings = AccessibilityUtils.getEnabledServicesFromSettings(this.mContext);
            List permittedAccessibilityServices = this.mDpm.getPermittedAccessibilityServices(UserHandle.myUserId());
            int size = list.size();
            ArrayList arrayList = new ArrayList(size);
            int i = 0;
            while (i < size) {
                AccessibilityServiceInfo accessibilityServiceInfo = list.get(i);
                ResolveInfo resolveInfo = accessibilityServiceInfo.getResolveInfo();
                String str = resolveInfo.serviceInfo.packageName;
                ComponentName componentName = new ComponentName(str, resolveInfo.serviceInfo.name);
                String flattenToString = componentName.flattenToString();
                CharSequence loadLabel = resolveInfo.loadLabel(this.mPm);
                boolean contains = enabledServicesFromSettings.contains(componentName);
                CharSequence serviceSummary = AccessibilitySettings.getServiceSummary(this.mContext, accessibilityServiceInfo, contains);
                String accessibilityServiceFragmentTypeName = getAccessibilityServiceFragmentTypeName(accessibilityServiceInfo);
                Drawable loadIcon = resolveInfo.loadIcon(this.mPm);
                if (resolveInfo.getIconResource() == 0) {
                    loadIcon = ContextCompat.getDrawable(this.mContext, R.drawable.ic_accessibility_generic);
                }
                boolean z = contains;
                CharSequence charSequence = serviceSummary;
                ComponentName componentName2 = componentName;
                Set<ComponentName> set = enabledServicesFromSettings;
                String str2 = str;
                RestrictedPreference createRestrictedPreference = createRestrictedPreference(flattenToString, loadLabel, charSequence, loadIcon, accessibilityServiceFragmentTypeName);
                setRestrictedPreferenceEnabled(createRestrictedPreference, str2, permittedAccessibilityServices == null || permittedAccessibilityServices.contains(str2), z);
                String key = createRestrictedPreference.getKey();
                int animatedImageRes = accessibilityServiceInfo.getAnimatedImageRes();
                CharSequence serviceDescription = AccessibilitySettings.getServiceDescription(this.mContext, accessibilityServiceInfo, z);
                String loadHtmlDescription = accessibilityServiceInfo.loadHtmlDescription(this.mPm);
                RestrictedPreference restrictedPreference = createRestrictedPreference;
                CharSequence charSequence2 = loadLabel;
                List list2 = permittedAccessibilityServices;
                RestrictedPreference restrictedPreference2 = createRestrictedPreference;
                int i2 = animatedImageRes;
                int i3 = size;
                String settingsActivityName = accessibilityServiceInfo.getSettingsActivityName();
                putBasicExtras(restrictedPreference, key, charSequence2, serviceDescription, i2, loadHtmlDescription, componentName2);
                putServiceExtras(restrictedPreference2, resolveInfo, Boolean.valueOf(z));
                putSettingsExtras(restrictedPreference2, str2, settingsActivityName);
                arrayList.add(restrictedPreference2);
                i++;
                permittedAccessibilityServices = list2;
                size = i3;
                enabledServicesFromSettings = set;
            }
            return arrayList;
        }

        /* access modifiers changed from: package-private */
        public List<RestrictedPreference> createAccessibilityActivityPreferenceList(List<AccessibilityShortcutInfo> list) {
            Set<ComponentName> enabledServicesFromSettings = AccessibilityUtils.getEnabledServicesFromSettings(this.mContext);
            List permittedAccessibilityServices = this.mDpm.getPermittedAccessibilityServices(UserHandle.myUserId());
            int size = list.size();
            ArrayList arrayList = new ArrayList(size);
            int i = 0;
            while (i < size) {
                AccessibilityShortcutInfo accessibilityShortcutInfo = list.get(i);
                ActivityInfo activityInfo = accessibilityShortcutInfo.getActivityInfo();
                ComponentName componentName = accessibilityShortcutInfo.getComponentName();
                String flattenToString = componentName.flattenToString();
                CharSequence loadLabel = activityInfo.loadLabel(this.mPm);
                RestrictedPreference createRestrictedPreference = createRestrictedPreference(flattenToString, loadLabel, accessibilityShortcutInfo.loadSummary(this.mPm), activityInfo.getIconResource() == 0 ? ContextCompat.getDrawable(this.mContext, R.drawable.ic_accessibility_generic) : activityInfo.loadIcon(this.mPm), LaunchAccessibilityActivityPreferenceFragment.class.getName());
                String packageName = componentName.getPackageName();
                setRestrictedPreferenceEnabled(createRestrictedPreference, packageName, permittedAccessibilityServices == null || permittedAccessibilityServices.contains(packageName), enabledServicesFromSettings.contains(componentName));
                String key = createRestrictedPreference.getKey();
                String loadDescription = accessibilityShortcutInfo.loadDescription(this.mPm);
                int animatedImageRes = accessibilityShortcutInfo.getAnimatedImageRes();
                String loadHtmlDescription = accessibilityShortcutInfo.loadHtmlDescription(this.mPm);
                String str = packageName;
                String str2 = loadDescription;
                Set<ComponentName> set = enabledServicesFromSettings;
                RestrictedPreference restrictedPreference = createRestrictedPreference;
                int i2 = animatedImageRes;
                List list2 = permittedAccessibilityServices;
                String settingsActivityName = accessibilityShortcutInfo.getSettingsActivityName();
                putBasicExtras(createRestrictedPreference, key, loadLabel, str2, i2, loadHtmlDescription, componentName);
                putSettingsExtras(restrictedPreference, str, settingsActivityName);
                arrayList.add(restrictedPreference);
                i++;
                enabledServicesFromSettings = set;
                permittedAccessibilityServices = list2;
            }
            return arrayList;
        }

        private String getAccessibilityServiceFragmentTypeName(AccessibilityServiceInfo accessibilityServiceInfo) {
            String name = VolumeShortcutToggleAccessibilityServicePreferenceFragment.class.getName();
            int accessibilityServiceFragmentType = AccessibilityUtil.getAccessibilityServiceFragmentType(accessibilityServiceInfo);
            if (accessibilityServiceFragmentType == 0) {
                return name;
            }
            if (accessibilityServiceFragmentType == 1) {
                return InvisibleToggleAccessibilityServicePreferenceFragment.class.getName();
            }
            if (accessibilityServiceFragmentType == 2) {
                return ToggleAccessibilityServicePreferenceFragment.class.getName();
            }
            throw new AssertionError();
        }

        private RestrictedPreference createRestrictedPreference(String str, CharSequence charSequence, CharSequence charSequence2, Drawable drawable, String str2) {
            RestrictedPreference restrictedPreference = new RestrictedPreference(this.mContext);
            restrictedPreference.setKey(str);
            restrictedPreference.setTitle(charSequence);
            restrictedPreference.setSummary(charSequence2);
            restrictedPreference.setIcon(Utils.getAdaptiveIcon(this.mContext, drawable, -1));
            restrictedPreference.setFragment(str2);
            restrictedPreference.setIconSize(1);
            restrictedPreference.setPersistent(false);
            restrictedPreference.setOrder(-1);
            return restrictedPreference;
        }

        private void setRestrictedPreferenceEnabled(RestrictedPreference restrictedPreference, String str, boolean z, boolean z2) {
            if (z || z2) {
                restrictedPreference.setEnabled(true);
                return;
            }
            RestrictedLockUtils.EnforcedAdmin checkIfAccessibilityServiceDisallowed = RestrictedLockUtilsInternal.checkIfAccessibilityServiceDisallowed(this.mContext, str, UserHandle.myUserId());
            if (checkIfAccessibilityServiceDisallowed != null) {
                restrictedPreference.setDisabledByAdmin(checkIfAccessibilityServiceDisallowed);
            } else {
                restrictedPreference.setEnabled(false);
            }
        }

        private void putBasicExtras(RestrictedPreference restrictedPreference, String str, CharSequence charSequence, CharSequence charSequence2, int i, String str2, ComponentName componentName) {
            Bundle extras = restrictedPreference.getExtras();
            extras.putString("preference_key", str);
            extras.putCharSequence("title", charSequence);
            extras.putCharSequence("summary", charSequence2);
            extras.putParcelable("component_name", componentName);
            extras.putInt("animated_image_res", i);
            extras.putString("html_description", str2);
        }

        private void putServiceExtras(RestrictedPreference restrictedPreference, ResolveInfo resolveInfo, Boolean bool) {
            Bundle extras = restrictedPreference.getExtras();
            extras.putParcelable("resolve_info", resolveInfo);
            extras.putBoolean("checked", bool.booleanValue());
        }

        private void putSettingsExtras(RestrictedPreference restrictedPreference, String str, String str2) {
            Bundle extras = restrictedPreference.getExtras();
            if (!TextUtils.isEmpty(str2)) {
                extras.putString("settings_title", this.mContext.getText(R.string.accessibility_menu_item_settings).toString());
                extras.putString("settings_component_name", new ComponentName(str, str2).flattenToString());
            }
        }
    }
}
