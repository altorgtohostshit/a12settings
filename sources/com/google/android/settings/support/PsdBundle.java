package com.google.android.settings.support;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.BatteryStatsManager;
import android.os.BatteryUsageStats;
import android.os.UidBatteryConsumer;
import com.android.settings.bluetooth.Utils;
import com.android.settings.fuelgauge.BatteryUtils;
import com.android.settings.fuelgauge.batterytip.AppInfo;
import com.android.settings.fuelgauge.batterytip.BatteryTipPolicy;
import com.android.settings.fuelgauge.batterytip.BatteryTipUtils;
import com.android.settingslib.bluetooth.A2dpProfile;
import com.android.settingslib.bluetooth.HeadsetProfile;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.bluetooth.LocalBluetoothProfile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class PsdBundle {
    /* access modifiers changed from: private */
    public static final String[] TELEPHONY_PROJECTION = {"KEY", "VALUE"};
    private final String[] mKeys;
    private final String[] mValues;

    private PsdBundle(Builder builder) {
        this.mKeys = (String[]) builder.mSignalKeys.toArray(new String[builder.mSignalKeys.size()]);
        this.mValues = (String[]) builder.mSignalValues.toArray(new String[builder.mSignalValues.size()]);
    }

    public String[] getKeys() {
        return this.mKeys;
    }

    public String[] getValues() {
        return this.mValues;
    }

    public static class Builder {
        private final String CALL_STATISTICS_PATH;
        private final String DIAGNOSTICS_PATH;
        private final String TELEPHONY_AUTHORITY;
        private BatteryUsageStats mBatteryUsageStats;
        private BatteryUtils mBatteryUtils;
        private final Context mContext;
        private long mPsdValuesSize;
        private long mPsdValuesSizeLimit;
        /* access modifiers changed from: private */
        public List<String> mSignalKeys;
        /* access modifiers changed from: private */
        public List<String> mSignalValues;

        public Builder(Context context, long j) {
            this(context, j, BatteryUtils.getInstance(context));
        }

        Builder(Context context, long j, BatteryUtils batteryUtils) {
            this.mSignalKeys = new ArrayList();
            this.mSignalValues = new ArrayList();
            this.mPsdValuesSize = 0;
            this.TELEPHONY_AUTHORITY = "com.google.android.connectivitymonitor.troubleshooterprovider";
            this.CALL_STATISTICS_PATH = "call_statistics";
            this.DIAGNOSTICS_PATH = "diagnostics";
            this.mContext = context;
            this.mPsdValuesSizeLimit = j;
            this.mBatteryUtils = batteryUtils;
        }

        public PsdBundle build() {
            return new PsdBundle(this);
        }

        public Builder addSignal(String str, String str2) {
            this.mSignalKeys.add(str);
            if (str2 != null) {
                long length = (long) str2.length();
                if (this.mPsdValuesSize + length <= this.mPsdValuesSizeLimit) {
                    this.mSignalValues.add(str2);
                    this.mPsdValuesSize += length;
                    return this;
                }
            }
            this.mSignalValues.add("");
            return this;
        }

        public Builder addTopBatteryApps() {
            BatteryUsageStats batteryUsageStats = getBatteryUsageStats();
            int dischargePercentage = batteryUsageStats.getDischargePercentage();
            double consumedPower = batteryUsageStats.getConsumedPower();
            double d = consumedPower / ((double) dischargePercentage);
            List<UidBatteryConsumer> uidBatteryConsumers = batteryUsageStats.getUidBatteryConsumers();
            uidBatteryConsumers.sort(PsdBundle$Builder$$ExternalSyntheticLambda0.INSTANCE);
            StringBuilder sb = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            for (UidBatteryConsumer uidBatteryConsumer : uidBatteryConsumers) {
                if (uidBatteryConsumer.getConsumedPower() >= d && !this.mBatteryUtils.shouldHideUidBatteryConsumer(uidBatteryConsumer)) {
                    String packageName = this.mBatteryUtils.getPackageName(uidBatteryConsumer.getUid());
                    sb2.append((int) this.mBatteryUtils.calculateBatteryPercent(uidBatteryConsumer.getConsumedPower(), consumedPower, dischargePercentage));
                    sb2.append(',');
                    sb.append(packageName);
                    sb.append(',');
                }
            }
            int length = sb.length();
            int length2 = sb2.length();
            if (length > 0) {
                sb.setLength(length - 1);
                sb2.setLength(length2 - 1);
            }
            addSignal("noe_battery_usage_names", sb.toString());
            addSignal("noe_battery_usage_percentages", sb2.toString());
            return this;
        }

        public Builder addBatteryAnomalyApps() {
            StringBuilder sb = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            List<AppInfo> anomalyAppList = getAnomalyAppList();
            int size = anomalyAppList.size();
            for (int i = 0; i < size; i++) {
                AppInfo appInfo = anomalyAppList.get(i);
                Iterator<Integer> it = appInfo.anomalyTypes.iterator();
                while (it.hasNext()) {
                    sb.append(appInfo.packageName);
                    sb.append(',');
                    sb2.append(it.next());
                    sb2.append(',');
                }
            }
            if (sb.length() != 0) {
                sb.setLength(sb.length() - 1);
                sb2.setLength(sb2.length() - 1);
            }
            addSignal("noe_battery_anomaly_names", sb.toString());
            addSignal("noe_battery_anomaly_types", sb2.toString());
            return this;
        }

        public Builder addConnectedBluetoothDevicesSignals() {
            Set<BluetoothDevice> bondedBtDevices = getBondedBtDevices(this.mContext);
            StringBuilder[] sbArr = {new StringBuilder(), new StringBuilder(), new StringBuilder()};
            LocalBluetoothManager localBtManager = getLocalBtManager(this.mContext);
            for (BluetoothDevice next : bondedBtDevices) {
                if (next.isConnected()) {
                    processBTDevice(localBtManager, sbArr, next);
                }
            }
            for (int i = 0; i < 3; i++) {
                StringBuilder sb = sbArr[i];
                if (sb.length() > 0) {
                    sb.deleteCharAt(sb.length() - 1);
                }
            }
            String sb2 = sbArr[0].toString();
            String sb3 = sbArr[1].toString();
            String sb4 = sbArr[2].toString();
            addSignal("noe_connected_bluetooth_devices", sb2);
            addSignal("noe_connected_bluetooth_devices_headset", sb3);
            addSignal("noe_connected_bluetooth_devices_a2dp", sb4);
            return this;
        }

        public Builder addPairedBluetoothDevices() {
            Set<BluetoothDevice> bondedBtDevices = getBondedBtDevices(this.mContext);
            StringBuilder sb = new StringBuilder();
            for (BluetoothDevice alias : bondedBtDevices) {
                sb.append(getEncodedName(alias.getAlias()));
                sb.append(',');
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            addSignal("noe_paired_bluetooth_devices", sb.toString());
            return this;
        }

        public Builder addTelephonyTroubleshooterStatisticsSignals() {
            addSignal("noe_telephony_stats_signal_flag", "");
            return addTelephonyCursorSignals("call_statistics");
        }

        public Builder addTelephonyTroubleshooterDiagnosticSignals() {
            addSignal("noe_telephony_diagnostic_signal_flag", "");
            return addTelephonyCursorSignals("diagnostics");
        }

        /* access modifiers changed from: package-private */
        public List<AppInfo> getAnomalyAppList() {
            return BatteryTipUtils.detectAnomalies(this.mContext, System.currentTimeMillis() - TimeUnit.HOURS.toMillis((long) new BatteryTipPolicy(this.mContext).appRestrictionActiveHour));
        }

        private Builder addTelephonyCursorSignals(String str) {
            Cursor query = this.mContext.getContentResolver().query(new Uri.Builder().scheme("content").authority("com.google.android.connectivitymonitor.troubleshooterprovider").path(str).build(), PsdBundle.TELEPHONY_PROJECTION, (String) null, (String[]) null, (String) null);
            if (query == null) {
                if (query != null) {
                    query.close();
                }
                return this;
            }
            try {
                String[] columnNames = query.getColumnNames();
                if (columnNames != null) {
                    if (columnNames.length != 0) {
                        if (!query.moveToFirst()) {
                            query.close();
                            return this;
                        }
                        int columnIndex = query.getColumnIndex("KEY");
                        int columnIndex2 = query.getColumnIndex("VALUE");
                        if (columnIndex2 != -1) {
                            if (columnIndex != -1) {
                                do {
                                    addSignal(query.getString(columnIndex), query.getString(columnIndex2));
                                } while (query.moveToNext());
                                query.close();
                                return this;
                            }
                        }
                        query.close();
                        return this;
                    }
                }
                query.close();
                return this;
            } catch (Throwable th) {
                th.addSuppressed(th);
            }
            throw th;
        }

        private BatteryUsageStats getBatteryUsageStats() {
            if (this.mBatteryUsageStats == null) {
                this.mBatteryUsageStats = ((BatteryStatsManager) this.mContext.getSystemService(BatteryStatsManager.class)).getBatteryUsageStats();
            }
            return this.mBatteryUsageStats;
        }

        private void processBTDevice(LocalBluetoothManager localBluetoothManager, StringBuilder[] sbArr, BluetoothDevice bluetoothDevice) {
            boolean z;
            boolean z2;
            if (localBluetoothManager != null) {
                List<LocalBluetoothProfile> connectableProfiles = localBluetoothManager.getCachedDeviceManager().findDevice(bluetoothDevice).getConnectableProfiles();
                z2 = false;
                z = false;
                for (int i = 0; i < connectableProfiles.size(); i++) {
                    LocalBluetoothProfile localBluetoothProfile = connectableProfiles.get(i);
                    if ((localBluetoothProfile instanceof HeadsetProfile) && localBluetoothProfile.isEnabled(bluetoothDevice)) {
                        z2 = true;
                    }
                    if ((localBluetoothProfile instanceof A2dpProfile) && localBluetoothProfile.isEnabled(bluetoothDevice)) {
                        z = true;
                    }
                }
            } else {
                z2 = false;
                z = false;
            }
            StringBuilder sb = sbArr[0];
            sb.append(getEncodedName(bluetoothDevice.getAlias()));
            sb.append(',');
            StringBuilder sb2 = sbArr[1];
            sb2.append(z2);
            sb2.append(',');
            StringBuilder sb3 = sbArr[2];
            sb3.append(z);
            sb3.append(',');
        }

        private String getEncodedName(String str) {
            return Uri.encode(str);
        }

        private LocalBluetoothManager getLocalBtManager(Context context) {
            return Utils.getLocalBtManager(context);
        }

        private Set<BluetoothDevice> getBondedBtDevices(Context context) {
            BluetoothAdapter adapter;
            BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService("bluetooth");
            if (bluetoothManager == null || (adapter = bluetoothManager.getAdapter()) == null) {
                return null;
            }
            return adapter.getBondedDevices();
        }
    }
}
