package com.android.settings.shortcut;

import android.content.pm.ResolveInfo;
import androidx.preference.Preference;

public final /* synthetic */ class CreateShortcutPreferenceController$$ExternalSyntheticLambda0 implements Preference.OnPreferenceClickListener {
    public final /* synthetic */ CreateShortcutPreferenceController f$0;
    public final /* synthetic */ ResolveInfo f$1;

    public /* synthetic */ CreateShortcutPreferenceController$$ExternalSyntheticLambda0(CreateShortcutPreferenceController createShortcutPreferenceController, ResolveInfo resolveInfo) {
        this.f$0 = createShortcutPreferenceController;
        this.f$1 = resolveInfo;
    }

    public final boolean onPreferenceClick(Preference preference) {
        return this.f$0.lambda$updateState$0(this.f$1, preference);
    }
}
