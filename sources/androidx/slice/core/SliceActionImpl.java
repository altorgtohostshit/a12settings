package androidx.slice.core;

import android.app.PendingIntent;
import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.Slice;
import androidx.slice.SliceItem;

public class SliceActionImpl implements SliceAction {
    private PendingIntent mAction;
    private SliceItem mActionItem;
    private String mActionKey;
    private ActionType mActionType;
    private CharSequence mContentDescription;
    private long mDateTimeMillis;
    private IconCompat mIcon;
    private int mImageMode;
    private boolean mIsActivity;
    private boolean mIsChecked;
    private int mPriority;
    private SliceItem mSliceItem;
    private CharSequence mTitle;

    enum ActionType {
        DEFAULT,
        TOGGLE,
        DATE_PICKER,
        TIME_PICKER
    }

    public SliceActionImpl(PendingIntent pendingIntent, IconCompat iconCompat, int i, CharSequence charSequence) {
        this.mImageMode = 5;
        this.mActionType = ActionType.DEFAULT;
        this.mPriority = -1;
        this.mDateTimeMillis = -1;
        this.mAction = pendingIntent;
        this.mIcon = iconCompat;
        this.mTitle = charSequence;
        this.mImageMode = i;
    }

    public SliceActionImpl(PendingIntent pendingIntent, IconCompat iconCompat, CharSequence charSequence, boolean z) {
        this(pendingIntent, iconCompat, 0, charSequence);
        this.mIsChecked = z;
        this.mActionType = ActionType.TOGGLE;
    }

