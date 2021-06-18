package com.google.android.settings.fuelgauge;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.BatteryStatsManager;
import android.os.BatteryUsageStats;
import android.os.BatteryUsageStatsQuery;
import android.util.Log;
import com.android.settings.SettingsActivity;
import com.android.settings.core.InstrumentedPreferenceFragment;
import com.android.settings.fuelgauge.BatteryAppListPreferenceController;
import com.android.settings.fuelgauge.BatteryEntry;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.List;

public final class BatteryBroadcastReceiver extends BroadcastReceiver {
    BatteryAppListPreferenceController mController;

    public void onReceive(Context context, Intent intent) {
        String action = intent != null ? intent.getAction() : null;
        if (!"settings.intelligence.battery.action.FETCH_BATTERY_USAGE_DATA".equals(action)) {
            Log.w("BatteryBroadcastReceiver", "receive unexpected intent:" + action);
            return;
        }
        Log.d("BatteryBroadcastReceiver", "onReceive():" + action);
        AsyncTask.execute(new BatteryBroadcastReceiver$$ExternalSyntheticLambda0(this, context, goAsync()));
    }

    /* access modifiers changed from: private */
    /* renamed from: loadUsageData */
    public void lambda$onReceive$0(Context context, BroadcastReceiver.PendingResult pendingResult) {
        if (!DatabaseUtils.isContentProviderEnabled(context)) {
            Log.w("BatteryBroadcastReceiver", "battery usage content provider is disabled!");
            pendingResult.finish();
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        BatteryUsageStats batteryUsageStats = ((BatteryStatsManager) context.getSystemService(BatteryStatsManager.class)).getBatteryUsageStats(new BatteryUsageStatsQuery.Builder().includeBatteryHistory().build());
        if (batteryUsageStats == null) {
            Log.w("BatteryBroadcastReceiver", "getBatteryUsageStats() returns null content");
        }
        BatteryAppListPreferenceController batteryAppListPreferenceController = this.mController;
        if (batteryAppListPreferenceController == null) {
            batteryAppListPreferenceController = new BatteryAppListPreferenceController(context, (String) null, (Lifecycle) null, (SettingsActivity) null, (InstrumentedPreferenceFragment) null);
        }
        List<BatteryEntry> batteryEntryList = batteryUsageStats != null ? batteryAppListPreferenceController.getBatteryEntryList(batteryUsageStats, true) : null;
        if (batteryEntryList == null || batteryEntryList.isEmpty()) {
            Log.w("BatteryBroadcastReceiver", "getBatteryEntryList() returns null or empty content");
        }
        Log.d("BatteryBroadcastReceiver", String.format("getBatteryUsageStats() in %d/ms", new Object[]{Long.valueOf(System.currentTimeMillis() - currentTimeMillis)}));
        DatabaseUtils.sendBatteryEntryData(context, batteryEntryList, batteryUsageStats);
        pendingResult.finish();
    }
}
