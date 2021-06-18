package com.android.settings.media;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.session.MediaController;
import android.net.Uri;
import android.util.Log;
import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.Slice;
import androidx.slice.builders.ListBuilder;
import androidx.slice.builders.SliceAction;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.slices.CustomSliceRegistry;
import com.android.settings.slices.CustomSliceable;
import com.android.settings.slices.SliceBackgroundWorker;

public class MediaOutputIndicatorSlice implements CustomSliceable {
    private Context mContext;
    private MediaOutputIndicatorWorker mWorker;

    public Intent getIntent() {
        return null;
    }

    public MediaOutputIndicatorSlice(Context context) {
        this.mContext = context;
    }

    public Slice getSlice() {
        if (!isVisible()) {
            return new ListBuilder(this.mContext, getUri(), -1).setIsError(true).build();
        }
        IconCompat createWithResource = IconCompat.createWithResource(this.mContext, 17302833);
        Context context = this.mContext;
        String string = context.getString(R.string.media_output_label_title, new Object[]{Utils.getApplicationLabel(context, getWorker().getPackageName())});
        SliceAction create = SliceAction.create(getBroadcastIntent(this.mContext), createWithResource, 0, string);
        return new ListBuilder(this.mContext, getUri(), -1).setAccentColor(com.android.settingslib.Utils.getColorAccentDefaultColor(this.mContext)).addRow(new ListBuilder.RowBuilder().setTitle(string).setTitleItem(createEmptyIcon(), 0).setSubtitle(getWorker().getCurrentConnectedMediaDevice().getName()).setPrimaryAction(create)).build();
    }

    private IconCompat createEmptyIcon() {
        return IconCompat.createWithBitmap(Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888));
    }

    public Uri getUri() {
        return CustomSliceRegistry.MEDIA_OUTPUT_INDICATOR_SLICE_URI;
    }

    public Class getBackgroundWorkerClass() {
        return MediaOutputIndicatorWorker.class;
    }

    private MediaOutputIndicatorWorker getWorker() {
        if (this.mWorker == null) {
            this.mWorker = (MediaOutputIndicatorWorker) SliceBackgroundWorker.getInstance(getUri());
        }
        return this.mWorker;
    }

    /* access modifiers changed from: package-private */
    public boolean isVisible() {
        return getWorker() != null && !com.android.settingslib.Utils.isAudioModeOngoingCall(this.mContext) && getWorker().getMediaDevices().size() > 0 && getWorker().getActiveLocalMediaController() != null;
    }

    public void onNotifyChange(Intent intent) {
        MediaController activeLocalMediaController = getWorker().getActiveLocalMediaController();
        if (activeLocalMediaController == null) {
            Log.d("MediaOutputIndSlice", "No active local media controller");
            return;
        }
        this.mContext.sendBroadcast(new Intent().setPackage("com.android.systemui").setAction("com.android.systemui.action.LAUNCH_MEDIA_OUTPUT_DIALOG").putExtra("key_media_session_token", activeLocalMediaController.getSessionToken()).putExtra("package_name", activeLocalMediaController.getPackageName()));
        this.mContext.sendBroadcast(new Intent().setPackage("com.android.settings").setAction("com.android.settings.panel.action.CLOSE_PANEL"));
    }
}
