package com.android.settings.applications.assist;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import java.util.ArrayList;
import java.util.List;

public final class VoiceInputHelper {
    final List<ResolveInfo> mAvailableRecognition;
    final ArrayList<RecognizerInfo> mAvailableRecognizerInfos = new ArrayList<>();
    final Context mContext;
    ComponentName mCurrentRecognizer;

    public static class BaseInfo implements Comparable {
        public final CharSequence appLabel;
        public final ComponentName componentName;
        public final String key;
        public final CharSequence label;
        public final String labelStr;
        public final ServiceInfo service;
        public final ComponentName settings;

        public BaseInfo(PackageManager packageManager, ServiceInfo serviceInfo, String str) {
            this.service = serviceInfo;
            ComponentName componentName2 = new ComponentName(serviceInfo.packageName, serviceInfo.name);
            this.componentName = componentName2;
            this.key = componentName2.flattenToShortString();
            this.settings = str != null ? new ComponentName(serviceInfo.packageName, str) : null;
            CharSequence loadLabel = serviceInfo.loadLabel(packageManager);
            this.label = loadLabel;
            this.labelStr = loadLabel.toString();
            this.appLabel = serviceInfo.applicationInfo.loadLabel(packageManager);
        }

        public int compareTo(Object obj) {
            return this.labelStr.compareTo(((BaseInfo) obj).labelStr);
        }
    }

    public static class RecognizerInfo extends BaseInfo {
        public final boolean mSelectableAsDefault;

        public RecognizerInfo(PackageManager packageManager, ServiceInfo serviceInfo, String str, boolean z) {
            super(packageManager, serviceInfo, str);
            this.mSelectableAsDefault = z;
        }
    }

    public VoiceInputHelper(Context context) {
        this.mContext = context;
        this.mAvailableRecognition = context.getPackageManager().queryIntentServices(new Intent("android.speech.RecognitionService"), 128);
    }

