package com.google.android.settings.biometrics.face;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import androidx.fragment.app.FragmentActivity;
import com.android.settings.R;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupcompat.util.WizardManagerHelper;
import com.google.android.setupdesign.GlifLayout;
import com.google.android.setupdesign.util.ThemeResolver;

public class FaceEnrollParticipation extends FragmentActivity {
    private boolean mDebugConsent;
    private IBinder mFaceService;
    private boolean mNextLaunched;
    private FooterButton mPrimaryButton;
    private int mUserId;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        ThemeResolver.getDefault().applyTheme(this);
        super.onCreate(bundle);
        setContentView((int) R.layout.face_enroll_participation);
        FooterBarMixin footerBarMixin = (FooterBarMixin) getLayout().getMixin(FooterBarMixin.class);
        FooterButton build = new FooterButton.Builder(this).setText(R.string.face_enrolling_confirm_help_debug).setListener(new FaceEnrollParticipation$$ExternalSyntheticLambda2(this)).setButtonType(5).setTheme(2131952136).build();
        this.mPrimaryButton = build;
        build.setEnabled(false);
        footerBarMixin.setPrimaryButton(this.mPrimaryButton);
        footerBarMixin.setSecondaryButton(new FooterButton.Builder(this).setText(R.string.face_enrolling_skip_help_debug).setListener(new FaceEnrollParticipation$$ExternalSyntheticLambda1(this)).setButtonType(7).setTheme(2131952137).build());
        this.mUserId = getIntent().getIntExtra("android.intent.extra.USER_ID", UserHandle.myUserId());
        ((CheckBox) findViewById(R.id.agree_to_participate)).setOnClickListener(new FaceEnrollParticipation$$ExternalSyntheticLambda0(this));
        this.mDebugConsent = false;
        getApplicationContext();
        IBinder service = ServiceManager.getService("face");
        this.mFaceService = service;
        if (service == null) {
            Log.e("FaceEnrollParticipation", "Could not connect to face service");
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(View view) {
        this.mPrimaryButton.setEnabled(((CheckBox) view).isChecked());
    }

    /* access modifiers changed from: protected */
    public void onApplyThemeResource(Resources.Theme theme, int i, boolean z) {
        theme.applyStyle(R.style.SetupWizardPartnerResource, true);
        super.onApplyThemeResource(theme, i, z);
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        if (!isChangingConfigurations() && !WizardManagerHelper.isAnySetupWizard(getIntent()) && !this.mNextLaunched) {
            setResult(3);
            finish();
        }
    }

    private GlifLayout getLayout() {
        return (GlifLayout) findViewById(R.id.face_enroll_participation);
    }

    /* access modifiers changed from: private */
    public void onButtonPositive(View view) {
        Log.d("FaceEnrollParticipation", "Participant agreed to data collection");
        sendDebugMessageToFaceService("--enable");
        this.mDebugConsent = true;
        Settings.Secure.putIntForUser(getContentResolver(), "biometric_debug_enabled", 1, this.mUserId);
        startEnrolling();
    }

    /* access modifiers changed from: private */
    public void onButtonNegative(View view) {
        sendDebugMessageToFaceService("--disable");
        Settings.Secure.putIntForUser(getContentResolver(), "biometric_debug_enabled", 0, this.mUserId);
        startEnrolling();
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x003e A[SYNTHETIC, Splitter:B:21:0x003e] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0046 A[SYNTHETIC, Splitter:B:25:0x0046] */
    /* JADX WARNING: Removed duplicated region for block: B:35:? A[RETURN, SYNTHETIC] */
    @com.android.internal.annotations.VisibleForTesting
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void sendDebugMessageToFaceService(java.lang.String r8) {
        /*
            r7 = this;
            java.lang.String r0 = "IOException"
            java.lang.String r1 = "FaceEnrollParticipation"
            android.os.IBinder r2 = r7.mFaceService
            if (r2 == 0) goto L_0x0053
            r2 = 0
            java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch:{ IOException -> 0x0035 }
            java.lang.String r4 = "/dev/null"
            r3.<init>(r4)     // Catch:{ IOException -> 0x0035 }
            android.os.IBinder r7 = r7.mFaceService     // Catch:{ IOException -> 0x0030, all -> 0x002d }
            java.io.FileDescriptor r2 = r3.getFD()     // Catch:{ IOException -> 0x0030, all -> 0x002d }
            r4 = 2
            java.lang.String[] r4 = new java.lang.String[r4]     // Catch:{ IOException -> 0x0030, all -> 0x002d }
            r5 = 0
            java.lang.String r6 = "--hal"
            r4[r5] = r6     // Catch:{ IOException -> 0x0030, all -> 0x002d }
            r5 = 1
            r4[r5] = r8     // Catch:{ IOException -> 0x0030, all -> 0x002d }
            r7.dump(r2, r4)     // Catch:{ IOException -> 0x0030, all -> 0x002d }
            r3.close()     // Catch:{ IOException -> 0x0028 }
            goto L_0x0053
        L_0x0028:
            r7 = move-exception
        L_0x0029:
            android.util.Log.e(r1, r0, r7)     // Catch:{ RemoteException -> 0x004f }
            goto L_0x0053
        L_0x002d:
            r7 = move-exception
            r2 = r3
            goto L_0x0044
        L_0x0030:
            r7 = move-exception
            r2 = r3
            goto L_0x0036
        L_0x0033:
            r7 = move-exception
            goto L_0x0044
        L_0x0035:
            r7 = move-exception
        L_0x0036:
            r7.printStackTrace()     // Catch:{ all -> 0x0033 }
            android.util.Log.e(r1, r0, r7)     // Catch:{ all -> 0x0033 }
            if (r2 == 0) goto L_0x0053
            r2.close()     // Catch:{ IOException -> 0x0042 }
            goto L_0x0053
        L_0x0042:
            r7 = move-exception
            goto L_0x0029
        L_0x0044:
            if (r2 == 0) goto L_0x004e
            r2.close()     // Catch:{ IOException -> 0x004a }
            goto L_0x004e
        L_0x004a:
            r8 = move-exception
            android.util.Log.e(r1, r0, r8)     // Catch:{ RemoteException -> 0x004f }
        L_0x004e:
            throw r7     // Catch:{ RemoteException -> 0x004f }
        L_0x004f:
            r7 = move-exception
            r7.printStackTrace()
        L_0x0053:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.settings.biometrics.face.FaceEnrollParticipation.sendDebugMessageToFaceService(java.lang.String):void");
    }

    private void startEnrolling() {
        Intent intent;
        this.mNextLaunched = true;
        if (getResources().getBoolean(R.bool.config_face_enroll_use_traffic_light)) {
            intent = new Intent("com.google.android.settings.future.biometrics.faceenroll.action.ENROLL");
        } else {
            intent = new Intent(this, FaceEnrollEnrolling.class);
        }
        intent.putExtras(getIntent());
        intent.putExtra("debug_consent", this.mDebugConsent);
        startActivityForResult(intent, 1);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 1) {
            setResult(i2, intent);
            finish();
        }
    }
}
