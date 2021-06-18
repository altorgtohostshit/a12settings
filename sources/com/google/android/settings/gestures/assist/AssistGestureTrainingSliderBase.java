package com.google.android.settings.gestures.assist;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.SeekBar;
import android.widget.TextView;
import com.android.settings.R;
import com.google.android.settings.gestures.assist.AssistGestureTrainingBase;

public abstract class AssistGestureTrainingSliderBase extends AssistGestureTrainingBase implements SeekBar.OnSeekBarChangeListener {
    private int mCurrentProgress;
    protected TextView mErrorView;
    private Interpolator mFastOutLinearInInterpolator;
    private AssistGestureTrainingBase.HandleProgress mHandleProgress;
    protected Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    AssistGestureTrainingSliderBase.this.clearMessage();
                    AssistGestureTrainingSliderBase.this.handleGestureDetected();
                    return;
                case 2:
                    AssistGestureTrainingSliderBase.this.showMessage(message.arg1, AssistGestureTrainingSliderBase.this.getErrorString(message.arg1));
                    return;
                case 3:
                    AssistGestureTrainingSliderBase.this.clearMessage();
                    return;
                case 4:
                    AssistGestureTrainingSliderBase.this.fadeInView((View) message.obj);
                    return;
                case 5:
                    AssistGestureTrainingSliderBase.this.fadeOutView((View) message.obj);
                    return;
                case 6:
                    ((View) message.obj).setVisibility(4);
                    return;
                case 7:
                    View view = (View) message.obj;
                    view.setAlpha(1.0f);
                    view.setVisibility(0);
                    return;
                default:
                    return;
            }
        }
    };
    private int mLastProgress;
    private Interpolator mLinearOutSlowInInterpolator;
    private SeekBar mSeekBar;
    private boolean mSeekBarTrackingTouch;

    /* access modifiers changed from: protected */
    public abstract void handleGestureDetected();

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mErrorView = (TextView) findViewById(R.id.error_message);
        this.mLinearOutSlowInInterpolator = AnimationUtils.loadInterpolator(this, 17563662);
        this.mFastOutLinearInInterpolator = AnimationUtils.loadInterpolator(this, 17563663);
        SeekBar seekBar = (SeekBar) findViewById(R.id.assist_gesture_sensitivity_seekbar);
        this.mSeekBar = seekBar;
        seekBar.setOnSeekBarChangeListener(this);
        this.mHandleProgress = new AssistGestureTrainingBase.HandleProgress(this.mHandler);
    }

    public void onResume() {
        super.onResume();
        updateSeekBar();
    }

    /* access modifiers changed from: private */
    public void fadeOutView(final View view) {
        view.animate().alpha(0.0f).setDuration(350).setListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                view.setVisibility(4);
            }
        });
    }

    /* access modifiers changed from: private */
    public void fadeInView(View view) {
        view.setAlpha(0.0f);
        view.setVisibility(0);
        view.animate().alpha(1.0f).setDuration(350).setListener((Animator.AnimatorListener) null);
    }

    private void updateSeekBar() {
        int i = (int) ((1.0f - Settings.Secure.getFloat(getContentResolver(), "assist_gesture_sensitivity", 0.5f)) * ((float) this.mSeekBar.getMax()));
        this.mLastProgress = i;
        SeekBar seekBar = this.mSeekBar;
        if (seekBar != null && i != seekBar.getProgress()) {
            this.mSeekBar.setProgress(i, false);
        }
    }

    /* access modifiers changed from: protected */
    public void showMessage(int i, String str) {
        this.mErrorView.setText(str);
        if (this.mErrorView.getVisibility() == 4) {
            this.mErrorView.setVisibility(0);
            this.mErrorView.setTranslationY((float) getResources().getDimensionPixelSize(R.dimen.assist_gesture_error_text_appear_distance));
            this.mErrorView.setAlpha(0.0f);
            this.mErrorView.animate().alpha(1.0f).translationY(0.0f).setDuration(200).setInterpolator(this.mLinearOutSlowInInterpolator).start();
        } else {
            this.mErrorView.animate().cancel();
            this.mErrorView.setAlpha(1.0f);
            this.mErrorView.setTranslationY(0.0f);
        }
        this.mHandler.removeMessages(3);
        if (i != 4) {
            Handler handler = this.mHandler;
            handler.sendMessageDelayed(handler.obtainMessage(3), 5000);
        }
    }

    /* access modifiers changed from: private */
    public void clearMessage() {
        if (this.mErrorView.getVisibility() == 0) {
            this.mErrorView.animate().alpha(0.0f).translationY((float) getResources().getDimensionPixelSize(R.dimen.assist_gesture_error_text_disappear_distance)).setDuration(100).setInterpolator(this.mFastOutLinearInInterpolator).withEndAction(new Runnable() {
                public void run() {
                    AssistGestureTrainingSliderBase.this.mErrorView.setVisibility(4);
                }
            }).start();
        }
    }

    /* access modifiers changed from: private */
    public String getErrorString(int i) {
        if (i == 1) {
            return getResources().getString(R.string.assist_gesture_training_enrolling_error_squeeze_bottom);
        }
        if (i == 2) {
            return getResources().getString(R.string.assist_gesture_training_enrolling_error_squeeze_release_quickly);
        }
        if (i == 3) {
            return getResources().getString(R.string.assist_gesture_training_enrolling_error_may_cause_falsing);
        }
        if (i != 4) {
            return null;
        }
        return getResources().getString(R.string.assist_gesture_training_enrolling_error_try_adjusting);
    }

    public void onGestureProgress(float f, int i) {
        super.onGestureProgress(f, i);
        this.mHandleProgress.onGestureProgress(f, i);
    }

    public void onGestureDetected() {
        this.mHandler.removeMessages(2);
        this.mHandler.obtainMessage(1).sendToTarget();
        this.mHandleProgress.onGestureDetected();
    }

    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        if (z) {
            this.mCurrentProgress = i;
            if (!this.mSeekBarTrackingTouch) {
                updateSensitivity(seekBar);
            }
        }
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
        this.mSeekBarTrackingTouch = true;
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        updateSensitivity(seekBar);
        this.mSeekBarTrackingTouch = false;
    }

    private void updateSensitivity(SeekBar seekBar) {
        Settings.Secure.putFloat(getContentResolver(), "assist_gesture_sensitivity", 1.0f - (((float) this.mCurrentProgress) / ((float) seekBar.getMax())));
        int i = this.mCurrentProgress;
        if (i > this.mLastProgress || ((float) i) / ((float) seekBar.getMax()) >= 0.35f) {
            this.mHandler.removeMessages(2);
            this.mHandler.obtainMessage(3).sendToTarget();
        } else {
            this.mHandler.obtainMessage(2, 3, 0).sendToTarget();
        }
        this.mLastProgress = this.mCurrentProgress;
    }

    /* access modifiers changed from: protected */
    public void setShouldCheckForNoProgress(boolean z) {
        this.mHandleProgress.setShouldCheckForNoProgress(z);
    }
}
