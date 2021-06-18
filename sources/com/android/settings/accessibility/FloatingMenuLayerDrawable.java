package com.android.settings.accessibility;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import com.android.settings.R;
import java.util.Objects;

public class FloatingMenuLayerDrawable extends LayerDrawable {
    private FloatingMenuLayerDrawableState mState;

    private FloatingMenuLayerDrawable(Drawable[] drawableArr) {
        super(drawableArr);
    }

    public static FloatingMenuLayerDrawable createLayerDrawable(Context context, int i, int i2) {
        FloatingMenuLayerDrawable floatingMenuLayerDrawable = new FloatingMenuLayerDrawable(new Drawable[]{context.getDrawable(R.drawable.accessibility_button_preview_base), null});
        floatingMenuLayerDrawable.updateLayerDrawable(context, i, i2);
        return floatingMenuLayerDrawable;
    }

    public void updateLayerDrawable(Context context, int i, int i2) {
        Drawable drawable = context.getDrawable(i);
        drawable.setAlpha(i2);
        setDrawable(1, drawable);
        setConstantState(context, i, i2);
    }

    public Drawable.ConstantState getConstantState() {
        return this.mState;
    }

    private void setConstantState(Context context, int i, int i2) {
        this.mState = new FloatingMenuLayerDrawableState(context, i, i2);
    }

    static class FloatingMenuLayerDrawableState extends Drawable.ConstantState {
        private final Context mContext;
        private final int mOpacity;
        private final int mResId;

        public int getChangingConfigurations() {
            return 0;
        }

        FloatingMenuLayerDrawableState(Context context, int i, int i2) {
            this.mContext = context;
            this.mResId = i;
            this.mOpacity = i2;
        }

        public Drawable newDrawable() {
            return FloatingMenuLayerDrawable.createLayerDrawable(this.mContext, this.mResId, this.mOpacity);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            FloatingMenuLayerDrawableState floatingMenuLayerDrawableState = (FloatingMenuLayerDrawableState) obj;
            if (this.mResId == floatingMenuLayerDrawableState.mResId && this.mOpacity == floatingMenuLayerDrawableState.mOpacity && Objects.equals(this.mContext, floatingMenuLayerDrawableState.mContext)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return Objects.hash(new Object[]{this.mContext, Integer.valueOf(this.mResId), Integer.valueOf(this.mOpacity)});
        }
    }
}
