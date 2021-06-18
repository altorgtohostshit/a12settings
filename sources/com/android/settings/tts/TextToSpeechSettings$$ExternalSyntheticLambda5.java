package com.android.settings.tts;

import android.util.Pair;
import java.text.Collator;
import java.util.Comparator;

public final /* synthetic */ class TextToSpeechSettings$$ExternalSyntheticLambda5 implements Comparator {
    public final /* synthetic */ Collator f$0;

    public /* synthetic */ TextToSpeechSettings$$ExternalSyntheticLambda5(Collator collator) {
        this.f$0 = collator;
    }

    public final int compare(Object obj, Object obj2) {
        return this.f$0.compare((String) ((Pair) obj).first, (String) ((Pair) obj2).first);
    }
}
