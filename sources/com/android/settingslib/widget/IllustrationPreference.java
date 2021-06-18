package com.android.settingslib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.airbnb.lottie.LottieAnimationView;

public class IllustrationPreference extends Preference implements Preference.OnPreferenceClickListener {
    private int mAnimationId;
    private LottieAnimationView mIllustrationView;
    private boolean mIsAnimating;
    private ImageView mPlayButton;

    public IllustrationPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    public IllustrationPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet);
    }

    public IllustrationPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(context, attributeSet);
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        if (this.mAnimationId == 0) {
            Log.w("IllustrationPreference", "Invalid illustration resource id.");
            return;
        }
        this.mPlayButton = (ImageView) preferenceViewHolder.findViewById(R$id.video_play_button);
        LottieAnimationView lottieAnimationView = (LottieAnimationView) preferenceViewHolder.findViewById(R$id.lottie_view);
        this.mIllustrationView = lottieAnimationView;
        lottieAnimationView.setAnimation(this.mAnimationId);
        this.mIllustrationView.loop(true);
        this.mIllustrationView.playAnimation();
        updateAnimationStatus(this.mIsAnimating);
        setOnPreferenceClickListener(this);
    }

    public boolean onPreferenceClick(Preference preference) {
        boolean z = !isAnimating();
        this.mIsAnimating = z;
        updateAnimationStatus(z);
        return true;
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.mIsAnimating = this.mIsAnimating;
        return savedState;
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.mIsAnimating = savedState.mIsAnimating;
    }

    /* access modifiers changed from: package-private */
    public boolean isAnimating() {
        return this.mIllustrationView.isAnimating();
    }

    private void init(Context context, AttributeSet attributeSet) {
        setLayoutResource(R$layout.illustration_preference);
        this.mIsAnimating = true;
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.LottieAnimationView, 0, 0);
            this.mAnimationId = obtainStyledAttributes.getResourceId(R$styleable.LottieAnimationView_lottie_rawRes, 0);
            obtainStyledAttributes.recycle();
        }
    }

    private void updateAnimationStatus(boolean z) {
        if (z) {
            this.mIllustrationView.resumeAnimation();
            this.mPlayButton.setVisibility(4);
            return;
        }
        this.mIllustrationView.pauseAnimation();
        this.mPlayButton.setVisibility(0);
    }

    static class SavedState extends Preference.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        boolean mIsAnimating;

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.mIsAnimating = ((Boolean) parcel.readValue((ClassLoader) null)).booleanValue();
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeValue(Boolean.valueOf(this.mIsAnimating));
        }

        public String toString() {
            return "IllustrationPreference.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " mIsAnimating=" + this.mIsAnimating + "}";
        }
    }
}
