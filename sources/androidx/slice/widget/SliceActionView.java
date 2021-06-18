package androidx.slice.widget;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import androidx.appcompat.R$attr;
import androidx.appcompat.R$color;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.slice.SliceItem;
import androidx.slice.core.SliceActionImpl;
import androidx.slice.view.R$dimen;
import androidx.slice.view.R$layout;
import androidx.slice.widget.SliceView;

public class SliceActionView extends FrameLayout implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    static final int[] CHECKED_STATE_SET = {16842912};
    private View mActionView;
    private EventInfo mEventInfo;
    private int mIconSize;
    private int mImageSize;
    private SliceActionLoadingListener mLoadingListener;
    private SliceView.OnSliceActionListener mObserver;
    private ProgressBar mProgressView;
    private SliceActionImpl mSliceAction;
    private int mTextActionPadding = 0;

    interface SliceActionLoadingListener {
        void onSliceActionLoading(SliceItem sliceItem, int i);
    }

    public SliceActionView(Context context, SliceStyle sliceStyle, RowStyle rowStyle) {
        super(context);
        Resources resources = getContext().getResources();
        this.mIconSize = resources.getDimensionPixelSize(R$dimen.abc_slice_icon_size);
        this.mImageSize = resources.getDimensionPixelSize(R$dimen.abc_slice_small_image_size);
        if (rowStyle != null) {
            this.mIconSize = rowStyle.getIconSize();
            this.mImageSize = rowStyle.getImageSize();
            this.mTextActionPadding = rowStyle.getTextActionPadding();
        }
    }

    public void setAction(SliceActionImpl sliceActionImpl, EventInfo eventInfo, SliceView.OnSliceActionListener onSliceActionListener, int i, SliceActionLoadingListener sliceActionLoadingListener) {
        CharSequence charSequence;
        View view = this.mActionView;
        if (view != null) {
            removeView(view);
            this.mActionView = null;
        }
        ProgressBar progressBar = this.mProgressView;
        if (progressBar != null) {
            removeView(progressBar);
            this.mProgressView = null;
        }
        this.mSliceAction = sliceActionImpl;
        this.mEventInfo = eventInfo;
        this.mObserver = onSliceActionListener;
        this.mActionView = null;
        this.mLoadingListener = sliceActionLoadingListener;
        int i2 = 0;
        if (sliceActionImpl.isDefaultToggle()) {
            Switch switchR = (Switch) LayoutInflater.from(getContext()).inflate(R$layout.abc_slice_switch, this, false);
            switchR.setChecked(sliceActionImpl.isChecked());
            switchR.setOnCheckedChangeListener(this);
            switchR.setMinimumHeight(this.mImageSize);
            switchR.setMinimumWidth(this.mImageSize);
            addView(switchR);
            if (i != -1) {
                int colorAttr = SliceViewUtil.getColorAttr(getContext(), 16842800);
                int[] iArr = CHECKED_STATE_SET;
                int[] iArr2 = FrameLayout.EMPTY_STATE_SET;
                ColorStateList colorStateList = new ColorStateList(new int[][]{iArr, iArr2}, new int[]{i, colorAttr});
                Drawable wrap = DrawableCompat.wrap(switchR.getTrackDrawable());
                DrawableCompat.setTintList(wrap, colorStateList);
                switchR.setTrackDrawable(wrap);
                int colorAttr2 = SliceViewUtil.getColorAttr(getContext(), R$attr.colorSwitchThumbNormal);
                if (colorAttr2 == 0) {
                    colorAttr2 = ContextCompat.getColor(getContext(), R$color.switch_thumb_normal_material_light);
                }
                ColorStateList colorStateList2 = new ColorStateList(new int[][]{iArr, iArr2}, new int[]{i, colorAttr2});
                Drawable wrap2 = DrawableCompat.wrap(switchR.getThumbDrawable());
                DrawableCompat.setTintList(wrap2, colorStateList2);
                switchR.setThumbDrawable(wrap2);
            }
            this.mActionView = switchR;
        } else if (sliceActionImpl.getImageMode() == 6) {
            Button button = new Button(getContext());
            this.mActionView = button;
            button.setText(sliceActionImpl.getTitle());
            addView(this.mActionView);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mActionView.getLayoutParams();
            layoutParams.width = -2;
            layoutParams.height = -2;
            this.mActionView.setLayoutParams(layoutParams);
            int i3 = this.mTextActionPadding;
            this.mActionView.setPadding(i3, i3, i3, i3);
            this.mActionView.setOnClickListener(this);
        } else if (sliceActionImpl.getIcon() != null) {
            if (sliceActionImpl.isToggle()) {
                ImageToggle imageToggle = new ImageToggle(getContext());
                imageToggle.setChecked(sliceActionImpl.isChecked());
                this.mActionView = imageToggle;
            } else {
                this.mActionView = new ImageView(getContext());
            }
            addView(this.mActionView);
            Drawable loadDrawable = this.mSliceAction.getIcon().loadDrawable(getContext());
            ((ImageView) this.mActionView).setImageDrawable(loadDrawable);
            if (!(i == -1 || this.mSliceAction.getImageMode() != 0 || loadDrawable == null)) {
                DrawableCompat.setTint(loadDrawable, i);
            }
            FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) this.mActionView.getLayoutParams();
            int i4 = this.mImageSize;
            layoutParams2.width = i4;
            layoutParams2.height = i4;
            this.mActionView.setLayoutParams(layoutParams2);
            if (sliceActionImpl.getImageMode() == 0) {
                int i5 = this.mImageSize;
                i2 = (i5 == -1 ? this.mIconSize : i5 - this.mIconSize) / 2;
            }
            this.mActionView.setPadding(i2, i2, i2, i2);
            int i6 = 16843534;
            if (Build.VERSION.SDK_INT >= 21) {
                i6 = 16843868;
            }
            this.mActionView.setBackground(SliceViewUtil.getDrawable(getContext(), i6));
            this.mActionView.setOnClickListener(this);
        }
        if (this.mActionView != null) {
            if (sliceActionImpl.getContentDescription() != null) {
                charSequence = sliceActionImpl.getContentDescription();
            } else {
                charSequence = sliceActionImpl.getTitle();
            }
            this.mActionView.setContentDescription(charSequence);
        }
    }

    public void setLoading(boolean z) {
        int i = 0;
        if (z) {
            if (this.mProgressView == null) {
                ProgressBar progressBar = (ProgressBar) LayoutInflater.from(getContext()).inflate(R$layout.abc_slice_progress_view, this, false);
                this.mProgressView = progressBar;
                addView(progressBar);
            }
            SliceViewUtil.tintIndeterminateProgressBar(getContext(), this.mProgressView);
        }
        this.mActionView.setVisibility(z ? 8 : 0);
        ProgressBar progressBar2 = this.mProgressView;
        if (!z) {
            i = 8;
        }
        progressBar2.setVisibility(i);
    }

    public void toggle() {
        SliceActionImpl sliceActionImpl;
        if (this.mActionView != null && (sliceActionImpl = this.mSliceAction) != null && sliceActionImpl.isToggle()) {
            ((Checkable) this.mActionView).toggle();
        }
    }

    public SliceActionImpl getAction() {
        return this.mSliceAction;
    }

    public void onClick(View view) {
        if (this.mSliceAction != null && this.mActionView != null) {
            sendActionInternal();
        }
    }

    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        if (this.mSliceAction != null && this.mActionView != null) {
            sendActionInternal();
        }
    }

    public void sendAction() {
        SliceActionImpl sliceActionImpl = this.mSliceAction;
        if (sliceActionImpl != null) {
            if (sliceActionImpl.isToggle()) {
                toggle();
            } else {
                sendActionInternal();
            }
        }
    }

    private void sendActionInternal() {
        EventInfo eventInfo;
        SliceActionImpl sliceActionImpl = this.mSliceAction;
        if (sliceActionImpl != null && sliceActionImpl.getActionItem() != null) {
            Intent intent = null;
            try {
                if (this.mSliceAction.isToggle()) {
                    boolean isChecked = ((Checkable) this.mActionView).isChecked();
                    Intent putExtra = new Intent().addFlags(268435456).putExtra("android.app.slice.extra.TOGGLE_STATE", isChecked);
                    EventInfo eventInfo2 = this.mEventInfo;
                    if (eventInfo2 != null) {
                        eventInfo2.state = isChecked ? 1 : 0;
                    }
                    intent = putExtra;
                }
                if (this.mSliceAction.getActionItem().fireActionInternal(getContext(), intent)) {
                    setLoading(true);
                    SliceActionLoadingListener sliceActionLoadingListener = this.mLoadingListener;
                    if (sliceActionLoadingListener != null) {
                        EventInfo eventInfo3 = this.mEventInfo;
                        sliceActionLoadingListener.onSliceActionLoading(this.mSliceAction.getSliceItem(), eventInfo3 != null ? eventInfo3.rowIndex : -1);
                    }
                }
                SliceView.OnSliceActionListener onSliceActionListener = this.mObserver;
                if (onSliceActionListener != null && (eventInfo = this.mEventInfo) != null) {
                    onSliceActionListener.onSliceAction(eventInfo, this.mSliceAction.getSliceItem());
                }
            } catch (PendingIntent.CanceledException e) {
                View view = this.mActionView;
                if (view instanceof Checkable) {
                    view.setSelected(true ^ ((Checkable) view).isChecked());
                }
                Log.e("SliceActionView", "PendingIntent for slice cannot be sent", e);
            }
        }
    }

    private static class ImageToggle extends ImageView implements Checkable, View.OnClickListener {
        private boolean mIsChecked;
        private View.OnClickListener mListener;

        ImageToggle(Context context) {
            super(context);
            super.setOnClickListener(this);
        }

        public void onClick(View view) {
            toggle();
        }

        public void toggle() {
            setChecked(!isChecked());
        }

        public void setChecked(boolean z) {
            if (this.mIsChecked != z) {
                this.mIsChecked = z;
                refreshDrawableState();
                View.OnClickListener onClickListener = this.mListener;
                if (onClickListener != null) {
                    onClickListener.onClick(this);
                }
            }
        }

        public void setOnClickListener(View.OnClickListener onClickListener) {
            this.mListener = onClickListener;
        }

        public boolean isChecked() {
            return this.mIsChecked;
        }

        public int[] onCreateDrawableState(int i) {
            int[] onCreateDrawableState = super.onCreateDrawableState(i + 1);
            if (this.mIsChecked) {
                ImageView.mergeDrawableStates(onCreateDrawableState, SliceActionView.CHECKED_STATE_SET);
            }
            return onCreateDrawableState;
        }
    }
}
