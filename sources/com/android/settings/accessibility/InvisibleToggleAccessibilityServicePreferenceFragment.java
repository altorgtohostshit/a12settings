package com.android.settings.accessibility;

import android.content.DialogInterface;
import android.view.View;
import com.android.settings.R;
import com.android.settingslib.accessibility.AccessibilityUtils;

public class InvisibleToggleAccessibilityServicePreferenceFragment extends ToggleAccessibilityServicePreferenceFragment {
    /* access modifiers changed from: protected */
    public void onInstallSwitchPreferenceToggleSwitch() {
        super.onInstallSwitchPreferenceToggleSwitch();
        this.mToggleServiceSwitchPreference.setVisible(false);
    }

    public void onToggleClicked(ShortcutPreference shortcutPreference) {
        super.onToggleClicked(shortcutPreference);
        AccessibilityUtils.setAccessibilityServiceState(getContext(), this.mComponentName, getArguments().getBoolean("checked") && shortcutPreference.isChecked());
    }

    /* access modifiers changed from: package-private */
    public void onDialogButtonFromShortcutToggleClicked(View view) {
        super.onDialogButtonFromShortcutToggleClicked(view);
        if (view.getId() == R.id.permission_enable_allow_button) {
            AccessibilityUtils.setAccessibilityServiceState(getContext(), this.mComponentName, true);
        }
    }

    /* access modifiers changed from: protected */
    public void callOnAlertDialogCheckboxClicked(DialogInterface dialogInterface, int i) {
        super.callOnAlertDialogCheckboxClicked(dialogInterface, i);
        AccessibilityUtils.setAccessibilityServiceState(getContext(), this.mComponentName, this.mShortcutPreference.isChecked());
    }
}
