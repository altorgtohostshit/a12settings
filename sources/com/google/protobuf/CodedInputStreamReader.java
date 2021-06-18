package com.google.protobuf;

import com.google.protobuf.MapEntryLite;
import java.io.IOException;
import java.util.List;
import java.util.Map;

final class CodedInputStreamReader implements Reader {
    private int endGroupTag;
    private final CodedInputStream input;
    private int nextTag = 0;
    private int tag;

    public static CodedInputStreamReader forCodedInput(CodedInputStream codedInputStream) {
        CodedInputStreamReader codedInputStreamReader = codedInputStream.wrapper;
        if (codedInputStreamReader != null) {
            return codedInputStreamReader;
        }
        return new CodedInputStreamReader(codedInputStream);
    }

    private CodedInputStreamReader(CodedInputStream codedInputStream) {
        CodedInputStream codedInputStream2 = (CodedInputStream) Internal.checkNotNull(codedInputStream, "input");
        this.input = codedInputStream2;
        codedInputStream2.wrapper = this;
    }

    public int getFieldNumber() throws IOException {
        int i = this.nextTag;
        if (i != 0) {
            this.tag = i;
            this.nextTag = 0;
        } else {
            this.tag = this.input.readTag();
        }
        int i2 = this.tag;
        if (i2 == 0 || i2 == this.endGroupTag) {
            return Integer.MAX_VALUE;
        }
        return WireFormat.getTagFieldNumber(i2);
    }

    public int getTag() {
        return this.tag;
    }

    public boolean skipField() throws IOException {
        int i;
        if (this.input.isAtEnd() || (i = this.tag) == this.endGroupTag) {
            return false;
        }
        return this.input.skipField(i);
    }

    private void requireWireType(int i) throws IOException {
        if (WireFormat.getTagWireType(this.tag) != i) {
            throw InvalidProtocolBufferException.invalidWireType();
        }
    }

    public double readDouble() throws IOException {
        requireWireType(1);
        return this.input.readDouble();
    }

    public float readFloat() throws IOException {
        requireWireType(5);
        return this.input.readFloat();
    }

    public long readUInt64() throws IOException {
        requireWireType(0);
        return this.input.readUInt64();
    }

    public long readInt64() throws IOException {
        requireWireType(0);
        return this.input.readInt64();
    }

    public int readInt32() throws IOException {
        requireWireType(0);
        return this.input.readInt32();
    }

    public long readFixed64() throws IOException {
        requireWireType(1);
        return this.input.readFixed64();
    }

    public int readFixed32() throws IOException {
        requireWireType(5);
        return this.input.readFixed32();
    }

    public boolean readBool() throws IOException {
        requireWireType(0);
        return this.input.readBool();
    }

    public String readString() throws IOException {
        requireWireType(2);
        return this.input.readString();
    }

    public String readStringRequireUtf8() throws IOException {
        requireWireType(2);
        return this.input.readStringRequireUtf8();
    }

    public <T> T readMessage(Class<T> cls, ExtensionRegistryLite extensionRegistryLite) throws IOException {
        requireWireType(2);
        return readMessage(Protobuf.getInstance().schemaFor(cls), extensionRegistryLite);
    }

    public <T> T readMessageBySchemaWithCheck(Schema<T> schema, ExtensionRegistryLite extensionRegistryLite) throws IOException {
        requireWireType(2);
        return readMessage(schema, extensionRegistryLite);
    }

    public <T> T readGroup(Class<T> cls, ExtensionRegistryLite extensionRegistryLite) throws IOException {
        requireWireType(3);
        return readGroup(Protobuf.getInstance().schemaFor(cls), extensionRegistryLite);
    }

    public <T> T readGroupBySchemaWithCheck(Schema<T> schema, ExtensionRegistryLite extensionRegistryLite) throws IOException {
        requireWireType(3);
        return readGroup(schema, extensionRegistryLite);
    }

    private <T> T readMessage(Schema<T> schema, ExtensionRegistryLite extensionRegistryLite) throws IOException {
        int readUInt32 = this.input.readUInt32();
        CodedInputStream codedInputStream = this.input;
        if (codedInputStream.recursionDepth < codedInputStream.recursionLimit) {
            int pushLimit = codedInputStream.pushLimit(readUInt32);
            T newInstance = schema.newInstance();
            this.input.recursionDepth++;
            schema.mergeFrom(newInstance, this, extensionRegistryLite);
            schema.makeImmutable(newInstance);
            this.input.checkLastTagWas(0);
            CodedInputStream codedInputStream2 = this.input;
            codedInputStream2.recursionDepth--;
            codedInputStream2.popLimit(pushLimit);
            return newInstance;
        }
        throw InvalidProtocolBufferException.recursionLimitExceeded();
    }

