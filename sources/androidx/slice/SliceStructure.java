package androidx.slice;

import android.net.Uri;

public class SliceStructure {
    private final String mStructure;
    private final Uri mUri;

    public SliceStructure(Slice slice) {
        StringBuilder sb = new StringBuilder();
        getStructure(slice, sb);
        this.mStructure = sb.toString();
        this.mUri = slice.getUri();
    }

    public SliceStructure(SliceItem sliceItem) {
        StringBuilder sb = new StringBuilder();
        getStructure(sliceItem, sb);
        this.mStructure = sb.toString();
        if ("action".equals(sliceItem.getFormat()) || "slice".equals(sliceItem.getFormat())) {
            this.mUri = sliceItem.getSlice().getUri();
        } else {
            this.mUri = null;
        }
    }

    public Uri getUri() {
        return this.mUri;
    }

    public int hashCode() {
        return this.mStructure.hashCode();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof SliceStructure)) {
            return false;
        }
        return this.mStructure.equals(((SliceStructure) obj).mStructure);
    }

    private static void getStructure(Slice slice, StringBuilder sb) {
        sb.append("s{");
        for (SliceItem structure : slice.getItems()) {
            getStructure(structure, sb);
        }
        sb.append("}");
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void getStructure(androidx.slice.SliceItem r5, java.lang.StringBuilder r6) {
        /*
            java.lang.String r0 = r5.getFormat()
            int r1 = r0.hashCode()
            r2 = 3
            r3 = 2
            r4 = 1
            switch(r1) {
                case -1422950858: goto L_0x0055;
                case -1377881982: goto L_0x004b;
                case 104431: goto L_0x0041;
                case 3327612: goto L_0x0037;
                case 3556653: goto L_0x002d;
                case 100313435: goto L_0x0023;
                case 100358090: goto L_0x0019;
                case 109526418: goto L_0x000f;
                default: goto L_0x000e;
            }
        L_0x000e:
            goto L_0x005f
        L_0x000f:
            java.lang.String r1 = "slice"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x005f
            r0 = 0
            goto L_0x0060
        L_0x0019:
            java.lang.String r1 = "input"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x005f
            r0 = 6
            goto L_0x0060
        L_0x0023:
            java.lang.String r1 = "image"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x005f
            r0 = r2
            goto L_0x0060
        L_0x002d:
            java.lang.String r1 = "text"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x005f
            r0 = r3
            goto L_0x0060
        L_0x0037:
            java.lang.String r1 = "long"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x005f
            r0 = 5
            goto L_0x0060
        L_0x0041:
            java.lang.String r1 = "int"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x005f
            r0 = 4
            goto L_0x0060
        L_0x004b:
            java.lang.String r1 = "bundle"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x005f
            r0 = 7
            goto L_0x0060
        L_0x0055:
            java.lang.String r1 = "action"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x005f
            r0 = r4
            goto L_0x0060
        L_0x005f:
            r0 = -1
        L_0x0060:
            if (r0 == 0) goto L_0x0093
            if (r0 == r4) goto L_0x0075
            if (r0 == r3) goto L_0x006f
            if (r0 == r2) goto L_0x0069
            goto L_0x009a
        L_0x0069:
            r5 = 105(0x69, float:1.47E-43)
            r6.append(r5)
            goto L_0x009a
        L_0x006f:
            r5 = 116(0x74, float:1.63E-43)
            r6.append(r5)
            goto L_0x009a
        L_0x0075:
            r0 = 97
            r6.append(r0)
            java.lang.String r0 = r5.getSubType()
            java.lang.String r1 = "range"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x008b
            r0 = 114(0x72, float:1.6E-43)
            r6.append(r0)
        L_0x008b:
            androidx.slice.Slice r5 = r5.getSlice()
            getStructure((androidx.slice.Slice) r5, (java.lang.StringBuilder) r6)
            goto L_0x009a
        L_0x0093:
            androidx.slice.Slice r5 = r5.getSlice()
            getStructure((androidx.slice.Slice) r5, (java.lang.StringBuilder) r6)
        L_0x009a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.slice.SliceStructure.getStructure(androidx.slice.SliceItem, java.lang.StringBuilder):void");
    }
}
