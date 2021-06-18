package okio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;
import kotlin.collections.AbstractList;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: Options.kt */
public final class Options extends AbstractList<ByteString> implements RandomAccess {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    private final ByteString[] byteStrings;
    @NotNull
    private final int[] trie;

    public /* synthetic */ Options(ByteString[] byteStringArr, int[] iArr, DefaultConstructorMarker defaultConstructorMarker) {
        this(byteStringArr, iArr);
    }

    @NotNull
    /* renamed from: of */
    public static final Options m31of(@NotNull ByteString... byteStringArr) {
        return Companion.mo22946of(byteStringArr);
    }

    public final /* bridge */ boolean contains(Object obj) {
        if (!(obj instanceof ByteString)) {
            return false;
        }
        return contains((ByteString) obj);
    }

    public /* bridge */ boolean contains(ByteString byteString) {
        return super.contains(byteString);
    }

    public final /* bridge */ int indexOf(Object obj) {
        if (!(obj instanceof ByteString)) {
            return -1;
        }
        return indexOf((ByteString) obj);
    }

    public /* bridge */ int indexOf(ByteString byteString) {
        return super.indexOf(byteString);
    }

    public final /* bridge */ int lastIndexOf(Object obj) {
        if (!(obj instanceof ByteString)) {
            return -1;
        }
        return lastIndexOf((ByteString) obj);
    }

    public /* bridge */ int lastIndexOf(ByteString byteString) {
        return super.lastIndexOf(byteString);
    }

    @NotNull
    public final ByteString[] getByteStrings$external__okio__android_common__okio_lib() {
        return this.byteStrings;
    }

    @NotNull
    public final int[] getTrie$external__okio__android_common__okio_lib() {
        return this.trie;
    }

    private Options(ByteString[] byteStringArr, int[] iArr) {
        this.byteStrings = byteStringArr;
        this.trie = iArr;
    }

    public int getSize() {
        return this.byteStrings.length;
    }

    @NotNull
    public ByteString get(int i) {
        return this.byteStrings[i];
    }

    /* compiled from: Options.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        @NotNull
        /* renamed from: of */
        public final Options mo22946of(@NotNull ByteString... byteStringArr) {
            ByteString[] byteStringArr2 = byteStringArr;
            Intrinsics.checkNotNullParameter(byteStringArr2, "byteStrings");
            int i = 0;
            if (byteStringArr2.length == 0) {
                return new Options(new ByteString[0], new int[]{0, -1}, (DefaultConstructorMarker) null);
            }
            List mutableList = ArraysKt___ArraysKt.toMutableList(byteStringArr);
            CollectionsKt__MutableCollectionsJVMKt.sort(mutableList);
            ArrayList arrayList = new ArrayList(byteStringArr2.length);
            for (ByteString byteString : byteStringArr2) {
                arrayList.add(-1);
            }
            Object[] array = arrayList.toArray(new Integer[0]);
            Objects.requireNonNull(array, "null cannot be cast to non-null type kotlin.Array<T>");
            Integer[] numArr = (Integer[]) array;
            Integer[] numArr2 = new Integer[numArr.length];
            System.arraycopy(numArr, 0, numArr2, 0, numArr.length);
            List mutableListOf = CollectionsKt__CollectionsKt.mutableListOf(numArr2);
            int length = byteStringArr2.length;
            int i2 = 0;
            int i3 = 0;
            while (i2 < length) {
                mutableListOf.set(CollectionsKt__CollectionsKt.binarySearch$default(mutableList, byteStringArr2[i2], 0, 0, 6, (Object) null), Integer.valueOf(i3));
                i2++;
                i3++;
            }
            if (((ByteString) mutableList.get(0)).size() > 0) {
                int i4 = 0;
                while (i4 < mutableList.size()) {
                    ByteString byteString2 = (ByteString) mutableList.get(i4);
                    int i5 = i4 + 1;
                    int i6 = i5;
                    while (i6 < mutableList.size()) {
                        ByteString byteString3 = (ByteString) mutableList.get(i6);
                        if (!byteString3.startsWith(byteString2)) {
                            continue;
                            break;
                        }
                        if (!(byteString3.size() != byteString2.size())) {
                            throw new IllegalArgumentException(Intrinsics.stringPlus("duplicate option: ", byteString3).toString());
                        } else if (((Number) mutableListOf.get(i6)).intValue() > ((Number) mutableListOf.get(i4)).intValue()) {
                            mutableList.remove(i6);
                            mutableListOf.remove(i6);
                        } else {
                            i6++;
                        }
                    }
                    i4 = i5;
                }
                Buffer buffer = new Buffer();
                buildTrieRecursive$default(this, 0, buffer, 0, mutableList, 0, 0, mutableListOf, 53, (Object) null);
                int[] iArr = new int[((int) getIntCount(buffer))];
                while (!buffer.exhausted()) {
                    iArr[i] = buffer.readInt();
                    i++;
                }
                Object[] copyOf = Arrays.copyOf(byteStringArr2, byteStringArr2.length);
                Intrinsics.checkNotNullExpressionValue(copyOf, "java.util.Arrays.copyOf(this, size)");
                return new Options((ByteString[]) copyOf, iArr, (DefaultConstructorMarker) null);
            }
            throw new IllegalArgumentException("the empty byte string is not a supported option".toString());
        }

        static /* synthetic */ void buildTrieRecursive$default(Companion companion, long j, Buffer buffer, int i, List list, int i2, int i3, List list2, int i4, Object obj) {
            int i5;
            long j2 = (i4 & 1) != 0 ? 0 : j;
            int i6 = (i4 & 4) != 0 ? 0 : i;
            int i7 = (i4 & 16) != 0 ? 0 : i2;
            if ((i4 & 32) != 0) {
                i5 = list.size();
            } else {
                i5 = i3;
            }
            companion.buildTrieRecursive(j2, buffer, i6, list, i7, i5, list2);
        }

