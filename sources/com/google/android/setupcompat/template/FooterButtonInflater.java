package com.google.android.setupcompat.template;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import org.xmlpull.v1.XmlPullParser;

class FooterButtonInflater {
    protected final Context context;

    public FooterButtonInflater(Context context2) {
        this.context = context2;
    }

    public Resources getResources() {
        return this.context.getResources();
    }

    public FooterButton inflate(int i) {
        XmlResourceParser xml = getResources().getXml(i);
        try {
            return inflate((XmlPullParser) xml);
        } finally {
            xml.close();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0040 A[Catch:{ XmlPullParserException -> 0x007e, IOException -> 0x005b }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0011 A[Catch:{ XmlPullParserException -> 0x007e, IOException -> 0x005b }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.google.android.setupcompat.template.FooterButton inflate(org.xmlpull.v1.XmlPullParser r5) {
        /*
            r4 = this;
            android.util.AttributeSet r0 = android.util.Xml.asAttributeSet(r5)
        L_0x0004:
            int r1 = r5.next()     // Catch:{ XmlPullParserException -> 0x007e, IOException -> 0x005b }
            r2 = 2
            if (r1 == r2) goto L_0x000f
            r3 = 1
            if (r1 == r3) goto L_0x000f
            goto L_0x0004
        L_0x000f:
            if (r1 != r2) goto L_0x0040
            java.lang.String r1 = r5.getName()     // Catch:{ XmlPullParserException -> 0x007e, IOException -> 0x005b }
            java.lang.String r2 = "FooterButton"
            boolean r1 = r1.equals(r2)     // Catch:{ XmlPullParserException -> 0x007e, IOException -> 0x005b }
            if (r1 == 0) goto L_0x0025
            com.google.android.setupcompat.template.FooterButton r1 = new com.google.android.setupcompat.template.FooterButton     // Catch:{ XmlPullParserException -> 0x007e, IOException -> 0x005b }
            android.content.Context r4 = r4.context     // Catch:{ XmlPullParserException -> 0x007e, IOException -> 0x005b }
            r1.<init>(r4, r0)     // Catch:{ XmlPullParserException -> 0x007e, IOException -> 0x005b }
            return r1
        L_0x0025:
            android.view.InflateException r4 = new android.view.InflateException     // Catch:{ XmlPullParserException -> 0x007e, IOException -> 0x005b }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ XmlPullParserException -> 0x007e, IOException -> 0x005b }
            r0.<init>()     // Catch:{ XmlPullParserException -> 0x007e, IOException -> 0x005b }
            java.lang.String r1 = r5.getPositionDescription()     // Catch:{ XmlPullParserException -> 0x007e, IOException -> 0x005b }
            r0.append(r1)     // Catch:{ XmlPullParserException -> 0x007e, IOException -> 0x005b }
            java.lang.String r1 = ": not a FooterButton"
            r0.append(r1)     // Catch:{ XmlPullParserException -> 0x007e, IOException -> 0x005b }
            java.lang.String r0 = r0.toString()     // Catch:{ XmlPullParserException -> 0x007e, IOException -> 0x005b }
            r4.<init>(r0)     // Catch:{ XmlPullParserException -> 0x007e, IOException -> 0x005b }
            throw r4     // Catch:{ XmlPullParserException -> 0x007e, IOException -> 0x005b }
        L_0x0040:
            android.view.InflateException r4 = new android.view.InflateException     // Catch:{ XmlPullParserException -> 0x007e, IOException -> 0x005b }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ XmlPullParserException -> 0x007e, IOException -> 0x005b }
            r0.<init>()     // Catch:{ XmlPullParserException -> 0x007e, IOException -> 0x005b }
            java.lang.String r1 = r5.getPositionDescription()     // Catch:{ XmlPullParserException -> 0x007e, IOException -> 0x005b }
            r0.append(r1)     // Catch:{ XmlPullParserException -> 0x007e, IOException -> 0x005b }
            java.lang.String r1 = ": No start tag found!"
            r0.append(r1)     // Catch:{ XmlPullParserException -> 0x007e, IOException -> 0x005b }
            java.lang.String r0 = r0.toString()     // Catch:{ XmlPullParserException -> 0x007e, IOException -> 0x005b }
            r4.<init>(r0)     // Catch:{ XmlPullParserException -> 0x007e, IOException -> 0x005b }
            throw r4     // Catch:{ XmlPullParserException -> 0x007e, IOException -> 0x005b }
        L_0x005b:
            r4 = move-exception
            android.view.InflateException r0 = new android.view.InflateException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r5 = r5.getPositionDescription()
            r1.append(r5)
            java.lang.String r5 = ": "
            r1.append(r5)
            java.lang.String r5 = r4.getMessage()
            r1.append(r5)
            java.lang.String r5 = r1.toString()
            r0.<init>(r5, r4)
            throw r0
        L_0x007e:
            r4 = move-exception
            android.view.InflateException r5 = new android.view.InflateException
            java.lang.String r0 = r4.getMessage()
            r5.<init>(r0, r4)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.setupcompat.template.FooterButtonInflater.inflate(org.xmlpull.v1.XmlPullParser):com.google.android.setupcompat.template.FooterButton");
    }
}
