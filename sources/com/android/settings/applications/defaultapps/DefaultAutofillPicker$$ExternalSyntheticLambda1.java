package com.android.settings.applications.defaultapps;

import android.content.Context;
import android.content.Intent;
import androidx.preference.Preference;

public final /* synthetic */ class DefaultAutofillPicker$$ExternalSyntheticLambda1 implements Preference.OnPreferenceClickListener {
    public final /* synthetic */ DefaultAutofillPicker f$0;
    public final /* synthetic */ Context f$1;
    public final /* synthetic */ Intent f$2;

    public /* synthetic */ DefaultAutofillPicker$$ExternalSyntheticLambda1(DefaultAutofillPicker defaultAutofillPicker, Context context, Intent intent) {
        this.f$0 = defaultAutofillPicker;
        this.f$1 = context;
        this.f$2 = intent;
    }

    public final boolean onPreferenceClick(Preference preference) {
        return this.f$0.lambda$newAddServicePreferenceOrNull$1(this.f$1, this.f$2, preference);
    }
}
