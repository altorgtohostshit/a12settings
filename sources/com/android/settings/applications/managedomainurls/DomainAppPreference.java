package com.android.settings.applications.managedomainurls;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.UserHandle;
import android.util.ArraySet;
import android.util.IconDrawableFactory;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settingslib.applications.ApplicationsState;
import com.android.settingslib.widget.AppPreference;

public class DomainAppPreference extends AppPreference {
    private final ApplicationsState.AppEntry mEntry;
    private final IconDrawableFactory mIconDrawableFactory;
    private final PackageManager mPm;

    public DomainAppPreference(Context context, IconDrawableFactory iconDrawableFactory, ApplicationsState.AppEntry appEntry) {
        super(context);
        this.mIconDrawableFactory = iconDrawableFactory;
        this.mPm = context.getPackageManager();
        this.mEntry = appEntry;
        appEntry.ensureLabel(getContext());
        setState();
    }

    public void reuse() {
        setState();
        notifyChanged();
    }

    public ApplicationsState.AppEntry getEntry() {
        return this.mEntry;
    }

    private void setState() {
        setTitle((CharSequence) this.mEntry.label);
        setIcon(this.mIconDrawableFactory.getBadgedIcon(this.mEntry.info));
        setSummary(getDomainsSummary(this.mEntry.info.packageName));
    }

    private CharSequence getDomainsSummary(String str) {
        if (this.mPm.getIntentVerificationStatusAsUser(str, UserHandle.myUserId()) == 3) {
            return getContext().getText(R.string.domain_urls_summary_none);
        }
        ArraySet<String> handledDomains = Utils.getHandledDomains(this.mPm, str);
        if (handledDomains.isEmpty()) {
            return getContext().getText(R.string.domain_urls_summary_none);
        }
        if (handledDomains.size() == 1) {
            return getContext().getString(R.string.domain_urls_summary_one, new Object[]{handledDomains.valueAt(0)});
        }
        return getContext().getString(R.string.domain_urls_summary_some, new Object[]{handledDomains.valueAt(0)});
    }
}
