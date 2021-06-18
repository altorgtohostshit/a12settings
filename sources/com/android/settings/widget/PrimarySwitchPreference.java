package com.android.settings.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Switch;
import androidx.annotation.Keep;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedPreference;

public class PrimarySwitchPreference extends RestrictedPreference {
    /* access modifiers changed from: private */
    public boolean mChecked;
    private boolean mCheckedSet;
    private boolean mEnableSwitch = true;
    /* access modifiers changed from: private */
    public Switch mSwitch;

    /* access modifiers changed from: protected */
    public int getSecondTargetResId() {
        return R.layout.restricted_preference_widget_primary_switch;
    }

    public PrimarySwitchPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public PrimarySwitchPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public PrimarySwitchPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public PrimarySwitchPreference(Context context) {
        super(context);
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        View findViewById = preferenceViewHolder.findViewById(R.id.switchWidget);
        if (findViewById != null) {
            findViewById.setVisibility(isDisabledByAdmin() ? 8 : 0);
            findViewById.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (PrimarySwitchPreference.this.mSwitch == null || PrimarySwitchPreference.this.mSwitch.isEnabled()) {
                        PrimarySwitchPreference primarySwitchPreference = PrimarySwitchPreference.this;
                        primarySwitchPreference.setChecked(!primarySwitchPreference.mChecked);
                        PrimarySwitchPreference primarySwitchPreference2 = PrimarySwitchPreference.this;
                        if (!primarySwitchPreference2.callChangeListener(Boolean.valueOf(primarySwitchPreference2.mChecked))) {
                            PrimarySwitchPreference primarySwitchPreference3 = PrimarySwitchPreference.this;
                            primarySwitchPreference3.setChecked(!primarySwitchPreference3.mChecked);
                            return;
                        }
                        PrimarySwitchPreference primarySwitchPreference4 = PrimarySwitchPreference.this;
                        boolean unused = primarySwitchPreference4.persistBoolean(primarySwitchPreference4.mChecked);
                    }
                }
            });
            findViewById.setOnTouchListener(PrimarySwitchPreference$$ExternalSyntheticLambda0.INSTANCE);
        }
        Switch switchR = (Switch) preferenceViewHolder.findViewById(R.id.switchWidget);
        this.mSwitch = switchR;
        if (switchR != null) {
            switchR.setContentDescription(getTitle());
            this.mSwitch.setChecked(this.mChecked);
            this.mSwitch.setEnabled(this.mEnableSwitch);
        }
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$onBindViewHolder$0(View view, MotionEvent motionEvent) {
        return motionEvent.getActionMasked() == 2;
    }

    public boolean isChecked() {
        return this.mSwitch != null && this.mChecked;
    }

    @Keep
    public Boolean getCheckedState() {
        if (this.mCheckedSet) {
            return Boolean.valueOf(this.mChecked);
        }
        return null;
    }

    public void setChecked(boolean z) {
        if ((this.mChecked != z) || !this.mCheckedSet) {
            this.mChecked = z;
            this.mCheckedSet = true;
            Switch switchR = this.mSwitch;
            if (switchR != null) {
                switchR.setChecked(z);
            }
        }
    }

    public void setSwitchEnabled(boolean z) {
        this.mEnableSwitch = z;
        Switch switchR = this.mSwitch;
        if (switchR != null) {
            switchR.setEnabled(z);
        }
    }

    public void setDisabledByAdmin(RestrictedLockUtils.EnforcedAdmin enforcedAdmin) {
        super.setDisabledByAdmin(enforcedAdmin);
        setSwitchEnabled(enforcedAdmin == null);
    }

    /* access modifiers changed from: protected */
    public boolean shouldHideSecondTarget() {
        return getSecondTargetResId() == 0;
    }
}
