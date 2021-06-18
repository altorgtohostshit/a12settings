package androidx.slice.widget;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.slice.view.R$dimen;
import androidx.slice.view.R$styleable;

public class RowStyle {
    private int mActionDividerHeight = -1;
    private int mBottomDividerEndPadding = -1;
    private int mBottomDividerStartPadding = -1;
    private int mContentEndPadding = -1;
    private int mContentStartPadding = -1;
    private boolean mDisableRecyclerViewItemAnimator = false;
    private int mEndItemEndPadding = -1;
    private int mEndItemStartPadding = -1;
    private int mIconSize = -1;
    private int mImageSize;
    private int mProgressBarEndPadding = -1;
    private int mProgressBarInlineWidth = -1;
    private int mProgressBarStartPadding = -1;
    private int mSeekBarInlineWidth = -1;
    private final SliceStyle mSliceStyle;
    private int mSubContentEndPadding = -1;
    private int mSubContentStartPadding = -1;
    private Integer mSubtitleColor;
    private int mTextActionPadding = -1;
    private Integer mTintColor;
    private Integer mTitleColor;
    private int mTitleEndPadding = -1;
    private int mTitleItemEndPadding = -1;
    private int mTitleItemStartPadding = -1;
    private int mTitleStartPadding = -1;

    public RowStyle(Context context, SliceStyle sliceStyle) {
        this.mSliceStyle = sliceStyle;
        this.mImageSize = context.getResources().getDimensionPixelSize(R$dimen.abc_slice_small_image_size);
    }

