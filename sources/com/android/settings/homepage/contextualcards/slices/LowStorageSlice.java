package com.android.settings.homepage.contextualcards.slices;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.storage.StorageManager;
import android.text.format.Formatter;
import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.Slice;
import androidx.slice.builders.ListBuilder;
import androidx.slice.builders.SliceAction;
import com.android.settings.R;
import com.android.settings.SubSettings;
import com.android.settings.deviceinfo.StorageDashboardFragment;
import com.android.settings.slices.CustomSliceRegistry;
import com.android.settings.slices.CustomSliceable;
import com.android.settings.slices.SliceBuilderUtils;
import com.android.settingslib.Utils;
import com.android.settingslib.deviceinfo.PrivateStorageInfo;
import com.android.settingslib.deviceinfo.StorageManagerVolumeProvider;
import java.text.NumberFormat;

public class LowStorageSlice implements CustomSliceable {
    private final Context mContext;

    public void onNotifyChange(Intent intent) {
    }

    public LowStorageSlice(Context context) {
        this.mContext = context;
    }

    public Slice getSlice() {
        PrivateStorageInfo privateStorageInfo = PrivateStorageInfo.getPrivateStorageInfo(new StorageManagerVolumeProvider((StorageManager) this.mContext.getSystemService(StorageManager.class)));
        long j = privateStorageInfo.totalBytes;
        double d = ((double) (j - privateStorageInfo.freeBytes)) / ((double) j);
        String format = NumberFormat.getPercentInstance().format(d);
        String formatFileSize = Formatter.formatFileSize(this.mContext, privateStorageInfo.freeBytes);
        ListBuilder accentColor = new ListBuilder(this.mContext, CustomSliceRegistry.LOW_STORAGE_SLICE_URI, -1).setAccentColor(Utils.getColorAccentDefaultColor(this.mContext));
        IconCompat createWithResource = IconCompat.createWithResource(this.mContext, R.drawable.ic_storage);
        if (d < 0.85d) {
            return accentColor.addRow(buildRowBuilder(this.mContext.getText(R.string.storage_settings), this.mContext.getString(R.string.storage_summary, new Object[]{format, formatFileSize}), createWithResource)).setIsError(true).build();
        }
        return accentColor.addRow(buildRowBuilder(this.mContext.getText(R.string.storage_menu_free), this.mContext.getString(R.string.low_storage_summary, new Object[]{format, formatFileSize}), createWithResource)).build();
    }

    public Uri getUri() {
        return CustomSliceRegistry.LOW_STORAGE_SLICE_URI;
    }

    public Intent getIntent() {
        return SliceBuilderUtils.buildSearchResultPageIntent(this.mContext, StorageDashboardFragment.class.getName(), "", this.mContext.getText(R.string.storage_label).toString(), 1401).setClassName(this.mContext.getPackageName(), SubSettings.class.getName()).setData(CustomSliceRegistry.LOW_STORAGE_SLICE_URI);
    }

    private ListBuilder.RowBuilder buildRowBuilder(CharSequence charSequence, String str, IconCompat iconCompat) {
        return new ListBuilder.RowBuilder().setTitleItem(iconCompat, 0).setTitle(charSequence).setSubtitle(str).setPrimaryAction(SliceAction.createDeeplink(PendingIntent.getActivity(this.mContext, 0, getIntent(), 67108864), iconCompat, 0, charSequence));
    }
}
