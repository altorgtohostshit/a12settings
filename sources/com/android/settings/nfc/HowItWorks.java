package com.android.settings.nfc;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.android.settings.R;

public class HowItWorks extends Activity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.nfc_payment_how_it_works);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        ((Button) findViewById(R.id.nfc_how_it_works_button)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                HowItWorks.this.finish();
            }
        });
    }

    public boolean onNavigateUp() {
        finish();
        return true;
    }
}
