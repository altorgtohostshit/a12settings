package com.google.android.settings.gestures.assist;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.ContextThemeWrapper;
import android.view.WindowManager;
import com.android.settings.R;
import com.android.settings.core.InstrumentedActivity;
import com.android.settings.gestures.AssistGestureFeatureProvider;
import com.android.settings.overlay.FeatureFactory;
import com.google.android.settings.gestures.assist.AssistGestureHelper;

public abstract class AssistGestureTrainingBase extends InstrumentedActivity implements AssistGestureHelper.GestureListener {
    protected AssistGestureHelper mAssistGestureHelper;
    private AssistGestureIndicatorView mIndicatorView;
    private String mLaunchedFrom;
    private WindowManager mWindowManager;

    protected class HandleProgress {
        private boolean mErrorSqueezeBottomShown;
        private final Handler mHandler;
        private int mLastStage;
        private boolean mShouldCheckForNoProgress = true;

        public HandleProgress(Handler handler) {
            this.mHandler = handler;
        }

        public void setShouldCheckForNoProgress(boolean z) {
            this.mShouldCheckForNoProgress = z;
        }

        private boolean checkSqueezeNoProgress(int i) {
            return this.mLastStage == 1 && i == 0;
        }

        private boolean checkSqueezeTooLong(int i) {
            return this.mLastStage == 2 && i == 0;
        }

        public void onGestureProgress(float f, int i) {
            int i2;
            if (this.mLastStage != i) {
                if (!this.mShouldCheckForNoProgress || !checkSqueezeNoProgress(i)) {
                    i2 = checkSqueezeTooLong(i) ? 2 : 0;
                } else {
                    i2 = 1;
                }
                this.mLastStage = i;
                if (i2 != 0) {
                    if (i2 == 1) {
                        if (this.mErrorSqueezeBottomShown) {
                            i2 = 4;
                        }
                        this.mErrorSqueezeBottomShown = true;
                    }
                    this.mHandler.obtainMessage(2, i2, 0).sendToTarget();
                }
            }
        }

        public void onGestureDetected() {
            this.mLastStage = 0;
        }
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
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mWindowManager = (WindowManager) getSystemService("window");
        AssistGestureHelper assistGestureHelper = new AssistGestureHelper(getApplicationContext());
        this.mAssistGestureHelper = assistGestureHelper;
        assistGestureHelper.setListener(this);
        this.mLaunchedFrom = getIntent().getStringExtra("launched_from");
        this.mIndicatorView = new AssistGestureIndicatorView(new ContextThemeWrapper(getApplicationContext(), getTheme()));
    }

    public void onResume() {
        super.onResume();
        boolean z = Settings.Secure.getInt(getContentResolver(), "assist_gesture_enabled", 1) != 0;
        AssistGestureFeatureProvider assistGestureFeatureProvider = FeatureFactory.getFactory(this).getAssistGestureFeatureProvider();
        WindowManager windowManager = this.mWindowManager;
        AssistGestureIndicatorView assistGestureIndicatorView = this.mIndicatorView;
        windowManager.addView(assistGestureIndicatorView, assistGestureIndicatorView.getLayoutParams(getWindow().getAttributes()));
        this.mAssistGestureHelper.bindToElmyraServiceProxy();
        this.mAssistGestureHelper.setListener(this);
        if (!assistGestureFeatureProvider.isSupported(this) || !z) {
            setResult(1);
            finishAndRemoveTask();
        }
    }

    public void onPause() {
        super.onPause();
        this.mAssistGestureHelper.setListener((AssistGestureHelper.GestureListener) null);
        this.mAssistGestureHelper.unbindFromElmyraServiceProxy();
        clearIndicators();
        this.mWindowManager.removeView(this.mIndicatorView);
    }

    /* access modifiers changed from: protected */
    public void onApplyThemeResource(Resources.Theme theme, int i, boolean z) {
        theme.applyStyle(R.style.SetupWizardPartnerResource, true);
        super.onApplyThemeResource(theme, i, z);
    }

    public void onGestureProgress(float f, int i) {
        this.mIndicatorView.onGestureProgress(f);
    }

    /* access modifiers changed from: protected */
    public void clearIndicators() {
        this.mIndicatorView.onGestureProgress(0.0f);
    }

    /* access modifiers changed from: protected */
    public void fadeIndicators() {
        this.mIndicatorView.onGestureDetected();
    }

    /* access modifiers changed from: protected */
    public void handleDoneAndLaunch() {
        setResult(-1);
        this.mAssistGestureHelper.setListener((AssistGestureHelper.GestureListener) null);
        this.mAssistGestureHelper.launchAssistant();
        finishAndRemoveTask();
    }

    /* access modifiers changed from: protected */
    public void launchAssistGestureSettings() {
        startActivity(new Intent("android.settings.ASSIST_GESTURE_SETTINGS"));
    }
}
