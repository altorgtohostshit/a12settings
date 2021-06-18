package com.android.settings.development;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.debug.IAdbManager;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.settings.R;
import com.android.settings.wifi.dpp.AdbQrCode;
import com.android.settings.wifi.dpp.WifiDppQrCodeBaseFragment;
import com.android.settings.wifi.dpp.WifiNetworkConfig;
import com.android.settings.wifi.qrcode.QrCamera;
import com.android.settings.wifi.qrcode.QrDecorateView;

public class AdbQrcodeScannerFragment extends WifiDppQrCodeBaseFragment implements TextureView.SurfaceTextureListener, QrCamera.ScannerCallback {
    private WifiNetworkConfig mAdbConfig;
    private IAdbManager mAdbManager;
    private AdbQrCode mAdbQrCode;
    private QrCamera mCamera;
    private QrDecorateView mDecorateView;
    /* access modifiers changed from: private */
    public TextView mErrorMessage;
    private final Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                AdbQrcodeScannerFragment.this.mErrorMessage.setVisibility(4);
            } else if (i == 2) {
                AdbQrcodeScannerFragment.this.mErrorMessage.setVisibility(0);
                AdbQrcodeScannerFragment.this.mErrorMessage.setText((String) message.obj);
                AdbQrcodeScannerFragment.this.mErrorMessage.sendAccessibilityEvent(32);
                removeMessages(1);
                sendEmptyMessageDelayed(1, 10000);
            }
        }
    };
    private IntentFilter mIntentFilter;
    private View mQrCameraView;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if ("com.android.server.adb.WIRELESS_DEBUG_PAIRING_RESULT".equals(intent.getAction())) {
                Integer valueOf = Integer.valueOf(intent.getIntExtra("status", 0));
                if (valueOf.equals(1)) {
                    Intent intent2 = new Intent();
                    intent2.putExtra("request_type_pairing", 0);
                    AdbQrcodeScannerFragment.this.getActivity().setResult(-1, intent2);
                    AdbQrcodeScannerFragment.this.getActivity().finish();
                } else if (valueOf.equals(0)) {
                    Intent intent3 = new Intent();
                    intent3.putExtra("request_type_pairing", 1);
                    AdbQrcodeScannerFragment.this.getActivity().setResult(-1, intent3);
                    AdbQrcodeScannerFragment.this.getActivity().finish();
                } else if (valueOf.equals(4)) {
                    int intExtra = intent.getIntExtra("adb_port", 0);
                    Log.i("AdbQrcodeScannerFrag", "Got Qr pairing code port=" + intExtra);
                }
            }
        }
    };
    private TextureView mTextureView;
    private TextView mVerifyingTextView;
    private View mVerifyingView;

    public int getMetricsCategory() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public boolean isFooterAvailable() {
        return false;
    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mIntentFilter = new IntentFilter("com.android.server.adb.WIRELESS_DEBUG_PAIRING_RESULT");
    }

    public final View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.adb_qrcode_scanner_fragment, viewGroup, false);
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        TextureView textureView = (TextureView) view.findViewById(R.id.preview_view);
        this.mTextureView = textureView;
        textureView.setSurfaceTextureListener(this);
        this.mDecorateView = (QrDecorateView) view.findViewById(R.id.decorate_view);
        setProgressBarShown(false);
        setHeaderIconImageResource(R.drawable.ic_scan_24dp);
        this.mQrCameraView = view.findViewById(R.id.camera_layout);
        this.mVerifyingView = view.findViewById(R.id.verifying_layout);
        this.mVerifyingTextView = (TextView) view.findViewById(R.id.verifying_textview);
        setHeaderTitle(R.string.wifi_dpp_scan_qr_code, new Object[0]);
        this.mSummary.setText(R.string.adb_wireless_qrcode_pairing_description);
        this.mErrorMessage = (TextView) view.findViewById(R.id.error_message);
    }

    public void onResume() {
        super.onResume();
        this.mAdbManager = IAdbManager.Stub.asInterface(ServiceManager.getService("adb"));
        getActivity().registerReceiver(this.mReceiver, this.mIntentFilter);
    }

    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(this.mReceiver);
        try {
            this.mAdbManager.disablePairing();
        } catch (RemoteException unused) {
            Log.e("AdbQrcodeScannerFrag", "Unable to cancel pairing");
        }
        getActivity().setResult(0);
        getActivity().finish();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        getActivity().getActionBar().hide();
        getActivity().setTitle(R.string.wifi_dpp_scan_qr_code);
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

    public void setTransform(Matrix matrix) {
        this.mTextureView.setTransform(matrix);
    }

    public Rect getFramePosition(Size size, int i) {
        return new Rect(0, 0, size.getHeight(), size.getHeight());
    }

    public boolean isValid(String str) {
        try {
            AdbQrCode adbQrCode = new AdbQrCode(str);
            this.mAdbQrCode = adbQrCode;
            this.mAdbConfig = adbQrCode.getAdbNetworkConfig();
            return true;
        } catch (IllegalArgumentException unused) {
            showErrorMessage(R.string.wifi_dpp_qr_code_is_not_valid_format);
            return false;
        }
    }

    public void handleSuccessfulResult(String str) {
        destroyCamera();
        this.mDecorateView.setFocused(true);
        this.mQrCameraView.setVisibility(8);
        this.mVerifyingView.setVisibility(0);
        AdbQrCode.triggerVibrationForQrCodeRecognition(getContext());
        this.mVerifyingTextView.sendAccessibilityEvent(8);
        try {
            this.mAdbManager.enablePairingByQrCode(this.mAdbConfig.getSsid(), this.mAdbConfig.getPreSharedKey());
        } catch (RemoteException unused) {
            Log.e("AdbQrcodeScannerFrag", "Unable to enable QR code pairing");
            getActivity().setResult(0);
            getActivity().finish();
        }
    }

    public void handleCameraFailure() {
        destroyCamera();
    }

    private void initCamera(SurfaceTexture surfaceTexture) {
        if (this.mCamera == null) {
            QrCamera qrCamera = new QrCamera(getContext(), this);
            this.mCamera = qrCamera;
            qrCamera.start(surfaceTexture);
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
}
