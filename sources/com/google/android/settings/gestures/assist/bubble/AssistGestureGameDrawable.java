package com.google.android.settings.gestures.assist.bubble;

import android.animation.TimeAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.text.format.DateFormat;
import com.google.android.settings.gestures.assist.AssistGestureHelper;
import java.util.ArrayList;
import java.util.List;

public class AssistGestureGameDrawable extends Drawable {
    private AssistGestureHelper mAssistGestureHelper;
    /* access modifiers changed from: private */
    public Rect mBounds;
    private boolean mBubbleShouldShrink = true;
    /* access modifiers changed from: private */
    public boolean mBubbleTouchedBottom;
    /* access modifiers changed from: private */
    public List<Bubble> mBubbles;
    private Context mContext;
    /* access modifiers changed from: private */
    public List<Bubble> mDeadBubbles;
    /* access modifiers changed from: private */
    public TimeAnimator mDriftAnimation;
    private VibrationEffect mErrorVibrationEffect;
    /* access modifiers changed from: private */
    public int mGameState;
    private GameStateListener mGameStateListener;
    private AssistGestureHelper.GestureListener mGestureListener = new AssistGestureHelper.GestureListener() {
        public void onGestureProgress(float f, int i) {
            AssistGestureGameDrawable.this.onGestureProgress(f, i);
        }

        public void onGestureDetected() {
            AssistGestureGameDrawable.this.onGestureDetected();
        }
    };
    private int mKilledBubbles;
    private long mLastGestureTime;
    private float mLastProgress;
    private Bubble mLastShrunkBubble;
    private int mLastStage;
    /* access modifiers changed from: private */
    public float mLastTime;
    /* access modifiers changed from: private */
    public float mNextBubbleTime;
    private Paint mPaint;
    /* access modifiers changed from: private */
    public boolean mServiceConnected;
    /* access modifiers changed from: private */
    public List<SpiralingAndroid> mSpiralingAndroids;
    private int mTopKilledBubbles;
    private long mTopKilledBubblesDate;
    private Vibrator mVibrator;

    public interface GameStateListener {
        void gameStateChanged(int i);

        void updateScoreText(String str);
    }

    public int getOpacity() {
        return -3;
    }

    public AssistGestureGameDrawable(Context context, GameStateListener gameStateListener) {
        this.mContext = context;
        this.mAssistGestureHelper = new AssistGestureHelper(context);
        this.mGameStateListener = gameStateListener;
        this.mVibrator = (Vibrator) context.getSystemService(Vibrator.class);
        this.mErrorVibrationEffect = VibrationEffect.get(1);
        Paint paint = new Paint();
        this.mPaint = paint;
        paint.setAntiAlias(true);
        this.mBubbles = new ArrayList();
        this.mDeadBubbles = new ArrayList();
        this.mSpiralingAndroids = new ArrayList();
        this.mTopKilledBubbles = Settings.Secure.getIntForUser(context.getContentResolver(), "assist_gesture_egg_top_score", 0, -2);
        this.mTopKilledBubblesDate = Settings.Secure.getLongForUser(context.getContentResolver(), "assist_gesture_egg_top_score_time", 0, -2);
        updateScoreText();
    }

    /* access modifiers changed from: private */
    public void notifyGameStateChanged() {
        GameStateListener gameStateListener = this.mGameStateListener;
        if (gameStateListener != null) {
            gameStateListener.gameStateChanged(this.mGameState);
        }
    }

