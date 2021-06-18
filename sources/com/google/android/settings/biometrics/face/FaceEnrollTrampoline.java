package com.google.android.settings.biometrics.face;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.android.settings.R;

public class FaceEnrollTrampoline extends FragmentActivity {
    private Intent mExtras;
    private boolean mShouldIgnoreOnResume;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mExtras = getIntent();
        this.mShouldIgnoreOnResume = false;
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == 4) {
            Intent intent2 = new Intent(this.mExtras);
            intent2.putExtra("accessibility_diversity", false);
            intent2.putExtra("from_multi_timeout", true);
            this.mShouldIgnoreOnResume = true;
            startEnrollActivity(intent2);
        } else if (i2 != 5) {
            setResult(i2, intent);
            finish();
        } else {
            this.mShouldIgnoreOnResume = true;
            startEnrollActivity(this.mExtras);
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (this.mShouldIgnoreOnResume) {
            return;
        }
        if (Build.IS_ENG || Build.IS_USERDEBUG) {
            Intent intent = new Intent(this, FaceEnrollParticipation.class);
            intent.putExtras(this.mExtras);
            startActivityForResult(intent, 2);
            return;
        }
        startEnrollActivity(this.mExtras);
    }

    public void startEnrollActivity(Intent intent) {
        Intent intent2;
        if (getResources().getBoolean(R.bool.config_face_enroll_use_traffic_light)) {
            intent2 = new Intent("com.google.android.settings.future.biometrics.faceenroll.action.ENROLL");
        } else {
            intent2 = new Intent(this, FaceEnrollEnrolling.class);
        }
        intent2.putExtras(intent);
        startActivityForResult(intent2, 1);
    }
}
