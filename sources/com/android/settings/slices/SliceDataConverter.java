package com.android.settings.slices;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.net.Uri;
import android.provider.SearchIndexableResource;
import android.util.Log;
import android.view.accessibility.AccessibilityManager;
import com.android.settings.R;
import com.android.settings.accessibility.AccessibilitySettings;
import com.android.settings.accessibility.AccessibilitySlicePreferenceController;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.slices.SliceData;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.search.Indexable$SearchIndexProvider;
import com.android.settingslib.search.SearchIndexableData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

class SliceDataConverter {
    private Context mContext;
    private final MetricsFeatureProvider mMetricsFeatureProvider;

    public SliceDataConverter(Context context) {
        this.mContext = context;
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(context).getMetricsFeatureProvider();
    }

    public List<SliceData> getSliceData() {
        ArrayList arrayList = new ArrayList();
        for (SearchIndexableData next : FeatureFactory.getFactory(this.mContext).getSearchFeatureProvider().getSearchIndexableResources().getProviderValues()) {
            String name = next.getTargetClass().getName();
            Indexable$SearchIndexProvider searchIndexProvider = next.getSearchIndexProvider();
            if (searchIndexProvider == null) {
                Log.e("SliceDataConverter", name + " dose not implement Search Index Provider");
            } else {
                arrayList.addAll(getSliceDataFromProvider(searchIndexProvider, name));
            }
        }
        arrayList.addAll(getAccessibilitySliceData());
        return arrayList;
    }

