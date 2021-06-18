package androidx.slice.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.SparseArray;
import androidx.slice.SliceItem;
import androidx.slice.view.R$dimen;
import androidx.slice.view.R$styleable;
import java.util.ArrayList;
import java.util.List;

public class SliceStyle {
    private final Context mContext;
    private final int mDefaultRowStyleRes;
    private final boolean mExpandToAvailableHeight;
    private final int mGridAllImagesHeight;
    private final int mGridBigPicMaxHeight;
    private final int mGridBigPicMinHeight;
    private final int mGridBottomPadding;
    private final int mGridImageTextHeight;
    private final int mGridMaxHeight;
    private final int mGridMinHeight;
    private final int mGridRawImageTextHeight;
    private final int mGridSubtitleSize;
    private final int mGridTitleSize;
    private final int mGridTopPadding;
    private final int mHeaderSubtitleSize;
    private final int mHeaderTitleSize;
    private final boolean mHideHeaderRow;
    private final float mImageCornerRadius;
    private final int mListLargeHeight;
    private final int mListMinScrollHeight;
    private final SparseArray<RowStyle> mResourceToRowStyle = new SparseArray<>();
    private final int mRowInlineRangeHeight;
    private final int mRowMaxHeight;
    private final int mRowMinHeight;
    private final int mRowRangeHeight;
    private final int mRowSelectionHeight;
    private final int mRowSingleTextWithRangeHeight;
    private final int mRowSingleTextWithSelectionHeight;
    private RowStyleFactory mRowStyleFactory;
    private final int mRowTextWithRangeHeight;
    private final int mRowTextWithSelectionHeight;
    private final int mSubtitleColor;
    private final int mSubtitleSize;
    private int mTintColor = -1;
    private final int mTitleColor;
    private final int mTitleSize;
    private final int mVerticalGridTextPadding;
    private final int mVerticalHeaderTextPadding;
    private final int mVerticalTextPadding;

