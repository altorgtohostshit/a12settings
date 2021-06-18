package com.android.settings.localepicker;

import android.content.Context;
import android.view.View;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R;

public class LocaleLinearLayoutManager extends LinearLayoutManager {
    private final AccessibilityNodeInfoCompat.AccessibilityActionCompat mActionMoveBottom;
    private final AccessibilityNodeInfoCompat.AccessibilityActionCompat mActionMoveDown;
    private final AccessibilityNodeInfoCompat.AccessibilityActionCompat mActionMoveTop;
    private final AccessibilityNodeInfoCompat.AccessibilityActionCompat mActionMoveUp;
    private final AccessibilityNodeInfoCompat.AccessibilityActionCompat mActionRemove;
    private final LocaleDragAndDropAdapter mAdapter;
    private final Context mContext;

    public LocaleLinearLayoutManager(Context context, LocaleDragAndDropAdapter localeDragAndDropAdapter) {
        super(context);
        this.mContext = context;
        this.mAdapter = localeDragAndDropAdapter;
        this.mActionMoveUp = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(R.id.action_drag_move_up, context.getString(R.string.action_drag_label_move_up));
        this.mActionMoveDown = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(R.id.action_drag_move_down, context.getString(R.string.action_drag_label_move_down));
        this.mActionMoveTop = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(R.id.action_drag_move_top, context.getString(R.string.action_drag_label_move_top));
        this.mActionMoveBottom = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(R.id.action_drag_move_bottom, context.getString(R.string.action_drag_label_move_bottom));
        this.mActionRemove = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(R.id.action_drag_remove, context.getString(R.string.action_drag_label_remove));
    }

    public void onInitializeAccessibilityNodeInfoForItem(RecyclerView.Recycler recycler, RecyclerView.State state, View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        super.onInitializeAccessibilityNodeInfoForItem(recycler, state, view, accessibilityNodeInfoCompat);
        int itemCount = getItemCount();
        int position = getPosition(view);
        StringBuilder sb = new StringBuilder();
        int i = position + 1;
        sb.append(i);
        sb.append(", ");
        sb.append(((LocaleDragCell) view).getCheckbox().getContentDescription());
        accessibilityNodeInfoCompat.setContentDescription(sb.toString());
        if (!this.mAdapter.isRemoveMode()) {
            if (position > 0) {
                accessibilityNodeInfoCompat.addAction(this.mActionMoveUp);
                accessibilityNodeInfoCompat.addAction(this.mActionMoveTop);
            }
            if (i < itemCount) {
                accessibilityNodeInfoCompat.addAction(this.mActionMoveDown);
                accessibilityNodeInfoCompat.addAction(this.mActionMoveBottom);
            }
            if (itemCount > 1) {
                accessibilityNodeInfoCompat.addAction(this.mActionRemove);
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x0053  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean performAccessibilityActionForItem(androidx.recyclerview.widget.RecyclerView.Recycler r6, androidx.recyclerview.widget.RecyclerView.State r7, android.view.View r8, int r9, android.os.Bundle r10) {
        /*
            r5 = this;
            int r0 = r5.getItemCount()
            int r1 = r5.getPosition(r8)
            r2 = 0
            r3 = 1
            r4 = 2131558475(0x7f0d004b, float:1.8742267E38)
            if (r9 != r4) goto L_0x001a
            if (r1 <= 0) goto L_0x0051
            com.android.settings.localepicker.LocaleDragAndDropAdapter r6 = r5.mAdapter
            int r7 = r1 + -1
            r6.onItemMove(r1, r7)
        L_0x0018:
            r2 = r3
            goto L_0x0051
        L_0x001a:
            r4 = 2131558473(0x7f0d0049, float:1.8742263E38)
            if (r9 != r4) goto L_0x0029
            int r6 = r1 + 1
            if (r6 >= r0) goto L_0x0051
            com.android.settings.localepicker.LocaleDragAndDropAdapter r7 = r5.mAdapter
            r7.onItemMove(r1, r6)
            goto L_0x0018
        L_0x0029:
            r4 = 2131558474(0x7f0d004a, float:1.8742265E38)
            if (r9 != r4) goto L_0x0036
            if (r1 == 0) goto L_0x0051
            com.android.settings.localepicker.LocaleDragAndDropAdapter r6 = r5.mAdapter
            r6.onItemMove(r1, r2)
            goto L_0x0018
        L_0x0036:
            r4 = 2131558472(0x7f0d0048, float:1.874226E38)
            if (r9 != r4) goto L_0x0044
            int r0 = r0 - r3
            if (r1 == r0) goto L_0x0051
            com.android.settings.localepicker.LocaleDragAndDropAdapter r6 = r5.mAdapter
            r6.onItemMove(r1, r0)
            goto L_0x0018
        L_0x0044:
            r4 = 2131558476(0x7f0d004c, float:1.8742269E38)
            if (r9 != r4) goto L_0x0059
            if (r0 <= r3) goto L_0x0051
            com.android.settings.localepicker.LocaleDragAndDropAdapter r6 = r5.mAdapter
            r6.removeItem(r1)
            goto L_0x0018
        L_0x0051:
            if (r2 == 0) goto L_0x0058
            com.android.settings.localepicker.LocaleDragAndDropAdapter r5 = r5.mAdapter
            r5.doTheUpdate()
        L_0x0058:
            return r2
        L_0x0059:
            boolean r5 = super.performAccessibilityActionForItem(r6, r7, r8, r9, r10)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.localepicker.LocaleLinearLayoutManager.performAccessibilityActionForItem(androidx.recyclerview.widget.RecyclerView$Recycler, androidx.recyclerview.widget.RecyclerView$State, android.view.View, int, android.os.Bundle):boolean");
    }
}
