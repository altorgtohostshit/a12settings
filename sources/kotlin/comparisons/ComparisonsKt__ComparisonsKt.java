package kotlin.comparisons;

import org.jetbrains.annotations.Nullable;

/* compiled from: Comparisons.kt */
class ComparisonsKt__ComparisonsKt {
    public static <T extends Comparable<?>> int compareValues(@Nullable T t, @Nullable T t2) {
        if (t == t2) {
            return 0;
        }
        if (t == null) {
            return -1;
        }
        if (t2 == null) {
            return 1;
        }
        return t.compareTo(t2);
    }
}
