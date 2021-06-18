package com.android.settings.network.telephony;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.UiccSlotInfo;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.R;
import com.android.settings.SidecarFragment;
import com.android.settings.network.EnableMultiSimSidecar;
import com.android.settings.network.SubscriptionUtil;
import com.android.settings.network.SwitchToEuiccSubscriptionSidecar;
import com.android.settings.network.SwitchToRemovableSlotSidecar;
import com.android.settings.network.SwitchToRemovableSlotSidecar$$ExternalSyntheticLambda0;
import com.android.settings.network.UiccSlotUtil;
import com.android.settings.network.telephony.ConfirmDialogFragment;
import com.android.settings.sim.SimActivationNotifier;
import java.util.List;

public class ToggleSubscriptionDialogActivity extends SubscriptionActionDialogActivity implements SidecarFragment.Listener, ConfirmDialogFragment.OnConfirmListener {
    @VisibleForTesting
    public static final String ARG_enable = "enable";
    private boolean mEnable;
    private EnableMultiSimSidecar mEnableMultiSimSidecar;
    private boolean mIsEsimOperation;
    private SubscriptionInfo mSubInfo;
    private SwitchToEuiccSubscriptionSidecar mSwitchToEuiccSubscriptionSidecar;
    private SwitchToRemovableSlotSidecar mSwitchToRemovableSlotSidecar;
    private TelephonyManager mTelMgr;

