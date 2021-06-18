package com.google.android.settings.gestures.assist.bubble;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.settings.R;
import com.google.android.settings.gestures.assist.AssistGestureHelper;
import com.google.android.settings.gestures.assist.bubble.AssistGestureGameDrawable;

public class AssistGestureBubbleActivity extends Activity {
    /* access modifiers changed from: private */
    public AssistGestureHelper mAssistGestureHelper;
    /* access modifiers changed from: private */
    public TextView mCurrentScoreTextView;
    private AssistGestureGameDrawable mEasterEggDrawable;
    private AssistGestureGameDrawable.GameStateListener mEasterEggListener = new AssistGestureGameDrawable.GameStateListener() {
        public void updateScoreText(String str) {
            AssistGestureBubbleActivity.this.mCurrentScoreTextView.setText(str);
        }

        public void gameStateChanged(int i) {
            int unused = AssistGestureBubbleActivity.this.mGameState = i;
            if (i == 4) {
                AssistGestureBubbleActivity.this.pauseGame();
                boolean unused2 = AssistGestureBubbleActivity.this.mShouldStartNewGame = true;
            }
        }
    };
    /* access modifiers changed from: private */
    public AssistGesturePlayButtonDrawable mEasterEggPlayDrawable;
    /* access modifiers changed from: private */
    public int mGameState;
    private ImageView mGameView;
    private AssistGestureHelper.GestureListener mGestureListener = new AssistGestureHelper.GestureListener() {
        public void onGestureProgress(float f, int i) {
        }

        public void onGestureDetected() {
            AssistGestureBubbleActivity.this.mAssistGestureHelper.setListener((AssistGestureHelper.GestureListener) null);
            AssistGestureBubbleActivity.this.mAssistGestureHelper.unbindFromElmyraServiceProxy();
            AssistGestureBubbleActivity.this.mHandler.post(new AssistGestureBubbleActivity$2$$ExternalSyntheticLambda0(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onGestureDetected$0() {
            AssistGestureBubbleActivity assistGestureBubbleActivity = AssistGestureBubbleActivity.this;
            assistGestureBubbleActivity.startGame(assistGestureBubbleActivity.mShouldStartNewGame);
        }
    };
    /* access modifiers changed from: private */
    public Handler mHandler;
    /* access modifiers changed from: private */
    public boolean mIsNavigationHidden;
    /* access modifiers changed from: private */
    public ImageView mPlayView;
    /* access modifiers changed from: private */
    public boolean mShouldStartNewGame = true;

    /* access modifiers changed from: private */
    public void updateGameState() {
        if (this.mPlayView.getVisibility() != 4 || !this.mIsNavigationHidden) {
            pauseGame();
        } else {
            startGame(this.mShouldStartNewGame);
        }
    }

    /* access modifiers changed from: private */
    public void pauseGame() {
        if (this.mPlayView.getVisibility() == 4) {
            this.mPlayView.setVisibility(0);
        }
        this.mEasterEggDrawable.pauseGame();
        this.mAssistGestureHelper.bindToElmyraServiceProxy();
        this.mAssistGestureHelper.setListener(this.mGestureListener);
    }

    public void startGame(boolean z) {
        enterFullScreen();
        if (this.mPlayView.getVisibility() == 0) {
            this.mPlayView.setVisibility(4);
        }
        this.mEasterEggDrawable.startGame(z);
        this.mShouldStartNewGame = false;
    }

    private void registerDecorViewListener() {
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            public void onSystemUiVisibilityChange(int i) {
                if ((i & 4) == 0) {
                    boolean unused = AssistGestureBubbleActivity.this.mIsNavigationHidden = false;
                } else {
                    boolean unused2 = AssistGestureBubbleActivity.this.mIsNavigationHidden = true;
                }
                AssistGestureBubbleActivity.this.updateGameState();
            }
        });
    }

    private void unregisterDecorViewListener() {
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener((View.OnSystemUiVisibilityChangeListener) null);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(R.layout.assist_gesture_bubble_activity);
        getWindow().setBackgroundDrawableResource(R.drawable.assist_gesture_bubble_activity_bg);
        this.mHandler = new Handler(getMainLooper());
        this.mAssistGestureHelper = new AssistGestureHelper(getApplicationContext());
        this.mCurrentScoreTextView = (TextView) findViewById(R.id.current_score);
        this.mGameView = (ImageView) findViewById(R.id.game_view);
        AssistGestureGameDrawable assistGestureGameDrawable = new AssistGestureGameDrawable(getApplicationContext(), this.mEasterEggListener);
        this.mEasterEggDrawable = assistGestureGameDrawable;
        this.mGameView.setImageDrawable(assistGestureGameDrawable);
        this.mPlayView = (ImageView) findViewById(R.id.play_view);
        AssistGesturePlayButtonDrawable assistGesturePlayButtonDrawable = new AssistGesturePlayButtonDrawable();
        this.mEasterEggPlayDrawable = assistGesturePlayButtonDrawable;
        assistGesturePlayButtonDrawable.setAlpha(200);
        this.mPlayView.setImageDrawable(this.mEasterEggPlayDrawable);
        this.mPlayView.setOnTouchListener(new View.OnTouchListener() {
            boolean mTouching;

            public boolean onTouch(View view, MotionEvent motionEvent) {
                int actionMasked = motionEvent.getActionMasked();
                if (actionMasked != 0) {
                    if (actionMasked != 1) {
                        if (actionMasked == 3) {
                            this.mTouching = false;
                        }
                    } else if (this.mTouching) {
                        AssistGestureBubbleActivity.this.mPlayView.setVisibility(4);
                        AssistGestureBubbleActivity.this.enterFullScreen();
                        AssistGestureBubbleActivity assistGestureBubbleActivity = AssistGestureBubbleActivity.this;
                        assistGestureBubbleActivity.startGame(assistGestureBubbleActivity.mShouldStartNewGame);
                        this.mTouching = false;
                    }
                } else if (AssistGestureBubbleActivity.this.mEasterEggPlayDrawable.hitTest(motionEvent.getX(), motionEvent.getY())) {
                    this.mTouching = true;
                } else {
                    this.mTouching = false;
                }
                return true;
            }
        });
    }

    /* access modifiers changed from: private */
    public void enterFullScreen() {
        getWindow().getDecorView().setSystemUiVisibility(3846);
    }

    public void onPause() {
        super.onPause();
        this.mEasterEggDrawable.pauseGame();
        unregisterDecorViewListener();
        this.mAssistGestureHelper.setListener((AssistGestureHelper.GestureListener) null);
        this.mAssistGestureHelper.unbindFromElmyraServiceProxy();
    }

    public void onResume() {
        super.onResume();
        registerDecorViewListener();
        enterFullScreen();
    }
}
