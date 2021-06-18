package com.android.settings.development;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.UserManager;
import android.text.TextUtils;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.bugreporthandler.BugReportHandlerUtil;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.development.DeveloperOptionsPreferenceController;

public class BugReportHandlerPreferenceController extends DeveloperOptionsPreferenceController implements PreferenceControllerMixin {
    private final BugReportHandlerUtil mBugReportHandlerUtil = new BugReportHandlerUtil();
    private final UserManager mUserManager;

    public String getPreferenceKey() {
        return "bug_report_handler";
    }

    public BugReportHandlerPreferenceController(Context context) {
        super(context);
        this.mUserManager = (UserManager) context.getSystemService("user");
    }

    public boolean isAvailable() {
        return !this.mUserManager.hasUserRestriction("no_debugging_features") && this.mBugReportHandlerUtil.isBugReportHandlerEnabled(this.mContext);
    }

    public void updateState(Preference preference) {
        CharSequence currentBugReportHandlerAppLabel = getCurrentBugReportHandlerAppLabel();
        if (!TextUtils.isEmpty(currentBugReportHandlerAppLabel)) {
            this.mPreference.setSummary(currentBugReportHandlerAppLabel);
        } else {
            this.mPreference.setSummary((int) R.string.app_list_preference_none);
        }
    }

    /* access modifiers changed from: package-private */
    public CharSequence getCurrentBugReportHandlerAppLabel() {
        String str = (String) this.mBugReportHandlerUtil.getCurrentBugReportHandlerAppAndUser(this.mContext).first;
        if ("com.android.shell".equals(str)) {
            return this.mContext.getString(17039651);
        }
        try {
            return this.mContext.getPackageManager().getApplicationInfo(str, 4194304).loadLabel(this.mContext.getPackageManager());
        } catch (PackageManager.NameNotFoundException unused) {
            return null;
        }
    }
}
