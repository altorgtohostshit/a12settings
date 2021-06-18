package androidx.slice.widget;

import android.annotation.TargetApi;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import java.util.ArrayList;
import java.util.Iterator;

public class LocationBasedViewTracker implements Runnable, View.OnLayoutChangeListener {
    @TargetApi(21)
    private static final SelectionLogic A11Y_FOCUS = new SelectionLogic() {
        public void selectView(View view) {
            view.performAccessibilityAction(64, (Bundle) null);
        }
    };
    private static final SelectionLogic INPUT_FOCUS = new SelectionLogic() {
        public void selectView(View view) {
            view.requestFocus();
        }
    };
    private final Rect mFocusRect;
    private final ViewGroup mParent;
    private final SelectionLogic mSelectionLogic;

    private interface SelectionLogic {
        void selectView(View view);
    }

    private LocationBasedViewTracker(ViewGroup viewGroup, View view, SelectionLogic selectionLogic) {
        Rect rect = new Rect();
        this.mFocusRect = rect;
        this.mParent = viewGroup;
        this.mSelectionLogic = selectionLogic;
        view.getDrawingRect(rect);
        viewGroup.offsetDescendantRectToMyCoords(view, rect);
        viewGroup.addOnLayoutChangeListener(this);
        viewGroup.requestLayout();
    }

    public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        this.mParent.removeOnLayoutChangeListener(this);
        this.mParent.post(this);
    }

    public void run() {
        int abs;
        ArrayList arrayList = new ArrayList();
        this.mParent.addFocusables(arrayList, 2, 0);
        Rect rect = new Rect();
        Iterator it = arrayList.iterator();
        int i = Integer.MAX_VALUE;
        View view = null;
        while (it.hasNext()) {
            View view2 = (View) it.next();
            view2.getDrawingRect(rect);
            this.mParent.offsetDescendantRectToMyCoords(view2, rect);
            if (this.mFocusRect.intersect(rect) && i > (abs = Math.abs(this.mFocusRect.left - rect.left) + Math.abs(this.mFocusRect.right - rect.right) + Math.abs(this.mFocusRect.top - rect.top) + Math.abs(this.mFocusRect.bottom - rect.bottom))) {
                view = view2;
                i = abs;
            }
        }
        if (view != null) {
            this.mSelectionLogic.selectView(view);
        }
    }

    public static void trackInputFocused(ViewGroup viewGroup) {
        View findFocus = viewGroup.findFocus();
        if (findFocus != null) {
            new LocationBasedViewTracker(viewGroup, findFocus, INPUT_FOCUS);
        }
    }

    public static void trackA11yFocus(ViewGroup viewGroup) {
        if (Build.VERSION.SDK_INT >= 21 && ((AccessibilityManager) viewGroup.getContext().getSystemService("accessibility")).isTouchExplorationEnabled()) {
            ArrayList arrayList = new ArrayList();
            viewGroup.addFocusables(arrayList, 2, 0);
            View view = null;
            Iterator it = arrayList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                View view2 = (View) it.next();
                if (view2.isAccessibilityFocused()) {
                    view = view2;
                    break;
                }
            }
            if (view != null) {
                new LocationBasedViewTracker(viewGroup, view, A11Y_FOCUS);
            }
        }
    }
}
