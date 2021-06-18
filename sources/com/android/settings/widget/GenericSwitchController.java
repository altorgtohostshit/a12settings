package com.android.settings.widget;

import androidx.preference.Preference;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.widget.SwitchWidgetController;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedSwitchPreference;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;

public class GenericSwitchController extends SwitchWidgetController implements Preference.OnPreferenceChangeListener {
    private MetricsFeatureProvider mMetricsFeatureProvider;
    private Preference mPreference;

    public void setTitle(String str) {
    }

    public GenericSwitchController(PrimarySwitchPreference primarySwitchPreference) {
        setPreference(primarySwitchPreference);
    }

    public GenericSwitchController(RestrictedSwitchPreference restrictedSwitchPreference) {
        setPreference(restrictedSwitchPreference);
    }

    private void setPreference(Preference preference) {
        this.mPreference = preference;
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(preference.getContext()).getMetricsFeatureProvider();
    }

    public void startListening() {
        this.mPreference.setOnPreferenceChangeListener(this);
    }

    public void stopListening() {
        this.mPreference.setOnPreferenceChangeListener((Preference.OnPreferenceChangeListener) null);
    }

    public void setChecked(boolean z) {
        Preference preference = this.mPreference;
        if (preference instanceof PrimarySwitchPreference) {
            ((PrimarySwitchPreference) preference).setChecked(z);
        } else if (preference instanceof RestrictedSwitchPreference) {
            ((RestrictedSwitchPreference) preference).setChecked(z);
        }
    }

    public boolean isChecked() {
        Preference preference = this.mPreference;
        if (preference instanceof PrimarySwitchPreference) {
            return ((PrimarySwitchPreference) preference).isChecked();
        }
        if (preference instanceof RestrictedSwitchPreference) {
            return ((RestrictedSwitchPreference) preference).isChecked();
        }
        return false;
    }

    public void setEnabled(boolean z) {
        Preference preference = this.mPreference;
        if (preference instanceof PrimarySwitchPreference) {
            ((PrimarySwitchPreference) preference).setSwitchEnabled(z);
        } else if (preference instanceof RestrictedSwitchPreference) {
            ((RestrictedSwitchPreference) preference).setEnabled(z);
        }
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        SwitchWidgetController.OnSwitchChangeListener onSwitchChangeListener = this.mListener;
        if (onSwitchChangeListener == null) {
            return false;
        }
        boolean onSwitchToggled = onSwitchChangeListener.onSwitchToggled(((Boolean) obj).booleanValue());
        if (onSwitchToggled) {
            this.mMetricsFeatureProvider.logClickedPreference(preference, preference.getExtras().getInt("category"));
        }
        return onSwitchToggled;
    }

    public void setDisabledByAdmin(RestrictedLockUtils.EnforcedAdmin enforcedAdmin) {
        Preference preference = this.mPreference;
        if (preference instanceof PrimarySwitchPreference) {
            ((PrimarySwitchPreference) preference).setDisabledByAdmin(enforcedAdmin);
        } else if (preference instanceof RestrictedSwitchPreference) {
            ((RestrictedSwitchPreference) preference).setDisabledByAdmin(enforcedAdmin);
        }
    }
}
