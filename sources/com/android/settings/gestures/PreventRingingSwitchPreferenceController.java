package com.android.settings.gestures;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.widget.Switch;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.widget.MainSwitchPreference;
import com.android.settingslib.widget.OnMainSwitchChangeListener;

public class PreventRingingSwitchPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin, OnMainSwitchChangeListener {
    private final Context mContext;
    MainSwitchPreference mSwitch;

    public String getPreferenceKey() {
        return "gesture_prevent_ringing_switch";
    }

    public PreventRingingSwitchPreferenceController(Context context) {
        super(context);
        this.mContext = context;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        Preference findPreference;
        super.displayPreference(preferenceScreen);
        if (isAvailable() && (findPreference = preferenceScreen.findPreference(getPreferenceKey())) != null) {
            findPreference.setOnPreferenceClickListener(new C0948x7358cf34(this));
            MainSwitchPreference mainSwitchPreference = (MainSwitchPreference) findPreference;
            this.mSwitch = mainSwitchPreference;
            mainSwitchPreference.setTitle(this.mContext.getString(R.string.prevent_ringing_main_switch_title));
            this.mSwitch.addOnSwitchChangeListener(this);
            updateState(this.mSwitch);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$displayPreference$0(Preference preference) {
        Settings.Secure.putInt(this.mContext.getContentResolver(), "volume_hush_gesture", (Settings.Secure.getInt(this.mContext.getContentResolver(), "volume_hush_gesture", 1) != 0 ? 1 : 0) ^ 1);
        return true;
    }

    public void setChecked(boolean z) {
        MainSwitchPreference mainSwitchPreference = this.mSwitch;
        if (mainSwitchPreference != null) {
            mainSwitchPreference.updateStatus(z);
        }
    }

    public void updateState(Preference preference) {
        boolean z = true;
        if (Settings.Secure.getInt(this.mContext.getContentResolver(), "volume_hush_gesture", 1) == 0) {
            z = false;
        }
        setChecked(z);
    }

    public boolean isAvailable() {
        return this.mContext.getResources().getBoolean(17891694);
    }

    public void onSwitchChanged(Switch switchR, boolean z) {
        int i = 1;
        int i2 = Settings.Secure.getInt(this.mContext.getContentResolver(), "volume_hush_gesture", 1);
        if (i2 != 0) {
            i = i2;
        }
        ContentResolver contentResolver = this.mContext.getContentResolver();
        if (!z) {
            i = 0;
        }
        Settings.Secure.putInt(contentResolver, "volume_hush_gesture", i);
    }
}
