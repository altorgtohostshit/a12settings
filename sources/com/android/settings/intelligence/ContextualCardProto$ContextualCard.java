package com.android.settings.intelligence;

import com.google.protobuf.GeneratedMessageLite;
import com.google.protobuf.Internal;
import com.google.protobuf.MessageLiteOrBuilder;
import com.google.protobuf.Parser;
import java.util.Objects;

public final class ContextualCardProto$ContextualCard extends GeneratedMessageLite<ContextualCardProto$ContextualCard, Builder> implements MessageLiteOrBuilder {
    public static final int CARDNAME_FIELD_NUMBER = 3;
    public static final int CARD_CATEGORY_FIELD_NUMBER = 4;
    public static final int CARD_SCORE_FIELD_NUMBER = 5;
    /* access modifiers changed from: private */
    public static final ContextualCardProto$ContextualCard DEFAULT_INSTANCE;
    private static volatile Parser<ContextualCardProto$ContextualCard> PARSER = null;
    public static final int SLICEURI_FIELD_NUMBER = 1;
    private int bitField0_;
    private int cardCategory_;
    private String cardName_ = "";
    private double cardScore_;
    private String sliceUri_ = "";

    private ContextualCardProto$ContextualCard() {
    }

    public enum Category implements Internal.EnumLite {
        DEFAULT(0),
        SUGGESTION(1),
        POSSIBLE(2),
        IMPORTANT(3),
        DEFERRED_SETUP(5),
        STICKY(6);
        
        private static final Internal.EnumLiteMap<Category> internalValueMap = null;
        private final int value;

        static {
            internalValueMap = new Internal.EnumLiteMap<Category>() {
                public Category findValueByNumber(int i) {
                    return Category.forNumber(i);
                }
            };
        }

        public final int getNumber() {
            return this.value;
        }

        public static Category forNumber(int i) {
            if (i == 0) {
                return DEFAULT;
            }
            if (i == 1) {
                return SUGGESTION;
            }
            if (i == 2) {
                return POSSIBLE;
            }
            if (i == 3) {
                return IMPORTANT;
            }
            if (i == 5) {
                return DEFERRED_SETUP;
            }
            if (i != 6) {
                return null;
            }
            return STICKY;
        }

        public static Internal.EnumVerifier internalGetVerifier() {
            return CategoryVerifier.INSTANCE;
        }

        private static final class CategoryVerifier implements Internal.EnumVerifier {
            static final Internal.EnumVerifier INSTANCE = null;

            private CategoryVerifier() {
            }

            static {
                INSTANCE = new CategoryVerifier();
            }

            public boolean isInRange(int i) {
                return Category.forNumber(i) != null;
            }
        }

        private Category(int i) {
            this.value = i;
        }
    }

    public String getSliceUri() {
        return this.sliceUri_;
    }

    /* access modifiers changed from: private */
    public void setSliceUri(String str) {
        Objects.requireNonNull(str);
        this.bitField0_ |= 1;
        this.sliceUri_ = str;
    }

    /* access modifiers changed from: private */
    public void setCardName(String str) {
        Objects.requireNonNull(str);
        this.bitField0_ |= 2;
        this.cardName_ = str;
    }

    /* access modifiers changed from: private */
    public void setCardCategory(Category category) {
        Objects.requireNonNull(category);
        this.bitField0_ |= 4;
        this.cardCategory_ = category.getNumber();
    }

    public static Builder newBuilder() {
        return (Builder) DEFAULT_INSTANCE.createBuilder();
    }

    public static final class Builder extends GeneratedMessageLite.Builder<ContextualCardProto$ContextualCard, Builder> implements MessageLiteOrBuilder {
        /* synthetic */ Builder(ContextualCardProto$1 contextualCardProto$1) {
            this();
        }

        private Builder() {
            super(ContextualCardProto$ContextualCard.DEFAULT_INSTANCE);
        }

        public Builder setSliceUri(String str) {
            copyOnWrite();
            ((ContextualCardProto$ContextualCard) this.instance).setSliceUri(str);
            return this;
        }

        public Builder setCardName(String str) {
            copyOnWrite();
            ((ContextualCardProto$ContextualCard) this.instance).setCardName(str);
            return this;
        }

        public Builder setCardCategory(Category category) {
            copyOnWrite();
            ((ContextualCardProto$ContextualCard) this.instance).setCardCategory(category);
            return this;
        }
    }

    /* access modifiers changed from: protected */
    public final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke methodToInvoke, Object obj, Object obj2) {
        switch (ContextualCardProto$1.f84xa1df5c61[methodToInvoke.ordinal()]) {
            case 1:
                return new ContextualCardProto$ContextualCard();
            case 2:
                return new Builder((ContextualCardProto$1) null);
            case 3:
                return GeneratedMessageLite.newMessageInfo(DEFAULT_INSTANCE, "\u0001\u0004\u0000\u0001\u0001\u0005\u0004\u0000\u0000\u0000\u0001\b\u0000\u0003\b\u0001\u0004\f\u0002\u0005\u0000\u0003", new Object[]{"bitField0_", "sliceUri_", "cardName_", "cardCategory_", Category.internalGetVerifier(), "cardScore_"});
            case 4:
                return DEFAULT_INSTANCE;
            case 5:
                Parser<ContextualCardProto$ContextualCard> parser = PARSER;
                if (parser == null) {
                    synchronized (ContextualCardProto$ContextualCard.class) {
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
        ContextualCardProto$ContextualCard contextualCardProto$ContextualCard = new ContextualCardProto$ContextualCard();
        DEFAULT_INSTANCE = contextualCardProto$ContextualCard;
        GeneratedMessageLite.registerDefaultInstance(ContextualCardProto$ContextualCard.class, contextualCardProto$ContextualCard);
    }
}
