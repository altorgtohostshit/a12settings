package androidx.slice.builders.impl;

import android.os.Bundle;
import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.Slice;
import androidx.slice.SliceItem;
import androidx.slice.SliceSpec;
import androidx.slice.builders.ListBuilder;
import androidx.slice.builders.SliceAction;
import java.util.Set;

public class ListBuilderBasicImpl extends TemplateBuilderImpl implements ListBuilder {
    private Bundle mHostExtras;
    private IconCompat mIconCompat;
    boolean mIsError;
    private Set<String> mKeywords;
    private SliceAction mSliceAction;
    private CharSequence mSubtitle;
    private CharSequence mTitle;

    public void addAction(SliceAction sliceAction) {
    }

    public ListBuilderBasicImpl(Slice.Builder builder, SliceSpec sliceSpec) {
        super(builder, sliceSpec);
    }

    public void addRow(ListBuilder.RowBuilder rowBuilder) {
        if (this.mTitle == null && rowBuilder.getTitle() != null) {
            this.mTitle = rowBuilder.getTitle();
        }
        if (this.mSubtitle == null && rowBuilder.getSubtitle() != null) {
            this.mSubtitle = rowBuilder.getSubtitle();
        }
        if (this.mSliceAction == null && rowBuilder.getPrimaryAction() != null) {
            this.mSliceAction = rowBuilder.getPrimaryAction();
        }
        if (this.mSliceAction == null && rowBuilder.getTitleAction() != null) {
            this.mSliceAction = rowBuilder.getTitleAction();
        }
        if (this.mIconCompat == null && rowBuilder.getTitleIcon() != null) {
            this.mIconCompat = rowBuilder.getTitleIcon();
        }
    }

    public void setHeader(ListBuilder.HeaderBuilder headerBuilder) {
        if (headerBuilder.getTitle() != null) {
            this.mTitle = headerBuilder.getTitle();
        }
        if (headerBuilder.getSubtitle() != null) {
            this.mSubtitle = headerBuilder.getSubtitle();
        }
        if (headerBuilder.getPrimaryAction() != null) {
            this.mSliceAction = headerBuilder.getPrimaryAction();
        }
    }

    public void addInputRange(ListBuilder.InputRangeBuilder inputRangeBuilder) {
        if (this.mTitle == null && inputRangeBuilder.getTitle() != null) {
            this.mTitle = inputRangeBuilder.getTitle();
        }
        if (this.mSubtitle == null && inputRangeBuilder.getSubtitle() != null) {
            this.mSubtitle = inputRangeBuilder.getSubtitle();
        }
        if (this.mSliceAction == null && inputRangeBuilder.getPrimaryAction() != null) {
            this.mSliceAction = inputRangeBuilder.getPrimaryAction();
        }
        if (this.mIconCompat == null && inputRangeBuilder.getThumb() != null) {
            this.mIconCompat = inputRangeBuilder.getThumb();
        }
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

    public void apply(Slice.Builder builder) {
        if (this.mIsError) {
            builder.addHints("error");
        }
        if (this.mKeywords != null) {
            Slice.Builder builder2 = new Slice.Builder(getBuilder());
            for (String addText : this.mKeywords) {
                builder2.addText((CharSequence) addText, (String) null, new String[0]);
            }
            builder.addSubSlice(builder2.addHints("keywords").build());
        }
        Slice.Builder builder3 = new Slice.Builder(getBuilder());
        SliceAction sliceAction = this.mSliceAction;
        if (sliceAction != null) {
            if (this.mTitle == null && sliceAction.getTitle() != null) {
                this.mTitle = this.mSliceAction.getTitle();
            }
            if (this.mIconCompat == null && this.mSliceAction.getIcon() != null) {
                this.mIconCompat = this.mSliceAction.getIcon();
            }
            this.mSliceAction.setPrimaryAction(builder3);
        }
        CharSequence charSequence = this.mTitle;
        if (charSequence != null) {
            builder3.addItem(new SliceItem((Object) charSequence, "text", (String) null, new String[]{"title"}));
        }
        CharSequence charSequence2 = this.mSubtitle;
        if (charSequence2 != null) {
            builder3.addItem(new SliceItem((Object) charSequence2, "text", (String) null, new String[0]));
        }
        IconCompat iconCompat = this.mIconCompat;
        if (iconCompat != null) {
            builder.addIcon(iconCompat, (String) null, "title");
        }
        Bundle bundle = this.mHostExtras;
        if (bundle != null) {
            builder3.addItem(new SliceItem((Object) bundle, "bundle", "host_extras", new String[0]));
        }
        builder.addSubSlice(builder3.build());
    }
}