    /* JADX INFO: finally extract failed */
    public SliceStyle(Context context, AttributeSet attributeSet, int i, int i2) {
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R$styleable.SliceView, i, i2);
        try {
            int color = obtainStyledAttributes.getColor(R$styleable.SliceView_tintColor, -1);
            if (color == -1) {
                color = this.mTintColor;
            }
            this.mTintColor = color;
            this.mTitleColor = obtainStyledAttributes.getColor(R$styleable.SliceView_titleColor, 0);
            this.mSubtitleColor = obtainStyledAttributes.getColor(R$styleable.SliceView_subtitleColor, 0);
            this.mHeaderTitleSize = (int) obtainStyledAttributes.getDimension(R$styleable.SliceView_headerTitleSize, 0.0f);
            this.mHeaderSubtitleSize = (int) obtainStyledAttributes.getDimension(R$styleable.SliceView_headerSubtitleSize, 0.0f);
            this.mVerticalHeaderTextPadding = (int) obtainStyledAttributes.getDimension(R$styleable.SliceView_headerTextVerticalPadding, 0.0f);
            this.mTitleSize = (int) obtainStyledAttributes.getDimension(R$styleable.SliceView_titleSize, 0.0f);
            this.mSubtitleSize = (int) obtainStyledAttributes.getDimension(R$styleable.SliceView_subtitleSize, 0.0f);
            this.mVerticalTextPadding = (int) obtainStyledAttributes.getDimension(R$styleable.SliceView_textVerticalPadding, 0.0f);
            this.mGridTitleSize = (int) obtainStyledAttributes.getDimension(R$styleable.SliceView_gridTitleSize, 0.0f);
            this.mGridSubtitleSize = (int) obtainStyledAttributes.getDimension(R$styleable.SliceView_gridSubtitleSize, 0.0f);
            this.mVerticalGridTextPadding = (int) obtainStyledAttributes.getDimension(R$styleable.SliceView_gridTextVerticalPadding, (float) context.getResources().getDimensionPixelSize(R$dimen.abc_slice_grid_text_inner_padding));
            this.mGridTopPadding = (int) obtainStyledAttributes.getDimension(R$styleable.SliceView_gridTopPadding, 0.0f);
            this.mGridBottomPadding = (int) obtainStyledAttributes.getDimension(R$styleable.SliceView_gridBottomPadding, 0.0f);
            this.mDefaultRowStyleRes = obtainStyledAttributes.getResourceId(R$styleable.SliceView_rowStyle, 0);
            Resources resources = context.getResources();
            int i3 = R$dimen.abc_slice_row_min_height;
            this.mRowMinHeight = (int) obtainStyledAttributes.getDimension(R$styleable.SliceView_rowMinHeight, (float) resources.getDimensionPixelSize(i3));
            this.mRowMaxHeight = (int) obtainStyledAttributes.getDimension(R$styleable.SliceView_rowMaxHeight, (float) context.getResources().getDimensionPixelSize(R$dimen.abc_slice_row_max_height));
            this.mRowRangeHeight = (int) obtainStyledAttributes.getDimension(R$styleable.SliceView_rowRangeHeight, (float) context.getResources().getDimensionPixelSize(R$dimen.abc_slice_row_range_height));
            this.mRowSingleTextWithRangeHeight = (int) obtainStyledAttributes.getDimension(R$styleable.SliceView_rowRangeSingleTextHeight, (float) context.getResources().getDimensionPixelSize(R$dimen.abc_slice_row_range_single_text_height));
            this.mRowInlineRangeHeight = (int) obtainStyledAttributes.getDimension(R$styleable.SliceView_rowInlineRangeHeight, (float) context.getResources().getDimensionPixelSize(R$dimen.abc_slice_row_range_inline_height));
            this.mExpandToAvailableHeight = obtainStyledAttributes.getBoolean(R$styleable.SliceView_expandToAvailableHeight, false);
            this.mHideHeaderRow = obtainStyledAttributes.getBoolean(R$styleable.SliceView_hideHeaderRow, false);
            this.mContext = context;
            this.mImageCornerRadius = obtainStyledAttributes.getDimension(R$styleable.SliceView_imageCornerRadius, 0.0f);
            obtainStyledAttributes.recycle();
            Resources resources2 = context.getResources();
            this.mRowTextWithRangeHeight = resources2.getDimensionPixelSize(R$dimen.abc_slice_row_range_multi_text_height);
            this.mRowSelectionHeight = resources2.getDimensionPixelSize(R$dimen.abc_slice_row_selection_height);
            this.mRowTextWithSelectionHeight = resources2.getDimensionPixelSize(R$dimen.abc_slice_row_selection_multi_text_height);
            this.mRowSingleTextWithSelectionHeight = resources2.getDimensionPixelSize(R$dimen.abc_slice_row_selection_single_text_height);
            this.mGridBigPicMinHeight = resources2.getDimensionPixelSize(R$dimen.abc_slice_big_pic_min_height);
            this.mGridBigPicMaxHeight = resources2.getDimensionPixelSize(R$dimen.abc_slice_big_pic_max_height);
            this.mGridAllImagesHeight = resources2.getDimensionPixelSize(R$dimen.abc_slice_grid_image_only_height);
            this.mGridImageTextHeight = resources2.getDimensionPixelSize(R$dimen.abc_slice_grid_image_text_height);
            this.mGridRawImageTextHeight = resources2.getDimensionPixelSize(R$dimen.abc_slice_grid_raw_image_text_offset);
            this.mGridMinHeight = resources2.getDimensionPixelSize(R$dimen.abc_slice_grid_min_height);
            this.mGridMaxHeight = resources2.getDimensionPixelSize(R$dimen.abc_slice_grid_max_height);
            this.mListMinScrollHeight = resources2.getDimensionPixelSize(i3);
            this.mListLargeHeight = resources2.getDimensionPixelSize(R$dimen.abc_slice_large_height);
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
            throw th;
        }
    }

    public int getRowMaxHeight() {
        return this.mRowMaxHeight;
    }

    public void setTintColor(int i) {
        this.mTintColor = i;
    }

    public int getTintColor() {
        return this.mTintColor;
    }

    public int getTitleColor() {
        return this.mTitleColor;
    }

    public int getSubtitleColor() {
        return this.mSubtitleColor;
    }

    public int getHeaderTitleSize() {
        return this.mHeaderTitleSize;
    }

    public int getHeaderSubtitleSize() {
        return this.mHeaderSubtitleSize;
    }

    public int getVerticalHeaderTextPadding() {
        return this.mVerticalHeaderTextPadding;
    }

    public int getTitleSize() {
        return this.mTitleSize;
    }

    public int getSubtitleSize() {
        return this.mSubtitleSize;
    }

    public int getVerticalTextPadding() {
        return this.mVerticalTextPadding;
    }

    public int getGridTitleSize() {
        return this.mGridTitleSize;
    }

    public int getGridSubtitleSize() {
        return this.mGridSubtitleSize;
    }

    public int getVerticalGridTextPadding() {
        return this.mVerticalGridTextPadding;
    }

    public int getGridTopPadding() {
        return this.mGridTopPadding;
    }

    public int getGridBottomPadding() {
        return this.mGridBottomPadding;
    }

    public RowStyle getRowStyle(SliceItem sliceItem) {
        RowStyleFactory rowStyleFactory;
        int rowStyleRes;
        int i = this.mDefaultRowStyleRes;
        if (!(sliceItem == null || (rowStyleFactory = this.mRowStyleFactory) == null || (rowStyleRes = rowStyleFactory.getRowStyleRes(sliceItem)) == 0)) {
            i = rowStyleRes;
        }
        if (i == 0) {
            return new RowStyle(this.mContext, this);
        }
        RowStyle rowStyle = this.mResourceToRowStyle.get(i);
        if (rowStyle != null) {
            return rowStyle;
        }
        RowStyle rowStyle2 = new RowStyle(this.mContext, i, this);
        this.mResourceToRowStyle.put(i, rowStyle2);
        return rowStyle2;
    }

    public void setRowStyleFactory(RowStyleFactory rowStyleFactory) {
        this.mRowStyleFactory = rowStyleFactory;
    }

    public int getRowRangeHeight() {
        return this.mRowRangeHeight;
    }

    public int getRowSelectionHeight() {
        return this.mRowSelectionHeight;
    }

    public boolean getExpandToAvailableHeight() {
        return this.mExpandToAvailableHeight;
    }

    public boolean getHideHeaderRow() {
        return this.mHideHeaderRow;
    }

    public boolean getApplyCornerRadiusToLargeImages() {
        return this.mImageCornerRadius > 0.0f;
    }

    public float getImageCornerRadius() {
        return this.mImageCornerRadius;
    }

    public int getRowHeight(RowContent rowContent, SliceViewPolicy sliceViewPolicy) {
        int i;
        int i2;
        int i3;
        int maxSmallHeight = sliceViewPolicy.getMaxSmallHeight() > 0 ? sliceViewPolicy.getMaxSmallHeight() : this.mRowMaxHeight;
        if (rowContent.getRange() == null && rowContent.getSelection() == null && sliceViewPolicy.getMode() != 2) {
            return maxSmallHeight;
        }
        if (rowContent.getRange() != null) {
            if (rowContent.getStartItem() != null) {
                return this.mRowInlineRangeHeight;
            }
            if (rowContent.getLineCount() == 0) {
                i = 0;
            } else if (rowContent.getLineCount() > 1) {
                i = this.mRowTextWithRangeHeight;
            } else {
                i = this.mRowSingleTextWithRangeHeight;
            }
            i2 = this.mRowRangeHeight;
        } else if (rowContent.getSelection() == null) {
            return (rowContent.getLineCount() > 1 || rowContent.getIsHeader()) ? maxSmallHeight : this.mRowMinHeight;
        } else {
            if (rowContent.getLineCount() > 1) {
                i3 = this.mRowTextWithSelectionHeight;
            } else {
                i3 = this.mRowSingleTextWithSelectionHeight;
            }
            i2 = this.mRowSelectionHeight;
        }
        return i + i2;
    }

    public int getGridHeight(GridContent gridContent, SliceViewPolicy sliceViewPolicy) {
        int i;
        int i2 = 0;
        int i3 = 1;
        boolean z = sliceViewPolicy.getMode() == 1;
        if (!gridContent.isValid()) {
            return 0;
        }
        int largestImageMode = gridContent.getLargestImageMode();
        if (!gridContent.isAllImages()) {
            boolean z2 = gridContent.getMaxCellLineCount() > 1;
            boolean hasImage = gridContent.hasImage();
            boolean z3 = largestImageMode == 0 || largestImageMode == 5;
            if (largestImageMode == 4) {
                int i4 = gridContent.getFirstImageSize(this.mContext).y;
                if (z2) {
                    i3 = 2;
                }
                i = i4 + (i3 * this.mGridRawImageTextHeight);
            } else if (!z2 || z) {
                if (z3) {
                    i = this.mGridMinHeight;
                } else {
                    i = this.mGridImageTextHeight;
                }
            } else if (hasImage) {
                i = this.mGridMaxHeight;
            } else {
                i = this.mGridMinHeight;
            }
        } else if (gridContent.getGridContent().size() == 1) {
            if (z) {
                i = this.mGridBigPicMinHeight;
            } else {
                i = this.mGridBigPicMaxHeight;
            }
        } else if (largestImageMode == 0) {
            i = this.mGridMinHeight;
        } else if (largestImageMode == 4) {
            i = gridContent.getFirstImageSize(this.mContext).y;
        } else {
            i = this.mGridAllImagesHeight;
        }
        int i5 = (!gridContent.isAllImages() || gridContent.getRowIndex() != 0) ? 0 : this.mGridTopPadding;
        if (gridContent.isAllImages() && gridContent.getIsLastIndex()) {
            i2 = this.mGridBottomPadding;
        }
        return i + i5 + i2;
    }

    public int getListHeight(ListContent listContent, SliceViewPolicy sliceViewPolicy) {
        int i;
        boolean z = true;
        if (sliceViewPolicy.getMode() == 1) {
            return listContent.getHeader().getHeight(this, sliceViewPolicy);
        }
        int maxHeight = sliceViewPolicy.getMaxHeight();
        boolean isScrollable = sliceViewPolicy.isScrollable();
        int listItemsHeight = getListItemsHeight(listContent.getRowItems(), sliceViewPolicy);
        if (maxHeight > 0) {
            maxHeight = Math.max(listContent.getHeader().getHeight(this, sliceViewPolicy), maxHeight);
        }
        if (maxHeight > 0) {
            i = maxHeight;
        } else {
            i = this.mListLargeHeight;
        }
        if (listItemsHeight - i < this.mListMinScrollHeight) {
            z = false;
        }
        if (z && !getExpandToAvailableHeight()) {
            listItemsHeight = i;
        } else if (maxHeight > 0) {
            listItemsHeight = Math.min(i, listItemsHeight);
        }
        return !isScrollable ? getListItemsHeight(getListItemsForNonScrollingList(listContent, listItemsHeight, sliceViewPolicy).getDisplayedItems(), sliceViewPolicy) : listItemsHeight;
    }

    public int getListItemsHeight(List<SliceContent> list, SliceViewPolicy sliceViewPolicy) {
        if (list == null) {
            return 0;
        }
        int i = 0;
        for (int i2 = 0; i2 < list.size(); i2++) {
            SliceContent sliceContent = list.get(i2);
            if (i2 != 0 || !shouldSkipFirstListItem(list)) {
                i += sliceContent.getHeight(this, sliceViewPolicy);
            }
        }
        return i;
    }

    public DisplayedListItems getListItemsForNonScrollingList(ListContent listContent, int i, SliceViewPolicy sliceViewPolicy) {
        int i2;
        ArrayList arrayList = new ArrayList();
        if (listContent.getRowItems() == null || listContent.getRowItems().size() == 0) {
            return new DisplayedListItems(arrayList, 0);
        }
        boolean shouldSkipFirstListItem = shouldSkipFirstListItem(listContent.getRowItems());
        int size = listContent.getRowItems().size();
        int i3 = 0;
        int i4 = 0;
        while (true) {
            if (i3 >= size) {
                i2 = 0;
                break;
            }
            SliceContent sliceContent = listContent.getRowItems().get(i3);
            if (i3 != 0 || !shouldSkipFirstListItem) {
                int height = sliceContent.getHeight(this, sliceViewPolicy);
                if (i > 0 && i4 + height > i) {
                    i2 = size - i3;
                    break;
                }
                i4 += height;
                arrayList.add(sliceContent);
            }
            i3++;
        }
        int i5 = shouldSkipFirstListItem ? 1 : 2;
        if (listContent.getSeeMoreItem() != null && arrayList.size() >= i5 && i2 > 0) {
            int height2 = i4 + listContent.getSeeMoreItem().getHeight(this, sliceViewPolicy);
            while (height2 > i && arrayList.size() >= i5) {
                int size2 = arrayList.size() - 1;
                height2 -= ((SliceContent) arrayList.get(size2)).getHeight(this, sliceViewPolicy);
                arrayList.remove(size2);
                i2++;
            }
            if (arrayList.size() >= i5) {
                arrayList.add(listContent.getSeeMoreItem());
            }
        }
        if (arrayList.size() == 0) {
            arrayList.add(listContent.getRowItems().get(0));
        }
        return new DisplayedListItems(arrayList, i2);
    }

    public List<SliceContent> getListItemsToDisplay(ListContent listContent) {
        ArrayList<SliceContent> rowItems = listContent.getRowItems();
        return (rowItems.size() <= 0 || !shouldSkipFirstListItem(rowItems)) ? rowItems : rowItems.subList(1, rowItems.size());
    }

    private boolean shouldSkipFirstListItem(List<SliceContent> list) {
        if (!getHideHeaderRow() || list.size() <= 1 || !(list.get(0) instanceof RowContent) || !((RowContent) list.get(0)).getIsHeader()) {
            return false;
        }
        return true;
    }
}