    public static Intent getIntent(Context context, int i, boolean z) {
        Intent intent = new Intent(context, ToggleSubscriptionDialogActivity.class);
        intent.putExtra("sub_id", i);
        intent.putExtra(ARG_enable, z);
        return intent;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Intent intent = getIntent();
        int intExtra = intent.getIntExtra("sub_id", -1);
        this.mTelMgr = (TelephonyManager) getSystemService(TelephonyManager.class);
        if (!((UserManager) getSystemService(UserManager.class)).isAdminUser()) {
            Log.e("ToggleSubscriptionDialogActivity", "It is not the admin user. Unable to toggle subscription.");
            finish();
        } else if (!SubscriptionManager.isUsableSubscriptionId(intExtra)) {
            Log.e("ToggleSubscriptionDialogActivity", "The subscription id is not usable.");
            finish();
        } else {
            SubscriptionInfo subById = SubscriptionUtil.getSubById(this.mSubscriptionManager, intExtra);
            this.mSubInfo = subById;
            this.mIsEsimOperation = subById != null && subById.isEmbedded();
            this.mSwitchToEuiccSubscriptionSidecar = SwitchToEuiccSubscriptionSidecar.get(getFragmentManager());
            this.mSwitchToRemovableSlotSidecar = SwitchToRemovableSlotSidecar.get(getFragmentManager());
            this.mEnableMultiSimSidecar = EnableMultiSimSidecar.get(getFragmentManager());
            boolean booleanExtra = intent.getBooleanExtra(ARG_enable, true);
            this.mEnable = booleanExtra;
            if (bundle != null) {
                return;
            }
            if (booleanExtra) {
                showEnableSubDialog();
            } else {
                showDisableSimConfirmDialog();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.mSwitchToEuiccSubscriptionSidecar.addListener(this);
        this.mSwitchToRemovableSlotSidecar.addListener(this);
        this.mEnableMultiSimSidecar.addListener(this);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        this.mEnableMultiSimSidecar.removeListener(this);
        this.mSwitchToRemovableSlotSidecar.removeListener(this);
        this.mSwitchToEuiccSubscriptionSidecar.removeListener(this);
        super.onPause();
    }

    public void onStateChange(SidecarFragment sidecarFragment) {
        if (sidecarFragment == this.mSwitchToEuiccSubscriptionSidecar) {
            handleSwitchToEuiccSubscriptionSidecarStateChange();
        } else if (sidecarFragment == this.mSwitchToRemovableSlotSidecar) {
            handleSwitchToRemovableSlotSidecarStateChange();
        } else if (sidecarFragment == this.mEnableMultiSimSidecar) {
            handleEnableMultiSimSidecarStateChange();
        }
    }

    public void onConfirm(int i, boolean z) {
        if (!z && i != 3 && i != 4) {
            finish();
        } else if (i != 1) {
            if (i == 2) {
                Log.i("ToggleSubscriptionDialogActivity", "User confirmed to enable the subscription.");
                if (this.mIsEsimOperation) {
                    showProgressDialog(getString(R.string.sim_action_switch_sub_dialog_progress, new Object[]{SubscriptionUtil.getUniqueSubscriptionDisplayName(this.mSubInfo, (Context) this)}));
                    this.mSwitchToEuiccSubscriptionSidecar.run(this.mSubInfo.getSubscriptionId());
                    return;
                }
                showProgressDialog(getString(R.string.sim_action_enabling_sim_without_carrier_name));
                this.mSwitchToRemovableSlotSidecar.run(-1);
            } else if (i != 3) {
                if (i != 4) {
                    Log.e("ToggleSubscriptionDialogActivity", "Unrecognized confirmation dialog tag: " + i);
                } else if (!z) {
                    Log.i("ToggleSubscriptionDialogActivity", "User cancel the dialog to reboot to enable DSDS.");
                    showEnableSimConfirmDialog();
                } else {
                    Log.i("ToggleSubscriptionDialogActivity", "User confirmed reboot to enable DSDS.");
                    SimActivationNotifier.setShowSimSettingsNotification(this, true);
                    this.mTelMgr.switchMultiSimConfig(2);
                }
            } else if (!z) {
                Log.i("ToggleSubscriptionDialogActivity", "User cancel the dialog to enable DSDS.");
                showEnableSimConfirmDialog();
            } else if (this.mTelMgr.doesSwitchMultiSimConfigTriggerReboot()) {
                Log.i("ToggleSubscriptionDialogActivity", "Device does not support reboot free DSDS.");
                showRebootConfirmDialog();
            } else {
                Log.i("ToggleSubscriptionDialogActivity", "Enabling DSDS without rebooting.");
                showProgressDialog(getString(R.string.sim_action_enabling_sim_without_carrier_name));
                this.mEnableMultiSimSidecar.run(2);
            }
        } else if (this.mIsEsimOperation) {
            Log.i("ToggleSubscriptionDialogActivity", "Disabling the eSIM profile.");
            showProgressDialog(getString(R.string.privileged_action_disable_sub_dialog_progress));
            this.mSwitchToEuiccSubscriptionSidecar.run(-1);
        } else {
            Log.i("ToggleSubscriptionDialogActivity", "Disabling the pSIM profile.");
            handleTogglePsimAction();
        }
    }

    private void handleSwitchToEuiccSubscriptionSidecarStateChange() {
        int state = this.mSwitchToEuiccSubscriptionSidecar.getState();
        String str = ARG_enable;
        if (state == 2) {
            Object[] objArr = new Object[1];
            if (!this.mEnable) {
                str = "disable";
            }
            objArr[0] = str;
            Log.i("ToggleSubscriptionDialogActivity", String.format("Successfully %s the eSIM profile.", objArr));
            this.mSwitchToEuiccSubscriptionSidecar.reset();
            dismissProgressDialog();
            finish();
        } else if (state == 3) {
            Object[] objArr2 = new Object[1];
            if (!this.mEnable) {
                str = "disable";
            }
            objArr2[0] = str;
            Log.i("ToggleSubscriptionDialogActivity", String.format("Failed to %s the eSIM profile.", objArr2));
            this.mSwitchToEuiccSubscriptionSidecar.reset();
            dismissProgressDialog();
            showErrorDialog(getString(R.string.privileged_action_disable_fail_title), getString(R.string.privileged_action_disable_fail_text));
        }
    }

    private void handleSwitchToRemovableSlotSidecarStateChange() {
        int state = this.mSwitchToRemovableSlotSidecar.getState();
        if (state == 2) {
            Log.i("ToggleSubscriptionDialogActivity", "Successfully switched to removable slot.");
            this.mSwitchToRemovableSlotSidecar.reset();
            handleTogglePsimAction();
            dismissProgressDialog();
            finish();
        } else if (state == 3) {
            Log.e("ToggleSubscriptionDialogActivity", "Failed switching to removable slot.");
            this.mSwitchToRemovableSlotSidecar.reset();
            dismissProgressDialog();
            showErrorDialog(getString(R.string.sim_action_enable_sim_fail_title), getString(R.string.sim_action_enable_sim_fail_text));
        }
    }

    private void handleEnableMultiSimSidecarStateChange() {
        int state = this.mEnableMultiSimSidecar.getState();
        if (state == 2) {
            this.mEnableMultiSimSidecar.reset();
            Log.i("ToggleSubscriptionDialogActivity", "Successfully switched to DSDS without reboot.");
            handleEnableSubscriptionAfterEnablingDsds();
        } else if (state == 3) {
            this.mEnableMultiSimSidecar.reset();
            Log.i("ToggleSubscriptionDialogActivity", "Failed to switch to DSDS without rebooting.");
            dismissProgressDialog();
            showErrorDialog(getString(R.string.dsds_activation_failure_title), getString(R.string.dsds_activation_failure_body_msg2));
        }
    }

    private void handleEnableSubscriptionAfterEnablingDsds() {
        if (this.mIsEsimOperation) {
            Log.i("ToggleSubscriptionDialogActivity", "DSDS enabled, start to enable profile: " + this.mSubInfo.getSubscriptionId());
            this.mSwitchToEuiccSubscriptionSidecar.run(this.mSubInfo.getSubscriptionId());
            return;
        }
        Log.i("ToggleSubscriptionDialogActivity", "DSDS enabled, start to enable pSIM profile.");
        handleTogglePsimAction();
        dismissProgressDialog();
        finish();
    }

    private void handleTogglePsimAction() {
        SubscriptionInfo subscriptionInfo;
        if (!this.mSubscriptionManager.canDisablePhysicalSubscription() || (subscriptionInfo = this.mSubInfo) == null) {
            Log.i("ToggleSubscriptionDialogActivity", "The device does not support toggling pSIM. It is enough to just enable the removable slot.");
            return;
        }
        this.mSubscriptionManager.setUiccApplicationsEnabled(subscriptionInfo.getSubscriptionId(), this.mEnable);
        finish();
    }

    private void showEnableSubDialog() {
        Log.i("ToggleSubscriptionDialogActivity", "Handle subscription enabling.");
        if (isDsdsConditionSatisfied()) {
            showEnableDsdsConfirmDialog();
        } else if (this.mIsEsimOperation || !this.mTelMgr.isMultiSimEnabled()) {
            showEnableSimConfirmDialog();
        } else {
            Log.i("ToggleSubscriptionDialogActivity", "Toggle on pSIM, no dialog displayed.");
            handleTogglePsimAction();
            finish();
        }
    }

    private void showEnableDsdsConfirmDialog() {
        ConfirmDialogFragment.show(this, ConfirmDialogFragment.OnConfirmListener.class, 3, getString(R.string.sim_action_enable_dsds_title), getString(R.string.sim_action_enable_dsds_text), getString(R.string.sim_action_continue), getString(R.string.sim_action_no_thanks));
    }

    private void showRebootConfirmDialog() {
        ConfirmDialogFragment.show(this, ConfirmDialogFragment.OnConfirmListener.class, 4, getString(R.string.sim_action_restart_title), getString(R.string.sim_action_enable_dsds_text), getString(R.string.sim_action_reboot), getString(R.string.cancel));
    }

    private void showDisableSimConfirmDialog() {
        String str;
        CharSequence uniqueSubscriptionDisplayName = SubscriptionUtil.getUniqueSubscriptionDisplayName(this.mSubInfo, (Context) this);
        if (this.mSubInfo == null || TextUtils.isEmpty(uniqueSubscriptionDisplayName)) {
            str = getString(R.string.privileged_action_disable_sub_dialog_title_without_carrier);
        } else {
            str = getString(R.string.privileged_action_disable_sub_dialog_title, new Object[]{uniqueSubscriptionDisplayName});
        }
        ConfirmDialogFragment.show(this, ConfirmDialogFragment.OnConfirmListener.class, 1, str, (String) null, getString(R.string.yes), getString(R.string.cancel));
    }

    private void showEnableSimConfirmDialog() {
        List<SubscriptionInfo> activeSubscriptions = SubscriptionUtil.getActiveSubscriptions(this.mSubscriptionManager);
        boolean z = false;
        SubscriptionInfo subscriptionInfo = activeSubscriptions.isEmpty() ? null : activeSubscriptions.get(0);
        if (subscriptionInfo == null) {
            Log.i("ToggleSubscriptionDialogActivity", "No active subscriptions available.");
            showNonSwitchSimConfirmDialog();
            return;
        }
        Log.i("ToggleSubscriptionDialogActivity", "Found active subscription.");
        if (this.mIsEsimOperation && subscriptionInfo.isEmbedded()) {
            z = true;
        }
        if (!this.mTelMgr.isMultiSimEnabled() || z) {
            ConfirmDialogFragment.show(this, ConfirmDialogFragment.OnConfirmListener.class, 2, getSwitchSubscriptionTitle(), getSwitchDialogBodyMsg(subscriptionInfo, z), getSwitchDialogPosBtnText(), getString(17039360));
        } else {
            showNonSwitchSimConfirmDialog();
        }
    }

    private void showNonSwitchSimConfirmDialog() {
        ConfirmDialogFragment.show(this, ConfirmDialogFragment.OnConfirmListener.class, 2, getEnableSubscriptionTitle(), (String) null, getString(R.string.yes), getString(17039360));
    }

    private String getSwitchDialogPosBtnText() {
        if (!this.mIsEsimOperation) {
            return getString(R.string.sim_switch_button);
        }
        return getString(R.string.sim_action_switch_sub_dialog_confirm, new Object[]{SubscriptionUtil.getUniqueSubscriptionDisplayName(this.mSubInfo, (Context) this)});
    }

    private String getEnableSubscriptionTitle() {
        CharSequence uniqueSubscriptionDisplayName = SubscriptionUtil.getUniqueSubscriptionDisplayName(this.mSubInfo, (Context) this);
        if (this.mSubInfo == null || TextUtils.isEmpty(uniqueSubscriptionDisplayName)) {
            return getString(R.string.sim_action_enable_sub_dialog_title_without_carrier_name);
        }
        return getString(R.string.sim_action_enable_sub_dialog_title, new Object[]{uniqueSubscriptionDisplayName});
    }

    private String getSwitchSubscriptionTitle() {
        if (!this.mIsEsimOperation) {
            return getString(R.string.sim_action_switch_psim_dialog_title);
        }
        return getString(R.string.sim_action_switch_sub_dialog_title, new Object[]{SubscriptionUtil.getUniqueSubscriptionDisplayName(this.mSubInfo, (Context) this)});
    }

    private String getSwitchDialogBodyMsg(SubscriptionInfo subscriptionInfo, boolean z) {
        CharSequence uniqueSubscriptionDisplayName = SubscriptionUtil.getUniqueSubscriptionDisplayName(this.mSubInfo, (Context) this);
        CharSequence uniqueSubscriptionDisplayName2 = SubscriptionUtil.getUniqueSubscriptionDisplayName(subscriptionInfo, (Context) this);
        if (z && this.mIsEsimOperation) {
            return getString(R.string.sim_action_switch_sub_dialog_text_downloaded, new Object[]{uniqueSubscriptionDisplayName, uniqueSubscriptionDisplayName2});
        } else if (this.mIsEsimOperation) {
            return getString(R.string.sim_action_switch_sub_dialog_text, new Object[]{uniqueSubscriptionDisplayName, uniqueSubscriptionDisplayName2});
        } else {
            return getString(R.string.sim_action_switch_sub_dialog_text_single_sim, new Object[]{uniqueSubscriptionDisplayName2});
        }
    }

    private boolean isDsdsConditionSatisfied() {
        if (this.mTelMgr.isMultiSimEnabled()) {
            Log.i("ToggleSubscriptionDialogActivity", "DSDS is already enabled. Condition not satisfied.");
            return false;
        } else if (this.mTelMgr.isMultiSimSupported() != 0) {
            Log.i("ToggleSubscriptionDialogActivity", "Hardware does not support DSDS.");
            return false;
        } else {
            boolean anyMatch = UiccSlotUtil.getSlotInfos(this.mTelMgr).stream().anyMatch(ToggleSubscriptionDialogActivity$$ExternalSyntheticLambda0.INSTANCE);
            if (!this.mIsEsimOperation || !anyMatch) {
                boolean anyMatch2 = SubscriptionUtil.getActiveSubscriptions(this.mSubscriptionManager).stream().anyMatch(SwitchToRemovableSlotSidecar$$ExternalSyntheticLambda0.INSTANCE);
                if (this.mIsEsimOperation || !anyMatch2) {
                    Log.i("ToggleSubscriptionDialogActivity", "DSDS condition not satisfied.");
                    return false;
                }
                Log.i("ToggleSubscriptionDialogActivity", "Removable SIM operation and eSIM profile is enabled. DSDS condition satisfied.");
                return true;
            }
            Log.i("ToggleSubscriptionDialogActivity", "eSIM operation and removable SIM is enabled. DSDS condition satisfied.");
            return true;
        }
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$isDsdsConditionSatisfied$0(UiccSlotInfo uiccSlotInfo) {
        return uiccSlotInfo != null && uiccSlotInfo.isRemovable() && uiccSlotInfo.getIsActive() && uiccSlotInfo.getCardStateInfo() == 2;
    }
}
