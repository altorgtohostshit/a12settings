package com.google.android.settings.gestures.columbus;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RadioButton;
import com.android.settings.R$styleable;

public class ColumbusRadioButton extends RadioButton {
    private String mSecureValue;

    public ColumbusRadioButton(Context context) {
        super(context);
        this.mSecureValue = null;
    }

    public ColumbusRadioButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R$styleable.ColumbusRadioButton, 0, 0);
        this.mSecureValue = obtainStyledAttributes.getString(0);
        obtainStyledAttributes.recycle();
    }

    public ColumbusRadioButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R$styleable.ColumbusRadioButton, i, 0);
        this.mSecureValue = obtainStyledAttributes.getString(0);
        obtainStyledAttributes.recycle();
    }

    public ColumbusRadioButton(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R$styleable.ColumbusRadioButton, i, i2);
        this.mSecureValue = obtainStyledAttributes.getString(0);
        obtainStyledAttributes.recycle();
    }

    /* access modifiers changed from: package-private */
    public String getSecureValue() {
        String str = this.mSecureValue;
        if (str != null) {
            return str;
        }
        throw new IllegalStateException("Secure value was never set");
    }

    /* access modifiers changed from: package-private */
    public void setSecureValue(String str) {
        this.mSecureValue = str;
    }
}
