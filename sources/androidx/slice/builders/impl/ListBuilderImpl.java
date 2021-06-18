package androidx.slice.builders.impl;

import android.app.PendingIntent;
import android.os.Bundle;
import androidx.core.graphics.drawable.IconCompat;
import androidx.core.util.Pair;
import androidx.slice.Clock;
import androidx.slice.Slice;
import androidx.slice.SliceItem;
import androidx.slice.SliceSpec;
import androidx.slice.builders.ListBuilder;
import androidx.slice.builders.SliceAction;
import androidx.slice.core.SliceQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ListBuilderImpl extends TemplateBuilderImpl implements ListBuilder {
    private boolean mFirstRowChecked;
    private boolean mFirstRowHasText;
    private Bundle mHostExtras;
    private boolean mIsError;
    private boolean mIsFirstRowTypeValid;
    private Set<String> mKeywords;
    private List<Slice> mSliceActions;
    private Slice mSliceHeader;

    public ListBuilderImpl(Slice.Builder builder, SliceSpec sliceSpec, Clock clock) {
        super(builder, sliceSpec, clock);
    }

    public void apply(Slice.Builder builder) {
        builder.addLong(getClock().currentTimeMillis(), "millis", "last_updated");
        Slice slice = this.mSliceHeader;
        if (slice != null) {
            builder.addSubSlice(slice);
        }
        if (this.mSliceActions != null) {
            Slice.Builder builder2 = new Slice.Builder(builder);
            for (int i = 0; i < this.mSliceActions.size(); i++) {
                builder2.addSubSlice(this.mSliceActions.get(i));
            }
            builder.addSubSlice(builder2.addHints("actions").build());
        }
        if (this.mIsError) {
            builder.addHints("error");
        }
        if (this.mKeywords != null) {
            Slice.Builder builder3 = new Slice.Builder(getBuilder());
            for (String addText : this.mKeywords) {
                builder3.addText((CharSequence) addText, (String) null, new String[0]);
            }
            getBuilder().addSubSlice(builder3.addHints("keywords").build());
        }
        Bundle bundle = this.mHostExtras;
        if (bundle != null) {
            builder.addItem(new SliceItem((Object) bundle, "bundle", "host_extras", new String[0]));
        }
    }

    public Slice build() {
        Slice build = super.build();
        boolean z = true;
        boolean z2 = SliceQuery.find(build, (String) null, "partial", (String) null) != null;
        if (SliceQuery.find(build, "slice", "list_item", (String) null) != null) {
            z = false;
        }
        String[] strArr = {"shortcut", "title"};
        SliceItem find = SliceQuery.find(build, "action", strArr, (String[]) null);
        List<SliceItem> findAll = SliceQuery.findAll(build, "slice", strArr, (String[]) null);
        if (z2 || z || find != null || (findAll != null && !findAll.isEmpty())) {
            boolean z3 = this.mFirstRowChecked;
            if (z3 && !this.mIsFirstRowTypeValid) {
                throw new IllegalStateException("A slice cannot have the first row be constructed from a GridRowBuilder, consider using #setHeader.");
            } else if (!z3 || this.mFirstRowHasText) {
                return build;
            } else {
                throw new IllegalStateException("A slice requires the first row to have some text.");
            }
        } else {
            throw new IllegalStateException("A slice requires a primary action; ensure one of your builders has called #setPrimaryAction with a valid SliceAction.");
        }
    }

    public void addRow(ListBuilder.RowBuilder rowBuilder) {
        RowBuilderImpl rowBuilderImpl = new RowBuilderImpl(createChildBuilder());
        rowBuilderImpl.fillFrom(rowBuilder);
        checkRow(true, rowBuilderImpl.hasText());
        addRow(rowBuilderImpl);
    }

    public void addRow(RowBuilderImpl rowBuilderImpl) {
        checkRow(true, rowBuilderImpl.hasText());
        rowBuilderImpl.getBuilder().addHints("list_item");
        if (rowBuilderImpl.isEndOfSection()) {
            rowBuilderImpl.getBuilder().addHints("end_of_section");
        }
        getBuilder().addSubSlice(rowBuilderImpl.build());
    }

    public void setHeader(ListBuilder.HeaderBuilder headerBuilder) {
        this.mIsFirstRowTypeValid = true;
        this.mFirstRowHasText = true;
        this.mFirstRowChecked = true;
        HeaderBuilderImpl headerBuilderImpl = new HeaderBuilderImpl(this);
        headerBuilderImpl.fillFrom(headerBuilder);
        this.mSliceHeader = headerBuilderImpl.build();
    }

    public void addAction(SliceAction sliceAction) {
        if (this.mSliceActions == null) {
            this.mSliceActions = new ArrayList();
        }
        this.mSliceActions.add(sliceAction.buildSlice(new Slice.Builder(getBuilder()).addHints("actions")));
    }

    public void addInputRange(ListBuilder.InputRangeBuilder inputRangeBuilder) {
        InputRangeBuilderImpl inputRangeBuilderImpl = new InputRangeBuilderImpl(createChildBuilder(), inputRangeBuilder);
        checkRow(true, inputRangeBuilderImpl.hasText());
        getBuilder().addSubSlice(inputRangeBuilderImpl.build(), "range");
    }

    public void setColor(int i) {
        getBuilder().addInt(i, "color", new String[0]);
    }

    public void setKeywords(Set<String> set) {
        this.mKeywords = set;
    }

    public void setTtl(long j) {
        long j2 = -1;
        if (j != -1) {
            j2 = getClock().currentTimeMillis() + j;
        }
        getBuilder().addTimestamp(j2, "millis", "ttl");
    }

    public void setIsError(boolean z) {
        this.mIsError = z;
    }

    private void checkRow(boolean z, boolean z2) {
        if (!this.mFirstRowChecked) {
            this.mFirstRowChecked = true;
            this.mIsFirstRowTypeValid = z;
            this.mFirstRowHasText = z2;
        }
    }

    public static class RangeBuilderImpl extends TemplateBuilderImpl {
        protected CharSequence mContentDescr;
        protected int mLayoutDir = -1;
        protected int mMax = 100;
        protected int mMin = 0;
        private int mMode = 0;
        protected SliceAction mPrimaryAction;
        private Slice mStartItem;
        protected CharSequence mSubtitle;
        protected CharSequence mTitle;
        protected int mValue = 0;
        protected boolean mValueSet = false;

        RangeBuilderImpl(Slice.Builder builder, ListBuilder.RangeBuilder rangeBuilder) {
            super(builder, (SliceSpec) null);
        }

        public void apply(Slice.Builder builder) {
            int i;
            if (!this.mValueSet) {
                this.mValue = this.mMin;
            }
            int i2 = this.mMin;
            int i3 = this.mValue;
            if (i2 > i3 || i3 > (i = this.mMax) || i2 >= i) {
                throw new IllegalArgumentException("Invalid range values, min=" + this.mMin + ", value=" + this.mValue + ", max=" + this.mMax + " ensure value falls within (min, max) and min < max.");
            }
            Slice slice = this.mStartItem;
            if (slice != null) {
                builder.addSubSlice(slice);
            }
            CharSequence charSequence = this.mTitle;
            if (charSequence != null) {
                builder.addText(charSequence, (String) null, "title");
            }
            CharSequence charSequence2 = this.mSubtitle;
            if (charSequence2 != null) {
                builder.addText(charSequence2, (String) null, new String[0]);
            }
            CharSequence charSequence3 = this.mContentDescr;
            if (charSequence3 != null) {
                builder.addText(charSequence3, "content_description", new String[0]);
            }
            SliceAction sliceAction = this.mPrimaryAction;
            if (sliceAction != null) {
                sliceAction.setPrimaryAction(builder);
            }
            int i4 = this.mLayoutDir;
            if (i4 != -1) {
                builder.addInt(i4, "layout_direction", new String[0]);
            }
            builder.addHints("list_item").addInt(this.mMin, "min", new String[0]).addInt(this.mMax, "max", new String[0]).addInt(this.mValue, "value", new String[0]).addInt(this.mMode, "range_mode", new String[0]);
        }

        /* access modifiers changed from: package-private */
        public boolean hasText() {
            return (this.mTitle == null && this.mSubtitle == null) ? false : true;
        }
    }

    public static class InputRangeBuilderImpl extends RangeBuilderImpl {
        private final PendingIntent mAction;
        private final ArrayList<Slice> mEndItems = new ArrayList<>();
        private Slice mStartItem;
        private final IconCompat mThumb;

        InputRangeBuilderImpl(Slice.Builder builder, ListBuilder.InputRangeBuilder inputRangeBuilder) {
            super(builder, (ListBuilder.RangeBuilder) null);
            this.mValueSet = inputRangeBuilder.isValueSet();
            this.mMin = inputRangeBuilder.getMin();
            this.mMax = inputRangeBuilder.getMax();
            this.mValue = inputRangeBuilder.getValue();
            this.mTitle = inputRangeBuilder.getTitle();
            this.mSubtitle = inputRangeBuilder.getSubtitle();
            this.mContentDescr = inputRangeBuilder.getContentDescription();
            this.mPrimaryAction = inputRangeBuilder.getPrimaryAction();
            this.mLayoutDir = inputRangeBuilder.getLayoutDirection();
            this.mAction = inputRangeBuilder.getInputAction();
            this.mThumb = inputRangeBuilder.getThumb();
            if (inputRangeBuilder.getTitleIcon() != null) {
                setTitleItem(inputRangeBuilder.getTitleIcon(), inputRangeBuilder.getTitleImageMode(), inputRangeBuilder.isTitleItemLoading());
            }
            List<Object> endItems = inputRangeBuilder.getEndItems();
            List<Integer> endTypes = inputRangeBuilder.getEndTypes();
            List<Boolean> endLoads = inputRangeBuilder.getEndLoads();
            for (int i = 0; i < endItems.size(); i++) {
                if (endTypes.get(i).intValue() == 2) {
                    addEndItem((SliceAction) endItems.get(i), endLoads.get(i).booleanValue());
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void setTitleItem(IconCompat iconCompat, int i, boolean z) {
            Slice.Builder addIcon = new Slice.Builder(getBuilder()).addIcon(iconCompat, (String) null, (List<String>) parseImageMode(i, z));
            if (z) {
                addIcon.addHints("partial");
            }
            this.mStartItem = addIcon.addHints("title").build();
        }

        private void addEndItem(SliceAction sliceAction, boolean z) {
            Slice.Builder builder = new Slice.Builder(getBuilder());
            if (z) {
                builder.addHints("partial");
            }
            this.mEndItems.add(sliceAction.buildSlice(builder));
        }

        public void apply(Slice.Builder builder) {
            if (this.mAction != null) {
                Slice.Builder builder2 = new Slice.Builder(builder);
                super.apply(builder2);
                IconCompat iconCompat = this.mThumb;
                if (iconCompat != null) {
                    builder2.addIcon(iconCompat, (String) null, new String[0]);
                }
                builder.addAction(this.mAction, builder2.build(), "range").addHints("list_item");
                Slice slice = this.mStartItem;
                if (slice != null) {
                    builder.addSubSlice(slice);
                }
                for (int i = 0; i < this.mEndItems.size(); i++) {
                    builder.addSubSlice(this.mEndItems.get(i));
                }
                return;
            }
            throw new IllegalStateException("Input ranges must have an associated action.");
        }
    }

    public static class RowBuilderImpl extends TemplateBuilderImpl {
        private CharSequence mContentDescr;
        private final ArrayList<Slice> mEndItems = new ArrayList<>();
        private boolean mIsEndOfSection;
        private SliceAction mPrimaryAction;
        private Slice mStartItem;
        private SliceItem mSubtitleItem;
        private SliceItem mTitleItem;

        RowBuilderImpl(Slice.Builder builder) {
            super(builder, (SliceSpec) null);
        }

        /* access modifiers changed from: package-private */
        public void fillFrom(ListBuilder.RowBuilder rowBuilder) {
            if (rowBuilder.getUri() != null) {
                setBuilder(new Slice.Builder(rowBuilder.getUri()));
            }
            setPrimaryAction(rowBuilder.getPrimaryAction());
            this.mIsEndOfSection = rowBuilder.isEndOfSection();
            if (rowBuilder.getLayoutDirection() != -1) {
                setLayoutDirection(rowBuilder.getLayoutDirection());
            }
            if (rowBuilder.getTitleAction() != null || rowBuilder.isTitleActionLoading()) {
                setTitleItem(rowBuilder.getTitleAction(), rowBuilder.isTitleActionLoading());
            } else if (rowBuilder.getTitleIcon() != null || rowBuilder.isTitleItemLoading()) {
                setTitleItem(rowBuilder.getTitleIcon(), rowBuilder.getTitleImageMode(), rowBuilder.isTitleItemLoading());
            } else if (rowBuilder.getTimeStamp() != -1) {
                setTitleItem(rowBuilder.getTimeStamp());
            }
            if (rowBuilder.getTitle() != null || rowBuilder.isTitleLoading()) {
                setTitle(rowBuilder.getTitle(), rowBuilder.isTitleLoading());
            }
            if (rowBuilder.getSubtitle() != null || rowBuilder.isSubtitleLoading()) {
                setSubtitle(rowBuilder.getSubtitle(), rowBuilder.isSubtitleLoading());
            }
            if (rowBuilder.getContentDescription() != null) {
                setContentDescription(rowBuilder.getContentDescription());
            }
            List<Object> endItems = rowBuilder.getEndItems();
            List<Integer> endTypes = rowBuilder.getEndTypes();
            List<Boolean> endLoads = rowBuilder.getEndLoads();
            for (int i = 0; i < endItems.size(); i++) {
                int intValue = endTypes.get(i).intValue();
                if (intValue == 0) {
                    addEndItem(((Long) endItems.get(i)).longValue());
                } else if (intValue == 1) {
                    Pair pair = (Pair) endItems.get(i);
                    addEndItem((IconCompat) pair.first, ((Integer) pair.second).intValue(), endLoads.get(i).booleanValue());
                } else if (intValue == 2) {
                    addEndItem((SliceAction) endItems.get(i), endLoads.get(i).booleanValue());
                }
            }
        }

        private void setTitleItem(long j) {
            this.mStartItem = new Slice.Builder(getBuilder()).addTimestamp(j, (String) null, new String[0]).addHints("title").build();
        }

        private void setTitleItem(IconCompat iconCompat, int i, boolean z) {
            Slice.Builder addIcon = new Slice.Builder(getBuilder()).addIcon(iconCompat, (String) null, (List<String>) parseImageMode(i, z));
            if (z) {
                addIcon.addHints("partial");
            }
            this.mStartItem = addIcon.addHints("title").build();
        }

        private void setTitleItem(SliceAction sliceAction, boolean z) {
            Slice.Builder addHints = new Slice.Builder(getBuilder()).addHints("title");
            if (z) {
                addHints.addHints("partial");
            }
            this.mStartItem = sliceAction.buildSlice(addHints);
        }

        private void setPrimaryAction(SliceAction sliceAction) {
            this.mPrimaryAction = sliceAction;
        }

        private void setTitle(CharSequence charSequence, boolean z) {
            SliceItem sliceItem = new SliceItem((Object) charSequence, "text", (String) null, new String[]{"title"});
            this.mTitleItem = sliceItem;
            if (z) {
                sliceItem.addHint("partial");
            }
        }

        private void setSubtitle(CharSequence charSequence, boolean z) {
            SliceItem sliceItem = new SliceItem((Object) charSequence, "text", (String) null, new String[0]);
            this.mSubtitleItem = sliceItem;
            if (z) {
                sliceItem.addHint("partial");
            }
        }

        /* access modifiers changed from: protected */
        public void addEndItem(long j) {
            this.mEndItems.add(new Slice.Builder(getBuilder()).addTimestamp(j, (String) null, new String[0]).build());
        }

        private void addEndItem(IconCompat iconCompat, int i, boolean z) {
            Slice.Builder addIcon = new Slice.Builder(getBuilder()).addIcon(iconCompat, (String) null, (List<String>) parseImageMode(i, z));
            if (z) {
                addIcon.addHints("partial");
            }
            this.mEndItems.add(addIcon.build());
        }

        private void addEndItem(SliceAction sliceAction, boolean z) {
            Slice.Builder builder = new Slice.Builder(getBuilder());
            if (z) {
                builder.addHints("partial");
            }
            this.mEndItems.add(sliceAction.buildSlice(builder));
        }

        private void setContentDescription(CharSequence charSequence) {
            this.mContentDescr = charSequence;
        }

        private void setLayoutDirection(int i) {
            getBuilder().addInt(i, "layout_direction", new String[0]);
        }

        public boolean isEndOfSection() {
            return this.mIsEndOfSection;
        }

        /* access modifiers changed from: package-private */
        public boolean hasText() {
            return (this.mTitleItem == null && this.mSubtitleItem == null) ? false : true;
        }

        public void apply(Slice.Builder builder) {
            Slice slice = this.mStartItem;
            if (slice != null) {
                builder.addSubSlice(slice);
            }
            SliceItem sliceItem = this.mTitleItem;
            if (sliceItem != null) {
                builder.addItem(sliceItem);
            }
            SliceItem sliceItem2 = this.mSubtitleItem;
            if (sliceItem2 != null) {
                builder.addItem(sliceItem2);
            }
            for (int i = 0; i < this.mEndItems.size(); i++) {
                builder.addSubSlice(this.mEndItems.get(i));
            }
            CharSequence charSequence = this.mContentDescr;
            if (charSequence != null) {
                builder.addText(charSequence, "content_description", new String[0]);
            }
            SliceAction sliceAction = this.mPrimaryAction;
            if (sliceAction != null) {
                sliceAction.setPrimaryAction(builder);
            }
        }
    }

    public static class HeaderBuilderImpl extends TemplateBuilderImpl {
        private CharSequence mContentDescr;
        private SliceAction mPrimaryAction;
        private SliceItem mSubtitleItem;
        private SliceItem mSummaryItem;
        private SliceItem mTitleItem;

        HeaderBuilderImpl(ListBuilderImpl listBuilderImpl) {
            super(listBuilderImpl.createChildBuilder(), (SliceSpec) null);
        }

        /* access modifiers changed from: package-private */
        public void fillFrom(ListBuilder.HeaderBuilder headerBuilder) {
            if (headerBuilder.getUri() != null) {
                setBuilder(new Slice.Builder(headerBuilder.getUri()));
            }
            setPrimaryAction(headerBuilder.getPrimaryAction());
            if (headerBuilder.getLayoutDirection() != -1) {
                setLayoutDirection(headerBuilder.getLayoutDirection());
            }
            if (headerBuilder.getTitle() != null || headerBuilder.isTitleLoading()) {
                setTitle(headerBuilder.getTitle(), headerBuilder.isTitleLoading());
            }
            if (headerBuilder.getSubtitle() != null || headerBuilder.isSubtitleLoading()) {
                setSubtitle(headerBuilder.getSubtitle(), headerBuilder.isSubtitleLoading());
            }
            if (headerBuilder.getSummary() != null || headerBuilder.isSummaryLoading()) {
                setSummary(headerBuilder.getSummary(), headerBuilder.isSummaryLoading());
            }
            if (headerBuilder.getContentDescription() != null) {
                setContentDescription(headerBuilder.getContentDescription());
            }
        }

        public void apply(Slice.Builder builder) {
            SliceItem sliceItem = this.mTitleItem;
            if (sliceItem != null) {
                builder.addItem(sliceItem);
            }
            SliceItem sliceItem2 = this.mSubtitleItem;
            if (sliceItem2 != null) {
                builder.addItem(sliceItem2);
            }
            SliceItem sliceItem3 = this.mSummaryItem;
            if (sliceItem3 != null) {
                builder.addItem(sliceItem3);
            }
            CharSequence charSequence = this.mContentDescr;
            if (charSequence != null) {
                builder.addText(charSequence, "content_description", new String[0]);
            }
            SliceAction sliceAction = this.mPrimaryAction;
            if (sliceAction != null) {
                sliceAction.setPrimaryAction(builder);
            }
            if (this.mSubtitleItem == null && this.mTitleItem == null) {
                throw new IllegalStateException("Header requires a title or subtitle to be set.");
            }
        }

        private void setTitle(CharSequence charSequence, boolean z) {
            SliceItem sliceItem = new SliceItem((Object) charSequence, "text", (String) null, new String[]{"title"});
            this.mTitleItem = sliceItem;
            if (z) {
                sliceItem.addHint("partial");
            }
        }

        private void setSubtitle(CharSequence charSequence, boolean z) {
            SliceItem sliceItem = new SliceItem((Object) charSequence, "text", (String) null, new String[0]);
            this.mSubtitleItem = sliceItem;
            if (z) {
                sliceItem.addHint("partial");
            }
        }

        private void setSummary(CharSequence charSequence, boolean z) {
            SliceItem sliceItem = new SliceItem((Object) charSequence, "text", (String) null, new String[]{"summary"});
            this.mSummaryItem = sliceItem;
            if (z) {
                sliceItem.addHint("partial");
            }
        }

        private void setPrimaryAction(SliceAction sliceAction) {
            this.mPrimaryAction = sliceAction;
        }

        private void setContentDescription(CharSequence charSequence) {
            this.mContentDescr = charSequence;
        }

        private void setLayoutDirection(int i) {
            getBuilder().addInt(i, "layout_direction", new String[0]);
        }
    }
}