    public RowStyle(Context context, int i, SliceStyle sliceStyle) {
        this.mSliceStyle = sliceStyle;
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(i, R$styleable.RowStyle);
        try {
            this.mTitleItemStartPadding = (int) obtainStyledAttributes.getDimension(R$styleable.RowStyle_titleItemStartPadding, -1.0f);
            this.mTitleItemEndPadding = (int) obtainStyledAttributes.getDimension(R$styleable.RowStyle_titleItemEndPadding, -1.0f);
            this.mContentStartPadding = (int) obtainStyledAttributes.getDimension(R$styleable.RowStyle_contentStartPadding, -1.0f);
            this.mContentEndPadding = (int) obtainStyledAttributes.getDimension(R$styleable.RowStyle_contentEndPadding, -1.0f);
            this.mTitleStartPadding = (int) obtainStyledAttributes.getDimension(R$styleable.RowStyle_titleStartPadding, -1.0f);
            this.mTitleEndPadding = (int) obtainStyledAttributes.getDimension(R$styleable.RowStyle_titleEndPadding, -1.0f);
            this.mSubContentStartPadding = (int) obtainStyledAttributes.getDimension(R$styleable.RowStyle_subContentStartPadding, -1.0f);
            this.mSubContentEndPadding = (int) obtainStyledAttributes.getDimension(R$styleable.RowStyle_subContentEndPadding, -1.0f);
            this.mEndItemStartPadding = (int) obtainStyledAttributes.getDimension(R$styleable.RowStyle_endItemStartPadding, -1.0f);
            this.mEndItemEndPadding = (int) obtainStyledAttributes.getDimension(R$styleable.RowStyle_endItemEndPadding, -1.0f);
            this.mBottomDividerStartPadding = (int) obtainStyledAttributes.getDimension(R$styleable.RowStyle_bottomDividerStartPadding, -1.0f);
            this.mBottomDividerEndPadding = (int) obtainStyledAttributes.getDimension(R$styleable.RowStyle_bottomDividerEndPadding, -1.0f);
            this.mActionDividerHeight = (int) obtainStyledAttributes.getDimension(R$styleable.RowStyle_actionDividerHeight, -1.0f);
            this.mSeekBarInlineWidth = (int) obtainStyledAttributes.getDimension(R$styleable.RowStyle_seekBarInlineWidth, -1.0f);
            this.mProgressBarInlineWidth = (int) obtainStyledAttributes.getDimension(R$styleable.RowStyle_progressBarInlineWidth, -1.0f);
            this.mProgressBarStartPadding = (int) obtainStyledAttributes.getDimension(R$styleable.RowStyle_progressBarStartPadding, -1.0f);
            this.mProgressBarEndPadding = (int) obtainStyledAttributes.getDimension(R$styleable.RowStyle_progressBarEndPadding, -1.0f);
            this.mTextActionPadding = (int) obtainStyledAttributes.getDimension(R$styleable.RowStyle_textActionPadding, 10.0f);
            this.mIconSize = (int) obtainStyledAttributes.getDimension(R$styleable.RowStyle_iconSize, -1.0f);
            this.mDisableRecyclerViewItemAnimator = obtainStyledAttributes.getBoolean(R$styleable.RowStyle_disableRecyclerViewItemAnimator, false);
            this.mImageSize = (int) obtainStyledAttributes.getDimension(R$styleable.RowStyle_imageSize, (float) context.getResources().getDimensionPixelSize(R$dimen.abc_slice_small_image_size));
            this.mTintColor = getOptionalColor(obtainStyledAttributes, R$styleable.RowStyle_tintColor);
            this.mTitleColor = getOptionalColor(obtainStyledAttributes, R$styleable.RowStyle_titleColor);
            this.mSubtitleColor = getOptionalColor(obtainStyledAttributes, R$styleable.RowStyle_subtitleColor);
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    public int getTitleItemStartPadding() {
        return this.mTitleItemStartPadding;
    }

    public int getTitleItemEndPadding() {
        return this.mTitleItemEndPadding;
    }

    public int getContentStartPadding() {
        return this.mContentStartPadding;
    }

    public int getContentEndPadding() {
        return this.mContentEndPadding;
    }

    public int getTitleStartPadding() {
        return this.mTitleStartPadding;
    }

    public int getTitleEndPadding() {
        return this.mTitleEndPadding;
    }

    public int getSubContentStartPadding() {
        return this.mSubContentStartPadding;
    }

    public int getSubContentEndPadding() {
        return this.mSubContentEndPadding;
    }

    public int getEndItemStartPadding() {
        return this.mEndItemStartPadding;
    }

    public int getEndItemEndPadding() {
        return this.mEndItemEndPadding;
    }

    public int getBottomDividerStartPadding() {
        return this.mBottomDividerStartPadding;
    }

    public int getBottomDividerEndPadding() {
        return this.mBottomDividerEndPadding;
    }

    public int getActionDividerHeight() {
        return this.mActionDividerHeight;
    }

    public int getSeekBarInlineWidth() {
        return this.mSeekBarInlineWidth;
    }

    public int getProgressBarInlineWidth() {
        return this.mProgressBarInlineWidth;
    }

    public int getProgressBarStartPadding() {
        return this.mProgressBarStartPadding;
    }

    public int getProgressBarEndPadding() {
        return this.mProgressBarEndPadding;
    }

    public int getTextActionPadding() {
        return this.mTextActionPadding;
    }

    public int getIconSize() {
        return this.mIconSize;
    }

    public boolean getDisableRecyclerViewItemAnimator() {
        return this.mDisableRecyclerViewItemAnimator;
    }

    public int getImageSize() {
        return this.mImageSize;
    }

    public int getTintColor() {
        Integer num = this.mTintColor;
        return num != null ? num.intValue() : this.mSliceStyle.getTintColor();
    }

    public int getTitleColor() {
        Integer num = this.mTitleColor;
        return num != null ? num.intValue() : this.mSliceStyle.getTitleColor();
    }

    public int getSubtitleColor() {
        Integer num = this.mSubtitleColor;
        return num != null ? num.intValue() : this.mSliceStyle.getSubtitleColor();
    }

    private static Integer getOptionalColor(TypedArray typedArray, int i) {
        if (typedArray.hasValue(i)) {
            return Integer.valueOf(typedArray.getColor(i, 0));
        }
        return null;
    }
}
