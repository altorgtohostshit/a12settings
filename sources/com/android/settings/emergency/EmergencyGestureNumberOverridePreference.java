package com.android.settings.emergency;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import com.android.settings.R;
import com.android.settingslib.CustomDialogPreferenceCompat;
import com.android.settingslib.emergencynumber.EmergencyNumberUtils;

public class EmergencyGestureNumberOverridePreference extends CustomDialogPreferenceCompat {
    EditText mEditText;
    private EmergencyNumberUtils mEmergencyNumberUtils;

    public EmergencyGestureNumberOverridePreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(context);
    }

    public EmergencyGestureNumberOverridePreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    public EmergencyGestureNumberOverridePreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public EmergencyGestureNumberOverridePreference(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.mEmergencyNumberUtils = new EmergencyNumberUtils(context);
    }

    /* access modifiers changed from: protected */
    public void onBindDialogView(View view) {
        super.onBindDialogView(view);
        this.mEditText = (EditText) view.findViewById(R.id.emergency_gesture_number_override);
        String defaultPoliceNumber = this.mEmergencyNumberUtils.getDefaultPoliceNumber();
        this.mEditText.setHint(defaultPoliceNumber);
        String policeNumber = this.mEmergencyNumberUtils.getPoliceNumber();
        if (!TextUtils.equals(policeNumber, defaultPoliceNumber)) {
            this.mEditText.setText(policeNumber);
        }
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == -1) {
            String obj = this.mEditText.getText().toString();
            if (!TextUtils.isEmpty(obj)) {
                this.mEmergencyNumberUtils.setEmergencyNumberOverride(obj);
                return;
            }
            EmergencyNumberUtils emergencyNumberUtils = this.mEmergencyNumberUtils;
            emergencyNumberUtils.setEmergencyNumberOverride(emergencyNumberUtils.getDefaultPoliceNumber());
        }
    }
}