    private void updateScoreText() {
        String charSequence = DateFormat.format("MM/dd/yyyy HH:mm:ss", this.mTopKilledBubblesDate).toString();
        GameStateListener gameStateListener = this.mGameStateListener;
        gameStateListener.updateScoreText("" + this.mKilledBubbles + "/" + this.mTopKilledBubbles + " " + charSequence);
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0057, code lost:
        r0 = r4.mLastShrunkBubble;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0059, code lost:
        if (r0 == null) goto L_0x007f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x005d, code lost:
        if (r4.mBubbleShouldShrink == false) goto L_0x007f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0063, code lost:
        if (r0.getState() != 0) goto L_0x007f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0065, code lost:
        r0 = r4.mLastShrunkBubble;
        r0.setSize(java.lang.Math.max((int) (((float) r0.getOriginalSize()) - (((float) r4.mLastShrunkBubble.getOriginalSize()) * r5)), 16));
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onGestureProgress(float r5, int r6) {
        /*
            r4 = this;
            int r0 = r4.mGameState
            r1 = 3
            if (r0 == r1) goto L_0x0006
            return
        L_0x0006:
            if (r6 != 0) goto L_0x0014
            int r0 = r4.mLastStage
            r1 = 2
            if (r0 != r1) goto L_0x0014
            android.os.Vibrator r0 = r4.mVibrator
            android.os.VibrationEffect r1 = r4.mErrorVibrationEffect
            r0.vibrate(r1)
        L_0x0014:
            r0 = 1
            if (r6 != 0) goto L_0x0019
            r4.mBubbleShouldShrink = r0
        L_0x0019:
            monitor-enter(r4)
            r1 = 0
            r2 = r1
        L_0x001c:
            java.util.List<com.google.android.settings.gestures.assist.bubble.Bubble> r3 = r4.mBubbles     // Catch:{ all -> 0x0085 }
            int r3 = r3.size()     // Catch:{ all -> 0x0085 }
            if (r2 >= r3) goto L_0x0057
            java.util.List<com.google.android.settings.gestures.assist.bubble.Bubble> r3 = r4.mBubbles     // Catch:{ all -> 0x0085 }
            java.lang.Object r3 = r3.get(r2)     // Catch:{ all -> 0x0085 }
            com.google.android.settings.gestures.assist.bubble.Bubble r3 = (com.google.android.settings.gestures.assist.bubble.Bubble) r3     // Catch:{ all -> 0x0085 }
            int r3 = r3.getState()     // Catch:{ all -> 0x0085 }
            if (r3 != 0) goto L_0x0054
            if (r6 == 0) goto L_0x0047
            java.util.List<com.google.android.settings.gestures.assist.bubble.Bubble> r2 = r4.mBubbles     // Catch:{ all -> 0x0085 }
            java.lang.Object r2 = r2.get(r1)     // Catch:{ all -> 0x0085 }
            com.google.android.settings.gestures.assist.bubble.Bubble r2 = (com.google.android.settings.gestures.assist.bubble.Bubble) r2     // Catch:{ all -> 0x0085 }
            com.google.android.settings.gestures.assist.bubble.Bubble r3 = r4.mLastShrunkBubble     // Catch:{ all -> 0x0085 }
            boolean r2 = r2.equals(r3)     // Catch:{ all -> 0x0085 }
            if (r2 != 0) goto L_0x0047
            r4.mBubbleShouldShrink = r1     // Catch:{ all -> 0x0085 }
            goto L_0x0057
        L_0x0047:
            r4.mBubbleShouldShrink = r0     // Catch:{ all -> 0x0085 }
            java.util.List<com.google.android.settings.gestures.assist.bubble.Bubble> r0 = r4.mBubbles     // Catch:{ all -> 0x0085 }
            java.lang.Object r0 = r0.get(r1)     // Catch:{ all -> 0x0085 }
            com.google.android.settings.gestures.assist.bubble.Bubble r0 = (com.google.android.settings.gestures.assist.bubble.Bubble) r0     // Catch:{ all -> 0x0085 }
            r4.mLastShrunkBubble = r0     // Catch:{ all -> 0x0085 }
            goto L_0x0057
        L_0x0054:
            int r2 = r2 + 1
            goto L_0x001c
        L_0x0057:
            com.google.android.settings.gestures.assist.bubble.Bubble r0 = r4.mLastShrunkBubble     // Catch:{ all -> 0x0085 }
            if (r0 == 0) goto L_0x007f
            boolean r1 = r4.mBubbleShouldShrink     // Catch:{ all -> 0x0085 }
            if (r1 == 0) goto L_0x007f
            int r0 = r0.getState()     // Catch:{ all -> 0x0085 }
            if (r0 != 0) goto L_0x007f
            com.google.android.settings.gestures.assist.bubble.Bubble r0 = r4.mLastShrunkBubble     // Catch:{ all -> 0x0085 }
            int r1 = r0.getOriginalSize()     // Catch:{ all -> 0x0085 }
            float r1 = (float) r1     // Catch:{ all -> 0x0085 }
            com.google.android.settings.gestures.assist.bubble.Bubble r2 = r4.mLastShrunkBubble     // Catch:{ all -> 0x0085 }
            int r2 = r2.getOriginalSize()     // Catch:{ all -> 0x0085 }
            float r2 = (float) r2     // Catch:{ all -> 0x0085 }
            float r2 = r2 * r5
            float r1 = r1 - r2
            int r1 = (int) r1     // Catch:{ all -> 0x0085 }
            r2 = 16
            int r1 = java.lang.Math.max(r1, r2)     // Catch:{ all -> 0x0085 }
            r0.setSize(r1)     // Catch:{ all -> 0x0085 }
        L_0x007f:
            monitor-exit(r4)     // Catch:{ all -> 0x0085 }
            r4.mLastProgress = r5
            r4.mLastStage = r6
            return
        L_0x0085:
            r5 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0085 }
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.settings.gestures.assist.bubble.AssistGestureGameDrawable.onGestureProgress(float, int):void");
    }

    /* access modifiers changed from: private */
    public void onGestureDetected() {
        if (this.mGameState == 3) {
            this.mLastProgress = 0.0f;
            this.mLastStage = 0;
            this.mBubbleShouldShrink = true;
            long currentTimeMillis = System.currentTimeMillis();
            this.mLastGestureTime = currentTimeMillis;
            if (this.mLastShrunkBubble != null) {
                synchronized (this) {
                    this.mLastShrunkBubble.setState(1);
                }
                int i = this.mKilledBubbles + 1;
                this.mKilledBubbles = i;
                if (i > this.mTopKilledBubbles) {
                    this.mTopKilledBubbles = i;
                    this.mTopKilledBubblesDate = currentTimeMillis;
                    Settings.Secure.putIntForUser(this.mContext.getContentResolver(), "assist_gesture_egg_top_score", this.mTopKilledBubbles, -2);
                    Settings.Secure.putLongForUser(this.mContext.getContentResolver(), "assist_gesture_egg_top_score_time", this.mTopKilledBubblesDate, -2);
                }
                this.mNextBubbleTime = 0.0f;
                updateScoreText();
            }
        }
    }

    public void disconnectService() {
        this.mAssistGestureHelper.setListener((AssistGestureHelper.GestureListener) null);
        this.mAssistGestureHelper.unbindFromElmyraServiceProxy();
        this.mServiceConnected = false;
    }

    private void connectService() {
        this.mAssistGestureHelper.bindToElmyraServiceProxy();
        this.mAssistGestureHelper.setListener(this.mGestureListener);
        this.mServiceConnected = true;
    }

    public void pauseGame() {
        if (this.mGameState != 1) {
            this.mGameState = 1;
            notifyGameStateChanged();
            disconnectService();
            this.mNextBubbleTime -= this.mLastTime;
            TimeAnimator timeAnimator = this.mDriftAnimation;
            if (timeAnimator != null) {
                timeAnimator.pause();
            }
        }
    }

    private void resetGameState() {
        resetSpiralingAndroids(this.mBounds);
        this.mDeadBubbles.clear();
        this.mKilledBubbles = 0;
        updateScoreText();
        this.mBubbleTouchedBottom = false;
    }

    public void startGame(boolean z) {
        if (this.mBounds == null) {
            this.mGameState = 2;
            notifyGameStateChanged();
        } else if (this.mGameState != 3) {
            if (z) {
                resetGameState();
            }
            connectService();
            if (this.mBubbleTouchedBottom) {
                this.mGameState = 4;
            } else {
                this.mGameState = 3;
                notifyGameStateChanged();
            }
            if (this.mDriftAnimation == null) {
                TimeAnimator timeAnimator = new TimeAnimator();
                this.mDriftAnimation = timeAnimator;
                timeAnimator.setTimeListener(new TimeAnimator.TimeListener() {
                    public void onTimeUpdate(TimeAnimator timeAnimator, long j, long j2) {
                        float unused = AssistGestureGameDrawable.this.mLastTime = ((float) j) * 0.001f;
                        if (AssistGestureGameDrawable.this.mGameState == 3) {
                            synchronized (this) {
                                if (AssistGestureGameDrawable.this.mLastTime > AssistGestureGameDrawable.this.mNextBubbleTime) {
                                    AssistGestureGameDrawable.this.mBubbles.add(new Bubble(AssistGestureGameDrawable.this.mBounds));
                                    AssistGestureGameDrawable assistGestureGameDrawable = AssistGestureGameDrawable.this;
                                    float unused2 = assistGestureGameDrawable.mNextBubbleTime = assistGestureGameDrawable.mLastTime + 1.0f;
                                }
                                for (int size = AssistGestureGameDrawable.this.mBubbles.size() - 1; size >= 0; size--) {
                                    Bubble bubble = (Bubble) AssistGestureGameDrawable.this.mBubbles.get(size);
                                    bubble.update(j, j2);
                                    if (bubble.isBubbleDead()) {
                                        AssistGestureGameDrawable.this.mBubbles.remove(size);
                                    } else if (bubble.isBubbleTouchingTop() && bubble.getState() == 0) {
                                        AssistGestureGameDrawable.this.mDeadBubbles.add(bubble);
                                        AssistGestureGameDrawable.this.mBubbles.remove(size);
                                    } else if (AssistGestureGameDrawable.this.hasCollisionWithDeadBubbles(bubble)) {
                                        if (bubble.getPoint().y + ((float) bubble.getSize()) > ((float) AssistGestureGameDrawable.this.mBounds.bottom)) {
                                            int unused3 = AssistGestureGameDrawable.this.mGameState = 4;
                                            boolean unused4 = AssistGestureGameDrawable.this.mBubbleTouchedBottom = true;
                                        }
                                        if (bubble.getState() == 0) {
                                            AssistGestureGameDrawable.this.mDeadBubbles.add(bubble);
                                            AssistGestureGameDrawable.this.mBubbles.remove(size);
                                        }
                                    }
                                }
                            }
                        }
                        if (AssistGestureGameDrawable.this.mGameState == 4) {
                            synchronized (this) {
                                boolean z = false;
                                for (int i = 0; i < AssistGestureGameDrawable.this.mSpiralingAndroids.size(); i++) {
                                    SpiralingAndroid spiralingAndroid = (SpiralingAndroid) AssistGestureGameDrawable.this.mSpiralingAndroids.get(i);
                                    if (spiralingAndroid.getAndroid().getBounds().bottom < AssistGestureGameDrawable.this.mBounds.bottom) {
                                        spiralingAndroid.update(j, j2);
                                        z = true;
                                    }
                                }
                                if (AssistGestureGameDrawable.this.mServiceConnected) {
                                    AssistGestureGameDrawable.this.disconnectService();
                                }
                                if (!z) {
                                    AssistGestureGameDrawable.this.notifyGameStateChanged();
                                    AssistGestureGameDrawable.this.mDriftAnimation.pause();
                                }
                            }
                        }
                        AssistGestureGameDrawable.this.invalidateSelf();
                    }
                });
            }
            this.mDriftAnimation.start();
        }
    }

    private double distance(Bubble bubble, Bubble bubble2) {
        PointF point = bubble.getPoint();
        PointF point2 = bubble2.getPoint();
        return Math.sqrt(Math.pow((double) (point2.x - point.x), 2.0d) + Math.pow((double) (point2.y - point.y), 2.0d));
    }

    /* access modifiers changed from: private */
    public boolean hasCollisionWithDeadBubbles(Bubble bubble) {
        for (int i = 0; i < this.mDeadBubbles.size(); i++) {
            Bubble bubble2 = this.mDeadBubbles.get(i);
            if (distance(bubble, bubble2) < ((double) (bubble.getSize() + bubble2.getSize()))) {
                return true;
            }
        }
        return false;
    }

    private void resetSpiralingAndroids(Rect rect) {
        synchronized (this) {
            this.mSpiralingAndroids.clear();
            for (int i = 0; i < 40; i++) {
                this.mSpiralingAndroids.add(new SpiralingAndroid(this.mContext, rect));
            }
        }
    }

    public void onBoundsChange(Rect rect) {
        this.mBounds = rect;
        if (this.mGameState == 2) {
            startGame(true);
        }
    }

    public void draw(Canvas canvas) {
        int i;
        float f;
        float f2;
        long currentTimeMillis = System.currentTimeMillis();
        canvas.save();
        synchronized (this) {
            for (int i2 = 0; i2 < this.mBubbles.size(); i2++) {
                Bubble bubble = this.mBubbles.get(i2);
                this.mPaint.setColor(bubble.getColor());
                canvas.drawCircle(bubble.getPoint().x, bubble.getPoint().y, (float) bubble.getSize(), this.mPaint);
            }
            for (int i3 = 0; i3 < this.mDeadBubbles.size(); i3++) {
                Bubble bubble2 = this.mDeadBubbles.get(i3);
                this.mPaint.setColor(bubble2.getColor());
                canvas.drawCircle(bubble2.getPoint().x, bubble2.getPoint().y, (float) bubble2.getSize(), this.mPaint);
            }
        }
        this.mPaint.setColor(-1);
        this.mPaint.setAlpha(180);
        float height = (float) (this.mBounds.height() - 80);
        float height2 = (float) this.mBounds.height();
        if (currentTimeMillis - this.mLastGestureTime < 450) {
            float centerX = (float) ((((long) this.mBounds.centerX()) * (currentTimeMillis - this.mLastGestureTime)) / 450);
            f2 = ((float) this.mBounds.centerX()) - centerX;
            f = ((float) this.mBounds.centerX()) + centerX;
        } else {
            float centerX2 = ((float) this.mBounds.centerX()) * this.mLastProgress;
            f2 = centerX2;
            f = ((float) this.mBounds.width()) - centerX2;
        }
        canvas.drawRect(f2, height, f, height2, this.mPaint);
        if (this.mGameState != 3) {
            synchronized (this) {
                for (i = 0; i < this.mSpiralingAndroids.size(); i++) {
                    canvas.save();
                    SpiralingAndroid spiralingAndroid = this.mSpiralingAndroids.get(i);
                    Drawable android2 = spiralingAndroid.getAndroid();
                    canvas.rotate(spiralingAndroid.getCurrentRotation(), (float) android2.getBounds().centerX(), (float) android2.getBounds().centerY());
                    spiralingAndroid.getAndroid().draw(canvas);
                    canvas.restore();
                }
            }
        }
        canvas.restore();
    }

    public void setAlpha(int i) {
        this.mPaint.setAlpha(i);
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
    }
}
