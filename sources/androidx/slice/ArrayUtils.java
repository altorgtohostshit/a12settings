package androidx.slice;

import androidx.core.util.ObjectsCompat;
import java.lang.reflect.Array;

class ArrayUtils {
    public static <T> boolean contains(T[] tArr, T t) {
        for (T equals : tArr) {
            if (ObjectsCompat.equals(equals, t)) {
                return true;
            }
        }
        return false;
    }

    public static <T> T[] appendElement(Class<T> cls, T[] tArr, T t) {
        T[] tArr2;
        int i = 0;
        if (tArr != null) {
            int length = tArr.length;
            tArr2 = (Object[]) Array.newInstance(cls, length + 1);
            System.arraycopy(tArr, 0, tArr2, 0, length);
            i = length;
        } else {
            tArr2 = (Object[]) Array.newInstance(cls, 1);
        }
        tArr2[i] = t;
        return tArr2;
    }

    public static <T> T[] removeElement(Class<T> cls, T[] tArr, T t) {
        if (tArr == null || !contains(tArr, t)) {
            return tArr;
        }
        int length = tArr.length;
        int i = 0;
        while (i < length) {
            if (!ObjectsCompat.equals(tArr[i], t)) {
                i++;
            } else if (length == 1) {
                return null;
            } else {
                T[] tArr2 = (Object[]) Array.newInstance(cls, length - 1);
                System.arraycopy(tArr, 0, tArr2, 0, i);
                System.arraycopy(tArr, i + 1, tArr2, i, (length - i) - 1);
                return tArr2;
            }
        }
        return tArr;
    }
}
