package com.android.settings.tts;

import android.speech.tts.TextToSpeech;

public final /* synthetic */ class TextToSpeechSettings$$ExternalSyntheticLambda0 implements TextToSpeech.OnInitListener {
    public final /* synthetic */ TextToSpeechSettings f$0;

    public /* synthetic */ TextToSpeechSettings$$ExternalSyntheticLambda0(TextToSpeechSettings textToSpeechSettings) {
        this.f$0 = textToSpeechSettings;
    }

    public final void onInit(int i) {
        this.f$0.onInitEngine(i);
    }
}
