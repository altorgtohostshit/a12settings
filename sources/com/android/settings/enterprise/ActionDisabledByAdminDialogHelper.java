package com.android.settings.enterprise;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Process;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.IconDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import com.android.settings.R;
import com.android.settings.Settings;
import com.android.settings.Utils;
import com.android.settings.applications.specialaccess.deviceadmin.DeviceAdminAdd;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import java.util.Objects;

public class ActionDisabledByAdminDialogHelper {
    private Activity mActivity;
    private ViewGroup mDialogView;
    RestrictedLockUtils.EnforcedAdmin mEnforcedAdmin;
    private String mRestriction = null;

    public ActionDisabledByAdminDialogHelper(Activity activity) {
        this.mActivity = activity;
    }

    private int getEnforcementAdminUserId(RestrictedLockUtils.EnforcedAdmin enforcedAdmin) {
        UserHandle userHandle = enforcedAdmin.user;
        if (userHandle == null) {
            return -10000;
        }
        return userHandle.getIdentifier();
    }

    private int getEnforcementAdminUserId() {
        return getEnforcementAdminUserId(this.mEnforcedAdmin);
    }

    public AlertDialog.Builder prepareDialogBuilder(String str, RestrictedLockUtils.EnforcedAdmin enforcedAdmin) {
        this.mEnforcedAdmin = enforcedAdmin;
        this.mRestriction = str;
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mActivity);
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(builder.getContext()).inflate(R.layout.admin_support_details_dialog, (ViewGroup) null);
        this.mDialogView = viewGroup;
        initializeDialogViews(viewGroup, this.mEnforcedAdmin.component, getEnforcementAdminUserId(), this.mRestriction);
        builder.setPositiveButton((int) R.string.okay, (DialogInterface.OnClickListener) null).setView((View) this.mDialogView);
        maybeSetLearnMoreButton(builder);
        return builder;
    }

    /* access modifiers changed from: package-private */
    public void maybeSetLearnMoreButton(AlertDialog.Builder builder) {
        UserManager userManager = UserManager.get(this.mActivity.getApplicationContext());
        if (userManager.isSameProfileGroup(getEnforcementAdminUserId(this.mEnforcedAdmin), userManager.getUserHandle())) {
            builder.setNeutralButton(R.string.learn_more, new ActionDisabledByAdminDialogHelper$$ExternalSyntheticLambda0(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$maybeSetLearnMoreButton$0(DialogInterface dialogInterface, int i) {
        showAdminPolicies(this.mEnforcedAdmin, this.mActivity);
        this.mActivity.finish();
    }

    public void updateDialog(String str, RestrictedLockUtils.EnforcedAdmin enforcedAdmin) {
        if (!this.mEnforcedAdmin.equals(enforcedAdmin) || !Objects.equals(this.mRestriction, str)) {
            this.mEnforcedAdmin = enforcedAdmin;
            this.mRestriction = str;
            initializeDialogViews(this.mDialogView, enforcedAdmin.component, getEnforcementAdminUserId(), this.mRestriction);
        }
    }

    private void initializeDialogViews(View view, ComponentName componentName, int i, String str) {
        if (componentName != null) {
            setAdminSupportIcon(view, componentName, i);
            UserHandle userHandle = null;
            if (isNotCurrentUserOrProfile(componentName, i)) {
                componentName = null;
            }
            setAdminSupportTitle(view, str);
            if (i != -10000) {
                userHandle = UserHandle.of(i);
            }
            setAdminSupportDetails(this.mActivity, view, new RestrictedLockUtils.EnforcedAdmin(componentName, userHandle));
        }
    }

    private boolean isNotCurrentUserOrProfile(ComponentName componentName, int i) {
        return !RestrictedLockUtilsInternal.isAdminInCurrentUserOrProfile(this.mActivity, componentName) || !RestrictedLockUtils.isCurrentUserOrProfile(this.mActivity, i);
    }

    /* access modifiers changed from: package-private */
    public void setAdminSupportIcon(View view, ComponentName componentName, int i) {
        ImageView imageView = (ImageView) view.requireViewById(R.id.admin_support_icon);
        if (isNotCurrentUserOrProfile(componentName, i)) {
            imageView.setImageDrawable(this.mActivity.getDrawable(17301684));
            TypedArray obtainStyledAttributes = this.mActivity.obtainStyledAttributes(new int[]{16843829});
            imageView.setImageTintList(ColorStateList.valueOf(obtainStyledAttributes.getColor(0, 0)));
            obtainStyledAttributes.recycle();
            return;
        }
        imageView.setImageDrawable(Utils.getBadgedIcon(IconDrawableFactory.newInstance(this.mActivity), this.mActivity.getPackageManager(), componentName.getPackageName(), i));
    }

    /* access modifiers changed from: package-private */
    public void setAdminSupportTitle(View view, String str) {
        TextView textView = (TextView) view.findViewById(R.id.admin_support_dialog_title);
        if (textView != null) {
            if (isFinancedDevice()) {
                textView.setText(R.string.disabled_by_policy_title_financed_device);
            } else if (str == null) {
                textView.setText(R.string.disabled_by_policy_title);
            } else {
                char c = 65535;
                switch (str.hashCode()) {
                    case -1040305701:
                        if (str.equals("no_sms")) {
                            c = 0;
                            break;
                        }
                        break;
                    case -932215031:
                        if (str.equals("policy_disable_camera")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 620339799:
                        if (str.equals("policy_disable_screen_capture")) {
                            c = 2;
                            break;
                        }
                        break;
                    case 1416425725:
                        if (str.equals("policy_suspend_packages")) {
                            c = 3;
                            break;
                        }
                        break;
                    case 1950494080:
                        if (str.equals("no_outgoing_calls")) {
                            c = 4;
                            break;
                        }
                        break;
                    case 2135693260:
                        if (str.equals("no_adjust_volume")) {
                            c = 5;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        textView.setText(R.string.disabled_by_policy_title_sms);
                        return;
                    case 1:
                        textView.setText(R.string.disabled_by_policy_title_camera);
                        return;
                    case 2:
                        textView.setText(R.string.disabled_by_policy_title_screen_capture);
                        return;
                    case 3:
                        textView.setText(R.string.disabled_by_policy_title_suspend_packages);
                        return;
                    case 4:
                        textView.setText(R.string.disabled_by_policy_title_outgoing_calls);
                        return;
                    case 5:
                        textView.setText(R.string.disabled_by_policy_title_adjust_volume);
                        return;
                    default:
                        textView.setText(R.string.disabled_by_policy_title);
                        return;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void setAdminSupportDetails(Activity activity, View view, RestrictedLockUtils.EnforcedAdmin enforcedAdmin) {
        if (enforcedAdmin != null && enforcedAdmin.component != null) {
            DevicePolicyManager devicePolicyManager = (DevicePolicyManager) activity.getSystemService("device_policy");
            CharSequence charSequence = null;
            if (!RestrictedLockUtilsInternal.isAdminInCurrentUserOrProfile(activity, enforcedAdmin.component) || !RestrictedLockUtils.isCurrentUserOrProfile(activity, getEnforcementAdminUserId(enforcedAdmin))) {
                enforcedAdmin.component = null;
            } else {
                if (enforcedAdmin.user == null) {
                    enforcedAdmin.user = UserHandle.of(UserHandle.myUserId());
                }
                if (UserHandle.isSameApp(Process.myUid(), 1000)) {
                    charSequence = devicePolicyManager.getShortSupportMessageForUser(enforcedAdmin.component, getEnforcementAdminUserId(enforcedAdmin));
                }
            }
            TextView textView = (TextView) view.findViewById(R.id.admin_support_msg);
            if (charSequence != null) {
                textView.setText(charSequence);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void showAdminPolicies(RestrictedLockUtils.EnforcedAdmin enforcedAdmin, Activity activity) {
        Intent intent = new Intent();
        if (enforcedAdmin.component != null) {
            intent.setClass(activity, DeviceAdminAdd.class);
            intent.putExtra("android.app.extra.DEVICE_ADMIN", enforcedAdmin.component);
            intent.putExtra("android.app.extra.CALLED_FROM_SUPPORT_DIALOG", true);
            activity.startActivityAsUser(intent, enforcedAdmin.user);
            return;
        }
        intent.setClass(activity, Settings.DeviceAdminSettingsActivity.class);
        intent.addFlags(268435456);
        activity.startActivity(intent);
    }

    private boolean isFinancedDevice() {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) this.mActivity.getSystemService(DevicePolicyManager.class);
        return devicePolicyManager.isDeviceManaged() && devicePolicyManager.getDeviceOwnerType(devicePolicyManager.getDeviceOwnerComponentOnAnyUser()) == 1;
    }
}
