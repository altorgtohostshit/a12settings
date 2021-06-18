package com.android.settings.security.trustagent;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.widget.LockPatternUtils;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import java.util.ArrayList;
import java.util.List;

public class TrustAgentManager {
    static final String PERMISSION_PROVIDE_AGENT = "android.permission.PROVIDE_TRUST_AGENT";
    private static final Intent TRUST_AGENT_INTENT = new Intent("android.service.trust.TrustAgentService");

    public static class TrustAgentComponentInfo {
        public RestrictedLockUtils.EnforcedAdmin admin = null;
        public ComponentName componentName;
        public String summary;
        public String title;
    }

    public boolean shouldProvideTrust(ResolveInfo resolveInfo, PackageManager packageManager) {
        String str = resolveInfo.serviceInfo.packageName;
        if (packageManager.checkPermission(PERMISSION_PROVIDE_AGENT, str) == 0) {
            return true;
        }
        Log.w("TrustAgentManager", "Skipping agent because package " + str + " does not have permission " + PERMISSION_PROVIDE_AGENT + ".");
        return false;
    }

    public CharSequence getActiveTrustAgentLabel(Context context, LockPatternUtils lockPatternUtils) {
        List<TrustAgentComponentInfo> activeTrustAgents = getActiveTrustAgents(context, lockPatternUtils);
        if (activeTrustAgents.isEmpty()) {
            return null;
        }
        return activeTrustAgents.get(0).title;
    }

    public List<TrustAgentComponentInfo> getActiveTrustAgents(Context context, LockPatternUtils lockPatternUtils) {
        int myUserId = UserHandle.myUserId();
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(DevicePolicyManager.class);
        PackageManager packageManager = context.getPackageManager();
        ArrayList arrayList = new ArrayList();
        List<ResolveInfo> queryIntentServices = packageManager.queryIntentServices(TRUST_AGENT_INTENT, 128);
        List enabledTrustAgents = lockPatternUtils.getEnabledTrustAgents(myUserId);
        RestrictedLockUtils.EnforcedAdmin checkIfKeyguardFeaturesDisabled = RestrictedLockUtilsInternal.checkIfKeyguardFeaturesDisabled(context, 16, myUserId);
        if (enabledTrustAgents != null && !enabledTrustAgents.isEmpty()) {
            for (ResolveInfo next : queryIntentServices) {
                if (next.serviceInfo != null && shouldProvideTrust(next, packageManager)) {
                    TrustAgentComponentInfo settingsComponent = getSettingsComponent(packageManager, next);
                    if (settingsComponent.componentName != null && enabledTrustAgents.contains(getComponentName(next)) && !TextUtils.isEmpty(settingsComponent.title)) {
                        if (checkIfKeyguardFeaturesDisabled != null && devicePolicyManager.getTrustAgentConfiguration((ComponentName) null, getComponentName(next)) == null) {
                            settingsComponent.admin = checkIfKeyguardFeaturesDisabled;
                        }
                        arrayList.add(settingsComponent);
                    }
                }
            }
        }
        return arrayList;
    }

