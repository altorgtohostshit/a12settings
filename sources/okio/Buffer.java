package okio;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.charset.Charset;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import okio.internal.BufferKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: Buffer.kt */
public final class Buffer implements BufferedSource, BufferedSink, Cloneable, ByteChannel {
    @Nullable
    public Segment head;
    private long size;

    public void close() {
    }

    public void flush() {
    }

    @NotNull
    public Buffer getBuffer() {
        return this;
    }

    public boolean isOpen() {
        return true;
    }

    public final long size() {
        return this.size;
    }

    public final void setSize$external__okio__android_common__okio_lib(long j) {
        this.size = j;
    }

    public long indexOfElement(@NotNull ByteString byteString, long j) {
        long j2;
        int i;
        int i2;
        int i3;
        Intrinsics.checkNotNullParameter(byteString, "targetBytes");
        long j3 = 0;
        if (j >= 0) {
            Segment segment = this.head;
            if (segment == null) {
                return -1;
            }
            if (size() - j < j) {
                j2 = size();
                while (j2 > j) {
                    segment = segment.prev;
                    Intrinsics.checkNotNull(segment);
                    j2 -= (long) (segment.limit - segment.pos);
                }
                if (byteString.size() == 2) {
                    byte b = byteString.getByte(0);
                    byte b2 = byteString.getByte(1);
                    while (j2 < size()) {
                        byte[] bArr = segment.data;
                        i2 = (int) ((((long) segment.pos) + j) - j2);
                        int i4 = segment.limit;
                        while (i2 < i4) {
                            byte b3 = bArr[i2];
                            if (!(b3 == b || b3 == b2)) {
                                i2++;
                            }
                        }
                        j2 += (long) (segment.limit - segment.pos);
                        segment = segment.next;
                        Intrinsics.checkNotNull(segment);
                        j = j2;
                    }
                    return -1;
                }
                byte[] internalArray$external__okio__android_common__okio_lib = byteString.internalArray$external__okio__android_common__okio_lib();
                while (j2 < size()) {
                    byte[] bArr2 = segment.data;
                    i = (int) ((((long) segment.pos) + j) - j2);
                    int i5 = segment.limit;
                    while (i < i5) {
                        byte b4 = bArr2[i];
                        int length = internalArray$external__okio__android_common__okio_lib.length;
                        int i6 = 0;
                        while (i6 < length) {
                            byte b5 = internalArray$external__okio__android_common__okio_lib[i6];
                            i6++;
                            if (b4 == b5) {
                                i3 = segment.pos;
                                return ((long) (i2 - i3)) + j2;
                            }
                        }
                        i++;
                    }
                    j2 += (long) (segment.limit - segment.pos);
                    segment = segment.next;
                    Intrinsics.checkNotNull(segment);
                    j = j2;
                }
                return -1;
            }
            while (true) {
                long j4 = ((long) (segment.limit - segment.pos)) + j3;
                if (j4 > j) {
                    break;
                }
                segment = segment.next;
                Intrinsics.checkNotNull(segment);
                j3 = j4;
            }
            if (byteString.size() == 2) {
                byte b6 = byteString.getByte(0);
                byte b7 = byteString.getByte(1);
                while (j2 < size()) {
                    byte[] bArr3 = segment.data;
                    int i7 = (int) ((((long) segment.pos) + j) - j2);
                    int i8 = segment.limit;
                    while (i2 < i8) {
                        byte b8 = bArr3[i2];
                        if (!(b8 == b6 || b8 == b7)) {
                            i7 = i2 + 1;
                        }
                    }
                    j3 = j2 + ((long) (segment.limit - segment.pos));
                    segment = segment.next;
                    Intrinsics.checkNotNull(segment);
                    j = j3;
                }
                return -1;
            }
            byte[] internalArray$external__okio__android_common__okio_lib2 = byteString.internalArray$external__okio__android_common__okio_lib();
            while (j2 < size()) {
                byte[] bArr4 = segment.data;
                int i9 = (int) ((((long) segment.pos) + j) - j2);
                int i10 = segment.limit;
                while (i < i10) {
                    byte b9 = bArr4[i];
                    int length2 = internalArray$external__okio__android_common__okio_lib2.length;
                    int i11 = 0;
                    while (i11 < length2) {
                        byte b10 = internalArray$external__okio__android_common__okio_lib2[i11];
                        i11++;
                        if (b9 == b10) {
                            i3 = segment.pos;
                            return ((long) (i2 - i3)) + j2;
                        }
                    }
                    i9 = i + 1;
                }
                j3 = j2 + ((long) (segment.limit - segment.pos));
                segment = segment.next;
                Intrinsics.checkNotNull(segment);
                j = j3;
            }
            return -1;
            i3 = segment.pos;
            return ((long) (i2 - i3)) + j2;
        }
        throw new IllegalArgumentException(Intrinsics.stringPlus("fromIndex < 0: ", Long.valueOf(j)).toString());
    }

