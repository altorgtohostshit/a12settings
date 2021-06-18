package com.android.settings.development.featureflags;

import android.content.Context;
import android.util.FeatureFlagUtils;
import androidx.preference.SwitchPreference;

public class FeatureFlagPreference extends SwitchPreference {
    private final boolean mIsPersistent;
    private final String mKey;

    public FeatureFlagPreference(Context context, String str) {
        super(context);
        boolean z;
        this.mKey = str;
        setKey(str);
        setTitle((CharSequence) str);
        boolean isPersistent = FeatureFlagPersistent.isPersistent(str);
        this.mIsPersistent = isPersistent;
        if (isPersistent) {
            z = FeatureFlagPersistent.isEnabled(context, str);
        } else {
            z = FeatureFlagUtils.isEnabled(context, str);
        }
        super.setChecked(z);
    }

    public void setChecked(boolean z) {
        super.setChecked(z);
        if (this.mIsPersistent) {
            FeatureFlagPersistent.setEnabled(getContext(), this.mKey, z);
        } else {
            FeatureFlagUtils.setEnabled(getContext(), this.mKey, z);
        }
    }
}
