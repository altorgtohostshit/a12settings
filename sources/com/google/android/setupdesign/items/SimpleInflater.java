package com.google.android.setupdesign.items;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.view.InflateException;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public abstract class SimpleInflater<T> {
    protected final Resources resources;

    /* access modifiers changed from: protected */
    public abstract void onAddChildItem(T t, T t2);

    /* access modifiers changed from: protected */
    public abstract T onCreateItem(String str, AttributeSet attributeSet);

    /* access modifiers changed from: protected */
    public boolean onInterceptCreateItem(XmlPullParser xmlPullParser, T t, AttributeSet attributeSet) throws XmlPullParserException {
        return false;
    }

    protected SimpleInflater(Resources resources2) {
        this.resources = resources2;
    }

    public Resources getResources() {
        return this.resources;
    }

    public T inflate(int i) {
        XmlResourceParser xml = getResources().getXml(i);
        try {
            return inflate((XmlPullParser) xml);
        } finally {
            xml.close();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x001d A[Catch:{ XmlPullParserException -> 0x005b, IOException -> 0x0038 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0011 A[Catch:{ XmlPullParserException -> 0x005b, IOException -> 0x0038 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public T inflate(org.xmlpull.v1.XmlPullParser r5) {
        /*
            r4 = this;
            android.util.AttributeSet r0 = android.util.Xml.asAttributeSet(r5)
        L_0x0004:
            int r1 = r5.next()     // Catch:{ XmlPullParserException -> 0x005b, IOException -> 0x0038 }
            r2 = 2
            if (r1 == r2) goto L_0x000f
            r3 = 1
            if (r1 == r3) goto L_0x000f
            goto L_0x0004
        L_0x000f:
            if (r1 != r2) goto L_0x001d
            java.lang.String r1 = r5.getName()     // Catch:{ XmlPullParserException -> 0x005b, IOException -> 0x0038 }
            java.lang.Object r1 = r4.createItemFromTag(r1, r0)     // Catch:{ XmlPullParserException -> 0x005b, IOException -> 0x0038 }
            r4.rInflate(r5, r1, r0)     // Catch:{ XmlPullParserException -> 0x005b, IOException -> 0x0038 }
            return r1
        L_0x001d:
            android.view.InflateException r4 = new android.view.InflateException     // Catch:{ XmlPullParserException -> 0x005b, IOException -> 0x0038 }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ XmlPullParserException -> 0x005b, IOException -> 0x0038 }
            r0.<init>()     // Catch:{ XmlPullParserException -> 0x005b, IOException -> 0x0038 }
            java.lang.String r1 = r5.getPositionDescription()     // Catch:{ XmlPullParserException -> 0x005b, IOException -> 0x0038 }
            r0.append(r1)     // Catch:{ XmlPullParserException -> 0x005b, IOException -> 0x0038 }
            java.lang.String r1 = ": No start tag found!"
            r0.append(r1)     // Catch:{ XmlPullParserException -> 0x005b, IOException -> 0x0038 }
            java.lang.String r0 = r0.toString()     // Catch:{ XmlPullParserException -> 0x005b, IOException -> 0x0038 }
            r4.<init>(r0)     // Catch:{ XmlPullParserException -> 0x005b, IOException -> 0x0038 }
            throw r4     // Catch:{ XmlPullParserException -> 0x005b, IOException -> 0x0038 }
        L_0x0038:
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
        L_0x005b:
            r4 = move-exception
            android.view.InflateException r5 = new android.view.InflateException
            java.lang.String r0 = r4.getMessage()
            r5.<init>(r0, r4)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.setupdesign.items.SimpleInflater.inflate(org.xmlpull.v1.XmlPullParser):java.lang.Object");
    }

    private T createItemFromTag(String str, AttributeSet attributeSet) {
        try {
            return onCreateItem(str, attributeSet);
        } catch (InflateException e) {
            throw e;
        } catch (Exception e2) {
            throw new InflateException(attributeSet.getPositionDescription() + ": Error inflating class " + str, e2);
        }
    }

    private void rInflate(XmlPullParser xmlPullParser, T t, AttributeSet attributeSet) throws XmlPullParserException, IOException {
        int depth = xmlPullParser.getDepth();
        while (true) {
            int next = xmlPullParser.next();
            if ((next == 3 && xmlPullParser.getDepth() <= depth) || next == 1) {
                return;
            }
            if (next == 2 && !onInterceptCreateItem(xmlPullParser, t, attributeSet)) {
                Object createItemFromTag = createItemFromTag(xmlPullParser.getName(), attributeSet);
                onAddChildItem(t, createItemFromTag);
                rInflate(xmlPullParser, createItemFromTag, attributeSet);
            }
        }
    }
}