    public ComponentName getComponentName(ResolveInfo resolveInfo) {
        if (resolveInfo == null || resolveInfo.serviceInfo == null) {
            return null;
        }
        ServiceInfo serviceInfo = resolveInfo.serviceInfo;
        return new ComponentName(serviceInfo.packageName, serviceInfo.name);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v1, resolved type: android.content.ComponentName} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v3, resolved type: android.content.res.XmlResourceParser} */
    /* JADX WARNING: type inference failed for: r0v0 */
    /* JADX WARNING: type inference failed for: r0v4 */
    /* JADX WARNING: type inference failed for: r0v5 */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0074, code lost:
        r9 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0076, code lost:
        r9 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0078, code lost:
        r9 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x007a, code lost:
        r8 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x007b, code lost:
        r0 = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x0089, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x0090, code lost:
        if (r2 == null) goto L_0x00a2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x0092, code lost:
        r2.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x0099, code lost:
        if (r2 == null) goto L_0x00a2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x009f, code lost:
        if (r2 == null) goto L_0x00a2;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [B:11:0x0020, B:29:0x006c] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x007a A[ExcHandler: all (th java.lang.Throwable), Splitter:B:11:0x0020] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x0089  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.android.settings.security.trustagent.TrustAgentManager.TrustAgentComponentInfo getSettingsComponent(android.content.pm.PackageManager r9, android.content.pm.ResolveInfo r10) {
        /*
            r8 = this;
            java.lang.String r8 = "TrustAgentManager"
            r0 = 0
            if (r10 == 0) goto L_0x00e9
            android.content.pm.ServiceInfo r1 = r10.serviceInfo
            if (r1 == 0) goto L_0x00e9
            android.os.Bundle r1 = r1.metaData
            if (r1 != 0) goto L_0x000f
            goto L_0x00e9
        L_0x000f:
            com.android.settings.security.trustagent.TrustAgentManager$TrustAgentComponentInfo r1 = new com.android.settings.security.trustagent.TrustAgentManager$TrustAgentComponentInfo
            r1.<init>()
            android.content.pm.ServiceInfo r2 = r10.serviceInfo     // Catch:{ NameNotFoundException -> 0x009c, IOException -> 0x0096, XmlPullParserException -> 0x008d, all -> 0x0086 }
            java.lang.String r3 = "android.service.trust.trustagent"
            android.content.res.XmlResourceParser r2 = r2.loadXmlMetaData(r9, r3)     // Catch:{ NameNotFoundException -> 0x009c, IOException -> 0x0096, XmlPullParserException -> 0x008d, all -> 0x0086 }
            if (r2 != 0) goto L_0x0029
            java.lang.String r9 = "Can't find android.service.trust.trustagent meta-data"
            android.util.Slog.w(r8, r9)     // Catch:{ NameNotFoundException -> 0x0083, IOException -> 0x0080, XmlPullParserException -> 0x007d, all -> 0x007a }
            if (r2 == 0) goto L_0x0028
            r2.close()
        L_0x0028:
            return r0
        L_0x0029:
            android.content.pm.ServiceInfo r3 = r10.serviceInfo     // Catch:{ NameNotFoundException -> 0x0083, IOException -> 0x0080, XmlPullParserException -> 0x007d, all -> 0x007a }
            android.content.pm.ApplicationInfo r3 = r3.applicationInfo     // Catch:{ NameNotFoundException -> 0x0083, IOException -> 0x0080, XmlPullParserException -> 0x007d, all -> 0x007a }
            android.content.res.Resources r9 = r9.getResourcesForApplication(r3)     // Catch:{ NameNotFoundException -> 0x0083, IOException -> 0x0080, XmlPullParserException -> 0x007d, all -> 0x007a }
            android.util.AttributeSet r3 = android.util.Xml.asAttributeSet(r2)     // Catch:{ NameNotFoundException -> 0x0083, IOException -> 0x0080, XmlPullParserException -> 0x007d, all -> 0x007a }
        L_0x0035:
            int r4 = r2.next()     // Catch:{ NameNotFoundException -> 0x0083, IOException -> 0x0080, XmlPullParserException -> 0x007d, all -> 0x007a }
            r5 = 2
            r6 = 1
            if (r4 == r6) goto L_0x0040
            if (r4 == r5) goto L_0x0040
            goto L_0x0035
        L_0x0040:
            java.lang.String r4 = r2.getName()     // Catch:{ NameNotFoundException -> 0x0083, IOException -> 0x0080, XmlPullParserException -> 0x007d, all -> 0x007a }
            java.lang.String r7 = "trust-agent"
            boolean r4 = r7.equals(r4)     // Catch:{ NameNotFoundException -> 0x0083, IOException -> 0x0080, XmlPullParserException -> 0x007d, all -> 0x007a }
            if (r4 != 0) goto L_0x0055
            java.lang.String r9 = "Meta-data does not start with trust-agent tag"
            android.util.Slog.w(r8, r9)     // Catch:{ NameNotFoundException -> 0x0083, IOException -> 0x0080, XmlPullParserException -> 0x007d, all -> 0x007a }
            r2.close()
            return r0
        L_0x0055:
            int[] r4 = com.android.internal.R.styleable.TrustAgent     // Catch:{ NameNotFoundException -> 0x0083, IOException -> 0x0080, XmlPullParserException -> 0x007d, all -> 0x007a }
            android.content.res.TypedArray r9 = r9.obtainAttributes(r3, r4)     // Catch:{ NameNotFoundException -> 0x0083, IOException -> 0x0080, XmlPullParserException -> 0x007d, all -> 0x007a }
            java.lang.String r3 = r9.getString(r6)     // Catch:{ NameNotFoundException -> 0x0083, IOException -> 0x0080, XmlPullParserException -> 0x007d, all -> 0x007a }
            r1.summary = r3     // Catch:{ NameNotFoundException -> 0x0083, IOException -> 0x0080, XmlPullParserException -> 0x007d, all -> 0x007a }
            r3 = 0
            java.lang.String r3 = r9.getString(r3)     // Catch:{ NameNotFoundException -> 0x0083, IOException -> 0x0080, XmlPullParserException -> 0x007d, all -> 0x007a }
            r1.title = r3     // Catch:{ NameNotFoundException -> 0x0083, IOException -> 0x0080, XmlPullParserException -> 0x007d, all -> 0x007a }
            java.lang.String r3 = r9.getString(r5)     // Catch:{ NameNotFoundException -> 0x0083, IOException -> 0x0080, XmlPullParserException -> 0x007d, all -> 0x007a }
            r9.recycle()     // Catch:{ NameNotFoundException -> 0x0078, IOException -> 0x0076, XmlPullParserException -> 0x0074, all -> 0x007a }
            r2.close()
            r9 = r0
            goto L_0x00a2
        L_0x0074:
            r9 = move-exception
            goto L_0x0090
        L_0x0076:
            r9 = move-exception
            goto L_0x0099
        L_0x0078:
            r9 = move-exception
            goto L_0x009f
        L_0x007a:
            r8 = move-exception
            r0 = r2
            goto L_0x0087
        L_0x007d:
            r9 = move-exception
            r3 = r0
            goto L_0x0090
        L_0x0080:
            r9 = move-exception
            r3 = r0
            goto L_0x0099
        L_0x0083:
            r9 = move-exception
            r3 = r0
            goto L_0x009f
        L_0x0086:
            r8 = move-exception
        L_0x0087:
            if (r0 == 0) goto L_0x008c
            r0.close()
        L_0x008c:
            throw r8
        L_0x008d:
            r9 = move-exception
            r2 = r0
            r3 = r2
        L_0x0090:
            if (r2 == 0) goto L_0x00a2
        L_0x0092:
            r2.close()
            goto L_0x00a2
        L_0x0096:
            r9 = move-exception
            r2 = r0
            r3 = r2
        L_0x0099:
            if (r2 == 0) goto L_0x00a2
            goto L_0x0092
        L_0x009c:
            r9 = move-exception
            r2 = r0
            r3 = r2
        L_0x009f:
            if (r2 == 0) goto L_0x00a2
            goto L_0x0092
        L_0x00a2:
            if (r9 == 0) goto L_0x00bd
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Error parsing : "
            r1.append(r2)
            android.content.pm.ServiceInfo r10 = r10.serviceInfo
            java.lang.String r10 = r10.packageName
            r1.append(r10)
            java.lang.String r10 = r1.toString()
            android.util.Slog.w(r8, r10, r9)
            return r0
        L_0x00bd:
            if (r3 == 0) goto L_0x00df
            r8 = 47
            int r8 = r3.indexOf(r8)
            if (r8 >= 0) goto L_0x00df
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            android.content.pm.ServiceInfo r9 = r10.serviceInfo
            java.lang.String r9 = r9.packageName
            r8.append(r9)
            java.lang.String r9 = "/"
            r8.append(r9)
            r8.append(r3)
            java.lang.String r3 = r8.toString()
        L_0x00df:
            if (r3 != 0) goto L_0x00e2
            goto L_0x00e6
        L_0x00e2:
            android.content.ComponentName r0 = android.content.ComponentName.unflattenFromString(r3)
        L_0x00e6:
            r1.componentName = r0
            return r1
        L_0x00e9:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.security.trustagent.TrustAgentManager.getSettingsComponent(android.content.pm.PackageManager, android.content.pm.ResolveInfo):com.android.settings.security.trustagent.TrustAgentManager$TrustAgentComponentInfo");
    }
}
