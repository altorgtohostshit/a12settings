package androidx.slice.widget;

import android.app.RemoteInput;
import android.content.Context;
import android.content.res.ColorStateList;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.graphics.drawable.IconCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.slice.SliceItem;

public class ActionRow extends FrameLayout {
    private final LinearLayout mActionsGroup;
    private int mColor = -16777216;
    private final int mIconPadding;
    private final int mSize;

    public ActionRow(Context context, boolean z) {
        super(context);
        this.mSize = (int) TypedValue.applyDimension(1, 48.0f, context.getResources().getDisplayMetrics());
        this.mIconPadding = (int) TypedValue.applyDimension(1, 12.0f, context.getResources().getDisplayMetrics());
        LinearLayout linearLayout = new LinearLayout(context);
        this.mActionsGroup = linearLayout;
        linearLayout.setOrientation(0);
        linearLayout.setLayoutParams(new FrameLayout.LayoutParams(-1, -2));
        addView(linearLayout);
    }

    private void setColor(int i) {
        this.mColor = i;
        for (int i2 = 0; i2 < this.mActionsGroup.getChildCount(); i2++) {
            View childAt = this.mActionsGroup.getChildAt(i2);
            if (((Integer) childAt.getTag()).intValue() == 0) {
                ImageViewCompat.setImageTintList((ImageView) childAt, ColorStateList.valueOf(this.mColor));
            }
        }
    }

