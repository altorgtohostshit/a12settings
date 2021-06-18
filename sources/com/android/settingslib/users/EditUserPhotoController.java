package com.android.settingslib.users;

import android.app.Activity;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.os.UserHandle;
import android.provider.ContactsContract;
import android.util.EventLog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import androidx.core.content.FileProvider;
import com.android.settingslib.R$dimen;
import com.android.settingslib.R$id;
import com.android.settingslib.R$layout;
import com.android.settingslib.R$string;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.drawable.CircleFramedDrawable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import libcore.io.Streams;

public class EditUserPhotoController {
    /* access modifiers changed from: private */
    public final Activity mActivity;
    private final ActivityStarter mActivityStarter;
    private final Uri mCropPictureUri;
    private final String mFileAuthority;
    private final ImageView mImageView;
    private final File mImagesDir;
    private Bitmap mNewUserPhotoBitmap;
    private Drawable mNewUserPhotoDrawable;
    /* access modifiers changed from: private */
    public final int mPhotoSize;
    /* access modifiers changed from: private */
    public final Uri mTakePictureUri;

    public EditUserPhotoController(Activity activity, ActivityStarter activityStarter, ImageView imageView, Bitmap bitmap, boolean z, String str) {
        this.mActivity = activity;
        this.mActivityStarter = activityStarter;
        this.mImageView = imageView;
        this.mFileAuthority = str;
        File file = new File(activity.getCacheDir(), "multi_user");
        this.mImagesDir = file;
        file.mkdir();
        this.mCropPictureUri = createTempImageUri(activity, "CropEditUserPhoto.jpg", !z);
        this.mTakePictureUri = createTempImageUri(activity, "TakeEditUserPhoto.jpg", !z);
        this.mPhotoSize = getPhotoSize(activity);
        imageView.setOnClickListener(new EditUserPhotoController$$ExternalSyntheticLambda0(this));
        this.mNewUserPhotoBitmap = bitmap;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view) {
        showUpdatePhotoPopup();
    }

    public boolean onActivityResult(int i, int i2, Intent intent) {
        if (i2 != -1) {
            return false;
        }
        Uri data = (intent == null || intent.getData() == null) ? this.mTakePictureUri : intent.getData();
        if (!"content".equals(data.getScheme())) {
            Log.e("EditUserPhotoController", "Invalid pictureUri scheme: " + data.getScheme());
            EventLog.writeEvent(1397638484, new Object[]{"172939189", -1, data.getPath()});
            return false;
        }
        switch (i) {
            case 1001:
            case 1002:
                if (!this.mTakePictureUri.equals(data)) {
                    copyAndCropPhoto(data);
                } else if (PhotoCapabilityUtils.canCropPhoto(this.mActivity)) {
                    cropPhoto();
                } else {
                    onPhotoNotCropped(data);
                }
                return true;
            case 1003:
                onPhotoCropped(data);
                return true;
            default:
                return false;
        }
    }

    public Drawable getNewUserPhotoDrawable() {
        return this.mNewUserPhotoDrawable;
    }

    private void showUpdatePhotoPopup() {
        Context context = this.mImageView.getContext();
        boolean canTakePhoto = PhotoCapabilityUtils.canTakePhoto(context);
        boolean canChoosePhoto = PhotoCapabilityUtils.canChoosePhoto(context);
        if (canTakePhoto || canChoosePhoto) {
            ArrayList arrayList = new ArrayList();
            if (canTakePhoto) {
                arrayList.add(new RestrictedMenuItem(context, context.getString(R$string.user_image_take_photo), "no_set_user_icon", new EditUserPhotoController$$ExternalSyntheticLambda3(this)));
            }
            if (canChoosePhoto) {
                arrayList.add(new RestrictedMenuItem(context, context.getString(R$string.user_image_choose_photo), "no_set_user_icon", new EditUserPhotoController$$ExternalSyntheticLambda2(this)));
            }
            ListPopupWindow listPopupWindow = new ListPopupWindow(context);
            listPopupWindow.setAnchorView(this.mImageView);
            listPopupWindow.setModal(true);
            listPopupWindow.setInputMethodMode(2);
            listPopupWindow.setAdapter(new RestrictedPopupMenuAdapter(context, arrayList));
            listPopupWindow.setWidth(Math.max(this.mImageView.getWidth(), context.getResources().getDimensionPixelSize(R$dimen.update_user_photo_popup_min_width)));
            listPopupWindow.setDropDownGravity(8388611);
            listPopupWindow.setOnItemClickListener(new EditUserPhotoController$$ExternalSyntheticLambda1(listPopupWindow));
            listPopupWindow.show();
        }
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$showUpdatePhotoPopup$1(ListPopupWindow listPopupWindow, AdapterView adapterView, View view, int i, long j) {
        listPopupWindow.dismiss();
        ((RestrictedMenuItem) adapterView.getAdapter().getItem(i)).doAction();
    }

    /* access modifiers changed from: private */
    public void takePhoto() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE_SECURE");
        appendOutputExtra(intent, this.mTakePictureUri);
        this.mActivityStarter.startActivityForResult(intent, 1002);
    }

