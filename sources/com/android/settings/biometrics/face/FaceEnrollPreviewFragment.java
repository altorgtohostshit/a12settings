package com.android.settings.biometrics.face;

import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Size;
import android.util.TypedValue;
import android.view.Surface;
import android.view.TextureView;
import android.widget.ImageView;
import com.android.settings.R;
import com.android.settings.biometrics.BiometricEnrollSidecar;
import com.android.settings.biometrics.face.ParticleCollection;
import com.android.settings.core.InstrumentedPreferenceFragment;
import java.util.Arrays;

public class FaceEnrollPreviewFragment extends InstrumentedPreferenceFragment implements BiometricEnrollSidecar.Listener {
    private FaceEnrollAnimationDrawable mAnimationDrawable;
    private final ParticleCollection.Listener mAnimationListener = new ParticleCollection.Listener() {
        public void onEnrolled() {
            FaceEnrollPreviewFragment.this.mListener.onEnrolled();
        }
    };
    /* access modifiers changed from: private */
    public CameraDevice mCameraDevice;
    private String mCameraId;
    private CameraManager mCameraManager;
    private final CameraDevice.StateCallback mCameraStateCallback = new CameraDevice.StateCallback() {
        public void onOpened(CameraDevice cameraDevice) {
            CameraDevice unused = FaceEnrollPreviewFragment.this.mCameraDevice = cameraDevice;
            try {
                SurfaceTexture surfaceTexture = FaceEnrollPreviewFragment.this.mTextureView.getSurfaceTexture();
                surfaceTexture.setDefaultBufferSize(FaceEnrollPreviewFragment.this.mPreviewSize.getWidth(), FaceEnrollPreviewFragment.this.mPreviewSize.getHeight());
                Surface surface = new Surface(surfaceTexture);
                FaceEnrollPreviewFragment faceEnrollPreviewFragment = FaceEnrollPreviewFragment.this;
                CaptureRequest.Builder unused2 = faceEnrollPreviewFragment.mPreviewRequestBuilder = faceEnrollPreviewFragment.mCameraDevice.createCaptureRequest(1);
                FaceEnrollPreviewFragment.this.mPreviewRequestBuilder.addTarget(surface);
                FaceEnrollPreviewFragment.this.mCameraDevice.createCaptureSession(Arrays.asList(new Surface[]{surface}), new CameraCaptureSession.StateCallback() {
                    public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                        if (FaceEnrollPreviewFragment.this.mCameraDevice != null) {
                            CameraCaptureSession unused = FaceEnrollPreviewFragment.this.mCaptureSession = cameraCaptureSession;
                            try {
                                FaceEnrollPreviewFragment.this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, 4);
                                FaceEnrollPreviewFragment faceEnrollPreviewFragment = FaceEnrollPreviewFragment.this;
                                CaptureRequest unused2 = faceEnrollPreviewFragment.mPreviewRequest = faceEnrollPreviewFragment.mPreviewRequestBuilder.build();
                                FaceEnrollPreviewFragment.this.mCaptureSession.setRepeatingRequest(FaceEnrollPreviewFragment.this.mPreviewRequest, (CameraCaptureSession.CaptureCallback) null, FaceEnrollPreviewFragment.this.mHandler);
                            } catch (CameraAccessException e) {
                                Log.e("FaceEnrollPreviewFragment", "Unable to access camera", e);
                            }
                        }
                    }

                    public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                        Log.e("FaceEnrollPreviewFragment", "Unable to configure camera");
                    }
                }, (Handler) null);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        public void onDisconnected(CameraDevice cameraDevice) {
            cameraDevice.close();
            CameraDevice unused = FaceEnrollPreviewFragment.this.mCameraDevice = null;
        }

