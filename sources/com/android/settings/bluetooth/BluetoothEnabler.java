package com.android.settings.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;
import com.android.settings.R;
import com.android.settings.widget.SwitchWidgetController;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.WirelessUtils;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;

public final class BluetoothEnabler implements SwitchWidgetController.OnSwitchChangeListener {
    private final BluetoothAdapter mBluetoothAdapter;
    private SwitchWidgetController.OnSwitchChangeListener mCallback;
    private Context mContext;
    private final IntentFilter mIntentFilter;
    private final int mMetricsEvent;
    private final MetricsFeatureProvider mMetricsFeatureProvider;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            BluetoothEnabler.this.handleStateChanged(intent.getIntExtra("android.bluetooth.adapter.extra.STATE", Integer.MIN_VALUE));
        }
    };
    private final RestrictionUtils mRestrictionUtils;
    private final SwitchWidgetController mSwitchController;
    private boolean mValidListener;

    public BluetoothEnabler(Context context, SwitchWidgetController switchWidgetController, MetricsFeatureProvider metricsFeatureProvider, int i, RestrictionUtils restrictionUtils) {
        this.mContext = context;
        this.mMetricsFeatureProvider = metricsFeatureProvider;
        this.mSwitchController = switchWidgetController;
        switchWidgetController.setListener(this);
        switchWidgetController.setTitle(context.getString(R.string.bluetooth_main_switch_title));
        this.mValidListener = false;
        this.mMetricsEvent = i;
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mBluetoothAdapter = defaultAdapter;
        if (defaultAdapter == null) {
            switchWidgetController.setEnabled(false);
        }
        this.mIntentFilter = new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED");
        this.mRestrictionUtils = restrictionUtils;
    }

    public void resume(Context context) {
        if (this.mContext != context) {
            this.mContext = context;
        }
        boolean maybeEnforceRestrictions = maybeEnforceRestrictions();
        BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
        if (bluetoothAdapter == null) {
            this.mSwitchController.setEnabled(false);
            return;
        }
        if (!maybeEnforceRestrictions) {
            handleStateChanged(bluetoothAdapter.getState());
        }
        this.mSwitchController.startListening();
        this.mContext.registerReceiver(this.mReceiver, this.mIntentFilter);
        this.mValidListener = true;
    }

    public void pause() {
        if (this.mBluetoothAdapter != null && this.mValidListener) {
            this.mSwitchController.stopListening();
            this.mContext.unregisterReceiver(this.mReceiver);
            this.mValidListener = false;
        }
    }

    /* access modifiers changed from: package-private */
    public void handleStateChanged(int i) {
        switch (i) {
            case 10:
                setChecked(false);
                this.mSwitchController.setEnabled(true);
                return;
            case 11:
                this.mSwitchController.setEnabled(false);
                return;
            case 12:
                setChecked(true);
                this.mSwitchController.setEnabled(true);
                return;
            case 13:
                this.mSwitchController.setEnabled(false);
                return;
            default:
                setChecked(false);
                this.mSwitchController.setEnabled(true);
                return;
        }
    }

    private void setChecked(boolean z) {
        if (z != this.mSwitchController.isChecked()) {
            if (this.mValidListener) {
                this.mSwitchController.stopListening();
            }
            this.mSwitchController.setChecked(z);
            if (this.mValidListener) {
                this.mSwitchController.startListening();
            }
        }
    }

    public boolean onSwitchToggled(boolean z) {
        if (maybeEnforceRestrictions()) {
            triggerParentPreferenceCallback(z);
            return true;
        } else if (!z || WirelessUtils.isRadioAllowed(this.mContext, "bluetooth")) {
            this.mMetricsFeatureProvider.action(this.mContext, this.mMetricsEvent, z);
            if (this.mBluetoothAdapter != null) {
                boolean bluetoothEnabled = setBluetoothEnabled(z);
                if (z && !bluetoothEnabled) {
                    this.mSwitchController.setChecked(false);
                    this.mSwitchController.setEnabled(true);
                    triggerParentPreferenceCallback(false);
                    return false;
                }
            }
            this.mSwitchController.setEnabled(false);
            triggerParentPreferenceCallback(z);
            return true;
        } else {
            Toast.makeText(this.mContext, R.string.wifi_in_airplane_mode, 0).show();
            this.mSwitchController.setChecked(false);
            triggerParentPreferenceCallback(false);
            return false;
        }
    }

    public void setToggleCallback(SwitchWidgetController.OnSwitchChangeListener onSwitchChangeListener) {
        this.mCallback = onSwitchChangeListener;
    }

    /* access modifiers changed from: package-private */
    public boolean maybeEnforceRestrictions() {
        RestrictedLockUtils.EnforcedAdmin enforcedAdmin = getEnforcedAdmin(this.mRestrictionUtils, this.mContext);
        this.mSwitchController.setDisabledByAdmin(enforcedAdmin);
        if (enforcedAdmin != null) {
            this.mSwitchController.setChecked(false);
            this.mSwitchController.setEnabled(false);
        }
        if (enforcedAdmin != null) {
            return true;
        }
        return false;
    }

    public static RestrictedLockUtils.EnforcedAdmin getEnforcedAdmin(RestrictionUtils restrictionUtils, Context context) {
        RestrictedLockUtils.EnforcedAdmin checkIfRestrictionEnforced = restrictionUtils.checkIfRestrictionEnforced(context, "no_bluetooth");
        return checkIfRestrictionEnforced == null ? restrictionUtils.checkIfRestrictionEnforced(context, "no_config_bluetooth") : checkIfRestrictionEnforced;
    }

    private void triggerParentPreferenceCallback(boolean z) {
        SwitchWidgetController.OnSwitchChangeListener onSwitchChangeListener = this.mCallback;
        if (onSwitchChangeListener != null) {
            onSwitchChangeListener.onSwitchToggled(z);
        }
    }

    private boolean setBluetoothEnabled(boolean z) {
        BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
        return z ? bluetoothAdapter.enable() : bluetoothAdapter.disable();
    }
}
