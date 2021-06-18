package com.android.settings.inputmethod;

import com.android.settings.inputmethod.PhysicalKeyboardFragment;
import java.text.Collator;
import java.util.Comparator;

public final /* synthetic */ class PhysicalKeyboardFragment$$ExternalSyntheticLambda4 implements Comparator {
    public final /* synthetic */ Collator f$0;

    public /* synthetic */ PhysicalKeyboardFragment$$ExternalSyntheticLambda4(Collator collator) {
        this.f$0 = collator;
    }

    public final int compare(Object obj, Object obj2) {
        return PhysicalKeyboardFragment.lambda$getHardKeyboards$4(this.f$0, (PhysicalKeyboardFragment.HardKeyboardDeviceInfo) obj, (PhysicalKeyboardFragment.HardKeyboardDeviceInfo) obj2);
    }
}
