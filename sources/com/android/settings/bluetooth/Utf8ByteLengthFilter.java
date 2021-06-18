package com.android.settings.bluetooth;

import android.text.InputFilter;
import android.text.Spanned;
import androidx.annotation.Keep;

public class Utf8ByteLengthFilter implements InputFilter {
    private final int mMaxBytes;

    @Keep
    Utf8ByteLengthFilter(int i) {
        this.mMaxBytes = i;
    }

    public CharSequence filter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4) {
        CharSequence charSequence2 = charSequence;
        int i5 = i2;
        int i6 = i;
        int i7 = 0;
        while (true) {
            int i8 = 2;
            if (i6 >= i5) {
                break;
            }
            char charAt = charSequence2.charAt(i6);
            if (charAt < 128) {
                i8 = 1;
            } else if (charAt >= 2048) {
                i8 = 3;
            }
            i7 += i8;
            i6++;
        }
        int length = spanned.length();
        int i9 = 0;
        for (int i10 = 0; i10 < length; i10++) {
            int i11 = i4;
            Spanned spanned2 = spanned;
            if (i10 < i3 || i10 >= i11) {
                char charAt2 = spanned2.charAt(i10);
                i9 += charAt2 < 128 ? 1 : charAt2 < 2048 ? 2 : 3;
            }
        }
        int i12 = this.mMaxBytes - i9;
        if (i12 <= 0) {
            return "";
        }
        if (i12 >= i7) {
            return null;
        }
        for (int i13 = i; i13 < i5; i13++) {
            char charAt3 = charSequence2.charAt(i13);
            i12 -= charAt3 < 128 ? 1 : charAt3 < 2048 ? 2 : 3;
            if (i12 < 0) {
                return charSequence2.subSequence(i, i13);
            }
            int i14 = i;
        }
        return null;
    }
}
