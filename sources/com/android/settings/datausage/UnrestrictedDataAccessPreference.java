package com.android.settings.datausage;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.UserHandle;
import android.util.AttributeSet;
import android.view.View;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
import com.android.settings.applications.appinfo.AppInfoDashboardFragment;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.datausage.AppStateDataUsageBridge;
import com.android.settings.datausage.DataSaverBackend;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.RestrictedPreferenceHelper;
import com.android.settingslib.applications.ApplicationsState;
import com.android.settingslib.widget.AppSwitchPreference;

public class UnrestrictedDataAccessPreference extends AppSwitchPreference implements DataSaverBackend.Listener {
    /* access modifiers changed from: private */
    public final ApplicationsState mApplicationsState;
    private final DataSaverBackend mDataSaverBackend;
    private final AppStateDataUsageBridge.DataUsageState mDataUsageState;
    /* access modifiers changed from: private */
    public final ApplicationsState.AppEntry mEntry;
    private final RestrictedPreferenceHelper mHelper;
    private final DashboardFragment mParentFragment;

    public void onDataSaverChanged(boolean z) {
    }

    public UnrestrictedDataAccessPreference(Context context, ApplicationsState.AppEntry appEntry, ApplicationsState applicationsState, DataSaverBackend dataSaverBackend, DashboardFragment dashboardFragment) {
        super(context);
        setWidgetLayoutResource(R.layout.restricted_switch_widget);
        this.mHelper = new RestrictedPreferenceHelper(context, this, (AttributeSet) null);
        this.mEntry = appEntry;
        this.mDataUsageState = (AppStateDataUsageBridge.DataUsageState) appEntry.extraInfo;
        appEntry.ensureLabel(context);
        this.mApplicationsState = applicationsState;
        this.mDataSaverBackend = dataSaverBackend;
        this.mParentFragment = dashboardFragment;
        ApplicationInfo applicationInfo = appEntry.info;
        setDisabledByAdmin(RestrictedLockUtilsInternal.checkIfMeteredDataRestricted(context, applicationInfo.packageName, UserHandle.getUserId(applicationInfo.uid)));
        updateState();
        setKey(generateKey(appEntry));
        Drawable drawable = appEntry.icon;
        if (drawable != null) {
            setIcon(drawable);
        }
    }

    static String generateKey(ApplicationsState.AppEntry appEntry) {
        return appEntry.info.packageName + "|" + appEntry.info.uid;
    }

    public void onAttached() {
        super.onAttached();
        this.mDataSaverBackend.addListener(this);
    }

    public void onDetached() {
        this.mDataSaverBackend.remListener(this);
        super.onDetached();
    }

    /* access modifiers changed from: protected */
    public void onClick() {
        if (this.mDataUsageState.isDataSaverDenylisted) {
            AppInfoDashboardFragment.startAppInfoFragment(AppDataUsage.class, R.string.data_usage_app_summary_title, (Bundle) null, this.mParentFragment, this.mEntry);
        } else {
            super.onClick();
        }
    }

    public void performClick() {
        if (!this.mHelper.performClick()) {
            super.performClick();
        }
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        if (this.mEntry.icon == null) {
            preferenceViewHolder.itemView.post(new Runnable() {
                public void run() {
                    UnrestrictedDataAccessPreference.this.mApplicationsState.ensureIcon(UnrestrictedDataAccessPreference.this.mEntry);
                    UnrestrictedDataAccessPreference unrestrictedDataAccessPreference = UnrestrictedDataAccessPreference.this;
                    unrestrictedDataAccessPreference.setIcon(unrestrictedDataAccessPreference.mEntry.icon);
                }
            });
        }
        boolean isDisabledByAdmin = isDisabledByAdmin();
        View findViewById = preferenceViewHolder.findViewById(16908312);
        int i = 0;
        if (isDisabledByAdmin) {
            findViewById.setVisibility(0);
        } else {
            AppStateDataUsageBridge.DataUsageState dataUsageState = this.mDataUsageState;
            findViewById.setVisibility((dataUsageState == null || !dataUsageState.isDataSaverDenylisted) ? 0 : 4);
        }
        super.onBindViewHolder(preferenceViewHolder);
        this.mHelper.onBindViewHolder(preferenceViewHolder);
        preferenceViewHolder.findViewById(R.id.restricted_icon).setVisibility(isDisabledByAdmin ? 0 : 8);
        View findViewById2 = preferenceViewHolder.findViewById(16908352);
        if (isDisabledByAdmin) {
            i = 8;
        }
        findViewById2.setVisibility(i);
    }

    public void onAllowlistStatusChanged(int i, boolean z) {
        AppStateDataUsageBridge.DataUsageState dataUsageState = this.mDataUsageState;
        if (dataUsageState != null && this.mEntry.info.uid == i) {
            dataUsageState.isDataSaverAllowlisted = z;
            updateState();
        }
    }

    public void onDenylistStatusChanged(int i, boolean z) {
        AppStateDataUsageBridge.DataUsageState dataUsageState = this.mDataUsageState;
        if (dataUsageState != null && this.mEntry.info.uid == i) {
            dataUsageState.isDataSaverDenylisted = z;
            updateState();
        }
    }

    public AppStateDataUsageBridge.DataUsageState getDataUsageState() {
        return this.mDataUsageState;
    }

    public ApplicationsState.AppEntry getEntry() {
        return this.mEntry;
    }

    public boolean isDisabledByAdmin() {
        return this.mHelper.isDisabledByAdmin();
    }

    public void setDisabledByAdmin(RestrictedLockUtils.EnforcedAdmin enforcedAdmin) {
        this.mHelper.setDisabledByAdmin(enforcedAdmin);
    }

    public void updateState() {
        setTitle((CharSequence) this.mEntry.label);
        AppStateDataUsageBridge.DataUsageState dataUsageState = this.mDataUsageState;
        if (dataUsageState != null) {
            setChecked(dataUsageState.isDataSaverAllowlisted);
            if (isDisabledByAdmin()) {
                setSummary((int) R.string.disabled_by_admin);
            } else if (this.mDataUsageState.isDataSaverDenylisted) {
                setSummary((int) R.string.restrict_background_blocklisted);
            } else {
                setSummary((CharSequence) "");
            }
        }
        notifyChanged();
    }
}
