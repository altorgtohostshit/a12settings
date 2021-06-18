package com.android.settings.password;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.UserHandle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.android.internal.widget.LockPatternUtils;
import com.android.settings.R;
import com.android.settings.SetupEncryptionInterstitial;
import com.android.settings.SetupWizardUtils;
import com.android.settings.password.ChooseLockGeneric;
import com.android.settings.utils.SettingsDividerItemDecoration;
import com.google.android.setupdesign.GlifPreferenceLayout;

public class SetupChooseLockGeneric extends ChooseLockGeneric {
    /* access modifiers changed from: protected */
    public boolean isValidFragment(String str) {
        return SetupChooseLockGenericFragment.class.getName().equals(str);
    }

    /* access modifiers changed from: package-private */
    public Class<? extends PreferenceFragmentCompat> getFragmentClass() {
        return SetupChooseLockGenericFragment.class;
    }

    /* access modifiers changed from: protected */
    public void onApplyThemeResource(Resources.Theme theme, int i, boolean z) {
        super.onApplyThemeResource(theme, SetupWizardUtils.getTheme(this, getIntent()), z);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getIntent().hasExtra("requested_min_complexity")) {
            IBinder activityToken = getActivityToken();
            if (!PasswordUtils.isCallingAppPermitted(this, activityToken, "android.permission.REQUEST_PASSWORD_COMPLEXITY")) {
                PasswordUtils.crashCallingApplication(activityToken, "Must have permission android.permission.REQUEST_PASSWORD_COMPLEXITY to use extra android.app.extra.PASSWORD_COMPLEXITY");
                finish();
                return;
            }
        }
        findViewById(R.id.content_parent).setFitsSystemWindows(false);
    }

    public static class SetupChooseLockGenericFragment extends ChooseLockGeneric.ChooseLockGenericFragment {
        /* access modifiers changed from: protected */
        public boolean alwaysHideInsecureScreenLockTypes() {
            return true;
        }

        /* access modifiers changed from: protected */
        public boolean canRunBeforeDeviceProvisioned() {
            return true;
        }

        public void onViewCreated(View view, Bundle bundle) {
            super.onViewCreated(view, bundle);
            GlifPreferenceLayout glifPreferenceLayout = (GlifPreferenceLayout) view;
            glifPreferenceLayout.setDividerItemDecoration(new SettingsDividerItemDecoration(getContext()));
            glifPreferenceLayout.setDividerInset(getContext().getResources().getDimensionPixelSize(R.dimen.sud_items_glif_text_divider_inset));
            glifPreferenceLayout.setIcon(getContext().getDrawable(R.drawable.ic_lock));
            int i = isForBiometric() ? R.string.lock_settings_picker_title : R.string.setup_lock_settings_picker_title;
            if (getActivity() != null) {
                getActivity().setTitle(i);
            }
            glifPreferenceLayout.setHeaderText(i);
            setDivider((Drawable) null);
        }

        /* access modifiers changed from: protected */
        public void addHeaderView() {
            if (isForBiometric()) {
                setHeaderView(R.layout.setup_choose_lock_generic_biometrics_header);
            } else {
                setHeaderView(R.layout.setup_choose_lock_generic_header);
            }
        }

        public void onActivityResult(int i, int i2, Intent intent) {
            if (intent == null) {
                intent = new Intent();
            }
            intent.putExtra(":settings:password_quality", new LockPatternUtils(getActivity()).getKeyguardStoredPasswordQuality(UserHandle.myUserId()));
            super.onActivityResult(i, i2, intent);
        }

        public RecyclerView onCreateRecyclerView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            return ((GlifPreferenceLayout) viewGroup).onCreateRecyclerView(layoutInflater, viewGroup, bundle);
        }

        /* access modifiers changed from: protected */
        public Class<? extends ChooseLockGeneric.InternalActivity> getInternalActivityClass() {
            return InternalActivity.class;
        }

        /* access modifiers changed from: protected */
        public void addPreferences() {
            if (isForBiometric()) {
                super.addPreferences();
            } else {
                addPreferencesFromResource(R.xml.setup_security_settings_picker);
            }
        }

        public boolean onPreferenceTreeClick(Preference preference) {
            if (!"unlock_set_do_later".equals(preference.getKey())) {
                return super.onPreferenceTreeClick(preference);
            }
            SetupSkipDialog.newInstance(getActivity().getIntent().getBooleanExtra(":settings:frp_supported", false), false, false, false, false, false).show(getFragmentManager());
            return true;
        }

        /* access modifiers changed from: protected */
        public Intent getLockPasswordIntent(int i) {
            Intent modifyIntentForSetup = SetupChooseLockPassword.modifyIntentForSetup(getContext(), super.getLockPasswordIntent(i));
            SetupWizardUtils.copySetupExtras(getActivity().getIntent(), modifyIntentForSetup);
            return modifyIntentForSetup;
        }

        /* access modifiers changed from: protected */
        public Intent getLockPatternIntent() {
            Intent modifyIntentForSetup = SetupChooseLockPattern.modifyIntentForSetup(getContext(), super.getLockPatternIntent());
            SetupWizardUtils.copySetupExtras(getActivity().getIntent(), modifyIntentForSetup);
            return modifyIntentForSetup;
        }

        /* access modifiers changed from: protected */
        public Intent getEncryptionInterstitialIntent(Context context, int i, boolean z, Intent intent) {
            Intent createStartIntent = SetupEncryptionInterstitial.createStartIntent(context, i, z, intent);
            SetupWizardUtils.copySetupExtras(getActivity().getIntent(), createStartIntent);
            return createStartIntent;
        }

        /* access modifiers changed from: protected */
        public Intent getBiometricEnrollIntent(Context context) {
            Intent biometricEnrollIntent = super.getBiometricEnrollIntent(context);
            SetupWizardUtils.copySetupExtras(getActivity().getIntent(), biometricEnrollIntent);
            return biometricEnrollIntent;
        }

        private boolean isForBiometric() {
            return this.mForFingerprint || this.mForFace || this.mForBiometrics;
        }
    }

    public static class InternalActivity extends ChooseLockGeneric.InternalActivity {

        public static class InternalSetupChooseLockGenericFragment extends ChooseLockGeneric.ChooseLockGenericFragment {
            /* access modifiers changed from: protected */
            public boolean canRunBeforeDeviceProvisioned() {
                return true;
            }
        }

        /* access modifiers changed from: protected */
        public boolean isValidFragment(String str) {
            return InternalSetupChooseLockGenericFragment.class.getName().equals(str);
        }

        /* access modifiers changed from: package-private */
        public Class<? extends Fragment> getFragmentClass() {
            return InternalSetupChooseLockGenericFragment.class;
        }
    }
}