    private <T> T readGroup(Schema<T> schema, ExtensionRegistryLite extensionRegistryLite) throws IOException {
        int i = this.endGroupTag;
        this.endGroupTag = WireFormat.makeTag(WireFormat.getTagFieldNumber(this.tag), 4);
        try {
            T newInstance = schema.newInstance();
            schema.mergeFrom(newInstance, this, extensionRegistryLite);
            schema.makeImmutable(newInstance);
            if (this.tag == this.endGroupTag) {
                return newInstance;
            }
            throw InvalidProtocolBufferException.parseFailure();
        } finally {
            this.endGroupTag = i;
        }
    }

    public ByteString readBytes() throws IOException {
        requireWireType(2);
        return this.input.readBytes();
    }

    public int readUInt32() throws IOException {
        requireWireType(0);
        return this.input.readUInt32();
    }

    public int readEnum() throws IOException {
        requireWireType(0);
        return this.input.readEnum();
    }

    public int readSFixed32() throws IOException {
        requireWireType(5);
        return this.input.readSFixed32();
    }

    public long readSFixed64() throws IOException {
        requireWireType(1);
        return this.input.readSFixed64();
    }

    public int readSInt32() throws IOException {
        requireWireType(0);
        return this.input.readSInt32();
    }

    public long readSInt64() throws IOException {
        requireWireType(0);
        return this.input.readSInt64();
    }

    public void readDoubleList(List<Double> list) throws IOException {
        int readTag;
        int readTag2;
        if (list instanceof DoubleArrayList) {
            DoubleArrayList doubleArrayList = (DoubleArrayList) list;
            int tagWireType = WireFormat.getTagWireType(this.tag);
            if (tagWireType == 1) {
                do {
                    doubleArrayList.addDouble(this.input.readDouble());
                    if (!this.input.isAtEnd()) {
                        readTag2 = this.input.readTag();
                    } else {
                        return;
                    }
                } while (readTag2 == this.tag);
                this.nextTag = readTag2;
            } else if (tagWireType == 2) {
                int readUInt32 = this.input.readUInt32();
                verifyPackedFixed64Length(readUInt32);
                int totalBytesRead = this.input.getTotalBytesRead() + readUInt32;
                do {
                    doubleArrayList.addDouble(this.input.readDouble());
                } while (this.input.getTotalBytesRead() < totalBytesRead);
            } else {
                throw InvalidProtocolBufferException.invalidWireType();
            }
        } else {
            int tagWireType2 = WireFormat.getTagWireType(this.tag);
            if (tagWireType2 == 1) {
                do {
                    list.add(Double.valueOf(this.input.readDouble()));
                    if (!this.input.isAtEnd()) {
                        readTag = this.input.readTag();
                    } else {
                        return;
                    }
                } while (readTag == this.tag);
                this.nextTag = readTag;
            } else if (tagWireType2 == 2) {
                int readUInt322 = this.input.readUInt32();
                verifyPackedFixed64Length(readUInt322);
                int totalBytesRead2 = this.input.getTotalBytesRead() + readUInt322;
                do {
                    list.add(Double.valueOf(this.input.readDouble()));
                } while (this.input.getTotalBytesRead() < totalBytesRead2);
            } else {
                throw InvalidProtocolBufferException.invalidWireType();
            }
        }
    }

