package kotlin.jvm.internal;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.jetbrains.annotations.NotNull;

/* compiled from: ArrayIterator.kt */
final class ArrayIterator<T> implements Iterator<T> {
    @NotNull
    private final T[] array;
    private int index;

    public void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public ArrayIterator(@NotNull T[] tArr) {
        Intrinsics.checkNotNullParameter(tArr, "array");
        this.array = tArr;
    }

    public boolean hasNext() {
        return this.index < this.array.length;
    }

    public T next() {
        try {
            T[] tArr = this.array;
            int i = this.index;
            this.index = i + 1;
            return tArr[i];
        } catch (ArrayIndexOutOfBoundsException e) {
            this.index--;
            throw new NoSuchElementException(e.getMessage());
        }
    }
}
