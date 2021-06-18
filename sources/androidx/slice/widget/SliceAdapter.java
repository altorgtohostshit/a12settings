package androidx.slice.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.collection.ArrayMap;
import androidx.recyclerview.widget.RecyclerView;
import androidx.slice.SliceItem;
import androidx.slice.core.SliceAction;
import androidx.slice.core.SliceQuery;
import androidx.slice.view.R$layout;
import androidx.slice.widget.SliceActionView;
import androidx.slice.widget.SliceView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SliceAdapter extends RecyclerView.Adapter<SliceViewHolder> implements SliceActionView.SliceActionLoadingListener {
    boolean mAllowTwoLines;
    final Context mContext;
    private final IdGenerator mIdGen = new IdGenerator();
    int mInsetBottom;
    int mInsetEnd;
    int mInsetStart;
    int mInsetTop;
    long mLastUpdated;
    Set<SliceItem> mLoadingActions = new HashSet();
    SliceView mParent;
    SliceViewPolicy mPolicy;
    boolean mShowLastUpdated;
    List<SliceAction> mSliceActions;
    SliceView.OnSliceActionListener mSliceObserver;
    SliceStyle mSliceStyle;
    private List<SliceWrapper> mSlices = new ArrayList();
    TemplateView mTemplateView;
    int mTintColor;

    public SliceAdapter(Context context) {
        this.mContext = context;
        setHasStableIds(true);
    }

    public void setParents(SliceView sliceView, TemplateView templateView) {
        this.mParent = sliceView;
        this.mTemplateView = templateView;
    }

    public void setInsets(int i, int i2, int i3, int i4) {
        this.mInsetStart = i;
        this.mInsetTop = i2;
        this.mInsetEnd = i3;
        this.mInsetBottom = i4;
    }

    public void setSliceObserver(SliceView.OnSliceActionListener onSliceActionListener) {
        this.mSliceObserver = onSliceActionListener;
    }

    public void setSliceActions(List<SliceAction> list) {
        this.mSliceActions = list;
        notifyHeaderChanged();
    }

    public void setSliceItems(List<SliceContent> list, int i, int i2) {
        if (list == null) {
            this.mLoadingActions.clear();
            this.mSlices.clear();
        } else {
            this.mIdGen.resetUsage();
            this.mSlices = new ArrayList(list.size());
            for (SliceContent sliceWrapper : list) {
                this.mSlices.add(new SliceWrapper(sliceWrapper, this.mIdGen, i2));
            }
        }
        this.mTintColor = i;
        notifyDataSetChanged();
    }

    public void setStyle(SliceStyle sliceStyle) {
        this.mSliceStyle = sliceStyle;
        notifyDataSetChanged();
    }

    public void setPolicy(SliceViewPolicy sliceViewPolicy) {
        this.mPolicy = sliceViewPolicy;
    }

    public void setShowLastUpdated(boolean z) {
        if (this.mShowLastUpdated != z) {
            this.mShowLastUpdated = z;
            notifyHeaderChanged();
        }
    }

    public void setLastUpdated(long j) {
        if (this.mLastUpdated != j) {
            this.mLastUpdated = j;
            notifyHeaderChanged();
        }
    }

    public void setLoadingActions(Set<SliceItem> set) {
        if (set == null) {
            this.mLoadingActions.clear();
        } else {
            this.mLoadingActions = set;
        }
        notifyDataSetChanged();
    }

    public Set<SliceItem> getLoadingActions() {
        return this.mLoadingActions;
    }

    public void onSliceActionLoading(SliceItem sliceItem, int i) {
        this.mLoadingActions.add(sliceItem);
        if (getItemCount() > i) {
            notifyItemChanged(i);
        } else {
            notifyDataSetChanged();
        }
    }

    public void setAllowTwoLines(boolean z) {
        this.mAllowTwoLines = z;
        notifyHeaderChanged();
    }

    public void notifyHeaderChanged() {
        if (getItemCount() > 0) {
            notifyItemChanged(0);
        }
    }

    public SliceViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflateForType = inflateForType(i);
        inflateForType.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
        return new SliceViewHolder(inflateForType);
    }

    public int getItemViewType(int i) {
        return this.mSlices.get(i).mType;
    }

    public long getItemId(int i) {
        return this.mSlices.get(i).mId;
    }

    public int getItemCount() {
        return this.mSlices.size();
    }

    public void onBindViewHolder(SliceViewHolder sliceViewHolder, int i) {
        sliceViewHolder.bind(this.mSlices.get(i).mItem, i);
    }

    private View inflateForType(int i) {
        if (i == 3) {
            return getGridRowView();
        }
        if (i == 4) {
            return LayoutInflater.from(this.mContext).inflate(R$layout.abc_slice_message, (ViewGroup) null);
        }
        if (i != 5) {
            return getRowView();
        }
        return LayoutInflater.from(this.mContext).inflate(R$layout.abc_slice_message_local, (ViewGroup) null);
    }

    public GridRowView getGridRowView() {
        View inflate = LayoutInflater.from(this.mContext).inflate(R$layout.abc_slice_grid, (ViewGroup) null);
        if (inflate instanceof GridRowView) {
            return (GridRowView) inflate;
        }
        return new GridRowView(this.mContext, (AttributeSet) null);
    }

    public RowView getRowView() {
        return new RowView(this.mContext);
    }

    protected static class SliceWrapper {
        final long mId;
        final SliceContent mItem;
        final int mType;

        public SliceWrapper(SliceContent sliceContent, IdGenerator idGenerator, int i) {
            this.mItem = sliceContent;
            this.mType = getFormat(sliceContent.getSliceItem());
            this.mId = idGenerator.getId(sliceContent.getSliceItem());
        }

        public static int getFormat(SliceItem sliceItem) {
            if ("message".equals(sliceItem.getSubType())) {
                return SliceQuery.findSubtype(sliceItem, (String) null, "source") != null ? 4 : 5;
            }
            if (sliceItem.hasHint("horizontal")) {
                return 3;
            }
            return !sliceItem.hasHint("list_item") ? 2 : 1;
        }
    }

    public class SliceViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener, View.OnClickListener {
        public final SliceChildView mSliceChildView;

        public SliceViewHolder(View view) {
            super(view);
            this.mSliceChildView = view instanceof SliceChildView ? (SliceChildView) view : null;
        }

        /* access modifiers changed from: package-private */
        public void bind(SliceContent sliceContent, int i) {
            if (this.mSliceChildView != null && sliceContent != null) {
                RowStyle rowStyle = SliceAdapter.this.mSliceStyle.getRowStyle(sliceContent.getSliceItem());
                this.mSliceChildView.setOnClickListener(this);
                this.mSliceChildView.setOnTouchListener(this);
                this.mSliceChildView.setSliceActionLoadingListener(SliceAdapter.this);
                boolean isHeader = sliceContent instanceof RowContent ? ((RowContent) sliceContent).getIsHeader() : i == 0;
                this.mSliceChildView.setLoadingActions(SliceAdapter.this.mLoadingActions);
                this.mSliceChildView.setPolicy(SliceAdapter.this.mPolicy);
                this.mSliceChildView.setTint(rowStyle.getTintColor());
                this.mSliceChildView.setStyle(SliceAdapter.this.mSliceStyle, rowStyle);
                this.mSliceChildView.setShowLastUpdated(isHeader && SliceAdapter.this.mShowLastUpdated);
                this.mSliceChildView.setLastUpdated(isHeader ? SliceAdapter.this.mLastUpdated : -1);
                int i2 = i == 0 ? SliceAdapter.this.mInsetTop : 0;
                int i3 = i == SliceAdapter.this.getItemCount() - 1 ? SliceAdapter.this.mInsetBottom : 0;
                SliceChildView sliceChildView = this.mSliceChildView;
                SliceAdapter sliceAdapter = SliceAdapter.this;
                sliceChildView.setInsets(sliceAdapter.mInsetStart, i2, sliceAdapter.mInsetEnd, i3);
                this.mSliceChildView.setAllowTwoLines(SliceAdapter.this.mAllowTwoLines);
                this.mSliceChildView.setSliceActions(isHeader ? SliceAdapter.this.mSliceActions : null);
                this.mSliceChildView.setSliceItem(sliceContent, isHeader, i, SliceAdapter.this.getItemCount(), SliceAdapter.this.mSliceObserver);
                this.mSliceChildView.setTag(new int[]{ListContent.getRowType(sliceContent, isHeader, SliceAdapter.this.mSliceActions), i});
            }
        }

        public void onClick(View view) {
            SliceView sliceView = SliceAdapter.this.mParent;
            if (sliceView != null) {
                sliceView.setClickInfo((int[]) view.getTag());
                SliceAdapter.this.mParent.performClick();
            }
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            TemplateView templateView = SliceAdapter.this.mTemplateView;
            if (templateView == null) {
                return false;
            }
            templateView.onForegroundActivated(motionEvent);
            return false;
        }
    }

    private static class IdGenerator {
        private final ArrayMap<String, Long> mCurrentIds = new ArrayMap<>();
        private long mNextLong = 0;
        private final ArrayMap<String, Integer> mUsedIds = new ArrayMap<>();

        IdGenerator() {
        }

        public long getId(SliceItem sliceItem) {
            String genString = genString(sliceItem);
            if (!this.mCurrentIds.containsKey(genString)) {
                ArrayMap<String, Long> arrayMap = this.mCurrentIds;
                long j = this.mNextLong;
                this.mNextLong = 1 + j;
                arrayMap.put(genString, Long.valueOf(j));
            }
            long longValue = this.mCurrentIds.get(genString).longValue();
            Integer num = this.mUsedIds.get(genString);
            int intValue = num != null ? num.intValue() : 0;
            this.mUsedIds.put(genString, Integer.valueOf(intValue + 1));
            return longValue + ((long) (intValue * 10000));
        }

        private String genString(SliceItem sliceItem) {
            if ("slice".equals(sliceItem.getFormat()) || "action".equals(sliceItem.getFormat())) {
                return String.valueOf(sliceItem.getSlice().getItems().size());
            }
            return sliceItem.toString();
        }

        public void resetUsage() {
            this.mUsedIds.clear();
        }
    }
}
