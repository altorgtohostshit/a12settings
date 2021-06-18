package com.android.settings.sim.smartForwarding;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;
import com.android.settings.R;
import com.android.settingslib.core.instrumentation.Instrumentable;

public class SmartForwardingFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener, Instrumentable {
    private boolean turnOffSwitch;

    public int getMetricsCategory() {
        return 1571;
    }

    public SmartForwardingFragment() {
    }

    public SmartForwardingFragment(boolean z) {
        this.turnOffSwitch = z;
    }

    public void onCreatePreferences(Bundle bundle, String str) {
        setPreferencesFromResource(R.xml.smart_forwarding_switch, str);
        getActivity().getActionBar().setTitle(getResources().getString(R.string.smart_forwarding_title));
        SwitchPreference switchPreference = (SwitchPreference) findPreference("smart_forwarding_switch");
        if (this.turnOffSwitch) {
            switchPreference.setChecked(false);
        }
        switchPreference.setOnPreferenceChangeListener(this);
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        boolean booleanValue = ((Boolean) obj).booleanValue();
        Log.d("SmartForwarding", "onPreferenceChange. Update value to " + booleanValue);
        if (booleanValue) {
            String phoneNumber = SmartForwardingUtils.getPhoneNumber(getContext(), 0);
            String phoneNumber2 = SmartForwardingUtils.getPhoneNumber(getContext(), 1);
            String[] strArr = {phoneNumber2, phoneNumber};
            if (TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(phoneNumber2)) {
                Log.d("SmartForwarding", "Slot 0 or Slot 1 phone number missing.");
                switchToMDNFragment();
            } else {
                ((SmartForwardingActivity) getActivity()).enableSmartForwarding(strArr);
            }
            return false;
        }
        ((SmartForwardingActivity) getActivity()).disableSmartForwarding();
        return true;
    }

    private void switchToMDNFragment() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new MDNHandlerFragment()).commit();
    }

    public void turnOnSwitchPreference() {
        ((SwitchPreference) findPreference("smart_forwarding_switch")).setChecked(true);
    }
}
