package okio;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import org.jetbrains.annotations.NotNull;

/* compiled from: BufferedSource.kt */
public interface BufferedSource extends Source, ReadableByteChannel {
    @NotNull
    Buffer getBuffer();

    long indexOf(@NotNull ByteString byteString) throws IOException;

    long indexOfElement(@NotNull ByteString byteString) throws IOException;

    boolean request(long j) throws IOException;

    int select(@NotNull Options options) throws IOException;
}
