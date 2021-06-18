package com.google.android.setupcompat.internal;

import java.util.concurrent.TimeUnit;

public class ClockProvider {
    private static final Ticker SYSTEM_TICKER;
    private static Ticker ticker;

    public interface Supplier<T> {
        T get();
    }

    public static long timeInNanos() {
        return ticker.read();
    }

    public static long timeInMillis() {
        return TimeUnit.NANOSECONDS.toMillis(timeInNanos());
    }

    public static void resetInstance() {
        ticker = SYSTEM_TICKER;
    }

    public static void setInstance(Supplier<Long> supplier) {
        ticker = new ClockProvider$$ExternalSyntheticLambda0(supplier);
    }

    static {
        ClockProvider$$ExternalSyntheticLambda1 clockProvider$$ExternalSyntheticLambda1 = ClockProvider$$ExternalSyntheticLambda1.INSTANCE;
        SYSTEM_TICKER = clockProvider$$ExternalSyntheticLambda1;
        ticker = clockProvider$$ExternalSyntheticLambda1;
    }
}
