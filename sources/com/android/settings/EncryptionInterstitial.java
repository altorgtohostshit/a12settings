package com.android.settings;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import com.android.settings.core.InstrumentedFragment;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupdesign.GlifLayout;
import java.util.List;

public class EncryptionInterstitial extends SettingsActivity {
    /* access modifiers changed from: private */
    public static final String TAG = "EncryptionInterstitial";

    public Intent getIntent() {
        Intent intent = new Intent(super.getIntent());
        intent.putExtra(":settings:show_fragment", EncryptionInterstitialFragment.class.getName());
        return intent;
    }

    /* access modifiers changed from: protected */
    public void onApplyThemeResource(Resources.Theme theme, int i, boolean z) {
        super.onApplyThemeResource(theme, SetupWizardUtils.getTheme(this, getIntent()), z);
    }

    /* access modifiers changed from: protected */
    public boolean isValidFragment(String str) {
        return EncryptionInterstitialFragment.class.getName().equals(str);
    }

    public static Intent createStartIntent(Context context, int i, boolean z, Intent intent) {
        return new Intent(context, EncryptionInterstitial.class).putExtra("extra_password_quality", i).putExtra(":settings:show_fragment_title_resid", R.string.encryption_interstitial_header).putExtra("extra_require_password", z).putExtra("extra_unlock_method_intent", intent);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        findViewById(R.id.content_parent).setFitsSystemWindows(false);
    }

    public static class EncryptionInterstitialFragment extends InstrumentedFragment {
        private boolean mPasswordRequired;
        private int mRequestedPasswordQuality;
        private Intent mUnlockMethodIntent;

        public int getMetricsCategory() {
            return 48;
        }

        public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            return layoutInflater.inflate(R.layout.encryption_interstitial, viewGroup, false);
        }

        public void onViewCreated(View view, Bundle bundle) {
            super.onViewCreated(view, bundle);
            boolean booleanExtra = getActivity().getIntent().getBooleanExtra("for_fingerprint", false);
            boolean booleanExtra2 = getActivity().getIntent().getBooleanExtra("for_face", false);
            boolean booleanExtra3 = getActivity().getIntent().getBooleanExtra("for_biometrics", false);
            Intent intent = getActivity().getIntent();
            this.mRequestedPasswordQuality = intent.getIntExtra("extra_password_quality", 0);
            this.mUnlockMethodIntent = (Intent) intent.getParcelableExtra("extra_unlock_method_intent");
            int i = this.mRequestedPasswordQuality;
            ((TextView) getActivity().findViewById(R.id.sud_layout_description)).setText(i != 65536 ? (i == 131072 || i == 196608) ? booleanExtra ? R.string.encryption_interstitial_message_pin_for_fingerprint : booleanExtra2 ? R.string.encryption_interstitial_message_pin_for_face : booleanExtra3 ? R.string.encryption_interstitial_message_pin_for_biometrics : R.string.encryption_interstitial_message_pin : booleanExtra ? R.string.encryption_interstitial_message_password_for_fingerprint : booleanExtra2 ? R.string.encryption_interstitial_message_password_for_face : booleanExtra3 ? R.string.encryption_interstitial_message_password_for_biometrics : R.string.encryption_interstitial_message_password : booleanExtra ? R.string.encryption_interstitial_message_pattern_for_fingerprint : booleanExtra2 ? R.string.encryption_interstitial_message_pattern_for_face : booleanExtra3 ? R.string.encryption_interstitial_message_pattern_for_biometrics : R.string.encryption_interstitial_message_pattern);
            setRequirePasswordState(getActivity().getIntent().getBooleanExtra("extra_require_password", true));
            GlifLayout glifLayout = (GlifLayout) view;
            glifLayout.setHeaderText(getActivity().getTitle());
            FooterBarMixin footerBarMixin = (FooterBarMixin) glifLayout.getMixin(FooterBarMixin.class);
            footerBarMixin.setSecondaryButton(new FooterButton.Builder(getContext()).setText(R.string.encryption_interstitial_no).setListener(new C0540x423ab284(this)).setButtonType(7).setTheme(2131952137).build());
            footerBarMixin.setPrimaryButton(new FooterButton.Builder(getContext()).setText(R.string.encryption_interstitial_yes).setListener(new C0539x423ab283(this)).setButtonType(5).setTheme(2131952136).build());
        }

