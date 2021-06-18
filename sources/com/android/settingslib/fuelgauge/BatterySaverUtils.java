package com.android.settingslib.fuelgauge;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.KeyValueListParser;
import android.util.Slog;

public class BatterySaverUtils {

    private static class Parameters {
        public final int endNth;
        private final Context mContext;
        public final int startNth;

        public Parameters(Context context) {
            this.mContext = context;
            String string = Settings.Global.getString(context.getContentResolver(), "low_power_mode_suggestion_params");
            KeyValueListParser keyValueListParser = new KeyValueListParser(',');
            try {
                keyValueListParser.setString(string);
            } catch (IllegalArgumentException unused) {
                Slog.wtf("BatterySaverUtils", "Bad constants: " + string);
            }
            this.startNth = keyValueListParser.getInt("start_nth", 4);
            this.endNth = keyValueListParser.getInt("end_nth", 8);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0063, code lost:
        return true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized boolean setPowerSaveMode(android.content.Context r6, boolean r7, boolean r8) {
        /*
            java.lang.Class<com.android.settingslib.fuelgauge.BatterySaverUtils> r0 = com.android.settingslib.fuelgauge.BatterySaverUtils.class
            monitor-enter(r0)
            android.content.ContentResolver r1 = r6.getContentResolver()     // Catch:{ all -> 0x0066 }
            android.os.Bundle r2 = new android.os.Bundle     // Catch:{ all -> 0x0066 }
            r3 = 1
            r2.<init>(r3)     // Catch:{ all -> 0x0066 }
            java.lang.String r4 = "extra_confirm_only"
            r5 = 0
            r2.putBoolean(r4, r5)     // Catch:{ all -> 0x0066 }
            if (r7 == 0) goto L_0x001f
            if (r8 == 0) goto L_0x001f
            boolean r4 = maybeShowBatterySaverConfirmation(r6, r2)     // Catch:{ all -> 0x0066 }
            if (r4 == 0) goto L_0x001f
            monitor-exit(r0)
            return r5
        L_0x001f:
            if (r7 == 0) goto L_0x0026
            if (r8 != 0) goto L_0x0026
            setBatterySaverConfirmationAcknowledged(r6)     // Catch:{ all -> 0x0066 }
        L_0x0026:
            java.lang.Class<android.os.PowerManager> r8 = android.os.PowerManager.class
            java.lang.Object r8 = r6.getSystemService(r8)     // Catch:{ all -> 0x0066 }
            android.os.PowerManager r8 = (android.os.PowerManager) r8     // Catch:{ all -> 0x0066 }
            boolean r8 = r8.setPowerSaveModeEnabled(r7)     // Catch:{ all -> 0x0066 }
            if (r8 == 0) goto L_0x0064
            if (r7 == 0) goto L_0x0062
            java.lang.String r7 = "low_power_manual_activation_count"
            int r7 = android.provider.Settings.Secure.getInt(r1, r7, r5)     // Catch:{ all -> 0x0066 }
            int r7 = r7 + r3
            java.lang.String r8 = "low_power_manual_activation_count"
            android.provider.Settings.Secure.putInt(r1, r8, r7)     // Catch:{ all -> 0x0066 }
            com.android.settingslib.fuelgauge.BatterySaverUtils$Parameters r8 = new com.android.settingslib.fuelgauge.BatterySaverUtils$Parameters     // Catch:{ all -> 0x0066 }
            r8.<init>(r6)     // Catch:{ all -> 0x0066 }
            int r4 = r8.startNth     // Catch:{ all -> 0x0066 }
            if (r7 < r4) goto L_0x0062
            int r8 = r8.endNth     // Catch:{ all -> 0x0066 }
            if (r7 > r8) goto L_0x0062
            java.lang.String r7 = "low_power_trigger_level"
            int r7 = android.provider.Settings.Global.getInt(r1, r7, r5)     // Catch:{ all -> 0x0066 }
            if (r7 != 0) goto L_0x0062
            java.lang.String r7 = "suppress_auto_battery_saver_suggestion"
            int r7 = android.provider.Settings.Secure.getInt(r1, r7, r5)     // Catch:{ all -> 0x0066 }
            if (r7 != 0) goto L_0x0062
            showAutoBatterySaverSuggestion(r6, r2)     // Catch:{ all -> 0x0066 }
        L_0x0062:
            monitor-exit(r0)
            return r3
        L_0x0064:
            monitor-exit(r0)
            return r5
        L_0x0066:
            r6 = move-exception
            monitor-exit(r0)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.fuelgauge.BatterySaverUtils.setPowerSaveMode(android.content.Context, boolean, boolean):boolean");
    }

    public static boolean maybeShowBatterySaverConfirmation(Context context, Bundle bundle) {
        if (Settings.Secure.getInt(context.getContentResolver(), "low_power_warning_acknowledged", 0) != 0) {
            return false;
        }
        context.sendBroadcast(getSystemUiBroadcast("PNW.startSaverConfirmation", bundle));
        return true;
    }

    private static void showAutoBatterySaverSuggestion(Context context, Bundle bundle) {
        context.sendBroadcast(getSystemUiBroadcast("PNW.autoSaverSuggestion", bundle));
    }

    private static Intent getSystemUiBroadcast(String str, Bundle bundle) {
        Intent intent = new Intent(str);
        intent.setFlags(268435456);
        intent.setPackage("com.android.systemui");
        intent.putExtras(bundle);
        return intent;
    }

    private static void setBatterySaverConfirmationAcknowledged(Context context) {
        Settings.Secure.putIntForUser(context.getContentResolver(), "low_power_warning_acknowledged", 1, -2);
    }

    public static void suppressAutoBatterySaver(Context context) {
        Settings.Secure.putInt(context.getContentResolver(), "suppress_auto_battery_saver_suggestion", 1);
    }

    public static void revertScheduleToNoneIfNeeded(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        int i = Settings.Global.getInt(contentResolver, "automatic_power_save_mode", 0);
        boolean z = !TextUtils.isEmpty(context.getString(17039859));
        if (i == 1 && !z) {
            Settings.Global.putInt(contentResolver, "low_power_trigger_level", 0);
            Settings.Global.putInt(contentResolver, "automatic_power_save_mode", 0);
        }
    }
}