    public void readFloatList(List<Float> list) throws IOException {
        int readTag;
        int readTag2;
        if (list instanceof FloatArrayList) {
            FloatArrayList floatArrayList = (FloatArrayList) list;
            int tagWireType = WireFormat.getTagWireType(this.tag);
            if (tagWireType == 2) {
                int readUInt32 = this.input.readUInt32();
                verifyPackedFixed32Length(readUInt32);
                int totalBytesRead = this.input.getTotalBytesRead() + readUInt32;
                do {
                    floatArrayList.addFloat(this.input.readFloat());
                } while (this.input.getTotalBytesRead() < totalBytesRead);
            } else if (tagWireType == 5) {
                do {
                    floatArrayList.addFloat(this.input.readFloat());
                    if (!this.input.isAtEnd()) {
                        readTag2 = this.input.readTag();
                    } else {
                        return;
                    }
                } while (readTag2 == this.tag);
                this.nextTag = readTag2;
            } else {
                throw InvalidProtocolBufferException.invalidWireType();
            }
        } else {
            int tagWireType2 = WireFormat.getTagWireType(this.tag);
            if (tagWireType2 == 2) {
                int readUInt322 = this.input.readUInt32();
                verifyPackedFixed32Length(readUInt322);
                int totalBytesRead2 = this.input.getTotalBytesRead() + readUInt322;
                do {
                    list.add(Float.valueOf(this.input.readFloat()));
                } while (this.input.getTotalBytesRead() < totalBytesRead2);
            } else if (tagWireType2 == 5) {
                do {
                    list.add(Float.valueOf(this.input.readFloat()));
                    if (!this.input.isAtEnd()) {
                        readTag = this.input.readTag();
                    } else {
                        return;
                    }
                } while (readTag == this.tag);
                this.nextTag = readTag;
            } else {
                throw InvalidProtocolBufferException.invalidWireType();
            }
        }
    }

    public void readUInt64List(List<Long> list) throws IOException {
        int readTag;
        int readTag2;
        if (list instanceof LongArrayList) {
            LongArrayList longArrayList = (LongArrayList) list;
            int tagWireType = WireFormat.getTagWireType(this.tag);
            if (tagWireType == 0) {
                do {
                    longArrayList.addLong(this.input.readUInt64());
                    if (!this.input.isAtEnd()) {
                        readTag2 = this.input.readTag();
                    } else {
                        return;
                    }
                } while (readTag2 == this.tag);
                this.nextTag = readTag2;
            } else if (tagWireType == 2) {
                int totalBytesRead = this.input.getTotalBytesRead() + this.input.readUInt32();
                do {
                    longArrayList.addLong(this.input.readUInt64());
                } while (this.input.getTotalBytesRead() < totalBytesRead);
                requirePosition(totalBytesRead);
            } else {
                throw InvalidProtocolBufferException.invalidWireType();
            }
        } else {
            int tagWireType2 = WireFormat.getTagWireType(this.tag);
            if (tagWireType2 == 0) {
                do {
                    list.add(Long.valueOf(this.input.readUInt64()));
                    if (!this.input.isAtEnd()) {
                        readTag = this.input.readTag();
                    } else {
                        return;
                    }
                } while (readTag == this.tag);
                this.nextTag = readTag;
            } else if (tagWireType2 == 2) {
                int totalBytesRead2 = this.input.getTotalBytesRead() + this.input.readUInt32();
                do {
                    list.add(Long.valueOf(this.input.readUInt64()));
                } while (this.input.getTotalBytesRead() < totalBytesRead2);
                requirePosition(totalBytesRead2);
            } else {
                throw InvalidProtocolBufferException.invalidWireType();
            }
        }
    }

    public void readInt64List(List<Long> list) throws IOException {
        int readTag;
        int readTag2;
        if (list instanceof LongArrayList) {
            LongArrayList longArrayList = (LongArrayList) list;
            int tagWireType = WireFormat.getTagWireType(this.tag);
            if (tagWireType == 0) {
                do {
                    longArrayList.addLong(this.input.readInt64());
                    if (!this.input.isAtEnd()) {
                        readTag2 = this.input.readTag();
                    } else {
                        return;
                    }
                } while (readTag2 == this.tag);
                this.nextTag = readTag2;
            } else if (tagWireType == 2) {
                int totalBytesRead = this.input.getTotalBytesRead() + this.input.readUInt32();
                do {
                    longArrayList.addLong(this.input.readInt64());
                } while (this.input.getTotalBytesRead() < totalBytesRead);
                requirePosition(totalBytesRead);
            } else {
                throw InvalidProtocolBufferException.invalidWireType();
            }
        } else {
            int tagWireType2 = WireFormat.getTagWireType(this.tag);
            if (tagWireType2 == 0) {
                do {
                    list.add(Long.valueOf(this.input.readInt64()));
                    if (!this.input.isAtEnd()) {
                        readTag = this.input.readTag();
                    } else {
                        return;
                    }
                } while (readTag == this.tag);
                this.nextTag = readTag;
            } else if (tagWireType2 == 2) {
                int totalBytesRead2 = this.input.getTotalBytesRead() + this.input.readUInt32();
                do {
                    list.add(Long.valueOf(this.input.readInt64()));
                } while (this.input.getTotalBytesRead() < totalBytesRead2);
                requirePosition(totalBytesRead2);
            } else {
                throw InvalidProtocolBufferException.invalidWireType();
            }
        }
    }

