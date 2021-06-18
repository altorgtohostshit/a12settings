package androidx.constraintlayout.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer;
import androidx.constraintlayout.solver.widgets.Guideline;
import androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure;
import java.util.ArrayList;
import java.util.HashMap;

public class ConstraintLayout extends ViewGroup {
    SparseArray<View> mChildrenByIds = new SparseArray<>();
    /* access modifiers changed from: private */
    public ArrayList<ConstraintHelper> mConstraintHelpers = new ArrayList<>(4);
    protected ConstraintLayoutStates mConstraintLayoutSpec = null;
    private ConstraintSet mConstraintSet = null;
    private int mConstraintSetId = -1;
    private HashMap<String, Integer> mDesignIds = new HashMap<>();
    protected boolean mDirtyHierarchy = true;
    private int mLastMeasureHeight = -1;
    int mLastMeasureHeightMode = 0;
    int mLastMeasureHeightSize = -1;
    private int mLastMeasureWidth = -1;
    int mLastMeasureWidthMode = 0;
    int mLastMeasureWidthSize = -1;
    /* access modifiers changed from: protected */
    public ConstraintWidgetContainer mLayoutWidget = new ConstraintWidgetContainer();
    private int mMaxHeight = Integer.MAX_VALUE;
    private int mMaxWidth = Integer.MAX_VALUE;
    Measurer mMeasurer = new Measurer(this);
    private int mMinHeight = 0;
    private int mMinWidth = 0;
    /* access modifiers changed from: private */
    public int mOnMeasureHeightMeasureSpec = 0;
    /* access modifiers changed from: private */
    public int mOnMeasureWidthMeasureSpec = 0;
    private int mOptimizationLevel = 7;
    private SparseArray<ConstraintWidget> mTempMapIdToWidget = new SparseArray<>();

    public boolean shouldDelayChildPressedState() {
        return false;
    }

    public void setDesignInformation(int i, Object obj, Object obj2) {
        if (i == 0 && (obj instanceof String) && (obj2 instanceof Integer)) {
            if (this.mDesignIds == null) {
                this.mDesignIds = new HashMap<>();
            }
            String str = (String) obj;
            int indexOf = str.indexOf("/");
            if (indexOf != -1) {
                str = str.substring(indexOf + 1);
            }
            this.mDesignIds.put(str, Integer.valueOf(((Integer) obj2).intValue()));
        }
    }

    public Object getDesignInformation(int i, Object obj) {
        if (i != 0 || !(obj instanceof String)) {
            return null;
        }
        String str = (String) obj;
        HashMap<String, Integer> hashMap = this.mDesignIds;
        if (hashMap == null || !hashMap.containsKey(str)) {
            return null;
        }
        return this.mDesignIds.get(str);
    }

    public ConstraintLayout(Context context) {
        super(context);
        init((AttributeSet) null, 0, 0);
    }

