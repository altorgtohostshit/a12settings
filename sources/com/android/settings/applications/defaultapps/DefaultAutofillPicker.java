package com.android.settings.applications.defaultapps;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.Settings;
import android.service.autofill.AutofillServiceInfo;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import com.android.internal.content.PackageMonitor;
import com.android.settings.R;
import com.android.settings.applications.defaultapps.DefaultAppPickerFragment;
import com.android.settingslib.applications.DefaultAppInfo;
import com.android.settingslib.utils.ThreadUtils;
import com.android.settingslib.widget.CandidateInfo;
import java.util.ArrayList;
import java.util.List;

public class DefaultAutofillPicker extends DefaultAppPickerFragment {
    static final Intent AUTOFILL_PROBE = new Intent("android.service.autofill.AutofillService");
    /* access modifiers changed from: private */
    public DialogInterface.OnClickListener mCancelListener;
    private final PackageMonitor mSettingsPackageMonitor = new PackageMonitor() {
        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onPackageAdded$0() {
            DefaultAutofillPicker.this.update();
        }

        public void onPackageAdded(String str, int i) {
            ThreadUtils.postOnMainThread(new DefaultAutofillPicker$1$$ExternalSyntheticLambda1(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onPackageModified$1() {
            DefaultAutofillPicker.this.update();
        }

        public void onPackageModified(String str) {
            ThreadUtils.postOnMainThread(new DefaultAutofillPicker$1$$ExternalSyntheticLambda2(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onPackageRemoved$2() {
            DefaultAutofillPicker.this.update();
        }

        public void onPackageRemoved(String str, int i) {
            ThreadUtils.postOnMainThread(new DefaultAutofillPicker$1$$ExternalSyntheticLambda0(this));
        }
    };

    public int getMetricsCategory() {
        return 792;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.default_autofill_settings;
    }

    /* access modifiers changed from: protected */
    public boolean shouldShowItemNone() {
        return true;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        FragmentActivity activity = getActivity();
        if (!(activity == null || activity.getIntent().getStringExtra("package_name") == null)) {
            this.mCancelListener = new DefaultAutofillPicker$$ExternalSyntheticLambda0(activity);
            this.mUserId = UserHandle.myUserId();
        }
        this.mSettingsPackageMonitor.register(activity, activity.getMainLooper(), false);
        update();
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$onCreate$0(Activity activity, DialogInterface dialogInterface, int i) {
        activity.setResult(0);
        activity.finish();
    }

    /* access modifiers changed from: protected */
    public DefaultAppPickerFragment.ConfirmationDialogFragment newConfirmationDialogFragment(String str, CharSequence charSequence) {
        AutofillPickerConfirmationDialogFragment autofillPickerConfirmationDialogFragment = new AutofillPickerConfirmationDialogFragment();
        autofillPickerConfirmationDialogFragment.init(this, str, charSequence);
        return autofillPickerConfirmationDialogFragment;
    }

    public static class AutofillPickerConfirmationDialogFragment extends DefaultAppPickerFragment.ConfirmationDialogFragment {
        public void onCreate(Bundle bundle) {
            setCancelListener(((DefaultAutofillPicker) getTargetFragment()).mCancelListener);
            super.onCreate(bundle);
        }
    }

    /* access modifiers changed from: private */
    public void update() {
        updateCandidates();
        addAddServicePreference();
    }

    public void onDestroy() {
        this.mSettingsPackageMonitor.unregister();
        super.onDestroy();
    }

    private Preference newAddServicePreferenceOrNull() {
        String stringForUser = Settings.Secure.getStringForUser(getActivity().getContentResolver(), "autofill_service_search_uri", this.mUserId);
        if (TextUtils.isEmpty(stringForUser)) {
            return null;
        }
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(stringForUser));
        Context prefContext = getPrefContext();
        Preference preference = new Preference(prefContext);
        preference.setOnPreferenceClickListener(new DefaultAutofillPicker$$ExternalSyntheticLambda1(this, prefContext, intent));
        preference.setTitle((int) R.string.print_menu_item_add_service);
        preference.setIcon((int) R.drawable.ic_add_24dp);
        preference.setOrder(2147483646);
        preference.setPersistent(false);
        return preference;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$newAddServicePreferenceOrNull$1(Context context, Intent intent, Preference preference) {
        context.startActivityAsUser(intent, UserHandle.of(this.mUserId));
        return true;
    }

    private void addAddServicePreference() {
        Preference newAddServicePreferenceOrNull = newAddServicePreferenceOrNull();
        if (newAddServicePreferenceOrNull != null) {
            getPreferenceScreen().addPreference(newAddServicePreferenceOrNull);
        }
    }

    /* access modifiers changed from: protected */
    public List<DefaultAppInfo> getCandidates() {
        ArrayList arrayList = new ArrayList();
        List<ResolveInfo> queryIntentServicesAsUser = this.mPm.queryIntentServicesAsUser(AUTOFILL_PROBE, 128, this.mUserId);
        Context context = getContext();
        for (ResolveInfo resolveInfo : queryIntentServicesAsUser) {
            String str = resolveInfo.serviceInfo.permission;
            if ("android.permission.BIND_AUTOFILL_SERVICE".equals(str)) {
                PackageManager packageManager = this.mPm;
                int i = this.mUserId;
                ServiceInfo serviceInfo = resolveInfo.serviceInfo;
                arrayList.add(new DefaultAppInfo(context, packageManager, i, new ComponentName(serviceInfo.packageName, serviceInfo.name)));
            }
            if ("android.permission.BIND_AUTOFILL".equals(str)) {
                Log.w("DefaultAutofillPicker", "AutofillService from '" + resolveInfo.serviceInfo.packageName + "' uses unsupported permission " + "android.permission.BIND_AUTOFILL" + ". It works for now, but might not be supported on future releases");
                PackageManager packageManager2 = this.mPm;
                int i2 = this.mUserId;
                ServiceInfo serviceInfo2 = resolveInfo.serviceInfo;
                arrayList.add(new DefaultAppInfo(context, packageManager2, i2, new ComponentName(serviceInfo2.packageName, serviceInfo2.name)));
            }
        }
        return arrayList;
    }

    public static String getDefaultKey(Context context, int i) {
        ComponentName unflattenFromString;
        String stringForUser = Settings.Secure.getStringForUser(context.getContentResolver(), "autofill_service", i);
        if (stringForUser == null || (unflattenFromString = ComponentName.unflattenFromString(stringForUser)) == null) {
            return null;
        }
        return unflattenFromString.flattenToString();
    }

    /* access modifiers changed from: protected */
    public String getDefaultKey() {
        return getDefaultKey(getContext(), this.mUserId);
    }

    /* access modifiers changed from: protected */
    public CharSequence getConfirmationMessage(CandidateInfo candidateInfo) {
        if (candidateInfo == null) {
            return null;
        }
        CharSequence loadLabel = candidateInfo.loadLabel();
        return Html.fromHtml(getContext().getString(R.string.autofill_confirmation_message, new Object[]{loadLabel}));
    }

    /* access modifiers changed from: protected */
    public boolean setDefaultKey(String str) {
        String stringExtra;
        Settings.Secure.putStringForUser(getContext().getContentResolver(), "autofill_service", str, this.mUserId);
        FragmentActivity activity = getActivity();
        if (activity == null || (stringExtra = activity.getIntent().getStringExtra("package_name")) == null) {
            return true;
        }
        activity.setResult((str == null || !str.startsWith(stringExtra)) ? 0 : -1);
        activity.finish();
        return true;
    }

    static final class AutofillSettingIntentProvider {
        private final Context mContext;
        private final String mSelectedKey;
        private final int mUserId;

        public AutofillSettingIntentProvider(Context context, int i, String str) {
            this.mSelectedKey = str;
            this.mContext = context;
            this.mUserId = i;
        }

        public Intent getIntent() {
            for (ResolveInfo resolveInfo : this.mContext.getPackageManager().queryIntentServicesAsUser(DefaultAutofillPicker.AUTOFILL_PROBE, 128, this.mUserId)) {
                ServiceInfo serviceInfo = resolveInfo.serviceInfo;
                if (TextUtils.equals(this.mSelectedKey, new ComponentName(serviceInfo.packageName, serviceInfo.name).flattenToString())) {
                    try {
                        String settingsActivity = new AutofillServiceInfo(this.mContext, serviceInfo).getSettingsActivity();
                        if (TextUtils.isEmpty(settingsActivity)) {
                            return null;
                        }
                        return new Intent("android.intent.action.MAIN").setComponent(new ComponentName(serviceInfo.packageName, settingsActivity));
                    } catch (SecurityException e) {
                        Log.w("DefaultAutofillPicker", "Error getting info for " + serviceInfo + ": " + e);
                    }
                }
            }
            return null;
        }
    }
}
