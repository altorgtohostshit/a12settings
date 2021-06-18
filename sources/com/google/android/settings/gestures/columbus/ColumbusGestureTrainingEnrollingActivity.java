package com.google.android.settings.gestures.columbus;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import androidx.constraintlayout.widget.R$styleable;
import com.android.settings.R;
import com.android.settings.SetupWizardUtils;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupdesign.GlifLayout;

public class ColumbusGestureTrainingEnrollingActivity extends ColumbusGestureTrainingBase {
    private boolean mFirstGestureDetected;
    private final Handler mHandler = new Handler(Looper.myLooper());
    private ColumbusEnrollingIllustration mIllustration;
    private GlifLayout mLayout;

    public int getMetricsCategory() {
        return 1749;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        setTheme(SetupWizardUtils.getTheme(this, getIntent()));
        setContentView((int) R.layout.columbus_gesture_training_enrolling_activity);
        super.onCreate(bundle);
        GlifLayout glifLayout = (GlifLayout) findViewById(R.id.layout);
        this.mLayout = glifLayout;
        this.mIllustration = (ColumbusEnrollingIllustration) glifLayout.findViewById(R.id.columbus_gesture_illustration);
        this.mLayout.setDescriptionText((int) R.string.columbus_gesture_training_enrolling_text);
        ((FooterBarMixin) this.mLayout.getMixin(FooterBarMixin.class)).setSecondaryButton(new FooterButton.Builder(this).setText(R.string.columbus_gesture_enrollment_do_it_later).setListener(new C1856x6af21a1f(this)).setButtonType(2).setTheme(2131952137).build());
    }

    public void onTrigger() {
        if (this.mFirstGestureDetected) {
            this.mHandler.post(new C1858x6af21a21(this));
            return;
        }
        this.mFirstGestureDetected = true;
        this.mHandler.post(new C1859x6af21a22(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onTrigger$0() {
        this.mLayout.setHeaderText((int) R.string.columbus_gesture_training_enrolling_second_gesture_title);
        this.mLayout.setDescriptionText((int) R.string.columbus_gesture_training_enrolling_second_gesture_text);
        ((FooterBarMixin) this.mLayout.getMixin(FooterBarMixin.class)).setPrimaryButton(new FooterButton.Builder(this).setText(R.string.wizard_next).setListener(new C1857x6af21a20(this)).setButtonType(5).setTheme(2131952136).build());
        this.mIllustration.setGestureCount(2);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onTrigger$1() {
        this.mLayout.setHeaderText((int) R.string.columbus_gesture_training_enrolling_first_gesture_title);
        this.mLayout.setDescriptionText((int) R.string.columbus_gesture_training_enrolling_first_gesture_text);
        this.mIllustration.setGestureCount(1);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1) {
            setResult(i2, intent);
            finishAndRemoveTask();
        }
    }

    /* access modifiers changed from: private */
    public void onNextButtonClicked(View view) {
        startActionActivity();
        finishAndRemoveTask();
    }

    /* access modifiers changed from: private */
    public void onCancelButtonClicked(View view) {
        setResult(R$styleable.Constraint_layout_goneMarginRight);
        finishAndRemoveTask();
    }

    private void startActionActivity() {
        Intent intent = new Intent(this, ColumbusGestureTrainingActionActivity.class);
        intent.putExtra("launched_from", getIntent().getStringExtra("launched_from"));
        SetupWizardUtils.copySetupExtras(getIntent(), intent);
        startActivityForResult(intent, 1);
    }
}
