package com.google.zxing.oned;

import androidx.constraintlayout.widget.R$styleable;
import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitArray;

public final class Code128Reader extends OneDReader {
    static final int[][] CODE_PATTERNS;

    static {
        int[][] iArr = new int[R$styleable.Constraint_progress][];
        iArr[0] = new int[]{2, 1, 2, 2, 2, 2};
        iArr[1] = new int[]{2, 2, 2, 1, 2, 2};
        iArr[2] = new int[]{2, 2, 2, 2, 2, 1};
        iArr[3] = new int[]{1, 2, 1, 2, 2, 3};
        iArr[4] = new int[]{1, 2, 1, 3, 2, 2};
        iArr[5] = new int[]{1, 3, 1, 2, 2, 2};
        iArr[6] = new int[]{1, 2, 2, 2, 1, 3};
        iArr[7] = new int[]{1, 2, 2, 3, 1, 2};
        iArr[8] = new int[]{1, 3, 2, 2, 1, 2};
        iArr[9] = new int[]{2, 2, 1, 2, 1, 3};
        iArr[10] = new int[]{2, 2, 1, 3, 1, 2};
        iArr[11] = new int[]{2, 3, 1, 2, 1, 2};
        iArr[12] = new int[]{1, 1, 2, 2, 3, 2};
        iArr[13] = new int[]{1, 2, 2, 1, 3, 2};
        iArr[14] = new int[]{1, 2, 2, 2, 3, 1};
        iArr[15] = new int[]{1, 1, 3, 2, 2, 2};
        iArr[16] = new int[]{1, 2, 3, 1, 2, 2};
        iArr[17] = new int[]{1, 2, 3, 2, 2, 1};
        iArr[18] = new int[]{2, 2, 3, 2, 1, 1};
        iArr[19] = new int[]{2, 2, 1, 1, 3, 2};
        iArr[20] = new int[]{2, 2, 1, 2, 3, 1};
        iArr[21] = new int[]{2, 1, 3, 2, 1, 2};
        iArr[22] = new int[]{2, 2, 3, 1, 1, 2};
        iArr[23] = new int[]{3, 1, 2, 1, 3, 1};
        iArr[24] = new int[]{3, 1, 1, 2, 2, 2};
        iArr[25] = new int[]{3, 2, 1, 1, 2, 2};
        iArr[26] = new int[]{3, 2, 1, 2, 2, 1};
        iArr[27] = new int[]{3, 1, 2, 2, 1, 2};
        iArr[28] = new int[]{3, 2, 2, 1, 1, 2};
        iArr[29] = new int[]{3, 2, 2, 2, 1, 1};
        iArr[30] = new int[]{2, 1, 2, 1, 2, 3};
        iArr[31] = new int[]{2, 1, 2, 3, 2, 1};
        iArr[32] = new int[]{2, 3, 2, 1, 2, 1};
        iArr[33] = new int[]{1, 1, 1, 3, 2, 3};
        iArr[34] = new int[]{1, 3, 1, 1, 2, 3};
        iArr[35] = new int[]{1, 3, 1, 3, 2, 1};
        iArr[36] = new int[]{1, 1, 2, 3, 1, 3};
        iArr[37] = new int[]{1, 3, 2, 1, 1, 3};
        iArr[38] = new int[]{1, 3, 2, 3, 1, 1};
        iArr[39] = new int[]{2, 1, 1, 3, 1, 3};
        iArr[40] = new int[]{2, 3, 1, 1, 1, 3};
        iArr[41] = new int[]{2, 3, 1, 3, 1, 1};
        iArr[42] = new int[]{1, 1, 2, 1, 3, 3};
        iArr[43] = new int[]{1, 1, 2, 3, 3, 1};
        iArr[44] = new int[]{1, 3, 2, 1, 3, 1};
        iArr[45] = new int[]{1, 1, 3, 1, 2, 3};
        iArr[46] = new int[]{1, 1, 3, 3, 2, 1};
        iArr[47] = new int[]{1, 3, 3, 1, 2, 1};
        iArr[48] = new int[]{3, 1, 3, 1, 2, 1};
        iArr[49] = new int[]{2, 1, 1, 3, 3, 1};
        iArr[50] = new int[]{2, 3, 1, 1, 3, 1};
        iArr[51] = new int[]{2, 1, 3, 1, 1, 3};
        iArr[52] = new int[]{2, 1, 3, 3, 1, 1};
        iArr[53] = new int[]{2, 1, 3, 1, 3, 1};
        iArr[54] = new int[]{3, 1, 1, 1, 2, 3};
        iArr[55] = new int[]{3, 1, 1, 3, 2, 1};
        iArr[56] = new int[]{3, 3, 1, 1, 2, 1};
        iArr[57] = new int[]{3, 1, 2, 1, 1, 3};
        iArr[58] = new int[]{3, 1, 2, 3, 1, 1};
        iArr[59] = new int[]{3, 3, 2, 1, 1, 1};
        iArr[60] = new int[]{3, 1, 4, 1, 1, 1};
        iArr[61] = new int[]{2, 2, 1, 4, 1, 1};
        iArr[62] = new int[]{4, 3, 1, 1, 1, 1};
        iArr[63] = new int[]{1, 1, 1, 2, 2, 4};
        iArr[64] = new int[]{1, 1, 1, 4, 2, 2};
        iArr[65] = new int[]{1, 2, 1, 1, 2, 4};
        iArr[66] = new int[]{1, 2, 1, 4, 2, 1};
        iArr[67] = new int[]{1, 4, 1, 1, 2, 2};
        iArr[68] = new int[]{1, 4, 1, 2, 2, 1};
        iArr[69] = new int[]{1, 1, 2, 2, 1, 4};
        iArr[70] = new int[]{1, 1, 2, 4, 1, 2};
        iArr[71] = new int[]{1, 2, 2, 1, 1, 4};
        iArr[72] = new int[]{1, 2, 2, 4, 1, 1};
        iArr[73] = new int[]{1, 4, 2, 1, 1, 2};
        iArr[74] = new int[]{1, 4, 2, 2, 1, 1};
        iArr[75] = new int[]{2, 4, 1, 2, 1, 1};
        iArr[76] = new int[]{2, 2, 1, 1, 1, 4};
        iArr[77] = new int[]{4, 1, 3, 1, 1, 1};
        iArr[78] = new int[]{2, 4, 1, 1, 1, 2};
        iArr[79] = new int[]{1, 3, 4, 1, 1, 1};
        iArr[80] = new int[]{1, 1, 1, 2, 4, 2};
        iArr[81] = new int[]{1, 2, 1, 1, 4, 2};
        iArr[82] = new int[]{1, 2, 1, 2, 4, 1};
        iArr[83] = new int[]{1, 1, 4, 2, 1, 2};
        iArr[84] = new int[]{1, 2, 4, 1, 1, 2};
        iArr[85] = new int[]{1, 2, 4, 2, 1, 1};
        iArr[86] = new int[]{4, 1, 1, 2, 1, 2};
        iArr[87] = new int[]{4, 2, 1, 1, 1, 2};
        iArr[88] = new int[]{4, 2, 1, 2, 1, 1};
        iArr[89] = new int[]{2, 1, 2, 1, 4, 1};
        iArr[90] = new int[]{2, 1, 4, 1, 2, 1};
        iArr[91] = new int[]{4, 1, 2, 1, 2, 1};
        iArr[92] = new int[]{1, 1, 1, 1, 4, 3};
        iArr[93] = new int[]{1, 1, 1, 3, 4, 1};
        iArr[94] = new int[]{1, 3, 1, 1, 4, 1};
        iArr[95] = new int[]{1, 1, 4, 1, 1, 3};
        iArr[96] = new int[]{1, 1, 4, 3, 1, 1};
        iArr[97] = new int[]{4, 1, 1, 1, 1, 3};
        iArr[98] = new int[]{4, 1, 1, 3, 1, 1};
        iArr[99] = new int[]{1, 1, 3, 1, 4, 1};
        iArr[100] = new int[]{1, 1, 4, 1, 3, 1};
        iArr[101] = new int[]{3, 1, 1, 1, 4, 1};
        iArr[102] = new int[]{4, 1, 1, 1, 3, 1};
        iArr[103] = new int[]{2, 1, 1, 4, 1, 2};
        iArr[104] = new int[]{2, 1, 1, 2, 1, 4};
        iArr[105] = new int[]{2, 1, 1, 2, 3, 2};
        iArr[106] = new int[]{2, 3, 3, 1, 1, 1, 2};
        CODE_PATTERNS = iArr;
    }

