package com.android.settings.bluetooth;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Pair;
import androidx.preference.Preference;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;

public class BluetoothFilesPreferenceController extends BasePreferenceController implements PreferenceControllerMixin {
    static final String ACTION_OPEN_FILES = "com.android.bluetooth.action.TransferHistory";
    static final String EXTRA_DIRECTION = "direction";
    static final String EXTRA_SHOW_ALL_FILES = "android.btopp.intent.extra.SHOW_ALL";
    public static final String KEY_RECEIVED_FILES = "bt_received_files";
    private static final String TAG = "BluetoothFilesPrefCtrl";
    private MetricsFeatureProvider mMetricsFeatureProvider;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public String getPreferenceKey() {
        return KEY_RECEIVED_FILES;
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public BluetoothFilesPreferenceController(Context context) {
        super(context, KEY_RECEIVED_FILES);
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(context).getMetricsFeatureProvider();
    }

    public int getAvailabilityStatus() {
        return this.mContext.getPackageManager().hasSystemFeature("android.hardware.bluetooth") ? 0 : 3;
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!KEY_RECEIVED_FILES.equals(preference.getKey())) {
            return false;
        }
        this.mMetricsFeatureProvider.action(this.mContext, 162, (Pair<Integer, Object>[]) new Pair[0]);
        Intent intent = new Intent(ACTION_OPEN_FILES);
        intent.setFlags(335544320);
        intent.putExtra(EXTRA_DIRECTION, 1);
        intent.putExtra(EXTRA_SHOW_ALL_FILES, true);
        this.mContext.startActivity(intent);
        return true;
    }
}
