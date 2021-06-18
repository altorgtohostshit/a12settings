package com.android.settings.accessibility;

import android.accessibilityservice.AccessibilityShortcutInfo;
import android.app.ActivityOptions;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import androidx.preference.Preference;
import com.android.settings.R;
import java.util.ArrayList;
import java.util.List;

public class LaunchAccessibilityActivityPreferenceFragment extends ToggleFeaturePreferenceFragment {
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
    }

    /* access modifiers changed from: protected */
    public void onPreferenceToggled(String str, boolean z) {
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View onCreateView = super.onCreateView(layoutInflater, viewGroup, bundle);
        initLaunchPreference();
        removePreference("use_service");
        return onCreateView;
    }

    /* access modifiers changed from: protected */
    public void onProcessArguments(Bundle bundle) {
        super.onProcessArguments(bundle);
        this.mComponentName = (ComponentName) bundle.getParcelable("component_name");
        this.mPackageName = getAccessibilityShortcutInfo().getActivityInfo().loadLabel(getPackageManager()).toString();
        this.mImageUri = new Uri.Builder().scheme("android.resource").authority(this.mComponentName.getPackageName()).appendPath(String.valueOf(bundle.getInt("animated_image_res"))).build();
        this.mHtmlDescription = bundle.getCharSequence("html_description");
        String string = bundle.getString("settings_title");
        Intent settingsIntent = TextUtils.isEmpty(string) ? null : getSettingsIntent(bundle);
        this.mSettingsIntent = settingsIntent;
        if (settingsIntent == null) {
            string = null;
        }
        this.mSettingsTitle = string;
    }

    /* access modifiers changed from: package-private */
    public int getUserShortcutTypes() {
        return AccessibilityUtil.getUserShortcutTypesFromSettings(getPrefContext(), this.mComponentName);
    }

    private AccessibilityShortcutInfo getAccessibilityShortcutInfo() {
        List installedAccessibilityShortcutListAsUser = AccessibilityManager.getInstance(getPrefContext()).getInstalledAccessibilityShortcutListAsUser(getPrefContext(), UserHandle.myUserId());
        int size = installedAccessibilityShortcutListAsUser.size();
        for (int i = 0; i < size; i++) {
            AccessibilityShortcutInfo accessibilityShortcutInfo = (AccessibilityShortcutInfo) installedAccessibilityShortcutListAsUser.get(i);
            ActivityInfo activityInfo = accessibilityShortcutInfo.getActivityInfo();
            if (this.mComponentName.getPackageName().equals(activityInfo.packageName) && this.mComponentName.getClassName().equals(activityInfo.name)) {
                return accessibilityShortcutInfo;
            }
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public List<String> getPreferenceOrderList() {
        ArrayList arrayList = new ArrayList();
        arrayList.add("animated_image");
        arrayList.add("launch_preference");
        arrayList.add("general_categories");
        arrayList.add("html_description");
        return arrayList;
    }

    private void initLaunchPreference() {
        String str;
        Preference preference = new Preference(getPrefContext());
        preference.setKey("launch_preference");
        AccessibilityShortcutInfo accessibilityShortcutInfo = getAccessibilityShortcutInfo();
        if (accessibilityShortcutInfo == null) {
            str = "";
        } else {
            str = getString(R.string.accessibility_service_primary_open_title, accessibilityShortcutInfo.getActivityInfo().loadLabel(getPackageManager()));
        }
        preference.setTitle((CharSequence) str);
        preference.setOnPreferenceClickListener(new C0607xd3ae02e4(this));
        getPreferenceScreen().addPreference(preference);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$initLaunchPreference$0(Preference preference) {
        AccessibilityStatsLogUtils.logAccessibilityServiceEnabled(this.mComponentName, true);
        launchShortcutTargetActivity(getPrefContext().getDisplayId(), this.mComponentName);
        return true;
    }

    private void launchShortcutTargetActivity(int i, ComponentName componentName) {
        Intent intent = new Intent();
        Bundle bundle = ActivityOptions.makeBasic().setLaunchDisplayId(i).toBundle();
        intent.setComponent(componentName);
        intent.addFlags(268435456);
        try {
            getPrefContext().startActivityAsUser(intent, bundle, UserHandle.of(UserHandle.myUserId()));
        } catch (ActivityNotFoundException unused) {
            Log.w("LaunchA11yActivity", "Target activity not found.");
        }
    }

    private Intent getSettingsIntent(Bundle bundle) {
        String string = bundle.getString("settings_component_name");
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        Intent component = new Intent("android.intent.action.MAIN").setComponent(ComponentName.unflattenFromString(string));
        if (getPackageManager().queryIntentActivities(component, 0).isEmpty()) {
            return null;
        }
        return component;
    }
}
