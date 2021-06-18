package com.android.settings;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import com.android.settings.accessibility.AccessibilityGestureNavigationTutorial;

public class SettingsTutorialDialogWrapperActivity extends Activity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        showDialog();
    }

    private void showDialog() {
        AccessibilityGestureNavigationTutorial.showGestureNavigationSettingsTutorialDialog(this, new SettingsTutorialDialogWrapperActivity$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showDialog$0(DialogInterface dialogInterface) {
        finish();
    }
}
