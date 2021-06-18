package com.android.settings.wifi;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.SimpleClock;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.FragmentActivity;
import com.android.settings.R;
import com.android.settings.core.InstrumentedFragment;
import com.android.settings.overlay.FeatureFactory;
import com.android.wifitrackerlib.NetworkDetailsTracker;
import com.android.wifitrackerlib.WifiEntry;
import java.time.ZoneOffset;

public class ConfigureWifiEntryFragment extends InstrumentedFragment implements WifiConfigUiBase2 {
    private Button mCancelBtn;
    NetworkDetailsTracker mNetworkDetailsTracker;
    private Button mSubmitBtn;
    private WifiConfigController2 mUiController;
    private WifiEntry mWifiEntry;
    private HandlerThread mWorkerThread;

    public Button getForgetButton() {
        return null;
    }

    public int getMetricsCategory() {
        return 1800;
    }

    public int getMode() {
        return 1;
    }

    public void setForgetButton(CharSequence charSequence) {
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        setupNetworkDetailsTracker();
        this.mWifiEntry = this.mNetworkDetailsTracker.getWifiEntry();
    }

    public void onDestroy() {
        HandlerThread handlerThread = this.mWorkerThread;
        if (handlerThread != null) {
            handlerThread.quit();
        }
        super.onDestroy();
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.wifi_add_network_view, viewGroup, false);
        Button button = (Button) inflate.findViewById(16908315);
        if (button != null) {
            button.setVisibility(8);
        }
        this.mSubmitBtn = (Button) inflate.findViewById(16908313);
        this.mCancelBtn = (Button) inflate.findViewById(16908314);
        this.mSubmitBtn.setOnClickListener(new ConfigureWifiEntryFragment$$ExternalSyntheticLambda0(this));
        this.mCancelBtn.setOnClickListener(new ConfigureWifiEntryFragment$$ExternalSyntheticLambda1(this));
        this.mUiController = new WifiConfigController2(this, inflate, this.mWifiEntry, getMode());
        ActionBar actionBar = getActivity().getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
        }
        return inflate;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateView$0(View view) {
        handleSubmitAction();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateView$1(View view) {
        handleCancelAction();
    }

    public void onViewStateRestored(Bundle bundle) {
        super.onViewStateRestored(bundle);
        this.mUiController.updatePassword();
    }

    public void dispatchSubmit() {
        handleSubmitAction();
    }

    public void setTitle(int i) {
        getActivity().setTitle(i);
    }

    public void setTitle(CharSequence charSequence) {
        getActivity().setTitle(charSequence);
    }

    public void setSubmitButton(CharSequence charSequence) {
        this.mSubmitBtn.setText(charSequence);
    }

    public void setCancelButton(CharSequence charSequence) {
        this.mCancelBtn.setText(charSequence);
    }

    public Button getSubmitButton() {
        return this.mSubmitBtn;
    }

    /* access modifiers changed from: package-private */
    public void handleSubmitAction() {
        Intent intent = new Intent();
        FragmentActivity activity = getActivity();
        intent.putExtra("network_config_key", this.mUiController.getConfig());
        activity.setResult(-1, intent);
        activity.finish();
    }

    /* access modifiers changed from: package-private */
    public void handleCancelAction() {
        getActivity().finish();
    }

    /* JADX WARNING: type inference failed for: r6v0, types: [com.android.settings.wifi.ConfigureWifiEntryFragment$1, java.time.Clock] */
    private void setupNetworkDetailsTracker() {
        if (this.mNetworkDetailsTracker == null) {
            Context context = getContext();
            HandlerThread handlerThread = new HandlerThread("ConfigureWifiEntryFragment{" + Integer.toHexString(System.identityHashCode(this)) + "}", 10);
            this.mWorkerThread = handlerThread;
            handlerThread.start();
            this.mNetworkDetailsTracker = FeatureFactory.getFactory(context).getWifiTrackerLibProvider().createNetworkDetailsTracker(getSettingsLifecycle(), context, new Handler(Looper.getMainLooper()), this.mWorkerThread.getThreadHandler(), new SimpleClock(ZoneOffset.UTC) {
                public long millis() {
                    return SystemClock.elapsedRealtime();
                }
            }, 15000, 10000, getArguments().getString("key_chosen_wifientry_key"));
        }
    }
}