    private List<SliceData> getSliceDataFromProvider(Indexable$SearchIndexProvider indexable$SearchIndexProvider, String str) {
        ArrayList arrayList = new ArrayList();
        List<SearchIndexableResource> xmlResourcesToIndex = indexable$SearchIndexProvider.getXmlResourcesToIndex(this.mContext, true);
        if (xmlResourcesToIndex == null) {
            return arrayList;
        }
        for (SearchIndexableResource searchIndexableResource : xmlResourcesToIndex) {
            int i = searchIndexableResource.xmlResId;
            if (i == 0) {
                Log.e("SliceDataConverter", str + " provides invalid XML (0) in search provider.");
            } else {
                arrayList.addAll(getSliceDataFromXML(i, str));
            }
        }
        return arrayList;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0126, code lost:
        if (0 == 0) goto L_0x016a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x0165, code lost:
        if (0 == 0) goto L_0x016a;
     */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x016d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.util.List<com.android.settings.slices.SliceData> getSliceDataFromXML(int r17, java.lang.String r18) {
        /*
            r16 = this;
            r1 = r16
            r0 = r17
            r4 = r18
            java.lang.String r2 = "SliceDataConverter"
            java.util.ArrayList r6 = new java.util.ArrayList
            r6.<init>()
            java.lang.String r3 = ""
            r5 = 0
            android.content.Context r7 = r1.mContext     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            android.content.res.Resources r7 = r7.getResources()     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            android.content.res.XmlResourceParser r5 = r7.getXml(r0)     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
        L_0x001a:
            int r7 = r5.next()     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            r8 = 1
            if (r7 == r8) goto L_0x0025
            r8 = 2
            if (r7 == r8) goto L_0x0025
            goto L_0x001a
        L_0x0025:
            java.lang.String r7 = r5.getName()     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            java.lang.String r8 = "PreferenceScreen"
            boolean r8 = r8.equals(r7)     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            if (r8 == 0) goto L_0x00dd
            android.util.AttributeSet r7 = android.util.Xml.asAttributeSet(r5)     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            android.content.Context r8 = r1.mContext     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            java.lang.String r7 = com.android.settings.core.PreferenceXmlParserUtils.getDataTitle(r8, r7)     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            android.content.Context r8 = r1.mContext     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            r9 = 2174(0x87e, float:3.046E-42)
            java.util.List r0 = com.android.settings.core.PreferenceXmlParserUtils.extractMetadata(r8, r0, r9)     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            java.util.Iterator r0 = r0.iterator()     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
        L_0x0047:
            boolean r8 = r0.hasNext()     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            if (r8 == 0) goto L_0x0167
            java.lang.Object r8 = r0.next()     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            android.os.Bundle r8 = (android.os.Bundle) r8     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            java.lang.String r9 = "controller"
            java.lang.String r3 = r8.getString(r9)     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            boolean r9 = android.text.TextUtils.isEmpty(r3)     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            if (r9 == 0) goto L_0x0062
        L_0x005f:
            r17 = r0
            goto L_0x00d9
        L_0x0062:
            java.lang.String r9 = "key"
            java.lang.String r9 = r8.getString(r9)     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            android.content.Context r10 = r1.mContext     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            com.android.settings.core.BasePreferenceController r10 = com.android.settings.slices.SliceBuilderUtils.getPreferenceController(r10, r3, r9)     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            boolean r11 = r10.isSliceable()     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            if (r11 == 0) goto L_0x005f
            boolean r11 = r10.isAvailable()     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            if (r11 != 0) goto L_0x007b
            goto L_0x005f
        L_0x007b:
            java.lang.String r11 = "title"
            java.lang.String r11 = r8.getString(r11)     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            java.lang.String r12 = "summary"
            java.lang.String r12 = r8.getString(r12)     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            java.lang.String r13 = "icon"
            int r13 = r8.getInt(r13)     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            int r14 = r10.getSliceType()     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            java.lang.String r15 = "unavailable_slice_subtitle"
            java.lang.String r8 = r8.getString(r15)     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            boolean r15 = r10.isPublicSlice()     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            r17 = r0
            com.android.settings.slices.SliceData$Builder r0 = new com.android.settings.slices.SliceData$Builder     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            r0.<init>()     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            com.android.settings.slices.SliceData$Builder r0 = r0.setKey(r9)     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            android.net.Uri r9 = r10.getSliceUri()     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            com.android.settings.slices.SliceData$Builder r0 = r0.setUri(r9)     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            com.android.settings.slices.SliceData$Builder r0 = r0.setTitle(r11)     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            com.android.settings.slices.SliceData$Builder r0 = r0.setSummary(r12)     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            com.android.settings.slices.SliceData$Builder r0 = r0.setIcon(r13)     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            com.android.settings.slices.SliceData$Builder r0 = r0.setScreenTitle(r7)     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            com.android.settings.slices.SliceData$Builder r0 = r0.setPreferenceControllerClassName(r3)     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            com.android.settings.slices.SliceData$Builder r0 = r0.setFragmentName(r4)     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            com.android.settings.slices.SliceData$Builder r0 = r0.setSliceType(r14)     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            com.android.settings.slices.SliceData$Builder r0 = r0.setUnavailableSliceSubtitle(r8)     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            com.android.settings.slices.SliceData$Builder r0 = r0.setIsPublicSlice(r15)     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            com.android.settings.slices.SliceData r0 = r0.build()     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            r6.add(r0)     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
        L_0x00d9:
            r0 = r17
            goto L_0x0047
        L_0x00dd:
            java.lang.RuntimeException r0 = new java.lang.RuntimeException     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            r8.<init>()     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            java.lang.String r9 = "XML document must start with <PreferenceScreen> tag; found"
            r8.append(r9)     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            r8.append(r7)     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            java.lang.String r7 = " at "
            r8.append(r7)     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            java.lang.String r7 = r5.getPositionDescription()     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            r8.append(r7)     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            java.lang.String r7 = r8.toString()     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            r0.<init>(r7)     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
            throw r0     // Catch:{ InvalidSliceDataException -> 0x0145, NotFoundException | IOException | XmlPullParserException -> 0x0129, Exception -> 0x0102 }
        L_0x0100:
            r0 = move-exception
            goto L_0x016b
        L_0x0102:
            r0 = move-exception
            java.lang.String r7 = "Get slice data from XML failed "
            android.util.Log.w(r2, r7, r0)     // Catch:{ all -> 0x0100 }
            com.android.settingslib.core.instrumentation.MetricsFeatureProvider r8 = r1.mMetricsFeatureProvider     // Catch:{ all -> 0x0100 }
            r9 = 0
            r10 = 1727(0x6bf, float:2.42E-42)
            r11 = 0
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x0100 }
            r0.<init>()     // Catch:{ all -> 0x0100 }
            r0.append(r4)     // Catch:{ all -> 0x0100 }
            java.lang.String r1 = "_"
            r0.append(r1)     // Catch:{ all -> 0x0100 }
            r0.append(r3)     // Catch:{ all -> 0x0100 }
            java.lang.String r12 = r0.toString()     // Catch:{ all -> 0x0100 }
            r13 = 1
            r8.action(r9, r10, r11, r12, r13)     // Catch:{ all -> 0x0100 }
            if (r5 == 0) goto L_0x016a
            goto L_0x0167
        L_0x0129:
            r0 = move-exception
            r7 = r5
            java.lang.String r3 = "Error parsing PreferenceScreen: "
            android.util.Log.w(r2, r3, r0)     // Catch:{ all -> 0x0142 }
            com.android.settingslib.core.instrumentation.MetricsFeatureProvider r0 = r1.mMetricsFeatureProvider     // Catch:{ all -> 0x0142 }
            r1 = 0
            r2 = 1726(0x6be, float:2.419E-42)
            r3 = 0
            r5 = 1
            r4 = r18
            r0.action(r1, r2, r3, r4, r5)     // Catch:{ all -> 0x0142 }
            if (r7 == 0) goto L_0x016a
            r7.close()
            goto L_0x016a
        L_0x0142:
            r0 = move-exception
            r5 = r7
            goto L_0x016b
        L_0x0145:
            r0 = move-exception
            r12 = r3
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x0100 }
            r3.<init>()     // Catch:{ all -> 0x0100 }
            java.lang.String r7 = "Invalid data when building SliceData for "
            r3.append(r7)     // Catch:{ all -> 0x0100 }
            r3.append(r4)     // Catch:{ all -> 0x0100 }
            java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x0100 }
            android.util.Log.w(r2, r3, r0)     // Catch:{ all -> 0x0100 }
            com.android.settingslib.core.instrumentation.MetricsFeatureProvider r8 = r1.mMetricsFeatureProvider     // Catch:{ all -> 0x0100 }
            r9 = 0
            r10 = 1725(0x6bd, float:2.417E-42)
            r11 = 0
            r13 = 1
            r8.action(r9, r10, r11, r12, r13)     // Catch:{ all -> 0x0100 }
            if (r5 == 0) goto L_0x016a
        L_0x0167:
            r5.close()
        L_0x016a:
            return r6
        L_0x016b:
            if (r5 == 0) goto L_0x0170
            r5.close()
        L_0x0170:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.slices.SliceDataConverter.getSliceDataFromXML(int, java.lang.String):java.util.List");
    }

    private List<SliceData> getAccessibilitySliceData() {
        ArrayList arrayList = new ArrayList();
        String name = AccessibilitySlicePreferenceController.class.getName();
        String name2 = AccessibilitySettings.class.getName();
        SliceData.Builder preferenceControllerClassName = new SliceData.Builder().setFragmentName(name2).setScreenTitle(this.mContext.getText(R.string.accessibility_settings)).setPreferenceControllerClassName(name);
        HashSet hashSet = new HashSet();
        Collections.addAll(hashSet, this.mContext.getResources().getStringArray(R.array.config_settings_slices_accessibility_components));
        List<AccessibilityServiceInfo> accessibilityServiceInfoList = getAccessibilityServiceInfoList();
        PackageManager packageManager = this.mContext.getPackageManager();
        for (AccessibilityServiceInfo resolveInfo : accessibilityServiceInfoList) {
            ResolveInfo resolveInfo2 = resolveInfo.getResolveInfo();
            ServiceInfo serviceInfo = resolveInfo2.serviceInfo;
            String flattenToString = new ComponentName(serviceInfo.packageName, serviceInfo.name).flattenToString();
            if (hashSet.contains(flattenToString)) {
                String charSequence = resolveInfo2.loadLabel(packageManager).toString();
                int iconResource = resolveInfo2.getIconResource();
                if (iconResource == 0) {
                    iconResource = R.drawable.ic_accessibility_generic;
                }
                preferenceControllerClassName.setKey(flattenToString).setTitle(charSequence).setUri(new Uri.Builder().scheme("content").authority("com.android.settings.slices").appendPath("action").appendPath(flattenToString).build()).setIcon(iconResource).setSliceType(1);
                try {
                    arrayList.add(preferenceControllerClassName.build());
                } catch (SliceData.InvalidSliceDataException e) {
                    Log.w("SliceDataConverter", "Invalid data when building a11y SliceData for " + flattenToString, e);
                }
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: package-private */
    public List<AccessibilityServiceInfo> getAccessibilityServiceInfoList() {
        return AccessibilityManager.getInstance(this.mContext).getInstalledAccessibilityServiceList();
    }
}
