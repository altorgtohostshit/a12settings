package com.android.settings.network.telephony;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.Slice;
import androidx.slice.builders.ListBuilder;
import androidx.slice.builders.SliceAction;
import com.android.settings.R;
import com.android.settings.network.MobileDataContentObserver;
import com.android.settings.network.SubscriptionUtil;
import com.android.settings.slices.CustomSliceRegistry;
import com.android.settings.slices.CustomSliceable;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.Utils;
import com.android.settingslib.WirelessUtils;
import java.io.IOException;
import java.util.List;

public class MobileDataSlice implements CustomSliceable {
    private final Context mContext;
    private final SubscriptionManager mSubscriptionManager;
    private final TelephonyManager mTelephonyManager;

    public MobileDataSlice(Context context) {
        this.mContext = context;
        this.mSubscriptionManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
        this.mTelephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
    }

    public Slice getSlice() {
        IconCompat createWithResource = IconCompat.createWithResource(this.mContext, R.drawable.ic_network_cell);
        String charSequence = this.mContext.getText(R.string.mobile_data_settings_title).toString();
        int colorAccentDefaultColor = Utils.getColorAccentDefaultColor(this.mContext);
        if (isAirplaneModeEnabled() || !isMobileDataAvailable()) {
            return null;
        }
        CharSequence summary = getSummary();
        PendingIntent broadcastIntent = getBroadcastIntent(this.mContext);
        ListBuilder.RowBuilder primaryAction = new ListBuilder.RowBuilder().setTitle(charSequence).addEndItem(SliceAction.createToggle(broadcastIntent, (CharSequence) null, isMobileDataEnabled())).setPrimaryAction(SliceAction.createDeeplink(getPrimaryAction(), createWithResource, 0, charSequence));
        if (!com.android.settings.Utils.isSettingsIntelligence(this.mContext)) {
            primaryAction.setSubtitle(summary);
        }
        return new ListBuilder(this.mContext, getUri(), -1).setAccentColor(colorAccentDefaultColor).addRow(primaryAction).build();
    }

    public Uri getUri() {
        return CustomSliceRegistry.MOBILE_DATA_SLICE_URI;
    }

    public void onNotifyChange(Intent intent) {
        boolean booleanExtra = intent.getBooleanExtra("android.app.slice.extra.TOGGLE_STATE", isMobileDataEnabled());
        int defaultSubscriptionId = getDefaultSubscriptionId(this.mSubscriptionManager);
        if (defaultSubscriptionId != -1) {
            MobileNetworkUtils.setMobileDataEnabled(this.mContext, defaultSubscriptionId, booleanExtra, false);
        }
    }

    public IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
        return intentFilter;
    }

    public Intent getIntent() {
        return new Intent(this.mContext, MobileNetworkActivity.class);
    }

    public Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return MobileDataWorker.class;
    }

    protected static int getDefaultSubscriptionId(SubscriptionManager subscriptionManager) {
        SubscriptionInfo activeSubscriptionInfo = subscriptionManager.getActiveSubscriptionInfo(SubscriptionManager.getDefaultDataSubscriptionId());
        if (activeSubscriptionInfo == null) {
            return -1;
        }
        return activeSubscriptionInfo.getSubscriptionId();
    }

    private CharSequence getSummary() {
        SubscriptionInfo activeSubscriptionInfo = this.mSubscriptionManager.getActiveSubscriptionInfo(SubscriptionManager.getDefaultDataSubscriptionId());
        if (activeSubscriptionInfo == null) {
            return null;
        }
        return SubscriptionUtil.getUniqueSubscriptionDisplayName(activeSubscriptionInfo, this.mContext);
    }

    private PendingIntent getPrimaryAction() {
        return PendingIntent.getActivity(this.mContext, 0, getIntent(), 67108864);
    }

    private boolean isMobileDataAvailable() {
        List<SubscriptionInfo> selectableSubscriptionInfoList = SubscriptionUtil.getSelectableSubscriptionInfoList(this.mContext);
        return selectableSubscriptionInfoList != null && !selectableSubscriptionInfoList.isEmpty();
    }

    /* access modifiers changed from: package-private */
    public boolean isAirplaneModeEnabled() {
        return WirelessUtils.isAirplaneModeOn(this.mContext);
    }

    /* access modifiers changed from: package-private */
    public boolean isMobileDataEnabled() {
        TelephonyManager telephonyManager = this.mTelephonyManager;
        if (telephonyManager == null) {
            return false;
        }
        return telephonyManager.isDataEnabled();
    }

    public static class MobileDataWorker extends SliceBackgroundWorker<Void> {
        DataContentObserver mMobileDataObserver = new DataContentObserver(new Handler(Looper.getMainLooper()), this);

        public MobileDataWorker(Context context, Uri uri) {
            super(context, uri);
        }

        /* access modifiers changed from: protected */
        public void onSlicePinned() {
            this.mMobileDataObserver.register(getContext(), MobileDataSlice.getDefaultSubscriptionId((SubscriptionManager) getContext().getSystemService(SubscriptionManager.class)));
        }

        /* access modifiers changed from: protected */
        public void onSliceUnpinned() {
            this.mMobileDataObserver.unRegister(getContext());
        }

        public void close() throws IOException {
            this.mMobileDataObserver = null;
        }

        public void updateSlice() {
            notifySliceChange();
        }

        public class DataContentObserver extends ContentObserver {
            private final MobileDataWorker mSliceBackgroundWorker;

            public DataContentObserver(Handler handler, MobileDataWorker mobileDataWorker) {
                super(handler);
                this.mSliceBackgroundWorker = mobileDataWorker;
            }

            public void onChange(boolean z) {
                this.mSliceBackgroundWorker.updateSlice();
            }

            public void register(Context context, int i) {
                context.getContentResolver().registerContentObserver(MobileDataContentObserver.getObservableUri(context, i), false, this);
            }

            public void unRegister(Context context) {
                context.getContentResolver().unregisterContentObserver(this);
            }
        }
    }
}
