package com.android.settings.widget;

import android.widget.Switch;
import com.android.settings.widget.SwitchWidgetController;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.widget.OnMainSwitchChangeListener;

public class MainSwitchBarController extends SwitchWidgetController implements OnMainSwitchChangeListener {
    private final SettingsMainSwitchBar mMainSwitch;

    public MainSwitchBarController(SettingsMainSwitchBar settingsMainSwitchBar) {
        this.mMainSwitch = settingsMainSwitchBar;
    }

    public void setupView() {
        this.mMainSwitch.show();
    }

    public void teardownView() {
        this.mMainSwitch.hide();
    }

    public void setTitle(String str) {
        this.mMainSwitch.setTitle(str);
    }

    public void startListening() {
        this.mMainSwitch.addOnSwitchChangeListener(this);
    }

    public void stopListening() {
        this.mMainSwitch.removeOnSwitchChangeListener(this);
    }

    public void setChecked(boolean z) {
        this.mMainSwitch.setChecked(z);
    }

    public boolean isChecked() {
        return this.mMainSwitch.isChecked();
    }

    public void setEnabled(boolean z) {
        this.mMainSwitch.setEnabled(z);
    }

    public void onSwitchChanged(Switch switchR, boolean z) {
        SwitchWidgetController.OnSwitchChangeListener onSwitchChangeListener = this.mListener;
        if (onSwitchChangeListener != null) {
            onSwitchChangeListener.onSwitchToggled(z);
        }
    }

    public void setDisabledByAdmin(RestrictedLockUtils.EnforcedAdmin enforcedAdmin) {
        this.mMainSwitch.setDisabledByAdmin(enforcedAdmin);
    }
}
