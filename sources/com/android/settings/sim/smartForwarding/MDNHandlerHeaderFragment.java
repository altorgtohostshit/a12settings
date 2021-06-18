package com.android.settings.sim.smartForwarding;

import android.os.Bundle;
import android.widget.EditText;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import com.android.settings.R;
import com.android.settingslib.core.instrumentation.Instrumentable;

public class MDNHandlerHeaderFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener, EditTextPreference.OnBindEditTextListener, Instrumentable {
    public int getMetricsCategory() {
        return 1571;
    }

    public void onCreatePreferences(Bundle bundle, String str) {
        setPreferencesFromResource(R.xml.smart_forwarding_mdn_handler_header, str);
        EditTextPreference editTextPreference = (EditTextPreference) findPreference("slot0_phone_number");
        editTextPreference.setOnBindEditTextListener(this);
        editTextPreference.setOnPreferenceChangeListener(this);
        String phoneNumber = SmartForwardingUtils.getPhoneNumber(getContext(), 0);
        editTextPreference.setSummary((CharSequence) phoneNumber);
        editTextPreference.setText(phoneNumber);
        EditTextPreference editTextPreference2 = (EditTextPreference) findPreference("slot1_phone_number");
        editTextPreference2.setOnPreferenceChangeListener(this);
        editTextPreference2.setOnBindEditTextListener(this);
        String phoneNumber2 = SmartForwardingUtils.getPhoneNumber(getContext(), 1);
        editTextPreference2.setSummary((CharSequence) phoneNumber2);
        editTextPreference2.setText(phoneNumber2);
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        preference.setSummary((CharSequence) obj.toString());
        return true;
    }

    public void onBindEditText(EditText editText) {
        editText.setInputType(3);
        editText.setSingleLine(true);
    }
}
