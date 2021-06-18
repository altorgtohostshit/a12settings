package com.android.settingslib.widget.settingsspinner;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.Spinner;
import com.android.settingslib.widget.R$dimen;
import com.android.settingslib.widget.R$drawable;

public class SettingsSpinner extends Spinner {
    public SettingsSpinner(Context context) {
        super(context);
        setBackgroundResource(R$drawable.settings_spinner_background);
    }

    public SettingsSpinner(Context context, int i) {
        super(context, i);
        setBackgroundResource(R$drawable.settings_spinner_background);
    }

    public SettingsSpinner(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setBackgroundResource(R$drawable.settings_spinner_background);
    }

    public SettingsSpinner(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setBackgroundResource(R$drawable.settings_spinner_background);
    }

    public SettingsSpinner(Context context, AttributeSet attributeSet, int i, int i2, int i3) {
        super(context, attributeSet, i, i2, i3, (Resources.Theme) null);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        setDropDownVerticalOffset(getMeasuredHeight() - ((int) getContext().getResources().getDimension(R$dimen.spinner_padding_top_or_bottom)));
    }
}
