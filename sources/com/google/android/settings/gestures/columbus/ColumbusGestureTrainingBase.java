package com.google.android.settings.gestures.columbus;

import android.content.Intent;
import android.os.Bundle;
import com.android.settings.SubSettings;
import com.android.settings.core.InstrumentedActivity;
import com.google.android.settings.gestures.columbus.ColumbusGestureHelper;

public abstract class ColumbusGestureTrainingBase extends InstrumentedActivity implements ColumbusGestureHelper.GestureListener {
    protected ColumbusGestureHelper mColumbusGestureHelper;
    private String mLaunchedFrom;

    public void onTrigger() {
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ColumbusGestureHelper columbusGestureHelper = new ColumbusGestureHelper(getApplicationContext());
        this.mColumbusGestureHelper = columbusGestureHelper;
        columbusGestureHelper.setListener(this);
        this.mLaunchedFrom = getIntent().getStringExtra("launched_from");
    }

    public void onResume() {
        super.onResume();
        if (ColumbusPreferenceController.isColumbusEnabled(getApplicationContext())) {
            this.mColumbusGestureHelper.bindToColumbusServiceProxy();
            this.mColumbusGestureHelper.setListener(this);
            return;
        }
        setResult(1);
        finishAndRemoveTask();
    }

    public void onPause() {
        super.onPause();
        this.mColumbusGestureHelper.setListener((ColumbusGestureHelper.GestureListener) null);
        this.mColumbusGestureHelper.unbindFromColumbusServiceProxy();
    }

    /* access modifiers changed from: protected */
    public void launchColumbusGestureSettings(int i) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setClass(this, SubSettings.class);
        intent.putExtra(":settings:show_fragment", ColumbusSettings.class.getName());
        intent.putExtra(":settings:source_metrics", i);
        startActivity(intent);
    }

    /* access modifiers changed from: protected */
    public boolean flowTypeSetup() {
        return "setup".contentEquals(this.mLaunchedFrom);
    }

    /* access modifiers changed from: protected */
    public boolean flowTypeDeferredSetup() {
        return "deferred_setup".contentEquals(this.mLaunchedFrom);
    }

    /* access modifiers changed from: protected */
    public boolean flowTypeSettingsSuggestion() {
        return "settings_suggestion".contentEquals(this.mLaunchedFrom);
    }

    /* access modifiers changed from: protected */
    public boolean flowTypeAccidentalTrigger() {
        return "accidental_trigger".contentEquals(this.mLaunchedFrom);
    }

    /* access modifiers changed from: protected */
    public void handleDone() {
        setResult(-1);
        this.mColumbusGestureHelper.setListener((ColumbusGestureHelper.GestureListener) null);
        finishAndRemoveTask();
    }
}
