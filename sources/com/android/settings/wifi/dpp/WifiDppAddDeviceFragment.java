package com.android.settings.wifi.dpp;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.EasyConnectStatusCallback;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import com.android.settings.R;
import com.google.android.setupcompat.template.FooterButton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WifiDppAddDeviceFragment extends WifiDppQrCodeBaseFragment {
    private Button mChooseDifferentNetwork;
    private OnClickChooseDifferentNetworkListener mClickChooseDifferentNetworkListener;
    private int mLatestStatusCode = 0;
    private ImageView mWifiApPictureView;

    public interface OnClickChooseDifferentNetworkListener {
        void onClickChooseDifferentNetwork();
    }

    private boolean hasRetryButton(int i) {
        return (i == -3 || i == -1) ? false : true;
    }

    public int getMetricsCategory() {
        return 1595;
    }

    /* access modifiers changed from: protected */
    public boolean isFooterAvailable() {
        return true;
    }

    private class EasyConnectConfiguratorStatusCallback extends EasyConnectStatusCallback {
        public void onEnrolleeSuccess(int i) {
        }

        public void onProgress(int i) {
        }

        private EasyConnectConfiguratorStatusCallback() {
        }

        public void onConfiguratorSuccess(int i) {
            WifiDppAddDeviceFragment.this.showSuccessUi(false);
        }

        public void onFailure(int i, String str, SparseArray<int[]> sparseArray, int[] iArr) {
            Log.d("WifiDppAddDeviceFragment", "EasyConnectConfiguratorStatusCallback.onFailure: " + i);
            if (!TextUtils.isEmpty(str)) {
                Log.d("WifiDppAddDeviceFragment", "Tried SSID: " + str);
            }
            if (sparseArray.size() != 0) {
                Log.d("WifiDppAddDeviceFragment", "Tried channels: " + sparseArray);
            }
            if (iArr != null && iArr.length > 0) {
                StringBuilder sb = new StringBuilder("Supported bands: ");
                for (int i2 = 0; i2 < iArr.length; i2++) {
                    sb.append(iArr[i2] + " ");
                }
                Log.d("WifiDppAddDeviceFragment", sb.toString());
            }
            WifiDppAddDeviceFragment wifiDppAddDeviceFragment = WifiDppAddDeviceFragment.this;
            wifiDppAddDeviceFragment.showErrorUi(i, wifiDppAddDeviceFragment.getResultIntent(i, str, sparseArray, iArr), false);
        }
    }

    /* access modifiers changed from: private */
    public void showSuccessUi(boolean z) {
        setHeaderIconImageResource(R.drawable.ic_devices_check_circle_green_32dp);
        setHeaderTitle(R.string.wifi_dpp_wifi_shared_with_device, new Object[0]);
        setProgressBarShown(isEasyConnectHandshaking());
        this.mSummary.setVisibility(4);
        this.mWifiApPictureView.setImageResource(R.drawable.wifi_dpp_success);
        this.mChooseDifferentNetwork.setVisibility(4);
        this.mLeftButton.setText(getContext(), R.string.wifi_dpp_add_another_device);
        this.mLeftButton.setOnClickListener(new WifiDppAddDeviceFragment$$ExternalSyntheticLambda1(this));
        this.mRightButton.setText(getContext(), R.string.done);
        this.mRightButton.setOnClickListener(new WifiDppAddDeviceFragment$$ExternalSyntheticLambda2(this));
        this.mRightButton.setVisibility(0);
        if (!z) {
            this.mLatestStatusCode = 1;
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showSuccessUi$0(View view) {
        getFragmentManager().popBackStack();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showSuccessUi$1(View view) {
        FragmentActivity activity = getActivity();
        activity.setResult(-1);
        activity.finish();
    }

    /* access modifiers changed from: private */
    public Intent getResultIntent(int i, String str, SparseArray<int[]> sparseArray, int[] iArr) {
        Intent intent = new Intent();
        intent.putExtra("android.provider.extra.EASY_CONNECT_ERROR_CODE", i);
        if (!TextUtils.isEmpty(str)) {
            intent.putExtra("android.provider.extra.EASY_CONNECT_ATTEMPTED_SSID", str);
        }
        if (!(sparseArray == null || sparseArray.size() == 0)) {
            JSONObject jSONObject = new JSONObject();
            int i2 = 0;
            while (true) {
                try {
                    int keyAt = sparseArray.keyAt(i2);
                    JSONArray jSONArray = new JSONArray();
                    int[] iArr2 = sparseArray.get(keyAt);
                    for (int put : iArr2) {
                        jSONArray.put(put);
                    }
                    try {
                        jSONObject.put(Integer.toString(keyAt), jSONArray);
                        i2++;
                    } catch (JSONException unused) {
                        jSONObject = new JSONObject();
                        intent.putExtra("android.provider.extra.EASY_CONNECT_CHANNEL_LIST", jSONObject.toString());
                        intent.putExtra("android.provider.extra.EASY_CONNECT_BAND_LIST", iArr);
                        return intent;
                    }
                } catch (ArrayIndexOutOfBoundsException unused2) {
                    intent.putExtra("android.provider.extra.EASY_CONNECT_CHANNEL_LIST", jSONObject.toString());
                    intent.putExtra("android.provider.extra.EASY_CONNECT_BAND_LIST", iArr);
                    return intent;
                }
            }
        }
        if (!(iArr == null || iArr.length == 0)) {
            intent.putExtra("android.provider.extra.EASY_CONNECT_BAND_LIST", iArr);
        }
        return intent;
    }

    /* access modifiers changed from: private */
    public void showErrorUi(int i, Intent intent, boolean z) {
        CharSequence charSequence;
        int i2 = 0;
        switch (i) {
            case -12:
                charSequence = getText(R.string.wifi_dpp_failure_enrollee_rejected_configuration);
                break;
            case -11:
                charSequence = getText(R.string.wifi_dpp_failure_enrollee_authentication);
                break;
            case -10:
                charSequence = getText(R.string.wifi_dpp_failure_cannot_find_network);
                break;
            case -9:
                throw new IllegalStateException("Wi-Fi DPP configurator used a non-PSK/non-SAEnetwork to handshake");
            case -8:
                charSequence = getString(R.string.wifi_dpp_failure_not_supported, getSsid());
                break;
            case -7:
                charSequence = getText(R.string.wifi_dpp_failure_generic);
                break;
            case -6:
                charSequence = getText(R.string.wifi_dpp_failure_timeout);
                break;
            case -5:
                if (!z) {
                    if (i != this.mLatestStatusCode) {
                        this.mLatestStatusCode = i;
                        ((WifiManager) getContext().getSystemService(WifiManager.class)).stopEasyConnectSession();
                        startWifiDppConfiguratorInitiator();
                        return;
                    }
                    throw new IllegalStateException("Tried restarting EasyConnectSession but stillreceiving EASY_CONNECT_EVENT_FAILURE_BUSY");
                }
                return;
            case -4:
                charSequence = getText(R.string.wifi_dpp_failure_authentication_or_configuration);
                break;
            case -3:
                charSequence = getText(R.string.wifi_dpp_failure_not_compatible);
                break;
            case -2:
                charSequence = getText(R.string.wifi_dpp_failure_authentication_or_configuration);
                break;
            case -1:
                charSequence = getText(R.string.wifi_dpp_qr_code_is_not_valid_format);
                break;
            default:
                throw new IllegalStateException("Unexpected Wi-Fi DPP error");
        }
        setHeaderTitle(R.string.wifi_dpp_could_not_add_device, new Object[0]);
        this.mSummary.setText(charSequence);
        this.mWifiApPictureView.setImageResource(R.drawable.wifi_dpp_error);
        this.mChooseDifferentNetwork.setVisibility(4);
        FooterButton footerButton = this.mLeftButton;
        if (hasRetryButton(i)) {
            this.mRightButton.setText(getContext(), R.string.retry);
        } else {
            this.mRightButton.setText(getContext(), R.string.done);
            footerButton = this.mRightButton;
            this.mLeftButton.setVisibility(4);
        }
        footerButton.setOnClickListener(new WifiDppAddDeviceFragment$$ExternalSyntheticLambda5(this, intent));
        if (isEasyConnectHandshaking()) {
            this.mSummary.setText(R.string.wifi_dpp_sharing_wifi_with_this_device);
        }
        setProgressBarShown(isEasyConnectHandshaking());
        FooterButton footerButton2 = this.mRightButton;
        if (isEasyConnectHandshaking()) {
            i2 = 4;
        }
        footerButton2.setVisibility(i2);
        if (!z) {
            this.mLatestStatusCode = i;
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showErrorUi$2(Intent intent, View view) {
        getActivity().setResult(0, intent);
        getActivity().finish();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            this.mLatestStatusCode = bundle.getInt("key_latest_status_code");
        }
        WifiDppInitiatorViewModel wifiDppInitiatorViewModel = (WifiDppInitiatorViewModel) ViewModelProviders.m5of((Fragment) this).get(WifiDppInitiatorViewModel.class);
        wifiDppInitiatorViewModel.getStatusCode().observe(this, new WifiDppAddDeviceFragment$$ExternalSyntheticLambda6(this, wifiDppInitiatorViewModel));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$3(WifiDppInitiatorViewModel wifiDppInitiatorViewModel, Integer num) {
        if (!wifiDppInitiatorViewModel.isWifiDppHandshaking()) {
            int intValue = num.intValue();
            if (intValue == 1) {
                new EasyConnectConfiguratorStatusCallback().onConfiguratorSuccess(intValue);
            } else {
                new EasyConnectConfiguratorStatusCallback().onFailure(intValue, wifiDppInitiatorViewModel.getTriedSsid(), wifiDppInitiatorViewModel.getTriedChannels(), wifiDppInitiatorViewModel.getBandArray());
            }
        }
    }

    public final View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.wifi_dpp_add_device_fragment, viewGroup, false);
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        setHeaderIconImageResource(R.drawable.ic_devices_other_32dp);
        String information = ((WifiDppConfiguratorActivity) getActivity()).getWifiDppQrCode().getInformation();
        int i = 0;
        if (TextUtils.isEmpty(information)) {
            setHeaderTitle(R.string.wifi_dpp_device_found, new Object[0]);
        } else {
            setHeaderTitle(information);
        }
        updateSummary();
        this.mWifiApPictureView = (ImageView) view.findViewById(R.id.wifi_ap_picture_view);
        Button button = (Button) view.findViewById(R.id.choose_different_network);
        this.mChooseDifferentNetwork = button;
        button.setOnClickListener(new WifiDppAddDeviceFragment$$ExternalSyntheticLambda4(this));
        this.mLeftButton.setText(getContext(), R.string.cancel);
        this.mLeftButton.setOnClickListener(new WifiDppAddDeviceFragment$$ExternalSyntheticLambda0(this));
        this.mRightButton.setText(getContext(), R.string.wifi_dpp_share_wifi);
        this.mRightButton.setOnClickListener(new WifiDppAddDeviceFragment$$ExternalSyntheticLambda3(this));
        if (bundle != null) {
            int i2 = this.mLatestStatusCode;
            if (i2 == 1) {
                showSuccessUi(true);
            } else if (i2 == 0) {
                setProgressBarShown(isEasyConnectHandshaking());
                FooterButton footerButton = this.mRightButton;
                if (isEasyConnectHandshaking()) {
                    i = 4;
                }
                footerButton.setVisibility(i);
            } else {
                showErrorUi(i2, (Intent) null, true);
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$4(View view) {
        this.mClickChooseDifferentNetworkListener.onClickChooseDifferentNetwork();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$5(View view) {
        getActivity().finish();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$6(View view) {
        setProgressBarShown(true);
        this.mRightButton.setVisibility(4);
        startWifiDppConfiguratorInitiator();
        updateSummary();
    }

    public void onSaveInstanceState(Bundle bundle) {
        bundle.putInt("key_latest_status_code", this.mLatestStatusCode);
        super.onSaveInstanceState(bundle);
    }

    private String getSsid() {
        WifiNetworkConfig wifiNetworkConfig = ((WifiDppConfiguratorActivity) getActivity()).getWifiNetworkConfig();
        if (WifiNetworkConfig.isValidConfig(wifiNetworkConfig)) {
            return wifiNetworkConfig.getSsid();
        }
        throw new IllegalStateException("Invalid Wi-Fi network for configuring");
    }

    private void startWifiDppConfiguratorInitiator() {
        ((WifiDppInitiatorViewModel) ViewModelProviders.m5of((Fragment) this).get(WifiDppInitiatorViewModel.class)).startEasyConnectAsConfiguratorInitiator(((WifiDppConfiguratorActivity) getActivity()).getWifiDppQrCode().getQrCode(), ((WifiDppConfiguratorActivity) getActivity()).getWifiNetworkConfig().getNetworkId());
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.mClickChooseDifferentNetworkListener = (OnClickChooseDifferentNetworkListener) context;
    }

    public void onDetach() {
        this.mClickChooseDifferentNetworkListener = null;
        super.onDetach();
    }

    private boolean isEasyConnectHandshaking() {
        return ((WifiDppInitiatorViewModel) ViewModelProviders.m5of((Fragment) this).get(WifiDppInitiatorViewModel.class)).isWifiDppHandshaking();
    }

    private void updateSummary() {
        if (isEasyConnectHandshaking()) {
            this.mSummary.setText(R.string.wifi_dpp_sharing_wifi_with_this_device);
            return;
        }
        this.mSummary.setText(getString(R.string.wifi_dpp_add_device_to_wifi, getSsid()));
    }
}
