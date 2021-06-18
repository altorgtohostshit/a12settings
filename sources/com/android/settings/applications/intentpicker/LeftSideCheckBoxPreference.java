package com.android.settings.applications.intentpicker;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
import com.android.settingslib.widget.TwoTargetPreference;

public class LeftSideCheckBoxPreference extends TwoTargetPreference {
    private CheckBox mCheckBox;
    private boolean mChecked;

    public LeftSideCheckBoxPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        setLayoutResource(R.layout.preference_checkable_two_target);
    }

    public LeftSideCheckBoxPreference(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public LeftSideCheckBoxPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public LeftSideCheckBoxPreference(Context context, boolean z) {
        super(context);
        this.mChecked = z;
        setLayoutResource(R.layout.preference_checkable_two_target);
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        CheckBox checkBox = (CheckBox) preferenceViewHolder.findViewById(16908289);
        this.mCheckBox = checkBox;
        if (checkBox != null) {
            checkBox.setChecked(this.mChecked);
        }
    }

    /* access modifiers changed from: protected */
    public void onClick() {
        CheckBox checkBox = this.mCheckBox;
        if (checkBox != null) {
            boolean z = !this.mChecked;
            this.mChecked = z;
            checkBox.setChecked(z);
            callChangeListener(Boolean.valueOf(this.mChecked));
        }
    }
}