    public ConstraintLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(attributeSet, 0, 0);
    }

    public ConstraintLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(attributeSet, i, 0);
    }

    @TargetApi(21)
    public ConstraintLayout(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(attributeSet, i, i2);
    }

    public void setId(int i) {
        this.mChildrenByIds.remove(getId());
        super.setId(i);
        this.mChildrenByIds.put(getId(), this);
    }

    class Measurer implements BasicMeasure.Measurer {
        ConstraintLayout layout;

        public Measurer(ConstraintLayout constraintLayout) {
            this.layout = constraintLayout;
        }

        /* JADX WARNING: Removed duplicated region for block: B:100:0x018f  */
        /* JADX WARNING: Removed duplicated region for block: B:103:0x0197  */
        /* JADX WARNING: Removed duplicated region for block: B:104:0x019c  */
        /* JADX WARNING: Removed duplicated region for block: B:107:0x01a1  */
        /* JADX WARNING: Removed duplicated region for block: B:110:0x01a9  */
        /* JADX WARNING: Removed duplicated region for block: B:111:0x01ae  */
        /* JADX WARNING: Removed duplicated region for block: B:114:0x01b3  */
        /* JADX WARNING: Removed duplicated region for block: B:117:0x01bb A[ADDED_TO_REGION] */
        /* JADX WARNING: Removed duplicated region for block: B:120:0x01cb A[ADDED_TO_REGION] */
        /* JADX WARNING: Removed duplicated region for block: B:125:0x01d8 A[ADDED_TO_REGION] */
        /* JADX WARNING: Removed duplicated region for block: B:129:0x01e3  */
        /* JADX WARNING: Removed duplicated region for block: B:131:0x01e9  */
        /* JADX WARNING: Removed duplicated region for block: B:134:0x0205  */
        /* JADX WARNING: Removed duplicated region for block: B:135:0x0207  */
        /* JADX WARNING: Removed duplicated region for block: B:140:0x0212  */
        /* JADX WARNING: Removed duplicated region for block: B:141:0x0215  */
        /* JADX WARNING: Removed duplicated region for block: B:144:0x021c  */
        /* JADX WARNING: Removed duplicated region for block: B:31:0x00a7  */
        /* JADX WARNING: Removed duplicated region for block: B:50:0x00fb  */
        /* JADX WARNING: Removed duplicated region for block: B:53:0x0107  */
        /* JADX WARNING: Removed duplicated region for block: B:54:0x0109  */
        /* JADX WARNING: Removed duplicated region for block: B:56:0x010c  */
        /* JADX WARNING: Removed duplicated region for block: B:57:0x010e  */
        /* JADX WARNING: Removed duplicated region for block: B:62:0x0118  */
        /* JADX WARNING: Removed duplicated region for block: B:63:0x011a  */
        /* JADX WARNING: Removed duplicated region for block: B:67:0x0122  */
        /* JADX WARNING: Removed duplicated region for block: B:68:0x0124  */
        /* JADX WARNING: Removed duplicated region for block: B:73:0x012e  */
        /* JADX WARNING: Removed duplicated region for block: B:74:0x0130  */
        /* JADX WARNING: Removed duplicated region for block: B:78:0x0139  */
        /* JADX WARNING: Removed duplicated region for block: B:79:0x013b  */
        /* JADX WARNING: Removed duplicated region for block: B:82:0x014c A[ADDED_TO_REGION] */
        /* JADX WARNING: Removed duplicated region for block: B:93:0x0169  */
        /* JADX WARNING: Removed duplicated region for block: B:94:0x0173  */
        /* JADX WARNING: Removed duplicated region for block: B:97:0x0184  */
        /* JADX WARNING: Removed duplicated region for block: B:98:0x018b  */
        @android.annotation.SuppressLint({"WrongCall"})
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final void measure(androidx.constraintlayout.solver.widgets.ConstraintWidget r19, androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure.Measure r20) {
            /*
                r18 = this;
                r0 = r18
                r1 = r19
                r2 = r20
                if (r1 != 0) goto L_0x0009
                return
            L_0x0009:
                int r3 = r19.getVisibility()
                r4 = 8
                r5 = 0
                if (r3 != r4) goto L_0x0019
                r2.measuredWidth = r5
                r2.measuredHeight = r5
                r2.measuredBaseline = r5
                return
            L_0x0019:
                androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r3 = r2.horizontalBehavior
                androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r4 = r2.verticalBehavior
                int r6 = r2.horizontalDimension
                int r7 = r2.verticalDimension
                androidx.constraintlayout.widget.ConstraintLayout r8 = r0.layout
                int r8 = r8.getPaddingTop()
                androidx.constraintlayout.widget.ConstraintLayout r9 = r0.layout
                int r9 = r9.getPaddingBottom()
                int r8 = r8 + r9
                androidx.constraintlayout.widget.ConstraintLayout r9 = r0.layout
                int r9 = r9.getPaddingWidth()
                int[] r10 = androidx.constraintlayout.widget.ConstraintLayout.C01291.f30xdde91696
                int r11 = r3.ordinal()
                r11 = r10[r11]
                r12 = 4
                r13 = 3
                r14 = 2
                r15 = -2
                r5 = 1
                if (r11 == r5) goto L_0x0098
                if (r11 == r14) goto L_0x008c
                if (r11 == r13) goto L_0x007b
                if (r11 == r12) goto L_0x004c
                r6 = 0
            L_0x004a:
                r11 = 0
                goto L_0x009f
            L_0x004c:
                androidx.constraintlayout.widget.ConstraintLayout r6 = r0.layout
                int r6 = r6.mOnMeasureWidthMeasureSpec
                int r6 = android.view.ViewGroup.getChildMeasureSpec(r6, r9, r15)
                int r9 = r1.mMatchConstraintDefaultWidth
                if (r9 != r5) goto L_0x005c
                r9 = r5
                goto L_0x005d
            L_0x005c:
                r9 = 0
            L_0x005d:
                boolean r11 = r2.useDeprecated
                if (r11 == 0) goto L_0x0096
                if (r9 == 0) goto L_0x0070
                if (r9 == 0) goto L_0x0096
                int[] r9 = r1.wrapMeasure
                r11 = 0
                r9 = r9[r11]
                int r11 = r19.getWidth()
                if (r9 == r11) goto L_0x0096
            L_0x0070:
                int r6 = r19.getWidth()
                r9 = 1073741824(0x40000000, float:2.0)
                int r6 = android.view.View.MeasureSpec.makeMeasureSpec(r6, r9)
                goto L_0x004a
            L_0x007b:
                androidx.constraintlayout.widget.ConstraintLayout r6 = r0.layout
                int r6 = r6.mOnMeasureWidthMeasureSpec
                int r11 = r19.getHorizontalMargin()
                int r9 = r9 + r11
                r11 = -1
                int r6 = android.view.ViewGroup.getChildMeasureSpec(r6, r9, r11)
                goto L_0x004a
            L_0x008c:
                androidx.constraintlayout.widget.ConstraintLayout r6 = r0.layout
                int r6 = r6.mOnMeasureWidthMeasureSpec
                int r6 = android.view.ViewGroup.getChildMeasureSpec(r6, r9, r15)
            L_0x0096:
                r11 = r5
                goto L_0x009f
            L_0x0098:
                r9 = 1073741824(0x40000000, float:2.0)
                int r6 = android.view.View.MeasureSpec.makeMeasureSpec(r6, r9)
                goto L_0x004a
            L_0x009f:
                int r9 = r4.ordinal()
                r9 = r10[r9]
                if (r9 == r5) goto L_0x00fb
                if (r9 == r14) goto L_0x00ef
                if (r9 == r13) goto L_0x00de
                if (r9 == r12) goto L_0x00b0
                r0 = 0
            L_0x00ae:
                r7 = 0
                goto L_0x0103
            L_0x00b0:
                androidx.constraintlayout.widget.ConstraintLayout r0 = r0.layout
                int r0 = r0.mOnMeasureHeightMeasureSpec
                int r0 = android.view.ViewGroup.getChildMeasureSpec(r0, r8, r15)
                int r7 = r1.mMatchConstraintDefaultHeight
                if (r7 != r5) goto L_0x00c0
                r7 = r5
                goto L_0x00c1
            L_0x00c0:
                r7 = 0
            L_0x00c1:
                boolean r8 = r2.useDeprecated
                if (r8 == 0) goto L_0x00f9
                if (r7 == 0) goto L_0x00d3
                if (r7 == 0) goto L_0x00f9
                int[] r7 = r1.wrapMeasure
                r7 = r7[r5]
                int r8 = r19.getHeight()
                if (r7 == r8) goto L_0x00f9
            L_0x00d3:
                int r0 = r19.getHeight()
                r7 = 1073741824(0x40000000, float:2.0)
                int r0 = android.view.View.MeasureSpec.makeMeasureSpec(r0, r7)
                goto L_0x00ae
            L_0x00de:
                androidx.constraintlayout.widget.ConstraintLayout r0 = r0.layout
                int r0 = r0.mOnMeasureHeightMeasureSpec
                int r7 = r19.getVerticalMargin()
                int r8 = r8 + r7
                r7 = -1
                int r0 = android.view.ViewGroup.getChildMeasureSpec(r0, r8, r7)
                goto L_0x00ae
            L_0x00ef:
                androidx.constraintlayout.widget.ConstraintLayout r0 = r0.layout
                int r0 = r0.mOnMeasureHeightMeasureSpec
                int r0 = android.view.ViewGroup.getChildMeasureSpec(r0, r8, r15)
            L_0x00f9:
                r7 = r5
                goto L_0x0103
            L_0x00fb:
                r0 = 1073741824(0x40000000, float:2.0)
                int r7 = android.view.View.MeasureSpec.makeMeasureSpec(r7, r0)
                r0 = r7
                goto L_0x00ae
            L_0x0103:
                androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r8 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
                if (r3 != r8) goto L_0x0109
                r9 = r5
                goto L_0x010a
            L_0x0109:
                r9 = 0
            L_0x010a:
                if (r4 != r8) goto L_0x010e
                r8 = r5
                goto L_0x010f
            L_0x010e:
                r8 = 0
            L_0x010f:
                androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r10 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_PARENT
                if (r4 == r10) goto L_0x011a
                androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r12 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.FIXED
                if (r4 != r12) goto L_0x0118
                goto L_0x011a
            L_0x0118:
                r4 = 0
                goto L_0x011b
            L_0x011a:
                r4 = r5
            L_0x011b:
                if (r3 == r10) goto L_0x0124
                androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r10 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.FIXED
                if (r3 != r10) goto L_0x0122
                goto L_0x0124
            L_0x0122:
                r3 = 0
                goto L_0x0125
            L_0x0124:
                r3 = r5
            L_0x0125:
                r10 = 0
                if (r9 == 0) goto L_0x0130
                float r12 = r1.mDimensionRatio
                int r12 = (r12 > r10 ? 1 : (r12 == r10 ? 0 : -1))
                if (r12 <= 0) goto L_0x0130
                r12 = r5
                goto L_0x0131
            L_0x0130:
                r12 = 0
            L_0x0131:
                if (r8 == 0) goto L_0x013b
                float r13 = r1.mDimensionRatio
                int r10 = (r13 > r10 ? 1 : (r13 == r10 ? 0 : -1))
                if (r10 <= 0) goto L_0x013b
                r10 = r5
                goto L_0x013c
            L_0x013b:
                r10 = 0
            L_0x013c:
                java.lang.Object r13 = r19.getCompanionWidget()
                android.view.View r13 = (android.view.View) r13
                android.view.ViewGroup$LayoutParams r14 = r13.getLayoutParams()
                androidx.constraintlayout.widget.ConstraintLayout$LayoutParams r14 = (androidx.constraintlayout.widget.ConstraintLayout.LayoutParams) r14
                boolean r15 = r2.useDeprecated
                if (r15 != 0) goto L_0x0161
                if (r9 == 0) goto L_0x0161
                int r9 = r1.mMatchConstraintDefaultWidth
                if (r9 != 0) goto L_0x0161
                if (r8 == 0) goto L_0x0161
                int r8 = r1.mMatchConstraintDefaultHeight
                if (r8 == 0) goto L_0x0159
                goto L_0x0161
            L_0x0159:
                r0 = 0
                r3 = 0
                r4 = -1
                r11 = 0
                r16 = 0
                goto L_0x0203
            L_0x0161:
                boolean r8 = r13 instanceof androidx.constraintlayout.widget.VirtualLayout
                if (r8 == 0) goto L_0x0173
                boolean r8 = r1 instanceof androidx.constraintlayout.solver.widgets.VirtualLayout
                if (r8 == 0) goto L_0x0173
                r8 = r1
                androidx.constraintlayout.solver.widgets.VirtualLayout r8 = (androidx.constraintlayout.solver.widgets.VirtualLayout) r8
                r9 = r13
                androidx.constraintlayout.widget.VirtualLayout r9 = (androidx.constraintlayout.widget.VirtualLayout) r9
                r9.onMeasure(r8, r6, r0)
                goto L_0x0176
            L_0x0173:
                r13.measure(r6, r0)
            L_0x0176:
                int r8 = r13.getMeasuredWidth()
                int r9 = r13.getMeasuredHeight()
                int r15 = r13.getBaseline()
                if (r11 == 0) goto L_0x018b
                int[] r11 = r1.wrapMeasure
                r16 = 0
                r11[r16] = r8
                goto L_0x018d
            L_0x018b:
                r16 = 0
            L_0x018d:
                if (r7 == 0) goto L_0x0193
                int[] r7 = r1.wrapMeasure
                r7[r5] = r9
            L_0x0193:
                int r7 = r1.mMatchConstraintMinWidth
                if (r7 <= 0) goto L_0x019c
                int r7 = java.lang.Math.max(r7, r8)
                goto L_0x019d
            L_0x019c:
                r7 = r8
            L_0x019d:
                int r11 = r1.mMatchConstraintMaxWidth
                if (r11 <= 0) goto L_0x01a5
                int r7 = java.lang.Math.min(r11, r7)
            L_0x01a5:
                int r11 = r1.mMatchConstraintMinHeight
                if (r11 <= 0) goto L_0x01ae
                int r11 = java.lang.Math.max(r11, r9)
                goto L_0x01af
            L_0x01ae:
                r11 = r9
            L_0x01af:
                int r5 = r1.mMatchConstraintMaxHeight
                if (r5 <= 0) goto L_0x01b7
                int r11 = java.lang.Math.min(r5, r11)
            L_0x01b7:
                r5 = 1056964608(0x3f000000, float:0.5)
                if (r12 == 0) goto L_0x01c9
                if (r4 == 0) goto L_0x01c9
                float r3 = r1.mDimensionRatio
                float r4 = (float) r11
                float r4 = r4 * r3
                float r4 = r4 + r5
                int r3 = (int) r4
                r17 = r11
                r11 = r3
                r3 = r17
                goto L_0x01d6
            L_0x01c9:
                if (r10 == 0) goto L_0x01d4
                if (r3 == 0) goto L_0x01d4
                float r3 = r1.mDimensionRatio
                float r4 = (float) r7
                float r4 = r4 / r3
                float r4 = r4 + r5
                int r3 = (int) r4
                goto L_0x01d5
            L_0x01d4:
                r3 = r11
            L_0x01d5:
                r11 = r7
            L_0x01d6:
                if (r8 != r11) goto L_0x01df
                if (r9 == r3) goto L_0x01db
                goto L_0x01df
            L_0x01db:
                r0 = r11
                r11 = r15
                r4 = -1
                goto L_0x0203
            L_0x01df:
                r4 = 1073741824(0x40000000, float:2.0)
                if (r8 == r11) goto L_0x01e7
                int r6 = android.view.View.MeasureSpec.makeMeasureSpec(r11, r4)
            L_0x01e7:
                if (r9 == r3) goto L_0x01ed
                int r0 = android.view.View.MeasureSpec.makeMeasureSpec(r3, r4)
            L_0x01ed:
                r13.measure(r6, r0)
                int r11 = r13.getMeasuredWidth()
                int r0 = r13.getMeasuredHeight()
                int r3 = r13.getBaseline()
                r4 = -1
                r17 = r3
                r3 = r0
                r0 = r11
                r11 = r17
            L_0x0203:
                if (r11 == r4) goto L_0x0207
                r4 = 1
                goto L_0x0209
            L_0x0207:
                r4 = r16
            L_0x0209:
                int r5 = r2.horizontalDimension
                if (r0 != r5) goto L_0x0215
                int r5 = r2.verticalDimension
                if (r3 == r5) goto L_0x0212
                goto L_0x0215
            L_0x0212:
                r5 = r16
                goto L_0x0216
            L_0x0215:
                r5 = 1
            L_0x0216:
                r2.measuredNeedsSolverPass = r5
                boolean r5 = r14.needsBaseline
                if (r5 == 0) goto L_0x021d
                r4 = 1
            L_0x021d:
                if (r4 == 0) goto L_0x022b
                r5 = -1
                if (r11 == r5) goto L_0x022b
                int r1 = r19.getBaselineDistance()
                if (r1 == r11) goto L_0x022b
                r1 = 1
                r2.measuredNeedsSolverPass = r1
            L_0x022b:
                r2.measuredWidth = r0
                r2.measuredHeight = r3
                r2.measuredHasBaseline = r4
                r2.measuredBaseline = r11
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.widget.ConstraintLayout.Measurer.measure(androidx.constraintlayout.solver.widgets.ConstraintWidget, androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure$Measure):void");
        }

        public final void didMeasures() {
            int childCount = this.layout.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = this.layout.getChildAt(i);
                if (childAt instanceof Placeholder) {
                    ((Placeholder) childAt).updatePostMeasure(this.layout);
                }
            }
            int size = this.layout.mConstraintHelpers.size();
            if (size > 0) {
                for (int i2 = 0; i2 < size; i2++) {
                    ((ConstraintHelper) this.layout.mConstraintHelpers.get(i2)).updatePostMeasure(this.layout);
                }
            }
        }
    }

    /* renamed from: androidx.constraintlayout.widget.ConstraintLayout$1 */
    static /* synthetic */ class C01291 {

        /* renamed from: $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintWidget$DimensionBehaviour */
        static final /* synthetic */ int[] f30xdde91696;

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        static {
            /*
                androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f30xdde91696 = r0
                androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r1 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.FIXED     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = f30xdde91696     // Catch:{ NoSuchFieldError -> 0x001d }
                androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r1 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = f30xdde91696     // Catch:{ NoSuchFieldError -> 0x0028 }
                androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r1 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_PARENT     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = f30xdde91696     // Catch:{ NoSuchFieldError -> 0x0033 }
                androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r1 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.widget.ConstraintLayout.C01291.<clinit>():void");
        }
    }

    private void init(AttributeSet attributeSet, int i, int i2) {
        this.mLayoutWidget.setCompanionWidget(this);
        this.mLayoutWidget.setMeasurer(this.mMeasurer);
        this.mChildrenByIds.put(getId(), this);
        this.mConstraintSet = null;
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.ConstraintLayout_Layout, i, i2);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i3 = 0; i3 < indexCount; i3++) {
                int index = obtainStyledAttributes.getIndex(i3);
                if (index == R$styleable.ConstraintLayout_Layout_android_minWidth) {
                    this.mMinWidth = obtainStyledAttributes.getDimensionPixelOffset(index, this.mMinWidth);
                } else if (index == R$styleable.ConstraintLayout_Layout_android_minHeight) {
                    this.mMinHeight = obtainStyledAttributes.getDimensionPixelOffset(index, this.mMinHeight);
                } else if (index == R$styleable.ConstraintLayout_Layout_android_maxWidth) {
                    this.mMaxWidth = obtainStyledAttributes.getDimensionPixelOffset(index, this.mMaxWidth);
                } else if (index == R$styleable.ConstraintLayout_Layout_android_maxHeight) {
                    this.mMaxHeight = obtainStyledAttributes.getDimensionPixelOffset(index, this.mMaxHeight);
                } else if (index == R$styleable.ConstraintLayout_Layout_layout_optimizationLevel) {
                    this.mOptimizationLevel = obtainStyledAttributes.getInt(index, this.mOptimizationLevel);
                } else if (index == R$styleable.ConstraintLayout_Layout_layoutDescription) {
                    int resourceId = obtainStyledAttributes.getResourceId(index, 0);
                    if (resourceId != 0) {
                        try {
                            parseLayoutDescription(resourceId);
                        } catch (Resources.NotFoundException unused) {
                            this.mConstraintLayoutSpec = null;
                        }
                    }
                } else if (index == R$styleable.ConstraintLayout_Layout_constraintSet) {
                    int resourceId2 = obtainStyledAttributes.getResourceId(index, 0);
                    try {
                        ConstraintSet constraintSet = new ConstraintSet();
                        this.mConstraintSet = constraintSet;
                        constraintSet.load(getContext(), resourceId2);
                    } catch (Resources.NotFoundException unused2) {
                        this.mConstraintSet = null;
                    }
                    this.mConstraintSetId = resourceId2;
                }
            }
            obtainStyledAttributes.recycle();
        }
        this.mLayoutWidget.setOptimizationLevel(this.mOptimizationLevel);
    }

    /* access modifiers changed from: protected */
    public void parseLayoutDescription(int i) {
        this.mConstraintLayoutSpec = new ConstraintLayoutStates(getContext(), this, i);
    }

    public void addView(View view, int i, ViewGroup.LayoutParams layoutParams) {
        super.addView(view, i, layoutParams);
        if (Build.VERSION.SDK_INT < 14) {
            onViewAdded(view);
        }
    }

    public void removeView(View view) {
        super.removeView(view);
        if (Build.VERSION.SDK_INT < 14) {
            onViewRemoved(view);
        }
    }

    public void onViewAdded(View view) {
        if (Build.VERSION.SDK_INT >= 14) {
            super.onViewAdded(view);
        }
        ConstraintWidget viewWidget = getViewWidget(view);
        if ((view instanceof Guideline) && !(viewWidget instanceof Guideline)) {
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            Guideline guideline = new Guideline();
            layoutParams.widget = guideline;
            layoutParams.isGuideline = true;
            guideline.setOrientation(layoutParams.orientation);
        }
        if (view instanceof ConstraintHelper) {
            ConstraintHelper constraintHelper = (ConstraintHelper) view;
            constraintHelper.validateParams();
            ((LayoutParams) view.getLayoutParams()).isHelper = true;
            if (!this.mConstraintHelpers.contains(constraintHelper)) {
                this.mConstraintHelpers.add(constraintHelper);
            }
        }
        this.mChildrenByIds.put(view.getId(), view);
        this.mDirtyHierarchy = true;
    }

    public void onViewRemoved(View view) {
        if (Build.VERSION.SDK_INT >= 14) {
            super.onViewRemoved(view);
        }
        this.mChildrenByIds.remove(view.getId());
        this.mLayoutWidget.remove(getViewWidget(view));
        this.mConstraintHelpers.remove(view);
        this.mDirtyHierarchy = true;
    }

    public void setMinWidth(int i) {
        if (i != this.mMinWidth) {
            this.mMinWidth = i;
            requestLayout();
        }
    }

    public void setMinHeight(int i) {
        if (i != this.mMinHeight) {
            this.mMinHeight = i;
            requestLayout();
        }
    }

    public int getMinWidth() {
        return this.mMinWidth;
    }

    public int getMinHeight() {
        return this.mMinHeight;
    }

    public void setMaxWidth(int i) {
        if (i != this.mMaxWidth) {
            this.mMaxWidth = i;
            requestLayout();
        }
    }

    public void setMaxHeight(int i) {
        if (i != this.mMaxHeight) {
            this.mMaxHeight = i;
            requestLayout();
        }
    }

    public int getMaxWidth() {
        return this.mMaxWidth;
    }

    public int getMaxHeight() {
        return this.mMaxHeight;
    }

    private boolean updateHierarchy() {
        int childCount = getChildCount();
        boolean z = false;
        int i = 0;
        while (true) {
            if (i >= childCount) {
                break;
            } else if (getChildAt(i).isLayoutRequested()) {
                z = true;
                break;
            } else {
                i++;
            }
        }
        if (z) {
            setChildrenConstraints();
        }
        return z;
    }

    private void setChildrenConstraints() {
        boolean isInEditMode = isInEditMode();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            ConstraintWidget viewWidget = getViewWidget(getChildAt(i));
            if (viewWidget != null) {
                viewWidget.reset();
            }
        }
        if (isInEditMode) {
            for (int i2 = 0; i2 < childCount; i2++) {
                View childAt = getChildAt(i2);
                try {
                    String resourceName = getResources().getResourceName(childAt.getId());
                    setDesignInformation(0, resourceName, Integer.valueOf(childAt.getId()));
                    int indexOf = resourceName.indexOf(47);
                    if (indexOf != -1) {
                        resourceName = resourceName.substring(indexOf + 1);
                    }
                    getTargetWidget(childAt.getId()).setDebugName(resourceName);
                } catch (Resources.NotFoundException unused) {
                }
            }
        }
        if (this.mConstraintSetId != -1) {
            for (int i3 = 0; i3 < childCount; i3++) {
                View childAt2 = getChildAt(i3);
                if (childAt2.getId() == this.mConstraintSetId && (childAt2 instanceof Constraints)) {
                    this.mConstraintSet = ((Constraints) childAt2).getConstraintSet();
                }
            }
        }
        ConstraintSet constraintSet = this.mConstraintSet;
        if (constraintSet != null) {
            constraintSet.applyToInternal(this, true);
        }
        this.mLayoutWidget.removeAllChildren();
        int size = this.mConstraintHelpers.size();
        if (size > 0) {
            for (int i4 = 0; i4 < size; i4++) {
                this.mConstraintHelpers.get(i4).updatePreLayout(this);
            }
        }
        for (int i5 = 0; i5 < childCount; i5++) {
            View childAt3 = getChildAt(i5);
            if (childAt3 instanceof Placeholder) {
                ((Placeholder) childAt3).updatePreLayout(this);
            }
        }
        this.mTempMapIdToWidget.clear();
        this.mTempMapIdToWidget.put(0, this.mLayoutWidget);
        this.mTempMapIdToWidget.put(getId(), this.mLayoutWidget);
        for (int i6 = 0; i6 < childCount; i6++) {
            View childAt4 = getChildAt(i6);
            this.mTempMapIdToWidget.put(childAt4.getId(), getViewWidget(childAt4));
        }
        for (int i7 = 0; i7 < childCount; i7++) {
            View childAt5 = getChildAt(i7);
            ConstraintWidget viewWidget2 = getViewWidget(childAt5);
            if (viewWidget2 != null) {
                this.mLayoutWidget.add(viewWidget2);
                applyConstraintsFromLayoutParams(isInEditMode, childAt5, viewWidget2, (LayoutParams) childAt5.getLayoutParams(), this.mTempMapIdToWidget);
            }
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:105:0x0212  */
    /* JADX WARNING: Removed duplicated region for block: B:113:0x0243  */
    /* JADX WARNING: Removed duplicated region for block: B:118:0x025a  */
    /* JADX WARNING: Removed duplicated region for block: B:126:0x028b  */
    /* JADX WARNING: Removed duplicated region for block: B:131:0x02a2  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00bc  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00cd  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void applyConstraintsFromLayoutParams(boolean r20, android.view.View r21, androidx.constraintlayout.solver.widgets.ConstraintWidget r22, androidx.constraintlayout.widget.ConstraintLayout.LayoutParams r23, android.util.SparseArray<androidx.constraintlayout.solver.widgets.ConstraintWidget> r24) {
        /*
            r19 = this;
            r0 = r19
            r1 = r21
            r7 = r22
            r8 = r23
            r9 = r24
            r23.validate()
            r10 = 0
            r8.helped = r10
            int r2 = r21.getVisibility()
            r7.setVisibility(r2)
            boolean r2 = r8.isInPlaceholder
            r11 = 1
            if (r2 == 0) goto L_0x0024
            r7.setInPlaceholder(r11)
            r2 = 8
            r7.setVisibility(r2)
        L_0x0024:
            r7.setCompanionWidget(r1)
            boolean r2 = r1 instanceof androidx.constraintlayout.widget.ConstraintHelper
            if (r2 == 0) goto L_0x0036
            androidx.constraintlayout.widget.ConstraintHelper r1 = (androidx.constraintlayout.widget.ConstraintHelper) r1
            androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer r2 = r0.mLayoutWidget
            boolean r2 = r2.isRtl()
            r1.resolveRtl(r7, r2)
        L_0x0036:
            boolean r1 = r8.isGuideline
            r2 = 17
            r12 = -1
            if (r1 == 0) goto L_0x0069
            r0 = r7
            androidx.constraintlayout.solver.widgets.Guideline r0 = (androidx.constraintlayout.solver.widgets.Guideline) r0
            int r1 = r8.resolvedGuideBegin
            int r3 = r8.resolvedGuideEnd
            float r4 = r8.resolvedGuidePercent
            int r5 = android.os.Build.VERSION.SDK_INT
            if (r5 >= r2) goto L_0x0050
            int r1 = r8.guideBegin
            int r3 = r8.guideEnd
            float r4 = r8.guidePercent
        L_0x0050:
            r2 = -1082130432(0xffffffffbf800000, float:-1.0)
            int r2 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1))
            if (r2 == 0) goto L_0x005b
            r0.setGuidePercent(r4)
            goto L_0x02cf
        L_0x005b:
            if (r1 == r12) goto L_0x0062
            r0.setGuideBegin(r1)
            goto L_0x02cf
        L_0x0062:
            if (r3 == r12) goto L_0x02cf
            r0.setGuideEnd(r3)
            goto L_0x02cf
        L_0x0069:
            int r1 = r8.resolvedLeftToLeft
            int r3 = r8.resolvedLeftToRight
            int r4 = r8.resolvedRightToLeft
            int r5 = r8.resolvedRightToRight
            int r6 = r8.resolveGoneLeftMargin
            int r13 = r8.resolveGoneRightMargin
            float r14 = r8.resolvedHorizontalBias
            int r15 = android.os.Build.VERSION.SDK_INT
            if (r15 >= r2) goto L_0x00b1
            int r1 = r8.leftToLeft
            int r2 = r8.leftToRight
            int r4 = r8.rightToLeft
            int r5 = r8.rightToRight
            int r3 = r8.goneLeftMargin
            int r6 = r8.goneRightMargin
            float r14 = r8.horizontalBias
            if (r1 != r12) goto L_0x0098
            if (r2 != r12) goto L_0x0098
            int r13 = r8.startToStart
            if (r13 == r12) goto L_0x0093
            r1 = r13
            goto L_0x0098
        L_0x0093:
            int r13 = r8.startToEnd
            if (r13 == r12) goto L_0x0098
            r2 = r13
        L_0x0098:
            if (r4 != r12) goto L_0x00ac
            if (r5 != r12) goto L_0x00ac
            int r13 = r8.endToStart
            if (r13 == r12) goto L_0x00a1
            goto L_0x00ad
        L_0x00a1:
            int r13 = r8.endToEnd
            if (r13 == r12) goto L_0x00ac
            r15 = r6
            r5 = r14
            r6 = r3
            r14 = r13
            r3 = r2
            r13 = r4
            goto L_0x00b8
        L_0x00ac:
            r13 = r4
        L_0x00ad:
            r15 = r6
            r6 = r3
            r3 = r2
            goto L_0x00b3
        L_0x00b1:
            r15 = r13
            r13 = r4
        L_0x00b3:
            r18 = r14
            r14 = r5
            r5 = r18
        L_0x00b8:
            int r2 = r8.circleConstraint
            if (r2 == r12) goto L_0x00cd
            java.lang.Object r0 = r9.get(r2)
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = (androidx.constraintlayout.solver.widgets.ConstraintWidget) r0
            if (r0 == 0) goto L_0x01fe
            float r1 = r8.circleAngle
            int r2 = r8.circleRadius
            r7.connectCircularConstraint(r0, r1, r2)
            goto L_0x01fe
        L_0x00cd:
            if (r1 == r12) goto L_0x00ec
            java.lang.Object r1 = r9.get(r1)
            r3 = r1
            androidx.constraintlayout.solver.widgets.ConstraintWidget r3 = (androidx.constraintlayout.solver.widgets.ConstraintWidget) r3
            if (r3 == 0) goto L_0x00e9
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r4 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.LEFT
            int r2 = r8.leftMargin
            r1 = r22
            r16 = r2
            r2 = r4
            r17 = r5
            r5 = r16
            r1.immediateConnect(r2, r3, r4, r5, r6)
            goto L_0x0104
        L_0x00e9:
            r17 = r5
            goto L_0x0104
        L_0x00ec:
            r17 = r5
            if (r3 == r12) goto L_0x0104
            java.lang.Object r1 = r9.get(r3)
            r3 = r1
            androidx.constraintlayout.solver.widgets.ConstraintWidget r3 = (androidx.constraintlayout.solver.widgets.ConstraintWidget) r3
            if (r3 == 0) goto L_0x0104
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r2 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.LEFT
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r4 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.RIGHT
            int r5 = r8.leftMargin
            r1 = r22
            r1.immediateConnect(r2, r3, r4, r5, r6)
        L_0x0104:
            if (r13 == r12) goto L_0x011c
            java.lang.Object r1 = r9.get(r13)
            r3 = r1
            androidx.constraintlayout.solver.widgets.ConstraintWidget r3 = (androidx.constraintlayout.solver.widgets.ConstraintWidget) r3
            if (r3 == 0) goto L_0x0132
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r2 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.RIGHT
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r4 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.LEFT
            int r5 = r8.rightMargin
            r1 = r22
            r6 = r15
            r1.immediateConnect(r2, r3, r4, r5, r6)
            goto L_0x0132
        L_0x011c:
            if (r14 == r12) goto L_0x0132
            java.lang.Object r1 = r9.get(r14)
            r3 = r1
            androidx.constraintlayout.solver.widgets.ConstraintWidget r3 = (androidx.constraintlayout.solver.widgets.ConstraintWidget) r3
            if (r3 == 0) goto L_0x0132
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r4 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.RIGHT
            int r5 = r8.rightMargin
            r1 = r22
            r2 = r4
            r6 = r15
            r1.immediateConnect(r2, r3, r4, r5, r6)
        L_0x0132:
            int r1 = r8.topToTop
            if (r1 == r12) goto L_0x014c
            java.lang.Object r1 = r9.get(r1)
            r3 = r1
            androidx.constraintlayout.solver.widgets.ConstraintWidget r3 = (androidx.constraintlayout.solver.widgets.ConstraintWidget) r3
            if (r3 == 0) goto L_0x0166
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r4 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.TOP
            int r5 = r8.topMargin
            int r6 = r8.goneTopMargin
            r1 = r22
            r2 = r4
            r1.immediateConnect(r2, r3, r4, r5, r6)
            goto L_0x0166
        L_0x014c:
            int r1 = r8.topToBottom
            if (r1 == r12) goto L_0x0166
            java.lang.Object r1 = r9.get(r1)
            r3 = r1
            androidx.constraintlayout.solver.widgets.ConstraintWidget r3 = (androidx.constraintlayout.solver.widgets.ConstraintWidget) r3
            if (r3 == 0) goto L_0x0166
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r2 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.TOP
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r4 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.BOTTOM
            int r5 = r8.topMargin
            int r6 = r8.goneTopMargin
            r1 = r22
            r1.immediateConnect(r2, r3, r4, r5, r6)
        L_0x0166:
            int r1 = r8.bottomToTop
            if (r1 == r12) goto L_0x0181
            java.lang.Object r1 = r9.get(r1)
            r3 = r1
            androidx.constraintlayout.solver.widgets.ConstraintWidget r3 = (androidx.constraintlayout.solver.widgets.ConstraintWidget) r3
            if (r3 == 0) goto L_0x019a
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r2 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.BOTTOM
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r4 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.TOP
            int r5 = r8.bottomMargin
            int r6 = r8.goneBottomMargin
            r1 = r22
            r1.immediateConnect(r2, r3, r4, r5, r6)
            goto L_0x019a
        L_0x0181:
            int r1 = r8.bottomToBottom
            if (r1 == r12) goto L_0x019a
            java.lang.Object r1 = r9.get(r1)
            r3 = r1
            androidx.constraintlayout.solver.widgets.ConstraintWidget r3 = (androidx.constraintlayout.solver.widgets.ConstraintWidget) r3
            if (r3 == 0) goto L_0x019a
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r4 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.BOTTOM
            int r5 = r8.bottomMargin
            int r6 = r8.goneBottomMargin
            r1 = r22
            r2 = r4
            r1.immediateConnect(r2, r3, r4, r5, r6)
        L_0x019a:
            int r1 = r8.baselineToBaseline
            if (r1 == r12) goto L_0x01eb
            android.util.SparseArray<android.view.View> r0 = r0.mChildrenByIds
            java.lang.Object r0 = r0.get(r1)
            android.view.View r0 = (android.view.View) r0
            int r1 = r8.baselineToBaseline
            java.lang.Object r1 = r9.get(r1)
            androidx.constraintlayout.solver.widgets.ConstraintWidget r1 = (androidx.constraintlayout.solver.widgets.ConstraintWidget) r1
            if (r1 == 0) goto L_0x01eb
            if (r0 == 0) goto L_0x01eb
            android.view.ViewGroup$LayoutParams r2 = r0.getLayoutParams()
            boolean r2 = r2 instanceof androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
            if (r2 == 0) goto L_0x01eb
            android.view.ViewGroup$LayoutParams r0 = r0.getLayoutParams()
            androidx.constraintlayout.widget.ConstraintLayout$LayoutParams r0 = (androidx.constraintlayout.widget.ConstraintLayout.LayoutParams) r0
            r8.needsBaseline = r11
            r0.needsBaseline = r11
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r2 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.BASELINE
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r3 = r7.getAnchor(r2)
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r1.getAnchor(r2)
            r3.connect(r1, r10, r12, r11)
            r7.setHasBaseline(r11)
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r0.widget
            r0.setHasBaseline(r11)
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r0 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.TOP
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r7.getAnchor(r0)
            r0.reset()
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r0 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.BOTTOM
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r7.getAnchor(r0)
            r0.reset()
        L_0x01eb:
            r0 = 0
            r14 = r17
            int r1 = (r14 > r0 ? 1 : (r14 == r0 ? 0 : -1))
            if (r1 < 0) goto L_0x01f5
            r7.setHorizontalBiasPercent(r14)
        L_0x01f5:
            float r1 = r8.verticalBias
            int r0 = (r1 > r0 ? 1 : (r1 == r0 ? 0 : -1))
            if (r0 < 0) goto L_0x01fe
            r7.setVerticalBiasPercent(r1)
        L_0x01fe:
            if (r20 == 0) goto L_0x020d
            int r0 = r8.editorAbsoluteX
            if (r0 != r12) goto L_0x0208
            int r1 = r8.editorAbsoluteY
            if (r1 == r12) goto L_0x020d
        L_0x0208:
            int r1 = r8.editorAbsoluteY
            r7.setOrigin(r0, r1)
        L_0x020d:
            boolean r0 = r8.horizontalDimensionFixed
            r1 = -2
            if (r0 != 0) goto L_0x0243
            int r0 = r8.width
            if (r0 != r12) goto L_0x023a
            boolean r0 = r8.constrainedWidth
            if (r0 == 0) goto L_0x0220
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r0 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            r7.setHorizontalDimensionBehaviour(r0)
            goto L_0x0225
        L_0x0220:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r0 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_PARENT
            r7.setHorizontalDimensionBehaviour(r0)
        L_0x0225:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r0 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.LEFT
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r7.getAnchor(r0)
            int r2 = r8.leftMargin
            r0.mMargin = r2
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r0 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.RIGHT
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r7.getAnchor(r0)
            int r2 = r8.rightMargin
            r0.mMargin = r2
            goto L_0x0256
        L_0x023a:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r0 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            r7.setHorizontalDimensionBehaviour(r0)
            r7.setWidth(r10)
            goto L_0x0256
        L_0x0243:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r0 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.FIXED
            r7.setHorizontalDimensionBehaviour(r0)
            int r0 = r8.width
            r7.setWidth(r0)
            int r0 = r8.width
            if (r0 != r1) goto L_0x0256
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r0 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            r7.setHorizontalDimensionBehaviour(r0)
        L_0x0256:
            boolean r0 = r8.verticalDimensionFixed
            if (r0 != 0) goto L_0x028b
            int r0 = r8.height
            if (r0 != r12) goto L_0x0282
            boolean r0 = r8.constrainedHeight
            if (r0 == 0) goto L_0x0268
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r0 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            r7.setVerticalDimensionBehaviour(r0)
            goto L_0x026d
        L_0x0268:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r0 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_PARENT
            r7.setVerticalDimensionBehaviour(r0)
        L_0x026d:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r0 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.TOP
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r7.getAnchor(r0)
            int r1 = r8.topMargin
            r0.mMargin = r1
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r0 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.BOTTOM
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r7.getAnchor(r0)
            int r1 = r8.bottomMargin
            r0.mMargin = r1
            goto L_0x029e
        L_0x0282:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r0 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            r7.setVerticalDimensionBehaviour(r0)
            r7.setHeight(r10)
            goto L_0x029e
        L_0x028b:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r0 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.FIXED
            r7.setVerticalDimensionBehaviour(r0)
            int r0 = r8.height
            r7.setHeight(r0)
            int r0 = r8.height
            if (r0 != r1) goto L_0x029e
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r0 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            r7.setVerticalDimensionBehaviour(r0)
        L_0x029e:
            java.lang.String r0 = r8.dimensionRatio
            if (r0 == 0) goto L_0x02a5
            r7.setDimensionRatio(r0)
        L_0x02a5:
            float r0 = r8.horizontalWeight
            r7.setHorizontalWeight(r0)
            float r0 = r8.verticalWeight
            r7.setVerticalWeight(r0)
            int r0 = r8.horizontalChainStyle
            r7.setHorizontalChainStyle(r0)
            int r0 = r8.verticalChainStyle
            r7.setVerticalChainStyle(r0)
            int r0 = r8.matchConstraintDefaultWidth
            int r1 = r8.matchConstraintMinWidth
            int r2 = r8.matchConstraintMaxWidth
            float r3 = r8.matchConstraintPercentWidth
            r7.setHorizontalMatchStyle(r0, r1, r2, r3)
            int r0 = r8.matchConstraintDefaultHeight
            int r1 = r8.matchConstraintMinHeight
            int r2 = r8.matchConstraintMaxHeight
            float r3 = r8.matchConstraintPercentHeight
            r7.setVerticalMatchStyle(r0, r1, r2, r3)
        L_0x02cf:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.widget.ConstraintLayout.applyConstraintsFromLayoutParams(boolean, android.view.View, androidx.constraintlayout.solver.widgets.ConstraintWidget, androidx.constraintlayout.widget.ConstraintLayout$LayoutParams, android.util.SparseArray):void");
    }

    private final ConstraintWidget getTargetWidget(int i) {
        if (i == 0) {
            return this.mLayoutWidget;
        }
        View view = this.mChildrenByIds.get(i);
        if (view == null && (view = findViewById(i)) != null && view != this && view.getParent() == this) {
            onViewAdded(view);
        }
        if (view == this) {
            return this.mLayoutWidget;
        }
        if (view == null) {
            return null;
        }
        return ((LayoutParams) view.getLayoutParams()).widget;
    }

    public final ConstraintWidget getViewWidget(View view) {
        if (view == this) {
            return this.mLayoutWidget;
        }
        if (view == null) {
            return null;
        }
        return ((LayoutParams) view.getLayoutParams()).widget;
    }

    /* access modifiers changed from: protected */
    public void resolveSystem(ConstraintWidgetContainer constraintWidgetContainer, int i, int i2, int i3) {
        int i4;
        int mode = View.MeasureSpec.getMode(i2);
        int size = View.MeasureSpec.getSize(i2);
        int mode2 = View.MeasureSpec.getMode(i3);
        int size2 = View.MeasureSpec.getSize(i3);
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom() + paddingTop;
        int paddingWidth = getPaddingWidth();
        if (Build.VERSION.SDK_INT >= 17) {
            i4 = getPaddingStart();
            int paddingEnd = getPaddingEnd();
            if (i4 <= 0 && paddingEnd <= 0) {
                i4 = getPaddingLeft();
            } else if (isRtl()) {
                i4 = paddingEnd;
            }
        } else {
            i4 = getPaddingLeft();
        }
        int i5 = i4;
        int i6 = mode;
        int i7 = size - paddingWidth;
        int i8 = mode2;
        int i9 = size2 - paddingBottom;
        setSelfDimensionBehaviour(constraintWidgetContainer, i6, i7, i8, i9);
        constraintWidgetContainer.measure(i, i6, i7, i8, i9, this.mLastMeasureWidth, this.mLastMeasureHeight, i5, paddingTop);
    }

    /* access modifiers changed from: protected */
    public void resolveMeasuredDimension(int i, int i2, int i3, int i4, boolean z, boolean z2) {
        int paddingTop = getPaddingTop() + getPaddingBottom();
        int paddingWidth = i3 + getPaddingWidth();
        int i5 = i4 + paddingTop;
        if (Build.VERSION.SDK_INT >= 11) {
            int resolveSizeAndState = ViewGroup.resolveSizeAndState(paddingWidth, i, 0);
            int min = Math.min(this.mMaxWidth, resolveSizeAndState & 16777215);
            int min2 = Math.min(this.mMaxHeight, ViewGroup.resolveSizeAndState(i5, i2, 0) & 16777215);
            if (z) {
                min |= 16777216;
            }
            if (z2) {
                min2 |= 16777216;
            }
            setMeasuredDimension(min, min2);
            this.mLastMeasureWidth = min;
            this.mLastMeasureHeight = min2;
            return;
        }
        setMeasuredDimension(paddingWidth, i5);
        this.mLastMeasureWidth = paddingWidth;
        this.mLastMeasureHeight = i5;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        this.mOnMeasureWidthMeasureSpec = i;
        this.mOnMeasureHeightMeasureSpec = i2;
        this.mLayoutWidget.setRtl(isRtl());
        if (this.mDirtyHierarchy) {
            this.mDirtyHierarchy = false;
            if (updateHierarchy()) {
                this.mLayoutWidget.updateHierarchy();
            }
        }
        resolveSystem(this.mLayoutWidget, this.mOptimizationLevel, i, i2);
        resolveMeasuredDimension(i, i2, this.mLayoutWidget.getWidth(), this.mLayoutWidget.getHeight(), this.mLayoutWidget.isWidthMeasuredTooSmall(), this.mLayoutWidget.isHeightMeasuredTooSmall());
    }

    /* access modifiers changed from: protected */
    public boolean isRtl() {
        if (Build.VERSION.SDK_INT < 17) {
            return false;
        }
        if (!((getContext().getApplicationInfo().flags & 4194304) != 0) || 1 != getLayoutDirection()) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: private */
    public int getPaddingWidth() {
        int paddingLeft = getPaddingLeft() + getPaddingRight();
        int paddingStart = Build.VERSION.SDK_INT >= 17 ? getPaddingStart() + getPaddingEnd() : 0;
        return paddingStart > 0 ? paddingStart : paddingLeft;
    }

    /* access modifiers changed from: protected */
    public void setSelfDimensionBehaviour(ConstraintWidgetContainer constraintWidgetContainer, int i, int i2, int i3, int i4) {
        ConstraintWidget.DimensionBehaviour dimensionBehaviour;
        int paddingTop = getPaddingTop() + getPaddingBottom();
        int paddingWidth = getPaddingWidth();
        ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = ConstraintWidget.DimensionBehaviour.FIXED;
        int childCount = getChildCount();
        if (i != Integer.MIN_VALUE) {
            if (i == 0) {
                dimensionBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                if (childCount == 0) {
                    i2 = Math.max(0, this.mMinWidth);
                }
            } else if (i != 1073741824) {
                dimensionBehaviour = dimensionBehaviour2;
            } else {
                i2 = Math.min(this.mMaxWidth - paddingWidth, i2);
                dimensionBehaviour = dimensionBehaviour2;
            }
            i2 = 0;
        } else {
            dimensionBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
            if (childCount == 0) {
                i2 = Math.max(0, this.mMinWidth);
            }
        }
        if (i3 != Integer.MIN_VALUE) {
            if (i3 == 0) {
                dimensionBehaviour2 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                if (childCount == 0) {
                    i4 = Math.max(0, this.mMinHeight);
                }
            } else if (i3 == 1073741824) {
                i4 = Math.min(this.mMaxHeight - paddingTop, i4);
            }
            i4 = 0;
        } else {
            dimensionBehaviour2 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
            if (childCount == 0) {
                i4 = Math.max(0, this.mMinHeight);
            }
        }
        if (!(i2 == constraintWidgetContainer.getWidth() && i4 == constraintWidgetContainer.getHeight())) {
            constraintWidgetContainer.invalidateMeasures();
        }
        constraintWidgetContainer.setX(0);
        constraintWidgetContainer.setY(0);
        constraintWidgetContainer.setMaxWidth(this.mMaxWidth - paddingWidth);
        constraintWidgetContainer.setMaxHeight(this.mMaxHeight - paddingTop);
        constraintWidgetContainer.setMinWidth(0);
        constraintWidgetContainer.setMinHeight(0);
        constraintWidgetContainer.setHorizontalDimensionBehaviour(dimensionBehaviour);
        constraintWidgetContainer.setWidth(i2);
        constraintWidgetContainer.setVerticalDimensionBehaviour(dimensionBehaviour2);
        constraintWidgetContainer.setHeight(i4);
        constraintWidgetContainer.setMinWidth(this.mMinWidth - paddingWidth);
        constraintWidgetContainer.setMinHeight(this.mMinHeight - paddingTop);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        View content;
        int childCount = getChildCount();
        boolean isInEditMode = isInEditMode();
        for (int i5 = 0; i5 < childCount; i5++) {
            View childAt = getChildAt(i5);
            LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
            ConstraintWidget constraintWidget = layoutParams.widget;
            if ((childAt.getVisibility() != 8 || layoutParams.isGuideline || layoutParams.isHelper || layoutParams.isVirtualGroup || isInEditMode) && !layoutParams.isInPlaceholder) {
                int x = constraintWidget.getX();
                int y = constraintWidget.getY();
                int width = constraintWidget.getWidth() + x;
                int height = constraintWidget.getHeight() + y;
                childAt.layout(x, y, width, height);
                if ((childAt instanceof Placeholder) && (content = ((Placeholder) childAt).getContent()) != null) {
                    content.setVisibility(0);
                    content.layout(x, y, width, height);
                }
            }
        }
        int size = this.mConstraintHelpers.size();
        if (size > 0) {
            for (int i6 = 0; i6 < size; i6++) {
                this.mConstraintHelpers.get(i6).updatePostLayout(this);
            }
        }
    }

    public void setOptimizationLevel(int i) {
        this.mOptimizationLevel = i;
        this.mLayoutWidget.setOptimizationLevel(i);
    }

    public int getOptimizationLevel() {
        return this.mLayoutWidget.getOptimizationLevel();
    }

    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    /* access modifiers changed from: protected */
    public ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    public void setConstraintSet(ConstraintSet constraintSet) {
        this.mConstraintSet = constraintSet;
    }

    public View getViewById(int i) {
        return this.mChildrenByIds.get(i);
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        Object tag;
        int size;
        ArrayList<ConstraintHelper> arrayList = this.mConstraintHelpers;
        if (arrayList != null && (size = arrayList.size()) > 0) {
            for (int i = 0; i < size; i++) {
                this.mConstraintHelpers.get(i).updatePreDraw(this);
            }
        }
        super.dispatchDraw(canvas);
        if (isInEditMode()) {
            int childCount = getChildCount();
            float width = (float) getWidth();
            float height = (float) getHeight();
            for (int i2 = 0; i2 < childCount; i2++) {
                View childAt = getChildAt(i2);
                if (!(childAt.getVisibility() == 8 || (tag = childAt.getTag()) == null || !(tag instanceof String))) {
                    String[] split = ((String) tag).split(",");
                    if (split.length == 4) {
                        int parseInt = Integer.parseInt(split[0]);
                        int parseInt2 = Integer.parseInt(split[1]);
                        int parseInt3 = Integer.parseInt(split[2]);
                        int i3 = (int) ((((float) parseInt) / 1080.0f) * width);
                        int i4 = (int) ((((float) parseInt2) / 1920.0f) * height);
                        Paint paint = new Paint();
                        paint.setColor(-65536);
                        float f = (float) i3;
                        float f2 = (float) (i3 + ((int) ((((float) parseInt3) / 1080.0f) * width)));
                        Canvas canvas2 = canvas;
                        float f3 = (float) i4;
                        float f4 = f;
                        float f5 = f;
                        float f6 = f3;
                        Paint paint2 = paint;
                        float f7 = f2;
                        Paint paint3 = paint2;
                        canvas2.drawLine(f4, f6, f7, f3, paint3);
                        float parseInt4 = (float) (i4 + ((int) ((((float) Integer.parseInt(split[3])) / 1920.0f) * height)));
                        float f8 = f2;
                        float f9 = parseInt4;
                        canvas2.drawLine(f8, f6, f7, f9, paint3);
                        float f10 = parseInt4;
                        float f11 = f5;
                        canvas2.drawLine(f8, f10, f11, f9, paint3);
                        float f12 = f5;
                        canvas2.drawLine(f12, f10, f11, f3, paint3);
                        Paint paint4 = paint2;
                        paint4.setColor(-16711936);
                        Paint paint5 = paint4;
                        float f13 = f2;
                        Paint paint6 = paint5;
                        canvas2.drawLine(f12, f3, f13, parseInt4, paint6);
                        canvas2.drawLine(f12, parseInt4, f13, f3, paint6);
                    }
                }
            }
        }
    }

    public void setOnConstraintsChanged(ConstraintsChangedListener constraintsChangedListener) {
        ConstraintLayoutStates constraintLayoutStates = this.mConstraintLayoutSpec;
        if (constraintLayoutStates != null) {
            constraintLayoutStates.setOnConstraintsChanged(constraintsChangedListener);
        }
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        public int baselineToBaseline = -1;
        public int bottomToBottom = -1;
        public int bottomToTop = -1;
        public float circleAngle = 0.0f;
        public int circleConstraint = -1;
        public int circleRadius = 0;
        public boolean constrainedHeight = false;
        public boolean constrainedWidth = false;
        public String constraintTag = null;
        public String dimensionRatio = null;
        int dimensionRatioSide = 1;
        float dimensionRatioValue = 0.0f;
        public int editorAbsoluteX = -1;
        public int editorAbsoluteY = -1;
        public int endToEnd = -1;
        public int endToStart = -1;
        public int goneBottomMargin = -1;
        public int goneEndMargin = -1;
        public int goneLeftMargin = -1;
        public int goneRightMargin = -1;
        public int goneStartMargin = -1;
        public int goneTopMargin = -1;
        public int guideBegin = -1;
        public int guideEnd = -1;
        public float guidePercent = -1.0f;
        public boolean helped = false;
        public float horizontalBias = 0.5f;
        public int horizontalChainStyle = 0;
        boolean horizontalDimensionFixed = true;
        public float horizontalWeight = -1.0f;
        boolean isGuideline = false;
        boolean isHelper = false;
        boolean isInPlaceholder = false;
        boolean isVirtualGroup = false;
        public int leftToLeft = -1;
        public int leftToRight = -1;
        public int matchConstraintDefaultHeight = 0;
        public int matchConstraintDefaultWidth = 0;
        public int matchConstraintMaxHeight = 0;
        public int matchConstraintMaxWidth = 0;
        public int matchConstraintMinHeight = 0;
        public int matchConstraintMinWidth = 0;
        public float matchConstraintPercentHeight = 1.0f;
        public float matchConstraintPercentWidth = 1.0f;
        boolean needsBaseline = false;
        public int orientation = -1;
        int resolveGoneLeftMargin = -1;
        int resolveGoneRightMargin = -1;
        int resolvedGuideBegin;
        int resolvedGuideEnd;
        float resolvedGuidePercent;
        float resolvedHorizontalBias = 0.5f;
        int resolvedLeftToLeft = -1;
        int resolvedLeftToRight = -1;
        int resolvedRightToLeft = -1;
        int resolvedRightToRight = -1;
        public int rightToLeft = -1;
        public int rightToRight = -1;
        public int startToEnd = -1;
        public int startToStart = -1;
        public int topToBottom = -1;
        public int topToTop = -1;
        public float verticalBias = 0.5f;
        public int verticalChainStyle = 0;
        boolean verticalDimensionFixed = true;
        public float verticalWeight = -1.0f;
        ConstraintWidget widget = new ConstraintWidget();

        public ConstraintWidget getConstraintWidget() {
            return this.widget;
        }

        private static class Table {
            public static final SparseIntArray map;

            static {
                SparseIntArray sparseIntArray = new SparseIntArray();
                map = sparseIntArray;
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintLeft_toLeftOf, 8);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintLeft_toRightOf, 9);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintRight_toLeftOf, 10);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintRight_toRightOf, 11);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintTop_toTopOf, 12);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintTop_toBottomOf, 13);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintBottom_toTopOf, 14);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintBottom_toBottomOf, 15);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintBaseline_toBaselineOf, 16);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintCircle, 2);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintCircleRadius, 3);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintCircleAngle, 4);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_editor_absoluteX, 49);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_editor_absoluteY, 50);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintGuide_begin, 5);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintGuide_end, 6);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintGuide_percent, 7);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_android_orientation, 1);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintStart_toEndOf, 17);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintStart_toStartOf, 18);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintEnd_toStartOf, 19);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintEnd_toEndOf, 20);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_goneMarginLeft, 21);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_goneMarginTop, 22);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_goneMarginRight, 23);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_goneMarginBottom, 24);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_goneMarginStart, 25);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_goneMarginEnd, 26);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintHorizontal_bias, 29);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintVertical_bias, 30);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintDimensionRatio, 44);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintHorizontal_weight, 45);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintVertical_weight, 46);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintHorizontal_chainStyle, 47);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintVertical_chainStyle, 48);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constrainedWidth, 27);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constrainedHeight, 28);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintWidth_default, 31);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintHeight_default, 32);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintWidth_min, 33);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintWidth_max, 34);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintWidth_percent, 35);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintHeight_min, 36);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintHeight_max, 37);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintHeight_percent, 38);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintLeft_creator, 39);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintTop_creator, 40);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintRight_creator, 41);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintBottom_creator, 42);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintBaseline_creator, 43);
                sparseIntArray.append(R$styleable.ConstraintLayout_Layout_layout_constraintTag, 51);
            }
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            int i;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.ConstraintLayout_Layout);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i2 = 0; i2 < indexCount; i2++) {
                int index = obtainStyledAttributes.getIndex(i2);
                int i3 = Table.map.get(index);
                switch (i3) {
                    case 1:
                        this.orientation = obtainStyledAttributes.getInt(index, this.orientation);
                        break;
                    case 2:
                        int resourceId = obtainStyledAttributes.getResourceId(index, this.circleConstraint);
                        this.circleConstraint = resourceId;
                        if (resourceId != -1) {
                            break;
                        } else {
                            this.circleConstraint = obtainStyledAttributes.getInt(index, -1);
                            break;
                        }
                    case 3:
                        this.circleRadius = obtainStyledAttributes.getDimensionPixelSize(index, this.circleRadius);
                        break;
                    case 4:
                        float f = obtainStyledAttributes.getFloat(index, this.circleAngle) % 360.0f;
                        this.circleAngle = f;
                        if (f >= 0.0f) {
                            break;
                        } else {
                            this.circleAngle = (360.0f - f) % 360.0f;
                            break;
                        }
                    case 5:
                        this.guideBegin = obtainStyledAttributes.getDimensionPixelOffset(index, this.guideBegin);
                        break;
                    case 6:
                        this.guideEnd = obtainStyledAttributes.getDimensionPixelOffset(index, this.guideEnd);
                        break;
                    case 7:
                        this.guidePercent = obtainStyledAttributes.getFloat(index, this.guidePercent);
                        break;
                    case 8:
                        int resourceId2 = obtainStyledAttributes.getResourceId(index, this.leftToLeft);
                        this.leftToLeft = resourceId2;
                        if (resourceId2 != -1) {
                            break;
                        } else {
                            this.leftToLeft = obtainStyledAttributes.getInt(index, -1);
                            break;
                        }
                    case 9:
                        int resourceId3 = obtainStyledAttributes.getResourceId(index, this.leftToRight);
                        this.leftToRight = resourceId3;
                        if (resourceId3 != -1) {
                            break;
                        } else {
                            this.leftToRight = obtainStyledAttributes.getInt(index, -1);
                            break;
                        }
                    case 10:
                        int resourceId4 = obtainStyledAttributes.getResourceId(index, this.rightToLeft);
                        this.rightToLeft = resourceId4;
                        if (resourceId4 != -1) {
                            break;
                        } else {
                            this.rightToLeft = obtainStyledAttributes.getInt(index, -1);
                            break;
                        }
                    case 11:
                        int resourceId5 = obtainStyledAttributes.getResourceId(index, this.rightToRight);
                        this.rightToRight = resourceId5;
                        if (resourceId5 != -1) {
                            break;
                        } else {
                            this.rightToRight = obtainStyledAttributes.getInt(index, -1);
                            break;
                        }
                    case 12:
                        int resourceId6 = obtainStyledAttributes.getResourceId(index, this.topToTop);
                        this.topToTop = resourceId6;
                        if (resourceId6 != -1) {
                            break;
                        } else {
                            this.topToTop = obtainStyledAttributes.getInt(index, -1);
                            break;
                        }
                    case 13:
                        int resourceId7 = obtainStyledAttributes.getResourceId(index, this.topToBottom);
                        this.topToBottom = resourceId7;
                        if (resourceId7 != -1) {
                            break;
                        } else {
                            this.topToBottom = obtainStyledAttributes.getInt(index, -1);
                            break;
                        }
                    case 14:
                        int resourceId8 = obtainStyledAttributes.getResourceId(index, this.bottomToTop);
                        this.bottomToTop = resourceId8;
                        if (resourceId8 != -1) {
                            break;
                        } else {
                            this.bottomToTop = obtainStyledAttributes.getInt(index, -1);
                            break;
                        }
                    case 15:
                        int resourceId9 = obtainStyledAttributes.getResourceId(index, this.bottomToBottom);
                        this.bottomToBottom = resourceId9;
                        if (resourceId9 != -1) {
                            break;
                        } else {
                            this.bottomToBottom = obtainStyledAttributes.getInt(index, -1);
                            break;
                        }
                    case 16:
                        int resourceId10 = obtainStyledAttributes.getResourceId(index, this.baselineToBaseline);
                        this.baselineToBaseline = resourceId10;
                        if (resourceId10 != -1) {
                            break;
                        } else {
                            this.baselineToBaseline = obtainStyledAttributes.getInt(index, -1);
                            break;
                        }
                    case 17:
                        int resourceId11 = obtainStyledAttributes.getResourceId(index, this.startToEnd);
                        this.startToEnd = resourceId11;
                        if (resourceId11 != -1) {
                            break;
                        } else {
                            this.startToEnd = obtainStyledAttributes.getInt(index, -1);
                            break;
                        }
                    case 18:
                        int resourceId12 = obtainStyledAttributes.getResourceId(index, this.startToStart);
                        this.startToStart = resourceId12;
                        if (resourceId12 != -1) {
                            break;
                        } else {
                            this.startToStart = obtainStyledAttributes.getInt(index, -1);
                            break;
                        }
                    case 19:
                        int resourceId13 = obtainStyledAttributes.getResourceId(index, this.endToStart);
                        this.endToStart = resourceId13;
                        if (resourceId13 != -1) {
                            break;
                        } else {
                            this.endToStart = obtainStyledAttributes.getInt(index, -1);
                            break;
                        }
                    case 20:
                        int resourceId14 = obtainStyledAttributes.getResourceId(index, this.endToEnd);
                        this.endToEnd = resourceId14;
                        if (resourceId14 != -1) {
                            break;
                        } else {
                            this.endToEnd = obtainStyledAttributes.getInt(index, -1);
                            break;
                        }
                    case 21:
                        this.goneLeftMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.goneLeftMargin);
                        break;
                    case 22:
                        this.goneTopMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.goneTopMargin);
                        break;
                    case 23:
                        this.goneRightMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.goneRightMargin);
                        break;
                    case 24:
                        this.goneBottomMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.goneBottomMargin);
                        break;
                    case 25:
                        this.goneStartMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.goneStartMargin);
                        break;
                    case 26:
                        this.goneEndMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.goneEndMargin);
                        break;
                    case 27:
                        this.constrainedWidth = obtainStyledAttributes.getBoolean(index, this.constrainedWidth);
                        break;
                    case 28:
                        this.constrainedHeight = obtainStyledAttributes.getBoolean(index, this.constrainedHeight);
                        break;
                    case 29:
                        this.horizontalBias = obtainStyledAttributes.getFloat(index, this.horizontalBias);
                        break;
                    case 30:
                        this.verticalBias = obtainStyledAttributes.getFloat(index, this.verticalBias);
                        break;
                    case 31:
                        int i4 = obtainStyledAttributes.getInt(index, 0);
                        this.matchConstraintDefaultWidth = i4;
                        if (i4 != 1) {
                            break;
                        } else {
                            Log.e("ConstraintLayout", "layout_constraintWidth_default=\"wrap\" is deprecated.\nUse layout_width=\"WRAP_CONTENT\" and layout_constrainedWidth=\"true\" instead.");
                            break;
                        }
                    case 32:
                        int i5 = obtainStyledAttributes.getInt(index, 0);
                        this.matchConstraintDefaultHeight = i5;
                        if (i5 != 1) {
                            break;
                        } else {
                            Log.e("ConstraintLayout", "layout_constraintHeight_default=\"wrap\" is deprecated.\nUse layout_height=\"WRAP_CONTENT\" and layout_constrainedHeight=\"true\" instead.");
                            break;
                        }
                    case 33:
                        try {
                            this.matchConstraintMinWidth = obtainStyledAttributes.getDimensionPixelSize(index, this.matchConstraintMinWidth);
                            break;
                        } catch (Exception unused) {
                            if (obtainStyledAttributes.getInt(index, this.matchConstraintMinWidth) != -2) {
                                break;
                            } else {
                                this.matchConstraintMinWidth = -2;
                                break;
                            }
                        }
                    case 34:
                        try {
                            this.matchConstraintMaxWidth = obtainStyledAttributes.getDimensionPixelSize(index, this.matchConstraintMaxWidth);
                            break;
                        } catch (Exception unused2) {
                            if (obtainStyledAttributes.getInt(index, this.matchConstraintMaxWidth) != -2) {
                                break;
                            } else {
                                this.matchConstraintMaxWidth = -2;
                                break;
                            }
                        }
                    case 35:
                        this.matchConstraintPercentWidth = Math.max(0.0f, obtainStyledAttributes.getFloat(index, this.matchConstraintPercentWidth));
                        this.matchConstraintDefaultWidth = 2;
                        break;
                    case 36:
                        try {
                            this.matchConstraintMinHeight = obtainStyledAttributes.getDimensionPixelSize(index, this.matchConstraintMinHeight);
                            break;
                        } catch (Exception unused3) {
                            if (obtainStyledAttributes.getInt(index, this.matchConstraintMinHeight) != -2) {
                                break;
                            } else {
                                this.matchConstraintMinHeight = -2;
                                break;
                            }
                        }
                    case 37:
                        try {
                            this.matchConstraintMaxHeight = obtainStyledAttributes.getDimensionPixelSize(index, this.matchConstraintMaxHeight);
                            break;
                        } catch (Exception unused4) {
                            if (obtainStyledAttributes.getInt(index, this.matchConstraintMaxHeight) != -2) {
                                break;
                            } else {
                                this.matchConstraintMaxHeight = -2;
                                break;
                            }
                        }
                    case 38:
                        this.matchConstraintPercentHeight = Math.max(0.0f, obtainStyledAttributes.getFloat(index, this.matchConstraintPercentHeight));
                        this.matchConstraintDefaultHeight = 2;
                        break;
                    default:
                        switch (i3) {
                            case 44:
                                String string = obtainStyledAttributes.getString(index);
                                this.dimensionRatio = string;
                                this.dimensionRatioValue = Float.NaN;
                                this.dimensionRatioSide = -1;
                                if (string == null) {
                                    break;
                                } else {
                                    int length = string.length();
                                    int indexOf = this.dimensionRatio.indexOf(44);
                                    if (indexOf <= 0 || indexOf >= length - 1) {
                                        i = 0;
                                    } else {
                                        String substring = this.dimensionRatio.substring(0, indexOf);
                                        if (substring.equalsIgnoreCase("W")) {
                                            this.dimensionRatioSide = 0;
                                        } else if (substring.equalsIgnoreCase("H")) {
                                            this.dimensionRatioSide = 1;
                                        }
                                        i = indexOf + 1;
                                    }
                                    int indexOf2 = this.dimensionRatio.indexOf(58);
                                    if (indexOf2 >= 0 && indexOf2 < length - 1) {
                                        String substring2 = this.dimensionRatio.substring(i, indexOf2);
                                        String substring3 = this.dimensionRatio.substring(indexOf2 + 1);
                                        if (substring2.length() > 0 && substring3.length() > 0) {
                                            try {
                                                float parseFloat = Float.parseFloat(substring2);
                                                float parseFloat2 = Float.parseFloat(substring3);
                                                if (parseFloat > 0.0f && parseFloat2 > 0.0f) {
                                                    if (this.dimensionRatioSide != 1) {
                                                        this.dimensionRatioValue = Math.abs(parseFloat / parseFloat2);
                                                        break;
                                                    } else {
                                                        this.dimensionRatioValue = Math.abs(parseFloat2 / parseFloat);
                                                        break;
                                                    }
                                                }
                                            } catch (NumberFormatException unused5) {
                                                break;
                                            }
                                        }
                                    } else {
                                        String substring4 = this.dimensionRatio.substring(i);
                                        if (substring4.length() <= 0) {
                                            break;
                                        } else {
                                            this.dimensionRatioValue = Float.parseFloat(substring4);
                                            break;
                                        }
                                    }
                                }
                                break;
                            case 45:
                                this.horizontalWeight = obtainStyledAttributes.getFloat(index, this.horizontalWeight);
                                break;
                            case 46:
                                this.verticalWeight = obtainStyledAttributes.getFloat(index, this.verticalWeight);
                                break;
                            case 47:
                                this.horizontalChainStyle = obtainStyledAttributes.getInt(index, 0);
                                break;
                            case 48:
                                this.verticalChainStyle = obtainStyledAttributes.getInt(index, 0);
                                break;
                            case 49:
                                this.editorAbsoluteX = obtainStyledAttributes.getDimensionPixelOffset(index, this.editorAbsoluteX);
                                break;
                            case 50:
                                this.editorAbsoluteY = obtainStyledAttributes.getDimensionPixelOffset(index, this.editorAbsoluteY);
                                break;
                            case 51:
                                this.constraintTag = obtainStyledAttributes.getString(index);
                                break;
                        }
                }
            }
            obtainStyledAttributes.recycle();
            validate();
        }

        public void validate() {
            this.isGuideline = false;
            this.horizontalDimensionFixed = true;
            this.verticalDimensionFixed = true;
            int i = this.width;
            if (i == -2 && this.constrainedWidth) {
                this.horizontalDimensionFixed = false;
                if (this.matchConstraintDefaultWidth == 0) {
                    this.matchConstraintDefaultWidth = 1;
                }
            }
            int i2 = this.height;
            if (i2 == -2 && this.constrainedHeight) {
                this.verticalDimensionFixed = false;
                if (this.matchConstraintDefaultHeight == 0) {
                    this.matchConstraintDefaultHeight = 1;
                }
            }
            if (i == 0 || i == -1) {
                this.horizontalDimensionFixed = false;
                if (i == 0 && this.matchConstraintDefaultWidth == 1) {
                    this.width = -2;
                    this.constrainedWidth = true;
                }
            }
            if (i2 == 0 || i2 == -1) {
                this.verticalDimensionFixed = false;
                if (i2 == 0 && this.matchConstraintDefaultHeight == 1) {
                    this.height = -2;
                    this.constrainedHeight = true;
                }
            }
            if (this.guidePercent != -1.0f || this.guideBegin != -1 || this.guideEnd != -1) {
                this.isGuideline = true;
                this.horizontalDimensionFixed = true;
                this.verticalDimensionFixed = true;
                if (!(this.widget instanceof Guideline)) {
                    this.widget = new Guideline();
                }
                ((Guideline) this.widget).setOrientation(this.orientation);
            }
        }

        public LayoutParams(int i, int i2) {
            super(i, i2);
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        /* JADX WARNING: Removed duplicated region for block: B:17:0x0052  */
        /* JADX WARNING: Removed duplicated region for block: B:20:0x0059  */
        /* JADX WARNING: Removed duplicated region for block: B:23:0x0060  */
        /* JADX WARNING: Removed duplicated region for block: B:26:0x0066  */
        /* JADX WARNING: Removed duplicated region for block: B:29:0x006c  */
        /* JADX WARNING: Removed duplicated region for block: B:36:0x007e  */
        /* JADX WARNING: Removed duplicated region for block: B:37:0x0086  */
        /* JADX WARNING: Removed duplicated region for block: B:41:0x0098  */
        /* JADX WARNING: Removed duplicated region for block: B:67:0x00cc  */
        /* JADX WARNING: Removed duplicated region for block: B:8:0x003f  */
        /* JADX WARNING: Removed duplicated region for block: B:95:? A[RETURN, SYNTHETIC] */
        @android.annotation.TargetApi(17)
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void resolveLayoutDirection(int r10) {
            /*
                r9 = this;
                int r0 = r9.leftMargin
                int r1 = r9.rightMargin
                int r2 = android.os.Build.VERSION.SDK_INT
                r3 = 0
                r4 = 1
                r5 = 17
                if (r2 < r5) goto L_0x0017
                super.resolveLayoutDirection(r10)
                int r10 = r9.getLayoutDirection()
                if (r4 != r10) goto L_0x0017
                r10 = r4
                goto L_0x0018
            L_0x0017:
                r10 = r3
            L_0x0018:
                r2 = -1
                r9.resolvedRightToLeft = r2
                r9.resolvedRightToRight = r2
                r9.resolvedLeftToLeft = r2
                r9.resolvedLeftToRight = r2
                r9.resolveGoneLeftMargin = r2
                r9.resolveGoneRightMargin = r2
                int r5 = r9.goneLeftMargin
                r9.resolveGoneLeftMargin = r5
                int r5 = r9.goneRightMargin
                r9.resolveGoneRightMargin = r5
                float r5 = r9.horizontalBias
                r9.resolvedHorizontalBias = r5
                int r6 = r9.guideBegin
                r9.resolvedGuideBegin = r6
                int r7 = r9.guideEnd
                r9.resolvedGuideEnd = r7
                float r8 = r9.guidePercent
                r9.resolvedGuidePercent = r8
                if (r10 == 0) goto L_0x0098
                int r10 = r9.startToEnd
                if (r10 == r2) goto L_0x0047
                r9.resolvedRightToLeft = r10
            L_0x0045:
                r3 = r4
                goto L_0x004e
            L_0x0047:
                int r10 = r9.startToStart
                if (r10 == r2) goto L_0x004e
                r9.resolvedRightToRight = r10
                goto L_0x0045
            L_0x004e:
                int r10 = r9.endToStart
                if (r10 == r2) goto L_0x0055
                r9.resolvedLeftToRight = r10
                r3 = r4
            L_0x0055:
                int r10 = r9.endToEnd
                if (r10 == r2) goto L_0x005c
                r9.resolvedLeftToLeft = r10
                r3 = r4
            L_0x005c:
                int r10 = r9.goneStartMargin
                if (r10 == r2) goto L_0x0062
                r9.resolveGoneRightMargin = r10
            L_0x0062:
                int r10 = r9.goneEndMargin
                if (r10 == r2) goto L_0x0068
                r9.resolveGoneLeftMargin = r10
            L_0x0068:
                r10 = 1065353216(0x3f800000, float:1.0)
                if (r3 == 0) goto L_0x0070
                float r3 = r10 - r5
                r9.resolvedHorizontalBias = r3
            L_0x0070:
                boolean r3 = r9.isGuideline
                if (r3 == 0) goto L_0x00bc
                int r3 = r9.orientation
                if (r3 != r4) goto L_0x00bc
                r3 = -1082130432(0xffffffffbf800000, float:-1.0)
                int r4 = (r8 > r3 ? 1 : (r8 == r3 ? 0 : -1))
                if (r4 == 0) goto L_0x0086
                float r10 = r10 - r8
                r9.resolvedGuidePercent = r10
                r9.resolvedGuideBegin = r2
                r9.resolvedGuideEnd = r2
                goto L_0x00bc
            L_0x0086:
                if (r6 == r2) goto L_0x008f
                r9.resolvedGuideEnd = r6
                r9.resolvedGuideBegin = r2
                r9.resolvedGuidePercent = r3
                goto L_0x00bc
            L_0x008f:
                if (r7 == r2) goto L_0x00bc
                r9.resolvedGuideBegin = r7
                r9.resolvedGuideEnd = r2
                r9.resolvedGuidePercent = r3
                goto L_0x00bc
            L_0x0098:
                int r10 = r9.startToEnd
                if (r10 == r2) goto L_0x009e
                r9.resolvedLeftToRight = r10
            L_0x009e:
                int r10 = r9.startToStart
                if (r10 == r2) goto L_0x00a4
                r9.resolvedLeftToLeft = r10
            L_0x00a4:
                int r10 = r9.endToStart
                if (r10 == r2) goto L_0x00aa
                r9.resolvedRightToLeft = r10
            L_0x00aa:
                int r10 = r9.endToEnd
                if (r10 == r2) goto L_0x00b0
                r9.resolvedRightToRight = r10
            L_0x00b0:
                int r10 = r9.goneStartMargin
                if (r10 == r2) goto L_0x00b6
                r9.resolveGoneLeftMargin = r10
            L_0x00b6:
                int r10 = r9.goneEndMargin
                if (r10 == r2) goto L_0x00bc
                r9.resolveGoneRightMargin = r10
            L_0x00bc:
                int r10 = r9.endToStart
                if (r10 != r2) goto L_0x0106
                int r10 = r9.endToEnd
                if (r10 != r2) goto L_0x0106
                int r10 = r9.startToStart
                if (r10 != r2) goto L_0x0106
                int r10 = r9.startToEnd
                if (r10 != r2) goto L_0x0106
                int r10 = r9.rightToLeft
                if (r10 == r2) goto L_0x00db
                r9.resolvedRightToLeft = r10
                int r10 = r9.rightMargin
                if (r10 > 0) goto L_0x00e9
                if (r1 <= 0) goto L_0x00e9
                r9.rightMargin = r1
                goto L_0x00e9
            L_0x00db:
                int r10 = r9.rightToRight
                if (r10 == r2) goto L_0x00e9
                r9.resolvedRightToRight = r10
                int r10 = r9.rightMargin
                if (r10 > 0) goto L_0x00e9
                if (r1 <= 0) goto L_0x00e9
                r9.rightMargin = r1
            L_0x00e9:
                int r10 = r9.leftToLeft
                if (r10 == r2) goto L_0x00f8
                r9.resolvedLeftToLeft = r10
                int r10 = r9.leftMargin
                if (r10 > 0) goto L_0x0106
                if (r0 <= 0) goto L_0x0106
                r9.leftMargin = r0
                goto L_0x0106
            L_0x00f8:
                int r10 = r9.leftToRight
                if (r10 == r2) goto L_0x0106
                r9.resolvedLeftToRight = r10
                int r10 = r9.leftMargin
                if (r10 > 0) goto L_0x0106
                if (r0 <= 0) goto L_0x0106
                r9.leftMargin = r0
            L_0x0106:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.resolveLayoutDirection(int):void");
        }

        public String getConstraintTag() {
            return this.constraintTag;
        }
    }

    public void requestLayout() {
        markHierarchyDirty();
        super.requestLayout();
    }

    public void forceLayout() {
        markHierarchyDirty();
        super.forceLayout();
    }

    private void markHierarchyDirty() {
        this.mDirtyHierarchy = true;
        this.mLastMeasureWidth = -1;
        this.mLastMeasureHeight = -1;
        this.mLastMeasureWidthSize = -1;
        this.mLastMeasureHeightSize = -1;
        this.mLastMeasureWidthMode = 0;
        this.mLastMeasureHeightMode = 0;
    }
}
