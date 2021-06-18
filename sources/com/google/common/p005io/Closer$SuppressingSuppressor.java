package com.google.common.p005io;

import java.lang.reflect.Method;

/* renamed from: com.google.common.io.Closer$SuppressingSuppressor */
final class Closer$SuppressingSuppressor implements Closer$Suppressor {
    static final Closer$SuppressingSuppressor INSTANCE = new Closer$SuppressingSuppressor();
    static final Method addSuppressed = addSuppressedMethodOrNull();

    Closer$SuppressingSuppressor() {
    }

    private static Method addSuppressedMethodOrNull() {
        try {
            return Throwable.class.getMethod("addSuppressed", new Class[]{Throwable.class});
        } catch (Throwable unused) {
            return null;
        }
    }
}
