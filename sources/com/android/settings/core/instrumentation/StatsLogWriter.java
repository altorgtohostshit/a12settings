package com.android.settings.core.instrumentation;

import android.content.Context;
import android.util.Pair;
import com.android.settingslib.core.instrumentation.LogWriter;

public class StatsLogWriter implements LogWriter {
    public void visible(Context context, int i, int i2, int i3) {
        SettingsStatsLog.write(97, i, 1, i2, "", (long) i3);
    }

    public void hidden(Context context, int i, int i2) {
        SettingsStatsLog.write(97, 0, 2, i, "", (long) i2);
    }

    public void action(Context context, int i, Pair<Integer, Object>... pairArr) {
        action(0, i, 0, (String) null, 0);
    }

    public void action(Context context, int i, int i2) {
        action(0, i, 0, (String) null, i2);
    }

    public void action(Context context, int i, boolean z) {
        action(0, i, 0, (String) null, z ? 1 : 0);
    }

    public void action(Context context, int i, String str) {
        action(0, i, 0, str, 1);
    }

    public void action(int i, int i2, int i3, String str, int i4) {
        SettingsStatsLog.write(97, i, i2, i3, str, (long) i4);
    }
}
