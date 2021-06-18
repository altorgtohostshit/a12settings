package com.android.settings.development;

import android.content.Context;
import android.os.Build;
import android.os.SystemProperties;
import android.provider.Settings;
import android.text.TextUtils;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.development.DeveloperOptionsPreferenceController;

public class BluetoothSnoopLogPreferenceController extends DeveloperOptionsPreferenceController implements Preference.OnPreferenceChangeListener, PreferenceControllerMixin {
    static final String BLUETOOTH_BTSNOOP_LOG_MODE_PROPERTY = "persist.bluetooth.btsnooplogmode";
    static final int BTSNOOP_LOG_MODE_DISABLED_INDEX = 0;
    static final int BTSNOOP_LOG_MODE_FILTERED_INDEX = 1;
    static final int BTSNOOP_LOG_MODE_FULL_INDEX = 2;
    private final String[] mListEntries;
    private final String[] mListValues;

    public String getPreferenceKey() {
        return "bt_hci_snoop_log";
    }

    public BluetoothSnoopLogPreferenceController(Context context) {
        super(context);
        this.mListValues = context.getResources().getStringArray(R.array.bt_hci_snoop_log_values);
        this.mListEntries = context.getResources().getStringArray(R.array.bt_hci_snoop_log_entries);
    }

    public int getDefaultModeIndex() {
        if (!Build.IS_DEBUGGABLE) {
            return 0;
        }
        String string = Settings.Global.getString(this.mContext.getContentResolver(), "bluetooth_btsnoop_default_mode");
        int i = 0;
        while (true) {
            String[] strArr = this.mListValues;
            if (i >= strArr.length) {
                return 0;
            }
            if (TextUtils.equals(string, strArr[i])) {
                return i;
            }
            i++;
        }
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        SystemProperties.set(BLUETOOTH_BTSNOOP_LOG_MODE_PROPERTY, obj.toString());
        updateState(this.mPreference);
        return true;
    }

    public void updateState(Preference preference) {
        ListPreference listPreference = (ListPreference) preference;
        String str = SystemProperties.get(BLUETOOTH_BTSNOOP_LOG_MODE_PROPERTY);
        int defaultModeIndex = getDefaultModeIndex();
        int i = 0;
        while (true) {
            String[] strArr = this.mListValues;
            if (i >= strArr.length) {
                break;
            } else if (TextUtils.equals(str, strArr[i])) {
                defaultModeIndex = i;
                break;
            } else {
                i++;
            }
        }
        listPreference.setValue(this.mListValues[defaultModeIndex]);
        listPreference.setSummary(this.mListEntries[defaultModeIndex]);
    }

    /* access modifiers changed from: protected */
    public void onDeveloperOptionsSwitchDisabled() {
        super.onDeveloperOptionsSwitchDisabled();
        SystemProperties.set(BLUETOOTH_BTSNOOP_LOG_MODE_PROPERTY, (String) null);
        ((ListPreference) this.mPreference).setValue(this.mListValues[getDefaultModeIndex()]);
        ((ListPreference) this.mPreference).setSummary(this.mListEntries[getDefaultModeIndex()]);
    }
}
