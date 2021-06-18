package com.google.android.settings.gestures.columbus;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import com.android.settings.R;
import com.android.settings.SetupWizardUtils;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupdesign.GlifLayout;

public class ColumbusGestureTrainingFinishedActivity extends ColumbusGestureTrainingBase {
    private final Handler mHandler = new Handler(Looper.myLooper());
    private GlifLayout mLayout;

    public int getMetricsCategory() {
        return 1750;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        setTheme(SetupWizardUtils.getTheme(this, getIntent()));
        setContentView((int) R.layout.columbus_gesture_training_finished_activity);
        this.mLayout = (GlifLayout) findViewById(R.id.layout);
        super.onCreate(bundle);
        this.mLayout.setHeaderText((int) R.string.columbus_gesture_training_finished_title);
        this.mLayout.setDescriptionText((int) R.string.columbus_gesture_training_finished_text);
        FooterBarMixin footerBarMixin = (FooterBarMixin) this.mLayout.getMixin(FooterBarMixin.class);
        footerBarMixin.setSecondaryButton(new FooterButton.Builder(this).setText(R.string.columbus_gesture_enrollment_settings).setListener(new C1860xe4e8ae2b(this)).setButtonType(0).setTheme(2131952137).build());
        FooterButton secondaryButton = footerBarMixin.getSecondaryButton();
        if (flowTypeDeferredSetup() || flowTypeSetup()) {
            secondaryButton.setVisibility(4);
        }
        footerBarMixin.setPrimaryButton(new FooterButton.Builder(this).setText(R.string.done).setListener(new C1861xe4e8ae2c(this)).setButtonType(5).setTheme(2131952136).build());
        FooterButton primaryButton = footerBarMixin.getPrimaryButton();
        if (flowTypeDeferredSetup() || flowTypeSetup()) {
            primaryButton.setText(this, R.string.next_label);
        } else if (flowTypeSettingsSuggestion()) {
            primaryButton.setText(this, R.string.done);
        } else if (flowTypeAccidentalTrigger()) {
            primaryButton.setText(this, R.string.columbus_gesture_enrollment_complete);
        }
        Settings.Secure.putInt(getContentResolver(), "columbus_gesture_setup_complete", 1);
    }

    /* access modifiers changed from: private */
    public void onNextButtonClicked(View view) {
        handleDone();
    }

    /* access modifiers changed from: private */
    public void onSettingsButtonClicked(View view) {
        launchColumbusGestureSettings(getMetricsCategory());
    }
}
