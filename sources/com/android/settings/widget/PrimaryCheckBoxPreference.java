package com.android.settings.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
import com.android.settingslib.widget.TwoTargetPreference;

public class PrimaryCheckBoxPreference extends TwoTargetPreference {
    /* access modifiers changed from: private */
    public CheckBox mCheckBox;
    /* access modifiers changed from: private */
    public boolean mChecked;
    private boolean mEnableCheckBox = true;

    /* access modifiers changed from: protected */
    public int getSecondTargetResId() {
        return R.layout.preference_widget_primary_checkbox;
    }

    public PrimaryCheckBoxPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public PrimaryCheckBoxPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public PrimaryCheckBoxPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        View findViewById = preferenceViewHolder.findViewById(16908312);
        if (findViewById != null) {
            findViewById.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (PrimaryCheckBoxPreference.this.mCheckBox == null || PrimaryCheckBoxPreference.this.mCheckBox.isEnabled()) {
                        PrimaryCheckBoxPreference primaryCheckBoxPreference = PrimaryCheckBoxPreference.this;
                        primaryCheckBoxPreference.setChecked(!primaryCheckBoxPreference.mChecked);
                        PrimaryCheckBoxPreference primaryCheckBoxPreference2 = PrimaryCheckBoxPreference.this;
                        if (!primaryCheckBoxPreference2.callChangeListener(Boolean.valueOf(primaryCheckBoxPreference2.mChecked))) {
                            PrimaryCheckBoxPreference primaryCheckBoxPreference3 = PrimaryCheckBoxPreference.this;
                            primaryCheckBoxPreference3.setChecked(!primaryCheckBoxPreference3.mChecked);
                            return;
                        }
                        PrimaryCheckBoxPreference primaryCheckBoxPreference4 = PrimaryCheckBoxPreference.this;
                        boolean unused = primaryCheckBoxPreference4.persistBoolean(primaryCheckBoxPreference4.mChecked);
                    }
                }
            });
        }
        CheckBox checkBox = (CheckBox) preferenceViewHolder.findViewById(R.id.checkboxWidget);
        this.mCheckBox = checkBox;
        if (checkBox != null) {
            checkBox.setContentDescription(getTitle());
            this.mCheckBox.setChecked(this.mChecked);
            this.mCheckBox.setEnabled(this.mEnableCheckBox);
        }
    }

    public void setEnabled(boolean z) {
        super.setEnabled(z);
        setCheckBoxEnabled(z);
    }

    public void setChecked(boolean z) {
        this.mChecked = z;
        CheckBox checkBox = this.mCheckBox;
        if (checkBox != null) {
            checkBox.setChecked(z);
        }
    }

    public void setCheckBoxEnabled(boolean z) {
        this.mEnableCheckBox = z;
        CheckBox checkBox = this.mCheckBox;
        if (checkBox != null) {
            checkBox.setEnabled(z);
        }
    }
}