    public void readInt32List(List<Integer> list) throws IOException {
        int readTag;
        int readTag2;
        if (list instanceof IntArrayList) {
            IntArrayList intArrayList = (IntArrayList) list;
            int tagWireType = WireFormat.getTagWireType(this.tag);
            if (tagWireType == 0) {
                do {
                    intArrayList.addInt(this.input.readInt32());
                    if (!this.input.isAtEnd()) {
                        readTag2 = this.input.readTag();
                    } else {
                        return;
                    }
                } while (readTag2 == this.tag);
                this.nextTag = readTag2;
            } else if (tagWireType == 2) {
                int totalBytesRead = this.input.getTotalBytesRead() + this.input.readUInt32();
                do {
                    intArrayList.addInt(this.input.readInt32());
                } while (this.input.getTotalBytesRead() < totalBytesRead);
                requirePosition(totalBytesRead);
            } else {
                throw InvalidProtocolBufferException.invalidWireType();
            }
        } else {
            int tagWireType2 = WireFormat.getTagWireType(this.tag);
            if (tagWireType2 == 0) {
                do {
                    list.add(Integer.valueOf(this.input.readInt32()));
                    if (!this.input.isAtEnd()) {
                        readTag = this.input.readTag();
                    } else {
                        return;
                    }
                } while (readTag == this.tag);
                this.nextTag = readTag;
            } else if (tagWireType2 == 2) {
                int totalBytesRead2 = this.input.getTotalBytesRead() + this.input.readUInt32();
                do {
                    list.add(Integer.valueOf(this.input.readInt32()));
                } while (this.input.getTotalBytesRead() < totalBytesRead2);
                requirePosition(totalBytesRead2);
            } else {
                throw InvalidProtocolBufferException.invalidWireType();
            }
        }
    }

    public void readFixed64List(List<Long> list) throws IOException {
        int readTag;
        int readTag2;
        if (list instanceof LongArrayList) {
            LongArrayList longArrayList = (LongArrayList) list;
            int tagWireType = WireFormat.getTagWireType(this.tag);
            if (tagWireType == 1) {
                do {
                    longArrayList.addLong(this.input.readFixed64());
                    if (!this.input.isAtEnd()) {
                        readTag2 = this.input.readTag();
                    } else {
                        return;
                    }
                } while (readTag2 == this.tag);
                this.nextTag = readTag2;
            } else if (tagWireType == 2) {
                int readUInt32 = this.input.readUInt32();
                verifyPackedFixed64Length(readUInt32);
                int totalBytesRead = this.input.getTotalBytesRead() + readUInt32;
                do {
                    longArrayList.addLong(this.input.readFixed64());
                } while (this.input.getTotalBytesRead() < totalBytesRead);
            } else {
                throw InvalidProtocolBufferException.invalidWireType();
            }
        } else {
            int tagWireType2 = WireFormat.getTagWireType(this.tag);
            if (tagWireType2 == 1) {
                do {
                    list.add(Long.valueOf(this.input.readFixed64()));
                    if (!this.input.isAtEnd()) {
                        readTag = this.input.readTag();
                    } else {
                        return;
                    }
                } while (readTag == this.tag);
                this.nextTag = readTag;
            } else if (tagWireType2 == 2) {
                int readUInt322 = this.input.readUInt32();
                verifyPackedFixed64Length(readUInt322);
                int totalBytesRead2 = this.input.getTotalBytesRead() + readUInt322;
                do {
                    list.add(Long.valueOf(this.input.readFixed64()));
                } while (this.input.getTotalBytesRead() < totalBytesRead2);
            } else {
                throw InvalidProtocolBufferException.invalidWireType();
            }
        }
    }