        private final void buildTrieRecursive(long j, Buffer buffer, int i, List<? extends ByteString> list, int i2, int i3, List<Integer> list2) {
            int i4;
            int i5;
            int i6;
            int i7;
            int i8;
            Buffer buffer2;
            Buffer buffer3 = buffer;
            int i9 = i;
            List<? extends ByteString> list3 = list;
            int i10 = i2;
            int i11 = i3;
            List<Integer> list4 = list2;
            if (i10 < i11) {
                if (i10 < i11) {
                    int i12 = i10;
                    while (true) {
                        int i13 = i12 + 1;
                        if (!(((ByteString) list3.get(i12)).size() >= i9)) {
                            throw new IllegalArgumentException("Failed requirement.".toString());
                        } else if (i13 >= i11) {
                            break;
                        } else {
                            i12 = i13;
                        }
                    }
                }
                ByteString byteString = (ByteString) list.get(i2);
                ByteString byteString2 = (ByteString) list3.get(i11 - 1);
                int i14 = -1;
                if (i9 == byteString.size()) {
                    int intValue = list4.get(i10).intValue();
                    int i15 = i10 + 1;
                    i4 = i15;
                    i5 = intValue;
                    byteString = (ByteString) list3.get(i15);
                } else {
                    i4 = i10;
                    i5 = -1;
                }
                if (byteString.getByte(i9) != byteString2.getByte(i9)) {
                    int i16 = i4 + 1;
                    int i17 = 1;
                    if (i16 < i11) {
                        while (true) {
                            int i18 = i16 + 1;
                            if (((ByteString) list3.get(i16 - 1)).getByte(i9) != ((ByteString) list3.get(i16)).getByte(i9)) {
                                i17++;
                            }
                            if (i18 >= i11) {
                                break;
                            }
                            i16 = i18;
                        }
                    }
                    long intCount = j + getIntCount(buffer3) + ((long) 2) + ((long) (i17 * 2));
                    buffer3.writeInt(i17);
                    buffer3.writeInt(i5);
                    if (i4 < i11) {
                        int i19 = i4;
                        while (true) {
                            int i20 = i19 + 1;
                            byte b = ((ByteString) list3.get(i19)).getByte(i9);
                            if (i19 == i4 || b != ((ByteString) list3.get(i19 - 1)).getByte(i9)) {
                                buffer3.writeInt(b & 255);
                            }
                            if (i20 >= i11) {
                                break;
                            }
                            i19 = i20;
                        }
                    }
                    Buffer buffer4 = new Buffer();
                    while (i4 < i11) {
                        byte b2 = ((ByteString) list3.get(i4)).getByte(i9);
                        int i21 = i4 + 1;
                        if (i21 < i11) {
                            int i22 = i21;
                            while (true) {
                                int i23 = i22 + 1;
                                if (b2 != ((ByteString) list3.get(i22)).getByte(i9)) {
                                    i7 = i22;
                                    break;
                                } else if (i23 >= i11) {
                                    break;
                                } else {
                                    i22 = i23;
                                }
                            }
                        }
                        i7 = i11;
                        if (i21 == i7 && i9 + 1 == ((ByteString) list3.get(i4)).size()) {
                            buffer3.writeInt(list4.get(i4).intValue());
                            i8 = i7;
                            buffer2 = buffer4;
                        } else {
                            buffer3.writeInt(((int) (intCount + getIntCount(buffer4))) * i14);
                            i8 = i7;
                            buffer2 = buffer4;
                            buildTrieRecursive(intCount, buffer4, i9 + 1, list, i4, i7, list2);
                        }
                        buffer4 = buffer2;
                        i4 = i8;
                        i14 = -1;
                    }
                    buffer3.writeAll(buffer4);
                    return;
                }
                int min = Math.min(byteString.size(), byteString2.size());
                if (i9 < min) {
                    int i24 = i9;
                    i6 = 0;
                    while (true) {
                        int i25 = i24 + 1;
                        if (byteString.getByte(i24) != byteString2.getByte(i24)) {
                            break;
                        }
                        i6++;
                        if (i25 >= min) {
                            break;
                        }
                        i24 = i25;
                    }
                } else {
                    i6 = 0;
                }
                long intCount2 = j + getIntCount(buffer3) + ((long) 2) + ((long) i6) + 1;
                buffer3.writeInt(-i6);
                buffer3.writeInt(i5);
                int i26 = i9 + i6;
                if (i9 < i26) {
                    while (true) {
                        int i27 = i9 + 1;
                        buffer3.writeInt(byteString.getByte(i9) & 255);
                        if (i27 >= i26) {
                            break;
                        }
                        i9 = i27;
                    }
                }
                if (i4 + 1 == i11) {
                    if (i26 == ((ByteString) list3.get(i4)).size()) {
                        buffer3.writeInt(list4.get(i4).intValue());
                        return;
                    }
                    throw new IllegalStateException("Check failed.".toString());
                }
                Buffer buffer5 = new Buffer();
                buffer3.writeInt(((int) (getIntCount(buffer5) + intCount2)) * -1);
                buildTrieRecursive(intCount2, buffer5, i26, list, i4, i3, list2);
                buffer3.writeAll(buffer5);
                return;
            }
            throw new IllegalArgumentException("Failed requirement.".toString());
        }

        private final long getIntCount(Buffer buffer) {
            return buffer.size() / ((long) 4);
        }
    }
}
