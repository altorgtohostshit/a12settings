package com.google.android.settings.biometrics.face.anim;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.widget.ImageView;
import com.android.settings.R;

public class FaceOutlineIndicatorController {
    private final Context mContext;
    private int mState = 0;
    private final ImageView mView;

    public FaceOutlineIndicatorController(Context context, ImageView imageView) {
        this.mContext = context;
        this.mView = imageView;
    }

    public void show() {
        if (this.mState != 1) {
            AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) this.mContext.getDrawable(R.drawable.face_distance_fade_in);
            this.mView.setImageDrawable(animatedVectorDrawable);
            animatedVectorDrawable.start();
            this.mState = 1;
        }
    }

    public void clear() {
        if (this.mState != 0) {
            AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) this.mContext.getDrawable(R.drawable.face_distance_fade_out);
            this.mView.setImageDrawable(animatedVectorDrawable);
            animatedVectorDrawable.start();
            this.mState = 0;
        }
    }
}