    public void readFixed32List(List<Integer> list) throws IOException {
        int readTag;
        int readTag2;
        if (list instanceof IntArrayList) {
            IntArrayList intArrayList = (IntArrayList) list;
            int tagWireType = WireFormat.getTagWireType(this.tag);
            if (tagWireType == 2) {
                int readUInt32 = this.input.readUInt32();
                verifyPackedFixed32Length(readUInt32);
                int totalBytesRead = this.input.getTotalBytesRead() + readUInt32;
                do {
                    intArrayList.addInt(this.input.readFixed32());
                } while (this.input.getTotalBytesRead() < totalBytesRead);
            } else if (tagWireType == 5) {
                do {
                    intArrayList.addInt(this.input.readFixed32());
                    if (!this.input.isAtEnd()) {
                        readTag2 = this.input.readTag();
                    } else {
                        return;
                    }
                } while (readTag2 == this.tag);
                this.nextTag = readTag2;
            } else {
                throw InvalidProtocolBufferException.invalidWireType();
            }
        } else {
            int tagWireType2 = WireFormat.getTagWireType(this.tag);
            if (tagWireType2 == 2) {
                int readUInt322 = this.input.readUInt32();
                verifyPackedFixed32Length(readUInt322);
                int totalBytesRead2 = this.input.getTotalBytesRead() + readUInt322;
                do {
                    list.add(Integer.valueOf(this.input.readFixed32()));
                } while (this.input.getTotalBytesRead() < totalBytesRead2);
            } else if (tagWireType2 == 5) {
                do {
                    list.add(Integer.valueOf(this.input.readFixed32()));
                    if (!this.input.isAtEnd()) {
                        readTag = this.input.readTag();
                    } else {
                        return;
                    }
                } while (readTag == this.tag);
                this.nextTag = readTag;
            } else {
                throw InvalidProtocolBufferException.invalidWireType();
            }
        }
    }

    public void readBoolList(List<Boolean> list) throws IOException {
        int readTag;
        int readTag2;
        if (list instanceof BooleanArrayList) {
            BooleanArrayList booleanArrayList = (BooleanArrayList) list;
            int tagWireType = WireFormat.getTagWireType(this.tag);
            if (tagWireType == 0) {
                do {
                    booleanArrayList.addBoolean(this.input.readBool());
                    if (!this.input.isAtEnd()) {
                        readTag2 = this.input.readTag();
                    } else {
                        return;
                    }
                } while (readTag2 == this.tag);
                this.nextTag = readTag2;
            } else if (tagWireType == 2) {
                int totalBytesRead = this.input.getTotalBytesRead() + this.input.readUInt32();
                do {
                    booleanArrayList.addBoolean(this.input.readBool());
                } while (this.input.getTotalBytesRead() < totalBytesRead);
                requirePosition(totalBytesRead);
            } else {
                throw InvalidProtocolBufferException.invalidWireType();
            }
        } else {
            int tagWireType2 = WireFormat.getTagWireType(this.tag);
            if (tagWireType2 == 0) {
                do {
                    list.add(Boolean.valueOf(this.input.readBool()));
                    if (!this.input.isAtEnd()) {
                        readTag = this.input.readTag();
                    } else {
                        return;
                    }
                } while (readTag == this.tag);
                this.nextTag = readTag;
            } else if (tagWireType2 == 2) {
                int totalBytesRead2 = this.input.getTotalBytesRead() + this.input.readUInt32();
                do {
                    list.add(Boolean.valueOf(this.input.readBool()));
                } while (this.input.getTotalBytesRead() < totalBytesRead2);
                requirePosition(totalBytesRead2);
            } else {
                throw InvalidProtocolBufferException.invalidWireType();
            }
        }
    }

    public void readStringList(List<String> list) throws IOException {
        readStringListInternal(list, false);
    }

    public void readStringListRequireUtf8(List<String> list) throws IOException {
        readStringListInternal(list, true);
    }

    public void readStringListInternal(List<String> list, boolean z) throws IOException {
        int readTag;
        int readTag2;
        if (WireFormat.getTagWireType(this.tag) != 2) {
            throw InvalidProtocolBufferException.invalidWireType();
        } else if (!(list instanceof LazyStringList) || z) {
            do {
                list.add(z ? readStringRequireUtf8() : readString());
                if (!this.input.isAtEnd()) {
                    readTag = this.input.readTag();
                } else {
                    return;
                }
            } while (readTag == this.tag);
            this.nextTag = readTag;
        } else {
            LazyStringList lazyStringList = (LazyStringList) list;
            do {
                lazyStringList.add(readBytes());
                if (!this.input.isAtEnd()) {
                    readTag2 = this.input.readTag();
                } else {
                    return;
                }
            } while (readTag2 == this.tag);
            this.nextTag = readTag2;
        }
    }

