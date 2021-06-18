package com.android.settings.biometrics.face;

import android.os.Bundle;
import android.view.View;
import com.android.settings.R;
import com.android.settings.biometrics.BiometricEnrollBase;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;

public class FaceEnrollFinish extends BiometricEnrollBase {
    public int getMetricsCategory() {
        return 1508;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.face_enroll_finish);
        setHeaderText((int) R.string.security_settings_face_enroll_finish_title);
        FooterBarMixin footerBarMixin = (FooterBarMixin) getLayout().getMixin(FooterBarMixin.class);
        this.mFooterBarMixin = footerBarMixin;
        footerBarMixin.setPrimaryButton(new FooterButton.Builder(this).setText(R.string.security_settings_face_enroll_done).setListener(new FaceEnrollFinish$$ExternalSyntheticLambda0(this)).setButtonType(5).setTheme(2131952136).build());
    }

    public void onNextButtonClick(View view) {
        setResult(1);
        finish();
    }
}
