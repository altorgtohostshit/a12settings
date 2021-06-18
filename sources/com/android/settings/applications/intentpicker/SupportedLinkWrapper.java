package com.android.settings.applications.intentpicker;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.verify.domain.DomainOwner;
import android.text.TextUtils;
import android.util.Log;
import com.android.settings.R;
import java.util.List;
import java.util.SortedSet;
import java.util.stream.Collectors;

public class SupportedLinkWrapper implements Comparable {
    private String mHost;
    private boolean mIsChecked = false;
    private boolean mIsEnabled = true;
    private String mLastOwnerName = "";
    private SortedSet<DomainOwner> mOwnerSet;

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getLastPackageLabel$3(String str) {
        return str != null;
    }

    public SupportedLinkWrapper(Context context, String str, SortedSet<DomainOwner> sortedSet) {
        this.mHost = str;
        this.mOwnerSet = sortedSet;
        init(context);
    }

    private void init(Context context) {
        if (this.mOwnerSet.size() > 0) {
            int i = (this.mOwnerSet.stream().filter(SupportedLinkWrapper$$ExternalSyntheticLambda2.INSTANCE).count() > 0 ? 1 : (this.mOwnerSet.stream().filter(SupportedLinkWrapper$$ExternalSyntheticLambda2.INSTANCE).count() == 0 ? 0 : -1));
            this.mIsEnabled = i == 0;
            if (i > 0) {
                this.mLastOwnerName = getLastPackageLabel(context, false);
            } else {
                this.mLastOwnerName = getLastPackageLabel(context, true);
            }
        }
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$init$0(DomainOwner domainOwner) {
        return !domainOwner.isOverrideable();
    }

    private String getLastPackageLabel(Context context, boolean z) {
        List list = (List) this.mOwnerSet.stream().filter(new SupportedLinkWrapper$$ExternalSyntheticLambda1(z)).map(new SupportedLinkWrapper$$ExternalSyntheticLambda0(this, context)).filter(SupportedLinkWrapper$$ExternalSyntheticLambda3.INSTANCE).collect(Collectors.toList());
        return (String) list.get(list.size() - 1);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getLastPackageLabel$1(boolean z, DomainOwner domainOwner) {
        return domainOwner.isOverrideable() == z;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$getLastPackageLabel$2(Context context, DomainOwner domainOwner) {
        return getLabel(context, domainOwner.getPackageName());
    }

    private String getLabel(Context context, String str) {
        try {
            PackageManager packageManager = context.getPackageManager();
            return packageManager.getApplicationInfo(str, 0).loadLabel(packageManager).toString();
        } catch (PackageManager.NameNotFoundException e) {
            Log.w("SupportedLinkWrapper", "getLabel error : " + e.getMessage());
            return null;
        }
    }

    public boolean isEnabled() {
        return this.mIsEnabled;
    }

    public String getDisplayTitle(Context context) {
        if (TextUtils.isEmpty(this.mLastOwnerName) || context == null) {
            return this.mHost;
        }
        return this.mHost + System.lineSeparator() + context.getString(R.string.app_launch_supported_links_subtext, new Object[]{this.mLastOwnerName});
    }

    public String getHost() {
        return this.mHost;
    }

    public boolean isChecked() {
        return this.mIsChecked;
    }

    public void setChecked(boolean z) {
        this.mIsChecked = z;
    }

    public int compareTo(Object obj) {
        SupportedLinkWrapper supportedLinkWrapper = (SupportedLinkWrapper) obj;
        boolean z = this.mIsEnabled;
        if (z != supportedLinkWrapper.mIsEnabled) {
            return z ? -1 : 1;
        }
        if (TextUtils.isEmpty(this.mLastOwnerName) == TextUtils.isEmpty(supportedLinkWrapper.mLastOwnerName)) {
            return 0;
        }
        if (TextUtils.isEmpty(this.mLastOwnerName)) {
            return -1;
        }
        return 1;
    }
}
