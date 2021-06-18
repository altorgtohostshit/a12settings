package com.android.settings.applications.specialaccess.notificationaccess;

import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.slices.SliceBackgroundWorker;

public class ApprovalPreferenceController extends BasePreferenceController {
    private static final String TAG = "ApprovalPrefController";
    private ComponentName mCn;
    private NotificationManager mNm;
    private PreferenceFragmentCompat mParent;
    private PackageInfo mPkgInfo;
    private PackageManager mPm;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public int getAvailabilityStatus() {
        return 0;
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

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

    public ApprovalPreferenceController(Context context, String str) {
        super(context, str);
    }

    public ApprovalPreferenceController setPkgInfo(PackageInfo packageInfo) {
        this.mPkgInfo = packageInfo;
        return this;
    }

    public ApprovalPreferenceController setCn(ComponentName componentName) {
        this.mCn = componentName;
        return this;
    }

    public ApprovalPreferenceController setParent(PreferenceFragmentCompat preferenceFragmentCompat) {
        this.mParent = preferenceFragmentCompat;
        return this;
    }

    public ApprovalPreferenceController setNm(NotificationManager notificationManager) {
        this.mNm = notificationManager;
        return this;
    }

    public ApprovalPreferenceController setPm(PackageManager packageManager) {
        this.mPm = packageManager;
        return this;
    }

    public void updateState(Preference preference) {
        SwitchPreference switchPreference = (SwitchPreference) preference;
        CharSequence loadLabel = this.mPkgInfo.applicationInfo.loadLabel(this.mPm);
        switchPreference.setChecked(isServiceEnabled(this.mCn));
        switchPreference.setOnPreferenceChangeListener(new ApprovalPreferenceController$$ExternalSyntheticLambda0(this, loadLabel));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$updateState$0(CharSequence charSequence, Preference preference, Object obj) {
        if (!((Boolean) obj).booleanValue()) {
            if (!isServiceEnabled(this.mCn)) {
                return true;
            }
            new FriendlyWarningDialogFragment().setServiceInfo(this.mCn, charSequence, this.mParent).show(this.mParent.getFragmentManager(), "friendlydialog");
            return false;
        } else if (isServiceEnabled(this.mCn)) {
            return true;
        } else {
            new ScaryWarningDialogFragment().setServiceInfo(this.mCn, charSequence, this.mParent).show(this.mParent.getFragmentManager(), "dialog");
            return false;
        }
    }

    public void disable(ComponentName componentName) {
        logSpecialPermissionChange(true, componentName.getPackageName());
        this.mNm.setNotificationListenerAccessGranted(componentName, false);
        AsyncTask.execute(new ApprovalPreferenceController$$ExternalSyntheticLambda1(this, componentName));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$disable$1(ComponentName componentName) {
        if (!this.mNm.isNotificationPolicyAccessGrantedForPackage(componentName.getPackageName())) {
            this.mNm.removeAutomaticZenRules(componentName.getPackageName());
        }
    }

    /* access modifiers changed from: protected */
    public void enable(ComponentName componentName) {
        logSpecialPermissionChange(true, componentName.getPackageName());
        this.mNm.setNotificationListenerAccessGranted(componentName, true);
    }

    /* access modifiers changed from: protected */
    public boolean isServiceEnabled(ComponentName componentName) {
        return this.mNm.isNotificationListenerAccessGranted(componentName);
    }

    /* access modifiers changed from: package-private */
    public void logSpecialPermissionChange(boolean z, String str) {
        FeatureFactory.getFactory(this.mContext).getMetricsFeatureProvider().action(this.mContext, z ? 776 : 777, str);
    }
}
