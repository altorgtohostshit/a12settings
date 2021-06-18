package com.android.settings.applications.defaultapps;

import android.content.Intent;
import com.android.settings.widget.GearPreference;

public final /* synthetic */ class DefaultAppPreferenceController$$ExternalSyntheticLambda0 implements GearPreference.OnGearClickListener {
    public final /* synthetic */ DefaultAppPreferenceController f$0;
    public final /* synthetic */ Intent f$1;

    public /* synthetic */ DefaultAppPreferenceController$$ExternalSyntheticLambda0(DefaultAppPreferenceController defaultAppPreferenceController, Intent intent) {
        this.f$0 = defaultAppPreferenceController;
        this.f$1 = intent;
    }

    public final void onGearClick(GearPreference gearPreference) {
        this.f$0.lambda$mayUpdateGearIcon$0(this.f$1, gearPreference);
    }
}
