package com.android.settings.applications.specialaccess.notificationaccess;

import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ServiceInfo;
import android.service.notification.NotificationListenerFilter;
import androidx.preference.Preference;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.notification.NotificationBackend;
import com.android.settings.slices.SliceBackgroundWorker;

public abstract class TypeFilterPreferenceController extends BasePreferenceController implements PreferenceControllerMixin, Preference.OnPreferenceChangeListener {
    private static final String TAG = "TypeFilterPrefCntlr";
    private static final String XML_SEPARATOR = ",";
    private ComponentName mCn;
    private NotificationListenerFilter mNlf;
    private NotificationBackend mNm;
    private ServiceInfo mSi;
    private int mTargetSdk;
    private int mUserId;

    private boolean hasFlag(int i, int i2) {
        return (i & i2) != 0;
    }

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    /* access modifiers changed from: protected */
    public abstract int getType();

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public TypeFilterPreferenceController(Context context, String str) {
        super(context, str);
    }

    public TypeFilterPreferenceController setCn(ComponentName componentName) {
        this.mCn = componentName;
        return this;
    }

    public TypeFilterPreferenceController setUserId(int i) {
        this.mUserId = i;
        return this;
    }

    public TypeFilterPreferenceController setNm(NotificationBackend notificationBackend) {
        this.mNm = notificationBackend;
        return this;
    }

    public TypeFilterPreferenceController setServiceInfo(ServiceInfo serviceInfo) {
        this.mSi = serviceInfo;
        return this;
    }

    public TypeFilterPreferenceController setTargetSdk(int i) {
        this.mTargetSdk = i;
        return this;
    }

    public int getAvailabilityStatus() {
        if (!this.mNm.isNotificationListenerAccessGranted(this.mCn)) {
            return 5;
        }
        if (this.mTargetSdk > 10000) {
            return 0;
        }
        NotificationListenerFilter listenerFilter = this.mNm.getListenerFilter(this.mCn, this.mUserId);
        this.mNlf = listenerFilter;
        if (!listenerFilter.areAllTypesAllowed() || !this.mNlf.getDisallowedPackages().isEmpty()) {
            return 0;
        }
        return 5;
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        int i;
        this.mNlf = this.mNm.getListenerFilter(this.mCn, this.mUserId);
        boolean booleanValue = ((Boolean) obj).booleanValue();
        int types = this.mNlf.getTypes();
        if (booleanValue) {
            i = getType() | types;
        } else {
            i = (~getType()) & types;
        }
        this.mNlf.setTypes(i);
        this.mNm.setListenerFilter(this.mCn, this.mUserId, this.mNlf);
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:37:0x009b  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x009d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateState(androidx.preference.Preference r9) {
        /*
            r8 = this;
            com.android.settings.notification.NotificationBackend r0 = r8.mNm
            android.content.ComponentName r1 = r8.mCn
            int r2 = r8.mUserId
            android.service.notification.NotificationListenerFilter r0 = r0.getListenerFilter(r1, r2)
            r8.mNlf = r0
            r1 = r9
            androidx.preference.CheckBoxPreference r1 = (androidx.preference.CheckBoxPreference) r1
            int r0 = r0.getTypes()
            int r2 = r8.getType()
            boolean r0 = r8.hasFlag(r0, r2)
            r1.setChecked(r0)
            android.content.pm.ServiceInfo r0 = r8.mSi
            r2 = 1
            r3 = 0
            if (r0 == 0) goto L_0x0092
            android.os.Bundle r0 = r0.metaData
            if (r0 == 0) goto L_0x0092
            java.lang.String r4 = "android.service.notification.disabled_filter_types"
            boolean r0 = r0.containsKey(r4)
            if (r0 == 0) goto L_0x0092
            android.content.pm.ServiceInfo r0 = r8.mSi
            android.os.Bundle r0 = r0.metaData
            java.lang.Object r0 = r0.get(r4)
            java.lang.String r0 = r0.toString()
            if (r0 == 0) goto L_0x0092
            java.lang.String r4 = ","
            java.lang.String[] r0 = r0.split(r4)
            r4 = r3
            r5 = r4
        L_0x0046:
            int r6 = r0.length
            if (r4 >= r6) goto L_0x0086
            r6 = r0[r4]
            boolean r7 = android.text.TextUtils.isEmpty(r6)
            if (r7 == 0) goto L_0x0052
            goto L_0x0083
        L_0x0052:
            java.lang.String r7 = "ONGOING"
            boolean r7 = r6.equalsIgnoreCase(r7)
            if (r7 == 0) goto L_0x005d
            r5 = r5 | 8
            goto L_0x0083
        L_0x005d:
            java.lang.String r7 = "CONVERSATIONS"
            boolean r7 = r6.equalsIgnoreCase(r7)
            if (r7 == 0) goto L_0x0068
            r5 = r5 | 1
            goto L_0x0083
        L_0x0068:
            java.lang.String r7 = "SILENT"
            boolean r7 = r6.equalsIgnoreCase(r7)
            if (r7 == 0) goto L_0x0073
            r5 = r5 | 4
            goto L_0x0083
        L_0x0073:
            java.lang.String r7 = "ALERTING"
            boolean r7 = r6.equalsIgnoreCase(r7)
            if (r7 == 0) goto L_0x007e
            r5 = r5 | 2
            goto L_0x0083
        L_0x007e:
            int r6 = java.lang.Integer.parseInt(r6)     // Catch:{ NumberFormatException -> 0x0083 }
            r5 = r5 | r6
        L_0x0083:
            int r4 = r4 + 1
            goto L_0x0046
        L_0x0086:
            int r0 = r8.getType()
            boolean r0 = r8.hasFlag(r5, r0)
            if (r0 == 0) goto L_0x0092
            r0 = r2
            goto L_0x0093
        L_0x0092:
            r0 = r3
        L_0x0093:
            if (r0 == 0) goto L_0x009d
            boolean r0 = r1.isChecked()
            if (r0 != 0) goto L_0x009d
            r0 = r2
            goto L_0x009e
        L_0x009d:
            r0 = r3
        L_0x009e:
            int r8 = r8.getAvailabilityStatus()
            if (r8 != 0) goto L_0x00a7
            if (r0 != 0) goto L_0x00a7
            goto L_0x00a8
        L_0x00a7:
            r2 = r3
        L_0x00a8:
            r9.setEnabled(r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.applications.specialaccess.notificationaccess.TypeFilterPreferenceController.updateState(androidx.preference.Preference):void");
    }
}
