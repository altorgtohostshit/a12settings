package com.android.settings.notification.history;

import android.content.Context;
import android.content.pm.PackageManager;
import com.android.settings.notification.NotificationBackend;
import com.android.settingslib.utils.ThreadUtils;
import java.util.List;

public class HistoryLoader {
    private final NotificationBackend mBackend;
    private final Context mContext;
    private final PackageManager mPm;

    interface OnHistoryLoaderListener {
        void onHistoryLoaded(List<NotificationHistoryPackage> list);
    }

    public HistoryLoader(Context context, NotificationBackend notificationBackend, PackageManager packageManager) {
        this.mContext = context;
        this.mBackend = notificationBackend;
        this.mPm = packageManager;
    }

    public void load(OnHistoryLoaderListener onHistoryLoaderListener) {
        ThreadUtils.postOnBackgroundThread((Runnable) new HistoryLoader$$ExternalSyntheticLambda1(this, onHistoryLoaderListener));
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:14:?, code lost:
        r2.icon = r7.mPm.getDefaultActivityIcon();
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x00ad */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public /* synthetic */ void lambda$load$2(com.android.settings.notification.history.HistoryLoader.OnHistoryLoaderListener r8) {
        /*
            r7 = this;
            java.util.HashMap r0 = new java.util.HashMap     // Catch:{ Exception -> 0x00bf }
            r0.<init>()     // Catch:{ Exception -> 0x00bf }
            com.android.settings.notification.NotificationBackend r1 = r7.mBackend     // Catch:{ Exception -> 0x00bf }
            android.content.Context r2 = r7.mContext     // Catch:{ Exception -> 0x00bf }
            java.lang.String r2 = r2.getPackageName()     // Catch:{ Exception -> 0x00bf }
            android.content.Context r3 = r7.mContext     // Catch:{ Exception -> 0x00bf }
            java.lang.String r3 = r3.getAttributionTag()     // Catch:{ Exception -> 0x00bf }
            android.app.NotificationHistory r1 = r1.getNotificationHistory(r2, r3)     // Catch:{ Exception -> 0x00bf }
        L_0x0017:
            boolean r2 = r1.hasNextNotification()     // Catch:{ Exception -> 0x00bf }
            if (r2 == 0) goto L_0x0059
            android.app.NotificationHistory$HistoricalNotification r2 = r1.getNextNotification()     // Catch:{ Exception -> 0x00bf }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00bf }
            r3.<init>()     // Catch:{ Exception -> 0x00bf }
            java.lang.String r4 = r2.getPackage()     // Catch:{ Exception -> 0x00bf }
            r3.append(r4)     // Catch:{ Exception -> 0x00bf }
            java.lang.String r4 = "|"
            r3.append(r4)     // Catch:{ Exception -> 0x00bf }
            int r4 = r2.getUid()     // Catch:{ Exception -> 0x00bf }
            r3.append(r4)     // Catch:{ Exception -> 0x00bf }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x00bf }
            com.android.settings.notification.history.NotificationHistoryPackage r4 = new com.android.settings.notification.history.NotificationHistoryPackage     // Catch:{ Exception -> 0x00bf }
            java.lang.String r5 = r2.getPackage()     // Catch:{ Exception -> 0x00bf }
            int r6 = r2.getUid()     // Catch:{ Exception -> 0x00bf }
            r4.<init>(r5, r6)     // Catch:{ Exception -> 0x00bf }
            java.lang.Object r4 = r0.getOrDefault(r3, r4)     // Catch:{ Exception -> 0x00bf }
            com.android.settings.notification.history.NotificationHistoryPackage r4 = (com.android.settings.notification.history.NotificationHistoryPackage) r4     // Catch:{ Exception -> 0x00bf }
            java.util.TreeSet<android.app.NotificationHistory$HistoricalNotification> r5 = r4.notifications     // Catch:{ Exception -> 0x00bf }
            r5.add(r2)     // Catch:{ Exception -> 0x00bf }
            r0.put(r3, r4)     // Catch:{ Exception -> 0x00bf }
            goto L_0x0017
        L_0x0059:
            java.util.ArrayList r1 = new java.util.ArrayList     // Catch:{ Exception -> 0x00bf }
            java.util.Collection r0 = r0.values()     // Catch:{ Exception -> 0x00bf }
            r1.<init>(r0)     // Catch:{ Exception -> 0x00bf }
            com.android.settings.notification.history.HistoryLoader$$ExternalSyntheticLambda2 r0 = com.android.settings.notification.history.HistoryLoader$$ExternalSyntheticLambda2.INSTANCE     // Catch:{ Exception -> 0x00bf }
            java.util.Collections.sort(r1, r0)     // Catch:{ Exception -> 0x00bf }
            java.util.Iterator r0 = r1.iterator()     // Catch:{ Exception -> 0x00bf }
        L_0x006b:
            boolean r2 = r0.hasNext()     // Catch:{ Exception -> 0x00bf }
            if (r2 == 0) goto L_0x00b6
            java.lang.Object r2 = r0.next()     // Catch:{ Exception -> 0x00bf }
            com.android.settings.notification.history.NotificationHistoryPackage r2 = (com.android.settings.notification.history.NotificationHistoryPackage) r2     // Catch:{ Exception -> 0x00bf }
            android.content.pm.PackageManager r3 = r7.mPm     // Catch:{ NameNotFoundException -> 0x00ad }
            java.lang.String r4 = r2.pkgName     // Catch:{ NameNotFoundException -> 0x00ad }
            r5 = 795136(0xc2200, float:1.114223E-39)
            int r6 = r2.uid     // Catch:{ NameNotFoundException -> 0x00ad }
            int r6 = android.os.UserHandle.getUserId(r6)     // Catch:{ NameNotFoundException -> 0x00ad }
            android.content.pm.ApplicationInfo r3 = r3.getApplicationInfoAsUser(r4, r5, r6)     // Catch:{ NameNotFoundException -> 0x00ad }
            if (r3 == 0) goto L_0x006b
            android.content.pm.PackageManager r4 = r7.mPm     // Catch:{ NameNotFoundException -> 0x00ad }
            java.lang.CharSequence r4 = r4.getApplicationLabel(r3)     // Catch:{ NameNotFoundException -> 0x00ad }
            java.lang.String r4 = java.lang.String.valueOf(r4)     // Catch:{ NameNotFoundException -> 0x00ad }
            r2.label = r4     // Catch:{ NameNotFoundException -> 0x00ad }
            android.content.pm.PackageManager r4 = r7.mPm     // Catch:{ NameNotFoundException -> 0x00ad }
            android.graphics.drawable.Drawable r3 = r4.getApplicationIcon(r3)     // Catch:{ NameNotFoundException -> 0x00ad }
            int r5 = r2.uid     // Catch:{ NameNotFoundException -> 0x00ad }
            int r5 = android.os.UserHandle.getUserId(r5)     // Catch:{ NameNotFoundException -> 0x00ad }
            android.os.UserHandle r5 = android.os.UserHandle.of(r5)     // Catch:{ NameNotFoundException -> 0x00ad }
            android.graphics.drawable.Drawable r3 = r4.getUserBadgedIcon(r3, r5)     // Catch:{ NameNotFoundException -> 0x00ad }
            r2.icon = r3     // Catch:{ NameNotFoundException -> 0x00ad }
            goto L_0x006b
        L_0x00ad:
            android.content.pm.PackageManager r3 = r7.mPm     // Catch:{ Exception -> 0x00bf }
            android.graphics.drawable.Drawable r3 = r3.getDefaultActivityIcon()     // Catch:{ Exception -> 0x00bf }
            r2.icon = r3     // Catch:{ Exception -> 0x00bf }
            goto L_0x006b
        L_0x00b6:
            com.android.settings.notification.history.HistoryLoader$$ExternalSyntheticLambda0 r7 = new com.android.settings.notification.history.HistoryLoader$$ExternalSyntheticLambda0     // Catch:{ Exception -> 0x00bf }
            r7.<init>(r8, r1)     // Catch:{ Exception -> 0x00bf }
            com.android.settingslib.utils.ThreadUtils.postOnMainThread(r7)     // Catch:{ Exception -> 0x00bf }
            goto L_0x00c7
        L_0x00bf:
            r7 = move-exception
            java.lang.String r8 = "HistoryLoader"
            java.lang.String r0 = "Error loading history"
            android.util.Slog.e(r8, r0, r7)
        L_0x00c7:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.notification.history.HistoryLoader.lambda$load$2(com.android.settings.notification.history.HistoryLoader$OnHistoryLoaderListener):void");
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ int lambda$load$0(NotificationHistoryPackage notificationHistoryPackage, NotificationHistoryPackage notificationHistoryPackage2) {
        return Long.compare(notificationHistoryPackage.getMostRecent(), notificationHistoryPackage2.getMostRecent()) * -1;
    }
}
