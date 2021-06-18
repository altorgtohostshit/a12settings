package com.android.settings.applications.intentpicker;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.verify.domain.DomainVerificationManager;
import android.content.pm.verify.domain.DomainVerificationUserState;
import android.net.Uri;
import android.os.Bundle;
import android.util.ArraySet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import com.android.settings.R;
import com.android.settings.applications.AppInfoBase;
import com.android.settings.applications.ClearDefaultsPreference;
import com.android.settings.utils.AnnotationSpan;
import com.android.settings.widget.EntityHeaderController;
import com.android.settingslib.Utils;
import com.android.settingslib.applications.AppUtils;
import com.android.settingslib.widget.FooterPreference;
import com.android.settingslib.widget.MainSwitchPreference;
import com.android.settingslib.widget.OnMainSwitchChangeListener;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class AppLaunchSettings extends AppInfoBase implements Preference.OnPreferenceChangeListener, OnMainSwitchChangeListener {
    private boolean mActivityCreated;
    private Preference mAddLinkPreference;
    private ClearDefaultsPreference mClearDefaultsPreference;
    Context mContext;
    DomainVerificationManager mDomainVerificationManager;
    private PreferenceCategory mMainPreferenceCategory;
    private MainSwitchPreference mMainSwitchPreference;
    private PreferenceCategory mOtherDefaultsPreferenceCategory;
    private PreferenceCategory mSelectedLinksPreferenceCategory;

    public int getMetricsCategory() {
        return 17;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        this.mActivityCreated = false;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        addPreferencesFromResource(R.xml.installed_app_launch_settings);
        this.mDomainVerificationManager = (DomainVerificationManager) this.mContext.getSystemService(DomainVerificationManager.class);
        initUIComponents();
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        createHeaderPreference();
    }

    /* access modifiers changed from: protected */
    public AlertDialog createDialog(int i, int i2) {
        if (i == 1) {
            return createVerifiedLinksDialog();
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public boolean refreshUi() {
        this.mClearDefaultsPreference.setPackageName(this.mPackageName);
        this.mClearDefaultsPreference.setAppEntry(this.mAppEntry);
        return true;
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        boolean booleanValue = ((Boolean) obj).booleanValue();
        IntentPickerUtils.logd("onPreferenceChange: " + preference.getTitle() + " isChecked: " + booleanValue);
        if (!(preference instanceof LeftSideCheckBoxPreference) || booleanValue) {
            return true;
        }
        ArraySet arraySet = new ArraySet();
        arraySet.add(preference.getTitle().toString());
        removePreference(preference.getKey());
        DomainVerificationUserState domainVerificationUserState = IntentPickerUtils.getDomainVerificationUserState(this.mDomainVerificationManager, this.mPackageName);
        if (domainVerificationUserState == null) {
            return false;
        }
        setDomainVerificationUserSelection(domainVerificationUserState.getIdentifier(), arraySet, false);
        this.mAddLinkPreference.setEnabled(isAddLinksNotEmpty());
        return true;
    }

    public void onSwitchChanged(Switch switchR, boolean z) {
        IntentPickerUtils.logd("onSwitchChanged: isChecked=" + z);
        MainSwitchPreference mainSwitchPreference = this.mMainSwitchPreference;
        if (mainSwitchPreference != null) {
            mainSwitchPreference.setChecked(z);
        }
        PreferenceCategory preferenceCategory = this.mMainPreferenceCategory;
        if (preferenceCategory != null) {
            preferenceCategory.setVisible(z);
        }
        DomainVerificationManager domainVerificationManager = this.mDomainVerificationManager;
        if (domainVerificationManager != null) {
            try {
                domainVerificationManager.setDomainVerificationLinkHandlingAllowed(this.mPackageName, z);
            } catch (PackageManager.NameNotFoundException e) {
                Log.w("AppLaunchSettings", "onSwitchChanged: " + e.getMessage());
            }
        }
    }

    private void createHeaderPreference() {
        if (this.mActivityCreated) {
            Log.w("AppLaunchSettings", "onParentActivityCreated: ignoring duplicate call.");
            return;
        }
        this.mActivityCreated = true;
        if (this.mPackageInfo == null) {
            Log.w("AppLaunchSettings", "onParentActivityCreated: PakcageInfo is null.");
            return;
        }
        FragmentActivity activity = getActivity();
        getPreferenceScreen().addPreference(EntityHeaderController.newInstance(activity, this, (View) null).setRecyclerView(getListView(), getSettingsLifecycle()).setIcon(Utils.getBadgedIcon(this.mContext, this.mPackageInfo.applicationInfo)).setLabel(this.mPackageInfo.applicationInfo.loadLabel(this.mPm)).setSummary((CharSequence) "").setIsInstantApp(AppUtils.isInstant(this.mPackageInfo.applicationInfo)).setPackageName(this.mPackageName).setUid(this.mPackageInfo.applicationInfo.uid).setHasAppInfoLink(true).setButtonActions(0, 0).done((Activity) activity, getPrefContext()));
    }

    private void initUIComponents() {
        initMainSwitchAndCategories();
        if (canUpdateMainSwitchAndCategories()) {
            initVerifiedLinksPreference();
            initAddLinkPreference();
            addSelectedLinksPreference();
            initFooter();
        }
    }

    private void initMainSwitchAndCategories() {
        this.mMainSwitchPreference = (MainSwitchPreference) findPreference("open_by_default_supported_links");
        this.mMainPreferenceCategory = (PreferenceCategory) findPreference("open_by_default_main_category");
        this.mSelectedLinksPreferenceCategory = (PreferenceCategory) findPreference("open_by_default_selected_links_category");
        initOtherDefaultsSection();
    }

    private boolean canUpdateMainSwitchAndCategories() {
        DomainVerificationUserState domainVerificationUserState = IntentPickerUtils.getDomainVerificationUserState(this.mDomainVerificationManager, this.mPackageName);
        if (domainVerificationUserState == null) {
            disabledPreference();
            return false;
        }
        IntentPickerUtils.logd("isLinkHandlingAllowed() : " + domainVerificationUserState.isLinkHandlingAllowed());
        this.mMainSwitchPreference.updateStatus(domainVerificationUserState.isLinkHandlingAllowed());
        this.mMainSwitchPreference.addOnSwitchChangeListener(this);
        this.mMainPreferenceCategory.setVisible(domainVerificationUserState.isLinkHandlingAllowed());
        return true;
    }

    private void initVerifiedLinksPreference() {
        VerifiedLinksPreference verifiedLinksPreference = (VerifiedLinksPreference) this.mMainPreferenceCategory.findPreference("open_by_default_verified_links");
        verifiedLinksPreference.setWidgetFrameClickListener(new AppLaunchSettings$$ExternalSyntheticLambda0(this));
        int linksNumber = getLinksNumber(2);
        verifiedLinksPreference.setTitle((CharSequence) getVerifiedLinksTitle(linksNumber));
        boolean z = true;
        verifiedLinksPreference.setCheckBoxVisible(linksNumber > 0);
        if (linksNumber <= 0) {
            z = false;
        }
        verifiedLinksPreference.setEnabled(z);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initVerifiedLinksPreference$0(View view) {
        showVerifiedLinksDialog();
    }

    private void showVerifiedLinksDialog() {
        if (getLinksNumber(2) != 0) {
            showDialogInner(1, 0);
        }
    }

    private AlertDialog createVerifiedLinksDialog() {
        int linksNumber = getLinksNumber(2);
        View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.app_launch_verified_links_title, (ViewGroup) null);
        ((TextView) inflate.findViewById(R.id.dialog_title)).setText(getVerifiedLinksTitle(linksNumber));
        ((TextView) inflate.findViewById(R.id.dialog_message)).setText(getVerifiedLinksMessage(linksNumber));
        return new AlertDialog.Builder(this.mContext).setCustomTitle(inflate).setCancelable(true).setItems((CharSequence[]) IntentPickerUtils.getLinksList(this.mDomainVerificationManager, this.mPackageName, 2).toArray(new String[0]), (DialogInterface.OnClickListener) null).setPositiveButton((int) R.string.app_launch_dialog_ok, (DialogInterface.OnClickListener) null).create();
    }

    /* access modifiers changed from: package-private */
    public String getVerifiedLinksTitle(int i) {
        return getResources().getQuantityString(R.plurals.app_launch_verified_links_title, i, new Object[]{Integer.valueOf(i)});
    }

    private String getVerifiedLinksMessage(int i) {
        return getResources().getQuantityString(R.plurals.app_launch_verified_links_message, i, new Object[]{Integer.valueOf(i)});
    }

    /* access modifiers changed from: package-private */
    public void addSelectedLinksPreference() {
        if (getLinksNumber(1) != 0) {
            this.mSelectedLinksPreferenceCategory.removeAll();
            for (String generateCheckBoxPreference : IntentPickerUtils.getLinksList(this.mDomainVerificationManager, this.mPackageName, 1)) {
                generateCheckBoxPreference(this.mSelectedLinksPreferenceCategory, generateCheckBoxPreference);
            }
            this.mAddLinkPreference.setEnabled(isAddLinksNotEmpty());
        }
    }

    private void initAddLinkPreference() {
        Preference findPreference = findPreference("open_by_default_add_link");
        this.mAddLinkPreference = findPreference;
        findPreference.setEnabled(isAddLinksNotEmpty());
        this.mAddLinkPreference.setOnPreferenceClickListener(new AppLaunchSettings$$ExternalSyntheticLambda2(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$initAddLinkPreference$1(Preference preference) {
        int linksNumber = getLinksNumber(0);
        IntentPickerUtils.logd("The number of the state none links: " + linksNumber);
        if (linksNumber <= 0) {
            return true;
        }
        showProgressDialogFragment();
        return true;
    }

    private boolean isAddLinksNotEmpty() {
        return getLinksNumber(0) > 0;
    }

    private void showProgressDialogFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("app_package", this.mPackageName);
        ProgressDialogFragment progressDialogFragment = new ProgressDialogFragment();
        progressDialogFragment.setArguments(bundle);
        progressDialogFragment.showDialog(getActivity().getSupportFragmentManager());
    }

    private void disabledPreference() {
        this.mMainSwitchPreference.updateStatus(false);
        this.mMainSwitchPreference.setSelectable(false);
        this.mMainSwitchPreference.setEnabled(false);
        this.mMainPreferenceCategory.setVisible(false);
    }

    private void initOtherDefaultsSection() {
        PreferenceCategory preferenceCategory = (PreferenceCategory) findPreference("app_launch_other_defaults");
        this.mOtherDefaultsPreferenceCategory = preferenceCategory;
        preferenceCategory.setVisible(isClearDefaultsEnabled());
        this.mClearDefaultsPreference = (ClearDefaultsPreference) findPreference("app_launch_clear_defaults");
    }

    private void initFooter() {
        AnnotationSpan.LinkInfo linkInfo = new AnnotationSpan.LinkInfo("url", new AppLaunchSettings$$ExternalSyntheticLambda1(this));
        ((FooterPreference) findPreference("open_by_default_footer")).setTitle(AnnotationSpan.linkify(this.mContext.getText(R.string.app_launch_footer), linkInfo));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initFooter$2(View view) {
        this.mContext.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://developer.android.com/training/app-links/verify-site-associations")));
    }

    private boolean isClearDefaultsEnabled() {
        boolean hasBindAppWidgetPermission = AppWidgetManager.getInstance(this.mContext).hasBindAppWidgetPermission(this.mAppEntry.info.packageName);
        boolean z = AppUtils.hasPreferredActivities(this.mPm, this.mPackageName) || AppUtils.isDefaultBrowser(this.mContext, this.mPackageName) || AppUtils.hasUsbDefaults(this.mUsbManager, this.mPackageName);
        IntentPickerUtils.logd("isClearDefaultsEnabled hasBindAppWidgetPermission : " + hasBindAppWidgetPermission);
        IntentPickerUtils.logd("isClearDefaultsEnabled isAutoLaunchEnabled : " + z);
        if (z || hasBindAppWidgetPermission) {
            return true;
        }
        return false;
    }

    private void setDomainVerificationUserSelection(UUID uuid, Set<String> set, boolean z) {
        try {
            this.mDomainVerificationManager.setDomainVerificationUserSelection(uuid, set, z);
        } catch (PackageManager.NameNotFoundException e) {
            Log.w("AppLaunchSettings", "addSelectedItems : " + e.getMessage());
        }
    }

    private void generateCheckBoxPreference(PreferenceCategory preferenceCategory, String str) {
        LeftSideCheckBoxPreference leftSideCheckBoxPreference = new LeftSideCheckBoxPreference(preferenceCategory.getContext(), true);
        leftSideCheckBoxPreference.setTitle((CharSequence) str);
        leftSideCheckBoxPreference.setOnPreferenceChangeListener(this);
        leftSideCheckBoxPreference.setKey(UUID.randomUUID().toString());
        preferenceCategory.addPreference(leftSideCheckBoxPreference);
    }

    private int getLinksNumber(int i) {
        List<String> linksList = IntentPickerUtils.getLinksList(this.mDomainVerificationManager, this.mPackageName, i);
        if (linksList == null) {
            return 0;
        }
        return linksList.size();
    }
}