    public <T> void readMessageList(List<T> list, Schema<T> schema, ExtensionRegistryLite extensionRegistryLite) throws IOException {
        int readTag;
        if (WireFormat.getTagWireType(this.tag) == 2) {
            int i = this.tag;
            do {
                list.add(readMessage(schema, extensionRegistryLite));
                if (!this.input.isAtEnd() && this.nextTag == 0) {
                    readTag = this.input.readTag();
                } else {
                    return;
                }
            } while (readTag == i);
            this.nextTag = readTag;
            return;
        }
        throw InvalidProtocolBufferException.invalidWireType();
    }

    public <T> void readGroupList(List<T> list, Schema<T> schema, ExtensionRegistryLite extensionRegistryLite) throws IOException {
        int readTag;
        if (WireFormat.getTagWireType(this.tag) == 3) {
            int i = this.tag;
            do {
                list.add(readGroup(schema, extensionRegistryLite));
                if (!this.input.isAtEnd() && this.nextTag == 0) {
                    readTag = this.input.readTag();
                } else {
                    return;
                }
            } while (readTag == i);
            this.nextTag = readTag;
            return;
        }
        throw InvalidProtocolBufferException.invalidWireType();
    }

    public void readBytesList(List<ByteString> list) throws IOException {
        int readTag;
        if (WireFormat.getTagWireType(this.tag) == 2) {
            do {
                list.add(readBytes());
                if (!this.input.isAtEnd()) {
                    readTag = this.input.readTag();
                } else {
                    return;
                }
            } while (readTag == this.tag);
            this.nextTag = readTag;
            return;
        }
        throw InvalidProtocolBufferException.invalidWireType();
    }

    public void readUInt32List(List<Integer> list) throws IOException {
        int readTag;
        int readTag2;
        if (list instanceof IntArrayList) {
            IntArrayList intArrayList = (IntArrayList) list;
            int tagWireType = WireFormat.getTagWireType(this.tag);
            if (tagWireType == 0) {
                do {
                    intArrayList.addInt(this.input.readUInt32());
                    if (!this.input.isAtEnd()) {
                        readTag2 = this.input.readTag();
                    } else {
                        return;
                    }
                } while (readTag2 == this.tag);
                this.nextTag = readTag2;
            } else if (tagWireType == 2) {
                int totalBytesRead = this.input.getTotalBytesRead() + this.input.readUInt32();
                do {
                    intArrayList.addInt(this.input.readUInt32());
                } while (this.input.getTotalBytesRead() < totalBytesRead);
                requirePosition(totalBytesRead);
            } else {
                throw InvalidProtocolBufferException.invalidWireType();
            }
        } else {
            int tagWireType2 = WireFormat.getTagWireType(this.tag);
            if (tagWireType2 == 0) {
                do {
                    list.add(Integer.valueOf(this.input.readUInt32()));
                    if (!this.input.isAtEnd()) {
                        readTag = this.input.readTag();
                    } else {
                        return;
                    }
                } while (readTag == this.tag);
                this.nextTag = readTag;
            } else if (tagWireType2 == 2) {
                int totalBytesRead2 = this.input.getTotalBytesRead() + this.input.readUInt32();
                do {
                    list.add(Integer.valueOf(this.input.readUInt32()));
                } while (this.input.getTotalBytesRead() < totalBytesRead2);
                requirePosition(totalBytesRead2);
            } else {
                throw InvalidProtocolBufferException.invalidWireType();
            }
        }
    }

    public void readEnumList(List<Integer> list) throws IOException {
        int readTag;
        int readTag2;
        if (list instanceof IntArrayList) {
            IntArrayList intArrayList = (IntArrayList) list;
            int tagWireType = WireFormat.getTagWireType(this.tag);
            if (tagWireType == 0) {
                do {
                    intArrayList.addInt(this.input.readEnum());
                    if (!this.input.isAtEnd()) {
                        readTag2 = this.input.readTag();
                    } else {
                        return;
                    }
                } while (readTag2 == this.tag);
                this.nextTag = readTag2;
            } else if (tagWireType == 2) {
                int totalBytesRead = this.input.getTotalBytesRead() + this.input.readUInt32();
                do {
                    intArrayList.addInt(this.input.readEnum());
                } while (this.input.getTotalBytesRead() < totalBytesRead);
                requirePosition(totalBytesRead);
            } else {
                throw InvalidProtocolBufferException.invalidWireType();
            }
        } else {
            int tagWireType2 = WireFormat.getTagWireType(this.tag);
            if (tagWireType2 == 0) {
                do {
                    list.add(Integer.valueOf(this.input.readEnum()));
                    if (!this.input.isAtEnd()) {
                        readTag = this.input.readTag();
                    } else {
                        return;
                    }
                } while (readTag == this.tag);
                this.nextTag = readTag;
            } else if (tagWireType2 == 2) {
                int totalBytesRead2 = this.input.getTotalBytesRead() + this.input.readUInt32();
                do {
                    list.add(Integer.valueOf(this.input.readEnum()));
                } while (this.input.getTotalBytesRead() < totalBytesRead2);
                requirePosition(totalBytesRead2);
            } else {
                throw InvalidProtocolBufferException.invalidWireType();
            }
        }
    }