    private static int[] findStartPattern(BitArray bitArray) throws NotFoundException {
        int size = bitArray.getSize();
        int nextSet = bitArray.getNextSet(0);
        int[] iArr = new int[6];
        boolean z = false;
        int i = 0;
        int i2 = nextSet;
        while (nextSet < size) {
            if (bitArray.get(nextSet) ^ z) {
                iArr[i] = iArr[i] + 1;
            } else {
                if (i == 5) {
                    int i3 = 64;
                    int i4 = -1;
                    for (int i5 = R$styleable.Constraint_layout_goneMarginTop; i5 <= 105; i5++) {
                        int patternMatchVariance = OneDReader.patternMatchVariance(iArr, CODE_PATTERNS[i5], 179);
                        if (patternMatchVariance < i3) {
                            i4 = i5;
                            i3 = patternMatchVariance;
                        }
                    }
                    if (i4 < 0 || !bitArray.isRange(Math.max(0, i2 - ((nextSet - i2) / 2)), i2, false)) {
                        i2 += iArr[0] + iArr[1];
                        System.arraycopy(iArr, 2, iArr, 0, 4);
                        iArr[4] = 0;
                        iArr[5] = 0;
                        i--;
                    } else {
                        return new int[]{i2, nextSet, i4};
                    }
                } else {
                    i++;
                }
                iArr[i] = 1;
                z = !z;
            }
            nextSet++;
        }
        throw NotFoundException.getNotFoundInstance();
    }

