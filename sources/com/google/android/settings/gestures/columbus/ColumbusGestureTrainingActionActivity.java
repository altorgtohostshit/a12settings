package com.google.android.settings.gestures.columbus;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.constraintlayout.widget.R$styleable;
import com.android.settings.R;
import com.android.settings.SetupWizardUtils;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupdesign.GlifLayout;

public class ColumbusGestureTrainingActionActivity extends ColumbusGestureTrainingBase {
    private RadioGroup mRadioGroup;

    public int getMetricsCategory() {
        return 1758;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        setTheme(SetupWizardUtils.getTheme(this, getIntent()));
        setContentView((int) R.layout.columbus_gesture_training_action_activity);
        super.onCreate(bundle);
        this.mRadioGroup = (RadioGroup) findViewById(R.id.actions);
        GlifLayout glifLayout = (GlifLayout) findViewById(R.id.layout);
        glifLayout.setDescriptionText((int) R.string.columbus_gesture_training_action_text);
        FooterBarMixin footerBarMixin = (FooterBarMixin) glifLayout.getMixin(FooterBarMixin.class);
        footerBarMixin.setPrimaryButton(new FooterButton.Builder(this).setText(R.string.wizard_next).setListener(new ColumbusGestureTrainingActionActivity$$ExternalSyntheticLambda0(this)).setButtonType(5).setTheme(2131952136).build());
        footerBarMixin.setSecondaryButton(new FooterButton.Builder(this).setText(R.string.columbus_gesture_enrollment_do_it_later).setListener(new ColumbusGestureTrainingActionActivity$$ExternalSyntheticLambda1(this)).setButtonType(2).setTheme(2131952137).build());
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1 || i == 2) {
            setResult(i2, intent);
            finishAndRemoveTask();
        }
    }

    /* access modifiers changed from: private */
    public void onNextButtonClicked(View view) {
        int checkedRadioButtonId = this.mRadioGroup.getCheckedRadioButtonId();
        if (checkedRadioButtonId == 0) {
            Toast.makeText(this, R.string.columbus_gesture_training_action_no_selection_error, 0).show();
        } else if (checkedRadioButtonId == R.id.launch) {
            Settings.Secure.putString(getContentResolver(), "columbus_action", getString(R.string.columbus_setting_action_launch_value));
            startLaunchActivity();
        } else {
            ColumbusRadioButton columbusRadioButton = (ColumbusRadioButton) findViewById(checkedRadioButtonId);
            Toast.makeText(this, "" + columbusRadioButton.getSecureValue(), 0).show();
            Settings.Secure.putString(getContentResolver(), "columbus_action", columbusRadioButton.getSecureValue());
            startFinishedActivity();
        }
    }

    /* access modifiers changed from: private */
    public void onCancelButtonClicked(View view) {
        setResult(R$styleable.Constraint_layout_goneMarginRight);
        finishAndRemoveTask();
    }

    private void startLaunchActivity() {
        Intent intent = new Intent(this, ColumbusGestureTrainingLaunchActivity.class);
        intent.putExtra("launched_from", getIntent().getStringExtra("launched_from"));
        SetupWizardUtils.copySetupExtras(getIntent(), intent);
        startActivityForResult(intent, 1);
    }

    private void startFinishedActivity() {
        Intent intent = new Intent(this, ColumbusGestureTrainingFinishedActivity.class);
        intent.putExtra("launched_from", getIntent().getStringExtra("launched_from"));
        SetupWizardUtils.copySetupExtras(getIntent(), intent);
        startActivityForResult(intent, 2);
    }
}
