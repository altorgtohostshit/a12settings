package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.LinearSystem;

class Chain {
    static void applyChainConstraints(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem linearSystem, int i) {
        ChainHead[] chainHeadArr;
        int i2;
        int i3;
        if (i == 0) {
            int i4 = constraintWidgetContainer.mHorizontalChainsSize;
            chainHeadArr = constraintWidgetContainer.mHorizontalChainsArray;
            i2 = i4;
            i3 = 0;
        } else {
            i3 = 2;
            i2 = constraintWidgetContainer.mVerticalChainsSize;
            chainHeadArr = constraintWidgetContainer.mVerticalChainsArray;
        }
        for (int i5 = 0; i5 < i2; i5++) {
            ChainHead chainHead = chainHeadArr[i5];
            chainHead.define();
            applyChainConstraints(constraintWidgetContainer, linearSystem, i, i3, chainHead);
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r22v0, resolved type: androidx.constraintlayout.solver.widgets.ConstraintWidget} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v4, resolved type: androidx.constraintlayout.solver.SolverVariable} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v5, resolved type: androidx.constraintlayout.solver.SolverVariable} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r22v1, resolved type: androidx.constraintlayout.solver.widgets.ConstraintWidget} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v9, resolved type: androidx.constraintlayout.solver.SolverVariable} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r22v2, resolved type: androidx.constraintlayout.solver.widgets.ConstraintWidget} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v11, resolved type: androidx.constraintlayout.solver.SolverVariable} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r22v3, resolved type: androidx.constraintlayout.solver.widgets.ConstraintWidget} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r22v4, resolved type: androidx.constraintlayout.solver.widgets.ConstraintWidget} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v39, resolved type: androidx.constraintlayout.solver.SolverVariable} */
    /* JADX WARNING: type inference failed for: r5v10, types: [androidx.constraintlayout.solver.SolverVariable] */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x002d, code lost:
        if (r8 == 2) goto L_0x003e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x003c, code lost:
        if (r8 == 2) goto L_0x003e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0040, code lost:
        r5 = false;
     */
    /* JADX WARNING: Failed to insert additional move for type inference */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:100:0x0195  */
    /* JADX WARNING: Removed duplicated region for block: B:109:0x01c0  */
    /* JADX WARNING: Removed duplicated region for block: B:110:0x01c4  */
    /* JADX WARNING: Removed duplicated region for block: B:113:0x01ce  */
    /* JADX WARNING: Removed duplicated region for block: B:132:0x025b A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:151:0x02b4 A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:209:0x03a5  */
    /* JADX WARNING: Removed duplicated region for block: B:218:0x03bc  */
    /* JADX WARNING: Removed duplicated region for block: B:219:0x03bf  */
    /* JADX WARNING: Removed duplicated region for block: B:222:0x03c5  */
    /* JADX WARNING: Removed duplicated region for block: B:267:0x048c  */
    /* JADX WARNING: Removed duplicated region for block: B:272:0x04c1  */
    /* JADX WARNING: Removed duplicated region for block: B:277:0x04d4 A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:281:0x04e6  */
    /* JADX WARNING: Removed duplicated region for block: B:282:0x04e9  */
    /* JADX WARNING: Removed duplicated region for block: B:285:0x04ef  */
    /* JADX WARNING: Removed duplicated region for block: B:286:0x04f2  */
    /* JADX WARNING: Removed duplicated region for block: B:288:0x04f6  */
    /* JADX WARNING: Removed duplicated region for block: B:293:0x0506  */
    /* JADX WARNING: Removed duplicated region for block: B:295:0x050c A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:305:0x03a6 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:316:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static void applyChainConstraints(androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer r39, androidx.constraintlayout.solver.LinearSystem r40, int r41, int r42, androidx.constraintlayout.solver.widgets.ChainHead r43) {
        /*
            r0 = r39
            r9 = r40
            r1 = r43
            androidx.constraintlayout.solver.widgets.ConstraintWidget r10 = r1.mFirst
            androidx.constraintlayout.solver.widgets.ConstraintWidget r11 = r1.mLast
            androidx.constraintlayout.solver.widgets.ConstraintWidget r12 = r1.mFirstVisibleWidget
            androidx.constraintlayout.solver.widgets.ConstraintWidget r13 = r1.mLastVisibleWidget
            androidx.constraintlayout.solver.widgets.ConstraintWidget r2 = r1.mHead
            float r3 = r1.mTotalWeight
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r4 = r0.mListDimensionBehaviors
            r4 = r4[r41]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r5 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            r7 = 1
            if (r4 != r5) goto L_0x001d
            r4 = r7
            goto L_0x001e
        L_0x001d:
            r4 = 0
        L_0x001e:
            r5 = 2
            if (r41 != 0) goto L_0x0030
            int r8 = r2.mHorizontalChainStyle
            if (r8 != 0) goto L_0x0027
            r14 = r7
            goto L_0x0028
        L_0x0027:
            r14 = 0
        L_0x0028:
            if (r8 != r7) goto L_0x002c
            r15 = r7
            goto L_0x002d
        L_0x002c:
            r15 = 0
        L_0x002d:
            if (r8 != r5) goto L_0x0040
            goto L_0x003e
        L_0x0030:
            int r8 = r2.mVerticalChainStyle
            if (r8 != 0) goto L_0x0036
            r14 = r7
            goto L_0x0037
        L_0x0036:
            r14 = 0
        L_0x0037:
            if (r8 != r7) goto L_0x003b
            r15 = r7
            goto L_0x003c
        L_0x003b:
            r15 = 0
        L_0x003c:
            if (r8 != r5) goto L_0x0040
        L_0x003e:
            r5 = r7
            goto L_0x0041
        L_0x0040:
            r5 = 0
        L_0x0041:
            r7 = r10
            r8 = 0
        L_0x0043:
            r22 = 0
            if (r8 != 0) goto L_0x0125
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r6 = r7.mListAnchors
            r6 = r6[r42]
            if (r5 == 0) goto L_0x0050
            r23 = 1
            goto L_0x0052
        L_0x0050:
            r23 = 4
        L_0x0052:
            int r24 = r6.getMargin()
            r25 = r3
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r3 = r7.mListDimensionBehaviors
            r3 = r3[r41]
            r26 = r8
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r8 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r3 != r8) goto L_0x006c
            int[] r3 = r7.mResolvedMatchConstraintDefault
            r3 = r3[r41]
            if (r3 != 0) goto L_0x006c
            r27 = r15
            r3 = 1
            goto L_0x006f
        L_0x006c:
            r27 = r15
            r3 = 0
        L_0x006f:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r15 = r6.mTarget
            if (r15 == 0) goto L_0x007b
            if (r7 == r10) goto L_0x007b
            int r15 = r15.getMargin()
            int r24 = r24 + r15
        L_0x007b:
            r15 = r24
            if (r5 == 0) goto L_0x0088
            if (r7 == r10) goto L_0x0088
            if (r7 == r12) goto L_0x0088
            r24 = r14
            r23 = 5
            goto L_0x008a
        L_0x0088:
            r24 = r14
        L_0x008a:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r14 = r6.mTarget
            if (r14 == 0) goto L_0x00bb
            if (r7 != r12) goto L_0x009d
            r28 = r2
            androidx.constraintlayout.solver.SolverVariable r2 = r6.mSolverVariable
            androidx.constraintlayout.solver.SolverVariable r14 = r14.mSolverVariable
            r29 = r10
            r10 = 6
            r9.addGreaterThan(r2, r14, r15, r10)
            goto L_0x00a9
        L_0x009d:
            r28 = r2
            r29 = r10
            androidx.constraintlayout.solver.SolverVariable r2 = r6.mSolverVariable
            androidx.constraintlayout.solver.SolverVariable r10 = r14.mSolverVariable
            r14 = 7
            r9.addGreaterThan(r2, r10, r15, r14)
        L_0x00a9:
            if (r3 == 0) goto L_0x00af
            if (r5 != 0) goto L_0x00af
            r2 = 5
            goto L_0x00b1
        L_0x00af:
            r2 = r23
        L_0x00b1:
            androidx.constraintlayout.solver.SolverVariable r3 = r6.mSolverVariable
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r6 = r6.mTarget
            androidx.constraintlayout.solver.SolverVariable r6 = r6.mSolverVariable
            r9.addEquality(r3, r6, r15, r2)
            goto L_0x00bf
        L_0x00bb:
            r28 = r2
            r29 = r10
        L_0x00bf:
            if (r4 == 0) goto L_0x00f2
            int r2 = r7.getVisibility()
            r3 = 8
            if (r2 == r3) goto L_0x00e1
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r2 = r7.mListDimensionBehaviors
            r2 = r2[r41]
            if (r2 != r8) goto L_0x00e1
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r2 = r7.mListAnchors
            int r3 = r42 + 1
            r3 = r2[r3]
            androidx.constraintlayout.solver.SolverVariable r3 = r3.mSolverVariable
            r2 = r2[r42]
            androidx.constraintlayout.solver.SolverVariable r2 = r2.mSolverVariable
            r6 = 5
            r8 = 0
            r9.addGreaterThan(r3, r2, r8, r6)
            goto L_0x00e2
        L_0x00e1:
            r8 = 0
        L_0x00e2:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r2 = r7.mListAnchors
            r2 = r2[r42]
            androidx.constraintlayout.solver.SolverVariable r2 = r2.mSolverVariable
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r3 = r0.mListAnchors
            r3 = r3[r42]
            androidx.constraintlayout.solver.SolverVariable r3 = r3.mSolverVariable
            r6 = 7
            r9.addGreaterThan(r2, r3, r8, r6)
        L_0x00f2:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r2 = r7.mListAnchors
            int r3 = r42 + 1
            r2 = r2[r3]
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r2.mTarget
            if (r2 == 0) goto L_0x0111
            androidx.constraintlayout.solver.widgets.ConstraintWidget r2 = r2.mOwner
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r3 = r2.mListAnchors
            r6 = r3[r42]
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r6 = r6.mTarget
            if (r6 == 0) goto L_0x0111
            r3 = r3[r42]
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r3 = r3.mTarget
            androidx.constraintlayout.solver.widgets.ConstraintWidget r3 = r3.mOwner
            if (r3 == r7) goto L_0x010f
            goto L_0x0111
        L_0x010f:
            r22 = r2
        L_0x0111:
            if (r22 == 0) goto L_0x0118
            r7 = r22
            r8 = r26
            goto L_0x0119
        L_0x0118:
            r8 = 1
        L_0x0119:
            r14 = r24
            r3 = r25
            r15 = r27
            r2 = r28
            r10 = r29
            goto L_0x0043
        L_0x0125:
            r28 = r2
            r25 = r3
            r29 = r10
            r24 = r14
            r27 = r15
            if (r13 == 0) goto L_0x0192
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r2 = r11.mListAnchors
            int r3 = r42 + 1
            r2 = r2[r3]
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r2.mTarget
            if (r2 == 0) goto L_0x0192
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r2 = r13.mListAnchors
            r2 = r2[r3]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r6 = r13.mListDimensionBehaviors
            r6 = r6[r41]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r7 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r6 != r7) goto L_0x014f
            int[] r6 = r13.mResolvedMatchConstraintDefault
            r6 = r6[r41]
            if (r6 != 0) goto L_0x014f
            r6 = 1
            goto L_0x0150
        L_0x014f:
            r6 = 0
        L_0x0150:
            if (r6 == 0) goto L_0x0168
            if (r5 != 0) goto L_0x0168
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r6 = r2.mTarget
            androidx.constraintlayout.solver.widgets.ConstraintWidget r7 = r6.mOwner
            if (r7 != r0) goto L_0x0168
            androidx.constraintlayout.solver.SolverVariable r7 = r2.mSolverVariable
            androidx.constraintlayout.solver.SolverVariable r6 = r6.mSolverVariable
            int r8 = r2.getMargin()
            int r8 = -r8
            r10 = 5
            r9.addEquality(r7, r6, r8, r10)
            goto L_0x017e
        L_0x0168:
            r10 = 5
            if (r5 == 0) goto L_0x017e
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r6 = r2.mTarget
            androidx.constraintlayout.solver.widgets.ConstraintWidget r7 = r6.mOwner
            if (r7 != r0) goto L_0x017e
            androidx.constraintlayout.solver.SolverVariable r7 = r2.mSolverVariable
            androidx.constraintlayout.solver.SolverVariable r6 = r6.mSolverVariable
            int r8 = r2.getMargin()
            int r8 = -r8
            r14 = 4
            r9.addEquality(r7, r6, r8, r14)
        L_0x017e:
            androidx.constraintlayout.solver.SolverVariable r6 = r2.mSolverVariable
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r7 = r11.mListAnchors
            r3 = r7[r3]
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r3 = r3.mTarget
            androidx.constraintlayout.solver.SolverVariable r3 = r3.mSolverVariable
            int r2 = r2.getMargin()
            int r2 = -r2
            r7 = 6
            r9.addLowerThan(r6, r3, r2, r7)
            goto L_0x0193
        L_0x0192:
            r10 = 5
        L_0x0193:
            if (r4 == 0) goto L_0x01ad
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r0 = r0.mListAnchors
            int r2 = r42 + 1
            r0 = r0[r2]
            androidx.constraintlayout.solver.SolverVariable r0 = r0.mSolverVariable
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r3 = r11.mListAnchors
            r4 = r3[r2]
            androidx.constraintlayout.solver.SolverVariable r4 = r4.mSolverVariable
            r2 = r3[r2]
            int r2 = r2.getMargin()
            r3 = 7
            r9.addGreaterThan(r0, r4, r2, r3)
        L_0x01ad:
            java.util.ArrayList<androidx.constraintlayout.solver.widgets.ConstraintWidget> r0 = r1.mWeightedMatchConstraintsWidgets
            if (r0 == 0) goto L_0x0257
            int r2 = r0.size()
            r3 = 1
            if (r2 <= r3) goto L_0x0257
            boolean r4 = r1.mHasUndefinedWeights
            if (r4 == 0) goto L_0x01c4
            boolean r4 = r1.mHasComplexMatchWeights
            if (r4 != 0) goto L_0x01c4
            int r4 = r1.mWidgetsMatchCount
            float r4 = (float) r4
            goto L_0x01c6
        L_0x01c4:
            r4 = r25
        L_0x01c6:
            r6 = 0
            r31 = r6
            r7 = r22
            r8 = 0
        L_0x01cc:
            if (r8 >= r2) goto L_0x0257
            java.lang.Object r14 = r0.get(r8)
            androidx.constraintlayout.solver.widgets.ConstraintWidget r14 = (androidx.constraintlayout.solver.widgets.ConstraintWidget) r14
            float[] r15 = r14.mWeight
            r15 = r15[r41]
            int r16 = (r15 > r6 ? 1 : (r15 == r6 ? 0 : -1))
            if (r16 >= 0) goto L_0x01f8
            boolean r15 = r1.mHasComplexMatchWeights
            if (r15 == 0) goto L_0x01f4
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r14 = r14.mListAnchors
            int r15 = r42 + 1
            r15 = r14[r15]
            androidx.constraintlayout.solver.SolverVariable r15 = r15.mSolverVariable
            r14 = r14[r42]
            androidx.constraintlayout.solver.SolverVariable r14 = r14.mSolverVariable
            r3 = 0
            r10 = 4
            r9.addEquality(r15, r14, r3, r10)
            r6 = r3
            r15 = 7
            goto L_0x020e
        L_0x01f4:
            r10 = 4
            r15 = 1065353216(0x3f800000, float:1.0)
            goto L_0x01f9
        L_0x01f8:
            r10 = 4
        L_0x01f9:
            int r3 = (r15 > r6 ? 1 : (r15 == r6 ? 0 : -1))
            if (r3 != 0) goto L_0x0213
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r3 = r14.mListAnchors
            int r14 = r42 + 1
            r14 = r3[r14]
            androidx.constraintlayout.solver.SolverVariable r14 = r14.mSolverVariable
            r3 = r3[r42]
            androidx.constraintlayout.solver.SolverVariable r3 = r3.mSolverVariable
            r6 = 0
            r15 = 7
            r9.addEquality(r14, r3, r6, r15)
        L_0x020e:
            r18 = r0
            r17 = r15
            goto L_0x024e
        L_0x0213:
            r6 = 0
            r17 = 7
            if (r7 == 0) goto L_0x0249
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r3 = r7.mListAnchors
            r7 = r3[r42]
            androidx.constraintlayout.solver.SolverVariable r7 = r7.mSolverVariable
            int r18 = r42 + 1
            r3 = r3[r18]
            androidx.constraintlayout.solver.SolverVariable r3 = r3.mSolverVariable
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r6 = r14.mListAnchors
            r10 = r6[r42]
            androidx.constraintlayout.solver.SolverVariable r10 = r10.mSolverVariable
            r6 = r6[r18]
            androidx.constraintlayout.solver.SolverVariable r6 = r6.mSolverVariable
            r18 = r0
            androidx.constraintlayout.solver.ArrayRow r0 = r40.createRow()
            r30 = r0
            r32 = r4
            r33 = r15
            r34 = r7
            r35 = r3
            r36 = r10
            r37 = r6
            r30.createRowEqualMatchDimensions(r31, r32, r33, r34, r35, r36, r37)
            r9.addConstraint(r0)
            goto L_0x024b
        L_0x0249:
            r18 = r0
        L_0x024b:
            r7 = r14
            r31 = r15
        L_0x024e:
            int r8 = r8 + 1
            r0 = r18
            r3 = 1
            r6 = 0
            r10 = 5
            goto L_0x01cc
        L_0x0257:
            r17 = 7
            if (r12 == 0) goto L_0x02b0
            if (r12 == r13) goto L_0x025f
            if (r5 == 0) goto L_0x02b0
        L_0x025f:
            r10 = r29
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r0 = r10.mListAnchors
            r0 = r0[r42]
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r1 = r11.mListAnchors
            int r2 = r42 + 1
            r1 = r1[r2]
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.mTarget
            if (r0 == 0) goto L_0x0273
            androidx.constraintlayout.solver.SolverVariable r0 = r0.mSolverVariable
            r3 = r0
            goto L_0x0275
        L_0x0273:
            r3 = r22
        L_0x0275:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r1.mTarget
            if (r0 == 0) goto L_0x027d
            androidx.constraintlayout.solver.SolverVariable r0 = r0.mSolverVariable
            r5 = r0
            goto L_0x027f
        L_0x027d:
            r5 = r22
        L_0x027f:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r0 = r12.mListAnchors
            r0 = r0[r42]
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r1 = r13.mListAnchors
            r1 = r1[r2]
            if (r3 == 0) goto L_0x04d2
            if (r5 == 0) goto L_0x04d2
            if (r41 != 0) goto L_0x0292
            r2 = r28
            float r2 = r2.mHorizontalBiasPercent
            goto L_0x0296
        L_0x0292:
            r2 = r28
            float r2 = r2.mVerticalBiasPercent
        L_0x0296:
            r4 = r2
            int r6 = r0.getMargin()
            int r7 = r1.getMargin()
            androidx.constraintlayout.solver.SolverVariable r2 = r0.mSolverVariable
            androidx.constraintlayout.solver.SolverVariable r8 = r1.mSolverVariable
            r10 = 5
            r0 = r40
            r1 = r2
            r2 = r3
            r3 = r6
            r6 = r8
            r8 = r10
            r0.addCentering(r1, r2, r3, r4, r5, r6, r7, r8)
            goto L_0x04d2
        L_0x02b0:
            r10 = r29
            if (r24 == 0) goto L_0x03ac
            if (r12 == 0) goto L_0x03ac
            int r0 = r1.mWidgetsMatchCount
            if (r0 <= 0) goto L_0x02c1
            int r1 = r1.mWidgetsCount
            if (r1 != r0) goto L_0x02c1
            r16 = 1
            goto L_0x02c3
        L_0x02c1:
            r16 = 0
        L_0x02c3:
            r14 = r12
            r15 = r14
        L_0x02c5:
            if (r14 == 0) goto L_0x04d2
            androidx.constraintlayout.solver.widgets.ConstraintWidget[] r0 = r14.mNextChainWidget
            r0 = r0[r41]
            r8 = r0
        L_0x02cc:
            if (r8 == 0) goto L_0x02db
            int r0 = r8.getVisibility()
            r6 = 8
            if (r0 != r6) goto L_0x02dd
            androidx.constraintlayout.solver.widgets.ConstraintWidget[] r0 = r8.mNextChainWidget
            r8 = r0[r41]
            goto L_0x02cc
        L_0x02db:
            r6 = 8
        L_0x02dd:
            if (r8 != 0) goto L_0x02ea
            if (r14 != r13) goto L_0x02e2
            goto L_0x02ea
        L_0x02e2:
            r18 = r8
            r19 = r17
            r17 = 5
            goto L_0x039d
        L_0x02ea:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r0 = r14.mListAnchors
            r0 = r0[r42]
            androidx.constraintlayout.solver.SolverVariable r1 = r0.mSolverVariable
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r0.mTarget
            if (r2 == 0) goto L_0x02f7
            androidx.constraintlayout.solver.SolverVariable r2 = r2.mSolverVariable
            goto L_0x02f9
        L_0x02f7:
            r2 = r22
        L_0x02f9:
            if (r15 == r14) goto L_0x0304
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r2 = r15.mListAnchors
            int r3 = r42 + 1
            r2 = r2[r3]
            androidx.constraintlayout.solver.SolverVariable r2 = r2.mSolverVariable
            goto L_0x0319
        L_0x0304:
            if (r14 != r12) goto L_0x0319
            if (r15 != r14) goto L_0x0319
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r2 = r10.mListAnchors
            r3 = r2[r42]
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r3 = r3.mTarget
            if (r3 == 0) goto L_0x0317
            r2 = r2[r42]
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r2.mTarget
            androidx.constraintlayout.solver.SolverVariable r2 = r2.mSolverVariable
            goto L_0x0319
        L_0x0317:
            r2 = r22
        L_0x0319:
            int r0 = r0.getMargin()
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r3 = r14.mListAnchors
            int r4 = r42 + 1
            r3 = r3[r4]
            int r3 = r3.getMargin()
            if (r8 == 0) goto L_0x033b
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r5 = r8.mListAnchors
            r5 = r5[r42]
            androidx.constraintlayout.solver.SolverVariable r7 = r5.mSolverVariable
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r6 = r14.mListAnchors
            r6 = r6[r4]
            androidx.constraintlayout.solver.SolverVariable r6 = r6.mSolverVariable
            r38 = r7
            r7 = r6
            r6 = r38
            goto L_0x034e
        L_0x033b:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r5 = r11.mListAnchors
            r5 = r5[r4]
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r5 = r5.mTarget
            if (r5 == 0) goto L_0x0346
            androidx.constraintlayout.solver.SolverVariable r6 = r5.mSolverVariable
            goto L_0x0348
        L_0x0346:
            r6 = r22
        L_0x0348:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r7 = r14.mListAnchors
            r7 = r7[r4]
            androidx.constraintlayout.solver.SolverVariable r7 = r7.mSolverVariable
        L_0x034e:
            if (r5 == 0) goto L_0x0355
            int r5 = r5.getMargin()
            int r3 = r3 + r5
        L_0x0355:
            if (r15 == 0) goto L_0x0360
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r5 = r15.mListAnchors
            r5 = r5[r4]
            int r5 = r5.getMargin()
            int r0 = r0 + r5
        L_0x0360:
            if (r1 == 0) goto L_0x02e2
            if (r2 == 0) goto L_0x02e2
            if (r6 == 0) goto L_0x02e2
            if (r7 == 0) goto L_0x02e2
            if (r14 != r12) goto L_0x0372
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r0 = r12.mListAnchors
            r0 = r0[r42]
            int r0 = r0.getMargin()
        L_0x0372:
            r5 = r0
            if (r14 != r13) goto L_0x0380
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r0 = r13.mListAnchors
            r0 = r0[r4]
            int r0 = r0.getMargin()
            r18 = r0
            goto L_0x0382
        L_0x0380:
            r18 = r3
        L_0x0382:
            if (r16 == 0) goto L_0x0387
            r21 = r17
            goto L_0x0389
        L_0x0387:
            r21 = 5
        L_0x0389:
            r4 = 1056964608(0x3f000000, float:0.5)
            r0 = r40
            r3 = r5
            r5 = r6
            r19 = r17
            r17 = 5
            r6 = r7
            r7 = r18
            r18 = r8
            r8 = r21
            r0.addCentering(r1, r2, r3, r4, r5, r6, r7, r8)
        L_0x039d:
            int r0 = r14.getVisibility()
            r8 = 8
            if (r0 == r8) goto L_0x03a6
            r15 = r14
        L_0x03a6:
            r14 = r18
            r17 = r19
            goto L_0x02c5
        L_0x03ac:
            r19 = r17
            r8 = 8
            if (r27 == 0) goto L_0x04d2
            if (r12 == 0) goto L_0x04d2
            int r0 = r1.mWidgetsMatchCount
            if (r0 <= 0) goto L_0x03bf
            int r1 = r1.mWidgetsCount
            if (r1 != r0) goto L_0x03bf
            r16 = 1
            goto L_0x03c1
        L_0x03bf:
            r16 = 0
        L_0x03c1:
            r14 = r12
            r15 = r14
        L_0x03c3:
            if (r14 == 0) goto L_0x0474
            androidx.constraintlayout.solver.widgets.ConstraintWidget[] r0 = r14.mNextChainWidget
            r0 = r0[r41]
        L_0x03c9:
            if (r0 == 0) goto L_0x03d6
            int r1 = r0.getVisibility()
            if (r1 != r8) goto L_0x03d6
            androidx.constraintlayout.solver.widgets.ConstraintWidget[] r0 = r0.mNextChainWidget
            r0 = r0[r41]
            goto L_0x03c9
        L_0x03d6:
            if (r14 == r12) goto L_0x0463
            if (r14 == r13) goto L_0x0463
            if (r0 == 0) goto L_0x0463
            if (r0 != r13) goto L_0x03e1
            r7 = r22
            goto L_0x03e2
        L_0x03e1:
            r7 = r0
        L_0x03e2:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r0 = r14.mListAnchors
            r0 = r0[r42]
            androidx.constraintlayout.solver.SolverVariable r1 = r0.mSolverVariable
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r0.mTarget
            if (r2 == 0) goto L_0x03ee
            androidx.constraintlayout.solver.SolverVariable r2 = r2.mSolverVariable
        L_0x03ee:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r2 = r15.mListAnchors
            int r3 = r42 + 1
            r2 = r2[r3]
            androidx.constraintlayout.solver.SolverVariable r2 = r2.mSolverVariable
            int r0 = r0.getMargin()
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r4 = r14.mListAnchors
            r4 = r4[r3]
            int r4 = r4.getMargin()
            if (r7 == 0) goto L_0x0414
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r5 = r7.mListAnchors
            r5 = r5[r42]
            androidx.constraintlayout.solver.SolverVariable r6 = r5.mSolverVariable
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r8 = r5.mTarget
            if (r8 == 0) goto L_0x0411
            androidx.constraintlayout.solver.SolverVariable r8 = r8.mSolverVariable
            goto L_0x0425
        L_0x0411:
            r8 = r22
            goto L_0x0425
        L_0x0414:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r5 = r13.mListAnchors
            r5 = r5[r42]
            if (r5 == 0) goto L_0x041d
            androidx.constraintlayout.solver.SolverVariable r6 = r5.mSolverVariable
            goto L_0x041f
        L_0x041d:
            r6 = r22
        L_0x041f:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r8 = r14.mListAnchors
            r8 = r8[r3]
            androidx.constraintlayout.solver.SolverVariable r8 = r8.mSolverVariable
        L_0x0425:
            if (r5 == 0) goto L_0x042c
            int r5 = r5.getMargin()
            int r4 = r4 + r5
        L_0x042c:
            r17 = r4
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r4 = r15.mListAnchors
            r3 = r4[r3]
            int r3 = r3.getMargin()
            int r3 = r3 + r0
            if (r16 == 0) goto L_0x043c
            r18 = r19
            goto L_0x043e
        L_0x043c:
            r18 = 4
        L_0x043e:
            if (r1 == 0) goto L_0x045a
            if (r2 == 0) goto L_0x045a
            if (r6 == 0) goto L_0x045a
            if (r8 == 0) goto L_0x045a
            r4 = 1056964608(0x3f000000, float:0.5)
            r0 = r40
            r5 = r6
            r6 = r8
            r20 = r7
            r7 = r17
            r17 = r15
            r15 = 8
            r8 = r18
            r0.addCentering(r1, r2, r3, r4, r5, r6, r7, r8)
            goto L_0x0460
        L_0x045a:
            r20 = r7
            r17 = r15
            r15 = 8
        L_0x0460:
            r0 = r20
            goto L_0x0466
        L_0x0463:
            r17 = r15
            r15 = r8
        L_0x0466:
            int r1 = r14.getVisibility()
            if (r1 == r15) goto L_0x046d
            goto L_0x046f
        L_0x046d:
            r14 = r17
        L_0x046f:
            r8 = r15
            r15 = r14
            r14 = r0
            goto L_0x03c3
        L_0x0474:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r0 = r12.mListAnchors
            r0 = r0[r42]
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r1 = r10.mListAnchors
            r1 = r1[r42]
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r1.mTarget
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r2 = r13.mListAnchors
            int r3 = r42 + 1
            r10 = r2[r3]
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r2 = r11.mListAnchors
            r2 = r2[r3]
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r14 = r2.mTarget
            if (r1 == 0) goto L_0x04c1
            if (r12 == r13) goto L_0x049b
            androidx.constraintlayout.solver.SolverVariable r2 = r0.mSolverVariable
            androidx.constraintlayout.solver.SolverVariable r1 = r1.mSolverVariable
            int r0 = r0.getMargin()
            r15 = 4
            r9.addEquality(r2, r1, r0, r15)
            goto L_0x04c2
        L_0x049b:
            r15 = 4
            if (r14 == 0) goto L_0x04c2
            androidx.constraintlayout.solver.SolverVariable r2 = r0.mSolverVariable
            androidx.constraintlayout.solver.SolverVariable r3 = r1.mSolverVariable
            int r4 = r0.getMargin()
            r5 = 1056964608(0x3f000000, float:0.5)
            androidx.constraintlayout.solver.SolverVariable r6 = r10.mSolverVariable
            androidx.constraintlayout.solver.SolverVariable r7 = r14.mSolverVariable
            int r8 = r10.getMargin()
            r16 = 4
            r0 = r40
            r1 = r2
            r2 = r3
            r3 = r4
            r4 = r5
            r5 = r6
            r6 = r7
            r7 = r8
            r8 = r16
            r0.addCentering(r1, r2, r3, r4, r5, r6, r7, r8)
            goto L_0x04c2
        L_0x04c1:
            r15 = 4
        L_0x04c2:
            if (r14 == 0) goto L_0x04d2
            if (r12 == r13) goto L_0x04d2
            androidx.constraintlayout.solver.SolverVariable r0 = r10.mSolverVariable
            androidx.constraintlayout.solver.SolverVariable r1 = r14.mSolverVariable
            int r2 = r10.getMargin()
            int r2 = -r2
            r9.addEquality(r0, r1, r2, r15)
        L_0x04d2:
            if (r24 != 0) goto L_0x04d6
            if (r27 == 0) goto L_0x052c
        L_0x04d6:
            if (r12 == 0) goto L_0x052c
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r0 = r12.mListAnchors
            r1 = r0[r42]
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r2 = r13.mListAnchors
            int r3 = r42 + 1
            r2 = r2[r3]
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r4 = r1.mTarget
            if (r4 == 0) goto L_0x04e9
            androidx.constraintlayout.solver.SolverVariable r4 = r4.mSolverVariable
            goto L_0x04eb
        L_0x04e9:
            r4 = r22
        L_0x04eb:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r5 = r2.mTarget
            if (r5 == 0) goto L_0x04f2
            androidx.constraintlayout.solver.SolverVariable r5 = r5.mSolverVariable
            goto L_0x04f4
        L_0x04f2:
            r5 = r22
        L_0x04f4:
            if (r11 == r13) goto L_0x0504
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r5 = r11.mListAnchors
            r5 = r5[r3]
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r5 = r5.mTarget
            if (r5 == 0) goto L_0x0502
            androidx.constraintlayout.solver.SolverVariable r5 = r5.mSolverVariable
            r22 = r5
        L_0x0502:
            r5 = r22
        L_0x0504:
            if (r12 != r13) goto L_0x050a
            r1 = r0[r42]
            r2 = r0[r3]
        L_0x050a:
            if (r4 == 0) goto L_0x052c
            if (r5 == 0) goto L_0x052c
            r6 = 1056964608(0x3f000000, float:0.5)
            int r7 = r1.getMargin()
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r0 = r13.mListAnchors
            r0 = r0[r3]
            int r8 = r0.getMargin()
            androidx.constraintlayout.solver.SolverVariable r1 = r1.mSolverVariable
            androidx.constraintlayout.solver.SolverVariable r10 = r2.mSolverVariable
            r11 = 5
            r0 = r40
            r2 = r4
            r3 = r7
            r4 = r6
            r6 = r10
            r7 = r8
            r8 = r11
            r0.addCentering(r1, r2, r3, r4, r5, r6, r7, r8)
        L_0x052c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.Chain.applyChainConstraints(androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer, androidx.constraintlayout.solver.LinearSystem, int, int, androidx.constraintlayout.solver.widgets.ChainHead):void");
    }
}
