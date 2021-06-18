package com.google.zxing.qrcode.decoder;

import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.common.BitSource;
import com.google.zxing.common.CharacterSetECI;
import com.google.zxing.common.StringUtils;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Map;

final class DecodedBitStreamParser {
    private static final char[] ALPHANUMERIC_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', ' ', '$', '%', '*', '+', '-', '.', '/', ':'};

    /* JADX WARNING: Removed duplicated region for block: B:57:0x00cf A[LOOP:0: B:1:0x001a->B:57:0x00cf, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x00b7 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static com.google.zxing.common.DecoderResult decode(byte[] r16, com.google.zxing.qrcode.decoder.Version r17, com.google.zxing.qrcode.decoder.ErrorCorrectionLevel r18, java.util.Map<com.google.zxing.DecodeHintType, ?> r19) throws com.google.zxing.FormatException {
        /*
            r0 = r16
            r1 = r17
            com.google.zxing.common.BitSource r8 = new com.google.zxing.common.BitSource
            r8.<init>(r0)
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r2 = 50
            r9.<init>(r2)
            java.util.ArrayList r10 = new java.util.ArrayList
            r11 = 1
            r10.<init>(r11)
            r2 = 0
            r12 = 0
            r13 = r2
            r14 = r12
        L_0x001a:
            int r2 = r8.available()     // Catch:{ IllegalArgumentException -> 0x00d2 }
            r3 = 4
            if (r2 >= r3) goto L_0x0025
            com.google.zxing.qrcode.decoder.Mode r2 = com.google.zxing.qrcode.decoder.Mode.TERMINATOR     // Catch:{ IllegalArgumentException -> 0x00d2 }
        L_0x0023:
            r15 = r2
            goto L_0x002e
        L_0x0025:
            int r2 = r8.readBits(r3)     // Catch:{ IllegalArgumentException -> 0x00d2 }
            com.google.zxing.qrcode.decoder.Mode r2 = com.google.zxing.qrcode.decoder.Mode.forBits(r2)     // Catch:{ IllegalArgumentException -> 0x00d2 }
            goto L_0x0023
        L_0x002e:
            com.google.zxing.qrcode.decoder.Mode r7 = com.google.zxing.qrcode.decoder.Mode.TERMINATOR     // Catch:{ IllegalArgumentException -> 0x00d2 }
            if (r15 == r7) goto L_0x00b4
            com.google.zxing.qrcode.decoder.Mode r2 = com.google.zxing.qrcode.decoder.Mode.FNC1_FIRST_POSITION     // Catch:{ IllegalArgumentException -> 0x00d2 }
            if (r15 == r2) goto L_0x00b1
            com.google.zxing.qrcode.decoder.Mode r2 = com.google.zxing.qrcode.decoder.Mode.FNC1_SECOND_POSITION     // Catch:{ IllegalArgumentException -> 0x00d2 }
            if (r15 != r2) goto L_0x003c
            goto L_0x00b1
        L_0x003c:
            com.google.zxing.qrcode.decoder.Mode r2 = com.google.zxing.qrcode.decoder.Mode.STRUCTURED_APPEND     // Catch:{ IllegalArgumentException -> 0x00d2 }
            if (r15 != r2) goto L_0x0052
            int r2 = r8.available()     // Catch:{ IllegalArgumentException -> 0x00d2 }
            r3 = 16
            if (r2 < r3) goto L_0x004d
            r8.readBits(r3)     // Catch:{ IllegalArgumentException -> 0x00d2 }
            goto L_0x00b4
        L_0x004d:
            com.google.zxing.FormatException r0 = com.google.zxing.FormatException.getFormatInstance()     // Catch:{ IllegalArgumentException -> 0x00d2 }
            throw r0     // Catch:{ IllegalArgumentException -> 0x00d2 }
        L_0x0052:
            com.google.zxing.qrcode.decoder.Mode r2 = com.google.zxing.qrcode.decoder.Mode.ECI     // Catch:{ IllegalArgumentException -> 0x00d2 }
            if (r15 != r2) goto L_0x0066
            int r2 = parseECIValue(r8)     // Catch:{ IllegalArgumentException -> 0x00d2 }
            com.google.zxing.common.CharacterSetECI r14 = com.google.zxing.common.CharacterSetECI.getCharacterSetECIByValue(r2)     // Catch:{ IllegalArgumentException -> 0x00d2 }
            if (r14 == 0) goto L_0x0061
            goto L_0x00b4
        L_0x0061:
            com.google.zxing.FormatException r0 = com.google.zxing.FormatException.getFormatInstance()     // Catch:{ IllegalArgumentException -> 0x00d2 }
            throw r0     // Catch:{ IllegalArgumentException -> 0x00d2 }
        L_0x0066:
            com.google.zxing.qrcode.decoder.Mode r2 = com.google.zxing.qrcode.decoder.Mode.HANZI     // Catch:{ IllegalArgumentException -> 0x00d2 }
            if (r15 != r2) goto L_0x007c
            int r2 = r8.readBits(r3)     // Catch:{ IllegalArgumentException -> 0x00d2 }
            int r3 = r15.getCharacterCountBits(r1)     // Catch:{ IllegalArgumentException -> 0x00d2 }
            int r3 = r8.readBits(r3)     // Catch:{ IllegalArgumentException -> 0x00d2 }
            if (r2 != r11) goto L_0x00b4
            decodeHanziSegment(r8, r9, r3)     // Catch:{ IllegalArgumentException -> 0x00d2 }
            goto L_0x00b4
        L_0x007c:
            int r2 = r15.getCharacterCountBits(r1)     // Catch:{ IllegalArgumentException -> 0x00d2 }
            int r4 = r8.readBits(r2)     // Catch:{ IllegalArgumentException -> 0x00d2 }
            com.google.zxing.qrcode.decoder.Mode r2 = com.google.zxing.qrcode.decoder.Mode.NUMERIC     // Catch:{ IllegalArgumentException -> 0x00d2 }
            if (r15 != r2) goto L_0x008c
            decodeNumericSegment(r8, r9, r4)     // Catch:{ IllegalArgumentException -> 0x00d2 }
            goto L_0x00b4
        L_0x008c:
            com.google.zxing.qrcode.decoder.Mode r2 = com.google.zxing.qrcode.decoder.Mode.ALPHANUMERIC     // Catch:{ IllegalArgumentException -> 0x00d2 }
            if (r15 != r2) goto L_0x0094
            decodeAlphanumericSegment(r8, r9, r4, r13)     // Catch:{ IllegalArgumentException -> 0x00d2 }
            goto L_0x00b4
        L_0x0094:
            com.google.zxing.qrcode.decoder.Mode r2 = com.google.zxing.qrcode.decoder.Mode.BYTE     // Catch:{ IllegalArgumentException -> 0x00d2 }
            if (r15 != r2) goto L_0x00a3
            r2 = r8
            r3 = r9
            r5 = r14
            r6 = r10
            r11 = r7
            r7 = r19
            decodeByteSegment(r2, r3, r4, r5, r6, r7)     // Catch:{ IllegalArgumentException -> 0x00d2 }
            goto L_0x00b5
        L_0x00a3:
            r11 = r7
            com.google.zxing.qrcode.decoder.Mode r2 = com.google.zxing.qrcode.decoder.Mode.KANJI     // Catch:{ IllegalArgumentException -> 0x00d2 }
            if (r15 != r2) goto L_0x00ac
            decodeKanjiSegment(r8, r9, r4)     // Catch:{ IllegalArgumentException -> 0x00d2 }
            goto L_0x00b5
        L_0x00ac:
            com.google.zxing.FormatException r0 = com.google.zxing.FormatException.getFormatInstance()     // Catch:{ IllegalArgumentException -> 0x00d2 }
            throw r0     // Catch:{ IllegalArgumentException -> 0x00d2 }
        L_0x00b1:
            r11 = r7
            r13 = 1
            goto L_0x00b5
        L_0x00b4:
            r11 = r7
        L_0x00b5:
            if (r15 != r11) goto L_0x00cf
            com.google.zxing.common.DecoderResult r1 = new com.google.zxing.common.DecoderResult
            java.lang.String r2 = r9.toString()
            boolean r3 = r10.isEmpty()
            if (r3 == 0) goto L_0x00c4
            r10 = r12
        L_0x00c4:
            if (r18 != 0) goto L_0x00c7
            goto L_0x00cb
        L_0x00c7:
            java.lang.String r12 = r18.toString()
        L_0x00cb:
            r1.<init>(r0, r2, r10, r12)
            return r1
        L_0x00cf:
            r11 = 1
            goto L_0x001a
        L_0x00d2:
            com.google.zxing.FormatException r0 = com.google.zxing.FormatException.getFormatInstance()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.qrcode.decoder.DecodedBitStreamParser.decode(byte[], com.google.zxing.qrcode.decoder.Version, com.google.zxing.qrcode.decoder.ErrorCorrectionLevel, java.util.Map):com.google.zxing.common.DecoderResult");
    }

    private static void decodeHanziSegment(BitSource bitSource, StringBuilder sb, int i) throws FormatException {
        if (i * 13 <= bitSource.available()) {
            byte[] bArr = new byte[(i * 2)];
            int i2 = 0;
            while (i > 0) {
                int readBits = bitSource.readBits(13);
                int i3 = (readBits % 96) | ((readBits / 96) << 8);
                int i4 = i3 + (i3 < 959 ? 41377 : 42657);
                bArr[i2] = (byte) ((i4 >> 8) & 255);
                bArr[i2 + 1] = (byte) (i4 & 255);
                i2 += 2;
                i--;
            }
            try {
                sb.append(new String(bArr, "GB2312"));
            } catch (UnsupportedEncodingException unused) {
                throw FormatException.getFormatInstance();
            }
        } else {
            throw FormatException.getFormatInstance();
        }
    }

    private static void decodeKanjiSegment(BitSource bitSource, StringBuilder sb, int i) throws FormatException {
        if (i * 13 <= bitSource.available()) {
            byte[] bArr = new byte[(i * 2)];
            int i2 = 0;
            while (i > 0) {
                int readBits = bitSource.readBits(13);
                int i3 = (readBits % 192) | ((readBits / 192) << 8);
                int i4 = i3 + (i3 < 7936 ? 33088 : 49472);
                bArr[i2] = (byte) (i4 >> 8);
                bArr[i2 + 1] = (byte) i4;
                i2 += 2;
                i--;
            }
            try {
                sb.append(new String(bArr, "SJIS"));
            } catch (UnsupportedEncodingException unused) {
                throw FormatException.getFormatInstance();
            }
        } else {
            throw FormatException.getFormatInstance();
        }
    }

    private static void decodeByteSegment(BitSource bitSource, StringBuilder sb, int i, CharacterSetECI characterSetECI, Collection<byte[]> collection, Map<DecodeHintType, ?> map) throws FormatException {
        String str;
        if ((i << 3) <= bitSource.available()) {
            byte[] bArr = new byte[i];
            for (int i2 = 0; i2 < i; i2++) {
                bArr[i2] = (byte) bitSource.readBits(8);
            }
            if (characterSetECI == null) {
                str = StringUtils.guessEncoding(bArr, map);
            } else {
                str = characterSetECI.name();
            }
            try {
                sb.append(new String(bArr, str));
                collection.add(bArr);
            } catch (UnsupportedEncodingException unused) {
                throw FormatException.getFormatInstance();
            }
        } else {
            throw FormatException.getFormatInstance();
        }
    }

    private static char toAlphaNumericChar(int i) throws FormatException {
        char[] cArr = ALPHANUMERIC_CHARS;
        if (i < cArr.length) {
            return cArr[i];
        }
        throw FormatException.getFormatInstance();
    }

    private static void decodeAlphanumericSegment(BitSource bitSource, StringBuilder sb, int i, boolean z) throws FormatException {
        while (i > 1) {
            if (bitSource.available() >= 11) {
                int readBits = bitSource.readBits(11);
                sb.append(toAlphaNumericChar(readBits / 45));
                sb.append(toAlphaNumericChar(readBits % 45));
                i -= 2;
            } else {
                throw FormatException.getFormatInstance();
            }
        }
        if (i == 1) {
            if (bitSource.available() >= 6) {
                sb.append(toAlphaNumericChar(bitSource.readBits(6)));
            } else {
                throw FormatException.getFormatInstance();
            }
        }
        if (z) {
            for (int length = sb.length(); length < sb.length(); length++) {
                if (sb.charAt(length) == '%') {
                    if (length < sb.length() - 1) {
                        int i2 = length + 1;
                        if (sb.charAt(i2) == '%') {
                            sb.deleteCharAt(i2);
                        }
                    }
                    sb.setCharAt(length, 29);
                }
            }
        }
    }

    private static void decodeNumericSegment(BitSource bitSource, StringBuilder sb, int i) throws FormatException {
        while (i >= 3) {
            if (bitSource.available() >= 10) {
                int readBits = bitSource.readBits(10);
                if (readBits < 1000) {
                    sb.append(toAlphaNumericChar(readBits / 100));
                    sb.append(toAlphaNumericChar((readBits / 10) % 10));
                    sb.append(toAlphaNumericChar(readBits % 10));
                    i -= 3;
                } else {
                    throw FormatException.getFormatInstance();
                }
            } else {
                throw FormatException.getFormatInstance();
            }
        }
        if (i == 2) {
            if (bitSource.available() >= 7) {
                int readBits2 = bitSource.readBits(7);
                if (readBits2 < 100) {
                    sb.append(toAlphaNumericChar(readBits2 / 10));
                    sb.append(toAlphaNumericChar(readBits2 % 10));
                    return;
                }
                throw FormatException.getFormatInstance();
            }
            throw FormatException.getFormatInstance();
        } else if (i != 1) {
        } else {
            if (bitSource.available() >= 4) {
                int readBits3 = bitSource.readBits(4);
                if (readBits3 < 10) {
                    sb.append(toAlphaNumericChar(readBits3));
                    return;
                }
                throw FormatException.getFormatInstance();
            }
            throw FormatException.getFormatInstance();
        }
    }

    private static int parseECIValue(BitSource bitSource) throws FormatException {
        int readBits = bitSource.readBits(8);
        if ((readBits & 128) == 0) {
            return readBits & 127;
        }
        if ((readBits & 192) == 128) {
            return bitSource.readBits(8) | ((readBits & 63) << 8);
        }
        if ((readBits & 224) == 192) {
            return bitSource.readBits(16) | ((readBits & 31) << 16);
        }
        throw FormatException.getFormatInstance();
    }
}
