package com.google.protobuf;

class GeneratedMessageInfoFactory implements MessageInfoFactory {
    private static final GeneratedMessageInfoFactory instance = new GeneratedMessageInfoFactory();

    private GeneratedMessageInfoFactory() {
    }

    public static GeneratedMessageInfoFactory getInstance() {
        return instance;
    }

    public boolean isSupported(Class<?> cls) {
        return GeneratedMessageLite.class.isAssignableFrom(cls);
    }

    /* JADX WARNING: type inference failed for: r4v0, types: [java.lang.Class<?>, java.lang.Class] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.google.protobuf.MessageInfo messageInfoFor(java.lang.Class<?> r4) {
        /*
            r3 = this;
            java.lang.Class<com.google.protobuf.GeneratedMessageLite> r3 = com.google.protobuf.GeneratedMessageLite.class
            boolean r0 = r3.isAssignableFrom(r4)
            if (r0 == 0) goto L_0x0033
            java.lang.Class r3 = r4.asSubclass(r3)     // Catch:{ Exception -> 0x0017 }
            com.google.protobuf.GeneratedMessageLite r3 = com.google.protobuf.GeneratedMessageLite.getDefaultInstance(r3)     // Catch:{ Exception -> 0x0017 }
            java.lang.Object r3 = r3.buildMessageInfo()     // Catch:{ Exception -> 0x0017 }
            com.google.protobuf.MessageInfo r3 = (com.google.protobuf.MessageInfo) r3     // Catch:{ Exception -> 0x0017 }
            return r3
        L_0x0017:
            r3 = move-exception
            java.lang.RuntimeException r0 = new java.lang.RuntimeException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Unable to get message info for "
            r1.append(r2)
            java.lang.String r4 = r4.getName()
            r1.append(r4)
            java.lang.String r4 = r1.toString()
            r0.<init>(r4, r3)
            throw r0
        L_0x0033:
            java.lang.IllegalArgumentException r3 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Unsupported message type: "
            r0.append(r1)
            java.lang.String r4 = r4.getName()
            r0.append(r4)
            java.lang.String r4 = r0.toString()
            r3.<init>(r4)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.GeneratedMessageInfoFactory.messageInfoFor(java.lang.Class):com.google.protobuf.MessageInfo");
    }
}
