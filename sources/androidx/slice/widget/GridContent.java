package androidx.slice.widget;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.SliceItem;
import androidx.slice.SliceUtils;
import androidx.slice.core.SliceActionImpl;
import androidx.slice.core.SliceQuery;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GridContent extends SliceContent {
    private boolean mAllImages;
    private IconCompat mFirstImage = null;
    private Point mFirstImageSize = null;
    private final ArrayList<CellContent> mGridContent = new ArrayList<>();
    private boolean mIsLastIndex;
    private int mLargestImageMode = 5;
    private int mMaxCellLineCount;
    private SliceItem mPrimaryAction;
    private SliceItem mSeeMoreItem;
    private SliceItem mTitleItem;

    public GridContent(SliceItem sliceItem, int i) {
        super(sliceItem, i);
        populate(sliceItem);
    }

    private boolean populate(SliceItem sliceItem) {
        List<SliceItem> items;
        SliceItem find = SliceQuery.find(sliceItem, (String) null, "see_more", (String) null);
        this.mSeeMoreItem = find;
        if (find != null && "slice".equals(find.getFormat()) && (items = this.mSeeMoreItem.getSlice().getItems()) != null && items.size() > 0) {
            this.mSeeMoreItem = items.get(0);
        }
        this.mPrimaryAction = SliceQuery.find(sliceItem, "slice", new String[]{"shortcut", "title"}, new String[]{"actions"});
        this.mAllImages = true;
        if ("slice".equals(sliceItem.getFormat())) {
            List<SliceItem> filterAndProcessItems = filterAndProcessItems(sliceItem.getSlice().getItems());
            for (int i = 0; i < filterAndProcessItems.size(); i++) {
                SliceItem sliceItem2 = filterAndProcessItems.get(i);
                if (!"content_description".equals(sliceItem2.getSubType())) {
                    processContent(new CellContent(sliceItem2));
                }
            }
        } else {
            processContent(new CellContent(sliceItem));
        }
        return isValid();
    }

    private void processContent(CellContent cellContent) {
        int i;
        if (cellContent.isValid()) {
            if (this.mTitleItem == null && cellContent.getTitleItem() != null) {
                this.mTitleItem = cellContent.getTitleItem();
            }
            this.mGridContent.add(cellContent);
            if (!cellContent.isImageOnly()) {
                this.mAllImages = false;
            }
            this.mMaxCellLineCount = Math.max(this.mMaxCellLineCount, cellContent.getTextCount());
            if (this.mFirstImage == null && cellContent.hasImage()) {
                this.mFirstImage = cellContent.getImageIcon();
            }
            int i2 = this.mLargestImageMode;
            if (i2 == 5) {
                i = cellContent.getImageMode();
            } else {
                i = Math.max(i2, cellContent.getImageMode());
            }
            this.mLargestImageMode = i;
        }
    }

    public ArrayList<CellContent> getGridContent() {
        return this.mGridContent;
    }

    public SliceItem getContentIntent() {
        return this.mPrimaryAction;
    }

    public SliceItem getSeeMoreItem() {
        return this.mSeeMoreItem;
    }

    public boolean isValid() {
        return super.isValid() && this.mGridContent.size() > 0;
    }

    public boolean isAllImages() {
        return this.mAllImages;
    }

    public int getLargestImageMode() {
        return this.mLargestImageMode;
    }

    public Point getFirstImageSize(Context context) {
        IconCompat iconCompat = this.mFirstImage;
        if (iconCompat == null) {
            return new Point(-1, -1);
        }
        if (this.mFirstImageSize == null) {
            Drawable loadDrawable = iconCompat.loadDrawable(context);
            this.mFirstImageSize = new Point(loadDrawable.getIntrinsicWidth(), loadDrawable.getIntrinsicHeight());
        }
        return this.mFirstImageSize;
    }

    private List<SliceItem> filterAndProcessItems(List<SliceItem> list) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            SliceItem sliceItem = list.get(i);
            boolean z = true;
            if (!(SliceQuery.find(sliceItem, (String) null, "see_more", (String) null) != null) && !sliceItem.hasAnyHints("shortcut", "see_more", "keywords", "ttl", "last_updated", "overlay")) {
                z = false;
            }
            if ("content_description".equals(sliceItem.getSubType())) {
                this.mContentDescr = sliceItem;
            } else if (!z) {
                arrayList.add(sliceItem);
            }
        }
        return arrayList;
    }

    public int getMaxCellLineCount() {
        return this.mMaxCellLineCount;
    }

    public boolean hasImage() {
        return this.mFirstImage != null;
    }

    public boolean getIsLastIndex() {
        return this.mIsLastIndex;
    }

    public void setIsLastIndex(boolean z) {
        this.mIsLastIndex = z;
    }

    public int getHeight(SliceStyle sliceStyle, SliceViewPolicy sliceViewPolicy) {
        return sliceStyle.getGridHeight(this, sliceViewPolicy);
    }

    public static class CellContent {
        private final ArrayList<SliceItem> mCellItems = new ArrayList<>();
        private SliceItem mContentDescr;
        private SliceItem mContentIntent;
        private IconCompat mImage;
        private int mImageCount;
        private int mImageMode = -1;
        private SliceItem mOverlayItem;
        private SliceItem mPicker;
        private int mTextCount;
        private SliceItem mTitleItem;
        private SliceItem mToggleItem;

        public CellContent(SliceItem sliceItem) {
            populate(sliceItem);
        }

        public boolean populate(SliceItem sliceItem) {
            String format = sliceItem.getFormat();
            if (!sliceItem.hasHint("shortcut") && ("slice".equals(format) || "action".equals(format))) {
                List<SliceItem> items = sliceItem.getSlice().getItems();
                List<SliceItem> list = null;
                Iterator<SliceItem> it = items.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    SliceItem next = it.next();
                    if (("action".equals(next.getFormat()) || "slice".equals(next.getFormat())) && !"date_picker".equals(next.getSubType()) && !"time_picker".equals(next.getSubType())) {
                        list = next.getSlice().getItems();
                        if (new SliceActionImpl(next).isToggle()) {
                            this.mToggleItem = next;
                        } else {
                            this.mContentIntent = items.get(0);
                        }
                    }
                }
                if ("action".equals(format)) {
                    this.mContentIntent = sliceItem;
                }
                this.mTextCount = 0;
                this.mImageCount = 0;
                fillCellItems(items);
                if (this.mTextCount == 0 && this.mImageCount == 0 && list != null) {
                    fillCellItems(list);
                }
            } else if (isValidCellContent(sliceItem)) {
                this.mCellItems.add(sliceItem);
            }
            return isValid();
        }

        private void fillCellItems(List<SliceItem> list) {
            for (int i = 0; i < list.size(); i++) {
                SliceItem sliceItem = list.get(i);
                String format = sliceItem.getFormat();
                if (this.mPicker == null && ("date_picker".equals(sliceItem.getSubType()) || "time_picker".equals(sliceItem.getSubType()))) {
                    this.mPicker = sliceItem;
                } else if ("content_description".equals(sliceItem.getSubType())) {
                    this.mContentDescr = sliceItem;
                } else if (this.mTextCount < 2 && ("text".equals(format) || "long".equals(format))) {
                    SliceItem sliceItem2 = this.mTitleItem;
                    if (sliceItem2 == null || (!sliceItem2.hasHint("title") && sliceItem.hasHint("title"))) {
                        this.mTitleItem = sliceItem;
                    }
                    if (!sliceItem.hasHint("overlay")) {
                        this.mTextCount++;
                        this.mCellItems.add(sliceItem);
                    } else if (this.mOverlayItem == null) {
                        this.mOverlayItem = sliceItem;
                    }
                } else if (this.mImageCount < 1 && "image".equals(sliceItem.getFormat())) {
                    this.mImageMode = SliceUtils.parseImageMode(sliceItem);
                    this.mImageCount++;
                    this.mImage = sliceItem.getIcon();
                    this.mCellItems.add(sliceItem);
                }
            }
        }

        public SliceItem getToggleItem() {
            return this.mToggleItem;
        }

        public SliceItem getTitleItem() {
            return this.mTitleItem;
        }

        public SliceItem getOverlayItem() {
            return this.mOverlayItem;
        }

        public SliceItem getContentIntent() {
            return this.mContentIntent;
        }

        public SliceItem getPicker() {
            return this.mPicker;
        }

        public ArrayList<SliceItem> getCellItems() {
            return this.mCellItems;
        }

        private boolean isValidCellContent(SliceItem sliceItem) {
            String format = sliceItem.getFormat();
            if ("content_description".equals(sliceItem.getSubType()) || sliceItem.hasAnyHints("keywords", "ttl", "last_updated")) {
                return false;
            }
            if ("text".equals(format) || "long".equals(format) || "image".equals(format)) {
                return true;
            }
            return false;
        }

        public boolean isValid() {
            return this.mPicker != null || (this.mCellItems.size() > 0 && this.mCellItems.size() <= 3);
        }

        public boolean isImageOnly() {
            return this.mCellItems.size() == 1 && "image".equals(this.mCellItems.get(0).getFormat());
        }

        public int getTextCount() {
            return this.mTextCount;
        }

        public boolean hasImage() {
            return this.mImage != null;
        }

        public int getImageMode() {
            return this.mImageMode;
        }

        public IconCompat getImageIcon() {
            return this.mImage;
        }

        public CharSequence getContentDescription() {
            SliceItem sliceItem = this.mContentDescr;
            if (sliceItem != null) {
                return sliceItem.getText();
            }
            return null;
        }
    }
}
