package com.android.settings.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import androidx.core.content.res.TypedArrayUtils;
import com.android.settings.R;
import com.android.settings.R$styleable;
import com.android.settingslib.widget.LayoutPreference;

public class TwoStateButtonPreference extends LayoutPreference implements View.OnClickListener {
    private final Button mButtonOff;
    private final Button mButtonOn;
    private boolean mIsChecked;

    public TwoStateButtonPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, TypedArrayUtils.getAttr(context, R.attr.twoStateButtonPreferenceStyle, 16842894));
        if (attributeSet == null) {
            this.mButtonOn = null;
            this.mButtonOff = null;
            return;
        }
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.TwoStateButtonPreference);
        int resourceId = obtainStyledAttributes.getResourceId(1, R.string.summary_placeholder);
        int resourceId2 = obtainStyledAttributes.getResourceId(0, R.string.summary_placeholder);
        obtainStyledAttributes.recycle();
        Button button = (Button) findViewById(R.id.state_on_button);
        this.mButtonOn = button;
        button.setText(resourceId);
        button.setOnClickListener(this);
        Button button2 = (Button) findViewById(R.id.state_off_button);
        this.mButtonOff = button2;
        button2.setText(resourceId2);
        button2.setOnClickListener(this);
        setChecked(isChecked());
    }

    public void onClick(View view) {
        boolean z = view.getId() == R.id.state_on_button;
        setChecked(z);
        callChangeListener(Boolean.valueOf(z));
    }

    public void setChecked(boolean z) {
        this.mIsChecked = z;
        if (z) {
            this.mButtonOn.setVisibility(8);
            this.mButtonOff.setVisibility(0);
            return;
        }
        this.mButtonOn.setVisibility(0);
        this.mButtonOff.setVisibility(8);
    }

    public boolean isChecked() {
        return this.mIsChecked;
    }

    public Button getStateOnButton() {
        return this.mButtonOn;
    }

    public Button getStateOffButton() {
        return this.mButtonOff;
    }
}
