package com.android.settings.accessibility;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.preference.Preference;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;
import com.android.settingslib.RestrictedPreference;
import com.google.android.setupdesign.GlifPreferenceLayout;

public class AccessibilitySettingsForSetupWizard extends SettingsPreferenceFragment implements Preference.OnPreferenceChangeListener {
    private Preference mDisplayMagnificationPreference;
    private RestrictedPreference mScreenReaderPreference;
    private RestrictedPreference mSelectToSpeakPreference;

    public int getMetricsCategory() {
        return 367;
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        return false;
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        GlifPreferenceLayout glifPreferenceLayout = (GlifPreferenceLayout) view;
        glifPreferenceLayout.setDividerInsets(Integer.MAX_VALUE, 0);
        glifPreferenceLayout.setDescriptionText((int) R.string.vision_settings_description);
        glifPreferenceLayout.setHeaderText((int) R.string.vision_settings_title);
        glifPreferenceLayout.setIcon(getResources().getDrawable(R.drawable.ic_accessibility_visibility));
        ((FrameLayout.LayoutParams) ((ImageView) glifPreferenceLayout.findManagedViewById(R.id.sud_layout_icon)).getLayoutParams()).gravity = 8388611;
        glifPreferenceLayout.getHeaderTextView().setGravity(8388611);
        glifPreferenceLayout.getDescriptionTextView().setGravity(8388611);
    }

    public RecyclerView onCreateRecyclerView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return ((GlifPreferenceLayout) viewGroup).onCreateRecyclerView(layoutInflater, viewGroup, bundle);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        addPreferencesFromResource(R.xml.accessibility_settings_for_setup_wizard);
        this.mDisplayMagnificationPreference = findPreference("screen_magnification_preference");
        this.mScreenReaderPreference = (RestrictedPreference) findPreference("screen_reader_preference");
        this.mSelectToSpeakPreference = (RestrictedPreference) findPreference("select_to_speak_preference");
    }

    public void onResume() {
        super.onResume();
        updateAccessibilityServicePreference(this.mScreenReaderPreference, "com.google.android.marvin.talkback", "com.google.android.marvin.talkback.TalkBackService", VolumeShortcutToggleScreenReaderPreferenceFragmentForSetupWizard.class.getName());
        updateAccessibilityServicePreference(this.mSelectToSpeakPreference, "com.google.android.marvin.talkback", "com.google.android.accessibility.selecttospeak.SelectToSpeakService", C0632xf12da7d1.class.getName());
        configureMagnificationPreferenceIfNeeded(this.mDisplayMagnificationPreference);
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        setHasOptionsMenu(false);
    }

    public boolean onPreferenceTreeClick(Preference preference) {
        Preference preference2 = this.mDisplayMagnificationPreference;
        if (preference2 == preference) {
            preference2.getExtras().putBoolean("from_suw", true);
        }
        return super.onPreferenceTreeClick(preference);
    }

    private AccessibilityServiceInfo findService(String str, String str2) {
        for (AccessibilityServiceInfo next : ((AccessibilityManager) getActivity().getSystemService(AccessibilityManager.class)).getInstalledAccessibilityServiceList()) {
            ServiceInfo serviceInfo = next.getResolveInfo().serviceInfo;
            if (str.equals(serviceInfo.packageName) && str2.equals(serviceInfo.name)) {
                return next;
            }
        }
        return null;
    }

    private void updateAccessibilityServicePreference(RestrictedPreference restrictedPreference, String str, String str2, String str3) {
        AccessibilityServiceInfo findService = findService(str, str2);
        if (findService == null) {
            getPreferenceScreen().removePreference(restrictedPreference);
            return;
        }
        ServiceInfo serviceInfo = findService.getResolveInfo().serviceInfo;
        restrictedPreference.setIcon(Utils.getAdaptiveIcon(getContext(), findService.getResolveInfo().loadIcon(getPackageManager()), -1));
        restrictedPreference.setIconSize(1);
        String charSequence = findService.getResolveInfo().loadLabel(getPackageManager()).toString();
        restrictedPreference.setTitle((CharSequence) charSequence);
        ComponentName componentName = new ComponentName(serviceInfo.packageName, serviceInfo.name);
        restrictedPreference.setKey(componentName.flattenToString());
        if (AccessibilityUtil.getAccessibilityServiceFragmentType(findService) == 0) {
            restrictedPreference.setFragment(str3);
        }
        Bundle extras = restrictedPreference.getExtras();
        extras.putParcelable("component_name", componentName);
        extras.putString("preference_key", restrictedPreference.getKey());
        extras.putString("title", charSequence);
        extras.putString("summary", findService.loadDescription(getPackageManager()));
        extras.putInt("animated_image_res", findService.getAnimatedImageRes());
        extras.putString("html_description", findService.loadHtmlDescription(getPackageManager()));
    }

    private static void configureMagnificationPreferenceIfNeeded(Preference preference) {
        Context context = preference.getContext();
        preference.setFragment(ToggleScreenMagnificationPreferenceFragmentForSetupWizard.class.getName());
        MagnificationGesturesPreferenceController.populateMagnificationGesturesPreferenceExtras(preference.getExtras(), context);
    }
}