    private ImageView addAction(IconCompat iconCompat, boolean z) {
        ImageView imageView = new ImageView(getContext());
        int i = this.mIconPadding;
        imageView.setPadding(i, i, i, i);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setImageDrawable(iconCompat.loadDrawable(getContext()));
        if (z) {
            ImageViewCompat.setImageTintList(imageView, ColorStateList.valueOf(this.mColor));
        }
        imageView.setBackground(SliceViewUtil.getDrawable(getContext(), 16843534));
        imageView.setTag(Boolean.valueOf(z));
        addAction(imageView);
        return imageView;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v4, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v6, resolved type: boolean} */
    /* JADX WARNING: type inference failed for: r0v3 */
    /* JADX WARNING: type inference failed for: r0v5 */
    /* JADX WARNING: type inference failed for: r0v8 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setActions(java.util.List<androidx.slice.core.SliceAction> r6, int r7) {
        /*
            r5 = this;
            r5.removeAllViews()
            android.widget.LinearLayout r0 = r5.mActionsGroup
            r0.removeAllViews()
            android.widget.LinearLayout r0 = r5.mActionsGroup
            r5.addView(r0)
            r0 = -1
            if (r7 == r0) goto L_0x0013
            r5.setColor(r7)
        L_0x0013:
            java.util.Iterator r6 = r6.iterator()
        L_0x0017:
            boolean r7 = r6.hasNext()
            r0 = 0
            if (r7 == 0) goto L_0x008c
            java.lang.Object r7 = r6.next()
            androidx.slice.core.SliceAction r7 = (androidx.slice.core.SliceAction) r7
            android.widget.LinearLayout r1 = r5.mActionsGroup
            int r1 = r1.getChildCount()
            r2 = 5
            if (r1 < r2) goto L_0x002e
            return
        L_0x002e:
            r1 = r7
            androidx.slice.core.SliceActionImpl r1 = (androidx.slice.core.SliceActionImpl) r1
            androidx.slice.SliceItem r2 = r1.getSliceItem()
            androidx.slice.SliceItem r1 = r1.getActionItem()
            java.lang.String r3 = "input"
            androidx.slice.SliceItem r3 = androidx.slice.core.SliceQuery.find((androidx.slice.SliceItem) r2, (java.lang.String) r3)
            java.lang.String r4 = "image"
            androidx.slice.SliceItem r2 = androidx.slice.core.SliceQuery.find((androidx.slice.SliceItem) r2, (java.lang.String) r4)
            if (r3 == 0) goto L_0x006a
            if (r2 == 0) goto L_0x006a
            int r7 = android.os.Build.VERSION.SDK_INT
            r0 = 21
            if (r7 < r0) goto L_0x0053
            r5.handleSetRemoteInputActions(r3, r2, r1)
            goto L_0x0017
        L_0x0053:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r0 = "Received RemoteInput on API <20 "
            r7.append(r0)
            r7.append(r3)
            java.lang.String r7 = r7.toString()
            java.lang.String r0 = "ActionRow"
            android.util.Log.w(r0, r7)
            goto L_0x0017
        L_0x006a:
            androidx.core.graphics.drawable.IconCompat r2 = r7.getIcon()
            if (r2 == 0) goto L_0x0017
            androidx.core.graphics.drawable.IconCompat r2 = r7.getIcon()
            if (r2 == 0) goto L_0x0017
            if (r1 == 0) goto L_0x0017
            int r7 = r7.getImageMode()
            if (r7 != 0) goto L_0x007f
            r0 = 1
        L_0x007f:
            android.widget.ImageView r7 = r5.addAction(r2, r0)
            androidx.slice.widget.ActionRow$1 r0 = new androidx.slice.widget.ActionRow$1
            r0.<init>(r1)
            r7.setOnClickListener(r0)
            goto L_0x0017
        L_0x008c:
            int r6 = r5.getChildCount()
            if (r6 == 0) goto L_0x0093
            goto L_0x0095
        L_0x0093:
            r0 = 8
        L_0x0095:
            r5.setVisibility(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.slice.widget.ActionRow.setActions(java.util.List, int):void");
    }

    private void addAction(View view) {
        LinearLayout linearLayout = this.mActionsGroup;
        int i = this.mSize;
        linearLayout.addView(view, new LinearLayout.LayoutParams(i, i, 1.0f));
    }

    private void handleSetRemoteInputActions(final SliceItem sliceItem, SliceItem sliceItem2, final SliceItem sliceItem3) {
        if (sliceItem.getRemoteInput().getAllowFreeFormInput()) {
            addAction(sliceItem2.getIcon(), !sliceItem2.hasHint("no_tint")).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    ActionRow.this.handleRemoteInputClick(view, sliceItem3, sliceItem.getRemoteInput());
                }
            });
            createRemoteInputView(this.mColor, getContext());
        }
    }

    private void createRemoteInputView(int i, Context context) {
        RemoteInputView inflate = RemoteInputView.inflate(context, this);
        inflate.setVisibility(4);
        addView(inflate, new FrameLayout.LayoutParams(-1, -1));
        inflate.setBackgroundColor(i);
    }

    /* access modifiers changed from: package-private */
    public boolean handleRemoteInputClick(View view, SliceItem sliceItem, RemoteInput remoteInput) {
        if (remoteInput == null) {
            return false;
        }
        ViewParent parent = view.getParent().getParent();
        RemoteInputView remoteInputView = null;
        while (parent != null && (!(parent instanceof View) || (remoteInputView = findRemoteInputView((View) parent)) == null)) {
            parent = parent.getParent();
        }
        if (remoteInputView == null) {
            return false;
        }
        int width = view.getWidth();
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            if (textView.getLayout() != null) {
                width = Math.min(width, ((int) textView.getLayout().getLineWidth(0)) + textView.getCompoundPaddingLeft() + textView.getCompoundPaddingRight());
            }
        }
        int left = view.getLeft() + (width / 2);
        int top = view.getTop() + (view.getHeight() / 2);
        int width2 = remoteInputView.getWidth();
        int height = remoteInputView.getHeight() - top;
        int i = width2 - left;
        remoteInputView.setRevealParameters(left, top, Math.max(Math.max(left + top, left + height), Math.max(i + top, i + height)));
        remoteInputView.setAction(sliceItem);
        remoteInputView.setRemoteInput(new RemoteInput[]{remoteInput}, remoteInput);
        remoteInputView.focusAnimated();
        return true;
    }

    private RemoteInputView findRemoteInputView(View view) {
        if (view == null) {
            return null;
        }
        return (RemoteInputView) view.findViewWithTag(RemoteInputView.VIEW_TAG);
    }
}
