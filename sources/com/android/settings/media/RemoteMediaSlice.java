package com.android.settings.media;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaRouter2Manager;
import android.media.RoutingSessionInfo;
import android.net.Uri;
import android.text.TextUtils;
import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.builders.SliceAction;
import com.android.settings.R;
import com.android.settings.SubSettings;
import com.android.settings.Utils;
import com.android.settings.notification.SoundSettings;
import com.android.settings.slices.CustomSliceRegistry;
import com.android.settings.slices.CustomSliceable;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.slices.SliceBroadcastReceiver;
import com.android.settings.slices.SliceBuilderUtils;

public class RemoteMediaSlice implements CustomSliceable {
    private final Context mContext;
    MediaRouter2Manager mRouterManager;
    private MediaDeviceUpdateWorker mWorker;

    public Intent getIntent() {
        return null;
    }

    public RemoteMediaSlice(Context context) {
        this.mContext = context;
    }

    public void onNotifyChange(Intent intent) {
        int intExtra = intent.getIntExtra("android.app.slice.extra.RANGE_VALUE", -1);
        String stringExtra = intent.getStringExtra("media_id");
        if (!TextUtils.isEmpty(stringExtra)) {
            getWorker().adjustSessionVolume(stringExtra, intExtra);
        } else if (TextUtils.equals("action_launch_dialog", intent.getStringExtra("customized_action"))) {
            this.mContext.sendBroadcast(new Intent().setPackage("com.android.systemui").setAction("com.android.systemui.action.LAUNCH_MEDIA_OUTPUT_DIALOG").putExtra("package_name", intent.getParcelableExtra("RoutingSessionInfo").getClientPackageName()));
            this.mContext.sendBroadcast(new Intent().setPackage("com.android.settings").setAction("com.android.settings.panel.action.CLOSE_PANEL"));
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r9v2, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v8, resolved type: android.text.SpannableString} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r9v3, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r9v5, resolved type: java.lang.String} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public androidx.slice.Slice getSlice() {
        /*
            r14 = this;
            androidx.slice.builders.ListBuilder r0 = new androidx.slice.builders.ListBuilder
            android.content.Context r1 = r14.mContext
            android.net.Uri r2 = r14.getUri()
            r3 = -1
            r0.<init>(r1, r2, r3)
            r1 = -1
            androidx.slice.builders.ListBuilder r0 = r0.setAccentColor(r1)
            com.android.settings.media.MediaDeviceUpdateWorker r1 = r14.getWorker()
            java.lang.String r2 = "RemoteMediaSlice"
            if (r1 != 0) goto L_0x0024
            java.lang.String r14 = "Unable to get the slice worker."
            android.util.Log.e(r2, r14)
            androidx.slice.Slice r14 = r0.build()
            return r14
        L_0x0024:
            android.media.MediaRouter2Manager r1 = r14.mRouterManager
            if (r1 != 0) goto L_0x0030
            android.content.Context r1 = r14.mContext
            android.media.MediaRouter2Manager r1 = android.media.MediaRouter2Manager.getInstance(r1)
            r14.mRouterManager = r1
        L_0x0030:
            com.android.settings.media.MediaDeviceUpdateWorker r1 = r14.getWorker()
            java.util.List r1 = r1.getActiveRemoteMediaDevice()
            boolean r3 = r1.isEmpty()
            if (r3 == 0) goto L_0x0048
            java.lang.String r14 = "No active remote media device"
            android.util.Log.d(r2, r14)
            androidx.slice.Slice r14 = r0.build()
            return r14
        L_0x0048:
            android.content.Context r3 = r14.mContext
            r4 = 2130972544(0x7f040f80, float:1.7553858E38)
            java.lang.CharSequence r3 = r3.getText(r4)
            android.content.Context r4 = r14.mContext
            r5 = 2130838424(0x7f020398, float:1.728183E38)
            androidx.core.graphics.drawable.IconCompat r4 = androidx.core.graphics.drawable.IconCompat.createWithResource(r4, r5)
            androidx.core.graphics.drawable.IconCompat r5 = r14.createEmptyIcon()
            java.util.Iterator r1 = r1.iterator()
        L_0x0062:
            boolean r6 = r1.hasNext()
            if (r6 == 0) goto L_0x013f
            java.lang.Object r6 = r1.next()
            android.media.RoutingSessionInfo r6 = (android.media.RoutingSessionInfo) r6
            int r7 = r6.getVolumeMax()
            if (r7 > 0) goto L_0x0095
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r9 = "Unable to add Slice. "
            r8.append(r9)
            java.lang.CharSequence r6 = r6.getName()
            r8.append(r6)
            java.lang.String r6 = ": max volume is "
            r8.append(r6)
            r8.append(r7)
            java.lang.String r6 = r8.toString()
            android.util.Log.d(r2, r6)
            goto L_0x0062
        L_0x0095:
            android.content.Context r8 = r14.mContext
            java.lang.String r9 = r6.getClientPackageName()
            java.lang.CharSequence r8 = com.android.settings.Utils.getApplicationLabel(r8, r9)
            android.content.Context r9 = r14.mContext
            r10 = 2130971740(0x7f040c5c, float:1.7552227E38)
            r11 = 1
            java.lang.Object[] r11 = new java.lang.Object[r11]
            r12 = 0
            r11[r12] = r8
            java.lang.String r9 = r9.getString(r10, r11)
            androidx.slice.builders.ListBuilder$InputRangeBuilder r10 = new androidx.slice.builders.ListBuilder$InputRangeBuilder
            r10.<init>()
            androidx.slice.builders.ListBuilder$InputRangeBuilder r10 = r10.setTitleItem(r4, r12)
            androidx.slice.builders.ListBuilder$InputRangeBuilder r10 = r10.setTitle(r3)
            java.lang.String r11 = r6.getId()
            int r11 = r11.hashCode()
            java.lang.String r13 = r6.getId()
            android.app.PendingIntent r11 = r14.getSliderInputAction(r11, r13)
            androidx.slice.builders.ListBuilder$InputRangeBuilder r10 = r10.setInputAction(r11)
            java.lang.String r11 = r6.getId()
            androidx.slice.builders.SliceAction r11 = r14.getSoundSettingAction(r3, r4, r11)
            androidx.slice.builders.ListBuilder$InputRangeBuilder r10 = r10.setPrimaryAction(r11)
            androidx.slice.builders.ListBuilder$InputRangeBuilder r7 = r10.setMax(r7)
            int r10 = r6.getVolume()
            androidx.slice.builders.ListBuilder$InputRangeBuilder r7 = r7.setValue(r10)
            r0.addInputRange(r7)
            com.android.settings.media.MediaDeviceUpdateWorker r7 = r14.getWorker()
            java.lang.String r10 = r6.getClientPackageName()
            boolean r7 = r7.shouldDisableMediaOutput(r10)
            android.text.SpannableString r10 = new android.text.SpannableString
            boolean r11 = android.text.TextUtils.isEmpty(r8)
            if (r11 == 0) goto L_0x0100
            java.lang.String r8 = ""
        L_0x0100:
            r10.<init>(r8)
            android.text.style.ForegroundColorSpan r8 = new android.text.style.ForegroundColorSpan
            android.content.Context r11 = r14.mContext
            r13 = 16842808(0x1010038, float:2.3693715E-38)
            int r11 = com.android.settingslib.Utils.getColorAttrDefaultColor(r11, r13)
            r8.<init>(r11)
            int r11 = r10.length()
            r13 = 33
            r10.setSpan(r8, r12, r11, r13)
            androidx.slice.builders.ListBuilder$RowBuilder r8 = new androidx.slice.builders.ListBuilder$RowBuilder
            r8.<init>()
            if (r7 == 0) goto L_0x0122
            r9 = r10
        L_0x0122:
            androidx.slice.builders.ListBuilder$RowBuilder r8 = r8.setTitle(r9)
            java.lang.CharSequence r9 = r6.getName()
            androidx.slice.builders.ListBuilder$RowBuilder r8 = r8.setSubtitle(r9)
            androidx.slice.builders.ListBuilder$RowBuilder r8 = r8.setTitleItem((androidx.core.graphics.drawable.IconCompat) r5, (int) r12)
            androidx.slice.builders.SliceAction r6 = r14.getMediaOutputDialogAction(r6, r7)
            androidx.slice.builders.ListBuilder$RowBuilder r6 = r8.setPrimaryAction(r6)
            r0.addRow(r6)
            goto L_0x0062
        L_0x013f:
            androidx.slice.Slice r14 = r0.build()
            return r14
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.media.RemoteMediaSlice.getSlice():androidx.slice.Slice");
    }

    private IconCompat createEmptyIcon() {
        return IconCompat.createWithBitmap(Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888));
    }

    private PendingIntent getSliderInputAction(int i, String str) {
        return PendingIntent.getBroadcast(this.mContext, i, new Intent(getUri().toString()).setData(getUri()).putExtra("media_id", str).setClass(this.mContext, SliceBroadcastReceiver.class), 33554432);
    }

    private SliceAction getSoundSettingAction(CharSequence charSequence, IconCompat iconCompat, String str) {
        Uri build = new Uri.Builder().appendPath(str).build();
        Intent buildSearchResultPageIntent = SliceBuilderUtils.buildSearchResultPageIntent(this.mContext, SoundSettings.class.getName(), str, this.mContext.getText(R.string.sound_settings).toString(), 0);
        buildSearchResultPageIntent.setClassName(this.mContext.getPackageName(), SubSettings.class.getName());
        buildSearchResultPageIntent.setData(build);
        return SliceAction.createDeeplink(PendingIntent.getActivity(this.mContext, 0, buildSearchResultPageIntent, 67108864), iconCompat, 0, charSequence);
    }

    private SliceAction getMediaOutputDialogAction(RoutingSessionInfo routingSessionInfo, boolean z) {
        PendingIntent broadcast = PendingIntent.getBroadcast(this.mContext, routingSessionInfo.hashCode(), new Intent(getUri().toString()).setData(getUri()).setClass(this.mContext, SliceBroadcastReceiver.class).putExtra("customized_action", z ? "" : "action_launch_dialog").putExtra("RoutingSessionInfo", routingSessionInfo).addFlags(268435456), 201326592);
        IconCompat createWithResource = IconCompat.createWithResource(this.mContext, R.drawable.ic_volume_remote);
        Context context = this.mContext;
        return SliceAction.createDeeplink(broadcast, createWithResource, 0, context.getString(R.string.media_output_label_title, new Object[]{Utils.getApplicationLabel(context, routingSessionInfo.getClientPackageName())}));
    }

    public Uri getUri() {
        return CustomSliceRegistry.REMOTE_MEDIA_SLICE_URI;
    }

    public Class getBackgroundWorkerClass() {
        return MediaDeviceUpdateWorker.class;
    }

    private MediaDeviceUpdateWorker getWorker() {
        if (this.mWorker == null) {
            this.mWorker = (MediaDeviceUpdateWorker) SliceBackgroundWorker.getInstance(getUri());
        }
        return this.mWorker;
    }
}
