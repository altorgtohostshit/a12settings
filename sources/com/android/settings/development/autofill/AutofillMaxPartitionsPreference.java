package com.android.settings.development.autofill;

import android.content.Context;
import android.util.AttributeSet;

public final class AutofillMaxPartitionsPreference extends AbstractGlobalSettingsPreference {
    public /* bridge */ /* synthetic */ void onAttached() {
        super.onAttached();
    }

    public /* bridge */ /* synthetic */ void onDetached() {
        super.onDetached();
    }

    public AutofillMaxPartitionsPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, "autofill_max_partitions_size", 10);
    }
}