    public void readSFixed32List(List<Integer> list) throws IOException {
        int readTag;
        int readTag2;
        if (list instanceof IntArrayList) {
            IntArrayList intArrayList = (IntArrayList) list;
            int tagWireType = WireFormat.getTagWireType(this.tag);
            if (tagWireType == 2) {
                int readUInt32 = this.input.readUInt32();
                verifyPackedFixed32Length(readUInt32);
                int totalBytesRead = this.input.getTotalBytesRead() + readUInt32;
                do {
                    intArrayList.addInt(this.input.readSFixed32());
                } while (this.input.getTotalBytesRead() < totalBytesRead);
            } else if (tagWireType == 5) {
                do {
                    intArrayList.addInt(this.input.readSFixed32());
                    if (!this.input.isAtEnd()) {
                        readTag2 = this.input.readTag();
                    } else {
                        return;
                    }
                } while (readTag2 == this.tag);
                this.nextTag = readTag2;
            } else {
                throw InvalidProtocolBufferException.invalidWireType();
            }
        } else {
            int tagWireType2 = WireFormat.getTagWireType(this.tag);
            if (tagWireType2 == 2) {
                int readUInt322 = this.input.readUInt32();
                verifyPackedFixed32Length(readUInt322);
                int totalBytesRead2 = this.input.getTotalBytesRead() + readUInt322;
                do {
                    list.add(Integer.valueOf(this.input.readSFixed32()));
                } while (this.input.getTotalBytesRead() < totalBytesRead2);
            } else if (tagWireType2 == 5) {
                do {
                    list.add(Integer.valueOf(this.input.readSFixed32()));
                    if (!this.input.isAtEnd()) {
                        readTag = this.input.readTag();
                    } else {
                        return;
                    }
                } while (readTag == this.tag);
                this.nextTag = readTag;
            } else {
                throw InvalidProtocolBufferException.invalidWireType();
            }
        }
    }

    public void readSFixed64List(List<Long> list) throws IOException {
        int readTag;
        int readTag2;
        if (list instanceof LongArrayList) {
            LongArrayList longArrayList = (LongArrayList) list;
            int tagWireType = WireFormat.getTagWireType(this.tag);
            if (tagWireType == 1) {
                do {
                    longArrayList.addLong(this.input.readSFixed64());
                    if (!this.input.isAtEnd()) {
                        readTag2 = this.input.readTag();
                    } else {
                        return;
                    }
                } while (readTag2 == this.tag);
                this.nextTag = readTag2;
            } else if (tagWireType == 2) {
                int readUInt32 = this.input.readUInt32();
                verifyPackedFixed64Length(readUInt32);
                int totalBytesRead = this.input.getTotalBytesRead() + readUInt32;
                do {
                    longArrayList.addLong(this.input.readSFixed64());
                } while (this.input.getTotalBytesRead() < totalBytesRead);
            } else {
                throw InvalidProtocolBufferException.invalidWireType();
            }
        } else {
            int tagWireType2 = WireFormat.getTagWireType(this.tag);
            if (tagWireType2 == 1) {
                do {
                    list.add(Long.valueOf(this.input.readSFixed64()));
                    if (!this.input.isAtEnd()) {
                        readTag = this.input.readTag();
                    } else {
                        return;
                    }
                } while (readTag == this.tag);
                this.nextTag = readTag;
            } else if (tagWireType2 == 2) {
                int readUInt322 = this.input.readUInt32();
                verifyPackedFixed64Length(readUInt322);
                int totalBytesRead2 = this.input.getTotalBytesRead() + readUInt322;
                do {
                    list.add(Long.valueOf(this.input.readSFixed64()));
                } while (this.input.getTotalBytesRead() < totalBytesRead2);
            } else {
                throw InvalidProtocolBufferException.invalidWireType();
            }
        }
    }

