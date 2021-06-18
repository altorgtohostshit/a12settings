package com.google.zxing.pdf417.decoder;

import com.google.zxing.FormatException;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.pdf417.PDF417ResultMetadata;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

final class DecodedBitStreamParser {
    private static final BigInteger[] EXP900;
    private static final char[] MIXED_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '&', 13, 9, ',', ':', '#', '-', '.', '$', '/', '+', '%', '*', '=', '^'};
    private static final char[] PUNCT_CHARS = {';', '<', '>', '@', '[', '\\', '}', '_', '`', '~', '!', 13, 9, ',', ':', 10, '-', '.', '$', '/', '\"', '|', '*', '(', ')', '?', '{', '}', '\''};

    private enum Mode {
        ALPHA,
        LOWER,
        MIXED,
        PUNCT,
        ALPHA_SHIFT,
        PUNCT_SHIFT
    }

    static {
        BigInteger[] bigIntegerArr = new BigInteger[16];
        EXP900 = bigIntegerArr;
        bigIntegerArr[0] = BigInteger.ONE;
        BigInteger valueOf = BigInteger.valueOf(900);
        bigIntegerArr[1] = valueOf;
        int i = 2;
        while (true) {
            BigInteger[] bigIntegerArr2 = EXP900;
            if (i < bigIntegerArr2.length) {
                bigIntegerArr2[i] = bigIntegerArr2[i - 1].multiply(valueOf);
                i++;
            } else {
                return;
            }
        }
    }

    static DecoderResult decode(int[] iArr, String str) throws FormatException {
        int i;
        int i2 = 2;
        StringBuilder sb = new StringBuilder(iArr.length * 2);
        int i3 = iArr[1];
        PDF417ResultMetadata pDF417ResultMetadata = new PDF417ResultMetadata();
        while (i2 < iArr[0]) {
            if (i3 == 913) {
                i = byteCompaction(i3, iArr, i2, sb);
            } else if (i3 != 928) {
                switch (i3) {
                    case 900:
                        i = textCompaction(iArr, i2, sb);
                        break;
                    case 901:
                        i = byteCompaction(i3, iArr, i2, sb);
                        break;
                    case 902:
                        i = numericCompaction(iArr, i2, sb);
                        break;
                    default:
                        switch (i3) {
                            case 922:
                            case 923:
                                throw FormatException.getFormatInstance();
                            case 924:
                                i = byteCompaction(i3, iArr, i2, sb);
                                break;
                            default:
                                i = textCompaction(iArr, i2 - 1, sb);
                                break;
                        }
                }
            } else {
                i = decodeMacroBlock(iArr, i2, pDF417ResultMetadata);
            }
            if (i < iArr.length) {
                i2 = i + 1;
                i3 = iArr[i];
            } else {
                throw FormatException.getFormatInstance();
            }
        }
        if (sb.length() != 0) {
            DecoderResult decoderResult = new DecoderResult((byte[]) null, sb.toString(), (List<byte[]>) null, str);
            decoderResult.setOther(pDF417ResultMetadata);
            return decoderResult;
        }
        throw FormatException.getFormatInstance();
    }

    private static int decodeMacroBlock(int[] iArr, int i, PDF417ResultMetadata pDF417ResultMetadata) throws FormatException {
        if (i + 2 <= iArr[0]) {
            int[] iArr2 = new int[2];
            int i2 = 0;
            while (i2 < 2) {
                iArr2[i2] = iArr[i];
                i2++;
                i++;
            }
            pDF417ResultMetadata.setSegmentIndex(Integer.parseInt(decodeBase900toBase10(iArr2, 2)));
            StringBuilder sb = new StringBuilder();
            int textCompaction = textCompaction(iArr, i, sb);
            pDF417ResultMetadata.setFileId(sb.toString());
            if (iArr[textCompaction] == 923) {
                int i3 = textCompaction + 1;
                int[] iArr3 = new int[(iArr[0] - i3)];
                boolean z = false;
                int i4 = 0;
                while (i3 < iArr[0] && !z) {
                    int i5 = i3 + 1;
                    int i6 = iArr[i3];
                    if (i6 < 900) {
                        iArr3[i4] = i6;
                        i3 = i5;
                        i4++;
                    } else if (i6 == 922) {
                        pDF417ResultMetadata.setLastSegment(true);
                        z = true;
                        i3 = i5 + 1;
                    } else {
                        throw FormatException.getFormatInstance();
                    }
                }
                pDF417ResultMetadata.setOptionalData(Arrays.copyOf(iArr3, i4));
                return i3;
            } else if (iArr[textCompaction] != 922) {
                return textCompaction;
            } else {
                pDF417ResultMetadata.setLastSegment(true);
                return textCompaction + 1;
            }
        } else {
            throw FormatException.getFormatInstance();
        }
    }

    private static int textCompaction(int[] iArr, int i, StringBuilder sb) {
        int[] iArr2 = new int[((iArr[0] - i) << 1)];
        int[] iArr3 = new int[((iArr[0] - i) << 1)];
        boolean z = false;
        int i2 = 0;
        while (i < iArr[0] && !z) {
            int i3 = i + 1;
            int i4 = iArr[i];
            if (i4 < 900) {
                iArr2[i2] = i4 / 30;
                iArr2[i2 + 1] = i4 % 30;
                i2 += 2;
            } else if (i4 != 913) {
                if (i4 != 928) {
                    switch (i4) {
                        case 900:
                            iArr2[i2] = 900;
                            i2++;
                            break;
                        case 901:
                        case 902:
                            break;
                        default:
                            switch (i4) {
                                case 922:
                                case 923:
                                case 924:
                                    break;
                            }
                    }
                }
                i3--;
                z = true;
            } else {
                iArr2[i2] = 913;
                i = i3 + 1;
                iArr3[i2] = iArr[i3];
                i2++;
            }
            i = i3;
            continue;
        }
        decodeTextCompaction(iArr2, iArr3, i2, sb);
        return i;
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0049, code lost:
        r1 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0055, code lost:
        r10 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x00b6, code lost:
        r10 = 0;
        r15 = r3;
        r3 = r1;
        r1 = r15;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x00d7, code lost:
        r10 = (char) r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x00f9, code lost:
        if (r10 == 0) goto L_0x00fe;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x00fb, code lost:
        r0.append(r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x00fe, code lost:
        r5 = r5 + 1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void decodeTextCompaction(int[] r16, int[] r17, int r18, java.lang.StringBuilder r19) {
        /*
            r0 = r19
            com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.ALPHA
            r2 = 0
            r4 = r18
            r3 = r1
            r5 = r2
        L_0x0009:
            if (r5 >= r4) goto L_0x0102
            r6 = r16[r5]
            int[] r7 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.C19811.f140x45bba1d
            int r8 = r1.ordinal()
            r7 = r7[r8]
            r8 = 28
            r9 = 27
            r10 = 32
            r11 = 913(0x391, float:1.28E-42)
            r12 = 900(0x384, float:1.261E-42)
            r13 = 29
            r14 = 26
            switch(r7) {
                case 1: goto L_0x00d3;
                case 2: goto L_0x00aa;
                case 3: goto L_0x0075;
                case 4: goto L_0x0057;
                case 5: goto L_0x0044;
                case 6: goto L_0x0028;
                default: goto L_0x0026;
            }
        L_0x0026:
            goto L_0x00f8
        L_0x0028:
            if (r6 >= r13) goto L_0x002f
            char[] r1 = PUNCT_CHARS
            char r10 = r1[r6]
            goto L_0x0049
        L_0x002f:
            if (r6 != r13) goto L_0x0035
            com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.ALPHA
            goto L_0x00f8
        L_0x0035:
            if (r6 != r11) goto L_0x003e
            r1 = r17[r5]
            char r1 = (char) r1
            r0.append(r1)
            goto L_0x0055
        L_0x003e:
            if (r6 != r12) goto L_0x0055
            com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.ALPHA
            goto L_0x00f8
        L_0x0044:
            if (r6 >= r14) goto L_0x004c
            int r6 = r6 + 65
            char r10 = (char) r6
        L_0x0049:
            r1 = r3
            goto L_0x00f9
        L_0x004c:
            if (r6 != r14) goto L_0x004f
            goto L_0x0049
        L_0x004f:
            if (r6 != r12) goto L_0x0055
            com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.ALPHA
            goto L_0x00f8
        L_0x0055:
            r10 = r2
            goto L_0x0049
        L_0x0057:
            if (r6 >= r13) goto L_0x005f
            char[] r7 = PUNCT_CHARS
            char r10 = r7[r6]
            goto L_0x00f9
        L_0x005f:
            if (r6 != r13) goto L_0x0065
            com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.ALPHA
            goto L_0x00f8
        L_0x0065:
            if (r6 != r11) goto L_0x006f
            r6 = r17[r5]
            char r6 = (char) r6
            r0.append(r6)
            goto L_0x00f8
        L_0x006f:
            if (r6 != r12) goto L_0x00f8
            com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.ALPHA
            goto L_0x00f8
        L_0x0075:
            r7 = 25
            if (r6 >= r7) goto L_0x007f
            char[] r7 = MIXED_CHARS
            char r10 = r7[r6]
            goto L_0x00f9
        L_0x007f:
            if (r6 != r7) goto L_0x0085
            com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.PUNCT
            goto L_0x00f8
        L_0x0085:
            if (r6 != r14) goto L_0x0089
            goto L_0x00f9
        L_0x0089:
            if (r6 != r9) goto L_0x008f
            com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.LOWER
            goto L_0x00f8
        L_0x008f:
            if (r6 != r8) goto L_0x0095
            com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.ALPHA
            goto L_0x00f8
        L_0x0095:
            if (r6 != r13) goto L_0x009a
            com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r3 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.PUNCT_SHIFT
            goto L_0x00b6
        L_0x009a:
            if (r6 != r11) goto L_0x00a4
            r6 = r17[r5]
            char r6 = (char) r6
            r0.append(r6)
            goto L_0x00f8
        L_0x00a4:
            if (r6 != r12) goto L_0x00f8
            com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.ALPHA
            goto L_0x00f8
        L_0x00aa:
            if (r6 >= r14) goto L_0x00af
            int r6 = r6 + 97
            goto L_0x00d7
        L_0x00af:
            if (r6 != r14) goto L_0x00b2
            goto L_0x00f9
        L_0x00b2:
            if (r6 != r9) goto L_0x00bb
            com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r3 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.ALPHA_SHIFT
        L_0x00b6:
            r10 = r2
            r15 = r3
            r3 = r1
            r1 = r15
            goto L_0x00f9
        L_0x00bb:
            if (r6 != r8) goto L_0x00c0
            com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.MIXED
            goto L_0x00f8
        L_0x00c0:
            if (r6 != r13) goto L_0x00c5
            com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r3 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.PUNCT_SHIFT
            goto L_0x00b6
        L_0x00c5:
            if (r6 != r11) goto L_0x00ce
            r6 = r17[r5]
            char r6 = (char) r6
            r0.append(r6)
            goto L_0x00f8
        L_0x00ce:
            if (r6 != r12) goto L_0x00f8
            com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.ALPHA
            goto L_0x00f8
        L_0x00d3:
            if (r6 >= r14) goto L_0x00d9
            int r6 = r6 + 65
        L_0x00d7:
            char r10 = (char) r6
            goto L_0x00f9
        L_0x00d9:
            if (r6 != r14) goto L_0x00dc
            goto L_0x00f9
        L_0x00dc:
            if (r6 != r9) goto L_0x00e1
            com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.LOWER
            goto L_0x00f8
        L_0x00e1:
            if (r6 != r8) goto L_0x00e6
            com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.MIXED
            goto L_0x00f8
        L_0x00e6:
            if (r6 != r13) goto L_0x00eb
            com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r3 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.PUNCT_SHIFT
            goto L_0x00b6
        L_0x00eb:
            if (r6 != r11) goto L_0x00f4
            r6 = r17[r5]
            char r6 = (char) r6
            r0.append(r6)
            goto L_0x00f8
        L_0x00f4:
            if (r6 != r12) goto L_0x00f8
            com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.ALPHA
        L_0x00f8:
            r10 = r2
        L_0x00f9:
            if (r10 == 0) goto L_0x00fe
            r0.append(r10)
        L_0x00fe:
            int r5 = r5 + 1
            goto L_0x0009
        L_0x0102:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.pdf417.decoder.DecodedBitStreamParser.decodeTextCompaction(int[], int[], int, java.lang.StringBuilder):void");
    }

    /* renamed from: com.google.zxing.pdf417.decoder.DecodedBitStreamParser$1 */
    static /* synthetic */ class C19811 {

        /* renamed from: $SwitchMap$com$google$zxing$pdf417$decoder$DecodedBitStreamParser$Mode */
        static final /* synthetic */ int[] f140x45bba1d;

        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|1|2|3|4|5|6|7|8|9|10|11|12|14) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode[] r0 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f140x45bba1d = r0
                com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.ALPHA     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = f140x45bba1d     // Catch:{ NoSuchFieldError -> 0x001d }
                com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.LOWER     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = f140x45bba1d     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.MIXED     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = f140x45bba1d     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.PUNCT     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = f140x45bba1d     // Catch:{ NoSuchFieldError -> 0x003e }
                com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.ALPHA_SHIFT     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = f140x45bba1d     // Catch:{ NoSuchFieldError -> 0x0049 }
                com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.PUNCT_SHIFT     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.pdf417.decoder.DecodedBitStreamParser.C19811.<clinit>():void");
        }
    }

    private static int byteCompaction(int i, int[] iArr, int i2, StringBuilder sb) {
        int i3;
        int i4 = i;
        StringBuilder sb2 = sb;
        int i5 = 922;
        int i6 = 923;
        long j = 900;
        int i7 = 6;
        if (i4 == 901) {
            char[] cArr = new char[6];
            int[] iArr2 = new int[6];
            int i8 = i2 + 1;
            boolean z = false;
            int i9 = 0;
            int i10 = iArr[i2];
            long j2 = 0;
            while (i8 < iArr[0] && !z) {
                int i11 = i9 + 1;
                iArr2[i9] = i10;
                j2 = (j2 * j) + ((long) i10);
                int i12 = i8 + 1;
                i10 = iArr[i8];
                if (i10 == 900 || i10 == 901 || i10 == 902 || i10 == 924 || i10 == 928 || i10 == 923 || i10 == 922) {
                    i8 = i12 - 1;
                    i10 = i10;
                    i9 = i11;
                    j = 900;
                    i7 = 6;
                    z = true;
                } else {
                    if (i11 % 5 != 0 || i11 <= 0) {
                        i10 = i10;
                        i9 = i11;
                        i8 = i12;
                    } else {
                        int i13 = 0;
                        while (i13 < i7) {
                            cArr[5 - i13] = (char) ((int) (j2 % 256));
                            j2 >>= 8;
                            i13++;
                            i10 = i10;
                            i7 = 6;
                        }
                        int i14 = i10;
                        sb2.append(cArr);
                        i8 = i12;
                        i9 = 0;
                    }
                    j = 900;
                    i7 = 6;
                }
            }
            if (i8 != iArr[0] || i10 >= 900) {
                i3 = i9;
            } else {
                i3 = i9 + 1;
                iArr2[i9] = i10;
            }
            for (int i15 = 0; i15 < i3; i15++) {
                sb2.append((char) iArr2[i15]);
            }
            return i8;
        } else if (i4 != 924) {
            return i2;
        } else {
            int i16 = i2;
            boolean z2 = false;
            int i17 = 0;
            long j3 = 0;
            while (i16 < iArr[0] && !z2) {
                int i18 = i16 + 1;
                int i19 = iArr[i16];
                if (i19 < 900) {
                    i17++;
                    j3 = (j3 * 900) + ((long) i19);
                } else if (i19 == 900 || i19 == 901 || i19 == 902 || i19 == 924 || i19 == 928 || i19 == i6 || i19 == i5) {
                    i16 = i18 - 1;
                    z2 = true;
                    if (i17 % 5 != 0 && i17 > 0) {
                        char[] cArr2 = new char[6];
                        for (int i20 = 0; i20 < 6; i20++) {
                            cArr2[5 - i20] = (char) ((int) (j3 & 255));
                            j3 >>= 8;
                        }
                        sb2.append(cArr2);
                        i17 = 0;
                    }
                    i5 = 922;
                    i6 = 923;
                }
                i16 = i18;
                if (i17 % 5 != 0) {
                }
                i5 = 922;
                i6 = 923;
            }
            return i16;
        }
    }

    private static int numericCompaction(int[] iArr, int i, StringBuilder sb) throws FormatException {
        int[] iArr2 = new int[15];
        boolean z = false;
        int i2 = 0;
        while (i < iArr[0] && !z) {
            int i3 = i + 1;
            int i4 = iArr[i];
            if (i3 == iArr[0]) {
                z = true;
            }
            if (i4 < 900) {
                iArr2[i2] = i4;
                i2++;
            } else if (i4 == 900 || i4 == 901 || i4 == 924 || i4 == 928 || i4 == 923 || i4 == 922) {
                i3--;
                z = true;
            }
            if (i2 % 15 == 0 || i4 == 902 || z) {
                sb.append(decodeBase900toBase10(iArr2, i2));
                i2 = 0;
            }
            i = i3;
        }
        return i;
    }

    private static String decodeBase900toBase10(int[] iArr, int i) throws FormatException {
        BigInteger bigInteger = BigInteger.ZERO;
        for (int i2 = 0; i2 < i; i2++) {
            bigInteger = bigInteger.add(EXP900[(i - i2) - 1].multiply(BigInteger.valueOf((long) iArr[i2])));
        }
        String bigInteger2 = bigInteger.toString();
        if (bigInteger2.charAt(0) == '1') {
            return bigInteger2.substring(1);
        }
        throw FormatException.getFormatInstance();
    }
}