    public SliceActionImpl(PendingIntent pendingIntent, CharSequence charSequence, boolean z) {
        this.mImageMode = 5;
        this.mActionType = ActionType.DEFAULT;
        this.mPriority = -1;
        this.mDateTimeMillis = -1;
        this.mAction = pendingIntent;
        this.mTitle = charSequence;
        this.mActionType = ActionType.TOGGLE;
        this.mIsChecked = z;
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    @android.annotation.SuppressLint({"InlinedApi"})
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public SliceActionImpl(androidx.slice.SliceItem r7) {
        /*
            r6 = this;
            r6.<init>()
            r0 = 5
            r6.mImageMode = r0
            androidx.slice.core.SliceActionImpl$ActionType r0 = androidx.slice.core.SliceActionImpl.ActionType.DEFAULT
            r6.mActionType = r0
            r1 = -1
            r6.mPriority = r1
            r2 = -1
            r6.mDateTimeMillis = r2
            r6.mSliceItem = r7
            java.lang.String r2 = "action"
            androidx.slice.SliceItem r7 = androidx.slice.core.SliceQuery.find((androidx.slice.SliceItem) r7, (java.lang.String) r2)
            if (r7 != 0) goto L_0x001c
            return
        L_0x001c:
            r6.mActionItem = r7
            android.app.PendingIntent r2 = r7.getAction()
            r6.mAction = r2
            androidx.slice.Slice r2 = r7.getSlice()
            java.lang.String r3 = "image"
            androidx.slice.SliceItem r2 = androidx.slice.core.SliceQuery.find((androidx.slice.Slice) r2, (java.lang.String) r3)
            if (r2 == 0) goto L_0x003c
            androidx.core.graphics.drawable.IconCompat r3 = r2.getIcon()
            r6.mIcon = r3
            int r2 = parseImageMode(r2)
            r6.mImageMode = r2
        L_0x003c:
            androidx.slice.Slice r2 = r7.getSlice()
            r3 = 0
            java.lang.String r4 = "text"
            java.lang.String r5 = "title"
            androidx.slice.SliceItem r2 = androidx.slice.core.SliceQuery.find((androidx.slice.Slice) r2, (java.lang.String) r4, (java.lang.String) r5, (java.lang.String) r3)
            if (r2 == 0) goto L_0x0051
            java.lang.CharSequence r2 = r2.getSanitizedText()
            r6.mTitle = r2
        L_0x0051:
            androidx.slice.Slice r2 = r7.getSlice()
            java.lang.String r3 = "content_description"
            androidx.slice.SliceItem r2 = androidx.slice.core.SliceQuery.findSubtype((androidx.slice.Slice) r2, (java.lang.String) r4, (java.lang.String) r3)
            if (r2 == 0) goto L_0x0063
            java.lang.CharSequence r2 = r2.getText()
            r6.mContentDescription = r2
        L_0x0063:
            java.lang.String r2 = r7.getSubType()
            if (r2 != 0) goto L_0x006d
            r6.mActionType = r0
            goto L_0x00d5
        L_0x006d:
            java.lang.String r2 = r7.getSubType()
            r2.hashCode()
            int r3 = r2.hashCode()
            switch(r3) {
                case -868304044: goto L_0x0093;
                case 759128640: goto L_0x0088;
                case 1250407999: goto L_0x007d;
                default: goto L_0x007b;
            }
        L_0x007b:
            r2 = r1
            goto L_0x009d
        L_0x007d:
            java.lang.String r3 = "date_picker"
            boolean r2 = r2.equals(r3)
            if (r2 != 0) goto L_0x0086
            goto L_0x007b
        L_0x0086:
            r2 = 2
            goto L_0x009d
        L_0x0088:
            java.lang.String r3 = "time_picker"
            boolean r2 = r2.equals(r3)
            if (r2 != 0) goto L_0x0091
            goto L_0x007b
        L_0x0091:
            r2 = 1
            goto L_0x009d
        L_0x0093:
            java.lang.String r3 = "toggle"
            boolean r2 = r2.equals(r3)
            if (r2 != 0) goto L_0x009c
            goto L_0x007b
        L_0x009c:
            r2 = 0
        L_0x009d:
            java.lang.String r3 = "millis"
            java.lang.String r5 = "long"
            switch(r2) {
                case 0: goto L_0x00c9;
                case 1: goto L_0x00b8;
                case 2: goto L_0x00a7;
                default: goto L_0x00a4;
            }
        L_0x00a4:
            r6.mActionType = r0
            goto L_0x00d5
        L_0x00a7:
            androidx.slice.core.SliceActionImpl$ActionType r0 = androidx.slice.core.SliceActionImpl.ActionType.DATE_PICKER
            r6.mActionType = r0
            androidx.slice.SliceItem r0 = androidx.slice.core.SliceQuery.findSubtype((androidx.slice.SliceItem) r7, (java.lang.String) r5, (java.lang.String) r3)
            if (r0 == 0) goto L_0x00d5
            long r2 = r0.getLong()
            r6.mDateTimeMillis = r2
            goto L_0x00d5
        L_0x00b8:
            androidx.slice.core.SliceActionImpl$ActionType r0 = androidx.slice.core.SliceActionImpl.ActionType.TIME_PICKER
            r6.mActionType = r0
            androidx.slice.SliceItem r0 = androidx.slice.core.SliceQuery.findSubtype((androidx.slice.SliceItem) r7, (java.lang.String) r5, (java.lang.String) r3)
            if (r0 == 0) goto L_0x00d5
            long r2 = r0.getLong()
            r6.mDateTimeMillis = r2
            goto L_0x00d5
        L_0x00c9:
            androidx.slice.core.SliceActionImpl$ActionType r0 = androidx.slice.core.SliceActionImpl.ActionType.TOGGLE
            r6.mActionType = r0
            java.lang.String r0 = "selected"
            boolean r0 = r7.hasHint(r0)
            r6.mIsChecked = r0
        L_0x00d5:
            androidx.slice.SliceItem r0 = r6.mSliceItem
            java.lang.String r2 = "activity"
            boolean r0 = r0.hasHint(r2)
            r6.mIsActivity = r0
            androidx.slice.Slice r0 = r7.getSlice()
            java.lang.String r2 = "int"
            java.lang.String r3 = "priority"
            androidx.slice.SliceItem r0 = androidx.slice.core.SliceQuery.findSubtype((androidx.slice.Slice) r0, (java.lang.String) r2, (java.lang.String) r3)
            if (r0 == 0) goto L_0x00f1
            int r1 = r0.getInt()
        L_0x00f1:
            r6.mPriority = r1
            androidx.slice.Slice r7 = r7.getSlice()
            java.lang.String r0 = "action_key"
            androidx.slice.SliceItem r7 = androidx.slice.core.SliceQuery.findSubtype((androidx.slice.Slice) r7, (java.lang.String) r4, (java.lang.String) r0)
            if (r7 == 0) goto L_0x0109
            java.lang.CharSequence r7 = r7.getText()
            java.lang.String r7 = r7.toString()
            r6.mActionKey = r7
        L_0x0109:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.slice.core.SliceActionImpl.<init>(androidx.slice.SliceItem):void");
    }

    public PendingIntent getAction() {
        PendingIntent pendingIntent = this.mAction;
        return pendingIntent != null ? pendingIntent : this.mActionItem.getAction();
    }

    public SliceItem getActionItem() {
        return this.mActionItem;
    }

    public IconCompat getIcon() {
        return this.mIcon;
    }

    public CharSequence getTitle() {
        return this.mTitle;
    }

    public CharSequence getContentDescription() {
        return this.mContentDescription;
    }

    public int getPriority() {
        return this.mPriority;
    }

    public String getKey() {
        return this.mActionKey;
    }

    public boolean isToggle() {
        return this.mActionType == ActionType.TOGGLE;
    }

    public boolean isChecked() {
        return this.mIsChecked;
    }

    public int getImageMode() {
        return this.mImageMode;
    }

    public boolean isDefaultToggle() {
        return this.mActionType == ActionType.TOGGLE && this.mIcon == null;
    }

    public SliceItem getSliceItem() {
        return this.mSliceItem;
    }

    public Slice buildSlice(Slice.Builder builder) {
        return builder.addHints("shortcut").addAction(this.mAction, buildSliceContent(builder).build(), getSubtype()).build();
    }

    public Slice buildPrimaryActionSlice(Slice.Builder builder) {
        return buildSliceContent(builder).addHints("shortcut", "title").build();
    }

    private Slice.Builder buildSliceContent(Slice.Builder builder) {
        Slice.Builder builder2 = new Slice.Builder(builder);
        IconCompat iconCompat = this.mIcon;
        if (iconCompat != null) {
            int i = this.mImageMode;
            builder2.addIcon(iconCompat, (String) null, i == 6 ? new String[]{"show_label"} : i == 0 ? new String[0] : new String[]{"no_tint"});
        }
        CharSequence charSequence = this.mTitle;
        if (charSequence != null) {
            builder2.addText(charSequence, (String) null, "title");
        }
        CharSequence charSequence2 = this.mContentDescription;
        if (charSequence2 != null) {
            builder2.addText(charSequence2, "content_description", new String[0]);
        }
        long j = this.mDateTimeMillis;
        if (j != -1) {
            builder2.addLong(j, "millis", new String[0]);
        }
        if (this.mActionType == ActionType.TOGGLE && this.mIsChecked) {
            builder2.addHints("selected");
        }
        int i2 = this.mPriority;
        if (i2 != -1) {
            builder2.addInt(i2, "priority", new String[0]);
        }
        String str = this.mActionKey;
        if (str != null) {
            builder2.addText((CharSequence) str, "action_key", new String[0]);
        }
        if (this.mIsActivity) {
            builder.addHints("activity");
        }
        return builder2;
    }

    /* renamed from: androidx.slice.core.SliceActionImpl$1 */
    static /* synthetic */ class C03801 {
        static final /* synthetic */ int[] $SwitchMap$androidx$slice$core$SliceActionImpl$ActionType;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|(3:5|6|8)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        static {
            /*
                androidx.slice.core.SliceActionImpl$ActionType[] r0 = androidx.slice.core.SliceActionImpl.ActionType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$androidx$slice$core$SliceActionImpl$ActionType = r0
                androidx.slice.core.SliceActionImpl$ActionType r1 = androidx.slice.core.SliceActionImpl.ActionType.TOGGLE     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$androidx$slice$core$SliceActionImpl$ActionType     // Catch:{ NoSuchFieldError -> 0x001d }
                androidx.slice.core.SliceActionImpl$ActionType r1 = androidx.slice.core.SliceActionImpl.ActionType.DATE_PICKER     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$androidx$slice$core$SliceActionImpl$ActionType     // Catch:{ NoSuchFieldError -> 0x0028 }
                androidx.slice.core.SliceActionImpl$ActionType r1 = androidx.slice.core.SliceActionImpl.ActionType.TIME_PICKER     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.slice.core.SliceActionImpl.C03801.<clinit>():void");
        }
    }

    public String getSubtype() {
        int i = C03801.$SwitchMap$androidx$slice$core$SliceActionImpl$ActionType[this.mActionType.ordinal()];
        if (i == 1) {
            return "toggle";
        }
        if (i == 2) {
            return "date_picker";
        }
        if (i != 3) {
            return null;
        }
        return "time_picker";
    }

    public void setActivity(boolean z) {
        this.mIsActivity = z;
    }

    public static int parseImageMode(SliceItem sliceItem) {
        if (sliceItem.hasHint("show_label")) {
            return 6;
        }
        if (!sliceItem.hasHint("no_tint")) {
            return 0;
        }
        return sliceItem.hasHint("raw") ? sliceItem.hasHint("large") ? 4 : 3 : sliceItem.hasHint("large") ? 2 : 1;
    }
}
