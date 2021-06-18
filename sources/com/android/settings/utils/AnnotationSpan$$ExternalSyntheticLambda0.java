package com.android.settings.utils;

import android.text.Annotation;
import android.text.SpannableString;
import java.util.function.ToIntFunction;

public final /* synthetic */ class AnnotationSpan$$ExternalSyntheticLambda0 implements ToIntFunction {
    public final /* synthetic */ SpannableString f$0;

    public /* synthetic */ AnnotationSpan$$ExternalSyntheticLambda0(SpannableString spannableString) {
        this.f$0 = spannableString;
    }

    public final int applyAsInt(Object obj) {
        return AnnotationSpan.lambda$textWithoutLink$0(this.f$0, (Annotation) obj);
    }
}
