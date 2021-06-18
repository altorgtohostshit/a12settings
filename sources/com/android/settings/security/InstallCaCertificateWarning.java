package com.android.settings.security;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.android.settings.R;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupdesign.GlifLayout;

public class InstallCaCertificateWarning extends Activity {
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.ca_certificate_warning_dialog);
        FooterBarMixin footerBarMixin = (FooterBarMixin) ((GlifLayout) findViewById(R.id.setup_wizard_layout)).getMixin(FooterBarMixin.class);
        footerBarMixin.setSecondaryButton(new FooterButton.Builder(this).setText(R.string.certificate_warning_install_anyway).setListener(installCaCertificate()).setButtonType(0).setTheme(2131952137).build());
        footerBarMixin.setPrimaryButton(new FooterButton.Builder(this).setText(R.string.certificate_warning_dont_install).setListener(returnToInstallCertificateFromStorage()).setButtonType(5).setTheme(2131952136).build());
    }

    private View.OnClickListener installCaCertificate() {
        return new InstallCaCertificateWarning$$ExternalSyntheticLambda1(this);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$installCaCertificate$0(View view) {
        Intent intent = new Intent();
        intent.setAction("android.credentials.INSTALL");
        intent.putExtra("certificate_install_usage", "ca");
        startActivity(intent);
        finish();
    }

    private View.OnClickListener returnToInstallCertificateFromStorage() {
        return new InstallCaCertificateWarning$$ExternalSyntheticLambda0(this);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$returnToInstallCertificateFromStorage$1(View view) {
        Toast.makeText(this, R.string.cert_not_installed, 0).show();
        finish();
    }
}
