package com.android.settings.accessibility;

import android.os.Bundle;
import android.view.View;
import com.android.settings.R;

public class VolumeShortcutToggleAccessibilityServicePreferenceFragment extends ToggleAccessibilityServicePreferenceFragment {
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mShortcutPreference.setSummary(getPrefContext().getText(R.string.accessibility_shortcut_edit_dialog_title_hardware));
        this.mShortcutPreference.setSettingsEditable(false);
        setAllowedPreferredShortcutType(2);
    }

    /* access modifiers changed from: package-private */
    public int getUserShortcutTypes() {
        int userShortcutTypes = super.getUserShortcutTypes();
        return (!((getAccessibilityServiceInfo().flags & 256) != 0) || !getArguments().getBoolean("checked")) ? userShortcutTypes & -2 : userShortcutTypes | 1;
    }

    private void setAllowedPreferredShortcutType(int i) {
        PreferredShortcuts.saveUserShortcutType(getPrefContext(), new PreferredShortcut(this.mComponentName.flattenToString(), i));
    }
}
