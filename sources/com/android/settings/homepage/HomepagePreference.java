package com.android.settings.homepage;

import android.content.Context;
import android.util.AttributeSet;
import androidx.preference.Preference;
import com.android.settings.R;

public class HomepagePreference extends Preference {
    public HomepagePreference(Context context) {
        super(context);
        setLayoutResource(R.layout.homepage_preference);
    }

    public HomepagePreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setLayoutResource(R.layout.homepage_preference);
    }

    public HomepagePreference(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
        setLayoutResource(R.layout.homepage_preference);
    }

    public HomepagePreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        setLayoutResource(R.layout.homepage_preference);
    }
}