    public void readSInt32List(List<Integer> list) throws IOException {
        int readTag;
        int readTag2;
        if (list instanceof IntArrayList) {
            IntArrayList intArrayList = (IntArrayList) list;
            int tagWireType = WireFormat.getTagWireType(this.tag);
            if (tagWireType == 0) {
                do {
                    intArrayList.addInt(this.input.readSInt32());
                    if (!this.input.isAtEnd()) {
                        readTag2 = this.input.readTag();
                    } else {
                        return;
                    }
                } while (readTag2 == this.tag);
                this.nextTag = readTag2;
            } else if (tagWireType == 2) {
                int totalBytesRead = this.input.getTotalBytesRead() + this.input.readUInt32();
                do {
                    intArrayList.addInt(this.input.readSInt32());
                } while (this.input.getTotalBytesRead() < totalBytesRead);
                requirePosition(totalBytesRead);
            } else {
                throw InvalidProtocolBufferException.invalidWireType();
            }
        } else {
            int tagWireType2 = WireFormat.getTagWireType(this.tag);
            if (tagWireType2 == 0) {
                do {
                    list.add(Integer.valueOf(this.input.readSInt32()));
                    if (!this.input.isAtEnd()) {
                        readTag = this.input.readTag();
                    } else {
                        return;
                    }
                } while (readTag == this.tag);
                this.nextTag = readTag;
            } else if (tagWireType2 == 2) {
                int totalBytesRead2 = this.input.getTotalBytesRead() + this.input.readUInt32();
                do {
                    list.add(Integer.valueOf(this.input.readSInt32()));
                } while (this.input.getTotalBytesRead() < totalBytesRead2);
                requirePosition(totalBytesRead2);
            } else {
                throw InvalidProtocolBufferException.invalidWireType();
            }
        }
    }

    public void readSInt64List(List<Long> list) throws IOException {
        int readTag;
        int readTag2;
        if (list instanceof LongArrayList) {
            LongArrayList longArrayList = (LongArrayList) list;
            int tagWireType = WireFormat.getTagWireType(this.tag);
            if (tagWireType == 0) {
                do {
                    longArrayList.addLong(this.input.readSInt64());
                    if (!this.input.isAtEnd()) {
                        readTag2 = this.input.readTag();
                    } else {
                        return;
                    }
                } while (readTag2 == this.tag);
                this.nextTag = readTag2;
            } else if (tagWireType == 2) {
                int totalBytesRead = this.input.getTotalBytesRead() + this.input.readUInt32();
                do {
                    longArrayList.addLong(this.input.readSInt64());
                } while (this.input.getTotalBytesRead() < totalBytesRead);
                requirePosition(totalBytesRead);
            } else {
                throw InvalidProtocolBufferException.invalidWireType();
            }
        } else {
            int tagWireType2 = WireFormat.getTagWireType(this.tag);
            if (tagWireType2 == 0) {
                do {
                    list.add(Long.valueOf(this.input.readSInt64()));
                    if (!this.input.isAtEnd()) {
                        readTag = this.input.readTag();
                    } else {
                        return;
                    }
                } while (readTag == this.tag);
                this.nextTag = readTag;
            } else if (tagWireType2 == 2) {
                int totalBytesRead2 = this.input.getTotalBytesRead() + this.input.readUInt32();
                do {
                    list.add(Long.valueOf(this.input.readSInt64()));
                } while (this.input.getTotalBytesRead() < totalBytesRead2);
                requirePosition(totalBytesRead2);
            } else {
                throw InvalidProtocolBufferException.invalidWireType();
            }
        }
    }

    private void verifyPackedFixed64Length(int i) throws IOException {
        if ((i & 7) != 0) {
            throw InvalidProtocolBufferException.parseFailure();
        }
    }

    public <K, V> void readMap(Map<K, V> map, MapEntryLite.Metadata<K, V> metadata, ExtensionRegistryLite extensionRegistryLite) throws IOException {
        requireWireType(2);
        this.input.pushLimit(this.input.readUInt32());
        throw null;
    }

    private void verifyPackedFixed32Length(int i) throws IOException {
        if ((i & 3) != 0) {
            throw InvalidProtocolBufferException.parseFailure();
        }
    }

    private void requirePosition(int i) throws IOException {
        if (this.input.getTotalBytesRead() != i) {
            throw InvalidProtocolBufferException.truncatedMessage();
        }
    }
}
