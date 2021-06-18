package com.airbnb.lottie.parser;

import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import androidx.collection.SparseArrayCompat;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.parser.moshi.JsonReader;
import com.airbnb.lottie.value.Keyframe;
import java.io.IOException;
import java.lang.ref.WeakReference;

class KeyframeParser {
    private static final Interpolator LINEAR_INTERPOLATOR = new LinearInterpolator();
    static JsonReader.Options NAMES = JsonReader.Options.m8of("t", "s", "e", "o", "i", "h", "to", "ti");
    private static SparseArrayCompat<WeakReference<Interpolator>> pathInterpolatorCache;

    KeyframeParser() {
    }

    private static SparseArrayCompat<WeakReference<Interpolator>> pathInterpolatorCache() {
        if (pathInterpolatorCache == null) {
            pathInterpolatorCache = new SparseArrayCompat<>();
        }
        return pathInterpolatorCache;
    }

    private static WeakReference<Interpolator> getInterpolator(int i) {
        WeakReference<Interpolator> weakReference;
        synchronized (KeyframeParser.class) {
            weakReference = pathInterpolatorCache().get(i);
        }
        return weakReference;
    }

    private static void putInterpolator(int i, WeakReference<Interpolator> weakReference) {
        synchronized (KeyframeParser.class) {
            pathInterpolatorCache.put(i, weakReference);
        }
    }

