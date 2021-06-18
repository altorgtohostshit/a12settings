package com.android.settings.accessibility;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.provider.Settings;
import android.util.AttributeSet;
import android.widget.SeekBar;
import com.android.settings.R;
import com.android.settings.Utils;

public class BalanceSeekBar extends SeekBar {
    static final float SNAP_TO_PERCENTAGE = 0.03f;
    /* access modifiers changed from: private */
    public int mCenter;
    private final Paint mCenterMarkerPaint;
    private final Rect mCenterMarkerRect;
    /* access modifiers changed from: private */
    public final Context mContext;
    /* access modifiers changed from: private */
    public int mLastProgress;
    /* access modifiers changed from: private */
    public final Object mListenerLock;
    /* access modifiers changed from: private */
    public SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener;
    private final SeekBar.OnSeekBarChangeListener mProxySeekBarListener;
    /* access modifiers changed from: private */
    public float mSnapThreshold;

    public BalanceSeekBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 16842875);
    }

    public BalanceSeekBar(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public BalanceSeekBar(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mListenerLock = new Object();
        int i3 = -1;
        this.mLastProgress = -1;
        C06001 r6 = new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                synchronized (BalanceSeekBar.this.mListenerLock) {
                    if (BalanceSeekBar.this.mOnSeekBarChangeListener != null) {
                        BalanceSeekBar.this.mOnSeekBarChangeListener.onStopTrackingTouch(seekBar);
                    }
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                synchronized (BalanceSeekBar.this.mListenerLock) {
                    if (BalanceSeekBar.this.mOnSeekBarChangeListener != null) {
                        BalanceSeekBar.this.mOnSeekBarChangeListener.onStartTrackingTouch(seekBar);
                    }
                }
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (z) {
                    if (i != BalanceSeekBar.this.mCenter) {
                        float f = (float) i;
                        if (f > ((float) BalanceSeekBar.this.mCenter) - BalanceSeekBar.this.mSnapThreshold && f < ((float) BalanceSeekBar.this.mCenter) + BalanceSeekBar.this.mSnapThreshold) {
                            i = BalanceSeekBar.this.mCenter;
                            seekBar.setProgress(i);
                        }
                    }
                    if (i != BalanceSeekBar.this.mLastProgress) {
                        if (i == BalanceSeekBar.this.mCenter || i == BalanceSeekBar.this.getMin() || i == BalanceSeekBar.this.getMax()) {
                            seekBar.performHapticFeedback(4);
                        }
                        int unused = BalanceSeekBar.this.mLastProgress = i;
                    }
                    Settings.System.putFloatForUser(BalanceSeekBar.this.mContext.getContentResolver(), "master_balance", ((float) (i - BalanceSeekBar.this.mCenter)) * 0.01f, -2);
                }
                synchronized (BalanceSeekBar.this.mListenerLock) {
                    if (BalanceSeekBar.this.mOnSeekBarChangeListener != null) {
                        BalanceSeekBar.this.mOnSeekBarChangeListener.onProgressChanged(seekBar, i, z);
                    }
                }
            }
        };
        this.mProxySeekBarListener = r6;
        this.mContext = context;
        Resources resources = getResources();
        this.mCenterMarkerRect = new Rect(0, 0, resources.getDimensionPixelSize(R.dimen.balance_seekbar_center_marker_width), resources.getDimensionPixelSize(R.dimen.balance_seekbar_center_marker_height));
        Paint paint = new Paint();
        this.mCenterMarkerPaint = paint;
        paint.setColor(!Utils.isNightMode(context) ? -16777216 : i3);
        paint.setStyle(Paint.Style.FILL);
        setProgressTintList(ColorStateList.valueOf(0));
        super.setOnSeekBarChangeListener(r6);
    }

    public void setOnSeekBarChangeListener(SeekBar.OnSeekBarChangeListener onSeekBarChangeListener) {
        synchronized (this.mListenerLock) {
            this.mOnSeekBarChangeListener = onSeekBarChangeListener;
        }
    }

    public synchronized void setMax(int i) {
        super.setMax(i);
        this.mCenter = i / 2;
        this.mSnapThreshold = ((float) i) * SNAP_TO_PERCENTAGE;
    }

    /* access modifiers changed from: protected */
    public synchronized void onDraw(Canvas canvas) {
        canvas.save();
        int width = canvas.getWidth();
        Rect rect = this.mCenterMarkerRect;
        canvas.translate((float) ((width - rect.right) / 2), (float) (((canvas.getHeight() - getPaddingBottom()) / 2) - (rect.bottom / 2)));
        canvas.drawRect(this.mCenterMarkerRect, this.mCenterMarkerPaint);
        canvas.restore();
        super.onDraw(canvas);
    }
}
