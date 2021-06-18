package okio;

import kotlin.jvm.internal.Intrinsics;
import okio.internal.SegmentedByteStringKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: SegmentedByteString.kt */
public final class SegmentedByteString extends ByteString {
    @NotNull
    private final transient int[] directory;
    @NotNull
    private final transient byte[][] segments;

    @NotNull
    public final byte[][] getSegments$external__okio__android_common__okio_lib() {
        return this.segments;
    }

    @NotNull
    public final int[] getDirectory$external__okio__android_common__okio_lib() {
        return this.directory;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public SegmentedByteString(@NotNull byte[][] bArr, @NotNull int[] iArr) {
        super(ByteString.EMPTY.getData$external__okio__android_common__okio_lib());
        Intrinsics.checkNotNullParameter(bArr, "segments");
        Intrinsics.checkNotNullParameter(iArr, "directory");
        this.segments = bArr;
        this.directory = iArr;
    }

    @NotNull
    public String hex() {
        return toByteString().hex();
    }

    private final ByteString toByteString() {
        return new ByteString(toByteArray());
    }

    @NotNull
    public byte[] internalArray$external__okio__android_common__okio_lib() {
        return toByteArray();
    }

    @NotNull
    public String toString() {
        return toByteString().toString();
    }

    private final Object writeReplace() {
        return toByteString();
    }

    public byte internalGet$external__okio__android_common__okio_lib(int i) {
        int i2;
        Util.checkOffsetAndCount((long) getDirectory$external__okio__android_common__okio_lib()[getSegments$external__okio__android_common__okio_lib().length - 1], (long) i, 1);
        int segment = SegmentedByteStringKt.segment(this, i);
        if (segment == 0) {
            i2 = 0;
        } else {
            i2 = getDirectory$external__okio__android_common__okio_lib()[segment - 1];
        }
        return getSegments$external__okio__android_common__okio_lib()[segment][(i - i2) + getDirectory$external__okio__android_common__okio_lib()[getSegments$external__okio__android_common__okio_lib().length + segment]];
    }

    public int getSize$external__okio__android_common__okio_lib() {
        return getDirectory$external__okio__android_common__okio_lib()[getSegments$external__okio__android_common__okio_lib().length - 1];
    }

    @NotNull
    public byte[] toByteArray() {
        byte[] bArr = new byte[size()];
        int length = getSegments$external__okio__android_common__okio_lib().length;
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        while (i < length) {
            int i4 = getDirectory$external__okio__android_common__okio_lib()[length + i];
            int i5 = getDirectory$external__okio__android_common__okio_lib()[i];
            int i6 = i5 - i2;
            byte[] unused = ArraysKt___ArraysJvmKt.copyInto(getSegments$external__okio__android_common__okio_lib()[i], bArr, i3, i4, i4 + i6);
            i3 += i6;
            i++;
            i2 = i5;
        }
        return bArr;
    }

    public boolean rangeEquals(int i, @NotNull ByteString byteString, int i2, int i3) {
        int i4;
        Intrinsics.checkNotNullParameter(byteString, "other");
        if (i < 0 || i > size() - i3) {
            return false;
        }
        int i5 = i3 + i;
        int segment = SegmentedByteStringKt.segment(this, i);
        while (i < i5) {
            if (segment == 0) {
                i4 = 0;
            } else {
                i4 = getDirectory$external__okio__android_common__okio_lib()[segment - 1];
            }
            int i6 = getDirectory$external__okio__android_common__okio_lib()[getSegments$external__okio__android_common__okio_lib().length + segment];
            int min = Math.min(i5, (getDirectory$external__okio__android_common__okio_lib()[segment] - i4) + i4) - i;
            if (!byteString.rangeEquals(i2, getSegments$external__okio__android_common__okio_lib()[segment], i6 + (i - i4), min)) {
                return false;
            }
            i2 += min;
            i += min;
            segment++;
        }
        return true;
    }

    public boolean rangeEquals(int i, @NotNull byte[] bArr, int i2, int i3) {
        int i4;
        Intrinsics.checkNotNullParameter(bArr, "other");
        if (i < 0 || i > size() - i3 || i2 < 0 || i2 > bArr.length - i3) {
            return false;
        }
        int i5 = i3 + i;
        int segment = SegmentedByteStringKt.segment(this, i);
        while (i < i5) {
            if (segment == 0) {
                i4 = 0;
            } else {
                i4 = getDirectory$external__okio__android_common__okio_lib()[segment - 1];
            }
            int i6 = getDirectory$external__okio__android_common__okio_lib()[getSegments$external__okio__android_common__okio_lib().length + segment];
            int min = Math.min(i5, (getDirectory$external__okio__android_common__okio_lib()[segment] - i4) + i4) - i;
            if (!Util.arrayRangeEquals(getSegments$external__okio__android_common__okio_lib()[segment], i6 + (i - i4), bArr, i2, min)) {
                return false;
            }
            i2 += min;
            i += min;
            segment++;
        }
        return true;
    }

    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof ByteString) {
            ByteString byteString = (ByteString) obj;
            if (byteString.size() == size() && rangeEquals(0, byteString, 0, size())) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        int hashCode$external__okio__android_common__okio_lib = getHashCode$external__okio__android_common__okio_lib();
        if (hashCode$external__okio__android_common__okio_lib != 0) {
            return hashCode$external__okio__android_common__okio_lib;
        }
        int length = getSegments$external__okio__android_common__okio_lib().length;
        int i = 0;
        int i2 = 1;
        int i3 = 0;
        while (i < length) {
            int i4 = getDirectory$external__okio__android_common__okio_lib()[length + i];
            int i5 = getDirectory$external__okio__android_common__okio_lib()[i];
            byte[] bArr = getSegments$external__okio__android_common__okio_lib()[i];
            int i6 = (i5 - i3) + i4;
            while (i4 < i6) {
                i2 = (i2 * 31) + bArr[i4];
                i4++;
            }
            i++;
            i3 = i5;
        }
        setHashCode$external__okio__android_common__okio_lib(i2);
        return i2;
    }
}