    private static int decodeCode(BitArray bitArray, int[] iArr, int i) throws NotFoundException {
        OneDReader.recordPattern(bitArray, i, iArr);
        int i2 = 64;
        int i3 = -1;
        int i4 = 0;
        while (true) {
            int[][] iArr2 = CODE_PATTERNS;
            if (i4 >= iArr2.length) {
                break;
            }
            int patternMatchVariance = OneDReader.patternMatchVariance(iArr, iArr2[i4], 179);
            if (patternMatchVariance < i2) {
                i3 = i4;
                i2 = patternMatchVariance;
            }
            i4++;
        }
        if (i3 >= 0) {
            return i3;
        }
        throw NotFoundException.getNotFoundInstance();
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x007e, code lost:
        r9 = 'd';
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x00ab, code lost:
        r14 = 'd';
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:0x00f4, code lost:
        r7 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x00f8, code lost:
        r7 = false;
        r14 = 'c';
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x00ff, code lost:
        r9 = 'd';
     */
    /* JADX WARNING: Code restructure failed: missing block: B:92:0x0135, code lost:
        r7 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:94:0x0138, code lost:
        if (r17 == false) goto L_0x013f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:95:0x013a, code lost:
        if (r14 != 'e') goto L_0x013e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:96:0x013c, code lost:
        r14 = r9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:97:0x013e, code lost:
        r14 = 'e';
     */
    /* JADX WARNING: Code restructure failed: missing block: B:98:0x013f, code lost:
        r17 = r7;
        r20 = r18;
        r15 = 6;
        r18 = r5;
        r21 = r16;
        r16 = r3;
        r3 = r21;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.google.zxing.Result decodeRow(int r23, com.google.zxing.common.BitArray r24, java.util.Map<com.google.zxing.DecodeHintType, ?> r25) throws com.google.zxing.NotFoundException, com.google.zxing.FormatException, com.google.zxing.ChecksumException {
        /*
            r22 = this;
            r0 = r24
            r1 = r25
            r2 = 1
            r3 = 0
            if (r1 == 0) goto L_0x0012
            com.google.zxing.DecodeHintType r4 = com.google.zxing.DecodeHintType.ASSUME_GS1
            boolean r1 = r1.containsKey(r4)
            if (r1 == 0) goto L_0x0012
            r1 = r2
            goto L_0x0013
        L_0x0012:
            r1 = r3
        L_0x0013:
            int[] r4 = findStartPattern(r24)
            r5 = 2
            r6 = r4[r5]
            switch(r6) {
                case 103: goto L_0x0028;
                case 104: goto L_0x0025;
                case 105: goto L_0x0022;
                default: goto L_0x001d;
            }
        L_0x001d:
            com.google.zxing.FormatException r0 = com.google.zxing.FormatException.getFormatInstance()
            throw r0
        L_0x0022:
            r10 = 99
            goto L_0x002a
        L_0x0025:
            r10 = 100
            goto L_0x002a
        L_0x0028:
            r10 = 101(0x65, float:1.42E-43)
        L_0x002a:
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r12 = 20
            r11.<init>(r12)
            java.util.ArrayList r13 = new java.util.ArrayList
            r13.<init>(r12)
            r12 = r4[r3]
            r14 = r4[r2]
            r15 = 6
            int[] r2 = new int[r15]
            r17 = r3
            r18 = r17
            r19 = r18
            r20 = r19
            r16 = r12
            r12 = r6
            r3 = r14
            r6 = r20
            r14 = r10
            r10 = 1
        L_0x004d:
            if (r6 != 0) goto L_0x014f
            int r5 = decodeCode(r0, r2, r3)
            byte r8 = (byte) r5
            java.lang.Byte r8 = java.lang.Byte.valueOf(r8)
            r13.add(r8)
            r8 = 106(0x6a, float:1.49E-43)
            if (r5 == r8) goto L_0x0060
            r10 = 1
        L_0x0060:
            if (r5 == r8) goto L_0x0068
            int r19 = r19 + 1
            int r16 = r19 * r5
            int r12 = r12 + r16
        L_0x0068:
            r16 = r3
            r9 = 0
        L_0x006b:
            if (r9 >= r15) goto L_0x0074
            r20 = r2[r9]
            int r16 = r16 + r20
            int r9 = r9 + 1
            goto L_0x006b
        L_0x0074:
            switch(r5) {
                case 103: goto L_0x0082;
                case 104: goto L_0x0082;
                case 105: goto L_0x0082;
                default: goto L_0x0077;
            }
        L_0x0077:
            r15 = 96
            java.lang.String r7 = "]C1"
            switch(r14) {
                case 99: goto L_0x0102;
                case 100: goto L_0x00c2;
                case 101: goto L_0x0087;
                default: goto L_0x007e;
            }
        L_0x007e:
            r9 = 100
            goto L_0x0135
        L_0x0082:
            com.google.zxing.FormatException r0 = com.google.zxing.FormatException.getFormatInstance()
            throw r0
        L_0x0087:
            r9 = 64
            if (r5 >= r9) goto L_0x0092
            int r7 = r5 + 32
            char r7 = (char) r7
            r11.append(r7)
            goto L_0x007e
        L_0x0092:
            if (r5 >= r15) goto L_0x009b
            int r7 = r5 + -64
            char r7 = (char) r7
            r11.append(r7)
            goto L_0x007e
        L_0x009b:
            if (r5 == r8) goto L_0x009e
            r10 = 0
        L_0x009e:
            r9 = 102(0x66, float:1.43E-43)
            if (r5 == r9) goto L_0x00b0
            if (r5 == r8) goto L_0x00ae
            switch(r5) {
                case 98: goto L_0x00aa;
                case 99: goto L_0x00f8;
                case 100: goto L_0x00a8;
                default: goto L_0x00a7;
            }
        L_0x00a7:
            goto L_0x00f4
        L_0x00a8:
            r7 = 0
            goto L_0x00ab
        L_0x00aa:
            r7 = 1
        L_0x00ab:
            r14 = 100
            goto L_0x00ff
        L_0x00ae:
            r6 = 1
            goto L_0x00f4
        L_0x00b0:
            if (r1 == 0) goto L_0x00f4
            int r8 = r11.length()
            if (r8 != 0) goto L_0x00bc
            r11.append(r7)
            goto L_0x00f4
        L_0x00bc:
            r7 = 29
            r11.append(r7)
            goto L_0x00f4
        L_0x00c2:
            if (r5 >= r15) goto L_0x00cb
            int r7 = r5 + 32
            char r7 = (char) r7
            r11.append(r7)
            goto L_0x007e
        L_0x00cb:
            if (r5 == r8) goto L_0x00ce
            r10 = 0
        L_0x00ce:
            r9 = 98
            if (r5 == r9) goto L_0x00fc
            r9 = 99
            if (r5 == r9) goto L_0x00f8
            r9 = 101(0x65, float:1.42E-43)
            if (r5 == r9) goto L_0x00f6
            r9 = 102(0x66, float:1.43E-43)
            if (r5 == r9) goto L_0x00e3
            if (r5 == r8) goto L_0x00e1
            goto L_0x00f4
        L_0x00e1:
            r6 = 1
            goto L_0x00f4
        L_0x00e3:
            if (r1 == 0) goto L_0x00f4
            int r8 = r11.length()
            if (r8 != 0) goto L_0x00ef
            r11.append(r7)
            goto L_0x00f4
        L_0x00ef:
            r7 = 29
            r11.append(r7)
        L_0x00f4:
            r7 = 0
            goto L_0x00ff
        L_0x00f6:
            r7 = 0
            goto L_0x00fd
        L_0x00f8:
            r7 = 0
            r14 = 99
            goto L_0x00ff
        L_0x00fc:
            r7 = 1
        L_0x00fd:
            r14 = 101(0x65, float:1.42E-43)
        L_0x00ff:
            r9 = 100
            goto L_0x0136
        L_0x0102:
            r9 = 100
            if (r5 >= r9) goto L_0x0113
            r7 = 10
            if (r5 >= r7) goto L_0x010f
            r7 = 48
            r11.append(r7)
        L_0x010f:
            r11.append(r5)
            goto L_0x0135
        L_0x0113:
            if (r5 == r8) goto L_0x0116
            r10 = 0
        L_0x0116:
            if (r5 == r8) goto L_0x0134
            switch(r5) {
                case 100: goto L_0x0132;
                case 101: goto L_0x012e;
                case 102: goto L_0x011c;
                default: goto L_0x011b;
            }
        L_0x011b:
            goto L_0x0135
        L_0x011c:
            if (r1 == 0) goto L_0x0135
            int r8 = r11.length()
            if (r8 != 0) goto L_0x0128
            r11.append(r7)
            goto L_0x0135
        L_0x0128:
            r7 = 29
            r11.append(r7)
            goto L_0x0135
        L_0x012e:
            r7 = 0
            r14 = 101(0x65, float:1.42E-43)
            goto L_0x0136
        L_0x0132:
            r14 = r9
            goto L_0x0135
        L_0x0134:
            r6 = 1
        L_0x0135:
            r7 = 0
        L_0x0136:
            r8 = 101(0x65, float:1.42E-43)
            if (r17 == 0) goto L_0x013f
            if (r14 != r8) goto L_0x013e
            r14 = r9
            goto L_0x013f
        L_0x013e:
            r14 = r8
        L_0x013f:
            r17 = r7
            r20 = r18
            r15 = 6
            r18 = r5
            r5 = 2
            r21 = r16
            r16 = r3
            r3 = r21
            goto L_0x004d
        L_0x014f:
            int r1 = r0.getNextUnset(r3)
            int r2 = r24.getSize()
            int r3 = r1 - r16
            r5 = 2
            int r3 = r3 / r5
            int r3 = r3 + r1
            int r2 = java.lang.Math.min(r2, r3)
            r3 = 0
            boolean r0 = r0.isRange(r1, r2, r3)
            if (r0 == 0) goto L_0x01dd
            r3 = r20
            int r19 = r19 * r3
            int r12 = r12 - r19
            int r12 = r12 % 103
            if (r12 != r3) goto L_0x01d8
            int r0 = r11.length()
            if (r0 == 0) goto L_0x01d3
            if (r0 <= 0) goto L_0x018a
            if (r10 == 0) goto L_0x018a
            r2 = 99
            if (r14 != r2) goto L_0x0185
            int r2 = r0 + -2
            r11.delete(r2, r0)
            goto L_0x018a
        L_0x0185:
            int r2 = r0 + -1
            r11.delete(r2, r0)
        L_0x018a:
            r0 = 1
            r2 = r4[r0]
            r0 = 0
            r3 = r4[r0]
            int r2 = r2 + r3
            float r0 = (float) r2
            r2 = 1073741824(0x40000000, float:2.0)
            float r0 = r0 / r2
            int r1 = r1 + r16
            float r1 = (float) r1
            float r1 = r1 / r2
            int r2 = r13.size()
            byte[] r3 = new byte[r2]
            r4 = 0
        L_0x01a0:
            if (r4 >= r2) goto L_0x01b1
            java.lang.Object r5 = r13.get(r4)
            java.lang.Byte r5 = (java.lang.Byte) r5
            byte r5 = r5.byteValue()
            r3[r4] = r5
            int r4 = r4 + 1
            goto L_0x01a0
        L_0x01b1:
            com.google.zxing.Result r2 = new com.google.zxing.Result
            java.lang.String r4 = r11.toString()
            r5 = 2
            com.google.zxing.ResultPoint[] r5 = new com.google.zxing.ResultPoint[r5]
            com.google.zxing.ResultPoint r6 = new com.google.zxing.ResultPoint
            r7 = r23
            float r7 = (float) r7
            r6.<init>(r0, r7)
            r0 = 0
            r5[r0] = r6
            com.google.zxing.ResultPoint r0 = new com.google.zxing.ResultPoint
            r0.<init>(r1, r7)
            r1 = 1
            r5[r1] = r0
            com.google.zxing.BarcodeFormat r0 = com.google.zxing.BarcodeFormat.CODE_128
            r2.<init>(r4, r3, r5, r0)
            return r2
        L_0x01d3:
            com.google.zxing.NotFoundException r0 = com.google.zxing.NotFoundException.getNotFoundInstance()
            throw r0
        L_0x01d8:
            com.google.zxing.ChecksumException r0 = com.google.zxing.ChecksumException.getChecksumInstance()
            throw r0
        L_0x01dd:
            com.google.zxing.NotFoundException r0 = com.google.zxing.NotFoundException.getNotFoundInstance()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.oned.Code128Reader.decodeRow(int, com.google.zxing.common.BitArray, java.util.Map):com.google.zxing.Result");
    }
}
