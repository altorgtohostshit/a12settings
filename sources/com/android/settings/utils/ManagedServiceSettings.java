package com.android.settings.utils;

import android.app.Dialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.IconDrawableFactory;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;
import com.android.settings.widget.EmptyTextSettings;
import com.android.settingslib.applications.ServiceListing;
import com.android.settingslib.widget.AppSwitchPreference;
import java.util.List;

public abstract class ManagedServiceSettings extends EmptyTextSettings {
    /* access modifiers changed from: private */
    public final Config mConfig = getConfig();
    protected Context mContext;
    private DevicePolicyManager mDpm;
    private IconDrawableFactory mIconDrawableFactory;
    private PackageManager mPm;
    private ServiceListing mServiceListing;

    /* access modifiers changed from: protected */
    public abstract Config getConfig();

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        FragmentActivity activity = getActivity();
        this.mContext = activity;
        this.mPm = activity.getPackageManager();
        this.mDpm = (DevicePolicyManager) this.mContext.getSystemService("device_policy");
        this.mIconDrawableFactory = IconDrawableFactory.newInstance(this.mContext);
        ServiceListing build = new ServiceListing.Builder(this.mContext).setPermission(this.mConfig.permission).setIntentAction(this.mConfig.intentAction).setNoun(this.mConfig.noun).setSetting(this.mConfig.setting).setTag(this.mConfig.tag).build();
        this.mServiceListing = build;
        build.addCallback(new ManagedServiceSettings$$ExternalSyntheticLambda1(this));
        setPreferenceScreen(getPreferenceManager().createPreferenceScreen(this.mContext));
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        setEmptyText(this.mConfig.emptyText);
    }

    public void onResume() {
        super.onResume();
        this.mServiceListing.reload();
        this.mServiceListing.setListening(true);
    }

    public void onPause() {
        super.onPause();
        this.mServiceListing.setListening(false);
    }

    /* access modifiers changed from: private */
    public void updateList(List<ServiceInfo> list) {
        int managedProfileId = Utils.getManagedProfileId((UserManager) this.mContext.getSystemService("user"), UserHandle.myUserId());
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        preferenceScreen.removeAll();
        list.sort(new PackageItemInfo.DisplayNameComparator(this.mPm));
        for (ServiceInfo next : list) {
            ComponentName componentName = new ComponentName(next.packageName, next.name);
            CharSequence charSequence = null;
            try {
                charSequence = this.mPm.getApplicationInfoAsUser(next.packageName, 0, UserHandle.myUserId()).loadLabel(this.mPm);
            } catch (PackageManager.NameNotFoundException e) {
                Log.e("ManagedServiceSettings", "can't find package name", e);
            }
            String charSequence2 = next.loadLabel(this.mPm).toString();
            AppSwitchPreference appSwitchPreference = new AppSwitchPreference(getPrefContext());
            appSwitchPreference.setPersistent(false);
            IconDrawableFactory iconDrawableFactory = this.mIconDrawableFactory;
            ApplicationInfo applicationInfo = next.applicationInfo;
            appSwitchPreference.setIcon(iconDrawableFactory.getBadgedIcon(next, applicationInfo, UserHandle.getUserId(applicationInfo.uid)));
            if (charSequence == null || charSequence.equals(charSequence2)) {
                appSwitchPreference.setTitle((CharSequence) charSequence2);
            } else {
                appSwitchPreference.setTitle(charSequence);
                appSwitchPreference.setSummary((CharSequence) charSequence2);
            }
            appSwitchPreference.setKey(componentName.flattenToString());
            appSwitchPreference.setChecked(isServiceEnabled(componentName));
            if (managedProfileId != -10000 && !this.mDpm.isNotificationListenerServicePermitted(next.packageName, managedProfileId)) {
                appSwitchPreference.setSummary((int) R.string.work_profile_notification_access_blocked_summary);
            }
            appSwitchPreference.setOnPreferenceChangeListener(new ManagedServiceSettings$$ExternalSyntheticLambda0(this, charSequence, componentName));
            appSwitchPreference.setKey(componentName.flattenToString());
            preferenceScreen.addPreference(appSwitchPreference);
        }
        highlightPreferenceIfNeeded();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$updateList$0(CharSequence charSequence, ComponentName componentName, Preference preference, Object obj) {
        boolean booleanValue = ((Boolean) obj).booleanValue();
        if (charSequence != null) {
            return setEnabled(componentName, charSequence.toString(), booleanValue);
        }
        return setEnabled(componentName, (String) null, booleanValue);
    }

    /* access modifiers changed from: protected */
    public boolean isServiceEnabled(ComponentName componentName) {
        return this.mServiceListing.isEnabled(componentName);
    }

    /* access modifiers changed from: protected */
    public boolean setEnabled(ComponentName componentName, String str, boolean z) {
        if (!z) {
            this.mServiceListing.setEnabled(componentName, false);
            return true;
        } else if (this.mServiceListing.isEnabled(componentName)) {
            return true;
        } else {
            new ScaryWarningDialogFragment().setServiceInfo(componentName, str, this).show(getFragmentManager(), "dialog");
            return false;
        }
    }

    /* access modifiers changed from: protected */
    public void enable(ComponentName componentName) {
        this.mServiceListing.setEnabled(componentName, true);
    }

    public static class ScaryWarningDialogFragment extends InstrumentedDialogFragment {
        /* access modifiers changed from: private */
        public static /* synthetic */ void lambda$onCreateDialog$1(DialogInterface dialogInterface, int i) {
        }

        public int getMetricsCategory() {
            return 557;
        }

        public ScaryWarningDialogFragment setServiceInfo(ComponentName componentName, String str, Fragment fragment) {
            Bundle bundle = new Bundle();
            bundle.putString("c", componentName.flattenToString());
            bundle.putString("l", str);
            setArguments(bundle);
            setTargetFragment(fragment, 0);
            return this;
        }

        public Dialog onCreateDialog(Bundle bundle) {
            Bundle arguments = getArguments();
            String string = arguments.getString("l");
            ComponentName unflattenFromString = ComponentName.unflattenFromString(arguments.getString("c"));
            ManagedServiceSettings managedServiceSettings = (ManagedServiceSettings) getTargetFragment();
            return new AlertDialog.Builder(getContext()).setMessage((CharSequence) getResources().getString(managedServiceSettings.mConfig.warningDialogSummary, new Object[]{string})).setTitle((CharSequence) getResources().getString(managedServiceSettings.mConfig.warningDialogTitle, new Object[]{string})).setCancelable(true).setPositiveButton((int) R.string.allow, (DialogInterface.OnClickListener) new C1296xc9287076(managedServiceSettings, unflattenFromString)).setNegativeButton((int) R.string.deny, (DialogInterface.OnClickListener) C1297xc9287077.INSTANCE).create();
        }
    }

    public static class Config {
        public final String configIntentAction;
        public final int emptyText;
        public final String intentAction;
        public final String noun;
        public final String permission;
        public final String setting;
        public final String tag;
        public final int warningDialogSummary;
        public final int warningDialogTitle;

        private Config(String str, String str2, String str3, String str4, String str5, String str6, int i, int i2, int i3) {
            this.tag = str;
            this.setting = str2;
            this.intentAction = str3;
            this.permission = str5;
            this.noun = str6;
            this.warningDialogTitle = i;
            this.warningDialogSummary = i2;
            this.emptyText = i3;
            this.configIntentAction = str4;
        }

        public static class Builder {
            private String mConfigIntentAction;
            private int mEmptyText;
            private String mIntentAction;
            private String mNoun;
            private String mPermission;
            private String mSetting;
            private String mTag;
            private int mWarningDialogSummary;
            private int mWarningDialogTitle;

            public Builder setTag(String str) {
                this.mTag = str;
                return this;
            }

            public Builder setSetting(String str) {
                this.mSetting = str;
                return this;
            }

            public Builder setIntentAction(String str) {
                this.mIntentAction = str;
                return this;
            }

            public Builder setConfigurationIntentAction(String str) {
                this.mConfigIntentAction = str;
                return this;
            }

            public Builder setPermission(String str) {
                this.mPermission = str;
                return this;
            }

            public Builder setNoun(String str) {
                this.mNoun = str;
                return this;
            }

            public Builder setWarningDialogTitle(int i) {
                this.mWarningDialogTitle = i;
                return this;
            }

            public Builder setWarningDialogSummary(int i) {
                this.mWarningDialogSummary = i;
                return this;
            }

            public Builder setEmptyText(int i) {
                this.mEmptyText = i;
                return this;
            }

            public Config build() {
                return new Config(this.mTag, this.mSetting, this.mIntentAction, this.mConfigIntentAction, this.mPermission, this.mNoun, this.mWarningDialogTitle, this.mWarningDialogSummary, this.mEmptyText);
            }
        }
    }
}
