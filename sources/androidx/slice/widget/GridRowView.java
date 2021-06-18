package androidx.slice.widget;

import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.core.content.ContextCompat;
import androidx.slice.CornerDrawable;
import androidx.slice.SliceItem;
import androidx.slice.core.SliceQuery;
import androidx.slice.view.R$dimen;
import androidx.slice.view.R$drawable;
import androidx.slice.view.R$id;
import androidx.slice.view.R$layout;
import androidx.slice.view.R$style;
import androidx.slice.widget.GridContent;
import androidx.slice.widget.SliceView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class GridRowView extends SliceChildView implements View.OnClickListener, View.OnTouchListener {
    private static final int TEXT_LAYOUT = R$layout.abc_slice_secondary_text;
    protected final View mForeground;
    protected GridContent mGridContent;
    private final int mGutter;
    private int mHiddenItemCount;
    protected final int mIconSize;
    protected final int mLargeImageHeight;
    private final int[] mLoc;
    boolean mMaxCellUpdateScheduled;
    protected int mMaxCells;
    private final ViewTreeObserver.OnPreDrawListener mMaxCellsUpdater;
    protected int mRowCount;
    protected int mRowIndex;
    protected final int mSmallImageMinWidth;
    protected final int mSmallImageSize;
    private final int mTextPadding;
    protected final LinearLayout mViewContainer;

    public GridRowView(Context context) {
        this(context, (AttributeSet) null);
    }

    public GridRowView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mLoc = new int[2];
        this.mMaxCells = -1;
        this.mMaxCellsUpdater = new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                GridRowView gridRowView = GridRowView.this;
                gridRowView.mMaxCells = gridRowView.getMaxCells();
                GridRowView.this.populateViews();
                GridRowView.this.getViewTreeObserver().removeOnPreDrawListener(this);
                GridRowView.this.mMaxCellUpdateScheduled = false;
                return true;
            }
        };
        Resources resources = getContext().getResources();
        LinearLayout linearLayout = new LinearLayout(getContext());
        this.mViewContainer = linearLayout;
        linearLayout.setOrientation(0);
        addView(linearLayout, new FrameLayout.LayoutParams(-1, -1));
        linearLayout.setGravity(16);
        this.mIconSize = resources.getDimensionPixelSize(R$dimen.abc_slice_icon_size);
        this.mSmallImageSize = resources.getDimensionPixelSize(R$dimen.abc_slice_small_image_size);
        this.mLargeImageHeight = resources.getDimensionPixelSize(R$dimen.abc_slice_grid_image_only_height);
        this.mSmallImageMinWidth = resources.getDimensionPixelSize(R$dimen.abc_slice_grid_image_min_width);
        this.mGutter = resources.getDimensionPixelSize(R$dimen.abc_slice_grid_gutter);
        this.mTextPadding = resources.getDimensionPixelSize(R$dimen.abc_slice_grid_text_padding);
        View view = new View(getContext());
        this.mForeground = view;
        addView(view, new FrameLayout.LayoutParams(-1, -1));
    }

    public void setInsets(int i, int i2, int i3, int i4) {
        super.setInsets(i, i2, i3, i4);
        this.mViewContainer.setPadding(i, i2 + getExtraTopPadding(), i3, i4 + getExtraBottomPadding());
    }

    /* access modifiers changed from: protected */
    public int getTitleTextLayout() {
        return R$layout.abc_slice_title;
    }

    /* access modifiers changed from: protected */
    public int getExtraTopPadding() {
        SliceStyle sliceStyle;
        GridContent gridContent = this.mGridContent;
        if (gridContent == null || !gridContent.isAllImages() || this.mRowIndex != 0 || (sliceStyle = this.mSliceStyle) == null) {
            return 0;
        }
        return sliceStyle.getGridTopPadding();
    }

    /* access modifiers changed from: protected */
    public int getExtraBottomPadding() {
        SliceStyle sliceStyle;
        GridContent gridContent = this.mGridContent;
        if (gridContent == null || !gridContent.isAllImages()) {
            return 0;
        }
        if ((this.mRowIndex == this.mRowCount - 1 || getMode() == 1) && (sliceStyle = this.mSliceStyle) != null) {
            return sliceStyle.getGridBottomPadding();
        }
        return 0;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int height = this.mGridContent.getHeight(this.mSliceStyle, this.mViewPolicy) + this.mInsetTop + this.mInsetBottom;
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, 1073741824);
        this.mViewContainer.getLayoutParams().height = height;
        super.onMeasure(i, makeMeasureSpec);
    }

    public void setTint(int i) {
        super.setTint(i);
        if (this.mGridContent != null) {
            resetView();
            populateViews();
        }
    }

    public void setSliceItem(SliceContent sliceContent, boolean z, int i, int i2, SliceView.OnSliceActionListener onSliceActionListener) {
        resetView();
        setSliceActionListener(onSliceActionListener);
        this.mRowIndex = i;
        this.mRowCount = i2;
        this.mGridContent = (GridContent) sliceContent;
        if (!scheduleMaxCellsUpdate()) {
            populateViews();
        }
        this.mViewContainer.setPadding(this.mInsetStart, this.mInsetTop + getExtraTopPadding(), this.mInsetEnd, this.mInsetBottom + getExtraBottomPadding());
    }

    /* access modifiers changed from: protected */
    public boolean scheduleMaxCellsUpdate() {
        GridContent gridContent = this.mGridContent;
        if (gridContent == null || !gridContent.isValid()) {
            return true;
        }
        if (getWidth() == 0) {
            this.mMaxCellUpdateScheduled = true;
            getViewTreeObserver().addOnPreDrawListener(this.mMaxCellsUpdater);
            return true;
        }
        this.mMaxCells = getMaxCells();
        return false;
    }

    /* access modifiers changed from: protected */
    public int getMaxCells() {
        int i;
        GridContent gridContent = this.mGridContent;
        if (gridContent == null || !gridContent.isValid() || getWidth() == 0) {
            return -1;
        }
        if (this.mGridContent.getGridContent().size() <= 1) {
            return 1;
        }
        int largestImageMode = this.mGridContent.getLargestImageMode();
        if (largestImageMode == 2) {
            i = this.mLargeImageHeight;
        } else if (largestImageMode != 4) {
            i = this.mSmallImageMinWidth;
        } else {
            i = this.mGridContent.getFirstImageSize(getContext()).x;
        }
        return getWidth() / (i + this.mGutter);
    }

    /* access modifiers changed from: protected */
    public void populateViews() {
        GridContent gridContent = this.mGridContent;
        if (gridContent == null || !gridContent.isValid()) {
            resetView();
        } else if (!scheduleMaxCellsUpdate()) {
            if (this.mGridContent.getLayoutDir() != -1) {
                setLayoutDirection(this.mGridContent.getLayoutDir());
            }
            boolean z = true;
            if (this.mGridContent.getContentIntent() != null) {
                this.mViewContainer.setTag(new Pair(this.mGridContent.getContentIntent(), new EventInfo(getMode(), 3, 1, this.mRowIndex)));
                makeEntireGridClickable(true);
            }
            CharSequence contentDescription = this.mGridContent.getContentDescription();
            if (contentDescription != null) {
                this.mViewContainer.setContentDescription(contentDescription);
            }
            ArrayList<GridContent.CellContent> gridContent2 = this.mGridContent.getGridContent();
            if (this.mGridContent.getLargestImageMode() == 2 || this.mGridContent.getLargestImageMode() == 4) {
                this.mViewContainer.setGravity(48);
            } else {
                this.mViewContainer.setGravity(16);
            }
            int i = this.mMaxCells;
            if (this.mGridContent.getSeeMoreItem() == null) {
                z = false;
            }
            this.mHiddenItemCount = 0;
            for (int i2 = 0; i2 < gridContent2.size(); i2++) {
                if (this.mViewContainer.getChildCount() >= i) {
                    int size = gridContent2.size() - i;
                    this.mHiddenItemCount = size;
                    if (z) {
                        addSeeMoreCount(size);
                        return;
                    }
                    return;
                }
                addCell(gridContent2.get(i2), i2, Math.min(gridContent2.size(), i));
            }
        }
    }

    /* JADX WARNING: type inference failed for: r0v7, types: [android.view.View] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void addSeeMoreCount(int r13) {
        /*
            r12 = this;
            android.widget.LinearLayout r0 = r12.mViewContainer
            int r1 = r0.getChildCount()
            r2 = 1
            int r1 = r1 - r2
            android.view.View r0 = r0.getChildAt(r1)
            android.widget.LinearLayout r1 = r12.mViewContainer
            r1.removeView(r0)
            androidx.slice.widget.GridContent r1 = r12.mGridContent
            androidx.slice.SliceItem r1 = r1.getSeeMoreItem()
            android.widget.LinearLayout r3 = r12.mViewContainer
            int r3 = r3.getChildCount()
            int r4 = r12.mMaxCells
            java.lang.String r5 = r1.getFormat()
            java.lang.String r6 = "slice"
            boolean r5 = r6.equals(r5)
            if (r5 != 0) goto L_0x0037
            java.lang.String r5 = r1.getFormat()
            java.lang.String r6 = "action"
            boolean r5 = r6.equals(r5)
            if (r5 == 0) goto L_0x004e
        L_0x0037:
            androidx.slice.Slice r5 = r1.getSlice()
            java.util.List r5 = r5.getItems()
            int r5 = r5.size()
            if (r5 <= 0) goto L_0x004e
            androidx.slice.widget.GridContent$CellContent r13 = new androidx.slice.widget.GridContent$CellContent
            r13.<init>(r1)
            r12.addCell(r13, r3, r4)
            return
        L_0x004e:
            android.content.Context r5 = r12.getContext()
            android.view.LayoutInflater r5 = android.view.LayoutInflater.from(r5)
            androidx.slice.widget.GridContent r6 = r12.mGridContent
            boolean r6 = r6.isAllImages()
            r7 = -1
            r8 = 0
            if (r6 == 0) goto L_0x009a
            int r6 = androidx.slice.view.R$layout.abc_slice_grid_see_more_overlay
            android.widget.LinearLayout r9 = r12.mViewContainer
            android.view.View r5 = r5.inflate(r6, r9, r8)
            android.widget.FrameLayout r5 = (android.widget.FrameLayout) r5
            android.widget.FrameLayout$LayoutParams r6 = new android.widget.FrameLayout$LayoutParams
            r6.<init>(r7, r7)
            r5.addView(r0, r8, r6)
            int r0 = androidx.slice.view.R$id.text_see_more_count
            android.view.View r0 = r5.findViewById(r0)
            android.widget.TextView r0 = (android.widget.TextView) r0
            int r6 = androidx.slice.view.R$id.overlay_see_more
            android.view.View r6 = r5.findViewById(r6)
            androidx.slice.CornerDrawable r9 = new androidx.slice.CornerDrawable
            android.content.Context r10 = r12.getContext()
            r11 = 16842800(0x1010030, float:2.3693693E-38)
            android.graphics.drawable.Drawable r10 = androidx.slice.widget.SliceViewUtil.getDrawable(r10, r11)
            androidx.slice.widget.SliceStyle r11 = r12.mSliceStyle
            float r11 = r11.getImageCornerRadius()
            r9.<init>(r10, r11)
            r6.setBackground(r9)
            goto L_0x00ce
        L_0x009a:
            int r0 = androidx.slice.view.R$layout.abc_slice_grid_see_more
            android.widget.LinearLayout r6 = r12.mViewContainer
            android.view.View r0 = r5.inflate(r0, r6, r8)
            r5 = r0
            android.widget.LinearLayout r5 = (android.widget.LinearLayout) r5
            int r0 = androidx.slice.view.R$id.text_see_more_count
            android.view.View r0 = r5.findViewById(r0)
            android.widget.TextView r0 = (android.widget.TextView) r0
            int r6 = androidx.slice.view.R$id.text_see_more
            android.view.View r6 = r5.findViewById(r6)
            android.widget.TextView r6 = (android.widget.TextView) r6
            androidx.slice.widget.SliceStyle r9 = r12.mSliceStyle
            if (r9 == 0) goto L_0x00ce
            androidx.slice.widget.RowStyle r10 = r12.mRowStyle
            if (r10 == 0) goto L_0x00ce
            int r9 = r9.getGridTitleSize()
            float r9 = (float) r9
            r6.setTextSize(r8, r9)
            androidx.slice.widget.RowStyle r9 = r12.mRowStyle
            int r9 = r9.getTitleColor()
            r6.setTextColor(r9)
        L_0x00ce:
            android.widget.LinearLayout r6 = r12.mViewContainer
            android.widget.LinearLayout$LayoutParams r9 = new android.widget.LinearLayout$LayoutParams
            r10 = 1065353216(0x3f800000, float:1.0)
            r9.<init>(r8, r7, r10)
            r6.addView(r5, r9)
            android.content.res.Resources r6 = r12.getResources()
            int r7 = androidx.slice.view.R$string.abc_slice_more_content
            java.lang.Object[] r9 = new java.lang.Object[r2]
            java.lang.Integer r13 = java.lang.Integer.valueOf(r13)
            r9[r8] = r13
            java.lang.String r13 = r6.getString(r7, r9)
            r0.setText(r13)
            androidx.slice.widget.EventInfo r13 = new androidx.slice.widget.EventInfo
            int r0 = r12.getMode()
            r6 = 4
            int r7 = r12.mRowIndex
            r13.<init>(r0, r6, r2, r7)
            r0 = 2
            r13.setPosition(r0, r3, r4)
            android.util.Pair r0 = new android.util.Pair
            r0.<init>(r1, r13)
            r5.setTag(r0)
            r12.makeClickable(r5, r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.slice.widget.GridRowView.addSeeMoreCount(int):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00c6, code lost:
        if ("long".equals(r4) != false) goto L_0x00cb;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void addCell(androidx.slice.widget.GridContent.CellContent r29, int r30, int r31) {
        /*
            r28 = this;
            r6 = r28
            r7 = r30
            r8 = r31
            int r0 = r28.getMode()
            r10 = 1
            if (r0 != r10) goto L_0x0017
            androidx.slice.widget.GridContent r0 = r6.mGridContent
            boolean r0 = r0.hasImage()
            if (r0 == 0) goto L_0x0017
            r11 = r10
            goto L_0x0018
        L_0x0017:
            r11 = 2
        L_0x0018:
            android.widget.LinearLayout r12 = new android.widget.LinearLayout
            android.content.Context r0 = r28.getContext()
            r12.<init>(r0)
            r12.setOrientation(r10)
            r12.setGravity(r10)
            java.util.ArrayList r13 = r29.getCellItems()
            androidx.slice.SliceItem r14 = r29.getContentIntent()
            androidx.slice.SliceItem r15 = r29.getPicker()
            androidx.slice.SliceItem r5 = r29.getToggleItem()
            int r0 = r13.size()
            if (r0 != r10) goto L_0x0040
            r16 = r10
            goto L_0x0042
        L_0x0040:
            r16 = 0
        L_0x0042:
            java.lang.String r3 = "text"
            r17 = 0
            if (r16 != 0) goto L_0x0095
            int r0 = r28.getMode()
            if (r0 != r10) goto L_0x0095
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            java.util.Iterator r1 = r13.iterator()
        L_0x0057:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x0071
            java.lang.Object r2 = r1.next()
            androidx.slice.SliceItem r2 = (androidx.slice.SliceItem) r2
            java.lang.String r4 = r2.getFormat()
            boolean r4 = r3.equals(r4)
            if (r4 == 0) goto L_0x0057
            r0.add(r2)
            goto L_0x0057
        L_0x0071:
            java.util.Iterator r1 = r0.iterator()
        L_0x0075:
            int r2 = r0.size()
            if (r2 <= r11) goto L_0x0093
            java.lang.Object r2 = r1.next()
            androidx.slice.SliceItem r2 = (androidx.slice.SliceItem) r2
            java.lang.String r4 = "title"
            java.lang.String r9 = "large"
            java.lang.String[] r4 = new java.lang.String[]{r4, r9}
            boolean r2 = r2.hasAnyHints(r4)
            if (r2 != 0) goto L_0x0075
            r1.remove()
            goto L_0x0075
        L_0x0093:
            r9 = r0
            goto L_0x0097
        L_0x0095:
            r9 = r17
        L_0x0097:
            r2 = r17
            r0 = 0
            r1 = 0
            r4 = 0
            r19 = 0
        L_0x009e:
            int r10 = r13.size()
            if (r4 >= r10) goto L_0x0141
            java.lang.Object r10 = r13.get(r4)
            androidx.slice.SliceItem r10 = (androidx.slice.SliceItem) r10
            r20 = r4
            java.lang.String r4 = r10.getFormat()
            r21 = r5
            int r5 = r6.determinePadding(r2)
            if (r1 >= r11) goto L_0x00e8
            boolean r22 = r3.equals(r4)
            if (r22 != 0) goto L_0x00c9
            r22 = r2
            java.lang.String r2 = "long"
            boolean r2 = r2.equals(r4)
            if (r2 == 0) goto L_0x00ea
            goto L_0x00cb
        L_0x00c9:
            r22 = r2
        L_0x00cb:
            if (r9 == 0) goto L_0x00d4
            boolean r2 = r9.contains(r10)
            if (r2 != 0) goto L_0x00d4
            goto L_0x0124
        L_0x00d4:
            boolean r2 = r6.addTextItem(r10, r12, r5)
            if (r2 == 0) goto L_0x0124
            int r1 = r1 + 1
            r22 = r3
            r2 = r10
            r18 = r20
            r25 = r21
            r19 = 1
            r20 = r9
            goto L_0x0137
        L_0x00e8:
            r22 = r2
        L_0x00ea:
            r2 = 1
            if (r0 >= r2) goto L_0x0124
            java.lang.String r2 = r10.getFormat()
            java.lang.String r4 = "image"
            boolean r2 = r4.equals(r2)
            if (r2 == 0) goto L_0x0124
            androidx.slice.SliceItem r2 = r29.getOverlayItem()
            int r4 = r6.mTintColor
            r23 = r0
            r0 = r28
            r24 = r1
            r1 = r10
            r5 = r22
            r22 = r3
            r3 = r4
            r18 = r20
            r20 = r9
            r9 = 0
            r4 = r12
            r9 = r5
            r25 = r21
            r5 = r16
            boolean r0 = r0.addImageItem(r1, r2, r3, r4, r5)
            if (r0 == 0) goto L_0x0132
            int r0 = r23 + 1
            r2 = r10
            r1 = r24
            r19 = 1
            goto L_0x0137
        L_0x0124:
            r23 = r0
            r24 = r1
            r18 = r20
            r25 = r21
            r20 = r9
            r9 = r22
            r22 = r3
        L_0x0132:
            r2 = r9
            r0 = r23
            r1 = r24
        L_0x0137:
            int r4 = r18 + 1
            r9 = r20
            r3 = r22
            r5 = r25
            goto L_0x009e
        L_0x0141:
            r9 = r2
            r25 = r5
            if (r15 == 0) goto L_0x0171
            java.lang.String r0 = r15.getSubType()
            java.lang.String r1 = "date_picker"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x015c
            int r0 = r6.determinePadding(r9)
            r1 = 1
            boolean r19 = r6.addPickerItem(r15, r12, r0, r1)
            goto L_0x0171
        L_0x015c:
            java.lang.String r0 = r15.getSubType()
            java.lang.String r1 = "time_picker"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x0171
            int r0 = r6.determinePadding(r9)
            r1 = 0
            boolean r19 = r6.addPickerItem(r15, r12, r0, r1)
        L_0x0171:
            r0 = r25
            if (r0 == 0) goto L_0x0189
            androidx.slice.widget.SliceActionView r1 = new androidx.slice.widget.SliceActionView
            android.content.Context r2 = r28.getContext()
            androidx.slice.widget.SliceStyle r3 = r6.mSliceStyle
            androidx.slice.widget.RowStyle r4 = r6.mRowStyle
            r1.<init>(r2, r3, r4)
            r12.addView(r1)
            r22 = r1
            r2 = 1
            goto L_0x018d
        L_0x0189:
            r22 = r17
            r2 = r19
        L_0x018d:
            if (r2 == 0) goto L_0x0200
            java.lang.CharSequence r1 = r29.getContentDescription()
            if (r1 == 0) goto L_0x0198
            r12.setContentDescription(r1)
        L_0x0198:
            android.widget.LinearLayout r1 = r6.mViewContainer
            android.widget.LinearLayout$LayoutParams r2 = new android.widget.LinearLayout$LayoutParams
            r3 = -2
            r4 = 1065353216(0x3f800000, float:1.0)
            r5 = 0
            r2.<init>(r5, r3, r4)
            r1.addView(r12, r2)
            int r1 = r8 + -1
            if (r7 == r1) goto L_0x01b8
            android.view.ViewGroup$LayoutParams r1 = r12.getLayoutParams()
            android.view.ViewGroup$MarginLayoutParams r1 = (android.view.ViewGroup.MarginLayoutParams) r1
            int r2 = r6.mGutter
            r1.setMarginEnd(r2)
            r12.setLayoutParams(r1)
        L_0x01b8:
            if (r14 == 0) goto L_0x01d5
            androidx.slice.widget.EventInfo r1 = new androidx.slice.widget.EventInfo
            int r2 = r28.getMode()
            int r3 = r6.mRowIndex
            r4 = 1
            r1.<init>(r2, r4, r4, r3)
            r2 = 2
            r1.setPosition(r2, r7, r8)
            android.util.Pair r2 = new android.util.Pair
            r2.<init>(r14, r1)
            r12.setTag(r2)
            r6.makeClickable(r12, r4)
        L_0x01d5:
            if (r0 == 0) goto L_0x0200
            androidx.slice.widget.EventInfo r1 = new androidx.slice.widget.EventInfo
            int r2 = r28.getMode()
            r3 = 3
            int r4 = r6.mRowIndex
            r5 = 0
            r1.<init>(r2, r5, r3, r4)
            androidx.slice.core.SliceActionImpl r2 = new androidx.slice.core.SliceActionImpl
            r2.<init>(r0)
            androidx.slice.widget.SliceView$OnSliceActionListener r0 = r6.mObserver
            int r3 = r6.mTintColor
            androidx.slice.widget.SliceActionView$SliceActionLoadingListener r4 = r6.mLoadingListener
            r23 = r2
            r24 = r1
            r25 = r0
            r26 = r3
            r27 = r4
            r22.setAction(r23, r24, r25, r26, r27)
            r0 = 2
            r1.setPosition(r0, r7, r8)
        L_0x0200:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.slice.widget.GridRowView.addCell(androidx.slice.widget.GridContent$CellContent, int, int):void");
    }

    private boolean addTextItem(SliceItem sliceItem, ViewGroup viewGroup, int i) {
        CharSequence charSequence;
        String format = sliceItem.getFormat();
        if (!"text".equals(format) && !"long".equals(format)) {
            return false;
        }
        boolean hasAnyHints = SliceQuery.hasAnyHints(sliceItem, "large", "title");
        TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(hasAnyHints ? getTitleTextLayout() : TEXT_LAYOUT, (ViewGroup) null);
        SliceStyle sliceStyle = this.mSliceStyle;
        if (!(sliceStyle == null || this.mRowStyle == null)) {
            textView.setTextSize(0, (float) (hasAnyHints ? sliceStyle.getGridTitleSize() : sliceStyle.getGridSubtitleSize()));
            textView.setTextColor(hasAnyHints ? this.mRowStyle.getTitleColor() : this.mRowStyle.getSubtitleColor());
        }
        if ("long".equals(format)) {
            charSequence = SliceViewUtil.getTimestampString(getContext(), sliceItem.getLong());
        } else {
            charSequence = sliceItem.getSanitizedText();
        }
        textView.setText(charSequence);
        viewGroup.addView(textView);
        textView.setPadding(0, i, 0, 0);
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean addImageItem(SliceItem sliceItem, SliceItem sliceItem2, int i, ViewGroup viewGroup, boolean z) {
        Drawable loadDrawable;
        LinearLayout.LayoutParams layoutParams;
        int i2;
        String format = sliceItem.getFormat();
        SliceStyle sliceStyle = this.mSliceStyle;
        boolean z2 = sliceStyle != null && sliceStyle.getApplyCornerRadiusToLargeImages();
        if (!"image".equals(format) || sliceItem.getIcon() == null || (loadDrawable = sliceItem.getIcon().loadDrawable(getContext())) == null) {
            return false;
        }
        ImageView imageView = new ImageView(getContext());
        if (z2) {
            imageView.setImageDrawable(new CornerDrawable(loadDrawable, this.mSliceStyle.getImageCornerRadius()));
        } else {
            imageView.setImageDrawable(loadDrawable);
        }
        if (sliceItem.hasHint("raw")) {
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            layoutParams = new LinearLayout.LayoutParams(this.mGridContent.getFirstImageSize(getContext()).x, this.mGridContent.getFirstImageSize(getContext()).y);
        } else if (sliceItem.hasHint("large")) {
            imageView.setScaleType(z2 ? ImageView.ScaleType.FIT_XY : ImageView.ScaleType.CENTER_CROP);
            if (z) {
                i2 = -1;
            } else {
                i2 = this.mLargeImageHeight;
            }
            layoutParams = new LinearLayout.LayoutParams(-1, i2);
        } else {
            boolean z3 = !sliceItem.hasHint("no_tint");
            int i3 = !z3 ? this.mSmallImageSize : this.mIconSize;
            imageView.setScaleType(z3 ? ImageView.ScaleType.CENTER_INSIDE : ImageView.ScaleType.CENTER_CROP);
            layoutParams = new LinearLayout.LayoutParams(i3, i3);
        }
        if (i != -1 && !sliceItem.hasHint("no_tint")) {
            imageView.setColorFilter(i);
        }
        if (sliceItem2 == null || this.mViewContainer.getChildCount() == this.mMaxCells - 1) {
            viewGroup.addView(imageView, layoutParams);
            return true;
        }
        FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(getContext()).inflate(R$layout.abc_slice_grid_text_overlay_image, viewGroup, false);
        frameLayout.addView(imageView, 0, new FrameLayout.LayoutParams(-2, -2));
        ((TextView) frameLayout.findViewById(R$id.text_overlay)).setText(sliceItem2.getText());
        frameLayout.findViewById(R$id.tint_overlay).setBackground(new CornerDrawable(ContextCompat.getDrawable(getContext(), R$drawable.abc_slice_gradient), this.mSliceStyle.getImageCornerRadius()));
        viewGroup.addView(frameLayout, layoutParams);
        return true;
    }

    private boolean addPickerItem(SliceItem sliceItem, ViewGroup viewGroup, int i, boolean z) {
        SliceItem findSubtype = SliceQuery.findSubtype(sliceItem, "long", "millis");
        if (findSubtype == null) {
            return false;
        }
        long j = findSubtype.getLong();
        TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(getTitleTextLayout(), (ViewGroup) null);
        SliceStyle sliceStyle = this.mSliceStyle;
        if (sliceStyle != null) {
            textView.setTextSize(0, (float) sliceStyle.getGridTitleSize());
            textView.setTextColor(this.mSliceStyle.getTitleColor());
        }
        final Date date = new Date(j);
        SliceItem find = SliceQuery.find(sliceItem, "text", "title", (String) null);
        if (find != null) {
            textView.setText(find.getText());
        }
        final int i2 = this.mRowIndex;
        final boolean z2 = z;
        final SliceItem sliceItem2 = sliceItem;
        viewGroup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Calendar instance = Calendar.getInstance();
                instance.setTime(date);
                if (z2) {
                    new DatePickerDialog(GridRowView.this.getContext(), R$style.DialogTheme, new DateSetListener(sliceItem2, i2), instance.get(1), instance.get(2), instance.get(5)).show();
                } else {
                    new TimePickerDialog(GridRowView.this.getContext(), R$style.DialogTheme, new TimeSetListener(sliceItem2, i2), instance.get(11), instance.get(12), false).show();
                }
            }
        });
        viewGroup.setClickable(true);
        int i3 = 16843534;
        if (Build.VERSION.SDK_INT >= 21) {
            i3 = 16843868;
        }
        viewGroup.setBackground(SliceViewUtil.getDrawable(getContext(), i3));
        viewGroup.addView(textView);
        textView.setPadding(0, i, 0, 0);
        return true;
    }

    private class DateSetListener implements DatePickerDialog.OnDateSetListener {
        private final SliceItem mActionItem;
        private final int mRowIndex;

        DateSetListener(SliceItem sliceItem, int i) {
            this.mActionItem = sliceItem;
            this.mRowIndex = i;
        }

        public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
            Calendar instance = Calendar.getInstance();
            instance.set(i, i2, i3);
            Date time = instance.getTime();
            SliceItem sliceItem = this.mActionItem;
            if (sliceItem != null) {
                try {
                    sliceItem.fireAction(GridRowView.this.getContext(), new Intent().addFlags(268435456).putExtra("android.app.slice.extra.RANGE_VALUE", time.getTime()));
                    GridRowView gridRowView = GridRowView.this;
                    if (gridRowView.mObserver != null) {
                        GridRowView.this.mObserver.onSliceAction(new EventInfo(gridRowView.getMode(), 6, 7, this.mRowIndex), this.mActionItem);
                    }
                } catch (PendingIntent.CanceledException e) {
                    Log.e("GridRowView", "PendingIntent for slice cannot be sent", e);
                }
            }
        }
    }

    private class TimeSetListener implements TimePickerDialog.OnTimeSetListener {
        private final SliceItem mActionItem;
        private final int mRowIndex;

        TimeSetListener(SliceItem sliceItem, int i) {
            this.mActionItem = sliceItem;
            this.mRowIndex = i;
        }

        public void onTimeSet(TimePicker timePicker, int i, int i2) {
            Date time = Calendar.getInstance().getTime();
            time.setHours(i);
            time.setMinutes(i2);
            SliceItem sliceItem = this.mActionItem;
            if (sliceItem != null) {
                try {
                    sliceItem.fireAction(GridRowView.this.getContext(), new Intent().addFlags(268435456).putExtra("android.app.slice.extra.RANGE_VALUE", time.getTime()));
                    GridRowView gridRowView = GridRowView.this;
                    if (gridRowView.mObserver != null) {
                        GridRowView.this.mObserver.onSliceAction(new EventInfo(gridRowView.getMode(), 7, 8, this.mRowIndex), this.mActionItem);
                    }
                } catch (PendingIntent.CanceledException e) {
                    Log.e("GridRowView", "PendingIntent for slice cannot be sent", e);
                }
            }
        }
    }

    private int determinePadding(SliceItem sliceItem) {
        SliceStyle sliceStyle;
        if (sliceItem == null) {
            return 0;
        }
        if ("image".equals(sliceItem.getFormat())) {
            return this.mTextPadding;
        }
        if (("text".equals(sliceItem.getFormat()) || "long".equals(sliceItem.getFormat())) && (sliceStyle = this.mSliceStyle) != null) {
            return sliceStyle.getVerticalGridTextPadding();
        }
        return 0;
    }

    private void makeEntireGridClickable(boolean z) {
        Drawable drawable = null;
        this.mViewContainer.setOnTouchListener(z ? this : null);
        this.mViewContainer.setOnClickListener(z ? this : null);
        View view = this.mForeground;
        if (z) {
            drawable = SliceViewUtil.getDrawable(getContext(), 16843534);
        }
        view.setBackground(drawable);
        this.mViewContainer.setClickable(z);
    }

    private void makeClickable(View view, boolean z) {
        Drawable drawable = null;
        view.setOnClickListener(z ? this : null);
        int i = 16843534;
        if (Build.VERSION.SDK_INT >= 21) {
            i = 16843868;
        }
        if (z) {
            drawable = SliceViewUtil.getDrawable(getContext(), i);
        }
        view.setBackground(drawable);
        view.setClickable(z);
    }

    public void onClick(View view) {
        SliceItem find;
        Pair pair = (Pair) view.getTag();
        SliceItem sliceItem = (SliceItem) pair.first;
        EventInfo eventInfo = (EventInfo) pair.second;
        if (sliceItem != null && (find = SliceQuery.find(sliceItem, "action", (String) null, (String) null)) != null) {
            try {
                find.fireAction((Context) null, (Intent) null);
                SliceView.OnSliceActionListener onSliceActionListener = this.mObserver;
                if (onSliceActionListener != null) {
                    onSliceActionListener.onSliceAction(eventInfo, find);
                }
            } catch (PendingIntent.CanceledException e) {
                Log.e("GridRowView", "PendingIntent for slice cannot be sent", e);
            }
        }
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        onForegroundActivated(motionEvent);
        return false;
    }

    private void onForegroundActivated(MotionEvent motionEvent) {
        if (Build.VERSION.SDK_INT >= 21) {
            this.mForeground.getLocationOnScreen(this.mLoc);
            this.mForeground.getBackground().setHotspot((float) ((int) (motionEvent.getRawX() - ((float) this.mLoc[0]))), (float) ((int) (motionEvent.getRawY() - ((float) this.mLoc[1]))));
        }
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.mForeground.setPressed(true);
        } else if (actionMasked == 3 || actionMasked == 1 || actionMasked == 2) {
            this.mForeground.setPressed(false);
        }
    }

    public void resetView() {
        if (this.mMaxCellUpdateScheduled) {
            this.mMaxCellUpdateScheduled = false;
            getViewTreeObserver().removeOnPreDrawListener(this.mMaxCellsUpdater);
        }
        this.mViewContainer.removeAllViews();
        setLayoutDirection(2);
        makeEntireGridClickable(false);
    }

    public int getHiddenItemCount() {
        return this.mHiddenItemCount;
    }
}
