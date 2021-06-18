package com.android.settings.network.telephony;

import android.content.Context;
import android.telephony.SignalStrength;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.util.ArraySet;
import com.google.common.collect.Sets;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class SignalStrengthListener {
    private TelephonyManager mBaseTelephonyManager;
    /* access modifiers changed from: private */
    public Callback mCallback;
    private Context mContext;
    Map<Integer, SignalStrengthTelephonyCallback> mTelephonyCallbacks = new TreeMap();

    public interface Callback {
        void onSignalStrengthChanged();
    }

    public SignalStrengthListener(Context context, Callback callback) {
        this.mBaseTelephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
        this.mCallback = callback;
        this.mContext = context;
    }

    public void resume() {
        for (Integer intValue : this.mTelephonyCallbacks.keySet()) {
            startListening(intValue.intValue());
        }
    }

    public void pause() {
        for (Integer intValue : this.mTelephonyCallbacks.keySet()) {
            stopListening(intValue.intValue());
        }
    }

    public void updateSubscriptionIds(Set<Integer> set) {
        ArraySet arraySet = new ArraySet(this.mTelephonyCallbacks.keySet());
        UnmodifiableIterator<Integer> it = Sets.difference(arraySet, set).iterator();
        while (it.hasNext()) {
            int intValue = it.next().intValue();
            stopListening(intValue);
            this.mTelephonyCallbacks.remove(Integer.valueOf(intValue));
        }
        UnmodifiableIterator<Integer> it2 = Sets.difference(set, arraySet).iterator();
        while (it2.hasNext()) {
            int intValue2 = it2.next().intValue();
            this.mTelephonyCallbacks.put(Integer.valueOf(intValue2), new SignalStrengthTelephonyCallback());
            startListening(intValue2);
        }
    }

    class SignalStrengthTelephonyCallback extends TelephonyCallback implements TelephonyCallback.SignalStrengthsListener {
        SignalStrengthTelephonyCallback() {
        }

        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            SignalStrengthListener.this.mCallback.onSignalStrengthChanged();
        }
    }

    private void startListening(int i) {
        this.mBaseTelephonyManager.createForSubscriptionId(i).registerTelephonyCallback(this.mContext.getMainExecutor(), this.mTelephonyCallbacks.get(Integer.valueOf(i)));
    }

    private void stopListening(int i) {
        this.mBaseTelephonyManager.createForSubscriptionId(i).unregisterTelephonyCallback(this.mTelephonyCallbacks.get(Integer.valueOf(i)));
    }
}
