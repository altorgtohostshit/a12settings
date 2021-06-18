package com.android.settings.accessibility;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityManager;
import androidx.fragment.app.FragmentActivity;
import com.android.internal.accessibility.AccessibilityShortcutController;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.R;
import com.android.settings.core.InstrumentedFragment;
import com.android.settings.core.SubSettingLauncher;
import com.android.settingslib.accessibility.AccessibilityUtils;
import java.util.List;
import java.util.Objects;

public class AccessibilityDetailsSettingsFragment extends InstrumentedFragment {
    public int getMetricsCategory() {
        return 1682;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        String stringExtra = getActivity().getIntent().getStringExtra("android.intent.extra.COMPONENT_NAME");
        if (stringExtra == null) {
            Log.w("A11yDetailsSettings", "Open accessibility services list due to no component name.");
            openAccessibilitySettingsAndFinish();
            return;
        }
        ComponentName unflattenFromString = ComponentName.unflattenFromString(stringExtra);
        if (!openSystemAccessibilitySettingsAndFinish(unflattenFromString) && !openAccessibilityDetailsSettingsAndFinish(unflattenFromString)) {
            openAccessibilitySettingsAndFinish();
        }
    }

    private boolean openSystemAccessibilitySettingsAndFinish(ComponentName componentName) {
        LaunchFragmentArguments systemAccessibilitySettingsLaunchArguments = getSystemAccessibilitySettingsLaunchArguments(componentName);
        if (systemAccessibilitySettingsLaunchArguments == null) {
            return false;
        }
        openSubSettings(systemAccessibilitySettingsLaunchArguments.mDestination, systemAccessibilitySettingsLaunchArguments.mArguments);
        finish();
        return true;
    }

    private LaunchFragmentArguments getSystemAccessibilitySettingsLaunchArguments(ComponentName componentName) {
        if (AccessibilityShortcutController.MAGNIFICATION_COMPONENT_NAME.equals(componentName)) {
            String name = ToggleScreenMagnificationPreferenceFragment.class.getName();
            Bundle bundle = new Bundle();
            MagnificationGesturesPreferenceController.populateMagnificationGesturesPreferenceExtras(bundle, getContext());
            return new LaunchFragmentArguments(name, bundle);
        } else if (AccessibilityShortcutController.ACCESSIBILITY_BUTTON_COMPONENT_NAME.equals(componentName)) {
            return new LaunchFragmentArguments(AccessibilityButtonFragment.class.getName(), (Bundle) null);
        } else {
            return null;
        }
    }

    private void openAccessibilitySettingsAndFinish() {
        openSubSettings(AccessibilitySettings.class.getName(), (Bundle) null);
        finish();
    }

    private boolean openAccessibilityDetailsSettingsAndFinish(ComponentName componentName) {
        AccessibilityServiceInfo accessibilityServiceInfo = getAccessibilityServiceInfo(componentName);
        if (accessibilityServiceInfo == null) {
            Log.w("A11yDetailsSettings", "openAccessibilityDetailsSettingsAndFinish : invalid component name.");
            return false;
        } else if (!isServiceAllowed(componentName.getPackageName())) {
            Log.w("A11yDetailsSettings", "openAccessibilityDetailsSettingsAndFinish: target accessibility service isprohibited by Device Admin.");
            return false;
        } else {
            openSubSettings(ToggleAccessibilityServicePreferenceFragment.class.getName(), buildArguments(accessibilityServiceInfo));
            finish();
            return true;
        }
    }

    private void openSubSettings(String str, Bundle bundle) {
        new SubSettingLauncher(getActivity()).setDestination(str).setSourceMetricsCategory(getMetricsCategory()).setArguments(bundle).launch();
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public boolean isServiceAllowed(String str) {
        List permittedAccessibilityServices = ((DevicePolicyManager) getContext().getSystemService(DevicePolicyManager.class)).getPermittedAccessibilityServices(UserHandle.myUserId());
        return permittedAccessibilityServices == null || permittedAccessibilityServices.contains(str);
    }

    private AccessibilityServiceInfo getAccessibilityServiceInfo(ComponentName componentName) {
        if (componentName == null) {
            return null;
        }
        List<AccessibilityServiceInfo> installedAccessibilityServiceList = AccessibilityManager.getInstance(getActivity()).getInstalledAccessibilityServiceList();
        int size = installedAccessibilityServiceList.size();
        for (int i = 0; i < size; i++) {
            AccessibilityServiceInfo accessibilityServiceInfo = installedAccessibilityServiceList.get(i);
            ResolveInfo resolveInfo = accessibilityServiceInfo.getResolveInfo();
            if (componentName.getPackageName().equals(resolveInfo.serviceInfo.packageName) && componentName.getClassName().equals(resolveInfo.serviceInfo.name)) {
                return accessibilityServiceInfo;
            }
        }
        return null;
    }

    private Bundle buildArguments(AccessibilityServiceInfo accessibilityServiceInfo) {
        ResolveInfo resolveInfo = accessibilityServiceInfo.getResolveInfo();
        String charSequence = resolveInfo.loadLabel(getActivity().getPackageManager()).toString();
        ServiceInfo serviceInfo = resolveInfo.serviceInfo;
        String str = serviceInfo.packageName;
        ComponentName componentName = new ComponentName(str, serviceInfo.name);
        boolean contains = AccessibilityUtils.getEnabledServicesFromSettings(getActivity()).contains(componentName);
        String loadDescription = accessibilityServiceInfo.loadDescription(getActivity().getPackageManager());
        if (contains && accessibilityServiceInfo.crashed) {
            loadDescription = getString(R.string.accessibility_description_state_stopped);
        }
        Bundle bundle = new Bundle();
        bundle.putString("preference_key", componentName.flattenToString());
        bundle.putBoolean("checked", contains);
        bundle.putString("title", charSequence);
        bundle.putParcelable("resolve_info", resolveInfo);
        bundle.putString("summary", loadDescription);
        String settingsActivityName = accessibilityServiceInfo.getSettingsActivityName();
        if (!TextUtils.isEmpty(settingsActivityName)) {
            bundle.putString("settings_title", getString(R.string.accessibility_menu_item_settings));
            bundle.putString("settings_component_name", new ComponentName(str, settingsActivityName).flattenToString());
        }
        bundle.putParcelable("component_name", componentName);
        bundle.putInt("animated_image_res", accessibilityServiceInfo.getAnimatedImageRes());
        bundle.putString("html_description", accessibilityServiceInfo.loadHtmlDescription(getActivity().getPackageManager()));
        return bundle;
    }

    private void finish() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    private static class LaunchFragmentArguments {
        final Bundle mArguments;
        final String mDestination;

        LaunchFragmentArguments(String str, Bundle bundle) {
            Objects.requireNonNull(str);
            this.mDestination = str;
            this.mArguments = bundle;
        }
    }
}
