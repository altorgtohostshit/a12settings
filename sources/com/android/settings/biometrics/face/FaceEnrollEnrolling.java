package com.android.settings.biometrics.face;

import android.content.Intent;
import android.database.ContentObserver;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.android.settings.R;
import com.android.settings.biometrics.BiometricEnrollSidecar;
import com.android.settings.biometrics.BiometricErrorDialog;
import com.android.settings.biometrics.BiometricsEnrollEnrolling;
import com.android.settings.biometrics.face.ParticleCollection;
import com.android.settings.slices.CustomSliceRegistry;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import java.util.ArrayList;

public class FaceEnrollEnrolling extends BiometricsEnrollEnrolling {
    private ArrayList<Integer> mDisabledFeatures = new ArrayList<>();
    private TextView mErrorText;
    private Interpolator mLinearOutSlowInInterpolator;
    private ParticleCollection.Listener mListener = new ParticleCollection.Listener() {
        public void onEnrolled() {
            FaceEnrollEnrolling faceEnrollEnrolling = FaceEnrollEnrolling.this;
            faceEnrollEnrolling.launchFinish(faceEnrollEnrolling.mToken);
        }
    };
    private FaceEnrollPreviewFragment mPreviewFragment;

    public int getMetricsCategory() {
        return 1507;
    }

    /* access modifiers changed from: protected */
    public boolean shouldStartAutomatically() {
        return false;
    }

    public static class FaceErrorDialog extends BiometricErrorDialog {
        public int getMetricsCategory() {
            return 1510;
        }

        public int getOkButtonTextResId() {
            return R.string.security_settings_face_enroll_dialog_ok;
        }

        public int getTitleResId() {
            return R.string.security_settings_face_enroll_error_dialog_title;
        }

        static FaceErrorDialog newInstance(CharSequence charSequence, int i) {
            FaceErrorDialog faceErrorDialog = new FaceErrorDialog();
            Bundle bundle = new Bundle();
            bundle.putCharSequence("error_msg", charSequence);
            bundle.putInt("error_id", i);
            faceErrorDialog.setArguments(bundle);
            return faceErrorDialog;
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.face_enroll_enrolling);
        setHeaderText((int) R.string.security_settings_face_enroll_repeat_title);
        this.mErrorText = (TextView) findViewById(R.id.error_text);
        this.mLinearOutSlowInInterpolator = AnimationUtils.loadInterpolator(this, 17563662);
        FooterBarMixin footerBarMixin = (FooterBarMixin) getLayout().getMixin(FooterBarMixin.class);
        this.mFooterBarMixin = footerBarMixin;
        footerBarMixin.setSecondaryButton(new FooterButton.Builder(this).setText(R.string.security_settings_face_enroll_enrolling_skip).setListener(new FaceEnrollEnrolling$$ExternalSyntheticLambda0(this)).setButtonType(7).setTheme(2131952137).build());
        if (!getIntent().getBooleanExtra("accessibility_diversity", true)) {
            this.mDisabledFeatures.add(2);
        }
        if (!getIntent().getBooleanExtra("accessibility_vision", true)) {
            this.mDisabledFeatures.add(1);
        }
        startEnrollment();
    }

    public void startEnrollment() {
        super.startEnrollment();
        FaceEnrollPreviewFragment faceEnrollPreviewFragment = (FaceEnrollPreviewFragment) getSupportFragmentManager().findFragmentByTag("tag_preview");
        this.mPreviewFragment = faceEnrollPreviewFragment;
        if (faceEnrollPreviewFragment == null) {
            this.mPreviewFragment = new FaceEnrollPreviewFragment();
            getSupportFragmentManager().beginTransaction().add((Fragment) this.mPreviewFragment, "tag_preview").commitAllowingStateLoss();
        }
        this.mPreviewFragment.setListener(this.mListener);
    }

    /* access modifiers changed from: protected */
    public Intent getFinishIntent() {
        return new Intent(this, FaceEnrollFinish.class);
    }

    /* access modifiers changed from: protected */
    public BiometricEnrollSidecar getSidecar() {
        int[] iArr = new int[this.mDisabledFeatures.size()];
        for (int i = 0; i < this.mDisabledFeatures.size(); i++) {
            iArr[i] = this.mDisabledFeatures.get(i).intValue();
        }
        return new FaceEnrollSidecar(iArr);
    }

    public void onEnrollmentHelp(int i, CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            showError(charSequence);
        }
        this.mPreviewFragment.onEnrollmentHelp(i, charSequence);
    }

    public void onEnrollmentError(int i, CharSequence charSequence) {
        int i2 = i != 3 ? R.string.security_settings_face_enroll_error_generic_dialog_message : R.string.security_settings_face_enroll_error_timeout_dialog_message;
        this.mPreviewFragment.onEnrollmentError(i, charSequence);
        showErrorDialog(getText(i2), i);
    }

    public void onEnrollmentProgressChange(int i, int i2) {
        this.mPreviewFragment.onEnrollmentProgressChange(i, i2);
        showError("Steps: " + i + " Remaining: " + i2);
        if (i2 == 0) {
            getApplicationContext().getContentResolver().notifyChange(CustomSliceRegistry.FACE_ENROLL_SLICE_URI, (ContentObserver) null);
            launchFinish(this.mToken);
        }
    }

    private void showErrorDialog(CharSequence charSequence, int i) {
        FaceErrorDialog.newInstance(charSequence, i).show(getSupportFragmentManager(), FaceErrorDialog.class.getName());
    }

    private void showError(CharSequence charSequence) {
        this.mErrorText.setText(charSequence);
        if (this.mErrorText.getVisibility() == 4) {
            this.mErrorText.setVisibility(0);
            this.mErrorText.setTranslationY((float) getResources().getDimensionPixelSize(R.dimen.fingerprint_error_text_appear_distance));
            this.mErrorText.setAlpha(0.0f);
            this.mErrorText.animate().alpha(1.0f).translationY(0.0f).setDuration(200).setInterpolator(this.mLinearOutSlowInInterpolator).start();
            return;
        }
        this.mErrorText.animate().cancel();
        this.mErrorText.setAlpha(1.0f);
        this.mErrorText.setTranslationY(0.0f);
    }
}
