package com.android.settings.bugreporthandler;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.util.Log;
import android.util.Pair;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.applications.defaultapps.DefaultAppPickerFragment;
import com.android.settingslib.applications.DefaultAppInfo;
import com.android.settingslib.development.DevelopmentSettingsEnabler;
import com.android.settingslib.widget.CandidateInfo;
import com.android.settingslib.widget.FooterPreference;
import com.android.settingslib.widget.RadioButtonPreference;
import java.util.ArrayList;
import java.util.List;

public class BugReportHandlerPicker extends DefaultAppPickerFragment {
    private BugReportHandlerUtil mBugReportHandlerUtil;
    private FooterPreference mFooter;

    public int getMetricsCategory() {
        return 1808;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.bug_report_handler_settings;
    }

    private static String getHandlerApp(String str) {
        return str.substring(0, str.lastIndexOf(35));
    }

    private static int getHandlerUser(String str) {
        try {
            return Integer.parseInt(str.substring(str.lastIndexOf(35) + 1));
        } catch (NumberFormatException unused) {
            Log.e("BugReportHandlerPicker", "Failed to get handlerUser");
            return 0;
        }
    }

    static String getKey(String str, int i) {
        return str + "#" + i;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (!DevelopmentSettingsEnabler.isDevelopmentSettingsEnabled(context)) {
            getActivity().finish();
        }
    }

    /* access modifiers changed from: protected */
    public void addStaticPreferences(PreferenceScreen preferenceScreen) {
        if (this.mFooter == null) {
            FooterPreference footerPreference = new FooterPreference(preferenceScreen.getContext());
            this.mFooter = footerPreference;
            footerPreference.setIcon((int) R.drawable.ic_info_outline_24dp);
            this.mFooter.setSingleLineTitle(false);
            this.mFooter.setTitle((int) R.string.bug_report_handler_picker_footer_text);
            this.mFooter.setSelectable(false);
        }
        preferenceScreen.addPreference(this.mFooter);
    }

    /* access modifiers changed from: protected */
    public List<DefaultAppInfo> getCandidates() {
        Context context = getContext();
        List<Pair<ApplicationInfo, Integer>> validBugReportHandlerInfos = getBugReportHandlerUtil().getValidBugReportHandlerInfos(context);
        ArrayList arrayList = new ArrayList();
        for (Pair next : validBugReportHandlerInfos) {
            arrayList.add(createDefaultAppInfo(context, this.mPm, ((Integer) next.second).intValue(), (PackageItemInfo) next.first));
        }
        return arrayList;
    }

    private BugReportHandlerUtil getBugReportHandlerUtil() {
        if (this.mBugReportHandlerUtil == null) {
            setBugReportHandlerUtil(createDefaultBugReportHandlerUtil());
        }
        return this.mBugReportHandlerUtil;
    }

    /* access modifiers changed from: package-private */
    public void setBugReportHandlerUtil(BugReportHandlerUtil bugReportHandlerUtil) {
        this.mBugReportHandlerUtil = bugReportHandlerUtil;
    }

    /* access modifiers changed from: package-private */
    public BugReportHandlerUtil createDefaultBugReportHandlerUtil() {
        return new BugReportHandlerUtil();
    }

    /* access modifiers changed from: protected */
    public String getDefaultKey() {
        Pair<String, Integer> currentBugReportHandlerAppAndUser = getBugReportHandlerUtil().getCurrentBugReportHandlerAppAndUser(getContext());
        return getKey((String) currentBugReportHandlerAppAndUser.first, ((Integer) currentBugReportHandlerAppAndUser.second).intValue());
    }

    /* access modifiers changed from: protected */
    public boolean setDefaultKey(String str) {
        return getBugReportHandlerUtil().setCurrentBugReportHandlerAppAndUser(getContext(), getHandlerApp(str), getHandlerUser(str));
    }

    /* access modifiers changed from: protected */
    public void onSelectionPerformed(boolean z) {
        Intent intent;
        super.onSelectionPerformed(z);
        if (z) {
            FragmentActivity activity = getActivity();
            if (activity == null) {
                intent = null;
            } else {
                intent = activity.getIntent();
            }
            if (intent != null && "android.settings.BUGREPORT_HANDLER_SETTINGS".equals(intent.getAction())) {
                getActivity().finish();
                return;
            }
            return;
        }
        getBugReportHandlerUtil().showInvalidChoiceToast(getContext());
        updateCandidates();
    }

    public void bindPreferenceExtra(RadioButtonPreference radioButtonPreference, String str, CandidateInfo candidateInfo, String str2, String str3) {
        super.bindPreferenceExtra(radioButtonPreference, str, candidateInfo, str2, str3);
        radioButtonPreference.setAppendixVisibility(8);
    }

    /* access modifiers changed from: package-private */
    public DefaultAppInfo createDefaultAppInfo(Context context, PackageManager packageManager, int i, PackageItemInfo packageItemInfo) {
        return new BugreportHandlerAppInfo(context, packageManager, i, packageItemInfo, getDescription(packageItemInfo.packageName, i));
    }

    private String getDescription(String str, int i) {
        Context context = getContext();
        if ("com.android.shell".equals(str)) {
            return context.getString(R.string.system_default_app_subtext);
        }
        if (this.mUserManager.getUserProfiles().size() < 2) {
            return "";
        }
        UserInfo userInfo = this.mUserManager.getUserInfo(i);
        if (userInfo == null || !userInfo.isManagedProfile()) {
            return context.getString(R.string.personal_profile_app_subtext);
        }
        return context.getString(R.string.work_profile_app_subtext);
    }

    private static class BugreportHandlerAppInfo extends DefaultAppInfo {
        private final Context mContext;

        BugreportHandlerAppInfo(Context context, PackageManager packageManager, int i, PackageItemInfo packageItemInfo, String str) {
            super(context, packageManager, i, packageItemInfo, str, true);
            this.mContext = context;
        }

        public String getKey() {
            PackageItemInfo packageItemInfo = this.packageItemInfo;
            if (packageItemInfo != null) {
                return BugReportHandlerPicker.getKey(packageItemInfo.packageName, this.userId);
            }
            return null;
        }

        public CharSequence loadLabel() {
            PackageItemInfo packageItemInfo;
            if (this.mContext == null || (packageItemInfo = this.packageItemInfo) == null) {
                return null;
            }
            if ("com.android.shell".equals(packageItemInfo.packageName)) {
                return this.mContext.getString(17039651);
            }
            return super.loadLabel();
        }
    }
}
