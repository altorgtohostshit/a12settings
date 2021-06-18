package com.android.settings.notification;

import android.content.Context;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;
import com.android.settingslib.core.lifecycle.Lifecycle;

public class DialPadTonePreferenceController extends SettingPrefController {
    public DialPadTonePreferenceController(Context context, SettingsPreferenceFragment settingsPreferenceFragment, Lifecycle lifecycle) {
        super(context, settingsPreferenceFragment, lifecycle);
        this.mPreference = new SettingPref(2, "dial_pad_tones", "dtmf_tone", 1, new int[0]) {
            public boolean isApplicable(Context context) {
                return Utils.isVoiceCapable(context);
            }
        };
    }
}