    /* access modifiers changed from: private */
    public void choosePhoto() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT", (Uri) null);
        intent.setType("image/*");
        appendOutputExtra(intent, this.mTakePictureUri);
        this.mActivityStarter.startActivityForResult(intent, 1001);
    }

    private void copyAndCropPhoto(final Uri uri) {
        new AsyncTask<Void, Void, Void>() {
            /* access modifiers changed from: protected */
            public Void doInBackground(Void... voidArr) {
                OutputStream openOutputStream;
                ContentResolver contentResolver = EditUserPhotoController.this.mActivity.getContentResolver();
                try {
                    InputStream openInputStream = contentResolver.openInputStream(uri);
                    try {
                        openOutputStream = contentResolver.openOutputStream(EditUserPhotoController.this.mTakePictureUri);
                        Streams.copy(openInputStream, openOutputStream);
                        if (openOutputStream != null) {
                            openOutputStream.close();
                        }
                        if (openInputStream == null) {
                            return null;
                        }
                        openInputStream.close();
                        return null;
                    } catch (Throwable th) {
                        if (openInputStream != null) {
                            openInputStream.close();
                        }
                        throw th;
                    }
                    throw th;
                } catch (IOException e) {
                    Log.w("EditUserPhotoController", "Failed to copy photo", e);
                    return null;
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(Void voidR) {
                if (!EditUserPhotoController.this.mActivity.isFinishing() && !EditUserPhotoController.this.mActivity.isDestroyed()) {
                    EditUserPhotoController.this.cropPhoto();
                }
            }
        }.execute(new Void[0]);
    }

    /* access modifiers changed from: private */
    public void cropPhoto() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(this.mTakePictureUri, "image/*");
        appendOutputExtra(intent, this.mCropPictureUri);
        appendCropExtras(intent);
        if (intent.resolveActivity(this.mActivity.getPackageManager()) != null) {
            try {
                StrictMode.disableDeathOnFileUriExposure();
                this.mActivityStarter.startActivityForResult(intent, 1003);
            } finally {
                StrictMode.enableDeathOnFileUriExposure();
            }
        } else {
            onPhotoNotCropped(this.mTakePictureUri);
        }
    }

    private void appendOutputExtra(Intent intent, Uri uri) {
        intent.putExtra("output", uri);
        intent.addFlags(3);
        intent.setClipData(ClipData.newRawUri("output", uri));
    }

    private void appendCropExtras(Intent intent) {
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", this.mPhotoSize);
        intent.putExtra("outputY", this.mPhotoSize);
    }

    private void onPhotoCropped(final Uri uri) {
        new AsyncTask<Void, Void, Bitmap>() {
            /* access modifiers changed from: protected */
            /* JADX WARNING: Removed duplicated region for block: B:20:0x0034 A[SYNTHETIC, Splitter:B:20:0x0034] */
            /* JADX WARNING: Removed duplicated region for block: B:27:0x0040 A[SYNTHETIC, Splitter:B:27:0x0040] */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public android.graphics.Bitmap doInBackground(java.lang.Void... r6) {
                /*
                    r5 = this;
                    java.lang.String r6 = "Cannot close image stream"
                    java.lang.String r0 = "EditUserPhotoController"
                    r1 = 0
                    com.android.settingslib.users.EditUserPhotoController r2 = com.android.settingslib.users.EditUserPhotoController.this     // Catch:{ FileNotFoundException -> 0x002b, all -> 0x0026 }
                    android.app.Activity r2 = r2.mActivity     // Catch:{ FileNotFoundException -> 0x002b, all -> 0x0026 }
                    android.content.ContentResolver r2 = r2.getContentResolver()     // Catch:{ FileNotFoundException -> 0x002b, all -> 0x0026 }
                    android.net.Uri r5 = r2     // Catch:{ FileNotFoundException -> 0x002b, all -> 0x0026 }
                    java.io.InputStream r5 = r2.openInputStream(r5)     // Catch:{ FileNotFoundException -> 0x002b, all -> 0x0026 }
                    android.graphics.Bitmap r1 = android.graphics.BitmapFactory.decodeStream(r5)     // Catch:{ FileNotFoundException -> 0x0024 }
                    if (r5 == 0) goto L_0x0023
                    r5.close()     // Catch:{ IOException -> 0x001f }
                    goto L_0x0023
                L_0x001f:
                    r5 = move-exception
                    android.util.Log.w(r0, r6, r5)
                L_0x0023:
                    return r1
                L_0x0024:
                    r2 = move-exception
                    goto L_0x002d
                L_0x0026:
                    r5 = move-exception
                    r4 = r1
                    r1 = r5
                    r5 = r4
                    goto L_0x003e
                L_0x002b:
                    r2 = move-exception
                    r5 = r1
                L_0x002d:
                    java.lang.String r3 = "Cannot find image file"
                    android.util.Log.w(r0, r3, r2)     // Catch:{ all -> 0x003d }
                    if (r5 == 0) goto L_0x003c
                    r5.close()     // Catch:{ IOException -> 0x0038 }
                    goto L_0x003c
                L_0x0038:
                    r5 = move-exception
                    android.util.Log.w(r0, r6, r5)
                L_0x003c:
                    return r1
                L_0x003d:
                    r1 = move-exception
                L_0x003e:
                    if (r5 == 0) goto L_0x0048
                    r5.close()     // Catch:{ IOException -> 0x0044 }
                    goto L_0x0048
                L_0x0044:
                    r5 = move-exception
                    android.util.Log.w(r0, r6, r5)
                L_0x0048:
                    throw r1
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.users.EditUserPhotoController.C15092.doInBackground(java.lang.Void[]):android.graphics.Bitmap");
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(Bitmap bitmap) {
                EditUserPhotoController.this.onPhotoProcessed(bitmap);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[]) null);
    }

    private void onPhotoNotCropped(final Uri uri) {
        new AsyncTask<Void, Void, Bitmap>() {
            /* access modifiers changed from: protected */
            public Bitmap doInBackground(Void... voidArr) {
                Bitmap createBitmap = Bitmap.createBitmap(EditUserPhotoController.this.mPhotoSize, EditUserPhotoController.this.mPhotoSize, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                try {
                    Bitmap decodeStream = BitmapFactory.decodeStream(EditUserPhotoController.this.mActivity.getContentResolver().openInputStream(uri));
                    if (decodeStream != null) {
                        EditUserPhotoController editUserPhotoController = EditUserPhotoController.this;
                        int access$500 = editUserPhotoController.getRotation(editUserPhotoController.mActivity, uri);
                        int min = Math.min(decodeStream.getWidth(), decodeStream.getHeight());
                        int width = (decodeStream.getWidth() - min) / 2;
                        int height = (decodeStream.getHeight() - min) / 2;
                        Matrix matrix = new Matrix();
                        matrix.setRectToRect(new RectF((float) width, (float) height, (float) (width + min), (float) (height + min)), new RectF(0.0f, 0.0f, (float) EditUserPhotoController.this.mPhotoSize, (float) EditUserPhotoController.this.mPhotoSize), Matrix.ScaleToFit.CENTER);
                        matrix.postRotate((float) access$500, ((float) EditUserPhotoController.this.mPhotoSize) / 2.0f, ((float) EditUserPhotoController.this.mPhotoSize) / 2.0f);
                        canvas.drawBitmap(decodeStream, matrix, new Paint());
                        return createBitmap;
                    }
                } catch (FileNotFoundException unused) {
                }
                return null;
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(Bitmap bitmap) {
                EditUserPhotoController.this.onPhotoProcessed(bitmap);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[]) null);
    }

    /* access modifiers changed from: private */
    public int getRotation(Context context, Uri uri) {
        int i = -1;
        try {
            i = new ExifInterface(context.getContentResolver().openInputStream(uri)).getAttributeInt("Orientation", -1);
        } catch (IOException e) {
            Log.e("EditUserPhotoController", "Error while getting rotation", e);
        }
        if (i == 3) {
            return 180;
        }
        if (i != 6) {
            return i != 8 ? 0 : 270;
        }
        return 90;
    }

    /* access modifiers changed from: private */
    public void onPhotoProcessed(Bitmap bitmap) {
        if (bitmap != null) {
            this.mNewUserPhotoBitmap = bitmap;
            CircleFramedDrawable instance = CircleFramedDrawable.getInstance(this.mImageView.getContext(), this.mNewUserPhotoBitmap);
            this.mNewUserPhotoDrawable = instance;
            this.mImageView.setImageDrawable(instance);
        }
        new File(this.mImagesDir, "TakeEditUserPhoto.jpg").delete();
        new File(this.mImagesDir, "CropEditUserPhoto.jpg").delete();
    }

    private static int getPhotoSize(Context context) {
        Cursor query = context.getContentResolver().query(ContactsContract.DisplayPhoto.CONTENT_MAX_DIMENSIONS_URI, new String[]{"display_max_dim"}, (String) null, (String[]) null, (String) null);
        if (query != null) {
            try {
                query.moveToFirst();
                int i = query.getInt(0);
                query.close();
                return i;
            } catch (Throwable th) {
                th.addSuppressed(th);
            }
        } else {
            if (query != null) {
                query.close();
            }
            return 500;
        }
        throw th;
    }

    private Uri createTempImageUri(Context context, String str, boolean z) {
        File file = new File(this.mImagesDir, str);
        if (z) {
            file.delete();
        }
        return FileProvider.getUriForFile(context, this.mFileAuthority, file);
    }

    /* access modifiers changed from: package-private */
    public File saveNewUserPhotoBitmap() {
        if (this.mNewUserPhotoBitmap == null) {
            return null;
        }
        try {
            File file = new File(this.mImagesDir, "NewUserPhoto.png");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            this.mNewUserPhotoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            return file;
        } catch (IOException e) {
            Log.e("EditUserPhotoController", "Cannot create temp file", e);
            return null;
        }
    }

    static Bitmap loadNewUserPhotoBitmap(File file) {
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }

    /* access modifiers changed from: package-private */
    public void removeNewUserPhotoBitmapFile() {
        new File(this.mImagesDir, "NewUserPhoto.png").delete();
    }

    private static final class RestrictedMenuItem {
        private final Runnable mAction;
        private final RestrictedLockUtils.EnforcedAdmin mAdmin;
        private final Context mContext;
        private final boolean mIsRestrictedByBase;
        private final String mTitle;

        RestrictedMenuItem(Context context, String str, String str2, Runnable runnable) {
            this.mContext = context;
            this.mTitle = str;
            this.mAction = runnable;
            int myUserId = UserHandle.myUserId();
            this.mAdmin = RestrictedLockUtilsInternal.checkIfRestrictionEnforced(context, str2, myUserId);
            this.mIsRestrictedByBase = RestrictedLockUtilsInternal.hasBaseUserRestriction(context, str2, myUserId);
        }

        public String toString() {
            return this.mTitle;
        }

        /* access modifiers changed from: package-private */
        public void doAction() {
            if (!isRestrictedByBase()) {
                if (isRestrictedByAdmin()) {
                    RestrictedLockUtils.sendShowAdminSupportDetailsIntent(this.mContext, this.mAdmin);
                } else {
                    this.mAction.run();
                }
            }
        }

        /* access modifiers changed from: package-private */
        public boolean isRestrictedByAdmin() {
            return this.mAdmin != null;
        }

        /* access modifiers changed from: package-private */
        public boolean isRestrictedByBase() {
            return this.mIsRestrictedByBase;
        }
    }

    private static final class RestrictedPopupMenuAdapter extends ArrayAdapter<RestrictedMenuItem> {
        RestrictedPopupMenuAdapter(Context context, List<RestrictedMenuItem> list) {
            super(context, R$layout.restricted_popup_menu_item, R$id.text, list);
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            View view2 = super.getView(i, view, viewGroup);
            RestrictedMenuItem restrictedMenuItem = (RestrictedMenuItem) getItem(i);
            TextView textView = (TextView) view2.findViewById(R$id.text);
            ImageView imageView = (ImageView) view2.findViewById(R$id.restricted_icon);
            int i2 = 0;
            textView.setEnabled(!restrictedMenuItem.isRestrictedByAdmin() && !restrictedMenuItem.isRestrictedByBase());
            if (!restrictedMenuItem.isRestrictedByAdmin() || restrictedMenuItem.isRestrictedByBase()) {
                i2 = 8;
            }
            imageView.setVisibility(i2);
            return view2;
        }
    }
}
