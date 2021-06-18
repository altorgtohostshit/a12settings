package com.android.settings.inputmethod;

import com.android.settingslib.inputmethod.InputMethodPreference;
import java.text.Collator;
import java.util.Comparator;

public final /* synthetic */ class AvailableVirtualKeyboardFragment$$ExternalSyntheticLambda0 implements Comparator {
    public final /* synthetic */ Collator f$0;

    public /* synthetic */ AvailableVirtualKeyboardFragment$$ExternalSyntheticLambda0(Collator collator) {
        this.f$0 = collator;
    }

    public final int compare(Object obj, Object obj2) {
        return ((InputMethodPreference) obj).compareTo((InputMethodPreference) obj2, this.f$0);
    }
}