        public void onError(CameraDevice cameraDevice, int i) {
            cameraDevice.close();
            CameraDevice unused = FaceEnrollPreviewFragment.this.mCameraDevice = null;
        }
    };
    /* access modifiers changed from: private */
    public CameraCaptureSession mCaptureSession;
    private ImageView mCircleView;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler(Looper.getMainLooper());
    /* access modifiers changed from: private */
    public ParticleCollection.Listener mListener;
    /* access modifiers changed from: private */
    public CaptureRequest mPreviewRequest;
    /* access modifiers changed from: private */
    public CaptureRequest.Builder mPreviewRequestBuilder;
    /* access modifiers changed from: private */
    public Size mPreviewSize;
    private final TextureView.SurfaceTextureListener mSurfaceTextureListener = new TextureView.SurfaceTextureListener() {
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            return true;
        }

        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        }

        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
            FaceEnrollPreviewFragment.this.openCamera(i, i2);
        }

        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
            FaceEnrollPreviewFragment.this.configureTransform(i, i2);
        }
    };
    /* access modifiers changed from: private */
    public FaceSquareTextureView mTextureView;

    public int getMetricsCategory() {
        return 1554;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mTextureView = (FaceSquareTextureView) getActivity().findViewById(R.id.texture_view);
        ImageView imageView = (ImageView) getActivity().findViewById(R.id.circle_view);
        this.mCircleView = imageView;
        imageView.setLayerType(1, (Paint) null);
        FaceEnrollAnimationDrawable faceEnrollAnimationDrawable = new FaceEnrollAnimationDrawable(getContext(), this.mAnimationListener);
        this.mAnimationDrawable = faceEnrollAnimationDrawable;
        this.mCircleView.setImageDrawable(faceEnrollAnimationDrawable);
        this.mCameraManager = (CameraManager) getContext().getSystemService("camera");
    }

    public void onResume() {
        super.onResume();
        if (this.mTextureView.isAvailable()) {
            openCamera(this.mTextureView.getWidth(), this.mTextureView.getHeight());
        } else {
            this.mTextureView.setSurfaceTextureListener(this.mSurfaceTextureListener);
        }
    }

    public void onPause() {
        super.onPause();
        closeCamera();
    }

    public void onEnrollmentError(int i, CharSequence charSequence) {
        this.mAnimationDrawable.onEnrollmentError(i, charSequence);
    }

    public void onEnrollmentHelp(int i, CharSequence charSequence) {
        this.mAnimationDrawable.onEnrollmentHelp(i, charSequence);
    }

    public void onEnrollmentProgressChange(int i, int i2) {
        this.mAnimationDrawable.onEnrollmentProgressChange(i, i2);
    }

    public void setListener(ParticleCollection.Listener listener) {
        this.mListener = listener;
    }

    private void setUpCameraOutputs() {
        try {
            for (String str : this.mCameraManager.getCameraIdList()) {
                CameraCharacteristics cameraCharacteristics = this.mCameraManager.getCameraCharacteristics(str);
                Integer num = (Integer) cameraCharacteristics.get(CameraCharacteristics.LENS_FACING);
                if (num != null) {
                    if (num.intValue() == 0) {
                        this.mCameraId = str;
                        this.mPreviewSize = chooseOptimalSize(((StreamConfigurationMap) cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)).getOutputSizes(SurfaceTexture.class));
                        return;
                    }
                }
            }
        } catch (CameraAccessException e) {
            Log.e("FaceEnrollPreviewFragment", "Unable to access camera", e);
        }
    }

    /* access modifiers changed from: private */
    public void openCamera(int i, int i2) {
        try {
            setUpCameraOutputs();
            this.mCameraManager.openCamera(this.mCameraId, this.mCameraStateCallback, this.mHandler);
            configureTransform(i, i2);
        } catch (CameraAccessException e) {
            Log.e("FaceEnrollPreviewFragment", "Unable to open camera", e);
        }
    }

    private Size chooseOptimalSize(Size[] sizeArr) {
        for (int i = 0; i < sizeArr.length; i++) {
            if (sizeArr[i].getHeight() == 1080 && sizeArr[i].getWidth() == 1920) {
                return sizeArr[i];
            }
        }
        Log.w("FaceEnrollPreviewFragment", "Unable to find a good resolution");
        return sizeArr[0];
    }

    /* access modifiers changed from: private */
    public void configureTransform(int i, int i2) {
        if (this.mTextureView != null) {
            float width = ((float) i) / ((float) this.mPreviewSize.getWidth());
            float height = ((float) i2) / ((float) this.mPreviewSize.getHeight());
            float min = Math.min(width, height);
            float f = width / min;
            float f2 = height / min;
            TypedValue typedValue = new TypedValue();
            TypedValue typedValue2 = new TypedValue();
            TypedValue typedValue3 = new TypedValue();
            getResources().getValue(R.dimen.face_preview_translate_x, typedValue, true);
            getResources().getValue(R.dimen.face_preview_translate_y, typedValue2, true);
            getResources().getValue(R.dimen.face_preview_scale, typedValue3, true);
            Matrix matrix = new Matrix();
            this.mTextureView.getTransform(matrix);
            matrix.setScale(f * typedValue3.getFloat(), f2 * typedValue3.getFloat());
            matrix.postTranslate(typedValue.getFloat(), typedValue2.getFloat());
            this.mTextureView.setTransform(matrix);
        }
    }

    private void closeCamera() {
        CameraCaptureSession cameraCaptureSession = this.mCaptureSession;
        if (cameraCaptureSession != null) {
            cameraCaptureSession.close();
            this.mCaptureSession = null;
        }
        CameraDevice cameraDevice = this.mCameraDevice;
        if (cameraDevice != null) {
            cameraDevice.close();
            this.mCameraDevice = null;
        }
    }
}
