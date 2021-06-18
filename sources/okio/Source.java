package okio;

import java.io.Closeable;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;

/* compiled from: Source.kt */
public interface Source extends Closeable {
    void close() throws IOException;

    long read(@NotNull Buffer buffer, long j) throws IOException;
}
