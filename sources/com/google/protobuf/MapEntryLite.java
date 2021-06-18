package com.google.protobuf;

import java.io.IOException;

public class MapEntryLite<K, V> {
    private final Metadata<K, V> metadata;

    static class Metadata<K, V> {
    }

    static <K, V> void writeTo(CodedOutputStream codedOutputStream, Metadata<K, V> metadata2, K k, V v) throws IOException {
        throw null;
    }

    static <K, V> int computeSerializedSize(Metadata<K, V> metadata2, K k, V v) {
        throw null;
    }

    public int computeMessageSize(int i, K k, V v) {
        return CodedOutputStream.computeTagSize(i) + CodedOutputStream.computeLengthDelimitedFieldSize(computeSerializedSize(this.metadata, k, v));
    }

    /* access modifiers changed from: package-private */
    public Metadata<K, V> getMetadata() {
        return this.metadata;
    }
}
