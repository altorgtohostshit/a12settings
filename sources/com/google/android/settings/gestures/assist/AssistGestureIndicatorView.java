package com.google.android.settings.gestures.assist;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.android.settings.R;

public class AssistGestureIndicatorView extends LinearLayout {
    private ViewGroup mLayout;
    private IndicatorDrawable mLeftDrawable;
    private ImageView mLeftIndicator;
    private IndicatorDrawable mRightDrawable;
    private ImageView mRightIndicator;

    public AssistGestureIndicatorView(Context context) {
        super(context);
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.assist_gesture_indicator_container, this, false);
        this.mLayout = viewGroup;
        if (viewGroup != null) {
            addView(viewGroup);
            this.mLeftDrawable = new IndicatorDrawable(context, false);
            this.mRightDrawable = new IndicatorDrawable(context, true);
            this.mLeftIndicator = (ImageView) this.mLayout.findViewById(R.id.indicator_left);
            this.mRightIndicator = (ImageView) this.mLayout.findViewById(R.id.indicator_right);
            this.mLeftIndicator.setImageDrawable(this.mLeftDrawable);
            this.mRightIndicator.setImageDrawable(this.mRightDrawable);
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{16844140, 16844000});
            if (obtainStyledAttributes.getBoolean(0, false)) {
                setSystemUiVisibility(getSystemUiVisibility() | 16);
            }
            if (obtainStyledAttributes.getBoolean(1, false)) {
                setSystemUiVisibility(getSystemUiVisibility() | 8192);
            }
            obtainStyledAttributes.recycle();
        }
    }

    public void onGestureProgress(float f) {
        this.mLeftDrawable.onGestureProgress(f);
        this.mRightDrawable.onGestureProgress(f);
    }

    public void onGestureDetected() {
        this.mLeftDrawable.onGestureDetected();
        this.mRightDrawable.onGestureDetected();
    }

    public WindowManager.LayoutParams getLayoutParams(WindowManager.LayoutParams layoutParams) {
        WindowManager.LayoutParams layoutParams2 = new WindowManager.LayoutParams(-1, -1, 2, (layoutParams.flags & Integer.MIN_VALUE) | 16777240, -3);
        layoutParams2.setTitle("AssistGestureIndicatorView");
        layoutParams2.token = getContext().getActivityToken();
        return layoutParams2;
    }
}