    static <T> Keyframe<T> parse(JsonReader jsonReader, LottieComposition lottieComposition, float f, ValueParser<T> valueParser, boolean z) throws IOException {
        if (z) {
            return parseKeyframe(lottieComposition, jsonReader, f, valueParser);
        }
        return parseStaticValue(jsonReader, f, valueParser);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v5, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v6, resolved type: android.view.animation.Interpolator} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static <T> com.airbnb.lottie.value.Keyframe<T> parseKeyframe(com.airbnb.lottie.LottieComposition r12, com.airbnb.lottie.parser.moshi.JsonReader r13, float r14, com.airbnb.lottie.parser.ValueParser<T> r15) throws java.io.IOException {
        /*
            r13.beginObject()
            r0 = 0
            r1 = 0
            r2 = 0
            r3 = r1
            r4 = r3
            r5 = r4
            r6 = r5
            r10 = r6
            r11 = r10
            r8 = r2
        L_0x000d:
            r2 = r0
        L_0x000e:
            boolean r7 = r13.hasNext()
            if (r7 == 0) goto L_0x004e
            com.airbnb.lottie.parser.moshi.JsonReader$Options r7 = NAMES
            int r7 = r13.selectName(r7)
            switch(r7) {
                case 0: goto L_0x0048;
                case 1: goto L_0x0043;
                case 2: goto L_0x003e;
                case 3: goto L_0x0039;
                case 4: goto L_0x0034;
                case 5: goto L_0x002b;
                case 6: goto L_0x0026;
                case 7: goto L_0x0021;
                default: goto L_0x001d;
            }
        L_0x001d:
            r13.skipValue()
            goto L_0x000e
        L_0x0021:
            android.graphics.PointF r11 = com.airbnb.lottie.parser.JsonUtils.jsonToPoint(r13, r14)
            goto L_0x000e
        L_0x0026:
            android.graphics.PointF r10 = com.airbnb.lottie.parser.JsonUtils.jsonToPoint(r13, r14)
            goto L_0x000e
        L_0x002b:
            int r2 = r13.nextInt()
            r7 = 1
            if (r2 != r7) goto L_0x000d
            r2 = r7
            goto L_0x000e
        L_0x0034:
            android.graphics.PointF r4 = com.airbnb.lottie.parser.JsonUtils.jsonToPoint(r13, r14)
            goto L_0x000e
        L_0x0039:
            android.graphics.PointF r3 = com.airbnb.lottie.parser.JsonUtils.jsonToPoint(r13, r14)
            goto L_0x000e
        L_0x003e:
            java.lang.Object r6 = r15.parse(r13, r14)
            goto L_0x000e
        L_0x0043:
            java.lang.Object r5 = r15.parse(r13, r14)
            goto L_0x000e
        L_0x0048:
            double r7 = r13.nextDouble()
            float r8 = (float) r7
            goto L_0x000e
        L_0x004e:
            r13.endObject()
            if (r2 == 0) goto L_0x0058
            android.view.animation.Interpolator r13 = LINEAR_INTERPOLATOR
            r7 = r13
            r6 = r5
            goto L_0x00b9
        L_0x0058:
            if (r3 == 0) goto L_0x00b6
            if (r4 == 0) goto L_0x00b6
            float r13 = r3.x
            float r15 = -r14
            float r13 = com.airbnb.lottie.utils.MiscUtils.clamp((float) r13, (float) r15, (float) r14)
            r3.x = r13
            float r13 = r3.y
            r0 = -1027080192(0xffffffffc2c80000, float:-100.0)
            r2 = 1120403456(0x42c80000, float:100.0)
            float r13 = com.airbnb.lottie.utils.MiscUtils.clamp((float) r13, (float) r0, (float) r2)
            r3.y = r13
            float r13 = r4.x
            float r13 = com.airbnb.lottie.utils.MiscUtils.clamp((float) r13, (float) r15, (float) r14)
            r4.x = r13
            float r13 = r4.y
            float r13 = com.airbnb.lottie.utils.MiscUtils.clamp((float) r13, (float) r0, (float) r2)
            r4.y = r13
            float r15 = r3.x
            float r0 = r3.y
            float r2 = r4.x
            int r13 = com.airbnb.lottie.utils.Utils.hashFor(r15, r0, r2, r13)
            java.lang.ref.WeakReference r15 = getInterpolator(r13)
            if (r15 == 0) goto L_0x0098
            java.lang.Object r0 = r15.get()
            r1 = r0
            android.view.animation.Interpolator r1 = (android.view.animation.Interpolator) r1
        L_0x0098:
            if (r15 == 0) goto L_0x009c
            if (r1 != 0) goto L_0x00b4
        L_0x009c:
            float r15 = r3.x
            float r15 = r15 / r14
            float r0 = r3.y
            float r0 = r0 / r14
            float r1 = r4.x
            float r1 = r1 / r14
            float r2 = r4.y
            float r2 = r2 / r14
            android.view.animation.Interpolator r1 = androidx.core.view.animation.PathInterpolatorCompat.create(r15, r0, r1, r2)
            java.lang.ref.WeakReference r14 = new java.lang.ref.WeakReference     // Catch:{ ArrayIndexOutOfBoundsException -> 0x00b4 }
            r14.<init>(r1)     // Catch:{ ArrayIndexOutOfBoundsException -> 0x00b4 }
            putInterpolator(r13, r14)     // Catch:{ ArrayIndexOutOfBoundsException -> 0x00b4 }
        L_0x00b4:
            r7 = r1
            goto L_0x00b9
        L_0x00b6:
            android.view.animation.Interpolator r13 = LINEAR_INTERPOLATOR
            r7 = r13
        L_0x00b9:
            com.airbnb.lottie.value.Keyframe r13 = new com.airbnb.lottie.value.Keyframe
            r9 = 0
            r3 = r13
            r4 = r12
            r3.<init>(r4, r5, r6, r7, r8, r9)
            r13.pathCp1 = r10
            r13.pathCp2 = r11
            return r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.parser.KeyframeParser.parseKeyframe(com.airbnb.lottie.LottieComposition, com.airbnb.lottie.parser.moshi.JsonReader, float, com.airbnb.lottie.parser.ValueParser):com.airbnb.lottie.value.Keyframe");
    }

    private static <T> Keyframe<T> parseStaticValue(JsonReader jsonReader, float f, ValueParser<T> valueParser) throws IOException {
        return new Keyframe<>(valueParser.parse(jsonReader, f));
    }
}
