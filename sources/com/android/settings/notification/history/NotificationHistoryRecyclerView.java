package com.android.settings.notification.history;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationHistoryRecyclerView extends RecyclerView {
    /* access modifiers changed from: private */
    public float dXLast;
    /* access modifiers changed from: private */
    public OnItemSwipeDeleteListener listener;

    public interface OnItemSwipeDeleteListener {
        void onItemSwipeDeleted(int i);
    }

    public NotificationHistoryRecyclerView(Context context) {
        this(context, (AttributeSet) null);
    }

    public NotificationHistoryRecyclerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public NotificationHistoryRecyclerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setLayoutManager(new LinearLayoutManager(getContext()));
        addItemDecoration(new DividerItemDecoration(getContext(), 1));
        new ItemTouchHelper(new DismissTouchHelper(0, 48)).attachToRecyclerView(this);
        setNestedScrollingEnabled(false);
    }

    public void setOnItemSwipeDeleteListener(OnItemSwipeDeleteListener onItemSwipeDeleteListener) {
        this.listener = onItemSwipeDeleteListener;
    }

    private class DismissTouchHelper extends ItemTouchHelper.SimpleCallback {
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2) {
            return false;
        }

        public DismissTouchHelper(int i, int i2) {
            super(i, i2);
        }

        public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
            if (NotificationHistoryRecyclerView.this.listener != null) {
                NotificationHistoryRecyclerView.this.listener.onItemSwipeDeleted(viewHolder.getAdapterPosition());
            }
        }

        public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float f, float f2, int i, boolean z) {
            super.onChildDraw(canvas, recyclerView, viewHolder, f, f2, i, z);
            if (z) {
                View view = viewHolder.itemView;
                float swipeThreshold = getSwipeThreshold(viewHolder) * ((float) view.getWidth());
                float f3 = -swipeThreshold;
                boolean z2 = false;
                boolean z3 = f < f3 || f > swipeThreshold;
                if (NotificationHistoryRecyclerView.this.dXLast < f3 || NotificationHistoryRecyclerView.this.dXLast > swipeThreshold) {
                    z2 = true;
                }
                if (z3 != z2) {
                    view.performHapticFeedback(4);
                }
                float unused = NotificationHistoryRecyclerView.this.dXLast = f;
                return;
            }
            float unused2 = NotificationHistoryRecyclerView.this.dXLast = 0.0f;
        }
    }
}
