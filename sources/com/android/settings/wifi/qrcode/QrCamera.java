package com.android.settings.wifi.qrcode;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Size;
import android.view.WindowManager;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class QrCamera extends Handler {
    private static List<BarcodeFormat> FORMATS;
    private static Map<DecodeHintType, List<BarcodeFormat>> HINTS = new ArrayMap();
    Camera mCamera;
    /* access modifiers changed from: private */
    public int mCameraOrientation;
    private WeakReference<Context> mContext;
    private DecodingTask mDecodeTask;
    Camera.Parameters mParameters;
    private Size mPreviewSize;
    /* access modifiers changed from: private */
    public MultiFormatReader mReader;
    /* access modifiers changed from: private */
    public ScannerCallback mScannerCallback;

    public interface ScannerCallback {
        Rect getFramePosition(Size size, int i);

        Size getViewSize();

        void handleCameraFailure();

        void handleSuccessfulResult(String str);

        boolean isValid(String str);

        void setTransform(Matrix matrix);
    }

    private double getRatio(double d, double d2) {
        return d < d2 ? d / d2 : d2 / d;
    }

    static {
        ArrayList arrayList = new ArrayList();
        FORMATS = arrayList;
        arrayList.add(BarcodeFormat.QR_CODE);
        HINTS.put(DecodeHintType.POSSIBLE_FORMATS, FORMATS);
    }

    public QrCamera(Context context, ScannerCallback scannerCallback) {
        this.mContext = new WeakReference<>(context);
        this.mScannerCallback = scannerCallback;
        MultiFormatReader multiFormatReader = new MultiFormatReader();
        this.mReader = multiFormatReader;
        multiFormatReader.setHints(HINTS);
    }

    public void start(SurfaceTexture surfaceTexture) {
        if (this.mDecodeTask == null) {
            DecodingTask decodingTask = new DecodingTask(surfaceTexture);
            this.mDecodeTask = decodingTask;
            decodingTask.executeOnExecutor(Executors.newSingleThreadExecutor(), new Void[0]);
        }
    }

    public void stop() {
        removeMessages(1);
        DecodingTask decodingTask = this.mDecodeTask;
        if (decodingTask != null) {
            decodingTask.cancel(true);
            this.mDecodeTask = null;
        }
        Camera camera = this.mCamera;
        if (camera != null) {
            camera.stopPreview();
        }
    }

    /* access modifiers changed from: package-private */
    public void setCameraParameter() {
        Camera.Parameters parameters = this.mCamera.getParameters();
        this.mParameters = parameters;
        Size bestPreviewSize = getBestPreviewSize(parameters);
        this.mPreviewSize = bestPreviewSize;
        this.mParameters.setPreviewSize(bestPreviewSize.getWidth(), this.mPreviewSize.getHeight());
        Size bestPictureSize = getBestPictureSize(this.mParameters);
        this.mParameters.setPictureSize(bestPictureSize.getWidth(), bestPictureSize.getHeight());
        List<String> supportedFlashModes = this.mParameters.getSupportedFlashModes();
        if (supportedFlashModes != null && supportedFlashModes.contains("off")) {
            this.mParameters.setFlashMode("off");
        }
        List<String> supportedFocusModes = this.mParameters.getSupportedFocusModes();
        if (supportedFocusModes.contains("continuous-picture")) {
            this.mParameters.setFocusMode("continuous-picture");
        } else if (supportedFocusModes.contains("auto")) {
            this.mParameters.setFocusMode("auto");
        }
        this.mCamera.setParameters(this.mParameters);
    }

    /* access modifiers changed from: private */
    public boolean startPreview() {
        int i = 0;
        if (this.mContext.get() == null) {
            return false;
        }
        int rotation = ((WindowManager) ((Context) this.mContext.get()).getSystemService("window")).getDefaultDisplay().getRotation();
        if (rotation != 0) {
            if (rotation == 1) {
                i = 90;
            } else if (rotation == 2) {
                i = 180;
            } else if (rotation == 3) {
                i = 270;
            }
        }
        this.mCamera.setDisplayOrientation(((this.mCameraOrientation - i) + 360) % 360);
        this.mCamera.startPreview();
        if ("auto".equals(this.mParameters.getFocusMode())) {
            this.mCamera.autoFocus((Camera.AutoFocusCallback) null);
            sendMessageDelayed(obtainMessage(1), 1500);
        }
        return true;
    }

    private class DecodingTask extends AsyncTask<Void, Void, String> {
        private QrYuvLuminanceSource mImage;
        private SurfaceTexture mSurface;

        private DecodingTask(SurfaceTexture surfaceTexture) {
            this.mSurface = surfaceTexture;
        }

        /* access modifiers changed from: protected */
        public String doInBackground(Void... voidArr) {
            Result result;
            if (!initCamera(this.mSurface)) {
                return null;
            }
            Semaphore semaphore = new Semaphore(0);
            while (true) {
                QrCamera.this.mCamera.setOneShotPreviewCallback(new QrCamera$DecodingTask$$ExternalSyntheticLambda0(this, semaphore));
                try {
                    semaphore.acquire();
                    result = QrCamera.this.mReader.decodeWithState(new BinaryBitmap(new HybridBinarizer(this.mImage)));
                    QrCamera.this.mReader.reset();
                } catch (ReaderException unused) {
                    QrCamera.this.mReader.reset();
                    result = null;
                } catch (InterruptedException unused2) {
                    Thread.currentThread().interrupt();
                    return null;
                } catch (Throwable th) {
                    QrCamera.this.mReader.reset();
                    throw th;
                }
                if (result != null && QrCamera.this.mScannerCallback.isValid(result.getText())) {
                    return result.getText();
                }
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$doInBackground$0(Semaphore semaphore, byte[] bArr, Camera camera) {
            this.mImage = QrCamera.this.getFrameImage(bArr);
            semaphore.release();
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String str) {
            if (str != null) {
                QrCamera.this.mScannerCallback.handleSuccessfulResult(str);
            }
        }

        private boolean initCamera(SurfaceTexture surfaceTexture) {
            int numberOfCameras = Camera.getNumberOfCameras();
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            int i = 0;
            while (true) {
                if (i >= numberOfCameras) {
                    break;
                }
                try {
                    Camera.getCameraInfo(i, cameraInfo);
                    if (cameraInfo.facing == 0) {
                        QrCamera.this.releaseCamera();
                        QrCamera.this.mCamera = Camera.open(i);
                        int unused = QrCamera.this.mCameraOrientation = cameraInfo.orientation;
                        break;
                    }
                    i++;
                } catch (RuntimeException e) {
                    Log.e("QrCamera", "Fail to open camera: " + e);
                    QrCamera qrCamera = QrCamera.this;
                    qrCamera.mCamera = null;
                    qrCamera.mScannerCallback.handleCameraFailure();
                    return false;
                }
            }
            if (QrCamera.this.mCamera == null && numberOfCameras > 0) {
                Log.i("QrCamera", "Can't find back camera. Opening a different camera");
                Camera.getCameraInfo(0, cameraInfo);
                QrCamera.this.releaseCamera();
                QrCamera.this.mCamera = Camera.open(0);
                int unused2 = QrCamera.this.mCameraOrientation = cameraInfo.orientation;
            }
            try {
                Camera camera = QrCamera.this.mCamera;
                if (camera != null) {
                    camera.setPreviewTexture(surfaceTexture);
                    QrCamera.this.setCameraParameter();
                    QrCamera.this.setTransformationMatrix();
                    if (QrCamera.this.startPreview()) {
                        return true;
                    }
                    throw new IOException("Lost contex");
                }
                throw new IOException("Cannot find available camera");
            } catch (IOException e2) {
                Log.e("QrCamera", "Fail to startPreview camera: " + e2);
                QrCamera qrCamera2 = QrCamera.this;
                qrCamera2.mCamera = null;
                qrCamera2.mScannerCallback.handleCameraFailure();
                return false;
            }
        }
    }

    /* access modifiers changed from: private */
    public void releaseCamera() {
        Camera camera = this.mCamera;
        if (camera != null) {
            camera.release();
            this.mCamera = null;
        }
    }

    /* access modifiers changed from: private */
    public void setTransformationMatrix() {
        float f;
        boolean z = true;
        if (((Context) this.mContext.get()).getResources().getConfiguration().orientation != 1) {
            z = false;
        }
        Size size = this.mPreviewSize;
        int width = z ? size.getWidth() : size.getHeight();
        int height = z ? this.mPreviewSize.getHeight() : this.mPreviewSize.getWidth();
        float ratio = (float) getRatio((double) width, (double) height);
        float f2 = 1.0f;
        if (width > height) {
            f = 1.0f / ratio;
        } else {
            f2 = 1.0f / ratio;
            f = 1.0f;
        }
        Matrix matrix = new Matrix();
        matrix.setScale(f2, f);
        this.mScannerCallback.setTransform(matrix);
    }

    /* access modifiers changed from: private */
    public QrYuvLuminanceSource getFrameImage(byte[] bArr) {
        Rect framePosition = this.mScannerCallback.getFramePosition(this.mPreviewSize, this.mCameraOrientation);
        return (QrYuvLuminanceSource) new QrYuvLuminanceSource(bArr, this.mPreviewSize.getWidth(), this.mPreviewSize.getHeight()).crop(framePosition.left, framePosition.top, framePosition.width(), framePosition.height());
    }

    public void handleMessage(Message message) {
        if (message.what != 1) {
            Log.d("QrCamera", "Unexpected Message: " + message.what);
            return;
        }
        this.mCamera.autoFocus((Camera.AutoFocusCallback) null);
        sendMessageDelayed(obtainMessage(1), 1500);
    }

    private Size getBestPreviewSize(Camera.Parameters parameters) {
        Size viewSize = this.mScannerCallback.getViewSize();
        double ratio = getRatio((double) viewSize.getWidth(), (double) viewSize.getHeight());
        Size size = new Size(0, 0);
        double d = 0.0d;
        for (Camera.Size next : parameters.getSupportedPreviewSizes()) {
            double ratio2 = getRatio((double) next.width, (double) next.height);
            if (next.height * next.width > size.getWidth() * size.getHeight() && (Math.abs(d - ratio) / ratio > 0.1d || Math.abs(ratio2 - ratio) / ratio <= 0.1d)) {
                size = new Size(next.width, next.height);
                d = getRatio((double) next.width, (double) next.height);
            }
        }
        return size;
    }

    private Size getBestPictureSize(Camera.Parameters parameters) {
        Camera.Size previewSize = parameters.getPreviewSize();
        double ratio = getRatio((double) previewSize.width, (double) previewSize.height);
        ArrayList<Size> arrayList = new ArrayList<>();
        ArrayList arrayList2 = new ArrayList();
        for (Camera.Size next : parameters.getSupportedPictureSizes()) {
            double ratio2 = getRatio((double) next.width, (double) next.height);
            if (ratio2 == ratio) {
                arrayList.add(new Size(next.width, next.height));
            } else if (Math.abs(ratio2 - ratio) < 0.1d) {
                arrayList2.add(new Size(next.width, next.height));
            }
        }
        if (arrayList.size() == 0 && arrayList2.size() == 0) {
            Log.d("QrCamera", "No proper picture size, return default picture size");
            Camera.Size pictureSize = parameters.getPictureSize();
            return new Size(pictureSize.width, pictureSize.height);
        }
        if (arrayList.size() == 0) {
            arrayList = arrayList2;
        }
        int i = Integer.MAX_VALUE;
        Size size = null;
        int i2 = previewSize.width * previewSize.height;
        for (Size size2 : arrayList) {
            int abs = Math.abs((size2.getWidth() * size2.getHeight()) - i2);
            if (abs < i) {
                size = size2;
                i = abs;
            }
        }
        return size;
    }

    /* JADX INFO: finally extract failed */
    /* access modifiers changed from: protected */
    public void decodeImage(BinaryBitmap binaryBitmap) {
        Result result;
        try {
            result = this.mReader.decodeWithState(binaryBitmap);
            this.mReader.reset();
        } catch (ReaderException unused) {
            this.mReader.reset();
            result = null;
        } catch (Throwable th) {
            this.mReader.reset();
            throw th;
        }
        if (result != null) {
            this.mScannerCallback.handleSuccessfulResult(result.getText());
        }
    }

    public boolean isDecodeTaskAlive() {
        return this.mDecodeTask != null;
    }
}
