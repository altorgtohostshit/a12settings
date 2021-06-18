package com.android.settings.applications;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.usb.IUsbManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.BulletSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.core.content.res.TypedArrayUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.applications.AppUtils;
import com.android.settingslib.applications.ApplicationsState;

public class ClearDefaultsPreference extends Preference {
    protected static final String TAG = ClearDefaultsPreference.class.getSimpleName();
    private Button mActivitiesButton;
    protected ApplicationsState.AppEntry mAppEntry;
    /* access modifiers changed from: private */
    public AppWidgetManager mAppWidgetManager;
    /* access modifiers changed from: private */
    public final RestrictedLockUtils.EnforcedAdmin mAppsControlDisallowedAdmin;
    /* access modifiers changed from: private */
    public final boolean mAppsControlDisallowedBySystem;
    /* access modifiers changed from: private */
    public String mPackageName;
    /* access modifiers changed from: private */
    public PackageManager mPm;
    /* access modifiers changed from: private */
    public IUsbManager mUsbManager;

    public ClearDefaultsPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        setLayoutResource(R.layout.app_preferred_settings);
        this.mAppWidgetManager = AppWidgetManager.getInstance(context);
        this.mPm = context.getPackageManager();
        this.mUsbManager = IUsbManager.Stub.asInterface(ServiceManager.getService("usb"));
        this.mAppsControlDisallowedBySystem = RestrictedLockUtilsInternal.hasBaseUserRestriction(getContext(), "no_control_apps", UserHandle.myUserId());
        this.mAppsControlDisallowedAdmin = RestrictedLockUtilsInternal.checkIfRestrictionEnforced(getContext(), "no_control_apps", UserHandle.myUserId());
    }

    public ClearDefaultsPreference(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public ClearDefaultsPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, TypedArrayUtils.getAttr(context, R.attr.preferenceStyle, 16842894));
    }

    public ClearDefaultsPreference(Context context) {
        this(context, (AttributeSet) null);
    }

    public void setPackageName(String str) {
        this.mPackageName = str;
    }

    public void setAppEntry(ApplicationsState.AppEntry appEntry) {
        this.mAppEntry = appEntry;
    }

    public void onBindViewHolder(final PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        Button button = (Button) preferenceViewHolder.findViewById(R.id.clear_activities_button);
        this.mActivitiesButton = button;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ClearDefaultsPreference.this.mAppsControlDisallowedAdmin != null && !ClearDefaultsPreference.this.mAppsControlDisallowedBySystem) {
                    RestrictedLockUtils.sendShowAdminSupportDetailsIntent(ClearDefaultsPreference.this.getContext(), ClearDefaultsPreference.this.mAppsControlDisallowedAdmin);
                } else if (ClearDefaultsPreference.this.mUsbManager != null) {
                    int myUserId = UserHandle.myUserId();
                    ClearDefaultsPreference.this.mPm.clearPackagePreferredActivities(ClearDefaultsPreference.this.mPackageName);
                    if (AppUtils.isDefaultBrowser(ClearDefaultsPreference.this.getContext(), ClearDefaultsPreference.this.mPackageName)) {
                        ClearDefaultsPreference.this.mPm.setDefaultBrowserPackageNameAsUser((String) null, myUserId);
                    }
                    try {
                        ClearDefaultsPreference.this.mUsbManager.clearDefaults(ClearDefaultsPreference.this.mPackageName, myUserId);
                    } catch (RemoteException e) {
                        Log.e(ClearDefaultsPreference.TAG, "mUsbManager.clearDefaults", e);
                    }
                    ClearDefaultsPreference.this.mAppWidgetManager.setBindAppWidgetPermission(ClearDefaultsPreference.this.mPackageName, false);
                    ClearDefaultsPreference.this.resetLaunchDefaultsUi((TextView) preferenceViewHolder.findViewById(R.id.auto_launch));
                }
            }
        });
        updateUI(preferenceViewHolder);
    }

    public boolean updateUI(PreferenceViewHolder preferenceViewHolder) {
        boolean hasBindAppWidgetPermission = this.mAppWidgetManager.hasBindAppWidgetPermission(this.mAppEntry.info.packageName);
        TextView textView = (TextView) preferenceViewHolder.findViewById(R.id.auto_launch);
        boolean z = AppUtils.hasPreferredActivities(this.mPm, this.mPackageName) || AppUtils.isDefaultBrowser(getContext(), this.mPackageName) || AppUtils.hasUsbDefaults(this.mUsbManager, this.mPackageName);
        if (z || hasBindAppWidgetPermission) {
            boolean z2 = hasBindAppWidgetPermission && z;
            if (hasBindAppWidgetPermission) {
                textView.setText(R.string.auto_launch_label_generic);
            } else {
                textView.setText(R.string.auto_launch_label);
            }
            Context context = getContext();
            CharSequence charSequence = null;
            int dimensionPixelSize = context.getResources().getDimensionPixelSize(R.dimen.installed_app_details_bullet_offset);
            if (z) {
                CharSequence text = context.getText(R.string.auto_launch_enable_text);
                SpannableString spannableString = new SpannableString(text);
                if (z2) {
                    spannableString.setSpan(new BulletSpan(dimensionPixelSize), 0, text.length(), 0);
                }
                charSequence = TextUtils.concat(new CharSequence[]{spannableString, "\n"});
            }
            if (hasBindAppWidgetPermission) {
                CharSequence text2 = context.getText(R.string.always_allow_bind_appwidgets_text);
                SpannableString spannableString2 = new SpannableString(text2);
                if (z2) {
                    spannableString2.setSpan(new BulletSpan(dimensionPixelSize), 0, text2.length(), 0);
                }
                charSequence = charSequence == null ? TextUtils.concat(new CharSequence[]{spannableString2, "\n"}) : TextUtils.concat(new CharSequence[]{charSequence, "\n", spannableString2, "\n"});
            }
            textView.setText(charSequence);
            this.mActivitiesButton.setEnabled(true);
        } else {
            resetLaunchDefaultsUi(textView);
        }
        return true;
    }

    /* access modifiers changed from: private */
    public void resetLaunchDefaultsUi(TextView textView) {
        textView.setText(R.string.auto_launch_disable_text);
        this.mActivitiesButton.setEnabled(false);
    }
}
