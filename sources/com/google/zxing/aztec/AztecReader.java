package com.google.zxing.aztec;

import com.google.zxing.Reader;

public final class AztecReader implements Reader {
    public void reset() {
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0031  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0063 A[LOOP:0: B:34:0x0061->B:35:0x0063, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0080  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x008b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.google.zxing.Result decode(com.google.zxing.BinaryBitmap r5, java.util.Map<com.google.zxing.DecodeHintType, ?> r6) throws com.google.zxing.NotFoundException, com.google.zxing.FormatException {
        /*
            r4 = this;
            com.google.zxing.aztec.detector.Detector r4 = new com.google.zxing.aztec.detector.Detector
            com.google.zxing.common.BitMatrix r5 = r5.getBlackMatrix()
            r4.<init>(r5)
            r5 = 0
            r0 = 0
            com.google.zxing.aztec.AztecDetectorResult r1 = r4.detect(r5)     // Catch:{ NotFoundException -> 0x002b, FormatException -> 0x0025 }
            com.google.zxing.ResultPoint[] r2 = r1.getPoints()     // Catch:{ NotFoundException -> 0x002b, FormatException -> 0x0025 }
            com.google.zxing.aztec.decoder.Decoder r3 = new com.google.zxing.aztec.decoder.Decoder     // Catch:{ NotFoundException -> 0x0023, FormatException -> 0x0021 }
            r3.<init>()     // Catch:{ NotFoundException -> 0x0023, FormatException -> 0x0021 }
            com.google.zxing.common.DecoderResult r1 = r3.decode(r1)     // Catch:{ NotFoundException -> 0x0023, FormatException -> 0x0021 }
            r3 = r2
            r2 = r0
            r0 = r1
            r1 = r2
            goto L_0x002f
        L_0x0021:
            r1 = move-exception
            goto L_0x0027
        L_0x0023:
            r1 = move-exception
            goto L_0x002d
        L_0x0025:
            r1 = move-exception
            r2 = r0
        L_0x0027:
            r3 = r2
            r2 = r1
            r1 = r0
            goto L_0x002f
        L_0x002b:
            r1 = move-exception
            r2 = r0
        L_0x002d:
            r3 = r2
            r2 = r0
        L_0x002f:
            if (r0 != 0) goto L_0x0054
            r0 = 1
            com.google.zxing.aztec.AztecDetectorResult r4 = r4.detect(r0)     // Catch:{ NotFoundException -> 0x004c, FormatException -> 0x0044 }
            com.google.zxing.ResultPoint[] r3 = r4.getPoints()     // Catch:{ NotFoundException -> 0x004c, FormatException -> 0x0044 }
            com.google.zxing.aztec.decoder.Decoder r0 = new com.google.zxing.aztec.decoder.Decoder     // Catch:{ NotFoundException -> 0x004c, FormatException -> 0x0044 }
            r0.<init>()     // Catch:{ NotFoundException -> 0x004c, FormatException -> 0x0044 }
            com.google.zxing.common.DecoderResult r0 = r0.decode(r4)     // Catch:{ NotFoundException -> 0x004c, FormatException -> 0x0044 }
            goto L_0x0054
        L_0x0044:
            r4 = move-exception
            if (r1 != 0) goto L_0x004b
            if (r2 == 0) goto L_0x004a
            throw r2
        L_0x004a:
            throw r4
        L_0x004b:
            throw r1
        L_0x004c:
            r4 = move-exception
            if (r1 != 0) goto L_0x0053
            if (r2 == 0) goto L_0x0052
            throw r2
        L_0x0052:
            throw r4
        L_0x0053:
            throw r1
        L_0x0054:
            if (r6 == 0) goto L_0x006b
            com.google.zxing.DecodeHintType r4 = com.google.zxing.DecodeHintType.NEED_RESULT_POINT_CALLBACK
            java.lang.Object r4 = r6.get(r4)
            com.google.zxing.ResultPointCallback r4 = (com.google.zxing.ResultPointCallback) r4
            if (r4 == 0) goto L_0x006b
            int r6 = r3.length
        L_0x0061:
            if (r5 >= r6) goto L_0x006b
            r1 = r3[r5]
            r4.foundPossibleResultPoint(r1)
            int r5 = r5 + 1
            goto L_0x0061
        L_0x006b:
            com.google.zxing.Result r4 = new com.google.zxing.Result
            java.lang.String r5 = r0.getText()
            byte[] r6 = r0.getRawBytes()
            com.google.zxing.BarcodeFormat r1 = com.google.zxing.BarcodeFormat.AZTEC
            r4.<init>(r5, r6, r3, r1)
            java.util.List r5 = r0.getByteSegments()
            if (r5 == 0) goto L_0x0085
            com.google.zxing.ResultMetadataType r6 = com.google.zxing.ResultMetadataType.BYTE_SEGMENTS
            r4.putMetadata(r6, r5)
        L_0x0085:
            java.lang.String r5 = r0.getECLevel()
            if (r5 == 0) goto L_0x0090
            com.google.zxing.ResultMetadataType r6 = com.google.zxing.ResultMetadataType.ERROR_CORRECTION_LEVEL
            r4.putMetadata(r6, r5)
        L_0x0090:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.aztec.AztecReader.decode(com.google.zxing.BinaryBitmap, java.util.Map):com.google.zxing.Result");
    }
}
