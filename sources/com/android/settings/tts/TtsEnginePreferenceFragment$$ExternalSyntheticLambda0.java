package com.android.settings.tts;

import android.content.DialogInterface;

public final /* synthetic */ class TtsEnginePreferenceFragment$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public final /* synthetic */ TtsEnginePreferenceFragment f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ TtsEnginePreferenceFragment$$ExternalSyntheticLambda0(TtsEnginePreferenceFragment ttsEnginePreferenceFragment, String str) {
        this.f$0 = ttsEnginePreferenceFragment;
        this.f$1 = str;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$onRadioButtonConfirmed$0(this.f$1, dialogInterface, i);
    }
}