    /* JADX WARNING: Removed duplicated region for block: B:35:0x00b3 A[SYNTHETIC, Splitter:B:35:0x00b3] */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x00dd  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x00bb A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x00ef A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void buildUi() {
        /*
            r14 = this;
            java.lang.String r0 = "error parsing recognition service meta-data"
            java.lang.String r1 = "VoiceInputHelper"
            android.content.Context r2 = r14.mContext
            android.content.ContentResolver r2 = r2.getContentResolver()
            java.lang.String r3 = "voice_recognition_service"
            java.lang.String r2 = android.provider.Settings.Secure.getString(r2, r3)
            r3 = 0
            if (r2 == 0) goto L_0x0020
            boolean r4 = r2.isEmpty()
            if (r4 != 0) goto L_0x0020
            android.content.ComponentName r2 = android.content.ComponentName.unflattenFromString(r2)
            r14.mCurrentRecognizer = r2
            goto L_0x0022
        L_0x0020:
            r14.mCurrentRecognizer = r3
        L_0x0022:
            java.util.List<android.content.pm.ResolveInfo> r2 = r14.mAvailableRecognition
            int r2 = r2.size()
            r4 = 0
            r5 = r4
        L_0x002a:
            if (r5 >= r2) goto L_0x00f3
            java.util.List<android.content.pm.ResolveInfo> r6 = r14.mAvailableRecognition
            java.lang.Object r6 = r6.get(r5)
            android.content.pm.ResolveInfo r6 = (android.content.pm.ResolveInfo) r6
            android.content.ComponentName r7 = new android.content.ComponentName
            android.content.pm.ServiceInfo r8 = r6.serviceInfo
            java.lang.String r9 = r8.packageName
            java.lang.String r8 = r8.name
            r7.<init>(r9, r8)
            android.content.pm.ServiceInfo r8 = r6.serviceInfo
            r9 = 1
            android.content.Context r10 = r14.mContext     // Catch:{ XmlPullParserException -> 0x00ce, IOException -> 0x00c8, NameNotFoundException -> 0x00c2 }
            android.content.pm.PackageManager r10 = r10.getPackageManager()     // Catch:{ XmlPullParserException -> 0x00ce, IOException -> 0x00c8, NameNotFoundException -> 0x00c2 }
            java.lang.String r11 = "android.speech"
            android.content.res.XmlResourceParser r10 = r8.loadXmlMetaData(r10, r11)     // Catch:{ XmlPullParserException -> 0x00ce, IOException -> 0x00c8, NameNotFoundException -> 0x00c2 }
            if (r10 == 0) goto L_0x0098
            android.content.Context r11 = r14.mContext     // Catch:{ all -> 0x0095 }
            android.content.pm.PackageManager r11 = r11.getPackageManager()     // Catch:{ all -> 0x0095 }
            android.content.pm.ApplicationInfo r8 = r8.applicationInfo     // Catch:{ all -> 0x0095 }
            android.content.res.Resources r8 = r11.getResourcesForApplication(r8)     // Catch:{ all -> 0x0095 }
            android.util.AttributeSet r11 = android.util.Xml.asAttributeSet(r10)     // Catch:{ all -> 0x0095 }
        L_0x0060:
            int r12 = r10.next()     // Catch:{ all -> 0x0095 }
            if (r12 == r9) goto L_0x006a
            r13 = 2
            if (r12 == r13) goto L_0x006a
            goto L_0x0060
        L_0x006a:
            java.lang.String r12 = r10.getName()     // Catch:{ all -> 0x0095 }
            java.lang.String r13 = "recognition-service"
            boolean r12 = r13.equals(r12)     // Catch:{ all -> 0x0095 }
            if (r12 == 0) goto L_0x008d
            int[] r12 = com.android.internal.R.styleable.RecognitionService     // Catch:{ all -> 0x0095 }
            android.content.res.TypedArray r8 = r8.obtainAttributes(r11, r12)     // Catch:{ all -> 0x0095 }
            java.lang.String r11 = r8.getString(r4)     // Catch:{ all -> 0x0095 }
            boolean r9 = r8.getBoolean(r9, r9)     // Catch:{ all -> 0x008b }
            r8.recycle()     // Catch:{ all -> 0x008b }
            r10.close()     // Catch:{ XmlPullParserException -> 0x00c0, IOException -> 0x00be, NameNotFoundException -> 0x00bc }
            goto L_0x00d3
        L_0x008b:
            r8 = move-exception
            goto L_0x00b1
        L_0x008d:
            org.xmlpull.v1.XmlPullParserException r8 = new org.xmlpull.v1.XmlPullParserException     // Catch:{ all -> 0x0095 }
            java.lang.String r11 = "Meta-data does not start with recognition-service tag"
            r8.<init>(r11)     // Catch:{ all -> 0x0095 }
            throw r8     // Catch:{ all -> 0x0095 }
        L_0x0095:
            r8 = move-exception
            r11 = r3
            goto L_0x00b1
        L_0x0098:
            org.xmlpull.v1.XmlPullParserException r11 = new org.xmlpull.v1.XmlPullParserException     // Catch:{ all -> 0x0095 }
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ all -> 0x0095 }
            r12.<init>()     // Catch:{ all -> 0x0095 }
            java.lang.String r13 = "No android.speech meta-data for "
            r12.append(r13)     // Catch:{ all -> 0x0095 }
            java.lang.String r8 = r8.packageName     // Catch:{ all -> 0x0095 }
            r12.append(r8)     // Catch:{ all -> 0x0095 }
            java.lang.String r8 = r12.toString()     // Catch:{ all -> 0x0095 }
            r11.<init>(r8)     // Catch:{ all -> 0x0095 }
            throw r11     // Catch:{ all -> 0x0095 }
        L_0x00b1:
            if (r10 == 0) goto L_0x00bb
            r10.close()     // Catch:{ all -> 0x00b7 }
            goto L_0x00bb
        L_0x00b7:
            r10 = move-exception
            r8.addSuppressed(r10)     // Catch:{ XmlPullParserException -> 0x00c0, IOException -> 0x00be, NameNotFoundException -> 0x00bc }
        L_0x00bb:
            throw r8     // Catch:{ XmlPullParserException -> 0x00c0, IOException -> 0x00be, NameNotFoundException -> 0x00bc }
        L_0x00bc:
            r8 = move-exception
            goto L_0x00c4
        L_0x00be:
            r8 = move-exception
            goto L_0x00ca
        L_0x00c0:
            r8 = move-exception
            goto L_0x00d0
        L_0x00c2:
            r8 = move-exception
            r11 = r3
        L_0x00c4:
            android.util.Log.e(r1, r0, r8)
            goto L_0x00d3
        L_0x00c8:
            r8 = move-exception
            r11 = r3
        L_0x00ca:
            android.util.Log.e(r1, r0, r8)
            goto L_0x00d3
        L_0x00ce:
            r8 = move-exception
            r11 = r3
        L_0x00d0:
            android.util.Log.e(r1, r0, r8)
        L_0x00d3:
            if (r9 != 0) goto L_0x00dd
            android.content.ComponentName r8 = r14.mCurrentRecognizer
            boolean r7 = r7.equals(r8)
            if (r7 == 0) goto L_0x00ef
        L_0x00dd:
            java.util.ArrayList<com.android.settings.applications.assist.VoiceInputHelper$RecognizerInfo> r7 = r14.mAvailableRecognizerInfos
            com.android.settings.applications.assist.VoiceInputHelper$RecognizerInfo r8 = new com.android.settings.applications.assist.VoiceInputHelper$RecognizerInfo
            android.content.Context r10 = r14.mContext
            android.content.pm.PackageManager r10 = r10.getPackageManager()
            android.content.pm.ServiceInfo r6 = r6.serviceInfo
            r8.<init>(r10, r6, r11, r9)
            r7.add(r8)
        L_0x00ef:
            int r5 = r5 + 1
            goto L_0x002a
        L_0x00f3:
            java.util.ArrayList<com.android.settings.applications.assist.VoiceInputHelper$RecognizerInfo> r14 = r14.mAvailableRecognizerInfos
            java.util.Collections.sort(r14)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.applications.assist.VoiceInputHelper.buildUi():void");
    }
}
