package com.google.zxing.oned;

import androidx.constraintlayout.widget.R$styleable;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.util.ArrayList;
import java.util.Map;

public final class Code128Writer extends OneDimensionalCodeWriter {
    public BitMatrix encode(String str, BarcodeFormat barcodeFormat, int i, int i2, Map<EncodeHintType, ?> map) throws WriterException {
        if (barcodeFormat == BarcodeFormat.CODE_128) {
            return super.encode(str, barcodeFormat, i, i2, map);
        }
        throw new IllegalArgumentException("Can only encode CODE_128, but got " + barcodeFormat);
    }

    public boolean[] encode(String str) {
        int i;
        int length = str.length();
        if (length < 1 || length > 80) {
            throw new IllegalArgumentException("Contents length should be between 1 and 80 characters, but got " + length);
        }
        int i2 = 0;
        for (int i3 = 0; i3 < length; i3++) {
            char charAt = str.charAt(i3);
            if (charAt < ' ' || charAt > '~') {
                switch (charAt) {
                    case 241:
                    case 242:
                    case 243:
                    case 244:
                        break;
                    default:
                        throw new IllegalArgumentException("Bad character in input: " + charAt);
                }
            }
        }
        ArrayList<int[]> arrayList = new ArrayList<>();
        int i4 = 1;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        while (i5 < length) {
            int i8 = 99;
            int i9 = 100;
            if (!isDigits(str, i5, i7 == 99 ? 2 : 4)) {
                i8 = 100;
            }
            if (i8 == i7) {
                if (i7 != 100) {
                    switch (str.charAt(i5)) {
                        case 241:
                            i9 = R$styleable.Constraint_layout_goneMarginStart;
                            break;
                        case 242:
                            i9 = 97;
                            break;
                        case 243:
                            i9 = 96;
                            break;
                        case 244:
                            break;
                        default:
                            int i10 = i5 + 2;
                            i = Integer.parseInt(str.substring(i5, i10));
                            i5 = i10;
                            break;
                    }
                } else {
                    i9 = str.charAt(i5) - ' ';
                }
                i5++;
            } else {
                if (i7 == 0) {
                    i = i8 == 100 ? R$styleable.Constraint_motionStagger : R$styleable.Constraint_pathMotionArc;
                } else {
                    i = i8;
                }
                i7 = i8;
            }
            arrayList.add(Code128Reader.CODE_PATTERNS[i]);
            i6 += i * i4;
            if (i5 != 0) {
                i4++;
            }
        }
        int i11 = i6 % R$styleable.Constraint_layout_goneMarginTop;
        int[][] iArr = Code128Reader.CODE_PATTERNS;
        arrayList.add(iArr[i11]);
        arrayList.add(iArr[106]);
        int i12 = 0;
        for (int[] iArr2 : arrayList) {
            for (int i13 : (int[]) r11.next()) {
                i12 += i13;
            }
        }
        boolean[] zArr = new boolean[i12];
        for (int[] appendPattern : arrayList) {
            i2 += OneDimensionalCodeWriter.appendPattern(zArr, i2, appendPattern, true);
        }
        return zArr;
    }

    private static boolean isDigits(CharSequence charSequence, int i, int i2) {
        int i3 = i2 + i;
        int length = charSequence.length();
        while (i < i3 && i < length) {
            char charAt = charSequence.charAt(i);
            if (charAt < '0' || charAt > '9') {
                if (charAt != 241) {
                    return false;
                }
                i3++;
            }
            i++;
        }
        if (i3 <= length) {
            return true;
        }
        return false;
    }
}