    public boolean exhausted() {
        return this.size == 0;
    }

    public boolean request(long j) {
        return this.size >= j;
    }

    public byte readByte() throws EOFException {
        if (size() != 0) {
            Segment segment = this.head;
            Intrinsics.checkNotNull(segment);
            int i = segment.pos;
            int i2 = segment.limit;
            int i3 = i + 1;
            byte b = segment.data[i];
            setSize$external__okio__android_common__okio_lib(size() - 1);
            if (i3 == i2) {
                this.head = segment.pop();
                SegmentPool segmentPool = SegmentPool.INSTANCE;
                SegmentPool.recycle(segment);
            } else {
                segment.pos = i3;
            }
            return b;
        }
        throw new EOFException();
    }

    @NotNull
    public String readUtf8() {
        return readString(this.size, Charsets.UTF_8);
    }

    @NotNull
    public String readUtf8(long j) throws EOFException {
        return readString(j, Charsets.UTF_8);
    }

    @NotNull
    public String readString(long j, @NotNull Charset charset) throws EOFException {
        Intrinsics.checkNotNullParameter(charset, "charset");
        int i = (j > 0 ? 1 : (j == 0 ? 0 : -1));
        if (!(i >= 0 && j <= 2147483647L)) {
            throw new IllegalArgumentException(Intrinsics.stringPlus("byteCount: ", Long.valueOf(j)).toString());
        } else if (this.size < j) {
            throw new EOFException();
        } else if (i == 0) {
            return "";
        } else {
            Segment segment = this.head;
            Intrinsics.checkNotNull(segment);
            int i2 = segment.pos;
            if (((long) i2) + j > ((long) segment.limit)) {
                return new String(readByteArray(j), charset);
            }
            int i3 = (int) j;
            String str = new String(segment.data, i2, i3, charset);
            int i4 = segment.pos + i3;
            segment.pos = i4;
            this.size -= j;
            if (i4 == segment.limit) {
                this.head = segment.pop();
                SegmentPool segmentPool = SegmentPool.INSTANCE;
                SegmentPool.recycle(segment);
            }
            return str;
        }
    }

    public int readInt() throws EOFException {
        if (size() >= 4) {
            Segment segment = this.head;
            Intrinsics.checkNotNull(segment);
            int i = segment.pos;
            int i2 = segment.limit;
            if (((long) (i2 - i)) < 4) {
                return (readByte() & 255) | ((readByte() & 255) << 24) | ((readByte() & 255) << 16) | ((readByte() & 255) << 8);
            }
            byte[] bArr = segment.data;
            int i3 = i + 1;
            int i4 = i3 + 1;
            byte b = ((bArr[i] & 255) << 24) | ((bArr[i3] & 255) << 16);
            int i5 = i4 + 1;
            byte b2 = b | ((bArr[i4] & 255) << 8);
            int i6 = i5 + 1;
            byte b3 = b2 | (bArr[i5] & 255);
            setSize$external__okio__android_common__okio_lib(size() - 4);
            if (i6 == i2) {
                this.head = segment.pop();
                SegmentPool segmentPool = SegmentPool.INSTANCE;
                SegmentPool.recycle(segment);
            } else {
                segment.pos = i6;
            }
            return b3;
        }
        throw new EOFException();
    }

