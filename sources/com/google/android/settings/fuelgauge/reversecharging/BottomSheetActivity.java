package com.google.android.settings.fuelgauge.reversecharging;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import com.android.settings.R;

public class BottomSheetActivity extends FragmentActivity {
    static final String REVERSE_CHARGING_SETTINGS = "android.settings.REVERSE_CHARGING_SETTINGS";
    ReverseChargingManager mReverseChargingManager;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (this.mReverseChargingManager == null) {
            this.mReverseChargingManager = ReverseChargingManager.getInstance(this);
        }
        if (!this.mReverseChargingManager.isSupportedReverseCharging()) {
            finish();
            return;
        }
        setContentView((int) R.layout.reverse_charging_bottom_sheet);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new VideoPreferenceFragment()).commit();
        ((Button) findViewById(R.id.ok_btn)).setOnClickListener(new BottomSheetActivity$$ExternalSyntheticLambda1(this));
        ((Button) findViewById(R.id.learn_more_btn)).setOnClickListener(new BottomSheetActivity$$ExternalSyntheticLambda0(this));
        setTitle(getString(R.string.reverse_charging_title));
        ((TextView) findViewById(R.id.toolbar_title)).setText(getString(R.string.reverse_charging_title));
        ((TextView) findViewById(R.id.header_subtitle)).setText(getString(R.string.reverse_charging_instructions_title));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(View view) {
        onOkClick();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$1(View view) {
        onLearnMoreClick();
    }

    private void onLearnMoreClick() {
        Intent intent = new Intent(REVERSE_CHARGING_SETTINGS);
        intent.setPackage("com.android.settings");
        startActivity(intent);
        finish();
    }

    private void onOkClick() {
        finish();
    }
}
