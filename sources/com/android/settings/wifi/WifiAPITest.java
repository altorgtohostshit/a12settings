package com.android.settings.wifi;

import android.content.DialogInterface;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class WifiAPITest extends SettingsPreferenceFragment implements Preference.OnPreferenceClickListener {
    private Preference mWifiDisableNetwork;
    private Preference mWifiDisconnect;
    private Preference mWifiEnableNetwork;
    /* access modifiers changed from: private */
    public WifiManager mWifiManager;
    /* access modifiers changed from: private */
    public int netid;

    public int getMetricsCategory() {
        return 89;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mWifiManager = (WifiManager) getSystemService("wifi");
    }

    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.layout.wifi_api_test);
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        Preference findPreference = preferenceScreen.findPreference("disconnect");
        this.mWifiDisconnect = findPreference;
        findPreference.setOnPreferenceClickListener(this);
        Preference findPreference2 = preferenceScreen.findPreference("disable_network");
        this.mWifiDisableNetwork = findPreference2;
        findPreference2.setOnPreferenceClickListener(this);
        Preference findPreference3 = preferenceScreen.findPreference("enable_network");
        this.mWifiEnableNetwork = findPreference3;
        findPreference3.setOnPreferenceClickListener(this);
    }

    public boolean onPreferenceTreeClick(Preference preference) {
        super.onPreferenceTreeClick(preference);
        return false;
    }

    public boolean onPreferenceClick(Preference preference) {
        if (preference == this.mWifiDisconnect) {
            this.mWifiManager.disconnect();
            return true;
        } else if (preference == this.mWifiDisableNetwork) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle((CharSequence) "Input");
            builder.setMessage((CharSequence) "Enter Network ID");
            final EditText editText = new EditText(getPrefContext());
            builder.setView((View) editText);
            builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {
                        int unused = WifiAPITest.this.netid = Integer.parseInt(editText.getText().toString());
                        WifiAPITest.this.mWifiManager.disableNetwork(WifiAPITest.this.netid);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            });
            builder.setNegativeButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            builder.show();
            return true;
        } else if (preference != this.mWifiEnableNetwork) {
            return true;
        } else {
            AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
            builder2.setTitle((CharSequence) "Input");
            builder2.setMessage((CharSequence) "Enter Network ID");
            final EditText editText2 = new EditText(getPrefContext());
            builder2.setView((View) editText2);
            builder2.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    int unused = WifiAPITest.this.netid = Integer.parseInt(editText2.getText().toString());
                    WifiAPITest.this.mWifiManager.enableNetwork(WifiAPITest.this.netid, false);
                }
            });
            builder2.setNegativeButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            builder2.show();
            return true;
        }
    }
}
