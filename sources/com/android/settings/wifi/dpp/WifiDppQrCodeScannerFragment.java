package com.android.settings.wifi.dpp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.net.wifi.EasyConnectStatusCallback;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SimpleClock;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import com.android.settings.R;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.wifi.dpp.WifiNetworkConfig;
import com.android.settings.wifi.qrcode.QrCamera;
import com.android.settings.wifi.qrcode.QrDecorateView;
import com.android.wifitrackerlib.WifiEntry;
import com.android.wifitrackerlib.WifiPickerTracker;
import java.time.ZoneOffset;
import java.util.List;

public class WifiDppQrCodeScannerFragment extends WifiDppQrCodeBaseFragment implements TextureView.SurfaceTextureListener, QrCamera.ScannerCallback, WifiManager.ActionListener {
    private QrCamera mCamera;
    /* access modifiers changed from: private */
    public QrDecorateView mDecorateView;
    /* access modifiers changed from: private */
    public WifiConfiguration mEnrolleeWifiConfiguration;
    /* access modifiers changed from: private */
    public TextView mErrorMessage;
    private final Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                WifiDppQrCodeScannerFragment.this.mErrorMessage.setVisibility(4);
            } else if (i == 2) {
                WifiDppQrCodeScannerFragment.this.mErrorMessage.setVisibility(0);
                WifiDppQrCodeScannerFragment.this.mErrorMessage.setText((String) message.obj);
                WifiDppQrCodeScannerFragment.this.mErrorMessage.sendAccessibilityEvent(32);
                removeMessages(1);
                sendEmptyMessageDelayed(1, 10000);
                if (message.arg1 == 1) {
                    WifiDppQrCodeScannerFragment.this.setProgressBarShown(false);
                    WifiDppQrCodeScannerFragment.this.mDecorateView.setFocused(false);
                    WifiDppQrCodeScannerFragment.this.restartCamera();
                }
            } else if (i != 3) {
                if (i == 4) {
                    WifiManager wifiManager = (WifiManager) WifiDppQrCodeScannerFragment.this.getContext().getSystemService(WifiManager.class);
                    boolean z = false;
                    for (WifiConfiguration next : ((WifiNetworkConfig) message.obj).getWifiConfigurations()) {
                        int addNetwork = wifiManager.addNetwork(next);
                        if (addNetwork != -1) {
                            wifiManager.enableNetwork(addNetwork, false);
                            if (next.hiddenSSID || WifiDppQrCodeScannerFragment.this.isReachableWifiNetwork(next)) {
                                WifiConfiguration unused = WifiDppQrCodeScannerFragment.this.mEnrolleeWifiConfiguration = next;
                                wifiManager.connect(addNetwork, WifiDppQrCodeScannerFragment.this);
                                z = true;
                            }
                        }
                    }
                    if (!z) {
                        WifiDppQrCodeScannerFragment.this.showErrorMessageAndRestartCamera(R.string.wifi_dpp_check_connection_try_again);
                        return;
                    }
                    WifiDppQrCodeScannerFragment.this.mMetricsFeatureProvider.action(WifiDppQrCodeScannerFragment.this.mMetricsFeatureProvider.getAttribution(WifiDppQrCodeScannerFragment.this.getActivity()), 1711, 1596, (String) null, Integer.MIN_VALUE);
                    WifiDppQrCodeScannerFragment.this.notifyUserForQrCodeRecognition();
                }
            } else if (WifiDppQrCodeScannerFragment.this.mScanWifiDppSuccessListener != null) {
                WifiDppQrCodeScannerFragment.this.mScanWifiDppSuccessListener.onScanWifiDppSuccess((WifiQrCode) message.obj);
                if (!WifiDppQrCodeScannerFragment.this.mIsConfiguratorMode) {
                    WifiDppQrCodeScannerFragment.this.setProgressBarShown(true);
                    WifiDppQrCodeScannerFragment.this.startWifiDppEnrolleeInitiator((WifiQrCode) message.obj);
                    WifiDppQrCodeScannerFragment.this.updateEnrolleeSummary();
                    WifiDppQrCodeScannerFragment.this.mSummary.sendAccessibilityEvent(32);
                }
                WifiDppQrCodeScannerFragment.this.notifyUserForQrCodeRecognition();
            }
        }
    };
    /* access modifiers changed from: private */
    public boolean mIsConfiguratorMode = true;
    /* access modifiers changed from: private */
    public int mLatestStatusCode = 0;
    /* access modifiers changed from: private */
    public OnScanWifiDppSuccessListener mScanWifiDppSuccessListener;
    private String mSsid;
    private TextureView mTextureView;
    private WifiPickerTracker mWifiPickerTracker;
    /* access modifiers changed from: private */
    public WifiQrCode mWifiQrCode;
    private HandlerThread mWorkerThread;

    public interface OnScanWifiDppSuccessListener {
        void onScanWifiDppSuccess(WifiQrCode wifiQrCode);
    }

    /* access modifiers changed from: protected */
    public boolean isFooterAvailable() {
        return false;
    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }

    /* access modifiers changed from: private */
    public void notifyUserForQrCodeRecognition() {
        QrCamera qrCamera = this.mCamera;
        if (qrCamera != null) {
            qrCamera.stop();
        }
        this.mDecorateView.setFocused(true);
        this.mErrorMessage.setVisibility(4);
        WifiDppUtils.triggerVibrationForQrCodeRecognition(getContext());
    }

    /* access modifiers changed from: private */
    public boolean isReachableWifiNetwork(WifiConfiguration wifiConfiguration) {
        List<WifiEntry> wifiEntries = this.mWifiPickerTracker.getWifiEntries();
        WifiEntry connectedWifiEntry = this.mWifiPickerTracker.getConnectedWifiEntry();
        if (connectedWifiEntry != null) {
            wifiEntries.add(connectedWifiEntry);
        }
        for (WifiEntry next : wifiEntries) {
            if (TextUtils.equals(next.getSsid(), WifiInfo.sanitizeSsid(wifiConfiguration.SSID))) {
                int securityTypeFromWifiConfiguration = WifiDppUtils.getSecurityTypeFromWifiConfiguration(wifiConfiguration);
                if (securityTypeFromWifiConfiguration == next.getSecurity()) {
                    return true;
                }
                if (securityTypeFromWifiConfiguration == 5 && next.getSecurity() == 2) {
                    return true;
                }
            }
        }
        return false;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            this.mIsConfiguratorMode = bundle.getBoolean("key_is_configurator_mode");
            this.mLatestStatusCode = bundle.getInt("key_latest_error_code");
            this.mEnrolleeWifiConfiguration = (WifiConfiguration) bundle.getParcelable("key_wifi_configuration");
        }
        WifiDppInitiatorViewModel wifiDppInitiatorViewModel = (WifiDppInitiatorViewModel) ViewModelProviders.m5of((Fragment) this).get(WifiDppInitiatorViewModel.class);
        wifiDppInitiatorViewModel.getEnrolleeSuccessNetworkId().observe(this, new WifiDppQrCodeScannerFragment$$ExternalSyntheticLambda1(this, wifiDppInitiatorViewModel));
        wifiDppInitiatorViewModel.getStatusCode().observe(this, new WifiDppQrCodeScannerFragment$$ExternalSyntheticLambda0(this, wifiDppInitiatorViewModel));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(WifiDppInitiatorViewModel wifiDppInitiatorViewModel, Integer num) {
        if (!wifiDppInitiatorViewModel.isWifiDppHandshaking()) {
            new EasyConnectEnrolleeStatusCallback().onEnrolleeSuccess(num.intValue());
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$1(WifiDppInitiatorViewModel wifiDppInitiatorViewModel, Integer num) {
        if (!wifiDppInitiatorViewModel.isWifiDppHandshaking()) {
            int intValue = num.intValue();
            Log.d("WifiDppQrCodeScanner", "Easy connect enrollee callback onFailure " + intValue);
            new EasyConnectEnrolleeStatusCallback().onFailure(intValue);
        }
    }

    public void onPause() {
        QrCamera qrCamera = this.mCamera;
        if (qrCamera != null) {
            qrCamera.stop();
        }
        super.onPause();
    }

    public void onResume() {
        super.onResume();
        if (!isWifiDppHandshaking()) {
            restartCamera();
        }
    }

    public int getMetricsCategory() {
        return this.mIsConfiguratorMode ? 1595 : 1596;
    }

    public WifiDppQrCodeScannerFragment() {
    }

    WifiDppQrCodeScannerFragment(String str) {
        this.mSsid = str;
    }

    /* JADX WARNING: type inference failed for: r7v0, types: [com.android.settings.wifi.dpp.WifiDppQrCodeScannerFragment$2, java.time.Clock] */
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        HandlerThread handlerThread = new HandlerThread("WifiDppQrCodeScanner{" + Integer.toHexString(System.identityHashCode(this)) + "}", 10);
        this.mWorkerThread = handlerThread;
        handlerThread.start();
        ? r7 = new SimpleClock(ZoneOffset.UTC) {
            public long millis() {
                return SystemClock.elapsedRealtime();
            }
        };
        Context context = getContext();
        this.mWifiPickerTracker = FeatureFactory.getFactory(context).getWifiTrackerLibProvider().createWifiPickerTracker(getSettingsLifecycle(), context, new Handler(Looper.getMainLooper()), this.mWorkerThread.getThreadHandler(), r7, 15000, 10000, (WifiPickerTracker.WifiPickerTrackerCallback) null);
        if (this.mIsConfiguratorMode) {
            getActivity().setTitle(R.string.wifi_dpp_add_device_to_network);
        } else {
            getActivity().setTitle(R.string.wifi_dpp_scan_qr_code);
        }
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.mScanWifiDppSuccessListener = (OnScanWifiDppSuccessListener) context;
    }

    public void onDetach() {
        this.mScanWifiDppSuccessListener = null;
        super.onDetach();
    }

    public void onDestroyView() {
        this.mWorkerThread.quit();
        super.onDestroyView();
    }

    public final View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.wifi_dpp_qrcode_scanner_fragment, viewGroup, false);
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        TextureView textureView = (TextureView) view.findViewById(R.id.preview_view);
        this.mTextureView = textureView;
        textureView.setSurfaceTextureListener(this);
        this.mDecorateView = (QrDecorateView) view.findViewById(R.id.decorate_view);
        setProgressBarShown(isWifiDppHandshaking());
        if (this.mIsConfiguratorMode) {
            setHeaderTitle(R.string.wifi_dpp_add_device_to_network, new Object[0]);
            WifiNetworkConfig wifiNetworkConfig = ((WifiNetworkConfig.Retriever) getActivity()).getWifiNetworkConfig();
            if (WifiNetworkConfig.isValidConfig(wifiNetworkConfig)) {
                this.mSummary.setText(getString(R.string.wifi_dpp_center_qr_code, wifiNetworkConfig.getSsid()));
            } else {
                throw new IllegalStateException("Invalid Wi-Fi network for configuring");
            }
        } else {
            setHeaderTitle(R.string.wifi_dpp_scan_qr_code, new Object[0]);
            updateEnrolleeSummary();
        }
        this.mErrorMessage = (TextView) view.findViewById(R.id.error_message);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menu.removeItem(1);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
        initCamera(surfaceTexture);
    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        destroyCamera();
        return true;
    }

    public Size getViewSize() {
        return new Size(this.mTextureView.getWidth(), this.mTextureView.getHeight());
    }

    public Rect getFramePosition(Size size, int i) {
        return new Rect(0, 0, size.getHeight(), size.getHeight());
    }

    public void setTransform(Matrix matrix) {
        this.mTextureView.setTransform(matrix);
    }

    public boolean isValid(String str) {
        try {
            WifiQrCode wifiQrCode = new WifiQrCode(str);
            this.mWifiQrCode = wifiQrCode;
            String scheme = wifiQrCode.getScheme();
            if (!this.mIsConfiguratorMode || !"WIFI".equals(scheme)) {
                return true;
            }
            showErrorMessage(R.string.wifi_dpp_qr_code_is_not_valid_format);
            return false;
        } catch (IllegalArgumentException unused) {
            showErrorMessage(R.string.wifi_dpp_qr_code_is_not_valid_format);
            return false;
        }
    }

    public void handleSuccessfulResult(String str) {
        String scheme = this.mWifiQrCode.getScheme();
        scheme.hashCode();
        if (scheme.equals("DPP")) {
            handleWifiDpp();
        } else if (scheme.equals("WIFI")) {
            handleZxingWifiFormat();
        }
    }

    private void handleWifiDpp() {
        Message obtainMessage = this.mHandler.obtainMessage(3);
        obtainMessage.obj = new WifiQrCode(this.mWifiQrCode.getQrCode());
        this.mHandler.sendMessageDelayed(obtainMessage, 1000);
    }

    private void handleZxingWifiFormat() {
        Message obtainMessage = this.mHandler.obtainMessage(4);
        obtainMessage.obj = new WifiQrCode(this.mWifiQrCode.getQrCode()).getWifiNetworkConfig();
        this.mHandler.sendMessageDelayed(obtainMessage, 1000);
    }

    public void handleCameraFailure() {
        destroyCamera();
    }

    private void initCamera(SurfaceTexture surfaceTexture) {
        if (this.mCamera == null) {
            this.mCamera = new QrCamera(getContext(), this);
            if (isWifiDppHandshaking()) {
                QrDecorateView qrDecorateView = this.mDecorateView;
                if (qrDecorateView != null) {
                    qrDecorateView.setFocused(true);
                    return;
                }
                return;
            }
            this.mCamera.start(surfaceTexture);
        }
    }

    private void destroyCamera() {
        QrCamera qrCamera = this.mCamera;
        if (qrCamera != null) {
            qrCamera.stop();
            this.mCamera = null;
        }
    }

    private void showErrorMessage(int i) {
        this.mHandler.obtainMessage(2, getString(i)).sendToTarget();
    }

    /* access modifiers changed from: private */
    public void showErrorMessageAndRestartCamera(int i) {
        Message obtainMessage = this.mHandler.obtainMessage(2, getString(i));
        obtainMessage.arg1 = 1;
        obtainMessage.sendToTarget();
    }

    public void onSaveInstanceState(Bundle bundle) {
        bundle.putBoolean("key_is_configurator_mode", this.mIsConfiguratorMode);
        bundle.putInt("key_latest_error_code", this.mLatestStatusCode);
        bundle.putParcelable("key_wifi_configuration", this.mEnrolleeWifiConfiguration);
        super.onSaveInstanceState(bundle);
    }

    private class EasyConnectEnrolleeStatusCallback extends EasyConnectStatusCallback {
        public void onConfiguratorSuccess(int i) {
        }

        public void onProgress(int i) {
        }

        private EasyConnectEnrolleeStatusCallback() {
        }

        public void onEnrolleeSuccess(int i) {
            WifiManager wifiManager = (WifiManager) WifiDppQrCodeScannerFragment.this.getContext().getSystemService(WifiManager.class);
            for (WifiConfiguration wifiConfiguration : wifiManager.getPrivilegedConfiguredNetworks()) {
                if (wifiConfiguration.networkId == i) {
                    int unused = WifiDppQrCodeScannerFragment.this.mLatestStatusCode = 1;
                    WifiConfiguration unused2 = WifiDppQrCodeScannerFragment.this.mEnrolleeWifiConfiguration = wifiConfiguration;
                    wifiManager.connect(wifiConfiguration, WifiDppQrCodeScannerFragment.this);
                    return;
                }
            }
            Log.e("WifiDppQrCodeScanner", "Invalid networkId " + i);
            int unused3 = WifiDppQrCodeScannerFragment.this.mLatestStatusCode = -7;
            WifiDppQrCodeScannerFragment.this.updateEnrolleeSummary();
            WifiDppQrCodeScannerFragment.this.showErrorMessageAndRestartCamera(R.string.wifi_dpp_check_connection_try_again);
        }

        public void onFailure(int i) {
            Log.d("WifiDppQrCodeScanner", "EasyConnectEnrolleeStatusCallback.onFailure " + i);
            int i2 = R.string.wifi_dpp_failure_authentication_or_configuration;
            switch (i) {
                case -9:
                    throw new IllegalStateException("EASY_CONNECT_EVENT_FAILURE_INVALID_NETWORK should be a configurator only error");
                case -8:
                    throw new IllegalStateException("EASY_CONNECT_EVENT_FAILURE_NOT_SUPPORTED should be a configurator only error");
                case -7:
                    i2 = R.string.wifi_dpp_failure_generic;
                    break;
                case -6:
                    i2 = R.string.wifi_dpp_failure_timeout;
                    break;
                case -5:
                    if (i != WifiDppQrCodeScannerFragment.this.mLatestStatusCode) {
                        int unused = WifiDppQrCodeScannerFragment.this.mLatestStatusCode = i;
                        ((WifiManager) WifiDppQrCodeScannerFragment.this.getContext().getSystemService(WifiManager.class)).stopEasyConnectSession();
                        WifiDppQrCodeScannerFragment wifiDppQrCodeScannerFragment = WifiDppQrCodeScannerFragment.this;
                        wifiDppQrCodeScannerFragment.startWifiDppEnrolleeInitiator(wifiDppQrCodeScannerFragment.mWifiQrCode);
                        return;
                    }
                    throw new IllegalStateException("stopEasyConnectSession and try again forEASY_CONNECT_EVENT_FAILURE_BUSY but still failed");
                case -4:
                case -2:
                    break;
                case -3:
                    i2 = R.string.wifi_dpp_failure_not_compatible;
                    break;
                case -1:
                    i2 = R.string.wifi_dpp_qr_code_is_not_valid_format;
                    break;
                default:
                    throw new IllegalStateException("Unexpected Wi-Fi DPP error");
            }
            int unused2 = WifiDppQrCodeScannerFragment.this.mLatestStatusCode = i;
            WifiDppQrCodeScannerFragment.this.updateEnrolleeSummary();
            WifiDppQrCodeScannerFragment.this.showErrorMessageAndRestartCamera(i2);
        }
    }

    /* access modifiers changed from: private */
    public void startWifiDppEnrolleeInitiator(WifiQrCode wifiQrCode) {
        ((WifiDppInitiatorViewModel) ViewModelProviders.m5of((Fragment) this).get(WifiDppInitiatorViewModel.class)).startEasyConnectAsEnrolleeInitiator(wifiQrCode.getQrCode());
    }

    public void onSuccess() {
        Intent intent = new Intent();
        intent.putExtra("key_wifi_configuration", this.mEnrolleeWifiConfiguration);
        FragmentActivity activity = getActivity();
        activity.setResult(-1, intent);
        activity.finish();
    }

    public void onFailure(int i) {
        Log.d("WifiDppQrCodeScanner", "Wi-Fi connect onFailure reason - " + i);
        showErrorMessageAndRestartCamera(R.string.wifi_dpp_check_connection_try_again);
    }

    private boolean isWifiDppHandshaking() {
        return ((WifiDppInitiatorViewModel) ViewModelProviders.m5of((Fragment) this).get(WifiDppInitiatorViewModel.class)).isWifiDppHandshaking();
    }

    /* access modifiers changed from: private */
    public void restartCamera() {
        QrCamera qrCamera = this.mCamera;
        if (qrCamera == null) {
            Log.d("WifiDppQrCodeScanner", "mCamera is not available for restarting camera");
            return;
        }
        if (qrCamera.isDecodeTaskAlive()) {
            this.mCamera.stop();
        }
        SurfaceTexture surfaceTexture = this.mTextureView.getSurfaceTexture();
        if (surfaceTexture != null) {
            this.mCamera.start(surfaceTexture);
            return;
        }
        throw new IllegalStateException("SurfaceTexture is not ready for restarting camera");
    }

    /* access modifiers changed from: private */
    public void updateEnrolleeSummary() {
        String str;
        if (isWifiDppHandshaking()) {
            this.mSummary.setText(R.string.wifi_dpp_connecting);
            return;
        }
        if (TextUtils.isEmpty(this.mSsid)) {
            str = getString(R.string.wifi_dpp_scan_qr_code_join_unknown_network, this.mSsid);
        } else {
            str = getString(R.string.wifi_dpp_scan_qr_code_join_network, this.mSsid);
        }
        this.mSummary.setText(str);
    }

    /* access modifiers changed from: protected */
    public boolean isDecodeTaskAlive() {
        QrCamera qrCamera = this.mCamera;
        return qrCamera != null && qrCamera.isDecodeTaskAlive();
    }
}
