package com.android.settings.homepage;

import android.content.Context;
import android.util.AttributeSet;
import androidx.core.content.res.TypedArrayUtils;
import com.android.settings.R;
import com.android.settingslib.RestrictedTopLevelPreference;

public class RestrictedHomepagePreference extends RestrictedTopLevelPreference {
    public RestrictedHomepagePreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        setLayoutResource(R.layout.homepage_preference);
    }

    public RestrictedHomepagePreference(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public RestrictedHomepagePreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, TypedArrayUtils.getAttr(context, R.attr.preferenceStyle, 16842894));
    }

    public RestrictedHomepagePreference(Context context) {
        this(context, (AttributeSet) null);
    }
}
