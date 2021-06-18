package com.google.zxing.oned;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitArray;
import java.util.Arrays;
import java.util.Map;

public abstract class OneDReader implements Reader {
    public abstract Result decodeRow(int i, BitArray bitArray, Map<DecodeHintType, ?> map) throws NotFoundException, ChecksumException, FormatException;

    public void reset() {
    }

    public Result decode(BinaryBitmap binaryBitmap, Map<DecodeHintType, ?> map) throws NotFoundException, FormatException {
        try {
            return doDecode(binaryBitmap, map);
        } catch (NotFoundException e) {
            if (!(map != null && map.containsKey(DecodeHintType.TRY_HARDER)) || !binaryBitmap.isRotateSupported()) {
                throw e;
            }
            BinaryBitmap rotateCounterClockwise = binaryBitmap.rotateCounterClockwise();
            Result doDecode = doDecode(rotateCounterClockwise, map);
            Map<ResultMetadataType, Object> resultMetadata = doDecode.getResultMetadata();
            int i = 270;
            if (resultMetadata != null) {
                ResultMetadataType resultMetadataType = ResultMetadataType.ORIENTATION;
                if (resultMetadata.containsKey(resultMetadataType)) {
                    i = (((Integer) resultMetadata.get(resultMetadataType)).intValue() + 270) % 360;
                }
            }
            doDecode.putMetadata(ResultMetadataType.ORIENTATION, Integer.valueOf(i));
            ResultPoint[] resultPoints = doDecode.getResultPoints();
            if (resultPoints != null) {
                int height = rotateCounterClockwise.getHeight();
                for (int i2 = 0; i2 < resultPoints.length; i2++) {
                    resultPoints[i2] = new ResultPoint((((float) height) - resultPoints[i2].getY()) - 1.0f, resultPoints[i2].getX());
                }
            }
            return doDecode;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:40:0x007c A[Catch:{ ReaderException -> 0x00cc }] */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x00cb A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.google.zxing.Result doDecode(com.google.zxing.BinaryBitmap r22, java.util.Map<com.google.zxing.DecodeHintType, ?> r23) throws com.google.zxing.NotFoundException {
        /*
            r21 = this;
            r0 = r23
            int r1 = r22.getWidth()
            int r2 = r22.getHeight()
            com.google.zxing.common.BitArray r3 = new com.google.zxing.common.BitArray
            r3.<init>(r1)
            int r4 = r2 >> 1
            r5 = 0
            r6 = 1
            if (r0 == 0) goto L_0x001f
            com.google.zxing.DecodeHintType r7 = com.google.zxing.DecodeHintType.TRY_HARDER
            boolean r7 = r0.containsKey(r7)
            if (r7 == 0) goto L_0x001f
            r7 = r6
            goto L_0x0020
        L_0x001f:
            r7 = r5
        L_0x0020:
            if (r7 == 0) goto L_0x0025
            r8 = 8
            goto L_0x0026
        L_0x0025:
            r8 = 5
        L_0x0026:
            int r8 = r2 >> r8
            int r8 = java.lang.Math.max(r6, r8)
            if (r7 == 0) goto L_0x0030
            r7 = r2
            goto L_0x0032
        L_0x0030:
            r7 = 15
        L_0x0032:
            r9 = r5
        L_0x0033:
            if (r9 >= r7) goto L_0x00e7
            int r10 = r9 + 1
            int r11 = r10 >> 1
            r9 = r9 & 1
            if (r9 != 0) goto L_0x003f
            r9 = r6
            goto L_0x0040
        L_0x003f:
            r9 = r5
        L_0x0040:
            if (r9 == 0) goto L_0x0043
            goto L_0x0044
        L_0x0043:
            int r11 = -r11
        L_0x0044:
            int r11 = r11 * r8
            int r11 = r11 + r4
            if (r11 < 0) goto L_0x00e7
            if (r11 >= r2) goto L_0x00e7
            r9 = r22
            com.google.zxing.common.BitArray r3 = r9.getBlackRow(r11, r3)     // Catch:{ NotFoundException -> 0x00db }
            r12 = r5
        L_0x0051:
            r13 = 2
            if (r12 >= r13) goto L_0x00db
            if (r12 != r6) goto L_0x0074
            r3.reverse()
            if (r0 == 0) goto L_0x0074
            com.google.zxing.DecodeHintType r13 = com.google.zxing.DecodeHintType.NEED_RESULT_POINT_CALLBACK
            boolean r14 = r0.containsKey(r13)
            if (r14 == 0) goto L_0x0074
            java.util.EnumMap r14 = new java.util.EnumMap
            java.lang.Class<com.google.zxing.DecodeHintType> r15 = com.google.zxing.DecodeHintType.class
            r14.<init>(r15)
            r14.putAll(r0)
            r14.remove(r13)
            r13 = r21
            r0 = r14
            goto L_0x0076
        L_0x0074:
            r13 = r21
        L_0x0076:
            com.google.zxing.Result r14 = r13.decodeRow(r11, r3, r0)     // Catch:{ ReaderException -> 0x00cc }
            if (r12 != r6) goto L_0x00cb
            com.google.zxing.ResultMetadataType r15 = com.google.zxing.ResultMetadataType.ORIENTATION     // Catch:{ ReaderException -> 0x00cc }
            r16 = 180(0xb4, float:2.52E-43)
            java.lang.Integer r6 = java.lang.Integer.valueOf(r16)     // Catch:{ ReaderException -> 0x00c5 }
            r14.putMetadata(r15, r6)     // Catch:{ ReaderException -> 0x00c5 }
            com.google.zxing.ResultPoint[] r6 = r14.getResultPoints()     // Catch:{ ReaderException -> 0x00c5 }
            if (r6 == 0) goto L_0x00cb
            com.google.zxing.ResultPoint r15 = new com.google.zxing.ResultPoint     // Catch:{ ReaderException -> 0x00c5 }
            r16 = r0
            float r0 = (float) r1
            r18 = r6[r5]     // Catch:{ ReaderException -> 0x00c7 }
            float r18 = r18.getX()     // Catch:{ ReaderException -> 0x00c7 }
            float r18 = r0 - r18
            r19 = 1065353216(0x3f800000, float:1.0)
            r20 = r1
            float r1 = r18 - r19
            r18 = r6[r5]     // Catch:{ ReaderException -> 0x00c9 }
            float r5 = r18.getY()     // Catch:{ ReaderException -> 0x00c9 }
            r15.<init>(r1, r5)     // Catch:{ ReaderException -> 0x00c9 }
            r1 = 0
            r6[r1] = r15     // Catch:{ ReaderException -> 0x00c9 }
            com.google.zxing.ResultPoint r5 = new com.google.zxing.ResultPoint     // Catch:{ ReaderException -> 0x00c9 }
            r15 = 1
            r17 = r6[r15]     // Catch:{ ReaderException -> 0x00d1 }
            float r17 = r17.getX()     // Catch:{ ReaderException -> 0x00d1 }
            float r0 = r0 - r17
            float r0 = r0 - r19
            r17 = r6[r15]     // Catch:{ ReaderException -> 0x00d1 }
            float r1 = r17.getY()     // Catch:{ ReaderException -> 0x00d1 }
            r5.<init>(r0, r1)     // Catch:{ ReaderException -> 0x00d1 }
            r6[r15] = r5     // Catch:{ ReaderException -> 0x00d1 }
            goto L_0x00cb
        L_0x00c5:
            r16 = r0
        L_0x00c7:
            r20 = r1
        L_0x00c9:
            r15 = 1
            goto L_0x00d1
        L_0x00cb:
            return r14
        L_0x00cc:
            r16 = r0
            r20 = r1
            r15 = r6
        L_0x00d1:
            int r12 = r12 + 1
            r6 = r15
            r0 = r16
            r1 = r20
            r5 = 0
            goto L_0x0051
        L_0x00db:
            r13 = r21
            r20 = r1
            r15 = r6
            r9 = r10
            r6 = r15
            r1 = r20
            r5 = 0
            goto L_0x0033
        L_0x00e7:
            com.google.zxing.NotFoundException r0 = com.google.zxing.NotFoundException.getNotFoundInstance()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.oned.OneDReader.doDecode(com.google.zxing.BinaryBitmap, java.util.Map):com.google.zxing.Result");
    }

    protected static void recordPattern(BitArray bitArray, int i, int[] iArr) throws NotFoundException {
        int length = iArr.length;
        int i2 = 0;
        Arrays.fill(iArr, 0, length, 0);
        int size = bitArray.getSize();
        if (i < size) {
            boolean z = !bitArray.get(i);
            while (i < size) {
                if (bitArray.get(i) ^ z) {
                    iArr[i2] = iArr[i2] + 1;
                } else {
                    i2++;
                    if (i2 == length) {
                        break;
                    }
                    iArr[i2] = 1;
                    z = !z;
                }
                i++;
            }
            if (i2 == length) {
                return;
            }
            if (i2 != length - 1 || i != size) {
                throw NotFoundException.getNotFoundInstance();
            }
            return;
        }
        throw NotFoundException.getNotFoundInstance();
    }

    protected static void recordPatternInReverse(BitArray bitArray, int i, int[] iArr) throws NotFoundException {
        int length = iArr.length;
        boolean z = bitArray.get(i);
        while (i > 0 && length >= 0) {
            i--;
            if (bitArray.get(i) != z) {
                length--;
                z = !z;
            }
        }
        if (length < 0) {
            recordPattern(bitArray, i + 1, iArr);
            return;
        }
        throw NotFoundException.getNotFoundInstance();
    }

    protected static int patternMatchVariance(int[] iArr, int[] iArr2, int i) {
        int length = iArr.length;
        int i2 = 0;
        int i3 = 0;
        for (int i4 = 0; i4 < length; i4++) {
            i2 += iArr[i4];
            i3 += iArr2[i4];
        }
        if (i2 < i3) {
            return Integer.MAX_VALUE;
        }
        int i5 = (i2 << 8) / i3;
        int i6 = (i * i5) >> 8;
        int i7 = 0;
        for (int i8 = 0; i8 < length; i8++) {
            int i9 = iArr[i8] << 8;
            int i10 = iArr2[i8] * i5;
            int i11 = i9 > i10 ? i9 - i10 : i10 - i9;
            if (i11 > i6) {
                return Integer.MAX_VALUE;
            }
            i7 += i11;
        }
        return i7 / i2;
    }
}
