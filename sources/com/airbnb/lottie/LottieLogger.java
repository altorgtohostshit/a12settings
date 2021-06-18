package com.airbnb.lottie;

public interface LottieLogger {
    void debug(String str);

    void error(String str, Throwable th);

    void warning(String str);

    void warning(String str, Throwable th);
}