    public int read(@NotNull ByteBuffer byteBuffer) throws IOException {
        Intrinsics.checkNotNullParameter(byteBuffer, "sink");
        Segment segment = this.head;
        if (segment == null) {
            return -1;
        }
        int min = Math.min(byteBuffer.remaining(), segment.limit - segment.pos);
        byteBuffer.put(segment.data, segment.pos, min);
        int i = segment.pos + min;
        segment.pos = i;
        this.size -= (long) min;
        if (i == segment.limit) {
            this.head = segment.pop();
            SegmentPool segmentPool = SegmentPool.INSTANCE;
            SegmentPool.recycle(segment);
        }
        return min;
    }

    @NotNull
    public Buffer writeUtf8(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "string");
        return writeUtf8(str, 0, str.length());
    }

    public final byte getByte(long j) {
        Util.checkOffsetAndCount(size(), j, 1);
        Segment segment = this.head;
        if (segment == null) {
            Intrinsics.checkNotNull((Object) null);
            throw null;
        } else if (size() - j < j) {
            long size2 = size();
            while (size2 > j) {
                segment = segment.prev;
                Intrinsics.checkNotNull(segment);
                size2 -= (long) (segment.limit - segment.pos);
            }
            return segment.data[(int) ((((long) segment.pos) + j) - size2)];
        } else {
            long j2 = 0;
            while (true) {
                int i = segment.limit;
                int i2 = segment.pos;
                long j3 = ((long) (i - i2)) + j2;
                if (j3 > j) {
                    return segment.data[(int) ((((long) i2) + j) - j2)];
                }
                segment = segment.next;
                Intrinsics.checkNotNull(segment);
                j2 = j3;
            }
        }
    }

    public final void clear() {
        skip(size());
    }

    public int write(@NotNull ByteBuffer byteBuffer) throws IOException {
        Intrinsics.checkNotNullParameter(byteBuffer, "source");
        int remaining = byteBuffer.remaining();
        int i = remaining;
        while (i > 0) {
            Segment writableSegment$external__okio__android_common__okio_lib = writableSegment$external__okio__android_common__okio_lib(1);
            int min = Math.min(i, 8192 - writableSegment$external__okio__android_common__okio_lib.limit);
            byteBuffer.get(writableSegment$external__okio__android_common__okio_lib.data, writableSegment$external__okio__android_common__okio_lib.limit, min);
            i -= min;
            writableSegment$external__okio__android_common__okio_lib.limit += min;
        }
        this.size += (long) remaining;
        return remaining;
    }

    public void skip(long j) throws EOFException {
        while (j > 0) {
            Segment segment = this.head;
            if (segment != null) {
                int min = (int) Math.min(j, (long) (segment.limit - segment.pos));
                long j2 = (long) min;
                setSize$external__okio__android_common__okio_lib(size() - j2);
                j -= j2;
                int i = segment.pos + min;
                segment.pos = i;
                if (i == segment.limit) {
                    this.head = segment.pop();
                    SegmentPool segmentPool = SegmentPool.INSTANCE;
                    SegmentPool.recycle(segment);
                }
            } else {
                throw new EOFException();
            }
        }
    }

    public long indexOf(@NotNull ByteString byteString) throws IOException {
        Intrinsics.checkNotNullParameter(byteString, "bytes");
        return indexOf(byteString, 0);
    }

    public long indexOfElement(@NotNull ByteString byteString) {
        Intrinsics.checkNotNullParameter(byteString, "targetBytes");
        return indexOfElement(byteString, 0);
    }

    @NotNull
    public final Segment writableSegment$external__okio__android_common__okio_lib(int i) {
        boolean z = true;
        if (i < 1 || i > 8192) {
            z = false;
        }
        if (z) {
            Segment segment = this.head;
            if (segment == null) {
                SegmentPool segmentPool = SegmentPool.INSTANCE;
                Segment take = SegmentPool.take();
                this.head = take;
                take.prev = take;
                take.next = take;
                return take;
            }
            Intrinsics.checkNotNull(segment);
            Segment segment2 = segment.prev;
            Intrinsics.checkNotNull(segment2);
            if (segment2.limit + i > 8192 || !segment2.owner) {
                SegmentPool segmentPool2 = SegmentPool.INSTANCE;
                segment2 = segment2.push(SegmentPool.take());
            }
            return segment2;
        }
        throw new IllegalArgumentException("unexpected capacity".toString());
    }

    @NotNull
    public String toString() {
        return snapshot().toString();
    }

    @NotNull
    public Buffer clone() {
        return copy();
    }

    @NotNull
    public byte[] readByteArray(long j) throws EOFException {
        if (!(j >= 0 && j <= 2147483647L)) {
            throw new IllegalArgumentException(Intrinsics.stringPlus("byteCount: ", Long.valueOf(j)).toString());
        } else if (size() >= j) {
            byte[] bArr = new byte[((int) j)];
            readFully(bArr);
            return bArr;
        } else {
            throw new EOFException();
        }
    }

    public void readFully(@NotNull byte[] bArr) throws EOFException {
        Intrinsics.checkNotNullParameter(bArr, "sink");
        int i = 0;
        while (i < bArr.length) {
            int read = read(bArr, i, bArr.length - i);
            if (read != -1) {
                i += read;
            } else {
                throw new EOFException();
            }
        }
    }

    public int read(@NotNull byte[] bArr, int i, int i2) {
        Intrinsics.checkNotNullParameter(bArr, "sink");
        Util.checkOffsetAndCount((long) bArr.length, (long) i, (long) i2);
        Segment segment = this.head;
        if (segment == null) {
            return -1;
        }
        int min = Math.min(i2, segment.limit - segment.pos);
        byte[] bArr2 = segment.data;
        int i3 = segment.pos;
        byte[] unused = ArraysKt___ArraysJvmKt.copyInto(bArr2, bArr, i, i3, i3 + min);
        segment.pos += min;
        setSize$external__okio__android_common__okio_lib(size() - ((long) min));
        if (segment.pos == segment.limit) {
            this.head = segment.pop();
            SegmentPool segmentPool = SegmentPool.INSTANCE;
            SegmentPool.recycle(segment);
        }
        return min;
    }

    @NotNull
    public ByteString readByteString() {
        return readByteString(size());
    }

    @NotNull
    public ByteString readByteString(long j) throws EOFException {
        if (!(j >= 0 && j <= 2147483647L)) {
            throw new IllegalArgumentException(Intrinsics.stringPlus("byteCount: ", Long.valueOf(j)).toString());
        } else if (size() < j) {
            throw new EOFException();
        } else if (j < 4096) {
            return new ByteString(readByteArray(j));
        } else {
            ByteString snapshot = snapshot((int) j);
            skip(j);
            return snapshot;
        }
    }

    public int select(@NotNull Options options) {
        Intrinsics.checkNotNullParameter(options, "options");
        int selectPrefix$default = BufferKt.selectPrefix$default(this, options, false, 2, (Object) null);
        if (selectPrefix$default == -1) {
            return -1;
        }
        skip((long) options.getByteStrings$external__okio__android_common__okio_lib()[selectPrefix$default].size());
        return selectPrefix$default;
    }

    @NotNull
    public Buffer writeUtf8(@NotNull String str, int i, int i2) {
        char c;
        char charAt;
        Intrinsics.checkNotNullParameter(str, "string");
        if (i >= 0) {
            if (i2 >= i) {
                if (i2 <= str.length()) {
                    while (i < i2) {
                        char charAt2 = str.charAt(i);
                        if (charAt2 < 128) {
                            Segment writableSegment$external__okio__android_common__okio_lib = writableSegment$external__okio__android_common__okio_lib(1);
                            byte[] bArr = writableSegment$external__okio__android_common__okio_lib.data;
                            int i3 = writableSegment$external__okio__android_common__okio_lib.limit - i;
                            int min = Math.min(i2, 8192 - i3);
                            int i4 = i + 1;
                            bArr[i + i3] = (byte) charAt2;
                            while (true) {
                                i = i4;
                                if (i >= min || (charAt = str.charAt(i)) >= 128) {
                                    int i5 = writableSegment$external__okio__android_common__okio_lib.limit;
                                    int i6 = (i3 + i) - i5;
                                    writableSegment$external__okio__android_common__okio_lib.limit = i5 + i6;
                                    setSize$external__okio__android_common__okio_lib(size() + ((long) i6));
                                } else {
                                    i4 = i + 1;
                                    bArr[i + i3] = (byte) charAt;
                                }
                            }
                            int i52 = writableSegment$external__okio__android_common__okio_lib.limit;
                            int i62 = (i3 + i) - i52;
                            writableSegment$external__okio__android_common__okio_lib.limit = i52 + i62;
                            setSize$external__okio__android_common__okio_lib(size() + ((long) i62));
                        } else {
                            if (charAt2 < 2048) {
                                Segment writableSegment$external__okio__android_common__okio_lib2 = writableSegment$external__okio__android_common__okio_lib(2);
                                byte[] bArr2 = writableSegment$external__okio__android_common__okio_lib2.data;
                                int i7 = writableSegment$external__okio__android_common__okio_lib2.limit;
                                bArr2[i7] = (byte) ((charAt2 >> 6) | 192);
                                bArr2[i7 + 1] = (byte) ((charAt2 & '?') | 128);
                                writableSegment$external__okio__android_common__okio_lib2.limit = i7 + 2;
                                setSize$external__okio__android_common__okio_lib(size() + 2);
                            } else if (charAt2 < 55296 || charAt2 > 57343) {
                                Segment writableSegment$external__okio__android_common__okio_lib3 = writableSegment$external__okio__android_common__okio_lib(3);
                                byte[] bArr3 = writableSegment$external__okio__android_common__okio_lib3.data;
                                int i8 = writableSegment$external__okio__android_common__okio_lib3.limit;
                                bArr3[i8] = (byte) ((charAt2 >> 12) | 224);
                                bArr3[i8 + 1] = (byte) ((63 & (charAt2 >> 6)) | 128);
                                bArr3[i8 + 2] = (byte) ((charAt2 & '?') | 128);
                                writableSegment$external__okio__android_common__okio_lib3.limit = i8 + 3;
                                setSize$external__okio__android_common__okio_lib(size() + 3);
                            } else {
                                int i9 = i + 1;
                                if (i9 < i2) {
                                    c = str.charAt(i9);
                                } else {
                                    c = 0;
                                }
                                if (charAt2 <= 56319) {
                                    if (56320 <= c && c <= 57343) {
                                        int i10 = (((charAt2 & 1023) << 10) | (c & 1023)) + 0;
                                        Segment writableSegment$external__okio__android_common__okio_lib4 = writableSegment$external__okio__android_common__okio_lib(4);
                                        byte[] bArr4 = writableSegment$external__okio__android_common__okio_lib4.data;
                                        int i11 = writableSegment$external__okio__android_common__okio_lib4.limit;
                                        bArr4[i11] = (byte) ((i10 >> 18) | 240);
                                        bArr4[i11 + 1] = (byte) (((i10 >> 12) & 63) | 128);
                                        bArr4[i11 + 2] = (byte) (((i10 >> 6) & 63) | 128);
                                        bArr4[i11 + 3] = (byte) ((i10 & 63) | 128);
                                        writableSegment$external__okio__android_common__okio_lib4.limit = i11 + 4;
                                        setSize$external__okio__android_common__okio_lib(size() + 4);
                                        i += 2;
                                    }
                                }
                                writeByte(63);
                                i = i9;
                            }
                            i++;
                        }
                    }
                    return this;
                }
                throw new IllegalArgumentException(("endIndex > string.length: " + i2 + " > " + str.length()).toString());
            }
            throw new IllegalArgumentException(("endIndex < beginIndex: " + i2 + " < " + i).toString());
        }
        throw new IllegalArgumentException(Intrinsics.stringPlus("beginIndex < 0: ", Integer.valueOf(i)).toString());
    }

    public long writeAll(@NotNull Source source) throws IOException {
        Intrinsics.checkNotNullParameter(source, "source");
        long j = 0;
        while (true) {
            long read = source.read(this, 8192);
            if (read == -1) {
                return j;
            }
            j += read;
        }
    }

    @NotNull
    public Buffer writeByte(int i) {
        Segment writableSegment$external__okio__android_common__okio_lib = writableSegment$external__okio__android_common__okio_lib(1);
        byte[] bArr = writableSegment$external__okio__android_common__okio_lib.data;
        int i2 = writableSegment$external__okio__android_common__okio_lib.limit;
        writableSegment$external__okio__android_common__okio_lib.limit = i2 + 1;
        bArr[i2] = (byte) i;
        setSize$external__okio__android_common__okio_lib(size() + 1);
        return this;
    }

    @NotNull
    public Buffer writeInt(int i) {
        Segment writableSegment$external__okio__android_common__okio_lib = writableSegment$external__okio__android_common__okio_lib(4);
        byte[] bArr = writableSegment$external__okio__android_common__okio_lib.data;
        int i2 = writableSegment$external__okio__android_common__okio_lib.limit;
        int i3 = i2 + 1;
        bArr[i2] = (byte) ((i >>> 24) & 255);
        int i4 = i3 + 1;
        bArr[i3] = (byte) ((i >>> 16) & 255);
        int i5 = i4 + 1;
        bArr[i4] = (byte) ((i >>> 8) & 255);
        bArr[i5] = (byte) (i & 255);
        writableSegment$external__okio__android_common__okio_lib.limit = i5 + 1;
        setSize$external__okio__android_common__okio_lib(size() + 4);
        return this;
    }

    public void write(@NotNull Buffer buffer, long j) {
        Segment segment;
        Intrinsics.checkNotNullParameter(buffer, "source");
        if (buffer != this) {
            Util.checkOffsetAndCount(buffer.size(), 0, j);
            while (j > 0) {
                Segment segment2 = buffer.head;
                Intrinsics.checkNotNull(segment2);
                int i = segment2.limit;
                Segment segment3 = buffer.head;
                Intrinsics.checkNotNull(segment3);
                if (j < ((long) (i - segment3.pos))) {
                    Segment segment4 = this.head;
                    if (segment4 != null) {
                        Intrinsics.checkNotNull(segment4);
                        segment = segment4.prev;
                    } else {
                        segment = null;
                    }
                    if (segment != null && segment.owner) {
                        if ((((long) segment.limit) + j) - ((long) (segment.shared ? 0 : segment.pos)) <= 8192) {
                            Segment segment5 = buffer.head;
                            Intrinsics.checkNotNull(segment5);
                            segment5.writeTo(segment, (int) j);
                            buffer.setSize$external__okio__android_common__okio_lib(buffer.size() - j);
                            setSize$external__okio__android_common__okio_lib(size() + j);
                            return;
                        }
                    }
                    Segment segment6 = buffer.head;
                    Intrinsics.checkNotNull(segment6);
                    buffer.head = segment6.split((int) j);
                }
                Segment segment7 = buffer.head;
                Intrinsics.checkNotNull(segment7);
                long j2 = (long) (segment7.limit - segment7.pos);
                buffer.head = segment7.pop();
                Segment segment8 = this.head;
                if (segment8 == null) {
                    this.head = segment7;
                    segment7.prev = segment7;
                    segment7.next = segment7;
                } else {
                    Intrinsics.checkNotNull(segment8);
                    Segment segment9 = segment8.prev;
                    Intrinsics.checkNotNull(segment9);
                    segment9.push(segment7).compact();
                }
                buffer.setSize$external__okio__android_common__okio_lib(buffer.size() - j2);
                setSize$external__okio__android_common__okio_lib(size() + j2);
                j -= j2;
            }
            return;
        }
        throw new IllegalArgumentException("source == this".toString());
    }

    public long read(@NotNull Buffer buffer, long j) {
        Intrinsics.checkNotNullParameter(buffer, "sink");
        if (!(j >= 0)) {
            throw new IllegalArgumentException(Intrinsics.stringPlus("byteCount < 0: ", Long.valueOf(j)).toString());
        } else if (size() == 0) {
            return -1;
        } else {
            if (j > size()) {
                j = size();
            }
            buffer.write(this, j);
            return j;
        }
    }

    public long indexOf(@NotNull ByteString byteString, long j) throws IOException {
        long j2;
        boolean z;
        int i;
        Intrinsics.checkNotNullParameter(byteString, "bytes");
        if (byteString.size() > 0) {
            long j3 = 0;
            if (j >= 0) {
                Segment segment = this.head;
                if (segment != null) {
                    if (size() - j < j) {
                        long size2 = size();
                        while (size2 > j) {
                            segment = segment.prev;
                            Intrinsics.checkNotNull(segment);
                            size2 -= (long) (segment.limit - segment.pos);
                        }
                        byte[] internalArray$external__okio__android_common__okio_lib = byteString.internalArray$external__okio__android_common__okio_lib();
                        byte b = internalArray$external__okio__android_common__okio_lib[0];
                        int size3 = byteString.size();
                        long size4 = (size() - ((long) size3)) + 1;
                        j2 = size2;
                        long j4 = j;
                        loop1:
                        while (j2 < size4) {
                            byte[] bArr = segment.data;
                            int min = (int) Math.min((long) segment.limit, (((long) segment.pos) + size4) - j2);
                            i = (int) ((((long) segment.pos) + j4) - j2);
                            if (i < min) {
                                while (true) {
                                    int i2 = i + 1;
                                    if (bArr[i] == b && BufferKt.rangeEquals(segment, i2, internalArray$external__okio__android_common__okio_lib, 1, size3)) {
                                        break loop1;
                                    } else if (i2 >= min) {
                                        break;
                                    } else {
                                        i = i2;
                                    }
                                }
                            }
                            j2 += (long) (segment.limit - segment.pos);
                            segment = segment.next;
                            Intrinsics.checkNotNull(segment);
                            j4 = j2;
                        }
                    } else {
                        while (true) {
                            long j5 = ((long) (segment.limit - segment.pos)) + j3;
                            if (j5 > j) {
                                break;
                            }
                            segment = segment.next;
                            Intrinsics.checkNotNull(segment);
                            j3 = j5;
                        }
                        byte[] internalArray$external__okio__android_common__okio_lib2 = byteString.internalArray$external__okio__android_common__okio_lib();
                        byte b2 = internalArray$external__okio__android_common__okio_lib2[0];
                        int size5 = byteString.size();
                        long size6 = (size() - ((long) size5)) + 1;
                        long j6 = j3;
                        long j7 = j;
                        loop4:
                        while (j2 < size6) {
                            byte[] bArr2 = segment.data;
                            int min2 = (int) Math.min((long) segment.limit, (((long) segment.pos) + size6) - j2);
                            int i3 = (int) ((((long) segment.pos) + j7) - j2);
                            if (i3 < min2) {
                                while (true) {
                                    int i4 = i + 1;
                                    if (bArr2[i] == b2) {
                                        z = true;
                                        if (BufferKt.rangeEquals(segment, i4, internalArray$external__okio__android_common__okio_lib2, 1, size5)) {
                                            break loop4;
                                        }
                                    } else {
                                        z = true;
                                    }
                                    if (i4 >= min2) {
                                        break;
                                    }
                                    i3 = i4;
                                }
                            } else {
                                z = true;
                            }
                            j6 = j2 + ((long) (segment.limit - segment.pos));
                            segment = segment.next;
                            Intrinsics.checkNotNull(segment);
                            j7 = j6;
                            boolean z2 = z;
                        }
                    }
                    return ((long) (i - segment.pos)) + j2;
                }
                return -1;
            }
            throw new IllegalArgumentException(Intrinsics.stringPlus("fromIndex < 0: ", Long.valueOf(j)).toString());
        }
        throw new IllegalArgumentException("bytes is empty".toString());
    }

    /* JADX WARNING: type inference failed for: r19v0, types: [java.lang.Object] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(@org.jetbrains.annotations.Nullable java.lang.Object r19) {
        /*
            r18 = this;
            r0 = r18
            r1 = r19
            r2 = 0
            r3 = 1
            if (r0 != r1) goto L_0x000b
        L_0x0008:
            r2 = r3
            goto L_0x008b
        L_0x000b:
            boolean r4 = r1 instanceof okio.Buffer
            if (r4 != 0) goto L_0x0011
            goto L_0x008b
        L_0x0011:
            long r4 = r18.size()
            okio.Buffer r1 = (okio.Buffer) r1
            long r6 = r1.size()
            int r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r4 == 0) goto L_0x0021
            goto L_0x008b
        L_0x0021:
            long r4 = r18.size()
            r6 = 0
            int r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r4 != 0) goto L_0x002c
            goto L_0x0008
        L_0x002c:
            okio.Segment r4 = r0.head
            kotlin.jvm.internal.Intrinsics.checkNotNull(r4)
            okio.Segment r1 = r1.head
            kotlin.jvm.internal.Intrinsics.checkNotNull(r1)
            int r5 = r4.pos
            int r8 = r1.pos
            r9 = r6
        L_0x003b:
            long r11 = r18.size()
            int r11 = (r9 > r11 ? 1 : (r9 == r11 ? 0 : -1))
            if (r11 >= 0) goto L_0x0008
            int r11 = r4.limit
            int r11 = r11 - r5
            int r12 = r1.limit
            int r12 = r12 - r8
            int r11 = java.lang.Math.min(r11, r12)
            long r11 = (long) r11
            int r13 = (r6 > r11 ? 1 : (r6 == r11 ? 0 : -1))
            if (r13 >= 0) goto L_0x0073
            r13 = r6
        L_0x0053:
            r15 = 1
            long r13 = r13 + r15
            byte[] r15 = r4.data
            int r16 = r5 + 1
            byte r5 = r15[r5]
            byte[] r15 = r1.data
            int r17 = r8 + 1
            byte r8 = r15[r8]
            if (r5 == r8) goto L_0x0065
            goto L_0x008b
        L_0x0065:
            int r5 = (r13 > r11 ? 1 : (r13 == r11 ? 0 : -1))
            if (r5 < 0) goto L_0x006e
            r5 = r16
            r8 = r17
            goto L_0x0073
        L_0x006e:
            r5 = r16
            r8 = r17
            goto L_0x0053
        L_0x0073:
            int r13 = r4.limit
            if (r5 != r13) goto L_0x007e
            okio.Segment r4 = r4.next
            kotlin.jvm.internal.Intrinsics.checkNotNull(r4)
            int r5 = r4.pos
        L_0x007e:
            int r13 = r1.limit
            if (r8 != r13) goto L_0x0089
            okio.Segment r1 = r1.next
            kotlin.jvm.internal.Intrinsics.checkNotNull(r1)
            int r8 = r1.pos
        L_0x0089:
            long r9 = r9 + r11
            goto L_0x003b
        L_0x008b:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.Buffer.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        Segment segment = this.head;
        if (segment == null) {
            return 0;
        }
        int i = 1;
        do {
            int i2 = segment.limit;
            for (int i3 = segment.pos; i3 < i2; i3++) {
                i = (i * 31) + segment.data[i3];
            }
            segment = segment.next;
            Intrinsics.checkNotNull(segment);
        } while (segment != this.head);
        return i;
    }

    @NotNull
    public final Buffer copy() {
        Buffer buffer = new Buffer();
        if (size() != 0) {
            Segment segment = this.head;
            Intrinsics.checkNotNull(segment);
            Segment sharedCopy = segment.sharedCopy();
            buffer.head = sharedCopy;
            sharedCopy.prev = sharedCopy;
            sharedCopy.next = sharedCopy;
            for (Segment segment2 = segment.next; segment2 != segment; segment2 = segment2.next) {
                Segment segment3 = sharedCopy.prev;
                Intrinsics.checkNotNull(segment3);
                Intrinsics.checkNotNull(segment2);
                segment3.push(segment2.sharedCopy());
            }
            buffer.setSize$external__okio__android_common__okio_lib(size());
        }
        return buffer;
    }

    @NotNull
    public final ByteString snapshot() {
        if (size() <= 2147483647L) {
            return snapshot((int) size());
        }
        throw new IllegalStateException(Intrinsics.stringPlus("size > Int.MAX_VALUE: ", Long.valueOf(size())).toString());
    }

    @NotNull
    public final ByteString snapshot(int i) {
        if (i == 0) {
            return ByteString.EMPTY;
        }
        Util.checkOffsetAndCount(size(), 0, (long) i);
        Segment segment = this.head;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        while (i3 < i) {
            Intrinsics.checkNotNull(segment);
            int i5 = segment.limit;
            int i6 = segment.pos;
            if (i5 != i6) {
                i3 += i5 - i6;
                i4++;
                segment = segment.next;
            } else {
                throw new AssertionError("s.limit == s.pos");
            }
        }
        byte[][] bArr = new byte[i4][];
        int[] iArr = new int[(i4 * 2)];
        Segment segment2 = this.head;
        int i7 = 0;
        while (i2 < i) {
            Intrinsics.checkNotNull(segment2);
            bArr[i7] = segment2.data;
            i2 += segment2.limit - segment2.pos;
            iArr[i7] = Math.min(i2, i);
            iArr[i7 + i4] = segment2.pos;
            segment2.shared = true;
            i7++;
            segment2 = segment2.next;
        }
        return new SegmentedByteString(bArr, iArr);
    }
}
