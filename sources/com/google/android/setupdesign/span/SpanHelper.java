package com.google.android.setupdesign.span;

import android.text.Spannable;

public class SpanHelper {
    public static void replaceSpan(Spannable spannable, Object obj, Object... objArr) {
        int spanStart = spannable.getSpanStart(obj);
        int spanEnd = spannable.getSpanEnd(obj);
        spannable.removeSpan(obj);
        for (Object span : objArr) {
            spannable.setSpan(span, spanStart, spanEnd, 0);
        }
    }
}
