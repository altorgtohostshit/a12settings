package com.android.settings.webview;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.webkit.UserPackage;
import androidx.fragment.app.FragmentActivity;
import com.android.settings.R;
import com.android.settings.applications.defaultapps.DefaultAppPickerFragment;
import com.android.settingslib.applications.DefaultAppInfo;
import java.util.ArrayList;
import java.util.List;

public class WebViewAppPicker extends DefaultAppPickerFragment {
    private WebViewUpdateServiceWrapper mWebViewUpdateServiceWrapper;

    public int getMetricsCategory() {
        return 405;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.webview_app_settings;
    }

    private WebViewUpdateServiceWrapper getWebViewUpdateServiceWrapper() {
        if (this.mWebViewUpdateServiceWrapper == null) {
            setWebViewUpdateServiceWrapper(createDefaultWebViewUpdateServiceWrapper());
        }
        return this.mWebViewUpdateServiceWrapper;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (!this.mUserManager.isAdminUser()) {
            getActivity().finish();
        }
    }

    /* access modifiers changed from: protected */
    public List<DefaultAppInfo> getCandidates() {
        ArrayList arrayList = new ArrayList();
        Context context = getContext();
        WebViewUpdateServiceWrapper webViewUpdateServiceWrapper = getWebViewUpdateServiceWrapper();
        for (ApplicationInfo next : webViewUpdateServiceWrapper.getValidWebViewApplicationInfos(context)) {
            arrayList.add(createDefaultAppInfo(context, this.mPm, next, getDisabledReason(webViewUpdateServiceWrapper, context, next.packageName)));
        }
        return arrayList;
    }

    /* access modifiers changed from: protected */
    public String getDefaultKey() {
        PackageInfo currentWebViewPackage = getWebViewUpdateServiceWrapper().getCurrentWebViewPackage();
        if (currentWebViewPackage == null) {
            return null;
        }
        return currentWebViewPackage.packageName;
    }

    /* access modifiers changed from: protected */
    public boolean setDefaultKey(String str) {
        return getWebViewUpdateServiceWrapper().setWebViewProvider(str);
    }

    /* access modifiers changed from: protected */
    public void onSelectionPerformed(boolean z) {
        Intent intent;
        if (z) {
            FragmentActivity activity = getActivity();
            if (activity == null) {
                intent = null;
            } else {
                intent = activity.getIntent();
            }
            if (intent != null && "android.settings.WEBVIEW_SETTINGS".equals(intent.getAction())) {
                getActivity().finish();
                return;
            }
            return;
        }
        getWebViewUpdateServiceWrapper().showInvalidChoiceToast(getActivity());
        updateCandidates();
    }

    private WebViewUpdateServiceWrapper createDefaultWebViewUpdateServiceWrapper() {
        return new WebViewUpdateServiceWrapper();
    }

    /* access modifiers changed from: package-private */
    public void setWebViewUpdateServiceWrapper(WebViewUpdateServiceWrapper webViewUpdateServiceWrapper) {
        this.mWebViewUpdateServiceWrapper = webViewUpdateServiceWrapper;
    }

    private static class WebViewAppInfo extends DefaultAppInfo {
        public WebViewAppInfo(Context context, PackageManager packageManager, int i, PackageItemInfo packageItemInfo, String str, boolean z) {
            super(context, packageManager, i, packageItemInfo, str, z);
        }

        public CharSequence loadLabel() {
            String str;
            try {
                str = this.mPm.getPackageInfo(this.packageItemInfo.packageName, 0).versionName;
            } catch (PackageManager.NameNotFoundException unused) {
                str = "";
            }
            return String.format("%s %s", new Object[]{super.loadLabel(), str});
        }
    }

    /* access modifiers changed from: package-private */
    public DefaultAppInfo createDefaultAppInfo(Context context, PackageManager packageManager, PackageItemInfo packageItemInfo, String str) {
        return new WebViewAppInfo(context, packageManager, this.mUserId, packageItemInfo, str, TextUtils.isEmpty(str));
    }

    /* access modifiers changed from: package-private */
    public String getDisabledReason(WebViewUpdateServiceWrapper webViewUpdateServiceWrapper, Context context, String str) {
        for (UserPackage next : webViewUpdateServiceWrapper.getPackageInfosAllUsers(context, str)) {
            if (!next.isInstalledPackage()) {
                return context.getString(R.string.webview_uninstalled_for_user, new Object[]{next.getUserInfo().name});
            } else if (!next.isEnabledPackage()) {
                return context.getString(R.string.webview_disabled_for_user, new Object[]{next.getUserInfo().name});
            }
        }
        return null;
    }
}
