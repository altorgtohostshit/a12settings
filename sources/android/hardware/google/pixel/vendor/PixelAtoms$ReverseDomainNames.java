package android.hardware.google.pixel.vendor;

import com.google.protobuf.GeneratedMessageLite;
import com.google.protobuf.MessageLiteOrBuilder;
import com.google.protobuf.Parser;

public final class PixelAtoms$ReverseDomainNames extends GeneratedMessageLite<PixelAtoms$ReverseDomainNames, Builder> implements MessageLiteOrBuilder {
    /* access modifiers changed from: private */
    public static final PixelAtoms$ReverseDomainNames DEFAULT_INSTANCE;
    private static volatile Parser<PixelAtoms$ReverseDomainNames> PARSER = null;
    public static final int PIXEL_FIELD_NUMBER = 1;
    private int bitField0_;
    private String pixel_ = "com.google.pixel";

    private PixelAtoms$ReverseDomainNames() {
    }

    public String getPixel() {
        return this.pixel_;
    }

    public static Builder newBuilder() {
        return (Builder) DEFAULT_INSTANCE.createBuilder();
    }

    public static final class Builder extends GeneratedMessageLite.Builder<PixelAtoms$ReverseDomainNames, Builder> implements MessageLiteOrBuilder {
        /* synthetic */ Builder(PixelAtoms$1 pixelAtoms$1) {
            this();
        }

        private Builder() {
            super(PixelAtoms$ReverseDomainNames.DEFAULT_INSTANCE);
        }
    }

    /* access modifiers changed from: protected */
    public final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke methodToInvoke, Object obj, Object obj2) {
        switch (PixelAtoms$1.f0xa1df5c61[methodToInvoke.ordinal()]) {
            case 1:
                return new PixelAtoms$ReverseDomainNames();
            case 2:
                return new Builder((PixelAtoms$1) null);
            case 3:
                return GeneratedMessageLite.newMessageInfo(DEFAULT_INSTANCE, "\u0001\u0001\u0000\u0001\u0001\u0001\u0001\u0000\u0000\u0000\u0001\b\u0000", new Object[]{"bitField0_", "pixel_"});
            case 4:
                return DEFAULT_INSTANCE;
            case 5:
                Parser<PixelAtoms$ReverseDomainNames> parser = PARSER;
                if (parser == null) {
                    synchronized (PixelAtoms$ReverseDomainNames.class) {
                        parser = PARSER;
                        if (parser == null) {
                            parser = new GeneratedMessageLite.DefaultInstanceBasedParser<>(DEFAULT_INSTANCE);
                            PARSER = parser;
                        }
                    }
                }
                return parser;
            case 6:
                return (byte) 1;
            case 7:
                return null;
            default:
                throw new UnsupportedOperationException();
        }
    }

    static {
        PixelAtoms$ReverseDomainNames pixelAtoms$ReverseDomainNames = new PixelAtoms$ReverseDomainNames();
        DEFAULT_INSTANCE = pixelAtoms$ReverseDomainNames;
        GeneratedMessageLite.registerDefaultInstance(PixelAtoms$ReverseDomainNames.class, pixelAtoms$ReverseDomainNames);
    }
}
