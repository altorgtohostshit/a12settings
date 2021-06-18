package okio.internal;

import org.jetbrains.annotations.NotNull;

/* compiled from: ByteString.kt */
public final class ByteStringKt {
    @NotNull
    private static final char[] HEX_DIGIT_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    @NotNull
    public static final char[] getHEX_DIGIT_CHARS() {
        return HEX_DIGIT_CHARS;
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:251:0x0220 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:257:0x0047 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:261:0x0173 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:263:0x0083 A[EDGE_INSN: B:263:0x0083->B:51:0x0083 ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:275:0x00df A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final int codePointIndexToCharIndex(byte[] r19, int r20) {
        /*
            r0 = r19
            r1 = r20
            int r2 = r0.length
            r4 = 0
            r5 = 0
            r6 = 0
        L_0x0008:
            if (r4 >= r2) goto L_0x0234
            byte r7 = r0[r4]
            r8 = 159(0x9f, float:2.23E-43)
            r9 = 127(0x7f, float:1.78E-43)
            r10 = 31
            r11 = 13
            r12 = 65533(0xfffd, float:9.1831E-41)
            r13 = 10
            r14 = 65536(0x10000, float:9.18355E-41)
            r16 = -1
            r17 = 1
            if (r7 < 0) goto L_0x008d
            int r18 = r6 + 1
            if (r6 != r1) goto L_0x0026
            return r5
        L_0x0026:
            if (r7 == r13) goto L_0x0045
            if (r7 == r11) goto L_0x0045
            if (r7 < 0) goto L_0x0031
            if (r7 > r10) goto L_0x0031
            r6 = r17
            goto L_0x0032
        L_0x0031:
            r6 = 0
        L_0x0032:
            if (r6 != 0) goto L_0x0041
            if (r9 > r7) goto L_0x003b
            if (r7 > r8) goto L_0x003b
            r6 = r17
            goto L_0x003c
        L_0x003b:
            r6 = 0
        L_0x003c:
            if (r6 == 0) goto L_0x003f
            goto L_0x0041
        L_0x003f:
            r6 = 0
            goto L_0x0043
        L_0x0041:
            r6 = r17
        L_0x0043:
            if (r6 != 0) goto L_0x0047
        L_0x0045:
            if (r7 != r12) goto L_0x0048
        L_0x0047:
            return r16
        L_0x0048:
            if (r7 >= r14) goto L_0x004d
            r6 = r17
            goto L_0x004e
        L_0x004d:
            r6 = 2
        L_0x004e:
            int r5 = r5 + r6
            int r4 = r4 + 1
        L_0x0051:
            r6 = r18
            if (r4 >= r2) goto L_0x0008
            byte r7 = r0[r4]
            if (r7 < 0) goto L_0x0008
            int r7 = r4 + 1
            byte r4 = r0[r4]
            int r18 = r6 + 1
            if (r6 != r1) goto L_0x0062
            return r5
        L_0x0062:
            if (r4 == r13) goto L_0x0081
            if (r4 == r11) goto L_0x0081
            if (r4 < 0) goto L_0x006d
            if (r4 > r10) goto L_0x006d
            r6 = r17
            goto L_0x006e
        L_0x006d:
            r6 = 0
        L_0x006e:
            if (r6 != 0) goto L_0x007d
            if (r9 > r4) goto L_0x0077
            if (r4 > r8) goto L_0x0077
            r6 = r17
            goto L_0x0078
        L_0x0077:
            r6 = 0
        L_0x0078:
            if (r6 == 0) goto L_0x007b
            goto L_0x007d
        L_0x007b:
            r6 = 0
            goto L_0x007f
        L_0x007d:
            r6 = r17
        L_0x007f:
            if (r6 != 0) goto L_0x0083
        L_0x0081:
            if (r4 != r12) goto L_0x0084
        L_0x0083:
            return r16
        L_0x0084:
            if (r4 >= r14) goto L_0x0089
            r4 = r17
            goto L_0x008a
        L_0x0089:
            r4 = 2
        L_0x008a:
            int r5 = r5 + r4
            r4 = r7
            goto L_0x0051
        L_0x008d:
            int r3 = r7 >> 5
            r15 = -2
            r14 = 128(0x80, float:1.794E-43)
            if (r3 != r15) goto L_0x00f0
            int r3 = r4 + 1
            if (r2 > r3) goto L_0x009c
            if (r6 != r1) goto L_0x009b
            return r5
        L_0x009b:
            return r16
        L_0x009c:
            byte r7 = r0[r4]
            byte r3 = r0[r3]
            r15 = r3 & 192(0xc0, float:2.69E-43)
            if (r15 != r14) goto L_0x00a7
            r15 = r17
            goto L_0x00a8
        L_0x00a7:
            r15 = 0
        L_0x00a8:
            if (r15 != 0) goto L_0x00ae
            if (r6 != r1) goto L_0x00ad
            return r5
        L_0x00ad:
            return r16
        L_0x00ae:
            r3 = r3 ^ 3968(0xf80, float:5.56E-42)
            int r7 = r7 << 6
            r3 = r3 ^ r7
            if (r3 >= r14) goto L_0x00b9
            if (r6 != r1) goto L_0x00b8
            return r5
        L_0x00b8:
            return r16
        L_0x00b9:
            int r7 = r6 + 1
            if (r6 != r1) goto L_0x00be
            return r5
        L_0x00be:
            if (r3 == r13) goto L_0x00dd
            if (r3 == r11) goto L_0x00dd
            if (r3 < 0) goto L_0x00c9
            if (r3 > r10) goto L_0x00c9
            r6 = r17
            goto L_0x00ca
        L_0x00c9:
            r6 = 0
        L_0x00ca:
            if (r6 != 0) goto L_0x00d9
            if (r9 > r3) goto L_0x00d3
            if (r3 > r8) goto L_0x00d3
            r6 = r17
            goto L_0x00d4
        L_0x00d3:
            r6 = 0
        L_0x00d4:
            if (r6 == 0) goto L_0x00d7
            goto L_0x00d9
        L_0x00d7:
            r6 = 0
            goto L_0x00db
        L_0x00d9:
            r6 = r17
        L_0x00db:
            if (r6 != 0) goto L_0x00df
        L_0x00dd:
            if (r3 != r12) goto L_0x00e0
        L_0x00df:
            return r16
        L_0x00e0:
            r6 = 65536(0x10000, float:9.18355E-41)
            if (r3 >= r6) goto L_0x00e7
            r15 = r17
            goto L_0x00e8
        L_0x00e7:
            r15 = 2
        L_0x00e8:
            int r5 = r5 + r15
            kotlin.Unit r3 = kotlin.Unit.INSTANCE
            int r4 = r4 + 2
        L_0x00ed:
            r6 = r7
            goto L_0x0008
        L_0x00f0:
            int r3 = r7 >> 4
            r12 = 57343(0xdfff, float:8.0355E-41)
            r8 = 55296(0xd800, float:7.7486E-41)
            if (r3 != r15) goto L_0x0183
            int r3 = r4 + 2
            if (r2 > r3) goto L_0x0102
            if (r6 != r1) goto L_0x0101
            return r5
        L_0x0101:
            return r16
        L_0x0102:
            byte r7 = r0[r4]
            int r15 = r4 + 1
            byte r15 = r0[r15]
            r9 = r15 & 192(0xc0, float:2.69E-43)
            if (r9 != r14) goto L_0x010f
            r9 = r17
            goto L_0x0110
        L_0x010f:
            r9 = 0
        L_0x0110:
            if (r9 != 0) goto L_0x0116
            if (r6 != r1) goto L_0x0115
            return r5
        L_0x0115:
            return r16
        L_0x0116:
            byte r3 = r0[r3]
            r9 = r3 & 192(0xc0, float:2.69E-43)
            if (r9 != r14) goto L_0x011f
            r9 = r17
            goto L_0x0120
        L_0x011f:
            r9 = 0
        L_0x0120:
            if (r9 != 0) goto L_0x0126
            if (r6 != r1) goto L_0x0125
            return r5
        L_0x0125:
            return r16
        L_0x0126:
            r9 = -123008(0xfffffffffffe1f80, float:NaN)
            r3 = r3 ^ r9
            int r9 = r15 << 6
            r3 = r3 ^ r9
            int r7 = r7 << 12
            r3 = r3 ^ r7
            r7 = 2048(0x800, float:2.87E-42)
            if (r3 >= r7) goto L_0x0138
            if (r6 != r1) goto L_0x0137
            return r5
        L_0x0137:
            return r16
        L_0x0138:
            if (r8 > r3) goto L_0x013f
            if (r3 > r12) goto L_0x013f
            r7 = r17
            goto L_0x0140
        L_0x013f:
            r7 = 0
        L_0x0140:
            if (r7 == 0) goto L_0x0146
            if (r6 != r1) goto L_0x0145
            return r5
        L_0x0145:
            return r16
        L_0x0146:
            int r7 = r6 + 1
            if (r6 != r1) goto L_0x014b
            return r5
        L_0x014b:
            if (r3 == r13) goto L_0x016e
            if (r3 == r11) goto L_0x016e
            if (r3 < 0) goto L_0x0156
            if (r3 > r10) goto L_0x0156
            r6 = r17
            goto L_0x0157
        L_0x0156:
            r6 = 0
        L_0x0157:
            if (r6 != 0) goto L_0x016a
            r6 = 127(0x7f, float:1.78E-43)
            if (r6 > r3) goto L_0x0164
            r6 = 159(0x9f, float:2.23E-43)
            if (r3 > r6) goto L_0x0164
            r6 = r17
            goto L_0x0165
        L_0x0164:
            r6 = 0
        L_0x0165:
            if (r6 == 0) goto L_0x0168
            goto L_0x016a
        L_0x0168:
            r6 = 0
            goto L_0x016c
        L_0x016a:
            r6 = r17
        L_0x016c:
            if (r6 != 0) goto L_0x0173
        L_0x016e:
            r6 = 65533(0xfffd, float:9.1831E-41)
            if (r3 != r6) goto L_0x0174
        L_0x0173:
            return r16
        L_0x0174:
            r6 = 65536(0x10000, float:9.18355E-41)
            if (r3 >= r6) goto L_0x017b
            r15 = r17
            goto L_0x017c
        L_0x017b:
            r15 = 2
        L_0x017c:
            int r5 = r5 + r15
            kotlin.Unit r3 = kotlin.Unit.INSTANCE
            int r4 = r4 + 3
            goto L_0x00ed
        L_0x0183:
            int r3 = r7 >> 3
            if (r3 != r15) goto L_0x0230
            int r3 = r4 + 3
            if (r2 > r3) goto L_0x018f
            if (r6 != r1) goto L_0x018e
            return r5
        L_0x018e:
            return r16
        L_0x018f:
            byte r7 = r0[r4]
            int r9 = r4 + 1
            byte r9 = r0[r9]
            r15 = r9 & 192(0xc0, float:2.69E-43)
            if (r15 != r14) goto L_0x019c
            r15 = r17
            goto L_0x019d
        L_0x019c:
            r15 = 0
        L_0x019d:
            if (r15 != 0) goto L_0x01a3
            if (r6 != r1) goto L_0x01a2
            return r5
        L_0x01a2:
            return r16
        L_0x01a3:
            int r15 = r4 + 2
            byte r15 = r0[r15]
            r10 = r15 & 192(0xc0, float:2.69E-43)
            if (r10 != r14) goto L_0x01ae
            r10 = r17
            goto L_0x01af
        L_0x01ae:
            r10 = 0
        L_0x01af:
            if (r10 != 0) goto L_0x01b5
            if (r6 != r1) goto L_0x01b4
            return r5
        L_0x01b4:
            return r16
        L_0x01b5:
            byte r3 = r0[r3]
            r10 = r3 & 192(0xc0, float:2.69E-43)
            if (r10 != r14) goto L_0x01be
            r10 = r17
            goto L_0x01bf
        L_0x01be:
            r10 = 0
        L_0x01bf:
            if (r10 != 0) goto L_0x01c5
            if (r6 != r1) goto L_0x01c4
            return r5
        L_0x01c4:
            return r16
        L_0x01c5:
            r10 = 3678080(0x381f80, float:5.154088E-39)
            r3 = r3 ^ r10
            int r10 = r15 << 6
            r3 = r3 ^ r10
            int r9 = r9 << 12
            r3 = r3 ^ r9
            int r7 = r7 << 18
            r3 = r3 ^ r7
            r7 = 1114111(0x10ffff, float:1.561202E-39)
            if (r3 <= r7) goto L_0x01db
            if (r6 != r1) goto L_0x01da
            return r5
        L_0x01da:
            return r16
        L_0x01db:
            if (r8 > r3) goto L_0x01e2
            if (r3 > r12) goto L_0x01e2
            r7 = r17
            goto L_0x01e3
        L_0x01e2:
            r7 = 0
        L_0x01e3:
            if (r7 == 0) goto L_0x01e9
            if (r6 != r1) goto L_0x01e8
            return r5
        L_0x01e8:
            return r16
        L_0x01e9:
            r7 = 65536(0x10000, float:9.18355E-41)
            if (r3 >= r7) goto L_0x01f1
            if (r6 != r1) goto L_0x01f0
            return r5
        L_0x01f0:
            return r16
        L_0x01f1:
            int r7 = r6 + 1
            if (r6 != r1) goto L_0x01f6
            return r5
        L_0x01f6:
            if (r3 == r13) goto L_0x021b
            if (r3 == r11) goto L_0x021b
            if (r3 < 0) goto L_0x0203
            r6 = 31
            if (r3 > r6) goto L_0x0203
            r6 = r17
            goto L_0x0204
        L_0x0203:
            r6 = 0
        L_0x0204:
            if (r6 != 0) goto L_0x0217
            r6 = 127(0x7f, float:1.78E-43)
            if (r6 > r3) goto L_0x0211
            r6 = 159(0x9f, float:2.23E-43)
            if (r3 > r6) goto L_0x0211
            r6 = r17
            goto L_0x0212
        L_0x0211:
            r6 = 0
        L_0x0212:
            if (r6 == 0) goto L_0x0215
            goto L_0x0217
        L_0x0215:
            r6 = 0
            goto L_0x0219
        L_0x0217:
            r6 = r17
        L_0x0219:
            if (r6 != 0) goto L_0x0220
        L_0x021b:
            r6 = 65533(0xfffd, float:9.1831E-41)
            if (r3 != r6) goto L_0x0221
        L_0x0220:
            return r16
        L_0x0221:
            r6 = 65536(0x10000, float:9.18355E-41)
            if (r3 >= r6) goto L_0x0228
            r15 = r17
            goto L_0x0229
        L_0x0228:
            r15 = 2
        L_0x0229:
            int r5 = r5 + r15
            kotlin.Unit r3 = kotlin.Unit.INSTANCE
            int r4 = r4 + 4
            goto L_0x00ed
        L_0x0230:
            if (r6 != r1) goto L_0x0233
            return r5
        L_0x0233:
            return r16
        L_0x0234:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.internal.ByteStringKt.codePointIndexToCharIndex(byte[], int):int");
    }
}
