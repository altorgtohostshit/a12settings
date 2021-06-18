package androidx.slice.widget;

import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.slice.SliceItem;
import androidx.slice.core.SliceAction;
import androidx.slice.core.SliceActionImpl;
import androidx.slice.core.SliceQuery;
import androidx.slice.view.R$dimen;
import androidx.slice.view.R$id;
import androidx.slice.view.R$layout;
import androidx.slice.view.R$plurals;
import androidx.slice.view.R$style;
import androidx.slice.widget.SliceActionView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RowView extends SliceChildView implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static final boolean sCanSpecifyLargerRangeBarHeight = (Build.VERSION.SDK_INT >= 23);
    private final View mActionDivider;
    private final ProgressBar mActionSpinner;
    private final ArrayMap<SliceActionImpl, SliceActionView> mActions = new ArrayMap<>();
    private boolean mAllowTwoLines;
    private final View mBottomDivider;
    private final LinearLayout mContent;
    private final LinearLayout mEndContainer;
    Handler mHandler;
    private List<SliceAction> mHeaderActions;
    private int mIconSize = getContext().getResources().getDimensionPixelSize(R$dimen.abc_slice_icon_size);
    private int mImageSize = getContext().getResources().getDimensionPixelSize(R$dimen.abc_slice_small_image_size);
    private boolean mIsHeader;
    boolean mIsRangeSliding;
    private boolean mIsStarRating;
    long mLastSentRangeUpdate;
    private final TextView mLastUpdatedText;
    protected Set<SliceItem> mLoadingActions = new HashSet();
    private int mMeasuredRangeHeight;
    private final TextView mPrimaryText;
    private View mRangeBar;
    boolean mRangeHasPendingUpdate;
    private SliceItem mRangeItem;
    int mRangeMaxValue;
    int mRangeMinValue;
    Runnable mRangeUpdater = new Runnable() {
        public void run() {
            RowView.this.sendSliderValue();
            RowView.this.mRangeUpdaterRunning = false;
        }
    };
    boolean mRangeUpdaterRunning;
    int mRangeValue;
    private final RatingBar.OnRatingBarChangeListener mRatingBarChangeListener = new RatingBar.OnRatingBarChangeListener() {
        public void onRatingChanged(RatingBar ratingBar, float f, boolean z) {
            RowView rowView = RowView.this;
            rowView.mRangeValue = Math.round(f + ((float) rowView.mRangeMinValue));
            long currentTimeMillis = System.currentTimeMillis();
            RowView rowView2 = RowView.this;
            long j = rowView2.mLastSentRangeUpdate;
            if (j != 0 && currentTimeMillis - j > 200) {
                rowView2.mRangeUpdaterRunning = false;
                rowView2.mHandler.removeCallbacks(rowView2.mRangeUpdater);
                RowView.this.sendSliderValue();
            } else if (!rowView2.mRangeUpdaterRunning) {
                rowView2.mRangeUpdaterRunning = true;
                rowView2.mHandler.postDelayed(rowView2.mRangeUpdater, 200);
            }
        }
    };
    private final LinearLayout mRootView;
    private SliceActionImpl mRowAction;
    RowContent mRowContent;
    int mRowIndex;
    private final TextView mSecondaryText;
    private View mSeeMoreView;
    private final SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            RowView rowView = RowView.this;
            rowView.mRangeValue = i + rowView.mRangeMinValue;
            long currentTimeMillis = System.currentTimeMillis();
            RowView rowView2 = RowView.this;
            long j = rowView2.mLastSentRangeUpdate;
            if (j != 0 && currentTimeMillis - j > 200) {
                rowView2.mRangeUpdaterRunning = false;
                rowView2.mHandler.removeCallbacks(rowView2.mRangeUpdater);
                RowView.this.sendSliderValue();
            } else if (!rowView2.mRangeUpdaterRunning) {
                rowView2.mRangeUpdaterRunning = true;
                rowView2.mHandler.postDelayed(rowView2.mRangeUpdater, 200);
            }
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
            RowView.this.mIsRangeSliding = true;
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            RowView rowView = RowView.this;
            rowView.mIsRangeSliding = false;
            if (rowView.mRangeUpdaterRunning || rowView.mRangeHasPendingUpdate) {
                rowView.mRangeUpdaterRunning = false;
                rowView.mRangeHasPendingUpdate = false;
                rowView.mHandler.removeCallbacks(rowView.mRangeUpdater);
                RowView rowView2 = RowView.this;
                int progress = seekBar.getProgress();
                RowView rowView3 = RowView.this;
                rowView2.mRangeValue = progress + rowView3.mRangeMinValue;
                rowView3.sendSliderValue();
            }
        }
    };
    private SliceItem mSelectionItem;
    private ArrayList<String> mSelectionOptionKeys;
    private ArrayList<CharSequence> mSelectionOptionValues;
    private Spinner mSelectionSpinner;
    boolean mShowActionSpinner;
    private final LinearLayout mStartContainer;
    private SliceItem mStartItem;
    private final LinearLayout mSubContent;
    private final ArrayMap<SliceActionImpl, SliceActionView> mToggles = new ArrayMap<>();

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public RowView(Context context) {
        super(context);
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R$layout.abc_slice_small_template, this, false);
        this.mRootView = linearLayout;
        addView(linearLayout);
        this.mStartContainer = (LinearLayout) findViewById(R$id.icon_frame);
        LinearLayout linearLayout2 = (LinearLayout) findViewById(16908290);
        this.mContent = linearLayout2;
        this.mSubContent = (LinearLayout) findViewById(R$id.subcontent);
        this.mPrimaryText = (TextView) findViewById(16908310);
        this.mSecondaryText = (TextView) findViewById(16908304);
        this.mLastUpdatedText = (TextView) findViewById(R$id.last_updated);
        this.mBottomDivider = findViewById(R$id.bottom_divider);
        this.mActionDivider = findViewById(R$id.action_divider);
        ProgressBar progressBar = (ProgressBar) findViewById(R$id.action_sent_indicator);
        this.mActionSpinner = progressBar;
        SliceViewUtil.tintIndeterminateProgressBar(getContext(), progressBar);
        this.mEndContainer = (LinearLayout) findViewById(16908312);
        ViewCompat.setImportantForAccessibility(this, 2);
        ViewCompat.setImportantForAccessibility(linearLayout2, 2);
    }

    public void setStyle(SliceStyle sliceStyle, RowStyle rowStyle) {
        super.setStyle(sliceStyle, rowStyle);
        applyRowStyle();
    }

    private void applyRowStyle() {
        RowStyle rowStyle;
        if (this.mSliceStyle != null && (rowStyle = this.mRowStyle) != null) {
            setViewSidePaddings(this.mStartContainer, rowStyle.getTitleItemStartPadding(), this.mRowStyle.getTitleItemEndPadding());
            setViewSidePaddings(this.mContent, this.mRowStyle.getContentStartPadding(), this.mRowStyle.getContentEndPadding());
            setViewSidePaddings(this.mPrimaryText, this.mRowStyle.getTitleStartPadding(), this.mRowStyle.getTitleEndPadding());
            setViewSidePaddings(this.mSubContent, this.mRowStyle.getSubContentStartPadding(), this.mRowStyle.getSubContentEndPadding());
            setViewSidePaddings(this.mEndContainer, this.mRowStyle.getEndItemStartPadding(), this.mRowStyle.getEndItemEndPadding());
            setViewSideMargins(this.mBottomDivider, this.mRowStyle.getBottomDividerStartPadding(), this.mRowStyle.getBottomDividerEndPadding());
            setViewHeight(this.mActionDivider, this.mRowStyle.getActionDividerHeight());
            if (this.mRowStyle.getTintColor() != -1) {
                setTint(this.mRowStyle.getTintColor());
            }
        }
    }

    private void setViewSidePaddings(View view, int i, int i2) {
        boolean z = i < 0 && i2 < 0;
        if (view != null && !z) {
            if (i < 0) {
                i = view.getPaddingStart();
            }
            int paddingTop = view.getPaddingTop();
            if (i2 < 0) {
                i2 = view.getPaddingEnd();
            }
            view.setPaddingRelative(i, paddingTop, i2, view.getPaddingBottom());
        }
    }

    private void setViewSideMargins(View view, int i, int i2) {
        boolean z = i < 0 && i2 < 0;
        if (view != null && !z) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            if (i >= 0) {
                marginLayoutParams.setMarginStart(i);
            }
            if (i2 >= 0) {
                marginLayoutParams.setMarginEnd(i2);
            }
            view.setLayoutParams(marginLayoutParams);
        }
    }

    private void setViewHeight(View view, int i) {
        if (view != null && i >= 0) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.height = i;
            view.setLayoutParams(layoutParams);
        }
    }

    private void setViewWidth(View view, int i) {
        if (view != null && i >= 0) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = i;
            view.setLayoutParams(layoutParams);
        }
    }

    public void setInsets(int i, int i2, int i3, int i4) {
        super.setInsets(i, i2, i3, i4);
        setPadding(i, i2, i3, i4);
    }

    /* access modifiers changed from: protected */
    public SliceItem getPrimaryActionItem() {
        RowContent rowContent = this.mRowContent;
        if (rowContent != null) {
            return rowContent.getPrimaryAction();
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public String getPrimaryActionKey() {
        SliceItem primaryAction;
        RowContent rowContent = this.mRowContent;
        if (rowContent == null || (primaryAction = rowContent.getPrimaryAction()) == null || primaryAction == this.mStartItem) {
            return null;
        }
        SliceActionImpl sliceActionImpl = new SliceActionImpl(primaryAction);
        this.mRowAction = sliceActionImpl;
        return sliceActionImpl.getKey();
    }

    /* access modifiers changed from: protected */
    public List<String> getEndItemKeys() {
        ArrayList arrayList = new ArrayList();
        RowContent rowContent = this.mRowContent;
        if (rowContent != null) {
            List<SliceItem> endItems = rowContent.getEndItems();
            for (int i = 0; i < endItems.size(); i++) {
                SliceActionImpl sliceActionImpl = new SliceActionImpl(endItems.get(i));
                if (sliceActionImpl.getKey() != null) {
                    arrayList.add(sliceActionImpl.getKey());
                }
            }
        }
        return arrayList;
    }

    private int getRowContentHeight() {
        int height = this.mRowContent.getHeight(this.mSliceStyle, this.mViewPolicy);
        if (this.mRangeBar != null && this.mStartItem == null) {
            height -= this.mSliceStyle.getRowRangeHeight();
        }
        return this.mSelectionSpinner != null ? height - this.mSliceStyle.getRowSelectionHeight() : height;
    }

    public void setTint(int i) {
        super.setTint(i);
        if (this.mRowContent != null) {
            populateViews(true);
        }
    }

    public void setSliceActions(List<SliceAction> list) {
        this.mHeaderActions = list;
        if (this.mRowContent != null) {
            updateEndItems();
        }
    }

    public void setShowLastUpdated(boolean z) {
        super.setShowLastUpdated(z);
        if (this.mRowContent != null) {
            populateViews(true);
        }
    }

    public void setAllowTwoLines(boolean z) {
        this.mAllowTwoLines = z;
        if (this.mRowContent != null) {
            populateViews(true);
        }
    }

    private void measureChildWithExactHeight(View view, int i, int i2) {
        measureChild(view, i, View.MeasureSpec.makeMeasureSpec(i2 + this.mInsetTop + this.mInsetBottom, 1073741824));
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int i3;
        int rowContentHeight = getRowContentHeight();
        if (rowContentHeight != 0) {
            this.mRootView.setVisibility(0);
            measureChildWithExactHeight(this.mRootView, i, rowContentHeight);
            i3 = this.mRootView.getMeasuredWidth();
        } else {
            this.mRootView.setVisibility(8);
            i3 = 0;
        }
        View view = this.mRangeBar;
        if (view == null || this.mStartItem != null) {
            Spinner spinner = this.mSelectionSpinner;
            if (spinner != null) {
                measureChildWithExactHeight(spinner, i, this.mSliceStyle.getRowSelectionHeight());
                i3 = Math.max(i3, this.mSelectionSpinner.getMeasuredWidth());
            }
        } else {
            if (sCanSpecifyLargerRangeBarHeight) {
                measureChildWithExactHeight(view, i, this.mSliceStyle.getRowRangeHeight());
            } else {
                measureChild(view, i, View.MeasureSpec.makeMeasureSpec(0, 0));
            }
            this.mMeasuredRangeHeight = this.mRangeBar.getMeasuredHeight();
            i3 = Math.max(i3, this.mRangeBar.getMeasuredWidth());
        }
        int max = Math.max(i3 + this.mInsetStart + this.mInsetEnd, getSuggestedMinimumWidth());
        RowContent rowContent = this.mRowContent;
        setMeasuredDimension(FrameLayout.resolveSizeAndState(max, i, 0), (rowContent != null ? rowContent.getHeight(this.mSliceStyle, this.mViewPolicy) : 0) + this.mInsetTop + this.mInsetBottom);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int paddingLeft = getPaddingLeft();
        LinearLayout linearLayout = this.mRootView;
        linearLayout.layout(paddingLeft, this.mInsetTop, linearLayout.getMeasuredWidth() + paddingLeft, getRowContentHeight() + this.mInsetTop);
        if (this.mRangeBar != null && this.mStartItem == null) {
            int rowContentHeight = getRowContentHeight() + ((this.mSliceStyle.getRowRangeHeight() - this.mMeasuredRangeHeight) / 2) + this.mInsetTop;
            View view = this.mRangeBar;
            view.layout(paddingLeft, rowContentHeight, view.getMeasuredWidth() + paddingLeft, this.mMeasuredRangeHeight + rowContentHeight);
        } else if (this.mSelectionSpinner != null) {
            int rowContentHeight2 = getRowContentHeight() + this.mInsetTop;
            Spinner spinner = this.mSelectionSpinner;
            spinner.layout(paddingLeft, rowContentHeight2, spinner.getMeasuredWidth() + paddingLeft, this.mSelectionSpinner.getMeasuredHeight() + rowContentHeight2);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0053, code lost:
        if (r2 != false) goto L_0x0057;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setSliceItem(androidx.slice.widget.SliceContent r5, boolean r6, int r7, int r8, androidx.slice.widget.SliceView.OnSliceActionListener r9) {
        /*
            r4 = this;
            r4.setSliceActionListener(r9)
            r8 = 0
            if (r5 == 0) goto L_0x0056
            androidx.slice.widget.RowContent r9 = r4.mRowContent
            if (r9 == 0) goto L_0x0056
            boolean r9 = r9.isValid()
            if (r9 == 0) goto L_0x0056
            androidx.slice.widget.RowContent r9 = r4.mRowContent
            if (r9 == 0) goto L_0x001e
            androidx.slice.SliceStructure r0 = new androidx.slice.SliceStructure
            androidx.slice.SliceItem r9 = r9.getSliceItem()
            r0.<init>((androidx.slice.SliceItem) r9)
            goto L_0x001f
        L_0x001e:
            r0 = 0
        L_0x001f:
            androidx.slice.SliceStructure r9 = new androidx.slice.SliceStructure
            androidx.slice.SliceItem r1 = r5.getSliceItem()
            androidx.slice.Slice r1 = r1.getSlice()
            r9.<init>((androidx.slice.Slice) r1)
            r1 = 1
            if (r0 == 0) goto L_0x0037
            boolean r2 = r0.equals(r9)
            if (r2 == 0) goto L_0x0037
            r2 = r1
            goto L_0x0038
        L_0x0037:
            r2 = r8
        L_0x0038:
            if (r0 == 0) goto L_0x0050
            android.net.Uri r3 = r0.getUri()
            if (r3 == 0) goto L_0x0050
            android.net.Uri r0 = r0.getUri()
            android.net.Uri r9 = r9.getUri()
            boolean r9 = r0.equals(r9)
            if (r9 == 0) goto L_0x0050
            r9 = r1
            goto L_0x0051
        L_0x0050:
            r9 = r8
        L_0x0051:
            if (r9 == 0) goto L_0x0056
            if (r2 == 0) goto L_0x0056
            goto L_0x0057
        L_0x0056:
            r1 = r8
        L_0x0057:
            r4.mShowActionSpinner = r8
            r4.mIsHeader = r6
            androidx.slice.widget.RowContent r5 = (androidx.slice.widget.RowContent) r5
            r4.mRowContent = r5
            r4.mRowIndex = r7
            r4.populateViews(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.slice.widget.RowView.setSliceItem(androidx.slice.widget.SliceContent, boolean, int, int, androidx.slice.widget.SliceView$OnSliceActionListener):void");
    }

    private void populateViews(boolean z) {
        int i;
        boolean z2 = true;
        boolean z3 = z && this.mIsRangeSliding;
        if (!z3) {
            resetViewState();
        }
        char c = 65535;
        if (this.mRowContent.getLayoutDir() != -1) {
            setLayoutDirection(this.mRowContent.getLayoutDir());
        }
        if (this.mRowContent.isDefaultSeeMore()) {
            showSeeMore();
            return;
        }
        CharSequence contentDescription = this.mRowContent.getContentDescription();
        if (contentDescription != null) {
            this.mContent.setContentDescription(contentDescription);
        }
        SliceItem startItem = this.mRowContent.getStartItem();
        this.mStartItem = startItem;
        boolean z4 = startItem != null && (!this.mRowContent.getIsHeader() || this.mRowContent.hasTitleItems());
        if (z4) {
            z4 = addItem(this.mStartItem, this.mTintColor, true);
        }
        int i2 = 8;
        this.mStartContainer.setVisibility(z4 ? 0 : 8);
        SliceItem titleItem = this.mRowContent.getTitleItem();
        if (titleItem != null) {
            this.mPrimaryText.setText(titleItem.getSanitizedText());
        }
        SliceStyle sliceStyle = this.mSliceStyle;
        if (sliceStyle != null) {
            TextView textView = this.mPrimaryText;
            if (this.mIsHeader) {
                i = sliceStyle.getHeaderTitleSize();
            } else {
                i = sliceStyle.getTitleSize();
            }
            textView.setTextSize(0, (float) i);
            this.mPrimaryText.setTextColor(this.mRowStyle.getTitleColor());
        }
        this.mPrimaryText.setVisibility(titleItem != null ? 0 : 8);
        addSubtitle(titleItem != null);
        View view = this.mBottomDivider;
        if (this.mRowContent.hasBottomDivider()) {
            i2 = 0;
        }
        view.setVisibility(i2);
        SliceItem primaryAction = this.mRowContent.getPrimaryAction();
        if (!(primaryAction == null || primaryAction == this.mStartItem)) {
            SliceActionImpl sliceActionImpl = new SliceActionImpl(primaryAction);
            this.mRowAction = sliceActionImpl;
            if (sliceActionImpl.getSubtype() != null) {
                String subtype = this.mRowAction.getSubtype();
                subtype.hashCode();
                switch (subtype.hashCode()) {
                    case -868304044:
                        if (subtype.equals("toggle")) {
                            c = 0;
                            break;
                        }
                        break;
                    case 759128640:
                        if (subtype.equals("time_picker")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 1250407999:
                        if (subtype.equals("date_picker")) {
                            c = 2;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        addAction(this.mRowAction, this.mTintColor, this.mEndContainer, false);
                        setViewClickable(this.mRootView, true);
                        return;
                    case 1:
                        setViewClickable(this.mRootView, true);
                        return;
                    case 2:
                        setViewClickable(this.mRootView, true);
                        return;
                }
            }
        }
        SliceItem range = this.mRowContent.getRange();
        if (range != null) {
            if (this.mRowAction != null) {
                setViewClickable(this.mRootView, true);
            }
            this.mRangeItem = range;
            SliceItem findSubtype = SliceQuery.findSubtype(range, "int", "range_mode");
            if (findSubtype != null) {
                if (findSubtype.getInt() != 2) {
                    z2 = false;
                }
                this.mIsStarRating = z2;
            }
            if (!z3) {
                initRangeBar();
                addRangeView();
            }
            if (this.mStartItem == null) {
                return;
            }
        }
        SliceItem selection = this.mRowContent.getSelection();
        if (selection != null) {
            this.mSelectionItem = selection;
            addSelection(selection);
            return;
        }
        updateEndItems();
        updateActionSpinner();
    }

    /* JADX WARNING: Removed duplicated region for block: B:92:0x014a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateEndItems() {
        /*
            r11 = this;
            androidx.slice.widget.RowContent r0 = r11.mRowContent
            if (r0 == 0) goto L_0x014e
            androidx.slice.SliceItem r0 = r0.getRange()
            if (r0 == 0) goto L_0x0010
            androidx.slice.SliceItem r0 = r11.mStartItem
            if (r0 != 0) goto L_0x0010
            goto L_0x014e
        L_0x0010:
            android.widget.LinearLayout r0 = r11.mEndContainer
            r0.removeAllViews()
            androidx.slice.widget.RowContent r0 = r11.mRowContent
            java.util.List r0 = r0.getEndItems()
            java.util.List<androidx.slice.core.SliceAction> r1 = r11.mHeaderActions
            if (r1 == 0) goto L_0x0020
            r0 = r1
        L_0x0020:
            androidx.slice.widget.RowContent r1 = r11.mRowContent
            boolean r1 = r1.getIsHeader()
            if (r1 == 0) goto L_0x003f
            androidx.slice.SliceItem r1 = r11.mStartItem
            if (r1 == 0) goto L_0x003f
            boolean r1 = r0.isEmpty()
            if (r1 == 0) goto L_0x003f
            androidx.slice.widget.RowContent r1 = r11.mRowContent
            boolean r1 = r1.hasTitleItems()
            if (r1 != 0) goto L_0x003f
            androidx.slice.SliceItem r1 = r11.mStartItem
            r0.add(r1)
        L_0x003f:
            r1 = 0
            r2 = 0
            r3 = r2
            r4 = r3
            r5 = r4
            r6 = r5
        L_0x0045:
            int r7 = r0.size()
            java.lang.String r8 = "action"
            r9 = 1
            if (r3 >= r7) goto L_0x00a8
            java.lang.Object r7 = r0.get(r3)
            boolean r7 = r7 instanceof androidx.slice.SliceItem
            if (r7 == 0) goto L_0x005d
            java.lang.Object r7 = r0.get(r3)
            androidx.slice.SliceItem r7 = (androidx.slice.SliceItem) r7
            goto L_0x0067
        L_0x005d:
            java.lang.Object r7 = r0.get(r3)
            androidx.slice.core.SliceActionImpl r7 = (androidx.slice.core.SliceActionImpl) r7
            androidx.slice.SliceItem r7 = r7.getSliceItem()
        L_0x0067:
            r10 = 3
            if (r4 >= r10) goto L_0x00a5
            int r10 = r11.mTintColor
            boolean r10 = r11.addItem(r7, r10, r2)
            if (r10 == 0) goto L_0x00a5
            if (r1 != 0) goto L_0x007b
            androidx.slice.SliceItem r10 = androidx.slice.core.SliceQuery.find((androidx.slice.SliceItem) r7, (java.lang.String) r8)
            if (r10 == 0) goto L_0x007b
            r1 = r7
        L_0x007b:
            int r4 = r4 + 1
            if (r4 != r9) goto L_0x00a5
            android.util.ArrayMap<androidx.slice.core.SliceActionImpl, androidx.slice.widget.SliceActionView> r5 = r11.mToggles
            boolean r5 = r5.isEmpty()
            if (r5 != 0) goto L_0x0095
            androidx.slice.Slice r5 = r7.getSlice()
            java.lang.String r6 = "image"
            androidx.slice.SliceItem r5 = androidx.slice.core.SliceQuery.find((androidx.slice.Slice) r5, (java.lang.String) r6)
            if (r5 != 0) goto L_0x0095
            r5 = r9
            goto L_0x0096
        L_0x0095:
            r5 = r2
        L_0x0096:
            int r6 = r0.size()
            if (r6 != r9) goto L_0x00a4
            androidx.slice.SliceItem r6 = androidx.slice.core.SliceQuery.find((androidx.slice.SliceItem) r7, (java.lang.String) r8)
            if (r6 == 0) goto L_0x00a4
            r6 = r9
            goto L_0x00a5
        L_0x00a4:
            r6 = r2
        L_0x00a5:
            int r3 = r3 + 1
            goto L_0x0045
        L_0x00a8:
            android.widget.LinearLayout r0 = r11.mEndContainer
            r3 = 8
            if (r4 <= 0) goto L_0x00b0
            r7 = r2
            goto L_0x00b1
        L_0x00b0:
            r7 = r3
        L_0x00b1:
            r0.setVisibility(r7)
            android.view.View r0 = r11.mActionDivider
            androidx.slice.core.SliceActionImpl r7 = r11.mRowAction
            if (r7 == 0) goto L_0x00c7
            if (r5 != 0) goto L_0x00c6
            androidx.slice.widget.RowContent r5 = r11.mRowContent
            boolean r5 = r5.hasActionDivider()
            if (r5 == 0) goto L_0x00c7
            if (r6 == 0) goto L_0x00c7
        L_0x00c6:
            r3 = r2
        L_0x00c7:
            r0.setVisibility(r3)
            androidx.slice.SliceItem r0 = r11.mStartItem
            if (r0 == 0) goto L_0x00d6
            androidx.slice.SliceItem r0 = androidx.slice.core.SliceQuery.find((androidx.slice.SliceItem) r0, (java.lang.String) r8)
            if (r0 == 0) goto L_0x00d6
            r0 = r9
            goto L_0x00d7
        L_0x00d6:
            r0 = r2
        L_0x00d7:
            if (r1 == 0) goto L_0x00db
            r1 = r9
            goto L_0x00dc
        L_0x00db:
            r1 = r2
        L_0x00dc:
            androidx.slice.core.SliceActionImpl r3 = r11.mRowAction
            if (r3 == 0) goto L_0x00e6
            android.widget.LinearLayout r0 = r11.mRootView
            r11.setViewClickable(r0, r9)
            goto L_0x012c
        L_0x00e6:
            if (r1 == r0) goto L_0x012c
            if (r4 == r9) goto L_0x00ec
            if (r0 == 0) goto L_0x012c
        L_0x00ec:
            android.util.ArrayMap<androidx.slice.core.SliceActionImpl, androidx.slice.widget.SliceActionView> r0 = r11.mToggles
            boolean r0 = r0.isEmpty()
            if (r0 != 0) goto L_0x0107
            android.util.ArrayMap<androidx.slice.core.SliceActionImpl, androidx.slice.widget.SliceActionView> r0 = r11.mToggles
            java.util.Set r0 = r0.keySet()
            java.util.Iterator r0 = r0.iterator()
            java.lang.Object r0 = r0.next()
            androidx.slice.core.SliceActionImpl r0 = (androidx.slice.core.SliceActionImpl) r0
            r11.mRowAction = r0
            goto L_0x0125
        L_0x0107:
            android.util.ArrayMap<androidx.slice.core.SliceActionImpl, androidx.slice.widget.SliceActionView> r0 = r11.mActions
            boolean r0 = r0.isEmpty()
            if (r0 != 0) goto L_0x0125
            android.util.ArrayMap<androidx.slice.core.SliceActionImpl, androidx.slice.widget.SliceActionView> r0 = r11.mActions
            int r0 = r0.size()
            if (r0 != r9) goto L_0x0125
            android.util.ArrayMap<androidx.slice.core.SliceActionImpl, androidx.slice.widget.SliceActionView> r0 = r11.mActions
            java.lang.Object r0 = r0.valueAt(r2)
            androidx.slice.widget.SliceActionView r0 = (androidx.slice.widget.SliceActionView) r0
            androidx.slice.core.SliceActionImpl r0 = r0.getAction()
            r11.mRowAction = r0
        L_0x0125:
            android.widget.LinearLayout r0 = r11.mRootView
            r11.setViewClickable(r0, r9)
            r0 = r9
            goto L_0x012d
        L_0x012c:
            r0 = r2
        L_0x012d:
            androidx.slice.core.SliceActionImpl r1 = r11.mRowAction
            if (r1 == 0) goto L_0x0141
            if (r0 != 0) goto L_0x0141
            java.util.Set<androidx.slice.SliceItem> r0 = r11.mLoadingActions
            androidx.slice.SliceItem r1 = r1.getSliceItem()
            boolean r0 = r0.contains(r1)
            if (r0 == 0) goto L_0x0141
            r11.mShowActionSpinner = r9
        L_0x0141:
            android.widget.LinearLayout r11 = r11.mRootView
            boolean r0 = r11.isClickable()
            if (r0 == 0) goto L_0x014a
            goto L_0x014b
        L_0x014a:
            r2 = 2
        L_0x014b:
            androidx.core.view.ViewCompat.setImportantForAccessibility(r11, r2)
        L_0x014e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.slice.widget.RowView.updateEndItems():void");
    }

    public void setLastUpdated(long j) {
        super.setLastUpdated(j);
        RowContent rowContent = this.mRowContent;
        if (rowContent != null) {
            addSubtitle(rowContent.getTitleItem() != null && TextUtils.isEmpty(this.mRowContent.getTitleItem().getSanitizedText()));
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x004a  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0064  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00a4  */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00fd  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00ff  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x0107  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x0118  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x011a  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x012e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void addSubtitle(boolean r10) {
        /*
            r9 = this;
            androidx.slice.widget.RowContent r0 = r9.mRowContent
            if (r0 == 0) goto L_0x0141
            androidx.slice.SliceItem r0 = r0.getRange()
            if (r0 == 0) goto L_0x0010
            androidx.slice.SliceItem r0 = r9.mStartItem
            if (r0 == 0) goto L_0x0010
            goto L_0x0141
        L_0x0010:
            int r0 = r9.getMode()
            r1 = 1
            if (r0 != r1) goto L_0x001e
            androidx.slice.widget.RowContent r0 = r9.mRowContent
            androidx.slice.SliceItem r0 = r0.getSummaryItem()
            goto L_0x0024
        L_0x001e:
            androidx.slice.widget.RowContent r0 = r9.mRowContent
            androidx.slice.SliceItem r0 = r0.getSubtitleItem()
        L_0x0024:
            boolean r2 = r9.mShowLastUpdated
            r3 = 0
            r4 = 0
            if (r2 == 0) goto L_0x0047
            long r5 = r9.mLastUpdated
            r7 = -1
            int r2 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r2 == 0) goto L_0x0047
            java.lang.CharSequence r2 = r9.getRelativeTimeString(r5)
            if (r2 == 0) goto L_0x0047
            android.content.res.Resources r5 = r9.getResources()
            int r6 = androidx.slice.view.R$string.abc_slice_updated
            java.lang.Object[] r7 = new java.lang.Object[r1]
            r7[r4] = r2
            java.lang.String r2 = r5.getString(r6, r7)
            goto L_0x0048
        L_0x0047:
            r2 = r3
        L_0x0048:
            if (r0 == 0) goto L_0x004e
            java.lang.CharSequence r3 = r0.getSanitizedText()
        L_0x004e:
            boolean r5 = android.text.TextUtils.isEmpty(r3)
            if (r5 == 0) goto L_0x0061
            if (r0 == 0) goto L_0x005f
            java.lang.String r5 = "partial"
            boolean r0 = r0.hasHint(r5)
            if (r0 == 0) goto L_0x005f
            goto L_0x0061
        L_0x005f:
            r0 = r4
            goto L_0x0062
        L_0x0061:
            r0 = r1
        L_0x0062:
            if (r0 == 0) goto L_0x00a1
            android.widget.TextView r5 = r9.mSecondaryText
            r5.setText(r3)
            androidx.slice.widget.SliceStyle r5 = r9.mSliceStyle
            if (r5 == 0) goto L_0x00a1
            android.widget.TextView r6 = r9.mSecondaryText
            boolean r7 = r9.mIsHeader
            if (r7 == 0) goto L_0x0078
            int r5 = r5.getHeaderSubtitleSize()
            goto L_0x007c
        L_0x0078:
            int r5 = r5.getSubtitleSize()
        L_0x007c:
            float r5 = (float) r5
            r6.setTextSize(r4, r5)
            android.widget.TextView r5 = r9.mSecondaryText
            androidx.slice.widget.RowStyle r6 = r9.mRowStyle
            int r6 = r6.getSubtitleColor()
            r5.setTextColor(r6)
            boolean r5 = r9.mIsHeader
            if (r5 == 0) goto L_0x0096
            androidx.slice.widget.SliceStyle r5 = r9.mSliceStyle
            int r5 = r5.getVerticalHeaderTextPadding()
            goto L_0x009c
        L_0x0096:
            androidx.slice.widget.SliceStyle r5 = r9.mSliceStyle
            int r5 = r5.getVerticalTextPadding()
        L_0x009c:
            android.widget.TextView r6 = r9.mSecondaryText
            r6.setPadding(r4, r5, r4, r4)
        L_0x00a1:
            r5 = 2
            if (r2 == 0) goto L_0x00f3
            boolean r3 = android.text.TextUtils.isEmpty(r3)
            if (r3 != 0) goto L_0x00bb
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r6 = " · "
            r3.append(r6)
            r3.append(r2)
            java.lang.String r2 = r3.toString()
        L_0x00bb:
            android.text.SpannableString r3 = new android.text.SpannableString
            r3.<init>(r2)
            android.text.style.StyleSpan r6 = new android.text.style.StyleSpan
            r6.<init>(r5)
            int r7 = r2.length()
            r3.setSpan(r6, r4, r7, r4)
            android.widget.TextView r6 = r9.mLastUpdatedText
            r6.setText(r3)
            androidx.slice.widget.SliceStyle r3 = r9.mSliceStyle
            if (r3 == 0) goto L_0x00f3
            android.widget.TextView r6 = r9.mLastUpdatedText
            boolean r7 = r9.mIsHeader
            if (r7 == 0) goto L_0x00e0
            int r3 = r3.getHeaderSubtitleSize()
            goto L_0x00e4
        L_0x00e0:
            int r3 = r3.getSubtitleSize()
        L_0x00e4:
            float r3 = (float) r3
            r6.setTextSize(r4, r3)
            android.widget.TextView r3 = r9.mLastUpdatedText
            androidx.slice.widget.RowStyle r6 = r9.mRowStyle
            int r6 = r6.getSubtitleColor()
            r3.setTextColor(r6)
        L_0x00f3:
            android.widget.TextView r3 = r9.mLastUpdatedText
            boolean r6 = android.text.TextUtils.isEmpty(r2)
            r7 = 8
            if (r6 == 0) goto L_0x00ff
            r6 = r7
            goto L_0x0100
        L_0x00ff:
            r6 = r4
        L_0x0100:
            r3.setVisibility(r6)
            android.widget.TextView r3 = r9.mSecondaryText
            if (r0 == 0) goto L_0x0108
            r7 = r4
        L_0x0108:
            r3.setVisibility(r7)
            androidx.slice.widget.RowContent r3 = r9.mRowContent
            boolean r3 = r3.getIsHeader()
            if (r3 == 0) goto L_0x011a
            boolean r3 = r9.mAllowTwoLines
            if (r3 == 0) goto L_0x0118
            goto L_0x011a
        L_0x0118:
            r3 = r4
            goto L_0x011b
        L_0x011a:
            r3 = r1
        L_0x011b:
            if (r3 == 0) goto L_0x0128
            if (r10 != 0) goto L_0x0128
            if (r0 == 0) goto L_0x0128
            boolean r10 = android.text.TextUtils.isEmpty(r2)
            if (r10 == 0) goto L_0x0128
            goto L_0x0129
        L_0x0128:
            r5 = r1
        L_0x0129:
            android.widget.TextView r10 = r9.mSecondaryText
            if (r5 != r1) goto L_0x012e
            goto L_0x012f
        L_0x012e:
            r1 = r4
        L_0x012f:
            r10.setSingleLine(r1)
            android.widget.TextView r10 = r9.mSecondaryText
            r10.setMaxLines(r5)
            android.widget.TextView r10 = r9.mSecondaryText
            r10.requestLayout()
            android.widget.TextView r9 = r9.mLastUpdatedText
            r9.requestLayout()
        L_0x0141:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.slice.widget.RowView.addSubtitle(boolean):void");
    }

    private CharSequence getRelativeTimeString(long j) {
        long currentTimeMillis = System.currentTimeMillis() - j;
        if (currentTimeMillis > 31449600000L) {
            int i = (int) (currentTimeMillis / 31449600000L);
            return getResources().getQuantityString(R$plurals.abc_slice_duration_years, i, new Object[]{Integer.valueOf(i)});
        } else if (currentTimeMillis > 86400000) {
            int i2 = (int) (currentTimeMillis / 86400000);
            return getResources().getQuantityString(R$plurals.abc_slice_duration_days, i2, new Object[]{Integer.valueOf(i2)});
        } else if (currentTimeMillis <= 60000) {
            return null;
        } else {
            int i3 = (int) (currentTimeMillis / 60000);
            return getResources().getQuantityString(R$plurals.abc_slice_duration_min, i3, new Object[]{Integer.valueOf(i3)});
        }
    }

    private void initRangeBar() {
        SliceItem findSubtype = SliceQuery.findSubtype(this.mRangeItem, "int", "min");
        int i = 0;
        int i2 = findSubtype != null ? findSubtype.getInt() : 0;
        this.mRangeMinValue = i2;
        SliceItem findSubtype2 = SliceQuery.findSubtype(this.mRangeItem, "int", "max");
        int i3 = this.mIsStarRating ? 5 : 100;
        if (findSubtype2 != null) {
            i3 = findSubtype2.getInt();
        }
        this.mRangeMaxValue = i3;
        SliceItem findSubtype3 = SliceQuery.findSubtype(this.mRangeItem, "int", "value");
        if (findSubtype3 != null) {
            i = findSubtype3.getInt() - i2;
        }
        this.mRangeValue = i;
    }

    private void addRangeView() {
        ProgressBar progressBar;
        Drawable drawable;
        Drawable loadDrawable;
        if (this.mHandler == null) {
            this.mHandler = new Handler();
        }
        if (this.mIsStarRating) {
            addRatingBarView();
            return;
        }
        SliceItem findSubtype = SliceQuery.findSubtype(this.mRangeItem, "int", "range_mode");
        boolean z = findSubtype != null && findSubtype.getInt() == 1;
        boolean equals = "action".equals(this.mRangeItem.getFormat());
        boolean z2 = this.mStartItem == null;
        if (!equals) {
            if (z2) {
                progressBar = new ProgressBar(getContext(), (AttributeSet) null, 16842872);
            } else {
                progressBar = (ProgressBar) LayoutInflater.from(getContext()).inflate(R$layout.abc_slice_progress_inline_view, this, false);
                RowStyle rowStyle = this.mRowStyle;
                if (rowStyle != null) {
                    setViewWidth(progressBar, rowStyle.getProgressBarInlineWidth());
                    setViewSidePaddings(progressBar, this.mRowStyle.getProgressBarStartPadding(), this.mRowStyle.getProgressBarEndPadding());
                }
            }
            if (z) {
                progressBar.setIndeterminate(true);
            }
        } else if (z2) {
            progressBar = new SeekBar(getContext());
        } else {
            progressBar = (SeekBar) LayoutInflater.from(getContext()).inflate(R$layout.abc_slice_seekbar_view, this, false);
            RowStyle rowStyle2 = this.mRowStyle;
            if (rowStyle2 != null) {
                setViewWidth(progressBar, rowStyle2.getSeekBarInlineWidth());
            }
        }
        if (z) {
            drawable = DrawableCompat.wrap(progressBar.getIndeterminateDrawable());
        } else {
            drawable = DrawableCompat.wrap(progressBar.getProgressDrawable());
        }
        int i = this.mTintColor;
        if (!(i == -1 || drawable == null)) {
            DrawableCompat.setTint(drawable, i);
            if (z) {
                progressBar.setIndeterminateDrawable(drawable);
            } else {
                progressBar.setProgressDrawable(drawable);
            }
        }
        progressBar.setMax(this.mRangeMaxValue - this.mRangeMinValue);
        progressBar.setProgress(this.mRangeValue);
        progressBar.setVisibility(0);
        if (this.mStartItem == null) {
            addView(progressBar, new FrameLayout.LayoutParams(-1, -2));
        } else {
            this.mSubContent.setVisibility(8);
            this.mContent.addView(progressBar, 1);
        }
        this.mRangeBar = progressBar;
        if (equals) {
            SliceItem inputRangeThumb = this.mRowContent.getInputRangeThumb();
            SeekBar seekBar = (SeekBar) this.mRangeBar;
            if (!(inputRangeThumb == null || inputRangeThumb.getIcon() == null || (loadDrawable = inputRangeThumb.getIcon().loadDrawable(getContext())) == null)) {
                seekBar.setThumb(loadDrawable);
            }
            Drawable wrap = DrawableCompat.wrap(seekBar.getThumb());
            int i2 = this.mTintColor;
            if (!(i2 == -1 || wrap == null)) {
                DrawableCompat.setTint(wrap, i2);
                seekBar.setThumb(wrap);
            }
            seekBar.setOnSeekBarChangeListener(this.mSeekBarChangeListener);
        }
    }

    private void addRatingBarView() {
        RatingBar ratingBar = new RatingBar(getContext());
        ((LayerDrawable) ratingBar.getProgressDrawable()).getDrawable(2).setColorFilter(this.mTintColor, PorterDuff.Mode.SRC_IN);
        ratingBar.setStepSize(1.0f);
        ratingBar.setNumStars(this.mRangeMaxValue);
        ratingBar.setRating((float) this.mRangeValue);
        ratingBar.setVisibility(0);
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setGravity(17);
        linearLayout.setVisibility(0);
        linearLayout.addView(ratingBar, new FrameLayout.LayoutParams(-2, -2));
        addView(linearLayout, new FrameLayout.LayoutParams(-1, -2));
        ratingBar.setOnRatingBarChangeListener(this.mRatingBarChangeListener);
        this.mRangeBar = linearLayout;
    }

    /* access modifiers changed from: package-private */
    public void sendSliderValue() {
        if (this.mRangeItem != null) {
            try {
                this.mLastSentRangeUpdate = System.currentTimeMillis();
                this.mRangeItem.fireAction(getContext(), new Intent().addFlags(268435456).putExtra("android.app.slice.extra.RANGE_VALUE", this.mRangeValue));
                if (this.mObserver != null) {
                    EventInfo eventInfo = new EventInfo(getMode(), 2, 4, this.mRowIndex);
                    eventInfo.state = this.mRangeValue;
                    this.mObserver.onSliceAction(eventInfo, this.mRangeItem);
                }
            } catch (PendingIntent.CanceledException e) {
                Log.e("RowView", "PendingIntent for slice cannot be sent", e);
            }
        }
    }

    private void addSelection(SliceItem sliceItem) {
        if (this.mHandler == null) {
            this.mHandler = new Handler();
        }
        this.mSelectionOptionKeys = new ArrayList<>();
        this.mSelectionOptionValues = new ArrayList<>();
        List<SliceItem> items = sliceItem.getSlice().getItems();
        for (int i = 0; i < items.size(); i++) {
            SliceItem sliceItem2 = items.get(i);
            if (sliceItem2.hasHint("selection_option")) {
                SliceItem findSubtype = SliceQuery.findSubtype(sliceItem2, "text", "selection_option_key");
                SliceItem findSubtype2 = SliceQuery.findSubtype(sliceItem2, "text", "selection_option_value");
                if (!(findSubtype == null || findSubtype2 == null)) {
                    this.mSelectionOptionKeys.add(findSubtype.getText().toString());
                    this.mSelectionOptionValues.add(findSubtype2.getSanitizedText());
                }
            }
        }
        this.mSelectionSpinner = (Spinner) LayoutInflater.from(getContext()).inflate(R$layout.abc_slice_row_selection, this, false);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), R$layout.abc_slice_row_selection_text, this.mSelectionOptionValues);
        arrayAdapter.setDropDownViewResource(R$layout.abc_slice_row_selection_dropdown_text);
        this.mSelectionSpinner.setAdapter(arrayAdapter);
        addView(this.mSelectionSpinner);
        this.mSelectionSpinner.setOnItemSelectedListener(this);
    }

    private void addAction(SliceActionImpl sliceActionImpl, int i, ViewGroup viewGroup, boolean z) {
        SliceActionView sliceActionView = new SliceActionView(getContext(), this.mSliceStyle, this.mRowStyle);
        viewGroup.addView(sliceActionView);
        if (viewGroup.getVisibility() == 8) {
            viewGroup.setVisibility(0);
        }
        boolean isToggle = sliceActionImpl.isToggle();
        EventInfo eventInfo = new EventInfo(getMode(), !isToggle ? 1 : 0, isToggle ? 3 : 0, this.mRowIndex);
        if (z) {
            eventInfo.setPosition(0, 0, 1);
        }
        sliceActionView.setAction(sliceActionImpl, eventInfo, this.mObserver, i, this.mLoadingListener);
        if (this.mLoadingActions.contains(sliceActionImpl.getSliceItem())) {
            sliceActionView.setLoading(true);
        }
        if (isToggle) {
            this.mToggles.put(sliceActionImpl, sliceActionView);
        } else {
            this.mActions.put(sliceActionImpl, sliceActionView);
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v3, resolved type: android.widget.TextView} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v4, resolved type: android.widget.TextView} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v5, resolved type: android.widget.TextView} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v0, resolved type: android.widget.ImageView} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v9, resolved type: android.widget.TextView} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean addItem(androidx.slice.SliceItem r9, int r10, boolean r11) {
        /*
            r8 = this;
            if (r11 == 0) goto L_0x0005
            android.widget.LinearLayout r0 = r8.mStartContainer
            goto L_0x0007
        L_0x0005:
            android.widget.LinearLayout r0 = r8.mEndContainer
        L_0x0007:
            java.lang.String r1 = r9.getFormat()
            java.lang.String r2 = "slice"
            boolean r1 = r2.equals(r1)
            r2 = 1
            r3 = 0
            if (r1 != 0) goto L_0x0021
            java.lang.String r1 = r9.getFormat()
            java.lang.String r4 = "action"
            boolean r1 = r4.equals(r1)
            if (r1 == 0) goto L_0x004f
        L_0x0021:
            java.lang.String r1 = "shortcut"
            boolean r1 = r9.hasHint(r1)
            if (r1 == 0) goto L_0x0032
            androidx.slice.core.SliceActionImpl r1 = new androidx.slice.core.SliceActionImpl
            r1.<init>(r9)
            r8.addAction(r1, r10, r0, r11)
            return r2
        L_0x0032:
            androidx.slice.Slice r11 = r9.getSlice()
            java.util.List r11 = r11.getItems()
            int r11 = r11.size()
            if (r11 != 0) goto L_0x0041
            return r3
        L_0x0041:
            androidx.slice.Slice r9 = r9.getSlice()
            java.util.List r9 = r9.getItems()
            java.lang.Object r9 = r9.get(r3)
            androidx.slice.SliceItem r9 = (androidx.slice.SliceItem) r9
        L_0x004f:
            java.lang.String r11 = r9.getFormat()
            java.lang.String r1 = "image"
            boolean r11 = r1.equals(r11)
            r1 = 0
            if (r11 == 0) goto L_0x0062
            androidx.core.graphics.drawable.IconCompat r11 = r9.getIcon()
            r4 = r1
            goto L_0x0073
        L_0x0062:
            java.lang.String r11 = r9.getFormat()
            java.lang.String r4 = "long"
            boolean r11 = r4.equals(r11)
            if (r11 == 0) goto L_0x0071
            r4 = r9
            r11 = r1
            goto L_0x0073
        L_0x0071:
            r11 = r1
            r4 = r11
        L_0x0073:
            if (r11 == 0) goto L_0x013a
            java.lang.String r1 = "no_tint"
            boolean r1 = r9.hasHint(r1)
            r1 = r1 ^ r2
            java.lang.String r4 = "raw"
            boolean r4 = r9.hasHint(r4)
            android.content.res.Resources r5 = r8.getResources()
            android.util.DisplayMetrics r5 = r5.getDisplayMetrics()
            float r5 = r5.density
            android.widget.ImageView r6 = new android.widget.ImageView
            android.content.Context r7 = r8.getContext()
            r6.<init>(r7)
            android.content.Context r7 = r8.getContext()
            android.graphics.drawable.Drawable r11 = r11.loadDrawable(r7)
            androidx.slice.widget.SliceStyle r7 = r8.mSliceStyle
            if (r7 == 0) goto L_0x00a9
            boolean r7 = r7.getApplyCornerRadiusToLargeImages()
            if (r7 == 0) goto L_0x00a9
            r7 = r2
            goto L_0x00aa
        L_0x00a9:
            r7 = r3
        L_0x00aa:
            if (r7 == 0) goto L_0x00c3
            java.lang.String r7 = "large"
            boolean r9 = r9.hasHint(r7)
            if (r9 == 0) goto L_0x00c3
            androidx.slice.CornerDrawable r9 = new androidx.slice.CornerDrawable
            androidx.slice.widget.SliceStyle r7 = r8.mSliceStyle
            float r7 = r7.getImageCornerRadius()
            r9.<init>(r11, r7)
            r6.setImageDrawable(r9)
            goto L_0x00c6
        L_0x00c3:
            r6.setImageDrawable(r11)
        L_0x00c6:
            r9 = -1
            if (r1 == 0) goto L_0x00ce
            if (r10 == r9) goto L_0x00ce
            r6.setColorFilter(r10)
        L_0x00ce:
            boolean r10 = r8.mIsRangeSliding
            if (r10 == 0) goto L_0x00d9
            r0.removeAllViews()
            r0.addView(r6)
            goto L_0x00dc
        L_0x00d9:
            r0.addView(r6)
        L_0x00dc:
            androidx.slice.widget.RowStyle r10 = r8.mRowStyle
            if (r10 == 0) goto L_0x00f8
            int r10 = r10.getIconSize()
            if (r10 <= 0) goto L_0x00e7
            goto L_0x00e9
        L_0x00e7:
            int r10 = r8.mIconSize
        L_0x00e9:
            r8.mIconSize = r10
            androidx.slice.widget.RowStyle r10 = r8.mRowStyle
            int r10 = r10.getImageSize()
            if (r10 <= 0) goto L_0x00f4
            goto L_0x00f6
        L_0x00f4:
            int r10 = r8.mImageSize
        L_0x00f6:
            r8.mImageSize = r10
        L_0x00f8:
            android.view.ViewGroup$LayoutParams r10 = r6.getLayoutParams()
            android.widget.LinearLayout$LayoutParams r10 = (android.widget.LinearLayout.LayoutParams) r10
            if (r4 == 0) goto L_0x010b
            int r0 = r11.getIntrinsicWidth()
            float r0 = (float) r0
            float r0 = r0 / r5
            int r0 = java.lang.Math.round(r0)
            goto L_0x010d
        L_0x010b:
            int r0 = r8.mImageSize
        L_0x010d:
            r10.width = r0
            if (r4 == 0) goto L_0x011c
            int r11 = r11.getIntrinsicHeight()
            float r11 = (float) r11
            float r11 = r11 / r5
            int r11 = java.lang.Math.round(r11)
            goto L_0x011e
        L_0x011c:
            int r11 = r8.mImageSize
        L_0x011e:
            r10.height = r11
            r6.setLayoutParams(r10)
            if (r1 == 0) goto L_0x0134
            int r10 = r8.mImageSize
            if (r10 != r9) goto L_0x012e
            int r8 = r8.mIconSize
            int r8 = r8 / 2
            goto L_0x0135
        L_0x012e:
            int r8 = r8.mIconSize
            int r10 = r10 - r8
            int r8 = r10 / 2
            goto L_0x0135
        L_0x0134:
            r8 = r3
        L_0x0135:
            r6.setPadding(r8, r8, r8, r8)
            r1 = r6
            goto L_0x016c
        L_0x013a:
            if (r4 == 0) goto L_0x016c
            android.widget.TextView r1 = new android.widget.TextView
            android.content.Context r10 = r8.getContext()
            r1.<init>(r10)
            android.content.Context r10 = r8.getContext()
            long r4 = r9.getLong()
            java.lang.CharSequence r9 = androidx.slice.widget.SliceViewUtil.getTimestampString(r10, r4)
            r1.setText(r9)
            androidx.slice.widget.SliceStyle r9 = r8.mSliceStyle
            if (r9 == 0) goto L_0x0169
            int r9 = r9.getSubtitleSize()
            float r9 = (float) r9
            r1.setTextSize(r3, r9)
            androidx.slice.widget.RowStyle r8 = r8.mRowStyle
            int r8 = r8.getSubtitleColor()
            r1.setTextColor(r8)
        L_0x0169:
            r0.addView(r1)
        L_0x016c:
            if (r1 == 0) goto L_0x016f
            goto L_0x0170
        L_0x016f:
            r2 = r3
        L_0x0170:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.slice.widget.RowView.addItem(androidx.slice.SliceItem, int, boolean):boolean");
    }

    private void showSeeMore() {
        final Button button = (Button) LayoutInflater.from(getContext()).inflate(R$layout.abc_slice_row_show_more, this, false);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    RowView rowView = RowView.this;
                    if (rowView.mObserver != null) {
                        EventInfo eventInfo = new EventInfo(rowView.getMode(), 4, 0, RowView.this.mRowIndex);
                        RowView rowView2 = RowView.this;
                        rowView2.mObserver.onSliceAction(eventInfo, rowView2.mRowContent.getSliceItem());
                    }
                    RowView rowView3 = RowView.this;
                    rowView3.mShowActionSpinner = rowView3.mRowContent.getSliceItem().fireActionInternal(RowView.this.getContext(), (Intent) null);
                    RowView rowView4 = RowView.this;
                    if (rowView4.mShowActionSpinner) {
                        SliceActionView.SliceActionLoadingListener sliceActionLoadingListener = rowView4.mLoadingListener;
                        if (sliceActionLoadingListener != null) {
                            sliceActionLoadingListener.onSliceActionLoading(rowView4.mRowContent.getSliceItem(), RowView.this.mRowIndex);
                        }
                        RowView rowView5 = RowView.this;
                        rowView5.mLoadingActions.add(rowView5.mRowContent.getSliceItem());
                        button.setVisibility(8);
                    }
                    RowView.this.updateActionSpinner();
                } catch (PendingIntent.CanceledException e) {
                    Log.e("RowView", "PendingIntent for slice cannot be sent", e);
                }
            }
        });
        int i = this.mTintColor;
        if (i != -1) {
            button.setTextColor(i);
        }
        this.mSeeMoreView = button;
        this.mRootView.addView(button);
        if (this.mLoadingActions.contains(this.mRowContent.getSliceItem())) {
            this.mShowActionSpinner = true;
            button.setVisibility(8);
            updateActionSpinner();
        }
    }

    /* access modifiers changed from: package-private */
    public void updateActionSpinner() {
        this.mActionSpinner.setVisibility(this.mShowActionSpinner ? 0 : 8);
    }

    public void setLoadingActions(Set<SliceItem> set) {
        if (set == null) {
            this.mLoadingActions.clear();
            this.mShowActionSpinner = false;
        } else {
            this.mLoadingActions = set;
        }
        updateEndItems();
        updateActionSpinner();
    }

    public void onClick(View view) {
        SliceActionView sliceActionView;
        SliceActionView.SliceActionLoadingListener sliceActionLoadingListener;
        SliceActionImpl sliceActionImpl = this.mRowAction;
        if (sliceActionImpl != null && sliceActionImpl.getActionItem() != null) {
            if (this.mRowAction.getSubtype() != null) {
                String subtype = this.mRowAction.getSubtype();
                subtype.hashCode();
                char c = 65535;
                switch (subtype.hashCode()) {
                    case -868304044:
                        if (subtype.equals("toggle")) {
                            c = 0;
                            break;
                        }
                        break;
                    case 759128640:
                        if (subtype.equals("time_picker")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 1250407999:
                        if (subtype.equals("date_picker")) {
                            c = 2;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        sliceActionView = this.mToggles.get(this.mRowAction);
                        break;
                    case 1:
                        onClickPicker(false);
                        return;
                    case 2:
                        onClickPicker(true);
                        return;
                    default:
                        sliceActionView = this.mActions.get(this.mRowAction);
                        break;
                }
            } else {
                sliceActionView = this.mActions.get(this.mRowAction);
            }
            if (sliceActionView != null && !(view instanceof SliceActionView)) {
                sliceActionView.sendAction();
            } else if (this.mRowContent.getIsHeader()) {
                performClick();
            } else {
                try {
                    this.mShowActionSpinner = this.mRowAction.getActionItem().fireActionInternal(getContext(), (Intent) null);
                    if (this.mObserver != null) {
                        this.mObserver.onSliceAction(new EventInfo(getMode(), 3, 0, this.mRowIndex), this.mRowAction.getSliceItem());
                    }
                    if (this.mShowActionSpinner && (sliceActionLoadingListener = this.mLoadingListener) != null) {
                        sliceActionLoadingListener.onSliceActionLoading(this.mRowAction.getSliceItem(), this.mRowIndex);
                        this.mLoadingActions.add(this.mRowAction.getSliceItem());
                    }
                    updateActionSpinner();
                } catch (PendingIntent.CanceledException e) {
                    Log.e("RowView", "PendingIntent for slice cannot be sent", e);
                }
            }
        }
    }

    private void onClickPicker(boolean z) {
        if (this.mRowAction != null) {
            Log.d("ASDF", "ASDF" + z + ":" + this.mRowAction.getSliceItem());
            SliceItem findSubtype = SliceQuery.findSubtype(this.mRowAction.getSliceItem(), "long", "millis");
            if (findSubtype != null) {
                int i = this.mRowIndex;
                Calendar instance = Calendar.getInstance();
                instance.setTime(new Date(findSubtype.getLong()));
                if (z) {
                    new DatePickerDialog(getContext(), R$style.DialogTheme, new DateSetListener(this.mRowAction.getSliceItem(), i), instance.get(1), instance.get(2), instance.get(5)).show();
                    return;
                }
                new TimePickerDialog(getContext(), R$style.DialogTheme, new TimeSetListener(this.mRowAction.getSliceItem(), i), instance.get(11), instance.get(12), false).show();
            }
        }
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
                    sliceItem.fireAction(RowView.this.getContext(), new Intent().addFlags(268435456).putExtra("android.app.slice.extra.RANGE_VALUE", time.getTime()));
                    RowView rowView = RowView.this;
                    if (rowView.mObserver != null) {
                        RowView.this.mObserver.onSliceAction(new EventInfo(rowView.getMode(), 6, 7, this.mRowIndex), this.mActionItem);
                    }
                } catch (PendingIntent.CanceledException e) {
                    Log.e("RowView", "PendingIntent for slice cannot be sent", e);
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
                    sliceItem.fireAction(RowView.this.getContext(), new Intent().addFlags(268435456).putExtra("android.app.slice.extra.RANGE_VALUE", time.getTime()));
                    RowView rowView = RowView.this;
                    if (rowView.mObserver != null) {
                        RowView.this.mObserver.onSliceAction(new EventInfo(rowView.getMode(), 7, 8, this.mRowIndex), this.mActionItem);
                    }
                } catch (PendingIntent.CanceledException e) {
                    Log.e("RowView", "PendingIntent for slice cannot be sent", e);
                }
            }
        }
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
        if (this.mSelectionItem != null && adapterView == this.mSelectionSpinner && i >= 0 && i < this.mSelectionOptionKeys.size()) {
            if (this.mObserver != null) {
                this.mObserver.onSliceAction(new EventInfo(getMode(), 5, 6, this.mRowIndex), this.mSelectionItem);
            }
            try {
                if (this.mSelectionItem.fireActionInternal(getContext(), new Intent().addFlags(268435456).putExtra("android.app.slice.extra.SELECTION", this.mSelectionOptionKeys.get(i)))) {
                    this.mShowActionSpinner = true;
                    SliceActionView.SliceActionLoadingListener sliceActionLoadingListener = this.mLoadingListener;
                    if (sliceActionLoadingListener != null) {
                        sliceActionLoadingListener.onSliceActionLoading(this.mRowAction.getSliceItem(), this.mRowIndex);
                        this.mLoadingActions.add(this.mRowAction.getSliceItem());
                    }
                    updateActionSpinner();
                }
            } catch (PendingIntent.CanceledException e) {
                Log.e("RowView", "PendingIntent for slice cannot be sent", e);
            }
        }
    }

    private void setViewClickable(View view, boolean z) {
        Drawable drawable = null;
        view.setOnClickListener(z ? this : null);
        if (z) {
            drawable = SliceViewUtil.getDrawable(getContext(), 16843534);
        }
        view.setBackground(drawable);
        view.setClickable(z);
    }

    public void resetView() {
        this.mRowContent = null;
        this.mLoadingActions.clear();
        resetViewState();
    }

    private void resetViewState() {
        this.mRootView.setVisibility(0);
        setLayoutDirection(2);
        setViewClickable(this.mRootView, false);
        setViewClickable(this.mContent, false);
        this.mStartContainer.removeAllViews();
        this.mEndContainer.removeAllViews();
        this.mEndContainer.setVisibility(8);
        this.mPrimaryText.setText((CharSequence) null);
        this.mSecondaryText.setText((CharSequence) null);
        this.mLastUpdatedText.setText((CharSequence) null);
        this.mLastUpdatedText.setVisibility(8);
        this.mToggles.clear();
        this.mActions.clear();
        this.mRowAction = null;
        this.mBottomDivider.setVisibility(8);
        this.mActionDivider.setVisibility(8);
        View view = this.mSeeMoreView;
        if (view != null) {
            this.mRootView.removeView(view);
            this.mSeeMoreView = null;
        }
        this.mIsRangeSliding = false;
        this.mRangeHasPendingUpdate = false;
        this.mRangeItem = null;
        this.mRangeMinValue = 0;
        this.mRangeMaxValue = 0;
        this.mRangeValue = 0;
        this.mLastSentRangeUpdate = 0;
        this.mHandler = null;
        View view2 = this.mRangeBar;
        if (view2 != null) {
            if (this.mStartItem == null) {
                removeView(view2);
            } else {
                this.mContent.removeView(view2);
            }
            this.mRangeBar = null;
        }
        this.mSubContent.setVisibility(0);
        this.mStartItem = null;
        this.mActionSpinner.setVisibility(8);
        Spinner spinner = this.mSelectionSpinner;
        if (spinner != null) {
            removeView(spinner);
            this.mSelectionSpinner = null;
        }
        this.mSelectionItem = null;
    }
}
