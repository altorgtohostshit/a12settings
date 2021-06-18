package com.android.settings.notification;

import androidx.preference.ListPreference;
import com.android.settings.sound.AudioSwitchPreferenceController;

public final /* synthetic */ class SoundSettings$$ExternalSyntheticLambda0 implements AudioSwitchPreferenceController.AudioSwitchCallback {
    public final /* synthetic */ SoundSettings f$0;

    public /* synthetic */ SoundSettings$$ExternalSyntheticLambda0(SoundSettings soundSettings) {
        this.f$0 = soundSettings;
    }

    public final void onPreferenceDataChanged(ListPreference listPreference) {
        this.f$0.lambda$onAttach$0(listPreference);
    }
}
