package okio.internal;

import kotlin.jvm.internal.Intrinsics;
import okio.SegmentedByteString;
import org.jetbrains.annotations.NotNull;

/* compiled from: SegmentedByteString.kt */
public final class SegmentedByteStringKt {
    public static final int binarySearch(@NotNull int[] iArr, int i, int i2, int i3) {
        Intrinsics.checkNotNullParameter(iArr, "<this>");
        int i4 = i3 - 1;
        while (i2 <= i4) {
            int i5 = (i2 + i4) >>> 1;
            int i6 = iArr[i5];
            if (i6 < i) {
                i2 = i5 + 1;
            } else if (i6 <= i) {
                return i5;
            } else {
                i4 = i5 - 1;
            }
        }
        return (-i2) - 1;
    }

    public static final int segment(@NotNull SegmentedByteString segmentedByteString, int i) {
        Intrinsics.checkNotNullParameter(segmentedByteString, "<this>");
        int binarySearch = binarySearch(segmentedByteString.getDirectory$external__okio__android_common__okio_lib(), i + 1, 0, segmentedByteString.getSegments$external__okio__android_common__okio_lib().length);
        return binarySearch >= 0 ? binarySearch : ~binarySearch;
    }
}
