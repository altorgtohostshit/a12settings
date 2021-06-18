package com.airbnb.lottie.utils;

import android.util.Log;
import com.airbnb.lottie.C0478L;
import com.airbnb.lottie.LottieLogger;
import java.util.HashSet;
import java.util.Set;

public class LogcatLogger implements LottieLogger {
    private static final Set<String> loggedMessages = new HashSet();

    public void debug(String str) {
        debug(str, (Throwable) null);
    }

    public void debug(String str, Throwable th) {
        if (C0478L.DBG) {
            Log.d("LOTTIE", str, th);
        }
    }

    public void warning(String str) {
        warning(str, (Throwable) null);
    }

    public void warning(String str, Throwable th) {
        Set<String> set = loggedMessages;
        if (!set.contains(str)) {
            Log.w("LOTTIE", str, th);
            set.add(str);
        }
    }

    public void error(String str, Throwable th) {
        if (C0478L.DBG) {
            Log.d("LOTTIE", str, th);
        }
    }
}