        /* access modifiers changed from: protected */
        public void startLockIntent() {
            Intent intent = this.mUnlockMethodIntent;
            if (intent != null) {
                intent.putExtra("extra_require_password", this.mPasswordRequired);
                startActivityForResult(this.mUnlockMethodIntent, 100);
                return;
            }
            Log.wtf(EncryptionInterstitial.TAG, "no unlock intent to start");
            finish();
        }

        public void onActivityResult(int i, int i2, Intent intent) {
            super.onActivityResult(i, i2, intent);
            if (i == 100 && i2 != 0) {
                getActivity().setResult(i2, intent);
                finish();
            }
        }

        /* access modifiers changed from: private */
        public void onYesButtonClicked(View view) {
            if (!AccessibilityManager.getInstance(getActivity()).isEnabled() || this.mPasswordRequired) {
                setRequirePasswordState(true);
                startLockIntent();
                return;
            }
            setRequirePasswordState(false);
            AccessibilityWarningDialogFragment.newInstance(this.mRequestedPasswordQuality).show(getChildFragmentManager(), "AccessibilityWarningDialog");
        }

        /* access modifiers changed from: private */
        public void onNoButtonClicked(View view) {
            setRequirePasswordState(false);
            startLockIntent();
        }

        /* access modifiers changed from: private */
        public void setRequirePasswordState(boolean z) {
            this.mPasswordRequired = z;
        }

        public void finish() {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                } else {
                    activity.finish();
                }
            }
        }
    }

    public static class AccessibilityWarningDialogFragment extends InstrumentedDialogFragment implements DialogInterface.OnClickListener {
        public int getMetricsCategory() {
            return 581;
        }

        public static AccessibilityWarningDialogFragment newInstance(int i) {
            AccessibilityWarningDialogFragment accessibilityWarningDialogFragment = new AccessibilityWarningDialogFragment();
            Bundle bundle = new Bundle(1);
            bundle.putInt("extra_password_quality", i);
            accessibilityWarningDialogFragment.setArguments(bundle);
            return accessibilityWarningDialogFragment;
        }

        public Dialog onCreateDialog(Bundle bundle) {
            int i;
            int i2;
            Object obj;
            int i3 = getArguments().getInt("extra_password_quality");
            if (i3 == 65536) {
                i = R.string.encrypt_talkback_dialog_require_pattern;
                i2 = R.string.encrypt_talkback_dialog_message_pattern;
            } else if (i3 == 131072 || i3 == 196608) {
                i = R.string.encrypt_talkback_dialog_require_pin;
                i2 = R.string.encrypt_talkback_dialog_message_pin;
            } else {
                i = R.string.encrypt_talkback_dialog_require_password;
                i2 = R.string.encrypt_talkback_dialog_message_password;
            }
            FragmentActivity activity = getActivity();
            List<AccessibilityServiceInfo> enabledAccessibilityServiceList = AccessibilityManager.getInstance(activity).getEnabledAccessibilityServiceList(-1);
            if (enabledAccessibilityServiceList.isEmpty()) {
                obj = "";
            } else {
                obj = enabledAccessibilityServiceList.get(0).getResolveInfo().loadLabel(activity.getPackageManager());
            }
            return new AlertDialog.Builder(activity).setTitle(i).setMessage((CharSequence) getString(i2, obj)).setCancelable(true).setPositiveButton(17039370, (DialogInterface.OnClickListener) this).setNegativeButton(17039360, (DialogInterface.OnClickListener) this).create();
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            EncryptionInterstitialFragment encryptionInterstitialFragment = (EncryptionInterstitialFragment) getParentFragment();
            if (encryptionInterstitialFragment == null) {
                return;
            }
            if (i == -1) {
                encryptionInterstitialFragment.setRequirePasswordState(true);
                encryptionInterstitialFragment.startLockIntent();
            } else if (i == -2) {
                encryptionInterstitialFragment.setRequirePasswordState(false);
            }
        }
    }
}
