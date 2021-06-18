package com.android.settings.notification.zen;

import android.content.Context;
import android.widget.Switch;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.notification.SettingsEnableZenModeDialog;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.widget.MainSwitchPreference;
import com.android.settingslib.widget.OnMainSwitchChangeListener;

public class ZenModeButtonPreferenceController extends AbstractZenModePreferenceController implements OnMainSwitchChangeListener {
    private final FragmentManager mFragment;
    private MainSwitchPreference mPreference;

    public String getPreferenceKey() {
        return "zen_mode_toggle";
    }

    public boolean isAvailable() {
        return true;
    }

    public ZenModeButtonPreferenceController(Context context, Lifecycle lifecycle, FragmentManager fragmentManager) {
        super(context, "zen_mode_toggle", lifecycle);
        this.mFragment = fragmentManager;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        MainSwitchPreference mainSwitchPreference = (MainSwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = mainSwitchPreference;
        mainSwitchPreference.addOnSwitchChangeListener(this);
    }

    public void onSwitchChanged(Switch switchR, boolean z) {
        if (z) {
            updateZenModeState(this.mPreference);
            return;
        }
        writeMetrics(this.mPreference, false);
        this.mBackend.setZenMode(0);
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        updatePreference(preference);
    }

    private void updatePreference(Preference preference) {
        int zenMode = getZenMode();
        if (zenMode == 1 || zenMode == 2 || zenMode == 3) {
            this.mPreference.updateStatus(true);
            this.mPreference.setTitle((int) R.string.do_not_disturb_main_switch_title_on);
            return;
        }
        this.mPreference.setTitle((int) R.string.do_not_disturb_main_switch_title_off);
        this.mPreference.updateStatus(false);
    }

    private void updateZenModeState(Preference preference) {
        writeMetrics(preference, true);
        int zenDuration = getZenDuration();
        if (zenDuration == -1) {
            new SettingsEnableZenModeDialog().show(this.mFragment, "EnableZenModeButton");
        } else if (zenDuration != 0) {
            this.mBackend.setZenModeForDuration(zenDuration);
        } else {
            this.mBackend.setZenMode(1);
        }
    }

    private void writeMetrics(Preference preference, boolean z) {
        this.mMetricsFeatureProvider.logClickedPreference(preference, preference.getExtras().getInt("category"));
        this.mMetricsFeatureProvider.action(this.mContext, 1268, z);
    }
}
