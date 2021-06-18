package com.android.settings.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.util.Log;
import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.Slice;
import androidx.slice.builders.ListBuilder;
import androidx.slice.builders.SliceAction;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.slices.CustomSliceRegistry;
import com.android.settings.slices.CustomSliceable;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.slices.SliceBroadcastReceiver;

public class TurnOnWifiSlice implements CustomSliceable {
    private final Context mContext;
    private final WifiManager mWifiManager;

    public TurnOnWifiSlice(Context context) {
        this.mContext = context;
        this.mWifiManager = (WifiManager) context.getSystemService(WifiManager.class);
    }

    private static void logd(String str) {
        Log.d("TurnOnWifiSlice", str);
    }

    public Slice getSlice() {
        if (this.mWifiManager.isWifiEnabled()) {
            return null;
        }
        String charSequence = this.mContext.getText(R.string.turn_on_wifi).toString();
        return new ListBuilder(this.mContext, getUri(), -1).addRow(new ListBuilder.RowBuilder().setTitle(charSequence).addEndItem(getEndIcon(), 0).setPrimaryAction(SliceAction.create(getBroadcastIntent(this.mContext), getEndIcon(), 0, charSequence))).build();
    }

    public Uri getUri() {
        return CustomSliceRegistry.TURN_ON_WIFI_SLICE_URI;
    }

    public void onNotifyChange(Intent intent) {
        logd("Action: turn on Wi-Fi networks");
        this.mWifiManager.setWifiEnabled(true);
    }

    public Intent getIntent() {
        return new Intent(getUri().toString()).setData(getUri()).setClass(this.mContext, SliceBroadcastReceiver.class);
    }

    private IconCompat getEndIcon() {
        Drawable drawable = this.mContext.getDrawable(R.drawable.ic_settings_wireless);
        if (drawable == null) {
            return Utils.createIconWithDrawable(new ColorDrawable(0));
        }
        drawable.setTintList(com.android.settingslib.Utils.getColorAttr(this.mContext, 16843829));
        return Utils.createIconWithDrawable(drawable);
    }

    public Class getBackgroundWorkerClass() {
        return TurnOnWifiWorker.class;
    }

    public static class TurnOnWifiWorker extends SliceBackgroundWorker {
        private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if ("android.net.wifi.WIFI_STATE_CHANGED".equals(intent.getAction())) {
                    TurnOnWifiWorker.this.notifySliceChange();
                }
            }
        };
        private final IntentFilter mIntentFilter = new IntentFilter("android.net.wifi.WIFI_STATE_CHANGED");

        public void close() {
        }

        public TurnOnWifiWorker(Context context, Uri uri) {
            super(context, uri);
        }

        /* access modifiers changed from: protected */
        public void onSlicePinned() {
            getContext().registerReceiver(this.mBroadcastReceiver, this.mIntentFilter);
        }

        /* access modifiers changed from: protected */
        public void onSliceUnpinned() {
            getContext().unregisterReceiver(this.mBroadcastReceiver);
        }
    }
}
