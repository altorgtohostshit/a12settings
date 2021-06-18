package com.google.android.settings.accessibility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.android.settings.notification.AudioHelper;

public class HapticsRingReceiverHelper {
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public final C1786H mHandler = new C1786H();
    /* access modifiers changed from: private */
    public AudioHelper mHelper;
    private final RingReceiver mReceiver = new RingReceiver();
    /* access modifiers changed from: private */
    public int mRingerMode;

    public void onChange() {
        throw null;
    }

    public HapticsRingReceiverHelper(Context context) {
        this.mContext = context;
        AudioHelper audioHelper = new AudioHelper(context);
        this.mHelper = audioHelper;
        this.mRingerMode = audioHelper.getRingerModeInternal();
    }

    public boolean isRingerModeSilent() {
        return this.mHelper.getRingerModeInternal() == 0;
    }

    public int getRingerMode() {
        return this.mHelper.getRingerModeInternal();
    }

    public void register(boolean z) {
        this.mReceiver.register(z);
    }

    /* renamed from: com.google.android.settings.accessibility.HapticsRingReceiverHelper$H */
    private final class C1786H extends Handler {
        private C1786H() {
            super(Looper.getMainLooper());
        }

        public void handleMessage(Message message) {
            if (message.what == 2) {
                HapticsRingReceiverHelper.this.onChange();
            }
        }
    }

    private class RingReceiver extends BroadcastReceiver {
        private boolean mRegistered;

        private RingReceiver() {
        }

        public void register(boolean z) {
            if (this.mRegistered != z) {
                if (z) {
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction("android.media.INTERNAL_RINGER_MODE_CHANGED_ACTION");
                    HapticsRingReceiverHelper.this.mContext.registerReceiver(this, intentFilter);
                } else {
                    HapticsRingReceiverHelper.this.mContext.unregisterReceiver(this);
                }
                this.mRegistered = z;
            }
        }

        public void onReceive(Context context, Intent intent) {
            int ringerModeInternal;
            if ("android.media.INTERNAL_RINGER_MODE_CHANGED_ACTION".equals(intent.getAction()) && (ringerModeInternal = HapticsRingReceiverHelper.this.mHelper.getRingerModeInternal()) != HapticsRingReceiverHelper.this.mRingerMode) {
                HapticsRingReceiverHelper.this.mHandler.sendEmptyMessage(2);
                int unused = HapticsRingReceiverHelper.this.mRingerMode = ringerModeInternal;
            }
        }
    }
}
