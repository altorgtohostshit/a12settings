package com.android.settings.nfc;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.nfc.NfcPaymentPreference;
import com.android.settings.nfc.PaymentBackend;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import java.util.List;

public class NfcPaymentPreferenceController extends BasePreferenceController implements PaymentBackend.Callback, View.OnClickListener, NfcPaymentPreference.Listener, LifecycleObserver, OnStart, OnStop {
    private static final String TAG = "NfcPaymentController";
    private final NfcPaymentAdapter mAdapter;
    /* access modifiers changed from: private */
    public PaymentBackend mPaymentBackend;
    /* access modifiers changed from: private */
    public NfcPaymentPreference mPreference;
    private ImageView mSettingsButtonView;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
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

    public NfcPaymentPreferenceController(Context context, String str) {
        super(context, str);
        this.mAdapter = new NfcPaymentAdapter(context);
    }

    public void setPaymentBackend(PaymentBackend paymentBackend) {
        this.mPaymentBackend = paymentBackend;
    }

    public void onStart() {
        PaymentBackend paymentBackend = this.mPaymentBackend;
        if (paymentBackend != null) {
            paymentBackend.registerCallback(this);
        }
    }

    public void onStop() {
        PaymentBackend paymentBackend = this.mPaymentBackend;
        if (paymentBackend != null) {
            paymentBackend.unregisterCallback(this);
        }
    }

    public int getAvailabilityStatus() {
        if (!this.mContext.getPackageManager().hasSystemFeature("android.hardware.nfc") || NfcAdapter.getDefaultAdapter(this.mContext) == null) {
            return 3;
        }
        if (this.mPaymentBackend == null) {
            this.mPaymentBackend = new PaymentBackend(this.mContext);
        }
        List<PaymentBackend.PaymentAppInfo> paymentAppInfos = this.mPaymentBackend.getPaymentAppInfos();
        if (paymentAppInfos == null || paymentAppInfos.isEmpty()) {
            return 3;
        }
        return 0;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        NfcPaymentPreference nfcPaymentPreference = (NfcPaymentPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = nfcPaymentPreference;
        if (nfcPaymentPreference != null) {
            nfcPaymentPreference.initialize(this);
        }
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        ImageView imageView = (ImageView) preferenceViewHolder.findViewById(R.id.settings_button);
        this.mSettingsButtonView = imageView;
        imageView.setOnClickListener(this);
        updateSettingsVisibility();
    }

    public void updateState(Preference preference) {
        List<PaymentBackend.PaymentAppInfo> paymentAppInfos = this.mPaymentBackend.getPaymentAppInfos();
        if (paymentAppInfos != null) {
            this.mAdapter.updateApps((PaymentBackend.PaymentAppInfo[]) paymentAppInfos.toArray(new PaymentBackend.PaymentAppInfo[paymentAppInfos.size()]));
        }
        super.updateState(preference);
        updateSettingsVisibility();
    }

    public CharSequence getSummary() {
        PaymentBackend.PaymentAppInfo defaultApp = this.mPaymentBackend.getDefaultApp();
        if (defaultApp != null) {
            return defaultApp.label;
        }
        return this.mContext.getText(R.string.nfc_payment_default_not_set);
    }

    public void onPrepareDialogBuilder(AlertDialog.Builder builder, DialogInterface.OnClickListener onClickListener) {
        builder.setSingleChoiceItems((ListAdapter) this.mAdapter, 0, onClickListener);
    }

    public void onPaymentAppsChanged() {
        updateState(this.mPreference);
    }

    public void onClick(View view) {
        PaymentBackend.PaymentAppInfo defaultApp = this.mPaymentBackend.getDefaultApp();
        if (defaultApp != null && defaultApp.settingsComponent != null) {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.setComponent(defaultApp.settingsComponent);
            intent.addFlags(268435456);
            try {
                this.mContext.startActivity(intent);
            } catch (ActivityNotFoundException unused) {
                Log.e(TAG, "Settings activity not found.");
            }
        }
    }

    private void updateSettingsVisibility() {
        if (this.mSettingsButtonView != null) {
            PaymentBackend.PaymentAppInfo defaultApp = this.mPaymentBackend.getDefaultApp();
            if (defaultApp == null || defaultApp.settingsComponent == null) {
                this.mSettingsButtonView.setVisibility(8);
            } else {
                this.mSettingsButtonView.setVisibility(0);
            }
        }
    }

    private class NfcPaymentAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
        private PaymentBackend.PaymentAppInfo[] appInfos;
        private final LayoutInflater mLayoutInflater;

        public NfcPaymentAdapter(Context context) {
            this.mLayoutInflater = (LayoutInflater) context.getSystemService("layout_inflater");
        }

        public void updateApps(PaymentBackend.PaymentAppInfo[] paymentAppInfoArr) {
            this.appInfos = paymentAppInfoArr;
            notifyDataSetChanged();
        }

        public int getCount() {
            PaymentBackend.PaymentAppInfo[] paymentAppInfoArr = this.appInfos;
            if (paymentAppInfoArr != null) {
                return paymentAppInfoArr.length;
            }
            return 0;
        }

        public PaymentBackend.PaymentAppInfo getItem(int i) {
            return this.appInfos[i];
        }

        public long getItemId(int i) {
            return (long) this.appInfos[i].componentName.hashCode();
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            PaymentBackend.PaymentAppInfo paymentAppInfo = this.appInfos[i];
            if (view == null) {
                view = this.mLayoutInflater.inflate(R.layout.nfc_payment_option, viewGroup, false);
                viewHolder = new ViewHolder();
                viewHolder.radioButton = (RadioButton) view.findViewById(R.id.button);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.radioButton.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) null);
            viewHolder.radioButton.setChecked(paymentAppInfo.isDefault);
            viewHolder.radioButton.setContentDescription(paymentAppInfo.label);
            viewHolder.radioButton.setOnCheckedChangeListener(this);
            viewHolder.radioButton.setTag(paymentAppInfo);
            viewHolder.radioButton.setText(paymentAppInfo.label);
            return view;
        }

        private class ViewHolder {
            public RadioButton radioButton;

            private ViewHolder() {
            }
        }

        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            makeDefault((PaymentBackend.PaymentAppInfo) compoundButton.getTag());
        }

        public void onClick(View view) {
            makeDefault((PaymentBackend.PaymentAppInfo) view.getTag());
        }

        private void makeDefault(PaymentBackend.PaymentAppInfo paymentAppInfo) {
            if (!paymentAppInfo.isDefault) {
                NfcPaymentPreferenceController.this.mPaymentBackend.setDefaultPaymentApp(paymentAppInfo.componentName);
            }
            Dialog dialog = NfcPaymentPreferenceController.this.mPreference.getDialog();
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    }
}
